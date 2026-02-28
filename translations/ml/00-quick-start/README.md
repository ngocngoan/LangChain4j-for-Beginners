# മോഡ്യൂള്‍ 00: ക്വിക്ക് സ്റ്റാര്‍ട്ട്

## ഉള്ളടക്കം പട്ടിക

- [പരിചയം](../../../00-quick-start)
- [LangChain4j എന്നത് എന്താണ്?](../../../00-quick-start)
- [LangChain4j ആശ്രിതങ്ങള്‍](../../../00-quick-start)
- [മുൻ‌വിധികൾ](../../../00-quick-start)
- [സജ്ജീകരണം](../../../00-quick-start)
  - [1. നിങ്ങളുടെ GitHub ടോക്കൺ നേടുക](../../../00-quick-start)
  - [2. നിങ്ങളുടെ ടോക്കൺ സജ്ജമാക്കുക](../../../00-quick-start)
- [ഉദാഹരണങ്ങൾ നടത്തുക](../../../00-quick-start)
  - [1. അടിസ്ഥാന സംഭാഷണം](../../../00-quick-start)
  - [2. പ്രോംപ്റ്റ് ഡ്രശ്യങ്ങൾ](../../../00-quick-start)
  - [3. ഫംഗ്ഷൻ വിളിക്കൽ](../../../00-quick-start)
  - [4. ഡോക്യുമെന്റ് ചോദ്യോത്തരങ്ങൾ (ഇസി RAG)](../../../00-quick-start)
  - [5. ഉത്തരവാദിത്വമുള്ള AI](../../../00-quick-start)
- [ഒരു ഉദാഹരണം ഓരോന്നും എന്ത് കാണിക്കുന്നു](../../../00-quick-start)
- [അടിയെഴുത്തുകൾ](../../../00-quick-start)
- [പ്രശ്‌നം പരിഹാരം](../../../00-quick-start)

## പരിചയം

ഈ ക്വിക്ക്‌സ്റ്റാർട്ട് LangChain4j ഉപയോഗിച്ച് möglichst വിഹിതം സമയത്തിനുള്ളിൽ നിങ്ങൾക്ക് ആരംഭിക്കുവാനാണ് ഉദ്ധിഷ്ടം. ഇത് LangChain4j-ഉം GitHub മോഡലുകളുമായി AI ആപ്ലിക്കേഷനുകൾ നിർമ്മിക്കുന്നതിന്റെ അടിസ്ഥാന കാര്യങ്ങൾ ഉൾക്കൊള്ളുന്നു. അടുത്ത മോഡ്യൂളുകളിൽ നിങ്ങൾ Azure OpenAI LangChain4j-ഉം ഉപയോഗിച്ച് കൂടുതല്‍ സങ്കീർണ ആപ്ലിക്കേഷനുകൾ നിർമ്മിക്കും.

## LangChain4j എന്നാൽ എന്ത്?

LangChain4j ഒരു ജാവ ലൈബ്രറിയാണ്, AI-ബൈജച്ചാണ് ആപ്ലിക്കേഷനുകൾ നിർമ്മിക്കുവാൻ സുഗമമാക്കുന്നത്. HTTP ക്ലയന്റുകളുമായി JSON പാഴ്സിങ് ചെയ്യുന്നതിന് പകരം, നിങ്ങൾ ശുദ്ധമായ ജാവ API-കളുമായി പ്രവർത്തിക്കുന്നു.

LangChain中的 "chain" അതായത് നിരവധി ഘടകങ്ങൾ ബന്ധിപ്പിക്കൽ — നിങ്ങൾ ഒരു പ്രോംപ്റ്റ് ഒരു മോഡലിലേക്ക്, അപ്പോട് ഒരു പാഴ്സറിലേക്കോ, അഥവാ ഒരേ സമയം നിരവധി AI വിളികൾ തമ്മിൽ ബന്ധിപ്പിക്കൽ, ഒന്ന് തരും ഫലം അടുത്ത ഇൻപുട്ടായി ഉപയോഗിക്കുന്നത്. ഈ ക്വിക്ക് സ്റ്റാർട്ട് അടിസ്ഥാനങ്ങൾക്കു മുൻപ് സങ്കീർണ്ണമായ ചെയിനുകളിലേക്കുള്ള അന്വേഷണം ചെയ്യുന്നു.

<img src="../../../translated_images/ml/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j വിൽ ഘടകങ്ങൾ ബന്ധിപ്പിക്കൽ - ശക്തമായ AI പ്രവാഹങ്ങൾ സൃഷ്ടിക്കാൻ ബ്ലോക്ക്‌കൾ ബന്ധിപ്പിക്കുന്നു*

നാം മൂന്ന് പ്രധാന ഘടകങ്ങൾ ഉപയോഗിക്കും:

**ChatModel** - AI മോഡൽ ഇന്ററാക്ഷനുകൾക്കുള്ള ഇന്റർഫേസ്. `model.chat("prompt")` വിളിച്ച് പ്രതികരണമായ ഒരു ടെക്സ്റ്റ് സർവീസ് ലഭിക്കുന്നു. GitHub മോഡലുകളുമായുള്ള OpenAI അനുയോജ്യ എൻഡ്പോയിന്റുകളുമായി പ്രവർത്തിക്കുന്ന `OpenAiOfficialChatModel` ആണ് ഉപയോഗിക്കുന്നത്.

**AiServices** - ടൈപ്പ്-സേഫ് AI സർവീസ് ഇന്ററ്ഫേസുകൾ സൃഷ്ടിക്കുന്നു. പദ്ദതികൾ നിർവ്വചിച്ച് അവയ്ക്ക് `@Tool` കുറിച്ച്, LangChain4j ഓർക്കസ്ട്രേഷനു ഉത്തരവാദിയാണ്. ആവശ്യമായപ്പോൾ AI നിങ്ങളുടെ ജാവ നടപടികൾ സ്വയം വിളിക്കുന്നു.

**MessageWindowChatMemory** - സംഭാഷണ ചരിത്രം സൂക്ഷിക്കുന്നു. ഇത് ഇല്ലെങ്കിൽ ഓരോ അഭ്യർത്ഥനയും സ്വതന്ത്രമാണ്. ഇതോടെ AI മുമ്പത്തെ സന്ദേശങ്ങൾ ഓർക്കുകയും പല തവണ സംഭാഷണത്തിന്റെ പരിപ്രേക്ഷ്യം സൂക്ഷിക്കുകയും ചെയ്യുന്നു.

<img src="../../../translated_images/ml/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j ഘടന - നിങ്ങളുടെ AI ആപ്ലിക്കേഷനുകൾക്ക് ശക്തി പകരുന്ന പ്രധാന ഘടകങ്ങൾ*

## LangChain4j ആശ്രിതങ്ങൾ

ഈ ക്വിക്ക് സ്റ്റാർട്ട് മൂന്ന് Maven ആശ്രിതങ്ങൾ [`pom.xml`](../../../00-quick-start/pom.xml) ൽ ഉപയോഗിക്കുന്നു:

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

`langchain4j-open-ai-official` മോഡ്യൂൾ OpenAI-സഹജമായ APIs-ുമായി ബന്ധിപ്പിക്കപ്പെടുന്ന `OpenAiOfficialChatModel` ക്ലാസ് നൽകുന്നു. GitHub മോഡലുകൾ സമാന API ഫോർമാറ്റ് ഉപയോഗിക്കുന്നതിനാൽ പ്രത്യേക അഡാപ്റ്റർ ആവശ്യം ഇല്ല - അടിസ്ഥാന URL വഴി ഉണ്ടാക്കുക `https://models.github.ai/inference`.

`langchain4j-easy-rag` മോഡ്യൂൾത് ഡോക്യുമെന്റുകൾ സ്വയം വിഭജിക്കൽ, എംബെഡിങ്, റിട്രൈവൽ സ്വയം ഉണ്ടാക്കുന്നു, അങ്ങനെ RAG ആപ്ലിക്കേഷനുകൾ ഓരോ ഘട്ടവും മേധാവിത്വത്തിൽ ക്രമീകരിക്കാതെ നിർമ്മിക്കാം.

## മുൻ‌വിധികൾ

**പ്രവർത്തന കണ്ടെയ്നർ ഉപയോഗിക്കുകയാണോ?** ജാവയും Maven ഉം സജ്ജമാണ്. നിങ്ങൾക്ക് GitHub വ്യക്തിഗത ആക്സസ് ടോക്കൺ മാത്രമേ വേണേ ഉള്ളൂ.

**നാടുവഴി ഡവലപ്മെന്റ്:**
- ജावा 21+, Maven 3.9+
- GitHub വ്യക്തിഗത ആക്സസ് ടോക്കൺ (പിന്നീട് വിശദീകരിക്കൽ)

> **കുറിപ്പ്:** ഈ മോഡ്യൂൾ GitHub മോഡലുകളിൽ നിന്നുള്ള `gpt-4.1-nano` ഉപയോഗിക്കുന്നു. കോഡിൽ മോഡൽ നാമം മാറ്റരുത് - GitHub-ഉം ലഭ്യമായ മോഡലുകൾക്ക് ക്രമീകരിച്ചിരിക്കുന്നു.

## സജ്ജീകരണം

### 1. നിങ്ങളുടെ GitHub ടോക്കൺ നേടുക

1. [GitHub സെറ്റിംഗ്സ് → വ്യക്തിഗത ആക്സസ് ടോക്കണുകൾ](https://github.com/settings/personal-access-tokens) സന്ദർശിക്കുക
2. "Generate new token" ക്ലിക്ക് ചെയ്യുക
3. പ്രസിദ്ധീകരിക്കുന്ന പേര് നൽകുക (ഉദാ: "LangChain4j ഡെമോ")
4. കാലാവധി നിശ്ചയിക്കുക (7 ദിവസം ശുപാർശ ചെയ്‌തു)
5. "അക്കൗണ്ട് പെർമിഷനുകൾ" ൽ "Models" കണ്ടെത്തി "Read-only" ആയി സജ്ജമാക്കുക
6. "Generate token" ക്ലിക്ക് ചെയ്യുക
7. ടോക്കൺ കോപ്പി ചെയ്ത് സൂക്ഷിക്കുക - വീണ്ടും കാണാനാകില്ല

### 2. നിങ്ങളുടെ ടോക്കൺ സജ്ജമാക്കുക

**ഓപ്‌ഷൻ 1: VS Code ഉപയോഗിച്ച് (ശുപാർശ ചെയ്യാം)**

VS Code ഉപയോഗിക്കുന്നെങ്കിൽ, പ്രോജക്ട് റൂട്ടിലുള്ള `.env` ഫയലിലേക്ക് ടോക്കൺ ചേർക്കുക:

`.env` ഫയൽ ഇല്ലെങ്കിൽ, `.env.example` കോപ്പി ചെയ്ത് `.env` എന്നാക്കി മാറ്റുകയോ പുതിയ `.env` ഫയൽ സൃഷ്ടിക്കുകയോ ചെയ്യാം.

**ഉദാഹരണ `.env` ഫയൽ:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env ൽ
GITHUB_TOKEN=your_token_here
```

അപ്പോൾ Explorer-ലുള്ള ഏതെങ്കിലും ഡെമോ ഫയലിൽ (ഉദാ: `BasicChatDemo.java`) റൈറ്റ് ക്ലിക്ക് ചെയ്ത് **"Run Java"** തിരഞ്ഞെടുക്കുകയോ, Run and Debug പാനൽ ഉപയോഗിച്ച് ലോഞ്ച് കോൺഫിഗറേഷൻ ഉപയോഗിക്കവെ സംപ്രimam.

**ഓപ്‌ഷൻ 2: ടെർമിനൽ വഴി**

ടോക്കൺ ഒരു എൻവർൺമെന്റ് വേരിയബിളായി സജ്ജമാക്കുക:

**ബാഷ്:**
```bash
export GITHUB_TOKEN=your_token_here
```

**പവർഷെൽ:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## ഉദാഹരണങ്ങൾ നടത്തുക

**VS Code ഉപയോഗിച്ച്:** ഡെമോ ഫയൽ Explorer-ൽ റൈറ്റ്-ക്ലിക്ക് ചെയ്ത് **"Run Java"** തിരഞ്ഞെടുക്കുക, അല്ലെങ്കിൽ Run and Debug പാനലിൽ നിന്നുള്ള ലോഞ്ച് കോൺഫിഗറേഷൻ ഉപയോഗിക്കാം (നിങ്ങളുടെ ടോക്കൺ `.env` ഫയലിൽ ചേർത്തിട്ടുണ്ടെന്ന് ഉറപ്പാക്കുക).

**Maven ഉപയോഗിച്ച്:** അതോ കമാൻഡ് ലൈൻ വഴി:

### 1. അടിസ്ഥാന സംഭാഷണം

**ബാഷ്:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**പവർഷെൽ:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. പ്രോംപ്റ്റ് ഡ്രശ്യങ്ങൾ

**ബാഷ്:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**പവർഷെൽ:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

സീറോ-ഷോട്ട്, ഫ്യൂ-ഷോട്ട്, ചെയിൻ-ഓഫ്-തോട്ട്, റോൾ-ബേസ് പ്രോംപ്റ്റിംഗ് കാണിക്കുന്നു.

### 3. ഫംഗ്ഷൻ വിളിക്കൽ

**ബാഷ്:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**പവർഷെൽ:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

ആവശ്യമായപ്പോൾ AI സ്വയം ജാവ ഫംഗ്ഷനുകൾ വിളിക്കുന്നു.

### 4. ഡോക്യുമെന്റ് ചോദ്യോത്തരങ്ങൾ (ഇസി RAG)

**ബാഷ്:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**പവർഷെൽ:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

സ്വയം എംബെഡിങ്, റിട്രൈവൽ ഉപയോഗിച്ച് എളുപ്പം RAG-ലൂടെ ഡോക്യുമെന്റുകളേക്കുറിച്ച് ചോദ്യങ്ങൾ ചോദിക്കാം.

### 5. ഉത്തരവാദിത്വമുള്ള AI

**ബാഷ്:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**പവർഷെൽ:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI സുരക്ഷാ ഫിൽട്ടറുകൾ അപകടകരമായ ഉള്ളടക്കം തടയുന്നതെങ്ങനെ കാണുക.

## ഒരു ഉദാഹരണം ഓരോന്നും എന്ത് കാണിക്കുന്നു

**അടിസ്ഥാന സംഭാഷണം** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j ഏറ്റവും ലളിതമായ രൂപത്തിൽ കാണാൻ ഇവിടെ തുടങ്ങുക. നിങ്ങൾ ഒരു `OpenAiOfficialChatModel` സൃഷ്ടിച്ച്, `.chat()`-ഉം ഉപയോഗിച്ച് പ്രോംപ്റ്റ് അയച്ച്, മറുപടി നേടും. മോഡലുകൾ കസ്റ്റം എൻഡ്പോയിന്റുകൾ, API കീകൾ എന്നിവ ഉപയോഗിച്ച് എങ്ങനെ ഇൻഷിയലൈസ് ചെയ്യാമെന്ന് ഈ അടിസ്ഥാന പരിപാടി കാണിക്കുന്നു. ഇതുകഴിഞ്ഞാൽ, എല്ലാ മറ്റ് കാര്യങ്ങളും ഇതിൽ നിന്നാണ് നിർമ്മിക്കുന്നത്.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 GitHub Copilot ഉപയോഗിച്ച് പരീക്ഷിക്കുക:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) തുറന്ന് ചോദിക്കുക:
> - "GitHub മോഡലുകളിൽ നിന്ന് Azure OpenAI-യിലേക്ക് ഈ കോഡ് എങ്ങനെ മാറ്റാം?"
> - "OpenAiOfficialChatModel.builder()-യിലുള്ള മറ്റ് പാരാമീറ്ററുകൾ എന്തെല്ലാം സജ്ജീകരിക്കാം?"
> - "പൂർണ്ണ മറുപടിക്ക് കാത്തിരിക്കാൻ പകരം സ്റ്റ്രീമിംഗ് പ്രതികരണങ്ങൾ എങ്ങനെ ചേർക്കാം?"

**പ്രോംപ്റ്റ് എഞ്ചിനീയറിംഗ്** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

ഇപ്പോൾ നിങ്ങൾ മോഡലുമായി സംസാരിക്കുന്നത് മനസ്സിലായിരിക്കുന്നു, എന്ത് പറയാമെന്ന് പരിശോധിക്കാം. ഈ ഡെമോ തന്ന മോഡൽ സെറ്റപ്പിൽ അഞ്ചു വ്യത്യസ്ത പ്രോംപ്റ്റിംഗ് ഡീസൈനുകൾ കാണിക്കുന്നു. പഴയ, കുറച്ച് ഉദാഹരണങ്ങൾ ഉൾക്കൊള്ളിക്കുന്ന, ചിന്താപ്രവാഹം ഉള്ള, കാഴ്ച്ചയുടെ അടിസ്ഥാനത്തിലുള്ള പ്രോംപ്റ്റുകൾ. ഒരേ മോഡൽ വ്യത്യസ്ത വിധങ്ങളിൽ ഫലങ്ങൾ നൽകുന്നത് കാണാം.

ഡെമോ പ്രോംപ്റ്റ് ടെംപ്ലേറ്റുകളും കാണിക്കുന്നു, ഇത് വകമാറ്റങ്ങളുള്ള പ്രോംപ്റ്റുകൾ സൃഷ്ടിക്കാനായി ശക്തമായ മാർഗ്ഗമാണ്.
താഴെ ഉദാഹരണത്തിൽ LangChain4j `PromptTemplate` ഉപയോഗിച്ച് സംവിധാന വേരിയബിളുകൾ നിറയ്ക്കുന്നു. AI നൽകിയ ലക്ഷ്യസ്ഥലവും പ്രവർത്തനവും ആശ്രയിച്ച് മറുപടി നൽകും.

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

> **🤖 GitHub Copilot ഉപയോഗിച്ച് പരീക്ഷിക്കുക:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) തുറന്ന് ചോദിക്കുക:
> - "സീറോ-ഷോട്ട് കൂടാതെ ഫ്യൂ-ഷോട്ട് പ്രോംപ്റ്റിന്‍റെ വ്യത്യാസം എന്താണ്, എപ്പോൾ ഓരോന്നും ഉപയോഗിക്കണം?"
> - "ടെംപ്രറേച്ചർ പാരാമീറ്റർ മോഡൽ പ്രതികരണങ്ങളെ എങ്ങനെ ബാധിക്കുന്നു?"
> - "പ്രോംപ്റ്റ് ഇൻജെക്ഷൻ ആക്രമണങ്ങൾ വിലങ്ങാൻ എന്തെല്ലാം സാങ്കേതിക വിദ്യകൾ ഉണ്ട്?"
> - "പതിവായി ഉപയോഗിക്കുന്ന ഡ്രഷ്യങ്ങൾക്കായി എങ്ങനെ പുനരുപയോഗയോഗ്യമായ PromptTemplate ഒബ്ജക്റ്റുകൾ സൃഷ്ടിക്കാം?"

**ടൂൾ ഇന്റഗ്രേഷൻ** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ഇവിടെ LangChain4j ശക്തമായിത്തീരും. `AiServices` ഉപയോഗിച്ച് ഒരു AI സഹായി ഒരുക്കും, ഇത് നിങ്ങളുടെ ജാവ രീതികൾ വിളിക്കും. നിങ്ങളുടെ രീതികൾ `@Tool("description")` എന്ന് അനോട്ടേറ്റ് ചെയ്യുക, LangChain4j ബാക്കി കൈകാര്യം ചെയ്യും - ആവശ്യത്തിന് AI സ്വയം ഉപകരണങ്ങൾ ഉപയോഗിക്കാനെണ്ണും. ഇത് ഫംഗ്ഷന് വിളിക്കൽ എന്ന പ്രധാനം തെളിയിക്കുന്നു, AI പ്രവർത്തനങ്ങൾ എടുക്ക തീർപ്പു നൽകുന്നത്.

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

> **🤖 GitHub Copilot ഉപയോഗിച്ച് പരീക്ഷിക്കുക:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) തുറന്ന് ചോദിക്കുക:
> - "@Tool അനോട്ടേഷൻ എങ്ങനെ പ്രവർത്തിക്കുന്നു, LangChain4j പിന്നിൽ എന്ത് ചെയ്യുന്നു?"
> - "സങ്കീർണ്ണ പ്രശ്‌നങ്ങൾ പരിഹരിക്കാൻ AI അനുക്രമമായി പല ടൂളുകളും വിളിക്കുമോ?"
> - "ഒരു ടൂൾ ഒഴിഞ്ഞാല്‍ എന്താകും, പിശകുകൾ എങ്ങനെ കൈകാര്യം ചെയ്യണം?"
> - "ഈ കാൽക്കുലേറ്റർ ഉദാഹരണത്തിന് പകരം യഥാർത്ഥ API എങ്ങനെ സംയോജിപ്പിക്കാം?"

**ഡോക്യുമെന്റ് ചോദ്യോത്തരങ്ങൾ (ഇസി RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

LangChain4j-യുടെ "Easy RAG" ഉപയോഗിച്ച് RAG (വീണ് കണ്ടെത്തൽ-ധാരാളീകരണം വാതിലുകൾ വിതരണം) കാണിക്കുന്നു. ഡോക്യുമെന്റുകൾ ലോഡ് ചെയ്ത്, സ്വയം വിഭജിക്കുകയും എമ്പെഡ് ചെയ്യുകയും ഓർമ്മ ഷേഖരത്തിൽ സൂക്ഷിച്ചും, റിട്ട്രീവൽ സമയം പ്രാസക്തമായ ഭാഗങ്ങൾ AI-ക്ക് നൽകുന്നു. AI അക്ക അടിസ്ഥാനത്തിൽ മറുപടി നൽകുന്നു, പൊതുവായ അറിവ് അല്ല.

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

> **🤖 GitHub Copilot ഉപയോഗിച്ച് പരീക്ഷിക്കുക:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) തുറന്ന് ചോദിക്കുക:
> - "AI ഹലുസിനേഷൻ RAG ഉപയോ​ഗിച്ച് മോഡലിന്റെ പരിശീലന ഡാറ്റ ഉപയോ​ഗിക്കുന്നതുമായി താരതമ്യം ചെയ്യുമ്പോൾ എങ്ങനെ തടയും?"
> - "ഈ എളുപ്പത്തിലുള്ള സമീപനം, കസ്റ്റം RAG പൈപ്പ്‌ലൈനിന്‍റെ വ്യത്യാസം എന്ത്?"
> - "ബഹുഭൂരക ഡോക്യുമെന്റുകൾക്ക് അല്ലെങ്കിൽ വലുതായ അറിവ് സ്രോതസുകൾ കൈകാര്യം ചെയ്യാൻ ഇത് എങ്ങനെ സ്കെയിൽ ചെയ്യാം?"

**ഉത്തരവാദിത്വമുള്ള AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

ഊർജ്ജസമൃദ്ധമായ AI സുരക്ഷ നിർമ്മിക്കുക. ഈ ഡെമോ രണ്ട് നിലകളിലെ സംരക്ഷണം കാണിക്കുന്നു:

**ഭാഗം 1: LangChain4j ഇൻപുട്ട് ഗാർഡ്റെയിൽസ്** - LLM-വിലേക്ക് അതിക്രമം ചെയ്യും മുമ്പുണ്ടാകുന്ന കാരണങ്ങൾ തടയുക. നിരോധിത കീവർഡുകളോ പാറ്റേണുകളോ പരിശോധിക്കുന്ന കസ്റ്റം ഗാർഡ്റെയ്ലുകൾ സൃഷ്ടിക്കുക. ഇത് നിങ്ങളുടെ കോഡിൽ പ്രവർത്തിക്കുന്നു, അതിനാൽ വേഗത്തിലുമാണ് കൂടാതെ ഫ്രീ പോലും.

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

**ഭാഗം 2: പ്രൊവൈഡർ സേഫ്റ്റി ഫിൽട്ടറുകൾ** - GitHub മോഡലുകൾ ഉൾക്കൊള്ളുന്ന ഫിൽട്ടറുകൾ, നിങ്ങളുടെ ഗാർഡ്റെയിൽസ് കാണാനാകാത്തത് പിടിക്കുന്നു. കടുത്ത ലംഘനങ്ങൾക്കായി ഹാർഡ് ബ്ലോക്കുകൾ (HTTP 400 പിശകുകൾ) കാണിക്കും, ഇളവും പരിസരവുമുള്ള നിരസലുകൾ (soft refusals) AI നന്ദിയോടെയ്ക്കും നിരസിക്കട്ടെ.

> **🤖 GitHub Copilot ഉപയോഗിച്ച് പരീക്ഷിക്കുക:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) തുറന്ന് ചോദിക്കുക:
> - "InputGuardrail എന്നത് എന്ത്, എങ്ങനെ ഞാൻ സ്വന്തമായത് സൃഷ്ടിക്കാം?"
> - "ഹാർഡ് ബ്ലോക്കും സോഫ്റ്റ് നിരസലും തമ്മിൽ എന്ത് വ്യത്യാസം?"
> - "എന്തുകൊണ്ട് ഗാർഡ്റെയിൽസ് കൂടെയും പ്രൊവൈഡർ ഫിൽട്ടറുകളും ഒരുമിച്ച് ഉപയോഗിക്കണം?"

## അടിയെഴുത്തുകൾ

**അടുത്ത മോഡ്യൂൾ:** [01-പരിചയം - LangChain4j-ഉം gpt-5 Azure-ലും ആരംഭിക്കൽ](../01-introduction/README.md)

---

**നാവിഗേഷൻ:** [← മെയിനിലേയ്ക്ക് മടങ്ങുക](../README.md) | [അടുത്തത്: മോഡ്യൂള്‍ 01 - പരിചയം →](../01-introduction/README.md)

---

## പ്രശ്‌നം പരിഹാരം

### ആദ്യമായി Maven ബിൽഡ്

**പ്രശ്‌നം**: ആദ്യം `mvn clean compile` അല്ലെങ്കിൽ `mvn package` വളരെ സമയം (10-15 മിനിട്ടുകൾ)

**കാരണങ്ങൾ**: ആദ്യ ബിൽഡ് പാർലെ Maven എല്ലാ ആശ്രിതങ്ങളും ഡൗൺലോഡ് ചെയ്യണം (Spring Boot, LangChain4j ലൈബ്രറീസ്, Azure SDKs, മുതലായവ).

**പരിഹാരം**: ഇത് സാധാരണമാണു. അടുത്ത ബിൽഡുകൾ ആക്റ്റിവിറ്റികൾ കൂടുതൽ വേഗമാകും കാരണം ആശ്രിതങ്ങൾ ലോക്കലായി സേവ് ചെയ്തിരിക്കുന്നു. ഡൗൺലോഡ് സമയം നിങ്ങളുടെ നെറ്റ്‌വർക്കിന്റെ വേഗതയിൽ ആശ്രയിച്ചിരിക്കുന്നു.

### PowerShell-ൽ Maven കമാൻഡ് സിന്ടാക്‌സ്

**പ്രശ്‌നം**: Maven കമാൻഡുകൾ `Unknown lifecycle phase ".mainClass=..."` എന്ന പിശകോടെ പരാജയപ്പെടുന്നു.
**കാരണം**: Maven പ്രോപ്പർട്ടി സിന്താക്സ് തകർക്കുന്ന വിധം PowerShell `=` എന്നതിനെ വേരിയബിൾ അസൈൻമെന്റ് ഓപ്പറേറ്ററായി വ്യാഖ്യാനിക്കുന്നു

**പരിഹാരം**: Maven കമ്മാൻഡിനു മുമ്പ് സ്റ്റോപ്പ്-പാർസിംഗ് ഓപ്പറേറ്റർ `--%` ഉപയോഗിക്കുക:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ഓപ്പറേറ്റർ Maven-ലേക്ക് എല്ലാ ശേഷിക്കുന്ന ആർഗ്യുമെന്റുകളും വ്യാഖ്യാനിക്കാതെ ലിറ്ററലി ആയി പാസ് ചെയ്യാൻ PowerShell-നെ നിര്‍ബന്ധിക്കുന്നു.

### Windows PowerShell ഇമോജി പ്രദർശനം

**പ്രശ്നം**: PowerShell-ൽ AI മറുപടികൾ ഇമോജികളുടെ സ്ഥാനത്ത് കച്ചവട അക്ഷരങ്ങൾ (ഉദാ., `????` അല്ലെങ്കിൽ `â??`) കാണിക്കുന്നു

**കാരണങ്ങൾ**: PowerShell ന്റെ ഡിഫോൾട്ട് എൻകോഡിംഗ് UTF-8 ഇമോജികളെ പിന്തുണയ്ക്കുന്നില്ല

**പരിഹാരം**: ജാവ ആപ്ലിക്കേഷനുകൾ നടപ്പാക്കുന്നതിന് മുമ്പ് ഈ കമാൻഡ് പ്രവർത്തിപ്പിക്കുക:
```cmd
chcp 65001
```

ഇത് ടെർമിനലിൽ UTF-8 എൻകോഡിംഗ് നിർബന്ധിതമാക്കുന്നു. അല്ലാതെ, മികച്ച യൂണികോഡ് പിന്തുണയുള്ള Windows Terminal ഉപയോഗിക്കാം.

### API കോൾസ് ഡീബഗ്ഗിംഗ്

**പ്രശ്നം**: ഓതന്റിക്കേഷൻ പിഴവുകൾ, റേറ്റ് ലിമിറ്റുകൾ, അല്ലെങ്കിൽ AI മോഡലിൽ നിന്നുള്ള പ്രതീക്ഷിക്കാത്ത പ്രതികരണങ്ങൾ

**പരിഹാരം**: ഉദാഹരണങ്ങളിൽ `.logRequests(true)`യും `.logResponses(true)`യും ഉൾപ്പെടുത്തിയിട്ടുണ്ട്, ഇവ API കോൾസ് കോൺസോളിൽ കാണിക്കുന്നതിനായി. ഇത് ഓതന്റിക്കേഷൻ പിഴവുകൾ, റേറ്റ് ലിമിറ്റുകൾ, അല്ലെങ്കിൽ പ്രതീക്ഷിക്കാത്ത പ്രതികരണങ്ങൾ തിരിച്ചറിയാൻ സഹായിക്കുന്നു. പ്രൊഡക്ഷൻ പരിസ്ഥിതിയിൽ ഈ ഫ്ലാഗുകൾ നീക്കം ചെയ്യുക, ലോഗ് ശബ്ദം കുറയ്ക്കാൻ.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**വിവരണം**:
ഈ ഡോക്യുമെന്റ് AI പരിഭാഷ സേവനം [Co-op Translator](https://github.com/Azure/co-op-translator) ഉപയോഗിച്ച് വിവർത്തനം ചെയ്തതാണ്. ഞങ്ങൾ കൃത്യതയ്ക്ക് പരീക്ഷണം നടത്തുതlingen ഉളളതായിട്ടും, സ്വയം ഓർമ്മപ്പെടുത്തുന്നതായി ഓട്ടോമേറ്റഡ് പരിഭാഷകളിൽ പിശകുകളും അപ്രമാണങ്ങളും ഉണ്ടായിരിക്കാമെന്ന് കൗശല്യം കാണിക്കുക. മാതൃഭാഷയിലുള്ള മുൽ ഡോക്യുമെന്റാണ് വിശ്വാസയോഗ്യമായ ഉറവിടം എന്ന് കരുതണം. നിർണ്ണായക വിവരങ്ങൾക്ക്, പ്രൊഫഷണൽ മനുഷ്യ പരിഭാഷ ശുപാർശ ചെയ്യുന്നു. ഈ പരിഭാഷ ഉപയോഗത്തിൽ സംഭവിച്ച ഏതെങ്കിലും തെറ്റിപ്പറയലുകൾക്കും തെറ്റുവായവയ്ക്കും ഞങ്ങൾ ഉത്തരവാദിയല്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->