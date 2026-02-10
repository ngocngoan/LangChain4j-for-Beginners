# LangChain4j 용어집

## 목차

- [핵심 개념](../../../docs)
- [LangChain4j 구성 요소](../../../docs)
- [AI/ML 개념](../../../docs)
- [가드레일](../../../docs)
- [프롬프트 엔지니어링](../../../docs)
- [RAG (검색 증강 생성)](../../../docs)
- [에이전트 및 도구](../../../docs)
- [에이전틱 모듈](../../../docs)
- [모델 컨텍스트 프로토콜(MCP)](../../../docs)
- [Azure 서비스](../../../docs)
- [테스트 및 개발](../../../docs)

과정 전반에 걸쳐 사용되는 용어와 개념의 빠른 참고 자료.

## 핵심 개념

**AI 에이전트** - AI를 사용하여 자율적으로 추론하고 행동하는 시스템. [모듈 04](../04-tools/README.md)

**체인** - 출력이 다음 단계로 전달되는 작업 연속.

**청킹** - 문서를 더 작은 조각으로 분할. 일반적으로 300-500 토큰 중첩 포함. [모듈 03](../03-rag/README.md)

**컨텍스트 창** - 모델이 처리할 수 있는 최대 토큰 수. GPT-5.2: 400K 토큰.

**임베딩** - 텍스트 의미를 나타내는 수치 벡터. [모듈 03](../03-rag/README.md)

**함수 호출** - 모델이 외부 함수를 호출하기 위한 구조화된 요청 생성. [모듈 04](../04-tools/README.md)

**환각** - 모델이 그럴듯하지만 잘못된 정보를 생성하는 현상.

**프롬프트** - 언어 모델에 대한 텍스트 입력. [모듈 02](../02-prompt-engineering/README.md)

**의미 기반 검색** - 키워드가 아닌 임베딩을 이용한 의미 기반 검색. [모듈 03](../03-rag/README.md)

**상태 저장 vs 상태 비저장** - 상태 비저장: 메모리 없음. 상태 저장: 대화 기록 유지. [모듈 01](../01-introduction/README.md)

**토큰** - 모델이 처리하는 기본 텍스트 단위. 비용과 제한에 영향을 미침. [모듈 01](../01-introduction/README.md)

**도구 체인** - 출력이 다음 호출에 영향을 주는 연속적인 도구 실행. [모듈 04](../04-tools/README.md)

## LangChain4j 구성 요소

**AiServices** - 타입 안전 AI 서비스 인터페이스 생성.

**OpenAiOfficialChatModel** - OpenAI와 Azure OpenAI 모델을 위한 통합 클라이언트.

**OpenAiOfficialEmbeddingModel** - OpenAI 공식 클라이언트로 임베딩 생성 (OpenAI 및 Azure OpenAI 모두 지원).

**ChatModel** - 언어 모델의 핵심 인터페이스.

**ChatMemory** - 대화 기록 유지.

**ContentRetriever** - RAG용 관련 문서 청크 찾기.

**DocumentSplitter** - 문서를 청크로 분할.

**EmbeddingModel** - 텍스트를 수치 벡터로 변환.

**EmbeddingStore** - 임베딩 저장 및 검색.

**MessageWindowChatMemory** - 최근 메시지의 슬라이딩 윈도우 유지.

**PromptTemplate** - `{{variable}}` 자리표시자가 포함된 재사용 가능한 프롬프트 생성.

**TextSegment** - 메타데이터가 포함된 텍스트 청크. RAG에서 사용.

**ToolExecutionRequest** - 도구 실행 요청 표현.

**UserMessage / AiMessage / SystemMessage** - 대화 메시지 유형.

## AI/ML 개념

**Few-Shot Learning** - 프롬프트에 예시 제공. [모듈 02](../02-prompt-engineering/README.md)

**대형 언어 모델(LLM)** - 방대한 텍스트 데이터로 학습된 AI 모델.

**추론 노력** - GPT-5.2에서 사고 깊이를 조절하는 매개변수. [모듈 02](../02-prompt-engineering/README.md)

**온도** - 출력 무작위성 제어. 낮음=결정론적, 높음=창의적.

**벡터 데이터베이스** - 임베딩 전용 데이터베이스. [모듈 03](../03-rag/README.md)

**제로샷 학습** - 예시 없이 작업 수행. [모듈 02](../02-prompt-engineering/README.md)

## 가드레일 - [모듈 00](../00-quick-start/README.md)

**심층 방어** - 애플리케이션 수준 가드레일과 공급자 안전 필터 결합한 다층 보안 접근법.

**하드 블록** - 공급자가 심각한 콘텐츠 위반에 대해 HTTP 400 오류 반환.

**InputGuardrail** - LLM에 도달하기 전에 사용자 입력 유효성 검사하는 LangChain4j 인터페이스. 유해한 프롬프트를 초기에 차단하여 비용과 지연 절감.

**InputGuardrailResult** - 가드레일 검증 반환 타입: `success()` 또는 `fatal("reason")`.

**OutputGuardrail** - 사용자에게 반환하기 전에 AI 응답을 검증하는 인터페이스.

**공급자 안전 필터** - API 수준에서 위반을 감지하는 AI 공급자의 내장 콘텐츠 필터(예: GitHub Models).

**소프트 거부** - 모델이 오류 없이 공손히 답변 거절.

## 프롬프트 엔지니어링 - [모듈 02](../02-prompt-engineering/README.md)

**연쇄 사고(Chain-of-Thought)** - 더 나은 정확성을 위한 단계별 추론.

**제한된 출력** - 특정 형식 또는 구조 강제.

**높은 열의(High Eagerness)** - 철저한 추론을 위한 GPT-5.2 패턴.

**낮은 열의(Low Eagerness)** - 빠른 답변을 위한 GPT-5.2 패턴.

**다중 회차 대화** - 교환 간 컨텍스트 유지.

**역할 기반 프롬프트** - 시스템 메시지를 통해 모델 페르소나 설정.

**자기 성찰** - 모델이 자신의 출력을 평가하고 개선.

**구조화된 분석** - 고정된 평가 프레임워크.

**작업 실행 패턴** - 계획 → 실행 → 요약.

## RAG (검색 증강 생성) - [모듈 03](../03-rag/README.md)

**문서 처리 파이프라인** - 로드 → 청크 → 임베드 → 저장.

**인메모리 임베딩 스토어** - 테스트용 비영구 저장소.

**RAG** - 검색과 생성을 결합하여 답변 근거 제공.

**유사도 점수** - 의미적 유사성 측정(0-1).

**출처 참조** - 검색된 콘텐츠에 대한 메타데이터.

## 에이전트 및 도구 - [모듈 04](../04-tools/README.md)

**@Tool 어노테이션** - Java 메서드를 AI 호출 가능 도구로 표시.

**ReAct 패턴** - 추론 → 행동 → 관찰 → 반복.

**세션 관리** - 사용자별 별도 컨텍스트 유지.

**도구** - AI 에이전트가 호출할 수 있는 기능.

**도구 설명** - 도구 목적 및 매개변수 문서화.

## 에이전틱 모듈 - [모듈 05](../05-mcp/README.md)

**@Agent 어노테이션** - 인터페이스를 AI 에이전트로 표시하고 선언적 행위 정의.

**Agent Listener** - `beforeAgentInvocation()`, `afterAgentInvocation()`을 통한 에이전트 실행 모니터링 훅.

**에이전틱 스코프** - 에이전트가 `outputKey`를 사용해 결과를 저장하고, 후속 에이전트가 사용 가능한 공유 메모리.

**AgenticServices** - `agentBuilder()`와 `supervisorBuilder()`로 에이전트 생성하는 팩토리.

**조건부 워크플로우** - 조건에 따라 다양한 전문 에이전트로 경로 분기.

**사람 참여 루프(Human-in-the-Loop)** - 승인 또는 콘텐츠 검토를 위한 사람 체크포인트 추가 워크플로우 패턴.

**langchain4j-agentic** - 선언적 에이전트 빌딩용 Maven 의존성(실험적).

**루프 워크플로우** - 조건(예: 품질 점수 ≥ 0.8) 만족할 때까지 에이전트 실행 반복.

**outputKey** - 에이전트 어노테이션 매개변수로, 결과를 에이전틱 스코프 내 저장 위치 지정.

**병렬 워크플로우** - 독립 작업을 위해 여러 에이전트 동시에 실행.

**응답 전략** - 감독자가 최종 답변을 작성하는 방식: LAST, SUMMARY, 또는 SCORED.

**순차 워크플로우** - 출력이 다음 단계로 흐르는 순서대로 에이전트 실행.

**감독 에이전트 패턴** - 감독자 LLM이 어떤 하위 에이전트를 호출할지 동적으로 결정하는 고급 에이전틱 패턴.

## 모델 컨텍스트 프로토콜(MCP) - [모듈 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j에서 MCP 통합을 위한 Maven 의존성.

**MCP** - 모델 컨텍스트 프로토콜: AI 앱이 외부 도구에 연결하는 표준. 한 번 구축하면 어디서나 사용 가능.

**MCP 클라이언트** - MCP 서버에 연결해 도구를 검색하고 사용하는 애플리케이션.

**MCP 서버** - 명확한 설명과 매개변수 스키마를 제공하며 도구를 MCP를 통해 노출하는 서비스.

**McpToolProvider** - AI 서비스 및 에이전트에서 사용할 MCP 도구를 래핑하는 LangChain4j 구성 요소.

**McpTransport** - MCP 통신 인터페이스. 구현체로는 Stdio와 HTTP 포함.

**Stdio 전송** - stdin/stdout을 통한 로컬 프로세스 전송. 파일시스템 접근이나 커맨드라인 도구에 유용.

**StdioMcpTransport** - MCP 서버를 서브프로세스로 생성하는 LangChain4j 구현체.

**도구 검색** - 클라이언트가 서버에 사용 가능한 도구와 설명, 스키마를 질의.

## Azure 서비스 - [모듈 01](../01-introduction/README.md)

**Azure AI 검색** - 벡터 기능을 갖춘 클라우드 검색. [모듈 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Azure 리소스 배포.

**Azure OpenAI** - 마이크로소프트의 기업용 AI 서비스.

**Bicep** - Azure 인프라 코드 언어. [인프라 가이드](../01-introduction/infra/README.md)

**배포 이름** - Azure에서 모델 배포 이름.

**GPT-5.2** - 추론 제어 가능한 최신 OpenAI 모델. [모듈 02](../02-prompt-engineering/README.md)

## 테스트 및 개발 - [테스트 가이드](TESTING.md)

**개발 컨테이너(Dev Container)** - 컨테이너화된 개발 환경. [구성](../../../.devcontainer/devcontainer.json)

**GitHub 모델** - 무료 AI 모델 플레이그라운드. [모듈 00](../00-quick-start/README.md)

**인메모리 테스트** - 인메모리 저장소를 이용한 테스트.

**통합 테스트** - 실제 인프라를 통한 테스트.

**Maven** - 자바 빌드 자동화 도구.

**Mockito** - 자바 목킹 프레임워크.

**Spring Boot** - 자바 애플리케이션 프레임워크. [모듈 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다하고 있지만, 자동 번역에는 오류나 부정확성이 포함될 수 있음을 유의하시기 바랍니다. 원본 문서가 권위 있는 자료로 간주되어야 합니다. 중요한 정보의 경우 전문적인 인간 번역을 권장합니다. 본 번역물 사용으로 인한 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->