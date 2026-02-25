# Module 02: GPT-5.2 ਨਾਲ ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ

## ਸੂਚੀਬੱਧ ਸਮੱਗਰੀ

- [ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ](../../../02-prompt-engineering)
- [ਪੂਰਵ ਸ਼ਰਤਾਂ](../../../02-prompt-engineering)
- [ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਦੀ ਸਮਝ](../../../02-prompt-engineering)
- [ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਦੇ ਮੂਲ ਤੱਤ](../../../02-prompt-engineering)
  - [ਜ਼ੀਰੋ-ਸ਼ਾਟ ਪ੍ਰਾਂਪਟਿੰਗ](../../../02-prompt-engineering)
  - [ਥੋੜ੍ਹੇ-ਸ਼ਾਟ ਪ੍ਰਾਂਪਟਿੰਗ](../../../02-prompt-engineering)
  - [ਚੇਨ ਆਫ ਥੌਟ](../../../02-prompt-engineering)
  - [ਰੋਲ-ਅਧਾਰਿਤ ਪ੍ਰਾਂਪਟਿੰਗ](../../../02-prompt-engineering)
  - [ਪ੍ਰਾਂਪਟ ਟੈਮਪਲੇਟ](../../../02-prompt-engineering)
- [ਉਨ੍ਹੇਰੇ ਪੈਟਰਨ](../../../02-prompt-engineering)
- [ਮੌਜੂਦਾ Azure ਸਰੋਤਾਂ ਦਾ ਉਪਯੋਗ](../../../02-prompt-engineering)
- [ਐਪਲੀਕੇਸ਼ਨ ਸਕ੍ਰੀਨਸ਼ਾਟ](../../../02-prompt-engineering)
- [ਪੈਟਰਨ ਦੀ ਖੋਜ](../../../02-prompt-engineering)
  - [ਘੱਟ ਵਿਰੁਧ ਵੱਧ ਉਤਸ਼ਾਹ](../../../02-prompt-engineering)
  - [ਟਾਸਕ ਐਕਜੀਕਿਊਸ਼ਨ (ਟੂਲ ਪ੍ਰੀਐਂਬਲ)](../../../02-prompt-engineering)
  - [ਸਵੈ-ਪ੍ਰਤੀਬਿੰਬਕ ਕੋਡ](../../../02-prompt-engineering)
  - [ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ](../../../02-prompt-engineering)
  - [ਕਈ-ਚਰਣ ਚੈਟ](../../../02-prompt-engineering)
  - [ਕਦਮ-ਦਰ-ਕਦਮ ਵਿਚਾਰ](../../../02-prompt-engineering)
  - [ਸੀਮਿਤ ਨਿਕਾਸ](../../../02-prompt-engineering)
- [ਤੁਸੀਂ ਅਸਲ ਵਿੱਚ ਕੀ ਸਿੱਖ ਰਹੇ ਹੋ](../../../02-prompt-engineering)
- [ਅਗਲੇ ਕਦਮ](../../../02-prompt-engineering)

## ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ

<img src="../../../translated_images/pa/what-youll-learn.c68269ac048503b2.webp" alt="ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ" width="800"/>

ਪਿਛਲੇ ਮਾਡਿਊਲ ਵਿੱਚ, ਤੁਸੀਂ ਦੇਖਿਆ ਕਿ ਕਿਵੇਂ ਮੈਮੋਰੀ ਕਨਵਰਸੇਸ਼ਨਲ AI ਨੂੰ ਯੋਗ ਬਣਾਉਂਦੀ ਹੈ ਅਤੇ ਮੂਲ ਤੌਰ ਤੇ GitHub ਮਾਡਲਾਂ ਨਾਲ ਮੁਲਾਕਾਤ ਕੀਤੀ। ਹੁਣ ਅਸੀਂ ਇਸ ਗੱਲ ‘ਤੇ ਧਿਆਨ ਦੇਵਾਂਗੇ ਕਿ ਤੁਸੀਂ سوال ਕਿਵੇਂ ਪੁੱਛਦੇ ਹੋ — ਆਪਣੀ ਪ੍ਰਾਂਪਟਾਂ ਨੂੰ — ਜੋ ਕਿ Azure OpenAI ਦੇ GPT-5.2 ਦਾ ਉਪਯੋਗ ਕਰਦੇ ਹਨ। ਤੁਹਾਡੇ ਪ੍ਰਾਂਪਟਾਂ ਦੀ ਰਚਨਾ ਉਹ ਤਰੀਕਾ ਹੈ ਜੋ ਰਿਸਪੌਂਸ ਦੀ ਕੁਆਲਟੀ ਨੂੰ ਬੜਾ ਪ੍ਰਭਾਵਤ ਕਰਦਾ ਹੈ। ਅਸੀਂ ਮੁਢਲੀ ਪ੍ਰਾਂਪਟਿੰਗ ਤਕਨੀਕਾਂ ਦੀ ਸਮੀਖਿਆ ਕਰਦਿਆਂ ਸ਼ੁਰੂ ਕਰਦੇ ਹਾਂ, ਫਿਰ GPT-5.2 ਦੀਆਂ ਯੋਗਤਾਵਾਂ ਦਾ ਪੂਰਾ ਲਾਭ ਉਠਾਉਂਦੇ ਹੋਏ ਅੱਠ ਉਨ੍ਹੇਰੇ ਪੈਟਰਨਾਂ ‘ਤੇ ਜਾਵਾਂਗੇ।

ਅਸੀਂ GPT-5.2 ਇਸ ਲਈ ਵਰਤਾਂਗੇ ਕਿਉਂਕਿ ਇਸ ਵਿੱਚ ਸੋਚਣ ਨੂੰ ਨਿਯੰਤਰਿਤ ਕਰਨ ਦੀ ਸੁਵਿਧਾ ਸ਼ਾਮਲ ਹੈ - ਤੁਸੀਂ ਮਾਡਲ ਨੂੰ ਦੱਸ ਸਕਦੇ ਹੋ ਕਿ ਉੱਤਰ ਦੇਣ ਤੋਂ ਪਹਿਲਾਂ ਕਿੰਨੀ ਸੋਚ ਕਰਨੀ ਹੈ। ਇਹ ਵੱਖ-ਵੱਖ ਪ੍ਰਾਂਪਟਿੰਗ ਰਣਨੀਤੀਆਂ ਨੂੰ ਹੋਰ ਸਪਸ਼ਟ ਬਣਾਉਂਦਾ ਹੈ ਅਤੇ ਤੁਹਾਨੂੰ ਹਰ ਪਹੁੰਚ ਵਰਤਣ ਦਾ ਸਮਾਂ ਸਮਝਣ ਵਿੱਚ ਮਦਦ ਕਰਦਾ ਹੈ। ਸਾਥ ਹੀ, Azure ਦੇ GPT-5.2 ਲਈ ਘੱਟ ਰੇਟ ਸੀਮਾਵਾਂ GitHub ਮਾਡਲਾਂ ਨਾਲੋਂ ਬਿਹਤਰ ਹਨ।

## ਪੂਰਵ ਸ਼ਰਤਾਂ

- ਮਾਡਿਊਲ 01 ਮੁਕੰਮਲ ਕੀਤਾ ਹੋਇਆ (Azure OpenAI ਸਰੋਤ ਤਿਆਰ ਕੀਤੇ ਹੋਏ)
- ਰੂਟ ਡਾਇਰੈਕਟਰੀ ‘ਚ `.env` ਫਾਈਲ ਜਿਸ ਵਿੱਚ Azure ਪ੍ਰਮਾਣ ਪੱਤਰ ਹਨ (Module 01 ਵਿੱਚ `azd up` ਨਾਲ ਬਣਾਈ ਗਈ)

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਮਾਡਿਊਲ 01 ਨਹੀਂ ਕੀਤਾ ਤਾਂ ਪਹਿਲਾਂ ਉਹਥੇ ਦਿੱਤੇ ਗਿਆ ਤਿਨੂੰ ਤਦਬੀਰਾਂ ਦੀ ਪਾਲਨਾ ਕਰੋ।

## ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਦੀ ਸਮਝ

<img src="../../../translated_images/pa/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਕੀ ਹੈ?" width="800"/>

ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਇਸ ਗੱਲ ਬਾਰੇ ਹੈ ਕਿ ਇੰਪੁੱਟ ਟੈਕਸਟ ਨੂੰ ਇਸ ਤਰੀਕੇ ਨਾਲ ਡਿਜ਼ਾਈਨ ਕਰਨਾ ਜੋ ਤੁਹਾਨੂੰ ਚਾਹੀਦਾ ਨਤੀਜਾ ਮੁਹੱਈਆ ਕਰਵਾਏ। ਇਹ ਸਿਰਫ ਸਵਾਲ ਪੁੱਛਣ ਬਾਰੇ ਨਹੀਂ ਹੈ - ਇਹ ਅਰਜ਼ੀ ਨੂੰ ਇਸ ਤਰੀਕੇ ਨਾਲ ਸੰਰਚਿਤ ਕਰਨ ਬਾਰੇ ਹੈ ਤਾਂ ਜੋ ਮਾਡਲ ਸਮਝ ਸਕੇ ਕਿ ਤੁਸੀਂ ਕੀ ਚਾਹੁੰਦੇ ਹੋ ਅਤੇ ਕਿਵੇਂ ਪੁੱਜਾਉਣਾ ਹੈ।

ਇਸਨੂੰ ਇੱਕ ਸਾਥੀ ਨੂੰ ਹੁਕਮ ਦੇਣ ਵਰਗਾ ਸੋਚੋ। "ਬੱਗ ਠੀਕ ਕਰੋ" ਅਸਪਸ਼ਟ ਹੈ। "UserService.java ਦੀ ਲਾਈਨ 45 ਵਿੱਚ ਨਲ ਪੌਇੰਟਰ ਐਕਸਪਸ਼ਨ ਨੂੰ ਨਲ ਚੈੱਕ ਸ਼ਾਮਲ ਕਰਕੇ ਠੀਕ ਕਰੋ" ਵਿਸ਼ੇਸ਼ ਹੈ। ਭਾਸ਼ਾ ਮਾਡਲ ਵੀ ਇਸੇ ਤਰ੍ਹਾਂ ਕੰਮ ਕਰਦੇ ਹਨ - ਵਿਸ਼ੇਸ਼ਤਾ ਅਤੇ ਸੰਰਚਨਾ ਮਹੱਤਵਪੂਰਨ ਹਨ।

<img src="../../../translated_images/pa/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="ਕਿਵੇਂ LangChain4j ਮਿਲਦਾ ਹੈ" width="800"/>

LangChain4j ਢਾਂਚਾ ਮੁਹੱਈਆ ਕਰਵਾਂਦਾ ਹੈ — ਮਾਡਲ ਕਨੈਕਸ਼ਨ, ਮੈਮੋਰੀ, ਅਤੇ ਸੁਨੇਹਾ ਕਿਸਮਾਂ — ਜਦਕਿ ਪ੍ਰਾਂਪਟ ਪੈਟਰਨ ਸਿਰਫ ਧਿਆਨਪੂਰਵਕ ਬਣੇ ਹੋਏ ਟੈਕਸਟ ਹੁੰਦੇ ਹਨ ਜੋ ਤੁਸੀਂ ਉਸ ਢਾਂਚੇ ਰਾਹੀਂ ਭੇਜਦੇ ਹੋ। ਮੁੱਖ ਇਮਾਰਤੀ ਪੱਥਰ ਹਨ `SystemMessage` (ਜੋ AI ਦਾ ਬਿਹੇਵਿਅਰ ਅਤੇ ਭੂਮਿਕਾ ਤੈਅ ਕਰਦਾ ਹੈ) ਅਤੇ `UserMessage` (ਜੋ ਤੁਹਾਡੀ ਅਸਲੀ ਬੇਨਤੀ ਲਿਆਉਂਦਾ ਹੈ)।

## ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਦੇ ਮੂਲ ਤੱਤ

<img src="../../../translated_images/pa/five-patterns-overview.160f35045ffd2a94.webp" alt="ਪੰਜ ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਪੈਟਰਨਾਂ ਦਾ ਜਾਇਜ਼ਾ" width="800"/>

ਇਸ ਮਾਡਿਊਲ ਵਿੱਚ ਉਨ੍ਹੇਰੇ ਪੈਟਰਨਾਂ ਵਿੱਚ ਡੁਬਕੀਆਂ ਮਾਰਨ ਤੋਂ ਪਹਿਲਾਂ ਆਓ ਪੰਜ ਬੁਨਿਆਦੀ ਪ੍ਰਾਂਪਟਿੰਗ ਤਕਨੀਕਾਂ ਦੀ ਸਮੀਖਿਆ ਕਰੀਏ। ਇਹ ਉਹ ਨਿਰਮਾਣ-ਖੰਡ ਹਨ ਜਿਨ੍ਹਾਂ ਬਾਰੇ ਹਰ ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰ ਨੂੰ ਜਾਣਨਾ ਚਾਹੀਦਾ ਹੈ। ਜੇ ਤੁਸੀਂ ਪਹਿਲਾਂ ਹੀ [ਕੁਇਕ ਸਟਾਰਟ ਮਾਡਿਊਲ](../00-quick-start/README.md#2-prompt-patterns) ਕਰ ਚੁੱਕੇ ਹੋ, ਤਾਂ ਤੁਸੀਂ ਇਹਨਾਂ ਨੂੰ عملي ਢੰਗ ਨਾਲ ਵੇਖਿਆ ਹੈ — ਇਹ ਹੈ ਪਿਠਭੂਮੀ ਦਾ ਢਾਂਚਾ।

### ਜ਼ੀਰੋ-ਸ਼ਾਟ ਪ੍ਰਾਂਪਟਿੰਗ

ਸਭ ਤੋਂ ਸਧਾਰਣ ਤਰੀਕਾ: ਬਿਨਾਂ ਕਿਸੇ ਉਦਾਹਰਨ ਦੇ ਮਾਡਲ ਨੂੰ ਸਿੱਧਾ ਹੁਕਮ ਦਿਓ। ਮਾਡਲ ਸਿਰਫ਼ ਆਪਣੀ ਤਿਆਰੀ 'ਤੇ ਨਿਰਭਰ ਕਰਦਾ ਹੈ ਕਿ ਕਿਵੇਂ ਕਿਰਿਆਨਵਿਤ ਕਰਨੀ ਹੈ। ਇਹ ਸਹੀ ਹੁੰਦਾ ਹੈ ਜਦੋਂ ਮੰਗਿਆ ਗਿਆ ਵਿਭਾਵ ਸਪਸ਼ਟ ਹੋਵੇ।

<img src="../../../translated_images/pa/zero-shot-prompting.7abc24228be84e6c.webp" alt="ਜ਼ੀਰੋ-ਸ਼ਾਟ ਪ੍ਰਾਂਪਟਿੰਗ" width="800"/>

*ਬਿਨਾਂ ਕਿਸੇ ਉਦਾਹਰਨ ਦੇ ਸਿੱਧਾ ਹੁਕਮ — ਮਾਡਲ ਸਿਰਫ ਹੁਕਮ ਤੋਂ ਕੰਮ ਦਾ ਅਨੁਮਾਨ ਲਗਾਉਂਦਾ ਹੈ*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// ਜਵਾਬ: "ਧਨਾਤਮਕ"
```
  
**ਕਦੋਂ ਵਰਤਣਾ:** ਸਧਾਰਣ ਵਰਗੀਕਰਨ, ਸਿੱਧੇ ਸਵਾਲ, ਅਨੁਵਾਦ, ਜਾਂ ਕੋਈ ਵੀ ਕੰਮ ਜੋ ਮਾਡਲ ਬਿਨਾਂ ਵਾਧੂ ਮੱਦਦ ਤੋਂ ਕਰ ਸਕਦਾ ਹੈ।

### ਥੋੜ੍ਹੇ-ਸ਼ਾਟ ਪ੍ਰਾਂਪਟਿੰਗ

ਉਦਾਹਰਨਾਂ ਦਿਓ ਜੋ ਮਾਡਲ ਨੂੰ ਦਿਖਾਉਣ ਕਿ ਤੁਸੀਂ ਕਿਹੜਾ ਪੈਟਰਨ ਫਾਲੋ ਕਰਨਾ ਚਾਹੁੰਦੇ ਹੋ। ਮਾਡਲ ਤੁਹਾਡੇ ਉਦਾਹਰਨਾਂ ਤੋਂ ਇਨਪੁੱਟ-ਆਉਟਪੁੱਟ ਦਾ ਫਾਰਮੈਟ ਸਿੱਖਦਾ ਹੈ ਅਤੇ ਨਵਾਂ ਇਨਪੁੱਟ ਮਿਲਣ ‘ਤੇ ਇਸਦੀ ਲਾਗੂ ਕਰਦਾ ਹੈ। ਇਹ ਮੁਰੀਦਗੀ ਦੀ ਸਥਿਰਤਾ ਨੂੰ ਬਹੁਤ ਸੁਧਾਰਦਾ ਹੈ ਜਦ ਪਹਿਨਦਾ ਫਾਰਮੈਟ ਸਪਸ਼ਟ ਨਹੀਂ ਹੁੰਦਾ।

<img src="../../../translated_images/pa/few-shot-prompting.9d9eace1da88989a.webp" alt="ਥੋੜ੍ਹੇ-ਸ਼ਾਟ ਪ੍ਰਾਂਪਟਿੰਗ" width="800"/>

*ਉਦਾਹਰਨਾਂ ਤੋਂ ਸਿੱਖਣਾ — ਮਾਡਲ ਪੈਟਰਨ ਨੂੰ ਸਮਝਦਾ ਹੈ ਅਤੇ ਨਵਾਂ ਇਨਪੁੱਟ ‘ਤੇ ਲਾਗੂ ਕਰਦਾ ਹੈ*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```
  
**ਕਦੋਂ ਵਰਤਣਾ:** ਕਸਟਮ ਵਰਗੀਕਰਨ, ਲਗਾਤਾਰ ਫਾਰਮੈਟਿੰਗ, ਖੇਤਰ-ਵਿਸ਼ੇਸ਼ ਕੰਮ ਜਾਂ ਜਦ ਜ਼ੀਰੋ-ਸ਼ਾਟ ਨਤੀਜੇ ਅਸਥਿਰ ਹੋਣ।

### ਚੇਨ ਆਫ ਥੌਟ

ਮਾਡਲ ਨੂੰ ਕਦਮ-ਦਰ-ਕਦਮ ਆਪਣਾ ਵਿਚਾਰ ਦਰਸਾਉਣ ਲਈ ਕਹੋ। ਸਿੱਧਾ ਜਵਾਬ ਦੇਣ ਦੀ ਬਜਾਏ, ਮਾਡਲ ਸਮੱਸਿਆ ਨੂੰ ਢਾਹਦਾ ਹੈ ਅਤੇ ਹਰ ਹਿੱਸੇ ਨੂੰ ਖੁੱਲ੍ਹ ਕੇ ਕੰਮ ਕਰਦਾ ਹੈ। ਇਹ ਗਣਿਤ, ਤਰਕ, ਅਤੇ ਕਈ-ਕਦਮੀ ਸੋਚ ਵਾਲੇ ਕੰਮਾਂ ਵਿੱਚ ਸਹੀਤਾ ਸੁਧਾਰਦਾ ਹੈ।

<img src="../../../translated_images/pa/chain-of-thought.5cff6630e2657e2a.webp" alt="ਚੇਨ ਆਫ ਥੌਟ ਪ੍ਰਾਂਪਟਿੰਗ" width="800"/>

*ਕਦਮ-ਦਰ-ਕਦਮ ਵਿਚਾਰ — ਜਟਿਲ ਸਮੱਸਿਆਵਾਂ ਨੂੰ ਸਪਸ਼ਟ ਤਰਕਕੁੱਲ ਕਦਮਾਂ ਵਿੱਚ ਤੋੜਨਾ*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// ਮਾਡਲ ਦਿਖਾਉਂਦਾ ਹੈ: 15 - 8 = 7, ਫਿਰ 7 + 12 = 19 ਸੇਬ
```
  
**ਕਦੋਂ ਵਰਤਣਾ:** ਗਣਿਤ ਦੀਆਂ ਸਮੱਸਿਆਵਾਂ, ਤਰਕਕੁੱਲ ਪਹੇਲੀਆਂ, ਡੀਬੱਗਿੰਗ, ਜਾਂ ਕੋਈ ਵੀ ਕੰਮ ਜਿਸ ਵਿਚ ਵਿਚਾਰ ਪ੍ਰਕਿਰਿਆ ਦੇਖਾਉਣ ਨਾਲ ਸਹੀਤਾ ਅਤੇ ਭਰੋਸੇਮੰਦਤਾ ਵਧਦੀ ਹੈ।

### ਰੋਲ-ਅਧਾਰਿਤ ਪ੍ਰਾਂਪਟਿੰਗ

ਆਪਣੇ ਸਵਾਲ ਤੋਂ ਪਹਿਲਾਂ AI ਲਈ ਕੋਈ ਖਾਸ ਹਾਲਤ ਜਾਂ ਭੂਮਿਕਾ ਨਿਰਧਾਰਿਤ ਕਰੋ। ਇਹ ਸੰਦਰਭ ਮੁਹੱਈਆ ਕਰਵਾਂਦਾ ਹੈ ਜੋ ਜਵਾਬ ਦੇਣ ਦੀ ਟੋਨ, ਗਹਿਰਾਈ ਅਤੇ ਧਿਆਨ ਨੂੰ ਰੂਪ ਦੇਂਦਾ ਹੈ। "ਸਾਫਟਵੇਅਰ ਆਰਕੀਟੈਕਟ" ਵੱਖਰਾ ਸਲਾਹ ਦਿੰਦਾ ਹੈ ਬਿਨਾਂ "ਜੂਨੀਅਰ ਡਿਵੈਲਪਰ" ਜਾਂ "ਸੁਰੱਖਿਆ ਆਡੀਟਰ" ਤੋਂ।

<img src="../../../translated_images/pa/role-based-prompting.a806e1a73de6e3a4.webp" alt="ਰੋਲ-ਅਧਾਰਿਤ ਪ੍ਰਾਂਪਟਿੰਗ" width="800"/>

*ਸੰਦਰਭ ਅਤੇ ਭੂਮਿਕਾ ਬਣਾਉਣਾ — ਇੱਕੋ ਸਵਾਲ ਵੱਖਰੇ ਜਵਾਬ ਦਿੰਦਾ ਹੈ ਜੋ ਦਿੱਤੀ ਗਈ ਭੂਮਿਕਾ ਤੇ ਨਿਰਭਰ ਕਰਦਾ ਹੈ*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```
  
**ਕਦੋਂ ਵਰਤਣਾ:** ਕੂਡ ਸਮੀਖਿਆ, ਟਿਊਟੋਰਿੰਗ, ਖੇਤਰ-ਵਿਸ਼ੇਸ਼ ਵਿਸ਼ਲੇਸ਼ਣ, ਜਾਂ ਜਦ ਤੁਸੀਂ ਕਿਸੇ ਮਾਹਰਤਾ ਸਤਰ ਜਾਂ ਦ੍ਰਿਸ਼ਟੀਕੋਣ ਲਈ ਖਾਸ ਤੌਰ ਤੇ ਨਿਰਧਾਰਿਤ ਜਵਾਬ ਚਾਹੁੰਦੇ ਹੋ।

### ਪ੍ਰਾਂਪਟ ਟੈਮਪਲੇਟ

ਵੱਖ-ਵੱਖ ਕਿਸਮਾਂ ਵਾਲੇ ਚਿਹਰਿਆਕਾਰਨਾਂ ਲਈ ਦੁਹਰਾਈ ਜਾ ਸਕਣ ਵਾਲੇ ਪ੍ਰਾਂਪਟ ਬਣਾਓ। ਹਰ ਵਾਰੀ ਨਵਾਂ ਪ੍ਰਾਂਪਟ ਲਿਖਣ ਦੀ ਬਜਾਏ, ਇੱਕ ਵਾਰੀ ਟੈਮਪਲੇਟ ਤਿਆਰ ਕਰੋ ਅਤੇ ਵੱਖਰੇ ਮੁੱਲ ਭਰੋ। LangChain4j ਦਾ `PromptTemplate` ਕਲਾਸ ਇਹ ਅਸਾਨੀ ਨਾਲ `{{variable}}` ਵਾਰਗੀ ਸਿੰਟੈਕਸ ਦੇ ਨਾਲ ਬਣਾਉਂਦਾ ਹੈ।

<img src="../../../translated_images/pa/prompt-templates.14bfc37d45f1a933.webp" alt="ਪ੍ਰਾਂਪਟ ਟੈਮਪਲੇਟ" width="800"/>

*ਬਦਲ-ਬਦਲ ਕੰਮ ਵਾਲੇ ਦੁਹਰਾਏ ਜਾ ਸਕਣ ਵਾਲੇ ਪ੍ਰਾਂਪਟ — ਇਕ ਟੈਮਪਲੇਟ ਕਈ ਵਰਤੋਂ*

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```
  
**ਕਦੋਂ ਵਰਤਣਾ:** ਵੱਖਰੇ ਇਨਪੁੱਟਾਂ ਨਾਲ ਦੁਹਰਾਏ ਜਾਣ ਵਾਲੇ ਪ੍ਰਸ਼ਨ, ਬੈਚ ਪ੍ਰੋਸੈਸਿੰਗ, ਦੁਹਰਾਈ ਜਾ ਸਕਣ ਵਾਲੇ AI ਵਰਕਫਲੋਜ਼ ਬਣਾਉਣਾ, ਜਾਂ ਜਦ ਪ੍ਰਾਂਪਟ ਦੀ ਰਚਨਾ ਬਰਕਾਰ ਰਹਿੰਦੀ ਹੈ ਪਰ ਡੇਟਾ ਬਦਲਦਾ ਹੈ।

---

ਇਹ ਪੰਜ ਮੂਲ ਤੱਤ ਤੁਹਾਨੂੰ ਬਹੁਤੇ ਪ੍ਰਾਂਪਟਿੰਗ ਕੰਮਾਂ ਲਈ ਮਜ਼ਬੂਤ ਸੰਦ ਦੀ ਸ਼੍ਰੇਣੀ ਦੇਂਦੇ ਹਨ। ਇਸ ਮਾਡਿਊਲ ਵਿੱਚ ਬਾਕੀ ਹਿੱਸਾ ਆਠ ਉਨ੍ਹੇਰੇ ਪੈਟਰਨਾਂ ‘ਤੇ ਧਿਆਨ ਕੇਂਦਰਿਤ ਕਰਦਾ ਹੈ ਜੋ GPT-5.2 ਦੀ ਸੋਚ ਕਰਨ ਦਾ ਨਿਰੀਖਣ, ਸੁਆਤਮ-ਮੁਲਾਂਕਣ, ਅਤੇ ਸੰਰਚਿਤ ਨਿਕਾਸ ਯੋਗਤਾਵਾਂ ਦਿੰਦੇ ਹਨ।

## ਉਨ੍ਹੇਰੇ ਪੈਟਰਨ

ਮੂਲ ਤੱਤ ਕਵਰ ਕਰਨ ਤੋਂ ਬਾਅਦ, ਆਓ ਉਹ ਆਠ ਉਨ੍ਹੇਰੇ ਪੈਟਰਨਾਂ ‘ਤੇ ਚੱਲੀਏ ਜੋ ਇਸ ਮਾਡਿਊਲ ਨੂੰ ਵਿਲੱਖਣ ਬਣਾਉਂਦੇ ਹਨ। ਹਰ ਸਮੱਸਿਆ ਲਈ ਇਕੋ ਉਪਾਇ ਨਹੀਂ ਲੋੜੀਂਦਾ। ਕੁਝ ਸਵਾਲਾਂ ਨੂੰ ਜਲਦੀ ਜਵਾਬ ਚਾਹੀਦਾ ਹੈ, ਕੁਝ ਨੂੰ ਗਹਿਰਾ ਵਿਚਾਰ। ਕੁਝ ਨੂੰ ਦਿੱਸਣ ਵਾਲੀ ਸੋਚ, ਕੁਝ ਨੂੰ ਸਿਰਫ ਨਤੀਜੇ। ਹੇਠਾਂ ਦਿੱਤੇ ਪ੍ਰਤੀਕ ਹਰ ਇਕ ਵੱਖਰੇ ਸੰਦਰਭ ਲਈ ਮੁਹੱਈਆ ਕੀਤਾ ਗਿਆ ਹੈ — ਅਤੇ GPT-5.2 ਦੀ ਸੋਚ ਨਿਯੰਤਰਣ ਇਹ ਫਰਕ ਹੋਰ ਜ਼ਾਹਿਰ ਕਰਦਾ ਹੈ।

<img src="../../../translated_images/pa/eight-patterns.fa1ebfdf16f71e9a.webp" alt="ਆਠ ਪ੍ਰਾਂਪਟਿੰਗ ਪੈਟਰਨ" width="800"/>

*ਆਠ ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਪੈਟਰਨਾਂ ਅਤੇ ਉਨ੍ਹਾਂ ਦੀ ਵਰਤੋਂ ਵਰਗਾ ਜਾਇਜ਼ਾ*

<img src="../../../translated_images/pa/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 ਨਾਲ ਸੋਚ ਨਿਯੰਤਰਣ" width="800"/>

*GPT-5.2 ਦਾ ਸੋਚ ਨਿਯੰਤਰਣ ਤੁਹਾਨੂੰ ਤੈਅ ਕਰਨ ਦਿੰਦਾ ਹੈ ਕਿ ਮਾਡਲ ਕਿੰਨੀ ਸੋਚ ਕਰੇ — ਤੇਜ਼ ਸਿੱਧੇ ਜਵਾਬ ਤੋਂ ਲੈ ਕੇ ਡੂੰਘੀ ਖੋਜ ਤੱਕ*

**ਘੱਟ ਉਤਸ਼ਾਹ (ਜਲਦੀ ਅਤੇ ਕੇਂਦਰਿਤ)** - ਸਧਾਰਣ ਸਵਾਲਾਂ ਲਈ ਜਿੱਥੇ ਤੁਸੀਂ ਤੇਜ਼, ਸਿੱਧੇ ਜਵਾਬ ਚਾਹੁੰਦੇ ਹੋ। ਮਾਡਲ ਘੱਟ ਤੋਂ ਘੱਟ 2 ਕਦਮਾਂ ਤੱਕ ਹੀ ਸੋਚਦਾ ਹੈ। ਇਹ ਗਣਨਾ, ਲੁੱਕਅਪ, ਜਾਂ ਸਿੱਧੇ ਸਵਾਲਾਂ ਲਈ ਵਰਤੋਂ।

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```
  
> 💡 **GitHub Copilot ਨਾਲ ਖੋਜ ਕਰੋ:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:  
> - "ਘੱਟ ਉਤਸ਼ਾਹ ਅਤੇ ਵੱਧ ਉਤਸ਼ਾਹ ਪ੍ਰਾਂਪਟਿੰਗ ਪੈਟਰਨ ਵਿਚੋਂ ਕੀ ਫਰਕ ਹੈ?"  
> - "ਪ੍ਰਾਂਪਟਾਂ ਵਿੱਚ XML ਟੈਗ ਕਿਵੇਂ AI ਦੇ ਜਵਾਬ ਦੀ ਸੰਗਠਨਾ ਵਿੱਚ ਮਦਦ ਕਰਦੇ ਹਨ?"  
> - "ਸਵੈ-ਪ੍ਰਤੀਬਿੰਬ ਪੈਟਰਨ ਅਤੇ ਸਿੱਧੇ ਹੁਕਮ ਵਿੱਚ ਕਦੋਂ ਵਰਤਣਾ ਚਾਹੀਦਾ ਹੈ?"

**ਵੱਧ ਉਤਸ਼ਾਹ (ਗਹਿਰਾਈ ਅਤੇ ਪੂਰੀ ਤਰ੍ਹਾਂ)** - ਜਟਿਲ ਸਮੱਸਿਆਵਾਂ ਲਈ ਜਿੱਥੇ ਤੁਸੀਂ ਵਿਸਤਾਰਪੂਰਵਕ ਵਿਸ਼ਲੇਸ਼ਣ ਚਾਹੁੰਦੇ ਹੋ। ਮਾਡਲ ਪੂਰੀ ਤਰ੍ਹਾਂ ਖੋਜ ਕਰਦਾ ਹੈ ਅਤੇ ਵਿਸਥਾਰ ਨਾਲ ਸੋਚ ਦਿਖਾਉਂਦਾ ਹੈ। ਇਹ ਸਿਸਟਮ ਡਿਜ਼ਾਈਨ, ਵਾਸਤੁਕਲਾ ਫੈਸਲੇ, ਜਾਂ ਗਹਿਰੇ ਅਨੁਸੰਧਾਨ ਲਈ ਵਰਤੋਂ।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**ਟਾਸਕ ਐਕਜੀਕਿਊਸ਼ਨ (ਕਦਮ-ਦਰ-ਕਦਮ ਤਰੱਕੀ)** - ਕੁਈ-ਕਦਮੀ ਵਰਕਫਲੋਜ਼ ਲਈ। ਮਾਡਲ ਪਹਿਲਾਂ ਯੋਜਨਾ ਦਿੰਦਾ ਹੈ, ਹਰ ਕਦਮ ਵਿੱਚ ਕੰਮ ਦੱਸਦਾ ਹੈ, ਫਿਰ ਨਤੀਜਾ ਦਿੰਦਾ ਹੈ। ਇਹ ਮਾਈਗ੍ਰੇਸ਼ਨ, ਕਾਰਜਨਵੀਤੀਆਂ, ਜਾਂ ਕੋਈ ਵੀ ਕਾਈ-ਕਦਮੀ ਪ੍ਰਕਿਰਿਆ ਲਈ ਵਰਤੋਂ।

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```
  
ਚੇਨ-ਆਫ-ਥੌਟ ਪ੍ਰਾਂਪਟਿੰਗ ਮਾਡਲ ਨੂੰ ਖੁੱਲ੍ਹ ਕੇ ਆਪਣੀ ਸੋਚ ਦਰਸਾਉਣ ਲਈ ਕਹਿੰਦਾ ਹੈ, ਜੋ ਜਟਿਲ ਕੰਮਾਂ ਲਈ ਸਹੀਤਾ ਸੁਧਾਰਦਾ ਹੈ। ਕਦਮ-ਦਰ-ਕਦਮ ਵਿਸ਼ਲੇਸ਼ਣ ਮਨੁੱਖਾਂ ਅਤੇ AI ਦੋਹਾਂ ਦੀ ਤਰਕ ਸਮਝਣ ਵਿੱਚ ਮਦਦ ਕਰਦੀ ਹੈ।

> **🤖 GitHub Copilot ਚੈਟ ਨਾਲ ਟ੍ਰਾਈ ਕਰੋ:** ਇਸ ਪੈਟਰਨ ਬਾਰੇ ਪੁੱਛੋ:  
> - "ਲੰਬੇ ਦੌਰਾਨੇ ਬੰਦੋਬਸਤ ਦੇ ਕਾਰਜਾਂ ਲਈ ਟਾਸਕ ਐਕਜੀਕਿਊਸ਼ਨ ਪੈਟਰਨ ਨੂੰ ਕਿਵੇਂ ਅਨੁਕੂਲਿਤ ਕਰਾਂ?"  
> - "ਉਤਪਾਦਨ ਐਪਲੀਕੇਸ਼ਨਾਂ ਵਿੱਚ ਟੂਲ ਪ੍ਰੀਐਂਬਲਾਂ ਦੀ ਰਚਨਾ ਲਈ ਸਭ ਤੋਂ ਵਧੀਆ ਤਰੀਕਾ ਕੀ ਹੈ?"  
> - "ਇੱਕ ਯੂਆਈ ਵਿੱਚ ਦਰਮਿਆਨੀ ਤਰੱਕੀ ਅਪਡੇਟ ਕਿਵੇਂ ਇੱਕੱਠੇ ਅਤੇ ਦਿਖਾਏ ਜਾ ਸਕਦੇ ਹਨ?"

<img src="../../../translated_images/pa/task-execution-pattern.9da3967750ab5c1e.webp" alt="ਟਾਸਕ ਐਕਜੀਕਿਊਸ਼ਨ ਪੈਟਰਨ" width="800"/>

*ਯੋਜਨਾ → ਕਾਰਜ → ਸੰਖਿਪਤ ਵਰਕਫਲੋਜ਼ ਕਈ-ਕਦਮੀ ਕਾਰਜਾਂ ਲਈ*

**ਸਵੈ-ਪ੍ਰਤੀਬਿੰਬਕ ਕੋਡ** - ਉਤਪਾਦਨ-ਗੁਣਵੱਤਾ ਵਾਲਾ ਕੋਡ ਤਿਆਰ ਕਰਨ ਲਈ। ਮਾਡਲ ਉਤਪਾਦਨ ਮਾਪਦੰਡਾਂ ਦੀ ਪਾਲਣਾ ਕਰਦਿਆਂ ਸੋਧੀ ਕੋਡ ਬਣਾਉਂਦਾ ਹੈ ਜਿਸ ਵਿੱਚ ਠੀਕ ਤਰੀਕੇ ਨਾਲ ਏਰਰ ਹੈਂਡਲਿੰਗ ਵੀ ਸ਼ਾਮਲ ਹੈ। ਜਦੋਂ ਨਵੀਆਂ ਫੀਚਰਾਂ ਜਾਂ ਸਰਵਿਸਿਜ਼ ਬਣਾਓ, ਇਹ ਵਰਤੋ।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
<img src="../../../translated_images/pa/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="ਸਵੈ-ਪ੍ਰਤੀਬਿੰਬ ਚੱਕਰ" width="800"/>

*ਦੋਹਰਾਉਂਦੇ ਸੁਧਾਰ ਦਾ ਚੱਕਰ - ਬਣਾਓ, ਮੁਲਾਂਕਣ ਕਰੋ, ਮੁੱਦੇ ਪਛਾਣੋ, ਸੁਧਾਰ ਕਰੋ, ਦੁਹਰਾਓ*

**ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ** - ਲਗਾਤਾਰ ਮੁਲਾਂਕਣ ਲਈ। ਮਾਡਲ ਇੱਕ ਨਿਰਧਾਰਿਤ ਢਾਂਚਾ ਦੇ ਤਹਿਤ ਕੋਡ ਦੀ ਸਮੀਖਿਆ ਕਰਦਾ ਹੈ (ਸਹੀਤਾ, ਅਭਿਆਸ, ਕਾਰਕਿਰਦਗੀ, ਸੁਰੱਖਿਆ, ਸੰਭਾਲਯੋਗਤਾ)। ਇਹ ਕੋਡ ਸਮੀਖਿਆ ਜਾਂ ਗੁਣਵੱਤਾ ਮੁਲਾਂਕਣ ਲਈ ਵਰਤਿਆ ਜਾ ਸਕਦਾ ਹੈ।

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```
  
> **🤖 GitHub Copilot ਚੈਟ ਨਾਲ ਟ੍ਰਾਈ ਕਰੋ:** ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ ਬਾਰੇ ਪੁੱਛੋ:  
> - "ਵੱਖ-ਵੱਖ ਕਿਸਮਾਂ ਦੀਆਂ ਕੋਡ ਸਮੀਖਿਆਵਾਂ ਲਈ ਵਿਸ਼ਲੇਸ਼ਣ ਢਾਂਚਾ ਕਿਵੇਂ ਕਸਟਮਾਈਜ਼ ਕਰਾਂ?"  
> - "ਸੰਰਚਿਤ ਨਿਕਾਸ ਨੂੰ ਪ੍ਰੋਗਰਾਮੈਟਿਕ ਤੌਰ ‘ਤੇ ਕਿਵੇਂ ਪਰਸ ਅਤੇ ਕੰਮ ਵਿੱਚ ਲਿਆਵਾਂ?"  
> - "ਵੱਖ-ਵੱਖ ਸਮੀਖਿਆ ਸੈਸ਼ਨਾਂ ਵਿੱਚ ਇਕਸਾਰਤਾ ਨਾਲ ਤੀਬਰਤਾ ਦਰਜਿਆਂ ਨੂੰ ਕਿਵੇਂ ਯਕੀਨੀ ਬਣਾਵਾਂ?"

<img src="../../../translated_images/pa/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ ਪੈਟਰਨ" width="800"/>

*ਸਥਿਰ ਕੋਡ ਸਮੀਖਿਆ ਲਈ ਢਾਂਚਾ ਤੇ ਤੀਬਰਤਾ ਦਰਜੇ*

**ਕਈ-ਚਰਣ ਚੈਟ** - ਗੱਲਬਾਤ ਲਈ ਜੋ ਸੰਦਰਭ ਦੀ ਲੋੜ ਹੈ। ਮਾਡਲ ਪਿਛਲੇ ਸੁਨੇਹਿਆਂ ਨੂੰ ਯਾਦ ਰੱਖਦਾ ਹੈ ਅਤੇ ਉਨ੍ਹਾਂ ‘ਤੇ ਅਗੇ ਤੇਜ਼ੀ ਨਾਲ ਅੱਗੇ ਵਧਦਾ ਹੈ। ਇਹ ਇੰਟਰਐਕਟਿਵ ਸਹਾਇਤਾ ਸੈਸ਼ਨਾਂ ਜਾਂ ਕਠਿਨ ਪ੍ਰਸ਼ਨ-ਉੱਤਰ ਲਈ ਬਿਹਤਰ ਹੈ।

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
<img src="../../../translated_images/pa/context-memory.dff30ad9fa78832a.webp" alt="ਸੰਦਰਭ ਅਤੇ ਮੈਮੋਰੀ" width="800"/>

*ਕਿਵੇਂ ਗੱਲਬਾਤ ਸੰਦਰਭ ਕਈ ਚਰਣਾਂ ਵਿੱਚ ਇਕੱਠਾ ਹੁੰਦਾ ਹੈ ਜਦ ਤੱਕ ਟੋਕਨ ਸੀਮਾ ਨਹੀਂ ਪਹੁੰਚਦੀ*

**ਕਦਮ-ਦਰ-ਕਦਮ ਵਿਚਾਰ** - ਉਹ ਸਮੱਸਿਆਵਾਂ ਲਈ ਜਿਨ੍ਹਾਂ ਵਿੱਚ ਤਰਕ ਸਪਸ਼ਟ ਹੋਣਾ ਜਰੂਰੀ ਹੈ। ਮਾਡਲ ਹਰ ਕਦਮ ਲਈ ਖੁੱਲ੍ਹਾ ਵਿਚਾਰ ਦਿਖਾਉਂਦਾ ਹੈ। ਇਹ ਗਣਿਤ, ਤਰਕ ਪਹੇਲੀਆਂ ਲਈ ਜ਼ਰੂਰੀ ਹੈ ਜਾਂ ਜਦ ਤੂੰ ਸੋਚਣ ਦੀ ਪ੍ਰਕਿਰਿਆ ਸਮਝਣਾ ਚਾਹੁੰਦਾ ਹੈ।

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

*ਸਮੱਸਿਆਵਾਂ ਨੂੰ ਖੁੱਲ੍ਹੇ ਤਰਕਕੁੱਲ ਕਦਮਾਂ ਵਿੱਚ ਵੰਡਣਾ*

**ਸੀਮਿਤ ਨਿਕਾਸ** - ਉਹ ਉੱਤਰ ਜੋ ਵਿਸ਼ੇਸ਼ ਫਾਰਮੈਟ ਦੀ ਲੋੜ ਰੱਖਦੇ ਹਨ। ਮਾਡਲ ਫਾਰਮੈਟ ਅਤੇ ਲੰਬਾਈ ਦੇ ਨਿਯਮਾਂ ਦਾ ਕੜੀ ਪਾਲਣਾ ਕਰਦਾ ਹੈ। ਇਸਨੂੰ ਸੰਖੇਪ ਜਾਂ ਜਦ ਤੁਹਾਨੂੰ ਬਿਲਕੁਲ ਨਿਰਧਾਰਿਤ ਨਿਕਾਸ ਦੀ ਲੋੜ ਹੋਵੇ, ਵਰਤੋਂ।

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
  
<img src="../../../translated_images/pa/constrained-output-pattern.0ce39a682a6795c2.webp" alt="ਸੀਮਿਤ ਨਿਕਾਸ ਪੈਟਰਨ" width="800"/>

*ਖਾਸ ਫਾਰਮੈਟ, ਲੰਬਾਈ, ਅਤੇ ਸੰਰਚਨਾ ਦੇ ਨਿਯਮਾਂ ਨੂੰ ਲਾਗੂ ਕਰਨਾ*

## ਮੌਜੂਦਾ Azure ਸਰੋਤਾਂ ਦਾ ਉਪਯੋਗ

**ਤਦਬੀਰ ਦੀ ਜਾਂਚ ਕਰੋ:**

ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਰੂਟ ਡਾਇਰੈਕਟਰੀ 'ਚ `.env` ਫਾਈਲ ਹੈ ਜਿੱਥੇ Azure ਪ੍ਰਮਾਣ ਪੱਤਰ ਹਨ (Module 01 ਦੌਰਾਨ ਬਣਾਈ ਗਈ):  
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ਦਿਖਾਉਣੇ ਚਾਹੀਦੇ ਹਨ
```
  
**ਐਪਲੀਕੇਸ਼ਨ ਸ਼ੁਰੂ ਕਰੋ:**

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਪਹਿਲਾਂ ਹੀ ਸਾਰੇ ਐਪਲੀਕੇਸ਼ਨ `./start-all.sh` ਨਾਲ Module 01 ਤੋਂ ਸ਼ੁਰੂ ਕਰ ਚੁੱਕੇ ਹੋ, ਤਾਂ ਇਹ ਮਾਡਿਊਲ ਪਹਿਲਾਂ ਹੀ ਪੋਰਟ 8083 ਤੇ ਚੱਲ ਰਿਹਾ ਹੈ। ਤੁਸੀਂ ਹੇਠਾਂ ਦਿੱਤੇ ਸ਼ੁਰੂ ਕਰਨ ਦੇ ਹੁਕਮ ਛੱਡ ਕੇ ਸਿੱਧਾ http://localhost:8083 ‘ਤੇ ਜਾ ਸਕਦੇ ਹੋ।

**ਵਿਕਲਪ 1: ਸਪ੍ਰਿੰਗ ਬੂਟ ਡੈਸ਼ਬੋਰਡ ਦਾ ਉਪਯੋਗ (VS Code ਉਪਭੋਗਤਾਵਾਂ ਲਈ ਸਿਫਾਰਸ਼ੀ)**

ਡੈਵ ਕੰਟੇਨਰ ਵਿੱਚ ਸਪ੍ਰਿੰਗ ਬੂਟ ਡੈਸ਼ਬੋਰਡ ਵਿਸਥਾਰ ਸ਼ਾਮਲ ਹੈ, ਜੋ ਸਾਰੇ ਸਪ੍ਰਿੰਗ ਬੂਟ ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਪ੍ਰਬੰਧਿਤ ਕਰਨ ਲਈ ਵਿਜੁਅਲ ਇੰਟਰਫੇਸ ਪਰਦਾਨ ਕਰਦਾ ਹੈ। ਤੁਸੀਂ ਇਸਨੂੰ VS Code ਦੇ ਖੱਬੇ ਪਾਸੇ ਐਕਟਿਵਿਟੀ ਬਾਰ ਵਿੱਚ (ਸਪ੍ਰਿੰਗ ਬੂਟ ਚਿੰਨ੍ਹ ਦੇਖੋ) ਲੱਭ ਸਕਦੇ ਹੋ।

ਸਪ੍ਰਿੰਗ ਬੂਟ ਡੈਸ਼ਬੋਰਡ ਤੋਂ ਤੁਸੀਂ:  
- ਵਰਕਸਪੇਸ ਵਿੱਚ ਉਪਲਬਧ ਸਾਰੇ ਸਪ੍ਰਿੰਗ ਬੂਟ ਐਪਲੀਕੇਸ਼ਨਾਂ ਦੀ ਲਿਸਟ ਵੇਖ ਸਕਦੇ ਹੋ  
- ਇੱਕ ਕਲਿੱਕ ਨਾਲ ਐਪਲੀਕੇਸ਼ਨ ਸ਼ੁਰੂ ਜਾਂ ਰੋਕ ਸਕਦੇ ਹੋ  
- ਐਪਲੀਕੇਸ਼ਨ ਦੇ ਲਾਗ ਰੀਅਲ-ਟਾਈਮ ਵਿੱਚ ਵੇਖ ਸਕਦੇ ਹੋ  
- ਐਪਲੀਕੇਸ਼ਨ ਦੀ ਸਥਿਤੀ ਨੂੰ ਨਿਗਰਾਨੀ ਕਰ ਸਕਦੇ ਹੋ
ਸਿੱਧਾ "prompt-engineering" ਦੇ ਨਾਲ ਖੇਡ ਬਟਨ 'ਤੇ ਕਲਿੱਕ ਕਰੋ ਇਸ ਮਾਡਿਊਲ ਨੂੰ ਸ਼ੁਰੂ ਕਰਨ ਲਈ, ਜਾਂ ਸਾਰੇ ਮਾਡਿਊਲ ਇੱਕ ਵਾਰ ਵਿੱਚ ਸ਼ੁਰੂ ਕਰੋ।

<img src="../../../translated_images/pa/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ਚੋਣ 2: ਸ਼ੈਲ ਸਕ੍ਰਿਪਟਾਂ ਦੀ ਵਰਤੋਂ ਕਰਨਾ**

ਸਾਰੇ ਵੈੱਬ ਐਪਲੀਕੇਸ਼ਨ (ਮਾਡਿਊਲ 01-04) ਸ਼ੁਰੂ ਕਰੋ:

**ਬੈਸ਼:**
```bash
cd ..  # ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਤੋਂ
./start-all.sh
```

**ਪਾਵਰਸ਼ੈਲ:**
```powershell
cd ..  # ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਤੋਂ
.\start-all.ps1
```

ਜਾਂ ਸਿਰਫ਼ ਇਸ ਮਾਡਿਊਲ ਨੂੰ ਸ਼ੁਰੂ ਕਰੋ:

**ਬੈਸ਼:**
```bash
cd 02-prompt-engineering
./start.sh
```

**ਪਾਵਰਸ਼ੈਲ:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

ਦੋਹਾਂ ਸਕ੍ਰਿਪਟਾਂ ਆਟੋਮੈਟਿਕਲੀ ਰੂਟ `.env` ਫਾਈਲ ਤੋਂ ਵਾਤਾਵਰਣ ਵੈਰੀਏਬਲ ਲੋਡ ਕਰਦੀਆਂ ਹਨ ਅਤੇ ਜੇਕਰ JARs ਮੌਜੂਦ ਨਹੀਂ ਹਨ ਤਾਂ ਉਹ ਬਣਾਉਣਗੀਆਂ।

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਸ਼ੁਰੂ ਕਰਨ ਤੋਂ ਪਹਿਲਾਂ ਸਾਰੇ ਮਾਡਿਊਲ ਮੈਨੁਅਲ ਤੌਰ 'ਤੇ ਬਿਲਡ ਕਰਨਾ ਚਾਹੁੰਦੇ ਹੋ:
>
> **ਬੈਸ਼:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **ਪਾਵਰਸ਼ੈਲ:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

ਆਪਣੇ ਬ੍ਰਾੁਜ਼ਰ ਵਿੱਚ http://localhost:8083 ਖੋਲ੍ਹੋ।

**ਰੋਕਣ ਲਈ:**

**ਬੈਸ਼:**
```bash
./stop.sh  # ਸਿਰਫ ਇਹ ਮੋਡੀਊਲ
# ਜਾਂ
cd .. && ./stop-all.sh  # ਸਾਰੇ ਮੋਡੀਊਲ
```

**ਪਾਵਰਸ਼ੈਲ:**
```powershell
.\stop.ps1  # ਸਿਰਫ ਇਹ ਮਾਡਿਊਲ
# ਜਾਂ
cd ..; .\stop-all.ps1  # ਸਾਰੇ ਮਾਡਿਊਲ
```

## ਐਪਲੀਕੇਸ਼ਨ ਦੇ ਸਕ੍ਰੀਨਸ਼ੌਟ

<img src="../../../translated_images/pa/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*ਮੁੱਖ ਡੈਸ਼ਬੋਰਡ ਜੋ ਸਾਰੇ 8 ਪ੍ਰੌਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਪੈਟਰਨਾਂ ਨੂੰ ਉਨ੍ਹਾਂ ਦੀਆਂ ਵਿਸ਼ੇਸ਼ਤਾਵਾਂ ਅਤੇ ਵਰਤੋਂ ਦੇ ਕੇਸਾਂ ਨਾਲ ਦਰਸਾਉਂਦਾ ਹੈ*

## ਪੈਟਰਨਾਂ ਦੀ ਖੋਜ

ਵੈੱਬ ਇੰਟਰਫੇਸ ਤੁਹਾਨੂੰ ਵੱਖ-ਵੱਖ ਪ੍ਰੌਂਪਟਿੰਗ ਰਣਨੀਤੀਆਂ ਨਾਲ ਪ੍ਰਯੋਗ ਕਰਨ ਦੀ ਆਗਿਆ ਦਿੰਦਾ ਹੈ। ਹਰ ਪੈਟਰਨ ਵੱਖ-ਵੱਖ ਸਮੱਸਿਆਵਾਂ ਦਾ ਹੱਲ ਕਰਦਾ ਹੈ - ਉਨ੍ਹਾਂ ਨੂੰ ਅਜ਼ਮਾਓ ਅਤੇ ਵੇਖੋ ਕਿ ਕਦੋਂ ਹਰ ਤਰੀਕਾ ਚਮਕਦਾ ਹੈ।

> **ਨੋਟ: ਸਟ੍ਰੀਮਿੰਗ ਵਿਰੁੱਧ ਨਾਨ-ਸਟ੍ਰੀਮਿੰਗ** — ਹਰ ਪੈਟਰਨ ਪੇਜ 'ਤੇ ਦੋ ਬਟਨ ਹਨ: **🔴 Stream Response (Live)** ਅਤੇ ਇੱਕ **ਨਾਨ-ਸਟ੍ਰੀਮਿੰਗ** ਵਿਕਲਪ। ਸਟ੍ਰੀਮਿੰਗ ਸਰਵਰ-ਸੈਂਟ ਇਵੈਂਟਸ (SSE) ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ ਤਾਂ ਜੋ ਮਾਡਲ ਜਦੋਂ ਟੋਕਨ ਜਨਰੇਟ ਕਰਦਾ ਹੈ ਤੁਰੰਤ ਵਿਖੇ ਸਕੋ। ਨਾਨ-ਸਟ੍ਰੀਮਿੰਗ ਵਿਕਲਪ ਪੂਰੇ ਜਵਾਬ ਦੀ ਉਡੀਕ ਕਰਦਾ ਹੈ ਫਿਰ ਵਿਖਾਉਂਦਾ ਹੈ। ਉਹ ਪ੍ਰੌਂਪਟ ਜਿਹੜੇ ਡੂੰਘੇ ਤਰਕ ਦੀ ਲੋੜ ਰੱਖਦੇ ਹਨ (ਜਿਵੇਂ ਕਿ High Eagerness, Self-Reflecting Code) ਲਈ ਨਾਨ-ਸਟ੍ਰੀਮਿੰਗ ਕਾਲ ਕਾਫ਼ੀ ਲੰਮਾ ਸਮਾਂ ਲੈ ਸਕਦੀ ਹੈ — ਕਈ ਵਾਰੀ ਮਿੰਟਾਂ — ਬਿਨਾ ਕਿਸੇ ਦਿੱਖਵੀਂ ਫੀਡਬੈਕ ਦੇ। **ਜਦੋਂ ਤੁਸੀਂ ਜਟਿਲ ਪ੍ਰੌਂਪਟਾਂ ਦੇ ਨਾਲ ਪ੍ਰਯੋਗ ਕਰ ਰਹੇ ਹੋ ਤਾਂ ਸਟ੍ਰੀਮਿੰਗ ਦੀ ਵਰਤੋਂ ਕਰੋ** ਤਾਂ ਜੋ ਤੁਸੀਂ ਮਾਡਲ ਨੂੰ ਕੰਮ ਕਰਦੇ ਦੇਖ ਸਕੋ ਅਤੇ ਇਹ ਅਹਿਸਾਸ ਨਾ ਹੋਵੇ ਕਿ ਬੇਨਤੀ ਦਾ ਸਮਾਂ ਸਮਾਪਤ ਹੋ ਗਿਆ ਹੈ।
>
> **ਨੋਟ: ਬ੍ਰਾਊਜ਼ਰ ਦੀ ਲੋੜ** — ਸਟ੍ਰੀਮਿੰਗ ਫੀਚਰ Fetch Streams API (`response.body.getReader()`) ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ ਜੋ ਇੱਕ ਪੂਰੇ ਬ੍ਰਾਊਜ਼ਰ (Chrome, Edge, Firefox, Safari) ਦੀ ਲੋੜ ਹੈ। ਇਹ VS Code ਦੇ ਨਾਲ ਦੇ ਸਮਰਥਿਤ Simple Browser ਵਿੱਚ ਕੰਮ ਨਹੀਂ ਕਰਦਾ, ਕਿਉਂਕਿ ਉਸ ਦਾ ਵੈੱਬਵਿਊ ReadableStream API ਦਾ ਸਮਰਥਨ ਨਹੀਂ ਕਰਦਾ। ਜੇ ਤੁਸੀਂ Simple Browser ਦੀ ਵਰਤੋਂ ਕਰਦੇ ਹੋ, ਤਾਂ ਨਾਨ-ਸਟ੍ਰੀਮਿੰਗ ਬਟਨਾਂ ਹੇਠਾਂ ਵੀ ਸਧਾਰਨ ਤੌਰ 'ਤੇ ਕੰਮ ਕਰਨਗੇ — ਸਿਰਫ ਸਟ੍ਰੀਮਿੰਗ ਬਟਨਾਂ ਨੂੰ ਪ੍ਰਭਾਵਿਤ ਕੀਤਾ ਜਾਂਦਾ ਹੈ। ਪੂਰੇ ਅਨੁਭਵ ਲਈ `http://localhost:8083` ਨੂੰ ਬਾਹਰੀ ਬ੍ਰਾਊਜ਼ਰ ਵਿੱਚ ਖੋਲ੍ਹੋ।

### ਘੱਟ ਵਿਰੁੱਧ ਵਧੀਕ Eagerness

ਘੱਟ Eagerness ਨਾਲ "What is 15% of 200?" ਵਰਗਾ ਸਧਾਰਣ ਸਵਾਲ ਪੁੱਛੋ। ਤੁਹਾਨੂੰ ਤੁਰੰਤ, ਸਿੱਧਾ ਜਵਾਬ ਮਿਲੇਗਾ। ਹੁਣ ਵਧੀਕ Eagerness ਨਾਲ "Design a caching strategy for a high-traffic API" ਵਰਗਾ ਜਟਿਲ ਸਵਾਲ ਪੁੱਛੋ। **🔴 Stream Response (Live)** 'ਤੇ ਕਲਿੱਕ ਕਰੋ ਅਤੇ ਮਾਡਲ ਦੀ ਵਿਆਪਕ ਤਰਕ ਪ੍ਰਤੀ ਟੋਕਨ ਦਿਖਾਏ ਜਾਣ ਦਾ ਮੁਆਇਨਾ ਕਰੋ। ਇੱਕੋ ਮਾਡਲ, ਇੱਕੋ ਸਵਾਲ ਸੰਰਚਨਾ - ਪਰ ਪ੍ਰੌਂਪਟ ਦੱਸਦਾ ਹੈ ਕਿ ਕਿੰਨੀ ਗਹਿਰਾਈ ਨਾਲ ਸੋਚਣਾ ਹੈ।

### ਟਾਸਕ ਏਗਜ਼ਿਕਿਊਸ਼ਨ (ਟੂਲ ਪਿਊਰੈਂਬਲਸ)

ਬਹੁ-ਕਦਮੀ ਕਾਰਜਪ੍ਰਵਾਹ ਲਈ ਪਹਿਲਾਂ ਤੋਂ ਯੋਜਨਾ ਅਤੇ ਪ੍ਰਗਤਾਵਲੀ ਕਹਾਣੀ ਫਾਇਦੇਮੰਦ ਹੁੰਦੀ ਹੈ। ਮਾਡਲ ਦੱਸਦਾ ਹੈ ਕਿ ਉਹ ਕੀ ਕਰਨ ਵਾਲਾ ਹੈ, ਹਰ ਕਦਮ ਨੂੰ ਵਰਨਨ ਕਰਦਾ ਹੈ, ਫਿਰ ਨਤੀਜੇ ਸੰਖੇਪ ਕਰਦਾ ਹੈ।

### ਸਵ-ਅਵਲੋਕਨ ਕੋਡ

"Create an email validation service" ਅਜ਼ਮਾਓ। ਸਿਰਫ ਕੋਡ ਬਣਾਉਣ ਅਤੇ ਰੋਕਣ ਦੀ ਬਜਾਏ, ਮਾਡਲ ਜਨਰੇਟ ਕਰਦਾ ਹੈ, ਗੁਣਵੱਤਾ ਮਾਪਦੰਡਾਂ ਵਿਰੁੱਧ ਮੁਲਾਂਕਣ ਕਰਦਾ ਹੈ, ਕਮਜ਼ੋਰੀਆਂ ਦਰਸਾਉਂਦਾ ਹੈ ਅਤੇ ਸੁਧਾਰ ਕਰਦਾ ਹੈ। ਤੁਸੀਂ ਵੇਖੋਗੇ ਕਿ ਇਹ ਤਦ ਤੱਕ ਦੁਹਰਾਨਾ ਕਰਦਾ ਹੈ ਜਦ ਤੱਕ ਕੋਡ ਉਤਪਾਦਨ ਮਿਆਰਾਂ ਨੂੰ ਪੂਰਾ ਨਹੀਂ ਕਰਦਾ।

### ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ

ਕੋਡ ਸਮੀਖਿਆ ਲਈ ਸਥਿਰ ਮੁਲਾਂਕਣ ਫ੍ਰੇਮਵਰਕ ਦੀ ਲੋੜ ਹੁੰਦੀ ਹੈ। ਮਾਡਲ ਨਿਰਧਾਰਿਤ ਘਟਕੀਆਂ (ਸਹੀਤਾ, ਅਭਿਆਸ, ਪ੍ਰਦਰਸ਼ਨ, ਸੁਰੱਖਿਆ) ਨਾਲ ਕੋਡ ਦਾ ਵਿਸ਼ਲੇਸ਼ਣ ਕਰਦਾ ਹੈ ਸਾਥੀ ਗੰਭੀਰਤਾ ਪੱਧਰਾਂ ਦੇ।

### ਬਹੁ-ਮੁਹੰਮਤ ਵਾਲੀ ਗੱਲਬਾਤ

"ਸਤ੍ਰਿੰਗ ਬੂਟ ਕੀ ਹੈ?" ਪੁੱਛੋ, ਫਿਰ ਤੁਰੰਤ "ਮੈਨੂੰ ਇਕ ਉਦਾਹਰਣ ਦਿਖਾਓ" ਨਾਲ ਫਾਲੋਅਪ ਕਰੋ। ਮਾਡਲ ਤੁਹਾਡਾ ਪਹਿਲਾ ਸਵਾਲ ਯਾਦ ਰੱਖਦਾ ਹੈ ਅਤੇ ਤੁਹਾਨੂੰ ਸਪਸ਼ਟ ਤੌਰ 'ਤੇ ਸਤ੍ਰਿੰਗ ਬੂਟ ਦਾ ਉਦਾਹਰਣ ਦਿੰਦਾ ਹੈ। ਬਿਨਾ ਯਾਦਦਾਸ਼ਤ ਦੇ, ਦੂਜਾ ਸਵਾਲ ਬਹੁਤ ਅਸਪਸ਼ਟ ਹੁੰਦਾ।

### ਕਦਮ-ਦਰ-ਕਦਮ ਤਰਕ

ਕੋਈ ਗਣਿਤ ਸਮੱਸਿਆ ਚੁਣੋ ਅਤੇ ਦੋਹਾਂ ਕਦਮ-ਦਰ-ਕਦਮ ਤਰਕ ਅਤੇ ਘੱਟ Eagerness ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ। ਘੱਟ Eagerness ਸਿਰਫ਼ ਤੇਜ਼ ਜਵਾਬ ਦਿੰਦੀ ਹੈ ਪਰ ਸਪਸ਼ਟ ਨਹੀਂ ਹੁੰਦੀ। ਕਦਮ-ਦਰ-ਕਦਮ ਤੁਹਾਨੂੰ ਹਰ ਗਣਨਾ ਅਤੇ ਫੈਸਲਾ ਦਿਖਾਉਂਦੀ ਹੈ।

### ਸੀਮਿਤ ਨਿਕਾਸ

ਜਦੋਂ ਤੁਹਾਨੂੰ ਖ਼ਾਸ ਫਾਰਮੈਟ ਜਾਂ ਸ਼ਬਦ ਗਿਣਤੀ ਦੀ ਲੋੜ ਹੋਵੇ, ਇਹ ਪੈਟਰਨ ਸਖਤ ਪਾਲਣਾ ਲਾਗੂ ਕਰਦਾ ਹੈ। 100 ਸ਼ਬਦਾਂ ਵਿੱਚ ਬਿਲਕੁਲ ਇੱਕ ਸੰਖੇਪ ਬੁਲੇਟ ਪੌਇੰਟ ਫਾਰਮੈਟ ਵਿੱਚ ਬਣਾਉਣ ਦੀ ਕੋਸ਼ਿਸ਼ ਕਰੋ।

## ਤੁਸੀਂ ਜੋ ਸੱਚਮੁੱਚ ਸਿੱਖ ਰਹੇ ਹੋ

**ਤਰਕ ਦੀ ਕੋਸ਼ਿਸ਼ ਸਭ ਕੁਝ ਬਦਲ ਦਿੰਦੀ ਹੈ**

GPT-5.2 ਤੁਹਾਨੂੰ ਆਪਣੇ ਪ੍ਰੌਂਪਟਾਂ ਰਾਹੀਂ ਗਣਨਾਤਮਕ ਕੋਸ਼ਿਸ਼ ਨਿਯੰਤਰਿਤ ਕਰਨ ਦੀ ਆਗਿਆ ਦਿੰਦਾ ਹੈ। ਘੱਟ ਕੋਸ਼ਿਸ਼ ਦਾ ਮਤਲਬ ਤੇਜ਼ ਜਵਾਬ ਅਤੇ ਘੱਟ ਖੋਜ ਹੈ। ਵਧੀਕ ਕੋਸ਼ਿਸ਼ ਦਾ ਮਤਲਬ ਮਾਡਲ ਨੂੰ ਡੂੰਘਾ ਸੋਚਣ ਲਈ ਸਮਾਂ ਲੈਣਾ ਹੈ। ਤੁਸੀਂ ਸਿੱਖ ਰਹੇ ਹੋ ਕਿ ਕੋਸ਼ਿਸ਼ ਨੂੰ ਕੰਮ ਦੀ ਜਟਿਲਤਾ ਨਾਲ ਮੇਲ ਖਾਣਾ ਚਾਹੀਦਾ ਹੈ - ਸਧਾਰਣ ਸਵਾਲਾਂ ਉੱਤੇ ਸਮਾਂ ਬਰਬਾਦ ਨਾ ਕਰੋ, ਪਰ ਜਟਿਲ ਫੈਸਲਿਆਂ ਵਿੱਚ ਜਲਦੀ ਨਾ ਕਰੋ।

**ਸੰਰਚਨਾ ਰਵਿਆ ਦੇ ਰਾਹਦਾਰੀ ਕਰਦੀ ਹੈ**

ਕੀ ਤੁਸੀਂ ਪ੍ਰੌਂਪਟਾਂ ਵਿੱਚ XML ਟੈਗਾਂ ਨੂੰ ਧਿਆਨ ਵਿੱਚ ਲਿਆ? ਉਹ ਸਿਰਫ਼ ਸਜ਼ਾਵਟੀ ਨਹੀਂ ਹਨ। ਮਾਡਲ ਸੰਰਚਿਤ ਹੁਕਮਾਂ ਦਾ ਪਾਲਣ ਹੁੰਦਾ ਹੈ ਜਿਵੇਂ ਕਿ ਖੁੱਲ੍ਹੇ ਲਿਖਤ ਵਿੱਚ ਨਹੀਂ। ਜਦੋਂ ਤੁਹਾਨੂੰ ਬਹੁ-ਕਦਮੀ ਪ੍ਰਕਿਰਿਆਵਾਂ ਜਾਂ ਜਟਿਲ ਲੌਜਿਕ ਦੀ ਲੋੜ ਹੋਵੇ, ਸੰਰਚਨਾ ਮਾਡਲ ਨੂੰ ਇਹ ਯਾਦ ਰੱਖਣ ਵਿੱਚ ਮਦਦ ਕਰਦੀ ਹੈ ਕਿ ਉਹ ਕਿੱਥੇ ਹੈ ਅਤੇ ਅਗਲਾ ਕੀ ਕਰਨਾ ਹੈ।

<img src="../../../translated_images/pa/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*ਇੱਕ ਵਧੀਆਂ ਸੰਰਚਿਤ ਪ੍ਰੌਂਪਟ ਦੀ ਬਣਤਰ ਜਿਸ ਵਿੱਚ ਸਪਸ਼ਟ ਹਿੱਸੇ ਅਤੇ XML-ਸਟਾਈਲ ਸੰਗਠਨ ਹੈ*

**ਆਪਣੇ ਆਪ ਦਾ ਮੁਲਾਂਕਣ ਕਰਕੇ ਗੁਣਵੱਤਾ**

ਸਵ-ਅਵਲੋਕਨ ਵਾਲੇ ਪੈਟਰਨ ਗੁਣਵੱਤਾ ਮਾਪਦੰਡਾਂ ਨੂੰ ਸਪਸ਼ਟ ਬਣਾਉਣ ਰਾਹੀਂ ਕੰਮ ਕਰਦੇ ਹਨ। ਲੈਦੀ ਕਿਉਂਕਿ ਮਾਡਲ "ਸਹੀ ਕਰਦਾ ਹੈ" ਇਹ ਆਸ਼ਾ ਕਰਨ ਦੀ ਬਜਾਏ, ਤੁਸੀਂ ਇਸਨੂੰ ਸਪਸ਼ਟ ਦੱਸਦੇ ਹੋ ਕਿ "ਸਹੀ" ਦਾ ਕੀ ਮਤਲਬ ਹੈ: ਸਹੀ ਤਰਕ, ਤਰੁਟਿ ਸੰਭਾਲ, ਪ੍ਰਦਰਸ਼ਨ, ਸੁਰੱਖਿਆ। ਫਿਰ ਮਾਡਲ ਆਪਣੀ ਨਿਕਾਸ ਦਾ ਮੁਲਾਂਕਣ ਕਰਕੇ ਸੁਧਾਰ ਕਰ ਸਕਦਾ ਹੈ। ਇਹ ਕੋਡ ਜਨਰੇਸ਼ਨ ਨੂੰ ਇਕ ਲਾਟਰੀ ਤੋਂ ਪ੍ਰਕਿਰਿਆ ਬਣਾਉਂਦਾ ਹੈ।

**ਸੰਦਰਭ ਸੀਮਿਤ ਹੁੰਦਾ ਹੈ**

ਬਹੁ-ਮੁਹੰਮਤ ਗੱਲ-ਬਾਤ ਹਰ ਬੇਨਤੀ ਨਾਲ ਸੁਨੇਹਾ ਇਤਿਹਾਸ ਸ਼ਾਮਲ ਕਰਦੀ ਹੈ। ਪਰ ਇੱਕ ਸੀਮਾ ਹੁੰਦੀ ਹੈ - ਹਰ ਮਾਡਲ ਦਾ ਇੱਕ ਵੱਧ ਤੋਂ ਵੱਧ ਟੋਕਨ ਗਿਣਤੀ ਹੁੰਦਾ ਹੈ। ਗੱਲਬਾਤ ਵਧਦਿਆਂ, ਤੁਹਾਨੂੰ ਲੋੜੀਂਦਾ ਸੰਦਰਭ ਬਿਨਾ ਸੀਮਾ ਨੂੰ ਛੁਹਿੰਦਿਆਂ ਰੱਖਣ ਲਈ ਰਣਨੀਤੀਆਂ ਦੀ ਲੋੜ ਪਏਗੀ। ਇਹ ਮਾਡਿਊਲ ਤੁਹਾਨੂੰ ਯਾਦਦਾਸ਼ਤ ਦਾ ਕੰਮ ਕਰਨ ਦਾ ਤਰੀਕਾ ਦਿਖਾਉਂਦਾ ਹੈ; ਬਾਅਦ ਵਿੱਚ ਤੁਸੀਂ ਸਿੱਖੋਗੇ ਕਿ ਕਦੋਂ ਸੰਖੇਪ ਕਰਨਾ ਹੈ, ਕਦੋਂ ਭੁੱਲਣਾ ਹੈ, ਅਤੇ ਕਦੋਂ ਪ੍ਰਾਪਤ ਕਰਨਾ ਹੈ।

## ਅਗਲੇ ਕਦਮ

**ਅਗਲਾ ਮਾਡਿਊਲ:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**ਨੈਵੀਗੇਸ਼ਨ:** [← ਪਹਿਲਾਂ: ਮਾਡਿਊਲ 01 - ਪਹਿਚਾਣ](../01-introduction/README.md) | [ਮੁੱਖ ਪੰਨਾ](../README.md) | [ਅੱਗੇ: ਮਾਡਿਊਲ 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਵੀਕਿਰਣ**:
ਇਹ ਦਸਤਾਵੇਜ਼ [Co-op Translator](https://github.com/Azure/co-op-translator) ਏਆਈ ਅਨੁਵਾਦ ਸੇਵਾ ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦਿਤ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਕੋਸ਼ਿਸ਼ ਕਰਦੇ ਹਾਂ,ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਵਿੱਚ ਰੱਖੋ ਕਿ ਸਵੈਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸਹੀਤੀਆਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਹੀ ਅਧਿਕਾਰਤ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਅਹਿਮ ਜਾਣਕਾਰੀ ਲਈ, ਵਿਸ਼ੇਸ਼ਜ്ഞ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਤੋਂ ਪੈਦਾ ਹੋਣ ਵਾਲੀਆਂ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀਆਂ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆਵਾਂ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->