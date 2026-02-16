# Module 00: ക്വിക്ക് സ്റ്റാർട്ട്

## Table of Contents

- [പരിചയം](../../../00-quick-start)
- [LangChain4jഎന്ന് എന്താണ്?](../../../00-quick-start)
- [LangChain4j ആശ്രിതങ്ങൾ](../../../00-quick-start)
- [പൂര്‍വാവശ്യങ്ങള്‍](../../../00-quick-start)
- [സജ്ജീകരണം](../../../00-quick-start)
  - [1. നിങ്ങളുടെ GitHub ടോകൻ ലഭിക്കുക](../../../00-quick-start)
  - [2. നിങ്ങളുടെ ടോകൺ സെറ്റ് ചെയ്യുക](../../../00-quick-start)
- [ഉദാഹരണങ്ങൾ പ്രവർത്തിപ്പിക്കുക](../../../00-quick-start)
  - [1. അടിസ്ഥാന ചാറ്റ്](../../../00-quick-start)
  - [2. പ്രോംപ്റ്റ് മാതൃകകൾ](../../../00-quick-start)
  - [3. ഫംഗ്ഷൻ کالിങ്ങ്](../../../00-quick-start)
  - [4. ഡോക്യുമെന്റ് ചോദ്യോത്തരങ്ങൾ (RAG)](../../../00-quick-start)
  - [5. ഉത്തരവാദിത്വമുള്ള AI](../../../00-quick-start)
- [ഏത് ഉദാഹരണം എന്ത് കാണിക്കുന്നു](../../../00-quick-start)
- [അടുത്ത ഘട്ടങ്ങൾ](../../../00-quick-start)
- [സമസ്യപരിഹാരം](../../../00-quick-start)

## പരിചയം

LangChain4j-യുമായി അപകടത്തിലാതെ വേഗത്തിൽ തുടങ്ങാനായി ഈ ക്വിക്ക് സ്റ്റാർട്ട് ഉദ്ദേശിച്ചിരിക്കുന്നു. ഇത് LangChain4j-ഉം GitHub മോഡലുമുള്ള AI ആപ്ലിക്കേഷനുകൾ നിർമ്മിക്കുന്ന അടിസ്ഥാന കാര്യങ്ങൾ തന്നെ ഉൾക്കൊള്ളുന്നു. അടുത്ത മൊഡ്യൂളുകളിൽ, നിങ്ങൾ LangChain4j-ന്റെ സഹായത്തോടെ Azure OpenAI ഉപയോഗിച്ച് കൂടുതൽ വികസിത ആപ്ലിക്കേഷനുകൾ നിർമ്മിക്കും.

## LangChain4jഎന്ന് എന്താണ്?

LangChain4j ഒരു ജാവ ലൈബ്രറിയാണ്, AI-ശക്തിപെടുത്തിയ ആപ്ലിക്കേഷനുകൾ സൃഷ്ടിക്കുന്നതിനെ ലളിതമാക്കുന്നത്. HTTP ക്ളയന്റുകളും JSON പാഴ്സിംഗും കൈകാര്യം ചെയ്യുന്നതിന് പകരം, നിങ്ങൾ ശുദ്ധമായ ജാവ API-കളുമായാണ് പ്രവർത്തിക്കുക.

LangChain എന്ന പദം പല ഘടകങ്ങളും ചേർത്ത് ഒരു ക_chain_ രൂപപ്പെടുത്തുന്നതിന് സൂചിപ്പിക്കുന്നു - നിങ്ങൾ ഒരു പ്രോംപ്റ്റ് ഒരു മോഡലിലേക്ക്, പിന്നീട് ഒരു പാഴ്സറിലേക്കോ, അല്ലെങ്കിൽ ഒരുപാട് AI വിളികൾ തമ്മിൽ ക_chain_ ചെയ്ത് ഒരതിന്റെ ഔട്ട്പുട്ട് അടുത്ത ഇൻപുട്ടായി ഉപയോഗിക്കുകയോ ചെയ്യാം. ഈ ക്വിക്ക് സ്റ്റാർട്ട് അടിസ്ഥാനമേഖലകളെ കേന്ദ്രീകരിക്കുന്നു, അതിനു ശേഷം കൂടുതൽ സങ്കീർണ്ണമായ ക_chain_കൾ പരിശോധിക്കും.

<img src="../../../translated_images/ml/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j-ൽ ഘടകങ്ങൾ ക_chain_ ചെയ്യുന്നത് - ശക്തമായ AI പ്രവർത്തനങ്ങൾ സൃഷ്ടിക്കാൻ നിര്‍മ്മാണഘടകങ്ങൾ ബന്ധിപ്പിക്കുന്നു*

നാം മൂന്ന് പ്രധാന ഘടകങ്ങൾ ഉപയോഗിക്കും:

**ChatLanguageModel** - AI മോഡലുമായുള്ള ഇടപെടലുകൾക്കുള്ള ഇന്റർഫേസ്. `model.chat("prompt")` എന്ന് വിളിച്ച് ഒരു പ്രതികരണ സ്ട്രിംഗ് നേടാം. ഞങ്ങൾ `OpenAiOfficialChatModel` ഉപയോഗിക്കുന്നു, 이는 GitHub Models പോലുള്ള OpenAI-സമാനമുള്ള എന്റർ‌പോയിന്റുകളുമായി പ്രവർത്തിക്കുന്നു.

**AiServices** - തരംസുരക്ഷയുള്ള AI സർവീസ് ഇന്റർഫെയ്‌സുകളെ സൃഷ്ടിക്കുന്നു.  രീതികൾ നിർവചിച്ച്, അവ @Tool ആയി അടയാളപ്പെടുത്താം, LangChain4j ഓർക്കസ്ട്രേഷൻ കൈകാര്യം ചെയ്യും. ആവശ്യമായപ്പോൾ AI സ്വയം നിങ്ങളുടെ ജാവ രീതികൾ വിളിക്കുന്നു.

**MessageWindowChatMemory** - സംഭാഷണ ചരിത്രം പരിരക്ഷിക്കുന്നു. ഇതു ഇല്ലാതെ, ഓരോ അഭ്യർത്ഥനയും സ്വതന്ത്രമാണ്. ഇത് ഉണ്ടായാൽ, AI മുൻപത്തെ സന്ദേശങ്ങൾ ഓർക്കുകയും പല തവണകളിലും പശ്ചാത്തലം നിലനിര്‍ത്തുകയും ചെയ്യും.

<img src="../../../translated_images/ml/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j ഘടന - AI ആപ്ലിക്കേഷനുകൾക്ക് ശക്തി നൽകുന്ന കോർ ഘടകങ്ങൾ*

## LangChain4j ആശ്രിതങ്ങൾ

ഈ ക്വിക്ക് സ്റ്റാർട്ട് [`pom.xml`](../../../00-quick-start/pom.xml) ലെ രണ്ട് Maven ആശ്രിതങ്ങൾ ഉപയോഗിക്കുന്നു:

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

`langchain4j-open-ai-official` മോഡ്യൂൾ OpenAiOfficialChatModel ക്ലാസ് നൽകുന്നു, അത് OpenAI-സമാന API-കളിലേക്ക് ബന്ധിപ്പിക്കുന്നു. GitHub മോഡലും അതേ API ഫോർമാറ്റ് ഉപയോഗിക്കുന്നു, അതിനാൽ പ്രത്യേക അഡാപ്റ്റർ ആവശ്യമില്ല - താഴെ നൽകിയിട്ടുള്ള അടിസ്ഥാന URL-ന് `https://models.github.ai/inference` പോയിന്റ് ചെയ്യുക.

## പൂര്‍വാവശ്യങ്ങള്‍

**ഡെവ് കണ്ടെയ്‌നർ ഉപയോഗിക്കുകയാണോ?** Javaയും Mavenയും ഇതിനകം സ്ഥാപിച്ചിരിക്കുന്നു. GitHub Personal Access Token മാത്രം ആവശ്യമാണ്.

**സ്ഥലീയ വികസനം:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (നിർദ്ദേശങ്ങൾ താഴെ)

> **ശ്രദ്ധിക്കുക:** ഈ മൊഡ്യൂൾ GitHub മോഡലിൽ നിന്നുള്ള `gpt-4.1-nano` ഉപയോഗിക്കുന്നു. കോഡിലെ മോഡൽ നാമം മാറ്റരുത് - GitHub-ന്റെ ലഭ്യമായ മോഡലുകളോടു മാത്രമേ ഇത് പ്രവർത്തിക്കൂ.

## സജ്ജീകരണം

### 1. നിങ്ങളുടെ GitHub ടോകൻ ലഭിക്കുക

1. [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens) സന്ദർശിക്കുക
2. "Generate new token" ക്ലിക്ക് ചെയ്യുക
3. വിവരണപരമായ പേര് നൽകുക (ഉദാ: "LangChain4j Demo")
4. ആസൂത്രിത കാലാവധി (7 ദിവസം ശുപാർശ)
5. "Account permissions" ൽ നിന്ന് "Models" "Read-only" ആക്കി സെറ്റ് ചെയ്യുക
6. "Generate token" ക്ലിക്ക് ചെയ്യുക
7. ടോകൺ കോപ്പിയെടുത്ത് സൂക്ഷിക്കുക - വീണ്ടും കാണാൻ സാധിക്കില്ല

### 2. നിങ്ങളുടെ ടോകൺ സെറ്റ് ചെയ്യുക

**ഓപ്ഷൻ 1: VS Code ഉപയോഗിക്കുക (ശുപാർശ)**

നിങ്ങൾ VS Code ഉപയോഗിക്കുന്നുവെങ്കിൽ, പ്രോജക്ട് റൂട്ടിൽ `.env` ഫയൽക്ക് നിങ്ങളുടെ ടോകൺ ചേർക്കുക:

`.env` ഫയൽ ഇല്ലെങ്കിൽ, `.env.example` കോപ്പി ചെയ്ത് `.env` ആക്കുക അല്ലെങ്കിൽ പുതിയ `.env` ഫയൽ സൃഷ്ടിക്കുക.

**ഉദാഹരണ `.env` ഫയൽ:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env ൽ
GITHUB_TOKEN=your_token_here
```

ശേഷം എഡിറ്ററിലെ ഏതെങ്കിലും ഡെമോ ഫയലിൽ (ഉദാ: `BasicChatDemo.java`) റൈറ്റ് ക്ലിക്ക് ചെയ്ത് **"Run Java"** തിരഞ്ഞെടുക്കാം അല്ലെങ്കിൽ Run and Debug പാനലിൽ നിന്നുള്ള ലോഞ്ച് കോൺഫിഗറേഷനുകൾ ഉപയോഗിക്കാം.

**ഓപ്ഷൻ 2: ടെർമിനൽ ഉപയോഗിക്കുക**

ടോകൺ എൻവയോൺമെന്റ് വേരിയബിൾ ആയി സെറ്റ് ചെയ്യുക:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## ഉദാഹരണങ്ങൾ പ്രവർത്തിപ്പിക്കുക

**VS Code ഉപയോഗിച്ച്:** എളുപ്പത്തിൽ എക്‌സ്‌പ്ലോററിൽ ഏതെങ്കിലും ഡെമോ ഫയലിൽ റൈറ്റ് ക്ലിക്ക് ചെയ്ത് **"Run Java"** തിരഞ്ഞടുക്കുക, അല്ലെങ്കിൽ Run and Debug പാനലിൽ നിന്നുള്ള ലോഞ്ച് കോൺഫിഗറേഷനുകൾ ഉപയോഗിക്കുക (ആദ്യം .env ഫയലിൽ ടോകൺ ചേർക്കുന്നത് ഉറപ്പാക്കുക).

**Maven ഉപയോഗിച്ച്:** അല്ലെങ്കിൽ കമാൻഡ് ലൈൻ വഴി പ്രവർത്തിപ്പിക്കാം:

### 1. അടിസ്ഥാന ചാറ്റ്

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. പ്രോംപ്റ്റ് മാതൃകകൾ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

സീറോ-ഷോട്ട്, Few-shot, ചൈൻ-ഓഫ്-തോട്ട്, റോൾ-ബേസ് ചെയ്ത പ്രോംപ്റ്റിങ്ങുകൾ കാണിക്കുന്നു.

### 3. ഫംഗ്ഷൻ کالിങ്

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI ആവശ്യമായപ്പോൾ നിങ്ങളുടെ ജാവ രീതി സ്വയം വിളിക്കുന്നു.

### 4. ഡോക്യുമെന്റ് Q&A (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt` ലെ ഉള്ളടക്കത്തെക്കുറിച്ച് ചോദ്യങ്ങൾ ചോദിക്കുക.

### 5. ഉത്തരവാദിത്വമുള്ള AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI സുരക്ഷാ ഫിൽറ്ററുകൾ എങ്ങനെ ദുഷ്പ്രവൃത്തി തടയുന്നു എന്ന് കാണുക.

## ഏത് ഉദാഹരണം എന്ത് കാണിക്കുന്നു

**അടിസ്ഥാന ചാറ്റ്** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j-യെ ഏറ്റവും ലളിതമായി കാണാനാവുമ്പോൾ ഇവിടെ തുടങ്ങിയേക്കുക. `OpenAiOfficialChatModel` സൃഷ്ടിച്ച് `.chat()` കൊണ്ട് പ്രോംപ്റ്റ് അയച്ച് മറുപടി ലഭിക്കും. മോഡലുകളെ കസ്റ്റം എന്റർപോയിന്റുകളും API കീകളും ഉപയോഗിച്ച് എങ്ങനെ ആരംഭിക്കാമെന്ന് ഇതു കാണിക്കുന്നു. ഈ മാതൃക മനസിലാക്കി കഴിഞ്ഞാൽ, മറ്റെല്ലാം ഇതിൽനിന്നും വികസിക്കും.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ചാറ്റുമായി പരീക്ഷിക്കുക:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) തുറന്ന് ചോദിക്കൂ:  
> - "GitHub Models നിന്നു Azure OpenAI-യിലേക്ക് ഈ കോഡ് എങ്ങനെ മാറ്റാം?"  
> - "OpenAiOfficialChatModel.builder()-ൽ മറ്റേതെല്ലാമ парамет്രുകൾ കോൺഫിഗർ ചെയ്യാമു?"  
> - "പൂർണ്ണ മറുപടി വരുന്നതുവരെ കാത്തിരിക്കാൻ പകരം സ്റ്റ്രീമിങ് പ്രതികരണങ്ങൾ എങ്ങനെ ചേർക്കാം?"

**പ്രോംപ്റ്റ് എഞ്ചിനീയറിങ്** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

ഇപ്പോൾ നിങ്ങൾ മോഡലിനോടു സംസാരിക്കാൻ അറിയുമ്പോൾ, നിങ്ങൾ അതിനു നൽകുന്ന കാര്യങ്ങളും നോക്കാം. ഈ ഡെമോ ഒരേ മോഡൽ സെറ്റപ്പുപയോഗിച്ച് അഞ്ച് വ്യത്യസ്ത പ്രോംപ്റ്റ് മാസികകൾ കാണിക്കുന്നു. നേരിട്ട് നിർദ്ദേശങ്ങൾക്കായി സീറോ-ഷോട്ട്, ഉദാഹരണങ്ങളിൽ നിന്നു പഠിക്കുന്ന few-shot, ചിന്തയുടെ ഘട്ടം ചുരുളിക്കുന്ന chain-of-thought, സുവിശേഷം ക്രമീകരിക്കുന്ന role-based പ്രോംപ്റ്റുകൾ പരീക്ഷിക്കുക. നിങ്ങൾ ആവശ്യപ്പ് പ്രകാരം ഫ്രെയ്മ് ചെയ്ത രീതിയനുസരിച്ച് ഒരേ മോഡൽ വിവിധ ഫലങ്ങൾ നൽകുന്നു.

പ്രോംപ്റ്റ് ടെമ്പ്ലേറ്റുകൾ മൂലം പുനരുപയോഗിക്കാവുന്ന പ്രോംപ്റ്റുകൾ സൃഷ്ടിക്കാൻ എളുപ്പമാണ്. താഴെ LangChain4j `PromptTemplate`-ഉപയോഗിച്ച് വൈവിധ്യമാർന്ന വേരിയബിൾ നിറക്കുന്നതിന്റെ ഒരു ഉദാഹരണം കാണിക്കുന്നു. AI നൽകിയ ഗമനസ്ഥലവും പ്രവർത്തനവും അടിസ്ഥാനമാക്കി മറുപടി നൽകും.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ചാറ്റുമായി പരീക്ഷിക്കുക:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) തുറന്ന് ചോദിക്കൂ:  
> - "സീറോ-ഷോട്ട്, few-shot പ്രോംപ്റ്റിങ്ങിനുള്ള വ്യത്യാസങ്ങൾ എന്തൊക്കെ, എപ്പോൾ ഏത് ഉപയോഗിക്കണം?"  
> - "താപനില (temperature) parametർ മോഡലിന്റെ പ്രതികരണങ്ങളെ എങ്ങനെ സ്വാധീനിക്കുന്നു?"  
> - "ഉത്പാദനത്തിൽ പ്രോംപ്റ്റ് ഇൻജക്ഷൻ ആക്രമണങ്ങൾ തടയാൻ ചില സാങ്കേതിക വിദ്യകൾ?"  
> - "പുതിയ പ്രോംപ്റ്റ് ഫോർമാറ്റുകൾക്ക് പുനരുപയോഗാവുന്ന PromptTemplate ഒബ്ജക്റ്റുകൾ എങ്ങനെ സൃഷ്ടിക്കാം?"

**ടൂൾ ഇന്റഗ്രേഷൻ** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

LangChain4j ഇവിടെ ശക്തി കാണിക്കുന്നു. നിങ്ങളുടെ ജാവ രീതികൾ വിളിക്കാൻ കഴിവുള്ള AI അസിസ്റ്റന്റ് `AiServices` ഉപയോഗിച്ച് സൃഷ്ടിക്കും. @Tool("വിവരണം") ഉപയോഗിച്ച് രാജ്യങ്ങൾ അടയാളപ്പെടുത്തിയാൽ LangChain4j ബാക്കിയെ കൈകാര്യം ചെയ്യും - ഉപഭോക്താവ് ചോദിക്കുന്നതിനെ അടിസ്ഥാനമാക്കി AI സ്വയം ഏത് ടൂൾ ഉപയോഗിക്കണമെന്ന് തീരുമാനിക്കും. ഇത് ഫംഗ്ഷൻ കോളിംഗ് കാണിക്കുന്നു, ചോദ്യങ്ങൾക്ക് മറുപടിയല്ല, പ്രവർത്തനങ്ങൾ കൈകാര്യം ചെയ്യുന്ന AI നിർമ്മാണത്തിന് ഗുണകരമാണ്.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ചാറ്റുമായി പരീക്ഷിക്കുക:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) തുറന്ന് ചോദിക്കൂ:  
> - "@Tool അനോട്ടേഷൻ എങ്ങനെ പ്രവർത്തിക്കുന്നു? LangChain4j ഇതുമായി പിന്നിലെങ്കിൽ എന്താണ് ചെയ്യുന്നത്?"  
> - "AI സങ്കീർണ്ണ പ്രശ്‌നങ്ങൾ പരിഹരിക്കാൻ അനേകം ടൂൾകൾ തുടർച്ചയായി വിളിക്കാമോ?"  
> - "ടൂൾ എഡീഷനിൽ ഒരു എക്‌സപ്ഷൻ ഉണ്ടാകുമ്പോൾ എങ്ങനെ കൈകാര്യംചെയ്യണം?"  
> - "കാൽക്കുലേറ്റർ ഉദാഹരണത്തിന് പകരം യഥാർത്ഥ API എങ്ങനെ ഇന്റഗ്രേറ്റ് ചെയ്യും?"

**ഡോക്യുമെന്റ് Q&A (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ഇവിടെ RAG (retrieval-augmented generation) ന്റെ അടിസ്ഥാനങ്ങൾ കാണാം. മോഡലിന്റെ ട്രെയ്‌നിംഗ് ഡാറ്റയിൽ ആശ്രയിക്കുന്നതിന് പകരം, [`document.txt`](../../../00-quick-start/document.txt) ലെ ഉള്ളടക്കം ലോഡ് ചെയ്ത് പ്രോംപ്റ്റിൽ ഉൾപ്പെടുത്തുന്നു. AI നിങ്ങളുടെ ഡോക്യുമെന്റിന്റെ അടിസ്ഥാനത്തിൽ മറുപടി നൽകും, സാധാരണ ജെനറൽ അറിവിൽ അല്ല. നിങ്ങളുടെ സ്വന്തം ഡാറ്റ ഉപയോഗിച്ചു പ്രവർത്തിക്കുന്ന സിസ്റ്റങ്ങൾ നിർമ്മിക്കാനുള്ള ആദ്യപടി ഇത്.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **ശ്രദ്ധിക്കുക:** ഈ ലളിതമായ സമീപനം മുഴുവനായ ഡോക്യുമെന്റ് പ്രോംപ്റ്റിലേക്ക് ലോഡ് ചെയ്യുന്നു. വലുതായ ഫയലുകൾ (>10KB) ഉപയോഗിക്കുമ്പോൾ കോൺടെക്സ്റ്റ് പരിധി മുടിയാം. മോഡ്യൂൾ 03 chunking കൂടാതെ vector search ഉപയോഗിച്ച് പ്രൊഡക്ഷൻ RAG സിസ്റ്റങ്ങൾക്ക് വിശദീകരിക്കുന്നു.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ചാറ്റുമായി പരീക്ഷിക്കുക:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) തുറന്ന് ചോദിക്കൂ:  
> - "RAG AI ഹാള്യൂസിൻസ് തടയുന്നതിൽ മോഡലിന്റെ ട്രെയിനിംഗ് ഡാറ്റ ഉപയോഗിക്കുന്നതിൽ നിന്നും എന്ത് വ്യത്യാസമുണ്ട്?"  
> - "ഈ ലളിതമായ സമീപനത്തിലും വെക്ടർ എംബഡിങ്ങുകൾ ഉപയോഗിച്ചുള്ള വീതിയിലും വ്യത്യാസങ്ങൾ എന്തെല്ലാം?"  
> - "ഒരു സമയത്ത് ഒരുപാളി അല്ലെങ്കിൽ വലിയ നോളേജ് ബേസുകൾ മാനേജുചെയ്യാൻ ഇത് എങ്ങനെ സ്കെയിൽ ചെയ്യും?"  
> - "AI പ്രോംപ്റ്റിൽ നൽകിയിരിക്കുന്ന കോൺടെക്സ്റ്റ് മാത്രമേ ഉപയോഗിക്കേണ്ടതെന്ന് ഉറപ്പാക്കാൻ പ്രോംപ്റ്റ് എങ്ങനെ ക്രമീകരിക്കാം?"

**ഉത്തരവാദിത്തമുള്ള AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ഗൂഢപരിരക്ഷ കൂടിയ AI സുരക്ഷ നിർമ്മിക്കുക. ഈ ഡെമോ രണ്ട് ലെയറുകളുള്ള പ്രതിരോധം കാണിക്കുന്നു:

**ഭാഗം 1: LangChain4j ഇൻപുട്ട് ഗാർഡ്‌రെയിൽസ്** - അപകടകാരിയായ പ്രോംപ്റ്റുകൾ LLM-വരെ എത്തിക്കാൻ മുമ്പ് തടയുക. നിരോധിത കീവേഡുകളും മാതൃകകളും പരിശോധിക്കുന്ന കസ്റ്റം ഗാർഡ്‌റെയിൽസ് സൃഷ്ടിക്കുക. ഇത്തരം കോഡ് നിങ്ങളുടെ കോഡിൽ പ്രവർത്തിക്കുന്നതിനാൽ വേഗം കൂടിയതും സൗജന്യവുമാണ്.

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

**ഭാഗം 2: പ്രൊവൈഡർ സുരക്ഷാ ഫിൽറ്ററുകൾ** - GitHub മോഡലുകൾ ഗാർഡ്‌റെയിൽസ് മറന്നേക്കാവുന്ന കാര്യങ്ങൾ പിടികൂടുന്ന ഇൻബിൽട്ട് ഫിൽറ്ററുകൾ ഉൾക്കൊള്ളുന്നു. ഗൗരവമായ ലംഘനങ്ങൾക്ക് ഹാർഡ് ബ്ലോക്കുകൾ (HTTP 400 പിശക്) കാണിയും, സോഫ്റ്റ് നിരാകരണങ്ങളിലും AI മൃദുവായി തള്ളുന്നതും കാണാം.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) ചാറ്റുമായി പരീക്ഷിക്കുക:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) തുറന്ന് ചോദിക്കൂ:  
> - "InputGuardrail എന്താണ്, എങ്ങനെ എന്റെ സ്വന്തമായത് സൃഷ്ടിക്കാം?"  
> - "हार्ड്ബ്ലോക്കിനും സോഫ്റ്റ് നിരാകരണത്തിനും തമ്മിലുള്ള വ്യത്യാസം എന്താണ്?"  
> - "ഗാർഡ്‌റെയിലുകളും പ്രൊവൈഡർ ഫിൽറ്ററുകളും ഒരുമിച്ച് ഉപയോഗിക്കേണ്ടത് എന്തുകൊണ്ടാണ്?"

## അടുത്ത ഘട്ടങ്ങൾ

**അടുത്ത മൊഡ്യൂൾ:** [01-introduction - LangChain4j-യും gpt-5-ഉം Azure-ൽ ആരംഭിക്കൽ](../01-introduction/README.md)

---

**നവിഗേഷൻ:** [← പ്രധാനത്തിലേക്ക് തിരിച്ചുപോകം](../README.md) | [അടുത്തത്: Module 01 - പരിചയം →](../01-introduction/README.md)

---

## സമസ്യപരിഹാരം

### ആദ്യമായുള്ള Maven നിർമ്മാണം

**പ്രശ്നം**: ആദ്യം `mvn clean compile` അല്ലെങ്കിൽ `mvn package` വളരെ സമയം എടുക്കുന്നു (10-15 മിനിറ്റ്)

**കാരണം**: Maven ആദി നിര്‍മાણത്തിൽ എല്ലാ പ്രോജക്ട് ആശ്രിതങ്ങളും (Spring Boot, LangChain4j ലൈബ്രറികൾ, Azure SDKകൾ മുതലായവ) ഡൗൺലോഡ് ചെയ്യണം.

**പരിഹാരം**: ഇത് സാധാരണമാണ്. പിന്നീട് নির্মാണങ്ങൾ വളരെ വേഗത്തിൽ നടക്കും, കാരണം ആശ്രിതങ്ങൾ ലൊക്കലായി ക്യാഷ് ചെയ്യപ്പെടും. ഡൗൺലോഡ് സമയം നിങ്ങളുടെ നെറ്റ്‌വർക്ക് സ്പീഡിനെ ആശ്രയിച്ചിരിക്കും.
### PowerShell Maven കമാൻഡ് സിന്താക്സ്

**പ്രശ്നം**: Maven കമാൻഡുകൾ `Unknown lifecycle phase ".mainClass=..."` എന്ന പിഴവോടെ പരാജയപ്പെടുന്നു

**കാരണവും**: PowerShell `=` എന്നത് ഒരു വേരിയബിൾ അസൈൻമെന്റ് ഓപ്പറേറ്ററായി വ്യാഖ്യാനം ചെയ്യുന്നത്, Maven ഗുണഭാഗ സിന്താക്സ് തകരാറിലാക്കുന്നു

**പരിഹാരം**: Maven കമാൻഡിന് മുമ്പ് സ്റ്റോപ്പ്-പാർസിംഗ് ഓപ്പറേറ്റർ `--%` ഉപയോഗിക്കുക:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ഓപ്പറേറ്റർ PowerShell-ന് ബാക്കിയുള്ള എല്ലാ аргുമെന്റ്‌സ് അർത്ഥ വ്യാഖ്യാനം ഇല്ലാതെ കണ്ടെത്തൽ പൊതു Maven-ലേക്ക് നല്കാൻ নির্দেশിക്കുന്നു.

### Windows PowerShell ഇമോജി പ്രദർശനം

**പ്രശ്നം**: PowerShell-ൽ AI മറുപടികൾ എമോജികളുടെ പകരം (ഉദാഹരണം, `????` അല്ലെങ്കിൽ `â??`) പൊടുപേക്ഷികളായി കാണിക്കുന്നു

**കാരണവും**: PowerShellന്റെ ഡിഫോൾട്ട് എൻകോഡിംഗ് UTF-8 എമോജികൾ പിന്തുണയ്ക്കുന്നില്ല

**പരിഹാരം**: ജാവ ആപ്ലിക്കേഷനുകൾ പ്രവർത്തിപ്പിക്കുന്നതിനുമുമ്പ് ഈ കമാൻഡ് പ്രവർത്തിപ്പിക്കുക:
```cmd
chcp 65001
```

ഇത് ടെർമിനലിൽ UTF-8 എൻകോഡിംഗ് നിർബന്ധമാകും. മറ്റൊരു രീതിയിൽ, பிடി കൂടുതൽ യൂണിക്കോഡ് പിന്തുണയുള്ള Windows Terminal ഉപയോഗിക്കുക.

### API കോളുകൾ ഡീബഗ്ഗിങ്

**പ്രശ്നം**: ഓഥന്റിക്കേഷൻ പിഴവുകൾ, നിരക്ക് പരിധികൾ, അല്ലെങ്കിൽ പ്രതീക്ഷിക്കാത്ത മറുപടികൾ AI മോഡലിൽ നിന്ന്

**പരിഹാരം**: ഉദാഹരണം `.logRequests(true)` һәм `.logResponses(true)` ഉൾപ്പെടുത്തിയിരിക്കുന്നു, API കോളുകൾ കോൺസോളിൽ കാണുന്നതിനായി. ഇത് ഓഥന്റിക്കേഷൻ പിഴവുകൾ, നിരക്ക് പരിധികൾ, അല്ലെങ്കിൽ അനായാസമായ മറുപടികൾ കണ്ടെത്താൻ സഹായിക്കുന്നു. ഉത്പാദനത്തിൽ ഈ ഫ്ലാഗുകൾ നീക്കം ചെയ്ത് লগ് ശബ്ദം കുറയ്ക്കാം.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**അസാധാരണ തീരുമാനം**:  
ഈ രേഖ [Co-op Translator](https://github.com/Azure/co-op-translator) എന്ന AI പരിഭാഷ സേവനം ഉപയോഗിച്ച് പരിഭാഷപ്പെടുത്തിയതാണ്. നാം ശുദ്ധതയ്ക്കായി പരിശ്രമിക്കുന്നുവെങ്കിലും, ഓട്ടോമേറ്റഡ് പരിഭാഷകളിൽ പിശകുകളും അദൃശ്യതകളും ഉണ്ടാകാമെന്ന് ദയവായി ശ്രദ്ധിക്കുക. പ്രാമാണിക ഉറവിടമായി മാതൃഭാഷയിലെ യഥാർത്ഥ രേഖ പരിഗണിക്കേണ്ടതാണ്. നിർണായക വിവരങ്ങൾക്കായി, പ്രൊഫഷണൽ മനുഷ്യ പരിഭാഷ ഉപദേശിക്കുന്നു. ഈ പരിഭാഷയുടെ ഉപയോഗം വഴി ഉണ്ടാകുന്ന ഏതൊരു തെറ്റായ മനസിലാക്കലുകൾക്കും അല്ലെങ്കിൽ വ്യാഖ്യാനക്കൊള്ളലുകൾക്കും ഞങ്ങൾ ഉത്തരവാദിത്വം വഹിക്കുന്നില്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->