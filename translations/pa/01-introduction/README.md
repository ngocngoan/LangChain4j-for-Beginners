# Module 01: LangChain4j ਨਾਲ ਸ਼ੁਰੂਆਤ

## ਸੂਚੀ

- [ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ](../../../01-introduction)
- [ਪੂਰਵ-ਜਰੂਰੀਆਤ](../../../01-introduction)
- [ਮੁੱਖ ਸਮੱਸਿਆ ਨੂੰ ਸਮਝਣਾ](../../../01-introduction)
- [ਟੋਕਨ ਸਮਝਣਾ](../../../01-introduction)
- [ਯਾਦਦਾਸ਼ਤ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀ ਹੈ](../../../01-introduction)
- [ਇਹ LangChain4j ਕਿਵੇਂ ਵਰਤਦਾ ਹੈ](../../../01-introduction)
- [Azure OpenAI ਇੰਫ੍ਰਾਸਟਰਕਚਰ ਡਿਪਲੋਇ ਕਰੋ](../../../01-introduction)
- [ਐਪਲੀਕੇਸ਼ਨ ਨੂੰ ਸਥਾਨਕ ਰੂਪ ਵਿੱਚ ਚਲਾਓ](../../../01-introduction)
- [ਐਪਲੀਕੇਸ਼ਨ ਦੀ ਵਰਤੋਂ ਕਰਨਾ](../../../01-introduction)
  - [ਸਟੇਟਲੈੱਸ ਚੈਟ (ਖੱਬਾ ਪੈਨਲ)](../../../01-introduction)
  - [ਸਟੇਟਫੁਲ ਚੈਟ (ਸਤੰਬਰ ਪੈਨਲ)](../../../01-introduction)
- [ਅੱਗਲਾ ਕਦਮ](../../../01-introduction)

## ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ

ਜੇ ਤੁਸੀਂ ਕੁਇਕ ਸਟਾਰਟ ਮੁਕੰਮਲ ਕੀਤਾ, ਤਾਂ ਤੁਹਾਨੂੰ ਵਿਖਿਅਾ ਕਿ ਕੀਵੇਂ ਪ੍ਰੰਪਟ ਭੇਜੇ ਜਾਂਦੇ ਹਨ ਅਤੇ ਜਵਾਬ ਮੁੜ ਮਿਲਦੇ ਹਨ। ਇਹ ਬੁਨਿਆਦ ਹੈ, ਪਰ ਅਸਲੀ ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਹੋਰ ਕੁਝ ਚਾਹੀਦਾ ਹੈ। ਇਹ ਮੋਡੀਊਲ ਤੁਹਾਨੂੰ ਸਿਖਾਏਗਾ ਕਿ ਕਿਵੇਂ ਗੱਲਬਾਤੀ AI ਬਣਾਉਣੀ ਹੈ ਜੋ ਸੰਦੇਸ਼ਾਂ ਨੂੰ ਯਾਦ ਰੱਖਦੀ ਹੈ ਅਤੇ ਸਟੇਟ ਕਾਇਮ ਰੱਖਦੀ ਹੈ - ਜੋ ਕਿ ਇੱਕ ਵਾਰੀ ਲਈ ਡੈਮੋ ਅਤੇ ਪ੍ਰੋਡਕਸ਼ਨ-ਤਿਆਰ ਐਪਲੀਕੇਸ਼ਨ ਵਿੱਚ ਅੰਤਰ ਹੈ।

ਅਸੀਂ ਇਸ ਗਾਈਡ ਵਿੱਚ Azure OpenAI ਦੇ GPT-5.2 ਨੂੰ ਵਰਤਾਂਗੇ ਕਿਉਂਕਿ ਇਸਦੀ ਉੱਚੇ ਦਰਜੇ ਦੀ ਸੋਚਣ ਦੀ ਯੋਗਤਾ ਵੱਖ ਵੱਖ ਪੈਟਰਨਾਂ ਦੇ ਵਿਹਾਰ ਨੂੰ ਆਸਾਨੀ ਨਾਲ ਦਰਸਾਉਂਦੀ ਹੈ। ਜਦੋਂ ਤੁਸੀਂ ਯਾਦਦਾਸ਼ਤ ਸ਼ਾਮਿਲ ਕਰਦੇ ਹੋ, ਤਾਂ ਅੰਤਰ ਸੁਝਾਈ ਦੇਂਦਾ ਹੈ। ਇਹ ਤੁਹਾਨੂੰ ਸਮਝਣ ਵਿੱਚ ਮਦਦ ਕਰਦਾ ਹੈ ਕਿ ਹਰ ਕੰਪੋਨੈਂਟ ਤੁਹਾਡੇ ਐਪ ਲਈ ਕੀ ਲਿਆਉਂਦਾ ਹੈ।

ਤੁਸੀਂ ਇੱਕ ਐਪ ਬਣਾਵੋਗੇ ਜੋ ਦੋਨੋ ਪੈਟਰਨ ਦਰਸਾਉਂਦਾ ਹੈ:

**ਸਟੇਟਲੈੱਸ ਚੈਟ** - ਹਰ ਬੇਨਤੀ ਸੁਤੰਤਰ ਹੈ। ਮਾਡਲ ਕੋਲ ਪਿਛਲੇ ਸੁਨੇਹਿਆਂ ਦੀ ਕੋਈ ਯਾਦ ਨਹੀਂ ਹੈ। ਇਹ ਵੋ ਪੈਟਰਨ ਹੈ ਜੋ ਤੁਸੀਂ ਕੁਇਕ ਸਟਾਰਟ ਵਿੱਚ ਵਰਤਿਆ।

**ਸਟੇਟਫੁਲ ਗੱਲਬਾਤ** - ਹਰ ਬੇਨਤੀ ਵਿੱਚ ਗੱਲਬਾਤ ਦਾ ਇਤਿਹਾਸ ਹੁੰਦਾ ਹੈ। ਮਾਡਲ ਕਈ ਵਾਰੀ ਬਹਿਸ ਦੇ ਸੰਦਰਭ ਨੂੰ ਬਣਾਈ ਰੱਖਦਾ ਹੈ। ਇਹ ਉਹ ਹੈ ਜੋ ਉਤਪਾਦ ਐਪਲੀਕੇਸ਼ਨਾਂ ਲਈ ਲੋੜੀਂਦਾ ਹੈ।

## ਪੂਰਵ-ਜਰੂਰੀਆਤ

- Azure ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਜਿਸ ਵਿੱਚ Azure OpenAI ਦੀ ਪਹੁੰਚ ਹੋਵੇ
- ਜਾਵਾ 21, ਮਾਵਨ 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **ਨੋਟ:** ਜਾਵਾ, ਮਾਵਨ, Azure CLI ਅਤੇ Azure Developer CLI (azd) ਪ੍ਰਦਾਨ ਕੀਤੇ ਡਿਵਕੰਟੇਨਰ ਵਿੱਚ ਪਹਿਲਾਂ ਤੋਂ ਇੰਸਟਾਲ ਹਨ।

> **ਨੋਟ:** ਇਹ ਮੋਡੀਊਲ Azure OpenAI 'ਤੇ GPT-5.2 ਵਰਤਦਾ ਹੈ। ਡਿਪਲੌਇਮੈਂਟ `azd up` ਰਾਹੀਂ ਆਪਣੇ ਆਪ ਸੰਰਚਿਤ ਹੁੰਦਾ ਹੈ - ਕੋਡ ਵਿੱਚ ਮਾਡਲ ਨਾਂ ਬਦਲੋ ਨਾ।

## ਮੁੱਖ ਸਮੱਸਿਆ ਨੂੰ ਸਮਝਣਾ

ਭਾਸ਼ਾ ਮਾਡਲ ਸਟੇਟਲੈੱਸ ਹੁੰਦੇ ਹਨ। ਹਰ API ਕਾਲ ਸੁਤੰਤਰ ਹੈ। ਜੇ ਤੁਸੀਂ "ਮੇਰਾ ਨਾਮ ਜੋਨ ਹੈ" ਭੇਜਦੇ ਹੋ ਅਤੇ ਫਿਰ ਪੁੱਛਦੇ ਹੋ "ਮੇਰਾ ਨਾਮ ਕੀ ਹੈ?", ਮਾਡਲ ਨੂੰ ਇਹ ਕੋਈ ਖ਼ਬਰ ਨਹੀਂ ਹੁੰਦੀ ਕਿ ਤੁਸੀਂ ਸਵੈ-ਪਹਿਚਾਣ ਕਰਵਾਈ ਸੀ। ਇਹ ਹਰ ਬੇਨਤੀ ਨੂੰ ਪਹਿਲੀ ਗੱਲਬਾਤ ਵਾਂਗ ਲੈਂਦਾ ਹੈ।

ਇਹ ਸਧਾਰਨ ਸਵਾਲ-ਜਵਾਬ ਲਈ ਠੀਕ ਹੈ ਪਰ ਅਸਲੀ ਐਪ ਲਈ ਇਸਦਾ ਕੋਈ ਲਾਭ ਨਹੀਂ। ਗਾਹਕ ਸੇਵਾ ਬੋਟ ਨੂੰ ਯਾਦ ਰੱਖਣਾ ਪੈਂਦਾ ਹੈ ਕਿ ਤੁਸੀਂ ਕੀ ਕਿਹਾ ਸੀ। ਨਿੱਜੀ ਸਹਾਇਕਾਂ ਨੂੰ ਸੰਦਰਭ ਚਾਹੀਦਾ ਹੈ। ਕੋਈ ਵੀ ਕਈ ਵਾਰੀ ਦੀ ਗੱਲਬਾਤ ਯਾਦਦਾਸ਼ਤ ਮੰਗਦੀ ਹੈ।

<img src="../../../translated_images/pa/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*ਸਟੇਟਲੈੱਸ (ਸੁਤੰਤਰ ਕਾਲਾਂ) ਅਤੇ ਸਟੇਟਫੁਲ (ਸੰਦਰਭ-ਜਾਣੂ) ਗੱਲਬਾਤਾਂ ਵਿੱਚ ਅੰਤਰ*

## ਟੋਕਨ ਸਮਝਣਾ

ਗੱਲਬਾਤਾਂ ਵਿੱਚ ਉਤਰਨ ਤੋਂ ਪਹਿਲਾਂ, ਟੋਕਨਾਂ ਨੂੰ ਸਮਝਣਾ ਜਰੂਰੀ ਹੈ - ਟੈਕਸਟ ਦੇ ਮੁੱਢਲੇ ਇਕਾਈਆਂ ਜੋ ਭਾਸ਼ਾ ਮਾਡਲ ਪ੍ਰੋਸੈਸ ਕਰਦੇ ਹਨ:

<img src="../../../translated_images/pa/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*ਇਸ ਤਰ੍ਹਾਂ ਟੈਕਸਟ ਟੋਕਨਾਂ ਵਿੱਚ разбੋਟਿਆ ਜਾਂਦਾ ਹੈ - "ਮੈਨੂੰ AI ਪਸੰਦ ਹੈ!" 4 ਵੱਖਰੇ ਪ੍ਰੋਸੈਸਿੰਗ ਇਕਾਈਆਂ ਬਣ ਜਾਂਦੀਆਂ ਹਨ*

ਟੋਕਨ Ai ਮਾਡਲ ਟੈਕਸਟ ਮਾਪਣ ਅਤੇ ਪ੍ਰੋਸੈਸ ਕਰਨ ਲਈ ਵਰਤਦੇ ਹਨ। ਸ਼ਬਦ, ਵਿਸ਼ਰਾਮ ਚਿੰਨ੍ਹ ਅਤੇ ਖਾਲੀ ਥਾਂ ਵੀ ਟੋਕਨ ਹੋ ਸਕਦੇ ਹਨ। ਤੁਹਾਡੇ ਮਾਡਲ ਦੀ ਸੀਮਾ ਹੁੰਦੀ ਹੈ ਕਿ ਬਾਰਗੀ ਬਾਰੰਟੇ ਕਿੰਨੇ ਟੋਕਨ ਪ੍ਰੋਸੈਸ ਕਰ ਸਕਦਾ ਹੈ (GPT-5.2 ਲਈ 400,000, ਜਿਨ੍ਹਾਂ ਵਿੱਚ 272,000 ਇਨਪੁੱਟ ਅਤੇ 128,000 ਆਉਟਪੁੱਟ ਟੋਕਨ ਹੁੰਦੇ ਹਨ)। ਟੋਕਨਾਂ ਨੂੰ ਸਮਝ ਕੇ ਤੁਸੀਂ ਗੱਲਬਾਤ ਦੀ ਲੰਬਾਈ ਅਤੇ ਲਾਗਤਾਂ ਨੂੰ ਸੰਭਾਲ ਸਕਦੇ ਹੋ।

## ਯਾਦਦਾਸ਼ਤ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀ ਹੈ

ਚੈਟ ਮੈਮੋਰੀ ਸਟੇਟਲੈੱਸ ਸਮੱਸਿਆ ਦਾ ਹੱਲ ਹੈ ਜਿਸ ਵਿੱਚ ਗੱਲਬਾਤ ਇਤਿਹਾਸ ਨੂੰ ਕਾਇਮ ਰੱਖਿਆ ਜਾਂਦਾ ਹੈ। ਮਾਡਲ ਨੂੰ ਬੇਨਤੀ ਭੇਜਣ ਤੋਂ ਪਹਿਲਾਂ, ਫਰੇਮਵਰਕ ਸੰਬੰਧਿਤ ਪਿਛਲੇ ਸੁਨੇਹਿਆਂ ਨੂੰ ਪਹਿਲਾਂ ਸ਼ਾਮਿਲ ਕਰਦਾ ਹੈ। ਜਦੋਂ ਤੁਸੀਂ ਪੁੱਛਦੇ ਹੋ "ਮੇਰਾ ਨਾਮ ਕੀ ਹੈ?", ਸਿਸਟਮ ਮੂਲ ਰੂਪ ਵਿੱਚ ਪੂਰਾ ਇਤਿਹਾਸ ਭੇਜਦਾ ਹੈ, ਤਾਂ ਜੋ ਮਾਡਲ ਵੇਖ ਸਕੇ ਕਿ ਤੁਸੀਂ ਪਹਿਲਾਂ "ਮੇਰਾ ਨਾਮ ਜੋਨ ਹੈ" ਕਿਹਾ ਸੀ।

LangChain4j ਯਾਦਦਾਸ਼ਤ ਦੀਆਂ ਕਾਰਜਾਵਲੀਆਂ ਮੁਹੱਈਆ ਕਰਦਾ ਹੈ ਜੋ ਇਹ ਸਾਰਾ ਕੰਮ ਆਪਣੇ ਆਪ ਸੰਭਾਲਦੀਆਂ ਹਨ। ਤੁਸੀਂ ਲੰਮੀ ਇਹਦੇ ਕਿੰਨੇ ਸੁਨੇਹੇ ਰੱਖਣਾ ਹੈ ਚੁਣਦੇ ਹੋ ਅਤੇ ਫਰੇਮਵਰਕ ਸੰਦਰਭ ਵਿੰਡੋ ਦਾ ਪ੍ਰਬੰਧ ਕਰਦਾ ਹੈ।

<img src="../../../translated_images/pa/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory ਹਾਲ ਹੀ ਦੇ ਸੁਨੇਹਿਆਂ ਦੀ ਇੱਕ ਸਲਾਈਡਿੰਗ ਵਿੰਡੋ ਕਾਇਮ ਰੱਖਦੀ ਹੈ ਅਤੇ ਪੁਰਾਣੇ ਸੁਨੇਹਿਆਂ ਨੂੰ ਆਪਣੇ ਆਪ ਹਟਾਉਂਦੀ ਹੈ*

## ਇਹ LangChain4j ਕਿਵੇਂ ਵਰਤਦਾ ਹੈ

ਇਹ ਮੋਡੀਊਲ ਕੁਇਕ ਸਟਾਰਟ ਨੂੰ ਵਧਾਉਂਦਾ ਹੈ ਜਿੱਥੇ Spring Boot ਨੂੰ ਜੋੜ ਕੇ ਗੱਲਬਾਤੀ ਯਾਦਦਾਸ਼ਤ ਸ਼ਾਮਿਲ ਕੀਤੀ ਗਈ ਹੈ। ਇੱਥੇ ਟੁਕੜੇ ਇੱਕ-ਦੂਜੇ ਨਾਲ ਕਿਵੇਂ ਜੁੜਦੇ ਹਨ:

**Dependencies** - ਦੋ LangChain4j ਲਾਇਬਰੇਰੀਆਂ ਸ਼ਾਮਿਲ ਕਰੋ:

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

**ਚੈਟ ਮਾਡਲ** - Azure OpenAI ਨੂੰ Spring ਬੀਨ ਵਜੋਂ ਕੰਫਿਗਰ ਕਰੋ ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

ਬਿਲਡਰ ਏਨਵਾਇਰਨਮੈਂਟ ਵੈਰੀਏਬਲਾਂ ਤੋਂ ਸਾਕਸ਼ਮਤਾ ਪੜ੍ਹਦਾ ਹੈ ਜੋ `azd up` ਨਾਲ ਸੈੱਟ ਕੀਤੇ ਗਏ ਹਨ। `baseUrl` ਨੂੰ ਆਪਣੇ Azure ਸੰਗ੍ਰਹਿ ਪੁਆਇੰਟ ਤੇ ਸੈੱਟ ਕਰਨਾ OpenAI ਕਲਾਇੰਟ ਨੂੰ Azure OpenAI ਨਾਲ ਕੰਮ ਕਰਨ ਦੇ ਯੋਗ ਬਣਾਉਂਦਾ ਹੈ।

**ਗੱਲਬਾਤ ਯਾਦਦਾਸ਼ਤ** - MessageWindowChatMemory ਨਾਲ ਗੱਲਬਾਤ ਇਤਿਹਾਸ ਨੂੰ ਟ੍ਰੈਕ ਕਰੋ ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

ਯਾਦਦਾਸ਼ਤ ਬਣਾ ਕੇ `withMaxMessages(10)` ਨਾਲ ਆਖਰੀ 10 ਸੁਨੇਹੇ ਰੱਖੋ। ਯੂਜ਼ਰ ਅਤੇ AI ਸੁਨੇਹਿਆਂ ਨੂੰ ਟਾਈਪਡ ਰੈਪਰ `UserMessage.from(text)` ਅਤੇ `AiMessage.from(text)` ਨਾਲ ਸ਼ਾਮਿਲ ਕਰੋ। ਇਤਿਹਾਸ ਨੂੰ `memory.messages()` ਨਾਲ ਪ੍ਰਾਪਤ ਕਰੋ ਅਤੇ ਮਾਡਲ ਨੂੰ ਭੇਜੋ। ਸਰਵਿਸ ਹਰ ਗੱਲਬਾਤ ਵਾਲੇ ID ਲਈ ਖ਼ਾਸ ਯਾਦਦਾਸ਼ਤ ਥਾਂ ਸੁਰੱਖਿਅਤ ਕਰਦੀ ਹੈ, ਜਿਸ ਨਾਲ ਕਈ ਯੂਜ਼ਰ ਇੱਕੋ ਸਮੇਂ ਚੈਟ ਕਰ ਸਕਦੇ ਹਨ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** ਖੋਲ੍ਹੋ [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ਅਤੇ ਪੁੱਛੋ:
> - "MessageWindowChatMemory ਕਿਵੇਂ ਫੈਸਲਾ ਕਰਦਾ ਹੈ ਕਿ ਵਿੰਡੋ ਪੂਰੀ ਹੋਣ ਤੇ ਕਿਹੜੇ ਸੁਨੇਹੇ ਹਟਾਏ ਜਾਣ?"
> - "ਕੀ ਮੈਂ ਇਨ-ਮੇਮੋਰੀ ਦੇ ਬਦਲੇ ਡੇਟਾਬੇਸ ਦੀ ਵਰਤੋਂ ਕਰ ਕੇ ਕਸਟਮ ਯਾਦਦਾਸ਼ਤ ਸਟੋਰੇਜ਼ ਕਰ ਸਕਦਾ ਹਾਂ?"
> - "ਮੈਂ ਪੁਰਾਣੇ ਗੱਲਬਾਤ ਇਤਿਹਾਸ ਨੂੰ ਸੰਖੇਪ ਕਰਨ ਲਈ ਸੰਖੇਪ ਜੁੜਾਂ ਜੋੜ ਸਕਦਾ ਹਾਂ?"

ਸਟੇਟਲੈੱਸ ਚੈਟ ਏਂਡਪਾਇੰਟ ਯਾਦਦਾਸ਼ਤ ਨੂੰ ਬਿਲਕੁਲ ਬਾਈਪਾਸ ਕਰਦਾ ਹੈ - ਫਿਕਰ ਨਾ ਕਰੋ, ਸਿਰਫ `chatModel.chat(prompt)` ਵਰਗਾ ਜਿਵੇਂ ਕੁਇਕ ਸਟਾਰਟ ਵਿੱਚ। ਸਟੇਟਫੁਲ ਏਂਡਪਾਇੰਟ ਸੁਨੇਹੇ ਯਾਦਦਾਸ਼ਤ ਵਿੱਚ ਸ਼ਾਮਲ ਕਰਦਾ ਹੈ, ਇਤਿਹਾਸ ਪ੍ਰਾਪਤ ਕਰਦਾ ਹੈ ਅਤੇ ਹਰ ਬੇਨਤੀ ਨਾਲ ਉਹ ਸੰਦਰਭ ਭੇਜਦਾ ਹੈ। ਮਾਡਲ ਦਾ ਕੰਫਿਗਰੇਸ਼ਨ ਇੱਕੋ ਜਿਹਾ ਹੈ, ਪਰ ਪੈਟਰਨ ਵੱਖਰੇ ਹਨ।

## Azure OpenAI ਇੰਫ੍ਰਾਸਟਰਕਚਰ ਡਿਪਲੋਇ ਕਰੋ

**Bash:**
```bash
cd 01-introduction
azd up  # ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਅਤੇ ਸਥਾਨ ਚੁਣੋ (eastus2 ਸਿਫਾਰਸ਼ੀ)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਅਤੇ ਸਥਾਨ ਚੁਣੋ (ਪੂਰਬੀ ਯੂਐਸ 2 ਦੀ ਸਿਫਾਰਿਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ)
```

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ timeout error ਦੇਖੋ (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), ਤਾਂ ਸਿਰਫ `azd up` ਫਿਰ ਤੋਂ ਚਲਾਓ। Azure ਸਰੋਤ ਵਿੱਥੀ ਤਿਆਰ ਹੋ ਰਹੇ ਹੋਣ, ਇਸ ਲਈ ਦੁਬਾਰਾ ਕੋਸ਼ਿਸ਼ ਕਰਨ ਨਾਲ ਡਿਪਲੌਇਮੈਂਟ ਪੂਰਾ ਹੋ ਸਕਦਾ ਹੈ ਜਦੋਂ ਸਰੋਤ ਤਿਆਰ ਹੋ ਜਾਂਦੇ ਹਨ।

ਇਸ ਨਾਲ ਇਹ ਹੋਵੇਗਾ:
1. Azure OpenAI ਸਰੋਤ ਨੂੰ GPT-5.2 ਅਤੇ text-embedding-3-small ਮਾਡਲਾਂ ਨਾਲ ਡਿਪਲੌਇ ਕਰਨਾ
2. ਪ੍ਰੋਜੈਕਟ ਦੀ ਮੂਲ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ `.env` ਫਾਇਲ ਆਪੋ-ਆਪ ਬਣਾਉਣਾ
3. ਸਾਰੇ ਲੋੜੀਂਦੇ ਏਨਵਾਇਰਨਮੈਂਟ ਵੈਰੀਏਬਲ ਸੈੱਟ ਕਰਨਾ

**ਡਿਪਲੌਇਮੈਂਟ ਸਮੱਸਿਆਵਾਂ ਹਨ?** ਵੱਡੀ ਤਰੱਕੀ ਲਈ [Infrastructure README](infra/README.md) ਵੇਖੋ ਜਿਵੇਂ ਸਰਾਟੇ ਨਾਮ ਟਕਰਾਅ, Azure ਪੋਰਟਲ ਡਿਪਲੌਇਮੈਂਟ ਕਦਮ, ਅਤੇ ਮਾਡਲ ਕੰਫਿਗਰੇਸ਼ਨ ਸਲਾਹ।

**ਡਿਪਲੌਇਮੈਂਟ ਸਫਲ ਹੋਇਆ ਜਾਂ ਨਹੀਂ:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, ਆਦਿ ਦਿਖਾਉਣਾ ਚਾਹੀਦਾ ਹੈ।
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, ਆਦਿ ਦਿਖਾਉਣਾ ਚਾਹੀਦਾ ਹੈ।
```

> **ਨੋਟ:** `azd up` ਹੁਕਮ `.env` ਫਾਇਲ ਆਪ ਹੀ ਬਣਾਉਂਦਾ ਹੈ। ਜੇ ਤੁਹਾਨੂੰ ਬਾਅਦ ਵਿੱਚ ਅੱਪਡੇਟ ਕਰਨੀ ਹੋਵੇ ਤਾਂ ਤੁਸੀਂ ਜਾਂ ਤਾਂ `.env` ਫਾਈਲ ਹੱਥੋਂ ਸੋਧ ਸਕਦੇ ਹੋ ਜਾਂ ਇਹ ਦੁਬਾਰਾ ਬਣਾਉਣ ਲਈ ਇਹ ਚਲਾ ਸਕਦੇ ਹੋ:
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

## ਐਪਲੀਕੇਸ਼ਨ ਨੂੰ ਸਥਾਨਕ ਰੂਪ ਵਿੱਚ ਚਲਾਓ

**ਡਿਪਲੌਇਮੈਂਟ ਦੀ ਜਾਂਚ ਕਰੋ:**

ਸुनਿਸ਼ਚਿਤ ਕਰੋ ਕਿ ਮੂਲ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ `.env` ਫਾਇਲ Azure ਸਕ੍ਰਿਡੈਂਸ਼ੀਅਲਾਂ ਨਾਲ ਹੈ:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ਦਰਸਾਉਣਾ ਚਾਹੀਦਾ ਹੈ
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ਦਿਖਾਉਣਾ ਚਾਹੀਦਾ ਹੈ
```


**ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਚਾਲੂ ਕਰੋ:**

**ਵਿਕਲਪ 1: Spring Boot Dashboard ਵਰਤ ਕੇ (VS ਕੋਡ ਵਰਤੋਂਕਾਰਾਂ ਲਈ ਸਿਫਾਰਸ਼ੀ)**

ਡਿਵ ਕਨਟੇਨਰ ਵਿੱਚ Spring Boot Dashboard ਐਕਸਟੇੰਸ਼ਨ ਸ਼ਾਮਿਲ ਹੈ, ਜੋ ਸਾਰੇ Spring Boot ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਮੈਨੇਜ ਕਰਨ ਲਈ ਵਿਜ਼ੂਅਲ ਇੰਟਰਫੇਸ ਦਿੰਦਾ ਹੈ। ਤੁਸੀਂ ਇਸ ਨੂੰ VS ਕੋਡ ਦੇ ਖੱਬੇ ਪਾਸੇ ਕਿਰਿਆ ਬਾਰ ਵਿੱਚ ਸਪ੍ਰਿੰਗ ਬੂਟ ਦੇ ਆਈਕਨ ਵਾਂਗ ਵੇਖ ਸਕਦੇ ਹੋ।

Spring Boot ਡੈਸ਼ਬੋਰਡ ਤੋਂ, ਤੁਸੀਂ:
- ਵਰਕਸਪੇਸ ਵਿੱਚ ਸਾਰੇ ਉਪਲਬਧ Spring Boot ਐਪਲੀਕੇਸ਼ਨ ਵੇਖ ਸਕਦੇ ਹੋ
- ਇੱਕ ਹੀ ਕਲਿੱਕ ਨਾਲ ਐਪਲੀਕੇਸ਼ਨ ਸ਼ੁਰੂ/ਰੋਕ ਸਕਦੇ ਹੋ
- ਐਪਲੀਕੇਸ਼ਨ ਲਾਗਜ਼ ਲਾਈਵ ਦੇਖ ਸਕਦੇ ਹੋ
- ਐਪਲੀਕੇਸ਼ਨ ਦੀ ਸਥਿਤੀ ਨਿਗਰਾਨੀ ਕਰ ਸਕਦੇ ਹੋ

ਸਿੱਧਾ "introduction" ਦੇ ਨੇੜੇ ਪਲੇ ਬਟਨ 'ਤੇ ਕਲਿੱਕ ਕਰੋ ਇਸ ਮੋਡੀਊਲ ਨੂੰ ਚਲਾਉਣ ਲਈ, ਜਾਂ ਸਾਰੇ ਮੋਡੀਊਲ ਇਕੱਠੇ ਚਾਲੂ ਕਰੋ।

<img src="../../../translated_images/pa/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**ਵਿਕਲਪ 2: shell ਸਕ੍ਰਿਪਟ ਵਰਤ ਕੇ**

ਸਾਰੇ ਵੈੱਬ ਐਪਲੀਕੇਸ਼ਨ (ਮੋਡੀਊਲ 01-04) ਸ਼ੁਰੂ ਕਰੋ:

**Bash:**
```bash
cd ..  # ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਤੋਂ
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਤੋਂ
.\start-all.ps1
```


ਜਾਂ ਸਿਰਫ ਇਹ ਮੋਡੀਊਲ ਸ਼ੁਰੂ ਕਰੋ:

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


ਦੁਹਾਂ ਸਕ੍ਰਿਪਟਾਂ ਆਪਮੈਟਿਕ `.env` ਫਾਇਲ ਤੋਂ ਏਨਵਾਇਰਨਮੈਂਟ ਵੈਰੀਏਬਲ ਲੋਡ ਕਰਦੀਆਂ ਹਨ ਅਤੇ ਜੇ JAR ਨਹੀਂ ਹਨ, ਤਾਂ ਉਨ੍ਹਾਂ ਨੂੰ ਬਣਾਉਂਦੀਆਂ ਹਨ।

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਸਾਰੇ ਮੋਡੀਊਲ ਹੱਥੋਂ ਬਣਾਉਣੇ ਪਸੰਦ ਕਰਦੇ ਹੋ ਤਦੋਂ:
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


ਆਪਣੇ ਬ੍ਰਾਊਜ਼ਰ ਵਿੱਚ http://localhost:8080 ਖੋਲ੍ਹੋ।

**ਰੋਕਣ ਲਈ:**

**Bash:**
```bash
./stop.sh  # ਸਿਰਫ ਇਹ ਮੋਡਯੂਲ
# ਜਾਂ
cd .. && ./stop-all.sh  # ਸਾਰੇ ਮੋਡਯੂਲ
```

**PowerShell:**
```powershell
.\stop.ps1  # ਸਿਰਫ ਇਹ ਮੋਡੀਊਲ
# ਜਾਂ
cd ..; .\stop-all.ps1  # ਸਾਰੇ ਮੋਡੀਊਲ
```


## ਐਪਲੀਕੇਸ਼ਨ ਦੀ ਵਰਤੋਂ ਕਰਨਾ

ਐਪਲੀਕੇਸ਼ਨ ਇੱਕ ਵੈੱਬ ਇੰਟਰਫੇਸ ਦਿੰਦਾ ਹੈ ਜਿਸ ਵਿੱਚ ਦੋ ਚੈਟ ਇੰਪਲੀਮੈਂਟੇਸ਼ਨ ਇਕੱਠੇ ਹਨ।

<img src="../../../translated_images/pa/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*ਡੈਸ਼ਬੋਰਡ ਜੋ Simple Chat (ਸਟੇਟਲੈੱਸ) ਅਤੇ Conversational Chat (ਸਟੇਟਫੁਲ) ਵਿਕਲਪ ਦਿਖਾ ਰਿਹਾ ਹੈ*

### ਸਟੇਟਲੈੱਸ ਚੈਟ (ਖੱਬਾ ਪੈਨਲ)

ਇਸ ਨੂੰ ਪਹਿਲਾਂ ਕੋਸ਼ਿਸ਼ ਕਰੋ। ਪੁੱਛੋ "ਮੇਰਾ ਨਾਮ ਜੋਨ ਹੈ" ਅਤੇ ਤੁਰੰਤ ਬਾਅਦ ਪੁੱਛੋ "ਮੇਰਾ ਨਾਮ ਕੀ ਹੈ?" ਮਾਡਲ ਯਾਦ ਨਹੀਂ ਕਰੇਗਾ ਕਿਉਂਕਿ ਹਰ ਸੁਨੇਹਾ ਅਜ਼ਾਦ ਹੈ। ਇਹ ਸਧਾਰਨ ਭਾਸ਼ਾ ਮਾਡਲ ਇਨਟੀਗ੍ਰੇਸ਼ਨ ਦੀ ਮੁੱਖ ਸਮੱਸਿਆ ਨੂੰ ਦਰਸਾਉਂਦਾ ਹੈ - ਗੱਲਬਾਤ ਦਾ ਕੋਈ ਸੰਦਰਭ ਨਹੀਂ।

<img src="../../../translated_images/pa/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI ਤੁਹਾਡੇ ਪਿਛਲੇ ਸੁਨੇਹੇ ਵਿੱਚ ਕਿਹਾ ਨਾਮ ਯਾਦ ਨਹੀਂ ਰੱਖਦਾ*

### ਸਟੇਟਫੁਲ ਚੈਟ (ਸਤੰਬਰ ਪੈਨਲ)

ਹੁਣ ਇੱਥੇ ਉਹੀ ਲੜੀ ਕੋਸ਼ਿਸ਼ ਕਰੋ। ਪੁੱਛੋ "ਮੇਰਾ ਨਾਮ ਜੋਨ ਹੈ" ਅਤੇ ਫਿਰ "ਮੇਰਾ ਨਾਮ ਕੀ ਹੈ?" ਇਸ ਵਾਰੀ ਯਾਦ ਰੱਖਦਾ ਹੈ। ਅੰਤਰ MessageWindowChatMemory ਹੈ - ਇਹ ਗੱਲਬਾਤ ਇਤਿਹਾਸ ਨੂੰ ਕਾਇਮ ਰੱਖਦਾ ਹੈ ਅਤੇ ਹਰ ਬੇਨਤੀ ਨਾਲ ਸ਼ਾਮਿਲ ਕਰਦਾ ਹੈ। ਇਹ ਪ੍ਰੋਡਕਸ਼ਨ ਗੱਲਬਾਤੀ AI ਦੀ ਬਣਤਰ ਹੈ।

<img src="../../../translated_images/pa/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI ਤੁਹਾਡੇ ਪਹਿਲਾਂ ਕਹੇ ਗਿਆ ਨਾਮ ਯਾਦ ਰੱਖਦਾ ਹੈ*

ਦੋਵੇਂ ਪੈਨਲ ਇੱਕੋ GPT-5.2 ਮਾਡਲ ਵਰਤਦੇ ਹਨ। ਇਕੱਲਾ ਫਰਕ ਯਾਦਦਾਸ਼ਤ ਹੈ। ਇਹ ਸਪਸ਼ਟ ਕਰਦਾ ਹੈ ਕਿ ਯਾਦਦਾਸ਼ਤ ਤੁਹਾਡੇ ਐਪਲੀਕੇਸ਼ਨ ਲਈ ਕੀ ਲਿਆਉਂਦੀ ਹੈ ਅਤੇ ਕਿਉਂ ਅਸਲੀ ਵਰਤੋਂ ਲਈ ਇਹ ਜ਼ਰੂਰੀ ਹੈ।

## ਅੱਗਲਾ ਕਦਮ

**ਅਗਲਾ ਮੋਡੀਊਲ:** [02-prompt-engineering - GPT-5.2 ਨਾਲ ਪ੍ਰੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ](../02-prompt-engineering/README.md)

---

**ਨੈਵੀਗੇਸ਼ਨ:** [← ਪਿਛਲਾ: Module 00 - Quick Start](../00-quick-start/README.md) | [ਮੁੱਖ ਪੰਨਾ](../README.md) | [ਅਗਲਾ: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਡਿਸਕਲੇਮਰ**:  
ਇਸ ਦਸਤਾਵੇਜ਼ ਦਾ ਅਨੁਵਾਦ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦਕਿ ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਕੋਸ਼ਿਸ਼ ਕਰਦੇ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਇਹ ਜਾਣੋ ਕਿ ਆਟੋਮੈਟਿਕ ਅਨੁਵਾਦ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸਹੀਤਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਪ੍ਰਮਾਣਿਕ ਸਰੋਤ ਵਜੋਂ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਮਹੱਤਵਪੂਰਨ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਨਾਲ ਹੋਣ ਵਾਲੀਆਂ ਕਿਸੇ ਵੀ ਗਲਤਫ਼ਹਮੀਆਂ ਜਾਂ ਵਿਸ਼ਲੇਸ਼ਣ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->