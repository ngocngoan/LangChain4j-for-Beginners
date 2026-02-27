# Module 03: RAG (검색 증강 생성)

## Table of Contents

- [Video Walkthrough](../../../03-rag)
- [What You'll Learn](../../../03-rag)
- [Prerequisites](../../../03-rag)
- [Understanding RAG](../../../03-rag)
  - [Which RAG Approach Does This Tutorial Use?](../../../03-rag)
- [How It Works](../../../03-rag)
  - [Document Processing](../../../03-rag)
  - [Creating Embeddings](../../../03-rag)
  - [Semantic Search](../../../03-rag)
  - [Answer Generation](../../../03-rag)
- [Run the Application](../../../03-rag)
- [Using the Application](../../../03-rag)
  - [Upload a Document](../../../03-rag)
  - [Ask Questions](../../../03-rag)
  - [Check Source References](../../../03-rag)
  - [Experiment with Questions](../../../03-rag)
- [Key Concepts](../../../03-rag)
  - [Chunking Strategy](../../../03-rag)
  - [Similarity Scores](../../../03-rag)
  - [In-Memory Storage](../../../03-rag)
  - [Context Window Management](../../../03-rag)
- [When RAG Matters](../../../03-rag)
- [Next Steps](../../../03-rag)

## Video Walkthrough

이 모듈을 시작하는 방법을 설명하는 라이브 세션을 시청하세요: [LangChain4j와 함께하는 RAG - 라이브 세션](https://www.youtube.com/watch?v=_olq75ZH_eY)

## What You'll Learn

이전 모듈에서는 AI와 대화하는 방법과 프롬프트를 효과적으로 구성하는 방법을 배웠습니다. 그러나 근본적인 한계가 있습니다: 언어 모델은 훈련 시 배운 내용만 알 뿐입니다. 회사 정책, 프로젝트 문서 또는 훈련에 포함되지 않은 정보에 대한 질문에는 답할 수 없습니다.

RAG(검색 증강 생성)는 이 문제를 해결합니다. 모델에 정보를 가르치려고 하는 대신(비용도 많이 들고 실용적이지도 않음), 문서를 검색할 수 있는 능력을 제공합니다. 누군가 질문을 하면 시스템은 관련 정보를 찾아 프롬프트에 포함시킵니다. 모델은 그 검색된 문맥을 기반으로 답변합니다.

RAG를 모델에 참고 문헌 라이브러리를 주는 것으로 생각해 보세요. 질문할 때 시스템은:

1. **사용자 쿼리** - 질문을 합니다.
2. **임베딩** - 질문을 벡터로 변환합니다.
3. **벡터 검색** - 유사한 문서 청크를 찾습니다.
4. **문맥 조립** - 관련 청크를 프롬프트에 추가합니다.
5. **응답** - LLM이 문맥을 기반으로 답변을 생성합니다.

이것은 모델의 답변을 훈련 지식에만 의존하지 않고 실제 데이터에 기반하도록 합니다.

## Prerequisites

- [Module 00 - Quick Start](../00-quick-start/README.md) 완료 (위의 Easy RAG 예시 참고)
- [Module 01 - Introduction](../01-introduction/README.md) 완료 (Azure OpenAI 리소스 배포 포함, `text-embedding-3-small` 임베딩 모델 포함)
- `.env` 파일에 Azure 자격 증명 포함 (Module 01에서 `azd up`으로 생성됨)

> **참고:** Module 01을 완료하지 않았다면 먼저 배포 지침을 따르세요. `azd up` 커맨드는 이 모듈에서 사용하는 GPT 챗 모델과 임베딩 모델 모두를 배포합니다.

## Understanding RAG

아래 다이어그램은 핵심 개념을 보여줍니다: 모델의 훈련 데이터만 의존하지 않고, RAG는 모델이 답변 생성 전에 참조할 수 있도록 귀하의 문서 라이브러리를 제공합니다.

<img src="../../../translated_images/ko/what-is-rag.1f9005d44b07f2d8.webp" alt="무엇이 RAG인가" width="800"/>

*이 다이어그램은 표준 LLM(훈련 데이터로 추측)과 RAG가 포함된 LLM(문서를 우선 참조)이 어떻게 다른지 보여줍니다.*

사용자의 질문이 임베딩, 벡터 검색, 문맥 조립, 답변 생성의 네 단계로 흐르는 전체 연결 방식을 보여줍니다:

<img src="../../../translated_images/ko/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG 아키텍처" width="800"/>

*이 다이어그램은 RAG 파이프라인의 전체 흐름 - 사용자 쿼리가 임베딩, 벡터 검색, 문맥 조립, 답변 생성 단계를 거칩니다.*

이 모듈의 나머지 부분은 코드와 함께 각 단계를 자세히 다룹니다.

### Which RAG Approach Does This Tutorial Use?

LangChain4j는 세 가지 레벨의 추상화로 RAG를 구현하는 방법을 제공합니다. 아래 다이어그램은 세 가지 방법을 나란히 비교합니다:

<img src="../../../translated_images/ko/rag-approaches.5b97fdcc626f1447.webp" alt="LangChain4j의 세 가지 RAG 접근법" width="800"/>

*이 다이어그램은 Easy, Native, Advanced라는 LangChain4j의 세 가지 RAG 접근법의 주요 구성 요소와 활용 시점을 비교합니다.*

| 접근법 | 기능 | 트레이드오프 |
|---|---|---|
| **Easy RAG** | `AiServices`와 `ContentRetriever`를 통해 모든 과정을 자동으로 처리합니다. 인터페이스에 주석을 달고 검색기를 붙이면, LangChain4j가 임베딩, 검색, 프롬프트 조립을 모두 처리합니다. | 코드가 최소화되지만 각 단계가 어떻게 동작하는지 볼 수 없습니다. |
| **Native RAG** | 임베딩 모델 호출, 저장소 검색, 프롬프트 구축, 답변 생성을 직접 한 단계씩 명시적으로 수행합니다. | 코드량이 많지만 모든 단계가 보이고 수정 가능합니다. |
| **Advanced RAG** | `RetrievalAugmentor` 프레임워크를 사용해 쿼리 변환기, 라우터, 재정렬기, 콘텐츠 인젝터 등을 플러그인할 수 있는 생산 환경 파이프라인을 구성합니다. | 최대한 유연하지만 복잡도가 상당히 높습니다. |

**이 튜토리얼에서는 Native 접근법을 사용합니다.** RAG 파이프라인의 각 단계 — 쿼리 임베딩, 벡터 저장소 검색, 문맥 조립, 답변 생성 — 가 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) 에 명시적으로 작성되어 있습니다. 이것은 의도적입니다: 학습 목적상 코드를 최소화하는 것보다 각 단계를 직접 보고 이해하는 것이 더 중요하기 때문입니다. 구성 요소가 어떻게 맞춰지는지 익숙해지면 빠른 프로토타입을 위한 Easy RAG나 생산 시스템을 위한 Advanced RAG로 발전할 수 있습니다.

> **💡 Easy RAG를 이미 본 적 있나요?** [Quick Start 모듈](../00-quick-start/README.md)에는 Easy RAG 방식을 사용하는 문서 Q&A 예제([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java))가 포함되어 있습니다 — LangChain4j가 임베딩, 검색, 프롬프트 조립을 자동으로 처리합니다. 이 모듈은 그 파이프라인을 분해해 각 단계를 직접 제어하고 볼 수 있도록 한 단계 더 나아갑니다.

<img src="../../../translated_images/ko/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG 파이프라인 - LangChain4j" width="800"/>

*이 다이어그램은 `SimpleReaderDemo.java`의 Easy RAG 파이프라인을 보여줍니다. 이 모듈에서 사용하는 Native 접근법과 비교해 보세요: Easy RAG는 `AiServices`와 `ContentRetriever` 뒤에 임베딩, 검색, 프롬프트 조립을 숨깁니다 — 문서를 로드하고 검색기를 붙이면 답변을 받습니다. 이 모듈의 Native 접근법은 파이프라인을 분해해 각 단계를 직접 호출(임베딩, 검색, 문맥 조립, 생성)하여 전체 과정과 제어를 제공합니다.*

## How It Works

이 모듈의 RAG 파이프라인은 사용자가 질문할 때마다 순차적으로 실행되는 네 단계로 구성됩니다. 먼저 업로드된 문서를 **파싱하고 청크로 분할**합니다. 그런 다음 이 청크들을 **벡터 임베딩**으로 변환하여 수학적으로 비교할 수 있게 저장합니다. 쿼리가 들어오면 시스템은 관련성을 찾기 위해 **의미 검색**을 수행하고, 마지막으로 LLM에 문맥으로 전달하여 **답변 생성**을 합니다. 아래 섹션에서는 각 단계를 실제 코드와 다이어그램으로 살펴봅니다. 첫 번째 단계부터 시작해 보겠습니다.

### Document Processing

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

문서를 업로드하면 시스템이 문서(PDF 또는 텍스트)를 파싱하고 파일명 같은 메타데이터를 붙인 후, 모델의 문맥 창에 알맞게 문서를 여러 청크로 나눕니다. 청크들은 경계에서 문맥 손실이 없도록 약간 겹칩니다.

```java
// 업로드된 파일을 파싱하고 LangChain4j 문서로 래핑합니다
Document document = Document.from(content, metadata);

// 30토큰 중복을 포함하여 300토큰 단위로 분할합니다
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

아래 다이어그램은 시각적으로 작동 방식을 보여줍니다. 각 청크가 이웃 청크와 일부 토큰을 공유하는 것을 볼 수 있습니다 — 30토큰 겹침 덕분에 경계의 중요한 문맥이 끊기지 않습니다:

<img src="../../../translated_images/ko/document-chunking.a5df1dd1383431ed.webp" alt="문서 청크 분할" width="800"/>

*이 다이어그램은 문서를 300토큰 크기의 청크로 나누고, 30토큰씩 겹침하여 청크 경계에서 문맥을 유지하는 모습을 보여줍니다.*

> **🤖 GitHub Copilot [https://github.com/features/copilot]와 함께 시도해 보세요:** [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)를 열고 다음을 물어보세요:
> - "LangChain4j가 문서를 청크로 나누는 방법과 겹침이 중요한 이유는 무엇인가요?"
> - "문서 유형별 최적의 청크 크기는 얼마이며 그 이유는 무엇인가요?"
> - "다국어 문서나 특수 서식이 포함된 문서는 어떻게 처리하나요?"

### Creating Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

각 청크는 임베딩이라는 숫자 벡터 표현으로 변환됩니다 — 간단히 말해 의미를 숫자로 바꾸는 변환기입니다. 임베딩 모델은 챗 모델처럼 지능적이지 않습니다; 지시를 따르거나 추론하거나 질문에 답하지는 못합니다. 대신 비슷한 의미의 텍스트를 수학적 공간에서 가깝게 배치합니다 — "car"와 "automobile"이 가깝고, "refund policy"와 "return my money"도 가깝습니다. 챗 모델은 대화하는 상대라면, 임베딩 모델은 뛰어난 분류 시스템으로 생각하면 됩니다.

<img src="../../../translated_images/ko/embedding-model-concept.90760790c336a705.webp" alt="임베딩 모델 개념" width="800"/>

*이 다이어그램은 임베딩 모델이 텍스트를 수치 벡터로 변환하여 "car"와 "automobile" 같은 유사 의미를 벡터 공간에서 가깝게 위치시키는 과정을 보여줍니다.*

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```

아래 클래스 다이어그램은 RAG 파이프라인의 두 가지 플로우와 이를 구현하는 LangChain4j 클래스를 보여줍니다. **데이터 수집 플로우**(업로드 시 한번 실행)는 문서를 분할하고 청크를 임베딩해 `.addAll()`로 저장합니다. **쿼리 플로우**(사용자 질문 시마다 실행)는 질문을 임베딩하고 `.search()`로 저장소를 검색한 후 일치하는 문맥을 챗 모델에 전달합니다. 둘 다 공통 인터페이스인 `EmbeddingStore<TextSegment>`에서 만납니다:

<img src="../../../translated_images/ko/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG 클래스" width="800"/>

*이 다이어그램은 RAG 파이프라인의 두 플로우 — 데이터 수집과 쿼리 — 가 어떻게 공유 EmbeddingStore를 통해 연결되는지 보여줍니다.*

임베딩이 저장되고 나면 유사한 콘텐츠가 벡터 공간에서 자연스럽게 클러스터를 형성합니다. 아래 시각화는 관련 주제 문서들이 어떻게 가까운 위치에 분포해 의미 기반 검색이 가능한지를 보여줍니다:

<img src="../../../translated_images/ko/vector-embeddings.2ef7bdddac79a327.webp" alt="벡터 임베딩 공간" width="800"/>

*이 시각화는 관련 문서들이 3D 벡터 공간에서 클러스터를 이루는 모습을 보여줍니다. 기술 문서, 비즈니스 규칙, FAQ 등의 주제별로 뚜렷한 그룹을 형성합니다.*

사용자가 검색할 때 시스템은 네 단계를 따릅니다: 문서 임베딩을 한번 생성, 쿼리 임베딩, 모든 저장된 벡터와 코사인 유사도로 비교, 상위-K 청크 반환. 아래 다이어그램은 각 단계를 LangChain4j 클래스와 함께 설명합니다:

<img src="../../../translated_images/ko/embedding-search-steps.f54c907b3c5b4332.webp" alt="임베딩 검색 단계" width="800"/>

*이 다이어그램은 임베딩 검색의 4단계 과정을 보여줍니다: 문서 임베딩, 쿼리 임베딩, 코사인 유사도 계산, 상위-K 결과 반환.*

### Semantic Search

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

질문을 할 때 질문 자체도 임베딩이 됩니다. 시스템은 질문 임베딩과 문서 청크 임베딩을 비교해서 가장 의미가 비슷한 청크를 찾습니다 — 키워드 일치가 아닌 실제 의미 기반 유사성입니다.

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```

아래 다이어그램은 의미 검색과 전통적 키워드 검색을 비교합니다. "vehicle"에 대한 키워드 검색은 "cars and trucks"를 포함한 청크를 놓치지만, 의미 검색은 두 표현이 같은 뜻임을 인식해 높은 점수로 반환합니다:

<img src="../../../translated_images/ko/semantic-search.6b790f21c86b849d.webp" alt="의미 검색" width="800"/>

*이 다이어그램은 키워드 기반 검색과 의미 기반 검색을 비교하며, 의미 검색이 정확한 키워드가 달라도 개념적으로 연관된 내용을 찾아내는 방식을 보여줍니다.*

내부적으로 유사도는 코사인 유사도를 사용해 측정합니다 — "두 화살표가 같은 방향을 가리키는가?"를 묻는 것과 같습니다. 전혀 다른 단어를 써도 같은 의미면 벡터 방향이 같아서 1.0에 가까운 점수를 받습니다:

<img src="../../../translated_images/ko/cosine-similarity.9baeaf3fc3336abb.webp" alt="코사인 유사도" width="800"/>

*이 다이어그램은 임베딩 벡터 간의 코사인 유사도를 표시합니다 — 벡터가 더 일치할수록 1.0에 가까운 점수를 얻어 높은 의미 유사도를 나타냅니다.*
> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat으로 시도해보세요:** [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)를 열고 다음을 물어보세요:
> - "임베딩과 유사도 검색은 어떻게 작동하며 점수는 무엇에 의해 결정되나요?"
> - "어떤 유사도 임계값을 사용해야 하며 결과에 어떤 영향을 미치나요?"
> - "관련 문서가 없을 때는 어떻게 처리하나요?"

### 답변 생성

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

가장 관련성 높은 청크들이 명확한 지침, 검색된 컨텍스트, 사용자 질문을 포함하는 구조화된 프롬프트로 조합됩니다. 모델은 이 특정 청크들을 읽고 그 정보에 기반하여 답변을 생성합니다 — 모델이 앞에 있는 내용만 사용할 수 있어 허위 정보를 방지합니다.

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```

아래 다이어그램은 이 조립 과정을 보여줍니다 — 검색 단계에서 최고 점수를 받은 청크들이 프롬프트 템플릿에 삽입되고, `OpenAiOfficialChatModel`이 근거 있는 답변을 생성합니다:

<img src="../../../translated_images/ko/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*이 다이어그램은 최고 점수 청크들이 구조화된 프롬프트로 조합되어 모델이 데이터에서 근거 있는 답변을 생성하는 과정을 보여줍니다.*

## 애플리케이션 실행하기

**배포 확인:**

루트 디렉토리에 Azure 자격 증명이 포함된 `.env` 파일이 존재하는지 확인하세요 (모듈 01에서 생성됨):

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT가 표시되어야 합니다
```

**애플리케이션 시작하기:**

> **참고:** 만약 모듈 01에서 `./start-all.sh`로 모든 애플리케이션을 이미 시작했다면, 이 모듈은 포트 8081에서 이미 실행 중입니다. 아래 시작 명령은 건너뛰고 http://localhost:8081 로 바로 이동할 수 있습니다.

**옵션 1: Spring Boot 대시보드 사용 (VS Code 사용자 권장)**

개발 컨테이너에는 Spring Boot 대시보드 확장이 포함되어 있어, 모든 Spring Boot 애플리케이션을 시각적으로 관리할 수 있습니다. VS Code 왼쪽 활동 표시줄에서 Spring Boot 아이콘을 찾으세요.

Spring Boot 대시보드에서 할 수 있는 일:
- 워크스페이스 내 모든 Spring Boot 애플리케이션 확인
- 클릭 한 번으로 애플리케이션 시작/중지
- 실시간 애플리케이션 로그 보기
- 애플리케이션 상태 모니터링

"rag" 옆의 재생 버튼을 클릭해 모듈을 시작하거나 모든 모듈을 한 번에 시작하세요.

<img src="../../../translated_images/ko/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*이 스크린샷은 VS Code의 Spring Boot 대시보드를 보여주며, 여기서 애플리케이션을 시각적으로 시작, 중지 및 모니터링할 수 있습니다.*

**옵션 2: 셸 스크립트 사용**

웹 애플리케이션 모두 시작하기 (모듈 01-04):

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

혹은 이 모듈만 시작하기:

**Bash:**
```bash
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

두 스크립트 모두 루트 `.env` 파일에서 환경 변수를 자동으로 불러오며 JAR 파일이 없으면 빌드합니다.

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

브라우저에서 http://localhost:8081 를 엽니다.

**중지하기:**

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

이 애플리케이션은 문서 업로드와 질문을 위한 웹 인터페이스를 제공합니다.

<a href="images/rag-homepage.png"><img src="../../../translated_images/ko/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*이 스크린샷은 문서를 업로드하고 질문할 수 있는 RAG 애플리케이션 인터페이스를 보여줍니다.*

### 문서 업로드하기

먼저 문서를 업로드하세요 — 테스트용으로는 TXT 파일이 가장 좋습니다. 이 디렉토리에 제공된 `sample-document.txt` 파일에는 LangChain4j 기능, RAG 구현 및 모범 사례에 대한 정보가 들어 있어 시스템 테스트에 적합합니다.

시스템은 당신의 문서를 처리하여 여러 청크로 분할하고, 각 청크에 대한 임베딩을 생성합니다. 이 과정은 업로드 시 자동으로 수행됩니다.

### 질문하기

이제 문서 내용에 대해 구체적인 질문을 해보세요. 문서에 명확히 서술된 사실 기반 질문을 시도해 보세요. 시스템은 관련 청크를 검색하여 프롬프트에 포함시키고 답변을 생성합니다.

### 출처 참고 확인하기

각 답변에는 출처 참고와 유사도 점수가 포함되어 있습니다. 이 점수(0에서 1 사이)는 각 청크가 질문과 얼마나 관련이 있는지 나타냅니다. 점수가 높을수록 더 적합한 매칭입니다. 이를 통해 답변이 출처 자료와 일치하는지 검증할 수 있습니다.

<a href="images/rag-query-results.png"><img src="../../../translated_images/ko/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*이 스크린샷은 생성된 답변, 출처 참고 및 검색된 각 청크에 대한 관련성 점수를 포함한 조회 결과를 보여줍니다.*

### 다양한 질문 시도하기

다양한 유형의 질문을 시도해 보세요:
- 구체적 사실: "주요 주제는 무엇인가요?"
- 비교: "X와 Y의 차이점은 무엇인가요?"
- 요약: "Z에 관한 핵심 내용을 요약해주세요."

질문이 문서 내용과 얼마나 잘 맞는지에 따라 관련성 점수가 어떻게 변하는지 관찰하세요.

## 핵심 개념

### 청크 분할 전략

문서는 300토큰 길이의 청크로 나누며, 30토큰씩 겹치도록 합니다. 이 균형은 각 청크가 의미 있는 맥락을 유지하면서도 여러 청크를 프롬프트에 포함할 수 있도록 충분히 작게 유지함을 보장합니다.

### 유사도 점수

검색된 각 청크에는 사용자 질문과 얼마나 밀접한지 나타내는 0에서 1 사이의 유사도 점수가 부여됩니다. 아래 다이어그램은 점수 범위와 시스템이 이를 사용해 결과를 필터링하는 방법을 시각화합니다:

<img src="../../../translated_images/ko/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*이 다이어그램은 0에서 1까지 점수 범위와 관련 없는 청크를 제거하는 0.5의 최소 임계값을 보여줍니다.*

점수 범위:
- 0.7-1.0: 매우 관련성 높음, 정확한 매칭
- 0.5-0.7: 관련성 있음, 좋은 맥락
- 0.5 미만: 필터링됨, 너무 이질적

시스템은 품질 보장을 위해 최소 임계값 이상의 청크만 검색합니다.

임베딩은 의미가 명확히 군집될 때 잘 작동하지만, 한계도 있습니다. 아래 그림은 일반적인 실패 유형을 보여줍니다 — 너무 큰 청크는 벡터가 불명확해지고, 너무 작은 청크는 맥락이 부족하며, 모호한 용어는 여러 군집을 가리키고, 정확 매칭 조회(ID, 부품번호 등)는 임베딩으로 전혀 작동하지 않습니다:

<img src="../../../translated_images/ko/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*이 그림은 임베딩에서 발생하는 일반적인 실패 유형을 보여줍니다: 너무 큰 청크, 너무 작은 청크, 여러 군집을 가리키는 모호한 용어, ID 같은 정확 매칭 조회.*

### 인메모리 저장

이 모듈은 단순성을 위해 인메모리 저장소를 사용합니다. 애플리케이션을 재시작하면 업로드된 문서는 사라집니다. 실제 서비스에서는 Qdrant 또는 Azure AI Search 같은 영속 벡터 데이터베이스를 사용합니다.

### 컨텍스트 윈도우 관리

각 모델은 최대 컨텍스트 윈도우가 제한됩니다. 큰 문서의 모든 청크를 포함할 수 없습니다. 시스템은 상위 N개 관련 청크(기본값 5개)를 검색하여 한도를 유지하면서도 정확한 답변을 위한 충분한 맥락을 제공합니다.

## RAG가 필요한 경우

RAG가 항상 최선의 접근법은 아닙니다. 아래 결정 가이드는 RAG가 가치를 더하는 경우와, 단순히 콘텐츠를 프롬프트에 포함하거나 모델 내장 지식에 의존하는 것이 충분한 경우를 구분하는 데 도움을 줍니다:

<img src="../../../translated_images/ko/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*이 다이어그램은 RAG가 가치를 더하는 경우와 단순한 접근이 충분한 경우를 구분하는 결정 가이드를 보여줍니다.*

**RAG를 사용하세요:**
- 독점 문서에 대한 질문에 답해야 할 때
- 정보가 자주 변경될 때 (정책, 가격, 사양 등)
- 정확성에 출처 명시가 필요할 때
- 콘텐츠가 너무 커서 단일 프롬프트에 넣기 어려울 때
- 검증 가능하고 근거 있는 답변이 필요할 때

**RAG를 사용하지 마세요:**
- 질문이 모델이 이미 알고 있는 일반 지식을 요구할 때
- 실시간 데이터가 필요할 때 (RAG는 업로드된 문서 기반)
- 콘텐츠가 충분히 작아 프롬프트에 직접 포함 가능할 때

## 다음 단계

**다음 모듈:** [04-tools - AI 도구 에이전트](../04-tools/README.md)

---

**네비게이션:** [← 이전: 모듈 02 - 프롬프트 엔지니어링](../02-prompt-engineering/README.md) | [메인으로 돌아가기](../README.md) | [다음: 모듈 04 - 도구 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
본 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 최대한 정확성을 기하고자 노력하였으나, 자동 번역에는 오류나 부정확성이 포함될 수 있음을 유의하시기 바랍니다. 원문이 작성된 원어가 권위 있는 출처로 간주되어야 합니다. 중요한 정보에 대해서는 전문가의 인간 번역을 권장합니다. 본 번역 사용으로 인해 발생하는 오해나 해석상의 문제에 대해서는 당사가 책임지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->