# ਮੋਡੀਊਲ 00: ਕਵਿਕ ਸਟਾਰਟ

## ਸੂਚੀ

- [ਪਰਚਯ](../../../00-quick-start)
- [LangChain4j ਕੀ ਹੈ?](../../../00-quick-start)
- [LangChain4j Dependencies](../../../00-quick-start)
- [ਪੂਰਵ-ਸ਼ਰਤਾਂ](../../../00-quick-start)
- [ਸੈਟਅੱਪ](../../../00-quick-start)
  - [1. ਆਪਣਾ GitHub ਟੋਕਨ ਪ੍ਰਾਪਤ ਕਰੋ](../../../00-quick-start)
  - [2. ਆਪਣਾ ਟੋਕਨ ਸੈਟ ਕਰੋ](../../../00-quick-start)
- [ਉਦਾਹਰਨਾਂ ਚਲਾਓ](../../../00-quick-start)
  - [1. ਬੁਨਿਆਦੀ ਚੈਟ](../../../00-quick-start)
  - [2. ਪ੍ਰੋੰਪਟ ਪੈਟਰਨ](../../../00-quick-start)
  - [3. ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ](../../../00-quick-start)
  - [4. ਦਸਤਾਵੇਜ਼ Q&A (RAG)](../../../00-quick-start)
  - [5. ਜਿੰਮੇਵਾਰ AI](../../../00-quick-start)
- [ਹਰ ਉਦਾਹਰਨ ਕੀ ਦਿਖਾਉਂਦੀ ਹੈ](../../../00-quick-start)
- [ਅਗਲੇ ਕਦਮ](../../../00-quick-start)
- [ਟ੍ਰਾਬਲਸ਼ੂਟਿੰਗ](../../../00-quick-start)

## ਪਰਚਯ

ਇਹ ਕਵਿਕਸਟਾਰਟ ਤੁਹਾਨੂੰ LangChain4j ਨਾਲ ਜਿੰਨੀ ਜਲਦੀ ਹੋ ਸਕੇ ਸ਼ੁਰੂ ਕਰਨ ਲਈ ਬਣਾਇਆ ਗਿਆ ਹੈ। ਇਹ LangChain4j ਅਤੇ GitHub ਮਾਡਲਾਂ ਨਾਲ AI ਐਪਲੀਕੇਸ਼ਨਾਂ ਬਣਾਉਣ ਦੇ ਬਿਲਕੁਲ ਬੁਨਿਆਦੀ ਪੱਖਾਂ ਨੂੰ ਕਵਰ ਕਰਦਾ ਹੈ। ਅਗਲੇ ਮੋਡੀਊਲਾਂ ਵਿੱਚ ਤੁਸੀਂ Azure OpenAI ਨੂੰ LangChain4j ਨਾਲ ਵਰਤ ਕੇ ਹੋਰ ਵਿਕਸਿਤ ਐਪਲੀਕੇਸ਼ਨਾਂ ਬਣਾਵੋਗੇ।

## LangChain4j ਕੀ ਹੈ?

LangChain4j ਇੱਕ ਜਾਵਾ ਲਾਇਬ੍ਰੇਰੀ ਹੈ ਜੋ AI-ਚਲਿਤ ਐਪਲੀਕੇਸ਼ਨਾਂ ਬਣਾਉਣਾ ਆਸਾਨ ਬਣਾਉਂਦੀ ਹੈ। HTTP ਕਲਾਇੰਟ ਅਤੇ JSON ਪਾਰਸਿੰਗ ਨਾਲ ਜੁਝਣ ਦੀ ਬਜਾਏ, ਤੁਸੀਂ ਸਾਫ਼-ਸੁਥਰੀ ਜਾਵਾ APIs ਨਾਲ ਕੰਮ ਕਰਦੇ ਹੋ।

LangChain ਵਿੱਚ "ਚੇਨ" ਬਹੁਤ ਸਾਰੇ ਕੰਪੋਨੈਂਟ ਇਕੱਠੇ ਜੁੜਨ ਨੂੰ ਕਹਿੰਦਾ ਹੈ - ਤੁਸੀਂ ਪ੍ਰੋੰਪਟ ਨੂੰ ਮਾਡਲ ਨਾਲ, ਫਿਰ ਪਾਰਸਰ ਨਾਲ ਜੁੜ ਸਕਦੇ ਹੋ, ਜਾਂ ਕਈ AI ਕਾਲਾਂ ਨੂੰ ਲੜੀਵਾਰ ਜੋੜ ਸਕਦੇ ਹੋ ਜਿੱਥੇ ਇੱਕ ਦਾ ਆਉਟਪੁੱਟ ਦੂਜੇ ਲਈ ਇਨਪੁੱਟ ਬਣਦਾ ਹੈ। ਇਹ ਕਵਿਕਸਟਾਰਟ ਬੁਨਿਆਦਾਂ 'ਤੇ ਧਿਆਨ ਦਿੰਦਾ ਹੈ ਇਸ ਤੋਂ ਪਹਿਲਾਂ ਕਿ ਤੁਸੀਂ ਹੋਰ ਜਟਿਲ ਚੇਨਾਂ ਨੂੰ ਖੋਜੋ।

<img src="../../../translated_images/pa/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j ਵਿੱਚ ਕੰਪੋਨੈਂਟਾਂ ਦੀ ਚੇਨਿੰਗ - ਬਣਾਵਟੀਆਂ ਜੋ ਪਾਵਰਫੁਲ AI ਵਰਕਫਲੋ ਬਣਾਉਂਦੀਆਂ ਹਨ*

ਅਸੀਂ ਤਿੰਨ ਮੁੱਖ ਕੰਪੋਨੈਂਟ ਵਰਤਾਂਗੇ:

**ChatLanguageModel** - AI ਮਾਡਲ ਇੰਟਰੈਕਸ਼ਨਾਂ ਦਾ ਇੰਟਰਫੇਸ। `model.chat("prompt")` ਕਾਲ ਕਰੋ ਅਤੇ ਇਕ ਜਵਾਬੀ ਸਤਰ ਪ੍ਰਾਪਤ ਕਰੋ। ਅਸੀਂ `OpenAiOfficialChatModel` ਵਰਤਦੇ ਹਾਂ ਜੋ GitHub ਮਾਡਲ ਵਰਗੇ OpenAI-ਮੈਚਿੰਗ ਏਂਡਪੌਇੰਟਾਂ ਨਾਲ ਕੰਮ ਕਰਦਾ ਹੈ।

**AiServices** - ਪ੍ਰਕਾਰ-ਸੁਰੱਖਿਅਤ AI ਸੇਵਾ ਇੰਟਰਫੇਸ ਬਣਾਉਂਦਾ ਹੈ। ਮੇਥਡ ਪਰਿਭਾਸ਼ਿਤ ਕਰੋ, ਉਨ੍ਹਾਂ ਨੂੰ `@Tool` ਨਾਲ ਐਨੋਟੇਟ ਕਰੋ, ਅਤੇ LangChain4j ਆਟੋਮੈਟਿਕ ਉਹਨਾਂ ਦੀ ਸਮੇਂ-ਸਾਰਣੀ ਸੰਭਾਲਦਾ ਹੈ। AI ਤੁਹਾਡੇ ਜਾਵਾ ਮੇਥਡਾਂ ਨੂੰ ਜਰੂਰਤ ਪੈਣ 'ਤੇ ਕਾਲ ਕਰਦਾ ਹੈ।

**MessageWindowChatMemory** - ਗੱਲਬਾਤ ਦਾ ਇਤਿਹਾਸ ਸੰਭਾਲਦਾ ਹੈ। ਇਸ ਦੇ ਬਿਨਾ, ਹਰ ਬੇਨਤੀ ਸਵਤੰਤਰ ਹੁੰਦੀ ਹੈ। ਪਰ ਇਸ ਨਾਲ, AI ਪਿਛਲੇ ਸੁਨੇਹੇ ਯਾਦ ਰੱਖਦਾ ਹੈ ਅਤੇ ਕਈ ਗੋਲੀਆਂ ਵਿੱਚ ਸੰਦਰਭ ਬਣਾਈ ਰੱਖਦਾ ਹੈ।

<img src="../../../translated_images/pa/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j ਆਰਕੀਟੈਕਚਰ - ਕੋਰ ਕੰਪੋਨੈਂਟ ਜੋ ਤੁਹਾਡੇ AI ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਤਾਕਤ ਦਿੰਦੇ ਹਨ*

## LangChain4j Dependencies

ਇਹ ਕਵਿਕਸਟਾਰਟ [`pom.xml`](../../../00-quick-start/pom.xml) ਵਿੱਚ ਦੋ Maven dependencies ਵਰਤਦਾ ਹੈ:

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
```

`langchain4j-open-ai-official` ਮੋਡੀਊਲ `OpenAiOfficialChatModel` ਕਲਾਸ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ ਜੋ OpenAI-ਮੈਚਿੰਗ APIs ਨਾਲ ਜੁੜਦਾ ਹੈ। GitHub ਮਾਡਲ ਇਸੇ API ਫਾਰਮੈਟ ਨੂੰ ਵਰਤਦੇ ਹਨ, ਇਸ ਲਈ ਕਿਸੇ ਖਾਸ ਅਡਾਪਟਰ ਦੀ ਲੋੜ ਨਹੀਂ - ਸਿਰਫ਼ ਬੇਸ URL ਨੂੰ `https://models.github.ai/inference` ਵੱਲ ਤੁਹਾਢਾ ਪੁਆਇੰਟਿੰਗ ਕਰੋ।

## ਪੂਰਵ-ਸ਼ਰਤਾਂ

**Dev Container ਵਰਤ ਰਹੇ ਹੋ?** ਜਾਵਾ ਅਤੇ Maven ਪੂਰੇ ਤਰੀਕੇ ਨਾਲ ਇੰਸਟਾਲ ਹਨ। ਤੁਹਾਨੂੰ ਸਿਰਫ਼ ਇੱਕ GitHub Personal Access Token ਦੀ ਲੋੜ ਹੈ।

**ਲੋਕਲ ਵਿਕਾਸ:**
- ਜਾਵਾ 21+, Maven 3.9+
- GitHub Personal Access Token (ਹੇਠਾਂ ਹਦਾਇਤਾਂ)

> **ਨੋਟ:** ਇਹ ਮੋਡੀਊਲ GitHub ਮਾਡਲਾਂ ਤੋਂ `gpt-4.1-nano` ਵਰਤਦਾ ਹੈ। ਕੋਡ ਵਿੱਚ ਮਾਡਲ ਦਾ ਨਾਮ ਨਾ ਬਦਲੋ - ਇਹ GitHub ਦੇ ਉਪਲਬਧ ਮਾਡਲਾਂ ਲਈ ਤਿਆਰ ਹੈ।

## ਸੈਟਅੱਪ

### 1. ਆਪਣਾ GitHub ਟੋਕਨ ਪ੍ਰਾਪਤ ਕਰੋ

1. ਜਾਓ [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. "Generate new token" ਤੇ ਕਲਿੱਕ ਕਰੋ
3. ਇੱਕ ਵੇਰਵਾ-ਯੋਗ ਨਾਮ ਦੇਵੋ (ਜੈਿਵੇਂ "LangChain4j Demo")
4. ਮਿਆਦ ਨਿਰਧਾਰਤ ਕਰੋ (7 ਦਿਨ ਸਿਫ਼ਾਰਸ਼ੀ)
5. "Account permissions" ਹੇਠਾਂ "Models" ਨੂੰ "Read-only" ਸੈਟ ਕਰੋ
6. "Generate token" ਤੇ ਕਲਿੱਕ ਕਰੋ
7. ਟੋਕਨ ਕਾਪੀ ਅਤੇ ਸੰਭਾਲੋ - ਇਹ ਦੁਬਾਰਾ ਨਹੀਂ ਦਿਖਾਇਆ ਜਾਵੇਗਾ

### 2. ਆਪਣਾ ਟੋਕਨ ਸੈਟ ਕਰੋ

**ਵਿਕਲਪ 1: VS Code ਵਰਤ ਕੇ (ਸਿਫ਼ਾਰਸ਼ੀ)**

ਜੇ ਤੁਸੀਂ VS Code ਵਰਤ ਰਹੇ ਹੋ, ਆਪਣਾ ਟੋਕਨ ਪ੍ਰੋਜੈਕਟ ਰੂਟ ਵਿੱਚ `.env` ਫਾਇਲ ਵਿੱਚ ਸ਼ਾਮਲ ਕਰੋ:

ਜੇ `.env` ਫਾਇਲ ਨਹੀਂ ਹੈ, ਤਾਂ `.env.example` ਨੂੰ `.env` ਵਿੱਚ ਕਾਪੀ ਕਰੋ ਜਾਂ ਨਵੀਂ `.env` ਫਾਇਲ ਬਣਾਓ।

**ਉਦਾਹਰਨ `.env` ਫਾਇਲ:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env ਵਿਚ
GITHUB_TOKEN=your_token_here
```

ਫਿਰ ਤੁਸੀਂ ਕਿਸੇ ਵੀ ਡੈਮੋ ਫਾਇਲ (ਜੈਿਵੇਂ `BasicChatDemo.java`) 'ਤੇ ਇਕਲਿੱਕ ਕਰਕੇ **"Run Java"** ਚੁਣ ਸਕਦੇ ਹੋ ਜਾਂ Run ਅਤੇ Debug ਪੈਨਲ ਦੀ ਲਾਂਚ ਕੰਫਿਗਰੇਸ਼ਨ ਵਰਤ ਸਕਦੇ ਹੋ।

**ਵਿਕਲਪ 2: ਟਰਮੀਨਲ ਵਰਤ ਕੇ**

ਟੋਕਨ ਨੂੰ ਐਨਵਾਇਰਨਮੈਂਟ ਵੈਰੀਏਬਲ ਵਜੋਂ ਸੈਟ ਕਰੋ:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## ਉਦਾਹਰਨਾਂ ਚਲਾਓ

**VS Code ਵਰਤਦਿਆਂ:** ਬੱਸ ਕਿਸੇ ਵੀ ਡੈਮੋ ਫਾਇਲ 'ਤੇ ਰਾਈਟ-ਕਲਿੱਕ ਕਰੋ ਅਤੇ **"Run Java"** ਚੁਣੋ, ਜਾਂ Run ਅਤੇ Debug ਪੈਨਲ ਤੋਂ ਲਾਂਚ ਕੰਫਿਗਰੇਸ਼ਨ ਵਰਤੋਂ (ਪਹਿਲਾਂ `.env` ਵਿੱਚ ਟੋਕਨ ਸ਼ਾਮਲ ਕਰਨਾ ਯਕੀਨੀ ਬਣਾਓ)।

**Maven ਵਰਤਦਿਆਂ:** ਵਿਕਲਪ ਤੌਰ 'ਤੇ, ਤੁਸੀਂ ਕਮਾਂਡ ਲਾਈਨ ਤੋਂ ਚਲਾ ਸਕਦੇ ਹੋ:

### 1. ਬੁਨਿਆਦੀ ਚੈਟ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. ਪ੍ਰੋੰਪਟ ਪੈਟਰਨ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

ਜ਼ੀਰੋ-ਸ਼ਾਟ, ਫਿਊ-ਸ਼ਾਟ, ਚੇਨ-ਆਫ-ਥਾਟ, ਤੇ ਰੋਲ ਆਧਾਰਿਤ ਪ੍ਰੋੰਪਟਿੰਗ ਦਿਖਾਉਂਦਾ ਹੈ।

### 3. ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI ਜਰੂਰਤ ਪੈਣ 'ਤੇ ਤੁਹਾਡੇ ਜਾਵਾ ਮੇਥਡਾਂ ਨੂੰ ਆਟੋਮੈਟਿਕ ਕਾਲ ਕਰਦਾ ਹੈ।

### 4. ਦਸਤਾਵੇਜ਼ Q&A (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt` ਵਿਚ ਮੌਜੂਦ ਸਮੱਗਰੀ ਬਾਰੇ ਸਵਾਲ ਪੁੱਛੋ।

### 5. ਜਿੰਮੇਵਾਰ AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

ਵੇਖੋ ਕਿ ਕਿਵੇਂ AI ਸੁਰੱਖਿਆ ਫਿਲਟਰ ਨੁਕਸਾਨਦੇਹ ਸਮੱਗਰੀ ਨੂੰ ਰੋਕਦੇ ਹਨ।

## ਹਰ ਉਦਾਹਰਨ ਕੀ ਦਿਖਾਉਂਦੀ ਹੈ

**ਬੁਨਿਆਦੀ ਚੈਟ** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

ਇੱਥੋਂ ਸ਼ੁਰੂ ਕਰੋ ਤਾਂ ਕਿ LangChain4j ਦੀ ਸਭ ਤੋਂ ਸਾਦਾ ਤਹਿ ਸਮਝ ਸਕੋ। ਤੁਸੀਂ ਇੱਕ `OpenAiOfficialChatModel` ਬਣਾਵੋਗੇ, `.chat()` ਨਾਲ ਪ੍ਰੋੰਪਟ ਭੇਜੋਗੇ, ਅਤੇ ਜਵਾਬ ਵਾਪਸ ਮਿਲੇਗਾ। ਇਹ ਦਰਸਾਉਂਦਾ ਹੈ ਕਿ ਕਿਵੇਂ ਕਸਟਮ ਏਂਡਪੌਇੰਟ ਅਤੇ API ਕੀ ਨਾਲ ਮਾਡਲ ਸ਼ੁਰੂ ਕਰਦੇ ਹਨ। ਜਦੋਂ ਤੁਸੀਂ ਇਹ ਪੈਟਰਨ ਸਮਝ ਲਓਗੇ, ਹਰ ਚੀਜ਼ ਇਸ ਤੇ ਅਧਾਰਿਤ ਬਣੇਗੀ।

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** ਖੋਲ੍ਹੋ [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ਅਤੇ ਪੁੱਛੋ:
> - "ਮੈਂ ਇਸ ਕੋਡ ਵਿੱਚ GitHub ਮਾਡਲ ਤੋਂ Azure OpenAI ਤੇ ਕਿਵੇਂ ਸਵਿੱਚ ਕਰਾਂ?"
> - "OpenAiOfficialChatModel.builder() ਵਿੱਚ ਹੋਰ ਕਿਹੜੇ ਪੈਰਾਮੀਟਰ ਮੈਂ ਸੈਟ ਕਰ ਸਕਦਾ ਹਾਂ?"
> - "ਮੈਂ ਪੂਰੇ ਜਵਾਬ ਦੀ ਉਡੀਕ ਦੇ ਵਿਸਥਾਰ ਦੇ ਬਜਾਏ ਸਟ੍ਰੀਮਿੰਗ ਜਵਾਬ ਕਿਵੇਂ ਜੋੜ ਸਕਦਾ ਹਾਂ?"

**ਪ੍ਰੋੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

ਹੁਣਕਿ ਤੁਹਾਨੂੰ ਮਾਡਲ ਨਾਲ ਗੱਲ ਕਰਨ ਦਾ ਤਰੀਕਾ ਆ ਗਿਆ ਹੈ, ਆਓ ਵੇਖੀਏ ਤੁਸੀਂ ਮਾਡਲ ਨੂੰ ਕੀ ਕਹਿੰਦੇ ਹੋ। ਇਹ ਡੈਮੋ ਇੱਕੋ ਮਾਡਲ ਸੈਟਅੱਪ ਵਰਤਦਾ ਹੈ ਪਰ ਪੰਜ ਵੱਖਰੇ ਪ੍ਰੋੰਪਟ ਪੈਟਰਨ ਦਿਖਾਉਂਦਾ ਹੈ। ਸਿੱਧਾ ਹੁਕਮਾਂ ਲਈ ਜ਼ੀਰੋ-ਸ਼ਾਟ ਪ੍ਰੋੰਪਟ, ਉਦਾਹਰਨਾਂ ਤੋਂ ਸਿੱਖਣ ਵਾਲੇ ਫਿਊ-ਸ਼ਾਟ, ਸੋਚ-ਦਾ-ਚੇਨ ਪ੍ਰੋੰਪਟ ਅਤੇ ਸੰਦਰਭ ਸੈੱਟ ਕਰਦੇ ਰੋਲ-ਅਧਾਰਿਤ ਪ੍ਰੋੰਪਟ ਟ੍ਰਾਈ ਕਰੋ। ਤੁਸੀਂ ਵੇਖੋਗੇ ਕਿ ਕਿਵੇਂ ਇੱਕੋ ਮਾਡਲ ਵੱਖ-ਵੱਖ ਨਤੀਜੇ ਦਿੰਦਾ ਹੈ ਪ੍ਰੋੰਪਟ ਦੇ ਤਰੀਕੇ ਅਨੁਸਾਰ।

ਡੈਮੋ ਪ੍ਰੋੰਪਟ ਟੈਮਪਲੇਟ ਵੀ ਦਿਖਾਉਂਦਾ ਹੈ ਜੋ ਚਲਾਓਣਯੋਗ ਪ੍ਰੋੰਪਟ ਵੈਰੀਏਬਲਾਂ ਨਾਲ ਬਣਾਉਣ ਦਾ ਸ਼ਕਤੀਸ਼ਾਲੀ ਤਰੀਕਾ ਹੈ।
ਹੇਠਾਂ ਉਦਾਹਰਨ LangChain4j ਦੇ `PromptTemplate` ਦੇ ਵਰਤੋਂ ਨਾਲ ਪ੍ਰੋੰਪਟ ਵਿੱਚ ਵੈਰੀਏਬਲ ਭਰਨ ਦਿਖਾਉਂਦੀ ਹੈ। AI ਦਿੱਤੇ ਗਏ ਮੰਜਿਲ ਅਤੇ ਕਿਰਿਆ ਦੇ ਆਧਾਰ ਤੇ ਜਵਾਬ ਦੇਵੇਗਾ।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** ਖੋਲ੍ਹੋ [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ਅਤੇ ਪੁੱਛੋ:
> - "ਜ਼ੀਰੋ-ਸ਼ਾਟ ਅਤੇ ਫਿਊ-ਸ਼ਾਟ ਪ੍ਰੋੰਪਟ ਵਿੱਚ ਕੀ ਫ਼ਰਕ ਹੈ ਅਤੇ ਮੈਂ ਕਦੋਂ ਕਿਹੜਾ ਵਰਤਾਂ?"
> - "ਮਾਡਲ ਦੇ ਜਵਾਬਾਂ 'ਤੇ ਤਾਪਮਾਨ ਪੈਰਾਮੀਟਰ ਕਿਵੇਂ ਪ੍ਰਭਾਵ ਪਾਉਂਦਾ ਹੈ?"
> - "ਉਤਪਾਦਨ ਵਿੱਚ ਪ੍ਰੋੰਪਟ ਇੰਜੈਕਸ਼ਨ ਹਮਲਿਆਂ ਨੂੰ ਰੋਕਣ ਲਈ ਕੀ ਤਕਨੀਕਾਂ ਹਨ?"
> - "ਆਮ ਪੈਟਰਨ ਲਈ ਦੁਬਾਰਾ ਵਰਤ ਸਕਣ ਵਾਲੇ PromptTemplate ਵਸਤੂਆਂ ਕਿਵੇਂ ਬਣਾਈਏ?"

**ਟੂਲ ਇੰਟੀਗ੍ਰੇਸ਼ਨ** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ਇੱਥੇ LangChain4j ਤਾਕਤਵਰ ਬਣਦਾ ਹੈ। ਤੁਸੀਂ `AiServices` ਵਰਤ ਕੇ ਇੱਕ AI ਸਹਾਇਕ ਬਣਾਉਂਦੇ ਹੋ ਜੋ ਤੁਹਾਡੇ ਜਾਵਾ ਮੇਥਡਾਂ ਨੂੰ ਕਾਲ ਕਰ ਸਕਦਾ ਹੈ। ਸਿਰਫ਼ ਮੇਥਡਾਂ ਨੂੰ `@Tool("description")` ਨਾਲ ਐਨੋਟੇਟ ਕਰੋ ਅਤੇ LangChain4j ਬਾਕੀ ਕੰਮ ਸੰਭਾਲਦਾ ਹੈ - AI ਆਪਣੇ ਆਪ ਜੋ ਉਪਭੋਗਤਾ ਪੁੱਛਦਾ ਹੈ ਉਸ ਮੁਤਾਬਕ ਸੰਦ ਵਰਤਦਾ ਹੈ। ਇਹ ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ ਦਿਖਾਉਂਦਾ ਹੈ, ਜੋ ਕੇ AI ਨੂੰ ਸਿਰਫ ਸਵਾਲਾਂ ਦੇ ਜਵਾਬ ਦੇਣ ਦੇ ਬਜਾਏ ਕੰਮ ਕਰਨ ਦਾ ਤਰੀਕਾ ਹੈ।

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** ਖੋਲ੍ਹੋ [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ਅਤੇ ਪੁੱਛੋ:
> - "`@Tool` ਐਨੋਟੇਸ਼ਨ ਕਿਵੇਂ ਕੰਮ ਕਰਦਾ ਹੈ ਅਤੇ LangChain4j ਪਿੱਛੇ ਕੀ ਕਰਦਾ ਹੈ?"
> - "ਕੀ AI ਕਈ ਟੂਲ ਲੜੀਵਾਰ ਵਰਤ ਸਕਦਾ ਹੈ ਮੁਸ਼ਕਲ ਸਮੱਸਿਆਵਾਂ ਹੱਲ ਕਰਨ ਲਈ?"
> - "ਜੇ ਟੂਲ ਤੋਂ ਕੋਈ ਗਲਤੀ ਆਏ ਤਾਂ ਮੈਂ ਕਿਵੇਂ ਸੰਭਾਲਾਂ?"
> - "ਇਸ ਕੈਲਕิวਲੇਟਰ ਉਦਾਹਰਨ ਦੀ ਥਾਂ ਅਸਲੀ API ਕਿਵੇਂ ਜੋੜਾਂ?"

**ਦਸਤਾਵੇਜ਼ Q&A (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ਇੱਥੇ ਤੁਸੀਂ RAG (retrieval-augmented generation) ਦੀ ਬੁਨਿਆਦ ਵੇਖੋਗੇ। ਮਾਡਲ ਦੀ ਟ੍ਰੇਨਿੰਗ ਡੇਟਾ ਉੱਤੇ ਨਿਰਭਰ ਕਰਨ ਦੀ ਥਾਂ, ਤੁਸੀਂ ਸਮੱਗਰੀ `[document.txt](../../../00-quick-start/document.txt)` ਤੋਂ ਲੋਡ ਕਰਕੇ ਪ੍ਰੋੰਪਟ ਵਿੱਚ ਸ਼ਾਮਲ ਕਰਦੇ ਹੋ। AI ਤੁਹਾਡੇ ਦਸਤਾਵੇਜ਼ ਤੇ ਆਧਾਰਿਤ ਜਵਾਬ ਦਿੰਦਾ ਹੈ, ਆਪਣੀ ਸਧਾਰਣ ਜਾਣਕਾਰੀ 'ਤੇ ਨਹੀਂ। ਇਹ ਪਹਿਲਾ ਕਦਮ ਹੈ ਜਿੱਥੇ ਤੁਸੀਂ ਆਪਣੇ ਡੇਟਾ ਨਾਲ ਕੰਮ ਕਰਦੇ ਸਿਸਟਮ ਬਣਾਉਂਦੇ ਹੋ।

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **ਨੋਟ:** ਇਹ ਆਸਾਨ ਤਰੀਕਾ ਸਾਰੇ ਦਸਤਾਵੇਜ਼ ਨੂੰ ਪ੍ਰੋੰਪਟ ਵਿੱਚ ਭਰ ਲੈਂਦਾ ਹੈ। ਵੱਡੀ ਫਾਇਲਾਂ (>10KB) ਲਈ ਤੁਸੀਂ ਕਾਂਟੈਕਸਟ ਸੀਮਾਵਾਂ ਨੂੰ ਪਾਰ ਕਰ ਜਾਵੋਗੇ। ਮੋਡੀਊਲ 03 ਚੰਕਿੰਗ ਅਤੇ ਵੈਕਟਰ ਖੋਜ ਬਣਾਉਂਦਾ ਹੈ ਪ੍ਰੋਡਕਸ਼ਨ ਵਾਲੇ RAG ਸਿਸਟਮਾਂ ਲਈ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** ਖੋਲ੍ਹੋ [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ਅਤੇ ਪੁੱਛੋ:
> - "ਮਾਡਲ ਦੀ ਟ੍ਰੇਨਿੰਗ ਡੇਟਾ ਦੇ ਬਜਾਏ RAG ਕਿਵੇਂ AI ਹਲੂਸੀਨੇਸ਼ਨਾਂ ਨੂੰ ਰੋਕਦਾ ਹੈ?"
> - "ਇਸ ਸਾਦੇ ਤਰੀਕੇ ਅਤੇ ਵੈਕਟਰ ਐਮਬੈਡਿੰਗ retrieval ਵਿਚ ਕੀ ਫ਼ਰਕ ਹੈ?"
> - "ਮੈਂ ਇਸ ਨੂੰ ਕਈ ਦਸਤਾਵੇਜ਼ਾਂ ਜਾਂ ਵੱਡੇ ਗਿਆਨ ਬੇਸਾਂ ਲਈ ਕਿਵੇਂ ਵਧਾ ਸਕਦਾ ਹਾਂ?"
> - "ਪ੍ਰੋੰਪਟ ਨੂੰ ਇਸ ਤਰੀਕੇ ਨਾਲ ਬਣਾਉਣ ਦੇ ਬਿਹਤਰ ਤਰੀਕੇ ਕੀ ਹਨ ਜੋ AI ਸਿਰਫ ਦਿੱਤੇ ਸੰਦਰਭ ਨੂੰ ਵਰਤੇ?"

**ਜਿੰਮੇਵਾਰ AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ਗਹਿਰਾਈ ਨਾਲ AI ਸੁਰੱਖਿਆ ਬਣਾਓ। ਇਹ ਡੈਮੋ ਦੋ ਸਤਰਾਂ ਦੀ ਸੁਰੱਖਿਆ ਦਿਖਾਉਂਦਾ ਹੈ ਜੋ ਇਕੱਠੇ ਕੰਮ ਕਰਦੀਆਂ ਹਨ:

**ਭਾਗ 1: LangChain4j ਇਨਪੁੱਟ ਗਾਰਡਰੇਲਜ਼** - ਖਤਰਨਾਕ ਪ੍ਰੋੰਪਟਾਂ ਨੂੰ LLM ਤੱਕ ਪਹੁੰਚਣ ਤੋਂ ਪਹਿਲਾਂ ਰੋਕਦਾ ਹੈ। ਕਸਟਮ ਗਾਰਡਰੇਲਜ਼ ਬਣਾਓ ਜੋ ਮਨਾਹੀ ਸ਼ਬਦ ਜਾਂ ਪੈਟਰਨ ਜਾਂਚਦੇ ਹਨ। ਇਹ ਤੁਹਾਡੇ ਕੋਡ ਵਿੱਚ ਚਲਦੇ ਹਨ, ਇਸ ਲਈ ਇਹ ਤੇਜ਼ ਅਤੇ ਮੁਫ਼ਤ ਹਨ।

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

**ਭਾਗ 2: ਪ੍ਰਦਾਤਾ ਸੁਰੱਖਿਆ ਫਿਲਟਰ** - GitHub ਮਾਡਲਾਂ ਵਿੱਚ ਬਿਲਟ-ਇਨ ਫਿਲਟਰ ਹਨ ਜੋ ਤੁਹਾਡੇ ਗਾਰਡਰੇਲਜ਼ ਨੂੰ ਛूट ਜਾਂ ਗਲਤੀ ਲੱਗਣ ਵਾਲੀ ਚੀਜ਼ਾਂ ਨੂੰ ਫੜਦੇ ਹਨ। ਤੁਸੀਂ ਕਠੋਰ ਬਲਾਕ (HTTP 400 ਗਲਤੀਆਂ) ਅਤੇ ਨਰਮ ਇਨਕਾਰ ਦੇਖੋਗੇ ਜਿੱਥੇ AI ਨਮ੍ਰਤਾਪੂਰਵਕ ਇਨਕਾਰ ਕਰਦਾ ਹੈ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** ਖੋਲ੍ਹੋ [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ਅਤੇ ਪੁੱਛੋ:
> - "InputGuardrail ਕੀ ਹੈ ਅਤੇ ਮੈਂ ਆਪਣਾ ਕਿਵੇਂ ਬਣਾਵਾਂ?"
> - "ਕਠੋਰ ਬਲਾਕ ਅਤੇ ਨਰਮ ਇਨਕਾਰ ਵਿੱਚ ਕੀ ਫ਼ਰਕ ਹੈ?"
> - "ਗਾਰਡਰੇਲਜ਼ ਅਤੇ ਪ੍ਰਦਾਤਾ ਫਿਲਟਰ ਇੱਕੱਠੇ ਵਰਤਣ ਦਾ ਕਾਰਨ ਕੀ ਹੈ?"

## ਅਗਲੇ ਕਦਮ

**ਅਗਲਾ ਮੋਡੀਊਲ:** [01-introduction - LangChain4j ਅਤੇ gpt-5 ਦੇ ਨਾਲ Azure 'ਤੇ ਸ਼ੁਰੂਆਤ](../01-introduction/README.md)

---

**ਨੈਵੀਗੇਸ਼ਨ:** [← ਮੁੱਖ ਨੂੰ ਵਾਪਸ](../README.md) | [ਅਗਲਾ: ਮੋਡੀਊਲ 01 - ਪਰਚਯ →](../01-introduction/README.md)

---

## ਟ੍ਰਾਬਲਸ਼ੂਟਿੰਗ

### ਪਹਿਲੀ ਵਾਰੀ Maven ਬਿਲਡ

**ਮੁੱਦਾ:** ਪਹਿਲੀ ਵਾਰੀ `mvn clean compile` ਜਾਂ `mvn package` ਵਿੱਚ ਬਹੁਤ ਸਮਾਂ ਲੱਗਦਾ ਹੈ (10-15 ਮਿੰਟ)

**ਕਾਰਨ:** Maven ਨੂੰ ਸਭ ਪ੍ਰੋਜੈਕਟ ਡੀਪੈਂਡੇਨਸੀਜ਼ (Spring Boot, LangChain4j ਲਾਇਬ੍ਰੇਰੀਆਂ, Azure SDKs ਆਦਿ) ਡਾਊਨਲੋਡ ਕਰਣੀਆਂ ਪੈਂਦੀਆਂ ਹਨ।

**ਹੱਲ:** ਇਹ ਸਧਾਰਣ ਗੱਲ ਹੈ। ਅਗਲੇ ਬਿਲਡਜ਼ ਬਹੁਤ ਤੇਜ਼ ਹੋਣਗੇ ਕਿਉਂਕਿ ਡੀਪੈਂਡੇਨਸੀਜ਼ ਲੋਕਲ ਕੈਚ ਹੁੰਦੀਆਂ ਹਨ। ਡਾਊਨਲੋਡ ਦਾ ਸਮਾਂ ਤੁਹਾਡੇ ਨੈੱਟਵਰਕ ਸਪੀਡ 'ਤੇ ਨਿਰਭਰ ਕਰਦਾ ਹੈ।
### PowerShell Maven Command Syntax

**ਮੁੱਦਾ**: Maven ਕਮਾਂਡਾਂ ਵਿੱਚ ਗਲਤੀ `Unknown lifecycle phase ".mainClass=..."` ਆਉਂਦੀ ਹੈ

**ਕਾਰਨ**: PowerShell `=` ਨੂੰ ਵੈਰੀਏਬਲ ਨਾਲ ਜੋੜਨ ਵਾਲੇ ਆਪਰੇਟਰ ਵਜੋਂ ਸਮਝਦਾ ਹੈ, ਜਿਸ ਨਾਲ Maven ਦੇ ਗੁਣ (property) ਦੇ ਸਿੰਟੈਕਸ ਟੁੱਟ ਜਾਂਦਾ ਹੈ

**ਹੱਲ**: Maven ਕਮਾਂਡ ਤੋਂ ਪਹਿਲਾਂ stop-parsing ਆਪਰੇਟਰ `--%` ਵਰਤੋ:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ਆਪਰੇਟਰ PowerShell ਨੂੰ ਸਾਰੀਆਂ ਬਾਕੀ ਦੀਆਂ ਅਰਗੂਮੈਂਟ ਨੂੰ ਬਿਨਾਂ ਕਿਸੇ ਵਿਆਖਿਆ ਦੇ ਸੀਧਾ Maven ਨੂੰ ਦੇਣ ਲਈ ਦੱਸਦਾ ਹੈ।

### Windows PowerShell Emoji Display

**ਮੁੱਦਾ**: PowerShell ਵਿੱਚ AI ਦੇ ਜਵਾਬਾਂ ਵਿੱਚ emoji ਦੀ ਥਾਂ ਖਰਾਬ ਅੱਖਰ (ਜਿਵੇਂ `????` ਜਾਂ `â??`) ਦਿਖਾਈ ਦਿੰਦੇ ਹਨ

**ਕਾਰਨ**: PowerShell ਦੀ ਡਿਫੋਲਟ ਕੋਡਿੰਗ UTF-8 emoji ਨੂੰ ਸਮਰਥਨ ਨਹੀਂ ਕਰਦੀ

**ਹੱਲ**: ਜਾਵਾ ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਚਲਾਉਣ ਤੋਂ ਪਹਿਲਾਂ ਇਹ ਕਮਾਂਡ ਚਲਾਓ:
```cmd
chcp 65001
```

ਇਹ ਟਰਮੀਨਲ ਵਿੱਚ UTF-8 ਕੋਡਿੰਗ ਲਾਗੂ ਕਰਦਾ ਹੈ। ਵਿਵਕਲਪ ਦੇ ਤੌਰ 'ਤੇ, ਵਿੰਡੋਜ਼ ਟਰਮੀਨਲ ਵਰਤੋ ਜਿਸ ਵਿੱਚ ਯੂਨੀਕੋਡ ਲਈ ਬਿਹਤਰ ਸਹਾਇਤਾ ਹੈ।

### Debugging API Calls

**ਮੁੱਦਾ**: AI ਮਾਡਲ ਤੋਂ ਪਰਮਾਣਿਕਤਾ ਹੀਨਤਾ, ਰੇਟ ਸੀਮਾ, ਜਾਂ ਅਣਉਮੀਦ ਜਵਾਬ ਮਿਲਣ

**ਹੱਲ**: ਉਦਾਹਰਣਾਂ ਵਿੱਚ `.logRequests(true)` ਅਤੇ `.logResponses(true)` ਸ਼ਾਮਿਲ ਹਨ ਜੋ API ਕਾਲਾਂ ਨੂੰ ਕਨਸੋਲ ਵਿੱਚ ਦਿਖਾਉਂਦੀਆਂ ਹਨ। ਇਹ ਪਰਮਾਣਿਕਤਾ ਹੀਨਤਾ, ਰੇਟ ਸੀਮਾ, ਜਾਂ ਅਣਉਮੀਦ ਜਵਾਬਾਂ ਦੀ ਸਮੱਸਿਆ ਹੱਲ ਕਰਨ ਵਿੱਚ ਮਦਦ ਕਰਦਾ ਹੈ। ਨਿਰਮਾਣ ਵਿੱਚ ਇਨ੍ਹਾਂ ਫਲੈਗਾਂ ਨੂੰ ਹਟਾਓ ਤਾਂ ਜੋ ਲਾਗ ਦੇ ਸ਼ੋਰ ਨੂੰ ਘਟਾਇਆ ਜਾ ਸਕੇ।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਵੀਕਾਰਤਾ**:
ਇਹ ਦਸਤਾਵੇਜ਼ ਏਆਈ ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਚਾਈ ਲਈ ਕੋਸ਼ਿਸ਼ ਕਰਦੇ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਵਿਚ ਰੱਖੋ ਕਿ ਸੁਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸਥਿਰਤਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੇ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਤ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਜਰੂਰੀ ਜਾਣਕਾਰੀ ਲਈ, ਵਿਸ਼ੇਸ਼ਗ੍ਯ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਅਸੀਂ ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਤੋਂ ਪੈਦਾ ਹੋਣ ਵਾਲੀਆਂ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀਆਂ ਜਾਂ ਅਸਮੀਝਾਂ ਲਈ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->