# 모듈 03: RAG (검색 증강 생성)

## 목차

- [비디오 워크스루](../../../03-rag)
- [배우게 될 내용](../../../03-rag)
- [사전 조건](../../../03-rag)
- [RAG 이해하기](../../../03-rag)
  - [이 튜토리얼에서 사용하는 RAG 접근법은?](../../../03-rag)
- [작동 원리](../../../03-rag)
  - [문서 처리](../../../03-rag)
  - [임베딩 생성](../../../03-rag)
  - [의미 기반 검색](../../../03-rag)
  - [답변 생성](../../../03-rag)
- [애플리케이션 실행하기](../../../03-rag)
- [애플리케이션 사용법](../../../03-rag)
  - [문서 업로드](../../../03-rag)
  - [질문하기](../../../03-rag)
  - [출처 확인하기](../../../03-rag)
  - [질문 실험하기](../../../03-rag)
- [주요 개념](../../../03-rag)
  - [청킹 전략](../../../03-rag)
  - [유사도 점수](../../../03-rag)
  - [인메모리 저장소](../../../03-rag)
  - [컨텍스트 윈도우 관리](../../../03-rag)
- [RAG가 필요한 경우](../../../03-rag)
- [다음 단계](../../../03-rag)

## 비디오 워크스루

이 모듈을 시작하는 방법을 설명하는 라이브 세션을 시청하세요:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## 배우게 될 내용

이전 모듈들에서는 AI와 대화하는 방법과 효과적인 프롬프트 구조화 방법을 배웠습니다. 하지만 근본적인 한계가 있습니다: 언어 모델은 훈련 기간 동안 배운 내용만 알고 있습니다. 회사 정책, 프로젝트 문서 또는 훈련받지 않은 정보에 대한 질문에는 답할 수 없습니다.

RAG(검색 증강 생성)는 이 문제를 해결합니다. 모델에게 정보를 가르치는 대신(비용과 현실성이 떨어짐), 문서를 검색할 수 있는 능력을 제공합니다. 누군가 질문하면 시스템이 관련 정보를 찾아 프롬프트에 포함시킵니다. 모델은 그렇게 가져온 문맥을 기반으로 답변합니다.

RAG를 모델에게 참고 도서관을 주는 것으로 생각하세요. 질문할 때 시스템은:

1. **사용자 쿼리** - 질문을 합니다  
2. **임베딩** - 질문을 벡터로 변환합니다  
3. **벡터 검색** - 유사한 문서 청크를 찾습니다  
4. **문맥 조립** - 관련 청크를 프롬프트에 추가합니다  
5. **응답** - LLM이 문맥을 바탕으로 답변을 생성합니다

이렇게 해서 모델의 답변이 훈련 지식에 의존하거나 답을 만들어내는 대신 실제 데이터에 근거를 두게 됩니다.

## 사전 조건

- [모듈 00 - 빠른 시작](../00-quick-start/README.md) 완료 (위에서 언급한 Easy RAG 예제용)  
- [모듈 01 - 소개](../01-introduction/README.md) 완료 (Azure OpenAI 리소스 배포 완료, `text-embedding-3-small` 임베딩 모델 포함)  
- 루트 디렉터리에 Azure 인증 정보가 담긴 `.env` 파일 (모듈 01에서 `azd up` 명령어로 생성됨)

> **참고:** 모듈 01을 완료하지 않았다면 먼저 해당 배포 지침을 따르세요. `azd up`은 이 모듈에서 사용하는 GPT 챗 모델과 임베딩 모델을 모두 배포합니다.

## RAG 이해하기

아래 다이어그램은 핵심 개념을 보여줍니다: 모델의 훈련 데이터만 의존하는 대신, RAG는 답변 생성 전에 문서 참고 도서관을 제공합니다.

<img src="../../../translated_images/ko/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*이 그림은 일반 LLM(훈련 데이터에서 추론)과 RAG-강화 LLM(먼저 문서를 참고)의 차이를 보여줍니다.*

사용자 질문이 임베딩, 벡터 검색, 문맥 조립, 답변 생성의 네 단계로 흐르는 연결 방식을 보여줍니다:

<img src="../../../translated_images/ko/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*이 그림은 사용자 쿼리가 임베딩, 벡터 검색, 문맥 조립, 답변 생성 단계를 거치는 RAG 파이프라인을 나타냅니다.*

이 모듈의 나머지 부분에서는 각 단계를 실제 코드와 함께 자세히 설명합니다.

### 이 튜토리얼에서 사용하는 RAG 접근법은?

LangChain4j는 세 가지 RAG 구현 방식을 제공합니다. 아래 그림은 각각의 추상화 수준을 비교합니다:

<img src="../../../translated_images/ko/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*이 다이어그램은 LangChain4j의 세 가지 RAG 방식 — Easy, Native, Advanced — 주요 구성 요소와 사용 시점을 비교합니다.*

| 접근법 | 내용 | 트레이드오프 |
|---|---|---|
| **Easy RAG** | `AiServices`와 `ContentRetriever`로 모든 것을 자동 연결합니다. 인터페이스를 주석 처리하고 리트리버를 붙이면 LangChain4j가 임베딩, 검색, 프롬프트 조립을 자동 처리합니다. | 코드가 최소화되지만 각 단계가 어떻게 작동하는지 볼 수 없습니다. |
| **Native RAG** | 임베딩 모델 호출, 저장소 검색, 프롬프트 빌드, 답변 생성 단계를 직접 하나씩 실행합니다. | 더 많은 코드가 필요하지만 모든 단계가 명확하게 보이고 수정 가능합니다. |
| **Advanced RAG** | 쿼리 변환기, 라우터, 재순위자, 콘텐츠 인젝터 등 플러그형 컴포넌트가 있는 `RetrievalAugmentor` 프레임워크를 사용해 프로덕션 수준 파이프라인을 만듭니다. | 최대 유연성 제공, 그러나 상당히 복잡함. |

**이 튜토리얼은 Native 방식을 사용합니다.** 임베딩, 벡터 저장소 검색, 문맥 조립, 답변 생성 각 단계가 [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)에 명시적으로 작성되어 있습니다. 학습 자료로서 단계별 이해가 우선시되기 때문입니다. 어느 정도 익숙해지면 빠른 프로토타입용 Easy RAG나 프로덕션용 Advanced RAG로 넘어갈 수 있습니다.

> **💡 Easy RAG를 이미 봤나요?** [빠른 시작 모듈](../00-quick-start/README.md)에는 Easy RAG 방식을 사용한 문서 Q&A 예제([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java))가 포함되어 있습니다. LangChain4j가 임베딩, 검색, 프롬프트 조립을 자동으로 처리합니다. 이 모듈은 그 파이프라인을 분해해 각 단계를 직접 보고 제어할 수 있게 합니다.

<img src="../../../translated_images/ko/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*이 다이어그램은 `SimpleReaderDemo.java`의 Easy RAG 파이프라인을 보여줍니다. 본 모듈에서 사용하는 Native 방식과 비교해보세요: Easy RAG는 `AiServices`와 `ContentRetriever` 뒤에서 임베딩, 검색, 프롬프트 조립을 숨깁니다 — 문서를 로드하고 리트리버를 붙이면 답변을 받습니다. Native 방식은 각 단계를 직접 호출해 완전한 가시성과 제어를 제공합니다.*

## 작동 원리

이 모듈의 RAG 파이프라인은 사용자 질문마다 순차적으로 실행되는 네 단계로 나뉩니다. 먼저, 업로드된 문서를 **파싱하고 청킹**하여 다루기 쉬운 조각들로 나눕니다. 그런 다음 각 청크를 **벡터 임베딩**으로 변환해 수학적으로 비교할 수 있도록 저장합니다. 쿼리가 오면 **의미 검색**을 통해 가장 관련성이 높은 청크를 찾아내고, 이를 문맥으로 LLM에 전달해 **답변 생성**을 합니다. 아래 각 단계를 코드와 다이어그램으로 설명합니다. 첫 번째 단계부터 살펴봅시다.

### 문서 처리

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

문서를 업로드하면 시스템이 파서(pdf 또는 일반 텍스트)를 이용해 문서를 파싱하고, 파일명과 같은 메타데이터를 붙입니다. 그 뒤 모델 컨텍스트 창에 적합한 크기로 청크로 분할하며, 경계에서 문맥 손실을 방지하기 위해 청크들이 일부 중첩됩니다.

```java
// 업로드된 파일을 파싱하고 LangChain4j 문서에 래핑합니다
Document document = Document.from(content, metadata);

// 30토큰 중첩으로 300토큰 청크로 분할합니다
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

아래 다이어그램은 이를 시각적으로 보여줍니다. 각 청크가 인접 청크들과 일부 토큰을 공유하는데, 30토큰 중첩을 통해 중요한 문맥이 끊기지 않습니다:

<img src="../../../translated_images/ko/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*이 그림은 문서를 300토큰 단위로 나누며 30토큰씩 중첩하여 청크 경계에서 문맥이 유지되는 과정을 보여줍니다.*

> **🤖 GitHub Copilot Chat으로 시도해보세요:** [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)를 열고 질문해보세요:  
> - "LangChain4j가 문서를 어떻게 청크로 나누며 왜 중첩이 중요한가요?"  
> - "문서 유형별 최적의 청크 크기는 얼마이고 이유는 무엇인가요?"  
> - "다국어 문서나 특수 서식 문서는 어떻게 처리하나요?"

### 임베딩 생성

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

각 청크는 임베딩이라는 수치적 표현으로 변환됩니다 — 본질적으로 의미를 숫자로 바꾸는 작업입니다. 임베딩 모델은 챗 모델과 달리 "지능적"이지 않습니다; 지시를 따르거나 추론하거나 직접 답변하지는 못합니다. 대신 의미가 비슷한 텍스트가 수학적 공간에서 서로 가깝도록 매핑합니다 — 예를 들어 "car"와 "automobile"이 가깝고, "refund policy"와 "return my money"가 가깝습니다. 챗 모델이 대화 상대라면, 임베딩 모델은 뛰어난 분류 체계라고 생각하세요.

<img src="../../../translated_images/ko/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*이 다이어그램은 임베딩 모델이 텍스트를 숫자 벡터로 변환해 비슷한 의미의 단어들을 벡터 공간에서 가깝게 배치하는 과정을 보여줍니다.*

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

아래 클래스 다이어그램은 RAG 파이프라인 내 두 개의 흐름과 이를 구현하는 LangChain4j 클래스를 보여줍니다.  
**입력 흐름**(업로드 시 한 번 실행)은 문서를 분할, 청크 임베딩 후 `.addAll()`로 저장합니다.  
**쿼리 흐름**(사용자 질문 시 매번 실행)은 질문을 임베딩하고 저장소를 `.search()`로 검색해 일치하는 문맥을 챗 모델로 전달합니다. 두 흐름 모두 `EmbeddingStore<TextSegment>` 인터페이스를 공유합니다:

<img src="../../../translated_images/ko/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*이 그림은 RAG 파이프라인의 두 흐름 — 입력과 쿼리 — 및 공유 EmbeddingStore를 통해 연결되는 방식을 보여줍니다.*

임베딩이 저장되면, 비슷한 내용이 벡터 공간에서 자연스럽게 클러스터를 형성합니다. 아래 시각화는 관련 주제 문서들이 가까운 점들로 모이는 모습을 보여줍니다. 이것이 의미 기반 검색이 가능한 이유입니다:

<img src="../../../translated_images/ko/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*이 시각화는 기술 문서, 비즈니스 규칙, FAQ 같은 관련 문서들이 3D 벡터 공간에서 각기 다른 군집을 형성하는 모습을 보여줍니다.*

사용자가 검색할 때 시스템은 네 단계를 거칩니다: 문서 임베딩 (한 번), 질문 임베딩 (검색마다), 코사인 유사도로 쿼리 벡터를 저장된 모든 벡터와 비교, 상위 K개의 청크 반환. 아래 다이어그램은 각 단계와 관련 LangChain4j 클래스를 설명합니다:

<img src="../../../translated_images/ko/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*이 그림은 임베딩 검색 과정 네 단계를 보여줍니다: 문서 임베딩, 질문 임베딩, 코사인 유사도 비교, 상위 결과 반환.*

### 의미 기반 검색

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

질문을 할 때, 질문도 임베딩으로 변환됩니다. 시스템은 이 질문 임베딩을 모든 문서 청크 임베딩과 비교합니다. 키워드 매칭이 아닌 실제 의미가 가장 유사한 청크를 찾습니다.

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

아래 다이어그램은 의미 기반 검색과 전통적인 키워드 검색을 대비합니다. "vehicle" 키워드 검색은 "cars and trucks" 관련 청크를 놓치지만, 의미 검색은 두 표현이 같은 의미임을 이해해 높은 점수를 줍니다:

<img src="../../../translated_images/ko/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*이 그림은 키워드 기반 검색과 의미 기반 검색을 비교하며, 의미 검색이 키워드가 달라도 개념적으로 관련된 내용을 가져오는 방식을 보여줍니다.*

내부적으로 유사도는 코사인 유사도로 측정합니다 — 기본적으로 "두 화살표가 같은 방향을 가리키는가?"를 묻는 것입니다. 완전히 다른 단어를 써도 의미가 같으면 벡터가 같은 방향을 가리키고 점수는 1.0에 가까워집니다:

<img src="../../../translated_images/ko/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>
*이 다이어그램은 임베딩 벡터 간의 각도로서 코사인 유사도를 보여줍니다 — 더 정렬된 벡터는 1.0에 가까운 점수를 받아 더 높은 의미적 유사성을 나타냅니다.*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat으로 시도해보세요:** [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)를 열고 다음을 물어보세요:
> - "임베딩을 사용한 유사도 검색은 어떻게 작동하며 점수는 어떻게 결정되나요?"
> - "어떤 유사도 임계값을 사용해야 하며 결과에 어떻게 영향을 미치나요?"
> - "관련 문서를 찾지 못했을 때는 어떻게 처리하나요?"

### 답변 생성

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

가장 관련성 높은 청크들을 명확한 지침, 검색된 컨텍스트, 그리고 사용자의 질문을 포함하는 구조화된 프롬프트로 조립합니다. 모델은 해당 특정 청크들을 읽고 그 정보에 기반해 답변을 생성하며 — 현재 주어진 정보만을 사용하므로 환각을 방지할 수 있습니다.

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

아래 다이어그램은 이 조립 과정을 보여줍니다 — 검색 단계에서 가장 높은 점수를 받은 청크들이 프롬프트 템플릿에 주입되고, `OpenAiOfficialChatModel`이 근거 있는 답변을 생성합니다:

<img src="../../../translated_images/ko/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*이 다이어그램은 상위 점수 청크가 구조화된 프롬프트로 조립되어 모델이 귀하의 데이터에서 근거 있는 답변을 생성하는 방식을 보여줍니다.*

## 애플리케이션 실행

**배포 확인:**

루트 디렉터리에 Azure 자격 증명을 포함하는 `.env` 파일이 존재하는지 확인하세요 (모듈 01에서 생성됨):

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT을 보여야 합니다
```

**애플리케이션 시작:**

> **참고:** 모듈 01에서 `./start-all.sh`를 사용해 모든 애플리케이션을 이미 시작한 경우, 이 모듈은 포트 8081에서 이미 실행 중입니다. 아래 시작 명령은 건너뛰고 http://localhost:8081 로 바로 이동할 수 있습니다.

**옵션 1: Spring Boot 대시보드 사용 (VS Code 사용자에게 권장)**

개발 컨테이너에는 Spring Boot 대시보드 확장이 포함되어 있으며, 모든 Spring Boot 애플리케이션을 시각적으로 관리할 수 있습니다. VS Code 왼쪽의 활동 표시줄에서 Spring Boot 아이콘을 찾아보세요.

Spring Boot 대시보드에서 다음이 가능합니다:
- 작업 공간 내 모든 Spring Boot 애플리케이션 보기
- 클릭 한 번으로 애플리케이션 시작/중지
- 실시간 애플리케이션 로그 보기
- 애플리케이션 상태 모니터링

"rag" 옆의 재생 버튼을 클릭해 이 모듈을 시작하거나 모든 모듈을 한 번에 시작할 수 있습니다.

<img src="../../../translated_images/ko/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*이 스크린샷은 VS Code의 Spring Boot 대시보드를 보여주며, 여기서 애플리케이션을 시각적으로 시작, 중지 및 모니터링할 수 있습니다.*

**옵션 2: 셸 스크립트 사용**

모든 웹 애플리케이션(모듈 01~04)을 시작:

**Bash:**
```bash
cd ..  # 루트 디렉토리에서
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # 루트 디렉터리에서부터
.\start-all.ps1
```

또는 이 모듈만 시작:

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

두 스크립트 모두 루트 `.env` 파일에서 환경 변수를 자동으로 불러오며, JAR 파일이 없으면 빌드합니다.

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

브라우저에서 http://localhost:8081 을 엽니다.

**종료하려면:**

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

*이 스크린샷은 RAG 애플리케이션 인터페이스를 보여주며, 여기서 문서를 업로드하고 질문할 수 있습니다.*

### 문서 업로드

먼저 문서를 업로드하세요 — 테스트용으로는 TXT 파일이 가장 적합합니다. 이 디렉터리에는 LangChain4j 기능, RAG 구현 및 모범 사례에 관한 정보를 담은 `sample-document.txt`가 제공되어 있어 시스템 테스트에 적합합니다.

시스템은 문서를 처리하여 청크로 나누고, 각 청크별 임베딩을 생성합니다. 이는 업로드 시 자동으로 처리됩니다.

### 질문하기

문서 내용에 대해 구체적인 질문을 해보세요. 문서에 명확히 명시된 사실적인 내용을 물어보는 것이 좋습니다. 시스템은 관련 청크를 검색해 프롬프트에 포함하고 답변을 생성합니다.

### 출처 참고 확인

각 답변에는 출처 참조와 유사도 점수가 포함되어 있습니다. 이 점수는 (0부터 1 사이) 각 청크가 질문과 얼마나 관련성 있는지를 나타냅니다. 점수가 높을수록 더 나은 일치입니다. 이를 통해 답변을 출처 자료와 대조해 검증할 수 있습니다.

<a href="images/rag-query-results.png"><img src="../../../translated_images/ko/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*이 스크린샷은 생성된 답변, 출처 참조, 그리고 검색된 각 청크에 대한 관련성 점수가 포함된 질의 결과를 보여줍니다.*

### 질문으로 실험하기

다양한 유형의 질문을 시도해보세요:
- 구체적인 사실: "주요 주제는 무엇인가?"
- 비교: "X와 Y의 차이점은 무엇인가?"
- 요약: "Z에 관한 핵심 내용을 요약해줘"

질문이 문서 내용과 얼마나 잘 맞는지에 따라 관련성 점수가 어떻게 변하는지 확인할 수 있습니다.

## 주요 개념

### 청크 분할 전략

문서는 300 토큰 단위 청크로 나뉘며, 청크 간 30 토큰이 겹칩니다. 이 균형은 각 청크가 의미 있는 충분한 문맥을 가지면서도 프롬프트에 여러 청크를 포함할 수 있도록 충분히 작게 유지되도록 합니다.

### 유사도 점수

검색된 각 청크는 사용자의 질문과 얼마나 밀접하게 일치하는지를 나타내는 0에서 1 사이의 유사도 점수를 가지고 있습니다. 아래 다이어그램은 점수 범위와 시스템이 결과 필터링에 이를 어떻게 사용하는지 시각화합니다:

<img src="../../../translated_images/ko/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*이 다이어그램은 0부터 1까지 점수 범위를 보여주며, 0.5의 최소 임계값으로 관련 없는 청크를 필터링합니다.*

점수 범위는 다음과 같습니다:
- 0.7-1.0: 매우 관련성 높음, 정확한 일치
- 0.5-0.7: 관련성 있음, 좋은 문맥 제공
- 0.5 미만: 필터링, 너무 달라서 제외

시스템은 품질 유지를 위해 최소 임계값 이상인 청크만 검색합니다.

임베딩은 의미가 명료하게 클러스터링될 때 잘 작동하지만 맹점도 존재합니다. 아래 다이어그램은 일반적인 실패 모드를 보여줍니다 — 청크가 너무 크면 벡터가 혼잡해지고, 너무 작으면 문맥 부족, 모호한 용어는 여러 클러스터를 가리키며, 정확한 일치 조회(아이디, 부품 번호 등)는 임베딩에서 전혀 작동하지 않습니다:

<img src="../../../translated_images/ko/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*이 다이어그램은 일반적인 임베딩 실패 모드를 보여줍니다: 청크가 너무 크거나, 너무 작거나, 여러 클러스터를 가리키는 모호한 용어, 그리고 아이디 같은 정확한 일치 조회.*

### 인메모리 저장소

이 모듈은 간단히 하기 위해 인메모리 저장소를 사용합니다. 애플리케이션을 재시작하면 업로드된 문서가 사라집니다. 실제 서비스에서는 Qdrant나 Azure AI Search 같은 영구 벡터 데이터베이스를 활용합니다.

### 컨텍스트 윈도우 관리

각 모델은 최대 컨텍스트 윈도우 크기가 정해져 있습니다. 큰 문서의 모든 청크를 포함할 수 없습니다. 시스템은 제한 내에 머물면서 정확한 답변에 충분한 문맥을 제공하기 위해 가장 관련성 높은 상위 N개의 청크(기본값 5개)를 검색합니다.

## RAG가 필요한 경우

RAG가 항상 최선의 방법은 아닙니다. 아래 의사결정 가이드는 RAG가 가치를 더하는 경우와, 프롬프트에 직접 콘텐츠를 포함시키거나 모델 내장 지식에 의존하는 더 간단한 접근법이 좋은 경우를 구분하는 데 도움을 줍니다:

<img src="../../../translated_images/ko/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*이 다이어그램은 RAG가 가치를 추가하는 경우와 더 단순한 접근법이 충분한 경우를 보여주는 의사결정 가이드입니다.*

**RAG를 사용하는 경우:**
- 독점 문서에 관한 질문에 답해야 할 때
- 정보가 자주 변경되는 경우(정책, 가격, 사양)
- 답변의 정확성에 출처 표시가 필요한 경우
- 콘텐츠가 단일 프롬프트에 담기에는 너무 클 때
- 검증 가능하고 근거 있는 응답이 필요할 때

**RAG를 사용하지 않는 경우:**
- 질문이 모델이 이미 알고 있는 일반 지식에 기반할 때
- 실시간 데이터가 필요할 때(RAG는 업로드된 문서에서 작동)
- 콘텐츠가 프롬프트에 직접 포함될 만큼 작을 때

## 다음 단계

**다음 모듈:** [04-tools - 도구를 사용하는 AI 에이전트](../04-tools/README.md)

---

**내비게이션:** [← 이전: 모듈 02 - 프롬프트 엔지니어링](../02-prompt-engineering/README.md) | [메인으로 돌아가기](../README.md) | [다음: 모듈 04 - 도구 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다하고 있으나, 자동 번역은 오류나 부정확함이 포함될 수 있음을 유의하시기 바랍니다. 원문 문서는 권위 있는 출처로 간주되어야 합니다. 중요한 정보의 경우 전문 인력에 의한 번역을 권장합니다. 본 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->