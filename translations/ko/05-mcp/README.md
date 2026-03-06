# Module 05: Model Context Protocol (MCP)

## Table of Contents

- [배울 내용](../../../05-mcp)
- [MCP란?](../../../05-mcp)
- [MCP 작동 원리](../../../05-mcp)
- [에이전틱 모듈](../../../05-mcp)
- [예제 실행하기](../../../05-mcp)
  - [사전 준비 사항](../../../05-mcp)
- [빠른 시작](../../../05-mcp)
  - [파일 작업 (Stdio)](../../../05-mcp)
  - [슈퍼바이저 에이전트](../../../05-mcp)
    - [데모 실행하기](../../../05-mcp)
    - [슈퍼바이저 작동 방식](../../../05-mcp)
    - [FileAgent가 런타임에 MCP 도구를 발견하는 방법](../../../05-mcp)
    - [응답 전략](../../../05-mcp)
    - [출력 이해하기](../../../05-mcp)
    - [에이전틱 모듈 기능 설명](../../../05-mcp)
- [핵심 개념](../../../05-mcp)
- [축하합니다!](../../../05-mcp)
  - [다음 단계는?](../../../05-mcp)

## 배울 내용

대화형 AI를 구축하고, 프롬프트를 마스터하며, 문서에 근거한 응답을 생성하고, 도구가 포함된 에이전트를 만들었습니다. 하지만 지금까지 만든 모든 도구는 특정 애플리케이션을 위해 맞춤 제작된 것이었습니다. 만약 누구나 만들고 공유할 수 있는 표준화된 도구 생태계에 AI가 접근할 수 있다면 어떨까요? 이 모듈에서는 Model Context Protocol (MCP)과 LangChain4j의 에이전틱 모듈을 사용하여 이것을 구현하는 방법을 배웁니다. 먼저 간단한 MCP 파일 리더를 보여주고, 이어서 Supervisor Agent 패턴을 이용한 고급 에이전틱 워크플로우에 어떻게 쉽게 통합되는지 살펴봅니다.

## MCP란?

Model Context Protocol (MCP)은 바로 그 점을 제공합니다—AI 애플리케이션이 외부 도구를 발견하고 사용할 수 있는 표준화된 방법입니다. 각 데이터 소스나 서비스마다 맞춤 통합을 작성하는 대신, 일관된 형식으로 기능을 노출하는 MCP 서버에 연결합니다. 그러면 AI 에이전트가 이 도구들을 자동으로 발견하고 사용할 수 있습니다.

아래 다이어그램은 차이를 보여줍니다 — MCP가 없으면 매번 맞춤형 포인트 투 포인트 연결이 필요하지만, MCP가 있으면 하나의 프로토콜로 모든 도구와 앱이 연결됩니다:

<img src="../../../translated_images/ko/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP 이전: 복잡한 포인트 투 포인트 통합. MCP 이후: 하나의 프로토콜, 무한한 가능성.*

MCP는 AI 개발의 근본적인 문제를 해결합니다: 모든 통합이 맞춤형이라는 것. GitHub에 접근하고 싶습니까? 맞춤 코딩. 파일을 읽고 싶습니까? 맞춤 코딩. 데이터베이스를 질의하고 싶습니까? 맞춤 코딩. 그리고 이런 통합들은 다른 AI 애플리케이션과 호환되지 않습니다.

MCP는 이를 표준화합니다. MCP 서버는 명확한 설명과 스키마를 가진 도구를 노출합니다. 어떤 MCP 클라이언트든 연결해 이용 가능한 도구를 발견하고 활용할 수 있습니다. 한 번 구축하면 어디서든 사용할 수 있습니다.

아래 다이어그램은 이 아키텍처를 나타냅니다 — 하나의 MCP 클라이언트(여러분의 AI 애플리케이션)가 여러 MCP 서버에 연결하고, 각각은 표준 프로토콜을 통해 자신들의 도구 세트를 노출합니다:

<img src="../../../translated_images/ko/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol 아키텍처 - 표준화된 도구 발견 및 실행*

## MCP 작동 원리

내부적으로, MCP는 계층화된 아키텍처를 사용합니다. 여러분의 자바 애플리케이션(MCP 클라이언트)은 사용 가능한 도구를 발견하고, 전송 계층(Stdio 또는 HTTP)을 통해 JSON-RPC 요청을 보내며, MCP 서버는 작업을 실행하고 결과를 반환합니다. 다음 다이어그램은 이 프로토콜 각 계층을 상세히 보여줍니다:

<img src="../../../translated_images/ko/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP 내부 작동 방식 — 클라이언트가 도구를 발견하고 JSON-RPC 메시지를 교환하며 전송 계층을 통해 작업을 실행합니다.*

**서버-클라이언트 아키텍처**

MCP는 클라이언트-서버 모델을 사용합니다. 서버는 도구(파일 읽기, 데이터베이스 질의, API 호출 등)를 제공합니다. 클라이언트(여러분의 AI 애플리케이션)는 서버에 연결해 이 도구를 사용합니다.

LangChain4j와 MCP를 사용하려면 아래 Maven 의존성을 추가하세요:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**도구 발견**

클라이언트가 MCP 서버에 연결하면 "어떤 도구가 있나요?"라고 묻습니다. 서버는 설명과 매개변수 스키마가 포함된 사용 가능한 도구 목록을 응답으로 보냅니다. AI 에이전트는 사용자 요청에 따라 어떤 도구를 사용할지 결정할 수 있습니다. 아래 다이어그램은 이 핸드셰이크를 보여줍니다 — 클라이언트가 `tools/list` 요청을 보내고 서버가 도구 설명과 스키마를 포함한 리스트를 반환합니다:

<img src="../../../translated_images/ko/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI는 시작 시 사용 가능한 도구를 발견하여 어떤 기능이 있는지 알고, 사용할 도구를 선택할 수 있습니다.*

**전송 메커니즘**

MCP는 다양한 전송 메커니즘을 지원합니다. 두 가지 옵션이 있는데, 하나는 Stdio(로컬 하위 프로세스 통신용)이고, 또 하나는 스트림 가능 HTTP(원격 서버용)입니다. 이 모듈은 Stdio 전송을 시연합니다:

<img src="../../../translated_images/ko/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP 전송 메커니즘: 원격 서버용 HTTP, 로컬 프로세스용 Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

로컬 프로세스용입니다. 애플리케이션이 하위 프로세스로 서버를 실행하고 표준 입출력을 통해 소통합니다. 파일 시스템 접근이나 커맨드라인 도구 사용에 적합합니다.

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

`@modelcontextprotocol/server-filesystem` 서버는 아래 도구들을 노출하며, 지정한 디렉터리에 대해 샌드박스가 적용됩니다:

| 도구 | 설명 |
|------|-----|
| `read_file` | 단일 파일의 내용을 읽기 |
| `read_multiple_files` | 한 번에 여러 파일 읽기 |
| `write_file` | 파일 생성 또는 덮어쓰기 |
| `edit_file` | 특정 부분 찾아 바꾸기 편집 |
| `list_directory` | 경로 내 파일 및 디렉터리 목록 조회 |
| `search_files` | 패턴에 맞는 파일을 재귀적으로 검색 |
| `get_file_info` | 파일 메타데이터(크기, 타임스탬프, 권한) 조회 |
| `create_directory` | 디렉터리 생성(상위 디렉터리 포함) |
| `move_file` | 파일 또는 디렉터리 이동/이름 변경 |

아래 다이어그램은 런타임에 Stdio 전송이 어떻게 동작하는지 보여줍니다 — 자바 애플리케이션이 MCP 서버를 자식 프로세스로 실행하고, 네트워크나 HTTP를 사용하지 않고 stdin/stdout 파이프로 통신합니다:

<img src="../../../translated_images/ko/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio 전송 동작 모습 — 애플리케이션이 MCP 서버를 자식 프로세스로 실행하고 stdin/stdout 파이프로 통신합니다.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅으로 시도해보세요:** [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) 파일을 열고 질문해보세요:  
> - "Stdio 전송은 어떻게 작동하며 HTTP와 언제 사용을 구분해야 하나요?"  
> - "LangChain4j는 생성된 MCP 서버 프로세스의 수명 주기를 어떻게 관리하나요?"  
> - "AI에게 파일 시스템 접근 권한을 주는 것의 보안적 영향은 무엇인가요?"

## 에이전틱 모듈

MCP가 표준화된 도구를 제공하는 반면, LangChain4j의 **에이전틱 모듈**은 이러한 도구들을 오케스트레이션하는 에이전트를 선언적으로 구축할 수 있게 해줍니다. `@Agent` 애노테이션과 `AgenticServices`를 사용하면 명령형 코드 대신 인터페이스를 통해 에이전트 동작을 정의할 수 있습니다.

이 모듈에서는 **슈퍼바이저 에이전트** 패턴을 탐구합니다 — 사용자의 요청에 따라 하위 에이전트를 동적으로 호출할 에이전틱 AI의 고급 접근 방식입니다. MCP 기반 파일 접근 기능을 가진 하위 에이전트 중 하나를 지정하여 이 두 개념을 결합해 봅니다.

에이전틱 모듈을 사용하려면 아래 Maven 의존성을 추가하세요:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **참고:** `langchain4j-agentic` 모듈은 별도의 버전 속성(`langchain4j.mcp.version`)을 사용합니다. 이는 core LangChain4j 라이브러리와는 다른 일정으로 배포되기 때문입니다.

> **⚠️ 실험적:** `langchain4j-agentic` 모듈은 **실험적**이며 변경될 수 있습니다. 안정적인 AI 어시스턴트 구축 방법은 여전히 `langchain4j-core`와 사용자 정의 도구(모듈 04)입니다.

## 예제 실행하기

### 사전 준비 사항

- [모듈 04 - 도구](../04-tools/README.md) 완료 (이 모듈은 맞춤형 도구 개념 위에서 MCP 도구와 비교합니다)
- 최상위 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일 (모듈 01에서 `azd up`으로 생성)
- Java 21 이상, Maven 3.9 이상
- Node.js 16 이상 및 npm (MCP 서버용)

> **참고:** 환경 변수를 아직 설정하지 않았다면 [모듈 01 - 소개](../01-introduction/README.md)에서 배포 안내를 참고하세요 (`azd up`이 `.env` 파일을 자동 생성합니다). 또는 `.env.example`을 복사해 `.env`로 만들고 값을 채우세요.

## 빠른 시작

**VS Code 사용 시:** 탐색기에서 데모 파일 우클릭 후 **"Run Java"** 선택하거나, 실행 및 디버그 패널의 실행 구성을 사용하세요 (`.env` 파일에 Azure 자격 증명이 먼저 설정되어 있어야 합니다).

**Maven 사용 시:** 아래 예제대로 명령줄에서 실행할 수 있습니다.

### 파일 작업 (Stdio)

로컬 하위 프로세스 기반 도구를 시연합니다.

**✅ 사전 준비 불필요** — MCP 서버가 자동으로 실행됩니다.

**시작 스크립트 사용(권장):**

시작 스크립트는 최상위 `.env` 파일에서 환경 변수를 자동으로 로드합니다:

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**VS Code 사용:** `StdioTransportDemo.java`를 우클릭하여 **"Run Java"** 선택 (`.env` 파일이 설정된 상태여야 함).

애플리케이션이 파일 시스템 MCP 서버를 자동 실행하고 로컬 파일을 읽습니다. 하위 프로세스 관리는 자동 처리됩니다.

**예상 출력:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### 슈퍼바이저 에이전트

**슈퍼바이저 에이전트 패턴**은 **유연한** 에이전틱 AI 형태입니다. 슈퍼바이저는 LLM을 이용해 사용자의 요청에 따라 호출할 에이전트를 자율적으로 결정합니다. 다음 예제에서 우리는 MCP 기반 파일 접근과 LLM 에이전트를 결합해, 파일 읽기 → 보고서 생성 워크플로우를 자동으로 관리합니다.

데모에서 `FileAgent`는 MCP 파일 시스템 도구를 사용해 파일을 읽고, `ReportAgent`는 임원 요약(1문장), 3가지 핵심 포인트, 권고 사항을 포함한 구조화된 보고서를 생성합니다. 슈퍼바이저가 이 흐름을 자동으로 조율합니다:

<img src="../../../translated_images/ko/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*슈퍼바이저가 LLM으로 호출할 에이전트와 순서를 결정 — 하드코딩된 라우팅이 필요 없습니다.*

파일 → 보고서 파이프라인의 구체적 워크플로우 모습:

<img src="../../../translated_images/ko/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent가 MCP 도구로 파일을 읽고, ReportAgent가 원시 내용을 구조화된 보고서로 변환합니다.*

다음 시퀀스 다이어그램은 슈퍼바이저 전체 오케스트레이션을 추적합니다 — MCP 서버 생성부터 슈퍼바이저의 자율 에이전트 선택, stdio를 통한 도구 호출과 최종 보고서 생성까지:

<img src="../../../translated_images/ko/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*슈퍼바이저가 FileAgent를 호출해 MCP 서버에 stdio로 요청해 파일을 읽고, 이어서 ReportAgent를 호출해 구조화된 보고서를 생성 — 각 에이전트는 출력을 공유된 Agentic Scope에 저장합니다.*

각 에이전트는 출력을 **Agentic Scope**(공유 메모리)에 저장하여 후속 에이전트가 이전 결과에 접근할 수 있게 합니다. 이는 MCP 도구가 에이전틱 워크플로우에 원활히 통합되는 방식을 보여줍니다 — 슈퍼바이저는 *파일을 어떻게 읽는지* 알 필요 없이 `FileAgent`가 할 수 있다는 것만 알면 됩니다.

#### 데모 실행하기

시작 스크립트는 최상위 `.env` 파일의 환경 변수들을 자동으로 로드합니다:

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**VS Code 사용:** `SupervisorAgentDemo.java`를 우클릭 후 **"Run Java"** 선택 (`.env` 파일이 설정된 상태여야 함).

#### 슈퍼바이저 작동 방식

에이전트를 만들기 전에 MCP 전송을 클라이언트에 연결하고 이를 `ToolProvider`로 래핑해야 합니다. 이렇게 MCP 서버의 도구들이 에이전트 사용 가능 상태가 됩니다:

```java
// 전송에서 MCP 클라이언트를 생성합니다
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// 클라이언트를 ToolProvider로 래핑합니다 — 이것은 MCP 도구를 LangChain4j로 연결합니다
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

이제 MCP 도구가 필요한 에이전트에 `mcpToolProvider`를 주입할 수 있습니다:

```java
// 1단계: FileAgent가 MCP 도구를 사용하여 파일을 읽습니다
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // 파일 작업을 위한 MCP 도구를 가지고 있습니다
        .build();

// 2단계: ReportAgent가 구조화된 보고서를 생성합니다
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor가 파일 → 보고서 작업 흐름을 조율합니다
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // 최종 보고서를 반환합니다
        .build();

// 요청에 따라 어떤 에이전트를 호출할지 Supervisor가 결정합니다
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### FileAgent가 런타임에 MCP 도구를 발견하는 방법

여러분은 궁금할 수 있습니다: **`FileAgent`가 npm 파일 시스템 도구를 어떻게 아는가?** 정답은 모릅니다 — **LLM**이 도구 스키마를 통해 런타임에 판단합니다.

`FileAgent` 인터페이스는 단지 **프롬프트 정의**입니다. `read_file`, `list_directory` 같은 MCP 도구에 대한 하드코딩된 지식이 전혀 없습니다. 끝에서 끝까지 일어나는 일은 다음과 같습니다:
1. **서버 생성:** `StdioMcpTransport`가 `@modelcontextprotocol/server-filesystem` npm 패키지를 자식 프로세스로 실행합니다  
2. **도구 발견:** `McpClient`가 서버에 `tools/list` JSON-RPC 요청을 보내면, 서버는 도구 이름, 설명 및 파라미터 스키마(예: `read_file` — *"파일의 전체 내용을 읽기"* — `{ path: string }`)를 응답합니다  
3. **스키마 주입:** `McpToolProvider`가 발견된 이 스키마들을 감싸서 LangChain4j에 제공합니다  
4. **LLM 결정:** `FileAgent.readFile(path)`가 호출되면, LangChain4j가 시스템 메시지, 사용자 메시지, **그리고 도구 스키마 리스트**를 LLM에 보냅니다. LLM은 도구 설명을 읽고 도구 호출을 생성합니다(예: `read_file(path="/some/file.txt")`)  
5. **실행:** LangChain4j가 도구 호출을 가로채 MCP 클라이언트를 통해 Node.js 서브프로세스로 라우팅하고 결과를 받아 LLM에 다시 전달합니다  

이는 위에서 설명한 동일한 [도구 발견](../../../05-mcp) 메커니즘이지만, 에이전트 워크플로우에 구체적으로 적용된 것입니다. `@SystemMessage`와 `@UserMessage` 애노테이션은 LLM의 동작을 안내하고, 주입된 `ToolProvider`가 **기능**을 제공하며 — LLM이 런타임에 두 가지를 연결합니다.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat으로 시도해보세요:** [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java)를 열고 다음을 물어보세요:  
> - "이 에이전트가 어떤 MCP 도구를 호출하는지 어떻게 알까요?"  
> - "만약 에이전트 빌더에서 ToolProvider를 제거하면 어떻게 되나요?"  
> - "도구 스키마가 LLM에 어떻게 전달되나요?"  

#### 응답 전략

`SupervisorAgent`를 구성할 때, 하위 에이전트들이 작업을 완료한 후 최종 답변을 어떻게 작성할지 지정합니다. 아래 다이어그램은 세 가지 가능한 전략을 보여줍니다 — LAST는 마지막 에이전트의 출력을 직접 반환하고, SUMMARY는 모든 출력을 LLM을 통해 종합하며, SCORED는 원래 요청에 대해 더 점수가 높은 출력을 선택합니다:

<img src="../../../translated_images/ko/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Supervisor가 최종 응답을 구성하는 세 가지 전략 — 마지막 에이전트 출력, 요약된 결과, 또는 최고 점수 옵션 중 선택하세요.*

가능한 전략은 다음과 같습니다:

| 전략 | 설명 |
|----------|-------------|
| **LAST** | 최종 하위 에이전트나 호출된 도구의 출력을 반환합니다. 이는 워크플로우에서 마지막 에이전트가 완전한 최종 답변을 생성하도록 특별히 설계된 경우 유용합니다(예: 연구 파이프라인의 "요약 에이전트"). |
| **SUMMARY** | Supervisor가 자체 내부 LLM을 사용해 모든 상호작용과 하위 에이전트 출력을 요약하여, 최종 답변으로 반환합니다. 사용자에게 깔끔하고 통합된 답변을 제공합니다. |
| **SCORED** | 시스템이 내부 LLM을 사용해 LAST 응답과 SUMMARY 응답을 원래 사용자 요청에 따라 채점하고, 점수가 높은 출력을 반환합니다. |

전체 구현은 [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)를 참조하세요.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat으로 시도해보세요:** [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)를 열고 다음을 물어보세요:  
> - "Supervisor가 어떤 에이전트를 호출할지 어떻게 결정하나요?"  
> - "Supervisor와 Sequential 워크플로우 패턴의 차이는 무엇인가요?"  
> - "Supervisor의 계획 동작을 어떻게 커스터마이즈할 수 있나요?"  

#### 출력 이해하기

데모를 실행하면 Supervisor가 여러 에이전트를 어떻게 조율하는지 구조화된 과정을 볼 수 있습니다. 각 섹션의 의미는 다음과 같습니다:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**헤더**는 워크플로우 개념을 소개합니다: 파일 읽기부터 보고서 생성까지 집중된 파이프라인입니다.

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```
  
**워크플로우 다이어그램**은 에이전트 간 데이터 흐름을 보여줍니다. 각 에이전트는 특정 역할을 갖습니다:  
- **FileAgent**는 MCP 도구를 사용해 파일을 읽고 `fileContent`에 원시 내용을 저장합니다  
- **ReportAgent**는 해당 내용을 받아 구조화된 보고서 `report`를 생성합니다  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**사용자 요청**은 작업을 나타냅니다. Supervisor가 이를 파싱하고 FileAgent → ReportAgent를 호출하기로 결정합니다.

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```
  
**Supervisor 조율**은 2단계 흐름을 실제로 보여줍니다:  
1. **FileAgent**가 MCP를 통해 파일을 읽어 내용을 저장  
2. **ReportAgent**가 내용을 받아 구조화된 보고서 생성  

Supervisor는 사용자의 요청에 따라 **자율적으로** 이러한 결정을 내렸습니다.

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```
  
#### 에이전틱 모듈 기능 설명

예제는 에이전틱 모듈의 고급 기능 몇 가지를 보여줍니다. Agentic Scope와 Agent Listeners를 자세히 봅시다.

**Agentic Scope**는 `@Agent(outputKey="...")`를 사용해 에이전트들이 결과를 저장한 공유 메모리를 보여줍니다. 이를 통해:  
- 이후 에이전트가 이전 에이전트 출력을 액세스  
- Supervisor가 최종 응답을 종합  
- 사용자가 각 에이전트가 생성한 내용을 검사 가능  

아래 다이어그램은 파일-보고서 워크플로우에서 Agentic Scope가 공유 메모리로 작동하는 방식을 보여줍니다 — FileAgent가 `fileContent` 키 아래에 결과를 쓰고 ReportAgent가 이를 읽어 `report` 아래에 다시 씁니다:

<img src="../../../translated_images/ko/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope는 공유 메모리 역할 — FileAgent가 `fileContent`를 쓰고, ReportAgent가 읽어 `report`를 쓰며, 사용자는 최종 결과를 읽습니다.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // FileAgent의 원시 파일 데이터
String report = scope.readState("report");            // ReportAgent의 구조화된 보고서
```
  
**Agent Listeners**는 에이전트 실행 모니터링과 디버깅을 지원합니다. 데모에서 단계별 출력을 제공하는 것은 각 에이전트 호출에 연결된 AgentListener 덕분입니다:  
- **beforeAgentInvocation** - Supervisor가 에이전트를 선택할 때 호출되어 어떤 에이전트가 왜 선택됐는지 보여줌  
- **afterAgentInvocation** - 에이전트 실행이 완료되면 호출되어 결과를 보여줌  
- **inheritedBySubagents** - true일 때, 계층 내 모든 에이전트를 모니터링  

아래 다이어그램은 전체 Agent Listener 라이프사이클을 보여주며, `onError`가 에이전트 실행 중 오류를 어떻게 처리하는지도 포함합니다:

<img src="../../../translated_images/ko/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners가 실행 라이프사이클과 연결 — 에이전트 시작, 완료, 오류 발생 시 모니터링.*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // 모든 하위 에이전트에 전파하기
    }
};
```
  
Supervisor 패턴 외에도 `langchain4j-agentic` 모듈은 여러 강력한 워크플로우 패턴을 제공합니다. 아래 다이어그램은 단순 순차 파이프라인에서 인간 승인 워크플로우까지 다섯 가지 모두를 보여줍니다:

<img src="../../../translated_images/ko/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*에이전트 조율을 위한 다섯 가지 워크플로우 패턴 — 단순 순차 파이프라인부터 인간 승인 워크플로우까지.*

| 패턴 | 설명 | 사용 사례 |
|---------|-------------|----------|
| **순차(Sequential)** | 에이전트를 순서대로 실행, 출력이 다음 에이전트로 흐름 | 파이프라인: 연구 → 분석 → 보고서 |
| **병렬(Parallel)** | 에이전트를 동시에 실행 | 독립 작업: 날씨 + 뉴스 + 주식 |
| **반복(Loop)** | 조건 충족 시까지 반복 | 품질 평가: 점수 ≥ 0.8 될 때까지 개선 |
| **조건부(Conditional)** | 조건에 따라 경로 분기 | 분류 → 전문가 에이전트로 경로 지정 |
| **인간 개입(Human-in-the-Loop)** | 인간의 승인 추가 | 승인 워크플로우, 콘텐츠 검토 |

## 핵심 개념

MCP와 에이전틱 모듈을 실제로 살펴보았으니, 각 방식을 언제 사용하는지 요약해보겠습니다.

MCP의 가장 큰 장점 중 하나는 성장하는 생태계입니다. 아래 다이어그램은 단일 범용 프로토콜이 AI 애플리케이션을 다양한 MCP 서버(파일시스템, 데이터베이스, GitHub, 이메일, 웹 스크래핑 등)에 연결하는 방식을 보여줍니다:

<img src="../../../translated_images/ko/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP는 범용 프로토콜 생태계를 만듭니다 — MCP 호환 서버는 MCP 호환 클라이언트와 작동해 애플리케이션 간 도구 공유를 가능하게 합니다.*

**MCP**는 기존 도구 생태계를 활용하거나, 여러 애플리케이션이 공유할 도구를 구축하거나, 표준 프로토콜로 서드파티 서비스를 통합하거나, 코드 변경 없이 도구 구현을 교체할 때 이상적입니다.

**에이전틱 모듈**은 `@Agent` 애노테이션으로 선언형 에이전트 정의가 필요하거나, 워크플로우 조율(순차, 반복, 병렬), 명령형 코드보다 인터페이스 기반 에이전트 설계를 선호하거나, `outputKey`를 통해 출력 공유하는 여러 에이전트를 결합할 때 가장 적합합니다.

**Supervisor 에이전트 패턴**은 워크플로우가 미리 예측 불가능하고 LLM이 결정을 내려야 하거나, 동적 조율이 필요한 여러 특화 에이전트가 있거나, 다양한 기능으로 경로를 라우팅하는 대화형 시스템을 구축하거나, 가장 유연하고 적응력 있는 에이전트 동작이 필요한 경우에 빛을 발합니다.

여기서 모듈 04의 커스텀 `@Tool` 메서드와 이번 모듈의 MCP 도구 간 주요 장단점을 비교해보면 — 커스텀 도구는 앱 특화 로직에 대해 긴밀한 결합과 완전한 타입 안전성을 제공하고, MCP 도구는 표준화되고 재사용 가능한 통합을 제공합니다:

<img src="../../../translated_images/ko/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*커스텀 @Tool 메서드와 MCP 도구를 언제 사용하는지 — 앱 특화 로직에 완전한 타입 안전성을 원하면 커스텀, 여러 애플리케이션에서 사용하는 표준화된 통합은 MCP.*

## 축하합니다!

LangChain4j 초급 과정 다섯 개 모듈을 모두 완료하셨습니다! 기본 채팅부터 MCP 기반 에이전틱 시스템까지 완전한 학습 여정을 돌아보세요:

<img src="../../../translated_images/ko/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*기본 채팅부터 MCP 기반 에이전틱 시스템까지 전 모듈 학습 여정.*

LangChain4j 초급 과정을 완료하며 다음을 배웠습니다:

- 메모리를 갖춘 대화형 AI 구축 방법 (모듈 01)  
- 다양한 작업용 프롬프트 엔지니어링 패턴 (모듈 02)  
- 문서 기반 응답 근거 생성(RAG) (모듈 03)  
- 커스텀 도구를 이용한 기본 AI 에이전트 생성 (모듈 04)  
- LangChain4j MCP 및 에이전틱 모듈로 표준화된 도구 통합 (모듈 05)  

### 다음 단계는?

모듈 완료 후 [테스트 가이드](../docs/TESTING.md)를 탐색해 LangChain4j 테스트 개념을 직접 경험해보세요.

**공식 자료:**  
- [LangChain4j 문서](https://docs.langchain4j.dev/) - 포괄적인 가이드 및 API 레퍼런스  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - 소스 코드 및 예제  
- [LangChain4j 튜토리얼](https://docs.langchain4j.dev/tutorials/) - 다양한 사용 사례 단계별 튜토리얼  

과정을 수강해주셔서 감사합니다!

---

**내비게이션:** [← 이전: 모듈 04 - 도구들](../04-tools/README.md) | [메인으로 돌아가기](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
본 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 노력하고 있으나, 자동 번역에는 오류나 부정확한 내용이 포함될 수 있음을 유의해 주시기 바랍니다. 원문 문서가 권위 있는 원본 자료로 간주되어야 합니다. 중요한 정보의 경우, 전문적인 인간 번역을 권장합니다. 본 번역의 사용으로 인해 발생하는 어떠한 오해나 오역에 대해서도 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->