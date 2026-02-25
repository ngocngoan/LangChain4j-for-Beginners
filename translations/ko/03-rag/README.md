# Module 03: RAG (검색 증강 생성)

## 목차

- [배울 내용](../../../03-rag)
- [RAG 이해하기](../../../03-rag)
- [필수 조건](../../../03-rag)
- [작동 원리](../../../03-rag)
  - [문서 처리](../../../03-rag)
  - [임베딩 생성](../../../03-rag)
  - [의미 기반 검색](../../../03-rag)
  - [응답 생성](../../../03-rag)
- [애플리케이션 실행하기](../../../03-rag)
- [애플리케이션 사용법](../../../03-rag)
  - [문서 업로드](../../../03-rag)
  - [질문하기](../../../03-rag)
  - [출처 확인하기](../../../03-rag)
  - [질문 실험하기](../../../03-rag)
- [핵심 개념](../../../03-rag)
  - [청킹 전략](../../../03-rag)
  - [유사도 점수](../../../03-rag)
  - [인메모리 저장](../../../03-rag)
  - [컨텍스트 창 관리](../../../03-rag)
- [언제 RAG가 중요한가](../../../03-rag)
- [다음 단계](../../../03-rag)

## 배울 내용

이전 모듈에서 AI와 대화하는 법과 프롬프트를 효과적으로 구성하는 방법을 배웠습니다. 하지만 기본적인 한계가 있습니다: 언어 모델은 학습 중에 배운 내용만 알고 있습니다. 회사 정책이나 프로젝트 문서, 학습되지 않은 정보에 대해 질문에 답할 수 없습니다.

RAG(검색 증강 생성)는 이 문제를 해결합니다. 모델에게 직접 정보를 가르치기(비용도 많이 들고 실용적이지 않음) 대신, 문서를 검색할 수 있는 능력을 제공합니다. 누군가 질문하면 시스템이 관련 정보를 찾아 프롬프트에 포함시키고, 모델은 그 검색된 컨텍스트를 바탕으로 응답합니다.

RAG는 모델에 참고 도서를 주는 것과 같습니다. 질문하면 시스템은 다음 단계를 거칩니다:

1. **사용자 질문** - 질문하기  
2. **임베딩** - 질문을 벡터로 변환  
3. **벡터 검색** - 유사한 문서 청크 찾기  
4. **컨텍스트 조립** - 관련 청크를 프롬프트에 추가  
5. **응답** - LLM이 컨텍스트 기반으로 답변 생성  

이 방식은 모델 응답을 훈련된 지식에 의존하거나 허구를 만들어내는 대신 실제 데이터에 기반하도록 만듭니다.

## RAG 이해하기

아래 다이어그램은 핵심 개념을 보여줍니다: 단순히 모델 학습 데이터에만 의존하는 대신, RAG는 매 답변 생성 전에 참고할 수 있는 문서 라이브러리를 제공합니다.

<img src="../../../translated_images/ko/what-is-rag.1f9005d44b07f2d8.webp" alt="RAG란 무엇인가" width="800"/>

사용자의 질문은 임베딩, 벡터 검색, 컨텍스트 조립, 답변 생성의 네 단계로 흐르며, 각 단계는 이전 단계를 기반으로 합니다:

<img src="../../../translated_images/ko/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG 아키텍처" width="800"/>

이 모듈의 나머지 부분에서 각 단계를 코드와 함께 자세히 살펴봅니다.

## 필수 조건

- 모듈 01 완료(배포된 Azure OpenAI 리소스)  
- 루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일(모듈 01의 `azd up`으로 생성됨)  

> **참고:** 모듈 01을 완료하지 않았다면, 먼저 그곳의 배포 지침을 따라주세요.

## 작동 원리

### 문서 처리

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

문서를 업로드하면 시스템이 PDF나 일반 텍스트를 파싱하고, 파일 이름 같은 메타데이터를 붙입니다. 이후 문서를 청크로 나누는데, 청크는 모델 컨텍스트 창에 알맞게 작게 쪼갠 조각입니다. 청크 간에 약간 겹치는 토큰이 있어 경계에서 중요한 정보가 손실되지 않습니다.

```java
// 업로드된 파일을 파싱하고 LangChain4j 문서로 래핑합니다
Document document = Document.from(content, metadata);

// 30토큰 중첩으로 300토큰 청크로 분할합니다
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
아래 다이어그램은 이 과정을 시각적으로 보여줍니다. 각 청크가 이웃하는 청크와 일부 토큰을 공유하며, 30토큰 겹침을 통해 경계 사이에 중요한 맥락이 빠지지 않도록 합니다:

<img src="../../../translated_images/ko/document-chunking.a5df1dd1383431ed.webp" alt="문서 청킹" width="800"/>

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅과 함께 시도해보세요:** [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)를 열고 다음 질문을 해보세요:  
> - "LangChain4j는 문서를 어떻게 청크로 나누며, 왜 겹침이 중요한가?"  
> - "문서 유형마다 최적의 청크 크기는 얼마이며 그 이유는 무엇인가?"  
> - "다국어 문서나 특수 포맷 문서는 어떻게 처리해야 하나?"  

### 임베딩 생성

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

각 청크는 임베딩이라는 수치 표현으로 변환됩니다. 임베딩은 텍스트 의미를 포착하는 수학적 지문과 같으며, 의미가 비슷한 텍스트는 비슷한 임베딩을 만듭니다.

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
  
아래 클래스 다이어그램은 LangChain4j 구성 요소가 어떻게 연결되는지 보여줍니다. `OpenAiOfficialEmbeddingModel`이 텍스트를 벡터로 변환하고, `InMemoryEmbeddingStore`는 원본 `TextSegment` 데이터와 함께 벡터를 저장하며, `EmbeddingSearchRequest`는 `maxResults`, `minScore` 같은 검색 파라미터를 제어합니다:

<img src="../../../translated_images/ko/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG 클래스" width="800"/>

임베딩이 저장되면 유사한 내용이 벡터 공간에서 자연스럽게 군집됩니다. 아래 시각화는 연관 주제 문서가 인접한 점으로 나타나는 모습을 보여주며, 이것이 의미 기반 검색이 가능한 이유입니다:

<img src="../../../translated_images/ko/vector-embeddings.2ef7bdddac79a327.webp" alt="벡터 임베딩 공간" width="800"/>

### 의미 기반 검색

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

질문을 하면 질문 또한 임베딩으로 변환됩니다. 시스템은 질문 임베딩과 문서 청크 임베딩 전체를 비교하여 의미가 가장 비슷한 청크를 찾아냅니다. 단순 키워드 일치가 아니라 실제 의미 유사성으로 검색합니다.

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
  
아래 다이어그램은 의미 기반 검색과 전통 키워드 검색 차이를 보여줍니다. 키워드 검색으로 "vehicle"을 찾으면 "cars and trucks"가 포함된 청크를 놓치지만, 의미 기반 검색은 같은 뜻임을 이해해 높은 점수의 일치로 반환합니다:

<img src="../../../translated_images/ko/semantic-search.6b790f21c86b849d.webp" alt="의미 기반 검색" width="800"/>

> **🤖 [GitHub Copilot](https://github.com/features/copilot) 채팅과 함께 시도해보세요:** [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)를 열고 다음 질문을 해보세요:  
> - "임베딩을 활용한 유사도 검색은 어떻게 작동하며, 점수는 어떻게 결정되나?"  
> - "어떤 유사도 임계값을 써야 하며 결과에 어떤 영향이 있나?"  
> - "관련 문서를 찾지 못했을 때는 어떻게 처리하나?"  

### 응답 생성

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

가장 관련 높은 청크들이 구조화된 프롬프트로 조립되며, 명시적 지침, 검색된 컨텍스트, 사용자 질문을 포함합니다. 모델은 해당 청크만 읽고 정보를 바탕으로 대답합니다 — 앞에 실제 정보가 있어야만 사용 가능하기 때문에 허위 생성(환각)을 방지합니다.

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
  
아래 다이어그램은 이 조립 과정을 보여줍니다 — 검색 절차에서 높은 점수를 받은 청크들이 프롬프트 템플릿에 주입되고 `OpenAiOfficialChatModel`이 근거 있는 답변을 생성합니다:

<img src="../../../translated_images/ko/context-assembly.7e6dd60c31f95978.webp" alt="컨텍스트 조립" width="800"/>

## 애플리케이션 실행하기

**배포 확인:**

루트 디렉터리에 Azure 자격 증명이 포함된 `.env` 파일이 존재하는지 확인하세요 (모듈 01에서 생성됨):  
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT를 표시해야 합니다
```
  
**애플리케이션 시작:**

> **참고:** 모듈 01에서 `./start-all.sh`로 애플리케이션을 모두 시작했다면 이미 이 모듈은 8081 포트에서 실행 중입니다. 아래 시작 명령은 건너뛰고 바로 http://localhost:8081 로 이동하세요.

**옵션 1: Spring Boot 대시보드 사용 (VS Code 사용자 권장)**

개발 컨테이너에는 Spring Boot 대시보드 확장 기능이 포함되어 있어 모든 Spring Boot 애플리케이션을 시각적으로 관리할 수 있습니다. VS Code 왼쪽 액티비티 바의 Spring Boot 아이콘에서 찾을 수 있습니다.

대시보드에서:  
- 작업 공간 내 모든 Spring Boot 앱 확인  
- 클릭 한 번으로 앱 시작/중지  
- 실시간 로그 보기  
- 앱 상태 모니터링  

“rag” 옆 재생 버튼을 클릭해 이 모듈을 시작하거나, 모든 모듈을 한꺼번에 시작할 수 있습니다.

<img src="../../../translated_images/ko/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot 대시보드" width="400"/>

**옵션 2: 셸 스크립트 사용**

모든 웹 애플리케이션(모듈 01-04)을 시작하려면:

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
  
또는 이 모듈만 시작하려면:

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
  
두 스크립트는 루트 `.env`의 환경 변수를 자동 로드하고 JAR이 없으면 빌드도 수행합니다.

> **참고:** 시작 전 모든 모듈을 직접 빌드하려면:  
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
  
브라우저에서 http://localhost:8081 열기

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

애플리케이션은 문서 업로드와 질문을 위한 웹 인터페이스를 제공합니다.

<a href="images/rag-homepage.png"><img src="../../../translated_images/ko/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG 애플리케이션 인터페이스" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*RAG 애플리케이션 인터페이스 - 문서 업로드 후 질문하기*

### 문서 업로드

먼저 문서를 업로드하세요—테스트용으로는 TXT 파일이 가장 적합합니다. 이 디렉터리에는 LangChain4j 기능, RAG 구현, 베스트 프랙티스에 관한 정보가 들어있는 `sample-document.txt`가 제공되어 테스트에 적합합니다.

시스템은 문서를 처리하고 청크로 나눈 후 각 청크에 임베딩을 생성합니다. 이 과정은 업로드 시 자동으로 진행됩니다.

### 질문하기

이제 문서 내용에 관한 구체적인 질문을 하세요. 문서에 명확히 나와 있는 사실에 대해 질문해 보는 것이 좋습니다. 시스템은 관련 청크를 찾아 프롬프트에 포함시키고 답변을 생성합니다.

### 출처 확인하기

각 답변에는 관련 문서 조각 출처와 유사도 점수가 포함되어 있습니다. 점수는 0에서 1 사이이며, 질문과 청크 간 관련도를 보여줍니다. 점수가 높을수록 더 적절한 매칭입니다. 이를 통해 답변을 출처와 대조 검증할 수 있습니다.

<a href="images/rag-query-results.png"><img src="../../../translated_images/ko/rag-query-results.6d69fcec5397f355.webp" alt="RAG 쿼리 결과" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*출처와 관련도 점수가 포함된 답변 쿼리 결과*

### 질문 실험하기

다양한 유형의 질문을 시도해보세요:  
- 구체적 사실: "주요 주제는 무엇인가?"  
- 비교: "X와 Y의 차이는 무엇인가?"  
- 요약: "Z에 관한 핵심 포인트를 요약해줘"

질문과 문서 내용 매칭에 따라 유사도 점수가 어떻게 변하는지 지켜보세요.

## 핵심 개념

### 청킹 전략

문서는 300토큰 크기의 청크로 나뉘며, 각 청크는 30토큰씩 겹칩니다. 이 균형은 각 청크에 충분한 컨텍스트를 제공하면서, 여러 청크를 프롬프트에 포함할 수 있을 만큼 작게 유지합니다.

### 유사도 점수

검색된 각 청크는 0에서 1 사이의 유사도 점수를 받습니다. 이 점수는 사용자의 질문과 얼마나 밀접한 관련이 있는지 나타냅니다. 아래 다이어그램은 점수 범위와 시스템에서 결과를 필터링하는 방법을 시각화합니다:

<img src="../../../translated_images/ko/similarity-scores.b0716aa911abf7f0.webp" alt="유사도 점수" width="800"/>

점수 범위:  
- 0.7-1.0: 매우 관련있음, 정확한 매치  
- 0.5-0.7: 관련 있음, 좋은 컨텍스트 제공  
- 0.5 미만: 필터링됨, 너무 유사하지 않음  

시스템은 최소 임계값 이상 점수를 받는 청크만 검색해 품질을 보장합니다.

### 인메모리 저장

이 모듈은 간단하게 인메모리 저장소를 사용합니다. 애플리케이션을 재시작하면 업로드한 문서가 모두 사라집니다. 실제 운영 환경에서는 Qdrant나 Azure AI Search 같은 영속성 있는 벡터 데이터베이스를 사용합니다.

### 컨텍스트 창 관리

각 모델은 최대 컨텍스트 창 크기가 정해져 있습니다. 큰 문서의 모든 청크를 포함할 수 없으므로, 시스템은 기본값으로 가장 관련 높은 상위 5개 청크만 검색해 포함하여 정확한 답변에 충분한 컨텍스트를 제공합니다.

## 언제 RAG가 중요한가

RAG가 항상 최선의 방법은 아닙니다. 아래 결정 안내는 언제 RAG가 가치가 있고, 언제 프롬프트에 직접 내용을 포함시키거나 모델 내장 지식을 활용하는 단순한 방법으로 충분한지 도움을 줍니다:

<img src="../../../translated_images/ko/when-to-use-rag.1016223f6fea26bc.webp" alt="RAG 사용 시기" width="800"/>

**RAG를 사용할 때:**
- 독점 문서에 관한 질문에 답변하기
- 정보가 자주 변경됨(정책, 가격, 사양)
- 정확성을 위해 출처 명시 필요
- 내용이 너무 커서 단일 프롬프트에 맞지 않음
- 검증 가능하고 근거가 있는 응답이 필요함

**다음과 같은 경우에는 RAG를 사용하지 마세요:**
- 질문이 모델이 이미 알고 있는 일반 지식을 요구할 때
- 실시간 데이터가 필요할 때(RAG는 업로드된 문서로 작동함)
- 콘텐츠가 프롬프트에 직접 포함될 만큼 작을 때

## 다음 단계

**다음 모듈:** [04-tools - 도구가 포함된 AI 에이전트](../04-tools/README.md)

---

**네비게이션:** [← 이전: 모듈 02 - 프롬프트 엔지니어링](../02-prompt-engineering/README.md) | [메인으로 돌아가기](../README.md) | [다음: 모듈 04 - 도구 →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 노력하였으나, 자동 번역에는 오류나 부정확한 내용이 포함될 수 있음을 알려드립니다. 원본 문서의 원어 버전을 권위 있는 자료로 간주하시기 바랍니다. 중요한 정보에 대해서는 전문 인간 번역가의 번역을 권장합니다. 본 번역 사용으로 인한 오해나 잘못된 해석에 대해 당사는 책임을 지지 않습니다.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->