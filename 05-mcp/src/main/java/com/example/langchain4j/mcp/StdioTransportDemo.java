package com.example.langchain4j.mcp;

import java.io.File;
import java.time.Duration;
import java.util.List;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;

/**
 * StdioTransportDemo - MCP Integration via Stdio Transport
 * Run: mvn exec:java -Dexec.mainClass="com.example.langchain4j.mcp.StdioTransportDemo"
 * 
 * Demonstrates MCP integration via stdio transport for filesystem operations.
 * 
 * This example shows how to spawn a local MCP server as a subprocess,
 * communicate through standard input/output, and use its tools for
 * file system operations.
 * 
 * Prerequisites:
 * - npm installed
 * - Azure OpenAI environment variables configured (same as Modules 01-04):
 *   AZURE_OPENAI_ENDPOINT, AZURE_OPENAI_API_KEY, AZURE_OPENAI_DEPLOYMENT
 * 
 * Run this example:
 * 1. Configure .env file with Azure OpenAI variables
 * 2. cd 05-mcp
 * 3. ./start-stdio.ps1 (PowerShell) or ./start-stdio.sh (Bash)
 * 
 * Key Concepts:
 * - StdioMcpTransport for local subprocess servers
 * - Process spawning and lifecycle management
 * - File system tool integration
 * - Cross-platform path handling
 * 
 * 💡 Ask GitHub Copilot:
 * - "How does Stdio transport work and when should I use it vs HTTP?"
 * - "How does LangChain4j manage the lifecycle of spawned MCP server processes?"
 * - "What are the security implications of giving AI access to the file system?"
 * - "How can I limit which directories or files the MCP filesystem server can access?"
 */
public class StdioTransportDemo {

    private static final String TARGET_FILE = "src/main/resources/file.txt";
    
    // Azure OpenAI configuration (same as modules 01-04)
    private static final String AZURE_OPENAI_ENDPOINT = System.getenv("AZURE_OPENAI_ENDPOINT");
    private static final String AZURE_OPENAI_API_KEY = System.getenv("AZURE_OPENAI_API_KEY");
    private static final String AZURE_OPENAI_DEPLOYMENT = System.getenv("AZURE_OPENAI_DEPLOYMENT");

    public static void main(String[] args) throws Exception {
        validateEnvironment();

        // Configure chat model
        ChatModel chatModel = buildChatModel();

        // Setup stdio transport with filesystem server
        McpTransport stdioTransport = buildStdioTransport();

        // Initialize MCP client with try-with-resources for automatic cleanup
        try (McpClient client = buildMcpClient(stdioTransport)) {
            
            // Create tool provider
            ToolProvider tools = McpToolProvider.builder()
                    .mcpClients(List.of(client))
                    .build();

            // Build AI service
            Bot assistant = AiServices.builder(Bot.class)
                    .chatModel(chatModel)
                    .toolProvider(tools)
                    .build();

            File targetFile = new File(TARGET_FILE);
            String query = String.format(
                "Read and summarize the content from: %s", 
                targetFile.getAbsolutePath()
            );
            String result = assistant.chat(query);
            System.out.println("Assistant response: " + result);

            // Force exit: must be INSIDE the try block because mcpClient.close()
            // can hang waiting for the stdio child process to terminate.
            System.exit(0);
        }
    }

    private static void validateEnvironment() {
        if (AZURE_OPENAI_ENDPOINT == null || AZURE_OPENAI_ENDPOINT.isBlank()) {
            System.err.println("Error: AZURE_OPENAI_ENDPOINT environment variable not set");
            System.exit(1);
        }
        if (AZURE_OPENAI_API_KEY == null || AZURE_OPENAI_API_KEY.isBlank()) {
            System.err.println("Error: AZURE_OPENAI_API_KEY environment variable not set");
            System.exit(1);
        }
        if (AZURE_OPENAI_DEPLOYMENT == null || AZURE_OPENAI_DEPLOYMENT.isBlank()) {
            System.err.println("Error: AZURE_OPENAI_DEPLOYMENT environment variable not set");
            System.exit(1);
        }
    }

    private static ChatModel buildChatModel() {
        return OpenAiOfficialChatModel.builder()
                .baseUrl(AZURE_OPENAI_ENDPOINT)
                .apiKey(AZURE_OPENAI_API_KEY)
                .modelName(AZURE_OPENAI_DEPLOYMENT)
                .strictTools(false)
                .build();
    }

    private static McpTransport buildStdioTransport() {
        // Determine npm command based on OS
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase()
                .contains("win");
        String npmCmd = isWindows ? "npm.cmd" : "npm";

        // Get absolute path to resources directory
        File resourcesFile = new File("src/main/resources");
        if (!resourcesFile.exists()) {
            throw new RuntimeException(
                "Resources directory not found. Current working directory: " + 
                new File(".").getAbsolutePath() + 
                ". Please run from the 05-mcp directory."
            );
        }
        
        // Normalize path for Node.js (use forward slashes even on Windows)
        String resourcesDir = resourcesFile.getAbsolutePath()
                .replace("\\", "/");

        return new StdioMcpTransport.Builder()
                .command(List.of(
                    npmCmd, "exec",
                    "@modelcontextprotocol/server-filesystem@2026.1.14",
                    resourcesDir
                ))
                .logEvents(false)
                .build();
    }

    private static McpClient buildMcpClient(McpTransport transport) {
        return new DefaultMcpClient.Builder()
                .transport(transport)
                .autoHealthCheck(false)
                .initializationTimeout(Duration.ofSeconds(20))
                .toolExecutionTimeout(Duration.ofSeconds(20))
                .build();
    }
}
