# Module 02: GPT-5.2 ਨਾਲ ਪ੍ਰੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ

## ਟੇਬਲ ਆਫ਼ ਕੰਟੈਂਟਸ

- [ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ](../../../02-prompt-engineering)
- [ਪੂਰਵ ਸ਼ਰਤਾਂ](../../../02-prompt-engineering)
- [ਪ੍ਰੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਦੀ ਸਮਝ](../../../02-prompt-engineering)
- [ਪ੍ਰੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਦੇ ਮੂਲ ਸਿਧਾਂਤ](../../../02-prompt-engineering)
  - [ਜ਼ੀਰੋ-ਸ਼ੌਟ ਪ੍ਰੰਪਟਿੰਗ](../../../02-prompt-engineering)
  - [ਫਿਊ-ਸ਼ੌਟ ਪ੍ਰੰਪਟਿੰਗ](../../../02-prompt-engineering)
  - [ਚੇਨ ਆਫ਼ ਥੌਟ](../../../02-prompt-engineering)
  - [ਰੋਲ-ਅਧਾਰਿਤ ਪ੍ਰੰਪਟਿੰਗ](../../../02-prompt-engineering)
  - [ਪ੍ਰੰਪਟ ਟੈਮਪਲੇਟਸ](../../../02-prompt-engineering)
- [ਅਗੇਤਰ ਪੈਟਰਨ](../../../02-prompt-engineering)
- [ਮੌਜੂਦਾ ਐਜ਼ਯੂਰ ਸਰੋਤਾਂ ਦੀ ਵਰਤੋਂ](../../../02-prompt-engineering)
- [ਐਪਲੀਕੇਸ਼ਨ ਸਕ੍ਰੀਨਸ਼ਾਟ](../../../02-prompt-engineering)
- [ਪੈਟਰਨ ਦੀ ਭੇਟ](../../../02-prompt-engineering)
  - [ਘੱਟ ਵੱਧ ਉਤਸਾਹ](../../../02-prompt-engineering)
  - [ਟਾਸਕ ਨਿਭਾਣਾ (ਟੂਲ ਪ੍ਰੀਐਂਬਲ)](../../../02-prompt-engineering)
  - [ਆਪਣੇ ਆਪ ਦਾ ਮੁਲਾਂਕਣ ਕਰਨ ਵਾਲਾ ਕੋਡ](../../../02-prompt-engineering)
  - [ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ](../../../02-prompt-engineering)
  - [ਮਲਟੀ-ਟਰਨ ਚੈਟ](../../../02-prompt-engineering)
  - [ਕਦਮ-ਬਦ ਕਦਮ ਤਰਕ](../../../02-prompt-engineering)
  - [ਸੀਮਿਤ ਆਉਟਪੁੱਟ](../../../02-prompt-engineering)
- [ਤੁਸੀਂ ਅਸਲ ਵਿੱਚ ਕੀ ਸਿੱਖ ਰਹੇ ਹੋ](../../../02-prompt-engineering)
- [ਅਗਲੇ ਕਦਮ](../../../02-prompt-engineering)

## ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ

<img src="../../../translated_images/pa/what-youll-learn.c68269ac048503b2.webp" alt="ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ" width="800"/>

ਪਿਛਲੇ ਮਾਡਿਊਲ ਵਿੱਚ, ਤੁਸੀਂ ਦੇਖਿਆ ਕਿ ਮੈਮੋਰੀ ਕਿਵੇਂ ਗੱਲਬਾਤੀ AI ਨੂੰ ਸਮਰਥਿਤ ਕਰਦੀ ਹੈ ਅਤੇ GitHub ਮਾਡਲਾਂ ਨਾਲ ਬੁਨਿਆਦੀ ਇੰਟਰੈਕਸ਼ਨਾਂ ਦੀ ਵਰਤੋਂ ਕੀਤੀ। ਹੁਣ ਅਸੀਂ ਮੂਲ ਧਿਆਨ ਇਸ ਗੱਲ 'ਤੇ ਦੇਵਾਂਗੇ ਕਿ ਤੁਸੀਂ ਸਵਾਲ ਕਿਵੇਂ ਪੁੱਛਦੇ ਹੋ — ਪ੍ਰੰਪਟ ਖ਼ੁਦ — Azure OpenAI ਦੇ GPT-5.2 ਦੀ ਵਰਤੋਂ ਕਰਕੇ। ਤੁਹਾਡੇ ਪ੍ਰੰਪਟਾਂ ਦੇ ਸੰਰਚਨਾ ਦੇ ਤਰੀਕੇ ਨਾਲ ਤੁਹਾਨੂੰ ਮਿਲਣ ਵਾਲੇ ਉੱਤਰਾਂ ਦੀ ਗੁಣਵੱਤਾ ਤੇਜ਼ੀ ਨਾਲ ਪ੍ਰਭਾਵਿਤ ਹੁੰਦੀ ਹੈ। ਅਸੀਂ ਸ਼ੁਰੂ ਕਰਾਂਗੇ ਮੂਲ ਪ੍ਰੰਪਟਿੰਗ ਤਕਨੀਕਾਂ ਦੀ ਸਮੀਖਿਆ ਨਾਲ, ਫਿਰ ਅੱਠ ਅਗੇਤਰ ਪੈਟਰਨਾਂ ਵੱਲ ਵਧਾਂਗੇ ਜੋ GPT-5.2 ਦੀ ਪੂਰੀ ਸਮਰੱਥਾ ਲੈ ਸਕਦੀਆਂ ਹਨ।

ਅਸੀਂ GPT-5.2 ਦੀ ਵਰਤੋਂ ਕਰਾਂਗੇ ਕਿਉਂਕਿ ਇਹ ਰੀਜ਼ਨਿੰਗ ਕੰਟਰੋਲ ਲੈ ਕੇ ਆਉਂਦਾ ਹੈ - ਤੁਸੀਂ ਮਾਡਲ ਨੂੰ ਦੱਸ ਸਕਦੇ ਹੋ ਕਿ ਜਵਾਬ ਦੇਣ ਤੋਂ ਪਹਿਲਾਂ ਕਿੰਨਾ ਸੋਚਣਾ ਹੈ। ਇਸ ਨਾਲ ਵੱਖ-ਵੱਖ ਪ੍ਰੰਪਟਿੰਗ ਰਣਨੀਤੀਆਂ ਹੋਰ ਸਪਸ਼ਟ ਹੁੰਦੀਆਂ ਹਨ ਅਤੇ ਤੁਹਾਨੂੰ ਸਮਝ ਿਚ ਕਿਵੇਂ ਹਰ ਤਰੀਕਾ ਵਰਤਣਾ ਹੈ। ਅਸੀਂ Azure ਦੇ ਘੱਟ ਰੇਟ ਲਿਮਿਟਾਂ ਤੋਂ ਵੀ ਲਾਭ ਉਠਾਵਾਂਗੇ ਜੋ GPT-5.2 ਲਈ GitHub ਮਾਡਲਾਂ ਨਾਲੋਂ ਕਮ ਹਨ।

## ਪੂਰਵ ਸ਼ਰਤਾਂ

- ਮਾਡਿਊਲ 01 ਪੂਰਾ ਕੀਤਾ ਹੋਇਆ (Azure OpenAI ਸਰੋਤ ਤੈਯਾਰ ਕੀਤੇ)
- ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ `.env` ਫਾਈਲ ਜਿਸ ਵਿੱਚ Azure ਕ੍ਰੈਡੀਸ਼ਲ ਹੋਣ (ਮਾਡਿਊਲ 01 ਵਿੱਚ `azd up` ਨਾਲ ਬਣਾਈ ਗਈ)

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਮਾਡਿਊਲ 01 ਨਹੀਂ ਕੀਤਾ, ਤਾਂ ਸਭ ਤੋਂ ਪਹਿਲਾਂ ਉੱਥੇ ਦਿੱਤੀਆਂ ਡਿਪਲੋਇਮੈਂਟ ਹਦਾਇਤਾਂ ਦਾ ਪਾਲਣ ਕਰੋ।

## ਪ੍ਰੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਦੀ ਸਮਝ

<img src="../../../translated_images/pa/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="ਪ੍ਰੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਕੀ ਹੈ?" width="800"/>

ਪ੍ਰੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਦਾ ਮਤਲਬ ਹੈ ਇੰਪੁਟ ਟੈਕਸਟ ਦਾ ਐਸਾ ਡਿਜ਼ਾਈਨ ਕਰਨਾ ਜੋ ਸਤਤ ਤੌਰ ਤੇ ਤੁਹਾਨੂੰ ਲੋੜੀਂਦੇ ਨਤੀਜੇ ਦੇਵੇ। ਇਹ ਸਿਰਫ ਸਵਾਲ ਪੁੱਛਣ ਬਾਰੇ ਨਹੀਂ - ਇਹ ਅਜਿਹੇ ਬੇਨਤੀ ਦੇਣ ਬਾਰੇ ਹੈ ਕਿ ਮਾਡਲ ਬਿਲਕੁਲ ਸਮਝ ਲਵੇ ਕਿ ਤੁਹਾਨੂੰ ਕੀ ਚਾਹੀਦਾ ਹੈ ਅਤੇ ਕਿਵੇਂ ਮੁਹੱਈਆ ਕਰਨਾ ਹੈ।

ਇਸਨੂੰ ਆਪਣੇ ਸਾਥੀ ਨੂੰ ਹੁਕਮ ਦੇਣ ਵਾਂਗ ਸੋਚੋ। "ਬੱਗ ਠੀਕ ਕਰੋ" ਅਸਪਸ਼ਟ ਹੈ। "UserService.java ਦੀ ਲਾਈਨ 45 ਵਿੱਚ ਨੱਲ ਪੌਇੰਟਰ ਐਕਸੈਪਸ਼ਨ ਠੀਕ ਕਰੋ ਅਤੇ ਨੱਲ ਚੈੱਕ ਸ਼ਾਮਲ ਕਰੋ" ਖਾਸ ਹੈ। ਭਾਸ਼ਾ ਮਾਡਲ ਵੀ ਇਵੇਂ ਕੰਮ ਕਰਦੇ ਹਨ - ਖਾਸ ਪਨ੍ਹ ਅਤੇ ਸੰਰਚਨਾ ਜਰੂਰੀ ਹੈ।

<img src="../../../translated_images/pa/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j ਕਿਵੇਂ ਫਿੱਟ ਹੁੰਦਾ ਹੈ" width="800"/>

LangChain4j ਇੰਫ੍ਰਾਸਟ੍ਰਕਚਰ ਮੁਹੱਈਆ ਕਰਦਾ ਹੈ — ਮਾਡਲ ਕੰਨੇਕਸ਼ਨਾਂ, ਮੈਮੋਰੀ ਅਤੇ ਸੁਨੇਹੇ ਟਾਈਪ — ਜਦਕਿ ਪ੍ਰੰਪਟ ਪੈਟਰਨ ਸਿਰਫ਼ ਧਿਆਨ ਨਾਲ ਬਣਾਏ ਗਏ ਟੈਕਸਟ ਹੁੰਦੇ ਹਨ ਜੋ ਉਸ ਇੰਫ੍ਰਾਸਟ੍ਰਕਚਰ ਰਾਹੀਂ ਭੇਜੇ ਜਾਂਦੇ ਹਨ। ਮੁੱਖ ਇਮਾਰਤੀ ਢਾਂਚੇ ਹਨ `SystemMessage` (ਜੋ AI ਦੇ ਵਿਹਾਰ ਅਤੇ ਭੂਮਿਕਾ ਸੈੱਟ ਕਰਦਾ ਹੈ) ਅਤੇ `UserMessage` (ਜੋ ਤੁਹਾਡੀ ਅਸਲ ਬੇਨਤੀ ਲੈ ਕੇ ਆਉਂਦਾ ਹੈ)।

## ਪ੍ਰੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਦੇ ਮੂਲ ਸਿਧਾਂਤ

<img src="../../../translated_images/pa/five-patterns-overview.160f35045ffd2a94.webp" alt="ਪੰਜ ਪ੍ਰੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਪੈਟਰਨ ਸਾਰ" width="800"/>

ਇਸ ਮਾਡਿਊਲ ਵਿੱਚ ਅਗੇਤਰ ਪੈਟਰਨਾਂ ਵਿੱਚ ਜਾਣ ਤੋਂ ਪਹਿਲਾਂ, ਆਓ ਪੰਜ ਮੂਲ ਪ੍ਰੰਪਟਿੰਗ ਤਕਨੀਕਾਂ ਦੀ ਸਮੀਖਿਆ ਕਰੀਏ। ਇਹ ਉਹ ਬੁਨਿਆਦੀ ਢਾਂਚਾ ਹੈ ਜੋ ਹਰ ਪ੍ਰੰਪਟ ਇੰਜੀਨੀਅਰ ਨੂੰ ਜਾਣਨਾ ਚਾਹੀਦਾ ਹੈ। ਜੇ ਤੁਸੀਂ ਪਹਿਲਾਂ ਹੀ [ਕਵਿਕ ਸਟਾਰਟ ਮਾਡਿਊਲ](../00-quick-start/README.md#2-prompt-patterns) ਦਾ ਅਨੁਭਵ ਕਰ ਚੁਕੇ ਹੋ, ਤਾਂ ਇਹਨਾਂ ਤਕਨੀਕਾਂ ਨੂੰ ਪਹਿਲਾਂ ਹੀ ਦੇਖਿਆ ਹੈ — ਇੱਥੇ ਉਨ੍ਹਾਂ ਦਾ ਸੰਕਲਪਿਕ ਢਾਂਚਾ ਦਿੱਤਾ ਗਿਆ ਹੈ।

### ਜ਼ੀਰੋ-ਸ਼ੌਟ ਪ੍ਰੰਪਟਿੰਗ

ਸਬ ਤੋਂ ਸਧਾਰਣ ਤਰੀਕਾ: ਮਾਡਲ ਨੂੰ ਬਿਨਾਂ ਕਿਸੇ ਉਦਾਹਰਨ ਦੇ ਸਿੱਧਾ ਹੁਕਮ ਦਿਓ। ਮਾਡਲ ਪੂਰੀ ਤਰ੍ਹਾਂ ਆਪਣੀ ਆਪ੍ਰੇਸ਼ਨਲ ਟ੍ਰੇਨਿੰਗ 'ਤੇ ਨਿਰਭਰ ਕਰਦਾ ਹੈ। ਇਹ ਉਹਨਾਂ ਬੇਨਤੀਆਂ ਲਈ ਵਧੀਆ ਹੈ ਜਿੱਥੇ ਤਕਮੀਲ ਸਪਸ਼ਟ ਹੋਵੇ।

<img src="../../../translated_images/pa/zero-shot-prompting.7abc24228be84e6c.webp" alt="ਜ਼ੀਰੋ-ਸ਼ੌਟ ਪ੍ਰੰਪਟਿੰਗ" width="800"/>

*ਕੋਈ ਉਦਾਹਰਨ ਨਹੀਂ — ਮਾਡਲ ਸਿੱਧਾ ਹੁਕਮ ਸਮਝ ਕੇ ਕੰਮ ਕਰਦਾ ਹੈ*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// ਜਵਾਬ: "ਸਕਾਰਾਤਮਕ"
```

**ਕਦੋਂ ਵਰਤਣਾ ਹੈ:** ਆਸਾਨ ਵਰਗੀਕਰਨ, ਸਿੱਧੇ ਸਵਾਲ, ਅਨੁਵਾਦ, ਜਾਂ ਕੋਈ ਵੀ ਟਾਸਕ ਜਿਸਨੂੰ ਮਾਡਲ ਇਸਤਰੀ ਨਾਲ ਸਮਝ ਕੇ ਕਰ ਸਕਦਾ ਹੈ।

### ਫਿਊ-ਸ਼ੌਟ ਪ੍ਰੰਪਟਿੰਗ

ਉਦਾਹਰਨ ਦਿਓ ਜਿਹੜੀਆਂ ਮਾਡਲ ਨੂੰ ਤੁਹਾਡੇ ਚਾਹਵੀਂ ਪੈਟਰਨ ਸਿਖਾਉਂਦੀਆਂ ਹਨ। ਮਾਡਲ ਤੁਹਾਡੇ ਉਦਾਹਰਨਾਂ ਤੋਂ ਇਨਪੁੱਟ-ਆਉਟਪੁੱਟ ਫਾਰਮੈਟ ਸਮਝ ਕੇ ਨਵੇਂ ਇਨਪੁੱਟ ਤੇ ਅਮਲ ਕਰਦਾ ਹੈ। ਇਸ ਨਾਲ ਉਹ ਕਾਰਜ ਜਿੱਥੇ ਫਾਰਮੈਟ ਜਾਂ ਵਰਤਾਵ ਸਪਸ਼ਟ ਨਹੀਂ, ਉਨ੍ਹਾਂ ਵਿੱਚ ਸਥਿਰਤਾ ਆਉਂਦੀ ਹੈ।

<img src="../../../translated_images/pa/few-shot-prompting.9d9eace1da88989a.webp" alt="ਫਿਊ-ਸ਼ੌਟ ਪ੍ਰੰਪਟਿੰਗ" width="800"/>

*ਉਦਾਹਰਨਾਂ ਤੋਂ ਸਿੱਖਣਾ — ਮਾਡਲ ਪੈਟਰਨ ਪਛਾਣਦਾ ਹੈ ਅਤੇ ਨਵੇਂ ਇਨਪੁੱਟ 'ਤੇ ਲਾਗੂ ਕਰਦਾ ਹੈ*

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

**ਕਦੋਂ ਵਰਤਣਾ ਹੈ:** ਕਸਟਮ ਵਰਗੀਕਰਨ, ਇੱਕਸਾਰ ਫਾਰਮੈਟਿੰਗ, ਖੇਤਰ-ਵਿਸ਼ੇਸ਼ ਟਾਸਕ, ਜਾਂ ਜਦੋਂ ਜ਼ੀਰੋ-ਸ਼ੌਟ ਨਤੀਜੇ ਗੈਰ-ਇਕਸਾਰ ਹੋਣ।

### ਚੇਨ ਆਫ਼ ਥੌਟ

ਮਾਡਲ ਨੂੰ ਬੋਲਣ ਦਿਓ ਕਿ ਉਹ ਆਪਣੀ ਸੋਚ ਕਦਮ-ਬਦ ਕਦਮ ਦਿਖਾਉਂਦਾ ਜਾਵੇ। ਸਿੱਧਾ ਜਵਾਬ ਦੇਣ ਦੀ ਬਜਾਏ, ਮਾਡਲ ਮੁਸ਼ਕਲ ਨੂੰ ਖੰਡ-ਖੰਡ ਕੋੜ੍ਹਦਾ ਹੈ ਅਤੇ ਹਰ ਹਿੱਸੇ ਨੂੰ ਵਿਸ਼ੇਸ਼ ਰੂਪ ਵਿੱਚ ਹੱਲ ਕਰਦਾ ਹੈ। ਇਸ ਨਾਲ ਗਣਿਤ, ਤਰਕ, ਅਤੇ ਕਈ ਕਦਮੀ ਤਰਕ-ਬੁੱਝ ਕਾਰਜਾਂ ਵਿੱਚ ਸਹੀ ਜਵਾਬ ਮਿਲਦਾ ਹੈ।

<img src="../../../translated_images/pa/chain-of-thought.5cff6630e2657e2a.webp" alt="ਚੇਨ ਆਫ਼ ਥੌਟ ਪ੍ਰੰਪਟਿੰਗ" width="800"/>

*ਕਦਮ-ਕਦਮ ਤਰਕ — ਕਠਿਨ ਮੁਸ਼ਕਲਾਂ ਨੂੰ ਤਰਕਸ਼ੀਲ ਕਦਮਾਂ ਵਿੱਚ ਵੰਡਣਾ*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// ਮਾਡਲ ਦਿਖਾਉਂਦਾ ਹੈ: 15 - 8 = 7, ਫਿਰ 7 + 12 = 19 ਸੇਬ
```

**ਕਦੋਂ ਵਰਤਣਾ ਹੈ:** ਗਣਿਤ ਦੇ ਸਮੱਸਿਆਵਾਂ, ਤਰਕ ਸਬੰਧੀ ਪਜ਼ਲ, ਡੀਬੱਗਿੰਗ, ਜਾਂ ਜਿੱਥੇ ਸੋਚਣ ਦੀ ਪ੍ਰਕਿਰਿਆ ਦਰਸਾਉਣ ਨਾਲ ਤਪਾਸੀ ਅਤੇ ਵਿਸ਼ਵਾਸ ਵਧੇ।

### ਰੋਲ-ਅਧਾਰਿਤ ਪ੍ਰੰਪਟਿੰਗ

ਪ੍ਰਸ਼ਨ ਪੁੱਛਣ ਤੋਂ ਪਹਿਲਾਂ AI ਲਈ ਕੋਈ ਵਿਅਕਤੀਗਤ ਭੂਮਿਕਾ ਜਾਂ ਪਾਤਰ ਸੈੱਟ ਕਰੋ। ਇਸ ਨਾਲ ਪ੍ਰਸੰਗ ਮਿਲਦਾ ਹੈ ਜੋ ਜਵਾਬ ਦੇਟੇ ਦੀ ਬੋਲੀ, ਡੂੰਘਾਈ ਅਤੇ ਕੇਂਦਰਤਾਈ ਨੂੰ ਪ੍ਰਭਾਵਿਤ ਕਰਦਾ ਹੈ। "ਸੌਫਟਵੇਅਰ ਆਰਕੀਟੈਕਟ" ਵੱਖਰਾ ਸਲਾਹ ਦੇਵੇਗਾ "ਜੂਨੀਅਰ ਡਿਵੈਲਪਰ" ਜਾਂ "ਸੁਰੱਖਿਆ ਆਡੀਟਰ" ਤੋਂ।

<img src="../../../translated_images/pa/role-based-prompting.a806e1a73de6e3a4.webp" alt="ਰੋਲ-ਅਧਾਰਿਤ ਪ੍ਰੰਪਟਿੰਗ" width="800"/>

*ਪ੍ਰਸੰਗ ਅਤੇ ਪਾਤਰ ਸੈੱਟ ਕਰਨਾ — ਇੱਕੋ ਸਵਾਲ ਵੱਖੋ-ਵੱਖਰੇ ਭੂਮਿਕਾ ਵਿੱਚ ਵੱਖਰੇ ਜਵਾਬ ਦਿੰਦਾ ਹੈ*

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

**ਕਦੋਂ ਵਰਤਣਾ ਹੈ:** ਕੋਡ ਰਿਵਿਊ, ਟਿਊਟਰਿੰਗ, ਖੇਤਰ-ਵਿਸ਼ੇਸ਼ ਵਿਸ਼ਲੇਸ਼ਣ, ਜਾਂ ਜਿੱਥੇ ਵਿਸ਼ੇਸ਼ ਕਿਸਮ ਦੀ ਯੋਗਤਾ / ਦ੍ਰਿਸ਼ਟਿਕੋਣ ਮੁਤਾਬਕ ਜਵਾਬਾਂ ਚਾਹੀਦੀਆਂ ਹਨ।

### ਪ੍ਰੰਪਟ ਟੈਮਪਲੇਟਸ

ਵੈਰੀਏਬਲ ਪਲੇਸਹੋਲਡਰਾਂ ਨਾਲ ਮੁੜ ਉਪਯੋਗਯੋਗ ਪ੍ਰੰਪਟ ਤਿਆਰ ਕਰੋ। ਹਰ ਵਾਰੀ ਨਵਾਂ ਪ੍ਰੰਪਟ ਲਿਖਣ ਦੀ ਬਜਾਏ, ਇੱਕ ਟੈਮਪਲੇਟ ਬਣਾਓ ਅਤੇ ਵੱਖ-ਵੱਖ ਕੀਮਤਾਂ ਭਰੋ। LangChain4j ਦੀ `PromptTemplate` ਕਲਾਸ ਇਸ ਨੂੰ `{{variable}}` ਸਿੰਟੈਕਸ ਨਾਲ ਆਸਾਨ ਬਣਾਉਂਦੀ ਹੈ।

<img src="../../../translated_images/pa/prompt-templates.14bfc37d45f1a933.webp" alt="ਪ੍ਰੰਪਟ ਟੈਮਪਲੇਟਸ" width="800"/>

*ਵੈਰੀਏਬਲ ਪਲੇਸਹੋਲਡਰਾਂ ਨਾਲ ਮੁੜ-ਵਰਤੋਂ ਹੋਣ ਵਾਲੇ ਪ੍ਰੰਪਟ — ਇੱਕ ਟੈਮਪਲੇਟ, ਬਹੁਤ ਵਰਤੋਂ*

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

**ਕਦੋਂ ਵਰਤਣਾ ਹੈ:** ਵੱਖਰੇ ਇਨਪੁੱਟਾਂ ਵਾਲੇ ਦੁਹਰਾਏ ਜਾਣ ਵਾਲੇ ਸਵਾਲ, ਬੈਚ ਪ੍ਰੋਸੈਸਿੰਗ, ਮੁੜ-ਵਰਤੋਂਯੋਗ AI ਵਰਕਫਲੋਜ਼ ਬਣਾਉਣਾ, ਜਾਂ ਜਿੱਥੇ ਪ੍ਰੰਪਟ ਦੀ ਸੰਰਚਨਾ ਸਥਿਰ ਰਹਿੰਦੀ ਹੈ ਪਰ ਡਾਟਾ ਬਦਲਦਾ ਹੈ।

---

ਇਹ ਪੰਜ ਮੂਲ ਸਿਧਾਂਤ ਤੁਹਾਨੂੰ ਬਹੁਤ ਸਾਰੇ ਪ੍ਰੰਪਟਿੰਗ ਕਾਰਜਾਂ ਲਈ ਮਜ਼ਬੂਤ ਟੂਲਕਿਟ ਦਿੰਦੇ ਹਨ। ਇਹ ਮਾਡਿਊਲ ਉਸ ਉੱਤੇ ਅੱਠ ਅਗੇਤਰ ਪੈਟਰਨ ਨਾਲ ਬਣਦਾ ਹੈ ਜੋ GPT-5.2 ਦੇ ਰੀਜ਼ਨਿੰਗ ਕੰਟਰੋਲ, ਸਵੈ-ਮੁਲਾਂਕਣ ਅਤੇ ਸੰਰਚਿਤ ਆਉਟਪੁੱਟ ਦੀਆਂ ਸਮਰੱਥਾਵਾਂ ਨੂੰ ਲਾਂਭਦੇ ਹਨ।

## ਅਗੇਤਰ ਪੈਟਰਨ

ਮੂਲ ਸਿਧਾਂਤਾਂ ਨੂੰ ਕਵਰ ਕਰਨ ਤੋਂ ਬਾਅਦ, ਆਓ ਅਸੀਂ ਇਸ ਮਾਡਿਊਲ ਨੂੰ ਵਿਲੱਖਣ ਬਣਾਉਣ ਵਾਲੇ ਅੱਠ ਅਗੇਤਰ ਪੈਟਰਨਾਂ ਵੱਲ ਵਧੀਏ। ਹਰ ਸਮੱਸਿਆ ਨੂੰ ਇੱਕੋ ਢੰਗ ਦੀ ਲੋੜ ਨਹੀਂ ਹੁੰਦੀ। ਕੁਝ ਸਵਾਲਾਂ ਨੂੰ ਜ਼ਲਦੀ ਜਵਾਬ ਚਾਹੀਦਾ ਹੈ, ਕੁਝ ਨੂੰ ਡੂੰਘੀ ਸੋਚ। ਕੁਝ ਨੂੰ ਤਰਕ ਵੇਖਾਉਣੀ ਲੋੜ ਹੈ, ਕੁਝ ਨੂੰ ਸਿਰਫ ਨਤੀਜੇ। ਹੇਠਾਂ ਦਿੱਤੇ ਹਰ ਪੈਟਰਨ ਨੂੰ ਵੱਖਰੇ ਪਰੀਪਰਸ਼ ਲਈ ਤਿਆਰ ਕੀਤਾ ਗਿਆ ਹੈ — ਅਤੇ GPT-5.2 ਦਾ ਰੀਜ਼ਨਿੰਗ ਕੰਟਰੋਲ ਭੇਦ ਹੋਰ ਵਧਾ ਦਿੰਦਾ ਹੈ।

<img src="../../../translated_images/pa/eight-patterns.fa1ebfdf16f71e9a.webp" alt="ਅੱਠ ਪ੍ਰੰਪਟਿੰਗ ਪੈਟਰਨ" width="800"/>

*ਅੱਠ ਪ੍ਰੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਪੈਟਰਨਾਂ ਦਾ ਝਲਕ ਅਤੇ ਉਨ੍ਹਾਂ ਦੇ ਵਰਤੋਂ ਕੇਸ*

<img src="../../../translated_images/pa/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 ਨਾਲ ਰੀਜ਼ਨਿੰਗ ਕੰਟਰੋਲ" width="800"/>

*GPT-5.2 ਦਾ ਰੀਜ਼ਨਿੰਗ ਕੰਟਰੋਲ ਮਾਡਲ ਨੂੰ ਦੱਸਦਾ ਹੈ ਕਿੰਨੀ ਸੋਚ ਕਰਨੀ ਹੈ — ਤੇਜ਼ ਸਿੱਧੇ ਜਵਾਬ ਤੋਂ ਲੈ ਕੇ ਡੂੰਘੀ ਖੋਜ ਤੱਕ*

<img src="../../../translated_images/pa/reasoning-effort.db4a3ba5b8e392c1.webp" alt="ਰੀਜ਼ਨਿੰਗ ਮਿਹਨਤ ਦੀ ਤੁਲਨਾ" width="800"/>

*ਘੱਟ ਉਤਸਾਹ (ਤੇਜ਼, ਸਿੱਧਾ) ਵਿ. ਜ਼ਿਆਦਾ ਉਤਸਾਹ (ਪੂਰਾ, ਖੋਜੀ) ਰੀਜ਼ਨਿੰਗ ਰਣਨੀਤੀਆਂ*

**ਘੱਟ ਉਤਸਾਹ (ਤੇਜ਼ ਅਤੇ ਕੇਂਦਰਿਤ)** - ਉਹਨਾਂ ਆਸਾਨ ਸਵਾਲਾਂ ਲਈ ਜਿੱਥੇ ਤੇਜ਼, ਸਿੱਧੇ ਜਵਾਬ ਚਾਹੀਦੇ ਹਨ। ਮਾਡਲ ਬਹੁਤ ਘੱਟ ਤਰਕ ਕਰਦਾ ਹੈ - ਵੱਧ ਤੋਂ ਵੱਧ 2 ਕਦਮ। ਇਹ ਕੈਲਕੁਲੇਸ਼ਨ, ਲੁਕਅਪ, ਜਾਂ ਸਿੱਧੇ ਸਵਾਲਾਂ ਲਈ ਵਰਤੋ।

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
> - "ਘੱਟ ਉਤਸਾਹ ਅਤੇ ਜ਼ਿਆਦਾ ਉਤਸਾਹ ਵਾਲੇ ਪ੍ਰੰਪਟ ਪੈਟਰਨ ਵਿੱਚ ਕੀ ਫਰਕ ਹੈ?"
> - "ਪ੍ਰੰਪਟ ਵਿੱਚ XML ਟੈਗ ਆਈਏ ਦੇ ਜਵਾਬ ਨੂੰ ਕਿਵੇਂ ਸੰਰਚਿਤ ਕਰਨ ਵਿੱਚ ਮਦਦ ਕਰਦੇ ਹਨ?"
> - "ਸਵੈ-ਵਿਮਰਸ਼ ਪੈਟਰਨ ਕਦੋਂ ਵਰਤਣੇ ਹਨ ਅਤੇ ਸਿੱਧੇ ਹੁਕਮ ਕਦੋਂ?"

**ਜ਼ਿਆਦਾ ਉਤਸਾਹ (ਡੂੰਘਾ ਅਤੇ ਪੂਰਾ)** - ਜਟਿਲ ਸਮੱਸਿਆਵਾਂ ਲਈ ਜਿੱਥੇ ਤੁਸੀਂ ਵਿਸਥਾਰਿਤ ਵਿਸ਼ਲੇਸ਼ਣ ਚਾਹੁੰਦੇ ਹੋ। ਮਾਡਲ ਪੂਰੀ ਤਰ੍ਹਾਂ ਖੋਜ ਕਰਦਾ ਹੈ ਅਤੇ ਵਿਸਥਾਰਿਤ ਤਰਕ ਦਿਖਾਉਂਦਾ ਹੈ। ਇਹ ਸਿਸਟਮ ਡਿਜ਼ਾਈਨ, ਆਰਕੀਟੈਕਚਰ ਦੇ ਫੈਸਲੇ, ਜਾਂ ਪੇਚੀਦਾ ਖੋਜ ਲਈ ਵਰਤੋਂਯੋਗ ਹੈ।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**ਟਾਸਕ ਨਿਭਾਣਾ (ਕਦਮ-ਬਦ ਕਦਮ ਅੱਗੇ ਵਧਣਾ)** - ਕਈ ਕਦਮਾਂ ਵਾਲੇ ਵਰਕਫਲੋਜ਼ ਲਈ। ਮਾਡਲ ਪਹਿਲਾਂ ਇੱਕ ਯੋਜਨਾ ਦਿੰਦਾ ਹੈ, ਹਰ ਕਦਮ ਨੂੰ ਵਰਣਨ ਕਰਦਾ ਹੈ, ਫਿਰ ਸੰਖੇਪ ਦਿੰਦਾ ਹੈ। ਮਿਲਾਈਗੇਸ਼ਨਜ਼, ਲਾਗੂ ਕਰਨ, ਜਾਂ ਕਿਸੇ ਵੀ ਕਈ-ਕਦਮੀ ਪ੍ਰਕਿਰਿਆ ਲਈ ਵਰਤੋ।

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

ਚੇਨ-ਆਫ-ਥੌਟ ਪ੍ਰੰਪਟਿੰਗ ਸਪਸ਼ਟ ਤੌਰ 'ਤੇ ਮਾਡਲ ਨੂੰ ਆਪਣੀ ਸੋਚ ਦਿਖਾਉਣ ਲਈ ਕਹਿੰਦਾ ਹੈ, ਜੋ ਜਟਿਲ ਕਾਰਜਾਂ ਲਈ ਸਹੀ ਜਵਾਬਾਂ ਨੂੰ ਸੁਧਾਰਦਾ ਹੈ। ਕਦਮ-ਦਰ-кਦਮ ਵਿਸ਼ਲੇਸ਼ਣ ਮਨੁੱਖਾਂ ਅਤੇ AI ਦੋਹਾਂ ਲਈ ਲਾਜ਼ਮੀ ਤਰਕ ਸਮਝਣ ਵਿੱਚ ਸਹਾਇਕ ਹੁੰਦੀ ਹੈ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ਚੈਟ ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** ਇਸ ਪੈਟਰਨ ਬਾਰੇ ਪੁੱਛੋ:
> - "ਲੰਬੇ ਸਮੇਂ ਚੱਲਣ ਵਾਲੇ ਕੰਮਾਂ ਲਈ ਟਾਸਕ ਨਿਭਾਣੇ ਪੈਟਰਨ ਨੂੰ ਕਿਵੇਂ ਅਨੁਕੂਲਿਤ ਕਰਾਂ?"
> - "ਪ੍ਰਡਕਸ਼ਨ ਐਪਲੀਕੇਸ਼ਨਾਂ ਵਿੱਚ ਟੂਲ ਪ੍ਰੀਐਂਬਲ ਦੇ ਸਮਰਚਨਾ ਦੇ ਸਰੋਤ ਰੁਪਾਂਤਰਨ?"
> - "UI ਵਿੱਚ ਦਰਮਿਆਨੀ ਤਰੱਕੀ ਨੂੰ ਕੈਪਚਰ ਅਤੇ ਦਿਖਾਉਣ ਦਾ ਵਧੀਆ ਤਰੀਕਾ ਕੀ ਹੈ?"

<img src="../../../translated_images/pa/task-execution-pattern.9da3967750ab5c1e.webp" alt="ਟਾਸਕ ਨਿਭਾਣਾ ਪੈਟਰਨ" width="800"/>

*ਯੋਜਨਾ → ਅਮਲ → ਸੰਖੇਪ - ਕਈ ਕਦਮਾਂ ਵਾਲੇ ਕਾਰਜਾਂ ਲਈ ਵਰਕ ਫਲੋ*

**ਆਪਣੇ ਆਪ ਦਾ ਮੁਲਾਂਕਣ ਕਰਨ ਵਾਲਾ ਕੋਡ** - ਉਤਪਾਦਨ-ਗੁਣਵੱਤਾ ਵਾਲਾ ਕੋਡ ਤਿਆਰ ਕਰਨ ਲਈ। ਮਾਡਲ ਪ੍ਰੋਡਕਸ਼ਨ ਮਿਆਰਾਂ ਦੇ ਅਨੁਕੂਲ ਕੋਡ ਬਣਾਉਂਦਾ ਹੈ ਜਿਸ ਵਿੱਚ ਠੀਕ ਤਰ੍ਹਾਂ ਗਲਤੀ ਸੰਭਾਲ ਵੀ ਹੁੰਦੀ ਹੈ। ਇਹ ਨਵੇਂ ਫੀਚਰਾਂ ਜਾਂ ਸਰਵਿਸ ਬਣਾਉਣ ਸਮੇਂ ਵਰਤਿਆ ਜਾਂਦਾ ਹੈ।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pa/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="ਆਪਣੇ ਆਪ ਦਾ ਮੁਲਾਂਕਣ ਚੱਕਰ" width="800"/>

*ਰੈਪਟ ਵਰਧਨ ਦਾ ਚੱਕਰ - ਤਿਆਰ ਕਰੋ, ਮੁਲਾਂਕਣ ਕਰੋ, ਮੁੱਦੇ ਪਛਾਣੋ, ਸੁਧਾਰੋ, ਦੁਹਰਾ ਕਰੋ*

**ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ** - ਇੱਕਸਾਰ ਮੁਲਾਂਕਣ ਲਈ। ਮਾਡਲ ਕੋਡ ਦਾ ਸਮੀਖਿਆ ਇੱਕ ਨਿਸ਼ਚਿਤ ਫਰੇਮਵਰਕ (ਸਹੀਪਨ, ਅਮਲ, ਪ੍ਰਦਰਸ਼ਨ, ਸੁਰੱਖਿਆ, ਰੱਖ-ਰਖਾਵ) ਨਾਲ ਕਰਦਾ ਹੈ। ਇਹ ਕੋਡ ਸਮੀਖਿਆ ਜਾਂ ਗੁਣਵੱਤਾ ਮੁਲਾਂਕਣ ਲਈ ਵਰਤਣਾ।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ਚੈਟ ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ ਬਾਰੇ:
> - "ਮੁਖਤਲਿਫ ਕਿਸਮਾਂ ਦੀਆਂ ਕੋਡ ਸਮੀਖਿਆਵਾਂ ਲਈ ਵਿਸ਼ਲੇਸ਼ਣ ਫਰੇਮਵਰਕ ਨੂੰ ਕਿਵੇਂ ਕਸਟਮਾਈਜ਼ ਕਰਾਂ?"
> - "ਸੰਰਚਿਤ ਆਉਟਪੁੱਟ ਨੂੰ ਪ੍ਰੋਗਰਾਮੈਟਿਕ ਤਰੀਕੇ ਨਾਲ ਕਿਵੇਂ ਪਾਰਸ ਅਤੇ ਐਕਟ ਕਰਨਾ ਸਹੀ ਹੈ?"
> - "ਵੱਖ-ਵੱਖ ਸਮੀਖਿਆ ਸੈਸ਼ਨਾਂ ਵਿੱਚ ਜਿੰਮੇਵਾਰੀ ਪੱਧਰਾਂ ਨੂੰ ਕਿਵੇਂ ਯਕੀਨੀ ਬਣਾਇਆ ਜਾਵੇ?"

<img src="../../../translated_images/pa/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ ਪੈਟਰਨ" width="800"/>

*ਸਥਿਰ ਕੋਡ ਸਮੀਖਿਆ ਲਈ ਫਰੇਮਵਰਕ ਨਾਲ ਜਿੰਮੇਵਾਰੀ ਪੱਧਰ*

**ਮਲਟੀ-ਟਰਨ ਚੈਟ** - ਪ੍ਰਸੰਗ ਚਾਹੀਦਾ ਹੈ ਐਸੀਆਂ ਗੱਲਬਾਤਾਂ ਲਈ। ਮਾਡਲ ਪਹਿਲਾਂ ਬਾਤ ਕੀਤੀ ਗਈਆਂ ਗੱਲਾਂ ਨੂੰ ਯਾਦ ਰੱਖਦਾ ਹੈ ਅਤੇ ਉਨ੍ਹਾਂ 'ਤੇ ਬਨਾਉਂਦਾ ਹੈ। ਇੰਟਰੇਕਟਿਵ ਸਹਾਇਤਾ ਸੈਸ਼ਨਾਂ ਜਾਂ ਪੇਚੀਦਾ ਪ੍ਰਸ਼ਨਾਂ ਅਤੇ ਉੱਤਰਾਂ ਲਈ ਵਰਤੋ।

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/pa/context-memory.dff30ad9fa78832a.webp" alt="ਪਰਸੰਗ ਮੈਮੋਰੀ" width="800"/>

*ਕਿਵੇਂ ਸਾਂਝੀ ਗੱਲਬਾਤ ਬਹੁ-ਟਰਨਾਂ ਵਿੱਚ ਟੋਕੇਨ ਸੀਮਾ ਤੱਕ ਬੁਨਦੀ ਜਾਂਦੀ ਹੈ*

**ਕਦਮ-ਬਦ ਕਦਮ ਤਰਕ** - ਉਹਨਾਂ ਸਮੱਸਿਆਵਾਂ ਲਈ ਜਿੱਥੇ ਤਰਕ ਸਪਸ਼ਟ ਦਿੱਖਣਾ ਲਾਜ਼ਮੀ ਹੈ। ਮਾਡਲ ਹਰ ਕਦਮ ਦੀ ਵੁਜ਼੍ਹਾਨੀ ਤਰਕ ਦਿਖਾਉਂਦਾ ਹੈ। ਇਹ ਗਣਿਤ, ਤਰਕ ਪਜ਼ਲ ਜਾਂ ਜਿੱਥੇ ਸੋਚਣ ਦੀ ਪ੍ਰਕਿਰਿਆ ਸਮਝਣੀ ਹੋਵੇ, ਲਈ ਹੈ।

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/pa/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="ਕਦਮ-ਬਦ ਕਦਮ ਪੈਟਰਨ" width="800"/>

*ਮੁਸ਼ਕਲਾਂ ਨੂੰ ਸਪਸ਼ਟ ਤਰਕ پاਦਾਂ ਵਿੱਚ ਵੰਡਣਾ*

**ਸੀਮਿਤ ਆਉਟਪੁੱਟ** - ਜਬ ਜਵਾਬਾਂ ਲਈ ਖ਼ਾਸ ਫਾਰਮੈਟ ਦੀ ਲੋੜ ਹੋਵੇ। ਮਾਡਲ ਸਖਤੀ ਨਾਲ ਫਾਰਮੈਟ ਅਤੇ ਲੰਬਾਈ ਦੇ ਨਿਯਮਾਂ ਦੀ ਪਾਲਣਾ ਕਰਦਾ ਹੈ। ਇਹ ਸਮਾਪਤੀਆਂ ਜਾਂ ਜਦੋਂ ਤੁਸੀਂ ਨਿਰਧਾਰਿਤ ਢਾਂਚਾ ਚਾਹੁੰਦੇ ਹੋ, ਲਈ ਵਰਤਿਆ ਜਾਂਦਾ ਹੈ।

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

<img src="../../../translated_images/pa/constrained-output-pattern.0ce39a682a6795c2.webp" alt="ਸੀਮਿਤ ਆਉਟਪੁੱਟ ਪੈਟਰਨ" width="800"/>

*ਖ਼ਾਸ ਫਾਰਮੈਟ, ਲੰਬਾਈ, ਅਤੇ ਸੰਰਚਨਾ ਦੀ ਪਾਬੰਦੀ*

## ਮੌਜੂਦਾ ਐਜ਼ਯੂਰ ਸਰੋਤਾਂ ਦੀ ਵਰਤੋਂ

**ਡਿਪਲੋਇਮੈਂਟ ਦੀ ਪੁਸ਼ਟੀ ਕਰੋ:**

ਪੱਕਾ ਕਰੋ ਕਿ `.env` ਫਾਈਲ ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ ਮੌਜੂਦ ਹੈ ਜਿਸ ਵਿੱਚ Azure ਕ੍ਰੈਡੀਸ਼ਲ ਹਨ (ਮਾਡਿਊਲ 01 ਦੇ ਦੌਰਾਨ ਬਣਾਈਆਂ ਗਈਆਂ):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ਦਿਖਾਉਣਾ ਚਾਹੀਦਾ ਹੈ
```

**ਐਪਲੀਕੇਸ਼ਨ ਸ਼ੁਰੂ ਕਰੋ:**

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਪਹਿਲਾਂ ਹੀ ਮਾਡਿਊਲ 01 ਤੋਂ `./start-all.sh` ਨਾਲ ਸਾਰੇ ਐਪਲੀਕੇਸ਼ਨ ਚਾਲੂ ਕਰ ਚੁੱਕੇ ਹੋ, ਤਾਂ ਇਹ ਮਾਡਿਊਲ ਪਹਿਲਾਂ ਹੀ ਪੋਰਟ 8083 'ਤੇ ਚੱਲ ਰਿਹਾ ਹੈ। ਤੁਸੀਂ ਹੇਠਾਂ ਦਿੱਤੀਆਂ ਸਟਾਰਟ ਕਮਾਂਡਾਂ ਛੱਡ ਕੇ ਸਿੱਧਾ http://localhost:8083 ਤੇ ਜਾ ਸਕਦੇ ਹੋ।

**ਵਿਕਲਪ 1: ਸਪ੍ਰਿੰਗ ਬੂਟ ਡੈਸ਼ਬੋਰਡ ਦੀ ਵਰਤੋਂ (VS Code ਯੂਜ਼ਰਾਂ ਲਈ ਸਿਫ਼ਾਰਸ਼ੀ)**

ਡੈਵ ਕન્ટੇਨਰ ਵਿੱਚ ਸਪ੍ਰਿੰਗ ਬੂਟ ਡੈਸ਼ਬੋਰਡ ਐਕਸਟੈਂਸ਼ਨ ਸ਼ਾਮਿਲ ਹੈ, ਜੋ ਸਾਰੇ ਸਪ੍ਰਿੰਗ ਬੂਟ ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਵਿਜ਼ੂਅਲ ਤਰੀਕੇ ਨਾਲ ਪ੍ਰਬੰਧਿਤ ਕਰਨ ਦੀ ਸਹੂਲਤ ਦਿੰਦਾ ਹੈ। ਤੁਸੀਂ ਇਸਨੂੰ VS Code ਦੇ ਖੱਬੇ ਪਾਸੇ ਐਕਟਿਵਿਟੀ ਬਾਰ ਵਿੱਚ ਸਪ੍ਰਿੰਗ ਬੂਟ ਆਈਕਨ 'ਤੇ ਕਲਿੱਕ ਕਰਕੇ ਲੱਭ ਸਕਦੇ ਹੋ।
Spring Boot ਡੈਸ਼ਬੋਰਡ ਤੋਂ, ਤੁਸੀਂ:
- ਵਰਕਸਪੇਸ ਵਿੱਚ ਸਾਰੇ ਉਪਲਬਧ Spring Boot ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਦੇਖ ਸਕਦੇ ਹੋ
- ਇੱਕ ਕਲਿੱਕ ਨਾਲ ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਸ਼ੁਰੂ/ਰੋਕ ਸਕਦੇ ਹੋ
- ਐਪਲੀਕੇਸ਼ਨ ਲਾਗ ਨੂੰ ਰੀਅਲ-ਟਾਈਮ ਵਿੱਚ ਵੇਖ ਸਕਦੇ ਹੋ
- ਐਪਲੀਕੇਸ਼ਨ ਸਥਿਤੀ ਦੀ ਨਿਗਰਾਨੀ ਕਰ ਸਕਦੇ ਹੋ

ਸਿੱਧਾ "prompt-engineering" ਦੇ ਨਾਲ ਪਲੇ ਬਟਨ 'ਤੇ ਕਲਿੱਕ ਕਰੋ ਇਸ ਮੋਡਿਊਲ ਨੂੰ ਸ਼ੁਰੂ ਕਰਨ ਲਈ, ਜਾਂ ਸਾਰੇ ਮੋਡਿਊਲ ਨੂੰ ਇਕੱਠੇ ਸ਼ੁਰੂ ਕਰੋ।

<img src="../../../translated_images/pa/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ਵਿਕਲਪ 2: ਸ਼ੈੱਲ ਸਕ੍ਰਿਪਟਾਂ ਦੀ ਵਰਤੋਂ ਕਰਕੇ**

ਸਾਰੇ ਵੈੱਬ ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਸ਼ੁਰੂ ਕਰੋ (ਮੋਡਿਊਲ 01-04):

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

ਜਾਂ ਸਿਰਫ ਇਸ ਮੋਡਿਊਲ ਨੂੰ ਸ਼ੁਰੂ ਕਰੋ:

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

ਦੋਹਾਂ ਸਕ੍ਰਿਪਟਸ ਆਪਣੇ ਆਪ ਰੂਟ `.env` ਫਾਇਲ ਤੋਂ ਵਾਤਾਵਰਣ ਚਲਕਾਂ ਨੂੰ ਲੋਡ ਕਰਦੇ ਹਨ ਅਤੇ ਜੇ JARs ਮੌਜੂਦ ਨਹੀਂ ਹਨ ਤਾਂ ਉਹ ਬਣਾਵਾਂਗੇ।

> **ਨੋਟ:** ਜੇ ਤੁਸੀਂ ਸਾਰੇ ਮੋਡਿਊਲ ਨੂੰ ਮੈਨੁਅਲੀ ਤਰੀਕੇ ਨਾਲ ਬਣਾਉਣਾ ਪਸੰਦ ਕਰਦੇ ਹੋ ਜਦੋਂ ਤੱਕ ਸ਼ੁਰੂ ਨਾ ਕਰੋ:
>
> **ਬੈਸ਼:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

> **ਪਾਵਰਸ਼ੈੱਲ:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

ਆਪਣੇ ਬ੍ਰਾਊਜ਼ਰ ਵਿੱਚ http://localhost:8083 ਖੋਲ੍ਹੋ।

**ਰੋਕਣ ਲਈ:**

**ਬੈਸ਼:**
```bash
./stop.sh  # ਸਿਰਫ ਇਹ ਮਾਡਿਊਲ
# ਜਾਂ
cd .. && ./stop-all.sh  # ਸਾਰੇ ਮਾਡਿਊਲ
```

**ਪਾਵਰਸ਼ੈੱਲ:**
```powershell
.\stop.ps1  # ਸਿਰਫ ਇਹ ਮੋਡੀਊਲ
# ਜਾਂ
cd ..; .\stop-all.ps1  # ਸਾਰੇ ਮੋਡੀਊਲ
```

## ਐਪਲੀਕੇਸ਼ਨ ਸਕਰੀਨਸ਼ੌਟ

<img src="../../../translated_images/pa/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*ਮੁੱਖ ਡੈਸ਼ਬੋਰਡ ਜੋ ਸਾਰੇ 8 ਪ੍ਰੋੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਪੈਟਰਨ ਨੂੰ ਉਹਨਾਂ ਦੀਆਂ ਵਿਸ਼ੇਸ਼ਤਾਵਾਂ ਅਤੇ ਉਪਯੋਗ ਮਾਮਲਿਆਂ ਨਾਲ ਦਿਖਾਉਂਦਾ ਹੈ*

## ਪੈਟਰਨਾਂ ਦੀ ਖੋਜ

ਵੈੱਬ ਇੰਟਰਫ਼ੇਸ ਤੁਹਾਨੂੰ ਵੱਖ- ਵੱਖ ਪ੍ਰੋੰਪਟਿੰਗ ਰਣਨੀਤੀਆਂ ਨਾਲ ਪ੍ਰਯੋਗ ਕਰਨ ਦੀ ਆਗਿਆ ਦਿੰਦੀ ਹੈ। ਹਰ ਪੈਟਰਨ ਵੱਖ-ਵੱਖ ਸਮੱਸਿਆਵਾਂ ਦਾ ਹੱਲ ਪੇਸ਼ ਕਰਦਾ ਹੈ - ਇਸਾਂ ਦੀ ਕੋਸ਼ਿਸ਼ ਕਰੋ ਤਾਂ ਕਿ ਤੁਸੀਂ ਦੇਖ ਸਕੋ ਕਿ ਕਿਵੇਂ ਹਰੇਕ ਤਰੀਕਾ ਚਮਕਦਾ ਹੈ।

### ਘੱਟ ਤੇ ਉੱਚ ਇਛਾ

ਘੱਟ ਇਛਾ ਵਰਤ ਕੇ ਇੱਕ ਸਰਲ ਪ੍ਰਸ਼ਨ ਪੁੱਛੋ ਜਿਵੇਂ "200 ਦਾ 15% কি ਹੈ?"। ਤੁਹਾਨੂੰ ਤੁਰੰਤ ਅਤੇ ਸਿੱਧਾ ਜਵਾਬ ਮਿਲੇਗਾ। ਹੁਣ ਕੁਝ ਜਟਿਲ ਪੁੱਛੋ ਜਿਵੇਂ "ਹਾਈ ਟ੍ਰੈਫਿਕ API ਲਈ caching ਰਣਨੀਤੀ ਡਿਜ਼ਾਈਨ ਕਰੋ" ਉੱਚ ਇਛਾ ਨਾਲ। ਦੇਖੋ ਕਿ ਮਾਡਲ ਕਿਵੇਂ ਧੀਮਾ ਹੋ ਕੇ ਵਿਸਥਾਰ ਨਾਲ ਤਰਕ ਦਿੰਦਾ ਹੈ। ਇੱਕੋ ਮਾਡਲ, ਇੱਕੋ ਪ੍ਰਸ਼ਨ ਰਚਨਾ - ਪਰ ਪ੍ਰੋੰਪਟ ਦੱਸਦਾ ਹੈ ਕਿ ਕਿੰਨਾ ਸੋਚਣਾ ਹੈ।

<img src="../../../translated_images/pa/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*ਘੱਟ ਤਰਕ ਨਾਲ ਤੇਜ਼ ਗਣਨਾ*

<img src="../../../translated_images/pa/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*ਵਿਆਪਕ caching ਰਣਨੀਤੀ (2.8MB)*

### ਕਾਰਜਕਾਰੀ ਅਮਲ (ਟੂਲ ਪ்ரீਐਂਬਲ)

ਕਈ ਕਦਮਾਂ ਵਾਲੇ ਵਰਕਫਲੋਅ ਨੂੰ ਅਗਾਂਹ ਦੀ ਯੋਜਨਾ ਅਤੇ ਪ੍ਰਗਤੀ ਕਥਨ ਤੋਂ ਲਾਭ ਮਿਲਦਾ ਹੈ। ਮਾਡਲ ਦੱਸਦਾ ਹੈ ਕਿ ਉਹ ਕੀ ਕਰੇਗਾ, ਹਰ ਕਦਮ ਦੀ ਕਹਾਣੀ ਬਿਆਨ ਕਰਦਾ ਹੈ, ਤੇ ਫਿਰ ਨਤੀਜੇ ਸਾਰਾਂਸ਼ ਕਰਦਾ ਹੈ।

<img src="../../../translated_images/pa/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*ਕਦਮ-ਦਰ-कਦਮ ਕਥਨ ਦੇ ਨਾਲ REST endpoint ਬਣਾਉਣਾ (3.9MB)*

### ਸਵੈ-ਮੁਲਾਂਕਣ ਕੋਡ

"ਇਮੇਲ ਵੈਰੀਫਿਕੇਸ਼ਨ ਸਰਵਿਸ ਬਣਾਓ" ਕੋਸ਼ਿਸ਼ ਕਰੋ। ਸਿਰਫ ਕੋਡ ਬਣਾਉਣ ਅਤੇ ਰੁਕਣ ਦੀ ਬਜਾਏ, ਮਾਡਲ ਬਣਾਉਂਦਾ ਹੈ, ਗੁਣਵੱਤਾ ਮਾਪਦੰਡਾਂ ਦੇ ਮੁਕਾਬਲੇ ਮੁਲਾਂਕਣ ਕਰਦਾ ਹੈ, ਕਮਜ਼ੋਰੀਆਂ ਲੱਭਦਾ ਹੈ, ਅਤੇ ਸੁਧਾਰ ਕਰਦਾ ਹੈ। ਤੁਸੀਂ ਦੇਖੋਗੇ ਕਿ ਇਹ ਕਿਵੇਂ ਲਗਾਤਾਰ ਬਿਹਤਰ ਹੁੰਦਾ ਜਾਂਦਾ ਹੈ ਜਦ ਤਕ ਕਿ ਕੋਡ ਪ੍ਰੋਡਕਸ਼ਨ ਮਿਆਰਾਂ 'ਤੇ ਪੂਰਾ ਨਹੀਂ ਉਤਰਦਾ।

<img src="../../../translated_images/pa/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*ਪੂਰੀ ਇਮੇਲ ਵੈਰਿਫਿਕੇਸ਼ਨ ਸਰਵਿਸ (5.2MB)*

### ਸੰਰਚਿਤ ਵਿਸ਼ਲੇਸ਼ਣ

ਕੋਡ ਦੀ ਸਮੀਖਿਆ ਲਈ ਇੱਕ ਜ਼ਰੂਰੀ ਮੁਲਾਂਕਣ ਢਾਂਚਾ ਚਾਹੀਦਾ ਹੁੰਦਾ ਹੈ। ਮਾਡਲ ਇਸ ਨੂੰ ਨਿਯਮਤ ਸ਼੍ਰੇਣੀਆਂ (ਸਹੀ/ਗਲਤ, ਪ੍ਰਕਿਰਿਆਵਾਂ, ਪ੍ਰਦਰਸ਼ਨ, ਸੁਰੱਖਿਆ) ਅਤੇ ਗੰਭੀਰਤਾ ਪੱਧਰਾਂ ਨਾਲ ਕੋਡ ਦਾ ਵਿਸ਼ਲੇਸ਼ਣ ਕਰਦਾ ਹੈ।

<img src="../../../translated_images/pa/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*ਫਰੇਮਵਰਕ-ਆਧਾਰਿਤ ਕੋਡ ਸਮੀਖਿਆ*

### ਬਹੁ-ਮੁੜ ਗੱਲਬਾਤ

ਪੁੱਛੋ "Spring Boot ਕੀ ਹੈ?" ਫਿਰ ਫੌਰਨ ਹੀ ਪੁੱਛੋ "ਮੈਂਨੂੰ ਇੱਕ ਉਦਾਹਰਨ ਦਿਖਾਓ"। ਮਾਡਲ ਤੁਹਾਡੇ ਪਹਿਲੇ ਪ੍ਰਸ਼ਨ ਨੂੰ ਯਾਦ ਰੱਖਦਾ ਹੈ ਅਤੇ ਤੁਹਾਨੂੰ ਖਾਸ ਤੌਰ 'ਤੇ Spring Boot ਦੀ ਉਦਾਹਰਨ ਦਿੰਦਾ ਹੈ। ਯਾਦਦਾਸ਼ਤ ਦੇ ਬਿਨਾ, ਉਹ ਦੂਜਾ ਪ੍ਰਸ਼ਨ ਬਹੁਤ ਅਸਪਸ਼ਟ ਹੁੰਦਾ।

<img src="../../../translated_images/pa/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*ਪ੍ਰਸ਼ਨਾਂ ਵਿਚਕਾਰ ਸੰਦਰਭ ਬਚਾਉਣਾ*

### ਕਦਮ-ਦਰ-ਕਦਮ ਤਰਕ

ਕੋਈ ਗਣਿਤ ਸਮੱਸਿਆ ਲਓ ਅਤੇ ਦੋਹਾਂ ਕਦਮ-ਦਰ-ਕਦਮ ਤਰਕ ਅਤੇ ਘੱਟ ਇਛਾ ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ। ਘੱਟ ਇਛਾ ਸਿਰਫ ਤੁਹਾਨੂੰ ਜਵਾਬ ਦਿੰਦੀ ਹੈ - ਤੇਜ਼ ਪਰ ਅਸਪਸ਼ਟ। ਕਦਮ-ਦਰ-ਕਦਮ ਹਰ ਗਣਨਾ ਅਤੇ ਫੈਸਲੇ ਨੂੰ ਦਿਖਾਉਂਦਾ ਹੈ।

<img src="../../../translated_images/pa/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*ਖੁੱਲ੍ਹੇ ਕਦਮਾਂ ਨਾਲ ਗਣਿਤ ਸਮੱਸਿਆ*

### ਸੀਮਿਤ ਆਉਟਪੁੱਟ

ਜਦੋਂ ਤੁਹਾਨੂੰ ਖਾਸ ਫਾਰਮੈਟ ਜਾਂ ਸ਼ਬਦ ਗਿਣਤੀ ਦੀ ਲੋੜ ਹੋਵੇ, ਇਹ ਪੈਟਰਨ ਸਖਤ ਪਾਲਣਾ ਕਰਵਾਉਂਦਾ ਹੈ। 100 ਸ਼ਬਦਾਂ ਦੀ ਬੁਲੇਟ ਪੁਆਇੰਟ ਫਾਰਮੈਟ ਵਿੱਚ ਸੰਖੇਪ ਬਣਾਉਣ ਦੀ ਕੋਸ਼ਿਸ਼ ਕਰੋ।

<img src="../../../translated_images/pa/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*ਫਾਰਮੈਟ ਨਿਯੰਤਰਣ ਵਾਲੀ ਮਸ਼ੀਨ ਲਰਨਿੰਗ ਸੰਖੇਪ*

## ਤੁਸੀਂ ਅਸਲ ਵਿੱਚ ਕੀ ਸਿੱਖ ਰਹੇ ਹੋ

**ਤਰਕ ਦੀ ਕੋਸ਼ਿਸ਼ ਸਭ ਕੁਝ ਬਦਲ ਦਿੰਦੀ ਹੈ**

GPT-5.2 ਤੁਹਾਨੂੰ ਤੁਹਾਡੇ ਪ੍ਰੋੰਪਟਾਂ ਰਾਹੀਂ ਕੰਪਿਊਟੇਸ਼ਨਲ ਕੋਸ਼ਿਸ਼ ਨੂੰ ਨਿਯੰਤਰਿਤ ਕਰਨ ਦਿੰਦਾ ਹੈ। ਘੱਟ ਕੋਸ਼ਿਸ਼ ਦਾ ਮਤਲਬ ਤੇਜ਼ ਜਵਾਬ ਹੈ ਅਤੇ ਘੱਟ ਖੋਜ। ਉੱਚ ਕੋਸ਼ਿਸ਼ ਮਤਲਬ ਹੈ ਕਿ ਮਾਡਲ ਦੀਪ ਸੋਚ ਲਈ ਸਮਾਂ ਲੈਂਦਾ ਹੈ। ਤੁਸੀਂ ਸਿੱਖ ਰਹੇ ਹੋ ਕਿ ਕੋਸ਼ਿਸ਼ ਨੂੰ ਕੰਮ ਦੀ ਜਟਿਲਤਾ ਨਾਲ ਮਿਲਾਉਣਾ - ਸਧਾਰਨ ਪ੍ਰਸ਼ਨਾਂ 'ਤੇ ਸਮਾਂ ਬਰਬਾਦ ਨਾ ਕਰੋ ਪਰ ਜਟਿਲ ਫੈਸਲਿਆਂ ਨੂੰ ਵੀ ਤੁਰੰਤ ਕਰੋ ਨਹੀਂ।

**ਸੰਰਚਨਾ ਵਰਤਾਰਾ ਨੂੰ ਮਹਿਸੂਸ ਕਰਵਾਉਂਦੀ ਹੈ**

ਕੀ ਤੁਸੀਂ ਪ੍ਰੋੰਪਟਾਂ ਵਿੱਚ XML ਟੈਗਾਂ ਵੇਖੇ? ਉਹ ਸਿਰਫ਼ ਸੁੰਦਰਤਾ ਲਈ ਨਹੀਂ ਹਨ। ਮਾਡਲਾਂ ਨੂੰ ਬਿਨਾਂ ਗੜਬੜ ਸਥਿਰ ਨਿਰਦੇਸ਼ਾਂ ਦੀ ਪਾਲਣਾ ਜ਼ਿਆਦਾ ਭਰੋਸੇਯੋਗ ਹੁੰਦੀ ਹੈ। ਜਦੋਂ ਤੁਹਾਨੂੰ ਕਈ ਕਦਮਾਂ ਵਾਲੀਆਂ ਪ੍ਰਕਿਰਿਆਵਾਂ ਜਾਂ ਜਟਿਲ ਤਰਕ ਦੀ ਲੋੜ ਹੋਵੇ, ਤਦ ਸੰਰਚਨਾ ਮਾਡਲ ਦੀ ਸਹੀ ਟ੍ਰੈਕਿੰਗ ਵਿੱਚ ਮਦਦ ਕਰਦੀ ਹੈ ਕਿ ਉਹ ਕਿੱਥੇ ਹੈ ਅਤੇ ਅਗਲਾ ਕੀ ਹੈ।

<img src="../../../translated_images/pa/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*ਚੰਗੇ ਤਰੀਕੇ ਨਾਲ ਬਣੇ ਪ੍ਰੋੰਪਟ ਦੀ ਬਣਤਰ ਸਾਫ਼ ਹਿੱਸਿਆਂ ਅਤੇ XML-ਸਟਾਈਲ ਸੰਗਠਨ ਨਾਲ*

**ਗੁਣਵੱਤਾ ਆਪ-ਮੁਲਾਂਕਣ ਦੁਆਰਾ**

ਸਵੈ-ਮੁਲਾਂਕਣ ਪੈਟਰਨ ਗੁਣਵੱਤਾ ਮਾਪਦੰਡਾਂ ਨੂੰ ਖੁੱਲ੍ਹਾ ਕਰਨ ਨਾਲ ਕੰਮ ਕਰਦੇ ਹਨ। ਮਾਡਲ 'ਸਹੀ ਕਰੇ' ਦੀ ਉਮੀਦ ਕਰਨ ਦੀ ਬਜਾਏ, ਤੁਸੀਂ ਇਹ ਦੱਸਦੇ ਹੋ ਕਿ 'ਸਹੀ' ਦਾ ਕੀ ਮਤਲਬ ਹੈ: ਸਹੀ ਤਰਕ, ਗਲਤੀ ਹੈਂਡਲਿੰਗ, ਪ੍ਰਦਰਸ਼ਨ, ਸੁਰੱਖਿਆ। ਮਾਡਲ ਫਿਰ ਆਪਣੇ ਆਪ ਦੇ ਆਉਟਪੁੱਟ ਨੂੰ ਮੁਲਾਂਕਣ ਕਰ ਸਕਦਾ ਹੈ ਅਤੇ ਸੁਧਾਰ ਕਰਦਾ ਹੈ। ਇਹ ਕੋਡ ਬਣਾਉਣ ਨੂੰ ਇੱਕ ਲਾਟਰੀ ਤੋਂ ਪ੍ਰਕਿਰਿਆ ਵਿੱਚ ਬਦਲ ਦਿੰਦਾ ਹੈ।

**ਸੰਦਰਭ ਸੀਮਿਤ ਹੈ**

ਬਹੁ-ਮੁੜ ਗੱਲਬਾਤੀਂ ਪ੍ਰਤੀਕ੍ਰਿਆ ਵਿੱਚ ਮੈਸੇਜ ਇਤਿਹਾਸ ਸ਼ਾਮਿਲ ਕਰਕੇ ਕੰਮ ਕਰਦੀ ਹੈ। ਪਰ ਇੱਕ ਸੀਮਾ ਹੈ - ਹਰ ਮਾਡਲ ਦਾ ਇੱਕ ਵੱਧ ਤੋਂ ਵੱਧ ਟੋਕਨ ਗਿਣਤੀ ਹੁੰਦੀ ਹੈ। ਗੱਲਬਾਤ ਵਧਣ ਤਕ, ਤੁਹਾਨੂੰ ਇਸ ਗਿਣਤੀ ਦੇ ਅੰਦਰ ਵਾਂੱਛਿਤ ਸੰਦਰਭ ਰੱਖਣ ਲਈ ਰਣਨੀਤੀਆਂ ਦੀ ਲੋੜ ਪਵੇਗੀ। ਇਹ ਮੋਡਿਊਲ ਤੁਹਾਨੂੰ ਯਾਦਦਾਸ਼ਤ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀ ਹੈ ਦਿਖਾਉਂਦਾ ਹੈ; ਬਾਅਦ ਵਿੱਚ ਤੁਸੀਂ ਸਿੱਖੋਗੇ ਕਿ ਕਦੋਂ ਸੰਖੇਪ ਕਰਨਾ ਹੈ, ਕਦੋਂ ਭੁੱਲਣਾ ਹੈ ਅਤੇ ਕਦੋਂ ਪ੍ਰਾਪਤ ਕਰਨਾ ਹੈ।

## ਅਗਲੇ ਕਦਮ

**ਅਗਲਾ ਮੋਡਿਊਲ:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**ਨੈਵੀਗੇਸ਼ਨ:** [← ਪਿਛਲਾ: ਮੋਡਿਊਲ 01 - ਪ੍ਰਸਤਾਵਨਾ](../01-introduction/README.md) | [ਮੁੱਖ 'ਤੇ ਵਾਪਸ](../README.md) | [ਅਗਲਾ: ਮੋਡਿਊਲ 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਪਸ਼ਟੀਕਰਨ**:
ਇਹ ਦਸਤਾਵੇਜ਼ ਏਆਈ ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰ ਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਅਤ ਲਈ ਕੋਸ਼ਿਸ਼ ਕਰਦੇ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਇਹ ਧਿਆਨ ਵਿੱਚ ਰੱਖੋ ਕਿ ਸਵੈਚਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਣਸੁਚਿਤਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੇ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਕ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਮਹੱਤਵਪੂਰਨ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਅਸੀਂ ਇਸ ਅਨੁਵਾਦ ਦੇ ਇਸਤੇਮਾਲ ਤੋਂ ਉਤਪੰਨ ਹੋਣ ਵਾਲੀਆਂ ਕੋਈ ਵੀ ਗਲਤਫਹਮੀਆਂ ਜਾਂ ਗਲਤਵਿਆਖਿਆਵਾਂ ਲਈ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->