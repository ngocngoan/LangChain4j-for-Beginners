# Module 01: LangChain4j 시작하기

## 목차

- [비디오 안내](../../../01-introduction)
- [학습 내용](../../../01-introduction)
- [전제 조건](../../../01-introduction)
- [핵심 문제 이해하기](../../../01-introduction)
- [토큰 이해하기](../../../01-introduction)
- [메모리 작동 방식](../../../01-introduction)
- [LangChain4j 활용 방법](../../../01-introduction)
- [Azure OpenAI 인프라 배포](../../../01-introduction)
- [로컬에서 애플리케이션 실행하기](../../../01-introduction)
- [애플리케이션 사용법](../../../01-introduction)
  - [상태 없는 채팅 (왼쪽 패널)](../../../01-introduction)
  - [상태 있는 채팅 (오른쪽 패널)](../../../01-introduction)
- [다음 단계](../../../01-introduction)

## 비디오 안내

이 모듈 시작 방법을 설명하는 라이브 세션을 시청하세요:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## 학습 내용

빠른 시작에서 GitHub 모델을 사용해 프롬프트를 보내고, 도구를 호출하며, RAG 파이프라인을 빌드하고, 가드레일을 테스트했습니다. 그 데모들은 가능한 기능을 보여주었고—이제 Azure OpenAI와 GPT-5.2로 전환하며 실제 생산용 애플리케이션을 구축하기 시작합니다. 이 모듈은 대화형 AI에 초점을 맞추며, 컨텍스트를 기억하고 상태를 유지하는 개념에 대해 다룹니다—빠른 시작 데모에서 암묵적으로 사용되었으나 자세히 설명하지 않은 개념입니다.

이 가이드 전체에서 Azure OpenAI의 GPT-5.2를 사용할 것입니다. GPT-5.2의 고급 추론 기능 덕분에 다양한 패턴의 동작이 더 명확하게 드러납니다. 메모리를 추가하면 차이를 뚜렷하게 확인할 수 있습니다. 이는 각 구성 요소가 애플리케이션에 기여하는 바를 이해하는 데 도움이 됩니다.

두 가지 패턴을 보여주는 하나의 애플리케이션을 구축할 것입니다:

**상태 없는 채팅** - 각 요청이 독립적입니다. 모델은 이전 메시지를 기억하지 않습니다. 이것이 빠른 시작에서 사용한 패턴입니다.

**상태 있는 대화** - 각 요청에 대화 기록이 포함됩니다. 모델이 여러 턴에 걸쳐 컨텍스트를 유지합니다. 생산용 애플리케이션에 필요한 방식입니다.

## 전제 조건

- Azure OpenAI 접근 권한이 있는 Azure 구독
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **참고:** Java, Maven, Azure CLI 및 Azure Developer CLI(azd)는 제공된 devcontainer에 사전 설치되어 있습니다.

> **참고:** 이 모듈은 Azure OpenAI에서 GPT-5.2를 사용합니다. 배포는 `azd up`으로 자동 구성되므로 코드 내 모델 이름을 수정하지 마십시오.

## 핵심 문제 이해하기

언어 모델은 상태를 유지하지 않습니다. 각 API 호출은 독립적입니다. "내 이름은 존이야"라는 메시지를 보내고 나서 "내 이름이 뭐야?"라고 물어도, 모델은 자신이 방금 자신을 소개했음을 알지 못합니다. 모든 요청을 처음 하는 대화인 것처럼 처리합니다.

이것은 단순한 Q&A에는 괜찮지만 실제 애플리케이션에는 무용지물입니다. 고객 서비스 봇은 사용자가 무엇을 말했는지 기억해야 하고, 개인 비서도 컨텍스트가 필요합니다. 다중 턴 대화에는 메모리가 필요합니다.

다음 다이어그램은 두 접근 방식을 대비합니다 — 왼쪽은 이름을 잊는 상태 없는 호출, 오른쪽은 이름을 기억하는 ChatMemory 기반 상태 있는 호출입니다.

<img src="../../../translated_images/ko/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*상태 없는(독립 호출) 대화와 상태 있는(컨텍스트 인식) 대화의 차이*

## 토큰 이해하기

대화로 들어가기 전에, 토큰—언어 모델이 처리하는 텍스트의 기본 단위—을 이해하는 것이 중요합니다:

<img src="../../../translated_images/ko/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*텍스트가 어떻게 토큰으로 나뉘는지 예시—"I love AI!"가 4개의 별도 처리 단위가 됨*

토큰은 AI 모델이 텍스트를 측정하고 처리하는 방법입니다. 단어, 구두점, 심지어 공백도 토큰이 될 수 있습니다. 모델은 한 번에 처리할 수 있는 토큰 수에 제한이 있습니다(GPT-5.2는 최대 400,000 토큰, 입력 토큰 최대 272,000, 출력 토큰 최대 128,000). 토큰을 이해하면 대화 길이와 비용 관리를 쉽게 할 수 있습니다.

## 메모리 작동 방식

챗 메모리는 상태 없음을 해결하기 위해 대화 기록을 유지합니다. 당신의 요청을 모델에 보내기 전에, 프레임워크가 관련 이전 메시지를 앞에 덧붙입니다. "내 이름이 뭐야?"라고 물으면, 시스템은 실제로 전체 대화 기록을 전송해 모델이 이전에 "내 이름은 존이야"라고 말한 것을 볼 수 있게 합니다.

LangChain4j는 이 작업을 자동으로 처리하는 메모리 구현체를 제공합니다. 보관할 메시지 수를 선택하면 프레임워크가 컨텍스트 윈도우를 관리합니다. 아래 다이어그램은 MessageWindowChatMemory가 최근 메시지의 슬라이딩 윈도우를 어떻게 유지하는지 보여줍니다.

<img src="../../../translated_images/ko/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory는 최근 메시지의 슬라이딩 윈도우를 유지하며 오래된 메시지는 자동으로 버림*

## LangChain4j 활용 방법

이 모듈은 빠른 시작을 확장하여 Spring Boot를 통합하고 대화 메모리를 추가합니다. 구성 요소들의 연결 방식은 다음과 같습니다:

**종속성** - 두 개의 LangChain4j 라이브러리를 추가:

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
  
**챗 모델** - Azure OpenAI를 Spring 빈으로 설정 ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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
  
빌더는 `azd up`으로 설정된 환경 변수에서 자격 증명을 읽습니다. `baseUrl`을 Azure 엔드포인트로 설정하면 OpenAI 클라이언트가 Azure OpenAI와 작동합니다.

**대화 메모리** - MessageWindowChatMemory로 채팅 기록 추적 ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```
  
`withMaxMessages(10)`로 메모리를 생성해 마지막 10개 메시지를 보관합니다. 사용자 및 AI 메시지는 각각 `UserMessage.from(text)`와 `AiMessage.from(text)`로 타입 래퍼를 사용해 추가합니다. `memory.messages()`로 기록을 가져와 모델에 보냅니다. 서비스는 대화 ID별로 별도의 메모리 인스턴스를 저장해 여러 사용자가 동시에 채팅할 수 있습니다.

> **🤖 GitHub Copilot Chat으로 시도해 보세요:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)를 열고 다음을 질문하세요:  
> - "MessageWindowChatMemory가 윈도우가 가득 찼을 때 어떤 메시지를 버리는지 어떻게 결정하나요?"  
> - "메모리를 인메모리 대신 데이터베이스를 이용해 저장하도록 커스텀 구현할 수 있나요?"  
> - "과거 대화 기록을 압축하는 요약 기능을 어떻게 추가할 수 있나요?"

상태 없는 채팅 엔드포인트는 메모리를 완전히 건너뛰고 빠른 시작과 같이 `chatModel.chat(prompt)`만 호출합니다. 상태 있는 엔드포인트는 메시지를 메모리에 추가하고 기록을 불러와 요청마다 컨텍스트를 포함합니다. 같은 모델 설정, 다른 패턴입니다.

## Azure OpenAI 인프라 배포

**Bash:**
```bash
cd 01-introduction
azd up  # 구독 및 위치 선택 (eastus2 권장)
```
  
**PowerShell:**
```powershell
cd 01-introduction
azd up  # 구독 및 위치 선택(동부 미국 2 추천)
```
  
> **참고:** 타임아웃 오류(`RequestConflict: Cannot modify resource ... provisioning state is not terminal`)가 발생하면 단순히 `azd up`을 다시 실행하세요. Azure 리소스가 여전히 프로비저닝 중일 수 있으며, 재시도하면 리소스가 정상 상태에 도달할 때까지 배포가 완료됩니다.

이 작업은 다음을 수행합니다:  
1. GPT-5.2 및 text-embedding-3-small 모델이 포함된 Azure OpenAI 리소스 배포  
2. 프로젝트 루트에 자격 증명 `.env` 파일 자동 생성  
3. 필요 환경 변수 모두 설정  

**배포 문제 있나요?** [인프라 README](infra/README.md)를 참조해 서브도메인 이름 충돌, 수동 Azure Portal 배포, 모델 구성 관련 문제 해결법을 확인하세요.

**배포 성공 확인:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY 등을 보여야 합니다.
```
  
**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY 등을 보여야 합니다.
```
  
> **참고:** `azd up` 명령은 `.env` 파일을 자동 생성합니다. 나중에 업데이트가 필요하면 `.env` 파일을 수동으로 수정하거나 다음 명령어로 재생성할 수 있습니다:  
>  
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
  
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```


## 로컬에서 애플리케이션 실행하기

**배포 확인:**  

프로젝트 루트에 Azure 자격 증명이 포함된 `.env` 파일이 있는지 확인하세요. 모듈 디렉터리(`01-introduction/`)에서 다음을 실행합니다:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```
  
**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 보여야 합니다
```
  
**애플리케이션 시작:**  

**옵션 1: Spring Boot Dashboard 사용 (VS Code 사용자에게 권장)**

개발 컨테이너에는 Spring Boot Dashboard 확장 프로그램이 포함되어 있으며, 모든 Spring Boot 애플리케이션을 시각적으로 관리할 수 있습니다. VS Code 왼쪽의 액티비티 바에서(Spring Boot 아이콘을 찾아) 확인할 수 있습니다.

Spring Boot Dashboard에서는:  
- 작업 공간 내 모든 Spring Boot 애플리케이션을 확인 가능  
- 클릭 한 번으로 애플리케이션 시작/중지  
- 실시간 로그 확인  
- 애플리케이션 상태 모니터링  

"introduction" 옆 플레이 버튼을 클릭해 이 모듈을 시작하거나, 모든 모듈을 한 번에 시작하세요.

<img src="../../../translated_images/ko/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code의 Spring Boot Dashboard — 한 곳에서 모든 모듈을 시작, 중지, 모니터링*

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
  
두 스크립트는 루트 `.env` 파일에서 환경 변수를 자동으로 로드하며, JAR 파일이 없으면 빌드합니다.

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

애플리케이션은 두 가지 채팅 구현을 나란히 보여주는 웹 인터페이스를 제공합니다.

<img src="../../../translated_images/ko/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*상태 없는 단순 채팅과 상태 있는 대화형 채팅 옵션을 보여주는 대시보드*

### 상태 없는 채팅 (왼쪽 패널)

먼저 이 패널을 사용해 보세요. "내 이름은 존이야"라고 말하고 바로 "내 이름이 뭐야?"라고 질문하세요. 모델은 기억하지 못할 것입니다. 메시지가 서로 독립적이기 때문에 이전 메시지를 기억하지 않습니다. 이것이 언어 모델 통합의 핵심 문제—대화 컨텍스트가 없음—를 보여줍니다.

<img src="../../../translated_images/ko/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI가 이전 메시지에서 이름을 기억하지 못함*

### 상태 있는 채팅 (오른쪽 패널)

이제 똑같은 순서를 이곳에서 시도해 보세요. "내 이름은 존이야"라고 말하고 "내 이름이 뭐야?"라고 물으면 이번에는 기억합니다. 차이는 MessageWindowChatMemory 덕분이며, 대화 기록을 유지해 요청마다 포함시키기 때문입니다. 이것이 생산 대화형 AI의 작동 원리입니다.

<img src="../../../translated_images/ko/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI가 대화 중 이전에 말한 이름을 기억함*

두 패널 모두 같은 GPT-5.2 모델을 사용합니다. 유일한 차이는 메모리입니다. 이로써 메모리가 애플리케이션에 무엇을 가져다주고 실제 사용 사례에 왜 필수적인지 명확히 알 수 있습니다.

## 다음 단계

**다음 모듈:** [02-prompt-engineering - GPT-5.2를 이용한 프롬프트 엔지니어링](../02-prompt-engineering/README.md)

---

**네비게이션:** [← 이전: Module 00 - 빠른 시작](../00-quick-start/README.md) | [메인으로 돌아가기](../README.md) | [다음: Module 02 - 프롬프트 엔지니어링 →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 노력하고 있으나, 자동 번역에는 오류나 부정확성이 포함될 수 있음을 유의하시기 바랍니다. 원본 문서의 원어가 권위 있는 자료로 간주되어야 합니다. 중요한 정보의 경우 전문 인간 번역을 권장합니다. 본 번역 사용으로 인한 오해나 잘못된 해석에 대해서는 당사가 책임지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->