# Module 03: RAG (Retrieval-Augmented Generation)

## Table of Contents

- [What You'll Learn](#what-youll-learn)
- [Understanding RAG](#understanding-rag)
- [Prerequisites](#prerequisites)
- [How It Works](#how-it-works)
  - [Document Processing](#document-processing)
  - [Creating Embeddings](#creating-embeddings)
  - [Semantic Search](#semantic-search)
  - [Answer Generation](#answer-generation)
- [Run the Application](#run-the-application)
- [Using the Application](#using-the-application)
  - [Upload a Document](#upload-a-document)
  - [Ask Questions](#ask-questions)
  - [Check Source References](#check-source-references)
  - [Experiment with Questions](#experiment-with-questions)
- [Key Concepts](#key-concepts)
  - [Chunking Strategy](#chunking-strategy)
  - [Similarity Scores](#similarity-scores)
  - [In-Memory Storage](#in-memory-storage)
  - [Context Window Management](#context-window-management)
- [When RAG Matters](#when-rag-matters)
- [Next Steps](#next-steps)

## What You'll Learn

In the previous modules, you learned how to have conversations with AI and structure your prompts effectively. But there's a fundamental limitation: language models only know what they learned during training. They can't answer questions about your company's policies, your project documentation, or any information they weren't trained on.

RAG (Retrieval-Augmented Generation) solves this problem. Instead of trying to teach the model your information (which is expensive and impractical), you give it the ability to search through your documents. When someone asks a question, the system finds relevant information and includes it in the prompt. The model then answers based on that retrieved context.

Think of RAG as giving the model a reference library. When you ask a question, the system:

1. **User Query** - You ask a question
2. **Embedding** - Converts your question to a vector
3. **Vector Search** - Finds similar document chunks
4. **Context Assembly** - Adds relevant chunks to the prompt
5. **Response** - LLM generates an answer based on the context

This grounds the model's responses in your actual data instead of relying on its training knowledge or making up answers.

## Understanding RAG

The diagram below illustrates the core concept: instead of relying on the model's training data alone, RAG gives it a reference library of your documents to consult before generating each answer.

<img src="images/what-is-rag.png" alt="What is RAG" width="800"/>

Here's how the pieces connect end-to-end. A user's question flows through four stages — embedding, vector search, context assembly, and answer generation — each building on the previous one:

<img src="images/rag-architecture.png" alt="RAG Architecture" width="800"/>

The rest of this module walks through each stage in detail, with code you can run and modify.

## Prerequisites

- Completed Module 01 (Azure OpenAI resources deployed)
- `.env` file in root directory with Azure credentials (created by `azd up` in Module 01)

> **Note:** If you haven't completed Module 01, follow the deployment instructions there first.


## How It Works

### Document Processing

[DocumentService.java](src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

When you upload a document, the system breaks it into chunks - smaller pieces that fit comfortably in the model's context window. These chunks overlap slightly so you don't lose context at the boundaries.

```java
Document document = FileSystemDocumentLoader.loadDocument("sample-document.txt");

DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30, new OpenAiTokenizer());

List<TextSegment> segments = splitter.split(document);
```

The diagram below shows how this works visually. Notice how each chunk shares some tokens with its neighbors — the 30-token overlap ensures no important context falls between the cracks:

<img src="images/document-chunking.png" alt="Document Chunking" width="800"/>

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`DocumentService.java`](src/main/java/com/example/langchain4j/rag/service/DocumentService.java) and ask:
> - "How does LangChain4j split documents into chunks and why is overlap important?"
> - "What's the optimal chunk size for different document types and why?"
> - "How do I handle documents in multiple languages or with special formatting?"

### Creating Embeddings

[LangChainRagConfig.java](src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

Each chunk is converted into a numerical representation called an embedding - essentially a mathematical fingerprint that captures the meaning of the text. Similar text produces similar embeddings.

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

The class diagram below shows how these LangChain4j components connect. `OpenAiOfficialEmbeddingModel` converts text into vectors, `InMemoryEmbeddingStore` holds the vectors alongside their original `TextSegment` data, and `EmbeddingSearchRequest` controls retrieval parameters like `maxResults` and `minScore`:

<img src="images/rag-langchain4j-classes.png" alt="LangChain4j RAG Classes" width="800"/>

Once embeddings are stored, similar content naturally clusters together in vector space. The visualization below shows how documents about related topics end up as nearby points, which is what makes semantic search possible:

<img src="images/vector-embeddings.png" alt="Vector Embeddings Space" width="800"/>

### Semantic Search

[RagService.java](src/main/java/com/example/langchain4j/rag/service/RagService.java)

When you ask a question, your question also becomes an embedding. The system compares your question's embedding against all the document chunks' embeddings. It finds the chunks with the most similar meanings - not just matching keywords, but actual semantic similarity.

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

List<EmbeddingMatch<TextSegment>> matches = 
    embeddingStore.findRelevant(queryEmbedding, 5, 0.7);

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```

The diagram below contrasts semantic search with traditional keyword search. A keyword search for "vehicle" misses a chunk about "cars and trucks," but semantic search understands they mean the same thing and returns it as a high-scoring match:

<img src="images/semantic-search.png" alt="Semantic Search" width="800"/>

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`RagService.java`](src/main/java/com/example/langchain4j/rag/service/RagService.java) and ask:
> - "How does similarity search work with embeddings and what determines the score?"
> - "What similarity threshold should I use and how does it affect results?"
> - "How do I handle cases where no relevant documents are found?"

### Answer Generation

[RagService.java](src/main/java/com/example/langchain4j/rag/service/RagService.java)

The most relevant chunks are assembled into a structured prompt that includes explicit instructions, the retrieved context, and the user's question. The model reads those specific chunks and answers based on that information — it can only use what's in front of it, which prevents hallucination.

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

The diagram below shows this assembly in action — the top-scoring chunks from the search step are injected into the prompt template, and the `OpenAiOfficialChatModel` generates a grounded answer:

<img src="images/context-assembly.png" alt="Context Assembly" width="800"/>

## Run the Application

**Verify deployment:**

Ensure the `.env` file exists in root directory with Azure credentials (created during Module 01):
```bash
cat ../.env  # Should show AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT
```

**Start the application:**

> **Note:** If you already started all applications using `./start-all.sh` from Module 01, this module is already running on port 8081. You can skip the start commands below and go directly to http://localhost:8081.

**Option 1: Using Spring Boot Dashboard (Recommended for VS Code users)**

The dev container includes the Spring Boot Dashboard extension, which provides a visual interface to manage all Spring Boot applications. You can find it in the Activity Bar on the left side of VS Code (look for the Spring Boot icon).

From the Spring Boot Dashboard, you can:
- See all available Spring Boot applications in the workspace
- Start/stop applications with a single click
- View application logs in real-time
- Monitor application status

Simply click the play button next to "rag" to start this module, or start all modules at once.

<img src="images/dashboard.png" alt="Spring Boot Dashboard" width="400"/>

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

Or start just this module:

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

Both scripts automatically load environment variables from the root `.env` file and will build the JARs if they don't exist.

> **Note:** If you prefer to build all modules manually before starting:
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

Open http://localhost:8081 in your browser.

**To stop:**

**Bash:**
```bash
./stop.sh  # This module only
# Or
cd .. && ./stop-all.sh  # All modules
```

**PowerShell:**
```powershell
.\stop.ps1  # This module only
# Or
cd ..; .\stop-all.ps1  # All modules
```

## Using the Application

The application provides a web interface for document upload and questioning.

<a href="images/rag-homepage.png"><img src="images/rag-homepage.png" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*The RAG application interface - upload documents and ask questions*

### Upload a Document

Start by uploading a document - TXT files work best for testing. A `sample-document.txt` is provided in this directory that contains information about LangChain4j features, RAG implementation, and best practices - perfect for testing the system. 

The system processes your document, breaks it into chunks, and creates embeddings for each chunk. This happens automatically when you upload.

### Ask Questions

Now ask specific questions about the document content. Try something factual that's clearly stated in the document. The system searches for relevant chunks, includes them in the prompt, and generates an answer.

### Check Source References

Notice each answer includes source references with similarity scores. These scores (0 to 1) show how relevant each chunk was to your question. Higher scores mean better matches. This lets you verify the answer against the source material.

<a href="images/rag-query-results.png"><img src="images/rag-query-results.png" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*Query results showing answer with source references and relevance scores*

### Experiment with Questions

Try different types of questions:
- Specific facts: "What is the main topic?"
- Comparisons: "What's the difference between X and Y?"
- Summaries: "Summarize the key points about Z"

Watch how the relevance scores change based on how well your question matches document content.

## Key Concepts

### Chunking Strategy

Documents are split into 300-token chunks with 30 tokens of overlap. This balance ensures each chunk has enough context to be meaningful while staying small enough to include multiple chunks in a prompt.

### Similarity Scores

Every retrieved chunk comes with a similarity score between 0 and 1 that indicates how closely it matches the user's question. The diagram below visualizes the score ranges and how the system uses them to filter results:

<img src="images/similarity-scores.png" alt="Similarity Scores" width="800"/>

Scores range from 0 to 1:
- 0.7-1.0: Highly relevant, exact match
- 0.5-0.7: Relevant, good context
- Below 0.5: Filtered out, too dissimilar

The system only retrieves chunks above the minimum threshold to ensure quality.

### In-Memory Storage

This module uses in-memory storage for simplicity. When you restart the application, uploaded documents are lost. Production systems use persistent vector databases like Qdrant or Azure AI Search.

### Context Window Management

Each model has a maximum context window. You can't include every chunk from a large document. The system retrieves the top N most relevant chunks (default 5) to stay within limits while providing enough context for accurate answers.

## When RAG Matters

RAG isn't always the right approach. The decision guide below helps you determine when RAG adds value versus when simpler approaches — like including content directly in the prompt or relying on the model's built-in knowledge — are sufficient:

<img src="images/when-to-use-rag.png" alt="When to Use RAG" width="800"/>

**Use RAG when:**
- Answering questions about proprietary documents
- Information changes frequently (policies, prices, specifications)
- Accuracy requires source attribution
- Content is too large to fit in a single prompt
- You need verifiable, grounded responses

**Don't use RAG when:**
- Questions require general knowledge the model already has
- Real-time data is needed (RAG works on uploaded documents)
- Content is small enough to include directly in prompts

## Next Steps

**Next Module:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigation:** [← Previous: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Back to Main](../README.md) | [Next: Module 04 - Tools →](../04-tools/README.md)