# Module 00: ವೇಗದ ಪ್ರಾರಂಭ

## ವಿಷಯಸೂಚಿ

- [ಪರಿಚಯ](../../../00-quick-start)
- [LangChain4j ಎಂದರೆ ಏನು?](../../../00-quick-start)
- [LangChain4j ಅವಲಂಬನೆಗಳು](../../../00-quick-start)
- [ಅಗತ್ಯಮೀಯಿಗಳು](../../../00-quick-start)
- [ಸೆಟಪ್](../../../00-quick-start)
  - [1. ನಿಮ್ಮ GitHub ಟೋಕನ್ ಪಡೆಯುವುದು](../../../00-quick-start)
  - [2. ನಿಮ್ಮ ಟೋಕನ್ ಸೆಟ್ ಮಾಡುವುದು](../../../00-quick-start)
- [ಉದಾಹರಣೆಗಳನ್ನು ರನ್ ಮಾಡುವುದು](../../../00-quick-start)
  - [1. ಮೂಲ ಚಾಟ್](../../../00-quick-start)
  - [2. ಉತ್ತೇಜನ ಮಾದರಿಗಳು](../../../00-quick-start)
  - [3. ಫಂಕ್ಷನ್ ಕರೆ](../../../00-quick-start)
  - [4. ದಸ್ತಾವೇಜು ಪ್ರಶ್ನೋತ್ತರ (ಸುಲಭ RAG)](../../../00-quick-start)
  - [5. ಜವಾಬ್ದಾರಿಯುತ AI](../../../00-quick-start)
- [ಪ್ರತಿ ಉದಾಹರಣೆ ಏನನ್ನು ತೋರಿಸುತ್ತದೆ](../../../00-quick-start)
- [ಮುಂದಿನ ಹಂತಗಳು](../../../00-quick-start)
- [ತಪ್ಪುಪಡಿ ಪರಿಹಾರ](../../../00-quick-start)

## ಪರಿಚಯ

ಈ ವೇಗದ ಪ್ರಾರಂಭವು LangChain4j ನೊಂದಿಗೆ ನಿಮ್ಮನ್ನು ಶೀಘ್ರವಾಗಿ ಆರಂಭಿಸಲು ಉದ್ದೇಶಿಸಲಾಗಿದೆ. ಇದು LangChain4j ಮತ್ತು GitHub ಮಾದರಿಗಳೊಂದಿಗೆ AI ಅಪ್ಲಿಕೇಶನ್ಗಳನ್ನು ನಿರ್ಮಿಸುವ ಮೂಲತತ್ವಗಳನ್ನು ಒಳಗೊಂಡಿದೆ. ಮುಂದಿನ modules ನಲ್ಲಿ ನೀವು Azure OpenAI ಅನ್ನು LangChain4j ಜೊತೆಗೆ ಬಳಸಿಕೊಂಡು ಇನ್ನಷ್ಟು ಆಧುನಿಕ ಅಪ್ಲಿಕೇಶನ್ಗಳನ್ನು ನಿರ್ಮಿಸುವಿರಿ.

## LangChain4j ಎಂದರೆ ಏನು?

LangChain4j ಒಂದು Java ಗ್ರಂಥಾಲಯವಾಗಿದ್ದು, AI ಶಕ್ತಿಯಾದ ಅಪ್ಲಿಕೇಶನ್ಗಳನ್ನು ಸುಲಭವಾಗಿ ನಿರ್ಮಿಸುವುದಕ್ಕೆ ಸಹಾಯ ಮಾಡುತ್ತದೆ. HTTP ಕ್ಲೈಂಟ್ ಮತ್ತು JSON ಪಾರ್ಸಿಂಗ್ ನೊಂದಿಗೆ ಕಾರ್ಯನಿರ್ವಹಿಸುವ ಬದಲು, ನೀವು ಸ್ವಚ್ಚ Java APIs ಮೂಲಕ ಕೆಲಸ ಮಾಡುತ್ತೀರಾ.

LangChain ನ "ಚೈನ್" ಅಂದರೆ ಬಹು ಘಟಕಗಳನ್ನು ಕಟ್ಟಿ ಜೋಡಿಸುವುದನ್ನು ಸೂಚಿಸುತ್ತದೆ - ನೀವು ಒಂದು ಪ್ರಾಂಪ್ಟ್ ಅನ್ನು ಮಾದರಿಗೆ, ಆ ನಂತರ ಪಾರ್ಸರ್ ಗೆ ಜೋಡಿಸಬಹುದು; ಅಥವಾ ಬಹು AI ಕರೆಗಳನ್ನು ಪರಸ್ಪರ ಸಂಪರ್ಕಿಸಬಹುದು, ಇಲ್ಲಿ ಒಂದು ಫಲಿತಾಂಶ ಮುಂದಿನ ಇನ್‌ಪುಟ್ ಆಗಿ ಬಳಸಲಾಗುತ್ತದೆ. ಈ ವೇಗದ ಪ್ರಾರಂಭ ಮೂಲತತ್ವಗಳಿಗೆ ಒತ್ತು ನೀಡುತ್ತದೆ ಮತ್ತು ಬಹು ಜಟಿಲ ಚೈನ್‌ಗಳನ್ನು ಅನ್ವೇಷಿಸುವ ಮುಂಚೆ.

<img src="../../../translated_images/kn/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j ನಲ್ಲಿ ಘಟಕಗಳನ್ನು ಕಟ್ಟಿ ಜೋಡಿಸುವುದು - ಶಕ್ತಿ ಹೊಂದಿರುವ AI ವರ್ಕ್‌ಫ್ಲೋಗಳನ್ನು ರಚಿಸುವ ಕಟ್ಟಡ ಗೋಡೆಗಳು*

ನಾವು ಮೂರು ಪ್ರಮುಖ ಘಟಕಗಳನ್ನು ಬಳಸುತ್ತೇವೆ:

**ChatModel** - AI ಮಾದರಿಯೊಡನೆ ಸಂವಹನಕ್ಕೆ ಇಂಟರ್ಫೇಸ್. `model.chat("prompt")` ಅನ್ನು ಕರೆ ಮಾಡಿ ಉತ್ತರದ 문자열ವನ್ನು ಪಡೆಯಿರಿ. ನಾವು `OpenAiOfficialChatModel` ಬಳಸುತ್ತೇವೆ, ಇದು OpenAI-ಅನುಕೂಲವಾದ ಅಂತಿಮ ಬಿಂದುಗಳ ಮಾದರಿಗಳನ್ನು GitHub Models ಜೊತೆ ಕೆಲಸ ಮಾಡುತ್ತದೆ.

**AiServices** - ಪ್ರಕಾರ-ಸುರಕ್ಷಿತ AI ಸೇವಾ ಇಂಟರ್ಫೇಸ್‌ಗಳನ್ನು ರಚಿಸುತ್ತದೆ. ವಿಧಾನಗಳನ್ನು ವ್ಯಾಖ್ಯಾನಿಸಿ, ಅವುಗಳನ್ನು `@Tool` ನೊಂದಿಗೆ ಗುರುತಿಸಿ, LangChain4j ಆರ್ಕೆಸ್ಟ್ರೇಶನ್ ನ್ನು ನಿರ್ವಹಿಸುತ್ತದೆ. AI ಅಗತ್ಯವಿದ್ದಾಗ ನಿಮ್ಮ Java ವಿಧಾನಗಳನ್ನು ಸ್ವಯಂಚಾಲಿತವಾಗಿ ಕರೆ ಮಾಡುತ್ತದೆ.

**MessageWindowChatMemory** - ಮಾತುಕತೆ ಇತಿಹಾಸವನ್ನು ಕಾಪಾಡುತ್ತದೆ. ಇದಿಲ್ಲದೆ, ಪ್ರತಿಯೊಂದು ವಿನಂತಿಯು ಸ್ವತಂತ್ರವಾಗಿರುತ್ತದೆ. ಇದೊಂದಿಗೆ, AI ಹಿಂದಿನ ಸಂದೇಶಗಳನ್ನು ನೆನಪಿನಲ್ಲಿ ಇಟ್ಟುಕೊಂಡು ಅನೇಕ ಸುತ್ತುಗಳಿಗೆ ಸಂದರ್ಭವನ್ನು ಕಾಪಾಡುತ್ತದೆ.

<img src="../../../translated_images/kn/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j ವಾಸ್ತುಶಿಲ್ಪ - ನಿಮ್ಮ AI ಅಪ್ಲಿಕೇಶನ್ಗಳಿಗೆ ಶಕ್ತಿ ನೀಡುವ ಕೋರ್ ಘಟಕಗಳು*

## LangChain4j ಅವಲಂಬನೆಗಳು

ಈ ವೇಗದ ಪ್ರಾರಂಭವು ಮೂರು Maven ಅವಲಂಬನೆಗಳನ್ನು ಬಳಸುತ್ತದೆ [`pom.xml`](../../../00-quick-start/pom.xml) ನಲ್ಲಿ:

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

`langchain4j-open-ai-official` ಮೋಡ್ಯೂಲ್ `OpenAiOfficialChatModel` ಕ್ಲಾಸ್ ನ್ನು ಒದಗಿಸುತ್ತದೆ, ಇದು OpenAI-ಅನುಕೂಲವಾದ API ಗೆ ಸಂಪರ್ಕಿಸುತ್ತದೆ. GitHub Models ಕೂಡ ಅದೇ API ಫಾರ್ಮ್ಯಾಟ್ ಬಳಸುತ್ತದೆ, ಆದ್ದರಿಂದ ವಿಶೇಷ ಅಡಾಪ್ಟರ್ ಅಗತ್ಯವಿಲ್ಲ - ಕೇವಲ ಮೂಲ URL ಅನ್ನು `https://models.github.ai/inference` ಗೆ ಸೂಚಿಸಿ.

`langchain4j-easy-rag` ಮೋಡ್ಯೂಲ್ ಸ್ವಯಂಚಾಲಿತ ದಸ್ತಾವೇಜು ವಿಭಜನೆ, ಎम्बೆಡ್ಡಿಂಗ್, ಮತ್ತು ಹಿಂಪಡೆದುಕೊಳ್ಳುವಿಕೆಯನ್ನು ಒದಗಿಸುತ್ತದೆ, ನೀವು ಹಂತ ಹಂತವಾಗಿ ಪ್ರತಿ ಹಂತವನ್ನು ಕೈಯಿಂದ ಸಂರಚಿಸದೆ RAG ಅಪ್ಲಿಕೇಶನ್ಗಳನ್ನು ನಿರ್ಮಿಸಬಹುದು.

## ಅಗತ್ಯಮೀಯಿಗಳು

**Dev Container ಬಳಸುತ್ತಿದ್ದೀರಾ?** Java ಮತ್ತು Maven ಈಗಾಗಲೇ ಸ್ಥಾಪಿಸಲಾಗಿದೆ. ನಿಮಗೆ ಕೇವಲ GitHub ವೈಯಕ್ತಿಕ ಪ್ರವೇಶ ಟೋಕನ್ ಬೇಕಾಗುವುದು.

**ಸ್ಥಳೀಯ ಅಭಿವೃದ್ಧಿ:**
- Java 21+, Maven 3.9+
- GitHub ವೈಯಕ್ತಿಕ ಪ್ರವೇಶ ಟೋಕನ್ (ನೀಳಿನ ಸೂಚನೆಗಳು)

> **ಗಮನಿಸಿ:** ಈ module GitHub Models ನಿಂದ `gpt-4.1-nano` ಅನ್ನು ಬಳಸುತ್ತದೆ. ಕೋಡ್‌ನಲ್ಲಿ ಮಾದರಿ ಹೆಸರನ್ನು ಬದಲಾಯಿಸಬೇಡಿ — GitHub ನ ಲಭ್ಯವಿರುವ ಮಾದರಿಗಳೊಂದಿಗೆ ಕೆಲಸ ಮಾಡುವಂತೆ ಕಾನ್ಫಿಗರ್ ಮಾಡಲಾಗಿದೆ.

## ಸೆಟಪ್

### 1. ನಿಮ್ಮ GitHub ಟೋಕನ್ ಪಡೆಯಿರಿ

1. [GitHub ಸೆಟ್ಟಿಂಗ್ಸ್ → ವೈಯಕ್ತಿಕ ಪ್ರವೇಶ ಟೋಕನ್ಸ್](https://github.com/settings/personal-access-tokens) ಗೆ ಹೋಗಿ
2. " ಹೊಸ ಟೋಕನ್ ರಚಿಸಿ" ಕ್ಲಿಕ್ ಮಾಡಿ
3. ವಿವರಣಾತ್ಮಕ ಹೆಸರು ನೀಡಿ (ಉದಾಹರಣೆಗೆ, "LangChain4j ಡೆಮೋ")
4. ಕಾರಣಾವಧಿ ನಿಗದಿ ಮಾಡಿ (7 ದಿನ ಶಿಫಾರಸು)
5. "ಖಾತೆ ಅನುಮತಿಗಳ" ಅಡಿಯಲ್ಲಿರುವ "Models"ಯನ್ನು "ವಾಚಿಸಲು ಮಾತ್ರ"ಗೆ ಸೆಟ್ ಮಾಡಿ
6. "ಟೋಕನ್ ರಚಿಸಿ" ಕ್ಲಿಕ್ ಮಾಡಿ
7. ನಿಮ್ಮ ಟೋಕನನ್ನು ನಕಲಿಸಿ ಮತ್ತು ಉಳಿಸಿ - ಮತ್ತೊಮ್ಮೆ ಇದು ಕಾಣಿಸುವುದಿಲ್ಲ

### 2. ನಿಮ್ಮ ಟೋಕನ್ ಸೆಟ್ ಮಾಡಿ

**ಆಯ್ಕೆ 1: VS Code ಬಳಸಿ (ಶಿಫಾರಸು)**

ನೀವು VS Code ಬಳಸಿ ಇದ್ದರೆ, ನಿಮ್ಮ ಟೋಕನನ್ನು ಪ್ರಾಜೆಕ್ಟ್‌ನ ರೂಟ್ `.env` ಫೈಲ್‌ಗೆ ಸೇರಿಸಿ:

`.env` ಫೈಲ್ ಇಲ್ಲದಿದ್ದರೆ, `.env.example` ನಿಂದ ನಕಲಿಸಿ `.env` ನ್ನು ರಚಿಸಬಹುದು ಅಥವಾ ನೂತನ `.env` ಫೈಲ್ ನಿರ್ಮಿಸಬಹುದು.

**ಉದಾಹರಣೆಯ `.env` ಫೈಲ್:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env ನಲ್ಲಿ
GITHUB_TOKEN=your_token_here
```

ನಂತರ ನೀವು ಯಾವುದೇ ಡೆಮೊ ಫೈಲ್ (ಉದಾ: `BasicChatDemo.java`) ಮೇಲೆ Explorer ನಲ್ಲಿ ರೈಟ್ ಕ್ಲಿಕ್ ಮಾಡಿ **"Run Java"**ನ್ನು ಆಯ್ಕೆಮಾಡಬಹುದು ಅಥವಾ ರನ್ ಮತ್ತು ಡೀಬಗ್ ಪ್ಯಾನೆಲ್ ನಿಂದ ಲಾಂಚ್ ಕಾನ್ಫಿಗರೇಶನ್ನ್ಗಳನ್ನು ಬಳಸಬಹುದು.

**ಆಯ್ಕೆ 2: ಟರ್ಮಿನಲ್ ಬಳಸಿ**

ಟೋಕನನ್ನು ಪರಿಸರಚರ (environment variable) ಆಗಿ ಸೆಟ್ ಮಾಡಿ:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## ಉದಾಹರಣೆಗಳನ್ನು ರನ್ ಮಾಡಿ

**VS Code ಬಳಸುವವರು:** ಯಾವುದೇ ಡೆಮೊ ಫೈಲ್ ಮೇಲೆ ರೈಟ್ ಕ್ಲಿಕ್ ಮಾಡಿ **"Run Java"** ಆಯ್ಕೆಮಾಡಿ, ಅಥವಾ `.env` ಫೈಲ್‌ಗೆ ನಿಮ್ಮ ಟೋಕನನ್ನು ಸೇರಿಸಿರುವುದನ್ನು ಖಚಿತಪಡಿಸಿ ರನ್ ಮತ್ತು ಡೀಬಗ್ ಪ್ಯಾನೆಲ್ ಮೂಲಕ ಲಾಂಚ್ ಕಾನ್ಫಿಗರೇಶನ್ನ್ಗಳನ್ನು ಬಳಸಿ.

**Maven ಬಳಸಿ:** ಕಮಾಂಡ್ ಲೈನ್‌ನಲ್ಲಿ ಕೆಳಗಿನಂತೆಯೂ ರನ್ ಮಾಡಬಹುದು:

### 1. ಮೂಲ ಚಾಟ್

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. ಪ್ರಾಂಪ್ಟ್ ಮಾದರಿಗಳು

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

ಶೂನ್ಯ-ಶಾಟ್, ಕೆಲವು-ಶಾಟ್, ಚೇನ್-ಆಫ್-ಥಾಟ್, ಮತ್ತು ಪಾತ್ರ ಆಧರಿತ ಪ್ರಾಂಪ್ಟಿಂಗ್ ತೋರಿಸುತ್ತದೆ.

### 3. ಫಂಕ್ಷನ್ ಕರೆ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI ಅಗತ್ಯವಿದ್ದಾಗ ನಿಮ್ಮ Java ವಿಧಾನಗಳನ್ನು ಸ್ವಯಂಚಾಲಿತವಾಗಿ ಕರೆ ಮಾಡುತ್ತದೆ.

### 4. ದಸ್ತಾವೇಜು Q&A (ಸುಲಭ RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

ಸ್ವಯಂಚಾಲಿತ ಎಂಬೆಡ್ಡಿಂಗ್ ಮತ್ತು ಹಿಂಪಡೆದುಕೊಳ್ಳುವಿಕೆಯನ್ನು ಉಪಯೋಗಿಸಿ ಸುಲಭ RAG ಮೂಲಕ ನಿಮ್ಮ ದಸ್ತಾವೇಜುಗಳ ಬಗ್ಗೆ ಪ್ರಶ್ನೆಗಳನ್ನು ಕೇಳಬಹುದು.

### 5. ಜವಾಬ್ದಾರಿಯುತ AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

ಹಾನಿಕಾರಕ ವಿಷಯವನ್ನು ತಡೆಹಿಡಿಯುವ AI ಸುರಕ್ಷತೆ ಫಿಲ್ಟರ್‌ಗಳನ್ನು ನೋಡಿ.

## ಪ್ರತಿ ಉದಾಹರಣೆ ಏನನ್ನು ತೋರಿಸುತ್ತದೆ

**ಮೂಲ ಚಾಟ್** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j ನ್ನು ಅದರ ಅತ್ಯಂತ ಸರಳ ರೂಪದಲ್ಲಿಬ್ ನೋಡಲು ಇಲ್ಲಿ ಪ್ರಾರಂಭಿಸಿ. ನೀವು `OpenAiOfficialChatModel` ರಚಿಸಿ, `.chat()` ಮೂಲಕ ಪ್ರಾಂಪ್ಟ್ ಕಳುಹಿಸಿ, ಮತ್ತು ಪ್ರತಿಕ್ರಿಯೆಯನ್ನು ಪಡೆಯುತ್ತೀರಿ. ಇದು ಮೂಲಭೂತವನ್ನು ತೋರಿಸುತ್ತದೆ: ಕಸ್ಟಮ್ ಅಂತಿಮ ಬಿಂದುಗಳು ಮತ್ತು API ಕೀಲಿಗಳೊಂದಿಗೆ ಮಾದರಿಗಳನ್ನು ಹೇಗೆ ಪ್ರಾರಂಭಿಸುವುದು. ಈ ಮಾದರಿಯನ್ನು ತಿಳಿದುಕೊಂಡ ಮೇಲೆ ಉಳಿದವು ಅದಕ್ಕೆ ಆಧಾರವಾಗಿವೆ.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ಚಾಟ್ ಜೊತೆ ಪ್ರಯತ್ನಿಸಿ:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "GitHub Models ನಿಂದ Azure OpenAI ಗೆ ಈ ಕೋಡ್ ನಲ್ಲಿ ನಾನು ಹೇಗೆ ಬದಲಾಯಿಸಬಹುದು?"
> - "OpenAiOfficialChatModel.builder() ನಲ್ಲಿ ಇನ್ನೇನು ಪರಿಮಿತಿಗಳನ್ನು ನಾನು ಹೊಂದಿಸಬಹುದು?"
> - "ಸಂಪೂರ್ಣ ಪ್ರತಿಕ್ರಿಯೆಯನ್ನು ಕಾಯದೆ ಸ್ಟ್ರೀಮಿಂಗ್ ಪ್ರತಿಕ್ರಿಯೆಗಳನ್ನು ನಾನು ಹೇಗೆ ಸೇರಿಸಬಹುದು?"

**ಪ್ರಾಂಪ್ಟ್ ಎಂಜಿನಿಯರಿಂಗ್** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

ನೀವು ಈಗ ಮಾದರಿಯನ್ನು ಮಾತನಾಡಿಸುವುದನ್ನು ತಿಳಿದಿದ್ದೀರಿ, ಆದ್ರೆ ನೀವು ಅದಕ್ಕೆ ಏನು ಹೇಳಬಯಸುತ್ತೀರಿ ಅದನ್ನು ಅನ್ವೇಷಿಸೋಣ. ಈ ಡೆಮೊ ಅದೇ ಮಾದರಿ ನಿಯೋಜನೆಯನ್ನು ಬಳಸುತ್ತದೆ ಆದರೆ ಐದು ವಿಭಿನ್ನ ಪ್ರಾಂಪ್ಟ್ ಮಾದರಿಗಳನ್ನು ತೋರಿಸುತ್ತದೆ. ನೇರ ಸೂಚನೆಗಳಿಗೆ ಶೂನ್ಯ-ಶಾಟ್ ಪ್ರಾಂಪ್ಟ್‌ಗಳು, ಉದಾಹರಣೆಗಳಿಂದ ಕಲಿಯುವ ಕೆಲವು-ಶಾಟ್ ಪ್ರಾಂಪ್ಟ್‌ಗಳು, ಯೋಚನೆಚೈನ್ ಪ್ರಾಂಪ್ಟ್‌ಗಳು ಮತ್ತು ಸನ್ನಿವೇಶವನ್ನು ಹೊಂದಿಸುವ ಪಾತ್ರ ಆಧಾರಿತ ಪ್ರಾಂಪ್ಟ್‌ಗಳನ್ನು ಪ್ರಯತ್ನಿಸಿ. ನೀವು ಕೇಳುವ ರೀತಿಯ ಪ್ರಕಾರ ಅದೇ ಮಾದರಿ ಹೇಗೆ ಭಿನ್ನ ಫಲಿತಾಂಶಗಳನ್ನು ನೀಡುತ್ತದೆ ಎಂಬುದನ್ನು ನೋಡುತ್ತೀರಿ.

ಡೆಮೊ ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೇಟುಗಳನ್ನು ಸಹ ತೋರಿಸುತ್ತದೆ, ಅವು ಚರಗಳನ್ನು ಬಳಸಿಕೊಂಡು ಪುನಃಬಳಕೆ ಮಾಡಬಹುದಾದ ಪ್ರಾಂಪ್ಟ್‌ಗಳನ್ನು ರಚಿಸುವ ಶಕ್ತಿಶಾಲಿ ವಿಧಾನ.

ಕೆಳಗಿನ ಉದಾಹರಣೆ LangChain4j `PromptTemplate` ಉಪಯೋಗಿಸಿ ಚರಭರಿತ ಪ್ರಾಂಪ್ಟ್ ಅನ್ನು ತೋರಿಸುತ್ತದೆ. AI ನೀಡಲಾದ ಗಮ್ಯಸ್ಥಾನ ಮತ್ತು ಚಟುವಟಿಕೆಯ ಆಧಾರದ ಮೇಲೆ ಉತ್ತರಿಸುತ್ತದೆ.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ಚಾಟ್ ಜೊತೆ ಪ್ರಯತ್ನಿಸಿ:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "ಶೂನ್ಯ-ಶಾಟ್ ಮತ್ತು ಕೆಲವು-ಶಾಟ್ ಪ್ರಾಂಪ್ಟಿಂಗ್ ನಡುವೆ ಏನು ವ್ಯತ್ಯಾಸವಿದೆ ಮತ್ತು ಯಾವಾಗ ಯಾವದನ್ನು ಉಪಯೋಗಿಸಬೇಕು?"
> - "ತಾಪಮಾನ ಪರಿಮಿತಿಗಳು ಮಾದರಿಯ ಪ್ರತಿಕ್ರಿಯೆಗಳಿಗೆ ಹೇಗೆ ಪರಿಣಾಮ ಬೀರುತ್ತವೆ?"
> - "ಉತ್ಪಾದನೆಯಲ್ಲಿ ಪ್ರಾಂಪ್ಟ್ ಇಂಜೆಕ್ಷನ್ ದಾಳಿಗಳನ್ನು ತಡೆಯಲು ಯಾವ ತಂತ್ರಗಳನ್ನು ಉಪಯೋಗಿಸಬಹುದು?"
> - "ಸಾಮಾನ್ಯ ಮಾದರಿಗಳಿಗಾಗಿ ಪುನಃಬಳಕೆ ಮಾಡಬಹುದಾದ PromptTemplate ವಸ್ತುಗಳನ್ನು ನಾನು ಹೇಗೆ ರಚಿಸಬಹುದು?"

**ಟೂಲ್ ಇಂಟಿಗ್ರೇಶನ್** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ಇಲ್ಲಿ LangChain4j ಶಕ್ತಿಶಾಲಿಯಾಗುತ್ತದೆ. ನೀವು ನಿಮ್ಮ Java ವಿಧಾನಗಳನ್ನು ಕರೆ ಮಾಡಬಲ್ಲ AI ಸಹಾಯಕನಾಗಿ `AiServices` ನ್ನು ಬಳಸುತೀರಿ. ಮೆತಧಗಳನ್ನು `@Tool("ವಿವರಣೆ")`ಅಂದಾಗಿ ಗುರುತಿಸಿ ಮತ್ತು LangChain4j ಉಳಿದವನ್ನೆಲ್ಲ ನಿರ್ವಹಿಸುತ್ತದೆ - AI ಯು ಬಳಕೆದಾರ ಕೇಳಿದ ಒದಗುತ್ತದೆ ಪ್ರತಿ ಪರಿಕರವನ್ನು ಸ್ವಯಂಚಾಲಿತವಾಗಿ ಕರೆಮಾಡುತ್ತದೆ. ಇದು ಫಂಕ್ಷನ್ ಕರೆ, ದೊಡ್ಡ ಸಮಸ್ಯೆಗಳನ್ನು ಪರಿಹರಿಸಲು AI ಕ್ರಿಯೆಗಳನ್ನು ಹೇಗೆ ತೆಗೆದುಕೊಳ್ಳುತ್ತದೆ ಎಂಬ ಪ್ರಮುಖ ತಂತ್ರವನ್ನು ತೋರಿಸುತ್ತದೆ.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ಚಾಟ್ ಜೊತೆ ಪ್ರಯತ್ನಿಸಿ:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "@Tool ಅನೋಟೇಶನ್ ಹೇಗೆ ಕೆಲಸಮಾಡುತ್ತೆ ಮತ್ತು LangChain4j ಇದರಿಂದ ಹಿಂದೆ ಏನು ಮಾಡುತ್ತದೆ?"
> - "ಸಂಕೀರ್ಣ ಸಮಸ್ಯೆಗಳನ್ನು ಪರಿಹರಿಸಲು AI ಹಲವು ಉಪಕರಣಗಳನ್ನು ಕ್ರಮವಾಗಿ ಕರೆಮಾಡಬಹುದೇ?"
> - "ಒಂದು ಪರಿಕರವು ಹೊರತಾಗುವ ದೋಷವನ್ನು ಏಕೆThrow ಮಾಡಿದರೆ ನಾನು ದೋಷವನ್ನು ಹೇಗೆ ನಿರ್ವಹಿಸಬೇಕು?"
> - "ಈ ಕ್ಯಾಲ್ಕುಲೇಟರ್ ಉದಾಹರಣೆಯ ಬದಲು ನಿಜವಾದ API ಅನ್ನು ನಾನು ಹೇಗೆ ಅಳವಡಿಸಬಹುದು?"

**ದಸ್ತಾವೇಜು Q&A (ಸುಲಭ RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ಇಲ್ಲಿ ನೀವು RAG (ಪುನಃಪಡೆಯುವಿಕೆಯಂತಹ ತಯಾರಿಕೆ) ಅನ್ನು LangChain4j ನ "ಸುಲಭ RAG" ವಿಧಾನವನ್ನು ಬಳಸಿಕೊಂಡು ನೋಡಬಹುದು. ದಸ್ತಾವೇಜುಗಳನ್ನು ಲೋಡ್ ಮಾಡಲಾಗುತ್ತದೆ, ಸ್ವತಃ ವಿಧಿಮಾಡಿ ಮತ್ತು ಮೆಮೊರಿಯಲ್ಲಿ ಸಂಗ್ರಹಿಸುವಂತೆ ಇಂಬೆಡ್ ಮಾಡಲಾಗುತ್ತದೆ, ನಂತರ ವಿಷಯ ಹಿಂಪಡೆಯುವಿಕೆ (retriever) AI ಗೆ ಪ್ರಶ್ನಾ ಸಮಯದಲ್ಲಿ ಸಾನ್ವಿಧಾನಿಕ ತುಂಡುಗಳನ್ನು ನೀಡುತ್ತದೆ. AI ನಿಮ್ಮ ದಸ್ತಾವೇಜುಗಳ ಆಧಾರದ ಮೇಲೆ ಉತ್ತರಿಸುತ್ತದೆ, ಅದರ ಸಾಮಾನ್ಯ ಜ್ಞಾನ ಅಲ್ಲ.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ಚಾಟ್ ಜೊತೆ ಪ್ರಯತ್ನಿಸಿ:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "RAG AI ಮರುಕತೆಗಳನ್ನು ತಡೆಯಲು ಮಾದರಿಯ ತರಬೇತಿ ಡೇಟಾವನ್ನು ಬಳಸುವುದಾಗತೇ ಹೇಗೆ ಭಿನ್ನ?"
> - "ಈ ಸುಲಭ ವಿಧಾನ ಮತ್ತು ಕಸ್ಟಮ್ RAG ಪೈಪ್ಲೈನ್ ನಡುವಿನ ವ್ಯತ್ಯಾಸವೇನು?"
> - "ಇದು ಎನಬಹುದೆ? ಹಲವಾರು ದಸ್ತಾವೇಜುಗಳು ಅಥವಾ ದೊಡ್ಡ ಜ್ಞಾನ ಆಧಾರಗಳನ್ನು ನಿಭಾಯಿಸಲು ನಾನು ಇದನ್ನು ಹೇಗೆ ವಿಸ್ತರಿಸಬಹುದು?"

**ಜವಾಬ್ದಾರಿಯುತ AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ಆಳವಾದ ರಕ್ಷಣಾ ಪದರಗಳೊಂದಿಗೆ AI ಸುರಕ್ಷತೆಯನ್ನು ನಿರ್ಮಿಸಿ. ಈ ಡೆಮೊ ಎರಡು ಪದರಗಳನ್ನು ಒಟ್ಟಿಗೆ ಕೆಲಸ ಮಾಡುತ್ತಿರುವುದನ್ನು ತೋರುತ್ತದೆ:

**ಭಾಗ 1: LangChain4j ಇನ್‌ಪುಟ್ ಗಾರ್ಡ್‌ರೈಲ್ಸ್** - LLM ಗೆ ತಲುಪುವ ಮುನ್ನ ಅಪಾಯಕರ ಪ್ರಾಂಪ್ಟ್ಗಳನ್ನು ನಿರ್ಬಂಧಿಸಿ. ನಿಷಿದ್ಧ ಕೀವರ್ಡ್‌ಗಳು ಅಥವಾ ಮಾದರಿಗಳಿಗೆ ತಕ್ಕಂತೆ ಕಸ್ಟಮ್ ಗಾರ್ಡ್‌ರೈಲ್ಸ್ ರಚಿಸಿ. ಇವು ನಿಮ್ಮ ಕೋಡ್ನಲ್ಲಿ ಚಲಿಸುತ್ತವೆ, ಆದ್ದರಿಂದ ವೇಗವಾದ ಮತ್ತು ಉಚಿತ.

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

**ಭಾಗ 2: ಪೂರೈಕೆದಾರ ಸುರಕ್ಷತಾ ಫಿಲ್ಟರ್‌ಗಳು** - GitHub Models ನಿರ್ಮಿತ ಫಿಲ್ಟರ್‌ಗಳನ್ನು ಹೊಂದಿವೆ, ನಿಮ್ಮ ಗಾರ್ಡ್‌ರೈಲ್ಸ್ ತಪ್ಪಿಸಿದುದನ್ನು ಹಿಡಿಯುತ್ತದೆ. ತೀವ್ರ ಉಲ್ಲಂಘನೆಗಳಿಗೆ HTTP 400 ದೋಷದೊಂದಿಗೆ ಹಾರ್ಡ್ ಬ್ಲಾಕ್‌ಗಳು ಮತ್ತು AI ಸೌಮ್ಯವಾಗಿ ನಿರಾಕರಿಸುವ ಸಾಫ್ಟ್ ನಿರಾಕರಣೆಗಳಿವೆ.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ಚಾಟ್ ಜೊತೆ ಪ್ರಯತ್ನಿಸಿ:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "InputGuardrail ಎಂದರೆ ಏನು ಮತ್ತು ನಾನು ಸ್ವಂತದನ್ನು ಹೇಗೆ ರಚಿಸಬಹುದು?"
> - "ಹಾರ್ಡ್ ಬ್ಲಾಕ್ ಮತ್ತು ಸಾಫ್ಟ್ ನಿರಾಕರಣೆ ನಡುವಿನ ವ್ಯತ್ಯಾಸವೇನು?"
> - "ಗಾರ್ಡ್‌ರೈಲ್ಸ್ ಮತ್ತು ಪೂರೈಕೆದಾರ ಫಿಲ್ಟರ್‌ಗಳನ್ನು ಒಟ್ಟಿಗೆ ಬಳಸುವುದು ಯಾಕೆ ಬಹುಮುಖ್ಯ?"

## ಮುಂದಿನ ಹಂತಗಳು

**ಮುಂದಿನ Module:** [01-ಪರಿಚಯ - LangChain4j ಮತ್ತು gpt-5 ಜೊತೆಗೆ Azure ಮೂಲಕ ಪ್ರಾರಂಭ](../01-introduction/README.md)

---

**ನ್ಯಾವಿಗೇಶನ್:** [← ಮುಖ್ಯಕ್ಕೆ ಹಿಂತಿರುಗಿ](../README.md) | [ಮುಂದೆ: Module 01 - ಪರಿಚಯ →](../01-introduction/README.md)

---

## ತಪ್ಪುಪಡಿ ಪರಿಹಾರ

### ಮೊದಲ ಬಾರಿಗೆ Maven ಬಿಲ್ಡ್

**ಸಮಸ್ಯೆ**: ಪ್ರಾರಂಭಿಕ `mvn clean compile` ಅಥವಾ `mvn package` ಮಾಡುವುದು ದೀರ್ಘಕಾಲ (10-15 ನಿಮಿಷ) ಎಡದಿದೆ

**ಕಾರಣ**: ಮೊದಲು ಬಿಲ್ಡ್ ವೇಳೆ Maven ಎಲ್ಲಾ ಪ್ರಾಜೆಕ್ಟ್ ಅವಲಂಬನೆಗಳನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡುವುದು (Spring Boot, LangChain4j ಗ್ರಂಥಾಲಯಗಳು, Azure SDKಗಳು ಇತ್ಯಾದಿ).

**ಪರಿಹಾರ**: ಇದು ಸಹಜ ವರ್ತನೆಯಾಗಿದೆ. ಮುಂದಿನ ಬಿಲ್ಡುಗಳು ಬಹಳ ವೇಗವಾಗಿ ನಡೆಯುತ್ತವೆ ಏಕೆಂದರೆ ಅವಲಂಬನೆಗಳು ಸ್ಥಳೀಯವಾಗಿ ಕ್ಯಾಶ್ ಆಗಿರುತ್ತವೆ. ಡೌನ್‌ಲೋಡ್ ಸಮಯ ನಿಮ್ಮ ನೆಟ್‌ವರ್ಕ್ ವೇಗದ ಮೇಲೆ ಆಧಾರಿತವಾಗಿದೆ.

### PowerShell Maven ಕಮಾಂಡ್ ವ್ಯಾಕರಣ

**ಸಮಸ್ಯೆ**: Maven ಕಮಾಂಡ್‌ಗಳು `Unknown lifecycle phase ".mainClass=..."` ಎಂಬ ದೋಷದೊಂದಿಗೆ ವಿಫಲವಾಗುತ್ತದೆ
**ಕಾರಣ**: PowerShell `=` ಅನ್ನು ಒಂದು චರ(variable) ನಿಯೋಜನೆ ಸಂಸ್ಥಾಪಕ(operator) ಎಂದು ಅರ್ಥಮಾಡಿಕೊಳ್ಳುತ್ತದೆ, ಇದು Maven ಗುಣಲಕ್ಷಣ(property) ವಾಕ್ಯರಚನೆಯನ್ನು ಮೊರೆತಿದ್ದಾನೆ

**ಪರಿಹಾರ**: Maven ಆಜ್ಞೆಗೆ ಮೊದಲು `--%` ನ ಸ್ಥಗಿತ-ವಿಶ್ಲೇಷಣೆ ಕಾರ್ಯೋಪಕರಣ(stop-parsing operator) ಬಳಸಿ:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ಕಾರ್ಯೋಪಕರಣ PowerShell ಗೆ ಉಳಿದ ಎಲ್ಲಾ ಆರ್ಗ್ಯುಮೆಂಟ್‌ಗಳನ್ನು ಅರ್ಥಮಾಡಿಕೊಳ್ಳದೆ ನೇರವಾಗಿ Mavenಗೆ ಹಸ್ತಾಂತರಿಸಲು ಸೂಚಿಸುತ್ತದೆ.

### ವಿಂಡೋ우ಸ್ PowerShell ಎಮೋಜಿ ಪ್ರದರ್ಶನ

**ಸಮಸ್ಯೆ**: PowerShell ನಲ್ಲಿ AI ಪ್ರತಿಕ್ರಿಯೆಗಳು ಎಮೋಜಿ ಬದಲು ಗರ್ಭಿತ ಅಕ್ಷರಗಳನ್ನು (ಉದಾಹರಣೆಗಾಗಿ `????` ಅಥವಾ `â??`) ತೋರುತ್ತವೆ

**ಕಾರಣ**: PowerShell ಡಿಫಾಲ್ಟ್ ಎನ್ಕೋಡಿಂಗ್ UTF-8 ಎಮೋಜಿಗಳನ್ನು ಬೆಂಬಲಿಸುವುದಿಲ್ಲ

**ಪರಿಹಾರ**: Java ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ಕಾರ್ಯಗತಗೊಳಿಸುವ ಮೊದಲು ಈ ಆಜ್ಞೆಯನ್ನು exಕಳೆದಿರಿ:
```cmd
chcp 65001
```

ಇದು ಟರ್ಮಿನಲ್‌ನಲ್ಲಿ UTF-8 ಎನ್ಕೋಡಿಂಗ್ ಅನ್ನು ಬಲಪಡಿಸುತ್ತದೆ. ಪರ್ಯಾಯವಾಗಿ, ಉತ್ತಮ ಯುನಿಕೋಡ್ ಬೆಂಬಲ ಹೊಂದಿರುವ Windows Terminal ಅನ್ನು ಬಳಸಬಹುದು.

### API ಕಾಲ್‌ಗಳನ್ನು ಡೀಬಗ್ ಮಾಡುವುದು

**ಸಮಸ್ಯೆ**: ಪ್ರಮಾಣೀಕರಣ ದೋಷಗಳು, ದರ ಮಿತಿ, ಅಥವಾ AI ಮಾದರಿಯಿಂದ ಅಪ್ರತೀಕ್ಷಿತ ಪ್ರತಿಕ್ರಿಯೆಗಳು

**ಪರಿಹಾರ**: ಉದಾಹರಣೆಗಳಲ್ಲಿ `.logRequests(true)` ಮತ್ತು `.logResponses(true)` ಅನ್ನು ಒಳಗೊಂಡಿವೆ, ಇದು API ಕಾಲ್‌ಗಳನ್ನು ಕನ್ಸೋಲಿನಲ್ಲಿ ತೋರಿಸುತ್ತದೆ. ಇದರಿಂದ ಪ್ರಮಾಣೀಕರಣ ದೋಷಗಳು, ದರ ಮಿತಿ ಅಥವಾ ಅಪ್ರತೀಕ್ಷಿತ ಪ್ರತಿಕ್ರಿಯೆಗಳನ್ನು ಪತ್ತೆಹಚ್ಚಲು ಸಹಾಯವಾಗುತ್ತದೆ. ಉತ್ಪಾದನೆಯಲ್ಲಿ ಈ ಫ್ಲಾಗ್‌ಗಳನ್ನು ತೆಗೆದು ಹಾಕಿ ಲಾಗ್ ಶಬ್ದವನ್ನು ಕಡಿಮೆಮಾಡಿ.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ತಕ್ಷಣದ ಅನುವಾದ ಸೂಚನೆ**:  
ಈ ದಾಖಲೆ AI ಅನುವಾದ ಸೇವೆ [Co-op Translator](https://github.com/Azure/co-op-translator) ಅನ್ನು ಬಳಸಿ ಅನುವಾದಿಸಲಾಗಿದೆ. ನಾವು ನಿಖರತೆಯನ್ನು ಪ್ರಯತ್ನಿಸುವುದಾಗಿ ಇದ್ದರೂ, ಸ್ವಯಂಚಾಲಿತ ಅನುವಾದಗಳಲ್ಲಿ ದೋಷಗಳು ಅಥವಾ ತಪ್ಪು ಅರ್ಥಗಳನ್ನು ಹೊಂದಿರಬಹುದು. ಮೂಲ ಭಾಷೆಯಲ್ಲಿನ ಮೂಲ ದಾಖಲೆ ಪ್ರಾಮಾಣಿಕ ಮೂಲವಾಗಿ ಪರಿಗಣಿಸಬೇಕು. ಅತ್ಯಂತ ಮುಖ್ಯ ಮಾಹಿತಿಗಾಗಿ ವೃತ್ತಿಪರ ಮಾನವ ಅನುವಾದವನ್ನು ಶಿಫಾರಸು ಮಾಡಲಾಗುತ್ತದೆ. ಈ ಅನುವಾದ ಬಳಕೆಯಿಂದ ಉಂಟಾಗುವ ಯಾವುದೇ ತಪ್ಪು ಅರ್ಥ ಅಥವಾ ಭ್ರಮೆಗಳಿಗಾಗಿ ನಾವು ಹೊಣೆಗಾರರಲ್ಲ.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->