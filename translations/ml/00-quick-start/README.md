# Module 00: ക്വിക്ക് സ്റ്റാർട്ട്

## ഉള്ളടക്ക പട്ടിക

- [പരിചയം](../../../00-quick-start)
- [LangChain4j എന്താണ്?](../../../00-quick-start)
- [LangChain4j ആശ്രിതവસ્તુകൾ](../../../00-quick-start)
- [ആവശ്യകതകൾ](../../../00-quick-start)
- [സജ്ജീകരിക്കൽ](../../../00-quick-start)
  - [1. നിങ്ങളുടെ GitHub ടോക്കൺ എടുത്തെടുക്കുക](../../../00-quick-start)
  - [2. നിങ്ങളുടെ ടോക്കൺ സജ്ജമാക്കുക](../../../00-quick-start)
- [ഉദാഹരണങ്ങൾ ഓടിക്കുക](../../../00-quick-start)
  - [1. അടിസ്ഥാന ചാറ്റ്](../../../00-quick-start)
  - [2. പ്രോംപ്റ്റ് പാറ്റേൺസ്](../../../00-quick-start)
  - [3. ഫങ്ഷൻ കോളിംഗ്](../../../00-quick-start)
  - [4. ഡോക്യുമെന്റ് Q&A (ഇസിയുടെ RAG)](../../../00-quick-start)
  - [5. ഉത്തരവാദിത്വമുള്ള AI](../../../00-quick-start)
- [പ്രതിയേക ഉദാഹരണങ്ങൾ കാണിക്കുന്നത്](../../../00-quick-start)
- [അടുത്ത കാര്യങ്ങൾ](../../../00-quick-start)
- [പ്രശ്നനിവാരണങ്ങൾ](../../../00-quick-start)

## പരിചയം

ഈ ക്വിക്കും സ്റ്റാർട്ട് LangChain4j ഉപയോഗിച്ച് നിങ്ങൾക്ക് വേഗത്തിൽ ആരംഭിക്കാനാണ് ഉദ്ദേശിച്ചിരിക്കുന്നത്. ഇത് LangChain4j, GitHub Models എന്നിവ ഉപയോഗിച്ച് AI ആപ്ലിക്കേഷനുകൾ നിർമ്മിക്കൽ സംബന്ധിച്ച ഏറ്റവും അടിസ്ഥാനങ്ങൾ ഉൾക്കൊള്ളുന്നു. അടുത്ത മൊഡ്യൂളുകളിൽ നിങ്ങൾ Azure OpenAI, GPT-5.2 എന്നിവയിലേക്ക് മാറുകയും ഓരോ ആശയം depth ആയി പഠിക്കുകയും ചെയ്യും.

## LangChain4j എന്താണ്?

LangChain4j ഒരു ജാവ ലൈബ്രറിയാണ്, AI-ചാലിത ആപ്ലിക്കേഷനുകൾ നിർമ്മിക്കുന്നത് ലളിതമാക്കുന്നു. HTTP ക്ലയന്റുകളും JSON പാഴ്‌സിംഗും കൈകാര്യം ചെയ്യാൻ പകരം, നിങ്ങൾ സുതാര്യമായ ജാവ API-കൾ ഉപയോഗിക്കുന്നു.

LangChain എന്ന പദം LangChain4j-യിലെ "ചെയിനിംഗ്" ആയി അന്വയം ചെയ്തിരിക്കുന്നു — നിരവധി ഘടകങ്ങൾ പരസ്പരം ബന്ധിപ്പിച്ച് പ്രവർത്തിക്കുന്നു - നിങ്ങൾ ഒരു പ്രോംപ്റ്റിൽ നിന്നും മോഡലിലേക്കും രൂപാന്തരിക്കാവുന്ന ഒരു പാഴ്സറിലേക്കും, അല്ലെങ്കിൽ ഒരൊറ്റ ഔട്ട്പുട്ട് അടുത്ത ഇൻപുട്ടിനു ഇടയായി പ്രവർത്തിക്കുന്ന AI കോളുകൾ പരമ്പരയായി ബന്ധിപ്പിക്കാം. ഈ ക്വിക്ക് സ്റ്റാർട്ട് അടിസ്ഥാനങ്ങൾക്കാണ് ഊന്നൽ നൽകുന്നത്, കൂടുതൽ സങ്കീർണ്ണമായ ചെയിനുകൾക്കു മുമ്പ്.

<img src="../../../translated_images/ml/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j-യിലെ ഘടകങ്ങൾ ചേർത്ത് ശക്തമായ AI പ്രവൃത്തികൾ സൃഷ്ടിക്കുന്നു*

നാം മൂന്നു മുഖ്യ ഘടകങ്ങൾ ഉപയോഗിക്കും:

**ChatModel** - AI മോഡൽ ഇടപെടലുകളുടെ ഇന്റർഫേസ്. `model.chat("prompt")` വിളിച്ച് ഒരു പ്രതികരണostring നേടാം. GitHub Models പോലുള്ള OpenAI- അനുയോജ്യ എൻഡ്‌പോയിന്റുകളുമായി പ്രവർത്തിക്കുന്ന `OpenAiOfficialChatModel` നാം ഉപയോഗിക്കുന്നു.

**AiServices** - ടൈപ്-സേഫ് AI സർവ്വീസ് ഇന്റർഫേസ് സൃഷ്ടിക്കുന്നു. മെത്തഡുകൾ നിർവ്വചിച്ച്, അവ `@Tool` ഉപയോഗിച്ച് അണോട്ടേറ്റ് ചെയ്യുക; LangChain4j ഓർക്കസ്ട്രേഷനു ശ്രമിക്കുന്നു. ആവശ്യമായപ്പോൾ AI ആтоматമായി നിങ്ങളുടെ ജാവ മെത്തഡുകൾ വിളിക്കും.

**MessageWindowChatMemory** - സംവാദ ചരിത്രം പരിരക്ഷിക്കുന്നു. ഇതില്ലാതെ, ഓരോ അഭ്യർത്ഥനയും സ്വതന്ത്രമാണ്. അതോടൊപ്പം, AI മുമ്പത്തെ സന്ദേശങ്ങൾ ഓർത്തുകൂടി പല തിരങ്ങളിലായി സന്ദർഭം നിലനിർത്തും.

<img src="../../../translated_images/ml/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j ആർക്കിടെക്ചർ - AI ആപ്ലിക്കേഷനുകൾക്ക് ശക്തി നൽകുന്ന പ്രധാന ഘടകങ്ങളുടെ പ്രവർത്തനം*

## LangChain4j ആശ്രിതവસ્તુകൾ

ഈ ക്വിക്ക് സ്റ്റാർട്ട് മൂന്ന് മേവൻ ആശ്രിതവസ്തുക്കൾ [`pom.xml`](../../../00-quick-start/pom.xml) ല്‍ ഉപയോഗിക്കുന്നു:

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

`langchain4j-open-ai-official` മോഡ്യൂൾ OpenAI-അനുരൂപ API-കളുമായി ബന്ധിപ്പിക്കുന്ന `OpenAiOfficialChatModel` ക്ലാസ് നൽകുന്നു. GitHub Models ഇതേ API ഫോർമാറ്റ് ഉപയോഗിക്കുന്നതിനാൽ പ്രത്യേക അഡാപ്പ്റ്റർ ആവശ്യമില്ല - അടിസ്ഥാന URL `https://models.github.ai/inference` ആയി സൂചിപ്പിക്കുക.

`langchain4j-easy-rag` മോഡ്യൂൾ സ്വയം ഡോക്യുമെന്റ് വിഭജനം, എംബെഡ്ഡിങ്ങ്, വീണ്ടെടുക്കൽ എന്നിവ അതേത്തരം സജ്ജമാക്കുന്നു, അതുകൊണ്ടുതന്നെ നിങ്ങൾക്ക് RAG ആപ്ലിക്കേഷനുകൾ രൂപീകരിക്കാൻ യാതൊരു നിമിത് ക്രമീകരണവും ആവശ്യമില്ല.

## ആവശ്യകതകൾ

**Dev Container ഉപയോഗിച്ചാലോ?** ജാവയും മേവനും ഇതിനകം ഇൻസ്റ്റാൾ ചെയ്തിട്ടുണ്ട്. നിങ്ങൾക്ക് GitHub വ്യക്തിഗത ആക്‌സസ് ടോക്കൺ മാത്രം വേണം.

**ലോകൽ ഡെവലപ്പ്മെന്റ്:**
- Java 21+, Maven 3.9+
- GitHub വ്യക്തിഗത ആക്‌സസ് ടോക്കൺ (നൽകുന്ന നിർദേശങ്ങൾ താഴെ)

> **ഗുരുതരം:** ഈ മഡ്‌യുല്‍ GitHub Models-നു വേണ്ടി `gpt-4.1-nano` മോഡൽ ഉപയോഗിക്കുന്നു. കോഡിൽ മോഡൽ പേരെ മാറ്റരുത് - അത് GitHub ലഭ്യമായ മോഡലുകളുമായി പ്രവർത്തിക്കുന്നതിന് ക്രമീകരിച്ചിരിക്കുന്നു.

## സജ്ജീകരിക്കൽ

### 1. നിങ്ങളുടെ GitHub ടോക്കൺ എടുത്തെടുക്കുക

1. [GitHub സെറ്റിങ്ങുകൾ → Personal Access Tokens](https://github.com/settings/personal-access-tokens) കാണുക
2. "Generate new token" ക്ലിക് ചെയ്യുക
3. വ്യക്തമായ ഒരു പേര് നൽകുക (ഉദാഹരണത്തിന്, "LangChain4j Demo")
4. കാലഹരണ തിയതി നിശ്ചയിക്കൽ (7 ദിവസം ശിപാർശ)
5. "Account permissions" ൽ "Models" എടുക്കുക, "Read-only" ആയി സജ്ജമാക്കുക
6. "Generate token" ക്ലിക് ചെയ്യുക
7. ടോക്കൺ കോപ്പി ചെയ്ത് സൂക്ഷിക്കുക - പിന്നീട് കാണാനാകില്ല

### 2. നിങ്ങളുടെ ടോക്കൺ സജ്ജമാക്കുക

**വികസ്വിടെ (VS Code) ഉപയോഗിക്കൽ (ശിപാർശ):**

VS Code ഉപയോഗിച്ചാൽ, പ്രോജക്റ്റ് റൂട്ട്-level `.env` ഫയലിൽ നിങ്ങളുടെ ടോക്കൺ ചേർക്കുക:

`.env` ഫയൽ ഇല്ലെങ്കിൽ, `.env.example` കോപ്പി ചെയ്ത് `.env` ആക്കൂ അല്ലെങ്കിൽ പുതിയ `.env` ഫയൽ പ്രോജക്റ്റ് റൂട്ട്-ലേക്ക് സൃഷ്ടിക്കുക.

**ഉദാഹരണ `.env` ഫയൽ:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env ഫയൽയിൽ
GITHUB_TOKEN=your_token_here
```

പിന്നീട്, എളുപ്പം Explorer-യിൽ ഏതെങ്കിലും ഡെമോ ഫയലിൽ (ഉദാ: `BasicChatDemo.java`) റൈറ്റ്-ക്ലിക്ക് ചെയ്ത് **"Run Java"** തിരഞ്ഞെടുക്കാം അല്ലെങ്കിൽ Run and Debug പാനലിലെ ലോഞ്ച് കോൺഫിഗറേഷനുകൾ ഉപയോഗിക്കുക.

**ടെർമിനൽ ഉപയോഗിക്കൽ**

ടോക്കൺ എൻവയോൺമെന്റ് വേരിയബിൾ ആയി സജ്ജമാക്കുക:

**ബാഷ്:**
```bash
export GITHUB_TOKEN=your_token_here
```

**പവർഷെൽ:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## ഉദാഹരണങ്ങൾ ഓടിക്കുക

**VS Code ഉപയോഗിച്ച്:** Explorer-ലേക്ക് ഏതെങ്കിലും ഡെമോ ഫയലിൽ റൈറ്റ്-ക്ലിക്ക് ചെയ്ത് **"Run Java"** തിരഞ്ഞെടുക്കുക, അല്ലെങ്കിൽ Run and Debug ല吤ഞ്ച് കോൺഫിഗറേഷനുകൾ ഉപയോഗിക്കാം (നിങ്ങളുടെ ടോക്കൺ `.env` ഫയലിൽ ചേർത്തിട്ടുണ്ടെന്ന് ഉറപ്പാക്കുക).

**മേവൻ ഉപയോഗിച്ച്:** comando line-ൽ നിന്നും ഓടിക്കാം:

### 1. അടിസ്ഥാന ചാറ്റ്

**ബാഷ്:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**പവർഷെൽ:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. പ്രോംപ്റ്റ് പാറ്റേൺസ്

**ബാഷ്:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**പവർഷെൽ:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

സീറോ ഷോട്ട്, ഫ്യൂ ഷോട്ട്, ചെയിൻ ഓഫ് തോട്ട്, റോള്ബേസ് പ്രോംപ്റ്റിംഗ് കാണിക്കുന്നു.

### 3. ഫങ്ഷൻ കോളിംഗ്

**ബാഷ്:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**പവർഷെൽ:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

ആവശ്യമായപ്പോൾ AI സ്വയം നിങ്ങളുടെ ജാവ മെത്തഡുകൾ വിളിക്കും.

### 4. ഡോക്യുമെന്റ് Q&A (ഇസിയുടെ RAG)

**ബാഷ്:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**പവർഷെൽ:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

സ്വയം എംബെഡിംഗ്, വീണ്ടെടുത്തൽ ഉപയോഗിച്ച് ഡോക്യുമെന്റുകൾക്കു പറ്റിയ ചോദ്യങ്ങൾ ചോദിക്കാം.

### 5. ഉത്തരവാദിത്വമുള്ള AI

**ബാഷ്:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**പവർഷെൽ:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI സുരക്ഷാ ഫിൽട്ടറുകൾ എങ്ങിനെ ഹാനികരമായ ഉള്ളടക്കം തടയുന്നു എന്ന് കാണുക.

## പ്രതിയേക ഉദാഹരണങ്ങൾ കാണിക്കുന്നത്

**അടിസ്ഥാന ചാറ്റ്** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

ഇവിടെ LangChain4j-ന്റെ ലളിതമായ രൂപം കാണാം. `OpenAiOfficialChatModel` സൃഷ്ടിച്ച് `.chat()` വഴി പ്രോംപ്റ്റ് അയച്ച് പ്രതികരണം നേടി. മോഡലുകൾ ഇനിഷ്യലൈസ് ചെയ്യൽ, കസ്റ്റം എൻഡ്‌പോയിന്റുകളും API കീകളും ഉപയോഗിച്ചുള്ള രീതികൾ ഇത് കാണിക്കുന്നു. ഇത് മനസ്സിലാക്കിയാൽ എല്ലാ മറ്റ് കാര്യങ്ങളും ഇതിന്റെ അടിസ്ഥാനത്തിലാണ്.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 GitHub Copilot [ചാറ്റുമായി പരീക്ഷിക്കുക](https://github.com/features/copilot):** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) തുറന്നിട്ട് ചോദിക്കുക:
> - "GitHub Models-ൽ നിന്ന് Azure OpenAI-ലേക്ക് ഈ കോഡ് എങ്ങനെ മാറ്റാം?"
> - "OpenAiOfficialChatModel.builder()-ൽ മറ്റേതെല്ലാം parameters ക്രമീകരിക്കാമ?"
> - "കൃത്യമായ പ്രതികരണം എത്തുന്നതുവരെ കാത്തിരിക്കാൻ പകരം സ്‌ട്രിമിംഗ് പ്രതികരണങ്ങൾ എങ്ങനെ ചേർക്കാം?"

**പ്രോംപ്റ്റ് എൻജിനീയറിങ്** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

ഇപ്പോൾ മോഡലുമായി സംവേദനം എങ്ങനെ നടത്തണം എന്നത് പഠിച്ചു. ഇനി നിങ്ങൾ പറയുന്ന കാര്യങ്ങൾ പരിശോധിക്കും. ഈ ഡെമോ ആ_same_ മോഡൽ സജ്ജീകരണവും അഞ്ച് വ്യത്യസ്ത പ്രോംപ്റ്റിംഗ് മാതൃകകളും കാണിക്കുന്നു. നേരിട്ട് നിർദ്ദേശങ്ങൾക്കായി സീറോ ഷോട്ട്, ഉദാഹരണങ്ങളിൽ നിന്നു പഠിക്കാൻ few-shot, ചിന്താശൃംഖലയുമായി chain-of-thought, പരിസ്ബന്ദ്ധം നിശ്ചയിക്കുന്ന role-based prompting എന്നിവ പരീക്ഷിക്കുക. നിയമിത വിചാര സമ്പ്രദായത്തിൽ വ്യത്യസ്ത ഫലങ്ങൾ ലഭിക്കുന്നതും കാണാം.

ഡെമോ പ്രോംപ്റ്റ് ടെംപ്ലേറ്റുകളും കാണിക്കുന്നു, പുനഃഉപയോഗയോഗ്യമായി വെറും മൂല്യങ്ങൾ പുറത്താക്കാൻ സഹായിക്കുന്നു.
താഴെയുള്ള ഉദാഹരണം LangChain4j `PromptTemplate` ഉപയോഗിച്ച് മൂല്യങ്ങൾ പൂരിപ്പിക്കുന്ന പ്രോംപ്റ്റ് കാണിക്കുന്നു. നൽകിയ ലക്ഷ്യസ്ഥലവും പ്രവർത്തനവും അടിസ്ഥാനമാക്കി AI മറുപടി നൽകും.

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

> **🤖 GitHub Copilot [ചാറ്റുമായി പരീക്ഷിക്കുക](https://github.com/features/copilot):** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) തുറന്ന് ചോദിക്കുക:
> - "സീറോ ഷോട്ടും few-shot ഉം എന്ത് വ്യത്യാസമുള്ളതാണ്, എപ്പോൾ ഏത് ഉപയോഗിക്കണം?"
> - "ടെംപറേച്ചർ പാരാമീറ്റർ മോഡലിന്റെ പ്രതികരണങ്ങളിൽ എങ്ങനെ സ്വാധീനം ചെലുത്തുന്നു?"
> - "പ്രോംപ്റ്റ് ഇൻജെക്ഷൻ ആക്രമണങ്ങൾ തടയാൻ പ്രയോഗിക്കുന്ന രീതികൾ ഏതെല്ലാം?"
> - "സാധാരണ മാതൃകകൾക്കായി പുനഃഉപയോഗയോഗ്യമായ PromptTemplate ഓബ്‌ജെക്ടുകൾ എങ്ങനെ സൃഷ്ടിക്കാം?"

**ടൂൾ ഇന്റഗ്രേഷൻ** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ഇവിടെ LangChain4j ശക്തമായി മാറുന്നു. `AiServices` ഉപയോഗിച്ച് ജാവ മെത്തഡുകൾക്ക് കോളുകൾ ചെയ്യാവുന്ന AI അസിസ്റ്റന്റിനെ സൃഷ്ടിക്കും. മെത്തഡുകൾ `@Tool("വിവരണം")` ഉപയോഗിച്ച് അണോട്ടേറ്റ് ചെയ്യുക; LangChain4j ബാക്കി ഓർക്കസ്ട്രേഷൻ നടത്തും - ഉപയോക്താവ് ചോദിക്കുന്നതിനെ അടിസ്ഥാനമാക്കി AI യഥോചിതമായി ടൂളുകൾ ഉപയോഗിക്കുന്നതും കാണിക്കും. ഫങ്ഷൻ കോളിംഗ് വ്യക്തമാക്കുന്നു, AI ചോദ്യം മാത്രമല്ല പ്രവർത്തനങൾ ചെയ്യാനും കഴിയുമെന്നു കാണിക്കുന്നു.

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

> **🤖 GitHub Copilot [ചാറ്റുമായി പരീക്ഷിക്കുക](https://github.com/features/copilot):** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) തുറന്ന് ചോദിക്കുക:
> - "@Tool അനോട്ടേഷൻ എങ്ങനെ പ്രവർത്തിക്കുന്നു? LangChain4j ഈ വിവരത്തോടു പിന്നോട്ട് എങ്ങനെ കൈകാര്യം ചെയ്യുന്നു?"
> - "സങ്കീര്‍ണ പ്രശ്നങ്ങൾ പരിഹരിക്കാൻ AI അനന്തരം പല ടൂളുകളും കോളുകൾ നടത്താമോ?"
> - "ടൂൾ ഒരു തെറ്റുചെയ്യുമ്പോൾ എങ്ങനെയാണ് അത് കൈകാര്യം ചെയ്യേണ്ടത്?"
> - "ഈ കാൽകുലേറ്റർ ഉദാഹരണത്തിന് പകരം യഥാർത്ഥ API എങ്ങനെ ഇന്റഗ്രേറ്റ് ചെയ്യാം?"

**ഡോക്യുമെന്റ് Q&A (ഇസിയുടെ RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ഇവിടെ LangChain4j-ന്റെ "ഇസിയുടെ RAG" ഉപയോഗiraju. ഡോക്യുമെന്റുകൾ ലോഡ് ചെയ്ത്, സ്വയം വിഭജിച്ച്, ഇൻ-മെമ്മറി സ്റ്റോറിൽ എംബഡ് ചെയ്തുകൊണ്ട്, AI ചോദിച്ചപ്പോൾ ബന്ധപ്പെട്ട ഭാഗങ്ങൾ ലഭിക്കുന്നു. AI പൊതുസമാഹാരം പകരം നിങ്ങളുടെ ഡോക്യുമെന്റുകൾ അടിസ്ഥാനമാക്കി മറുപടി നൽകുന്നു.

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

> **🤖 GitHub Copilot [ചാറ്റുമായി പരീക്ഷിക്കുക](https://github.com/features/copilot):** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) തുറന്ന് ചോദിക്കുക:
> - "AI ഹാളുസിനേഷനുകൾ RAG ഉപയോഗിച്ച് മോഡലിന്റെ ട്രെയിനിംഗ് ഡാറ്റാ ഉപയോഗിക്കുന്നതുമായുള്ള വ്യത്യാസം എങ്ങനെയാണ്?"
> - "ഈ എളുപ്പമായ സമീപനം ഒരു കസ്റ്റം RAG പൈപ്പ്‌ലൈൻനോട് താരതമ്യം ചെയ്‌താൽ എന്ത് വ്യത്യാസം ഉണ്ടാക്കുന്നു?"
> - "പല ഡോക്യുമെന്റുകളും കൂടുതൽ വലിയ ജ്ഞാന വാസ്തവങ്ങളും കൈകാര്യം ചെയ്യാൻ ഇത് എങ്ങനെ സ്കെയിൽ ചെയ്യാം?"

**ഉത്തരവാദിത്വമുള്ള AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

പ്രതിരോധത്തിലൂടെ AI സുരക്ഷ സ്ഥാപിക്കുക. ഈ ഡെമോ രണ്ട് സംരക്ഷണ പാളികൾ ഒന്നിച്ച് പ്രവർത്തിക്കുന്നതാണ്:

**പാര്‍ട്ട് 1: LangChain4j ഇൻപുട്ട് ഗാർഡ്‌റെയിൽസ്** - LLM വരെ എത്തുന്നതിന് മുമ്പെ അപകടകരമായ പ്രോംപ്റ്റുകൾ തടയുക. നിഷേധിച്ചിരിക്കുന്ന കീവേർഡുകളും മാതൃകകളും പരിശോധിക്കുന്ന കസ്റ്റം ഗാർഡ്‌റെയിൽസ് സൃഷ്ടിക്കുക. ഇത് നിങ്ങളുടെ കോഡിൽ പ്രവർത്തിക്കുന്നതിനാൽ വേഗം കൂടിയിരിക്കുന്നു.

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

**പാര്‍ട്ട് 2: പ്രൊവൈഡർ സുരക്ഷാ ഫിൽട്ടറുകൾ** - GitHub Models-ന് ഉൾപ്പെടുത്തിയ ഫിൽട്ടറുകൾ നിങ്ങളുടെ ഗാർഡ്‌റെയിൽസ് കാണാതാക്കുന്നത് പിടിക്കും. ഗുരുതരമായ ലംഘനങ്ങൾക്കുള്ള കഠിന ബ്ലോക്കുകൾ (HTTP 400 പിശക്) കൂടാതെ AI വിനീതമായി നിരാകരിക്കുന്ന സോഫ്റ്റ് നിരാകരണങ്ങളും നിങ്ങൾ കാണും.

> **🤖 GitHub Copilot [ചാറ്റുമായി പരീക്ഷിക്കുക](https://github.com/features/copilot):** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) തുറന്ന് ചോദിക്കുക:
> - "InputGuardrail എന്താണ്, എങ്ങനെ എന്റെ സ്വന്തം ഉണ്ടാക്കാം?"
> - "കഠിന ബ്ലോക്കും സോഫ്റ്റ് നിരാകരണവും തമ്മില有什么 വ്യത്യാസം?"
> - "ഗാർഡ്‌റെയിൽസും പ്രൊവൈഡർ ഫിൽട്ടറുകളും ഒരുമിച്ച് ഉപയോഗിക്കുന്നത് എന്തിനാണ്?"

## അടുത്ത കാര്യങ്ങൾ

**അടുത്ത മോദ്യൂൾ:** [01-പരിചയം - LangChain4j ഉപയോഗിച്ച് തുടങ്ങൽ](../01-introduction/README.md)

---

**നാവിഗേഷൻ:** [← മെയിനിലേക്ക് മടങ്ങുക](../README.md) | [അടുത്തത്: Module 01 - Introduction →](../01-introduction/README.md)

---

## പ്രശ്നനിവാരണങ്ങൾ

### പ്രഥമമായ മേവൻ ബിൽഡ്

**പ്രശ്നം:** ആദ്യ `mvn clean compile` അല്ലെങ്കിൽ `mvn package` വളരെ നീളുന്നു (10-15 മിനിറ്റ്)

**കാരണങ്ങൾ:** മേവന് പ്രോജക്റ്റ് ആശ്രിതവസ്തുക്കൾ (Spring Boot, LangChain4j ലൈബ്രറികൾ, Azure SDK-കൾ മുതലായവ) ആദ്യമായി ഡൗൺലോഡ് ചെയ്യേണ്ടതുണ്ട്.

**പരിഹാരം:** ഇത് അവശ്യമായ പ്രവർത്തനമാണ്. തുടര്‍ന്നുള്ള ബിൽഡുകൾ വേഗമേറിയതാണ്, ആവശ്യമായ ആശ്രിതവസ്തുക്കൾ ലോക്കൽ ആയി കാഷ് ചെയ്യും. ഡൗൺലോഡ് സമയത്ത് നിങ്ങളുടെ നെറ്റ്‌വർക്കിന്റെ വേഗത ആശ്രയിക്കും.

### പവർഷെൽ Maven കമാൻഡ് സിന്റാക്സ്

**പ്രശ്നം:** Maven കമാൻഡുകൾ `Unknown lifecycle phase ".mainClass=..."` എന്ന പിശക് കാണിക്കുന്നു
**കാരണം**: PowerShell `=` നെ വെരിയബിൾ അസൈൻമെന്റ് ഒപറേറ്ററായി വ്യാഖ്യാനിക്കുന്നു, Maven പ്രോപ്പർട്ടി സിന്റാക്സ് വീണ്ടും തകര്‍ക്കുന്നു

**പരിഹാരം**: Maven കമാൻഡിന് മുമ്പ് സ്റ്റോപ്പ്-പാഴ്സിംഗ് ഓപ്പറേറ്റർ `--%` ഉപയോഗിക്കുക:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ഓപ്പറേറ്റർ PowerShell-ന് ബാകിച്ചത് ലിറ്ററലി Maven-ന് നൽകണമെന്നാണ് without വാചക വ്യാഖ്യാനം.

### Windows PowerShell Emoji പ്രദർശനം

**പ്രശ്‌നം**: PowerShell-ൽ ഈമോജികളുടെ പകരം AI പ്രതികരണങ്ങൾ ചീഞ്ഞ കരക്ടറുകളായി കാണുന്നു (ഉദാഹരണം, `????` അല്ലെങ്കിൽ `â??`)

**കാരണം**: PowerShell-ന്റെ ഡിഫോൾട്ട് എൻകോഡിംഗ് UTF-8 ഈമോജികളെ പിന്തുണയ്ക്കുന്നില്ല

**പരിഹാരം**: ജावा ആപ്പ്‌സ് റൺ ചെയ്യുന്നതിന് മുമ്പായി ഈ കമാൻഡ് ഓടിക്കുക:
```cmd
chcp 65001
```

ഇത് ടെർമിനലിൽ UTF-8 എൻകോഡിങ് നിർബന്ധിതമാക്കും. അല്ലെങ്കിൽ, യുണികോഡ് പിന്തുണ മികച്ച Windows ടർമിനൽ ഉപയോഗിക്കുക.

### API കോളുകൾ ഡീബഗ്ഗിംഗ്

**പ്രശ്‌നം**: ഓത്‌മെന്റിക്കേഷൻ പിശകുകൾ, റേറ്റ് ലിമിറ്റുകൾ, അല്ലെങ്കിൽ 예상ത്തിൻറെ പുറത്ത് ആയ AI മോഡൽ പ്രതികരണങ്ങൾ

**പരിഹാരം**: ഉദാഹരണങ്ങളിൽ `.logRequests(true)`യും `.logResponses(true)`യും ഉൾപ്പെടുത്തിയിട്ടുണ്ട്, API കോളുകൾ കൺസോളിൽ കാണിക്കാൻ. ഇത് ഓത്‌മെന്റിക്കേഷൻ പിശകുകൾ, റേറ്റ് ലിമിറ്റുകൾ അല്ലെങ്കിൽ 예상ത്തിൻറെ വക പിശകുകൾ പരിഹരിക്കാൻ സഹായിക്കും. ലോങ് ധ്വനി കുറക്കാൻ പ്രൊഡക്ഷനിൽ ഈ ഫ്ലാഗുകൾ ഒഴിവാക്കുക.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**പരാമർശം**:  
ഈ രേഖ AI വിവർത്തനസേവനമായ [Co-op Translator](https://github.com/Azure/co-op-translator) ഉപയോഗിച്ചാണ് വിവർത്തനം ചെയ്തത്. നാം കാര്യക്ഷമതയ്ക്ക് ശ്രമിക്കുന്നതായിരുന്നാലും, സ്വയംകൃതമായ വിവർത്തനങ്ങളിൽ പിശകുകൾ അല്ലെങ്കിൽ തെറ്റുകൾ ഉണ്ടായേക്കാമെന്നതു ദയവായി ശ്രദ്ധിക്കുക. പ്രാഥമികഭാഷയിൽ ഉള്ള യഥാർത്ഥ രേഖയാണ് വിശ്വസനീയമായ ഉറവിടം. നിർണായക വിവരങ്ങൾക്ക്, പ്രൊഫഷണൽ മനുഷ്യ വിവർത്തനം നിർദ്ദേശിക്കുന്നു. ഈ വിവർത്തനത്തിന് ആശയക്കുഴപ്പങ്ങൾ അല്ലെങ്കിൽ തെറ്റായ വ്യാഖ്യാനങ്ങൾ ഉണ്ടായാലും ഞങ്ങൾ ഉത്തരവാദികളല്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->