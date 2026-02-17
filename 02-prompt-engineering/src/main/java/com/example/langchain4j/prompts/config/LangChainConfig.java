package com.example.langchain4j.prompts.config;

import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialStreamingChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;

/**
 * Configuration for LangChain4j with GPT-5 support.
 * 
 * Note: For GPT-5 reasoning effort is controlled through prompt engineering
 * rather than model configuration parameters. See Gpt5PromptService for examples of how to
 * use prompts like "<reasoning_effort>low</reasoning_effort>" to control model behavior.
 * 
 */
@Configuration
public class LangChainConfig {

    @Value("${AZURE_OPENAI_ENDPOINT}")
    private String azureEndpoint;

    @Value("${AZURE_OPENAI_API_KEY}")
    private String azureApiKey;

    @Value("${AZURE_OPENAI_DEPLOYMENT}")
    private String deploymentName; // Azure deployment name, used as modelName

    /**
     * Primary chat model for general use.
     */
    @Bean
    @Primary
    public OpenAiOfficialChatModel chatLanguageModel() {
        return OpenAiOfficialChatModel.builder()
                .baseUrl(azureEndpoint)
                .apiKey(azureApiKey)
                .modelName(deploymentName)
                .timeout(Duration.ofMinutes(5))  // GPT-5 reasoning can take time
                .maxRetries(3)
                .build();
    }

    /**
     * Streaming chat model for real-time token-by-token responses.
     * Use for high eagerness or long-running prompts to avoid timeouts.
     */
    @Bean
    public OpenAiOfficialStreamingChatModel streamingChatModel() {
        return OpenAiOfficialStreamingChatModel.builder()
                .baseUrl(azureEndpoint)
                .apiKey(azureApiKey)
                .modelName(deploymentName)
                .timeout(Duration.ofMinutes(10))
                .build();
    }
}
