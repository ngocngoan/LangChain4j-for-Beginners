# Module 00: အချိန်တိုအရင်စတင်မှု

## အကြောင်းအရာ

- [မိတ်ဆက်](../../../00-quick-start)
- [LangChain4j ဆိုတာဘာလဲ?](../../../00-quick-start)
- [LangChain4j မှာလိုအပ်ချက်များ](../../../00-quick-start)
- [လိုအပ်ချက်များ](../../../00-quick-start)
- [တပ်ဆင်ခြင်း](../../../00-quick-start)
  - [1. မိမိ GitHub Token ကိုရယူခြင်း](../../../00-quick-start)
  - [2. မိမိ Token ကိုသတ်မှတ်ခြင်း](../../../00-quick-start)
- [နမူနာများကိုပြေးစမ်းပါ](../../../00-quick-start)
  - [1. အခြေခံ စကားပြော](../../../00-quick-start)
  - [2. Prompt ပုံစံများ](../../../00-quick-start)
  - [3. Function ခေါ်ဆိုခြင်း](../../../00-quick-start)
  - [4. စာရွက်စာတမ်း Q&A (လွယ်ကူသော RAG)](../../../00-quick-start)
  - [5. တာဝန်ရှိသော AI](../../../00-quick-start)
- [နမူနာတိုင်းပြသသည့်အရာ](../../../00-quick-start)
- [နောက်တန်းအဆင့်များ](../../../00-quick-start)
- [ပြဿနာဖြေရှင်းခြင်း](../../../00-quick-start)

## မိတ်ဆက်

ဒီ quickstart သည် LangChain4j နှင့် မြန်ဆန်စွာ စတင်အသုံးပြုနိုင်ရန် ရည်ရွယ်သည်။ LangChain4j နှင့် GitHub Models အသုံးပြု၍ AI အသုံးပြု application များ တည်ဆောက်ရာတွင် အခြေခံများကိုဖော်ပြထားသည်။ နောက်ပိုင်း Modules များတွင် Azure OpenAI နှင့် GPT-5.2 သို့ ပစ်လွှတ်ကြပြီး concepts တစ်ခုချင်းစီကို ပိုမိုနက်ရှိုင်းစွာ လေ့လာသွားမည်ဖြစ်သည်။

## LangChain4j ဆိုတာဘာလဲ?

LangChain4j သည် AI ပါဝင်သော applications များ တည်ဆောက်ရာတွင် အလွယ်တကူလုပ်ဆောင်ခွင့်ပေးသည့် Java စာကြည့်တိုက်ဖြစ်သည်။ HTTP clients နှင့် JSON parsing ကိုကိုင်တွယ်ရန်မလိုပဲ သင်သည် သန့်ရှင်းသော Java APIs ဖြင့်သာ အလုပ်လုပ်နိုင်သည်။

LangChain4j မှာ "chain" ဆိုသည်မှာ စိတ်ကြိုက် chain များကို ဆက်ထားခြင်းဖြစ်ပြီး - တစ်ခုချင်းစီ prompt ကို model နှင့် parser နှင့် ချိတ်ဆက်ဖို့ဖြစ်နိုင်သည်၊ သို့မဟုတ် AI ခေါ်ဆိုမှု များအားလုံး output ကို နောက် input သို့ ပစ်ပေးလည်းဖြစ်နိုင်သည်။ ဒီ quick start သည် အခြေခံများနဲ့ စတင်ပြီး ထပ်ဆင့် အကြီးစား chain များကို ပြန်လည်လေ့လာမည်။

<img src="../../../translated_images/my/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j မှာ components များကို ချိတ်ဆက်သည့် စဉ်ဆက်များ - အခြေခံ အပိုင်းအစများက အကြီးစား AI workflows တွေ တည်ဆောက်သည်*

သုံးတဲ့ အခြေခံ component ငါးခု:

**ChatModel** - AI model နှင့် ဆက်သွယ်ရန် interface ဖြစ်သည်။ `model.chat("prompt")` ကို ခေါ်၍ string response ရသည်။ သင်သည် `OpenAiOfficialChatModel` ကို အသုံးပြုသည်၊ GitHub Models စသည့် OpenAI-ကိုက်ညီသော endpoint များနှင့်အတူ အလုပ်လုပ်သည်။

**AiServices** - type-safe AI service interfaces ကို ဖန်တီးသည်။ method များကို သတ်မှတ်ပြီး `@Tool` ဖြင့် အမှတ်အသား ပြုလုပ်ပါက LangChain4j သည် orchestration ကို ကိုင်တွယ်သည်။ AI သည် လိုအပ်သည့်အချိန်တွင် သင့် Java method များကို အလိုအလျောက် ခေါ်ဆိုသည်။

**MessageWindowChatMemory** - စကားပြောမှတ်တမ်း ထိန်းသိမ်းထားသည်။ ၎င်းမရှိပါက မေးသော တောင်းဆိုချက်တိုင်းသည် လွတ်လပ်ပြီးပြားသည်။ ၎င်းဖြင့် AI သည် မေးခွန်းများနှင့် context ကို သိမ်းဆည်းထားနိုင်မည်။

<img src="../../../translated_images/my/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j architecture - core components များသည် သင့် AI applications များအတွက် အလုပ်လုပ်ပေးသည်*

## LangChain4j မှာလိုအပ်ချက်များ

ဒီ quick start သည် Maven မှ အောက်ပါ dependency သုံးခုကို [`pom.xml`](../../../00-quick-start/pom.xml) ထဲတွင် သုံးသည် -

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

`langchain4j-open-ai-official` module သည် `OpenAiOfficialChatModel` class ကို ပံ့ပိုးသည်၊ OpenAI-ကိုက်ညီသော APIs နှင့် ချိတ်ဆက်ပေးသည်။ GitHub Models သည် သာမန် API ပုံစံကို အသုံးပြုသောကြောင့် အထူး adapter မလိုအပ်ပဲ base URL ကို `https://models.github.ai/inference` သို့ ချိတ်ဆက်နိုင်သည်။

`langchain4j-easy-rag` module သည် စာရွက်စာတမ်းများကို အလိုအလျောက် ခြားနားခြင်း ၊ embedding နှင့် retrieval ကို ပံ့ပိုးပေးပြီး သင်သည် RAG applications များကို လက်မလုပ်ဆောင်ဘဲ တည်ဆောက်နိုင်သည်။

## လိုအပ်ချက်များ

**Dev Container ဖြင့်သုံးပါသလား?** Java နှင့် Maven သည် ရှိပြီးဖြစ်သည်။ သင်တွင် GitHub Personal Access Token လုံလောက်သည်။

**ပြည်တွင်းဖွံ့ဖြိုးတိုးတက်မှု:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (အောက်တွင်အနည်းငယ်လမ်းညွှန်ပါ)

> **မှတ်ချက်:** ဒီ module သည် GitHub Models မှ `gpt-4.1-nano` ကို အသုံးပြုသည်။ ဤ code အတွင်းမှာ model နာမည် မပြောင်းလဲရ။ GitHub models တွေရဲ့ရရှိနိုင်မှုနှင့် ကိုက်ညီအောင် သတ်မှတ်ထားသည်။

## တပ်ဆင်ခြင်း

### 1. မိမိ GitHub Token ကိုရယူခြင်း

1. [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens) သို့ သွားပါ
2. "Generate new token" ကိုနှိပ်ပါ
3. အမည် ရွေးချယ်ပါ (ဥပမာ - "LangChain4j Demo")
4. သက်တမ်းသတ်မှတ်ပါ (၇ ရက် မျှဆက်လက်သုံးရန် အကြံပြု)
5. "Account permissions" အောက်မှ "Models" ကိုရွေး၍ "Read-only" ခွင့်ပြုပါ
6. "Generate token" နှိပ်ပါ
7. မိမိ token ကို မျက်နှာမကြည့်ခင် ကူးယူပြီးထားရှိပါ

### 2. မိမိ Token ကိုသတ်မှတ်ခြင်း

**ရွေးချယ်မှု ၁: VS Code ဖြင့်အသုံးပြုခြင်း (အကြံပြုသည်)**

VS Code အသုံးပြုပါက၊ project root တွင် `.env` ဖိုင်ထဲသို့ token ထည့်သွင်းပါ:

`.env` ဖိုင် မရှိပါက `.env.example` ကို `.env` အဖြစ် ကူးယူပါ၊ ဒါမှမဟုတ် နေရာသစ် `.env` ဖိုင်တစ်ခု ဖန်တီးပါ။

**ဥပမာ `.env` ဖိုင်:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env တွင်
GITHUB_TOKEN=your_token_here
```

ထို့နောက် Explorer မှ တစ်ခုခု demo ဖိုင် (ဥပမာ `BasicChatDemo.java`) ကို right-click ပြုလုပ်ပြီး **"Run Java"** ကိုရွေးပါ သို့မဟုတ် Run and Debug panel မှ launch configurations များကို သုံးနိုင်သည်။

**ရွေးချယ်မှု ၂: Terminal ဖြင့်သုံးခြင်း**

Environment variable အဖြစ် token ကို သတ်မှတ်ပါ:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## နမူနာများကိုပြေးစမ်းပါ

**VS Code အသုံးပြုခြင်း:** Explorer ထဲမှ demo ဖိုင်အားလုံးကို right-click ပြုလုပ်ပြီး **"Run Java"** ရွေးခြင်းဖြင့် သို့မဟုတ် Run and Debug panel မှ launch configurations အသုံးပြု၍ token ကို `.env` မှ ထည့်သွင်းထားမှသာ။

**Maven အသုံးပြုခြင်း:** command line မှ run နိုင်ပါသည် -

### 1. အခြေခံ စကားပြော

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Prompt ပုံစံများ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

Zero-shot, few-shot, chain-of-thought, နှင့် role-based prompting များကို ပြသသည်။

### 3. Function ခေါ်ဆိုခြင်း

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI သည် လိုအပ်သည့်အချိန်တွင် Java method များကို အလိုအလျောက် ခေါ်ဆိုသည်။

### 4. စာရွက်စာတမ်း Q&A (လွယ်ကူသော RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Easy RAG ဖြင့် စာရွက်စာတမ်းအကြောင်း မေးခွန်းများကို အလိုအလျောက် embedding နှင့် retrieval အားဖြင့် မေးမြန်းနိုင်သည်။

### 5. တာဝန်ရှိသော AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI သည် အန္တရာယ်ရှိသော အကြောင်းအရာများကို စိတ်ရှုပ်ဖြေရှင်းမှု ကာကွယ်ရေး filter များဖြင့် တားဆီးသည်။

## နမူနာတိုင်းပြသသည့်အရာ

**အခြေခံ စကားပြော** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j ကို အလွယ်တကူ စတင်အသုံးပြုမှုကို ဒီနေရာမှ ကြည့်ရှုနိုင်သည်။ သင်သည် `OpenAiOfficialChatModel` ဖန်တီးပြီး `.chat()` ဖြင့် prompt ပေး၍ response ရရှိမည်။ ဤသည်မှာ စတင်သည့် အခြေခံအဆောက်အအုံဖြစ်သည်။ သင်သည် custom endpoint နှင့် API key များဖြင့် မော်ဒယ်များကို စတင်ပါကဒီနည်းပညာကို နားလည်ထားရန်လိုသည်။ ၎င်းနောက် အခြား function များကို တည်ဆောက်နိုင်မည်။

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းပါ:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ကိုဖွင့်ပြီး မေးပါ -
> - "GitHub Models မှ Azure OpenAI သို့ ဘယ်လို အစားထိုးမှုပြုလုပ်မလဲ?"
> - "OpenAiOfficialChatModel.builder() မှာ ဘယ် parameter တွေ ပြောင်းလဲချင်ရဲ့?"
> - "Response ပြန်လာတာ စောင့်ရန်မလိုဘဲ streaming ပုံစံဖြင့် သုံးချင်ရင် ဘယ်လိုလုပ်မလဲ?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Model နှင့် ဒိုင်ရက် ပြောဆိုနည်းကို သိရှိပြီးနောက် အခုတော့ ပြောတဲ့ စကားလုံးများကို လေ့လာကြမယ်။ ဒီနမူနာသည် မော်ဒယ်တူတူသာ သုံးပေမဲ့ prompt ပုံစံ ၅ မျိုးကို ပြသသည်။ Zero-shot prompt များဖြင့် တိုက်ရိုက်ညွှန်ကြားချက်များ၊ few-shot prompts ဖြင့် ဥပမာများမှ သင်ယူခြင်း၊ chain-of-thought prompts ဖြင့် အတွေးခင်းများဖော်ပြခြင်းနှင့် role-based prompts ဖြင့် context သတ်မှတ်ခြင်းတွေကို စမ်းသပ်ကြည့်နိုင်ပါသည်။ မည်သည့် prompt ဖြင့် ဖိတ်ခေါ်သည်ကိုအပေါ် မော်ဒယ်၏ ထွက်ရှိမှုက မတူကွဲပြားစေသည်။

ဒီနမူနာသည် prompt templates ကိုလည်း ပြသသည်၊ ၎င်းသည် variables ဖြင့် ပြန်လည်အသုံးပြုနိုင်သော prompts ဖန်တီးရန် ဂီတမြောက်ပြုလုပ်ပုံ ဖြစ်သည်။
အောက်တွင် LangChain4j `PromptTemplate` ကို အသုံးပြုပြီး variable များ ဖြည့်စွက်ထားသော prompt လုပ်နည်းကို ပြထားသည်။ AI သည် destination နှင့် activity အပေါ်မူတည်၍ ဖြေကြားမည်။

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းပါ:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ကို ဖွင့်ပြီး မေးပါ -
> - "Zero-shot နှင့် few-shot prompting ၏ ကွဲပြားချက်ဘာလဲ? ဘယ်အခါမှာ ဘယ်မှာသုံးရမလဲ?"
> - "Temperature parameter က မော်ဒယ်ဖြေကြားမှုကို ဘယ်လို သက်ေရာက်အကျိုးသက်ရောက်မှုရှိသလဲ?"
> - "Production တွင် prompt injection attacks မှကာကွယ်ရန် နည်းလမ်းများ ဘာများ ရှိသလဲ?"
> - "ပုံမှန် သုံးသော PromptTemplate objects များ ကို မည်သို့ ပြန်လည်အသုံးပြုနိုင်မလဲ?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ဒီနေရာက LangChain4j ကျယ်ပြန့်သောစွမ်းဆောင်ရည်က ပြသသည်။ သင်သည် `AiServices` ကို အသုံးပြု၍ သင့် Java method များကိုခေါ်နိုင်သော AI အကူအညီကို ဖန်တီးမည်။ Method များကို `@Tool("description")` ဖြင့် အမှတ်အသားပြုပါက LangChain4j သည် ဆက်စပ်ပုံစံအားလုံးကို ကိုင်တွယ်ပေးသည်။ AI သည် user ၏ မေးခွန်းအပေါ် အလိုအလျောက် ဖုန်းခေါ်ခြင်းများ ဆောင်ရွက်နိုင်ပါသည်။ ဒီနမူနာက တာဝန်ယူပါးပြောင်မှု (function calling) ကို ပြသသည်၊ အဓိက AI က ဖော်ပြချက်ပေးသည့် နည်းလမ်း ဖြစ်သည်။

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းပါ:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ကို ဖွင့်ပြီး မေးပါ -
> - "@Tool annotation က ဘယ်လို အလုပ်လုပ်သလဲ? LangChain4j က ယင်းနောက်မှာ ဘာလုပ်ပါသလဲ?"
> - "AI သည် လုပ်ငန်းခွဲများကို စဉ်လိုက်ခေါ်ဆိုပေးနိုင်ပါသလား?"
> - "Tool တစ်ခုမှ exception ပေါ်လာပါက ဘာဖြစ်မလဲ? အမှားများကို ယခင်တွင် ဘယ်လို ကိုင်တွယ်သင့်သလဲ?"
> - "ဒီ calculator ဥပမာ မဟုတ်ဘဲ တကယ့် API integration ဘယ်လိုလုပ်မလဲ?"

**Document Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ဒီမှာ LangChain4j ၏ "Easy RAG" နည်းလမ်းဖြင့် RAG (retrieval-augmented generation) ကို တွေ့မြင်ရမည်။ စာရွက်စာတမ်းများကို ဖတ်ရှု၊ အလိုအလျောက် ခြားနား၊ embedding လုပ်ပြီး memory store ထဲသို့သိမ်းဆည်းသည်။ ပြီးနောက် ရှာဖွေမှုစနစ်က AI ကို query အချိန်တွင် သင့်စာရွက်စာတမ်းများမှ အကြောင်းအရာဆက်စပ် ယူပေးသည်။ AI သည် ပုံမှန်သိမှတ်ချက်မဟုတ်ဘဲ သင့်စာရွက်စာတမ်းများအပေါ် မူတည်၍ ဖြေကြားသည်။

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းပါ:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ကိုဖွင့်ပြီး မေးပါ -
> - "RAG သည် AI hallucination ကို မော်ဒယ် သင်ကြားမှုအချက်အလက်နှင့် မတူဘဲ မည်သို့ ကာကွယ်သနည်း?"
> - "ဒီလွယ်ကူသောနည်းလမ်းနှင့် စိတ်ကြိုက် RAG pipeline ကြားက ဘာကွာခြားမှုရှိသနည်း?"
> - "စာရွက်စာတမ်းများ များပြားသည့်အခါ သို့မဟုတ် သိမှတ်မှတ်တမ်းကြီးများကို မည်သို့ တိုးချဲ့ စီမံမည်နည်း?"

**တာဝန်ရှိသော AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

AI လုံခြုံရေးကို အဆင့်မြှင့်တင်ဖန်တီးပါ။ ဒီနမူနာမှာ ကာကွယ်မှု အဆင့် ၂ ဆင့် တွဲဖက်သုံးထားသည်။

**အပိုင်း ၁: LangChain4j Input Guardrails** - LLM သို့ ရောက်ခင် စကားပြော ပြပွဲများအ၊သိမ်းထိန်းခြင်း။ တားမြစ်ရမည့် စကားလုံးနဲ့ ပုံတူ pattern များကို စစ်ကြည့်ရန် စိတ်ကြိုက် guardrails ဖန်တီးနိုင်သည်။ ၎င်းသည် သင့်ကုဒ်ထဲတွင် ပြေးသောကြောင့် အလျင်မြန်ပြီး အခမဲ့ဖြစ်သည်။

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

**အပိုင်း ၂: Provider Safety Filters** - GitHub Models သည် သင့် guardrails မမြင်နိုင်ပေမယ့် စုံစမ်းစစ်ဆေးမှု filter များပါရှိသည်။ ပြင်းထန်သောချို့ယွင်းချက်များအတွက် (HTTP 400 အမှား) တားဆီးမှုများ ရှိပြီး ရှင်းလင်း ညှိနှိုင်းခြင်းများတွင် AI သည် ရိုးသားစွာ ငြင်းဆန်ပါသည်။

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းပါ:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ကိုဖွင့်ပြီး မေးပါ -
> - "InputGuardrail ဆိုတာဘာလဲ? ကိုယ့်ကိုယ်ကိုဘယ်လို ဖန်တီးမလဲ?"
> - "Hard block နှင့် soft refusal ကြားက ခြားနားချက် ဘာလဲ?"
> - "Guardrails နဲ့ provider filters ကို ဘယ်လိုတူတူအသုံးပြုသင့်သလဲ?"

## နောက်တန်းအဆင့်များ

**နောက်တန်း Module:** [01-introduction - LangChain4j နဲ့ စတင်တည်ဆောက်ခြင်း](../01-introduction/README.md)

---

**သွားလာရန်:** [← ပြန်သွား မူလစာမျက်နှာ](../README.md) | [နောက်တစ်ပိုင်း: Module 01 - မိတ်ဆက် →](../01-introduction/README.md)

---

## ပြဿနာဖြေရှင်းခြင်း

### ပထမဆုံး Maven Build

**ပြဿနာ:** ပထမဆုံး `mvn clean compile` သို့မဟုတ် `mvn package` သည် ကြာမြင့်သည် (10-15 မိနစ်)

**အကြောင်း:** Maven သည် ပထမဆုံး build အတွက် အားလုံး project dependency များ (Spring Boot, LangChain4j libraries, Azure SDKs စသည်) ကို ဒေါင်းလုပ်ချရန် လိုအပ်သည်။

**ဖြေရှင်းနည်း:**  ၎င်းသည် ပုံမှန် ဖြစ်စဉ်ဖြစ်သည်။ နောက်ထပ် build များတွင် ဒေတာများကို local ထဲတွင် သိမ်းဆည်းပြီးဖြစ်သဖြင့် လျှင်မြန်သွားမည်။ ဒေါင်းလုပ်နှုန်းသည် သင့်ကွန်ရက်အမြန်နှုန်းပေါ်မူတည်သည်။

### PowerShell Maven Command Syntax

**ပြဿနာ:** Maven command များတွင် `Unknown lifecycle phase ".mainClass=..."` အမှားဖြစ်ပေါ်သည်။
**အကြောင်းအရင်း**: PowerShell သည် `=` ကို မိမိ၏ အသုံးပြုထားသော မော်တင်အဖြေပိုင်း ဒေတာပေးလမ်းပြ မှာပုံမှန် မဟုတ်သည့် ပုံစံများအတွက် ရည်ညွှန်းချက်အဖြစ် မျှော်လင့်ခြင်း၊ Maven property syntax ကို ကွဲသွားစေသည်။

**ဖြေရှင်းချက်**: Maven command မတည့်ခင် `--%` ဆိုသော stop-parsing operator ကို အသုံးပြုမည်။

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` operator သည် PowerShell ကို Maven သို့ အကျန်အမြစ်အားလုံးကို အဓိပ္ပါယ်မပြောင်းဘဲ များစွာသော argument များကို တိုက်ရိုက်ပေးပို့ရန် ပြောဆိုသည်။

### Windows PowerShell Emoji ပြသမှု

**ပြဿနာ**: PowerShell မှာ AI အဖြေများကို emoji များထုတ်မထားဘဲ ကပ်ဖတ်စာလုံးများ (ဥပမာ `????` သို့မဟုတ် `â??`) ပုံစံဖြင့် ပြသခြင်း။

**အကြောင်းအရင်း**: PowerShell ၏ ပုံမှန် encoding သည် UTF-8 emojis မကိုင်တွယ်နိုင်ပါ။

**ဖြေရှင်းချက်**: Java application မတည့်ခင် ဤ command ကို run ပြီးတာ။
```cmd
chcp 65001
```

တစ်ကား UTF-8 encoding ကို terminal တည်အောင်လုပ်ပေးသည်။ တခြားနည်းလမ်းအဖြစ် Windows Terminal ကိုအသုံးပြုပါ၊ Unicode ကိုပိုမိုထောက်ပံ့သည်။

### API Call များ Debugging

**ပြဿနာ**: Authentication error များ၊ rate limit များ သို့မဟုတ် AI မော်ဒယ်ထံမှ မမျှော်လင့်ထားသော တုံ့ပြန်မှုများ

**ဖြေရှင်းချက်**: ဥပမာများတွင် `.logRequests(true)` နှင့် `.logResponses(true)` ကို console တွင် API call များကို ပေါ်လာစေဖို့ စိစစ်သုံးသပ်မှုအတွက် ထည့်ထားသည်။ ဤအရာများသည် authentication errors, rate limits, သို့မဟုတ် မမျှော်လင့်ထားသော တုံ့ပြန်ချက်များကို ပြန်လည်ရှာဖွေရာတွင် ကူညီပေးနိုင်သည်။ Production မှာ အဲ့ဒီ flag များကို ဖယ်ရှား၍ log ရုပ်သိမ်းလိုစရာနည်းစေသည်။

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**တရားဝင်မှတ်ချက်**  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်မှုဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ တိကျမှုအတွက် ကြိုးပမ်းသည်ဖြစ်သော်လည်း အော်တိုမိတက် ဘာသာပြန်မှုများတွင် အမှားများ သို့မဟုတ် မမှန်ကန်မှုများ ပါဝင်နိုင်သည်ကို သိရှိထားပါ။ မူလစာရွက်စာတမ်းကို မူရင်းဘာသာဖြင့် စစ်မှန်သော အတည်ပြုအရင်းအမြစ်အဖြစ် သတ်မှတ်စဉ်းစားသင့်သည်။ မဟာဗျူဟာဆိုင်ရာ သတင်းအချက်အလက်များအတွက် ဖြစ်ပါက ပညာရှင် များဖြင့် လူကြီးမင်း လက်တွေ့ ဘာသာပြန်ခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုရာမှ ဖြစ်ပေါ်နိုင်သော မေးမြန်းမှားប្រစ်မှုများ သို့မဟုတ် နားလည်မှုပျက်ပြားမှုများအတွက် ကျွန်ုပ်တို့ တာဝန်မရှိကြောင်း သတိပေးအပ်ပါသည်။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->