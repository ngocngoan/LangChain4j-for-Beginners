# Module 00: ತ್ವರಿತ ಪ್ರಾರಂಭ

## ವಿಷಯಪಟ್ಟಿ

- [ಪರಿಚಯ](../../../00-quick-start)
- [LangChain4j ಏನು?](../../../00-quick-start)
- [LangChain4j ಅವಲಂಬನೆಗಳು](../../../00-quick-start)
- [ಮುಂಭಾಗದ ಅಗತ್ಯಸ್ಥಿತಿಗಳು](../../../00-quick-start)
- [ಸೆಟಪ್](../../../00-quick-start)
  - [1. ನಿಮ್ಮ GitHub ಟೋಕನ್ ಪಡೆಯಿರಿ](../../../00-quick-start)
  - [2. ನಿಮ್ಮ ಟೋಕನ್ ಸೆಟ್ ಮಾಡಿ](../../../00-quick-start)
- [ಉದಾಹರಣೆಗಳನ್ನು ರನ್ ಮಾಡಿ](../../../00-quick-start)
  - [1. ಮೂಲ ಚಾಟ್](../../../00-quick-start)
  - [2. ಪ್ರಾಂಪ್ಟ್ ಮಾದರಿಗಳು](../../../00-quick-start)
  - [3. ಫಂಕ್ಷನ್ ಕಾಲ್ಲಿಂಗ್](../../../00-quick-start)
  - [4. ಡಾಕ್ಯುಮೆಂಟ್ ಪ್ರಶ್ನೋತ್ತರ (RAG)](../../../00-quick-start)
  - [5. ಜವಾಬ್ದಾರಿಯುಕ್ತ AI](../../../00-quick-start)
- [ಪ್ರತಿ ಉದಾಹರಣೆ ಏನನ್ನು ತೋರಿಸುತ್ತದೆ](../../../00-quick-start)
- [ಮುಂದಿನ ಹಂತಗಳು](../../../00-quick-start)
- [ತೊಂದರೆ ಪರಿಹಾರ](../../../00-quick-start)

## ಪರಿಚಯ

ಈ ತ್ವರಿತ ಪ್ರಾರಂಭವು LangChain4j ಯೊಂದಿಗೆ ಸಾಧ್ಯವಾದಷ್ಟು ಶೀಘ್ರವಾಗಿ ಪ್ರಾರಂಭಿಸಲು ನಿಮಗೆ ಸಹಾಯ ಮಾಡಲು ಉದ್ದೇಶಿಸಲಾಗಿದೆ. ಇದು LangChain4j ಮತ್ತು GitHub ಮಾದರಿಗಳೊಂದಿಗೆ AI ಅನ್ವಯಿಕೆಗಳನ್ನು ನಿರ್ಮಿಸುವ ಅತಿಥಿಗಳ ಮೂಲಭೂತಾಂಶಗಳನ್ನು ಒಳಗೊಂಡಿದೆ. ಮುಂದಿನ ಘಟಕಗಳಲ್ಲಿ ನೀವು LangChain4j ಸಹ Azure OpenAI ಅನ್ನು ಬಳಸಿ ಹೆಚ್ಚು ಪ್ರगत ಅನ್ವಯಿಕೆಗಳನ್ನು ನಿರ್ಮಿಸುವೀರಿ.

## LangChain4j ಏನು?

LangChain4j ಒಂದು ಜಾವಾ ಗ್ರಂಥಾಲಯವಾಗಿದೆ ಅದು AI-ಚಾಲಿತ ಅನ್ವಯಿಕೆಗಳನ್ನು ಸರಳಗೊಳಿಸುತ್ತದೆ. HTTP ಕ್ಲೈಂಟ್‌ಗಳು ಮತ್ತು JSON ಪಾರ್ಸಿಂಗ್ ನೊಂದಿಗೆ ಡೀಲಿಂಗ್ ಮಾಡುವ ಬದಲು ನೀವು ಸ್ವಚ್ಚ ಜಾವಾ API ಗಳೊಂದಿಗೆ ಕೆಲಸ ಮಾಡುತ್ತೀರಿ.

LangChain ನಲ್ಲಿ "ಚೈನ್" ಅಂದರೆ ಹಲವು ಘಟಕಗಳನ್ನು ಸರಣಿಬದ್ಧಗೊಳಿಸುವುದು — ನೀವು ಒಂದು ಪ್ರಾಂಪ್ಟ್ ಅನ್ನು ಮಾದರಿಯೊಂದಕ್ಕೆ, ನಂತರ ಪಾರ್ಸರ್ ಗೆ ಕಳಿಸಬಹುದು ಅಥವಾ ಹಲವು AI ಕರೆಗೆ ಸರಣಿಬದ್ಧವಾಗಿ ಕರೆಯಬಹುದು, ಇಲ್ಲಿ ಒಬ್ಬರ output ಮುಂದಿನ input ಗೆ ಪೂರೈಸುತ್ತದೆ. ಈ ತ್ವರಿತ ಪ್ರಾರಂಭವು ಇನ್ನಷ್ಟು ಸಂಕೀರ್ಣ ಸರಣಿಗಳನ್ನು ಅನ್ವೇಷಿಸುವ ಮೊದಲು ಮೂಲ ತತ್ವಗಳ ಮೇಲೆ ಕೇಂದ್ರೀಕೃತವಾಗಿದೆ.

<img src="../../../translated_images/kn/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j ನಲ್ಲಿ ಘಟಕಗಳನ್ನು ಜೋಡಿಸುವುದು - ಶಕ್ತಿಶಾಲಿ AI ವರ್ಕ್‌ಫ್ಲೋಗಳನ್ನು ನಿರ್ಮಿಸಲು ಬ್ಲಾಕುಗಳು ಸಂಪರ್ಕ ಹೊಂದಿವೆ*

ನಾವು ಮೂರು ಪ್ರಮುಖ ಘಟಕಗಳನ್ನು ಬಳಸುತ್ತೇವೆ:

**ChatLanguageModel** – AI ಮಾದರಿ ಸಂವಹನಗಳಿಗಾಗಿ ಇಂಟರ್ಫೇಸ್. `model.chat("prompt")` ಅನ್ನು ಕರೆಸಿ ಪ್ರತಿಕ್ರಿಯೆ ಸ್ಟ್ರಿಂಗ್ ಪಡೆಯಿರಿ. ನಾವು `OpenAiOfficialChatModel` ಅನ್ನು ಬಳಸುತ್ತೇವೆ, ಇದು GitHub Models ರಂತಹ OpenAI-ಒಂಬತ್ತು ಅಂತಿಮ ಬಿಂದುಗಳಿಗೆ ಕೆಲಸ ಮಾಡುತ್ತದೆ.

**AiServices** – ಪ್ರಕಾರ-ಭದ್ರ AI ಸೇವೆ ಇಂಟರ್ಫೇಸ್‌ಗಳನ್ನು ರಚಿಸುತ್ತದೆ. მეთೋಡ್‌ಗಳನ್ನು ವ್ಯಾಖ್ಯಾನಿಸಿ, ಅವುಗಳಿಗೆ `@Tool` ಅಡ್ಡಚಿಹ್ನೆ ನೀಡಿರಿ, LangChain4j ವ್ಯವಸ್ಥಾಪನೆಯನ್ನು ನಿರ್ವಹಿಸುತ್ತದೆ. AI ಅಗತ್ಯವಿದ್ದಾಗ ನಿಮ್ಮ Java ಮೆತೋಡ್ಗಳನ್ನು ಸ್ವಯಂಚಾಲಿತವಾಗಿ ಕರೆಯುತ್ತದೆ.

**MessageWindowChatMemory** – ಸಂಭಾಷಣೆಯ ಇತಿಹಾಸವನ್ನು ಕಾಯ್ದುಕೊಳ್ಳುತ್ತದೆ. ಇದಿಲ್ಲದೆ ಪ್ರತಿಯೊಂದು ವಿನಂತಿಯೂ ಸ್ವತಂತ್ರವಾಗಿರುತ್ತದೆ. ಇದನ್ನು ಬಳಸಿ AI ಹಿಂದಿನ ಸಂದೇಶಗಳನ್ನು ನೆನೆಸಿಕೊಳ್ಳುತ್ತದೆ ಮತ್ತು ಹಲವು ವ sapiಲಾಪುಗಳ ನಡುವೆ ಸಾಂದರ್ಭಿಕತೆಯನ್ನು ಕಾಯ್ದುಕೊಳ್ಳುತ್ತದೆ.

<img src="../../../translated_images/kn/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j ವಾಸ್ತುಶಿಲ್ಪ - ಮುಖ್ಯ ಘಟಕಗಳು ನಿಮ್ಮ AI ಅನ್ವಯಿಕೆಗಳಿಗೆ ಶಕ್ತಿ ನೀಡಲು ಒಟ್ಟಿಗೆ ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತವೆ*

## LangChain4j ಅವಲಂಬನೆಗಳು

ಈ ತ್ವರಿತ ಪ್ರಾರಂಭವು [`pom.xml`](../../../00-quick-start/pom.xml) ನಲ್ಲಿ ಎರಡು Maven ಅವಲಂಬನೆಗಳನ್ನು ಬಳಸುತ್ತದೆ:

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

`langchain4j-open-ai-official` מוד್ಯೂಲ್ `OpenAiOfficialChatModel` ವರ್ಗವನ್ನು ಒದಗಿಸುತ್ತದೆ, ಇದು OpenAI-ಸಮ್ಮತಿಯ ಉಡಾಣಗಳಿಗೆ ಸಂಪರ್ಕಿಸುತ್ತದೆ. GitHub Models ಕೂಡ ಇದೇ API ಸ್ವರೂಪವನ್ನು ಬಳಸುತ್ತದೆ, ಆದ್ದರಿಂದ ವಿಶೇಷ ಅಡುಪೇಟರ್ ಅವಶ್ಯಕತೆ ಇಲ್ಲ – ಕೇವಲ ಮೂಲ URL ಅನ್ನು `https://models.github.ai/inference` ಗೆ ಸೂಚಿಸಿ.

## ಮುಂಭಾಗದ ಅಗತ್ಯಸ್ಥಿತಿಗಳು

**Dev Container ಬಳಸುತ್ತಿದ್ದೀರಾ?** Java ಮತ್ತು Maven ಇನ್‌ಸ್ಟಾಲ್ ಆಗಿವೆ. ನಿಮಗೆ ಕೇವಲ GitHub ವೈಯಕ್ತಿಕ ಪ್ರವೇಶ ಟೋಕನ್ ಅಗತ್ಯವಿದೆ.

**ಸ್ಥಳೀಯ ಅಭಿವೃದ್ಧಿ:**
- Java 21+, Maven 3.9+
- GitHub ವೈಯಕ್ತಿಕ ಪ್ರವೇಶ ಟೋಕನ್ (ಕೆಳಗಿನ ಸೂಚನೆ)

> **ಗಮನಿಸಿ:** ಈ ಘಟಕವು GitHub Models ನಿಂದ `gpt-4.1-nano` ಅನ್ನು ಬಳಸುತ್ತದೆ. ಕೋಡ್ ನಲ್ಲಿ ಮಾದರಿಯ ಹೆಸರು ಬದಲಿಸಬೇಡಿ - ಇದು GitHub ಲಭ್ಯವಿರುವ ಮಾದರಿಗಳೊಂದಿಗೆ ಕೆಲಸ ಮಾಡಲು ಸಂರಚಿತವಾಗಿದೆ.

## ಸೆಟಪ್

### 1. ನಿಮ್ಮ GitHub ಟೋಕನ್ ಪಡೆಯಿರಿ

1. [GitHub ಸೆಟ್ಟಿಂಗ್ಸ್ → ವೈಯಕ್ತিক ಪ್ರವೇಶ ಟೋಕನ್ಗಳು](https://github.com/settings/personal-access-tokens) ಗೆ ಹೋಗಿ
2. "Generate new token" ಕ್ಲಿಕ್ ಮಾಡಿ
3. ವಿವರಣಾತ್ಮಕ ಹೆಸರನ್ನು ಸೆಟ್ ಮಾಡಿ (ಉದಾ: "LangChain4j ಡೆಮೋ")
4. ಅವಧಿ ಹೊಂದಿಸಿ (7 ದಿನ ಶಿಫಾರಸು ಮಾಡಲಾಗಿದೆ)
5. "ಖಾತೆ ಅನುಮತಿಗಳು" ಅಡಿ "Models" ಅನ್ನು "Read-only" ಗೆ ಸೆಟ್ ಮಾಡಿ
6. "Generate token" ಕ್ಲಿಕ್ ಮಾಡಿ
7. ನಿಮ್ಮ ಟೋಕನ್ ಅನ್ನು ನಕಲಿ ಮಾಡಿ ಮತ್ತು ಉಳಿಸಿ – ಮರುಬಾರಿಯೂ ದರ್ಶನವಾಗುವುದಿಲ್ಲ

### 2. ನಿಮ್ಮ ಟೋಕನ್ ಅನ್ನು ಸೆಟ್ ಮಾಡಿ

**ಆಯ್ಕೆ 1: VS Code ಬಳಸು (ಶಿಫಾರಸು ಮಾಡಲಾಗಿದೆ)**

ನೀವು VS Code ಬಳಸದಿದ್ದರೆ, ಪ್ರಾಜೆಕ್ಟ್ ರೂಟಿನಲ್ಲಿ `.env` ಫೈಲ್ ನಲ್ಲಿ ನಿಮ್ಮ ಟೋಕನ್ ಅನ್ನು ಸೇರಿಸಿ:

`.env` ಫೈಲ್ ಇಲ್ಲದಿದ್ದರೆ, `.env.example` ಅನ್ನು `.env` ಗೆ ನಕಲಿಸಿ ಅಥವಾ ಹೊಸ `.env` ಫೈಲ್ ರಚಿಸಿ.

**ಉದಾಹರಣೆಯ `.env` ಫೈಲ್:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env ನಲ್ಲಿ
GITHUB_TOKEN=your_token_here
```

ನಂತರ ನೀವು ಎಕ್ಸ್‌ಪ್ಲೋರರ್ ನಲ್ಲಿ ಯಾವುದೇ ಡೆಮೋ ಫೈಲ್ ಮೇಲ್ಕೆಲಸಮಾಡಿ (ಉದಾ: `BasicChatDemo.java`) ಮತ್ತು **"Run Java"** ಆಯ್ಕೆಯನ್ನು ಆರಿಸಬಹುದು ಅಥವಾ ರನ್ ಹಾಗೂ ಡಿಬಗ್ಗಿಂಗ್ ಪ್ಯಾನೆಲ್‌ನಿಂದ ಲಾಂಚ್ ಕಾನ್ಫಿಗರೇಷನ್ಗಳನ್ನು ಬಳಸಬಹುದು.

**ಆಯ್ಕೆ 2: ಟರ್ಮಿನಲ್ ಬಳಸು**

ಟೋಕನ್ ಅನ್ನು ಪರಿಸರ ಚರ হিসেবে ಸೆಟ್ ಮಾಡಿ:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## ಉದಾಹರಣೆಗಳನ್ನು ರನ್ ಮಾಡಿ

**VS Code ಬಳಸಿ:** ಯಾವುದೇ ಡೆಮೋ ಫೈಲ್ ಮೇಲೆ ಎಕ್ಸ್‌ಪ್ಲೋರರ್ ನಲ್ಲಿ ಕೀಳು-ಕ್ಲಿಕ್ ಮಾಡಿ ಮತ್ತು **"Run Java"** ಆಯ್ಕೆಮಾಡಿ ಅಥವಾ ರನ್ ಮತ್ತು ಡಿಬಗ್ ಪ್ಯಾನೆಲ್‌ನ ಲಾಂಚ್ ಕಾನ್ಫಿಗರೇಷನ್ಗಳನ್ನು ಬಳಸಿ (ಮೊದಲು ನೀವು ನಿಮ್ಮ ಟೋಕನ್ `.env` ಫೈಲ್ ಗೆ ಸೇರಿಸಿದ್ದೀರಾ ಎಂಬುದನ್ನು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ).

**Maven ಬಳಸಿ:** ಬದಲಿವಾಗಿ, ನೀವು ಕಮಾಂಡ್ ಲೈನ್ ನಿಂದ ರನ್ ಮಾಡಬಹುದು:

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

ಶೂನ್ಯ-ಶಾಟ್, ಕೆಲವು-ಶಾಟ್, ಚೈನ್-ಆಫ್-ಥಾಟ್ ಮತ್ತು ಪಾತ್ರಾಧಾರಿತ ಪ್ರಾಂಪ್ಟ್‌ಗಳನ್ನು ತೋರಿಸುತ್ತದೆ.

### 3. ಫಂಕ್ಷನ್ ಕಾಲ್ಲಿಂಗ್

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI ಅಗತ್ಯವಿದ್ದಾಗ ನಿಮ್ಮ Java ಮೆತೋಡ್‌ಗಳನ್ನು ಸ್ವಯಂಚಾಲಿತವಾಗಿ ಕರೆಸುತ್ತದೆ.

### 4. ಡಾಕ್ಯುಮೆಂಟ್ ಪ್ರಶ್ನೋತ್ತರ (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt` ನಲ್ಲಿ ഉള്ള ವಿಷಯದ ಬಗ್ಗೆ ಪ್ರಶ್ನೆಗಳು ಕೇಳಿ.

### 5. ಜವಾಬ್ದಾರಿಯುಕ್ತ AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI ಸುರಕ್ಷತಾ ಫಿಲ್ಟರ್‌ಗಳು ಹಾನಿಕರ ವಿಷಯಗಳನ್ನು ತಡೆಗಟ್ಟುವುದು ಹೇಗೆ ಎಂಬುದನ್ನು ನೋಡಿ.

## ಪ್ರತಿ ಉದಾಹರಣೆ ಏನನ್ನು ತೋರಿಸುತ್ತದೆ

**ಮೂಲ ಚಾಟ್** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j ನ ಮೂಲತತ್ವದಲ್ಲಿ ಇಲ್ಲಿ ಪ್ರಾರಂಭಿಸಿ. ನೀವು `OpenAiOfficialChatModel` ಅನ್ನು ರಚಿಸಿ, `.chat()` ಮೂಲಕ ಪ್ರಾಂಪ್ಟ್ ಕಳುಹಿಸಿ, ಪ್ರತಿಕ್ರಿಯೆ ಪಡೆದುಕೊಳ್ಳುತ್ತೀರಿ. ಇದು ಆಧಾರವನ್ನು ತೋರಿಸುತ್ತದೆ: ಕಸ್ಟಮ್ ಎಂಡ್‌ಪಾಯಿಂಟ್ ಮತ್ತು API ಕೀಲಿಗಳು ಬಳಸಿಕೊಂಡು ಮಾದರಿಗಳನ್ನು ಆರಂಭಿಸುವುದು ಹೇಗೆ. ಈ ಮಾದರಿಯನ್ನು ಅರ್ಥ ಮಾಡಿಕೊಂಡ ನಂತರ, ಇತರ ಎಲ್ಲವೂ ಇದರ ಮೇಲೆ ನಿರ್ಮಿಸಲಾಗುತ್ತದೆ.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ಚಾಟ್‍ನೊಂದಿಗೆ ಪ್ರಯತ್ನಿಸಿ:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "ನಾನು ಈ ಕೋಡ್ ನಲ್ಲಿ GitHub Models ನಿಂದ Azure OpenAI ಗೆ ಹೇಗೆ ಬದಲಾಯಿಸುತ್ತೇನೆ?"
> - "OpenAiOfficialChatModel.builder() ನಲ್ಲಿ ಇನ್ನೇನು ಪ್ಯಾರಾಮೀಟರ್ಗಳನ್ನು ಸೆಟ್ ಮಾಡಬಹುದು?"
> - "ಪೂರ್ಣ ಪ್ರತಿಕ್ರಿಯೆಗಾಗಿ ಕಾಯದೆ ಸ್ಟ್ರೀಮಿಂಗ್ ಪ್ರತಿಕ್ರಿಯೆಗಳನ್ನು ನಾನು ಹೇಗೆ ಸೇರಿಸಬಹುದು?"

**ಪ್ರಾಂಪ್ಟ್ ಎಂಜಿನಿಯರಿಂಗ್** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

ನೀವು ಈಗ ಮಾದರಿಯೊಂದಿಗೆ ಹೇಗೆ ಮಾತನಾಡುವುದು ತಿಳಿದಿದ್ದೀರಿ, ಈಗ ನೀವು ಅದಕ್ಕೆ ಏನನ್ನು ಹೇಳುವಿರಿ ಎನ್ನುವದು ಅನ್ವೇಷಣೆಯ ವಿಷಯ. ಈ ಡೆಮೊ ಅದೇ ಮಾದರಿ ಸೆಟಪ್ ಅನ್ನು ಬಳಸುತ್ತದೆ ಆದರೆ ಐದು ವಿಭಿನ್ನ ಪ್ರಾಂಪ್ಟ್ ಮಾದರಿಗಳನ್ನು ತೋರಿಸುತ್ತದೆ. ನೇರ ನಿರ್ದೇಶನಗಳಿಗೆ ಶೂನ್ಯ-ಶಾಟ್ ಪ್ರಾಂಪ್ಟ್‌ಗಳನ್ನು ಪ್ರಯತ್ನಿಸಿ, ಉದಾಹರಣೆಗಳಿಂದ ಕಲಿಯುವ ಕೆಲವು-ಶಾಟ್ ಪ್ರಾಂಪ್ಟ್‌ಗಳು, ತರ್ಕದ ಹಂತಗಳನ್ನು ಬಹಿರಂಗ ಮಾಡುವ ಚೈನ್-ಆಫ್-ಥಾಟ್ ಪ್ರಾಂಪ್ಟ್‌ಗಳು, ಮತ್ತು ಸಾಂದರ್ಭಿಕತೆಯನ್ನು ಹೊಂದಿಸುವ ಪಾತ್ರಾಧಾರಿತ ಪ್ರಾಂಪ್ಟ್‌ಗಳು. ನೀವು ನೋಡುತ್ತೀರಿ ಒಂದೇ ಮಾದರಿ ವಿನಂತಿಯ ರೂಪರೇಖೆಯ ಆಧಾರದ ಮೇಲೆ ಬಹುಮಾನಿತ ಬೇರೆ ಫಲಿತಾಂಶಗಳನ್ನು ನೀಡುತ್ತದೆ.

ಡೆಮೊ ಫಾರ್ಮ್ಯಾಟ್ ಪೇಟೆನ್‌ಗಳು ಕೂಡ ತೋರಿಸುತ್ತದೆ, ಇದು ಪರಿವರ್ತನೆಗಳೊಂದಿಗೆ ಪುನಃಬಳಕೆ ಮಾಡುವ ಪ್ರಾಂಪ್ಟ್ ಸೃಷ್ಟಿಸಲು ಶಕ್ತಿಶಾಲಿ ಮಾರ್ಗವಾಗಿದೆ.
ಕೆಳಗಿನ ಉದಾಹರಣೆ LangChain4j `PromptTemplate` ಬಳಸಿ ಪರಿವರ್ತನೆಗಳನ್ನು ತುಂಬುತ್ತದೆ. AI ನೀಡಲ್ಪಟ್ಟ ಗುರಿ ಮತ್ತು ಚಟುವಟಿಕೆಯ ಆಧಾರದ ಮೇಲೆ ಉತ್ತರಿಸುತ್ತದೆ.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ಚಾಟ್‍ನೊಂದಿಗೆ ಪ್ರಯತ್ನಿಸಿ:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "ಶೂನ್ಯ-ಶಾಟ್ ಮತ್ತು ಕೆಲವು-ಶಾಟ್ ಪ್ರಾಂಪ್ಟಿಂಗ್ ನಡುವಿನ ಭೇದವೇನು ಮತ್ತು ಯಾವಾಗ ಯಾವವನ್ನೂ ಬಳಸಬೇಕು?"
> - "ತೆಂಪರೇಚರ್ ಪ್ಯಾರಾಮೀಟರ್ ಮಾದರಿಯ ಪ್ರತಿಕ್ರಿಯೆಗಳನ್ನು ಹೇಗೆ ಪ್ರಭಾವಿಸುತ್ತದೆ?"
> - "ಉತ್ಪಾದನೆಯಲ್ಲಿ ಪ್ರಾಂಪ್ಟ್ ಇಂಜೆಕ್ಷನ್ ದಾಳಿಗಳನ್ನು ತಡೆಯಲು ಕೆಲವು ತಂತ್ರಗಳು ಯಾವುವು?"
> - "ಸಾಮಾನ್ಯ ಮಾದರಿಗಳಿಗಾಗಿ ಪುನಃಬಳಕೆ ಮಾಡಬಹುದಾದ PromptTemplate ವಸ್ತುಗಳನ್ನು ನಾನು ಹೇಗೆ ರಚಿಸಬಹುದೆ?"

**ಉಪಕರಣ ಏಕೀಕರಣ** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ಇಲ್ಲಿ LangChain4j ಶಕ್ತಿಶಾಲಿಯಾಗುತ್ತದೆ. ನೀವು `AiServices` ಅನ್ನು ಬಳಸಿಕೊಂಡು AI ಸಹಾಯಕರನ್ನು ಸೃಷ್ಟಿಸುತ್ತೀರಿ, ಅದು ನಿಮ್ಮ Java ಮೆಥಡ್‌ಗಳನ್ನು ಕರೆಸಬಹುದು. `@Tool("ವಿವರಣೆ")` ಅಡ್ಡಚಿಹ್ನೆ ನೀಡಿದ ಮೇಥಡ್‌ಗಳನ್ನು ಮಾತ್ರ ನೀವು ಟ್ಯಾಗ್ ಮಾಡಿ ಉಳಿಗಳು LangChain4j ಇತರ ಎಲ್ಲವನ್ನು ನಿರ್ವಹಿಸುತ್ತದೆ – AI ಉಪಯೋಗಿಸುವಾಗ ಎಲ್ಲ ಟೂಲ್ಗಳನ್ನು ಬಳಸುವ ಸಮಯವನ್ನು ಸ್ವಯಂಚಾಲಿತವಾಗಿ ತೀರ್ಮಾನಿಸುತ್ತದೆ. ಇದು ಫಂಕ್ಷನ್ ಕಾಲ್ಲಿಂಗ್ ಅನ್ನು ತೋರಿಸುತ್ತದೆ, ಅದು ಕ್ರಿಯೆಗಳು ಕೈಗೊಳ್ಳಬಹುದಾದ AI ನಿರ್ಮಿಸಲು ಪ್ರಮುಖ ತಂತ್ರವಾಗಿದೆ, ಕೇವಲ ಪ್ರಶ್ನೆಗಳನ್ನೇ ಇಲ್ಲ.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ಚಾಟ್‍ನೊಂದಿಗೆ ಪ್ರಯತ್ನಿಸಿ:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "@Tool ಅಡ್ಡಚಿಹ್ನೆ ಹೇಗೆ ಕೆಲಸ ಮಾಡುತ್ತದೆ ಮತ್ತು LangChain4j ಅದು ಹಿಂದೆ ಏನು ಮಾಡುತ್ತದೆ?"
> - "ಸಂಕೀರ್ಣ ಸಮಸ್ಯೆಗಳನ್ನು ಪರಿಹರಿಸಲು AI ಅನೇಕ ಉಪಕರಣಗಳನ್ನು ಶ್ರೇಣಿಬದ್ಧವಾಗಿ ಕರೆಸಬಹುದೇ?"
> - "ಒಂದು ಉಪಕರಣ ವಿನ್ಯಾಸ ದೋಷ ಉಂಟುಮಾಡಿದರೆ – ತಪ್ಪುಗಳನ್ನು ನಾನು ಹೇಗೆ ನಿರ್ವಹಿಸಬೇಕು?"
> - "ಈ ಕ್ಯಾಲ್ಕುಲೇಟರ್ ಉದಾಹರಣೆಯ ಬದಲು ನಿಜವಾದ API ಅನ್ನು ನಾನು ಹೇಗೆ ಏಕೀಕರಿಸಬಹುದು?"

**ಡಾಕ್ಯುಮೆಂಟ್ ಪ್ರಶ್ನೋತ್ತರ (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ನೀವು ಇಲ್ಲಿ RAG (ಪುನರ್ಫಿರಿತ-ವೃದ್ಧಿಗೊಳಿಸಿದ ರಚನೆ) ಯ ಮೂಲವನ್ನು ನೋಡುತ್ತೀರಿ. ಮಾದರಿಯ ತರಬೇತಿ ಡೇಟಾಗೆ ಆಧಾರವಿಲ್ಲದೆ, ನೀವು [`document.txt`](../../../00-quick-start/document.txt) ನಿಂದ ವಿಷಯವನ್ನು ಲೋಡ್ ಮಾಡಿ ಇದನ್ನು ಪ್ರಾಂಪ್ಟ್‌ನಲ್ಲಿ ಸೇರಿಸುತ್ತೀರಿ. AI ನಿಮ್ಮ ಡಾಕ್ಯುಮೆಂಟ್ ಆಧಾರದ ಮೇಲೆ ಉತ್ತರಿಸುತ್ತದೆ, ಸಾಮಾನ್ಯ ಜ್ಞಾನಕ್ಕೆ ಅಲ್ಲವಾಗಿದೆ. ಇದು ನಿಮ್ಮ ಸ್ವಂತ ಮಾಹಿತಿಯೊಂದಿಗೆ ಕಾರ್ಯನಿರ್ವಹಿಸುವ ವ್ಯವಸ್ಥೆಯ ನಿರ್ಮಾಣದ ಮೊದಲ ಹಂತವಾಗಿದೆ.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **ಗಮನಿಸಿ:** ಈ ಸರಳ ವಿಧಾನವು ಸಂಪೂರ್ಣ ಡಾಕ್ಯುಮೆಂಟ್ ಅನ್ನು ಪ್ರಾಂಪ್ಟ್‌ಗೆ ಲೋಡ್ ಮಾಡುತ್ತದೆ. ದೊಡ್ಡ ಫೈಲ್‌ಗಳಿಗೆ (>10KB), ನೀವು ಸಾಂದರ್ಭಿಕತೆ ಮಿತಿಗಳನ್ನು ಮೀರಿಸುತ್ತೀರಿ. ಘಟಕ 03 ನಿರ್ಮಾಣ RAG ವ್ಯವಸ್ಥೆಗಳಿಗಾಗಿ ಚಂಕಿಂಗ್ ಮತ್ತು ಷರತ್ತು ಹುಡುಕಾಟವನ್ನು ಕವರುತ್ತದೆ.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ಚಾಟ್‍ನೊಂದಿಗೆ ಪ್ರಯತ್ನಿಸಿ:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "RAG AI ಭ್ರಮಣಗಳನ್ನು ಮಾದರಿಯ ತರಬೇತಿ ಡೇಟಾ ಬಳಸದಿರುವುದರಿಂದ ಹೇಗೆ ತಡೆಯುತ್ತದೆ?"
> - "ಈ ಸರಳ ವಿಧಾನ ಮತ್ತು ವેક્ટರ್ ಎಮ್ಬೆಡಿಂಗ್‌ಗಳನ್ನು ಬಳಸಿ ಹುಡುಕಾಟದ ನಡುವೆ ಏನು ಭೇದ ಇದೆ?"
> - "ನಾನು ಇದನ್ನು ಹೇಗೆ ವಿಸ್ತರಿಸಿ ಹಲವಾರು ಡಾಕ್ಯುಮೆಂಟ್‌ಗಳು ಅಥವಾ ದೊಡ್ಡ ಜ್ಞಾನ ಕ್ಷೇತ್ರಗಳನ್ನು ನಿರ್ವಹಿಸುವೆನು?"
> - "AI ನ್ನು ಕೇವಲ ಒದಗಿಸಿದ ಸಾಂದರ್ಭಿಕತೆ ಮಾತ್ರ ಬಳಸುವುದು ಖಚಿತಪಡಿಸಲು ಪ್ರಾಂಪ್ಟ್ ವಿನ್ಯಾಸದ ಉತ್ತಮ ಪದ್ಧತಿಗಳು ಯಾವುವು?"

**ಜವಾಬ್ದಾರಿಯುಕ್ತ AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ಸುಮಾರು-ರಕ್ಷಣೆಗಳಿಂದ AI ಸುರಕ್ಷತೆ ನಿರ್ಮಿಸಿ. ಈ ಡೆಮೊ ಎರಡು ಪರರಕ್ಷಕ ಪದರಗಳನ್ನು ಒಟ್ಟಿಗೆ ಕಾರ್ಯನಿರ್ವಹಿಸುತ್ತವೆ:

**ಭಾಗ 1: LangChain4j ಇನ್ಪುಟ್ ಗಾರ್ಡ್‌ರೈಲ್ಸ್** – LLM ಗೆ ತಲುಪುವ ಮುನ್ನ ಹಾನಿಕರ ಪ್ರಾಂಪ್ಟ್‌ಗಳನ್ನು ತಡೆಯಿರಿ. ನಿಷಿದ್ಧ ಕೀವರ್ಡ್‌ಗಳು ಅಥವಾ ಮಾದರಿಗಳಿಗಾಗಿ ಕಸ್ಟಮ್ ಗಾರ್ಡ್‌ರೈಲ್ಸ್ ರಚಿಸಿ. ಇವು ನಿಮ್ಮ ಕೋಡ್‌ನಲ್ಲಿ ಕಾರ್ಯನಿರ್ವಹಿಸುವುದರಿಂದ ವೇಗವಾಗಿ ಮತ್ತು ಉಚಿತ.

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

**ಭಾಗ 2: ಸರಬರಾಜುದಾರರ ಸುರಕ್ಷತಾ ಫಿಲ್ಟರ್ ಗಳು** – GitHub Models ನಲ್ಲಿ-ನಿರ್ಮಿತ ಫಿಲ್ಟರ್‌ಗಳು ನಿಮ್ಮ ಗಾರ್ಡ್‌ರೈಲ್ಸ್ ತಪ್ಪಿದ ವಿಚಾರಗಳನ್ನು ಹಿಡಿಯುತ್ತವೆ. ಗಂಭೀರ ಉಲ್ಲಂಘನೆಗಳಿಗೆ ಕಠಿಣ ತಡೆಗಳು (HTTP 400 ದೋಷಗಳು) ಮತ್ತು ಮೃದು ನಿರಾಕರಣೆಗಳು (AI ಶಿಷ್ಟವಾಗಿ ನಿರಾಕರಿಸುವುದು) ಕಾಣಿಸುತ್ತವೆ.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ಚಾಟ್‍ನೊಂದಿಗೆ ಪ್ರಯತ್ನಿಸಿ:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ತೆರೆಯಿರಿ ಮತ್ತು ಕೇಳಿ:
> - "InputGuardrail ಏನು ಮತ್ತು ನಾನು ನನ್ನದೇನು ರಚಿಸಲು ಹೇಗೆ?"
> - "ಕಠಿಣ ತಡೆ ಮತ್ತು ಮೃದು ನಿರಾಕರಣೆಯ ನಡುವಿನ ಭೇದವೇನು?"
> - "ಏಕೆ ಗಾರ್ಡ್‌ರೈಲ್ಸ್ ಮತ್ತು ಸರಬರಾಜುದಾರರ ಫಿಲ್ಟರ್‌ಗಳನ್ನು ಒಟ್ಟಿಗೆ ಬಳಸಬೇಕು?"

## ಮುಂದಿನ ಹಂತಗಳು

**ಮುಂದಿನ ಘಟಕ:** [01-ಪರಿಚಯ - LangChain4j ಮತ್ತು gpt-5 ನಲ್ಲಿ Azure ಬಳಸಿ ಪ್ರಾರಂಭಿಕೆ](../01-introduction/README.md)

---

**ನ್ಯಾವಿಗೇಶನ್:** [← ಹಿಂಬಾಲಿಸಿ ಮುಖ್ಯಕ್ಕೆ](../README.md) | [ಮುಂದೆ: Module 01 - ಪರಿಚಯ →](../01-introduction/README.md)

---

## ತೊಂದರೆ ಪರಿಹಾರ

### ಮೊದಲ ಬಾರಿ Maven ರಚನೆ

**ಸಮಸ್ಯೆ:** ಪ್ರಾಥಮಿಕ `mvn clean compile` ಅಥವಾ `mvn package` ಬಹಳ ಸಮಯ ತೆಗೆದುಕೊಳ್ಳುತ್ತದೆ (10-15 ನಿಮಿಷ)

**ಕಾರಣ:** ಮೊದಲ ರಚನೆಯಲ್ಲಿ Mavenಗೆ ಎಲ್ಲಾ ಯೋಜನೆಯ ಅವಲಂಬನೆಗಳನ್ನು ಡೌನ್‌ಲೋಡ್ ಮಾಡಬೇಕಾಗುತ್ತದೆ (Spring Boot, LangChain4j ಗ್ರಂಥಾಲಯಗಳು, Azure SDK ಗಳು ಇತ್ಯಾದಿ).

**ಪರಿಹಾರ:** ಇದು නಾರ್ಮಲ್ ವರ್ತನೆ. ಮುಂದಿನ ರಚನೆಗಳು ಹೆಚ್ಚು ವೇಗವಾಗಿರುತ್ತವೆ ಏಗೆಂದರೆ ಅವಲಂಬನೆಗಳು ಸ್ಥಳೀಯವಾಗಿ ಕ್ಯಾಶ್ ಆಗಿರುತ್ತವೆ. ಡೌನ್‌ಲೋಡ್ ಸಮಯ ನಿಮ್ಮ ಜಾಲತಾಣ ವೇಗದ ಮೇಲೆ ಅವಲಂಬಿತವಾಗಿರುತ್ತದೆ.
### PowerShell Maven ಕಮాండ్ ಸಿಂಟ್ಯಾಕ್ಸ್

**ಸಮಸ್ಯೆ**: Maven ಕಮಾಂಡ್‌ಗಳು `Unknown lifecycle phase ".mainClass=..."` ಎಂಬ ದೋಷದೊಂದಿಗೆ ವಿಫಲವಾಗುತ್ತವೆ

**ಕಾರಣ**: PowerShell `=` ಅನ್ನು ವೇರಿಯಬಲ್ ನಿಯೋಜನಾ ಸೇತುವಾಗಿ ವಿವೇಚಿಸುತ್ತದೆ, ಇದು Maven ಗುಣಲಕ್ಷಣ ಸಿಂಟ್ಯಾಕ್ಸ್ ಅನ್ನು ಮುರಿದುಹಾಕುತ್ತದೆ

** ಪರಿಹಾರ**: Maven ಕಮಾಂಡ್ ಮುನ್ನ `--%` ಎಂಬ ಸ್ಟಾಪ್-ಪಾರ್ಸಿಂಗ್ ಆಪರೇಟರ್ ಅನ್ನು ಬಳಸಿ:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ಆಪರೇಟರ್ PowerShell ಗೆ ಉಳಿದ ಎಲ್ಲಾ ಅರ್ಗುಮೆಂಟ್‌ಗಳನ್ನು ವರ್ಣನಾತ್ಮಕವಾಗಿ, ಅರ್ಥಮಾಡಿಕೊಳ್ಳದೆ Maven ಕ್ಕೆ ಪಾಸುಮಾಡಬೇಕು ಎಂದು ಸೂಚಿಸುತ್ತದೆ.

### Windows PowerShell Emoji ಪ್ರದರ್ಶನ

**ಸಮಸ್ಯೆ**: PowerShell ನಲ್ಲಿ AI ಪ್ರತಿಕ್ರಿಯೆಗಳಂತೆ ಇಮೋಜಿಗಳ ಬದಲು `????` ಅಥವಾ `â??` ಮುಂತಾದ ಅಪ್ರಸ್ತುತ ಚಿಹ್ನೆಗಳು ಕಾಣಿಸುತ್ತವೆ

**ಕಾರಣ**: PowerShell ನ ಡೀಫಾಲ್ಟ್ ಎನ್‌ಕೋಡಿಂಗ್ UTF-8 ಇಮೋಜಿಗಳನ್ನು ಬೆಂಬಲಿಸುವಿಕೆಯಿಂದಿಲ್ಲ

**ಪರಿಹಾರ**: ಜಾವಾ ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ಕಾರ್ಯಗೊಳಿಸುವುದಕ್ಕೆ ಮುನ್ನ ಈ ಕಮಾಂಡ್ ಅನ್ನು ಚಾಲನೆ ಮಾಡಿ:
```cmd
chcp 65001
```

ಇದು ಟರ್ಮಿನಲ್‌ನಲ್ಲಿ UTF-8 ಎನ್‌ಕೋಡಿಂಗ್ ಬಲವಂತಪಡಿಸುತ್ತದೆ. ಪರ್ಯಾಯವಾಗಿ, ಉತ್ತಮ ಯುನಿಕೋಡ್ ಬೆಂಬಲ ಹೊಂದಿರುವ Windows Terminal ಉಪಯೋಗಿಸಬಹುದು.

### API ಕರೆಗಳನ್ನು ಡಿಬಗ್ಗಿಂಗ್

**ಸಮಸ್ಯೆ**: AI ಮಾದರಿಯಿಂದ ಪ್ರಮಾಣೀಕರಣ ದೋಷಗಳು, ದರ ಮಿತಿ ಗಳು ಅಥವಾ ಅಕಾಲಿಕ ಪ್ರತಿಕ್ರಿಯೆಗಳು

**ಪರಿಹಾರ**: ಉದಾಹರಣೆಗಳಲ್ಲಿ `.logRequests(true)` ಮತ್ತು `.logResponses(true)` ಇವೆ, ಇವು API ಕರೆಗಳನ್ನು ಕಾನ್ಸೋಲ್‌ನಲ್ಲಿ ಪ್ರದರ್ಶಿಸಲು ಸಹಾಯ ಮಾಡುತ್ತದೆ. ಇದು ಪ್ರಮಾಣೀಕರಣ ದೋಷಗಳು, ದರ ಮಿತಿಗಳು ಅಥವಾ ಅಕಾಲಿಕ ಪ್ರತಿಕ್ರಿಯೆಗಳನ್ನು ಪರಿಹರಿಸಲು ನೆರವಾಗುತ್ತದೆ. ಉತ್ಪಾದನೆಯಲ್ಲಿ ಈ ಫ್ಲಾಗ್‌ಗಳನ್ನು ತೆಗೆದುಹಾಕಿ ಲಾಗ್ ಶಬ್ದವನ್ನು ಕಡಿಮೆ ಮಾಡಬಹುದು.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ಜinăಾ ಅಧಿಸೂಚನೆ**:  
ಈ ಡಾಕ್ಯುಮೆಂಟ್ ಅನ್ನು AI ಅನುವಾದ ಸೇವೆ [Co-op Translator](https://github.com/Azure/co-op-translator) ಬಳಸಿ ಅನುವದಿಸಲಾಗಿದೆ. ನಾವು ಶುದ್ಧತೆಯನ್ನು ಕನಸಿಕೊಳ್ಳುತ್ತೇವೆ, ಆದರೆ ಸ್ವಯಂಚಾಲಿತ ಅನುವಾದಗಳಲ್ಲಿ ತಪ್ಪುಗಳು ಅಥವಾ ಅಸತ್ಯತೆಗಳು ಇರಬಹುದಾಗಿದೆ ಎಂದು ದಯವಿಟ್ಟು ತಿಳಿದುಕೊಳ್ಳಿ. ಮೂಲ ಭಾಷೆಯ ಅದರ ಮೂಲ ಡಾಕ್ಯುಮೆಂಟ್ ಅನ್ನು ಪ್ರಾಧಿಕಾರ ಮೂಲವೆಂದು ಪರಿಗಣಿಸಬೇಕು. ಮಹತ್ವಪೂರ್ಣ ಮಾಹಿತಿಗಾಗಿ, ವೃತ್ತಿಪರ ಮಾನವ ಅನುವಾದವನ್ನು ಶಿಫಾರಸು ಮಾಡಲಾಗುತ್ತದೆ. ಈ ಅನುವಾದದ ಬಳಕೆಯಿಂದಂಟಾಗುವ ಯಾವುದೇ ತಪ್ಪುಗಳಿಗಾಗಿ ನಾವು ಹೊಣೆಗಾರರು ಅಲ್ಲ.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->