# Module 00: Quick Start

## Table of Contents

- [Introduction](../../../00-quick-start)
- [What is LangChain4j?](../../../00-quick-start)
- [LangChain4j Dependencies](../../../00-quick-start)
- [Prerequisites](../../../00-quick-start)
- [Setup](../../../00-quick-start)
  - [1. Get Your GitHub Token](../../../00-quick-start)
  - [2. Set Your Token](../../../00-quick-start)
- [Run the Examples](../../../00-quick-start)
  - [1. Basic Chat](../../../00-quick-start)
  - [2. Prompt Patterns](../../../00-quick-start)
  - [3. Function Calling](../../../00-quick-start)
  - [4. Document Q&A (Easy RAG)](../../../00-quick-start)
  - [5. Responsible AI](../../../00-quick-start)
- [What Each Example Shows](../../../00-quick-start)
- [Next Steps](../../../00-quick-start)
- [Troubleshooting](../../../00-quick-start)

## Introduction

ਇਹ quickstart ਤੁਹਾਨੂੰ LangChain4j ਨਾਲ ਜਲਦੀ ਤੋਂ ਜਲਦੀ ਚਾਲੂ ਕਰਨ ਲਈ ਬਣਾਇਆ ਗਿਆ ਹੈ। ਇਹ LangChain4j ਅਤੇ GitHub ਮਾਡਲਾਂ ਨਾਲ AI ਐਪਲੀਕੇਸ਼ਨਾਂ ਬਣਾਉਣ ਦੀ ਬੁਨਿਆਦੀ ਜਾਣਕਾਰੀ ਨੂੰ ਕਵਰ ਕਰਦਾ ਹੈ। ਅਗਲੇ ਮੋਡੀਊਲਾਂ ਵਿੱਚ ਤੁਸੀਂ Azure OpenAI ਅਤੇ GPT-5.2 ਵਿੱਚ ਘੁੱਸੋਗੇ ਅਤੇ ਹਰ ਇਕ ਧਾਰਨਾ ਨੂੰ ਹੋਰ ਗਹਿਰਾਈ ਨਾਲ ਸਮਝੋਗੇ।

## What is LangChain4j?

LangChain4j ਇੱਕ ਜਾਵਾ ਲਾਇਬ੍ਰੇਰੀ ਹੈ ਜੋ AI-ਪાવਰਡ ਐਪਲੀਕੇਸ਼ਨਾਂ ਬਣਾਉਣਾ ਆਸਾਨ ਬਣਾਦੀ ਹੈ। HTTP ਕਲਾਇਟਾਂ ਅਤੇ JSON ਪਾਰਸਿੰਗ ਨਾਲ ਜੂਝਣ ਦੀ ਬਜਾਏ, ਤੁਸੀਂ ਸਾਫ ਪਵਿੱਤਰ ਜਾਵਾ APIs ਨਾਲ ਕੰਮ ਕਰਦੇ ਹੋ।

LangChain ਵਿੱਚ "ਚੇਨ" ਮਤਲਬ ਹੈ ਕਈ ਕੰਪੋਨੈਂਟਾਂ ਨੂੰ ਜੋੜਨਾ - ਤੁਸੀਂ ਇੱਕ ਪ੍ਰੌਂਪਟ ਨੂੰ ਮਾਡਲ ਨਾਲ, ਮਾਡਲ ਤੋਂ ਪਾਰਸਰ ਨਾਲ ਜੋੜ ਸਕਦੇ ਹੋ, ਜਾਂ ਕਈ AI ਕਾਲਾਂ ਨੂੰ ਇਕੱਠੇ ਲੈ ਕੇ ਜਿੱਥੇ ਇੱਕ ਔਟਪੁੱਟ ਦੂਜੇ ਇਨਪੁੱਟ ਲਈ ਹੈ। ਇਹ quickstart ਮੂਲ ਤੱਥਾਂ 'ਤੇ ਕੇਂਦਰਿਤ ਹੈ ਪਹਿਲਾਂ ਕਿ ਹੋਰ ਜਟਿਲ ਚੇਨਾਂ ਨੂੰ ਚਰਚਾ ਕਰਨ ਤੋਂ ਪਹਿਲਾਂ।

<img src="../../../translated_images/pa/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j ਵਿੱਚ ਕੰਪੋਨੈਂਟਾਂ ਨੂੰ ਚੇਨ ਕਰਨਾ - ਸ਼ਕਤੀਸ਼ালী AI ਵਣਜਿਹਾਂ ਬਣਾਉਣ ਲਈ ਬਿਲਡਿੰਗ ਬਲਾਕਾਂ ਨੂੰ ਜੋੜਦੇ ਹੋਏ*

ਅਸੀਂ ਤਿੰਨ ਮੁੱਖ ਕੰਪੋਨੈਂਟਾਂ ਨੂੰ ਵਰਤਾਂਗੇ:

**ChatModel** - AI ਮਾਡਲ ਇੰਟਰੈਕਸ਼ਨਾਂ ਲਈ ਇੰਟਰਫੇਸ। `model.chat("prompt")` ਨੂੰ ਕਾਲ ਕਰੋ ਅਤੇ ਇਕ ਜਵਾਬਦਾਰ ਸਟਰਿੰਗ ਪ੍ਰਾਪਤ ਕਰੋ। ਅਸੀਂ `OpenAiOfficialChatModel` ਵਰਤਦੇ ਹਾਂ ਜੋ GitHub ਮਾਡਲਾਂ ਵਰਗੇ OpenAI-ਅਨੁਕੂਲ ਐਂਡਪੁਆਇੰਟਸ ਨਾਲ ਕੰਮ ਕਰਦਾ ਹੈ।

**AiServices** - ਟਾਈਪ-ਸੇਫ AI ਸੇਵਾ ਇੰਟਰਫੇਸ ਬਣਾਉਂਦਾ ਹੈ। ਮੈਥਡ ਡਿਫਾਈਨ ਕਰੋ, ਉਨ੍ਹਾਂ ਨੂੰ `@Tool` ਨਾਲ ਅਨੋਟੇਟ ਕਰੋ, ਅਤੇ LangChain4j ਆਟੋਮੈਟਿਕ ਤੌਰ 'ਤੇ ਸਹਿਯੋਗ ਕਰਦਾ ਹੈ। ਜਦੋਂ ਲੋੜ ਪਵੇ ਤਾਂ AI ਤੁਹਾਡੇ ਜਾਵਾ ਮੈਥਡਾਂ ਨੂੰ ਕਾਲ ਕਰਦਾ ਹੈ।

**MessageWindowChatMemory** - ਗੱਲਬਾਤ ਦਾ ਇਤਿਹਾਸ ਸੰਭਾਲਦਾ ਹੈ। ਇਸ ਦੇ ਬਿਨਾ, ਹਰ ਰਿਕਵੇਸਟ ਸੁਤੰਤਰ ਹੁੰਦੀ ਹੈ। ਇਸ ਨਾਲ, AI ਪਿਛਲੇ ਸੁਨੇਹਿਆਂ ਨੂੰ ਯਾਦ ਕਰਦਾ ਹੈ ਅਤੇ ਕਈ ਵਾਰੀ ਗੱਲਬਾਤ ਦੇ ਸੰਦਰਭ ਨੂੰ ਬਣਾਈ ਰੱਖਦਾ ਹੈ।

<img src="../../../translated_images/pa/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j ਵਿਆਸਰਚਨਾ - ਮੁੱਖ ਕੰਪੋਨੈਂਟ ਇਕੱਠੇ ਕੰਮ ਕਰ ਕੇ ਤੁਹਾਡੇ AI ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਸਸ਼ਕਤ ਬਣਾਉਂਦੇ ਹਨ*

## LangChain4j Dependencies

ਇਹ quickstart [`pom.xml`](../../../00-quick-start/pom.xml) ਫਾਈਲ ਵਿੱਚ ਤਿੰਨ Maven ਡਿਪੈਂਡੇਸੀਜ਼ ਵਰਤਦਾ ਹੈ:

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

`langchain4j-open-ai-official` ਮੋਡੀਊਲ `OpenAiOfficialChatModel` ਕਲਾਸ ਦਿੰਦਾ ਹੈ ਜੋ OpenAI-ਅਨੁਕੂਲ APIਜ਼ ਨਾਲ ਜੁੜਦਾ ਹੈ। GitHub ਮਾਡਲ ਵੀ ਇਸੇ API ਫਾਰਮੈਟ ਨੂੰ ਵਰਤਦੇ ਹਨ, ਇਸ ਲਈ ਕੋਈ ਖਾਸ ਐਡਾਪਟਰ ਦੀ ਲੋੜ ਨਹੀਂ — ਸਿਰਫ ਬੇਸ URL ਨੂੰ `https://models.github.ai/inference` ਤੇ ਸੈੱਟ ਕਰੋ।

`langchain4j-easy-rag` ਮੋਡੀਊਲ ਆਟੋਮੈਟਿਕ ਡੌਕਯੂਮੈਂਟ ਸਪਲਿੱਟਿੰਗ, ਇੰਬੈੱਡਿੰਗ, ਅਤੇ ਰੀਟ੍ਰੀਵਲ ਮੁਹੱਈਆ ਕਰਵਾਉਂਦਾ ਹੈ ਤਾਂ ਜੋ ਤੁਸੀਂ ਹਰ ਕਦਮ ਨੂੰ ਮੈਨੂਅਲ ਤੌਰ 'ਤੇ ਸੰਰਚਿਤ ਕਰਨ ਦੇ ਬਿਨਾ RAG ਐਪਲੀਕੇਸ਼ਨਾਂ ਬਣਾ ਸਕੋ।

## Prerequisites

**Dev Container ਵਰਤ ਰਹੇ ਹੋ?** Java ਅਤੇ Maven ਪਹਿਲਾਂ ਹੀ ਇੰਸਟਾਲ ਕੀਤੇ ਹੋਏ ਹਨ। ਤੁਸੀਂ ਸਿਰਫ ਇੱਕ GitHub Personal Access Token ਦੀ ਲੋੜ ਹੈ।

**ਲੋਕਲ ਡਿਵੈਲਪਮੈਂਟ ਲਈ:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (ਹੇਠਾਂ ਦਿੱਤੇ ਹੁਕਮਾਂ ਦੇ ਅਨੁਸਾਰ)

> **ਨੋਟ:** ਇਹ ਮੋਡੀਊਲ GitHub Models ਵਿੱਚੋਂ `gpt-4.1-nano` ਵਰਤਦਾ ਹੈ। ਕੋਡ ਵਿੱਚ ਮਾਡਲ ਦਾ ਨਾਮ ਨਾ ਬਦਲੋ - ਇਹ ਵਰਤਮਾਨ GitHub ਦੇ ਉਪਲਬਧ ਮਾਡਲਾਂ ਨਾਲ ਕੰਮ ਕਰਨ ਲਈ ਸੰਰਚਿਤ ਹੈ।

## Setup

### 1. ਆਪਣਾ GitHub ਟੋਕਨ ਪ੍ਰਾਪਤ ਕਰੋ

1. ਜਾਓ [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. "Generate new token" ਤੇ ਕਲਿੱਕ ਕਰੋ
3. ਇੱਕ ਵਰਨਨਾਤਮਕ ਨਾਮ ਦੇਵੋ (ਜਿਵੇਂ "LangChain4j Demo")
4. ਸਮਾਪਤੀ ਸਮਾਂ ਸੈੱਟ ਕਰੋ (7 ਦਿਨਾਂ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ)
5. "Account permissions" ਦੇ ਹੇਠਾਂ "Models" ਨੂੰ "Read-only" ਸੈੱਟ ਕਰੋ
6. "Generate token" ਤੇ ਕਲਿੱਕ ਕਰੋ
7. ਆਪਣਾ ਟੋਕਨ ਕਾਪੀ ਕਰੋ ਅਤੇ ਸੁਰੱਖਿਅਤ ਰੱਖੋ - ਮੁੜ ਨਹੀਂ ਮਿਲੇਗਾ

### 2. ਆਪਣਾ ਟੋਕਨ ਸੈੱਟ ਕਰੋ

**ਵਿਕਲਪ 1: VS Code ਵਰਤ ਕੇ (ਸਿਫਾਰਸ਼ੀ)**

ਜੇ ਤੁਸੀਂ VS Code ਵਰਤ ਰਹੇ ਹੋ, ਤਾਂ ਆਪਣਾ ਟੋਕਨ ਪ੍ਰਾਜੈਕਟ ਦੀ ਰੂਟ ਵਿੱਚ `.env` ਫਾਈਲ ਵਿੱਚ ਸ਼ਾਮਲ ਕਰੋ:

ਜੇ `.env` ਫਾਈਲ ਮੌਜੂਦ ਨਹੀਂ, ਤਾਂ `.env.example` ਨੂੰ `.env` ਵਿੱਚ ਕਾਪੀ ਕਰੋ ਜਾਂ ਨਵੀਂ `.env` ਫਾਈਲ ਪੈਦਾ ਕਰੋ।

**ਨਮੂਨਾ `.env` ਫਾਈਲ:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env ਵਿੱਚ
GITHUB_TOKEN=your_token_here
```

ਫਿਰ ਤੁਸੀਂ ਸਿੱਧਾ ਕਿਥੇ ਵੀ ਕੋਈ ਡੈਮੋ ਫਾਈਲ (ਜਿਵੇਂ `BasicChatDemo.java`) ਤੇ ਰਾਈਟ-ਕਲਿੱਕ ਕਰਕੇ **"Run Java"** ਚੁਣ ਸਕਦੇ ਹੋ ਜਾਂ Run ਅਤੇ Debug ਪੈਨਲ ਦੇ ਲਾਂਚ ਕੰਫਿਗਰੇਸ਼ਨਾਂ ਦੀ ਵਰਤੋਂ ਕਰ ਸਕਦੇ ਹੋ।

**ਵਿਕਲਪ 2: ਟਰਮੀਨਲ ਵਰਤ ਕੇ**

ਟੋਕਨ ਨੂੰ ਵਾਤਾਵਰਣ ਚਲ ਉਤਰਾ ਵੈਰੀਏਬਲ ਵਜੋਂ ਸੈੱਟ ਕਰੋ:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## Run the Examples

**VS Code ਵਰਤਦੇ ਹੋਏ:** ਸਿੱਧਾ Explorer ਵਿੱਚ ਕਿਸੇ ਵੀ ਡੈਮੋ ਫਾਈਲ ਤੇ ਰਾਈਟ-ਕਲਿੱਕ ਕਰੋ ਅਤੇ **"Run Java"** ਚੁਣੋ, ਜਾਂ Run ਅਤੇ Debug ਪੈਨਲ ਤੋਂ ਲਾਂਚ ਕੰਫਿਗਰੇਸ਼ਨ ਸੈੱਟਿੰਗਜ਼ ਦੀ ਵਰਤੋਂ ਕਰੋ (ਪਹਿਲਾਂ ਇਹ ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਤੁਸੀਂ `.env` ਵਿੱਚ ਆਪਣਾ ਟੋਕਨ ਜੋੜਿਆ ਹੈ)।

**Maven ਵਰਤ ਕੇ:** ਵਿਕਲਪ ਵਜੋਂ, ਤੁਸੀਂ ਕਮਾਂਡ ਲਾਈਨ ਤੋਂ ਵੀ ਚਲਾ ਸਕਦੇ ਹੋ:

### 1. Basic Chat

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Prompt Patterns

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

ਜ਼ੀਰੋ-ਸ਼ਾਟ, ਫਿਊ-ਸ਼ਾਟ, ਚੇਨ-ਆਫ-ਥਾਟ, ਅਤੇ ਰੋਲ-ਅਧਾਰਿਤ ਪ੍ਰੌਂਪਟਿੰਗ ਦਿਖਾਉਂਦਾ ਹੈ।

### 3. Function Calling

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

ਜਦੋਂ ਲੋੜ ਹੋਵੇ ਤਾਂ AI ਆਪਣੇ ਆਪ ਤੁਹਾਡੇ ਜਾਵਾ ਮੈਥਡਾਂ ਨੂੰ ਕਾਲ ਕਰਦਾ ਹੈ।

### 4. Document Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

ਆਪਣੇ ਡੌਕਯੂਮੈਂਟਾਂ ਬਾਰੇ ਸਵਾਲ ਪੁੱਛੋ Easy RAG ਨਾਲ ਆਟੋਮੈਟਿਕ ਇੰਬੈੱਡਿੰਗ ਤੇ ਰੀਟ੍ਰੀਵਲ ਵਰਤ ਕੇ।

### 5. Responsible AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

ਵੇਖੋ ਕਿ AI ਸੁਰੱਖਿਆ ਫਿਲਟਰ ਕਿਵੇਂ ਹਾਨਿਕਾਰਕ ਸਮੱਗਰੀ ਨੂੰ ਬਲਾਕ ਕਰਦੇ ਹਨ।

## What Each Example Shows

**Basic Chat** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

ਇੱਥੇ ਸ਼ੁਰੂ ਕਰੋ ਤਾਂ ਕਿ LangChain4j ਨੂੰ ਸਭ ਤੋਂ ਸਧਾਰਨ ਰੂਪ ਵਿੱਚ ਵੇਖ ਸਕੋ। ਤੁਸੀਂ ਇਕ `OpenAiOfficialChatModel` ਬਣਾਉਗੇ, `.chat()` ਨਾਲ ਇੱਕ ਪ੍ਰੌਂਪਟ ਭੇਜੋਗੇ ਅਤੇ ਜਵਾਬ ਪ੍ਰਾਪਤ ਕਰੋਗੇ। ਇਹ ਮੁੱਢਲੇ ਤੌਰ 'ਤੇ ਦਿਖਾਉਂਦਾ ਹੈ ਕਿ ਕਿਵੇਂ ਮਾਡਲਾਂ ਨੂੰ ਕਸਟਮ ਐਂਡਪੁਆਇੰਟ ਤੇ API ਕੁੰਜੀਆਂ ਨਾਲ ਇਨਿਸ਼ੀਏਟ ਕੀਤਾ ਜਾਂਦਾ ਹੈ। ਇਸ ਪਰਿਬਾਧਾ ਨੂੰ ਸਮਝਣ ਦੇ ਬਾਅਦ ਬਾਕੀ ਸਾਰੀ ਚੀਜ਼ਾਂ ਇਸ 'ਤੇ ਅਧਾਰਿਤ ਬਣਦੀਆਂ ਹਨ।

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:
> - "ਮੈਂ ਇਸ ਕੋਡ ਵਿੱਚ GitHub Models ਤੋਂ Azure OpenAI ਵਿੱਚ ਕਿਵੇਂ ਬਦਲ ਕਰਾਂ?"
> - "OpenAiOfficialChatModel.builder() ਵਿੱਚ ਹੋਰ ਕਿਹੜੇ ਪੈਰਾਮੀਟਰ ਮੈਂ ਸੈੱਟ ਕਰ ਸਕਦਾ ਹਾਂ?"
> - "ਮੈਂ ਪੂਰਾ ਜਵਾਬ ਦੇਖਣ ਦੀ ਬਜਾਏ ਸਟ੍ਰੀਮਿੰਗ ਜਵਾਬ ਕਿਵੇਂ ਸ਼ਾਮਲ ਕਰਾਂ?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

ਹੁਣ ਜਦੋਂ ਤੁਸੀਂ ਮਾਡਲ ਨਾਲ ਗੱਲ ਕਰਨਾ ਜਾਣਦੇ ਹੋ, ਤਾਂ ਆਓ ਵੇਖੀਏ ਕਿ ਤੁਸੀਂ ਇਸ ਨੂੰ ਕੀ ਕਹਿੰਦੇ ਹੋ। ਇਹ ਡੈਮੋ ਵੱਖ-ਵੱਖ ਪ੍ਰੌਂਪਟਿੰਗ ਪੈਟਰਨ ਦਿਖਾਉਂਦਾ ਹੈ। ਜਿਵੇਂ:
ਜ਼ੀਰੋ-ਸ਼ਾਟ ਪ੍ਰੌਂਪਟ ਸਿੱਧੇ ਹੁਕਮਾਂ ਲਈ, ਫਿਊ-ਸ਼ਾਟ ਜਿਹੜੇ ਉਦਾਹਰਣਾਂ ਤੋਂ ਸਿੱਖਦੇ ਹਨ, ਚੇਨ-ਆਫ-ਥਾਟ ਜੋ ਤਰਕਬੱਧ ਕਦਮ ਪ੍ਰਗਟ ਕਰਦੇ ਹਨ, ਅਤੇ ਰੋਲ-ਅਧਾਰਿਤ ਪ੍ਰੌਂਪਟ ਜਿਹੜੇ ਸੰਦਰਭ ਸੈੱਟ ਕਰਦੇ ਹਨ। ਤੁਸੀਂ ਦੇਖੋਗੇ ਕਿ ਇੱਕੋ ਮਾਡਲ ਕਿਵੇਂ ਵੱਖਰੇ ਨਤੀਜੇ ਦਿੰਦਾ ਹੈ ਜਿਸ ਤਰ੍ਹਾਂ ਤੁਸੀਂ ਆਪਣੀ ਮੰਗ ਨੂੰ ਰੂਪ ਦੇਂਦੇ ਹੋ।

ਡੈਮੋ ਵਿੱਚ ਪ੍ਰੌਂਪਟ ਟੈਂਪਲੇਟ ਵੀ ਦਿਖਾਉਂਦਾ ਹੈ, ਜੋ ਵੈਰੀਏਬਲਾਂ ਨਾਲ ਦੁਬਾਰਾ ਵਰਤੋਂਯੋਗ ਪ੍ਰੌਂਪਟ ਬਣਾਉਣ ਦਾ ਸ਼ਕਤੀਸ਼ালী ਤਰੀਕਾ ਹੈ।
ਹੇਠਾਂ ਦਿੱਤਾ ਉਦਾਹਰਣ LangChain4j ਦੇ `PromptTemplate` ਨੂੰ ਵਰਤ ਕੇ ਵੈਰੀਏਬਲ ਭਰਨ ਦਾ ਦਿਖਾਵਾ ਕਰਦਾ ਹੈ। AI ਪ੍ਰਦਾਨ ਕੀਤੇ ਗੰਤੀਨ ਅਤੇ ਸਰਗਰਮੀ ਦੇ ਆਧਾਰ 'ਤੇ ਜਵਾਬ ਦੇਵੇਗਾ।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:
> - "ਜ਼ੀਰੋ-ਸ਼ਾਟ ਅਤੇ ਫਿਊ-ਸ਼ਾਟ ਪ੍ਰੌਂਪਟਿੰਗ ਵਿੱਚ ਕੀ ਫਰਕ ਹੈ ਅਤੇ ਮੈਂ ਇਹਨਾਂ ਵਿੱਚੋਂ ਕਦੋਂ ਵਰਤਾਂ?"
> - "ਆਪਣੇ ਮਾਡਲ ਦੇ ਜਵਾਬਾਂ 'ਤੇ ਤੇਮਪਰੇਚਰ ਪੈਰਾਮੀਟਰ ਦਾ ਕੀ ਅਸਰ ਹੁੰਦਾ ਹੈ?"
> - "ਪ੍ਰੌਂਪਟ ਇੰਜੈਕਸ਼ਨ ਹਮਲਿਆਂ ਤੋਂ ਕਿਵੇਂ ਬਚਿਆ ਜਾ ਸਕਦਾ ਹੈ?"
> - "ਆਮ ਪੈਟਰਨਾਂ ਲਈ ਦੁਬਾਰਾ ਵਰਤੋਂਯੋਗ `PromptTemplate` ਆਬਜੈਕਟ ਕਿਵੇਂ ਬਣਾਉਂਦਾ ਹਾਂ?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ਇੱਥੇ LangChain4j ਸ਼ਕਤੀਸ਼ਾਲੀ ਹੁੰਦਾ ਹੈ। ਤੁਸੀਂ `AiServices` ਨਾਲ ਇੱਕ AI ਸਹਾਇਕ ਬਣਾਉਗੇ ਜੋ ਤੁਹਾਡੇ ਜਾਵਾ ਮੈਥਡਾਂ ਨੂੰ ਕਾਲ ਕਰ ਸਕਦਾ ਹੈ। ਸਿਰਫ `@Tool("description")` ਨਾਲ ਮੈਥਡਾਂ ਨੂੰ ਅਨੋਟੇਟ ਕਰੋ ਅਤੇ LangChain4j ਬਾਕੀ ਕੰਮ ਸੰਭਾਲਦਾ ਹੈ - AI ਆਪਣੇ ਆਪ ਫੈਸਲਾ ਕਰਦਾ ਹੈ ਕਿ ਕਦੋਂ ਕਿਹੜਾ ਟੂਲ ਵਰਤਣਾ ਹੈ। ਇਹ ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ ਦੀ ਵਰਤੋਂ ਦਿਖਾਉਂਦਾ ਹੈ, ਜੋ ਕਿ AI ਬਣਾਉਣ ਲਈ ਇੱਕ ਅਹਮ ਤਕਨੀਕ ਹੈ ਜੋ ਸਿਰਫ ਸਵਾਲਾਂ ਦੇ ਜਵਾਬ ਨਹੀਂ ਦਿੰਦਾ, ਬਲਕਿ ਕਾਰਵਾਈਆਂ ਵੀ ਕਰਦਾ ਹੈ।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:
> - "@Tool ਅਨੋਟੇਸ਼ਨ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀ ਹੈ ਅਤੇ LangChain4j ਇਸਦੇ ਨਾਲ ਅੰਦਰੋਂ ਕੀ ਕਰਦਾ ਹੈ?"
> - "ਕੀ AI ਕਈ ਟੂਲਾਂ ਨੂੰ ਲੜੀਵਾਰ ਕਾਲ ਕਰਕੇ ਪੇਚੀਦਾ ਸਮੱਸਿਆ ਹੱਲ ਕਰ ਸਕਦਾ ਹੈ?"
> - "ਜੇ ਕੋਈ ਟੂਲ ਇਕਸਪਸ਼ਨ ਸੁੱਟੇ, ਤਾਂ ਮੈਂ ਏਰਰ ਨੂੰ ਕਿਵੇਂ ਹੈਂਡਲ ਕਰਾਂ?"
> - "ਇਸ ਕੈਲਕੂਲੇਟਰ ਉਦਾਹਰਣ ਦੀ ਥਾਂ ਤੇ ਮੈਂ ਅਸਲੀ API ਕਿਵੇਂ ਇੰਟੀਗ੍ਰੇਟ ਕਰਾਂ?"

**Document Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ਇੱਥੇ ਤੁਸੀਂ LangChain4j ਦੇ "Easy RAG" ਪਹੁੰਚ ਨਾਲ RAG (retrieval-augmented generation) ਦੇਖੋਗੇ। ਡੌਕਯੂਮੈਂਟ ਲੋਡ ਕੀਤੇ ਜਾਂਦੇ ਹਨ, ਆਟੋਮੈਟਿਕ ਤੌਰ 'ਤੇ ਵੰਡੇ ਜਾਂਦੇ ਹਨ ਅਤੇ ਇਨ-ਮੇਮੋਰੀ ਸਟੋਰ ਵਿੱਚ ਇੰਬੈੱਡ ਕੀਤੇ ਜਾਂਦੇ ਹਨ, ਫਿਰ ਇਕ ਸੰਬੰਧਿਤ ਸਮੱਗਰੀ ਲੈਣ ਵਾਲਾ AI ਨੂੰ ਸਵਾਲ ਸਮੇਂ ਉਚਿਤ ਟੁਕੜੇ ਪਰਦਾਨ ਕਰਦਾ ਹੈ। AI ਤੁਹਾਡੇ ਡੌਕਯੂਮੈਂਟਾਂ ਦੇ ਆਧਾਰ 'ਤੇ ਜਵਾਬ ਦਿੰਦਾ ਹੈ, ਨਾ ਕਿ ਇਸ ਦੀ ਆਮ ਜਾਣਕਾਰੀ ਤੇ।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:
> - "RAG ਕਿਸ ਤਰ੍ਹਾਂ ਮਾਡਲ ਦੀ ਟ੍ਰੇਨਿੰਗ ਜਾਣਕਾਰੀ ਦੀ ਤੁਲਨਾ ਵਿੱਚ AI ਹੁੱਛ ਕਹਾਣੀਆਂ ਨੂੰ ਰੋਕਦਾ ਹੈ?"
> - "ਇਸ ਆਸਾਨ ਪਹੁੰਚ ਅਤੇ ਕਸਟਮ RAG ਪਾਈਪਲਾਈਨ ਵਿੱਚ ਕੀ ਫਰਕ ਹੈ?"
> - "ਮੈਂ ਕਿਵੇਂ ਇਸ ਨੂੰ ਕਈ ਡੌਕਯੂਮੈਂਟਾਂ ਜਾਂ ਵੱਡੇ ਗਿਆਨ ਭੰਡਾਰਾਂ ਉੱਤੇ ਫੈਲਾ ਸਕਦਾ ਹਾਂ?"

**Responsible AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ਗਹਿਰਾਈ ਨਾਲ AI ਸੁਰੱਖਿਆ ਬਣਾਓ। ਇਹ ਡੈਮੋ ਦੋ ਸਤਰਾਂ ਦੀ ਸੁਰੱਖਿਆ ਦੇਖਾਉਂਦਾ ਹੈ ਜੋ ਇਕੱਠੇ ਕੰਮ ਕਰਦੀਆਂ ਹਨ:

**ਭਾਗ 1: LangChain4j ਇਨਪੁੱਟ ਗਾਰਡਰੇਲਜ਼** - ਖ਼ਤਰਨਾਕ ਪ੍ਰੌਂਪਟਾਂ ਨੂੰ LLM ਤਕ ਪਹੁੰਚਣ ਤੋਂ ਪਹਿਲਾਂ ਰੋਕਦਾ ਹੈ। ਕਸਟਮ ਗਾਰਡਰੇਲ ਬਣਾਓ ਜੋ ਮਨਾਹੀ ਸ਼ਬਦਾਂ ਜਾਂ ਪੈਟਰਨਾਂ ਨੂੰ ਜਾਂਚਦੇ ਹਨ। ਇਹ ਤੁਹਾਡੇ ਕੋਡ ਵਿੱਚ ਚਲਦੇ ਹਨ, ਇਸ ਲਈ ਤੇਜ਼ ਅਤੇ ਮੁਫਤ ਹਨ।

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

**ਭਾਗ 2: ਪ੍ਰੋਵਾਈਡਰ ਸੇਫਟੀ ਫਿਲਟਰ** - GitHub Models ਵਿੱਚ ਪਹਿਲਾਂ ਤੋਂ ਫਿਲਟਰ ਹਨ ਜੋ ਤੁਹਾਡੇ ਗਾਰਡਰੇਲਾਂ ਤੋਂ ਬਚ ਸਕਣ ਵਾਲੀਆਂ ਚੀਜ਼ਾਂ ਨੂੰ ਫੜਦੇ ਹਨ। ਤੁਸੀਂ ਭਿਆਨਕ ਉਲੰਘਣਾਂ ਲਈ ਕਠੋਰ ਬਲਾਕ (HTTP 400 ਐਰਰ) ਅਤੇ ਨਰਮ ਇਨਕਾਰ ਦੇਖੋਗੇ ਜਿੱਥੇ AI ਸਨਮਾਨਪੂਰਵਕ ਇਨਕਾਰ ਕਰਦਾ ਹੈ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ਨਾਲ ਕੋਸ਼ਿਸ਼ ਕਰੋ:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ਖੋਲ੍ਹੋ ਅਤੇ ਪੁੱਛੋ:
> - "InputGuardrail ਕੀ ਹੈ ਅਤੇ ਮੈਂ ਆਪਣਾ ਕਿਵੇਂ ਬਣाऊਂ?"
> - "ਕਠੋਰ ਬਲਾਕ ਤੇ ਨਰਮ ਇਨਕਾਰ ਵਿੱਚ ਕੀ ਫਰਕ ਹੈ?"
> - "ਗਾਰਡਰੇਲ ਅਤੇ ਪ੍ਰੋਵਾਈਡਰ ਫਿਲਟਰ ਦੋਹਾਂ ਨੂੰ ਇਕੱਠੇ ਵਰਤਣ ਦਾ ਕੀ ਲਾਭ ਹੈ?"

## Next Steps

**ਅਗਲਾ ਮੋਡੀਊਲ:** [01-introduction - Getting Started with LangChain4j](../01-introduction/README.md)

---

**ਨੈਵੀਗੇਸ਼ਨ:** [← ਮੁੱਖ ਪੇਜ ਤੇ ਵਾਪਸ](../README.md) | [ਅਗਲਾ: Module 01 - Introduction →](../01-introduction/README.md)

---

## Troubleshooting

### ਪਹਿਲੀ ਵਾਰੀ Maven ਬਿਲਡ

**ਮਸਲਾ**: ਪਹਿਲੀ ਵਾਰੀ `mvn clean compile` ਜਾਂ `mvn package` 10-15 ਮਿੰਟ ਲੱਗਦੇ ਹਨ

**ਕਾਰਨ**: Maven ਨੂੰ ਸਾਰੇ ਪ੍ਰਾਜੈਕਟ ਡਿਪੈਂਡੇਸੀਜ਼ (Spring Boot, LangChain4j ਲਾਇਬ੍ਰੇਰੀਆਂ, Azure SDK ਆਦਿ) ਪਹਿਲੀ ਵਾਰੀ ਡਾਊਨਲੋਡ ਕਰਨੀਆਂ ਪੈਂਦੀਆਂ ਹਨ।

**ਸਮਾਧਾਨ**: ਇਹ ਆਮ ਗੱਲ ਹੈ। ਅਗਲੀ ਵਾਰ ਬਿਲਡ ਬਹੁਤ ਜਲਦੀ ਹੋਵੇਗੀ ਕਿਉਂਕਿ ਡਿਪੈਂਡੇਸੀਜ਼ ਲੋਕਲ ਕੈਸ਼ ਹੋ ਜਾਨਗੀਆਂ। ਡਾਊਨਲੋਡ ਸਮਾਂ ਤੁਹਾਡੇ ਨੈੱਟਵਰਕ ਦੀ ਗਤੀ 'ਤੇ ਨਿਰਭਰ ਕਰਦਾ ਹੈ।

### PowerShell Maven ਕਮਾਂਡ ਸਿੰਟੈਕਸ

**ਮਸਲਾ**: Maven ਕਮਾਂਡ `Unknown lifecycle phase ".mainClass=..."` ਨਾਲ ਫੇਲ ਹੋ ਜਾਂਦੇ ਹਨ
**ਕਾਰਣ**: PowerShell `=` ਨੂੰ ਵੈਰੀਏਬਲ ਅਸਾਈਨਮੈਂਟ ਆਪਰੇਟਰ ਵਜੋਂ ਸਮਝਦਾ ਹੈ, ਜਿਸ ਨਾਲ Maven ਦੀ ਪ੍ਰਾਪਰਟੀ ਸਿੰਟੈਕਸ ਟੁੱਟ ਜਾਂਦੀ ਹੈ

**ਸਮਾਧਾਨ**: Maven ਕਮਾਂਡ ਤੋਂ ਪਹਿਲਾਂ	stop-parsing آپਰੇਟਰ `--%` ਦੀ ਵਰਤੋਂ ਕਰੋ:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` آپਰੇਟਰ PowerShell ਨੂੰ ਕਹਿੰਦਾ ਹੈ ਕਿ ਬਾਕੀ ਬਚੇ ਹੋਏ ਸਾਰੇ ਆਰਗੁਮੈਂਟਸ ਬਿਨਾਂ ਕਿਸੇ ਵਿਆਖਿਆ ਦੇ Maven ਨੂੰ ਸਿੱਧੇ ਪਾਸ ਕਰ ਦੇਵੇ।

### ਵਿੰਡੋਜ਼ PowerShell Emoji ਡਿਸਪਲੇ

**ਮੁੱਦਾ**: PowerShell ਵਿੱਚ AI ਜਵਾਬਾਂ emoji ਦੀ ਥਾਂ ਕੂੜਾਹਟ ਵਰਗੀਆਂ ਕਿਰਦਾਰ (`????` ਜਾਂ `â??`) ਦਿਖਾਉਂਦੇ ਹਨ

**ਕਾਰਣ**: PowerShell ਦੀ ਡਿਫਾਲਟ ਇੰਕੋਡਿੰਗ UTF-8 emojis ਦਾ ਸਮਰਥਨ ਨਹੀਂ ਕਰਦੀ

**ਸਮਾਧਾਨ**: ਜਾਵਾ ਐਪਲੀਕੇਸ਼ਨ ਚਲਾਉਣ ਤੋਂ ਪਹਿਲਾਂ ਇਹ ਕਮਾਂਡ ਚਲਾਓ:
```cmd
chcp 65001
```

ਇਸ ਨਾਲ ਟਰਮੀਨਲ ਵਿੱਚ UTF-8 ਇੰਕੋਡਿੰਗ ਮੁਕੰਮਲ ਹੋ ਜਾਂਦੀ ਹੈ। ਵਿਸ਼ਵਾਸਯੋਗ ਵਿਕਲਪ ਵਜੋਂ Windows Terminal ਦੀ ਵਰਤੋਂ ਕਰੋ ਜਿਸ ਵਿੱਚ Unicode ਲਈ ਵਧੀਆ ਸਮਰਥਨ ਹੈ।

### API ਕਾਲਾਂ ਦਾ ਡੀਬੱਗਿੰਗ

**ਮੁੱਦਾ**: AI ਮਾਡਲ ਤੋਂ ਪ੍ਰਮਾਣਿਕਤਾ ਦੀਆਂ ਗਲਤੀਆਂ, ਰੇਟ ਸੀਮਾਵਾਂ ਜਾਂ ਅਣਛਾਹੀਆਂ ਜਵਾਬ

**ਸਮਾਧਾਨ**: ਉਦਾਹਰਣਾਂ `.logRequests(true)` ਅਤੇ `.logResponses(true)` ਸ਼ਾਮਿਲ ਕਰਦੀਆਂ ਹਨ ਜੋ API ਕਾਲਾਂ ਨੂੰ ਕੰਸੋਲ ਵਿੱਚ ਵੇਖਾਉਂਦੀਆਂ ਹਨ। ਇਹ ਪ੍ਰਮਾਣਿਕਤਾ ਗਲਤੀਆਂ, ਰੇਟ ਸੀਮਾਵਾਂ ਜਾਂ ਅਣਛਾਹੀਆਂ ਜਵਾਬਾਂ ਦਾ ਪਤਾ ਲਗਾਉਣ ਵਿੱਚ ਮਦਦ ਕਰਦਾ ਹੈ। ਪ੍ਰੋਡਕਸ਼ਨ ਵਿੱਚ ਲੋਗ ਸ਼ੋਰ ਘਟਾਉਣ ਲਈ ਇਹ ਝੰਡੇ ਹਟਾਓ।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਵੀਕਾਰਨ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ ਏਆਈ ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦਿਤ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਕੋਸ਼ਿਸ਼ ਕਰਦੇ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਵਿੱਚ ਰੱਖੋ ਕਿ ਆਟੋਮੈਟਿਕ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਣਸਹੀਤੀਆਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਿਕ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਜ਼ਰੂਰੀ ਜਾਣਕਾਰੀ ਲਈ, ਮਾਹਿਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫ਼ਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਹੋਣ ਵਾਲੀਆਂ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀਆਂ ਜਾਂ ਭ੍ਰਮਾਂ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->