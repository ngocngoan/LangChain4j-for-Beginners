# Module 02: GPT-5.2 ਨਾਲ ਪ੍ਰੋੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ

## ਸੂਚੀ

- [ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ](../../../02-prompt-engineering)
- [ਪੂਰਵ-ਆਵਸ਼ਯਕਤਾਵਾਂ](../../../02-prompt-engineering)
- [ਪ੍ਰੋੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਨੂੰ ਸਮਝਣਾ](../../../02-prompt-engineering)
- [ਪ੍ਰੋੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਦੇ ਮੂਲ ਭਾਗ](../../../02-prompt-engineering)
  - [ਜ਼ੀਰੋ-ਸ਼ੌਟ ਪ੍ਰੋੰਪਟਿੰਗ](../../../02-prompt-engineering)
  - [ਘੱਟ-ਸ਼ੌਟ ਪ੍ਰੋੰਪਟਿੰਗ](../../../02-prompt-engineering)
  - [ਚੇਨ ਆਫ਼ ਥਾਟ](../../../02-prompt-engineering)
  - [ਰੋਲ-ਅਧਾਰਿਤ ਪ੍ਰੋੰਪਟਿੰਗ](../../../02-prompt-engineering)
  - [ਪ੍ਰੋੰਪਟ ਟੈਂਪਲੇਟ](../../../02-prompt-engineering)
- [ਅਡਵਾਂਸ ਪੈਟਰਨ](../../../02-prompt-engineering)
- [ਮੌਜੂਦਾ ਏਜ਼ਿਊਰ ਸੰਸਾਧਨਾਂ ਦੀ ਵਰਤੋਂ](../../../02-prompt-engineering)
- [ਐਪਲੀਕੇਸ਼ਨ ਸਕ੍ਰੀਨਸ਼ਾਟ](../../../02-prompt-engineering)
- [ਪੈਟਰਨਜ਼ ਦੀ ਖੋਜ](../../../02-prompt-engineering)
  - [ਘੱਟ ਵਿਰਸਾ ਬਨਾਮ ਵੱਧ ਵਰਸਾ](../../../02-prompt-engineering)
  - [ਟਾਸਕ ਐਕਸੀਕਿਊਸ਼ਨ (ਟੂਲ ਪ੍ਰੀਐਮਬਲ)](../../../02-prompt-engineering)
  - [ਸਵੈ-ਮਨਨ ਕੋਡ](../../../02-prompt-engineering)
  - [ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ](../../../02-prompt-engineering)
  - [ਮਲਟੀ-ਟਰਨ ਗੱਲਬਾਤ](../../../02-prompt-engineering)
  - [ਕਦਮ-ਦਰ-ਕਦਮ ਤਰਕ](../../../02-prompt-engineering)
  - [ਸੀਮਿਤ ਨਤੀਜਾ](../../../02-prompt-engineering)
- [ਤੁਸੀਂ ਅਸਲ ਵਿੱਚ ਕੀ ਸਿੱਖ ਰਹੇ ਹੋ](../../../02-prompt-engineering)
- [ਅਗਲੇ ਕਦਮ](../../../02-prompt-engineering)

## ਤੁਹਾਡਾ ਸਿੱਖਣਾ

<img src="../../../translated_images/pa/what-youll-learn.c68269ac048503b2.webp" alt="ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ" width="800"/>

ਪਿਛਲੇ ਮੌਡਿਊਲ ਵਿੱਚ, ਤੁਸੀਂ ਵੇਖਿਆ ਕਿ ਕਿਵੇਂ ਮੈਮੋਰੀ ਸੰਵਾਦਾਤਮਕ ਏਆਈ ਨੂੰ ਸਮਰੱਥ ਬਣਾਉਂਦੀ ਹੈ ਅਤੇ GitHub ਮਾਡਲਾਂ ਨਾਲ ਮੁਢਲੀ ਗੱਲਬਾਤ ਕੀਤੀ। ਹੁਣ ਅਸੀਂ ਧਿਆਨ ਦੇਵਾਂਗੇ ਕਿ ਤੁਸੀਂ ਸਵਾਲ ਕਿਵੇਂ ਪੁੱਛਦੇ ਹੋ — ਪ੍ਰੋੰਪਟ ਸਵੈ-ਆਪ — ਜੋ ਕਿ Azure OpenAI ਦੇ GPT-5.2 ਦੀ ਵਰਤੋਂ ਕਰਦੇ ਹਨ। ਤੁਹਾਡੇ ਪ੍ਰੋੰਪਟਾਂ ਦੀ ਸੰਰਚਨਾ ਜਵਾਬਾਂ ਦੀ ਗੁਣਵੱਤਾ ਨੂੰ ਬਹੁਤ ਪ੍ਰਭਾਵਿਤ ਕਰਦੀ ਹੈ। ਅਸੀਂ ਮੁਢਲੀ ਪ੍ਰੋੰਪਟਿੰਗ ਤਕਨੀਕਾਂ ਦੀ ਸਮੀਖਿਆ ਨਾਲ ਸ਼ੁਰੂ ਕਰਦੇ ਹਾਂ, ਫਿਰ ਅੱਠ ਅਡਵਾਂਸ ਪੈਟਰਨ ਵੱਲ ਵਧਦੇ ਹਾਂ ਜੋ GPT-5.2 ਦੀਆਂ ਸਮਰੱਥਾਵਾਂ ਦਾ ਪੂਰਾ ਲਾਭ ਲੈਂਦੇ ਹਨ।

ਅਸੀਂ GPT-5.2 ਦੀ ਵਰਤੋਂ ਕਰਾਂਗੇ ਕਿਉਂਕਿ ਇਹ ਤਰਕ ਨਿਯੰਤਰਣ ਲਿਆਉਂਦਾ ਹੈ — ਤੁਸੀਂ ਮਾਡਲ ਨੂੰ ਦੱਸ ਸਕਦੇ ਹੋ ਕਿ ਜਵਾਬ ਦੇਣ ਤੋਂ ਪਹਿਲਾਂ ਕਿੰਨੀ ਸੋਚਵਿਚਾਰ ਕਰਨੀ ਹੈ। ਇਸ ਨਾਲ ਵੱਖ-ਵੱਖ ਪ੍ਰੋੰਪਟਿੰਗ ਰਣਨੀਤੀਆਂ ਸਪਸ਼ਟ ਹੋ ਜਾਂਦੀਆਂ ਹਨ ਅਤੇ ਤੁਹਾਨੂੰ ਹਰ ਇਕ ਦ੍ਰਿਸ਼ਟੀਕੋਣ ਕਦੋਂ ਵਰਤਣਾ ਹੈ ਇਹ ਸਮਝ ਆਉਂਦਾ ਹੈ। ਅਸੀਂ Azure ਦੇ ਘੱਟ ਰੇਟ ਲਿਮਿਟਾਂ ਤੋਂ ਵੀ ਲਾਭ ਉਠਾਵਾਂਗੇ ਜਿਹੜੀਆਂ GPT-5.2 ਲਈ GitHub ਮਾਡਲਾਂ ਦੇ ਮੁਕਾਬਲੇ ਹਨ।

## ਪੂਰਵ-ਆਵਸ਼ਯਕਤਾਵਾਂ

- ਮੌਡਿਊਲ 01 ਮੁੱਕਿਆ ਹੋਇਆ (Azure OpenAI ਸੰਸਾਧਨ ਤैनਾਤ ਕੀਤੇ ਗਏ)
- ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ `.env` ਫਾਈਲ ਜਿਸ ਵਿੱਚ Azure ਪ੍ਰਮਾਣਕ ਤਥਾ (`azd up` ਮੌਡਿਊਲ 01 ਵਿੱਚ ਬਣਾਈ)

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਮੌਡਿਊਲ 01 ਮੁੱਕਿਆ ਨਹੀਂ, ਤਾਂ ਪਹਿਲਾਂ ਓਥੇ ਦਿੱਤੇ ਡਿਪਲોયਮੈਂਟ ਹਦਾਇਤਾਂ ਦੀ ਪਾਲਣਾ ਕਰੋ।

## ਪ੍ਰੋੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਨੂੰ ਸਮਝਣਾ

<img src="../../../translated_images/pa/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="ਪ੍ਰੋੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਕੀ ਹੈ?" width="800"/>

ਪ੍ਰੋੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਇਨਪੁਟ ਟੈਕਸਟ ਡਿਜ਼ਾਈਨ ਕਰਨ ਬਾਰੇ ਹੈ ਜੋ ਲਗਾਤਾਰ ਤੁਹਾਨੂੰ ਜ਼ਰੂਰੀ ਨਤੀਜੇ ਦਿੰਦਾ ਹੈ। ਇਹ ਸਿਰਫ ਸਵਾਲ ਪੁੱਛਣ ਬਾਰੇ ਨਹੀਂ — ਇਹ ਬੇਨਤੀ ਦੀ ਅਜਿਹੀ ਰਚਨਾ ਹੈ ਤਾਂ ਕਿ ਮਾਡਲ ਸਹੀ ਤਰੀਕੇ ਨਾਲ ਸਮਝੇ ਕਿ ਤੁਸੀਂ ਕੀ ਚਾਹੁੰਦੇ ਹੋ ਅਤੇ ਕਿਵੇਂ ਪ੍ਰਦਾਨ ਕਰਨਾ ਹੈ।

ਇਸ ਨੂੰ ਇੱਕ ਸਹਿਕਰਮੀ ਨੂੰ ਹੁਕਮ ਦੇਣ ਵਾਂਗ ਸੋਚੋ। "ਬੱਗ ਠੀਕ ਕਰੋ" ਬਹੁਤ ਮੂੜ੍ਹਾ ਹੁਕਮ ਹੈ। "UserService.java ਦੀ ਲਾਈਨ 45 ਵਿੱਚ ਨੱਲ ਪੌਇੰਟਰ ਐਕਸਪੇਪਸ਼ਨ ਨੂੰ ਨੱਲ ਚੈੱਕ ਜੁੜ ਕੇ ਠੀਕ ਕਰੋ" ਵਿਸ਼ੇਸ਼ ਹੁਕਮ ਹੈ। ਭਾਸ਼ਾ ਮਾਡਲ ਵੀ ਓਲੇ ਹੀ ਕੰਮ ਕਰਦੇ ਹਨ — ਵਿਸ਼ੇਸ਼ਤਾ ਅਤੇ ਸੰਰਚਨਾ ਬਹੁਤ ਮਹੱਤਵਪੂਰਣ ਹੈ।

<img src="../../../translated_images/pa/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j ਕਿਵੇਂ ਫਿੱਟ ਹੁੰਦਾ ਹੈ" width="800"/>

LangChain4j ਉਹ ਇੰਫਰਾਸਟਰੱਕਚਰ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ — ਮਾਡਲ ਕਨੈਕਸ਼ਨ, ਮੈਮੋਰੀ, ਅਤੇ ਸੁਨੇਹੇ ਕਿਸਮਾਂ — ਜਦਕਿ ਪ੍ਰੋੰਪਟ ਪੈਟਰਨਾਂ ਸਿਰਫ ਧਿਆਨ ਨਾਲ ਬਣਾਈ ਗਈ ਲਿਖਤੀ ਢਾਂਚਾ ਹੁੰਦੇ ਹਨ ਜੋ ਤੁਸੀਂ ਉਸ ਇੰਫਰਾਸਟਰੱਖਚਰ ਰਾਹੀਂ ਭੇਜਦੇ ਹੋ। ਮੁੱਖ ਬਲਾਕ ਹਨ `SystemMessage` (ਜੋ AI ਦਾ ਵਤੀਰਾ ਅਤੇ ਭੂਮਿਕਾ ਸੈੱਟ ਕਰਦਾ ਹੈ) ਅਤੇ `UserMessage` (ਜੋ ਤੁਹਾਡੀ ਅਸਲੀ ਬੇਨਤੀ ਲੈ ਕੇ ਜਾਂਦਾ ਹੈ)।

## ਪ੍ਰੋੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਦੇ ਮੂਲ ਭਾਗ

<img src="../../../translated_images/pa/five-patterns-overview.160f35045ffd2a94.webp" alt="ਪੰਜ ਪ੍ਰੋੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਪੈਟਰਨਾਂ ਦਾ ਓਵਰਵਿਊ" width="800"/>

ਇਸ ਮੌਡਿਊਲ ਵਿੱਚ ਅਡਵਾਂਸ ਪੈਟਰਨਾਂ ਵਿੱਚ ਜਾਣ ਤੋਂ ਪਹਿਲਾਂ, ਆਓ ਪੰਜ ਬੁਨਿਆਦੀ ਪ੍ਰੋੰਪਟਿੰਗ ਤਕਨੀਕਾਂ ਦੀ ਸਮੀਖਿਆ ਕਰੀਏ। ਇਹ ਉਹ ਮੂਲ ਢਾਂਚੇ ਹਨ ਜੋ ਹਰ ਪ੍ਰੋੰਪਟ ਇੰਜੀਨੀਅਰ ਨੂੰ ਜਾਣਣੇ ਚਾਹੀਦੇ ਹਨ। ਜੇ ਤੁਸੀਂ ਪਹਿਲਾਂ ਹੀ [ਕੁਇਕ ਸਟਾਰਟ ਮੌਡਿਊਲ](../00-quick-start/README.md#2-prompt-patterns) ਕਰ ਲਿਆ ਹੈ, ਤਾਂ ਤੁਸੀਂ ਇਹਨਾਂ ਨੂੰ ਕਾਰਵਾਈ ਵਿੱਚ ਵੇਖਿਆ ਹੈ — ਇੱਥੇ ਉਹਨਾਂ ਦਾ ਸੰਕਲਪਿਕ ਢਾਂਚਾ ਦਿੱਤਾ ਗਿਆ ਹੈ।

### ਜ਼ੀਰੋ-ਸ਼ੌਟ ਪ੍ਰੋੰਪਟਿੰਗ

ਸਭ ਤੋਂ ਸਿੱਧਾ ਤਰੀਕਾ: ਮਾਡਲ ਨੂੰ ਕੋਈ ਉਦਾਹਰਨ ਬਿਨਾਂ ਸਿੱਧਾ ਹੁਕਮ ਦੇਣਾ। ਮਾਡਲ ਪੂਰੀ ਤਰ੍ਹਾਂ ਆਪਣੀ ਟਰੇਨਿੰਗ 'ਤੇ ਨਿਰਭਰ ਕਰਦਾ ਹੈ ਕਿ ਟਾਸਕ ਨੂੰ ਸਮਝ ਕੇ ਕਰਨਾ ਹੈ। ਇਹ ਸਾਫ਼ ਟਾਸਕਾਂ ਲਈ ਚੰਗਾ ਕੰਮ ਕਰਦਾ ਹੈ ਜਿਥੇ ਉਮੀਦ ਵਾਲਾ ਤਰੀਕਾ ਸਪਸ਼ਟ ਹੁੰਦਾ ਹੈ।

<img src="../../../translated_images/pa/zero-shot-prompting.7abc24228be84e6c.webp" alt="ਜ਼ੀਰੋ-ਸ਼ੌਟ ਪ੍ਰੋੰਪਟਿੰਗ" width="800"/>

*ਬਿਨਾਂ ਉਦਾਹਰਨਾਂ ਦੇ ਸਿੱਧਾ ਹੁਕਮ — ਮਾਡਲ ਸਿਰਫ ਹੁਕਮ ਤੋਂ ਟਾਸਕ ਦਾ ਤਜਅਜ਼ੀ ਕਰਦਾ ਹੈ*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// ਜਵਾਬ: "ਸਕਾਰਾਤਮਕ"
```

**ਕਦੋਂ ਵਰਤਣਾ:** ਸਧਾਰਨ ਵਰਗੀਕਰਨ, ਸਿੱਧੇ ਸਵਾਲ, ਅਨੁਵਾਦ, ਜਾਂ ਕੋਈ ਵੀ ਟਾਸਕ ਜਿਸਦੀ ਮਾਡਲ ਵੱਲੋਂ ਵਾਧੂ ਮਦਦ ਦੀ ਲੋੜ ਨਹੀਂ।

### ਘੱਟ-ਸ਼ੌਟ ਪ੍ਰੋੰਪਟਿੰਗ

ਉਦਾਹਰਨਾਂ ਦਿਓ ਜੋ ਮਾਡਲ ਨੂੰ ਤੁਹਾਡੇ ਪ੍ਰਸੰਗ ਦਾ ਪੈਟਰਨ ਦਿਖਾਏ। ਮਾਡਲ ਇਨਪੁਟ-ਆਉਟਪੁੱਟ ਫਾਰਮੈਟ ਸਿੱਖਦਾ ਹੈ ਅਤੇ ਨਵੇਂ ਇਨਪੁਟਾਂ 'ਤੇ ਲਾਗੂ ਕਰਦਾ ਹੈ। ਇਹ ਉਸ ਟਾਸਕ ਲਈ ਵਿਸ਼ਲੇਸ਼ਣ ਨੂੰ ਬਹੁਤ ਬਿਹਤਰ ਬਣਾਉਂਦਾ ਹੈ ਜਿੱਥੇ ਮਨਚਾਹਾ ਫਾਰਮੈਟ ਜਾਂ ਵਰਤਾਵਾ ਸਪਸ਼ਟ ਨਹੀਂ ਹੁੰਦਾ।

<img src="../../../translated_images/pa/few-shot-prompting.9d9eace1da88989a.webp" alt="ਘੱਟ-ਸ਼ੌਟ ਪ੍ਰੋੰਪਟਿੰਗ" width="800"/>

*ਉਦਾਹਰਨਾਂ ਤੋਂ ਸਿੱਖਣਾ — ਮਾਡਲ ਪੈਟਰਨ ਪਛਾਣਦਾ ਹੈ ਅਤੇ ਨਵੇਂ ਇਨਪੁੱਟਾਂ ਤੇ ਲਾਗੂ ਕਰਦਾ ਹੈ*

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

**ਕਦੋਂ ਵਰਤਣਾ:** ਕਸਟਮ ਸ਼੍ਰੇਣੀਬੱਧਕਰਨ, ਇਕਸਾਰ ਫਾਰਮੈਟਿੰਗ, ਖੇਤਰ-ਵਿਸ਼ੇਸ਼ ਟਾਸਕ, ਜਦੋਂ ਜ਼ੀਰੋ-ਸ਼ੌਟ ਨਤੀਜੇ ਅਸਥਿਰ ਹੋਣ।

### ਚੇਨ ਆਫ਼ ਥਾਟ

ਮਾਡਲ ਨੂੰ ਕਦਮ-ਦਰ-कਦਮ ਆਪਣਾ ਤਰਕ ਦਰਸਾਉਣ ਲਈ ਕਹੋ। ਜਵਾਬ ਸਿੱਧਾ ਦੇਣ ਦੀ ਬਜਾਏ, ਮਾਡਲ ਸਮੱਸਿਆ ਨੂੰ ਵਿਭਾਜਿਤ ਕਰਦਾ ਹੈ ਅਤੇ ਹਰ ਹਿੱਸੇ 'ਤੇ ਖੁੱਲ੍ਹ ਕੇ ਕੰਮ ਕਰਦਾ ਹੈ। ਇਹ ਗਣਿਤ, ਤਰਕ ਅਤੇ ਬਹੁ-ਕਦਮੀ ਤਰਕ ਸ਼ਾਮਲ ਟਾਸਕਾਂ ਵਿੱਚ ਸਹੀ ਹੋਣੀ ਨੂੰ ਸੁਧਾਰਦਾ ਹੈ।

<img src="../../../translated_images/pa/chain-of-thought.5cff6630e2657e2a.webp" alt="ਚੇਨ ਆਫ਼ ਥਾਟ ਪ੍ਰੋੰਪਟਿੰਗ" width="800"/>

*ਕਦਮ-ਦਰ-ਕਦਮ ਤਰਕ — ਜਟਿਲ ਸਮੱਸਿਆਵਾਂ ਨੂੰ ਖੁੱਲ੍ਹੇ ਤਰਕਕ ਦੇ ਕਦਮਾਂ ਵਿੱਚ ਵੰਡਣਾ*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// ਮਾਡਲ ਦਿਖਾਉਂਦਾ ਹੈ: 15 - 8 = 7, ਫਿਰ 7 + 12 = 19 ਸੇਬ
```

**ਕਦੋਂ ਵਰਤਣਾ:** ਗਣਿਤ ਸਮੱਸਿਆਵਾਂ, ਤਰਕ ਦੇ ਪਹੇਲੀਆਂ, ਡੀਬੱਗਿੰਗ, ਜਾਂ ਕੋਈ ਵੀ ਟਾਸਕ ਜਿੱਥੇ ਤਰਕਕ ਦਰਸਾਉਣਾ ਸਹੀਅਤ ਅਤੇ ਭਰੋਸਾ ਵਧਾਉਂਦਾ ਹੈ।

### ਰੋਲ-ਅਧਾਰਿਤ ਪ੍ਰੋੰਪਟਿੰਗ

ਸਵਾਲ ਪੁੱਛਣ ਤੋਂ ਪਹਿਲਾਂ AI ਲਈ ਕੋਈ ਖਾਸ ਭੂਮਿਕਾ ਜਾਂ ਵਿਅਕਤੀਗਤ ਰੂਪ ਪੈਦਾ ਕਰੋ। ਇਸ ਨਾਲ ਸੰਦਰਭ ਬਣਦਾ ਹੈ ਜੋ ਜਵਾਬ ਦੀ ਲਹਿਜ਼ਾ, ਗਹਿਰਾਈ ਅਤੇ ਧਿਆਨ ਨੂੰ ਆਕਾਰ ਦਿੰਦਾ ਹੈ। "ਸਮਾਰਟ ਸਾਫਟਵੇਅਰ ਆਰਕੀਟੈਕਟ" "ਜੂਨੀਅਰ ਡਿਵੈਲਪਰ" ਜਾਂ "ਸੁਰੱਖਿਆ ਆਡੀਟਰ" ਦੀ ਤੁਲਨਾ ਵਿੱਚ ਵੱਖਰਾ ਸਲਾਹ ਦਿੰਦਾ ਹੈ।

<img src="../../../translated_images/pa/role-based-prompting.a806e1a73de6e3a4.webp" alt="ਰੋਲ-ਅਧਾਰਿਤ ਪ੍ਰੋੰਪਟਿੰਗ" width="800"/>

*ਸੰਦਰਭ ਅਤੇ ਭੂਮਿਕਾ ਸੈੱਟ ਕਰਨਾ — ਇੱਕੋ ਸਵਾਲ ਨੂੰ ਸੌਂਪੀ ਗਈ ਭੂਮਿਕਾ ਮੁਤਾਬਿਕ ਵੱਖਰੀ ਜਵਾਬ ਮਿਲਦੀ ਹੈ*

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

**ਕਦੋਂ ਵਰਤਣਾ:** ਕੋਡ ਸਮੀਖਿਆ, ਟਿਊਟਰਿੰਗ, ਖੇਤਰ-ਵਿਸ਼ੇਸ਼ ਵਿਸ਼ਲੇਸ਼ਣ, ਜਾਂ ਜਦੋਂ ਨੂੰ ਤੁਸੀਂ ਜਵਾਬ مخصوص ਮਾਹਰਤਾ ਦਰਜੇ ਜਾਂ ਦ੍ਰਿਸ਼ਟੀਕੋਣ ਲਈ ਚਾਹੁੰਦੇ ਹੋ।

### ਪ੍ਰੋੰਪਟ ਟੈਂਪਲੇਟ

ਚਲਦੀ ਲਿਖਾਈ ਲਈ ਪੁਨਰਵਰਤਨੀਯ ਪ੍ਰੋੰਪਟ ਬਣਾਓ ਜਿਸ ਵਿਚ ਵੈਰੀਅਬਲ ਪਲੇਸਹੋਲਡਰ ਹਨ। ਹਰ ਵਾਰੀ ਨਵਾਂ ਪ੍ਰੋੰਪਟ ਲਿਖਣ ਦੀ ਥਾਂ ਇੱਕ ਵਾਰੀ ਟੈਂਪਲੇਟ ਬਣਾਉ ਅਤੇ ਵੱਖ-ਵੱਖ ਮੁੱਲ ਭਰੋ। LangChain4j ਦੀ `PromptTemplate` ਕਲਾਸ ਇਹ `{{variable}}` ਸਿੰਟੈਗਸ ਨਾਲ ਆਸਾਨ ਕਰਦੀ ਹੈ।

<img src="../../../translated_images/pa/prompt-templates.14bfc37d45f1a933.webp" alt="ਪ੍ਰੋੰਪਟ ਟੈਂਪਲੇਟ" width="800"/>

*ਪੁਨਰਵਰਤਨੀਯ ਪ੍ਰੋੰਪਟ ਜਿਨ੍ਹਾਂ ਵਿੱਚ ਵੈਰੀਅਬਲ ਪਲੇਸਹੋਲਡਰ ਹੁੰਦੇ ਹਨ — ਇੱਕ ਟੈਂਪਲੇਟ, ਕਈ ਵਰਤੋ*

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

**ਕਦੋਂ ਵਰਤਣਾ:** ਵੱਖ-ਵੱਖ ਇਨਪੁੱਟਾਂ ਨਾਲ ਦੁਹਰਾਏ ਜਾਣ ਵਾਲੇ ਪ੍ਰਸ਼ਨ, ਬੈਚ ਪ੍ਰੋਸੈਸਿੰਗ, ਦੁਬਾਰਾ ਵਰਤੀ ਜਾ ਸਕਣ ਵਾਲੀ AI ਵਰਕਫਲੋ, ਜਾਂ ਕੋਈ ਵੀ ਸਥਿਤੀ ਜਿੱਥੇ ਪ੍ਰੋੰਪਟ ਦੀ ਸੰਰਚਨਾ ਇੱਕੋ ਰਹਿੰਦੀ ਹੋਵੇ ਪਰ ਡਾਟਾ ਬਦਲਦਾ ਰਹਿੰਦਾ ਹੋਵੇ।

---

ਇਹ ਪੰਜ ਮੁੱਢਲੇ ਹਿੱਸੇ ਤੁਹਾਨੂੰ ਜ਼ਿਆਦਾਤਰ ਪ੍ਰੋੰਪਟਿੰਗ ਟਾਸਕਾਂ ਲਈ ਮਜ਼ਬੂਤ ਟੂਲਕਿਟ ਦਿੰਦੇ ਹਨ। ਬਾਕੀ ਇਸ ਮੌਡਿਊਲ ਦਾ ਨਿਰਮਾਣ **ਅੱਠ ਅਡਵਾਂਸ ਪੈਟਰਨਾਂ** 'ਤੇ ਹੈ ਜੋ GPT-5.2 ਦੇ ਤਰਕ ਨਿਯੰਤਰਣ, ਸਵੈ-ਮੁਲਿਆਕੰਨ ਅਤੇ ਸੰਰਚਿਤ ਨਤੀਜਾ ਸਮਰੱਥਾਵਾਂ ਨੂੰ ਵਰਤਦੇ ਹਨ।

## ਅਡਵਾਂਸ ਪੈਟਰਨ

ਮੂਲ ਭਾਗਾਂ ਨੂੰ ਕਵਰ ਕਰਨ ਤੋਂ ਬਾਅਦ, ਚਲੋ ਅੱਠ ਅਡਵਾਂਸ ਪੈਟਰਨ ਵੱਲ ਵਧੀਏ ਜੋ ਇਸ ਮੌਡਿਊਲ ਨੂੰ ਵਿਲੱਖਣ ਬਣਾਉਂਦੇ ਹਨ। ਸਾਰੇ ਮੁੱਦੇ ਇਕੋ ਤਰੀਕੇ ਨਾਲ ਨਹੀਂ ਸੁਲਝਦੇ। ਕੁਝ ਸਵਾਲਾਂ ਨੂੰ ਤੇਜ਼ ਜਵਾਬ ਚਾਹੀਦਾ ਹੈ, ਕਈ ਨੂੰ ਡੂੰਘੀ ਸੋਚ। ਕੁਝ ਨੂੰ ਦਿਖਾਈ ਦੇਣ ਵਾਲਾ ਤਰਕ ਚਾਹੀਦਾ ਹੈ, ਦੂਜੇ ਸਿਰਫ ਨਤੀਜੇ। ਹਰੇਕ ਪੈਟਰਨ ਵੱਖਰੀ ਸਥਿਤੀ ਲਈ ਤਿਆਰ ਕੀਤਾ ਗਿਆ ਹੈ — ਅਤੇ GPT-5.2 ਦਾ ਤਰਕ ਨਿਯੰਤਰਣ ਅੰਤਰ ਨੂੰ ਹੋਰ ਧਿਆਨਯੋਗ ਬਣਾਉਂਦਾ ਹੈ।

<img src="../../../translated_images/pa/eight-patterns.fa1ebfdf16f71e9a.webp" alt="ਅੱਠ ਪ੍ਰੋੰਪਟਿੰਗ ਪੈਟਰਨ" width="800"/>

*ਅੱਠ ਪ੍ਰੋੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਪੈਟਰਨਾਂ ਦਾ ਓਵਰਵਿਊ ਅਤੇ ਉਨ੍ਹਾਂ ਦੇ ਵਰਤੋਂ ਦੇ ਕੇਸ*

<img src="../../../translated_images/pa/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 ਨਾਲ ਤਰਕ ਨਿਯੰਤਰਣ" width="800"/>

*GPT-5.2 ਦਾ ਤਰਕ ਨਿਯੰਤਰਣ ਤੁਹਾਨੂੰ ਦੱਸਦਾ ਹੈ ਕਿ ਮਾਡਲ ਨੂੰ ਕਿੰਨੀ ਸੋਚ ਕਰਨੀ ਚਾਹੀਦੀ ਹੈ — ਤੇਜ਼ ਸਿੱਧਾ ਜਵਾਬ ਤੋਂ ਲੈ ਕੇ ਡੂੰਘੀ ਛਾਣਬੀਣ ਤੱਕ*

**ਘੱਟ ਵਿਰਸਾ (ਤੇਜ਼ ਅਤੇ ਕੇਂਦਰਿਤ)** - ਸਧਾਰਨ ਸਵਾਲਾਂ ਲਈ ਜਿੱਥੇ ਤੁਸੀਂ ਤੇਜ਼ ਅਤੇ ਸਿੱਧੇ ਜਵਾਬ ਚਾਹੁੰਦੇ ਹੋ। ਮਾਡਲ ਘੱਟ ਤੋਂ ਘੱਟ ਤਰਕ ਕਰਦਾ ਹੈ - ਅਧਿਕਤਮ 2 ਕਦਮ। ਇਹ ਗਣਨਾ, ਲੁਕਅਪ, ਜਾਂ ਸਿੱਧੇ ਸਵਾਲਾਂ ਲਈ ਵਰਤੋ।

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

> 💡 **GitHub Copilot ਨਾਲ ਪ੍ਰਯੋਗ ਕਰੋ:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:
> - "ਘੱਟ ਵਿਰਸਾ ਅਤੇ ਵੱਧ ਵਿਰਸਾ ਪ੍ਰੋੰਪਟਿੰਗ ਪੈਟਰਨਾਂ ਵਿੱਚ ਕੀ ਫਰਕ ਹੈ?"
> - "ਪ੍ਰੋੰਪਟਾਂ ਵਿੱਚ XML ਟੈਗ AI ਦੇ ਜਵਾਬ ਨੂੰ ਸੰਰਚਿਤ ਕਰਨ ਵਿੱਚ ਕਿਵੇਂ ਮਦਦ ਕਰਦੇ ਹਨ?"
> - "ਮੈਂ ਸਵੈ-ਮਨਨ ਪੈਟਰਨ ਕਦੋਂ ਵਰਤਾਂ ਤੇ ਸਿੱਧੇ ਹੁਕਮ ਕਦੋਂ?"

**ਵਧੀਆ ਵਿਰਸਾ (ਘਿਣੀ ਤੇ ਡੂੰਘੀ)** - ਜਟਿਲ ਸਮੱਸਿਆਵਾਂ ਲਈ, ਜਿੱਥੇ ਤੁਸੀਂ ਵਿਸਤਾਰ ਨਾਲ ਵਿਸ਼ਲੇਸ਼ਣ ਚਾਹੁੰਦੇ ਹੋ। ਮਾਡਲ ਵਧੀਆ ਤਰ੍ਹਾਂ ਖੰਗਾਲਦਾ ਹੈ ਅਤੇ ਵਿਸਤਾਰ ਵਿੱਚ ਸੋਚ ਦਰਸਾਉਂਦਾ ਹੈ। ਇਸ ਨੂੰ ਸਿਸਟਮ ਡਿਜ਼ਾਇਨ, ਆਰਕੀਟੈਕਚਰ ਫੈਸਲੇ, ਜਾਂ ਜਟਿਲ ਅਨੁਸੰਧਾਨ ਲਈ ਵਰਤੋ।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**ਟਾਸਕ ਐਕਸੀਕਿਊਸ਼ਨ (ਕਦਮ-ਦਰ-ਕਦਮ ਪ੍ਰਗਟੀ)** - ਬਹੁ-ਕਦਮੀ ਵਰਕਫਲੋ ਲਈ। ਮਾਡਲ ਪਹਿਲਾਂ ਯੋਜਨਾ ਦਿੰਦਾ ਹੈ, ਹਰ ਕਦਮ ਨੂੰ ਕੰਮ ਕਰਦਿਆਂ ਦੱਸਦਾ ਹੈ, ਫਿਰ ਸਾਰ ਸੰਗ੍ਰਹਿਤ ਕਰਦਾ ਹੈ। ਇਹ ਮਾਈਗ੍ਰੇਸ਼ਨ, ਲਾਗੂ ਕਰਨ ਜਾਂ ਕਿਸੇ ਵੀ ਬਹੁ-ਕਦਮੀ ਪ੍ਰਕਿਰਿਆ ਲਈ ਵਰਤੋ।

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

ਚੇਨ-ਆਫ਼-ਥਾਟ ਪ੍ਰੋੰਪਟਿੰਗ ਖੁੱਲ੍ਹ ਕੇ ਤਰਕਕ ਦਿਖਾਉਣ ਲਈ ਮਾਡਲ ਨੂੰ ਕਹਿੰਦਾ ਹੈ, ਜੋ ਕਿ ਜਟਿਲ ਟਾਸਕਾਂ ਦੇ ਲਈ ਸਹੀਅਤ ਨੂੰ ਬਿਹਤਰ ਬਣਾਉਂਦਾ ਹੈ। ਕਦਮਾਂ ਦਾ ਤਕਸੀਮ ਮਨੁੱਖਾਂ ਅਤੇ AI ਦੋਹਾਂ ਨੂੰ ਤਰਕ ਸਮਝਣ ਵਿੱਚ ਮਦਦ ਕਰਦਾ ਹੈ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ਚੈਟ ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** ਇਸ ਪੈਟਰਨ ਬਾਰੇ ਪੁੱਛੋ:
> - "ਲੰਬੇ ਸਮੇਂ ਚੱਲਣ ਵਾਲੇ ਓਪਰੇਸ਼ਨਾਂ ਲਈ ਟਾਸਕ ਐਕਸੀਕਿਊਸ਼ਨ ਪੈਟਰਨ ਨੂੰ ਕਿਵੇਂ ਅਨੁਕੂਲਿਤ ਕਰਾਂ?"
> - "ਉਤਪਾਦਨ ਐਪਲੀਕੇਸ਼ਨਾਂ ਵਿੱਚ ਟੂਲ ਪ੍ਰੀਐਮਬਲ ਨੂੰ ਸੰਰਚਿਤ ਕਰਨ ਲਈ ਵਧੀਆ ਅਭਿਆਸ ਕੀ ਹਨ?"
> - "UI ਵਿੱਚ ਵਿਚਕਾਰਲੇ ਪ੍ਰਗਟਿਤੀਆਂ ਅਪਡੇਟ ਕੈਪਚਰ ਅਤੇ ਦਿਖਾਉਣ ਦਾ ਸਭ ਤੋਂ ਵਧੀਆ ਤਰੀਕਾ ਕੀ ਹੈ?"

<img src="../../../translated_images/pa/task-execution-pattern.9da3967750ab5c1e.webp" alt="ਟਾਸਕ ਐਕਸੀਕਿਊਸ਼ਨ ਪੈਟਰਨ" width="800"/>

*ਯੋਜਨਾ → ਅਮਲ → ਸਾਰ ਸੰਖੇਪ ਬਹੁ-ਕਦਮੀ ਟਾਸਕਾਂ ਲਈ ਵਰਕਫਲੋ*

**ਸਵੈ-ਮਨਨ ਕਰਦਾ ਕੋਡ** - ਪ੍ਰੋਡਕਸ਼ਨ-ਗੁਣਵੱਤਾ ਵਾਲਾ ਕੋਡ ਤਿਆਰ ਕਰਨ ਲਈ। ਮਾਡਲ ਉਤਪਾਦਨ ਦੇ ਮਿਆਰਾਂ ਦੇ ਨਾਲ ਕੋਡ ਬਣਾਉਂਦਾ ਹੈ ਜਿਸ ਵਿੱਚ ਠੀਕ ਏਰਰ ਹੈਂਡਲਿੰਗ ਹੁੰਦੀ ਹੈ। ਇਹ ਨਵੀਂ ਫੀਚਰਾਂ ਜਾਂ ਸੇਵਾਵਾਂ ਬਣਾਉਣ ਲਈ ਵਰਤਿਆ ਜਾਵੇ।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pa/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="ਸਵੈ-ਮਨਨ ਚੱਕਰ" width="800"/>

*ਦੌਰਾਨਾ ਸੁਧਾਰ - ਬਣਾਉਣਾ, ਮੁੱਲਾਂਕਣ, ਸਮੱਸਿਆਵਾਂ ਦੀ ਪਛਾਣ, ਸੁਧਾਰ, ਦੁਹਰਾਉ*

**ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ** - ਲਗਾਤਾਰ ਮੁਲਾਂਕਣ ਲਈ। ਮਾਡਲ ਕੋਡ ਦੀ ਸਕੀਮ ਦੀ ਪਾਲਣਾ ਕਰਦਾ ਹੈ (ਸਹੀਅਤ, ਰੀਤੀ, ਕਾਰਗੁਜ਼ਾਰੀ, ਸੁਰੱਖਿਆ, ਰੱਖ-ਰਖਾਵ)। ਇਹ ਕੋਡ ਸਮੀਖਿਆ ਜਾਂ ਗੁਣਵੱਤਾ ਮੁਲਾਂਕਣ ਲਈ ਵਰਤਿਆ ਜਾਂਦਾ ਹੈ।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ਚੈਟ ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ ਬਾਰੇ ਪੁੱਛੋ:
> - "ਮੁੱਖ ਕਿਸਮ ਦੇ ਕੋਡ ਸਮੀਖਿਆ ਲਈ ਵਿਸ਼ਲੇਸ਼ਣ ਫਰੇਮਵਰਕ ਨੂੰ ਕਿਵੇਂ ਅਨੁਕੂਲਿਤ ਕਰਾਂ?"
> - "ਕਿਵੇਂ ਮੈਂ ਪ੍ਰੋਗਰਾਮੈਟਿਕ ਤਰੀਕੇ ਨਾਲ ਸੰਰਚਿਤ ਨਤੀਜੇ ਨੂੰ ਪਾਰਸ ਅਤੇ ਕਾਰਵਾਈ ਲਈ ਸੁਰੱਖਿਅਤ ਕਰ ਸਕਦਾ ਹਾਂ?"
> - "ਵੱਖ-ਵੱਖ ਸਮੀਖਿਆ ਸੈਸ਼ਨਾਂ ਵਿੱਚ ਅਣੁਕੂਲ ਭਾਰੀਪਣ ਸਤਰ ਸਥਿਰ ਕਰਨ ਲਈ ਕੀ ਤਰੀਕੇ ਹਨ?"

<img src="../../../translated_images/pa/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ ਪੈਟਰਨ" width="800"/>

*ਭਾਰੀਪਣ ਸਤਰਾਂ ਦੇ ਨਾਲ ਲਗਾਤਾਰ ਕੋਡ ਸਮੀਖਿਆ ਲਈ ਫਰੇਮਵਰਕ*

**ਮਲਟੀ-ਟਰਨ ਗੱਲਬਾਤ** - ਜੇ ਗੱਲਬਾਤ ਨੂੰ ਸੰਦਰਭ ਦੀ ਲੋੜ ਹੋਵੇ। ਮਾਡਲ ਪਹਿਲੇ ਸੁਨੇਹਿਆਂ ਨੂੰ ਯਾਦ ਰੱਖਦਾ ਹੈ ਅਤੇ ਉਨ੍ਹਾਂ 'ਤੇ ਅਧਾਰਿਤ ਹੋ ਕੇ ਗੱਲ ਕਰਦਾ ਹੈ। ਇਹ ਇੰਟਰਐਕਟਿਵ ਸਹਾਇਤਾ ਸੈਸ਼ਨਾਂ ਜਾਂ ਜਟਿਲ ਪ੍ਰਸ਼ਨ-ਜਵਾਬ ਲਈ ਹੈ।

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/pa/context-memory.dff30ad9fa78832a.webp" alt="ਸੰਦਰਭ ਮੈਮੋਰੀ" width="800"/>

*ਕਿਵੇਂ ਗੱਲਬਾਤ ਦਾ ਸੰਦਰਭ ਕਈ ਵਾਰਾਂ ਤੇ ਬਰਤਰਫ ਹੁੰਦਾ ਹੈ ਜੋ ਟੋਕਨ ਸੀਮਾ ਤੱਕ ਪੁੱਜਦਾ ਹੈ*

**ਕਦਮ-ਦਰ-ਕਦਮ ਤਰਕ** - ਜੇ ਤਰਕਕ ਵਿਖਾਉਣ ਵਾਲੀ ਲਾਜ਼ਮੀ ਹੋਵੇ। ਮਾਡਲ ਹਰ ਕਦਮ ਲਈ ਖੁੱਲ੍ਹਾ ਤਰਕ ਦਿਖਾਉਂਦਾ ਹੈ। ਇਹ ਗਣਿਤ ਸਮੱਸਿਆਵਾਂ, ਤਰਕ ਦੀਆਂ ਪਹੇਲੀਆਂ ਜਾਂ ਸੋਚਣ ਦੀ ਪ੍ਰਕਿਰਿਆ ਸਮਝਣ ਲਈ ਵਰਤੋ।

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

*ਸਮੱਸਿਆਵਾਂ ਨੂੰ ਖੁੱਲ੍ਹੇ ਤਰਕਕ ਕਦਮਾਂ ਵਿੱਚ ਵੰਡਣਾ*

**ਸੀਮਿਤ ਨਤੀਜਾ** - ਜਵਾਬਾਂ ਦੀ ਖਾਸ ਫਾਰਮੈਟ ਲੋੜ ਲਈ। ਮਾਡਲ ਸਖ਼ਤੀ ਨਾਲ ਫਾਰਮੈਟ ਅਤੇ ਲੰਬਾਈ ਦੇ ਨਿਯਮਾਂ ਦੀ ਪਾਲਣਾ ਕਰਦਾ ਹੈ। ਇਹ ਸਾਰ-ਸੰਖੇਪ ਜਾਂ ਜਦੋਂ ਤੁਹਾਨੂੰ ਨਤੀਜੇ ਸੰਰਚਨਾ ਬਹੁਤ ਸਹੀ ਚਾਹੀਦੀ ਹੋਵੇ, ਲਈ ਵਰਤੋ।

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

<img src="../../../translated_images/pa/constrained-output-pattern.0ce39a682a6795c2.webp" alt="ਸੀਮਿਤ ਨਤੀਜਾ ਪੈਟਰਨ" width="800"/>

*ਖਾਸ ਫਾਰਮੈਟ, ਲੰਬਾਈ ਅਤੇ ਸੰਰਚਨਾ ਦੀਆਂ ਲੋੜਾਂ ਲਾਗੂ ਕਰਨਾ*

## ਮੌਜੂਦਾ ਏਜ਼ਿਊਰ ਸੰਸਾਧਨਾਂ ਦੀ ਵਰਤੋਂ

**ਡਿਪਲੋਇਮੈਂਟ ਦੀ ਪੁਸ਼ਟੀ ਕਰੋ:**

ਸੁਨਿਸ਼ਚਿਤ ਕਰੋ ਕਿ ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ `.env` ਫਾਈਲ ਹੈ ਜਿਸ ਵਿੱਚ Azure ਪ੍ਰਮਾਣਕ ਹਨ (ਜੋ ਮੌਡਿਊਲ 01 ਵਿੱਚ ਬਣਾਈ ਗਈ ਹੈ):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ਦਿਖਾਉਣਾ ਚਾਹੀਦਾ ਹੈ
```

**ਐਪਲੀਕੇਸ਼ਨ ਸ਼ੁਰੂ ਕਰੋ:**

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਪਹਿਲਾਂ ਹੀ ਸਾਰੇ ਐਪਲੀਕੇਸ਼ਨ `./start-all.sh` ਨਾਲ ਮੌਡਿਊਲ 01 ਵਿੱਚ ਚਲਾ ਚੁੱਕੇ ਹੋ, ਤਾਂ ਇਹ ਮੌਡਿਊਲ ਪਹਿਲਾਂ ਹੀ ਪੋਰਟ 8083 ਤੇ ਚੱਲ ਰਿਹਾ ਹੈ। ਤੁਸੀਂ ਹੇਠਾਂ ਦਿੱਤੇ ਸ਼ੁਰੂ ਆਦੇਸ਼ਾਂ ਨੂੰ ਛੱਡ ਕੇ ਸਿੱਧਾ http://localhost:8083 ਤੇ ਜਾ ਸਕਦੇ ਹੋ।

**ਵਿਕਲਪ 1: ਸਪ੍ਰਿੰਗ ਬੂਟ ਡੈਸ਼ਬੋਰਡ ਦੀ ਵਰਤੋਂ (VS ਕੋਡ ਉਪਭੋਗਤਾਵਾਂ ਲਈ ਸਿਫਾਰਸ਼ ਕੀਤੀ)**

ਡੈਵ ਕਨਟੇਨਰ ਵਿੱਚ ਸਪ੍ਰਿੰਗ ਬੂਟ ਡੈਸ਼ਬੋਰਡ ਐਕਸਟੈਂਸ਼ਨ ਸ਼ਾਮਲ ਹੈ, ਜੋ ਸਾਰੇ ਸਪ੍ਰਿੰਗ ਬੂਟ ਐਪਲੀਕੇਸ਼ਨਾਂ ਦਾ ਪ੍ਰਬੰਧਨ ਕਰਨ ਲਈ ਇੱਕ ਵਿਜ਼ੂਅਲ ਇੰਟਰਫੇਸ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ। ਤੁਸੀਂ ਇਸਨੂੰ VS ਕੋਡ ਦੇ ਖੱਬੇ ਪਾਸੇ ਐਕਟੀਵਿਟੀ ਬਾਰ ਵਿੱਚ ਸਪ੍ਰਿੰਗ ਬੂਟ ਆਈਕਨ ਨਾਲ ਲੱਭ ਸਕਦੇ ਹੋ।

ਸਪ੍ਰਿੰਗ ਬੂਟ ਡੈਸ਼ਬੋਰਡ ਤੋਂ ਤੁਸੀਂ:
- ਵਰਕਸਪੇਸ ਵਿੱਚ ਮੌਜੂਦ ਸਾਰੇ ਸਪ੍ਰਿੰਗ ਬੂਟ ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਵੇਖ ਸਕਦੇ ਹੋ
- ਇੱਕ ਕਲਿੱਕ ਨਾਲ ਐਪਲੀਕੇਸ਼ਨ ਸਟਾਰਟ/ਸਟਾਪ ਕਰ ਸਕਦੇ ਹੋ
- ਰੀਅਲ-ਟਾਈਮ ਵਿੱਚ ਐਪਲੀਕੇਸ਼ਨ ਲਾਗ ਤੱਕ ਪਹੁੰਚ ਸਕਦੇ ਹੋ
- ਐਪਲੀਕੇਸ਼ਨ ਦੀ ਸਥਿਤੀ ਦੀ ਨਿਗਰਾਨੀ ਕਰ ਸਕਦੇ ਹੋ
ਸਿਰਫ "prompt-engineering" ਦੇ ਨੇੜੇ ਪਲੇ ਬਟਨ 'ਤੇ ਕਲਿੱਕ ਕਰੋ ਇਸ ਮੌਡੀਊਲ ਨੂੰ ਸ਼ੁਰੂ ਕਰਨ ਲਈ, ਜਾਂ ਸਾਰੇ ਮੌਡੀਊਲ ਇਕੱਠੇ ਸ਼ੁਰੂ ਕਰੋ।

<img src="../../../translated_images/pa/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot ਡੈਸ਼ਬੋਰਡ" width="400"/>

**ਵਿਕਲਪ 2: shell ਸਕ੍ਰਿਪਟਾਂ ਦੀ ਵਰਤੋਂ ਕਰੋ**

ਸਾਰੇ ਵੈੱਬ ਐਪਲੀਕੇਸ਼ਨ (ਮੌਡੀਊਲ 01-04) ਸ਼ੁਰੂ ਕਰੋ:

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

ਜਾਂ ਸਿਰਫ ਇਸ ਮੌਡੀਊਲ ਨੂੰ ਸ਼ੁਰੂ ਕਰੋ:

**ਬੈਸ਼:**
```bash
cd 02-prompt-engineering
./start.sh
```

**ਪਾਵਰਸ਼ੈੱਲ:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

ਦੋਹਾਂ ਸਕ੍ਰਿਪਟਾਂ ਆਪਣੇ ਆਪ ਰੂਟ `.env` ਫਾਇਲ ਤੋਂ ਵਾਤਾਵਰਣ ਵੈਰੀਏਬਲ ਲੋਡ ਕਰਦੇ ਹਨ ਅਤੇ ਜੇ JAR ਮੌਜੂਦ ਨਹੀਂ ਹਨ ਤਾਂ ਉਹਨਾਂ ਨੂੰ ਬਣਾਉਂਦੇ ਹਨ।

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਸਾਰੇ ਮੌਡੀਊਲ ਨੂੰ ਮੈਨੁਅਲੀ ਤੌਰ 'ਤੇ ਬਣਾਉਣਾ ਪਸੰਦ ਕਰਦੇ ਹੋ ਸ਼ੁਰੂ ਕਰਨ ਤੋਂ ਪਹਿਲਾਂ:
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

http://localhost:8083 ਨੂੰ ਆਪਣੇ ਬ੍ਰਾਊਜ਼ਰ ਵਿੱਚ ਖੋਲ੍ਹੋ।

**ਰੋਕਣ ਲਈ:**

**ਬੈਸ਼:**
```bash
./stop.sh  # ਇਹ ਮਾਡਿਊਲ ਹੀ
# ਜਾਂ
cd .. && ./stop-all.sh  # ਸਾਰੇ ਮਾਡਿਊਲਾਂ
```

**ਪਾਵਰਸ਼ੈੱਲ:**
```powershell
.\stop.ps1  # ਸਿਰਫ ਇਹ ਮਾਡਿਊਲ
# ਜਾਂ
cd ..; .\stop-all.ps1  # ਸਾਰੇ ਮਾਡਿਊਲ
```


## ਐਪਲੀਕੇਸ਼ਨ ਸਕ੍ਰੀਨਸ਼ਾਟਸ

<img src="../../../translated_images/pa/dashboard-home.5444dbda4bc1f79d.webp" alt="ਡੈਸ਼ਬੋਰਡ ਘਰ" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*ਮੁੱਖ ਡੈਸ਼ਬੋਰਡ ਜੋ ਸਾਰੇ 8 ਪ੍ਰੋੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਪੈਟਰਨਾਂ ਨੂੰ ਉਹਨਾਂ ਦੀਆਂ ਵਿਸ਼ੇਸ਼ਤਾਵਾਂ ਅਤੇ ਵਰਤੋਂ ਦੇ ਕੇਸਾਂ ਦੇ ਨਾਲ ਦਿਖਾਂਦਾ ਹੈ*

## ਪੈਟਰਨਾਂ ਦੀ ਖੋਜ

ਵੈੱਬ ਇੰਟਰਫੇਸ ਤੁਹਾਨੂੰ ਵੱਖ-ਵੱਖ ਪ੍ਰੋੰਪਟਿੰਗ ਰਣਨੀਤੀਆਂ ਨਾਲ ਪ੍ਰਯੋਗ ਕਰਨ ਦੀ ਆਗਿਆ ਦਿੰਦਾ ਹੈ। ਹਰ ਪੈਟਰਨ ਵੱਖ-ਵੱਖ ਸਮੱਸਿਆਵਾਂ ਨੂੰ हल ਕਰਦਾ ਹੈ - ਉਹਨਾਂ ਨੂੰ ਅਜ਼ਮਾਉ ਅਤੇ ਵੇਖੋ ਕਿ ਕਦੋਂ ਹਰ ਵਿਧੀ ਚਮਕਦੀ ਹੈ।

### ਘੱਟ ਸਮਰੱਥਾ ਵਹਿ ਉੱਚ ਸਮਰੱਥਾ

ਘੱਟ ਸਮਰੱਥਾ ਵਰਤ ਕੇ ਕੋਈ ਸਿੱਧਾ ਪ੍ਰਸ਼ਨ ਪੁੱਛੋ ਜਿਵੇਂ "200 ਦਾ 15% ਕੀ ਹੈ?" ਤੁਹਾਨੂੰ ਤੁਰੰਤ, ਸਿੱਧਾ ਜਵਾਬ ਮਿਲੇਗਾ। ਹੁਣ ਉੱਚ ਸਮਰੱਥਾ ਵਰਤ ਕੇ ਕੁਝ ਜਟਿਲ ਪੁੱਛੋ ਜਿਵੇਂ "ਹਾਈ ਟ੍ਰੈਫਿਕ API ਲਈ ਕੈਸ਼ਿੰਗ ਰਣਨੀਤੀ ਤਿਆਰ ਕਰੋ"। ਦੇਖੋ ਕਿਸ ਤਰ੍ਹਾਂ ਮਾਡਲ ਹੌਲੀ ਕਰਦਾ ਹੈ ਅਤੇ ਵਿਸਥਾਰ ਨਾਲ ਤਰਕ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ। ਇੱਕੋ ਮਾਡਲ, ਇੱਕੋ ਪ੍ਰਸ਼ਨ ਢਾਂਚਾ - ਪਰ ਪ੍ਰੋੰਪਟ ਦੱਸਦਾ ਹੈ ਕਿ ਕਿੰਨੀ ਸੋਚ ਕਰਨੀ ਹੈ।

<img src="../../../translated_images/pa/low-eagerness-demo.898894591fb23aa0.webp" alt="ਘੱਟ ਸਮਰੱਥਾ ਡੈਮੋ" width="800"/>

*ਘੱਟ ਤਰਕ ਨਾਲ ਤੇਜ਼ ਗਣਨਾ*

<img src="../../../translated_images/pa/high-eagerness-demo.4ac93e7786c5a376.webp" alt="ਉੱਚ ਸਮਰੱਥਾ ਡੈਮੋ" width="800"/>

*ਵਿਆਪਕ ਕੈਸ਼ਿੰਗ ਰਣਨੀਤੀ (2.8MB)*

### ਟਾਸਕ ਐਕਸਿਕਿਊਸ਼ਨ (ਟੂਲ ਪ੍ਰੀਏਮਬਲ)

ਬਹੁ-ਕਦਮੀ ਕੰਮਾਂ ਲਈ ਪਹਿਲਾਂ ਦੀ ਯੋਜਨਾ ਅਤੇ ਪ੍ਰਗਤੀ ਵਰਨਨ ਲਾਭਦਾਇਕ ਹੁੰਦਾ ਹੈ। ਮਾਡਲ ਦੱਸਦਾ ਹੈ ਕਿ ਉਹ ਕੀ ਕਰੇਗਾ, ਹਰ ਕਦਮ ਦਾ ਵਰਣਨ ਕਰਦਾ ਹੈ, ਫਿਰ ਨਤੀਜੇ ਸੰਖੇਪ ਕਰਦਾ ਹੈ।

<img src="../../../translated_images/pa/tool-preambles-demo.3ca4881e417f2e28.webp" alt="ਟਾਸਕ ਐਕਸਿਕਿਊਸ਼ਨ ਡੈਮੋ" width="800"/>

*ਕਦਮ-ਦਰ-ਕਦਮ ਵਰਣਨ ਨਾਲ REST ਐੰਡਪੋਇੰਟ ਬਣਾਉਣਾ (3.9MB)*

### ਸਵੈ-ਮੁਲਾਂਕਣ ਵਾਲਾ ਕੋਡ

“ਇੱਕ ਈਮੇਲ ਵੈਰੀਫਿਕੇਸ਼ਨ ਸਰਵਿਸ ਬਣਾਓ” ਕੋਸ਼ਿਸ਼ ਕਰੋ। ਸਿਰਫ ਕੋਡ ਬਣਾਉਣ ਅਤੇ ਰੁਕਣ ਦੀ ਬਜਾਏ, ਮਾਡਲ ਬਣਾਉਂਦਾ ਹੈ, ਗੁਣਵੱਤਾ ਮਾਪਦੰਡਾਂ ਖਿਲਾਫ ਮੁਲਾਂਕਣ ਕਰਦਾ ਹੈ, ਕਮਜ਼ੋਰੀਆਂ ਪਛਾਣਦਾ ਹੈ ਅਤੇ ਸੁਧਾਰ ਕਰਦਾ ਹੈ। ਤੁਸੀਂ ਦੇਖੋਗੇ ਕਿ ਇਹ ਕੰਮ ਤਦ ਤਕ ਦੁਹਰਾਉਂਦਾ ਰਹਿੰਦਾ ਹੈ ਜਦ ਤਕ ਕੋਡ ਉਤਪਾਦਨ ਮਿਆਰਾਂ ਨੂੰ ਪੂਰਾ ਨਹੀਂ ਕਰਦਾ।

<img src="../../../translated_images/pa/self-reflecting-code-demo.851ee05c988e743f.webp" alt="ਸਵੈ-ਮੁਲਾਂਕਣ ਵਾਲਾ ਕੋਡ ਡੈਮੋ" width="800"/>

*ਪੂਰਨ ਈਮੇਲ ਵੈਰੀਫਿਕੇਸ਼ਨ ਸਰਵਿਸ (5.2MB)*

### ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ

ਕੋਡ ਸਮੀਖਿਆ ਲਈ ਸਥਿਰ ਮੁਲਾਂਕਣ ਫਰੇਮਵਰਕਾਂ ਦੀ ਲੋੜ ਹੁੰਦੀ ਹੈ। ਮਾਡਲ ਕੋਡ ਨੂੰ ਨਿਰਧਾਰਿਤ ਵਰਗਾਂ (ਸਹੀਪਣ, ਅਮਲ, ਪ੍ਰਦਰਸ਼ਨ, ਸੁਰੱਖਿਆ) ਅਤੇ ਗੰਭੀਰਤਾ ਪੱਧਰਾਂ ਨਾਲ ਵਿਸ਼ਲੇਸ਼ਣ ਕਰਦਾ ਹੈ।

<img src="../../../translated_images/pa/structured-analysis-demo.9ef892194cd23bc8.webp" alt="ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ ਡੈਮੋ" width="800"/>

*ਫਰੇਮਵਰਕ-ਆਧਾਰਤ ਕੋਡ ਸਮੀਖਿਆ*

### ਬਹੁ-ਚਰਣ ਚੈਟ

"Spring Boot ਕੀ ਹੈ?" ਪੁੱਛੋ ਫਿਰ turੰਤ "ਮੈਨੂੰ ਇੱਕ ਉਦਾਹਰਨ ਦਿਖਾਓ" ਪੁੱਛੋ। ਮਾਡਲ ਤੁਹਾਡੇ ਪਹਿਲੇ ਸਵਾਲ ਨੂੰ ਯਾਦ ਰੱਖਦਾ ਹੈ ਅਤੇ ਤੁਹਾਨੂੰ ਖਾਸ ਤੌਰ ਤੇ ਇੱਕ Spring Boot ਉਦਾਹਰਨ ਦਿੰਦਾ ਹੈ। ਯਾਦਦਾਸ਼ਤ ਦੇ ਬਿਨਾ, ਦੂਜਾ ਸਵਾਲ ਬਹੁਤ ਜ਼ਿਆਦਾ ਧੁੰਦਲਾ ਹੋਵੇਗਾ।

<img src="../../../translated_images/pa/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="ਬਹੁ-ਚਰਣ ਚੈਟ ਡੈਮੋ" width="800"/>

*ਪ੍ਰਸ਼ਨਾਂ ਵਿੱਚ ਸੰਦਰਭ ਸੰਭਾਲ*

### ਕਦਮ-ਦਰ-ਕਦਮ ਤਰਕ

ਕੋਈ ਗਣਿਤ ਦਾ ਸਮੱਸਿਆ ਚੁਣੋ ਅਤੇ ਦੋਵੇਂ ਕਦਮ-ਦਰ-ਕਦਮ ਤਰਕ ਅਤੇ ਘੱਟ ਸਮਰੱਥਾ ਨਾਲ ਇਸਨੂੰ ਕੋਸ਼ਿਸ਼ ਕਰੋ। ਘੱਟ ਸਮਰੱਥਾ ਸਿਰਫ ਜਵਾਬ ਦਿੰਦੀ ਹੈ - ਤੇਜ਼ ਪਰ ਅਸਪਸ਼ਟ। ਕਦਮ-ਦਰ-ਕਦਮ ਤੁਹਾਨੂੰ ਹਰ ਹਿਸਾਬ ਅਤੇ ਫੈਸਲੇ ਦਿਖਾਉਂਦਾ ਹੈ।

<img src="../../../translated_images/pa/step-by-step-reasoning-demo.12139513356faecd.webp" alt="ਕਦਮ-ਦਰ-ਕਦਮ ਤਰਕ ਡੈਮੋ" width="800"/>

*ਵਿਆਖਿਆਤਮਕ ਕਦਮਾਂ ਨਾਲ ਗਣਿਤ ਸਮੱਸਿਆ*

### ਸੀਮਿਤ ਨਿਕਾਸ

ਜਦੋਂ ਤੁਹਾਨੂੰ ਖਾਸ ਫਾਰਮੈਟ ਜਾਂ ਸ਼ਬਦ ਸੰਖਿਆ ਦੀ ਲੋੜ ਹੋਵੇ, ਇਹ ਪੈਟਰਨ ਕੜੀ ਪਾਲਣਾ ਕਰਵਾਉਂਦਾ ਹੈ। 100 ਸ਼ਬਦਾਂ ਦਾ ਬੁਲੇਟ ਪੁਆਇੰਟ ਫਾਰਮੈਟ ਵਿੱਚ ਇੱਕ ਸਾਰ ਬਣਾਉਣ ਦੀ ਕੋਸ਼ਿਸ਼ ਕਰੋ।

<img src="../../../translated_images/pa/constrained-output-demo.567cc45b75da1633.webp" alt="ਸੀਮਿਤ ਨਿਕਾਸ ਡੈਮੋ" width="800"/>

*ਫਾਰਮੈਟ ਕੰਟਰੋਲ ਨਾਲ ਮਸ਼ੀਨ ਲਰਨਿੰਗ ਦਾ ਸਾਰ*

## ਤੁਸੀਂ ਅਸਲ ਵਿੱਚ ਕੀ ਸਿੱਖ ਰਹੇ ਹੋ

**ਤਰਕ ਕਰਨ ਦੀ ਕੋਸ਼ਿਸ਼ ਸਭ ਕੁਝ ਬਦਲ ਦਿੰਦੀ ਹੈ**

GPT-5.2 ਤੁਹਾਨੂੰ ਤੁਹਾਡੇ ਪ੍ਰੋੰਪਟਸ ਰਾਹੀਂ ਗਣਨਾਤਮਕ ਕੋਸ਼ਿਸ਼ ਕਾਬੂ ਕਰਨ ਦਿੰਦਾ ਹੈ। ਘੱਟ ਕੋਸ਼ਿਸ਼ ਵਿੱਚ ਤੇਜ਼ ਜਵਾਬ ਹੁੰਦੇ ਹਨ ਪਰ ਘੱਟ ਤਲਾਸ਼ੀ। ਉੱਚ ਕੋਸ਼ਿਸ਼ ਵਿੱਚ ਮਾਡਲ ਧੀਰੇ-ਧੀਰੇ ਗਹਿਰਾਈ ਨਾਲ ਸੋਚਦਾ ਹੈ। ਤੁਸੀਂ ਸਿੱਖ ਰਹੇ ਹੋ ਕਿ ਹਰ ਕੰਮ ਦੀ ਕੁਠਿਨਤਾ ਦੇ ਅਨੁਸਾਰ ਕੋਸ਼ਿਸ਼ ਮਿਲਾਈਏ - ਸਧਾਰਣ ਸਵਾਲਾਂ ‘ਤੇ ਸਮਾਂ ਨਾ ਨਸ਼ਟ ਕਰੋ, ਪਰ ਜਟਿਲ ਫੈਸਲਿਆਂ ‘ਤੇ ਵੀ ਜਲਦੀ ਨਾ ਕਰੋ।

**ਸੰਰਚਨਾ ਵਿਵਹਾਰ ਨੂੰ ਰਹੀਤ ਕਰਦੀ ਹੈ**

ਕੀ ਤੁਸੀਂ ਪ੍ਰੋੰਪਟ ਵਿੱਚ XML ਟੈਗ ਦੇਖੇ? ਇਹ ਸਿਰਫ ਸਜਾਵਟ ਨਹੀਂ ਹਨ। ਮਾਡਲ ਸੰਰਚਿਤ ਹੁਕਮਾਂ ਦਾ ਮਨੋਯੋਗ ਨਾਲ ਪਾਲਣ ਕਰਦੇ ਹਨ ਮੁਕਤ ਰੂਪ ਵਾਲੇ ਲਿਖਤ ਨਾਲੋਂ। ਜਦੋਂ ਤੁਹਾਨੂੰ ਕਈ ਕਦਮਾਂ ਦੀ ਕਾਰਵਾਈ ਜਾਂ ਜਟਿਲ ਤਰਕਾਂ ਦੀ ਲੋੜ ਹੁੰਦੀ ਹੈ, ਸੰਰਚਨਾ ਮਾਡਲ ਨੂੰ ਇਹ ਟ੍ਰੈਕ ਕਰਨ ਵਿੱਚ ਮਦਦ ਕਰਦੀ ਹੈ ਕਿ ਉਹ ਕਿੱਥੇ ਖੜਾ ਹੈ ਅਤੇ ਅਗਲਾ ਕੀ ਹੈ।

<img src="../../../translated_images/pa/prompt-structure.a77763d63f4e2f89.webp" alt="ਪ੍ਰੋੰਪਟ ਸੰਰਚਨਾ" width="800"/>

*ਚੰਗੀ ਤਰ੍ਹਾਂ ਸੰਰਚਿਤ ਪ੍ਰੋੰਪਟ ਦੀ ਵਿਵਸਥਾ ਜਿਸ ਵਿੱਚ ਸਾਫ ਸੈਕਸ਼ਨ ਅਤੇ XML-ਸਟਾਈਲ ਅਯੋਜਨਾ ਹੈ*

**ਸਵੈ-ਮੁਲਾਂਕਣ ਦੁਆਰਾ ਗੁਣਵੱਤਾ**

ਸਵੈ-ਮੁਲਾਂਕਣ ਪੈਟਰਨ ਵੁਣਮਾਨ ਮਾਪਦੰਡਾਂ ਨੂੰ ਸਪਸ਼ਟ ਕਰਕੇ ਕੰਮ ਕਰਦੇ ਹਨ। ਉਮੀਦ ਕਰਨ ਦੀ ਬਜਾਏ ਕਿ ਮਾਡਲ "ਠੀਕ ਕਰੇਗਾ", ਤੁਸੀਂ ਇਸਨੂੰ ਸਹੀ ਤਰ੍ਹਾਂ ਸਮਝਾਉਂਦੇ ਹੋ ਕਿ "ਠੀਕ" ਦਾ ਕੀ ਮਤਲਬ ਹੈ: ਸਹੀ ਤਰਕ, ਗਲਤੀ ਹੈਂਡਲਿੰਗ, ਪ੍ਰਦਰਸ਼ਨ, ਸੁਰੱਖਿਆ। ਮਾਡਲ ਫਿਰ ਆਪਣਾ ਨਿਕਾਸ ਐਵੇਲੂਏਟ ਕਰ ਸਕਦਾ ਹੈ ਅਤੇ ਸੁਧਾਰ ਕਰ ਸਕਦਾ ਹੈ। ਇਹ ਕੋਡ ਉਤਪਾਦਨ ਨੂੰ ਕਿਸਮਤ ਤੋਂ ਇੱਕ ਪ੍ਰਕਿਰਿਆ ਬਣਾਉਂਦਾ ਹੈ।

**ਸੰਦਰਭ ਸੀਮਾ ਵਾਲਾ ਹੈ**

ਬਹੁ-ਚਰਣ ਗੱਲਬਾਤ ਹਰ ਬੇਨਤੀ ਨਾਲ ਸੰਦੇਸ਼ ਇਤਿਹਾਸ ਸ਼ਾਮਿਲ ਕਰਕੇ ਕੰਮ ਕਰਦੀ ਹੈ। ਪਰ ਇੱਕ ਸੀਮਾ ਹੈ - ਹਰ ਮਾਡਲ ਦੀ ਵੱਧ ਤੋਂ ਵੱਧ ਟੋਕਨ ਗਿਣਤੀ ਹੁੰਦੀ ਹੈ। ਗੱਲਬਾਤ ਵਧਣ ਨਾਲ, ਤੁਹਾਨੂੰ ਸੰਬੰਧਤ ਸੰਦਰਭ ਨੂੰ ਬਿਨਾਂ ਸੀਮਾ ਪਾਰ ਕੀਤੇ ਰੱਖਣ ਲਈ ਰਣਨੀਤੀਆਂ ਦੀ ਲੋੜ ਹੋਵੇਗੀ। ਇਹ ਮੌਡੀਊਲ ਤੁਹਾਨੂੰ ਦਿਖਾਉਂਦਾ ਹੈ ਕਿ ਯਾਦਦਾਸ਼ਤ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀ ਹੈ; ਬਾਅਦ ਵਿੱਚ ਤੁਸੀਂ ਸਿਖੋਗੇ ਕਿ ਕਦੋਂ ਸੰਖੇਪ ਕਰਨਾ ਹੈ, ਕਦੋਂ ਭੁੱਲਣਾ ਹੈ ਅਤੇ ਕਦੋਂ ਪ੍ਰਾਪਤ ਕਰਨਾ ਹੈ।

## ਅਗਲੇ ਕਦਮ

**ਅਗਲਾ ਮੌਡੀਊਲ:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**ਨੇਵੀਗੇਸ਼ਨ:** [← ਪਿਛਲਾ: ਮੌਡੀਊਲ 01 - ਪ੍ਰਸਤਾਵਨਾ](../01-introduction/README.md) | [ਮੁੱਖ ਸਫ਼ਾ ਵੱਲ ਵਾਪਸ](../README.md) | [ਅਗਲਾ: ਮੌਡੀਊਲ 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਵੀਕ੍ਰਿਤੀ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸ਼ੁੱਧਤਾ ਲਈ ਯਤਨਸ਼ੀਲ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਰੱਖੋ ਕਿ ਸਵੈਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸਮਰਥਤਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਸਭ ਤੋਂ ਪ੍ਰਮਾਣਿਕ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਮਹੱਤਵਪੂਰਨ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸੰਪੂਰਨ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਅਸੀਂ ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਕਾਰਨ ਪੈਦਾ ਹੋਣ ਵਾਲੀਆਂ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀਆਂ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆਵਾਂ ਲਈ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->