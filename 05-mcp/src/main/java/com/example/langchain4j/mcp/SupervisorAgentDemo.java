package com.example.langchain4j.mcp;

import java.time.Duration;
import java.util.List;

import com.example.langchain4j.mcp.agents.FileAgent;
import com.example.langchain4j.mcp.agents.ReportAgent;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.observability.AgentInvocationError;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.AgentRequest;
import dev.langchain4j.agentic.observability.AgentResponse;
import dev.langchain4j.agentic.scope.AgenticScope;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.agentic.supervisor.SupervisorAgent;
import dev.langchain4j.agentic.supervisor.SupervisorAgentServiceImpl;
import dev.langchain4j.agentic.supervisor.SupervisorResponseStrategy;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatModel;
import dev.langchain4j.service.tool.ToolProvider;

/**
 * Supervisor Agent Demo with AgenticScope.
 * 
 * Demonstrates:
 * - Supervisor pattern: LLM autonomously decides which agents to invoke
 * - AgenticScope: Shared context for agents to store/retrieve intermediate results
 * 
 * Agents store results via @Agent(outputKey="...") and access them through the scope.
 */
public class SupervisorAgentDemo {

    // Azure OpenAI configuration (same as modules 01-04)
    private static final String AZURE_OPENAI_ENDPOINT = System.getenv("AZURE_OPENAI_ENDPOINT");
    private static final String AZURE_OPENAI_API_KEY = System.getenv("AZURE_OPENAI_API_KEY");
    private static final String AZURE_OPENAI_DEPLOYMENT = System.getenv("AZURE_OPENAI_DEPLOYMENT");
    
    private static final String ALLOWED_DIRECTORY = System.getProperty("user.dir");

    public static void main(String[] args) throws Exception {
        validateEnvironment();

        try (McpClient mcpClient = createMcpClient()) {
            ChatModel model = createChatModel();
            ToolProvider mcpTools = McpToolProvider.builder()
                    .mcpClients(List.of(mcpClient))
                    .build();

            // Build agents with clear separation of concerns:
            // FileAgent: reads files (has MCP tools)
            // ReportAgent: generates structured reports (pure LLM)
            FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
                    .chatModel(model)
                    .toolProvider(mcpTools)
                    .build();

            ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
                    .chatModel(model)
                    .build();

            // Build supervisor for the file → report workflow
            // Uses only 2 agents to demonstrate clear data flow:
            //   FileAgent (reads file → stores in 'fileContent')
            //   ReportAgent (reads 'fileContent' → generates 'report')
            SupervisorAgent supervisor = ((SupervisorAgentServiceImpl<SupervisorAgent>) AgenticServices.supervisorBuilder())
                    .chatModel(model)
                    .subAgents(fileAgent, reportAgent)
                    .responseStrategy(SupervisorResponseStrategy.LAST)
                    .listener(createAgentListener())
                    .build();

            // Invoke supervisor - the AgenticScope is automatically created and managed
            String filePath = ALLOWED_DIRECTORY + "/src/main/resources/file.txt";
            String request = "Read the file at " + filePath + " and generate a report on its contents";

            printHeader("FILE → REPORT WORKFLOW DEMO");
            System.out.println("This demo shows a clear 2-step workflow: read a file, then generate a report.");
            System.out.println("The Supervisor orchestrates the agents automatically based on the request.\n");
            
            printSection("WORKFLOW");
            System.out.println("  ┌─────────────┐      ┌──────────────┐");
            System.out.println("  │  FileAgent  │ ───▶ │ ReportAgent  │");
            System.out.println("  │ (MCP tools) │      │  (pure LLM)  │");
            System.out.println("  └─────────────┘      └──────────────┘");
            System.out.println("   outputKey:           outputKey:");
            System.out.println("   'fileContent'        'report'\n");
            
            printSection("AVAILABLE AGENTS");
            System.out.println("  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'");
            System.out.println("  [REPORT] ReportAgent - Generates structured report → stores in 'report'\n");
            
            printSection("USER REQUEST");
            System.out.println("  \"" + request + "\"\n");
            
            printSection("SUPERVISOR ORCHESTRATION");
            System.out.println("  The Supervisor decides which agents to invoke and passes data between them...\n");

            // ResultWithAgenticScope provides access to both the result and the shared scope
            ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
            
            printSection("FINAL RESPONSE");
            System.out.println(stripMarkdown(result.result()));

            // Access agent outputs from the AgenticScope
            AgenticScope scope = result.agenticScope();
            printSection("AGENTIC SCOPE (Data Flow)");
            System.out.println("  Each agent stores its output for downstream agents to consume:");
            printScopeValue(scope, "fileContent");
            printScopeValue(scope, "report");
            
            System.out.println("\n" + "=".repeat(70));

            // Force exit: OkHttp connection pool threads from the chat model (and possibly MCP stdio transport threads)
            // keep non-daemon threads alive, so we terminate the JVM explicitly for this demo.
            // Must be called INSIDE the try block — mcpClient.close() can hang waiting for the
            // stdio child process to terminate, which would prevent System.exit() from being reached.
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

    private static McpClient createMcpClient() {
        String npmCmd = System.getProperty("os.name").toLowerCase().contains("win") ? "npm.cmd" : "npm";
        McpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of(npmCmd, "exec", "@modelcontextprotocol/server-filesystem@2025.12.18", ALLOWED_DIRECTORY))
                .logEvents(false)
                .build();

        return new DefaultMcpClient.Builder()
                .transport(transport)
                .clientName("supervisor-agent-demo")
                .clientVersion("1.0.0")
                .toolExecutionTimeout(Duration.ofSeconds(120))
                .build();
    }

    private static ChatModel createChatModel() {
        return OpenAiOfficialChatModel.builder()
                .baseUrl(AZURE_OPENAI_ENDPOINT)
                .apiKey(AZURE_OPENAI_API_KEY)
                .modelName(AZURE_OPENAI_DEPLOYMENT)
                .timeout(Duration.ofMinutes(10))
                .maxRetries(5)
                .build();
    }

    private static void printHeader(String title) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("  " + title);
        System.out.println("=".repeat(70) + "\n");
    }
    
    private static void printSection(String title) {
        System.out.println("--- " + title + " " + "-".repeat(Math.max(0, 65 - title.length())));
    }

    private static void printScopeValue(AgenticScope scope, String key) {
        Object value = scope.readState(key);
        if (value != null) {
            String strValue = stripMarkdown(value.toString());
            String truncated = strValue.length() > 150 ? strValue.substring(0, 150) + "..." : strValue;
            System.out.println("  * " + key + ": " + truncated);
        }
    }

    /**
     * Strips Markdown formatting and special characters from text for clean console output.
     */
    private static String stripMarkdown(String text) {
        if (text == null) return "";
        return text
            .replaceAll("\\*\\*([^*]+)\\*\\*", "$1")  // Bold **text**
            .replaceAll("\\*([^*]+)\\*", "$1")        // Italic *text*
            .replaceAll("__([^_]+)__", "$1")          // Bold __text__
            .replaceAll("_([^_]+)_", "$1")            // Italic _text_
            .replaceAll("#{1,6}\\s*", "")             // Headers # ## ###
            .replaceAll("`([^`]+)`", "$1")            // Inline code `text`
            .replaceAll("```[\\s\\S]*?```", "")       // Code blocks
            .replaceAll("\\[([^\\]]+)\\]\\([^)]+\\)", "$1")  // Links [text](url)
            .replaceAll("(?m)^[-*+]\\s+", "- ")       // List items (multiline)
            .replaceAll("(?m)^>\\s*", "")             // Blockquotes (multiline)
            .replaceAll("\\n{3,}", "\n\n")            // Multiple newlines
            .trim();
    }

    private static AgentListener createAgentListener() {
        return new AgentListener() {
            private int step = 0;
            
            @Override
            public void beforeAgentInvocation(AgentRequest request) {
                String agentName = request.agentName();
                
                // Skip the internal "invoke" wrapper
                if ("invoke".equals(agentName)) {
                    return;
                }
                
                step++;
                System.out.println();
                System.out.println("  +-- STEP " + step + ": Supervisor chose -> " + getAgentDisplayName(agentName));
                System.out.println("  |");
                
                // Show what input the agent received
                var inputs = request.inputs();
                if (inputs != null && !inputs.isEmpty()) {
                    inputs.forEach((k, v) -> {
                        String val = v != null ? v.toString() : "null";
                        String truncated = val.length() > 60 ? val.substring(0, 60) + "..." : val;
                        System.out.println("  |   Input: " + truncated);
                    });
                }
            }

            @Override
            public void afterAgentInvocation(AgentResponse response) {
                String agentName = response.agentName();
                
                // Skip the internal "invoke" wrapper
                if ("invoke".equals(agentName)) {
                    return;
                }
                
                String result = response.output() != null ? stripMarkdown(response.output().toString()) : "null";
                String truncated = result.length() > 80 ? result.substring(0, 80) + "..." : result;
                System.out.println("  |");
                System.out.println("  |   Result: " + truncated);
                System.out.println("  +-- [OK] " + getAgentDisplayName(agentName) + " completed");
            }

            @Override
            public void onAgentInvocationError(AgentInvocationError error) {
                System.out.println("  +-- [ERROR] " + error.agent().name() + ": " + error.error().getMessage());
            }

            @Override
            public boolean inheritedBySubagents() {
                return true;
            }
            
            private String getAgentDisplayName(String agentName) {
                return switch (agentName) {
                    case "readFile" -> "FileAgent (reading file via MCP)";
                    case "generateReport" -> "ReportAgent (generating structured report)";
                    default -> agentName;
                };
            }
        };
    }
}
