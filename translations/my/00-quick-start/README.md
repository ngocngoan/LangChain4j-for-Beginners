# Module 00: အလျင်အမြန် စတင်ခြင်း

## Table of Contents

- [နိဒါန်း](../../../00-quick-start)
- [LangChain4j ဆိုတာ ဘာလဲ?](../../../00-quick-start)
- [LangChain4j မှာ လိုအပ်တဲ့ ကုဒ်များ](../../../00-quick-start)
- [လိုအပ်ချက်များ](../../../00-quick-start)
- [တပ်ဆင်ခြင်း](../../../00-quick-start)
  - [1. ကိုယ်ပိုင် GitHub Token ရယူရန်](../../../00-quick-start)
  - [2. ကိုယ်ပိုင် Token မှတ်ပုံတင်ရန်](../../../00-quick-start)
- [ဥပမာများ ကို လုပ်ဆောင်ရန်](../../../00-quick-start)
  - [1. အခြေခံ စကားပြော](../../../00-quick-start)
  - [2. Prompt ပုံစံများ](../../../00-quick-start)
  - [3. Function အမိန့်ခေါ်ခြင်း](../../../00-quick-start)
  - [4. စာရွက်စာတမ်း Q&A (Easy RAG)](../../../00-quick-start)
  - [5. တာဝန်ယူပြီး AI](../../../00-quick-start)
- [ဥပမာတိုင်း ဖော်ပြထားသည့်အချက်များ](../../../00-quick-start)
- [နောက်တတ်လမ်းများ](../../../00-quick-start)
- [ပြဿနာပြုပြင်ခြင်း](../../../00-quick-start)

## နိဒါန်း

ဒီ quickstart သည် LangChain4j နှင့် အဆင်ပြေမြန်ဆန်စွာ စတင်အသုံးပြုနိုင်ရန်ရည်ရွယ်သည်။ LangChain4j နှင့် GitHub Models အသုံးပြုပြီး AI အပလီကေးရှင်းများ ဖန်တီးခြင်း၏ အခြေခံအချက်များသာ ပါဝင်သည်။ နောက် Module များတွင် Azure OpenAI နှင့် LangChain4j ကို အသုံးပြုကာ ပိုမိုတိုးတက်သော အပလီကေးရှင်းများ တည်ဆောက်ပါမည်။

## LangChain4j ဆိုတာ ဘာလဲ?

LangChain4j သည် AI ပါဝင်သော အပလီကေးရှင်းများ တည်ဆောက်ရာတွင် ရိုးရှင်းအဆင်ပြေ သော Java 라이ဘရာရီတစ်ခု ဖြစ်သည်။ HTTP client များနှင့် JSON ပားစင်အား ကိုင်တွယ်ရန် မလိုဘဲ သန့်ရှင်းသော Java API များဖြင့် လုပ်ဆောင်နိုင်သည်။

LangChain ၏ “chain” ဆိုသည်မှာ အစိတ်အပိုင်းများကို တန်းတူချိတ်ဆက်ခြင်းဖြစ်သည်။ ဥပမာ prompt ကို model သို့ parser သို့ချိတ်ဆက်ခြင်း၊ ဒါမှမဟုတ် AI ခေါ်ဆိုမှုများစွာကို တန်းစီချိတ်ဆက်၍ တစ်ခု၏ output ကို နောက်ထပ် input အဖြစ် သုံးပြီး ဆက်လက် လုပ်ဆောင်ခြင်းတို့ ဖြစ်နိုင်သည်။ ဒီ quick start မှာ အခြေခံအချက်များအပေါ် ဂရုစိုက်ပြီး ပိုမိုရှုပ်ထွေးသော chain များကို မကြာမီ လေ့လာပါမည်။

<img src="../../../translated_images/my/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j မှာ component များကို ချိတ်ဆက်ပုံ - အင်အားကြီး AI လုပ်ငန်းစဉ်များ ဖန်တီးခြင်းအတွက် အဆောက်အအုံ*

ကျွန်ုပ်တို့ ယခု သုံးမည့် အဓိက component သုံးခုမှာ -

**ChatModel** - AI model နှင့် ကွက်တိခွဲဆက်ဆံမှုအတွက် interface ပါ။ `model.chat("prompt")` ကို ခေါ်ပြီး တုံ့ပြန်မှု string ရယူနိုင်သည်။ OpenAI နှင့် ကိုက်ညီသည့် endpoints တွင် အသုံးပြုနိုင်သည့် `OpenAiOfficialChatModel` ကို သုံးသည်။

**AiServices** - အမျိုးအစားလုံခြုံသော AI ဝန်ဆောင်မှု interface များ ဖန်တီးပေးသည်။ method များကို `@Tool` ဖြင့် အမှတ်အသားပြု၍ LangChain4j သည် orchestration ကို ကိုင်တွယ်ပေးသည်။ AI သည် လိုအပ်သလို သင့် Java method များကို အလိုအလျောက် ခေါ်ယူပေးသည်။

**MessageWindowChatMemory** - စကားပြောသမိုင်းကို သိမ်းဆည်းထားပေးသည်။ မရှိလျှင် တစ်ခုချင်းစီ အဆိုပြုချက်များသည် အချိုးမျှမရှိပါ။ ရှိလျှင် AI သည် ယခင်စာများကို မှတ်မိပြီး စိတ်ဝင်စားရန်အဆိုပြုချက်များကို ကြိုးပမ်းဆက်လက် ထိန်းသိမ်းပေးသည်။

<img src="../../../translated_images/my/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j architecture - AI application များအား အဓိက component များ ပေါင်းစပ်လုပ်ဆောင်ခြင်း*

## LangChain4j မှာ လိုအပ်တဲ့ ကုဒ်များ

ဒီ quick start တွင် Maven သုံးခုကို [`pom.xml`](../../../00-quick-start/pom.xml) မှ တင်ပြထားသည်-

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

`langchain4j-open-ai-official` module မှ `OpenAiOfficialChatModel` class ကို ပေးပြီး OpenAI နှင့် ကိုက်ညီသည့် API များနှင့် ဆက်သွယ်သည်။ GitHub Models သည် API ပုံစံတူဖြစ်သောကြောင့် မည်သည့် အထူး adapter မလိုပဲ `https://models.github.ai/inference` ကို base URL အဖြစ် သတ်မှတ်ခြင်းဖြင့် အသုံးပြုနိုင်သည်။

`langchain4j-easy-rag` module မှ စာရွက်စာတမ်းဖြတ်ခြင်း၊ embedding နှင့် retrieval များကို အလိုအလျောက် ပြုလုပ်ပေး၍ RAG application များကို လက်ဖြင့်တစ်ဆင့်ချုပ်ရန် မလိုပဲ ဖန်တီးနိုင်သည်။

## လိုအပ်ချက်များ

**Dev Container အသုံးပြုပါသလား?** Java နှင့် Maven များ အပြီးအစီး ထည့်သွင်းထားပြီး ဖြစ်သည်။ GitHub Personal Access Token တစ်ခုသာ လိုအပ်သည်။

**Local Development:**
- Java 21+ ၊ Maven 3.9+
- GitHub Personal Access Token (အောက်ရှိ လမ်းညွှန်အတိုင်း)

> **မှတ်ချက်:** ဒီ module တွင် GitHub Models မှ `gpt-4.1-nano` ကို အသုံးပြုသည်။ လိုအပ်ချက်မရှိဘဲ ကိုးဒ်ထဲ၊ model name ကို ပြင်ဆင်မလုပ်ပါနှင့်- GitHub Models မှ အသုံးပြုနိုင်သော model များနှင့် သင့်တော်အောင် အသင့်တော်သောနေရာမှာ 이미 설정되어 있습니다။

## Setup

### 1. ကိုယ်ပိုင် GitHub Token ရယူရန်

1. [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens) သို့ သွားပါ
2. "Generate new token" ကို နှိပ်ပါ
3. အသုံးပြုရန် အမည်တစ်ခုပေးပါ (ဥပမာ "LangChain4j Demo")
4. သက်တမ်း သတ်မှတ်ပါ (7 ရက် အကြံပြု)
5. "Account permissions" အောက် "Models" ကို ရွေးပြီး "Read-only" သတ်မှတ်ပါ
6. "Generate token" ကို နှိပ်ပါ
7. token ကို မျက်နှာပြင်ပေါ် တွေ့ရမည့် အချိန် တစ်ကြိမ်တည်းဖြစ်သောကြောင့် ကူးယူပြီး သိမ်းဆည်းထားပါ

### 2. ကိုယ်ပိုင် Token မှတ်ပုံတင်ရန်

**ရွေးချယ်စရာ 1 - VS Code အသုံးပြုခြင်း (အကြံပြု)**

VS Code အသုံးပြုပါက project root တွင် `.env` ဖိုင်ထဲတွင် token ထည့်ပါ-

`.env` ဖိုင် မရှိပါက `.env.example` ကို `.env` ဟု ကူးယူပါ၊ သို့မဟုတ် project root တွင် အသစ်တစ်ခု ဖန်တီးပါ။

**ဥပမာ `.env` ဖိုင်:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env တွင်
GITHUB_TOKEN=your_token_here
```

ထို့နောက် Explorer တွင် မည်သည့် demo ဖိုင် (ဥပမာ `BasicChatDemo.java`) ကို မာယာကိုနှိပ်ပြီး **"Run Java"** ရွေးပါ။ သို့မဟုတ် Run and Debug panel မှ launch configuration များကို အသုံးပြုနိုင်သည်။

**ရွေးချယ်စရာ 2 - Terminal အသုံးပြုခြင်း**

Token ကို environment variable အဖြစ် သတ်မှတ်ပါ-

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## ဥပမာများ ကို လုပ်ဆောင်ရန်

**VS Code အသုံးပြုခြင်း:** Explorer တွင် demo ဖိုင် တစ်ခုအား မာယာကို နှိပ်ပြီး **"Run Java"** ရွေးပါ၊ သို့မဟုတ် Run and Debug panel မှ launch config များကို သုံးပါ (token ကို `.env` ဖိုင်ထဲ ထည့်ထားပြီးဖြစ်ရန် သေချာပါစေ)။

**Maven အသုံးပြုခြင်း:** Command line မှလည်း အောက်ပါအတိုင်း လုပ်ဆောင်နိုင်သည်-

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

Zero-shot၊ few-shot၊ chain-of-thought နှင့် role-based prompting များ ပြသထားသည်။

### 3. Function အမိန့်ခေါ်ခြင်း

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI သည် လိုအပ်သလို သင့် Java method များကို အလိုအလျောက်ခေါ်သည်။

### 4. စာရွက်စာတမ်း Q&A (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

စာရွက်စာတမ်းများအပေါ် မေးခွန်းများ မေးနိုင်ရန် Easy RAG နည်းလမ်းဖြင့် အလိုအလျောက် embedding နှင့် retrieval ပြုလုပ်သည်။

### 5. တာဝန်ယူပြီး AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI လုံခြုံရေး စစ်ဆေးမှုများကြောင့် အန္တရာယ်ရှိသော အကြောင်းအရာများကို ကျိတ်လိုက်ပုံကို ကြည့်ရှုပါ။

## ဥပမာတိုင်း ဖော်ပြထားသည့်အချက်များ

**အခြေခံ စကားပြော** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j ကို အခြေခံပုံစံဖြင့် စတင်ကြည့်ရှုရန်။ `OpenAiOfficialChatModel` တည်ဆောက်ပြီး `.chat()` ဖြင့် prompt ပို့ကာ တုံ့ပြန်မှုရယူသည်။ ဤသည်သည် custom endpoint များနှင့် API key များဖြင့် model များ initialized လုပ်နည်းကို ပြသသော အခြေခံ ဖြစ်သည်။ ဒီပုံစံကို နားလည်လိုက်ရင် နောက်ပိုင်း အရာအားလုံးလည်း ထူထောင်နိုင်ပါသည်။

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat နှင့် ကြိုးစားကြည့်ရန်:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ဖြင့် ဖွင့်ပြီး မေးမြန်းပါ-
> - "ဒီ code မှ GitHub Models ကနေ Azure OpenAI ကို ဘယ်လိုပြောင်းမလဲ?"
> - "OpenAiOfficialChatModel.builder() မှာ ဘာ parameter တွေ configure လုပ်လို့ရလဲ?"
> - "တုံ့ပြန်မှု ပြည့်စုံစောင့်ရှောက်စေဖို့ streaming responses ဖြည့်စွက် ဘယ်လိုလုပ်မလဲ?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

Model နှင့် စကားပြောနည်းအတိုင်း သိသွားရင်၊ ယူဆချက်ပုံစံတွေကို လေ့လာကြမယ်။ ဤ demo တွင် အတူတူ model setup ကို သုံးပြီး prompt အမျိုးမျိုး(၅) များကို ပြသထားသည်။ zero-shot prompts ဖြင့် တိုက်ရိုက် ညွှန်ကြားချက်များ၊ few-shot prompts ဖြင့် ဥပမာများမှ သင်ယူခြင်း၊ chain-of-thought prompts ဖြင့် တွေးခေါ်မှုအဆင့်များဖော်ပြခြင်း၊ role-based prompts ဖြင့် context သတ်မှတ်ခြင်းတို့ပါဝင်သည်။ ခုနကတော့ model တူပေမယ့် prompt ပုံစံအလိုက် ရလာဒ်က အတော်ကွာခြားမှု ရှိတယ်ဆိုတာ တွေ့မြင်ရပါမယ်။

ဒီ demo မှာ prompt template များလည်း ပြသထားပြီး ဥပမာအီဒီယာနဲ့ ပြန်လည် အသုံးပြုနိုင်သော prompt များ ဖန်တီးသည်။

အောက်တွင် LangChain4j `PromptTemplate` ကို သုံးပြီး variable များ ဖြည့်သွင်းထားသော prompt ကို နမူနာပြထားသည်။ AI သည် ပေးထားသော destination နှင့် activity အရ တုံ့ပြန်ပေးမည်။

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat နှင့် ကြိုးစားကြည့်ရန်:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ဖြင့် ဖွင့်ပြီး မေးမြန်းပါ-
> - "zero-shot နဲ့ few-shot prompting မတူတာ ဘာလဲ၊ ဘယ်တော့သုံးသင့်လဲ?"
> - "temperature parameter က model တုံ့ပြန်မှုကို ဘယ်လိုသက်ရောက်သလဲ?"
> - "production မှာ prompt injection ရှောင်ရန် နည်းလမ်းများ ဘာတွေရှိသလဲ?"
> - "Common patterns များအတွက် PromptTemplate objects ကို ပြန်လည်အသုံးပြုဖို့ ဘယ်လိုဖန်တီးမလဲ?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

LangChain4j ၏ အထူးအားသာချက် တစ်ခု ဖြစ်သည်။ `AiServices` ကို အသုံးပြုကာ AI မိတ်ဆက်သူတစ်ဦး ဖန်တီးနိုင်သည်။ သင့် Java method များကို `@Tool("ဖော်ပြချက်")` ဖြင့် အမှတ်အသားဖြစ်အောင် သတ်မှတ်လိုက်ပါ။ LangChain4j သည် user မေးခွန်းအရ tool များကို သုံးရန်ဆုံးဖြတ်ပေးသည်။ ဤနည်းလမ်းသည် လုပ်ဆောင်ချက် ခေါ်ယူမှု နည်းစနစ်တစ်ခုဖြစ်ပြီး AI ကို မေးခွန်းများသာ မဖြေသည့် နည်းလမ်းတစ်ခု ဖြစ်သည်။

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat နှင့် ကြိုးစားကြည့်ရန်:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ဖြင့် ဖွင့်ပြီး မေးမြန်းပါ-
> - "@Tool အမှတ်အသားက ဘယ်လို အလုပ်လုပ်သလဲ၊ LangChain4j က အဖွဲ့အစည်းကို ဖော်ပြပါ။"
> - "AI သည် မျိုးစုံသော tool များကို အစဉ်လိုက်ခေါ်ပြီး ခက်ခဲတဲ့ ပြဿနာများကို ဖြေရှင်းနိုင်မလား?"
> - "Tool မှ exception တက်လာရင် အဘယ်သို့ ကုစားသင့်သလဲ?"
> - "ဒီ calculator ဥပမာအစား စစ်မှန်သော API နဲ့ ဂဟေဆက်ရန် ဘယ်လိုလုပ်မလဲ?"

**စာရွက်စာတမ်း Q&A (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

LangChain4j ၏ Easy RAG နည်းလမ်းဖြင့် RAG (retrieval-augmented generation) ကို မြင်တွေ့ရမည်။ စာရွက်စာတမ်းများကို ပေးပြီး အလိုအလျောက် ဖြတ်ခြားခြင်း၊ embedding ပြုလုပ်၍ memory တွင် သိမ်းဆည်းထားသည်။ ထို့နောက် content retriever တစ်ခုမှ AI ထံ မေးခွန်းစဉ်အချိန်တွင် သင့်တော်သော အပိုင်းများကို ထုတ်ပေးသည်။ AI သည် ထိုစာရွက်စာတမ်းအပေါ် မူတည်၍ မေးခွန်းများ တုံ့ပြန်သည်၊ ၎င်း၏ ယေဘုယျ သတင်းအချက်အလက် မဟုတ်ပါ။

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat နှင့် ကြိုးစားကြည့်ရန်:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ဖြင့် ဖွင့်ပြီး မေးမြန်းပါ-
> - "RAG သည် AI hallucinations မဖြစ်စေရန် မူလ model training data သုံးခြင်းနှင့် ဘယ်လိုကွာခြားသလဲ?"
> - "ဒီ Easy method နဲ့ custom RAG pipeline မတူတာ ဘာလဲ?"
> - "စာရွက်စာတမ်း များစွာ သို့မဟုတ် သိပ္ပံဗဟုသုတ ကဏ္ဍ ကြီးများ ဖြစ်စေရန် ဘယ်လိုခန့်မှန်းမလဲ?"

**တာဝန်ယူပြီး AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

AI လုံခြုံရေးကို အဆင့်ဆင့်ကာကွယ်မှုဖြင့် တည်ဆောက်ပါ။ ဒီ demo သည် လုံခြုံရေး များလွှာ ၂ ဆင့် အတူ လုပ်ဆောင်မှုကို ဖော်ပြသည်။

**အပိုင်း ၁ - LangChain4j Input Guardrails** - အန္တရာယ်ရှိသော prompt များကို LLM သို့ မသွားမီ ပိတ်ဆို့ခြင်း။ custom guardrails များ ဖန်တီးကာ တားမြစ်ချက် keyword များ သို့မဟုတ် နမူနာ pattern များ စစ်ဆေးနိုင်သည်။ ဒီ guardrails များကို သင်၏ code မှာ ပြုလုပ်သည့်အတွက် မြန်နှုန်းမြင့်ပြီး အခမဲ့ဖြစ်သည်။

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

**အပိုင်း ၂ - Provider Safety Filters** - GitHub Models တွင်စနစ်တကျ ကာကွယ်စောင့်ကြည့်မှု filters ပါဝင်ပြီး သင်၏ guardrails မဖမ်းမရသော အရာများကို တားဆီးသည်။  ပြင်းထန်သော ချို့ယွင်းမှုများအတွက် hard block (HTTP 400 error) များနှင့် AI သည် နူးညံ့စွာ ငြင်းဆန်သည့် soft refusal များကို တွေ့မြင်ရမည်။

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat နှင့် ကြိုးစားကြည့်ရန်:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ဖြင့် ဖွင့်ပြီး မေးမြန်းပါ-
> - "InputGuardrail ဆိုတာဘာလဲ၊ ကိုယ်ပိုင် guardrail ဘယ်လို ဖန်တီးမလဲ?"
> - "hard block နဲ့ soft refusal ကွာခြားချက် ဘာလဲ?"
> - "guardrails နဲ့ provider filters တို့ကို တူတူအသုံးပြုရတဲ့ အကျိုးရှိချက် ဘာလဲ?"

## နောက်တတ်လမ်းများ

**နောက်တတ် Module:** [01-introduction - LangChain4j နှင့် Azure ရုပ်ပုံ gpt-5 နှင့် စတင်အသုံးပြုခြင်း](../01-introduction/README.md)

---

**နောက်ရှေ့လမ်းညွှန်:** [← နောက်သို့ Main page](../README.md) | [Next: Module 01 - နိဒါန်း →](../01-introduction/README.md)

---

## ပြဿနာပြုပြင်ခြင်း

### ပထမဆုံး Maven Build

**ပြဿနာ:** ပထမဆုံး `mvn clean compile` သို့မဟုတ် `mvn package` လိုက်လံပါက ကြာမြင့်သည် (၁၀-၁၅ မိနစ်)

**အကြောင်း:** Maven သည် ပထမဆုံး build အတွက် project အားလုံး dependency များ (Spring Boot, LangChain4j libraries၊ Azure SDKs စသည်) ကို ဒေါင်းလုပ် ဆွဲထားရမည်။

**ဖြေရှင်းနည်း:** ဒီတုန်းက အဆင်ပြေသည်။ နောက်တစ်ခါ build များတွင် dependency များ cache ထားပြီး ဖြစ်သောကြောင့် ပိုမိုလျင်မြန်ပေါ့။ ဒေါင်းလုပ်အချိန်သည် သင့်ကွန်ယက်အင်တာနက်အရှိန်ပေါ် မူတည်သည်။

### PowerShell Maven Command Syntax

**ပြဿနာ:** Maven command များ ပြုလုပ်ရာတွင် `Unknown lifecycle phase ".mainClass=..."` မှားယွင်းချက်ဖြစ်ပေါ်သည်။
**အကြောင်းအရင်း** - PowerShell သည် `=` ကို အမျိုးအစားသတ်မှတ်မှု ဂဏန်းသတ်မှတ်ချက်အဖြစ် သဘောပေါက်သော်လည်း Maven property syntax ကို ချိုးဖောက်လွန်းသည်။

**ဖြေရှင်းနည်း** - Maven command မစလုပ်ခင် stop-parsing operator `--%` ကို အသုံးပြုပါ။

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` operator သည် PowerShell အတွက် ကျန်ရှိသော argument များအားလုံးကို literal အဖြစ် Maven သို့ ဘာမှ အဓိပ္ပါယ်မပြောင်းပေးဘဲ ဖြတ်ပို့ရန် သတ်မှတ်ပေးသည်။

### Windows PowerShell Emoji ပြသမှု

**ပြဿနာ** - PowerShell တွင် emoji များအစား ကောင်းကင်အက္ခရာများ (ဥပမာ `????` သို့မဟုတ် `â??`) ပြသခြင်း

**အကြောင်းအရင်း** - PowerShell ၏ ပုံမှန် encoding သည် UTF-8 emoji များကို ထောက်ခံမှုမရှိခြင်း

**ဖြေရှင်းနည်း** - Java လုပ်ဆောင်မှုများမလုပ်ခင် ဒီ command ကို run ပါ။
```cmd
chcp 65001
```

ဤကောက်နုတ်ချက်သည် terminal တွင် UTF-8 encoding ကို စောင့်ဆောင်ပေးသည်။ ဒါမှမဟုတ် Unicode ထောက်ခံမှုကောင်းမွန်သော Windows Terminal ကို အသုံးပြုပါ။

### API ခေါ်ဆိုမှုများ အမှားရှာခြင်း

**ပြဿနာ** - Authentication errors, rate limits, သို့မဟုတ် AI မော်ဒယ်ထံမှ မမျှော်လင့်သည့်တုံ့ပြန်မှုများ

**ဖြေရှင်းနည်း** - ဥပမာများသည် `.logRequests(true)` နှင့် `.logResponses(true)` ကို ထည့်သွင်းထားပြီး console တွင် API ခေါ်ဆိုမှုများကို ပြသကူညီသည်။ ဤကိစ္စသည် authentication အမှားများ၊ rate limits သို့မဟုတ် မမျှော်လင့်သည့်တုံ့ပြန်မှု များကို ပြဿနာရှာထောက်ကူညီသည်။ ထုတ်လုပ်မှုတွင် မလိုအပ်သဖြင့် log အဆူညံသံများ လျှော့ချရန် ဤ flags များကို ဖယ်ရှားပါ။

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ပယ်ချထားချက်**:
ဤစာတမ်းကို AI ဘာသာပြန်မှုဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကိုသုံးပြီး ဘာသာပြန်ထားခြင်းဖြစ်သည်။ ကျွန်ုပ်တို့သည်တိကျမှန်ကန်မှုအတွက် ကြိုးစားသော်လည်း အလိုအလျောက် ဘာသာပြန်မှုတွင် အမှားများ သို့မဟုတ် မှားယွင်းချက်များ ပါဝင်နိုင်သည်ကို သတိပြုပါရန် လိုအပ်ပါသည်။ မူရင်းစာတမ်းသည် မူရင်းဘာသာစကားဖြင့် ရှိသည့် အတည်ပြုရင်းမြစ်အဖြစ် လက်ခံသင့်ပါသည်။ အရေးပါသော အချက်အလက်များအတွက်တော့ ပရော်ဖက်ရှင်နယ် လူ့ဘာသာပြန်မှုကို တိုက်တွန်းအပ်ပါသည်။ ဤဘာသာပြန်မှု၏ အသုံးပြုမှုကြောင့် ဖြစ်ပေါ်လာသော နားမလည်မှုများ သို့မဟုတ် မှားဖတ်နားလည်မှုများအတွက် ကျွန်ုပ်တို့သည် တာဝန်မပေးပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->