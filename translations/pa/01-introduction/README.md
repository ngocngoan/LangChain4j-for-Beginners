# Module 01: LangChain4j ਨਾਲ ਸ਼ੁਰੂਆਤ

## ਸੂਚੀ

- [ਵੀਡੀਓ ਚਾਲੂ](../../../01-introduction)
- [ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ](../../../01-introduction)
- [ਪੂਰਵ-ਆਵਸ਼ਕਤਾਵਾਂ](../../../01-introduction)
- [ਮੁੱਖ ਸਮੱਸਿਆ ਨੂੰ ਸਮਝਣਾ](../../../01-introduction)
- [ਟੋਕਨ ਸਮਝਣਾ](../../../01-introduction)
- [ਮੈਮੋਰੀ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀ ਹੈ](../../../01-introduction)
- [ਇਹ LangChain4j ਨੂੰ ਕਿਵੇਂ ਵਰਤਦਾ ਹੈ](../../../01-introduction)
- [Azure OpenAI ढਾਂਚਾ deploy ਕਰਨਾ](../../../01-introduction)
- [ਲੋਕਲ ਐਪਲੀਕੇਸ਼ਨ ਚਲਾਓ](../../../01-introduction)
- [ਐਪਲੀਕੇਸ਼ਨ ਦੀ ਵਰਤੋਂ](../../../01-introduction)
  - [Stateless ਚੈਟ (ਖੱਬਾ ਪੈਨਲ)](../../../01-introduction)
  - [Stateful ਚੈਟ (ਸੱਜਾ ਪੈਨਲ)](../../../01-introduction)
- [ਅਗਲੇ ਕਦਮ](../../../01-introduction)

## ਵੀਡੀਓ ਚਾਲੂ

ਇਸ ਲਾਈਵ ਸੈਸ਼ਨ ਨੂੰ ਦੇਖੋ ਜੋ ਦੱਸਦਾ ਹੈ ਕਿ ਇਸ ਮਾਡਿਊਲ ਨਾਲ ਸ਼ੁਰੂਆਤ ਕਿਵੇਂ ਕਰਨੀ ਹੈ: [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ

ਜੇ ਤੁਸੀਂ ਤੇਜ਼ ਸ਼ੁਰੂਆਤ ਪੂਰੀ ਕੀਤੀ, ਤਾਂ ਤੁਸੀਂ ਦੇਖਿਆ ਕਿ ਕਿਵੇਂ ਪ੍ਰਾਪਟਸ ਭੇਜੇ ਜਾਂਦੇ ਹਨ ਅਤੇ ਜਵਾਬ ਮਿਲਦੇ ਹਨ। ਇਹ ਬੁਨਿਆਦ ਹੈ, ਪਰ ਅਸਲੀ ਐਪਲੀਕੇਸ਼ਨਜ਼ ਨੂੰ ਇਸ ਤੋਂ ਵੱਧ ਚਾਹੀਦਾ ਹੈ। ਇਹ ਮਾਡਿਊਲ ਤੁਹਾਨੂੰ ਸਿਖਾਉਂਦਾ ਹੈ ਕਿ ਕਿਵੇਂ ਐਸਾ ਸੰਵਾਦਾਤਮਕ AI ਬਣਾਉਣਾ ਹੈ ਜੋ ਸੰਦਰਭ ਨੂੰ ਯਾਦ ਰੱਖਦਾ ਹੈ ਅਤੇ ਸਥਿਤੀ ਬਰਕਰਾਰ ਰੱਖਦਾ ਹੈ — ਇਕ ਵਾਰ ਦੀ ਡੈਮੋ ਅਤੇ ਪ੍ਰੋਡਕਸ਼ਨ-ਤਿਆਰ ਐਪਲੀਕੇਸ਼ਨ ਵਿੱਚ ਅੰਤਰ।

ਅਸੀਂ ਇਸ ਗਾਈਡ ਵਿੱਚ ਸਾਰਿਆਂ ਨੂੰ Azure OpenAI ਦਾ GPT-5.2 ਵਰਤਾਂਗੇ ਕਿਉਂਕਿ ਇਸ ਦੀ ਉੱਨਤ ਤਰਕਸ਼ੀਲ ਖ਼ੁਬੀਆਂ ਵੱਖਰੇ ਪੈਟਰਨ ਦੇ ਵਿਹਾਰ ਨੂੰ ਵੱਧ ਸਪਸ਼ਟ ਬਣਾ ਦਿੰਦੀਆਂ ਹਨ। ਜਦ ਤੁਸੀਂ ਮੈਮੋਰੀ ਜੋੜਦੇ ਹੋ, ਤਾਂ ਤੁਹਾਨੂੰ ਫਰਕ ਚੰਗੀ ਤਰ੍ਹਾਂ ਦਿਖਾਈ ਦੇਵੇਗਾ। ਇਸ ਨਾਲ ਇਹ ਸਮਝਣਾ ਆਸਾਨ ਹੋ ਜਾਵੇਗਾ ਕਿ ਹਰ ਇਕ ਭਾਗ ਤੁਹਾਡੇ ਐਪਲੀਕੇਸ਼ਨ ਵਿੱਚ ਕੀ ਲਿਆਉਂਦਾ ਹੈ।

ਤੁਸੀਂ ਇਕ ਐਪਲੀਕੇਸ਼ਨ ਬਣਾਉਂਦੇ ਹੋ ਜੋ ਦੋ ਪੈਟਰਨ ਦਿਖਾਂਦਾ ਹੈ:

**Stateless ਚੈਟ** - ਹਰ ਬਿਨਤੀ ਸੁਤੰਤਰ ਹੈ। ਮਾਡਲ ਨੂੰ ਪਹਿਲਾਂ ਦੇ ਸੁਨੇਹਿਆਂ ਦੀ ਕੋਈ ਯਾਦ ਨਹੀਂ। ਇਹੀ ਪੈਟਰਨ ਤੁਸੀਂ ਤੇਜ਼ ਸ਼ੁਰੂਆਤ ਵਿੱਚ ਵਰਤਿਆ ਸੀ।

**Stateful ਸੰਵਾਦ** - ਹਰ ਬਿਨਤੀ ਵਿੱਚ ਬਾਤਚੀਤ ਦਾ ਇਤਿਹਾਸ ਸ਼ਾਮਲ ਹੁੰਦਾ ਹੈ। ਮਾਡਲ ਕਈ ਮੁੜ-ਮੁੜ ਤਬਾਦਲੇ ਵਿੱਚ ਸੰਦਰਭ ਸੰਭਾਲਦਾ ਹੈ। ਇਹੀ ਪ੍ਰੋਡਕਸ਼ਨ ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਲੋੜ ਹੁੰਦੀ ਹੈ।

## ਪੂਰਵ-ਆਵਸ਼ਕਤਾਵਾਂ

- Azure ਦੀ ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਜਿਸ ਵਿੱਚ Azure OpenAI ਦੀ ਪਹੁੰਚ ਹੋਵੇ
- ਜਾਵਾ 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **ਨੋਟ:** ਜਾਵਾ, Maven, Azure CLI ਅਤੇ Azure Developer CLI (azd) ਪਹਿਲਾਂ ਤੋਂ devcontainer ਵਿੱਚ ਸਥਾਪਿਤ ਹਨ।

> **ਨੋਟ:** ਇਹ ਮਾਡਿਊਲ Azure OpenAI ਤੇ GPT-5.2 ਵਰਤਦਾ ਹੈ। ਡਿਪਲੋਇੰਟਮੈਂਟ `azd up` ਰਾਹੀਂ ਆਪਣੇ ਆਪ ਹੁੰਦਾ ਹੈ - ਕੋਡ ਵਿੱਚ ਮਾਡਲ ਦਾ ਨਾਮ ਬਦਲੋ ਨਾ।

## ਮੁੱਖ ਸਮੱਸਿਆ ਨੂੰ ਸਮਝਣਾ

ਭਾਸ਼ਾ ਮਾਡਲ stateless ਹੁੰਦੇ ਹਨ। ਹਰ API ਕਾਲ ਸੁਤੰਤਰ ਹੁੰਦੀ ਹੈ। ਜੇ ਤੁਸੀਂ "ਮੇਰਾ ਨਾਮ ਜੌਨ ਹੈ" ਭੇਜਦੇ ਹੋ ਅਤੇ ਫਿਰ ਪੁੱਛਦੇ ਹੋ "ਮੇਰਾ ਨਾਮ ਕੀ ਹੈ?", ਤਦ ਮਾਡਲ ਨੂੰ ਇਹ ਪਤਾ ਨਹੀਂ ਲੱਗਦਾ ਕਿ ਤੁਸੀਂ ਅਜੇ ਹੀ ਆਪਣੇ ਆਪ ਨੂੰ ਪੇਸ਼ ਕੀਤਾ ਸੀ। ਇਹ ਹਰ ਬਿਨਤੀ ਨੂੰ ਪਹਿਲੀ ਗੱਲਬਾਤ ਵਾਂਗ ਪ੍ਰਕਿਰਿਆ ਕਰਦਾ ਹੈ।

ਇਹ ਸਧਾਰਣ Q&A ਲਈ ਠੀਕ ਹੈ ਪਰ ਅਸਲੀ ਐਪਲੀਕੇਸ਼ਨਾਂ ਲਈ ਬੇਕਾਰ ਹੈ। ਗ੍ਰਾਹਕ ਸੇਵਾ ਬੋਟਸ ਨੂੰ ਯਾਦ ਰੱਖਣਾ ਪੈਂਦਾ ਹੈ ਕਿ ਤੁਸੀਂ ਕੀ ਦੱਸਿਆ। ਨਿੱਜੀ ਸਹਾਇਕਾਂ ਨੂੰ ਸੰਦਰਭ ਦੀ ਲੋੜ ਹੋਂਦੀ ਹੈ। ਕਿਸੇ ਵੀ ਕਈ ਤੋਰ ਦੀ ਗੱਲਬਾਤ ਲਈ ਮੈਮੋਰੀ ਜ਼ਰੂਰੀ ਹੈ।

<img src="../../../translated_images/pa/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Stateless (ਸੁਤੰਤਰ ਕਾਲਾਂ) ਅਤੇ Stateful (ਸੰਦਰਭ-ਜਾਣੂ) ਸੰਵਾਦ ਵਿੱਚ ਅੰਤਰ*

## ਟੋਕਨ ਸਮਝਣਾ

ਸੰਵਾਦ ਵਿੱਚ ਡੁੱਬਕੀਆਂ ਮਾਰਨ ਤੋਂ ਪਹਿਲਾਂ, ਟੋਕਨ ਨੂੰ ਸਮਝਣਾ ਜ਼ਰੂਰੀ ਹੈ - ਇਹ ਟੈਕਸਟ ਦੇ ਮੂਲ ਇਕਾਈਆਂ ਹਨ ਜੋ ਭਾਸ਼ਾ ਮਾਡਲਾਂ ਦੁਆਰਾ ਪ੍ਰਕਿਰਿਆ ਕੀਤੀਆਂ ਜਾਂਦੀਆਂ ਹਨ:

<img src="../../../translated_images/pa/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*ਜਿਵੇਂ ਟੈਕਸਟ ਟੋਕਨਾਂ ਵਿੱਚ ਤੋੜਿਆ ਜਾਂਦਾ ਹੈ - "I love AI!" ਵਿੱਚ 4 ਵੱਖ-ਵੱਖ ਪ੍ਰਕਿਰਿਆ ਇਕਾਈਆਂ ਬਣਦੀਆਂ ਹਨ*

ਟੋਕਨ ਹੀ AI ਮਾਡਲਾਂ ਦਾ ਮਾਪ ਅਤੇ ਪ੍ਰਕਿਰਿਆ ਦਾ ਸੌਜ਼ ਹਨ। ਸ਼ਬਦ, ਵਿਸ਼੍ਰਾਮ-ਚਿੰਨ੍ਹਾਂ ਅਤੇ ਖਾਲੀ ਜਗ੍ਹਾ ਵੀ ਟੋਕਨ ਹੋ ਸਕਦੇ ਹਨ। ਤੁਹਾਡੇ ਮਾਡਲ ਲਈ ਇਹ ਸੀਮਾ ਹੁੰਦੀ ਹੈ ਕਿੱਥੇ ਤੱਕ ਇਹ ਟੋਕਨਾਂ ਨੂੰ ਇਕੱਠਾ ਪ੍ਰਕਿਰਿਆ ਕਰ ਸਕਦਾ ਹੈ (GPT-5.2 ਲਈ 400,000, ਜਿਨ੍ਹਾਂ ਵਿੱਚ 272,000 ਇਨਪੁਟ ਅਤੇ 128,000 ਆਉਟਪੁਟ ਟੋਕਨ ਹਨ)। ਟੋਕਨਾਂ ਦੀ ਸਮਝ ਤੁਹਾਨੂੰ ਗੱਲਬਾਤ ਦੀ ਲੰਬਾਈ ਅਤੇ ਖਰਚੇ ਕੰਟਰੋਲ ਕਰਨ ਵਿੱਚ ਮਦਦ ਕਰਦੀ ਹੈ।

## ਮੈਮੋਰੀ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀ ਹੈ

ਚੈਟ ਮੈਮੋਰੀ stateless ਸਮੱਸਿਆ ਨੂੰ ਹੱਲ ਕਰਦਾ ਹੈ ਬਾਤਚੀਤ ਦੇ ਇਤਿਹਾਸ ਨੂੰ ਸੰਭਾਲ ਕੇ। ਮਾਡਲ ਨੂੰ ਬਿਨਤੀ ਭੇਜਣ ਤੋਂ ਪਹਿਲਾਂ, ਫਰੇਮਵਰਕ ਸਬੰਧਤ ਪਿਛਲੇ ਸੁਨੇਹੇ ਆਪਣੀ ਸਿੱਟ ਟਿਕਾਉਂਦਾ ਹੈ। ਜਦ ਤੁਸੀਂ "ਮੇਰਾ ਨਾਮ ਕੀ ਹੈ?" ਪੁੱਛਦੇ ਹੋ, ਤਦ ਸਿਸਟਮ ਅਸਲ ਵਿੱਚ ਸਾਰੀ ਗੱਲਬਾਤ ਦਾ ਇਤਿਹਾਸ ਭੇਜਦਾ ਹੈ ਜਿਸ ਨਾਲ ਮਾਡਲ ਨੂੰ ਪਤਾ ਲੱਗਦਾ ਹੈ ਕਿ ਤੁਸੀਂ ਪਹਿਲਾਂ ਕਿਹਾ ਸੀ "ਮੇਰਾ ਨਾਮ ਜੌਨ ਹੈ।"

LangChain4j ਮੈਮੋਰੀ ਇੰਪਲੀਮੈਂਟੇਸ਼ਨ ਨਾਲ ਆਉਂਦਾ ਹੈ ਜੋ ਇਸਨੂੰ ਆਪਣੇ ਆਪ ਸੰਭਾਲਦਾ ਹੈ। ਤੁਸੀਂ ਦਰਜਾ ਕਰ ਸਕਦੇ ਹੋ ਕਿ ਕਿੰਨੇ ਸੁਨੇਹੇ ਰੱਖਣੇ ਹਨ ਅਤੇ ਫਰੇਮਵਰਕ ਸੰਦਰਭ ਲਈ ਖਿੜਕੀ ਸਾਲੇ ਸਾਜਦਾ ਹੈ।

<img src="../../../translated_images/pa/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory ਹਾਲੀਆ ਸੁਨੇਹਿਆਂ ਦੀ ਖਿੜਕੀ ਰੱਖਦਾ ਹੈ, ਪੁਰਾਣੇ ਆਪਣੇ ਆਪ ਹਟਾ ਦਿੰਦਾ ਹੈ*

## ਇਹ LangChain4j ਨੂੰ ਕਿਵੇਂ ਵਰਤਦਾ ਹੈ

ਇਹ ਮਾਡਿਊਲ ਤੇਜ਼ ਸ਼ੁਰੂਆਤ ਨੂੰ ਵਧਾਉਂਦਾ ਹੈ Spring Boot ਨਾਲ ਇੰਟਗ੍ਰੇਸ਼ਨ ਅਤੇ ਗੱਲਬਾਤ ਮੈਮੋਰੀ ਸ਼ਾਮਲ ਕਰਦਾ ਹੈ। ਤਰੀਕਾ ਇਹ ਹੈ:

**Dependencies** - ਦੋ LangChain4j ਲਾਇਬ੍ਰੇਰੀਆਂ ਸ਼ਾਮਲ ਕਰੋ:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**ਚੈਟ ਮਾਡਲ** - Azure OpenAI ਨੂੰ Spring ਬੀਨ ਵਜੋਂ ਸੰਰਚਿਤ ਕਰੋ ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

ਬਿਲਡਰ ਮੂਲ ਐਕਸੈਸ ਕੀ ਸੈੱਟਿੰਗ ਤੋਂ environment variables ਤੋਂ ਪੜ੍ਹਦਾ ਹੈ ਜੋ `azd up` ਰਾਹੀਂ ਸੈੱਟ ਕੀਤੇ ਜਾਂਦੇ ਹਨ। ਆਪਣੇ Azure endpoint ਲਈ `baseUrl` ਸੈੱਟ ਕਰਕੇ OpenAI client ਨੂੰ Azure OpenAI ਲਈ ਕਾਰਗਰ ਬਣਾਓ।

**ਗੱਲਬਾਤ ਮੈਮੋਰੀ** - MessageWindowChatMemory ਨਾਲ ਚੈਟ ਇਤਿਹਾਸ ਟ੍ਰੈਕ ਕਰੋ ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

ਮੈਮੋਰੀ ਬਣਾਉਣ ਲਈ `withMaxMessages(10)` ਨਾਲ ਆਖਰੀ 10 ਸੁਨੇਹੇ ਰੱਖੋ। ਵਰਤੋਂਕਾਰ ਅਤੇ AI ਸੁਨੇਹੇ typed wrappers ਨਾਲ ਸ਼ਾਮਲ ਕਰੋ: `UserMessage.from(text)` ਅਤੇ `AiMessage.from(text)`। ਇਤਿਹਾਸ ਪ੍ਰਾਪਤ ਕਰਨ ਲਈ `memory.messages()` ਵਰਤੋ ਅਤੇ ਮਾਡਲ ਨੂੰ ਭੇਜੋ। ਸੇਵਾ ਹਰ ਗੱਲਬਾਤ ID ਲਈ ਵੱਖਰੀ ਮੈਮੋਰੀ ਸੰਭਾਲਦਾ ਹੈ, ਜੋ ਕਈ ਵਰਤੋਂਕਾਰਾਂ ਨੂੰ ਇਕੱਠੇ ਗੱਲਬਾਤ ਕਰਨ ਦੇ ਯੋਗ ਬਨਾਉਂਦਾ ਹੈ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ਚੈਟ ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** ਖੋਲ੍ਹੋ [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ਅਤੇ ਪੁੱਛੋ:
> - "ਜਦੋਂ ਮੈਸੇਜ ਵਿੰਡੋ ਪੂਰੀ ਹੋ ਜਾਵੇ ਤਾਂ MessageWindowChatMemory ਕਿਵੇਂ ਸੁਨੇਹੇ ਹਟਾਉਣ ਦਾ ਫੈਸਲਾ ਕਰਦਾ ਹੈ?"
> - "ਕੀ ਮੈਂ ਮੈਮੋਰੀ ਸਟੋਰੇਜ ਕਸਟਮ ਕਰ ਸਕਦਾ ਹਾਂ ਡੈਟਾਬੇਸ ਦੀ ਥਾਂ ਤੇ ਇਨ-ਮੇਮੋਰੀ ਦੀ ਵਰਤੋਂ ਨਾਲ?"
> - "ਪੁਰਾਣੇ conversation history ਨੂੰ ਸੰਖੇਪ ਕਰਨ ਲਈ ਸਾਰੇ ਜਾਣਕਾਰੀ ਜੋੜਨ ਲਈ ਕਿਵੇਂ summarization ਸ਼ਾਮਲ ਕਰਾਂ?"

Stateless ਚੈਟ endpoint ਸਿਰਫ਼ `chatModel.chat(prompt)` ਵਰਗਾ quick start ਵਰਗਾ ਬਿਨਾ ਮੈਮੋਰੀ ਵਰਤਦਾ ਹੈ। Stateful endpoint ਸੁਨੇਹੇ ਮੈਮੋਰੀ ਵਿੱਚ ਸ਼ਾਮਲ ਕਰਦਾ ਹੈ, ਇਤਿਹਾਸ ਪ੍ਰਾਪਤ ਕਰਦਾ ਹੈ ਅਤੇ ਬਿਨਤੀ ਨਾਲੋਂ ਸੰਦਰਭ ਸ਼ਾਮਿਲ ਕਰਦਾ ਹੈ। ਇਕੋ ਮਾਡਲ ਕਨਫਿਗਰੇਸ਼ਨ, ਵੱਖਰੇ ਪੈਟਰਨ।

## Azure OpenAI ढਾਂਚਾ deploy ਕਰੋ

**Bash:**
```bash
cd 01-introduction
azd up  # ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਅਤੇ ਸਥਾਨ ਚੁਣੋ (ਪੂਰਬੀ ਯੂਐਸ 2 ਸਿਫਾਰਸ਼ੀ)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਅਤੇ ਸਥਾਨ ਚੁਣੋ (ਪূর্বੀਅਸ2 ਸਿਫ਼ਾਰਸ਼ ਕੀਤੀ ਗਈ)
```

> **ਨੋਟ:** ਜੇ timeout error ਆਵੇ (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), ਤਾਂ `azd up` ਦੁਬਾਰਾ ਚਲਾਓ। Azure ਸਰੋਤ ਹੁਣ ਵੀ ਪ੍ਰੋਵਜ਼ਨ ਹੋ ਰਹੇ ਹੋ ਸਕਦੇ ਹਨ ਅਤੇ ਦੁਬਾਰਾ ਕੋਸ਼ਿਸ਼ ਨਾਲ ਡਿਪਲੋਇੰਟ ਖਤਮ ਹੋ ਜਾਵੇਗਾ ਜਦ ਸਰੋਤ ਟਰਮੀਨਲ ਸਥਿਤੀ ਵਿੱਚ ਆਉਂਦੇ ਹਨ।

ਇਸ ਨਾਲ:
1. GPT-5.2 ਅਤੇ text-embedding-3-small ਮਾਡਲਾਂ ਸਮੇਤ Azure OpenAI ਸਰੋਤ deploy ਕਰੇਗਾ
2. Automatically ਪ੍ਰੋਜੈਕਟ ਰੂਟ ਵਿੱਚ `.env` ਫ਼ਾਈਲ ਜੇਨਰੇਟ ਕਰੇਗਾ ਜੋ ਅਧਿਕਾਰ ਸੂਚਨਾ ਸਹਿਤ ਹੋਵੇਗੀ
3. ਸਾਰੀਆਂ ਲੋੜੀਂਦੀਆਂ environment variables ਸੈੱਟ ਕਰੇਗਾ

**ਡਿਪਲੋਇੰਟ ਸਮੱਸਿਆਵਾਂ ਆ ਰਹੀਆਂ ਹਨ?** ਵਿਸਥਾਰ ਲਈ [Infrastructure README](infra/README.md) ਵੇਖੋ ਜਿਸ ਵਿੱਚ ਸਬਡੋਮੇਨ ਨਾਮ ਵਿਵਾਦ, Azure Portal ਵਿੱਚ manual deployment ਕਦਮ ਅਤੇ ਮਾਡਲ ਕਨਫਿਗਰੇਸ਼ਨ ਸਮਝਾਈ ਗਈ ਹੈ।

**ਡਿਪਲੋਇੰਟ ਸਫਲ ਹੋਇਆ ਜਾਂ ਨਹੀਂ ਦੀ ਜਾਂਚ ਕਰੋ:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, ਆਦਿ ਦਿਖਾਉਣਾ ਚਾਹੀਦਾ ਹੈ।
```

**PowerShell:**
```powershell
Get-Content ..\.env  # ਦਿਖਾਉਣਾ ਚਾਹੀਦਾ ਹੈ AZURE_OPENAI_ENDPOINT, API_KEY, ਆਦਿ।
```

> **ਨੋਟ:** `azd up` ਆਟੋਮੈਟਿਕ `.env` ਫ਼ਾਈਲ ਜੇਨਰੇਟ ਕਰਦਾ ਹੈ। ਜੇ ਬਾਅਦ ਵਿੱਚ ਅੱਪਡੇਟ ਕਰਨਾ ਹੋਵੇ, ਤਾਂ ਤੁਸੀਂ ਹੱਥੋਂ `.env` ਫ਼ਾਈਲ ਸੋਧ ਸਕਦੇ ਹੋ ਜਾਂ ਇਹ ਕਮਾਂਡ ਦੁਬਾਰਾ ਚਲਾ ਸਕਦੇ ਹੋ:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## ਲੋਕਲ ਐਪਲੀਕੇਸ਼ਨ ਚਲਾਓ

**ਡਿਪਲੋਇੰਟ ਦੀ ਜਾਂਚ ਕਰੋ:**

ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ `.env` ਫ਼ਾਈਲ ਹੈ ਅਤੇ ਇਸ ਵਿੱਚ Azure ਹਵਾਲੇ ਸ਼ਾਮਲ ਹਨ ਇਹ ਯਕੀਨੀ ਬਣਾਓ:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ਦਿਖਾਉਣਾ ਚਾਹੀਦਾ ਹੈ
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ਦਿਖਾਉਣਾ ਚਾਹੀਦਾ ਹੈ
```

**ਐਪਲੀਕੇਸ਼ਨਾਂ ਸ਼ੁਰੂ ਕਰੋ:**

**ਵਿਕਲਪ 1: Spring Boot Dashboard ਵਰਤੋਂ (VS Code ਵਰਤੋਂਕਾਰਾਂ ਲਈ ਪ੍ਰਸਤਾਵਿਤ)**

devcontainer ਵਿੱਚ Spring Boot Dashboard ਐਕਸਟੈਨਸ਼ਨ ਸ਼ਾਮਲ ਹੈ, ਜੋ ਕਿ ਸਾਰੇ Spring Boot ਐਪਲੀਕੇਸ਼ਨਾਂ ਦੇ ਪ੍ਰਬੰਧ ਲਈ ਵਿਜ਼ੂਅਲ ਇੰਟਰਫੇਸ ਦਿੰਦਾ ਹੈ। ਇਹ VS Code ਵਿੱਚ ਖੱਬੇ ਪਾਸੇ Activity Bar ਵਿੱਚ Spring Boot ਆਈਕਨ ਵਜੋਂ ਮਿਲੇਗਾ।

Spring Boot Dashboard ਤੋਂ ਤੁਸੀਂ:
- ਵਰਕਸਪੇਸ ਵਿੱਚ ਸਾਰੇ ਉਪਲਬਧ Spring Boot ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਵੇਖ ਸਕਦੇ ਹੋ
- ਇੱਕ ਕਲਿਕ ਨਾਲ ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਚਾਲੂ/ਬੰਦ ਕਰ ਸਕਦੇ ਹੋ
- ਐਪਲੀਕੇਸ਼ਨ ਲਾਗਜ਼ ਨੂੰ ਰੀਅਲ-ਟਾਈਮ ਵਿੱਚ ਵੇਖ ਸਕਦੇ ਹੋ
- ਐਪਲੀਕੇਸ਼ਨ ਦੀ ਹਾਲਤ ਦੀ ਨਿਗਰਾਨੀ ਕਰ ਸਕਦੇ ਹੋ

ਸਰਲ ਜਾਣ-ਪਹਚਾਨ ‘introduction' ਦੇ ਨਾਲ ਖੇਡ ਬਟਨ ਤੇ ਕਲਿਕ ਕਰੋ ਜਾਂ ਸਾਰੇ ਮਾਡਿਊਲ ਇਕੱਠੇ ਚਲਾਓ।

<img src="../../../translated_images/pa/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**ਵਿਕਲਪ 2: ਸ਼ੈਲ ਸਕ੍ਰਿਪਟ ਵਰਤੋਂ**

ਸਾਰੇ ਵੈੱਬ ਐਪਲੀਕੇਸ਼ਨ (ਮਾਡਿਊਲ 01-04) ਸ਼ੁਰੂ ਕਰੋ:

**Bash:**
```bash
cd ..  # ਮੁਲ ਡਾਇਰੈਕਟਰੀ ਤੋਂ
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਤੋਂ
.\start-all.ps1
```

ਜਾਂ ਸਿਰਫ ਇਹ ਮਾਡਿਊਲ ਚਲਾਓ:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

ਦੋਹਾਂ ਸਕ੍ਰਿਪਟਾਂ ਆਟੋਮੈਟਿਕ O environment variables ਨੂੰ ਰੂਟ `.env` ਫਾਇਲ ਤੋਂ ਲੋਡ ਕਰਦੀਆਂ ਹਨ ਅਤੇ ਜੇ ਜਾਰ ਮੌਜੂਦ ਨਹੀਂ ਤਾਂ ਬਣਾਉਂਦੀਆਂ ਹਨ।

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਸਾਰੇ ਮਾਡਿਊਲ ਨੂੰ ਸਵੈ-ਹੱਥੋਂ ਬਿਲਡ ਕਰਨਾ ਚਾਹੁੰਦੇ ਹੋ ਇਸ ਤੋਂ ਪਹਿਲਾਂ ਕਿ ਸ਼ੁਰੂ ਕਰੋ:
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

ਆਪਣੇ ਬ੍ਰਾਉਜ਼ਰ ਵਿੱਚ http://localhost:8080 ਖੋਲ੍ਹੋ।

**ਰੋਕਣ ਲਈ:**

**Bash:**
```bash
./stop.sh  # ਇਹ ਮੋਡੀਊਲ ਸਿਰਫ
# ਜਾਂ
cd .. && ./stop-all.sh  # ਸਾਰੇ ਮੋਡੀਊਲ
```

**PowerShell:**
```powershell
.\stop.ps1  # ਸਿਰਫ ਇਹ ਮੋਡੀਊਲ
# ਜਾਂ
cd ..; .\stop-all.ps1  # ਸਾਰੇ ਮੋਡੀਊਲ
```

## ਐਪਲੀਕੇਸ਼ਨ ਦੀ ਵਰਤੋਂ

ਐਪਲੀਕੇਸ਼ਨ ਇੱਕ ਵੈੱਬ ਇੰਟਰਫੇਸ ਦਿੰਦਾ ਹੈ ਜਿਸ ਵਿੱਚ ਦੋ ਚੈਟ ਇੰਪਲੀਮੈਂਟੇਸ਼ਨ ਸਾਈਡ-ਬਾਈ-ਸਾਈਡ ਹਨ।

<img src="../../../translated_images/pa/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard ਦਿਖਾ ਰਿਹਾ ਹੈ Simple Chat (stateless) ਅਤੇ Conversational Chat (stateful) ਵਿਕਲਪ*

### Stateless ਚੈਟ (ਖੱਬਾ ਪੈਨਲ)

ਇਸ ਨੂੰ ਪਹਿਲਾਂ ਕੋਸ਼ਿਸ਼ ਕਰੋ। "ਮੇਰਾ ਨਾਮ ਜੌਨ ਹੈ" ਪੁੱਛੋ ਅਤੇ ਤੁਰੰਤ "ਮੇਰਾ ਨਾਮ ਕੀ ਹੈ?" ਪੁੱਛੋ। ਮਾਡਲ ਯਾਦ ਨਹੀਂ ਰੱਖੇਗਾ ਕਿਉਂਕਿ ਹਰ ਸੁਨੇਹਾ ਸੁਤੰਤਰ ਹੈ। ਇਹ ਮੁੱਖ ਸਮੱਸਿਆ ਹੈ ਬੇਸਿਕ ਭਾਸ਼ਾ ਮਾਡਲ ਏਲੀਕੇਸ਼ਨ ਨਾਲ - ਕੋਈ ਸੰਦਰਭ ਗੱਲਬਾਤ ਲਈ ਨਹੀਂ।

<img src="../../../translated_images/pa/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI ਪਹਿਲਾਂ ਦੇ ਸੁਨੇਹੇ ਤੋਂ ਤੁਹਾਡੇ ਨਾਮ ਨੂੰ ਯਾਦ ਨਹੀਂ ਰੱਖਦਾ*

### Stateful ਚੈਟ (ਸੱਜਾ ਪੈਨਲ)

ਹੁਣ ਇਥੇ ਉਹੀ ਕ੍ਰਮ ਦੁਬਾਰਾ ਕੋਸ਼ਿਸ਼ ਕਰੋ। "ਮੇਰਾ ਨਾਮ ਜੌਨ ਹੈ" ਬੋਲੋ ਅਤੇ ਫਿਰ "ਮੇਰਾ ਨਾਮ ਕੀ ਹੈ?"। ਇਹ ਵਾਰੀ ਮਾਡਲ ਯਾਦ ਰੱਖਦਾ ਹੈ। ਅੰਤਰ MessageWindowChatMemory ਹੈ - ਜੋ ਗੱਲਬਾਤ ਇਤਿਹਾਸ ਨੂੰ ਬਰਕਰਾਰ ਰੱਖਦਾ ਹੈ ਅਤੇ ਹਰ ਬਿਨਤੀ ਨਾਲ ਇਸਨੂੰ ਸ਼ਾਮਲ ਕਰਦਾ ਹੈ। ਇਹ ਪ੍ਰੋਡਕਸ਼ਨ ਵਿੱਚ ਸੰਵਾਦਾਤਮਕ AI ਦਾ ਤਰੀਕਾ ਹੈ।

<img src="../../../translated_images/pa/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI ਤੁਹਾਡੇ ਨਾਮ ਨੂੰ ਪਹਿਲਾਂ ਦੀ ਗੱਲਬਾਤ ਤੋਂ ਯਾਦ ਰੱਖਦਾ ਹੈ*

ਦੋਹਾਂ ਪੈਨਲ ਉਹੀ GPT-5.2 ਮਾਡਲ ਵਰਤਦੇ ਹਨ। ਸਿਰਫ ਮੈਮੋਰੀ ਵਿੱਚ ਅੰਤਰ ਹੈ। ਇਹ ਤੁਹਾਨੂੰ ਸਪਸ਼ਟ ਕਰਦਾ ਹੈ ਕਿ ਮੈਮੋਰੀ ਤੁਹਾਡੇ ਐਪ ਲਈ ਕੀ ਲਿਆਉਂਦੀ ਹੈ ਅਤੇ ਇਹ ਅਸਲੀ ਵਰਤੋਂ ਲਈ ਕਿਵੇਂ ਜ਼ਰੂਰੀ ਹੈ।

## ਅਗਲੇ ਕਦਮ

**ਅਗਲਾ ਮਾਡਿਊਲ:** [02-prompt-engineering - GPT-5.2 ਨਾਲ ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ](../02-prompt-engineering/README.md)

---

**ਨੈਵੀਗੇਸ਼ਨ:** [← ਪਹਿਲਾਂ: Module 00 - Quick Start](../00-quick-start/README.md) | [ਮੁੱਖ ਵਿੱਚ ਵਾਪਸ](../README.md) | [ਅਗਲਾ: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਡਿਸਕਲੇਮਰ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ ਏਆਈ ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦਿਤ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਕੋਸ਼ਿਸ਼ ਕਰਦੇ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਰੱਖੋ ਕਿ ਸਵੈਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸਮਰੱਥਤਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਤ ਸਰੋਤ ਵਜੋਂ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਸੰਵੇਦਨਸ਼ੀਲ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ਾਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਿਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਅਸੀਂ ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਨਾਲ ਹੋਣ ਵਾਲੀ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆ ਲਈ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->