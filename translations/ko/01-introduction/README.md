# 모듈 01: LangChain4j 시작하기

## 목차

- [학습 내용](../../../01-introduction)
- [필수 조건](../../../01-introduction)
- [핵심 문제 이해하기](../../../01-introduction)
- [토큰 이해하기](../../../01-introduction)
- [메모리 작동 방식](../../../01-introduction)
- [LangChain4j 사용 방법](../../../01-introduction)
- [Azure OpenAI 인프라 배포](../../../01-introduction)
- [애플리케이션 로컬 실행](../../../01-introduction)
- [애플리케이션 사용법](../../../01-introduction)
  - [무상태 채팅 (왼쪽 패널)](../../../01-introduction)
  - [상태 유지 채팅 (오른쪽 패널)](../../../01-introduction)
- [다음 단계](../../../01-introduction)

## 학습 내용

퀵 스타트를 완료했다면, 프롬프트를 보내고 응답을 받는 방법을 보셨을 것입니다. 이것이 기본이지만, 실제 애플리케이션은 더 많은 기능이 필요합니다. 이 모듈에서는 문맥을 기억하고 상태를 유지하는 대화형 AI를 만드는 방법을 알려드립니다 — 단순 데모와 프로덕션 수준 애플리케이션의 차이입니다.

이 가이드 전반에 걸쳐 Azure OpenAI의 GPT-5.2를 사용합니다. 이 모델은 고급 추론 기능 덕분에 다양한 패턴의 동작 차이를 더 명확하게 볼 수 있습니다. 메모리를 추가하면 차이점이 확연히 드러납니다. 이는 각 구성요소가 애플리케이션에 어떤 역할을 하는지 이해하기 쉽게 해줍니다.

다음 두 가지 패턴을 모두 보여주는 애플리케이션을 만듭니다:

**무상태 채팅** – 각 요청이 독립적입니다. 모델은 이전 메시지를 기억하지 않습니다. 퀵 스타트에서 사용한 패턴입니다.

**상태 유지 대화** – 각 요청에 대화 기록이 포함됩니다. 모델은 여러 번의 대화 턴 동안 문맥을 유지합니다. 프로덕션 애플리케이션에 필요한 패턴입니다.

## 필수 조건

- Azure 구독 및 Azure OpenAI 접근 권한
- Java 21, Maven 3.9 이상
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **참고:** 제공된 개발 컨테이너에는 Java, Maven, Azure CLI 및 Azure Developer CLI(azd)가 사전 설치되어 있습니다.

> **참고:** 이 모듈은 Azure OpenAI 상의 GPT-5.2를 사용합니다. 배포는 `azd up` 명령어로 자동 구성되므로 코드에서 모델 이름을 변경하지 마십시오.

## 핵심 문제 이해하기

언어 모델은 무상태입니다. 각 API 호출은 독립적입니다. "내 이름은 John입니다"라고 말하고 나서 "내 이름이 뭐야?"라고 물으면, 모델은 방금 자신을 소개했다는 사실을 모릅니다. 모든 요청을 마치 첫 대화인 것처럼 처리합니다.

단순 Q&A에는 괜찮지만 실제 애플리케이션에는 쓸모가 없습니다. 고객 서비스 봇은 사용자가 말한 내용을 기억해야 합니다. 개인 비서는 문맥이 필요합니다. 다중 턴 대화에는 메모리가 필수입니다.

<img src="../../../translated_images/ko/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="무상태 대화와 상태 유지 대화 비교" width="800"/>

*무상태(독립 호출)와 상태 유지(문맥 인식) 대화의 차이*

## 토큰 이해하기

대화에 뛰어들기 전에 토큰을 이해하는 것이 중요합니다 - 언어 모델이 처리하는 텍스트의 기본 단위입니다:

<img src="../../../translated_images/ko/token-explanation.c39760d8ec650181.webp" alt="토큰 설명" width="800"/>

*텍스트가 토큰으로 분리되는 예 — "I love AI!"는 4개의 처리 단위로 나뉨*

토큰은 AI 모델이 텍스트를 측정하고 처리하는 방식입니다. 단어, 구두점, 심지어 공백도 토큰이 될 수 있습니다. 모델이 한 번에 처리할 수 있는 토큰 수 제한이 있습니다(예: GPT-5.2 는 최대 400,000 토큰, 입력 토큰 272,000개, 출력 토큰 128,000개). 토큰을 이해하면 대화 길이 및 비용 관리에 도움이 됩니다.

## 메모리 작동 방식

채팅 메모리는 무상태 문제를 해결하기 위해 대화 기록을 유지합니다. 모델에 요청을 보내기 전에 프레임워크가 관련된 이전 메시지를 앞에 붙입니다. "내 이름이 뭐야?"라고 물으면, 시스템은 실제로 전체 대화 기록을 보내어 모델이 이전에 "내 이름은 John입니다"라고 말한 것을 볼 수 있습니다.

LangChain4j는 이 작업을 자동으로 처리하는 메모리 구현체를 제공합니다. 유지할 메시지 수를 선택하면 프레임워크가 문맥 창을 관리합니다.

<img src="../../../translated_images/ko/memory-window.bbe67f597eadabb3.webp" alt="메모리 창 개념" width="800"/>

*MessageWindowChatMemory는 최근 메시지의 슬라이딩 윈도우를 유지하며 오래된 메시지를 자동으로 삭제함*

## LangChain4j 사용 방법

이 모듈은 퀵 스타트에 Spring Boot를 통합하고 대화 메모리를 추가하여 확장합니다. 구성 요소가 어떻게 맞물리는지 설명합니다:

**종속성** - 두 개의 LangChain4j 라이브러리를 추가합니다:

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

**채팅 모델** - Azure OpenAI를 Spring 빈으로 구성합니다 ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

빌더는 `azd up` 명령어가 설정한 환경 변수에서 자격 증명을 읽습니다. `baseUrl`에 Azure 엔드포인트를 지정하여 OpenAI 클라이언트를 Azure OpenAI와 연동합니다.

**대화 메모리** - MessageWindowChatMemory로 채팅 기록을 추적합니다 ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)`로 메모리를 생성하여 최근 10개의 메시지를 저장합니다. `UserMessage.from(text)`와 `AiMessage.from(text)` 같은 타이핑된 래퍼로 사용자 및 AI 메시지를 추가합니다. `memory.messages()`로 기록을 조회하고 모델에 전송합니다. 서비스는 대화 ID별로 별도의 메모리 인스턴스를 저장하여 여러 사용자가 동시에 채팅할 수 있습니다.

> **🤖 GitHub Copilot 채팅으로 시도해보기:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)를 열고 다음을 물어보세요:
> - "MessageWindowChatMemory는 윈도우가 가득 찼을 때 어떤 메시지를 삭제하나요?"
> - "인메모리 대신 데이터베이스를 이용해 커스텀 메모리 저장소를 구현할 수 있나요?"
> - "이전 대화 기록을 압축하는 요약 기능을 어떻게 추가할 수 있나요?"

무상태 채팅 엔드포인트는 메모리를 전혀 사용하지 않고 `chatModel.chat(prompt)`만 호출합니다. 상태 유지 엔드포인트는 메시지를 메모리에 추가하고 기록을 조회하여 요청마다 해당 문맥을 포함합니다. 동일한 모델 구성이지만 다른 패턴입니다.

## Azure OpenAI 인프라 배포

**Bash:**
```bash
cd 01-introduction
azd up  # 구독 및 위치 선택(동미국2 권장)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # 구독 및 위치 선택 (eastus2 권장)
```

> **참고:** 타임아웃 오류(`RequestConflict: Cannot modify resource ... provisioning state is not terminal`)가 발생하면 `azd up` 명령을 다시 실행하세요. Azure 리소스가 백그라운드에서 아직 프로비저닝 중일 수 있으며, 리트라이 시 리소스가 완료 상태에 도달하면 배포가 완료됩니다.

이 작업은 다음을 수행합니다:
1. GPT-5.2 및 text-embedding-3-small 모델이 포함된 Azure OpenAI 리소스 배포
2. 프로젝트 루트에 자격 증명이 포함된 `.env` 파일 자동 생성
3. 필요한 모든 환경 변수 설정

**배포 문제 발생 시?** [인프라 README](infra/README.md)를 참조하여 하위 도메인 이름 충돌, 수동 Azure 포털 배포 단계 및 모델 구성 가이드를 확인하세요.

**배포 성공 여부 확인:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY 등을 보여주어야 합니다.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY 등을 표시해야 합니다.
```

> **참고:** `azd up` 명령어는 `.env` 파일을 자동으로 생성합니다. 나중에 업데이트가 필요하면 `.env` 파일을 수동으로 편집하거나 다음 명령어로 다시 생성할 수 있습니다:
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

## 애플리케이션 로컬 실행

**배포 확인:**

Azure 자격 증명이 포함된 `.env` 파일이 루트 디렉토리에 존재하는지 확인하세요:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 보여야 합니다
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT을 표시해야 합니다
```

**애플리케이션 시작:**

**옵션 1: Spring Boot 대시보드 사용 (VS Code 사용자를 위한 권장 방법)**

개발 컨테이너에는 Spring Boot 대시보드 확장 프로그램이 포함되어 있어 모든 Spring Boot 애플리케이션을 시각적으로 관리할 수 있습니다. VS Code의 왼쪽 활동 바에 있는 Spring Boot 아이콘에서 찾을 수 있습니다.

Spring Boot 대시보드에서:
- 워크스페이스의 모든 Spring Boot 애플리케이션 목록 확인
- 클릭 한 번으로 애플리케이션 시작/중지
- 실시간 애플리케이션 로그 보기
- 애플리케이션 상태 모니터링 가능

간단히 "introduction" 옆의 재생 버튼을 클릭해 이 모듈을 시작하거나 모든 모듈을 한꺼번에 시작할 수 있습니다.

<img src="../../../translated_images/ko/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot 대시보드" width="400"/>

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
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

스크립트는 루트 `.env` 파일에서 환경 변수를 자동 로드하며 JAR 파일이 없으면 빌드합니다.

> **참고:** 모든 모듈을 수동으로 빌드한 후 시작하려면:
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

브라우저에서 http://localhost:8080 을 엽니다.

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

## 애플리케이션 사용법

이 애플리케이션은 두 가지 채팅 구현을 나란히 보여주는 웹 인터페이스를 제공합니다.

<img src="../../../translated_images/ko/home-screen.121a03206ab910c0.webp" alt="애플리케이션 홈 화면" width="800"/>

*단순 채팅(무상태)과 대화형 채팅(상태 유지) 옵션을 보여주는 대시보드*

### 무상태 채팅 (왼쪽 패널)

먼저 여기를 시도해 보세요. "내 이름은 John입니다"라고 말한 다음 즉시 "내 이름이 뭐야?"라고 물어보세요. 모델은 기억하지 못합니다. 각 메시지가 독립적이기 때문입니다. 이것은 기본 언어 모델 통합의 핵심 문제 - 대화 문맥 부재를 보여줍니다.

<img src="../../../translated_images/ko/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="무상태 채팅 데모" width="800"/>

*AI가 이전 메시지에서 당신의 이름을 기억하지 못함*

### 상태 유지 채팅 (오른쪽 패널)

이번에는 같은 순서로 시도해 보세요. "내 이름은 John입니다"라고 말한 후 "내 이름이 뭐야?"라고 물으면 이번에는 기억합니다. 차이는 MessageWindowChatMemory입니다 - 대화 기록을 유지하여 각 요청에 포함시키기 때문입니다. 이것이 프로덕션용 대화형 AI가 작동하는 방식입니다.

<img src="../../../translated_images/ko/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="상태 유지 채팅 데모" width="800"/>

*AI가 대화 초반에 말한 이름을 기억함*

두 패널 모두 같은 GPT-5.2 모델을 사용합니다. 차이점은 메모리입니다. 이는 메모리가 애플리케이션에 무엇을 제공하는지, 실사용에 왜 필수적인지 명확히 보여줍니다.

## 다음 단계

**다음 모듈:** [02-prompt-engineering - GPT-5.2로 프롬프트 엔지니어링하기](../02-prompt-engineering/README.md)

---

**탐색:** [← 이전: 모듈 00 - 퀵 스타트](../00-quick-start/README.md) | [메인으로 돌아가기](../README.md) | [다음: 모듈 02 - 프롬프트 엔지니어링 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다했으나, 자동 번역에는 오류나 부정확성이 포함될 수 있음을 알려드립니다. 원본 문서는 해당 언어로 된 원문이 권위 있는 자료로 간주되어야 합니다. 중요한 정보의 경우에는 전문 인력에 의한 번역을 권장합니다. 본 번역 사용으로 인한 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->