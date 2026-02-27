# ਮੋਡੀਊਲ 00: ਤੇਜ਼ ਸ਼ੁਰੂਆਤ

## ਸਾਰਣੀ

- [ਪਰੀਚਯ](../../../00-quick-start)
- [LangChain4j ਕੀ ਹੈ?](../../../00-quick-start)
- [LangChain4j ਡੀਪੈਂਡੈਂਸੀਜ਼](../../../00-quick-start)
- [ਪੂਰਵ-ਸ਼ਰਤਾਂ](../../../00-quick-start)
- [ਸੈਟਅੱਪ](../../../00-quick-start)
  - [1. ਆਪਣਾ GitHub ਟੋਕਨ ਲਵੋ](../../../00-quick-start)
  - [2. ਆਪਣਾ ਟੋਕਨ ਸੈਟ ਕਰੋ](../../../00-quick-start)
- [ਉਦਾਹਰਨਾਂ ਚਲਾਓ](../../../00-quick-start)
  - [1. ਬੁਨਿਆਦੀ ਚੈਟ](../../../00-quick-start)
  - [2. ਪ੍ਰਾਂਪਟ ਪੈਟਰਨ](../../../00-quick-start)
  - [3. ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ](../../../00-quick-start)
  - [4. ਦਸਤਾਵੇਜ਼ Q&A (ਆਸਾਨ RAG)](../../../00-quick-start)
  - [5. ਜ਼ਿੰਮੇਵਾਰ AI](../../../00-quick-start)
- [ਹਰ ਉਦਾਹਰਨ ਕੀ ਦਿਖਾਉਂਦੀ ਹੈ](../../../00-quick-start)
- [ਅਗਲੇ ਕਦਮ](../../../00-quick-start)
- [ਟ੍ਰਬਲਸ਼ੂਟਿੰਗ](../../../00-quick-start)

## ਪਰੀਚਯ

ਇਹ ਤੇਜ਼ ਸ਼ੁਰੂਆਤ ਤੁਹਾਨੂੰ LangChain4j ਨਾਲ ਜ਼ਲਦੀ ਚਾਲੂ ਕਰਨ ਲਈ ਹੈ। ਇਹ LangChain4j ਅਤੇ GitHub Models ਨਾਲ AI ਐਪਲੀਕੇਸ਼ਨਾਂ ਬਣਾਉਣ ਦੇ ਬੁਨਿਆਦੀ ਅੰਸ਼ਾਂ ਨੂੰ ਕਵਰ ਕਰਦਾ ਹੈ। ਅਗਲੇ ਮੋਡੀਊਲਾਂ ਵਿੱਚ ਤੁਸੀਂ Azure OpenAI ਨਾਲ LangChain4j ਵਰਤ ਕੇ ਹੋਰ ਅਡਵਾਂਸ ਐਪਲੀਕੇਸ਼ਨ ਬਣਾਵੋਗੇ।

## LangChain4j ਕੀ ਹੈ?

LangChain4j ਇੱਕ ਜਾਵਾ ਲਾਇਬ੍ਰੇਰੀ ਹੈ ਜੋ AI-ਸਮਰੱਥ ਐਪਲੀਕੇਸ਼ਨਾਂ ਬਣਾਉਣ ਨੂੰ ਸੌਖਾ ਬਣਾਉਂਦੀ ਹੈ। HTTP ਕਲਾਇੰਟਾਂ ਅਤੇ JSON ਪ੍ਰਸਿੰਗ ਨਾਲ ਕੰਮ ਕਰਨ ਦੀ ਬਜਾਏ, ਤੁਸੀਂ ਸਾਫ਼ ਜਾਵા APIs ਨਾਲ ਕੰਮ ਕਰਦੇ ਹੋ। 

LangChain ਵਿੱਚ "ਚੇਨ" ਕਈ ਭਾਗਾਂ ਨੂੰ ਜੋੜਨ ਦਾ ਜ਼ਿਕਰ ਹੈ - ਤੁਸੀਂ ਪ੍ਰਾਂਪਟ ਨੂੰ ਮਾਡਲ ਨਾਲ ਜੋੜ ਸਕਦੇ ਹੋ ਜਾਂ ਕਈ AI ਕਾਲਾਂ ਨੂੰ ਇੱਕ ਦੂਜੇ ਨਤੀਜਿਆਂ ਦੇ ਕੇ ਜੋੜ ਸਕਦੇ ਹੋ। ਇਹ ਤੇਜ਼ ਸ਼ੁਰੂਆਤ ਮੂਲ ਸਿੱਧਾਂਤਾਂ 'ਤੇ ਕੇਂਦਰਿਤ ਹੈ, ਬਾਅਦ ਵਿੱਚ ਜਟਿਲ ਚੇਨਾਂ ਦੀ ਖੋਜ ਕੀਤੀ ਜਾਏਗੀ।

<img src="../../../translated_images/pa/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j ਵਿੱਚ ਚੇਨਿੰਗ ਭਾਗ - ਬਿਲਡਿੰਗ ਬਲਾਕਜ਼ ਜੁੜਦੇ ਹਨ ਤਾਕਿ ਸ਼ਕਤੀਸ਼ਾਲੀ AI ਵਰਕਫਲੋ ਬਣ ਸਕਣ*

ਅਸੀਂ ਤਿੰਨ ਮੁੱਖ ਭਾਗ ਵਰਤਾਂਗੇ:

**ChatModel** - AI ਮਾਡਲ ਨਾਲ ਗੱਲਬਾਤ ਦਾ ਇੰਟਰਫੇਸ। `model.chat("prompt")` ਕਾਲ ਕਰੋ ਅਤੇ ਇਕ ਜਵਾਬ ਸਤਰ ਪਾਓ। ਅਸੀਂ `OpenAiOfficialChatModel` ਵਰਤਦੇ ਹਾਂ ਜੋ OpenAI-ਅਨੁਕੂਲ ਐਂਡਪੌਇੰਟਾਂ ਜਿਵੇਂ ਕਿ GitHub Models ਨਾਲ ਕੰਮ ਕਰਦਾ ਹੈ।

**AiServices** - ਕਿਸਮ-ਸੁਰੱਖਿਅਤ AI ਸੇਵਾ ਇੰਟਰਫੇਸ ਬਣਾਉਂਦਾ ਹੈ। ਢੰਗ ਲਿਖੋ, `@Tool` ਨਾਲ ਅਨੋਟੇਟ ਕਰੋ, ਤੇ LangChain4j ਆਟੋਮੈਟਿਕ ਤੌਰ ਤੇ ਤੁਸੀਂ ਕੇ Java ਫੰਗਸ਼ਨਾਂ ਨੂੰ ਕਾਲ ਕਰਦਾ ਹੈ ਜਦੋਂ ਲੋੜ ਹੋਵੇ।

**MessageWindowChatMemory** - ਗੱਲਬਾਤ ਦਾ ਇਤਿਹਾਸ ਨਿਰਧਾਰਿਤ ਕਰਦਾ ਹੈ। ਇਹ ਦੇ ਬਗੈਰ ਹਰ ਬੇਨਤੀ ਅਲੱਗ ਹੁੰਦੀ ਹੈ। ਇਸ ਨਾਲ AI ਪਿਛਲੇ ਸੁਨੇਹੇ ਯਾਦ ਰੱਖਦਾ ਹੈ ਅਤੇ ਕਈ ਟਰਨਾਂ ਵਿੱਖੇ ਸੰਦੇਸ਼ ਦੇ ਸੰਦਰਭ ਨੂੰ ਬਰਕਰਾਰ ਰੱਖਦਾ ਹੈ।

<img src="../../../translated_images/pa/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j ਅਦਾਂਚਾ - ਮੁੱਖ ਭਾਗਾਂ ਮਿਲ ਕੇ ਤੁਹਾਡੇ AI ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਸ਼ਕਤੀ ਦਿੰਦੇ ਹਨ*

## LangChain4j ਡੀਪੈਂਡੈਂਸੀਜ਼

ਇਹ ਤੇਜ਼ ਸ਼ੁਰੂਆਤ ਤਿੰਨ Maven ਡੀਪੈਂਡੈਂਸੀਜ਼ ਵਰਤਦੀ ਹੈ[`pom.xml`](../../../00-quick-start/pom.xml):

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

`langchain4j-open-ai-official` ਮੋਡੀਊਲ `OpenAiOfficialChatModel` ਕਲਾਸ ਮੁਹੱਈਆ ਕਰਵਾਉਂਦਾ ਹੈ ਜੋ OpenAI-ਅਨੁਕੂਲ APIs ਨਾਲ ਜੁੜਦਾ ਹੈ। GitHub Models ਵੀ ਉਹੀ API ਫਾਰਮੈਟ ਵਰਤਦਾ ਹੈ, ਇਸ ਲਈ ਖਾਸ ਐਡੈਪਟਰ ਦੀ ਲੋੜ ਨਹੀਂ - ਸਿਰਫ਼ ਬੇਸ URL ਨੂੰ `https://models.github.ai/inference` ਵੱਲ ਸੰਕੇਤ ਕਰੋ।

`langchain4j-easy-rag` ਮੋਡੀਊਲ ਆਟੋਮੈਟਿਕ ਦਸਤਾਵੇਜ਼ ਵੰਡ, ਐਮਬੈਡਿੰਗ ਅਤੇ ਰੀਟਰਿਵਲ ਮੁਹੱਈਆ ਕਰਵਾਉਂਦਾ ਹੈ ਤਾਂ ਕਿ ਤੁਸੀਂ ਹੱਥੋਂ-ਹੱਥ ਹਰ ਕਦਮ ਨੂੰ ਬਿਨਾਂ ਸੈਟਿੰਗ ਕਰਦੇ ਹੋਏ RAG ਐਪ ਬਣਾਓ।

## ਪੂਰਵ-ਸ਼ਰਤਾਂ

**ਡੈਵ ਕੰਟੇਨਰ ਵਰਤ ਰਹੇ ਹੋ?** Java ਅਤੇ Maven ਪਹਿਲਾਂ ਹੀ ਇੰਸਟਾਲ ਹਨ। ਤੁਹਾਨੂੰ ਸਿਰਫ਼ GitHub Personal Access Token ਦੀ ਲੋੜ ਹੈ।

**ਲੋਕਲ ਡਿਵੈਲਪਮੈਂਟ:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (ਹੇਠਾਂ ਨਿਰਦੇਸ਼ ਦਿੱਤੇ ਗਏ ਹਨ)

> **ਨੋਟ:** ਇਹ ਮੋਡੀਊਲ GitHub Models ਦੇ `gpt-4.1-nano` ਵਰਤਦਾ ਹੈ। ਕੋਡ ਵਿੱਚ ਮਾਡਲ ਨਾਂ ਨਾ ਬਦਲੋ - ਇਹ GitHub ਦੇ ਉਪਲਬਧ ਮਾਡਲਾਂ ਲਈ ਤੈਅ ਹੈ।

## ਸੈਟਅੱਪ

### 1. ਆਪਣਾ GitHub ਟੋਕਨ ਲਵੋ

1. [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens) ਜਾਓ
2. "Generate new token" 'ਤੇ ਕਲਿੱਕ ਕਰੋ
3. ਵਰਣਨਾਤਮਕ ਨਾਮ ਦਿਓ (ਜਿਵੇਂ, "LangChain4j Demo")
4. ਮਿਆਦ ਸੈਟ ਕਰੋ (7 ਦਿਨ ਸਿਫ਼ਾਰਸ਼ੀ ਹੈ)
5. "Account permissions" 'ਚ "Models" ਲੱਭੋ ਤੇ "Read-only" ਕਰੋ
6. "Generate token" 'ਤੇ ਕਲਿੱਕ ਕਰੋ
7. ਆਪਣਾ ਟੋਕਨ ਕਾਪੀ ਕਰੋ ਅਤੇ ਸੰਭਾਲ ਕੇ ਰੱਖੋ - ਇਹ ਫਿਰ ਨਹੀਂ ਦਿੱਖੇਗਾ

### 2. ਆਪਣਾ ਟੋਕਨ ਸੈਟ ਕਰੋ

**ਵਿਕਲਪ 1: VS Code ਵਰਤ ਕੇ (ਸਿਫਾਰਸ਼ੀ)**

ਜੇ ਤੁਸੀਂ VS Code ਵਰਤ ਰਹੇ ਹੋ, ਆਪਣਾ ਟੋਕਨ ਪ੍ਰੋਜੈਕਟ ਜੜ੍ਹ `.env` ਫ਼ਾਈਲ ਵਿੱਚ ਜੋੜੋ:

ਜੇ `.env` ਫ਼ਾਈਲ ਮੌਜੂਦ ਨਹੀਂ ਹੈ, ਤਾਂ `.env.example` ਨੂੰ `.env` ਵਿੱਚ ਕਾਪੀ ਕਰੋ ਜਾਂ ਨਵੀਂ `.env` ਫ਼ਾਈਲ ਬਣਾਓ।

**ਉਦਾਹਰਨ `.env` ਫ਼ਾਈਲ:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env ਵਿੱਚ
GITHUB_TOKEN=your_token_here
```

ਫਿਰ ਤੁਸੀਂ ਕਿਸੇ ਵੀ ਡੈਮੋ ਫ਼ਾਈਲ (ਜਿਵੇਂ `BasicChatDemo.java`) 'ਤੇ ਰਾਈਟ-ਕਲਿੱਕ ਕਰਕੇ **"Run Java"** ਚੁਣ ਸਕਦੇ ਹੋ ਜਾਂ Run ਅਤੇ Debug ਪੈਨਲ ਤੋਂ ਲਾਂਚ ਕੰਫਿਗਰੇਸ਼ਨ ਵਰਤ ਸਕਦੇ ਹੋ।

**ਵਿਕਲਪ 2: ਟਰਮੀਨਲ ਵਰਤ ਕੇ**

ਟੋਕਨ ਨੂੰ ਵਾਤਾਵਰਣ ਵੇਰੀਏਬਲ ਵਜੋਂ ਸੈਟ ਕਰੋ:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## ਉਦਾਹਰਨ ਚਲਾਓ

**VS Code ਵਰਤ ਕੇ:** ਕਿਸੇ ਵੀ ਡੈਮੋ ਫ਼ਾਈਲ 'ਤੇ ਰਾਈਟ ਕਲਿੱਕ ਕਰੋ ਅਤੇ **"Run Java"** ਚੁਣੋ, ਜਾਂ Run ਅਤੇ Debug ਪੈਨਲ ਤੋਂ ਲਾਂਚ ਕੰਫਿਗਰੇਸ਼ਨ ਵਰਤੋ (ਪਹਿਲਾਂ ਟੋਕਨ `.env` ਵਿੱਚ ਜੋੜਨਾ ਯਕੀਨੀ ਬਣਾਓ)।

**Maven ਵਰਤ ਕੇ:** ਵਿਕਲਪ ਵਜੋਂ ਤμάਦਾ ਲਾਈਨ ਤੋਂ ਚਲਾਓ:

### 1. ਬੁਨਿਆਦੀ ਚੈਟ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. ਪ੍ਰਾਂਪਟ ਪੈਟਰਨ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

ਜ਼ੀਰੋ-ਸ਼ੌਟ, ਫਿਊ-ਸ਼ੌਟ, ਚੇਨ-ਆਫ-ਥੌਟ, ਅਤੇ ਰੋਲ-ਆਧਾਰਿਤ ਪ੍ਰਾਂਪਟਿੰਗ ਦਿਖਾਉਂਦਾ ਹੈ।

### 3. ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI ਜਦੋਂ ਲੋੜ ਹੋਵੇ ਤੁਹਾਡੇ ਜਾਵਾ ਫੰਕਸ਼ਨਾਂ ਨੂੰ ਆਟੋਮੈਟਿਕ ਕਾਲ ਕਰਦਾ ਹੈ।

### 4. ਦਸਤਾਵੇਜ਼ Q&A (ਆਸਾਨ RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

ਆਟੋਮੈਟਿਕ ਐਮਬੈਡਿੰਗ ਅਤੇ ਰੀਟਰਿਵਲ ਨਾਲ ਆਸਾਨ RAG ਵਰਤ ਕੇ ਆਪਣੇ ਦਸਤਾਵੇਜ਼ਾਂ ਬਾਰੇ ਸਵਾਲ ਪੁੱਛੋ।

### 5. ਜ਼ਿੰਮੇਵਾਰ AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

ਦੇਖੋ ਕਿ ਕਿਵੇਂ AI ਸੁਰੱਖਿਆ ਛਾਣ-ਬੀਣਖਾਤਰੇ ਸਮੱਗਰੀ ਨੂੰ ਰੋਕਦੀ ਹੈ।

## ਹਰ ਉਦਾਹਰਨ ਕੀ ਦਿਖਾਉਂਦੀ ਹੈ

**ਬੁਨਿਆਦੀ ਚੈਟ** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

ਇਥੋਂ ਸ਼ੁਰੂ ਕਰੋ ਤਾਂ ਕਿ LangChain4j ਦੇ ਬੁਨਿਆਦੀ ਰੂਪ ਨੂੰ ਵੇਖ ਸਕੋ। ਤੁਸੀਂ `OpenAiOfficialChatModel` ਬਣਾਵੋਗੇ, `.chat()` ਨਾਲ ਪ੍ਰਾਂਪਟ ਭੇਜੋਗੇ ਅਤੇ ਜਵਾਬ ਪ੍ਰਾਪਤ ਕਰੋਗੇ। ਇਹ ਬੁਨਿਆਦਾਂ ਨੂੰ ਦਿਖਾਉਂਦਾ ਹੈ: ਕਿਵੇਂ ਮਾਡਲਾਂ ਨੂੰ ਕਸਟਮ ਐਂਡਪੌਇੰਟ ਅਤੇ API ਕੀਜ਼ ਨਾਲ ਸ਼ੁਰੂ ਕਰਨਾ। ਇੱਕ ਵਾਰੀ ਤੁਸੀਂ ਇਹ ਸਮਝ ਜਾਓ, ਬਾਕੀ ਸਾਰਾ ਕੁਝ ਇਸ ਤੇ ਟਿਕਿਆ ਹੋਵਾ ਹੈ।

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:
> - "ਮੈਂ ਇਸ ਕੋਡ ਵਿੱਚ GitHub Models ਤੋਂ Azure OpenAI ਨੂੰ ਕਿਵੇਂ ਸਵਿੱਚ ਕਰਾਂ?"
> - "OpenAiOfficialChatModel.builder() ਵਿੱਚ ਹੋਰ ਕਿਹੜੇ ਪੈਰਾਮੀਟਰ ਸੈੱਟ ਕੀਤੇ ਜਾ ਸਕਦੇ ਹਨ?"
> - "ਮੈਂ ਕਿਵੇਂ ਸਟ੍ਰੀਮਿੰਗ ਜਵਾਬ ਸ਼ਾਮਲ ਕਰ ਸਕਦਾ ਹਾਂ ਬਜਾਏ ਕਿ ਮੁਕੰਮਲ ਜਵਾਬ ਦੀ ਉਮੀਦ ਕਰਨ ਦੇ?"

**ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

ਹੁਣ ਜਦੋਂ ਤੁਹਾਨੂੰ ਮਾਡਲ ਨਾਲ ਗੱਲ ਕਰਨੀ ਆ ਗਈ, ਦਿਖਾਈਏ ਕਿ ਤੁਸੀਂ ਉਸਨੂੰ ਕੀ ਕਹਿੰਦੇ ਹੋ। ਇਹ ਡੈਮੋ ਇਕੋ ਮਾਡਲ ਸੈੱਟਅੱਪ ਵਰਤਦਾ ਹੈ ਪਰ ਪੰਜ ਵੱਖਰੇ ਪ੍ਰਾਂਪਟਿੰਗ ਪੈਟਰਨ ਦਿਖਾਉਂਦਾ ਹੈ। ਸਿੱਧਾ ਹੁਕਮਾਂ ਲਈ ਜ਼ੀਰੋ-ਸ਼ੌਟ ਪ੍ਰਾਂਪਟ, ਉਦਾਹਰਨਾਂ ਤੋਂ ਸਿੱਖਣ ਲਈ ਫਿਊ-ਸ਼ੌਟ, ਸੋਚ ਦੇ ਕੜੇ ਤਕ ਚੇਨ ਪ੍ਰਾਂਪਟ, ਅਤੇ ਸੰਦਰਭ ਸੈਟ ਕਰਨ ਲਈ ਰੋਲ-ਆਧਾਰਿਤ ਪ੍ਰਾਂਪਟ। ਤੁਸੀਂ ਵੇਖੋਗੇ ਕਿ ਕਿਵੇਂ ਇਕੋ ਮਾਡਲ ਵੱਖ-ਵੱਖ ਨਤੀਜੇ ਦੇਂਦਾ ਹੈ ਅਧਾਰ ਤੇ ਕਿ ਤੁਸੀਂ ਕਿਵੇਂ ਸਵਾਲ ਰੱਖਦੇ ਹੋ।

ਡੈਮੋ ਪ੍ਰਾਂਪਟ ਟੈਪਲੇਟਾਂ ਵੀ ਦਿਖਾਉਂਦਾ ਹੈ, ਜੋ ਵੈਰੀਏਬਲਾਂ ਨਾਲ ਵਾਪਰਣਯੋਗ ਪ੍ਰਾਂਪਟ ਬਣਾਉਣ ਦਾ ਸ਼ਕਤੀਸ਼ਾਲੀ ਤਰੀਕਾ ਹਨ। ਹੇਠਾਂ ਇਕ ਪ੍ਰਾਂਪਟ ਦਾ ਉਦਾਹਰਨ ਹੈ ਜੋ LangChain4j ਦੇ `PromptTemplate` ਨਾਲ ਵੈਰੀਏਬਲ ਭਰਦਾ ਹੈ। AI ਦਿਓਤੀ ਗਈ ਮੰਜ਼ਿਲ ਅਤੇ ਕਿਰਿਆ ਦੇ ਅਧਾਰ 'ਤੇ ਜਵਾਬ ਦੇਵੇਗਾ।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:
> - "ਜ਼ੀਰੋ-ਸ਼ੌਟ ਅਤੇ ਫਿਊ-ਸ਼ੌਟ ਪ੍ਰਾਂਪਟਿੰਗ ਵਿੱਚ ਕੀ ਫਰਕ ਹੈ, ਅਤੇ ਮੈਂ ਕਦੋਂ ਕਿਸ ਤਰੀਕੇ ਦੀ ਵਰਤੋਂ ਕਰਾਂ?"
> - "ਟੈਂਪਰੇਚਰ ਪੈਰਾਮੀਟਰ ਮਾਡਲ ਦੇ ਜਵਾਬਾਂ 'ਤੇ ਕਿਵੇਂ ਪ੍ਰਭਾਵ ਪਾਂਦਾ ਹੈ?"
> - "ਪ੍ਰੋਡਕਸ਼ਨ ਵਿੱਚ ਪ੍ਰਾਂਪਟ ਇੰਜੈਕਸ਼ਨ ਹਮਲਿਆਂ ਨੂੰ ਰੋਕਣ ਲਈ ਕੁਝ ਤਕਨੀਕਾਂ ਕੀ ਹਨ?"
> - "ਮੈਂ ਆਮ ਪੈਟਰਨ ਲਈ ਵਾਪਰਣਯੋਗ PromptTemplate ਑ਬਜੈਕਟ ਕਿਵੇਂ ਬਣਾਵਾਂ?"

**ਟੂਲ ਇੰਟੀਗ੍ਰੇਸ਼ਨ** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ਇਹ ਹੈ LangChain4j ਦੀ ਸ਼ਕਤੀ। ਤੁਸੀਂ `AiServices` ਵਰਤ ਕੇ ਇੱਕ AI ਸਹਾਇਕ ਬਣਾਵੋਗੇ ਜੋ ਤੁਹਾਡੇ ਜਾਵਾ ਫੰਕਸ਼ਨਾਂ ਨੂੰ ਕਾਲ ਕਰ ਸਕਦਾ ਹੈ। ਸਿਰਫ਼ ਫੰਕਸ਼ਨਾਂ ਨੂੰ `@Tool("description")` ਨਾਲ ਅਨੋਟੇਟ ਕਰੋ ਅਤੇ LangChain4j ਬਾਕੀ ਕੰਮ sambhal ਲੈਂਦਾ ਹੈ - AI ਆਟੋਮੈਟਿਕ ਤੌਰ ਤੇ ਫੈਸਲਾ ਕਰਦਾ ਹੈ ਕਿ ਉਹ ਕਿਹੜਾ ਟੂਲ ਕਦੋਂ ਵਰਤੇ। ਇਹ ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ ਦਿਖਾਉਂਦਾ ਹੈ, ਇੱਕ ਮੁੱਖ ਤਕਨੀਕ ਜੋ AI ਨੂੰ ਸਿਰਫ਼ ਸਵਾਲਾਂ ਦੇ ਜਵਾਬ ਦੇਣ ਦੀ ਥਾਂ ਕਾਰਵਾਈ ਕਰਨ ਯੋਗ ਬਨਾਉਂਦੀ ਹੈ।

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:
> - "@Tool ਅਨੋਟੇਸ਼ਨ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀ ਹੈ ਅਤੇ LangChain4j ਪਿੱਛੇ ਕੀ ਕਰਦਾ ਹੈ?"
> - "ਕੀ AI ਜਟਿਲ ਸਮੱਸਿਆਵਾਂ ਨੂੰ ਹੱਲ ਕਰਨ ਲਈ ਕਈ ਟੂਲ ਕ੍ਰਮ ਵਿੱਚ ਕਾਲ ਕਰ ਸਕਦਾ ਹੈ?"
> - "ਜੇ ਕੋਈ ਟੂਲ ਐਕਸੀਪਸ਼ਨ ਵਲੈਂਦਾ ਹੈ ਤਾਂ ਕੀ ਹੁੰਦਾ ਹੈ - ਮੈਂ ਕਿਵੇਂ ਐਰਰ ਹੈਂਡਲ ਕਰਨਾ ਚਾਹੀਦਾ ਹੈ?"
> - "ਮੈਂ ਇਸ ਕੈਲਕੂਲੇਟਰ ਉਦਾਹਰਨ ਦੀ ਥਾਂ ਕਿਸੇ ਅਸਲ API ਨੂੰ ਕਿਵੇਂ ਇੰਟੀਗ੍ਰੇਟ ਕਰਾਂ?"

**ਦਸਤਾਵੇਜ਼ Q&A (ਆਸਾਨ RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ਇੱਥੇ ਤੁਸੀਂ LangChain4j ਦੇ "ਆਸਾਨ RAG" ਤਰੀਕੇ ਨਾਲ RAG (ਰੀਟ੍ਰੀਵਲ-ਆਗਮੰਨ ਜਨਰੇਸ਼ਨ) ਵੇਖੋਗੇ। ਦਸਤਾਵੇਜ਼ ਲੋਡ ਕੀਤੇ ਜਾਂਦੇ ਹਨ, ਆਪਣੇ ਆਪ ਵੰਡੇ ਜਾਂਦੇ ਹਨ ਅਤੇ ਮੇਮੋਰੀ ਸਟੋਰ ਵਿੱਚ ਐਮਬੈਡ ਹੋ ਜਾਂਦੇ ਹਨ, ਫਿਰ ਸਮੱਗਰੀ ਰੀਟਰਿਵਰ AI ਨੂੰ ਕੁਸ਼ਣ ਸਮੇਂ ਦੇ ਲਾਇਕ ਟੁਕੜੇ ਦਿੰਦਾ ਹੈ। AI ਤੁਹਾਡੇ ਦਸਤਾਵੇਜ਼ਾਂ ਦੇ ਆਧਾਰ 'ਤੇ ਜਵਾਬ ਦਿੰਦਾ ਹੈ, ਨਾ ਕਿ ਆਪਣੀ ਆਮ ਜਾਣਕਾਰੀ ਤੇ।

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:
> - "ਮਾਡਲ ਦੇ ਟ੍ਰੇਨਿੰਗ ਡੇਟਾ ਨਾਲੋਂ RAG AI ਹਲੂਸੀਨੇਸ਼ਨ ਨੂੰ ਕਿਵੇਂ ਰੋਕਦਾ ਹੈ?"
> - "ਇਹ ਆਸਾਨ ਤਰੀਕਾ ਅਤੇ ਇੱਕ ਕਸਟਮ RAG ਪਾਈਪਲਾਈਨ ਵਿੱਚ ਕੀ ਫਰਕ ਹੈ?"
> - "ਮੈਂ ਇਸਨੂੰ ਕਈ ਦਸਤਾਵੇਜ਼ਾਂ ਜਾਂ ਵੱਡੀਆਂ ਗਿਆਨ ਬੇਸਾਂ ਲਈ ਕਿਵੇਂ ਸ੍ਕੇਲ ਕਰਾਂ?"

**ਜ਼ਿੰਮੇਵਾਰ AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ਗਹਿਲਤਾ ਵਿੱਚ ਸੁਰੱਖਿਆ ਬਣਾਓ। ਇਹ ਡੈਮੋ ਦੋ ਸਰੱਖਿਆ ਪਰਤਾਂ ਨੂੰ ਦਿਖਾਉਂਦਾ ਹੈ ਜੋ ਇਕੱਠੇ ਕੰਮ ਕਰਦੀਆਂ ਹਨ:

**ਭਾਗ 1: LangChain4j ਇਨਪੁਟ ਗਾਰਡਰੇਲਜ਼** - LLM ਤੱਕ ਖਤਰਨਾਕ ਪ੍ਰਾਂਪਟਾਂ ਨੂੰ ਪਹੁੰਚਣ ਤੋਂ ਪਹਿਲਾਂ ਰੋਕੋ। ਵਿਸ਼ੇਸ਼ ਗਾਰਡਰੇਲ ਬਣਾਓ ਜੋ ਮਨਾਹੀ ਸ਼ਬਦ ਜਾਂ ਪੈਟਰਨ ਚੈੱਕ ਕਰਦੇ ਹਨ। ਇਹ ਤੁਹਾਡੇ ਕੋਡ ਵਿੱਚ ਚੱਲਦੇ ਹਨ, ਇਸ ਲਈ ਇਹ ਤੇਜ਼ ਅਤੇ ਮੁਫ਼ਤ ਹਨ।

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**ਭਾਗ 2: ਪ੍ਰੋਵਾਈਡਰ ਸੁਰੱਖਿਆ ਫਿਲਟਰ** - GitHub Models ਵਿੱਚ ਮੌਜੂਦ ਫਿਲਟਰਾਂ ਹਨ ਜੋ ਤੁਹਾਡੇ ਗਾਰਡਰੇਲਜ਼ ਤੋਂ ਰਹਿ ਗਏ ਕੁਝ ਗਲਤੀਆਂ ਨੂੰ ਫੜਦੇ ਹਨ। ਤੁਸੀਂਕੜੇ ਰੋਕ (HTTP 400 ਗਲਤੀ) ਦੇਖੋਗੇ ਜਦੋਂ ਵੱਡੀਆਂ ਉਲੰਘਣਾ ਹੁੰਦੀਆਂ ਹਨ ਅਤੇ ਨਰਮ ਰਦ ਕਰਨਾ ਜਿੱਥੇ AI ਸ਼ਾਲੀਨਤਾ ਨਾਲ ਇਨਕਾਰ ਕਰਦਾ ਹੈ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:
> - "InputGuardrail ਕੀ ਹੈ ਅਤੇ ਮੈਂ ਆਪਣੇ ਲਈ ਕਿਵੇਂ ਬਣਾਵਾਂ?"
> - "ਹਾਰਡ ਬਲੌਕ ਤੇ ਸਾਫਟ ਰੱਦ ਵਿਚ ਕੀ ਫਰਕ ਹੈ?"
> - "ਮੈਂ ਦੋਹਾਂ ਗਾਰਡਰੇਲਜ਼ ਅਤੇ ਪ੍ਰੋਵਾਈਡਰ ਫਿਲਟਰ ਇਕੱਠੇ ਕਿਉਂ ਵਰਤਾਂ?"

## ਅਗਲੇ ਕਦਮ

**ਅਗਲਾ ਮੋਡੀਊਲ:** [01-ਪ੍ਰਸਤਾਵ - LangChain4j ਅਤੇ gpt-5 ਉੱਤੇ Azure ਨਾਲ ਸ਼ੁਰੂਆਤ](../01-introduction/README.md)

---

**ਨੈਵੀਗੇਸ਼ਨ:** [← ਮੁੱਖ ਨੂੰ ਵਾਪਸ](../README.md) | [ਅਗਲਾ: ਮੋਡੀਊਲ 01 - ਪ੍ਰਸਤਾਵ →](../01-introduction/README.md)

---

## ਟ੍ਰਬਲਸ਼ੂਟਿੰਗ

### ਪਹਿਲੀ ਵਾਰੀ Maven ਬਣਾਉਣਾ

**ਸਮੱਸਿਆ**: ਪਹਿਲੀ ਵਾਰੀ `mvn clean compile` ਜਾਂ `mvn package` ਬਹੁਤ ਸਮਾਂ ਲੈਂਦਾ ਹੈ (10-15 ਮਿੰਟ)

**ਕਾਰਨ**: Maven ਨੂੰ ਪਹਿਲੀ ਵਾਰੀ ਸਾਰੇ ਪ੍ਰੋਜੈਕਟ ਡੀਪੈਂਡੈਂਸੀਜ਼ (Spring Boot, LangChain4j ਲਾਇਬ੍ਰੇਰੀਜ਼, Azure SDKs, ਆਦਿ) ਡਾਊਨਲੋਡ ਕਰਨੀ ਪੈਂਦੀ ਹੈ।

**ਹੱਲ**: ਇਹ ਆਮ ਹੈ। ਅਗਲੇ ਬਣਾਉਣ ਬਹੁਤ ਤੇਜ਼ ਹੋਣਗੇ ਕਿਉਂਕਿ ਡੀਪੈਂਡੈਂਸੀਜ਼ਲੀ ਸਥਾਨਕ ਤੌਰ ਤੇ ਕੈਸ਼ ਹੋ ਜਾਂਦੀਆਂ ਹਨ। ਡਾਊਨਲੋਡ ਸਮਾਂ ਤੁਹਾਡੇ ਨੈੱਟਵਰਕ ਦੀ ਗਤੀ ਤੇ ਨਿਰਭਰ ਕਰਦਾ ਹੈ।

### PowerShell Maven ਕਮਾਂਡ ਸਿੰਟੈਕਸ

**ਸਮੱਸਿਆ**: Maven ਕਮਾਂਡ `{Unknown lifecycle phase ".mainClass=..."}` ਗਲਤੀ ਦਿੰਦਾ ਹੈ।
**ਕਾਰਨ**: PowerShell `=` ਨੂੰ ਇਕ ਵੈਰੀਏਬਲ ਐਸਾਈਨਮੈਂਟ ਓਪਰੇਟਰ ਵੱਜੋਂ ਸਮਝਦਾ ਹੈ, ਜਿਸ ਨਾਲ Maven ਪ੍ਰਾਪਰਟੀ ਸਿੰਟੈਕਸ ਟੁੱਟ ਜਾਂਦਾ ਹੈ

**ਹੱਲ**: Maven ਕਮਾਂਡ ਤੋਂ ਪਹਿਲਾਂ stop-parsing ਓਪਰੇਟਰ `--%` ਦੀ ਵਰਤੋਂ ਕਰੋ:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ਓਪਰੇਟਰ PowerShell ਨੂੰ ਕਹਿੰਦਾ ਹੈ ਕਿ ਉਹ Maven ਨੂੰ ਬਾਕੀ ਸਾਰੇ ਆਰਗੁਮੈਂਟ ਬਿਨਾਂ ਕਿਸੇ ਵਿਆਖਿਆ ਦੇ ਲਿਟਰੇਲੀ ਭੇਜੇ।

### Windows PowerShell ਇਮੋਜੀ ਦਿਖਾਵਾ

**ਮੁੱਦਾ**: PowerShell ਵਿੱਚ AI ਜਵਾਬਾਂ ਵਿੱਚ ਇਮੋਜੀ ਦੀ ਬਜਾਏ ਗਾਰਬੇਜ ਨੂੰਡਰਵੇ (ਜਿਵੇਂ `????` ਜਾਂ `â??`) ਵੇਖਾਈ ਦਿੰਦੇ ਹਨ

**ਕਾਰਨ**: PowerShell ਦੀ ਡਿਫੌਲਟ ਐਨਕੋਡਿੰਗ UTF-8 ਇਮੋਜੀ ਨੂੰ ਸਹੀ ਤਰ੍ਹਾਂ ਸਮਰਥਨ ਨਹੀਂ ਕਰਦੀ

**ਹੱਲ**: Java ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਚਲਾਉਣ ਤੋਂ ਪਹਿਲਾਂ ਇਹ ਕਮਾਂਡ ਚਲਾਓ:
```cmd
chcp 65001
```

ਇਹ ਟਰਮੀਨਲ ਵਿੱਚ UTF-8 ਐਨਕੋਡਿੰਗ ਨੂੰ ਬਲਵਦਾ ਹੈ। ਵਿਕਲਪ ਵਜੋਂ, Windows Terminal ਵਰਤੋਂ ਜੋ ਬਿਹਤਰ ਯੂਨੀਕੋਡ ਸਹਾਇਤਾ ਰੱਖਦਾ ਹੈ।

### API ਕਾਲਾਂ ਦੀ ਡੀਬੱਗਿੰਗ

**ਮੁੱਦਾ**: 인증 ਕਰਨੀ ਸਮੱਸਿਆਵਾਂ, ਦਰ ਸੀਮਾਵਾਂ ਜਾਂ AI ਮਾਡਲ ਤੋਂ ਅਣਪਛਾਤੇ ਜਵਾਬ

**ਹੱਲ**: ਉਦਾਹਰਣਾਂ ਵਿੱਚ `.logRequests(true)` ਅਤੇ `.logResponses(true)` ਸ਼ਾਮਿਲ ਹਨ ਜੋ ਕਨਸੋਲ ਵਿੱਚ API ਕਾਲਾਂ ਦਿਖਾਉਂਦੇ ਹਨ। ਇਸ ਨਾਲ 인증 ਸੰਬੰਧੀ ਸਮੱਸਿਆਵਾਂ, ਦਰ ਸੀਮਾਵਾਂ ਜਾਂ ਅਣਪਛਾਤੇ ਜਵਾਬਾਂ ਦੀ ਜਾਂਚ ਵਿੱਚ ਮਦਦ ਮਿਲਦੀ ਹੈ। ਉਤਪਾਦਨ ਵਿੱਚ ਇਹ ਝੰਡੇ ਹਟਾ ਕੇ ਲੌਗ ਸ਼ੋਰ ਘਟਾਇਆ ਜਾ ਸਕਦਾ ਹੈ।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਵੀਕ੍ਰਿਤੀ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਅਤਾ ਲਈ ਯਤਨ ਕਰਦੇ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਵਿੱਚ ਰੱਖੋ ਕਿ ਆਟੋਮੈਟਿਕ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਕੁਝ ਗਲਤੀਆਂ ਜਾਂ ਅਸਪਸ਼ਟਤਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਨੂੰ ਇਸਦੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਹੀ ਪ੍ਰਮਾਣਿਕ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਜਰੂਰੀ ਜਾਣਕਾਰੀ ਲਈ ਜਾਂਚਿਆ ਹੋਇਆ ਮਨੁੱਖੀ ਪੇਸ਼ੇਵਰ ਅਨੁਵਾਦ ਸਿਫਾਰਸ਼ੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਪੈਦਾਬੰਦ ਦੀਆਂ ਗਲਤਫਹਿਮੀਆਂ ਜਾਂ ਵਿਗੜੇ ਅਰਥਾਂ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->