package com.example.langchain4j.quickstart;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;

/**
 * SimpleReaderDemo - Easy RAG (Retrieval-Augmented Generation)
 * Run: mvn exec:java -Dexec.mainClass="com.example.langchain4j.quickstart.SimpleReaderDemo"
 *
 * Demonstrates an "Easy RAG" approach using LangChain4j's built-in support:
 * 1. Load documents from the file system
 * 2. Ingest them into an in-memory embedding store (splitting + embedding handled automatically)
 * 3. Create an AI assistant backed by a content retriever over those embeddings
 * 4. Chat with the assistant — relevant document chunks are retrieved automatically
 *
 * The "langchain4j-easy-rag" module hides the complexity of parsing, splitting,
 * and embedding so you can focus on the application logic.
 *
 * Key Concepts:
 * - FileSystemDocumentLoader for loading documents
 * - EmbeddingStoreIngestor for automatic splitting and embedding
 * - EmbeddingStoreContentRetriever for semantic retrieval
 * - AiServices for creating a type-safe AI assistant with RAG
 *
 * 💡 Ask GitHub Copilot:
 * - "How does RAG prevent AI hallucinations compared to using the model's training data?"
 * - "What's the difference between this easy approach and a custom RAG pipeline?"
 * - "How would I scale this to handle multiple documents or larger knowledge bases?"
 */
public class SimpleReaderDemo {

    /**
     * Simple assistant interface used by AiServices to generate a type-safe proxy.
     * Any method that accepts a String and returns a String can be used.
     */
    interface Assistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {
        // --- Validate environment ---
        String apiKey = System.getenv("GITHUB_TOKEN");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("Error: GITHUB_TOKEN environment variable is not set or is empty.");
            System.exit(1);
        }

        // --- 1. Load documents ---
        // Look for *.txt files in several candidate directories
        Path documentsDir = resolveDocumentsDir();
        System.out.println("Loading documents from: " + documentsDir);
        List<Document> documents = loadDocuments(documentsDir);
        System.out.println("Loaded " + documents.size() + " document(s).");

        // --- 2. Create the chat model (GitHub Models / OpenAI-compatible) ---
        OpenAiOfficialChatModel chatModel = OpenAiOfficialChatModel.builder()
                .baseUrl("https://models.github.ai/inference")
                .apiKey(apiKey)
                .modelName("gpt-4.1-nano")
                .build();

        // --- 3. Build an Easy RAG assistant ---
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(createContentRetriever(documents))
                .build();

        // --- 4. Interactive conversation loop ---
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("\nAsk questions about the loaded documents (type 'exit' to quit):\n");
            while (true) {
                System.out.print("You: ");
                String question = scanner.nextLine();
                if (question == null || question.trim().equalsIgnoreCase("exit")) {
                    break;
                }
                String answer = assistant.chat(question);
                System.out.println("\nAssistant: " + answer + "\n");
            }
        }
    }

    /**
     * Creates a {@link ContentRetriever} backed by an in-memory embedding store.
     * The "easy-rag" module supplies default splitter and embedding model so that
     * a single call to {@code EmbeddingStoreIngestor.ingest} handles everything.
     */
    private static ContentRetriever createContentRetriever(List<Document> documents) {
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);
        return EmbeddingStoreContentRetriever.from(embeddingStore);
    }

    /** Resolve the directory that contains the *.txt documents to ingest. */
    private static Path resolveDocumentsDir() {
        // Look for the known sample document in candidate directories.
        // "00-quick-start" covers running from the repo root;
        // "." covers running from inside the 00-quick-start folder.
        for (String candidate : new String[]{"00-quick-start", "."}) {
            Path doc = Paths.get(candidate, "document.txt");
            if (doc.toFile().isFile()) {
                return Paths.get(candidate);
            }
        }
        // Fallback — current directory (FileSystemDocumentLoader will throw if empty)
        return Paths.get(".");
    }
}
