# Module 05: 모델 컨텍스트 프로토콜 (MCP)

## 목차

- [학습할 내용](../../../05-mcp)
- [MCP란 무엇인가?](../../../05-mcp)
- [MCP 작동 방식](../../../05-mcp)
- [에이전틱 모듈](../../../05-mcp)
- [예제 실행하기](../../../05-mcp)
  - [전제 조건](../../../05-mcp)
- [빠른 시작](../../../05-mcp)
  - [파일 작업 (Stdio)](../../../05-mcp)
  - [슈퍼바이저 에이전트](../../../05-mcp)
    - [데모 실행하기](../../../05-mcp)
    - [슈퍼바이저 작동 방식](../../../05-mcp)
    - [응답 전략](../../../05-mcp)
    - [출력 이해하기](../../../05-mcp)
    - [에이전틱 모듈 기능 설명](../../../05-mcp)
- [핵심 개념](../../../05-mcp)
- [축하합니다!](../../../05-mcp)
  - [다음은?](../../../05-mcp)

## 학습할 내용

대화형 AI를 구축하고, 프롬프트를 마스터하며, 문서 기반 응답을 구현하고, 도구를 가진 에이전트를 만들었습니다. 하지만 이 모든 도구는 특정 애플리케이션에 맞춰 제작된 맞춤형 도구였습니다. 만약 여러분의 AI에 누구나 만들고 공유할 수 있는 표준화된 도구 생태계에 접근할 수 있게 한다면 어떨까요? 이 모듈에서는 모델 컨텍스트 프로토콜(MCP)과 LangChain4j의 에이전틱 모듈을 사용해 그것을 어떻게 구현하는지 배웁니다. 우선 간단한 MCP 파일 리더를 보여주고, 이후에는 슈퍼바이저 에이전트 패턴을 통해 고급 에이전틱 워크플로에 쉽게 통합하는 방법을 설명합니다.

## MCP란 무엇인가?

모델 컨텍스트 프로토콜(MCP)은 바로 그 목적을 위해 존재합니다 — AI 애플리케이션이 외부 도구를 발견하고 사용할 수 있는 표준화된 방식입니다. 각 데이터 소스나 서비스마다 맞춤형 통합 코드를 작성하는 대신, MCP 서버에 연결해 그 서버가 표준화된 형식으로 제공하는 도구들을 사용할 수 있습니다. AI 에이전트는 이러한 도구들을 자동으로 발견하고 사용할 수 있습니다.

아래 다이어그램은 차이를 보여줍니다 — MCP가 없으면 각각의 통합이 맞춤형 포인트 투 포인트 연결을 필요로 합니다; MCP가 있으면 하나의 프로토콜로 앱을 모든 도구에 연결할 수 있습니다:

<img src="../../../translated_images/ko/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP 이전: 복잡한 포인트 투 포인트 통합. MCP 이후: 하나의 프로토콜, 무한한 가능성.*

MCP는 AI 개발의 근본적인 문제를 해결합니다: 모든 통합이 맞춤형이라는 점입니다. GitHub에 접근하고 싶다면? 맞춤 코드. 파일을 읽고 싶다면? 맞춤 코드. 데이터베이스 쿼리를 하고 싶다면? 맞춤 코드. 그리고 이런 통합 중 어느 것도 다른 AI 애플리케이션과는 호환되지 않습니다.

MCP는 이를 표준화합니다. MCP 서버는 명확한 설명과 스키마를 갖춘 도구를 공개합니다. 모든 MCP 클라이언트는 연결해서 사용 가능한 도구들을 발견하고 사용할 수 있습니다. 한 번 구축하면 어디서든 사용 가능합니다.

아래 다이어그램은 이 아키텍처를 보여줍니다 — 하나의 MCP 클라이언트(여러분의 AI 애플리케이션)가 여러 MCP 서버에 연결하고, 각 서버는 표준 프로토콜을 통해 자체 도구 세트를 공개합니다:

<img src="../../../translated_images/ko/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*모델 컨텍스트 프로토콜 아키텍처 - 표준화된 도구 발견과 실행*

## MCP 작동 방식

내부적으로 MCP는 계층화된 아키텍처를 사용합니다. 여러분의 자바 애플리케이션(MCP 클라이언트)은 사용 가능한 도구를 발견하고, JSON-RPC 요청을 전송하며, 전송 계층(Stdio 또는 HTTP)을 통해 MCP 서버가 작업을 실행하고 결과를 반환합니다. 다음 다이어그램은 프로토콜의 각 계층을 자세히 분해합니다:

<img src="../../../translated_images/ko/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP 내부 작동 방식 — 클라이언트는 도구를 발견하고, JSON-RPC 메시지를 교환하며, 전송 계층을 통해 작업을 실행합니다.*

**서버-클라이언트 아키텍처**

MCP는 클라이언트-서버 모델을 사용합니다. 서버는 파일 읽기, 데이터베이스 쿼리, API 호출 같은 도구를 제공합니다. 클라이언트(여러분의 AI 애플리케이션)는 서버에 연결해 도구를 사용합니다.

LangChain4j와 MCP를 사용하려면 다음 Maven 의존성을 추가하세요:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**도구 발견**

클라이언트가 MCP 서버에 연결하면 "어떤 도구들이 있나요?"라고 묻습니다. 서버는 설명과 파라미터 스키마가 있는 사용 가능한 도구들의 목록을 응답합니다. AI 에이전트는 사용자 요청에 따라 어떤 도구를 사용할지 결정할 수 있습니다. 아래 다이어그램은 이 핸드셰이크 과정을 보여줍니다 — 클라이언트는 `tools/list` 요청을 보내고 서버는 설명과 파라미터 스키마와 함께 사용 가능한 도구 목록을 반환합니다:

<img src="../../../translated_images/ko/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI가 시작 시 사용 가능한 도구를 발견합니다 — 사용할 수 있는 기능을 알게 되어 어떤 도구를 사용할지 결정할 수 있습니다.*

**전송 메커니즘**

MCP는 다양한 전송 메커니즘을 지원합니다. 두 가지 옵션은 Stdio(로컬 하위 프로세스 통신용)와 스트리밍 가능한 HTTP(원격 서버용)입니다. 이 모듈에서는 Stdio 전송을 시연합니다:

<img src="../../../translated_images/ko/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP 전송 메커니즘: 원격 서버용 HTTP, 로컬 프로세스용 Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

로컬 프로세스용입니다. 애플리케이션이 서버를 하위 프로세스로 생성하고 표준 입출력을 통해 통신합니다. 파일 시스템 접근이나 커맨드 라인 도구에 유용합니다.

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

`@modelcontextprotocol/server-filesystem` 서버는 다음 도구들을 제공하며, 모두 지정한 디렉터리 내에서 샌드박스됩니다:

| 도구 | 설명 |
|------|-------------|
| `read_file` | 단일 파일의 내용을 읽기 |
| `read_multiple_files` | 여러 파일을 한 번에 읽기 |
| `write_file` | 파일 생성 또는 덮어쓰기 |
| `edit_file` | 대상 지정 검색 및 교체 편집 |
| `list_directory` | 경로의 파일 및 디렉터리 목록 조회 |
| `search_files` | 패턴에 맞는 파일을 재귀적으로 검색 |
| `get_file_info` | 파일 메타데이터(크기, 타임스탬프, 권한) 획득 |
| `create_directory` | 디렉터리 생성 (상위 디렉터리 포함) |
| `move_file` | 파일 또는 디렉터리 이동 또는 이름 변경 |

다음 다이어그램은 Stdio 전송이 런타임에 어떻게 작동하는지를 보여줍니다 — 자바 애플리케이션이 MCP 서버를 하위 프로세스로 생성하고, 네트워크나 HTTP 없이 표준 입력/출력 파이프를 통해 통신합니다:

<img src="../../../translated_images/ko/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*실제 Stdio 전송 동작 — 애플리케이션이 하위 프로세스로 MCP 서버를 생성하고 stdin/stdout 파이프를 통해 통신합니다.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 챗으로 시도해 보세요:** [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)를 열고 다음을 물어보세요:
> - "Stdio 전송은 어떻게 작동하며 HTTP 대비 언제 사용하는 것이 좋은가요?"
> - "LangChain4j는 생성된 MCP 서버 프로세스의 생명주기를 어떻게 관리하나요?"
> - "AI에 파일 시스템 접근 권한을 부여할 때 보안적 고려사항은 무엇인가요?"

## 에이전틱 모듈

MCP가 표준화된 도구를 제공하는 반면, LangChain4j의 **에이전틱 모듈**은 이러한 도구들을 오케스트레이션하는 에이전트를 선언적으로 구축할 수 있게 합니다. `@Agent` 애노테이션과 `AgenticServices`를 이용해 명령형 코드 대신 인터페이스를 통해 에이전트 행동을 정의할 수 있습니다.

본 모듈에서는 **슈퍼바이저 에이전트** 패턴을 살펴봅니다 — 이는 "슈퍼바이저" 에이전트가 사용자 요청에 따라 어떤 하위 에이전트를 호출할지 동적으로 결정하는 고급 에이전틱 AI 접근법입니다. MCP 기능을 탑재한 파일 액세스 능력을 가진 하위 에이전트 중 하나와 결합해서 두 개념을 함께 보여줍니다.

에이전틱 모듈을 사용하려면 다음 Maven 의존성을 추가하세요:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **참고:** `langchain4j-agentic` 모듈은 코어 LangChain4j 라이브러리와 다른 출시 일정으로 인해 별도의 버전 프로퍼티(`langchain4j.mcp.version`)를 사용합니다.

> **⚠️ 실험적:** `langchain4j-agentic` 모듈은 **실험적**이며 변경될 수 있습니다. AI 어시스턴트를 구축하는 안정적인 방법은 여전히 `langchain4j-core`와 맞춤 도구(모듈 04)를 사용하는 것입니다.

## 예제 실행하기

### 전제 조건

- [모듈 04 - 도구](../04-tools/README.md) 완료 (본 모듈은 맞춤 도구 개념 위에 구축되며 MCP 도구와 비교합니다)
- 루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일 (모듈 01에서 `azd up`으로 생성)
- Java 21 이상, Maven 3.9 이상
- Node.js 16 이상 및 npm (MCP 서버용)

> **참고:** 환경 변수를 아직 설정하지 않았다면, [모듈 01 - 소개](../01-introduction/README.md)를 참고해 배포 지침을 확인하세요 (`azd up`이 `.env` 파일을 자동 생성합니다), 혹은 루트 디렉터리에서 `.env.example`을 `.env`로 복사한 후 값을 채우세요.

## 빠른 시작

**VS Code 사용 시:** 탐색기에서 원하는 데모 파일을 우클릭하여 **"Run Java"**를 선택하거나, 실행 및 디버그 패널의 실행 구성을 이용하세요 (`.env` 파일에 Azure 자격 증명이 설정되어 있어야 합니다).

**Maven 사용 시:** 또는 커맨드 라인에서 다음 예제 명령어로 실행할 수 있습니다.

### 파일 작업 (Stdio)

로컬 하위 프로세스 기반 도구를 시연합니다.

**✅ 별도 설치 필요 없음** — MCP 서버가 자동으로 생성됩니다.

**시작 스크립트 사용 (권장):**

시작 스크립트는 루트 `.env` 파일에서 환경 변수를 자동으로 로드합니다:

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

**VS Code 사용:** `StdioTransportDemo.java`를 우클릭해 **"Run Java"**를 선택하세요 (`.env` 파일이 설정되어 있어야 함).

애플리케이션이 파일 시스템 MCP 서버를 자동으로 시작하고, 로컬 파일을 읽습니다. 하위 프로세스 관리가 자동으로 처리되는 점에 주목하세요.

**예상 출력:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### 슈퍼바이저 에이전트

**슈퍼바이저 에이전트 패턴**은 **유연한** 형태의 에이전틱 AI입니다. 슈퍼바이저는 LLM을 사용해 사용자 요청을 기반으로 호출할 에이전트를 자율적으로 결정합니다. 다음 예제에서는 MCP 기반 파일 액세스와 LLM 에이전트를 결합해, 감독받는 파일 읽기 → 보고서 생성 워크플로를 만듭니다.

데모에서 `FileAgent`는 MCP 파일 시스템 도구로 파일을 읽고, `ReportAgent`는 간단한 요약(1문장), 주요 포인트 3개, 권장 사항이 포함된 구조화된 보고서를 작성합니다. 슈퍼바이저가 이 흐름을 자동으로 오케스트레이션합니다:

<img src="../../../translated_images/ko/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*슈퍼바이저가 LLM을 이용해 어떤 에이전트를 호출하고 어떤 순서로 실행할지 결정합니다 — 하드코딩된 라우팅이 필요 없습니다.*

파일-보고서 파이프라인의 구체적 워크플로는 다음과 같습니다:

<img src="../../../translated_images/ko/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent가 MCP 도구로 파일을 읽고, ReportAgent가 원시 내용을 구조화된 보고서로 변환합니다.*

각 에이전트는 출력물을 **에이전틱 범위**(공유 메모리)에 저장해, 이후 실행되는 에이전트가 이전 결과에 접근할 수 있습니다. 이는 MCP 도구가 에이전틱 워크플로에 매끄럽게 통합되는 방식을 보여줍니다 — 슈퍼바이저는 파일을 *어떻게* 읽는지 알 필요 없이 단지 `FileAgent`가 할 수 있다는 것만 알면 됩니다.

#### 데모 실행하기

시작 스크립트는 루트 `.env` 파일에서 환경 변수를 자동으로 로드합니다:

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

**VS Code 사용:** `SupervisorAgentDemo.java`를 우클릭해 **"Run Java"**를 선택하세요 (`.env` 파일이 설정되어 있어야 함).

#### 슈퍼바이저 작동 방식

에이전트를 만들기 전에 MCP 전송을 클라이언트에 연결하고 이를 `ToolProvider`로 래핑해야 합니다. 이렇게 MCP 서버의 도구가 에이전트가 사용할 수 있게 됩니다:

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
        .toolProvider(mcpToolProvider)  // 파일 작업을 위한 MCP 도구를 보유하고 있습니다
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

// Supervisor가 요청에 따라 호출할 에이전트를 결정합니다
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### 응답 전략

`SupervisorAgent`를 구성할 때는 하위 에이전트가 작업을 완료한 뒤 최종적으로 사용자에게 어떻게 답변할지 전략을 지정합니다. 아래 다이어그램은 총 세 가지 선택지를 보여줍니다 — LAST는 마지막 에이전트의 출력을 직접 반환, SUMMARY는 모든 출력을 LLM으로 합성해서 반환, SCORED는 원래 사용자 요청에 대해 더 높은 점수를 받은 출력을 선택:

<img src="../../../translated_images/ko/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*슈퍼바이저가 최종 응답을 생성하는 세 가지 전략 — 마지막 에이전트 출력을 원하는지, 요약된 합성 결과를 원하는지, 아니면 점수 기반으로 최고 결과를 원하는지 선택하세요.*

사용 가능한 전략은 다음과 같습니다:

| 전략 | 설명 |
|----------|-------------|
| **LAST** | 슈퍼바이저가 마지막으로 호출된 하위 에이전트 또는 도구의 출력을 반환합니다. 워크플로 우선순위에서 마지막 에이전트가 완전한 최종 답변을 생성하도록 설계된 경우(예: 연구 파이프라인의 "요약 에이전트")에 유용합니다. |
| **SUMMARY** | 슈퍼바이저가 자체 내부 LLM을 사용해 전체 인터렉션과 모든 하위 에이전트 출력을 요약하여 종합한 후 그 요약을 최종 응답으로 반환합니다. 사용자에게 깔끔하고 통합된 답변을 제공합니다. |
| **SCORED** | 시스템 내부 LLM이 LAST 응답과 SUMMARY 요약을 원래 사용자 요청에 대해 점수화하고, 더 높은 점수를 받은 출력을 반환합니다. |
전체 구현은 [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)를 참조하세요.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat으로 시도해보기:** [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java)를 열고 다음 질문을 해보세요:
> - "Supervisor는 어떤 에이전트를 호출할지 어떻게 결정하나요?"
> - "Supervisor와 순차적 워크플로우 패턴의 차이는 무엇인가요?"
> - "Supervisor의 계획 동작을 어떻게 사용자 맞춤화할 수 있나요?"

#### 출력 이해하기

데모를 실행하면 Supervisor가 여러 에이전트를 어떻게 조정하는지 단계별로 볼 수 있습니다. 각 섹션의 의미는 다음과 같습니다:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**헤더**는 파일 읽기부터 보고서 생성까지 집중된 파이프라인 워크플로우 개념을 소개합니다.

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
  
**워크플로우 다이어그램**은 에이전트 간의 데이터 흐름을 보여줍니다. 각 에이전트는 구체적인 역할을 가집니다:  
- **FileAgent**는 MCP 도구를 사용해 파일을 읽고 `fileContent`에 원본 내용을 저장합니다  
- **ReportAgent**는 그 내용을 소비하여 `report`에 구조화된 보고서를 생성합니다

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**사용자 요청**은 작업을 보여줍니다. Supervisor는 이를 파싱하여 FileAgent → ReportAgent를 호출하기로 결정합니다.

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
  
**Supervisor 조정**은 2단계 흐름을 실행하는 모습을 보여줍니다:  
1. **FileAgent**가 MCP를 통해 파일을 읽고 내용을 저장합니다  
2. **ReportAgent**가 그 내용을 받아 구조화된 보고서를 생성합니다

Supervisor는 사용자의 요청을 바탕으로 **자율적으로** 이러한 결정을 내렸습니다.

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
  
#### Agentic 모듈 기능 설명

예제는 agentic 모듈의 여러 고급 기능을 시연합니다. Agentic Scope와 Agent Listeners를 자세히 살펴보겠습니다.

**Agentic Scope**는 에이전트들이 `@Agent(outputKey="...")`를 이용해 결과를 저장하는 공유 메모리를 보여줍니다. 이를 통해:  
- 이후 에이전트가 이전 에이전트 출력에 접근 가능  
- Supervisor가 최종 응답을 종합 가능  
- 여러분이 각 에이전트가 생성한 결과를 확인 가능

아래 다이어그램은 파일에서 보고서로 가는 워크플로우에서 Agentic Scope가 공유 메모리로 동작하는 방식을 보여줍니다—FileAgent는 `fileContent` 키로 출력을 쓰고 ReportAgent는 이를 읽어 `report` 키로 자신의 출력을 씁니다:

<img src="../../../translated_images/ko/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope는 공유 메모리 역할을 합니다 — FileAgent가 `fileContent`를 쓰고 ReportAgent는 이를 읽어 `report`를 쓰며, 최종 결과는 여러분의 코드가 읽습니다.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // FileAgent에서 가져온 원시 파일 데이터
String report = scope.readState("report");            // ReportAgent에서 생성된 구조화된 보고서
```
  
**Agent Listeners**는 에이전트 실행의 모니터링 및 디버깅을 가능하게 합니다. 데모에서 보는 단계별 출력은 각 에이전트 호출 시점에 훅킹하는 AgentListener에서 나옵니다:  
- **beforeAgentInvocation** - Supervisor가 에이전트를 선택할 때 호출되어 어떤 에이전트가 왜 선택되었는지 보여줌  
- **afterAgentInvocation** - 에이전트 완료 후 호출되어 결과를 보여줌  
- **inheritedBySubagents** - true일 때 계층 구조에 있는 모든 에이전트를 모니터링함

다음 다이어그램은 에이전트 실행 중 오류 발생 시 `onError`가 처리하는 방법을 포함한 전체 Agent Listener 생명주기를 보여줍니다:

<img src="../../../translated_images/ko/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners는 에이전트가 시작, 완료, 오류 발생 시점에 실행 흐름에 훅킹합니다.*

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
        return true; // 모든 하위 에이전트에게 전파하기
    }
};
```
  
Supervisor 패턴 외에도 `langchain4j-agentic` 모듈은 여러 강력한 워크플로우 패턴을 제공합니다. 아래 다이어그램은 단순한 순차 파이프라인부터 사람 개입 승인 워크플로우까지 다섯 가지를 보여줍니다:

<img src="../../../translated_images/ko/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*에이전트 조정을 위한 다섯 가지 워크플로우 패턴 — 단순 순차 파이프라인부터 사람 개입 승인 워크플로우까지.*

| 패턴 | 설명 | 사용 사례 |
|---------|-------------|----------|
| **순차적(Sequential)** | 에이전트를 순서대로 실행하며 출력이 다음 단계로 흐름 | 파이프라인: 조사 → 분석 → 보고 |
| **병렬(Parallel)** | 에이전트를 동시에 실행 | 독립 작업: 날씨 + 뉴스 + 주식 |
| **반복(Loop)** | 조건 충족 시까지 반복 | 품질 점수: 점수 ≥ 0.8 될 때까지 정제 |
| **조건부(Conditional)** | 조건에 따라 라우팅 | 분류 후 전문 에이전트에 전달 |
| **사람 개입(Human-in-the-Loop)** | 사람 검토 단계 추가 | 승인 워크플로우, 콘텐츠 리뷰 |

## 주요 개념

MCP와 agentic 모듈을 직접 체험한 후, 각각을 언제 사용하는지 요약해 보겠습니다.

MCP의 가장 큰 강점 중 하나는 성장하는 생태계입니다. 아래 다이어그램은 파일 시스템, 데이터베이스 접근부터 GitHub, 이메일, 웹 스크래핑 등에 이르기까지 광범위한 MCP 서버와 연결하는 단일 범용 프로토콜을 보여줍니다:

<img src="../../../translated_images/ko/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP는 범용 프로토콜 생태계를 만듭니다 — MCP 호환 서버는 MCP 호환 클라이언트 어디에서나 작동하여 도구를 애플리케이션 간에 공유할 수 있게 합니다.*

**MCP**는 기존 도구 생태계를 활용하거나, 여러 애플리케이션이 공유할 수 있는 도구를 만들거나, 표준 프로토콜로 서드파티 서비스를 통합하거나, 코드를 바꾸지 않고 도구 구현을 교체하고 싶을 때 적합합니다.

**Agentic 모듈**은 `@Agent` 애노테이션으로 선언적 에이전트 정의를 원하거나, 순차/반복/병렬 워크플로우 조정이 필요하거나, 명령형 코드보다 인터페이스 기반 에이전트 설계를 선호하거나, `outputKey`로 출력을 공유하는 다수 에이전트를 결합할 때 최고입니다.

**Supervisor Agent 패턴**은 워크플로우가 미리 예측 불가능하고 LLM에게 결정권을 주고 싶을 때, 여러 전문 에이전트를 동적으로 조정해야 할 때, 다양한 기능으로 라우팅하는 대화형 시스템을 구축할 때, 가장 유연하고 적응력 있는 에이전트 행동이 필요할 때 빛납니다.

Module 04의 맞춤 `@Tool` 메서드와 이번 모듈의 MCP 도구 사용을 결정할 때, 다음 비교표가 주요 장단점을 강조합니다 — 맞춤 도구는 앱 특화 로직에 대한 강한 결합과 완전한 타입 안전성을 제공하며, MCP 도구는 표준화되고 재사용 가능한 통합을 제공합니다:

<img src="../../../translated_images/ko/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*맞춤 @Tool 메서드 사용 시기 vs MCP 도구 사용 시기 — 맞춤 도구는 완전한 타입 안전성으로 앱 특화 로직에, MCP 도구는 여러 애플리케이션에 통합 가능하도록 표준화에 중점.*

## 축하합니다!

LangChain4j 초보자 과정을 모두 수료하셨습니다! 기본 채팅부터 MCP 기반 agentic 시스템까지 완료한 전체 학습 여정을 살펴보세요:

<img src="../../../translated_images/ko/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*기본 채팅부터 MCP 기반 agentic 시스템까지 모든 5개 모듈을 거친 학습 여정.*

LangChain4j 초보자 과정을 완료하셨습니다. 다음을 배웠습니다:

- 메모리 기반 대화형 AI 구축법 (Module 01)  
- 다양한 작업을 위한 프롬프트 엔지니어링 패턴 (Module 02)  
- 문서에 근거한 답변 생성 RAG (Module 03)  
- 맞춤 도구로 기본 AI 에이전트(어시스턴트) 생성 (Module 04)  
- LangChain4j MCP 및 Agentic 모듈로 표준화 도구 통합 (Module 05)

### 다음은?

모듈을 완료한 후, [Testing Guide](../docs/TESTING.md)를 탐색하여 LangChain4j 테스트 개념을 실전에서 확인해 보세요.

**공식 자료:**  
- [LangChain4j Documentation](https://docs.langchain4j.dev/) - 포괄적인 가이드와 API 참고  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - 소스 코드와 예제  
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - 다양한 사용 사례별 단계별 튜토리얼

과정을 완주해 주셔서 감사합니다!

---

**네비게이션:** [← 이전: Module 04 - Tools](../04-tools/README.md) | [메인으로 돌아가기](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 노력하고 있으나 자동 번역에는 오류나 부정확성이 포함될 수 있음을 유의하시기 바랍니다. 원문 문서가 권위 있는 자료로 간주되어야 합니다. 중요한 정보의 경우 전문 인력에 의한 번역을 권장합니다. 본 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->