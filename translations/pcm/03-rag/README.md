# Module 03: RAG (Retrieval-Augmented Generation)

## Table of Contents

- [Wetyn You Go Learn](../../../03-rag)
- [Understand RAG](../../../03-rag)
- [Wetyn You Suppos Know Before](../../../03-rag)
- [How E Dey Work](../../../03-rag)
  - [Document Processing](../../../03-rag)
  - [How To Create Embeddings](../../../03-rag)
  - [Semantic Search](../../../03-rag)
  - [How E Dey Generate Answer](../../../03-rag)
- [How To Run Di Application](../../../03-rag)
- [How To Use Di Application](../../../03-rag)
  - [How To Upload Document](../../../03-rag)
  - [Ask Questions](../../../03-rag)
  - [Check Source References](../../../03-rag)
  - [Try Different Kain Questions](../../../03-rag)
- [Key Concepts](../../../03-rag)
  - [Chunking Strategy](../../../03-rag)
  - [Similarity Scores](../../../03-rag)
  - [In-Memory Storage](../../../03-rag)
  - [How To Manage Context Window](../../../03-rag)
- [When RAG Dey Important](../../../03-rag)
- [Next Steps](../../../03-rag)

## Wetyn You Go Learn

For the previous modules, you don learn how to yan with AI and arrange your prompts well well. But one gbege dey: language models fit only sabi wetin dem learn during training. Dem no fit answer questions about your company policy, your project documents, or any kain info wey dem no train on.

RAG (Retrieval-Augmented Generation) na im solve dis gbege. Instead make you dey teach di model your info (wey dey costly and no too practical), you go give am power to search your documents. When pesin ask question, the system go find the correct info come put am for prompt. Di model go answer based on di info wey e find.

Think of RAG as giving di model reference library. When you ask question, the system:

1. **User Query** - You ask question
2. **Embedding** - E go turn your question to vector
3. **Vector Search** - E go find document chunks Wey resemble your question
4. **Context Assembly** - E go add the correct chunks to prompt
5. **Response** - LLM go generate answer based on the context

Dis one make di model answer dey based on your real data instead of only im training or to dey make answer.

## Understand RAG

Di diagram wey dey below show di main idea: instead to depend only on model training data, RAG go give am library of your documents to check before e generate answer.

<img src="../../../translated_images/pcm/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

See how the parts to join start to finish. Person question dey waka through four stages — embedding, vector search, context assembly and answer generation — each one na because of di one before am:

<img src="../../../translated_images/pcm/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

Di rest of di module go show you each stage well well, with code wey you fit run and change.

## Wetyn You Suppos Know Before

- You suppose don finish Module 01 (Azure OpenAI resources deploy finish)
- `.env` file for root folder wey get Azure credentials (from `azd up` for Module 01)

> **Note:** If you never finish Module 01, follow deployment instructions wey dey there first.

## How E Dey Work

### Document Processing

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

When you upload document, system go parse am (PDF or plain text), attach metadata like filename, then e go break am into small chunks — smaller parts wey fit inside model context window well well. Di chunks go overlap small so say no context go lost for edge.

```java
// Parse di uploaded file den wrap am inside LangChain4j Document
Document document = Document.from(content, metadata);

// Split am into 300-token chunks wit 30-token overlap
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

Di diagram below dey show how e dey work visually. You go see say every chunk dey share some tokens with im neighbor — di 30-token overlap make sure say no important context go lost:

<img src="../../../translated_images/pcm/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) and ask:
> - "How LangChain4j dey split documents into chunks and why overlap matter?"
> - "Wetin be best chunk size for different documents and why?"
> - "How I fit handle documents Wey get multiple languages or correct formatting?"

### How To Create Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Every chunk go convert to number form called embedding - na mathematical fingerprint wey capture wetin the text mean. Similar text go produce similar embeddings.

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

The class diagram below dey show how LangChain4j parts join together. `OpenAiOfficialEmbeddingModel` dey convert text to vectors, `InMemoryEmbeddingStore` dey hold vectors along with original `TextSegment` data, and `EmbeddingSearchRequest` dey control retrieval settings like `maxResults` and `minScore`:

<img src="../../../translated_images/pcm/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

After you store embeddings, similar content go group together inside vector space. Di diagram below dey show how documents about related matter dem dey close, na wetin make semantic search possible:

<img src="../../../translated_images/pcm/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

### Semantic Search

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

When you ask question, your question go also become embedding. System go compare your question embedding with all di document chunk embeddings. E go find chunks Wey mean the same thing - no be keywords only, but real semantic similarity.

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

Di diagram below compare semantic search with normal keyword search. Keyword search wey search "vehicle" no go find chunk wey talk "cars and trucks," but semantic search sabi say dem mean the same thing and e go give am as high score:

<img src="../../../translated_images/pcm/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) and ask:
> - "How similarity search dey work with embeddings and wetin dey decide score?"
> - "Which similarity threshold I suppose use and how e go affect results?"
> - "How I fit handle situation wey no get relevant documents?"

### How E Dey Generate Answer

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

The most correct chunks go join form structured prompt wey get clear instructions, retrieved context, and user question. Model go read these chunks and answer based on am — e fit only use wetin dey front, dis one dey stop am from to dey make story (hallucination).

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

Di diagram below dey show how di joining dey happen — the top-scoring chunks from search go go inside prompt template, and `OpenAiOfficialChatModel` go generate correct answer:

<img src="../../../translated_images/pcm/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

## How To Run Di Application

**Make sure say deployment dey correct:**

Check say `.env` file dey root folder with Azure credentials (wey dem create for Module 01):
```bash
cat ../.env  # E suppose show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**How to start application:**

> **Note:** If you don already start all applications with `./start-all.sh` for Module 01, dis module dey run for port 8081 already. You fit skip start commands below and go straight http://localhost:8081.

**Option 1: Use Spring Boot Dashboard (Better for VS Code users)**

The dev container get Spring Boot Dashboard extension, wey get user interface to manage all Spring Boot apps. You fit find am for Activity Bar for left side for VS Code (look for Spring Boot icon).

For Spring Boot Dashboard, you fit:
- See all Spring Boot apps dat dey for workspace
- Start or stop apps with one click
- View application logs live
- Check how app status dey go

Just click play button beside "rag" to start dis module, or start all modules together.

<img src="../../../translated_images/pcm/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

**Option 2: Use shell scripts**

Start all web apps (modules 01-04):

**Bash:**
```bash
cd ..  # From root directory
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # From root folder
.\start-all.ps1
```

Or start only this module:

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

Both scripts go automatically load environment variables from root `.env` file and go build JARs if dem no dey.

> **Note:** If you want build all modules manually before start:
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


## How To Use Di Application

Di application get web interface for document upload and asking question.

<a href="images/rag-homepage.png"><img src="../../../translated_images/pcm/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Di RAG application interface - upload documents and ask questions*

### How To Upload Document

Start by uploading document - TXT files dem dey best for testing. A `sample-document.txt` dey this folder wey get info about LangChain4j features, RAG implementation, and best ways - perfect for system testing.

System go process your document, break am into chunks, and create embeddings for each chunk. This one go happen automatically once you upload.

### Ask Questions

Now ask specific questions about di document content. Try ask factual questions wey clear for the document. The system go search correct chunks, put dem inside prompt and generate answer.

### Check Source References

You go see say each answer get source references with similarity scores. These scores (0 to 1) show how close chunk match your question. Higher scores mean better match. This one make you fit verify answer with source material.

<a href="images/rag-query-results.png"><img src="../../../translated_images/pcm/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Query results wey show answer with source references and relevance scores*

### Try Different Kain Questions

Try different types of questions:
- Specific facts: "Wetin be main topic?"
- Comparisons: "Wetin be difference between X and Y?"
- Summaries: "Summarize key points about Z"

Watch how di relevance scores dey change depending on how well your question match document content.

## Key Concepts

### Chunking Strategy

Documents dey split into 300-token chunks with 30 tokens overlap. Dis balance make each chunk get enough context to meaningful but still small enough to fit many chunks for prompt.

### Similarity Scores

Every retrieved chunk get similarity score between 0 and 1 wey show how close e match user question. Di diagram below show how di score range go and how system dey use am to filter results:

<img src="../../../translated_images/pcm/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

Score range from 0 to 1:
- 0.7-1.0: High relevant, exact match
- 0.5-0.7: Relevant, good context
- Below 0.5: Filter out, no too similar

System go only bring chunks wey get above minimum threshold to guarantee quality.

### In-Memory Storage

Dis module dey use in-memory storage to make am easy. If you restart app, all uploaded documents go lost. For production, people dey use durable vector databases like Qdrant or Azure AI Search.

### How To Manage Context Window

Every model get maximum context window. You no fit put all chunks from big document. System go retrieve top N most relevant chunks (default na 5) to stay within limit and provide enough context for correct answer.

## When RAG Dey Important

RAG no be always dey best approach. Di decision guide below go help you know when RAG go add value versus when simpler kain like directly put content for prompt or rely on model built-in knowledge go suffice:

<img src="../../../translated_images/pcm/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

**Use RAG when:**
- Answer question dem about proprietary documents
- Information dey change quick quick (policies, prices, specifications)
- Accuracy need say source gats e be
- Content too big to fit inside one prompt
- You need answer wey fit verify and get pin

**No use RAG when:**
- Questions need general knowledge wey model don get before
- Need real-time data (RAG dey use uploaded documents)
- Content small reach make e fit inside prompt sharply

## Next Steps

**Next Module:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigation:** [← Previous: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Back to Main](../README.md) | [Next: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document dem translate am wit AI translation service wey dem call [Co-op Translator](https://github.com/Azure/co-op-translator). Even though we dey try make am correct, abeg sabi say automated translation fit get errors or wahala. Di original document wey dem write for im correct language na di real oga. If na serious matter, e better make human professional translate am. We no go hold ourselves responsible if pesin miss understanding or take am anyhow because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->