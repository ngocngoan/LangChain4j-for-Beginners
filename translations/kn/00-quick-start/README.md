# ಕಾರ್ಯಾಗಾರ 00: ತ್ವರಿತ ಪ್ರಾರಂಭ

## ವಿಷಯಸೂಚಿ

- [ಪರಿಚಯ](../../../00-quick-start)
- [ಲ್ಯಾಂಗ್‌ಚೈನ್4j ಎಂದರೆ ಏನು?](../../../00-quick-start)
- [ಲ್ಯಾಂಗ್‌ಚೈನ್4j ಅವಲಂಬನೆಗಳು](../../../00-quick-start)
- [ಪೂರ್ವಾಪೇಕ್ಷಿತಗಳು](../../../00-quick-start)
- [ಸಕ್ರಿಯಗೊಳಿಸುವಿಕೆ](../../../00-quick-start)
  - [1. ನಿಮ್ಮ GitHub ಟೋಕನ್ ಪಡೆಯಿರಿ](../../../00-quick-start)
  - [2. ನಿಮ್ಮ ಟೋಕನ್ ಸೆಟ್ ಮಾಡಿ](../../../00-quick-start)
- [ಉದಾಹರಣೆಗಳನ್ನು ಚಲಾಯಿಸಿ](../../../00-quick-start)
  - [1. ಮೂಲчат್](../../../00-quick-start)
  - [2. ಪ್ರಾಂಪ್ಟ್ ವಿನ್ಯಾಸಗಳು](../../../00-quick-start)
  - [3. ಫಂಕ್ಷನ್ ಕರೆಮಾಡುವಿಕೆ](../../../00-quick-start)
  - [4. ಡಾಕ್ಯುಮೆಂಟ್ ಪ್ರಶ್ನೋತ್ತರ (ಸುಲಭ RAG)](../../../00-quick-start)
  - [5. ಜವಾಬ್ದಾರಿಯುತ AI](../../../00-quick-start)
- [ಪ್ರತಿ ಉದಾಹರಣೆ ಏನು ತೋರಿಸುತ್ತದೆ](../../../00-quick-start)
- [ಮುಂದಿನ ಹಂತಗಳು](../../../00-quick-start)
- [ತೊಂದರೆ ಪರಿಹಾರ](../../../00-quick-start)

## ಪರಿಚಯ

ಈ ತ್ವರಿತ ಪ್ರಾರಂಭವು LangChain4j ಬಳಸಿ ನಿಮಗೆ ಸಾಧ್ಯವಾದಷ್ಟು ಬೇಗ ಕಾರ್ಯಾರಂಭ ಮಾಡಲು ಉದ್ದೇಶಿಸಲಾಗಿದೆ. ಇದು LangChain4j ಮತ್ತು GitHub ಮಾದರಿಗಳೊಂದಿಗೆ AI ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ಸುಲಭವಾಗಿ ರಚಿಸುವ ಮೂಲಭೂತ ವಿಷಯಗಳನ್ನು ಒಳಗೊಂಡಿದೆ. ಮುಂದಿನ ಕಾರ್ಯಾಗಾರಗಳಲ್ಲಿ ನೀವು Azure OpenAI ಮತ್ತು GPT-5.2 ಗೆ ಬದಲಾವಣೆ ಮಾಡಿ ಪ್ರತಿ ಕಲ್ಪನೆಗಳನ್ನು ಆಳವಾಗಿ ಅಧ್ಯಯನ ಮಾಡುತ್ತೀರಿ.

## ಲ್ಯಾಂಗ್‌ಚೈನ್4j ಎಂದರೆ ಏನು?

ಲ್ಯಾಂಗ್‌ಚೈನ್4j ಒಂದು ಜಾವಾ ಲೈಬ್ರರಿ ಆಗಿದ್ದು, AI ಚಾಲಿತ ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ನಿರ್ಮಿಸಲು ಸುಲಭಗೊಳಿಸುತ್ತದೆ. HTTP ಕ್ಲೈಂಟ್‌ಗಳು ಮತ್ತು JSON ಪಾರ್ಸಿಂಗ್ ಕಾರ್ಯಗಳನ್ನು ನಿಭಾಯಿಸುವ ಬದಲಾಗಿ, ನೀವು ಸ್ವಚ್ಛ ಜಾವಾ API ಗಳೊಂದಿಗೆ ಕೆಲಸ ಮಾಡುತ್ತೀರಿ.

ಲ್ಯಾಂಗ್‌ಚೈನ್‌ನಲ್ಲಿ "ಚೈನ್" ಅಂದರೆ ಅನೇಕ ಘಟಕಗಳನ್ನು ಸರಣಿಯಾಗಿ ಸಂಯೋಜಿಸುವುದು — ನೀವು ಪ್ರಾಂಪ್ಟ್ ಅನ್ನು ಮಾದರಿಗೆ, ಹಾಗು ಅದರಿಂದ ಪಾರ್ಸರ್‌ಗೆ, ಅಥವಾ ಅನೇಕ AI ಕರೆಗಳನ್ನು ಸರಣಿಯಾಗಿ ಸೇರಿಸಬಹುದು, ಅಲ್ಲಿ ಒಂದು ಔಟ್ಪುಟ್ ಮುಂದಿನ ಇನ್‌ಪುಟ್ ಆಗಿ ಬಳಸಲ್ಪಡುತ್ತದೆ. ಈ ತ್ವರಿತ ಪ್ರಾರಂಭ ಮೂಲಭೂತಾಂಶಗಳ ಮೇಲೆ ಕೇಂದ್ರಿತವಾಗಿದೆ, ಮುಂದಕ್ಕೆ ಹೆಚ್ಚು ಸಂಕೀರ್ಣ ಸರಣಿಗಳನ್ನು ಅನ್ವೇಷಿಸುವ ಮೊದಲು.

<img src="../../../translated_images/kn/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*ಲ್ಯಾಂಗ್‌ಚೈನ್4j ನಲ್ಲಿ ಘಟಕಗಳನ್ನು ಸರಣಿಪಡಿಸುವುದು - ಶಕ್ತಿಶಾಲಿ AI ಕಾರ್ಯಪ್ರವಾಹಗಳನ್ನು ನಿರ್ಮಿಸುವ ಕಟ್ಟಡ ಬ್ಲಾಕ್ಗಳು*

ನಾವು ಮೂರು ಪ್ರಮುಖ ಘಟಕಗಳನ್ನು ಬಳಸುತ್ತೇವೆ:

**ChatModel** - AI ಮಾದರಿಯ ಜೊತೆಗೆ ಸಂವಹನ ಮಾಡುವ ಅಂತರಮುಖ. `model.chat("prompt")` ಅನ್ನು ಕರೆಸಿ ಉತ್ತರ ಸರಣಿಯನ್ನು ಪಡೆಯಿರಿ. ನಾವು `OpenAiOfficialChatModel` ಅನ್ನು ಬಳಸುತ್ತೇವೆ ಅದು GitHub Models ಹಾಗು OpenAI-ಅನುಕೂಲ ಎಂಡ್‌ಪಾಯಿಂಟ್‌ಗಳೊಂದಿಗೆ ಕೆಲಸ ಮಾಡುತ್ತದೆ.

**AiServices** - ಪ್ರಕಾರ-ಸುರಕ್ಷಿತ AI ಸೇವಾ ಅಂತರಮುಖಗಳನ್ನು ಸೃಷ್ಟಿಸುತ್ತದೆ. ವಿಧಾನಗಳನ್ನು ವ್ಯಾಖ್ಯಾನಿಸಿ, ಅವುಗಳನ್ನು `@Tool` ಅನೋಟೇಷನ್ ಮಾಡಿ, ಮತ್ತು LangChain4j ಇವುಗಳ ನಿರ್ವಹಣೆಯನ್ನು ಮಾಡುತ್ತದೆ. AI ಅಗತ್ಯವಿದ್ದಾಗ ನಿಮ್ಮ ಜಾವಾ ವಿಧಾನಗಳನ್ನು ಸ್ವಯಂಚಾಲಿತವಾಗಿ ಕರೆ ಮಾಡುತ್ತದೆ.

**MessageWindowChatMemory** - ಮಾತುಕತೆಯ ಇತಿಹಾಸವನ್ನು ನಿರ್ವಹಿಸುತ್ತದೆ. ಇದಿಲ್ಲದೆ ಪ್ರತಿ ವಿನಂತಿ ಸ್ವತಂತ್ರವಾಗಿರುತ್ತದೆ. ಇದಿರುವಾಗ, AI ಹಿಂದಿನ ಸಂದೇಶಗಳನ್ನು ನೆನಪಿಡುತ್ತದೆ ಮತ್ತು ಹಲವಾರು ಹಂತಗಳಲ್ಲಿ ಸಂದರ್ಭ ನಿರ್ವಹಣೆ ಮಾಡುತ್ತದೆ.

<img src="../../../translated_images/kn/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*ಲ್ಯಾಂಗ್‌ಚೈನ್4j ಸ್ಥಾಪನೆ - ಕೋರ್ ಘಟಕಗಳು ಸೇರಿ ನಿಮ್ಮ AI ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ಚಾಲನೆ ನೀಡುತ್ತವೆ*

## ಲ್ಯಾಂಗ್‌ಚೈನ್4j ಅವಲಂಬನೆಗಳು

ಈ ತ್ವರಿತ ಪ್ರಾರಂಭವು [`pom.xml`](../../../00-quick-start/pom.xml) ನಲ್ಲಿ ಮೂರು Maven ಅವಲಂಬನೆಗಳನ್ನು ಉಪಯೋಗಿಸುತ್ತದೆ:

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

`langchain4j-open-ai-official` ಘಟಕವು `OpenAiOfficialChatModel` ಕ್ಲಾಸ್ ಅನ್ನು ಒದಗಿಸುತ್ತದೆ ಅದು OpenAI-ಅನುಕೂಲ API ಗಳಿಗೆ ಸಂಪರ್ಕ ಮಾಡುತ್ತದೆ. GitHub Models ಅದೇ API ಸ್ವರೂಪವನ್ನು ಬಳಸುತ್ತದೆ, ಆದ್ದರಿಂದ ವಿಶೇಷ ಅಡಾಪ್ಟರ್ ಅಗತ್ಯವಿಲ್ಲ - ಕೇವಲ ಮೂಲ URL ಅನ್ನು `https://models.github.ai/inference` ಗೆ ಸೂಚಿಸಿ.

`langchain4j-easy-rag` ಘಟಕವು ಸ್ವಯಂಚಾಲಿತ ಡಾಕ್ಯುಮೆಂಟ್ ವಿಭಜನೆ, ಎम्बೆಡಿಂಗ್ ಮತ್ತು ಹುಡುಕಾಟ ವ್ಯವಸ್ಥೆಯನ್ನು ಒದಗಿಸುತ್ತದೆ, ಇದರಿಂದ ನೀವು RAG ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ಕೈೆಯಿಂದ ಪ್ರತಿ ಹಂತವನ್ನು ಸಂರಚಿಸುವ ಅಗತ್ಯವಿಲ್ಲದೆ ನಿರ್ಮಿಸಬಹುದು.

## ಪೂರ್ವಾಪೇಕ್ಷಿತಗಳು

**ಡೆವ್ ಕಂಟೈನರ್ ಬಳಸಿ ಸಂತೋೕಷ?** ಜಯ ಮತ್ತು ಮೆವನ್ ಈಗಾಗಲೇ ಪ್ರತಿಷ್ಠಾಪಿಸಲಾಗಿದೆ. ನಿಮಗೆ ಕೇವಲ GitHub ವೈಯಕ್ತಿಕ ಪ್ರವೇಶ ಟೋಕನ್ ಬೇಕು.

**ಸ್ಥಳೀಯ ಅಭಿವೃದ್ಧಿ:**
- ಜಾವಾ 21+, ಮೆವನ್ 3.9+
- GitHub ವೈಯಕ್ತಿಕ ಪ್ರವೇಶ ಟೋಕನ್ (ನೀಡಲಾದ ಸೂಚನೆಗಳು ಕೆಳಗಿನಂತಿವೆ)

> **ಗಮನಿಸಿ:** ಈ ಕಾರ್ಯಾಗಾರವು GitHub Models ರಿಂದ `gpt-4.1-nano` ಬಳಕೆ ಮಾಡುತ್ತದೆ. ಕೋಡ್‌ನಲ್ಲಿ ಮಾದರಿಯ ಹೆಸರನ್ನು ಬದಲಾಯಿಸಬೇಡಿ - ಇದು GitHub ಲಭ್ಯವಿರುವ ಮಾದರಿಗಳೊಂದಿಗೆ ಕೆಲಸ ಮಾಡಲು ಸಂರಚಿಸಲಾಗಿದೆ.

## ಸಕ್ರಿಯಗೊಳಿಸುವಿಕೆ

### 1. ನಿಮ್ಮ GitHub ಟೋಕನ್ ಪಡೆದುಕೊಳ್ಳಿ

1. [GitHub ಸೆಟ್ಟಿಂಗ್ಸ್ → ವೈಯಕ್ತಿಕ ಪ್ರವೇಶ ಟೋಕನ್‌ಗಳು](https://github.com/settings/personal-access-tokens) ಗೆ ಹೋಗಿ
2. "ಹೊಸ ಟೋಕನ್ ಹುಟ್ಟುಹಾಕಿ" ಕ್ಲಿಕ್ ಮಾಡಿ
3. ವಿವರಣೆಾತ್ಮಕ ಹೆಸರು ನೀಡಿ (ಉದಾ: "LangChain4j ಡೆಮೊ")
4. ಅವಧಿ ನಿರ್ಧರಿಸಿ (7 ದಿನ ಶಿಫಾರಸು ಮಾಡಲಾಗಿದೆ)
5. "ಖಾತೆ ಅನುಮತಿಗಳು" ಅಡಿ "ಮಾದರಿಗಳು (Models)" ಅನ್ನು "ಓದಲು ಮಾತ್ರ (Read-only)" ಗೆ ಸೆಟ್ ಮಾಡಿ
6. "ಟೋಕನ್ ಸೃಷ್ಟಿಸಿ" ಕ್ಲಿಕ್ ಮಾಡಿ
7. ನಿಮ್ಮ ಟೋಕನ್ ಅನ್ನು ನಕಲಿಸಿ ಮತ್ತು ಸುರಕ್ಷಿತವಾಗಿ ಉಳಿಸಿ - ಇದು ಮರುಪಡೆಯಲು ಸಾಧ್ಯವಿಲ್ಲ

### 2. ನಿಮ್ಮ ಟೋಕನ್ ಸೆಟ್ ಮಾಡಿ

**ಆಯ್ಕೆ 1: VS ಕೋಡ್ ಬಳಸಿ (ಶಿಫಾರಸು ಮಾಡಲಾಗಿದೆ)**

ನೀವು VS ಕೋಡ್ ಬಳಸಿ ಇದ್ದರೆ, ನಿಮ್ಮ ಟೋಕನ್ ಅನ್ನು ಯೋಜನೆಯ ರೂಟ್‌ನಲ್ಲಿರುವ `.env` ಫೈಲ್‌ಗೆ ಸೇರಿಸಿ:

`.env` ಫೈಲ್ ಇಲ್ಲದಿದ್ದರೆ, `.env.example` ನಕಲು ಮಾಡಿ `.env` ಎಂದು ಹೆಸರು ಹಾಕಿ ಅಥವಾ ಹೊಸ `.env` ಫೈಲ್ ತಯಾರಿಸಿ.

**ಉದಾಹರಣೆಯ `.env` ಫೈಲ್:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env ನಲ್ಲಿ
GITHUB_TOKEN=your_token_here
```

ನಂತರ, ಎಕ್ಸ್‌ಪ್ಲೋರರ್‌ನಲ್ಲಿ ಯಾವುದೇ ಡೆಮೊ ಫೈಲ್ (ಉದಾ: `BasicChatDemo.java`) ಮೇಲೆ ರೈಟ್-ಕ್ಲಿಕ್ ಮಾಡಿ **"Run Java"** ಆಯ್ಕೆಮಾಡಿ ಅಥವಾ ಓಡುತ್ತಿರುವ ಮತ್ತು ಡಿಬಗ್ ಪ್ಯಾನೆಲ್‌ನ ಲಾಂಚ್ ಕಾನ್ಫಿಗರೇಷನ್‌ಗಳನ್ನು ಉಪಯೋಗಿಸಿ.

**ಆಯ್ಕೆ 2: ಟರ್ಮಿನಲ್ ಬಳಸಿ**

ಟೋಕನ್ ಅನ್ನು ಪರಿಸರ ಚರ (environment variable) ಆಗಿ ಸೆಟ್ ಮಾಡಿ:

**ಬ್ಯಾಶ್:**
```bash
export GITHUB_TOKEN=your_token_here
```

**ಪವರ್‌ಶೆಲ್:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## ಉದಾಹರಣೆಗಳನ್ನು ಚಲಾಯಿಸಿ

**VS ಕೋಡ್ ಬಳಸಿ:** ಯಾವುದೇ ಡೆಮೊ ಫೈಲ್ ಮೇಲೆ ರೈಟ್-ಕ್ಲಿಕ್ ಮಾಡಿ **"Run Java"** ಆಯ್ಕೆಮಾಡಿ, ಅಥವಾ ಓಡಿಸುವ ಮತ್ತು ಡಿಬಗ್ ಪ್ಯಾನೆಲ್‌ನ ಲಾಂಚ್ ಕಾನ್ಫಿಗರೇಷನ್ ಉಪಯೋಗಿಸಿ (ನಿಮ್ಮ ಟೋಕನ್ `.env` ಫೈಲಿನಲ್ಲಿ ಸೇರಿಸಿರುವುದನ್ನು ಖಾತ್ರಿ ಪಡಿಸಿಕೊಳ್ಳಿ).

**ಮೆವನ್ ಬಳಸಿ:** ಮುಂತಾದಂತೆ, ಕಮಾಂಡ್ ಲೈನಿಂದ ಆಗಬಹುದು:

### 1. ಮೂಲчат್

**ಬ್ಯಾಶ್:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**ಪವರ್‌ಶೆಲ್:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. ಪ್ರಾಂಪ್ಟ್ ವಿನ್ಯಾಸಗಳು

**ಬ್ಯಾಶ್:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**ಪವರ್‌ಶೆಲ್:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

ಶೂನ್ಯ-ಶಾಟ್, ಕೆಲವು-ಶಾಟ್, ಚೈನ್-ಆಫ್-ಥಾಟ್ ಮತ್ತು ಪಾತ್ರಾಧಾರಿತ ಪ್ರಾಂಪ್ಟ್ ಅನ್ನು ತೋರಿಸುತ್ತದೆ.

### 3. ಫಂಕ್ಷನ್ ಕರೆಮಾಡುವಿಕೆ

**ಬ್ಯಾಶ್:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**ಪವರ್‌ಶೆಲ್:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI ಬೇಕಾದಾಗ ನಿಮ್ಮ ಜಾವಾ ವಿಧಾನಗಳನ್ನು ಸ್ವಯಂಚಾಲಿತವಾಗಿ ಕರೆ ಮಾಡುತ್ತದೆ.

### 4. ಡಾಕ್ಯುಮೆಂಟ್ ಪ್ರಶ್ನೋತ್ತರ (ಸುಲಭ RAG)

**ಬ್ಯಾಶ್:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**ಪವರ್‌ಶೆಲ್:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

ಸ್ವಯಂಚಾಲಿತ ಎಂಬೆಡಿಂಗ್ ಮತ್ತು ಹುಡುಕಾಟದೊಂದಿಗೆ ಸುಲಭ RAG ಉಪಯೋಗಿಸಿ ನಿಮ್ಮ ಡಾಕ್ಯುಮೆಂಟ್ಗಳ ಬಗ್ಗೆ ಪ್ರಶ್ನೆಗಳು ಕೇಳಿ.

### 5. ಜವಾಬ್ದಾರಿಯುತ AI

**ಬ್ಯಾಶ್:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**ಪವರ್‌ಶೆಲ್:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI ಸುರಕ್ಷತಾ ಫಿಲ್ಟರ್‌ಗಳು ಹಾನಿಕಾರಕ ವಿಷಯಗಳನ್ನು ಹೇಗೆ ತಡೆಹಿಡಿಯುತ್ತವೆ ಎಂದು ನೋಡಿ.

## ಪ್ರತಿ ಉದಾಹರಣೆ ಏನು ತೋರಿಸುತ್ತದೆ

**ಮೂಲчат್** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

ಇಲ್ಲಿ LangChain4j ಅನ್ನು ಅತ್ಯಂತ ಸರಳವಾಗಿ ನೋಡಬಹುದು. ನೀವು `OpenAiOfficialChatModel` ರಚಿಸಿ, `.chat()` ಮೂಲಕ ಪ್ರಾಂಪ್ಟ್ ಕಳುಹಿಸಿ ಮತ್ತು ಪ್ರತಿಕ್ರಿಯೆ ಪಡೆಯುತ್ತೀರಿ. ಇದು ಆಧಾರವನ್ನು ತೋರಿಸುತ್ತದೆ: ಕಸ್ಟಮ್ ಎಂಡ್‌ಪಾಯಿಂಟ್ ಹಾಗೂ API ಕೀಲಿಗಳೊಂದಿಗೆ ಮಾದರಿಗಳನ್ನು ಪ್ರಾರಂಭಿಸುವುದು ಹೇಗೆ ಎಂಬುದು. ಈ ಮಾದರಿಯನ್ನು ಅರ್ಜಿ ಮಾಡುವ ಮೂಲಕ ಎಲ್ಲವನ್ನೂ ನಿರ್ಮಿಸಲಾಗುತ್ತದೆ.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 GitHub Copilot ಮೂಲಕ ಪ್ರಯತ್ನಿಸಿ:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "GitHub Models ನಿಂದ Azure OpenAI ಗೆ ಈ ಕೋಡ್‌ನಲ್ಲಿ ನಾನು ಹೇಗೆ ಬದಲಾಯಿಸಬಹುದು?"
> - "OpenAiOfficialChatModel.builder() ನಲ್ಲಿ ಇನ್ನೇನು ನಿಯಮಗಳನ್ನು ನಾನು ಸಂರಚಿಸಬಹುದು?"
> - "ಪೂರ್ಣ ಪ್ರತಿಕ್ರಿಯೆ ಬರುವೆಯೆಂದು ಕಾಯದೆ ಸ್ಟ್ರೀಮಿಂಗ್ ಪ್ರತಿಕ್ರಿಯೆಗಳನ್ನು ನಾನು ಹೇಗೆ ಸೇರಿಸಬಹುದು?"

**ಪ್ರಾಂಪ್ಟ್ ಇಂಜಿನಿಯರಿಂಗ್** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

ನೀವು ಈಗ ಮಾದರಿಯನ್ನು ಹೇಗೆ ಮಾತನಾಡಿಸುವುದು ತಿಳಿದಿದ್ದಾರೆ, ಅದಕ್ಕೆ ನೀವು ಏನು ಹೇಳುತ್ತೀರಿ ಎಂಬುದನ್ನು ಅನ್ವೇಷಿಸೋಣ. ಈ ಡೆಮೊ ಅದೇ ಮಾದರಿ ರಚನೆಯನ್ನು ಉಪಯೋಗಿಸಿ ಐದು ವಿಭಿನ್ನ ಪ್ರಾಂಪ್ಟ್ ವಿನ್ಯಾಸಗಳನ್ನು ತೋರಿಸುತ್ತದೆ. ನೇರ ನಿರ್ದೇಶನಗಳಿಗಾಗಿ ಶೂನ್ಯ-ಶಾಟ್ ಪ್ರಾಂಪ್ಟ್‌ಗಳು, ಉದಾಹರಣೆಗಳಿಂದ ಕಲಿಯುವ ಪಿಗ್ಗೆ-ಶಾಟ್, ಚಿಂತನೆ ಸರಣಿಯನ್ನು ಹಂಚುವ ಚೈನ್-ಆಫ್-ಥಾಟ್, ಮತ್ತು ಸನ್ನಿವೇಶವನ್ನು ಸೆಟ್ ಮಾಡುವ ಪಾತ್ರಾಧಾರಿತ ಪ್ರಾಂಪ್ಟ್ ಗಳನ್ನು ಪ್ರಯತ್ನಿಸಿ. ನೀವು ಭಿನ್ನವಾಗಿ ವಿನ್ಯಾಸಗೊಳಿಸಿದ ಪ್ರಾಂಪ್ಟ್ ಆಧಾರಿತ ಪರಿಣಾಮಗಳನ್ನು ನೋಡುತ್ತೀರಿ.

ಡೆಮೊ ಪ್ರಾಂಪ್ಟ್ ಟೆಂಪ್ಲೆಟ್‌ಗಳೂ ತೋರಿಸುತ್ತದೆ, ಅವು ಮರುಬಳಕೆ ಮಾಡಬಹುದಾದ ಪ್ರಾಂಪ್ಟ್‌ಗಳನ್ನು ವೇರಿಯಬಲ್ಗಳೊಂದಿಗೆ ರಚಿಸುವ ಶಕ್ತಿಶಾಲಿ ವಿಧಾನವಾಗಿದೆ.
ಕೆಳಗಿನ ಉದಾಹರಣೆ LangChain4j `PromptTemplate` ಬಳಸಿಕೊಂಡು ವೇರಿಯಬಲ್ಗಳನ್ನು ತುಂಬುವ ಪ್ರಾಂಪ್ಟ್ ಅನ್ನು ತೋರಿಸುತ್ತದೆ. AI ಕೊಟ್ಟಿರುವ ಗಮ್ಯಸ್ಥಾನ ಮತ್ತು ಚಟುವಟಿಕೆಯ ಆಧಾರದ ಮೇಲೆ ಉತ್ತರಿಸುತ್ತದೆ.

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

> **🤖 GitHub Copilot ಮೂಲಕ ಪ್ರಯತ್ನಿಸಿ:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "ಶೂನ್ಯ-ಶಾಟ್ ಮತ್ತು ಕೆಲವು-ಶಾಟ್ ಪ್ರಾಂಪ್ಟ್ ಮಧ್ಯೆ ವ್ಯತ್ಯಾಸ ಏನು ಮತ್ತು ಯಾವಾಗ ಯಾವದನ್ನು ಬಳಸಬೇಕು?"
> - "ತಾಪಮಾನ ನಿಯಮವು ಮಾದರಿಯ ಪ್ರತಿಕ್ರಿಯೆಗಳನ್ನು ಹೇಗೆ ಪ್ರಭಾವಿಸುತ್ತದೆ?"
> - "ಉತ್ಪಾದನೆಯಲ್ಲಿ ಪ್ರಾಂಪ್ಟ್ ಇಂಜೆಕ್ಷನ್ ದಾಳಿ ತಡೆಯಲು ಯಾವುದೇ ತಂತ್ರಗಳನ್ನು ನಾನು ಬಳಸಬಹುದು?"
> - "ಸಾಮಾನ್ಯ ವಿನ್ಯಾಸಗಳಿಗಾಗಿ ಮರುಬಳಕೆ ಮಾಡಬಹುದಾದ PromptTemplate ವಸ್ತುಗಳನ್ನು ಹೇಗೆ ಸೃಷ್ಟಿಸಬಹುದು?"

**ಸಾಧನ ಸಂಯೋಜನೆ** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ಇಲ್ಲಿ LangChain4j ಶಕ್ತಿಶಾಲಿಯಾಗುತ್ತದೆ. ನೀವು `AiServices` ಬಳಸಿ ಒಂದು AI ಸಹಾಯಕೆಯನ್ನು ಬರೆದು ನಿಮ್ಮ ಜಾವಾ ವಿಧಾನಗಳನ್ನು ಕರೆಯಬಹುದು. ವಿಧಾನಗಳನ್ನು `@Tool("ವಿವರಣೆ")` ಜೊತೆಗೆ ಅನೋಟೇಟ್ ಮಾಡಿ, ಲ್ಯಾಂಗ್‌ಚೈನ್4j ಇತರ ಎಲ್ಲ ನಿರ್ವಹಣೆ ಮಾಡುತ್ತದೆ - AI ಬಳಕೆದಾರ ಕೇಳುವ ಪ್ರಕಾರ ಯಾವ ಸಾಧನೆಯನ್ನು ಬಳಕೆ ಮಾಡಬೇಕೆಂದು ಸ್ವಯಂಚಾಲಿತವಾಗಿ ನಿರ್ಧರಿಸುತ್ತದೆ. ಇದು ಫಂಕ್ಷನ್ ಕರೆಮಾಡುವಿಕೆಯ ಮಹತ್ವದ ತಂತ್ರವನ್ನು ತೋರಿಸುತ್ತದೆ, ಅದು AI ಯನ್ನು ಜವಾಬ್ದಾರಿಗಳೊಂದಿಗೆ ಕ್ರಮಗಳನ್ನು ಕೈಗೊಳ್ಳಲು ಸಾಧ್ಯವಾಗಿಸುತ್ತದೆ.

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

> **🤖 GitHub Copilot ಮೂಲಕ ಪ್ರಯತ್ನಿಸಿ:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "@Tool ಅನೋಟೇಷನ್ ಹೇಗೆ ಕೆಲಸ ಮಾಡುತ್ತದೆ ಮತ್ತು ಲ್ಯಾಂಗ್‌ಚೈನ್4j ಅದನ್ನು ಹಿಂದೆ ಹೇಗೆ ನಿರ್ವಹಿಸುತ್ತದೆ?"
> - "ಸಂಯೋಜಿತ ಸಮಸ್ಯೆಗಳನ್ನು ಪರಿಹರಿಸಲು AI ಅನೇಕ ಸಾಧನಗಳನ್ನು क्रमವಾಗಿ ಕರೆ ಮಾಡಬಹುದೇ?"
> - "ಒಂದು ಸಾಧನದಲ್ಲಿ ತಪ್ಪು ಉಂಟಾದರೆ ನಾನು ಹೇಗೆ ದೋಷಗಳನ್ನು ಹ್ಯಾಂಡಲ್ ಮಾಡಬೇಕು?"
> - "ಈ ಕ್ಯಾಲ್ಕ್ಯುಲೇಟರ್ ಉದಾಹರಣೆಯ ಬದಲು ನಿಜವಾದ API ಅನ್ನು ನಾನು ಹೇಗೆ ಸಂಯೋಜಿಸಬಹುದು?"

**ಡಾಕ್ಯುಮೆಂಟ್ ಪ್ರಶ್ನೋತ್ತರ (ಸುಲಭ RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ಇಲ್ಲಿ ನೀವು LangChain4j ನ "ಸುಲಭ RAG" ವಿಧಾನದಿಂದ RAG (ರಿಟ್ರಿವಲ್-ಆಗ್ಮೆಂಟೆಡ್ ಜನರೇಶನ್) ನೋಡುತ್ತೀರಿ. ಡಾಕ್ಯುಮೆಂಟ್ಗಳು ಲೋಡ್ ಆಗಿ, ಸ್ವಯಂಚಾಲಿತವಾಗಿ ವಿಭಜನೆ ಮಾಡಲ್ಪಡಿ, ಮೆಮರಿ ಆಧಾರಿತ ಸ್ಟೋರ್‌ಗೆ ಎम्बೆಡ್ ಆಗುತ್ತವೆ, ನಂತರ ವಿಷಯ ಪೂರೈಸುವ ಯಂತ್ರವು AI ಗೆ ಸಂಬಂಧಿಸಿದ ಭಾಗಗಳನ್ನು ಪ್ರಶ್ನೆ ಸಮಯದಲ್ಲಿ ಒದಗಿಸುತ್ತದೆ. AI ನಿಮ್ಮ ಡಾಕ್ಯುಮೆಂಟ್‌ಗಳ ಆಧಾರದ ಮೇಲೆ ಉತ್ತರಿಸುತ್ತದೆ, ಅದರ ಸಾಮಾನ್ಯ ಜ್ಞಾನ ಅಲ್ಲ.

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

> **🤖 GitHub Copilot ಮೂಲಕ ಪ್ರಯತ್ನಿಸಿ:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "RAG AI ಹಲ್ಲಿನ ನಿಯಂತ್ರಣವನ್ನು ಮಾದರಿಯ ತರಬೇತಿ ದತ್ತಾಂಶದ ಬಳಕೆಯಿಂದ ಹೇಗೆ ತಡೆಯುತ್ತದೆ?"
> - "ಈ ಸುಲಭ ವಿಧಾನ ಮತ್ತು ಕಸ್ಟಮ್ RAG ಪೈಪ್‌ಲೈನ್ ಮಧ್ಯೆ ವ್ಯತ್ಯಾಸವೇನು?"
> - "ಹಲವಾರು ಡಾಕ್ಯುಮೆಂಟ್‌ಗಳು ಅಥವಾ ದೊಡ್ಡ ಜ್ಞಾನ ಆಧಾರಗಳನ್ನು ನಾನು ಹೇಗೆ ತಡೆತಿರುಗಿಸುವೆ?"

**ಜವಾಬ್ದಾರಿಯುತ AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ಆಳವಾದ ರಕ್ಷಣೆಯೊಂದಿಗೆ AI ಸುರಕ್ಷತೆ ನಿರ್ಮಿಸಿ. ಈ ಡೆಮೊ ಎರಡು ಅವರಣೆಗಳನ್ನು ಒಟ್ಟಿಗೆ ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತವೆ:

**ಭಾಗ 1: LangChain4j ಇನ್‌ಪುಟ್ ಗಾರ್ಡ್‌ರೈಲುಗಳು** - ಅಪಾಯಕಾರಿ ಪ್ರಾಂಪ್ಟ್‌ಗಳನ್ನು LLM ಗೆ ತಲುಪುವ ಮೊದಲು ತಡೆಯುತ್ತವೆ. ನಿಷಿದ್ಧಕೀಲಿಕೋಶಗಳು ಅಥವಾ ವಿನ್ಯಾಸಗಳಿಗೆ ತಕ್ಕಂತೆ ಕಸ್ಟಮ್ ಗಾರ್ಡ್‌ರೈಲುಗಳನ್ನು ರಚಿಸಿ. ಇವು ನಿಮ್ಮ ಕೋಡಿನಲ್ಲಿ ಕಾರ್ಯನಿರ್ವಹಿಸುವುದರಿಂದ ವೇಗವಾಗಿ ಮತ್ತು ಉಚಿತ.

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

**ಭಾಗ 2: ಪ್ರೊವೈಡರ್ ಸುರಕ್ಷತಾ ಫಿಲ್ಟರ್‌ಗಳು** - GitHub Models ನಲ್ಲಿರುವ ಸ್ಥಿರ ಫಿಲ್ಟರ್‌ಗಳು ನಿಮ್ಮ ಗಾರ್ಡ್‌ರೈಲುಗಳು ತಪ್ಪುವುದು ಹಿಡಿಯುತ್ತವೆ. ಗಂಭೀರ ಉಲ್ಲಂಘನೆಗಳಿಗೆ ಹಾರ್ಡ್ ಬ್ಲಾಕ್‌ಗಳು (HTTP 400 ದೋಷಗಳು) ಮತ್ತು ಸಾಫ್ಟ್ ನಿರಾಕರಣೆಗಳಿವೆ, ಅಂದರೆ AI ವಿನಮ್ರವಾಗಿ ತಿರಸ್ಕರಿಸುತ್ತದೆ.

> **🤖 GitHub Copilot ಮೂಲಕ ಪ್ರಯತ್ನಿಸಿ:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "InputGuardrail ಅಂದರೇನು ಮತ್ತು ನಾನು ನನ್ನದೇನನ್ನು ಹೇಗೆ ರಚಿಸಬಹುದು?"
> - "ಹಾರ್ಡ್ ಬ್ಲಾಕ್ ಮತ್ತು ಸಾಫ್ಟ್ ನಿರಾಕರಣೆಯ ವ್ಯತ್ಯಾಸವೇನು?"
> - "ಗೂಡಾಗಿ ಮತ್ತು ಪ್ರೊವೈಡರ್ ಫಿಲ್ಟರ್‌ಗಳನ್ನು ಒಟ್ಟಿಗೆ ಬಳಸудың ಪ್ರಯೋಜನವೇನು?"

## ಮುಂದಿನ ಹಂತಗಳು

**ಮುಂದಿನ ಕಾರ್ಯಾಗಾರ:** [01-ಪರಿಚಯ - LangChain4j ಜೊತೆ ಆರಂಭಿಸುವುದು](../01-introduction/README.md)

---

**ನವಿಗೆಶನ್:** [← ಮುಖ್ಯಕ್ಕೆ ಹಿಂದಿರುಗಿ](../README.md) | [ಮುಂದಿನ: ಕಾರ್ಯಾಗಾರ 01 - ಪರಿಚಯ →](../01-introduction/README.md)

---

## ತೊಂದರೆ ಪರಿಹಾರ

### ಮೊದಲ ಬಾರಿಗೆ ಮೆವನ್ ಬಿಲ್ಡ್

**ಸಮಸ್ಯೆ:** ಆರಂಭಿಕ `mvn clean compile` ಅಥವಾ `mvn package` ಬಹಳ ಸಮಯ ತೆಗೆದುಕೊಳ್ಳುತ್ತದೆ (10-15 ನಿಮಿಷಗಳು)

**ಕಾರಣ:** ಮೆವನ್ ಮೊದಲ ಬಾರಿಗೆ ಎಲ್ಲಾ ಪ್ರಾಜೆಕ್ಟ್ ಅವಲಂಬನೆಗಳನ್ನು (Spring Boot, LangChain4j ಲೈಬ್ರರಿಗಳು, Azure SDK ಗಳು ಇತ್ಯಾದಿ) ಡೌನ್‌ಲೋಡ್ ಮಾಡಬೇಕಾಗಿದೆ.

**ಉಪಾಯ:** ಇದು ಸಾಮಾನ್ಯ ವರ್ತನೆ. ಮುಂದಿನ ಬಿಲ್ಡ್‌ಗಳು ಸ್ಥಳೀಯವಾಗಿ ಕ್ಯಾಶ್ ಆಗಿರುವುದರಿಂದ ವೇಗವಾಗಿ ನಡೆಯುತ್ತವೆ. ಡೌನ್‌ಲೋಡ್ ಸಮಯ ನಿಮ್ಮ ನೆಟ್‌ವರ್ಕ್ ವೇಗದ ಮೇಲೆ ಆಧಾರಿತವಾಗಿರುತ್ತದೆ.

### ಪವರ್‌ಶೆಲ್ ಮೆವನ್ ಕಮಾಂಡ್ ವ್ಯಾಕರಣ

**ಸಮಸ್ಯೆ:** ಮೆವನ್ ಕಮಾಂಡ್‌ಗಳು `Unknown lifecycle phase ".mainClass=..."` ದೋಷದೊಂದಿಗೆ ವಿಫಲವಾಗುತ್ತವೆ
**ಕಾರಣ**: PowerShell `=` ಅನ್ನು ವೈಯರಿಯಬಲ್ ನಿಯೋಜನೆ ಅಪರೇಟರ್ ಎಂದು ಅರ್ಥಮಾಡಿಕೊಳ್ಳುತ್ತದೆ, ಇದು Maven ಪ್ರಾಪರ್ಟಿ ಸಿಂಟ್ಯಾಕ್ಸ್ ಅನ್ನು ಮುರಿಯುತ್ತದೆ

**ಪರಿಹಾರ**: Maven ಆಜ್ಞೆಯ ಮೊದಲು ಸ್ಟಾಪ್-पಾರ್ಸಿಂಗ್ ಅಪರೇಟರ್ `--%` ಬಳಸಿ:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ಅಪರೇಟರ್ PowerShell ಗೆ ಉಳಿದ ಎಲ್ಲಾ ಆರ್ಗ್ಯುಮೆಂಟ್ಸ್ ಅನ್ನು Maven ಗೆ ಅರ್ಥಮಾಡಿಕೊಳ್ಳದೆ ನೇರವಾಗಿ ಪಾಸ್ ಮಾಡುವಂತೆ ಹೇಳುತ್ತದೆ.

### Windows PowerShell Emoji ಪ್ರದರ್ಶನ

**ಸಮಸ್ಯೆ**: PowerShell ನಲ್ಲಿ ಇಮೋಜಿಗಳ ಬದಲು AI ಪ್ರತಿಕ್ರಿಯೆಗಳು ತ್ಯಾಜ್ಯ ಅಕ್ಷರಗಳು (ಉದಾ. `????` ಅಥವಾ `â??`) ತೋರಿಸುತ್ತವೆ

**ಕಾರಣ**: PowerShell ಡಿಫಾಲ್ಟ್ ಎನ್‌ಕೊಡಿಂಗ್ UTF-8 ಇಮೋಜೀವನ್ನು ಬೆಂಬಲಿಸುವುದಿಲ್ಲ

**ಪರಿಹಾರ**: ಜಾವಾ ಅಪ್ಲಿಕೆಷನ್‌ಗಳನ್ನು ನಡೆಸುವ ಮೊದಲು ಈ ಆಜ್ಞಾನವನ್ನು ಓಡಿ:
```cmd
chcp 65001
```

ಇದು ಟರ್ಮಿನಲ್ ನಲ್ಲಿ UTF-8 ಎನ್‌ಕೊಡಿಂಗ್ ಅನ್ನು ಬಲಪಡಿಸುತ್ತದೆ. ಪರ್ಯಾಯವಾಗಿ, ಉತ್ತಮ ಯುನಿಕೋಡ್ ಬೆಂಬಲವುಳ್ಳ Windows Terminal ಅನ್ನು ಬಳಸಿ.

### API ಕರೆಗಳ ಡಿಬಗ್ಗಿಂಗ್

**ಸಮಸ್ಯೆ**: ಪ್ರವೇಶ ಪರಿಶೀಲನೆ ದೋಷಗಳು, ದರ ಮಿತಿಗಳು ಅಥವಾ AI ಮಾದರಿಯಿಂದ ಅಪ್ರತೀಕ್ಷಿತ ಪ್ರತಿಕ್ರಿಯೆಗಳು

**ಪರಿಹಾರ**: ಉದಾಹರಣೆಗಳಲ್ಲಿ `.logRequests(true)` ಮತ್ತು `.logResponses(true)` ಸೇರಿವೆ, ಇದು API ಕರೆಗಳನ್ನು ಕನ್ಸೋಲ್ ನಲ್ಲಿ ತೋರಿಸುತ್ತದೆ. ಇದು ಪ್ರವೇಶ ಪರಿಶೀಲನೆ ದೋಷಗಳು, ದರ ಮಿತಿಗಳು ಅಥವಾ ಅಪ್ರತೀಕ್ಷಿತ ಪ್ರತಿಕ್ರಿಯೆಗಳನ್ನು ಪರಿಹರಿಸಲು ಸಹಾಯ ಮಾಡುತ್ತದೆ. ಲಾಗ್ ಧ್ವನಿಹರಣ ಕಡಿಮೆ ಮಾಡಲು ಉತ್ಪಾದನೆಯಲ್ಲಿ ಈ ಫ್ಲ್ಯಾಗ್‌ಗಳನ್ನು ತೆಗೆದುಹಾಕಿ.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ಅస್ಪಷ್ಟಿಕೆ**:
ಈ ದಾಖಲೆ [Co-op Translator](https://github.com/Azure/co-op-translator) ಎಂಬ AI ಅನುವಾದ ಸೇವೆಯನ್ನು ಬಳಸಿ ಅನುವದಿಸಲಾಗಿದೆ. ನಾವು ಶುದ್ಧತೆಗೆ ಪ್ರಯತ್ನಿಸುತ್ತಿದ್ದರೂ, ಸ್ವಯಂಚಾಲಿತ ಅನುವಾದಗಳಲ್ಲಿ ದೋಷಗಳು ಅಥವಾ ನಿಖರತೆ ಕೊರತೆಗಳು ಇರಬಹುದು ಎಂದು ದಯವಿಟ್ಟು ಗಮನಿಸಿ. ಮೂಲ ಭಾಷೆಯ ದಾಖಲೆ ಅನಧಿಕೃತ ಮೂಲವೆಂದು ಪರಿಗಣಿಸಬೇಕು. ಪ್ರಮುಖ ಮಾಹಿತಿಗಾಗಿ, ವೃತ್ತಿಪರ ಮಾನವ ಅನುವಾದವನ್ನು ಶಿಫಾರಸು ಮಾಡಲಾಗುತ್ತದೆ. ಈ ಅನುವಾದದ ಬಳಕೆಯಿಂದ ಉಂಟಾಗುವ ಯಾವುದೇ ತಪ್ಪು ಅರ್ಥಮಾಡಿಕೆ ಅಥವಾ ತುಂಡು ಅರ್ಥಗಳಿಗಾಗಿ ನಾವು ಜವಾಬ್ದಾರಿಯಲ್ಲ.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->