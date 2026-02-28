# ਮੋਡੀਊਲ 01: LangChain4j ਨਾਲ ਸ਼ੁਰੂਆਤ

## ਸਮੱਗਰੀ ਸੂਚੀ

- [ਵੀਡੀਓ ਵਾਕਥਰੂ](../../../01-introduction)
- [ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ](../../../01-introduction)
- [ਪੂਰਵ ਸ਼ਰਤਾਂ](../../../01-introduction)
- [ਮੁੱਖ ਸਮੱਸਿਆ ਨੂੰ ਸਮਝਣਾ](../../../01-introduction)
- [ਟੋਕਨਸ ਨੂੰ ਸਮਝਣਾ](../../../01-introduction)
- [ਮੈਮੋਰੀ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀ ਹੈ](../../../01-introduction)
- [ਇਹ ਕਿਵੇਂ LangChain4j ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ](../../../01-introduction)
- [Azure OpenAI ਢਾਂਚਾ ਤਿਆਰ ਕਰੋ](../../../01-introduction)
- [ਐਪਲੀਕੇਸ਼ਨ ਨੂੰ ਲੋਕਲ ਚਲਾਓ](../../../01-introduction)
- [ਐਪਲੀਕੇਸ਼ਨ ਦੀ ਵਰਤੋਂ](../../../01-introduction)
  - [ਸਟੇਟਲੈੱਸ ਚੈਟ (ਖੱਬਾ ਪੈਨਲ)](../../../01-introduction)
  - [ਸਟੇਟਫੁਲ ਚੈਟ (ਸੱਜਾ ਪੈਨਲ)](../../../01-introduction)
- [ਅਗਲੇ ਕਦਮ](../../../01-introduction)

## ਵੀਡੀਓ ਵਾਕਥਰੂ

ਇਸ ਲਾਈਵ ਸੈਸ਼ਨ ਨੂੰ ਦੇਖੋ ਜੋ ਇਸ ਮੋਡੀਊਲ ਨਾਲ ਸ਼ੁਰੂਆਤ ਕਰਨ ਦੇ ਤਰੀਕੇ ਨੂੰ ਸਮਝਾਉਂਦਾ ਹੈ:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ

ਜੇ ਤੁਸੀਂ ਕווਿਕ ਸਟਾਰਟ ਪੂਰਾ ਕੀਤਾ ਤਾਂ ਤੁਸੀਂ ਵੇਖਿਆ ਕਿ ਕਿਵੇਂ ਪ੍ਰਾਂਪਟ ਭੇਜੇ ਜਾਂਦੇ ਹਨ ਅਤੇ ਜਵਾਬ ਮਿਲਦੇ ਹਨ। ਇਹ ਬੁਨਿਆਦ ਹੈ, ਪਰ ਅਸਲ ਐਪਲੀਕੇਸ਼ਨਜ਼ ਨੂੰ ਹੋਰ ਕੁਝ ਚਾਹੀਦਾ ਹੈ। ਇਹ ਮੋਡੀਊਲ ਤੁਹਾਨੂੰ ਸਿੱਖਾਉਂਦਾ ਹੈ ਕਿ ਕਿਵੇਂ ਗੱਲਬਾਤੀ ਏਆਈ ਬਣਾਈ ਜਾਵੇ ਜੋ ਸੰਦਰਭ ਯਾਦ ਰੱਖਦੀ ਹੈ ਅਤੇ ਸਟੇਟ ਪ੍ਰਬੰਧਿਤ ਕਰਦੀ ਹੈ - ਜੋ ਇੱਕ ਵਾਰੀ ਵਰਤੋਂ ਵਾਲੇ ਡੈਮੋ ਅਤੇ ਤਿਆਰ ਉਤਪਾਦ ਐਪਲੀਕੇਸ਼ਨ ਵਿਚ ਅੰਤਰ ਹੈ।

ਅਸੀਂ ਇਸ ਗਾਈਡ ਵਿੱਚ Azure OpenAI ਦਾ GPT-5.2 ਵਰਤਾਂਗੇ ਕਿਉਂਕਿ ਇਸ ਦੀ ਉੱਨਤ ਤਰਕਸ਼ੀਲਤਾ ਵੱਖ-ਵੱਖ ਪੈਟਰਨਜ਼ ਦੀ ਵਰਤੋਂ ਸਮਝਣ ਵਿੱਚ ਮਦਦ ਕਰਦੀ ਹੈ। ਜਦੋਂ ਤੁਸੀਂ ਮੈਮੋਰੀ ਜੋੜੋਂਗੇ, ਤਦ ਤੁਹਾਨੂੰ ਫਰਕ ਸਪਸ਼ਟ ਹੋਵੇਗਾ। ਇਸ ਨਾਲ ਇਹ ਸਮਝਣਾ ਆਸਾਨ ਹੋ ਜਾਂਦਾ ਹੈ ਕਿ ਹਰ ਹਿੱਸਾ ਤੁਹਾਡੇ ਐਪਲੀਕੇਸ਼ਨ ਵਿੱਚ ਕੀ ਲੈ ਕੇ ਆਉਂਦਾ ਹੈ।

ਤੁਸੀਂ ਇੱਕ ਐਪਲੀਕੇਸ਼ਨ ਬਣਾਉਗੇ ਜੋ ਦੋਹਾਂ ਪੈਟਰਨਜ਼ ਨੂੰ ਦਰਸਾਉਂਦਾ ਹੈ:

**ਸਟੇਟਲੈੱਸ ਚੈਟ** - ਹਰ ਬੇਨਤੀ ਆਜ਼ਾਦ ਹੈ। ਮਾਡਲ ਨੂੰ ਪਿਛਲੇ ਸਨੇਹਿਆਂ ਦੀ ਕੋਈ ਯਾਦ ਨਹੀਂ। ਇਹ ਉਹ ਪੈਟਰਨ ਹੈ ਜੋ ਤੁਸੀਂ ਕווਿਕ ਸਟਾਰਟ ਵਿੱਚ ਵਰਤਿਆ ਸੀ।

**ਸਟੇਟਫੁਲ ਗੱਲਬਾਤ** - ਹਰ ਬੇਨਤੀ ਵਿੱਚ ਗੱਲਬਾਤ ਦਾ ਇਤਿਹਾਸ ਸ਼ਾਮਲ ਹੁੰਦਾ ਹੈ। ਮਾਡਲ ਕਈ ਮੁੜਾਂ ਦੁਆਰਾ ਸੰਦਰਭ ਬਰਕਰਾਰ ਰੱਖਦਾ ਹੈ। ਇਹ ਉਹ ਹੈ ਜੋ ਪ੍ਰੋਡਕਸ਼ਨ ਐਪਲੀਕੇਸ਼ਨਜ਼ ਲਈ ਜ਼ਰੂਰੀ ਹੈ।

## ਪੂਰਵ ਸ਼ਰਤਾਂ

- Azure ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਜਿਸ ਵਿੱਚ Azure OpenAI ਤੱਕ ਪਹੁੰਚ ਹੈ
- ਜਾਵਾ 21, ਮੈਵਨ 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure ਡਿਵੈਲਪਰ CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **ਨੋਟ:** ਜਾਵਾ, ਮੈਵਨ, Azure CLI ਅਤੇ Azure ਡਿਵੈਲਪਰ CLI (azd) ਪਹਿਲਾਂ ਤੋਂ ਮੁਹੱਈਆ ਹਨ ਡੈਵਕੰਟੇਨਰ ਵਿੱਚ।

> **ਨੋਟ:** ਇਹ ਮੋਡੀਊਲ Azure OpenAI 'ਤੇ GPT-5.2 ਵਰਤਦਾ ਹੈ। ਡਿਪਲੋਇਮੈਂਟ `azd up` ਰਾਹੀਂ ਆਟੋਮੈਟਿਕ ਤੌਰ 'ਤੇ ਕਨਫਿਗਰ ਹੁੰਦਾ ਹੈ - ਕੋਡ ਵਿੱਚ ਮਾਡਲ ਦਾ ਨਾਮ ਬਦਲੋ ਨਾ।

## ਮੁੱਖ ਸਮੱਸਿਆ ਨੂੰ ਸਮਝਣਾ

ਭਾਸ਼ਾ ਮਾਡਲ ਸਟੇਟਲੈੱਸ ਹੁੰਦੇ ਹਨ। ਹਰ API ਕਾਲ ਆਜ਼ਾਦ ਹੁੰਦੀ ਹੈ। ਜੇ ਤੁਸੀਂ "ਮੇਰਾ ਨਾਮ ਜੌਨ ਹੈ" ਭੇਜਦੇ ਹੋ ਅਤੇ ਫਿਰ ਪੁੱਛਦੇ ਹੋ "ਮੇਰਾ ਨਾਮ ਕੀ ਹੈ?", ਤਾਂ ਮਾਡਲ ਦੇ ਕੋਲ ਇਹ ਜਾਣਕਾਰੀ ਨਹੀਂ ਹੁੰਦੀ ਕਿ ਤੁਸੀਂ ਹੁਣੇ ਆਪਣੇ ਆਪ ਨੂੰ ਮਿਰਪਹੀਚਾਨ ਕਰਵਾਇਆ ਸੀ। ਇਹ ਹਰ ਬੇਨਤੀ ਨੂੰ ਇੱਕ ਨਵੀਂ ਗੱਲਬਾਤ ਵਾਂਗ ਸਮਝਦਾ ਹੈ।

ਇਹ ਸਧਾਰਨ ਸਵਾਲ-ਜਵਾਬ ਲਈ ਠੀਕ ਹੈ ਪਰ ਅਸਲ ਐਪਲੀਕੇਸ਼ਨਜ਼ ਲਈ ਬੇਕਾਰ। ਗਾਹਕ ਸੇਵਾ ਬੋਟਾਂ ਨੂੰ ਯਾਦ ਰੱਖਣਾ ਲਾਜ਼ਮੀ ਹੈ ਕਿ ਤੁਸੀਂ ਕੀ ਦੱਸਿਆ ਹੈ। ਨਿੱਜੀ ਸਹਾਇਕਾਂ ਨੂੰ ਸੰਦਰਭ ਦੀ ਲੋੜ ਹੁੰਦੀ ਹੈ। ਕਿਸੇ ਵੀ ਕਈ-ਮੁੜ ਗੱਲਬਾਤ ਲਈ ਮੈਮੋਰੀ ਜ਼ਰੂਰੀ ਹੈ।

<img src="../../../translated_images/pa/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*ਸਟੇਟਲੈੱਸ (ਆਜ਼ਾਦ ਕਾਲਾਂ) ਅਤੇ ਸਟੇਟਫੁਲ (ਸੰਦਰਭ-ਜਾਣੂ) ਗੱਲਬਾਤਾਂ ਵਿੱਚ ਫਰਕ*

## ਟੋਕਨਸ ਨੂੰ ਸਮਝਣਾ

ਗੱਲਬਾਤਾਂ ਵਿੱਚ ਜਾਣ ਤੋਂ ਪਹਿਲਾਂ, ਟੋਕਨਸ ਨੂੰ ਸਮਝਣਾ ਜਰੂਰੀ ਹੈ - ਟੈਕਸਟ ਦੇ ਬੁਨਿਆਦੀ ਇਕਾਈਆਂ ਜੋ ਭਾਸ਼ਾ ਮਾਡਲ ਪ੍ਰਕਿਰਿਆ ਕਰਦੇ ਹਨ:

<img src="../../../translated_images/pa/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*ਉਦਾਹਰਨ ਦਿਖਾਉਂਦਾ ਹੈ ਕਿ ਟੈਕਸਟ ਨੂੰ ਕਿਵੇਂ ਟੋਕਨਸ ਵਿੱਚ ਵੰਡਿਆ ਜਾਂਦਾ ਹੈ - "I love AI!" 4 ਅਲੱਗ ਪ੍ਰਕਿਰਿਆ ਇਕਾਈਆਂ ਬਣ ਜਾਂਦੀਆਂ ਹਨ*

ਟੋਕਨਸ ਹੀ ਏਆਈ ਮਾਡਲ ਟੈਕਸਟ ਨੂੰ ਮਾਪਦੇ ਅਤੇ ਪ੍ਰਕਿਰਿਆ ਕਰਦੇ ਹਨ। ਸ਼ਬਦ, ਵਿਰਾਮ-ਚਿੰਨ੍ਹ, ਅਤੇ ਖਾਲੀ ਥਾਵਾਂ ਵੀ ਟੋਕਨ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਤੁਹਾਡੇ ਮਾਡਲ ਦੀ ਕਿਸੇ ਵੇਲੇ ਪ੍ਰਕਿਰਿਆ ਕਰਨ ਵਾਲੀਆਂ ਟੋਕਨਸ ਦੀ ਸੀਮਾ ਹੁੰਦੀ ਹੈ (GPT-5.2 ਵਿੱਚ 400,000, ਜਿਸ ਵਿੱਚ 272,000 ਇਨਪੁੱਟ ਅਤੇ 128,000 ਆਉਟਪੁੱਟ ਟੋਕਨ ਹਨ)। ਟੋਕਨ ਦੀ ਸਮਝ ਤੁਹਾਨੂੰ ਗੱਲਬਾਤ ਦੀ ਲੰਬਾਈ ਅਤੇ ਖਰਚਿਆਂ ਨੂੰ ਸੰਭਾਲਣ ਵਿੱਚ ਮਦਦ ਕਰਦੀ ਹੈ।

## ਮੈਮੋਰੀ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀ ਹੈ

ਚੈਟ ਮੈਮੋਰੀ ਸਟੇਟਲੈੱਸ ਸਮੱਸਿਆ ਦਾ ਸਮਾਧਾਨ ਹੈ ਜੋ ਗੱਲਬਾਤ ਇਤਿਹਾਸ ਨੂੰ ਸੰਭਾਲਦੀ ਹੈ। ਮਾਡਲ ਨੂੰ ਆਪਣੀ ਬੇਨਤੀ ਭੇਜਣ ਤੋਂ ਪਹਿਲਾਂ, ਫਰੇਮਵਰਕ ਲੋੜੀਂਦੇ ਪਿਛਲੇ ਸਨੇਹੇ ਜੋੜਦਾ ਹੈ। ਜਦੋਂ ਤੁਸੀਂ ਪੁੱਛਦੇ ਹੋ "ਮੇਰਾ ਨਾਮ ਕੀ ਹੈ?", ਤਾਂ ਸਿਸਟਮ ਅਸਲ ਵਿੱਚ ਪੂਰਾ ਗੱਲਬਾਤ ਇਤਿਹਾਸ ਭੇਜਦਾ ਹੈ ਤਾਂ ਜੋ ਮਾਡਲ ਵੇਖ ਸਕੇ ਕਿ ਤੁਸੀਂ ਪਹਿਲਾਂ ਕਿਹਾ ਸੀ "ਮੇਰਾ ਨਾਮ ਜੌਨ ਹੈ।"

LangChain4j ਇਹ ਮੈਮੋਰੀ ਇੰਪਲੀਮੈਂਟੇਸ਼ਨ ਆਪਣੇ ਆਪ ਸੰਭਾਲਦਾ ਹੈ। ਤੁਸੀਂ ਇਹ ਚੁਣਦੇ ਹੋ ਕਿ ਕਿੰਨੇ ਸੁਨੇਹੇ ਰੱਖਣੇ ਹਨ ਅਤੇ ਫਰੇਮਵਰਕ ਸੰਦਰਭ ਵਿੰਡੋ ਨੂੰ ਸੰਭਾਲਦਾ ਹੈ।

<img src="../../../translated_images/pa/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory ਹਾਲੀਆ ਸੁਨੇਹਿਆਂ ਦੀ ਇੱਕ ਸਲਾਈਡਿੰਗ ਵਿੰਡੋ ਨੂੰ ਰੱਖਦਾ ਹੈ, ਪੁਰਾਣੇ ਸੁਨੇਹਿਆਂ ਨੂੰ ਆਪਣੇ ਆਪ ਹਟਾਉਂਦਾ ਹੈ*

## ਇਹ ਕਿਵੇਂ LangChain4j ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ

ਇਹ ਮੋਡੀਊਲ ਕווਿਕ ਸਟਾਰਟ ਨੂੰ ਵਧਾਉਂਦਾ ਹੈ ਸਪ੍ਰਿੰਗ ਬੂਟ ਨਾਲ ਇੰਟਿਗ੍ਰੇਸ਼ਨ ਕਰਕੇ ਅਤੇ ਗੱਲਬਾਤ ਮੈਮੋਰੀ ਸ਼ਾਮਲ ਕਰਕੇ। ਹੇਠਾਂ ਦਿਖਾਇਆ ਗਿਆ ਹੈ ਕਿ ਟੁਕੜੇ ਕਿਵੇਂ ਮਿਲਦੇ ਹਨ:

**ਡਿਪੈਂਡੇਨਸੀਜ਼** - ਦੋ LangChain4j ਲਾਇਬ੍ਰੇਰੀਆਂ ਸ਼ਾਮਲ ਕਰੋ:

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

**ਚੈਟ ਮਾਡਲ** - Azure OpenAI ਨੂੰ ਸਪ੍ਰਿੰਗ ਬੀਨ ਵਜੋਂ ਕਨਫ਼ਿਗਰ ਕਰੋ ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

ਬਿਲਡਰ `azd up` ਵੱਲੋਂ ਸੈੱਟ ਕੀਤੇ Env ਵੈਰੀਏਬਲਾਂ ਵਿੱਚੋਂ ਪਰਮਾਣ ਪੱਤਰ ਪੜ੍ਹਦਾ ਹੈ। `baseUrl` ਨੂੰ ਆਪਣੇ Azure ਐਂਡਪੌਇੰਟ ਨਾਲ ਸੈੱਟ ਕਰਨਾ OpenAI ਕਲਾਇੰਟ ਨੂੰ Azure OpenAI ਨਾਲ ਕੰਮ ਕਰਨ ਵਿੱਚ ਸਹਾਇਕ ਬਣਾਉਂਦਾ ਹੈ।

**ਗੱਲਬਾਤ ਮੈਮੋਰੀ** - MessageWindowChatMemory ਨਾਲ ਗੱਲਬਾਤ ਇਤਿਹਾਸ ਟਰੈਕ ਕਰੋ ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` ਨਾਲ ਮੈਮੋਰੀ ਬਣਾਓ ਤਾਂ ਜੋ ਆਖਰੀ 10 ਸੁਨੇਹੇ ਰੱਖੇ ਜਾ ਸਕਣ। Typed wrappers ਵਰਤ ਕੇ ਯੂਜ਼ਰ ਅਤੇ ਏਆਈ ਸੁਨੇਹੇ ਸ਼ਾਮਲ ਕਰੋ: `UserMessage.from(text)` ਅਤੇ `AiMessage.from(text)`। ਇਤਿਹਾਸ ਨੂੰ `memory.messages()` ਨਾਲ ਲਓ ਅਤੇ ਮਾਡਲ ਨੂੰ ਭੇਜੋ। ਸਰਵਿਸ ਹਰੇਕ ਗੱਲਬਾਤ ID ਲਈ ਵੱਖਰੀ ਮੈਮੋਰੀINSTANCEਜ ਸਟੋਰ ਕਰਦੀ ਹੈ, ਕਈ ਯੂਜ਼ਰ ਇਕੱਠੇ ਗੱਲਬਾਤ ਕਰ ਸਕਦੇ ਹਨ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ਚੈਟ ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:
> - ਜਦ ਵਿੰਡੋ ਭਰ ਜਾਂਦੀ ਹੈ ਤਾਂ MessageWindowChatMemory ਕਿਹੜੇ ਸੁਨੇਹੇ ਗਿਰਾਉਂਦਾ ਹੈ?
> - ਕੀ ਮੈਂ in-memory ਦੀ ਥਾਂ ਡੇਟਾਬੇਸ ਤੋਂ ਕਸਟਮ ਮੈਮੋਰੀ ਸਟੋਰੇਜ ਬਣਾਉਂ ਸਕਦਾ ਹਾਂ?
> - ਪੁਰਾਣੇ ਗੱਲਬਾਤ ਇਤਿਹਾਸ ਨੂੰ ਸੰਖੇਪ ਕਰਨ ਲਈ ਮੈਂ ਸਾਰਾਂਸ਼ ਜੋੜ ਸਕਦਾ ਹਾਂ?

ਸਟੇਟਲੈੱਸ ਚੈਟ ਐਂਡਪੌਇੰਟ ਮੈਮੋਰੀ ਪੂਰੀ ਤਰ੍ਹਾਂ ਛੱਡ ਦਿੰਦਾ ਹੈ - ਸਿਰਫ਼ `chatModel.chat(prompt)` ਕਿਵੇਂ ਕਿਵਿਕ ਸਟਾਰਟ ਵਿੱਚ ਸੀ। ਸਟੇਟਫੁਲ ਐਂਡਪੌਇੰਟ ਸੁਨੇਹੇ ਮੈਮੋਰੀ ਵਿੱਚ ਜੋੜਦਾ ਹੈ, ਇਤਿਹਾਸ ਲੈਂਦਾ ਹੈ ਅਤੇ ਹਰ ਬੇਨਤੀ ਨਾਲ ਸੰਦਰਭ ਸ਼ਾਮਲ ਕਰਦਾ ਹੈ। ਇੱਕੋ ਮਾਡਲ ਕਨਫਿਗਰੇਸ਼ਨ, ਵੱਖਰੇ ਪੈਟਰਨਜ਼।

## Azure OpenAI ਢਾਂਚਾ ਤਿਆਰ ਕਰੋ

**ਬੈਸ਼:**
```bash
cd 01-introduction
azd up  # ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਅਤੇ ਸਥਾਨ ਚੁਣੋ (eastus2 ਸਿਫਾਰਸ਼ੀ)
```

**ਪਾਵਰਸ਼ੈੱਲ:**
```powershell
cd 01-introduction
azd up  # ਚੁਣੋ ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਅਤੇ ਸਥਾਨ (eastus2 ਸੁਝਾਇਆ ਗਿਆ)
```

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ timeout error ਵਿੱਚ ਮਿਲਦੇ ਹੋ (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), ਤਾਂ ਸਿਰਫ਼ `azd up` ਫਿਰ ਚਲਾਓ। Azure ਸਰੋਤ ਪਿੱਛੇProvisioning ਵਿੱਚ ਹੋ ਸਕਦੇ ਹਨ, ਦੁਬਾਰਾ ਕੋਸ਼ਿਸ਼ ਕਰਨ ਨਾਲ ਡਿਪਲੋਇਮੈਂਟ ਸੰਪੂਰਨ ਹੁੰਦੀ ਹੈ ਜਦ ਸਰੋਤ ਟਰਮੀਨਲ ਸਥਿਤੀ ਵਿੱਚ ਪਹੁੰਚਦੇ ਹਨ।

ਇਹ ਕਰੇਗਾ:
1. Azure OpenAI ਸਰੋਤ GPT-5.2 ਅਤੇ text-embedding-3-small ਮਾਡਲਜ਼ ਨਾਲ ਡਿਪਲੋਇਮੈਂਟ
2. ਪ੍ਰੋਜੈਕਟ ਰੂਟ ਵਿੱਚ ਆਟੋਮੈਟਿਕ `.env` ਫਾਈਲ ਬਣਾਈ ਜਾਵੇਗੀ ਜੋ ਪਰਮਾਣ ਪੱਤਰਾਂ ਨਾਲ ਭਰੀ ਹੋਈ ਹੋਵੇਗੀ
3. ਸਾਰੇ ਲੋੜੀਂਦੇ ਵਾਤਾਵਰਣ ਵੈਰੀਏਬਲ ਸੈੱਟ ਕਰੇਗਾ

**ਡਿਪਲੋਇਮੈਂਟ ਵਿੱਚ ਸਮੱਸਿਆ ਆ ਰਹੀ ਹੈ?** [Infrastructure README](infra/README.md) ਪੜ੍ਹੋ ਜਿਸ ਵਿੱਚ ਸਬਡੋਮੇਨ ਨਾਂ ਟਕਰਾਅ, ਮੈਨੂਅਲ Azure ਪੋਰਟਲ ਡਿਪਲੋਇਮੈਂਟ ਅਤੇ ਮਾਡਲ ਸੈਟਿੰਗ ਲਾਈਨਾਂ ਦਿੱਤੀਆਂ ਹਨ।

**ਡਿਪਲੋਇਮੈਂਟ ਸਫਲ ਹੋਈ ਜਾਂ ਨਹੀਂ ਜਾਂਚੋ:**

**ਬੈਸ਼:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, ਆਦਿ ਦਿਖਾਉਣੀ ਚਾਹੀਦੀ ਹੈ।
```

**ਪਾਵਰਸ਼ੈੱਲ:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, ਆਦਿ ਦਿਖਾਉਣਾ ਚਾਹੀਦਾ ਹੈ।
```

> **ਨੋਟ:** `azd up` ਕਮਾਂਡ ਆਟੋਮੈਟਿਕ `.env` ਫਾਈਲ ਬਣਾਉਂਦਾ ਹੈ। ਜੇ ਤੁਹਾਨੂੰ ਬਾਅਦ ਵਿੱਚ ਇਸਨੂੰ ਅਪਡੇਟ ਕਰਨ ਦੀ ਲੋੜ ਹੋਵੇ ਤਾਂ ਤੁਸੀਂ `.env` ਫਾਈਲ ਨੂੰ ਨਾਲ ਹੀ ਜਾਂ ਫਿਰ ਦੁਬਾਰਾ ਬਣਾਉਣ ਲਈ ਚਲਾ ਸਕਦੇ ਹੋ:
>
> **ਬੈਸ਼:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **ਪਾਵਰਸ਼ੈੱਲ:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## ਐਪਲੀਕੇਸ਼ਨ ਨੂੰ ਲੋਕਲ ਚਲਾਓ

**ਡਿਪਲੋਇਮੈਂਟ ਦੀ ਪੁਸ਼ਟੀ ਕਰੋ:**

ਪੱਕਾ ਕਰੋ ਕਿ ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ `.env` ਫਾਈਲ ਮੌਜੂਦ ਹੈ ਜਿਸ ਵਿੱਚ Azure ਪਰਮਾਣਪੱਤਰ ਹਨ:

**ਬੈਸ਼:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ਦਿਖਾਉਣੀ ਚਾਹੀਦੀ ਹੈ
```

**ਪਾਵਰਸ਼ੈੱਲ:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ਦਿਖਾਉਣਾ ਚਾਹੀਦਾ ਹੈ
```

**ਐਪਲੀਕੇਸ਼ਨ ਚਲਾਓ:**

**ਚੋਣ 1: ਸਪ੍ਰਿੰਗ ਬੂਟ ਡੈਸ਼ਬੋਰਡ ਵਰਤਣਾ (VS ਕੋਡ ਲਈ ਸਿਫਾਰਸ਼ੀ)**

ਡੈਵ ਕੰਟੇਨਰ ਵਿੱਚ Spring Boot Dashboard ਐਕਸਟੇਂਸ਼ਨ ਸ਼ਾਮਲ ਹੈ, ਜੋ ਸਾਰੇ ਸਪ੍ਰਿੰਗ ਬੂਟ ਐਪਲੀਕੇਸ਼ਨ ਵਰਕਸਪੇਸ ਵਿੱਚ ਦਿਖਾਉਂਦਾ ਹੈ। ਤੁਸੀਂ ਇਸਨੂੰ VS ਕੋਡ ਦੀ ਐਕਟਿਵਿਟੀ ਬਾਰ ਵਿਚ ਖੱਬੇ ਪਾਸੇ ਦੇਖ ਸਕਦੇ ਹੋ (Spring Boot ਆਇਕਨ ਨੂੰ ਦੇਖੋ)।

Spring Boot ਡੈਸ਼ਬੋਰਡ ਤੋਂ:
- ਸਾਰੇ ਉਪਲਬਧ ਸਪ੍ਰਿੰਗ ਬੂਟ ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਵੇਖੋ
- ਇੱਕ ਕਲਿੱਕ ਨਾਲ ਐਪਲੀਕੇਸ਼ਨ ਸਟਾਰਟ/ਸਟਾਪ ਕਰੋ
- ਐਪਲੀਕੇਸ਼ਨ ਲਾਗ ਨੂੰ ਰੀਅਲ-ਟਾਈਮ ਵਿੱਚ ਵੇਖੋ
- ਐਪਲੀਕੇਸ਼ਨ ਸਥਿਤੀ ਨੂੰ ਮਾਨੀਟਰ ਕਰੋ

"introduction" ਦੇ ਕੋਲ ਪੁਲੇ ਬਟਨ ਨੂੰ ਕਲਿੱਕ ਕਰਕੇ ਮੋਡੀਊਲ ਸ਼ੁਰੂ ਕਰੋ, ਜਾਂ ਸਾਰੇ ਮੋਡੀਊਲਜ਼ ਨੂੰ ਇਕੱਠੇ ਸਟਾਰਟ ਕਰੋ।

<img src="../../../translated_images/pa/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**ਚੋਣ 2: ਸ਼ੈੱਲ ਸਕ੍ਰਿਪਟ ਵਰਤਣਾ**

ਸਾਰੇ ਵੈੱਬ ਐਪਲੀਕੇਸ਼ਨ (ਮੋਡੀਊਲ 01-04) ਸ਼ੁਰੂ ਕਰੋ:

**ਬੈਸ਼:**
```bash
cd ..  # ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਤੋਂ
./start-all.sh
```

**ਪਾਵਰਸ਼ੈੱਲ:**
```powershell
cd ..  # ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਤੋਂ
.\start-all.ps1
```

ਜਾਂ ਸਿਰਫ਼ ਇਹ ਮੋਡੀਊਲ ਸ਼ੁਰੂ ਕਰੋ:

**ਬੈਸ਼:**
```bash
cd 01-introduction
./start.sh
```

**ਪਾਵਰਸ਼ੈੱਲ:**
```powershell
cd 01-introduction
.\start.ps1
```

ਦੋਹਾਂ ਸਕ੍ਰਿਪਟਾਂ ਰੂਟ `.env` ਫਾਈਲ ਤੋਂ ਵਾਤਾਵਰਣ ਵੈਰੀਏਬਲ ਆਪਣੀਆយ getਤੀਆਂਦੇ ਹਨ ਅਤੇ ਜੇ jar ਬਣੇ ਨਹੀਂ ਹਨ ਤਾਂ ਉਹ ਬਣਾਉਂਦੀਆਂ ਹਨ।

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਸਾਰੇ ਮੋਡੀਊਲ ਪਹਿਲਾਂ ਹੀ ਮੈਨੂੰਅਲ ਤੌਰ ਤੇ ਬਣਾਉਣਾ ਚਾਹੁੰਦੇ ਹੋ ਤਾਂ:
>
> **ਬੈਸ਼:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **ਪਾਵਰਸ਼ੈੱਲ:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

http://localhost:8080 ਆਪਣੇ ਬ੍ਰਾਉਜ਼ਰ ਵਿੱਚ ਖੋਲ੍ਹੋ।

**ਰੋਕਣ ਲਈ:**

**ਬੈਸ਼:**
```bash
./stop.sh  # ਕੇਵਲ ਇਹ ਮੌਡੀਊਲ
# ਜਾਂ
cd .. && ./stop-all.sh  # ਸਾਰੇ ਮੌਡੀਊਲ
```

**ਪਾਵਰਸ਼ੈੱਲ:**
```powershell
.\stop.ps1  # ਸਿਰਫ ਇਹ ਮੌਡਿਊਲ
# ਜਾਂ
cd ..; .\stop-all.ps1  # ਸਾਰੇ ਮੌਡਿਊਲ
```

## ਐਪਲੀਕੇਸ਼ਨ ਦੀ ਵਰਤੋਂ

ਐਪਲੀਕੇਸ਼ਨ ਇੱਕ ਵੈੱਬ ਇੰਟਰਫੇਸ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ ਜਿਸ ਵਿੱਚ ਦੋ ਚੈਟ ਇੰਪਲੀਮੈਂਟੇਸ਼ਨ ਨਾਲ ਪਾਸੇ-ਪਾਸੇ ਦਿਖਾਈ ਦਿੰਦੇ ਹਨ।

<img src="../../../translated_images/pa/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*ਡੈਸ਼ਬੋਰਡ ਜੋ ਸਧਾਰਣ ਚੈਟ (ਸਟੇਟਲੈੱਸ) ਅਤੇ ਗੱਲਬਾਤੀ ਚੈਟ (ਸਟੇਟਫੁਲ) ਵਿਕਲਪ ਦਿਖਾਉਂਦਾ ਹੈ*

### ਸਟੇਟਲੈੱਸ ਚੈਟ (ਖੱਬਾ ਪੈਨਲ)

ਸਭ ਤੋਂ ਪਹਿਲਾਂ ਇਸਨੂੰ ਕੋਸ਼ਿਸ਼ ਕਰੋ। ਪੁੱਛੋ "ਮੇਰਾ ਨਾਮ ਜੌਨ ਹੈ" ਅਤੇ ਫਿਰ ਤੁਰੰਤ ਪੁੱਛੋ "ਮੇਰਾ ਨਾਮ ਕੀ ਹੈ?" ਮਾਡਲ ਯਾਦ ਨਹੀਂ ਰੱਖੇਗਾ ਕਿਉਂਕਿ ਹਰ ਸੁਨੇਹਾ ਆਜ਼ਾਦ ਹੈ। ਇਹ ਮੂਲ ਸਮੱਸਿਆ ਦਰਸਾਉਂਦਾ ਹੈ ਭਾਸ਼ਾ ਮਾਡਲ ਇੰਟਿਗ੍ਰੇਸ਼ਨ ਨਾਲ - ਕੋਈ ਗੱਲਬਾਤ ਸੰਦਰਭ ਨਹੀਂ।

<img src="../../../translated_images/pa/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*ਏਆਈ ਪਿਛਲੇ ਸੁਨੇਹੇ ਤੋਂ ਤੁਹਾਡਾ ਨਾਮ ਯਾਦ ਨਹੀਂ ਕਰਦਾ*

### ਸਟੇਟਫੁਲ ਚੈਟ (ਸੱਜਾ ਪੈਨਲ)

ਹੁਣ ਇੱਥੇ ਉਹੇ ਕ੍ਰਮ ਕੋਸ਼ਿਸ਼ ਕਰੋ। ਪੁੱਛੋ "ਮੇਰਾ ਨਾਮ ਜੌਨ ਹੈ" ਅਤੇ ਫਿਰ "ਮੇਰਾ ਨਾਮ ਕੀ ਹੈ?" ਇਸ ਵਾਰੀ ਇਹ ਯਾਦ ਰੱਖਦਾ ਹੈ। ਫਰਕ MessageWindowChatMemory ਹੈ - ਇਹ ਗੱਲਬਾਤ ਇਤਿਹਾਸ ਨੂੰ ਸੰਭਾਲਦਾ ਹੈ ਅਤੇ ਹਰ ਬੇਨਤੀ ਨਾਲ ਸੰਦਰਭ ਸ਼ਾਮਲ ਕਰਦਾ ਹੈ। ਇਹ ਉਹ ਹੈ ਜਿਸ ਤਰ੍ਹਾਂ ਪ੍ਰੋਡਕਸ਼ਨ ਗੱਲਬਾਤੀ ਏਆਈ ਕੰਮ ਕਰਦੀ ਹੈ।

<img src="../../../translated_images/pa/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*ਏਆਈ ਪਹਿਲਾਂ ਦੀ ਗੱਲਬਾਤ ਤੋਂ ਤੁਹਾਡਾ ਨਾਮ ਯਾਦ ਕਰਦਾ ਹੈ*

ਦੋਹਾਂ ਪੈਨਲ ਇੱਕੋ GPT-5.2 ਮਾਡਲ ਵਰਤਦੇ ਹਨ। ਇੱਕਲੌਤਾ ਫਰਕ ਮੈਮੋਰੀ ਹੈ। ਇਹ ਸਪਸ਼ਟ ਕਰਦਾ ਹੈ ਕਿ ਮੈਮੋਰੀ ਤੁਹਾਡੇ ਐਪਲੀਕੇਸ਼ਨ ਵਿੱਚ ਕੀ ਲਿਆਉਂਦੀ ਹੈ ਅਤੇ ਅਸਲੀ ਵਰਤੋਂ ਲਈ ਕਿਉਂ ਜ਼ਰੂਰੀ ਹੈ।

## ਅਗਲੇ ਕਦਮ

**ਅਗਲਾ ਮੋਡੀਊਲ:** [02-prompt-engineering - GPT-5.2 ਨਾਲ ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ](../02-prompt-engineering/README.md)

---

**ਨੇਵੀਗੇਸ਼ਨ:** [← ਪਹਿਲਾਂ: ਮੋਡੀਊਲ 00 - ਕווਿਕ ਸਟਾਰਟ](../00-quick-start/README.md) | [ਮੁੱਖ ਵਿੱਚ ਵਾਪਸੀ](../README.md) | [ਅਗਲਾ: ਮੋਡੀਊਲ 02 - ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਵੀਕਾਰੋਪੱਤਰ**:
ਇਹ ਦਸਤਾਵੇਜ਼ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦਿਤ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਮਹੱਤਵਪੂਰਨ ਕੋਸ਼ਿਸ਼ ਕਰਦੇ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਵਿੱਚ ਰੱਖੋ ਕਿ ਆਟੋਮੈਟਿਕ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸਤਿਰਤਾ ਹੋ ਸਕਦੀ ਹੈ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਪ੍ਰਮਾਣਿਕ ਸ੍ਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਅਹਮ ਜਾਣਕਾਰੀ ਲਈ, ਪ੍ਰੋਫੈਸ਼ਨਲ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫ਼ਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਨਾਲ ਹੋਣ ਵਾਲੀ ਕਿਸੇ ਵੀ ਗਲਤਫ਼ਹਮੀ ਜਾਂ ਗਲਤ ਸਮਝ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->