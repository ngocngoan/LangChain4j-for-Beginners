# ਮੋਡੀਊਲ 02: GPT-5.2 ਨਾਲ ਪ੍ਰੌਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ

## ਸਾਰਣੀ ਸਾਹਿਤ

- [ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ](../../../02-prompt-engineering)
- [ਪੂਰਵ-ਆਵਸ਼ਕਤਾਵਾਂ](../../../02-prompt-engineering)
- [ਪ੍ਰੌਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਦੀ ਸਮਝ](../../../02-prompt-engineering)
- [ਇਹ ਕਿਵੇਂ LangChain4j ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ](../../../02-prompt-engineering)
- [ਮੁੱਖ ਪੈਟਰਨ](../../../02-prompt-engineering)
- [ਮੌਜੂਦਾ Azure ਸ੍ਰੋਤਾਂ ਦੀ ਵਰਤੋਂ](../../../02-prompt-engineering)
- [ਐਪਲੀਕੇਸ਼ਨ ਸਕ੍ਰੀਨਸ਼ਾਟ](../../../02-prompt-engineering)
- [ਪੈਟਰਨ ਦੀ ਜਾਂਚ](../../../02-prompt-engineering)
  - [ਘੱਟ ਬੇਸਬਰੀ ਬਨਾਮ ਵੱਧ ਬੇਸਬਰੀ](../../../02-prompt-engineering)
  - [ਟਾਸਕ ਅਮਲੀ ਜਾਮਾ (ਟੂਲ ਪ੍ਰੀਐਂਬਲ)](../../../02-prompt-engineering)
  - [ਆਤਮ-ਮੁਖਾਵਲੀ ਕੋਡ](../../../02-prompt-engineering)
  - [ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ](../../../02-prompt-engineering)
  - [ਮਲਟੀ-ਟਰਨ ਚੈਟ](../../../02-prompt-engineering)
  - [ਕਦਮ-ਦਰ-ਕਦਮ ਤਰਕ](../../../02-prompt-engineering)
  - [ਸੀਮਿਤ ਅਉਟਪੁਟ](../../../02-prompt-engineering)
- [ਤੁਸੀਂ ਦਰਅਸਲ ਕੀ ਸਿੱਖ ਰਹੇ ਹੋ](../../../02-prompt-engineering)
- [ਅਗਲੇ ਕਦਮ](../../../02-prompt-engineering)

## ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ

ਪਿਛਲੇ ਮੋਡੀਊਲ ਵਿੱਚ, ਤੁਸੀਂ ਦੇਖਿਆ ਕਿ ਮੇਮੋਰੀ ਕਿਵੇਂ ਗੱਲਬਾਤ ਵਾਲੀ ਏਆਈ ਨੂੰ ਸੰਭਾਲਦੀ ਹੈ ਅਤੇ ਬੁਨਿਆਦੀ ਇੰਟਰੈਕਸ਼ਨਾਂ ਲਈ GitHub ਮਾਡਲਾਂ ਦੀ ਵਰਤੋਂ ਕੀਤੀ। ਹੁਣ ਅਸੀਂ ਧਿਆਨ ਦੇਵਾਂਗੇ ਕਿ ਤੁਸੀਂ ਸਵਾਲ ਕਿਵੇਂ ਪੁੱਛਦੇ ਹੋ—ਪ੍ਰੌਂਪਟ ਖੁਦ—Azure OpenAI ਦੇ GPT-5.2 ਦੀ ਵਰਤੋਂ ਕਰਦੇ ਹੋਏ। ਜਿਵੇਂ ਤੁਸੀਂ ਆਪਣੀਆਂ ਪ੍ਰੌਂਪਟਾਂ ਨੂੰ ਢਾਂਚਾ ਦਿੰਦੇ ਹੋ, ਇਹ ਤੁਹਾਨੂੰ ਮਿਲਣ ਵਾਲੇ ਜਵਾਬਾਂ ਦੀ ਗੁਣਵੱਤਾ ‘ਤੇ ਬਹੁਤ ਪ੍ਰਭਾਵ ਪਾਂਦਾ ਹੈ।

ਅਸੀਂ GPT-5.2 ਦੀ ਵਰਤੋਂ ਕਰਾਂਗੇ ਕਿਉਂਕਿ ਇਹ ਤਰਕਸ਼ੀਲਤਾ ਨਿਯੰਤਰਣ ਲਿਆਉਂਦਾ ਹੈ - ਤੁਸੀਂ ਮਾਡਲ ਨੂੰ ਦੱਸ ਸਕਦੇ ਹੋ ਕਿ ਜਵਾਬ ਦੇਣ ਤੋਂ ਪਹਿਲਾਂ ਕਿੰਨੀ ਸੋਚ ਕਰਨੀਆਂ ਹਨ। ਇਸ ਨਾਲ ਵੱਖ-ਵੱਖ ਪ੍ਰੌਂਪਟਿੰਗ ਰਣਨੀਤੀਆਂ ਹੋਰ ਦਰਸ਼ਨੀ ਹੋ ਜਾਂਦੀਆਂ ਹਨ ਅਤੇ ਇਹ ਸਮਝਣ ਵਿੱਚ ਮੱਦਦ ਮਿਲਦੀ ਹੈ ਕਿ ਹਰ ਪਹੁੰਚ ਕਦੋਂ ਵਰਤੋਂ। ਅਸੀਂ Azure ਦੀ ਘੱਟ ਦਰ ਸੀਮਾਵਾਂ ਦਾ ਵੀ ਫਾਇਦਾ ਪ੍ਰਾਪਤ ਕਰਾਂਗੇ ਜੋ GPT-5.2 ਲਈ GitHub ਮਾਡਲਾਂ ਨਾਲੋਂ ਵੱਧ ਹਨ।

## ਪੂਰਵ-ਆਵਸ਼ਕਤਾਵਾਂ

- ਮੋਡੀਊਲ 01 ਪੂਰਾ ਕੀਤਾ ਹੋਇਆ (Azure OpenAI ਸਰੇਆੜੇ ਡਿਪਲੋਇ ਕੀਤੇ)
- ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ `.env` ਫਾਈਲ ਜਿਸ ਵਿੱਚ Azure ਕ੍ਰੈਡੇਂਸ਼ੀਅਲਜ਼ ਹਨ (`azd up` ਦ੍ਵਾਰਾ ਮੋਡੀਊਲ 01 ਵਿੱਚ ਬਣਾਈ ਗਈ)

> **ਨੋਟ:** ਜੇ ਤੂੰ ਮੋਡੀਊਲ 01 ਪੂਰਾ ਨਹੀਂ ਕੀਤਾ, ਤਾਂ ਪਹਿਲਾਂ ਉਥੋਂ ਡਿਪਲੋਇ ਹੁਕਮਾਂ ਦੀ ਪਾਲਣਾ ਕਰੋ।

## ਪ੍ਰੌਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਦੀ ਸਮਝ

ਪ੍ਰੌਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਮਤਲਬ ਹੈ ਇੰਪੁਟ ਟੈਕਸਟ ਤਿਆਰ ਕਰਨਾ ਜੋ ਲਗਾਤਾਰ ਤੁਹਾਨੂੰ ਲੋੜੀਂਦਾ ਨਤੀਜਾ ਦਿੰਦਾ ਹੈ। ਇਹ ਸਿਰਫ ਸਵਾਲ ਪੁੱਛਣ ਬਾਰੇ ਨਹੀਂ, ਸਗੋਂ ਅਜਿਹਾ ਬੇਨਤੀ ਦੀ ਬਣਤਰ ਕਰਨਾ ਹੈ ਜਿਸ ਨਾਲ ਮਾਡਲ ਸੁਣਬੁਝ ਨਾਲ ਵੱਸਤੇ ਜਿੰਝਾ ਚਾਹੀਦਾ ਹੈ ਤੇ ਕਿਵੇਂ ਦੇਣਾ ਹੈ।

ਇਸਨੂੰ ਸੋਚੋ ਜਿਵੇਂ ਕਿਸੇ ਸਾਥੀ ਨੂੰ ਨਿਰਦੇਸ਼ ਦੇਣਾ। "ਬੱਗ ਠੀਕ ਕਰੋ" ਅਸਪਸ਼ਟ ਹੈ। "UserService.java ਲਾਈਨ 45 ਵਿੱਚ ਨੱਲ ਚੈੱਕ ਜੋੜ ਕੇ ਨੱਲ ਪੌਇੰਟਰ ਐਕਸਪਸ਼ਨ ਠੀਕ ਕਰੋ" ਵਿਸਥਾਰ ਨਾਲ ਹੈ। ਭਾਸ਼ਾ ਮਾਡਲ ਵੀ ਇੱਕੋ ਜਿਹੇ ਕੰਮ ਕਰਦੇ ਹਨ—ਵਿਸ਼ੇਸ਼ਤਾ ਅਤੇ ਬਣਤਰ ਅਹੰਕਾਰਸ਼ੀਲ ਹਨ।

## ਇਹ ਕਿਵੇਂ LangChain4j ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ

ਇਹ ਮੋਡੀਊਲ ਪਿਛਲੇ ਮੋਡੀਊਲਾਂ ਵਾਲੇ LangChain4j ਬੁਨਿਆਦ ਨਾਲ ਪ੍ਰੌਂਪਟ ਦੇ ਉੱਚ ਪੱਧਰੀ ਪੈਟਰਨ ਦਿਖਾਉਂਦਾ ਹੈ, ਜਿੱਥੇ ਧਿਆਨ ਪ੍ਰੌਂਪਟ ਬਣਤਰ ਤੇ ਤਰਕਸ਼ੀਲਤਾ ਨਿਯੰਤਰਣ 'ਤੇ ਹੈ।

<img src="../../../translated_images/pa/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j ਫਲੋ" width="800"/>

*ਕਿਵੇਂ LangChain4j ਤੁਹਾਡੇ ਪ੍ਰੌਂਪਟਾਂ ਨੂੰ Azure OpenAI GPT-5.2 ਨਾਲ ਜੋੜਦਾ ਹੈ*

**ਡਿਪੈਂਡੇਨਸੀਜ** - ਮੋਡੀਊਲ 02 ਹੇਠਾਂ ਦਿੱਤੀ LangChain4j ਡਿਪੈਂਡੇਨਸੀਜ਼ ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ ਜੋ `pom.xml` ਵਿੱਚ ਦਿੱਤੀਆਂ ਹਨ:
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
  
**OpenAiOfficialChatModel ਕਨਫਿਗਰੇਸ਼ਨ** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

ਚੈਟ ਮਾਡਲ ਨੂੰ ਹੱਥੋਂ Spring ਬੀਨ ਵਜੋਂ ਕਨਫਿਗਰ ਕੀਤਾ ਗਿਆ ਹੈ ਜਿਹੜਾ OpenAI Official ਕਲਾਇੰਟ ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ ਜੋ Azure OpenAI ਇੰਡੀਪੋਇੰਟਾਂ ਨੂੰ ਸਹਿਯੋਗ ਕਰਦਾ ਹੈ। ਮੋਡੀਊਲ 01 ਨਾਲ ਮੁੱਖ ਫਰਕ ਇਹ ਹੈ ਕਿ ਅਸੀਂ `chatModel.chat()` ਲਈ ਭੇਜੇ ਜਾ ਰਹੇ ਪ੍ਰੌਂਪਟਾਂ ਨੂੰ ਕਿਵੇਂ ਬਣਾਉਂਦੇ ਹਾਂ, ਮਾਡਲ ਸੈੱਟਅੱਪ ਨਹੀਂ।

**ਸਿਸਟਮ ਅਤੇ ਯੂਜ਼ਰ ਸੁਨੇਹੇ** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j ਸੁਨੇਹਾ ਕਿਸਮਾਂ ਨੂੰ ਸਪਸ਼ਟਤਾ ਲਈ ਵੱਖਰਾ ਕਰਦਾ ਹੈ। `SystemMessage` ਏਆਈ ਦੀ ਵਰਤੋਂ ਅਤੇ ਸੰਦਰਭ ਸੈੱਟ ਕਰਦਾ ਹੈ (ਜਿਵੇਂ "ਤੁਸੀਂ ਕੋਡ ਰਿਵਿਊਅਰ ਹੋ"), ਜਦੋਂਕਿ `UserMessage` ਅਸਲ ਦੀ ਬੇਨਤੀ ਰੱਖਦਾ ਹੈ। ਇਹ ਵੱਖਰੀ ਕਰਨ ਨਾਲ ਵੱਖ-ਵੱਖ ਯੂਜ਼ਰ ਬੇਨਤੀਆਂ ਵਿੱਚ ਏਆਈ ਵਿਹਾਰ ਲਗਾਤਾਰ ਰੱਖ ਸਕਦੇ ਹਾਂ।

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```
  
<img src="../../../translated_images/pa/message-types.93e0779798a17c9d.webp" alt="ਸੁਨੇਹਾ ਕਿਸਮਾਂ ਦਾ ਆਰਕੀਟੈਕਚਰ" width="800"/>

*SystemMessage ਦਿਰਘਕਾਲੀ ਸੰਦਰਭ ਦੇਂਦਾ ਹੈ ਜਦੋਂ ਕਿ UserMessages ਵਿਅਕਤੀਗਤ ਬੇਨਤੀਆਂ ਰੱਖਦੇ ਹਨ*

**Multi-Turn ਵਿੱਚ MessageWindowChatMemory** - ਬਹੁ-ਟਰਨ ਗੱਲਬਾਤ ਪੈਟਰਨ ਲਈ, ਅਸੀਂ ਮੋਡੀਊਲ 01 ਤੋਂ `MessageWindowChatMemory` ਨੂੰ ਦੁਬਾਰਾ ਵਰਤਦੇ ਹਾਂ। ਹਰ ਸੈਸ਼ਨ ਨੂੰ ਆਪਣਾ ਮੇਮੋਰੀ ਇੰਸਟੈਂਸ ਮਿਲਦਾ ਹੈ ਜੋ `Map<String, ChatMemory>` ਵਿੱਚ ਸਟੋਰ ਹੁੰਦਾ ਹੈ, ਇਸ ਨਾਲ ਇੱਕੋ ਸਮੇਂ ਹੋਰ ਕਈ ਗੱਲਬਾਤਾਂ ਬਿਨਾਂ ਸੰਦਰਭ ਮਿਲਾਉਣ ਦੇ ਚੱਲ ਸਕਦੀਆਂ ਹਨ।

**Prompt ਫਾਰਮੈਟ** - ਅਸਲੀ ਧਿਆਨ ਇੱਥੇ ਪ੍ਰੌਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ‘ਤੇ ਹੈ, ਨਵੀਂ LangChain4j APIs ‘ਤੇ ਨਹੀਂ। ਹਰ ਪੈਟਰਨ (ਘੱਟ ਬੇਸਬਰੀ, ਵੱਧ ਬੇਸਬਰੀ, ਟਾਸਕ ਅਮਲੀਜਾਮਾ ਆਦਿ) ਇੱਕੋ `chatModel.chat(prompt)` ਵਿਧੀ ਵਰਤਦਾ ਹੈ ਪਰ ਧਿਆਨ ਨਾਲ ਬਣੇ ਪ੍ਰੌਂਪਟ ਸਟਰਿੰਗਜ਼ ਨਾਲ। XML ਟੈਗ, ਹੁਕਮ ਅਤੇ ਫਾਰਮੈਟਿੰਗ ਸਾਰੇ ਪ੍ਰੌਂਪਟ ਟੈਕਸਟ ਦਾ ਹਿੱਸਾ ਹਨ, LangChain4j ਵਿਸ਼ੇਸ਼ਤਾਵਾਂ ਨਹੀਂ।

**ਤਰਕਸ਼ੀਲਤਾ ਨਿਯੰਤਰਣ** - GPT-5.2 ਦੀ ਤਰਕਸ਼ੀਲ ਕੋਸ਼ਿਸ਼ ਪ੍ਰੌਂਪਟ ਦੇ ਹੁਕਮਾਂ ਜਿਵੇਂ "ਵੱਧ ਤੋ ਵੱਧ 2 ਤਰਕ ਸਟੇਪ" ਜਾਂ "ਧੀਰੈ ਧੀਰੈ ਖੰਗਾਲੋ" ਨਾਲ ਨਿਯੰਤਰਿਤ ਹੁੰਦੀ ਹੈ। ਇਹ ਪ੍ਰੌਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਤਰੀਕੇ ਹਨ, LangChain4j ਵਿੱਚ ਨਹੀਂ। ਲਾਇਬ੍ਰੇਰੀ ਸਿਰਫ ਤੁਹਾਡੇ ਪ੍ਰੌਂਪਟ ਮਾਡਲ ਤੱਕ ਪਹੁੰਚਾਉਂਦੀ ਹੈ।

ਮੁੱਖ ਗੱਲ ਇਹ ਹੈ: LangChain4j ਇੰਫਰਾਸਟਰਕਚਰ (ਮਾਡਲ ਕਨੈਕਸ਼ਨ [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), ਯਾਦاشت, ਸੁਨੇਹਾ ਸੰਭਾਲ [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)) ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ, ਜਦੋਂ ਕਿ ਇਹ ਮੋਡੀਊਲ ਤੁਹਾਨੂੰ ਸਿਖਾਉਂਦਾ ਹੈ ਕਿ ਇੰਨ੍ਹਾ ਢਾਂਚੇ ਵਿੱਚ ਪ੍ਰਭਾਵਸ਼ਾਲੀ ਪ੍ਰੌਂਪਟ ਕਿਵੇਂ ਤਿਆਰ ਕਰਨੇ ਹਨ।

## ਮੁੱਖ ਪੈਟਰਨ

ਸਾਰੇ ਸਮੱਸਿਆਵਾਂ ਲਈ ਇਕੋ ਜਿਹੀ ਪਹੁੰਚ ਨਹੀਂ ਹੁੰਦੀ। ਕੁਝ ਸਵਾਲਾਂ ਲਈ ਤੇਜ਼ ਜਵਾਬ ਚਾਹੀਦੇ, ਦੂਜੇ ਲਈ ਡੂੰਘਾ ਸੋਚਣਾ ਲੋੜੀਂਦਾ ਹੈ। ਕੁਝ ਲਈ ਝਲਕਦਾਰ ਤਰਕ ਚਾਹੀਦਾ ਹੈ, ਕਈ ਲਈ ਸਿਰਫ ਨਤੀਜੇ। ਇਹ ਮੋਡੀਊਲ ਅੱਠ ਪ੍ਰੌਂਪਟਿੰਗ ਪੈਟਰਨਾਂ ਦੀ ਚਰਚਾ ਕਰਦਾ ਹੈ—ਹਰ ਇੱਕ ਵੱਖਰੇ ਪਰਿਸਥਿਤੀਆਂ ਲਈ ਬੇਹਤरीन ਹੈ। ਤੁਸੀਂ ਸਾਰੇ ਅਜ਼ਮਾਵੋਗੇ ਤਾਂ ਕਿ ਸਮਝ ਸਕੋ ਕਿ ਕਦੋਂ ਕਿਹੜੀ ਪਹੁੰਚ ਚੰਗੀ ਹੈ।

<img src="../../../translated_images/pa/eight-patterns.fa1ebfdf16f71e9a.webp" alt="ਅੱਠ ਪ੍ਰੌਂਪਟਿੰਗ ਪੈਟਰਨ" width="800"/>

*ਅੱਠ ਪ੍ਰੌਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਪੈਟਰਨਾਂ ਦਾ ਜਾਇਜ਼ਾ ਅਤੇ ਉਨ੍ਹਾਂ ਦੇ ਉਪਯੋਗ*

<img src="../../../translated_images/pa/reasoning-effort.db4a3ba5b8e392c1.webp" alt="ਤਰਕਸ਼ੀਲਤਾ ਮੁਕਾਬਲਾ" width="800"/>

*ਘੱਟ ਬੇਸਬਰੀ (ਤੇਜ਼, ਸਿੱਧਾ) ਬਨਾਮ ਵੱਧ ਬੇਸਬਰੀ (ਧੀਰੈ, ਖੰਗਾਲੀ)*

**ਘੱਟ ਬੇਸਬਰੀ (ਤੇਜ਼ ਅਤੇ ਕੇਂਦਰਤ)** - ਅਸਾਨ ਸਵਾਲਾਂ ਲਈ ਜਿੱਥੇ ਤੇਜ਼ ਤੇ ਸਿੱਧੇ ਜਵਾਬ ਚਾਹੀਦੇ ਹਨ। ਮਾਡਲ ਘੱਟ ਤਰਕ ਕਰਦਾ ਹੈ—ਵੱਧੋਂ ਵੱਧ 2 ਕਦਮ। ਇਸਨੂੰ ਗਣਨਾ, ਖੋਜ ਜਾਂ ਸਿੱਧੇ ਸਵਾਲਾਂ ਲਈ ਵਰਤੋ।

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **GitHub Copilot ਨਾਲ ਜਾਚ ਕਰੋ:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:  
> - "ਘੱਟ ਬੇਸਬਰੀ ਅਤੇ ਵੱਧ ਬੇਸਬਰੀ ਪ੍ਰੌਂਪਟਿੰਗ ਪੈਟਰਨਾਂ ਵਿੱਚ ਕੀ ਫਰਕ ਹੈ?"  
> - "ਪ੍ਰੌਂਪਟਾਂ ਵਿੱਚ XML ਟੈਗ AI ਦੇ ਜਵਾਬ ਦੀ ਬਣਤਰ ਕਿਵੇਂ ਸਹਾਇਕ ਹੁੰਦੀ ਹੈ?"  
> - "ਮੈਂ ਕਦੋਂ ਆਪ-ਮੁਖਾਵਲੀ ਪੈਟਰਨਾਂ ਵਰਤਾਂ ਅਤੇ ਕਦੋਂ ਸਿੱਧਾ ਹੁਕਮ ਦੇਵਾਂ?"

**ਵੱਧ ਬੇਸਬਰੀ (ਗਹਿਰਾਈ ਦੇ ਨਾਲ)** - ਜਟਿਲ ਮੁੱਦਿਆਂ ਲਈ ਜਿੱਥੇ ਵਿਆਪਕ ਵਿਸ਼ਲੇਸ਼ਣ ਚਾਹੀਦਾ ਹੈ। ਮਾਡਲ ਧੀਰੈ-ਧੀਰੈ ਖੰਗਾਲਦਾ ਹੈ ਤੇ ਵਿਸਥਾਰ ਨਾਲ ਤਰਕ ਦਿਖਾਉਂਦਾ ਹੈ। ਇਸਨੂੰ ਸਿਸਟਮ ਡਿਜ਼ਾਈਨ, ਆਰਕੀਟੈਕਚਰ ਫੈਸਲੇ ਜਾਂ ਗੰਭੀਰ ਖੋਜ ਲਈ ਵਰਤੋ।

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**ਟਾਸਕ ਅਮਲੀ ਜਾਮਾ (ਕਦਮ-ਦਰ-ਕਦਮ ਪ੍ਰਗਟਿ)** - ਬਹੁ-ਕਦਮ ਵਾਲੇ ਵਰਕਫਲੋਜ਼ ਲਈ। ਮਾਡਲ ਪਹਿਲਾਂ ਯੋਜਨਾ ਦਿੰਦਾ ਹੈ, ਹਰ ਕਦਮ ਨੂੰ ਕਹਾਣੀ ਵਾਂਗ ਦੱਸਦਾ ਹੈ, ਫਿਰ ਸਾਰਾਂਸ਼ ਦਿੰਦਾ ਹੈ। ਮਾਈਗ੍ਰੇਸ਼ਨ, ਐਮਪਲੀਮੈਂਟੇਸ਼ਨ ਜਾਂ ਕਿਸੇ ਹੋਰ ਬਹੁ-ਕਦਮੀ ਪ੍ਰਕਿਰਿਆ ਲਈ ਵਰਤੋ।

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```
  
ਚੇਨ-ਆਫ-ਥੌਟ ਪ੍ਰੌਂਪਟਿੰਗ ਖੁਲਾਸਾ ਤਰਕ ਪ੍ਰਕਿਰਿਆ ਦਿਖਾਉਂਦਾ ਹੈ, ਜਿਸ ਨਾਲ ਮੁਸ਼ਕਲ ਕਾਰਜਾਂ ਲਈ ਸੁਚਿਤਤਾ ਵੱਧਦੀ ਹੈ। ਕਦਮ-ਦਰ-ਕਦਮ ਵਿਸ਼ਲੇਸ਼ਣ ਮਨੁੱਖਾਂ ਅਤੇ AI ਦੋਹਾਂ ਲਈ ਤਰਕ ਸਮਝਣ ਵਿੱਚ ਮਦਦ ਕਰਦਾ ਹੈ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ਚੈਟ ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** ਇਸ ਪੈਟਰਨ ਬਾਰੇ ਪੁੱਛੋ:  
> - "ਲੰਬੇ ਸਮੇਂ ਚੱਲਣ ਵਾਲੇ ਕਾਰਜਾਂ ਲਈ ਟਾਸਕ ਅਮਲੀਜਾਮਾ ਪੈਟਰਨ ਨੂੰ ਕਿਵੇਂ ਅਨੁਕੂਲਿਤ ਕਰਾਂ?"  
> - "ਪ੍ਰੋਡਕਸ਼ਨ ਐਪਲੀਕੇਸ਼ਨਾਂ ਵਿੱਚ ਟੂਲ ਪ੍ਰੀਐਂਬਲ ਦੀ ਬਣਤਰ ਲੀਡ ਕਰਨ ਲਈ ਸਾਰਥਕ ਅਮਲ ਕੀ ਹਨ?"  
> - "ਇਕ UI ਵਿੱਚ ਦਰਮਿਆਨੀ ਪ੍ਰਗਟਿ ਅਪਡੇਟ ਕਿਵੇਂ ਕੈਪਚਰ ਅਤੇ ਦਰਸਾਈ ਜਾ ਸਕਦੀ ਹੈ?"

<img src="../../../translated_images/pa/task-execution-pattern.9da3967750ab5c1e.webp" alt="ਟਾਸਕ ਅਮਲੀਜਾਮਾ ਪੈਟਰਨ" width="800"/>

*ਯੋਜਨਾ → ਅਮਲੀਜਾਮਾ → ਸਾਰਾਂਸ਼ ਬਹੁ-ਕਦਮਿਕ ਕਾਰਜਾਂ ਲਈ ਵਰਕਫਲੋ*

**ਆਤਮ-ਮੁਖਾਵਲੀ ਕੋਡ** - ਪ੍ਰੋਡਕਸ਼ਨ-ਗੁਣਵੱਤਾ ਵਾਲਾ ਕੋਡ ਬਣਾਉਣ ਲਈ। ਮਾਡਲ ਕੋਡ ਤਿਆਰ ਕਰਦਾ ਹੈ, ਗੁਣਵੱਤਾ ਮਾਪਦੰਡਾਂ ਵੱਲੋਂ ਚੈੱਕ ਕਰਦਾ ਹੈ ਅਤੇ ਕਦਮ-ਦਰ-ਕਦਮ ਸੁਧਾਰ ਕਰਦਾ ਹੈ। ਨਵੇਂ ਫੀਚਰਾਂ ਜਾਂ ਸੇਵਾਵਾਂ ਬਣਾਉਂਦੇ ਸਮੇਂ ਇਸਨੂੰ ਵਰਤੋ।

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/pa/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="ਆਤਮ-ਮੁਖਾਵਲੀ ਚੱਕਰ" width="800"/>

*ਚੱਕਰਵਾਤ ਸੁਧਾਰ - ਤਿਆਰ ਕਰੋ, ਮੁਲਾਂਕਣ ਕਰੋ, ਮੁੱਦੇ ਪਛਾਣੋ, ਸੁਧਾਰ ਕਰੋ, ਦੁਹਰਾਓ*

**ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ** - ਲਗਾਤਾਰ ਮੁਲਾਂਕਣ ਲਈ। ਮਾਡਲ ਕੋਡ ਦੀ ਸਮੀਖਿਆ ਸਥਿਰ ਢਾਂਚਾ (ਸਹੀਤਾ, ਅਭਿਆਸ, ਪ੍ਰਦਰਸ਼ਨ, ਸੁਰੱਖਿਆ) ਲੈ ਕੇ ਕਰਦਾ ਹੈ। ਇਸਨੂੰ ਕੋਡ ਸਮੀਖਿਅਾ ਜਾਂ ਗੁਣਵੱਤਾ ਮੁਲਾਂਕਣਾਂ ਲਈ ਵਰਤੋ।

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```
  
> **🤖 [GitHub Copilot](https://github.com/features/copilot) ਚੈਟ ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ ਬਾਰੇ ਪੁੱਛੋ:  
> - "ਵੱਖ-ਵੱਖ ਕੋਡ ਸਮੀਖਿਆਂ ਲਈ ਵਿਸ਼ਲੇਸ਼ਣ ਢਾਂਚਾ ਕਿਵੇਂ ਅਨੁਕੂਲਿਤ ਕਰਾਂ?"  
> - "ਸੰਰਚਿਤ ਅਉਟਪੁਟ ਨੂੰ ਪ੍ਰੋਗ੍ਰਾਮੈਟਿਕ ਤੌਰ 'ਤੇ ਕਿਵੇਂ ਪਾਰਸ ਅਤੇ ਕਾਰਵਾਈ ਕਰ ਸਕਦੇ ਹਾਂ?"  
> - "ਕਿਵੇਂ ਯਕੀਨੀ ਬਣਾਵਾਂ ਕਿ ਵੱਖ-ਵੱਖ ਸਮੀਖਿਆ ਸੈਸ਼ਨਾਂ ਵਿੱਚ ਗੰਭੀਰਤਾ ਪੱਧਰ ਲਗਾਤਾਰ ਹਨ?"

<img src="../../../translated_images/pa/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ ਪੈਟਰਨ" width="800"/>

*ਚਾਰ-ਸ਼੍ਰੇਣੀ ਢਾਂਚਾ ਲਗਾਤਾਰ ਕੋਡ ਸਮੀਖਿਆ ਲਈ ਗੰਭੀਰਤਾ ਪੱਧਰਾਂ ਨਾਲ*

**ਮਲਟੀ-ਟਰਨ ਚੈਟ** - ਸੰਵਾਦਾਂ ਲਈ ਜਿਸਨੂੰ ਸੰਦਰਭ ਦੀ ਲੋੜ ਹੁੰਦੀ ਹੈ। ਮਾਡਲ ਪਿਛਲੇ ਸੁਨੇਹਿਆਂ ਨੂੰ ਯਾਦ ਰੱਖਦਾ ਹੈ ਅਤੇ ਉਸ ਤੇ ਅਧਾਰਿਤ ਹੁੰਦਾ ਹੈ। ਇਹ ਇੰਟਰਐਕਟਿਵ ਮਦਦ ਸੈਸ਼ਨਾਂ ਜਾਂ ਜਟਿਲ ਸਵਾਲ-ਜਵਾਬ ਲਈ ਉਪਯੋਗ ਹੈ।

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/pa/context-memory.dff30ad9fa78832a.webp" alt="ਸੰਦਰਭ ਯਾਦاشت" width="800"/>

*ਬਹੁ-ਕਦਮਾਂ ਵਿੱਚ ਗੱਲਬਾਤ ਦਾ ਸੰਦਰਭ ਇਕੱਤਰ ਹੁੰਦਾ ਹੈ ਜਦ ਤੱਕ ਟੋਕਨ ਸੀਮਾ ਤੱਕ ਨਹੀਂ ਪਹੁੰਚਦਾ*

**ਕਦਮ-ਦਰ-ਕਦਮ ਤਰਕ** - ਸਮੱਸਿਆਵਾਂ ਲਈ ਜਿੱਥੇ ਦਿਖਾਈ ਜਾਵੇ ਤਰਕ ਦੀ ਲੋੜ ਹੈ। ਮਾਡਲ ਹਰ ਕਦਮ ਲਈ ਸਪਸ਼ਟ ਤਰਕ ਵਜੋਂ ਦਿਖਾਉਂਦਾ ਹੈ। ਗਣਿਤ ਦੇ ਪ੍ਰਸ਼ਨਾਂ, ਤਰਕ ਦੇ ਪਹੇਲੀਆਂ ਜਾਂ ਸੋਚਣ ਦੀ ਪ੍ਰਕਿਰਿਆ ਸਮਝਣ ਲਈ ਵਰਤੋ।

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/pa/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="ਕਦਮ-ਦਰ-ਕਦਮ ਪੈਟਰਨ" width="800"/>

*ਸਮੱਸਿਆਵਾਂ ਨੂੰ ਸਪਸ਼ਟ ਲੋਜਿਕਲ ਕਦਮਾਂ ਵਿੱਚ ਵੰਡਣਾ*

**ਸੀਮਿਤ ਅਉਟਪੁਟ** - ਐਸੇ ਜਵਾਬਾਂ ਲਈ ਜਿਨ੍ਹਾਂ ਨੂੰ ਖਾਸ ਫਾਰਮੈਟ ਦੀ ਲੋੜ ਹੋਵੇ। ਮਾਡਲ ਕੜੀ ਤਰ੍ਹਾਂ ਫਾਰਮੈਟ ਅਤੇ ਲੰਬਾਈ ਨਿਯਮਾਂ ਦੀ ਪਾਲਣਾ ਕਰਦਾ ਹੈ। ਇਹ ਸੰਖੇਪਾਂ ਜਾਂ ਜਦੋਂ ਤੁਹਾਨੂੰ ਬਿਲਕੁਲ ਸਹੀ ਅਉਟਪੁਟ ਢਾਂਚਾ ਚਾਹੀਦਾ ਹੋਵੇ, ਵਰਤੋ।

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/pa/constrained-output-pattern.0ce39a682a6795c2.webp" alt="ਸੀਮਿਤ ਅਉਟਪੁਟ ਪੈਟਰਨ" width="800"/>

*ਖਾਸ ਫਾਰਮੈਟ, ਲੰਬਾਈ ਅਤੇ ਬਣਤਰ ਦੀਆਂ ਲੋੜਾਂ ਨੂੰ ਲਾਗੂ ਕਰਨਾ*

## ਮੌਜੂਦਾ Azure ਸ੍ਰੋਤਾਂ ਦੀ ਵਰਤੋਂ

**ਡਿਪਲੋਇ ਦੀ ਪੁਸ਼ਟੀ ਕਰੋ:**

ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ `.env` ਫਾਈਲ ਨੂੰ ਯਕੀਨੀ ਬਣਾਓ ਜਿਸ ਵਿੱਚ Azure ਕ੍ਰੈਡੈਂਸ਼ੀਅਲਜ਼ ਹਨ (ਮੋਡੀਊਲ 01 ਦੌਰਾਨ ਬਣਾਈ ਗਈ):  
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ਦਿਖਾਉਣਾ ਚਾਹੀਦਾ ਹੈ
```
  
**ਐਪਲੀਕੇਸ਼ਨ ਸਟਾਰਟ ਕਰੋ:**

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਪਹਿਲਾਂ ਹੀ Module 01 ਤੋਂ `./start-all.sh` ਨਾਲ ਸਾਰੇ ਐਪਲੀਕੇਸ਼ਨ ਸਟਾਰਟ ਕਰ ਚੁੱਕੇ ਹੋ, ਤਾਂ ਇਹ ਮੋਡੀਊਲ ਪਹਿਲਾਂ ਹੀ ਪੋਰਟ 8083 ‘ਤੇ ਚੱਲ ਰਿਹਾ ਹੈ। ਤੁਸੀਂ ਹੇਠਾਂ ਦਿੱਤੇ ਸਟਾਰਟ ਹੁਕਮ ਛੱਡ ਕੇ ਸਿੱਧਾ http://localhost:8083 ‘ਤੇ ਜਾ ਸਕਦੇ ਹੋ।

**ਵਿਕਲਪ 1: VS Code ਉਪਭੋਗਤਾਵਾਂ ਲਈ Spring Boot ਡੈਸ਼ਬੋਰਡ ਦੀ ਵਰਤੋਂ (ਸਿਫਾਰਸ਼ੀਦਾ)**

ਡੈਵ ਕੰਟੇਨਰ ਵਿੱਚ Spring Boot ਡੈਸ਼ਬੋਰਡ ਇਕਸਪੈਂਸ਼ਨ ਸ਼ਾਮਿਲ ਹੈ, ਜੋ ਸਾਰੇ Spring Boot ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਵੇਖਣ ਲਈ ਵਿਜ਼ੂਅਲ ਇੰਟਰਫੇਸ ਦਿੰਦਾ ਹੈ। ਇਹ ਤੁਹਾਨੂੰ VS Code ਦੀ ਸਾਈਡਬਾਰ ਵਿੱਚ Spring Boot ਆਈਕਨ 'ਤੇ ਮਿਲੇਗਾ।

Spring Boot ਡੈਸ਼ਬੋਰਡ ਤੋਂ, ਤੁਸੀਂ:  
- ਵਰਕਸਪੇਸ ਵਿੱਚ ਸਾਰੇ ਉਪਲੱਬਧ Spring Boot ਐਪ ਨੂੰ ਵੇਖ ਸਕਦੇ ਹੋ  
- ਸਿਰਫ ਇਕ ਕਲਿਕ ਨਾਲ ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਚਾਲੂ ਜਾਂ ਬੰਦ ਕਰ ਸਕਦੇ ਹੋ  
- ਐਪ ਲਈ ਲਾਈਵ ਲੌਗਸ ਵੇਖ ਸਕਦੇ ਹੋ  
- ਐਪ ਸਥਿਤੀ ਤੇ ਨਜ਼ਰ ਰੱਖ ਸਕਦੇ ਹੋ

ਸਿੱਧਾ "prompt-engineering" ਦੇ ਨਾਲ ਖੇਡ ਬਟਨ ਤੇ ਕਲਿਕ ਕਰਕੇ ਇਸ ਮੋਡੀਊਲ ਨੂੰ ਸ਼ੁਰੂ ਕਰੋ ਜਾਂ ਸਾਰੇ ਮੋਡੀਊਲ ਇਕੱਠੇ ਸ਼ੁਰੂ ਕਰੋ।

<img src="../../../translated_images/pa/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot ਡੈਸ਼ਬੋਰਡ" width="400"/>

**ਵਿਕਲਪ 2: ਸ਼ੈੱਲ ਸਕ੍ਰਿਪਟ ਦੀ ਵਰਤੋਂ**

ਸਾਰੇ ਵੈੱਬ ਐਪਲੀਕੇਸ਼ਨ (Module 01-04) ਸ਼ੁਰੂ ਕਰੋ:

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
  
ਜਾਂ ਕੇਵਲ ਇਹ ਮੋਡੀਊਲ ਸਟਾਰਟ ਕਰੋ:

**Bash:**  
```bash
cd 02-prompt-engineering
./start.sh
```
  
**PowerShell:**  
```powershell
cd 02-prompt-engineering
.\start.ps1
```
  
ਦੋਹਾਂ ਸਕ੍ਰਿਪਟਾਂ ਆਪਣੇ ਆਪ ਮੂਲ `.env` ਫਾਈਲ ਤੋਂ ਵਾਤਾਵਰਣ ਪਰਿਵਰਤਨਾਂ ਨੂੰ ਲੋਡ ਕਰਦੀਆਂ ਹਨ ਅਤੇ ਜੇ ਜਾਰ ਮੌਜੂਦ ਨਹੀਂ ਤਾਂ ਇਨ੍ਹਾਂ ਨੂੰ ਬਣਾਉਂਦੀਆਂ ਹਨ।

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਸਾਰੇ ਮੋਡੀਊਲ ਨੂੰ ਮੈਨੂਅਲੀ ਤੌਰ ‘ਤੇ ਬਿਲਡ ਕਰਨਾ ਚਾਹੁੰਦੇ ਹੋ ਪੂਰਵ ਸਟਾਰਟ ਤੋਂ ਪਹਿਲਾਂ:  
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
  
ਆਪਣਾ ਬ੍ਰਾਉਜ਼ਰ ਖੋਲ੍ਹੋ ਅਤੇ http://localhost:8083 'ਤੇ ਜਾਓ।

**ਰੋਕਣ ਲਈ:**  

**Bash:**  
```bash
./stop.sh  # ਇਹ ਮਾਡਿਊਲ ਸਿਰਫ਼
# ਜਾਂ
cd .. && ./stop-all.sh  # ਸਾਰੇ ਮਾਡਿਊਲਸ
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # ਸਿਰਫ ਇਹ ਮੋਡੀਊਲ
# ਜਾਂ
cd ..; .\stop-all.ps1  # ਸਾਰੇ ਮੋਡੀਊਲ
```
  

## ਐਪਲੀਕੇਸ਼ਨ ਸਕ੍ਰੀਨਸ਼ਾਟ

<img src="../../../translated_images/pa/dashboard-home.5444dbda4bc1f79d.webp" alt="ਡੈਸ਼ਬੋਰਡ ਘਰ" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*ਮੁੱਖ ਡੈਸ਼ਬੋਰਡ ਜੋ ਸਾਰੇ 8 ਪ੍ਰੌਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਪੈਟਰਨ ਅਤੇ ਉਨ੍ਹਾਂ ਦੀਆਂ ਵਿਸ਼ੇਸ਼ਤਾਵਾਂ ਤੇ ਉਪਯੋਗ ਦਿਖਾਉਂਦਾ ਹੈ*

## ਪੈਟਰਨ ਦੀ ਜਾਂਚ

ਵੈੱਬ ਇੰਟਰਫੇਸ ਤੁਹਾਨੂੰ ਵੱਖਰੇ ਪ੍ਰੌਂਪਟਿੰਗ ਰਣਨੀਤੀਆਂ ਨਾਲ ਅਜ਼ਮਾਉਣ ਦੀ ਆਜ਼ਾਦੀ ਦਿੰਦਾ ਹੈ। ਹਰ ਪੈਟਰਨ ਵੱਖਰੇ ਸਮੱਸਿਆਵਾਂ ਦਾ ਹੱਲ ਹੈ—ਇਹਨਾਂ ਨੂੰ ਟੈਸਟ ਕਰੋ ਤਾਂ ਕਿ ਦੇਖ ਸਕੋ ਕਦੋਂ ਕਿਹੜਾ ਪਹੁੰਚ ਬਿਹਤਰ ਹੈ।

### ਘੱਟ ਬੇਸਬਰੀ ਬਨਾਮ ਵੱਧ ਬੇਸਬਰੀ

ਘੱਟ ਬੇਸਬਰੀ ਵਰਤ ਕੇ ਇੱਕ ਅਸਾਨ ਸਵਾਲ ਪੁੱਛੋ ਜਿਵੇਂ "200 ਦਾ 15% ਕੀ ਹੈ?"। ਤੁਹਾਨੂੰ ਤੁਰੰਤ ਅਤੇ ਸਿੱਧਾ ਜਵਾਬ ਮਿਲੇਗਾ। ਹੁਣ ਕੋਈ ਗੰਭੀਰ ਸਵਾਲ ਪੁੱਛੋ ਜਿਵੇਂ "ਉੱਚ ਟ੍ਰੈਫਿਕ API ਲਈ ਕੈਸ਼ਿੰਗ ਰਣਨੀਤੀ ਬਣਾਓ" ਵੱਧ ਬੇਸਬਰੀ ਨਾਲ। ਦੇਖੋ ਕਿ ਮਾਡਲ ਕਿਵੇਂ ਧੀਮੈ ਹੋ ਜਾਂਦਾ ਹੈ ਅਤੇ ਵਿਸਥਾਰ ਨਾਲ ਤਰਕ ਦਿੰਦਾ ਹੈ। ਉਹੀ ਮਾਡਲ, ਉਹੀ ਪ੍ਰੌਂਪਟ ਬਣਤਰ - ਪਰ ਪ੍ਰੌਂਪਟ ਮਾਡਲ ਨੂੰ ਕਿੰਨੀ ਸੋਚ ਕਰਨੀ ਹੈ ਦੱਸਦਾ ਹੈ।
<img src="../../../translated_images/pa/low-eagerness-demo.898894591fb23aa0.webp" alt="ਘੱਟ ਉਤਸਾਹ ਡੈਮੋ" width="800"/>

*ਘੱਟ ਤੋਂ ਘੱਟ ਤਰਕੀਬ ਨਾਲ ਤੇਜ਼ ਗਣਨਾ*

<img src="../../../translated_images/pa/high-eagerness-demo.4ac93e7786c5a376.webp" alt="ਜ਼ਿਆਦਾ ਉਤਸਾਹ ਡੈਮੋ" width="800"/>

*ਵਿਆਪਕ ਕੈਸ਼ਿੰਗ ਰਣਨੀਤੀ (2.8MB)*

### ਟਾਸਕ ਕਾਰਗੁਜ਼ਾਰੀ (ਟੂਲ ਪ੍ਰੀਐੰਬਲ)

ਬਹੁ-ਕਦਮੀ ਵਰਕਫ਼ਲੋਜ਼ ਨੂੰ ਪਹਿਲਾਂ ਤੋਂ ਯੋਜਨਾ ਅਤੇ ਪ੍ਰਗਟ ਪ੍ਰਗਟਾਵਾ ਲਾਭਦਾਇਕ ਹੁੰਦਾ ਹੈ। ਮਾਡਲ ਦੱਸਦਾ ਹੈ ਕਿ ਇਹ ਕੀ ਕਰੇਗਾ, ਹਰ ਕਦਮ ਦਾ ਵੇਰਵਾ ਦਿੰਦਾ ਹੈ, ਫਿਰ ਨਤੀਜੇ ਸਮੇਟਦਾ ਹੈ।

<img src="../../../translated_images/pa/tool-preambles-demo.3ca4881e417f2e28.webp" alt="ਟਾਸਕ ਕਾਰਗੁਜ਼ਾਰੀ ਡੈਮੋ" width="800"/>

*ਕਦਮ-ਦਰ-ਕਦਮ ਕਹਾਣੀ ਨਾਲ REST ਐਂਡਪੌਇੰਟ ਬਣਾਉਣਾ (3.9MB)*

### ਖੁਦ-ਪਰਖ ਰਹਿਤ ਕੋਡ

"ਇਮੇਲ ਪ੍ਰਮਾਣੀਕਰਨ ਸੇਵਾ ਬਣਾਉ" ਦਾ ਉਦਾਹਰਨ ਦੇਖੋ। ਸਿਰਫ਼ ਕੋਡ ਬਣਾਉਣ ਅਤੇ ਰੁਕਣ ਦੀ ਥਾਂ, ਮਾਡਲ ਕੋਡ ਬਣਾ ਕੇ ਗੁਣਵੱਤਾ ਮਾਪਦੰਡਾਂ ਦੇ ਖਿਲਾਫ਼ ਮੁਲਾਂਕਣ ਕਰਦਾ ਹੈ, ਕਮਜ਼ੋਰੀਆਂ ਪਛਾਣਦਾ ਹੈ ਅਤੇ ਸੁਧਾਰ ਕਰਦਾ ਹੈ। ਤੁਸੀਂ ਦੇਖੋਗੇ ਇਹ ਲਗਾਤਾਰ ਸੁਧਾਰ ਕਰਦਾ ਹੈ ਜਦ ਤਕ ਕੋਡ ਉਤਪਾਦਨ ਮਿਆਰਾਂ 'ਤੇ ਖਰਾ ਨਹੀਂ ਉਤਰਦਾ।

<img src="../../../translated_images/pa/self-reflecting-code-demo.851ee05c988e743f.webp" alt="ਖੁਦ-ਪਰਖ ਰਹਿਤ ਕੋਡ ਡੈਮੋ" width="800"/>

*ਸਮਪੂਰਨ ਇਮੇਲ ਪ੍ਰਮਾਣੀਕਰਨ ਸੇਵਾ (5.2MB)*

### ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ

ਕੋਡ ਸਮੀਖਿਆ ਲਈ ਇੱਕ সর্ਗ੍ਰੌਥਮੂਲਕ ਮੁਲਾਂਕਣ ਢਾਂਚਾ ਲਾਜ਼ਮੀ ਹੁੰਦਾ ਹੈ। ਮਾਡਲ ਨਿਰੀਖਣ ਕਰਦਾ ਹੈ ਕੋਡ ਨੂੰ ਨਿਰਧਾਰਿਤ ਵਰਗ (ਸਹੀਤਾ, ਅਭਿਆਸ, ਕਾਰਕਿਰਦਗੀ, ਸੁਰੱਖਿਆ) ਅਤੇ ਗੰਭੀਰਤਾ ਦੇ ਪੱਧਰ ਨਾਲ।

<img src="../../../translated_images/pa/structured-analysis-demo.9ef892194cd23bc8.webp" alt="ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ ਡੈਮੋ" width="800"/>

*ਫਰੇਮਵਰਕ ਆਧارت ਕੋਡ ਸਮੀਖਿਆ*

### ਬਹੁ-ਪੜਾਅ ਵਾਲੀ ਚੈਟ

ਪਛੱਲੇ ਸਵਾਲ "Spring Boot ਕੀ ਹੈ?" ਪੁੱਛੋ ਅਤੇ ਫਿਰ ਤੁਰੰਤ "ਮੈਨੂੰ ਉਦਾਹਰਨ ਦਿਖਾਓ" ਵਾਲਾ ਸਵਾਲ ਦਿਓ। ਮਾਡਲ ਤੁਹਾਡਾ ਪਹਿਲਾ ਸਵਾਲ ਯਾਦ ਰੱਖਦਾ ਹੈ ਅਤੇ ਤੁਹਾਨੂੰ ਖ਼ਾਸ Spring Boot ਦੀ ਉਦਾਹਰਨ ਦਿੰਦਾ ਹੈ। ਯਾਦ ਸ਼ਕਤੀ ਬਿਨਾਂ, ਦੂਜਾ ਸਵਾਲ ਬਹੁਤ ਅਸਪਸ਼ਟ ਹੁੰਦਾ।

<img src="../../../translated_images/pa/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="ਬਹੁ-ਪੜਾਅ ਵਾਲੀ ਚੈਟ ਡੈਮੋ" width="800"/>

*ਸਵਾਲਾਂ ਵਿਚ ਸੰਦਰਭ ਦਾ ਸੰਰੱਖਣ*

### ਕਦਮ ਦਰ ਕਦਮ ਤਰਕੀਬ

ਕਿਸੇ ਗਣਿਤ ਪ੍ਰਸ਼ਨ ਨੂੰ ਚੁੱਕੋ ਅਤੇ ਦੋਹਾਂ Step-by-Step Reasoning ਅਤੇ ਘੱਟ ਉਤਸਾਹ ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ। ਘੱਟ ਉਤਸਾਹ ਸਿਰਫ਼ ਜਵਾਬ ਦਿੰਦਾ ਹੈ - ਤੇਜ਼ ਪਰ ਅਸਪਸ਼ਟ। ਕਦਮ ਦਰ ਕਦਮ ਹਰ ਗਣਨਾ ਅਤੇ ਫੈਸਲੇ ਨੂੰ ਦਿਖਾਉਂਦਾ ਹੈ।

<img src="../../../translated_images/pa/step-by-step-reasoning-demo.12139513356faecd.webp" alt="ਕਦਮ ਦਰ ਕਦਮ ਤਰਕੀਬ ਡੈਮੋ" width="800"/>

*ਸਪਸ਼ਟ ਕਦਮਾਂ ਨਾਲ ਗਣਿਤ ਪ੍ਰਸ਼ਨ*

### ਸੀਮਤ ਨਤੀਜਾ

ਜਦੋਂ ਤੁਹਾਨੂੰ ਨਿਰਧਾਰਿਤ ਫਾਰਮੈਟ ਜਾਂ ਸ਼ਬਦ ਗਿਣਤੀ ਦੀ ਲੋੜ ਹੁੰਦੀ ਹੈ, ਇਹ ਪੈਟਰਨ ਸਖ਼ਤ ਤੌਰ 'ਤੇ ਪਾਲਣਾ ਕਰਵਾਉਂਦਾ ਹੈ। ਸਹੀ 100 ਸ਼ਬਦਾਂ ਦੇ ਬੁਲੇਟ ਪੌਇਂਟ ਫਾਰਮੈਟ ਵਿੱਚ ਸਾਰ ਬਣਾਉਣ ਦੀ ਕੋਸ਼ਿਸ਼ ਕਰੋ।

<img src="../../../translated_images/pa/constrained-output-demo.567cc45b75da1633.webp" alt="ਸੀਮਤ ਨਤੀਜਾ ਡੈਮੋ" width="800"/>

*ਮਸ਼ੀਨ ਲਰਨਿੰਗ ਸਾਰ ਭਾਸ਼ਾ ਨਿਯੰਤਰਣ ਨਾਲ*

## ਤੁਸੀਂ ਦਰਅਸਲ ਕੀ ਸਿੱਖ ਰਹੇ ਹੋ

**ਤਰਕੀਬ ਦੀ ਕੋਸ਼ਿਸ਼ ਸਭ ਕੁਝ ਬਦਲਦੀ ਹੈ**

GPT-5.2 ਤੁਹਾਡੇ ਪ੍ਰਾਰੰਭਿਕ ਹੁਕਮਾਂ ਰਾਹੀਂ ਗਣਨਾਤਮਕ ਕੋਸ਼ਿਸ਼ 'ਤੇ ਨਿਯੰਤਰਣ ਦਿੰਦਾ ਹੈ। ਘੱਟ ਕੋਸ਼ਿਸ਼ ਮਤਲਬ ਤੇਜ਼ ਜਵਾਬ ਬਿਨਾਂ ਵਿਆਪਕ ਖੋਜ। ਜ਼ਿਆਦਾ ਕੋਸ਼ਿਸ਼ ਮਤਲਬ ਮਾਡਲ ਵਧੇਰੇ ਸਮੇਂ ਲਈ ਡੂੰਘੀ ਸੋਚ ਕਰਦਾ ਹੈ। ਤੁਸੀਂ ਕੋਸ਼ਿਸ਼ ਨੂੰ ਟਾਸਕ ਦੀ ਜਟਿਲਤਾ ਨਾਲ ਮਿਲਾਉਣਾ ਸਿੱਖ ਰਹੇ ਹੋ - ਸਧਾਰਨ ਸਵਾਲਾਂ 'ਤੇ ਸਮਾਂ ਬਰਬਾਦ ਨਾ ਕਰੋ ਪਰ ਜਟਿਲ ਫੈਸਲਿਆਂ ਨੂੰ ਵੀ ਜਲਦੀ ਨਾ ਕਰੋ।

**ਸੰਰਚਨਾ ਸੁਭਾਅ ਨਿਰਦੇਸ਼ ਕਰਦੀ ਹੈ**

ਕੀ ਤੁਸੀਂ ਪ੍ਰਾਰੰਭਿਕ ਹੁਕਮਾਂ ਵਿੱਚ XML ਟੈਗਜ਼ ਨੂੰ ਦੇਖਿਆ? ਇਹ ਸਿਰਫ਼ ਸ਼ਿੰਗਾਰੀ ਨਹੀਂ ਹਨ। ਮਾਡਲ ਸੰਰਚਿਤ ਨਿਰਦੇਸ਼ਾਂ ਨੂੰ ਮੁਫ਼ਤ ਲਿਖਤ ਨਾਲੋਂ ਜ਼ਿਆਦਾ ਭਰੋਸੇਯੋਗ ਤਰੀਕੇ ਨਾਲ ਦੇਖਦਾ ਹੈ। ਜਦੋਂ ਤੁਹਾਨੂੰ ਬਹੁ-ਕਦਮੀ ਪ੍ਰਕਿਰਿਆਵਾਂ ਜਾਂ ਜਟਿਲ ਤਰਕਾਂ ਦੀ ਲੋੜ ਹੁੰਦੀ ਹੈ, ਤਾਂ ਸੰਰਚਨਾ ਮਾਡਲ ਨੂੰ ਦੱਸਦੀ ਹੈ ਕਿ ਇਹ ਕਿੱਥੇ ਹੈ ਅਤੇ ਅਗਲਾ ਕਦਮ ਕੀ ਹੈ।

<img src="../../../translated_images/pa/prompt-structure.a77763d63f4e2f89.webp" alt="ਪ੍ਰਾਰੰਭਿਕ ਹੁਕਮ ਦੀ ਸੰਰਚਨਾ" width="800"/>

*ਸੁਚੱਜੇ ਵੰਡੇ ਹੋਏ ਖੰਡਾਂ ਅਤੇ XML-ਸਟਾਈਲ ਆਯੋਜਨ ਨਾਲ ਤਿਆਰ ਕੀਤਾ ਪ੍ਰਾਰੰਭਿਕ ਹੁਕਮ*

**ਗੁਣਵੱਤਾ ਖੁਦ-ਮੁਲਾਂਕਣ ਰਾਹੀਂ ਪ੍ਰਾਪਤ ਹੁੰਦੀ ਹੈ**

ਖੁਦ-ਪਰਖ ਰਹਿਤ ਪੈਟਰਨ ਗੁਣਵੱਤਾ ਮਾਪਦੰਡਾਂ ਨੂੰ ਖੁੱਲ੍ਹਾ ਕਰਕੇ ਕੰਮ ਕਰਦੇ ਹਨ। ਮਾਡਲ ਤੋਂ ਉਮੀਦ ਨਾ ਕਰੋ ਸਿਰਫ਼ "ਸਹੀ" ਕਰੇ, ਤੁਸੀਂ ਇਸਨੂੰ ਸਪਸਟ ਦੱਸੋ ਕਿ "ਸਹੀ" ਦਾ ਕੀ ਮਤਲਬ ਹੈ: ਸਹੀ ਤਰਕ, ਗਲਤੀ ਸੰਭਾਲਣਾ, ਕਾਰਗਿਰਦਗੀ, ਸੁਰੱਖਿਆ। ਫਿਰ ਮਾਡਲ ਆਪਣੀ ਉਤਪਾਦਤਾ ਦਾ ਮੁਲਾਂਕਣ ਕਰ ਕੇ ਸੁਧਾਰ ਕਰ ਸਕਦਾ ਹੈ। ਇਸ ਨਾਲ ਕੋਡ ਬਣਾਉਣ ਦੀ ਪ੍ਰਕਿਰਿਆ ਲਾਟਰੀ ਤੋਂ ਇੱਕ ਸਿਸਟਮ ਬਣ ਜਾਂਦੀ ਹੈ।

**ਸੰਦਰਭ ਸੀਮਤ ਹੁੰਦਾ ਹੈ**

ਬਹੁ-ਪੜਾਅ ਵਾਲੇ ਗੱਲਬਾਤ ਦੇਣ ਨਾਲ ਸੰਦੇਸ਼ ਇਤਿਹਾਸ ਹਰੇਕ ਬੇਨਤੀ ਨਾਲ ਸ਼ਾਮਲ ਹੁੰਦਾ ਹੈ। ਪਰ ਇੱਕ ਹੱਦ ਹੁੰਦੀ ਹੈ - ਹਰ ਮਾਡਲ ਦੀ ਇਕ ਵਧ ਤੋਂ ਵਧ ਟੋਕਨ ਗਿਣਤੀ ਹੁੰਦੀ ਹੈ। ਗੱਲਬਾਤ ਵਧਣ ਨਾਲ, ਤੁਹਾਨੂੰ ਉਹ ਚਾਲਾਂ ਲੁਭਾਵਣੀਆਂ ਪੈਣਗੀਆਂ ਜੋ ਜਰੂਰੀ ਸੰਦਰਭ ਨੂੰ ਬਿਨਾਂ ਹੱਦ ਨਾਲ ਟੱਕਰਾ ਖਾਏ ਪਕੜ ਕੇ ਰੱਖਣ। ਇਹ ਮੋਡਿਊਲ ਦਿਖਾਉਂਦਾ ਹੈ ਕਿ ਯਾਦ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀ ਹੈ; ਬਾਅਦ ਵਿੱਚ ਤੁਸੀਂ ਸਿੱਖੋਗੇ ਕਿ ਕਦੋਂ ਸਾਰ ਕਰਨਾ ਹੈ, ਕਦੋਂ ਭੁੱਲ ਜਾਣਾ ਹੈ, ਅਤੇ ਕਦੋਂ ਪ੍ਰਾਪਤ ਕਰਨਾ ਹੈ।

## ਅਗਲੇ ਕਦਮ

**ਅਗਲਾ ਮੋਡਿਊਲ:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**ਨੈਵੀਗੇਸ਼ਨ:** [← ਪਹਿਲਾਂ: ਮੋਡਿਊਲ 01 - ਜਾਣੂ ਕਰਵਾਉਣਾ](../01-introduction/README.md) | [ਮੁੱਖ ਸਫ਼ੇ ਤੇ ਵਾਪਸ](../README.md) | [ਅਗਲਾ: ਮੋਡਿਊਲ 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਵੀਕਾਰੋਪਤਰ**:
ਇਸ ਦਸਤਾਵੇਜ਼ ਦਾ ਅਨੁਵਾਦ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਗੀਤਾ ਲਈ ਯਤਨਸ਼ੀਲ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਇਹ ਜਾਣਨ ਲਾਇਕ ਹੈ ਕਿ ਸਵੈਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸਤਾ ਨਿਤੀਆਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਤ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਮਹੱਤਵਪੂਰਣ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫ਼ਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਤੋਂ ਉਪਜੀ ਕੋਈ ਵੀ ਗਲਤਫਹਿਮੀਆਂ ਜਾਂ ਗਲਤ ਸਮਝਾਂ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->