# Module 03: RAG (Retrieval-Augmented Generation)

## Table of Contents

- [Video Walkthrough](../../../03-rag)
- [Wetin You Go Learnt](../../../03-rag)
- [Prerequisites](../../../03-rag)
- [Understanding RAG](../../../03-rag)
  - [Which RAG Approach Dem Use for This Tutorial?](../../../03-rag)
- [How E Dey Work](../../../03-rag)
  - [Document Processing](../../../03-rag)
  - [Creating Embeddings](../../../03-rag)
  - [Semantic Search](../../../03-rag)
  - [Answer Generation](../../../03-rag)
- [Run the Application](../../../03-rag)
- [How to Use the Application](../../../03-rag)
  - [Upload Document](../../../03-rag)
  - [Ask Questions](../../../03-rag)
  - [Check Source References](../../../03-rag)
  - [Experiment with Questions](../../../03-rag)
- [Key Concepts](../../../03-rag)
  - [Chunking Strategy](../../../03-rag)
  - [Similarity Scores](../../../03-rag)
  - [In-Memory Storage](../../../03-rag)
  - [Context Window Management](../../../03-rag)
- [When RAG E Get Matter](../../../03-rag)
- [Next Steps](../../../03-rag)

## Video Walkthrough

Watch dis live session wey go explain how to start with dis module: [RAG with LangChain4j - Live Session](https://www.youtube.com/watch?v=_olq75ZH_eY)

## Wetin You Go Learnt

For di previous modules, you don learn how to dey talk wit AI and arrange your prompts well. But one gbege dey: language models fit only sabi wetin dem learn when dem train am. Dem no fit answer question about your company rules, your project documents, or anything wey dem no train on.

RAG (Retrieval-Augmented Generation) solve dis problem. Instead make you try teach di model your info (wey dey expensive and no easy), you just give am power to search your documents. When person ask question, the system go find correct info and put am inside the prompt. Di model go answer based on dat retrieved context.

Think as if RAG dey give di model one reference library. When you ask question, di system:

1. **User Query** - You ask question
2. **Embedding** - E convert your question to vector
3. **Vector Search** - E find similar document chunks
4. **Context Assembly** - E add correct chunks to di prompt
5. **Response** - LLM go generate answer based on di context

Dis one mean say di model answer base for your real data instead of wetin e learn for training or just make up answer.

## Prerequisites

- Finish [Module 00 - Quick Start](../00-quick-start/README.md) (for di Easy RAG example wey dem mention before)
- Finish [Module 01 - Introduction](../01-introduction/README.md) (Azure OpenAI resources dey deployed, including di `text-embedding-3-small` embedding model)
- `.env` file for root directory with Azure credentials (wey `azd up` create for Module 01)

> **Note:** If you never finish Module 01, abeg follow di deployment instruction for there first. Di `azd up` command go deploy GPT chat model and di embedding model wey dis module dey use.

## Understanding RAG

Di diagram wey dey below dey show di main idea: instead of just rely on di model training data alone, RAG go give am one reference library of your documents to sabi before e generate each answer.

<img src="../../../translated_images/pcm/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*Dis diagram dey show di difference between regular LLM (wey just guess from training data) and RAG-enhanced LLM (wey first check your documents).*

Dis na how di parts dem dey connect from beginning to end. User question dey go through four stages — embedding, vector search, context assembly, and answer generation — each one dey depend on di previous one:

<img src="../../../translated_images/pcm/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*Dis diagram dey show di whole RAG pipeline — user query dey go through embedding, vector search, context assembly, and answer generation.*

Di rest of dis module go waka you through each stage well well, with code wey you fit run and change.

### Which RAG Approach Dem Use for This Tutorial?

LangChain4j get three ways to do RAG, each one get different level of abstraction. Di diagram wey dey below go compare dem side by side:

<img src="../../../translated_images/pcm/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*Dis diagram dey compare three LangChain4j RAG approaches — Easy, Native, and Advanced — show their important parts and when to use each one.*

| Approach | Wetin E Dey Do | Trade-off |
|---|---|---|
| **Easy RAG** | E wire everything automatically through `AiServices` and `ContentRetriever`. You just mark interface, join retriever, and LangChain4j go handle embedding, searching, and prompt assembly for back. | Small code, but you no go see wetin dey happen for every step. |
| **Native RAG** | You dey call embedding model, search storage, build prompt, and generate answer yourself — one clear step at a time. | More code, but you fit see and change every stage. |
| **Advanced RAG** | E use `RetrievalAugmentor` framework with pluggable query transformers, routers, re-rankers, and content injectors for big production pipelines. | Most flexible, but e dey more complex well well. |

**Dis tutorial dey use Native approach.** Every step for RAG pipeline — embedding di query, searching vector store, joining context, and answer generation — dem write am clear for [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java). Na purpose be dis: as learning resource, e better sey you see and understand every stage pass sey code go short. After you sabi how di pieces join, you fit move go Easy RAG for quick prototype or Advanced RAG for production.

> **💡 You don see Easy RAG before?** [Quick Start module](../00-quick-start/README.md) get Document Q&A example ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) wey use Easy RAG — LangChain4j dey handle embedding, searching, and prompt assembly automatically. Dis module na next level — e break di pipeline open so you fit see and control every stage yourself.

<img src="../../../translated_images/pcm/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*Dis diagram dey show Easy RAG pipeline from `SimpleReaderDemo.java`. Compare am with Native approach for dis module: Easy RAG dey hide embedding, retrieval, and prompt assembly behind `AiServices` and `ContentRetriever` — you load document, join retriever, and get answers. Native approach here break the pipeline open so you fit call every stage (embed, search, join context, generate) yourself, giving you full control and clear view.*

## How E Dey Work

Di RAG pipeline for dis module break down into four stages wey run one after di other anytime user ask question. First, when document don upload, system go **parse and chunk** am into small pieces wey model fit handle small small. Dem chunks get some overlap make context no lost for borders.

Then the chunks go turn to **vector embeddings** and dem save am for storage to fit compare mathematically. When question kon, system go do **semantic search** to find the chunks wey get most meaning and finally pass dem as context to di LLM for **answer generation**. Di sections below go waka through each stage with real code and diagrams. Make we first look di first step.

### Document Processing

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

When you upload document, di system go parse am (whether na PDF or plain text), join metadata like di filename, then break am into chunks — small pieces wey fit well inside model context window. Dem chunks dey overlap small so that you no go lose context as you move from one chunk to another.

```java
// Parse di uploaded file an wrap am inside LangChain4j Document
Document document = Document.from(content, metadata);

// Cut am into 300-token pieces wit 30-token overlap
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
Di diagram below dey show di visual way e dey work. See how each chunk share some tokens with di neighbours — di 30-token overlap make sure no important context go lost:

<img src="../../../translated_images/pcm/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*Dis diagram dey show document wey dem split into 300-token chunks with 30-token overlap, to keep context for di chunk borders.*

> **🤖 Try wit [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) and ask:  
> - "How LangChain4j dey split documents into chunks and why overlap dey important?"  
> - "Which chunk size good for different document types and why?"  
> - "How I go handle doc wey get multiple languages or special formatting?"

### Creating Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Each chunk go turn to numerical description wey dem dey call embedding — basically e mean to convert meaning to numbers. Embedding model no be like chat model wey dey intelligent; e no fit follow instructions, reason or answer questions. Wetin e do na just map text go mathematical space so that similar meaning go near each other — like "car" near "automobile," "refund policy" near "return my money." Think of chat model as person wey fit talk; embedding model na sharp filing system.

<img src="../../../translated_images/pcm/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*Dis diagram dey show how embedding model dey convert text into numbers (vectors), where meanings wey similar — like "car" and "automobile" — dey near each other in vector space.*

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
  
Di class diagram below show two flows for RAG pipeline and di LangChain4j classes wey dem use. Di **ingestion flow** (wey run once when you upload) dey split document, embed di chunks, then store dem with `.addAll()`. Di **query flow** (wey run any time user ask) embed question, search store with `.search()`, then pass correct context to chat model. Both flows dey use di shared `EmbeddingStore<TextSegment>` interface:

<img src="../../../translated_images/pcm/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*Dis diagram dey show the two flows for RAG pipeline — ingestion and query — and how dem connect with shared EmbeddingStore.*

Once you store embeddings, similar content go naturally cluster for vector space. Di visual below show how documents about related topics go become nearby points, and na dis one dey make semantic search possible:

<img src="../../../translated_images/pcm/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*Dis visualization dey show how related documents cluster together for 3D vector space, with topics like Technical Docs, Business Rules, and FAQs forming different groups.*

When user search, system go do four things: embed documents once, embed question each time search dey done, compare question vector with all stored vectors with cosine similarity, then return top-K best scoring chunks. Di diagram below waka you through these steps and LangChain4j classes wey involved:

<img src="../../../translated_images/pcm/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*Dis diagram dey show four-step embedding search process: embed documents, embed question, compare vectors with cosine similarity, then return di top-K results.*

### Semantic Search

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

When you ask question, your question go also become embedding. Di system go compare your question embedding with all di chunks embeddings for documents. E go find di chunks wey mean nearly di same thing — no be just matching keywords, but real semantic similarity.

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
  
Di diagram below dey compare semantic search with traditional keyword search. Keyword search for "vehicle" no go find chunk about "cars and trucks," but semantic search go sabi say dem mean di same thing and e go bring am as top match:

<img src="../../../translated_images/pcm/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*Dis diagram compare keyword search wit semantic search, show how semantic search fit find concept-related content even if keywords no match exactly.*

Underneath, similarity dey measured using cosine similarity — basically e dey ask "these two arrows dey point the same direction?" Two chunks fit use complete different words, but if dem mean da same thing, their vectors fit point di same way and get score near 1.0:

<img src="../../../translated_images/pcm/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*Dis diagram dey show cosine similarity as angle between embedding vectors — vectors weh align more go get score near 1.0, meaning semantic similarity dey high.*
> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) and ask:
> - "How does similarity search work with embeddings and what determines the score?"
> - "What similarity threshold should I use and how does it affect results?"
> - "How do I handle cases where no relevant documents are found?"

### Answer Generation

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

The most relevant chunks dem put together into one clear prompt wey get explicit instructions, di context wey dem get, and di user question. Di model go read those particular chunks and answer based on dat info — e fit only use wetin dey front of am, and dat go stop am from hala things wey no dey true.

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

Di diagram below dey show how dem dey assemble am — di top-scoring chunks from di search step dem go put inside di prompt template, and di `OpenAiOfficialChatModel` go generate grounded answer:

<img src="../../../translated_images/pcm/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*Dis diagram dey show how di top-scoring chunks dem join form one structured prompt, wey allow di model generate grounded answer from your data.*

## Run the Application

**Verify deployment:**

Make sure sey di `.env` file dey for root directory with Azure credentials (wey dem create for Module 01):

**Bash:**
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**PowerShell:**
```powershell
Get-Content ..\.env  # E for show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start the application:**

> **Note:** If you don already start all di applications using `./start-all.sh` from Module 01, dis module don dey run for port 8081. You fit skip di start commands below and just go http://localhost:8081.

**Option 1: Using Spring Boot Dashboard (Recommended for VS Code users)**

Di dev container get Spring Boot Dashboard extension wey give visual interface to manage all Spring Boot applications. You fit find am for di Activity Bar for left side of VS Code (look for di Spring Boot icon).

From di Spring Boot Dashboard, you fit:
- See all di available Spring Boot applications for di workspace
- Start/stop applications with one click
- View application logs for real-time
- Monitor application status

Just click di play button beside "rag" to start dis module, or start all modules at once.

<img src="../../../translated_images/pcm/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*Dis screenshot dey show di Spring Boot Dashboard for VS Code, wey you fit start, stop, and monitor applications visually.*

**Option 2: Using shell scripts**

Start all web applications (modules 01-04):

**Bash:**
```bash
cd ..  # From root directory
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

Both di scripts automatically dey load environment variables from root `.env` file and dem go build di JARs if dem never dey.

> **Note:** If you prefer to build all modules manually before you start:
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

Open http://localhost:8081 inside your browser.

**To stop:**

**Bash:**
```bash
./stop.sh  # Dis module only
# Or
cd .. && ./stop-all.sh  # All di modules
```

**PowerShell:**
```powershell
.\stop.ps1  # Dis module only
# Or
cd ..; .\stop-all.ps1  # All modules
```

## Using the Application

Di application dey provide web interface for document upload and questioning.

<a href="images/rag-homepage.png"><img src="../../../translated_images/pcm/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dis screenshot dey show di RAG application interface where you dey upload documents and ask questions.*

### Upload a Document

Start by uploading one document - TXT files good well well for testing. Di `sample-document.txt` dey this directory wey get information about LangChain4j features, RAG implementation, and best practices - perfect for testing di system.

Di system go process your document, break am into chunks, and create embeddings for every chunk. Dis one go happen automatically when you upload.

### Ask Questions

Now ask specific questions about di document content. Try sometin factual weh clearly dey for di document. Di system go search for relevant chunks, put dem inside di prompt, and generate answer.

### Check Source References

You go notice sey every answer get source references with similarity scores. Di scores dem (0 to 1) dey show how relevant each chunk be to your question. High score mean better match. Dis one go help you check di answer against di source material.

<a href="images/rag-query-results.png"><img src="../../../translated_images/pcm/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Dis screenshot show query results with di generated answer, source references, and relevance scores for each retrieved chunk.*

### Experiment with Questions

Try different types of questions:
- Specific facts: "Wetin be di main topic?"
- Comparisons: "Wetin be di difference between X and Y?"
- Summaries: "Summarize di key points about Z"

Watch how di relevance scores dey change based on how well your question match di document content.

## Key Concepts

### Chunking Strategy

Documents dem dey split into 300-token chunks with 30 tokens overlap. Dis balance dey make sure sey each chunk get enough context to mean something but still small enough to fit multiple chunks inside one prompt.

### Similarity Scores

Every retrieved chunk get similarity score wey dey between 0 and 1 wey show how e match di user question. Di diagram below dey show the score ranges and how di system dey use dem to filter results:

<img src="../../../translated_images/pcm/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*Dis diagram show score ranges from 0 to 1, with minimum threshold of 0.5 wey dey filter out irrelevant chunks.*

Scores dey range from 0 to 1:
- 0.7-1.0: Highly relevant, exact match
- 0.5-0.7: Relevant, good context
- Below 0.5: Filtered out, too dissimilar

Di system dey only get chunks wey pass di minimum threshold to make sure quality dey.

Embeddings dey work well when meaning clusters clean, but dem get blind spots. Di diagram below dey show common failure modes — chunks wey too big dey produce muddled vectors, chunks wey too small no get context, ambiguous terms fit point to plenty clusters, and exact-match lookups (IDs, part numbers) no dey work with embeddings at all:

<img src="../../../translated_images/pcm/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*Dis diagram dey show common embedding failure modes: chunks too big, chunks too small, ambiguous terms wey point to different clusters, and exact-match lookups like IDs.*

### In-Memory Storage

Dis module dey use in-memory storage for simplicity. When you restart di application, the uploaded documents go lost. Production systems dey use persistent vector databases like Qdrant or Azure AI Search.

### Context Window Management

Every model get maximum context window. You no fit include every chunk from one big document. Di system go retrieve di top N most relevant chunks (default 5) to stay within limits and still provide enough context for correct answers.

## When RAG Matters

RAG no always be di right method. Di decision guide below go help you know when RAG add value compared to when simpler methods — like putting content directly inside di prompt or relying on di model built-in knowledge — dey okay:

<img src="../../../translated_images/pcm/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*Dis diagram dey show decision guide for when RAG add value compared to when simpler methods fit work.*

**Use RAG when:**
- You dey answer questions about proprietary documents
- Information dey change often (policies, prices, specifications)
- Accuracy dey require source attribution
- Content too big to fit inside one prompt
- You need answers wey get verification and grounded support

**No use RAG when:**
- Questions need general knowledge wey di model already get
- You need real-time data (RAG dey work only on uploaded documents)
- Content small enough to put inside prompts directly

## Next Steps

**Next Module:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigation:** [← Previous: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Back to Main](../README.md) | [Next: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document na di one wey AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator) translate. Even though we dey try make am correct, make you sabi say automated translation fit get some errors or wahala. Di original document wey e come from e own language na di correct one we suppose rely on. If na serious matter, na professional human translator you suppose use. We no go take responsibility if person misunderstand or misinterpret any tin wey come from dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->