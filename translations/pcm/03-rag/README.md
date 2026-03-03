# Module 03: RAG (Retrieval-Augmented Generation)

## Table of Contents

- [Video Walkthrough](../../../03-rag)
- [Wetin You Go Learn](../../../03-rag)
- [Wetin You Must Know Before](../../../03-rag)
- [Understanding RAG](../../../03-rag)
  - [Which RAG Approach Dis Tutorial Dey Use?](../../../03-rag)
- [How E Dey Work](../../../03-rag)
  - [Document Processing](../../../03-rag)
  - [Creating Embeddings](../../../03-rag)
  - [Semantic Search](../../../03-rag)
  - [Answer Generation](../../../03-rag)
- [Run the Application](../../../03-rag)
- [Using the Application](../../../03-rag)
  - [Upload Document](../../../03-rag)
  - [Ask Questions](../../../03-rag)
  - [Check Source References](../../../03-rag)
  - [Experiment with Questions](../../../03-rag)
- [Key Concepts](../../../03-rag)
  - [Chunking Strategy](../../../03-rag)
  - [Similarity Scores](../../../03-rag)
  - [In-Memory Storage](../../../03-rag)
  - [Context Window Management](../../../03-rag)
- [When RAG Matter](../../../03-rag)
- [Next Steps](../../../03-rag)

## Video Walkthrough

Watch dis live session wey dey explain how to start with dis module:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## Wetin You Go Learn

For the previous modules, you don learn how to dey talk with AI and how to arrange your prompts well well. But one basic wahala dey: language models only sabi wetin dem learn during training. Dem no fit answer questions about your company policy, your project documentation, or any info wey dem never train on.

RAG (Retrieval-Augmented Generation) na solution to dis problem. Instead of trying to teach the model your info (wey dey expensive and no too practical), you dey give am the power to search your documents. When person ask question, system go find relevant info, then put am for prompt. The model go then answer based on that retrieved context.

Think am like dis: RAG na like you dey give the model one reference library. When you ask question, the system:

1. **User Query** - You ask question
2. **Embedding** - E change your question to vector
3. **Vector Search** - E find similar document chunks
4. **Context Assembly** - E add correct chunks to the prompt
5. **Response** - LLM go generate answer based on the context

Dis one dey ground the model response for your real data instead of make e rely on training knowledge or make up answer.

## Prerequisites

- Complete [Module 00 - Quick Start](../00-quick-start/README.md) (for the Easy RAG example wey we talk about later for dis module)
- Complete [Module 01 - Introduction](../01-introduction/README.md) (Azure OpenAI resources deployed, including the `text-embedding-3-small` embedding model)
- `.env` file for root directory with Azure credentials (wey `azd up` create for Module 01)

> **Note:** If you never finish Module 01, abeg follow de deployment instructions wey dey there first. The `azd up` command go deploy both GPT chat model and the embedding model wey dis module dey use.

## Understanding RAG

The diagram below dey show the main idea: instead of only rely on the model training data, RAG dey give am one reference library of your documents to check before e generate answer.

<img src="../../../translated_images/pcm/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Dis diagram dey show the difference between normal LLM (wey dey guess from training data) and RAG-enhanced LLM (wey dey check your documents first).*

See how the parts connect from beginning to end. Question from user pass through four stages — embedding, vector search, context assembly, and answer generation — each one dey build on the other:

<img src="../../../translated_images/pcm/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Dis diagram dey show the RAG pipeline complete — user question pass through embedding, vector search, context assembly, and answer generation.*

The rest of this module go explain each stage well well, with code wey you fit run and modify.

### Which RAG Approach Dis Tutorial Dey Use?

LangChain4j get three ways to implement RAG, each get different abstraction level. The diagram below dey compare am side by side:

<img src="../../../translated_images/pcm/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Dis diagram dey compare the three LangChain4j RAG approaches — Easy, Native, and Advanced — showing the main parts and when to use each one.*

| Approach | Wetin E Dey Do | Trade-off |
|---|---|---|
| **Easy RAG** | E dey wire everything automatically through `AiServices` and `ContentRetriever`. You just annotate interface, attach retriever, and LangChain4j go handle embedding, search, and prompt assembly for you. | Small code, but you no go see wetin dey happen for each step. |
| **Native RAG** | You go call embedding model, search the store, build prompt, and generate answer yourself — one explicit step per time. | More code, but every stage dey clear and you fit change am. |
| **Advanced RAG** | E dey use `RetrievalAugmentor` framework with pluggable query transformers, routers, re-rankers, and content injectors for production level pipelines. | Maximum flexibility, but e dey complex well well. |

**Dis tutorial na Native approach e dey use.** Every step for RAG pipeline — embed the query, search vector store, assemble context, and generate answer — dey clearly written inside [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). E mean say as learning resource, e dey more important make you see and understand every stage pass to minimize code. When you don sabi how the pieces join, you fit move go Easy RAG for fast prototype or Advanced RAG for production.

> **💡 You don see Easy RAG before?** The [Quick Start module](../00-quick-start/README.md) get Document Q&A example ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) wey dey use Easy RAG — LangChain4j dey handle embedding, searching, and prompt assembly automatically. Dis module na next level wey open the pipeline make you fit see and control every stage by yourself.

Diagram below show Easy RAG pipeline from that Quick Start example. Notice how `AiServices` and `EmbeddingStoreContentRetriever` hide all the complexity — you just load document, attach retriever, and get answer. The Native approach for this module go open those hidden steps:

<img src="../../../translated_images/pcm/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Dis diagram dey show Easy RAG pipeline from `SimpleReaderDemo.java`. Compare am with Native approach wey dis module dey use: Easy RAG hide embedding, retrieval, and prompt assembly behind `AiServices` and `ContentRetriever` — you load document, attach retriever, and receive answers. Native approach for dis module open the pipeline so you fit call each stage (embed, search, assemble context, generate) yourself, give you full control and clear view.*

## How E Dey Work

RAG pipeline for dis module break down into four stages wey run one after another anytime user ask question. First, uploaded document go **parse and chunk** into small small parts. Those chunks go convert to **vector embeddings** and store so e fit compare mathematically. When query come, system go run **semantic search** to find the best chunks, then pass dem as context to LLM for **answer generation**. The sections below go explain each stage with real code and diagrams. Make we start with the first step.

### Document Processing

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

When you upload document, system go parse am (PDF or plain text), attach metadata like filename, then break am into chunks — smaller parts wey fit inside model context window well well. These chunks dey overlap small so you no lose context for boundaries.

```java
// Parse di uploaded file an wrap am inside LangChain4j Document
Document document = Document.from(content, metadata);

// Cut am into 300-token chunks wit 30-token overlap
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Diagram below dey show how e dey work visually. Notice say each chunk dey share some tokens with im neighbours — the 30-token overlap make sure say no important context go lost between the cracks:

<img src="../../../translated_images/pcm/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Dis diagram dey show document wey dey split into 300-token chunks wit 30-token overlap, to preserve context for chunk boundaries.*

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) and ask:
> - "How LangChain4j split documents into chunks and why overlap dey important?"
> - "Wetin be optimal chunk size for different document types and why?"
> - "How I fit handle documents wey get many languages or special formatting?"

### Creating Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Each chunk go convert to numerical format wey dem dey call embedding — na how text meaning dey convert to numbers. Embedding model no be "intelligent" like chat model; e no fit follow instruction, reason or answer questions. Di one e fit do na to put text for mathematical space where meanings wey dey similar go dey close — like "car" near "automobile," "refund policy" near "return my money." Think chat model like person wey you fit yarn with; embedding model na one keen filing system.

Diagram below show dis concept — text enter, numbers vectored come out, and similar meanings dey near each other:

<img src="../../../translated_images/pcm/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Di diagram show how embedding model dey convert text to numerical vectors, placing similar meanings — like "car" and "automobile" — close to each other for vector space.*

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

Class diagram below show di two separate flows inside RAG pipeline and LangChain4j classes wey handle dem. **Ingestion flow** (wey run once when upload) dey split documents, embed chunks, and store dem with `.addAll()`. **Query flow** (wey run anytime user ask) go embed question, search store with `.search()`, then pass context wey match to chat model. Both flows dey connected via shared `EmbeddingStore<TextSegment>` interface:

<img src="../../../translated_images/pcm/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Dis diagram show di two flows inside RAG pipeline — ingestion and query — and how dem take connect via shared EmbeddingStore.*

After embedding store finish, similar content go group together for vector space naturally. Visualization below dey show how documents about related topics dey come close one another as points, and dat na wetin make semantic search possible:

<img src="../../../translated_images/pcm/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Dis visualization dey show how related documents dey cluster for 3D vector space, with topics like Technical Docs, Business Rules, and FAQs making separate groups.*

When user search, system dey follow four steps: embed documents once, embed query everytime search happen, compare query vector with all stored vectors using cosine similarity, then return top-K chunks wey score pass. Diagram below go show each step and LangChain4j classes wey dey involved:

<img src="../../../translated_images/pcm/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Dis diagram dey show four-step embedding search process: embed documents, embed query, compare vectors with cosine similarity, and return top-K results.*

### Semantic Search

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

When you ask question, your question carry embedding too. System go compare your question embedding with all document chunks embedding. E go find chunks wey get closest meaning — no be only keywords, na real semantic similarity.

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

Diagram below go contrast semantic search with traditional keyword search. Keyword search for "vehicle" no fit find chunk wey talk about "cars and trucks," but semantic search sabi say dem mean same thing and e go return am as high scoring match:

<img src="../../../translated_images/pcm/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Dis diagram go compare keyword-based search with semantic search, showing how semantic search fit find content related by concept even if exact keywords differ.*
Unda di hood, similarity na wetin dem dey measure wit cosine similarity — e dey basically ask "di two arrow dem dey point for di same direction?" Two chunks fit use totally different words, but if dem mean di same tin their vectors go dey point di same way and dem go score near to 1.0:

<img src="../../../translated_images/pcm/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Dis diagram dey show cosine similarity as di angle between embedding vectors — vectors wen align well go score close to 1.0, wey mean say dem mean almost di same tin.*

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) and ask:
> - "How similarity search dey work wit embeddings and wetin dey determine di score?"
> - "Wetin similarity threshold I suppose use and how e dey affect results?"
> - "How I go handle cases wen no relevant documents dey found?"

### Answer Generation

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

Di most relevant chunks dem dey put together inside one structured prompt wey get explicit instructions, di context wey dem find, and di question wey user ask. Di model go read only di chunks dem and answer based on dat tin — e go fit use only wetin dey front am, dis one dey prevent hallucination.

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

Di diagram wey dey below dey show how dem dey put dis tin together — di top-scoring chunks from di search step dem dey put inside di prompt template, and di `OpenAiOfficialChatModel` go generate grounded answer:

<img src="../../../translated_images/pcm/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Dis diagram dey show how di top-scoring chunks dem dey join to form structured prompt, wey allow di model to generate grounded answer from your data.*

## Run the Application

**Verify deployment:**

Make sure say di `.env` file dey for root directory wit Azure credentials (wey dem create for Module 01). Run dis from di module directory (`03-rag/`):

**Bash:**
```bash
cat ../.env  # Suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start di application:**

> **Note:** If you don start all applications wit `./start-all.sh` from root directory before (as we explain for Module 01), dis module dey already run on port 8081. You fit skip di start commands below go directly to http://localhost:8081.

**Option 1: Using Spring Boot Dashboard (Recommended for VS Code users)**

Di dev container get Spring Boot Dashboard extension, wey dey give you visual interface to manage all Spring Boot applications. You fit find am for di Activity Bar for left side of VS Code (look for di Spring Boot icon).

From Spring Boot Dashboard, you fit:
- See all di Spring Boot applications wey dey di workspace
- Start/stop applications wit just one click
- View application logs in real-time
- Monitor application status

Just click di play button wey dey beside "rag" to start dis module, or start all modules at once.

<img src="../../../translated_images/pcm/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Dis screenshot dey show how Spring Boot Dashboard dey VS Code, where you fit start, stop, and monitor applications visually.*

**Option 2: Using shell scripts**

Start all web applications (modules 01-04):

**Bash:**
```bash
cd ..  # From root folder
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # From root directory
.\start-all.ps1
```

Or start just dis module:

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

Both scripts go automatically load environment variables from root `.env` file and dem go build di JARs if dem never dey.

> **Note:** If you wan build all modules by yourself before you start:
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

Open http://localhost:8081 for your browser.

**To stop:**

**Bash:**
```bash
./stop.sh  # Dis module only
# Or
cd .. && ./stop-all.sh  # All modules
```

**PowerShell:**
```powershell
.\stop.ps1  # Dis module only
# Or
cd ..; .\stop-all.ps1  # All modules
```

## Using di Application

Di application dey provide web interface for document upload and question ask.

<a href="images/rag-homepage.png"><img src="../../../translated_images/pcm/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dis screenshot dey show di RAG application interface wey you fit upload documents and ask questions.*

### Upload a Document

Start by uploading document — TXT files dey best for testing. One `sample-document.txt` dey for dis directory, e get information about LangChain4j features, RAG implementation, and best practices - perfect for testing di system.

Di system go process your document, break am into chunks, then e go create embeddings for each chunk. Dis one dey happen automatically when you upload.

### Ask Questions

Now ask di specific questions about di document content. Try ask factual tins wey clearly dey inside di document. Di system go search for relevant chunks, include dem inside di prompt, then generate answer.

### Check Source References

You go notice say each answer get source references wit similarity scores. Dis scores (from 0 to 1) dey show how relevant each chunk be to your question. Higher scores mean better match. Dis one go help you verify di answer against di source.

<a href="images/rag-query-results.png"><img src="../../../translated_images/pcm/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dis screenshot dey show query results wit generated answer, source references, and relevance scores for each chunk dem find.*

### Experiment wit Questions

Try different kinds of questions:
- Specific facts: "Wetin be di main topic?"
- Comparisons: "Wetin be di difference between X and Y?"
- Summaries: "Summarize di key points about Z"

Watch how di relevance scores dey change based on how well your question match di document content.

## Key Concepts

### Chunking Strategy

Documents dem dey split into 300-token chunks with 30 tokens overlap. Dis one dey balance so dat each chunk get enough context to mean wetin e suppose mean but e still small enough to fit many chunks inside prompt.

### Similarity Scores

Every chunk wey dem find get similarity score between 0 and 1 wey dey show how close e match di user's question. Di diagram below dey show di score ranges and how system dey use dem to filter results:

<img src="../../../translated_images/pcm/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Dis diagram dey show score ranges from 0 to 1, wit minimum threshold of 0.5 wey dey filter out chunks wey no too relate.*

Scores range from 0 to 1:
- 0.7-1.0: Highly relevant, exact match
- 0.5-0.7: Relevant, good context
- Below 0.5: Dem filter am out, e too different

Di system na only chunks wey pass di minimum threshold e go retrieve so dat quality go dey.

Embeddings dey work well when meaning clusters cleanly, but dem get blind spots. Di diagram below dey show di common failure modes — chunks wey big too much go create muddy vectors, chunks wey small die no get enough context, ambiguous terms dey point to many clusters, and exact-match lookups (IDs, part numbers) no dey work wit embeddings at all:

<img src="../../../translated_images/pcm/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Dis diagram dey show common embedding failure modes: chunks wey big die, chunks wey small die, ambiguous terms wey dey point to different clusters, and exact-match lookups like IDs.*

### In-Memory Storage

Dis module dey use in-memory storage for simplicity. When you restart di application, di uploaded documents go lost. Production systems dey use persistent vector databases like Qdrant or Azure AI Search.

### Context Window Management

Each model get maximum context window. You no fit put every chunk from big document. Di system go retrieve top N most relevant chunks (default na 5) to stay within di limit but still provide enough context make answers accurate.

## When RAG Matters

RAG no always be di right approach. Di decision guide below dey help you sabi when RAG get value versus when simpler approaches — like putting content straight inside prompt or relying on di model built-in knowledge — dey good enough:

<img src="../../../translated_images/pcm/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Dis diagram dey show decision guide for when RAG get value versus when simpler methods jus enough.*

## Next Steps

**Next Module:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigation:** [← Previous: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Back to Main](../README.md) | [Next: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis dokumant don translate wit AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even as we dey try make am correct, abeg sabi say automated translation fit get errors or mistakes. Di original dokumant wey dem write for im own language na di correct one. If na important tin, better make human professional person do di translation. We no go hold ourselves responsible if people no understand well or misinterpret because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->