# Module 00: မြန်ဆန်စွာ စတင်ခြင်း

## အကြောင်းအရာ ဇယား

- [နိဒါန်း](../../../00-quick-start)
- [LangChain4j ဆိုတာဘာလဲ?](../../../00-quick-start)
- [LangChain4j အခြေခံလိုအပ်ချက်များ](../../../00-quick-start)
- [လိုအပ်ချက်များ](../../../00-quick-start)
- [တပ်ဆင်ခြင်း](../../../00-quick-start)
  - [1. GitHub Token ကိုရယူရန်](../../../00-quick-start)
  - [2. Token ကိုတပ်ဆင်ရန်](../../../00-quick-start)
- [ဥပမာများကို လည်ပတ်ရန်](../../../00-quick-start)
  - [1. အခြေခံ စကားပြောခြင်း](../../../00-quick-start)
  - [2. Prompt နမူနာများ](../../../00-quick-start)
  - [3. Function ခေါ်ဆိုခြင်း](../../../00-quick-start)
  - [4. စာရွက်စာတမ်း မေးမြန်း-ဖြေကြားခြင်း (RAG)](../../../00-quick-start)
  - [5. တာဝန်ခံ AI](../../../00-quick-start)
- [ဥပမာတစ်ခုစီ ပြသမှုများ](../../../00-quick-start)
- [နောက်တစ်ဆင့်များ](../../../00-quick-start)
- [ပြဿနာဖြေရှင်းခြင်း](../../../00-quick-start)

## နိဒါန်း

ဒီ quickstart ဟာ LangChain4j နဲ့ ပြီးပြည့်စုံစွာ လည်ပတ်စေဖို့ ရည်ရွယ်ထားတာဖြစ်ပြီး LangChain4j နဲ့ GitHub Models ကို အသုံးပြုပြီး AI အပလီကေးရှင်းများ တည်ဆောက်ရာမှာ အခြေခံအချက်များကို အကျဉ်းချုပ် ဖော်ပြထားပါတယ်။ နောက် modules တွင် Azure OpenAI နဲ့ LangChain4j ကို အသုံးပြုကာ ပိုပြီး အဆင့်မြင့်အပလီကေးရှင်းများ တည်ဆောက်မှာဖြစ်ပါတယ်။

## LangChain4j ဆိုတာဘာလဲ?

LangChain4j သည် AI စွမ်းရည် ပါဝင်သော အပလီကေးရှင်းများကို တည်ဆောက်ရင်းတွင် အခက်အခဲများဖြေရှင်းပေးသော Java 라이ဘราရီတစ်ခုဖြစ်သည်။ HTTP client များနှင့် JSON parsing များကို ကိုင်တွယ်ရန် အစား သန့်ရှင်းသော Java API များဖြင့် အလုပ်လုပ်နိုင်သည်။

LangChain ၏ "chain" ဆိုသည်မှာ အစိတ်အပိုင်းများစီစဉ် ချိတ်ဆက်ခြင်းကို ရည်ညွှန်းသည်။ ဥပမာ prompt တစ်ခုကို model တစ်ခုဆီ ဆက်သွယ်၊ နောက်ဆက်တွဲ parser တစ်ခုသို့ ချိတ်ဆက်ခြင်း၊ သို့မဟုတ် အတန်းလိုက် AI ခေါ်ဆိုမှုများကို တစ်ခုထဲထွက်သော အချက်အလက်ကို နောက်ထပ် input အဖြစ် သုံးခြင်း စသည်ဖြင့်။ ဒီ quickstart မှာ အခြေခံများအပေါ် ဦးတည်ပြီး ကွဲပြားသော chain များကို နည်းနည်းအရင်လေ့လာမှာ ဖြစ်သည်။

<img src="../../../translated_images/my/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j တွင် component များ ချိတ်ဆက်ခြင်း - အဆောက်အအုံများ ချိတ်ဆက်ကာ အစွမ်းထက် AI လုပ်ငန်းစဉ်များ အတည်ပြုသည်*

ကျွန်ုပ်တို့ သုံးမည့် အဓိက component သုံးခုမှာ -

**ChatLanguageModel** - AI model နှင့် ဆက်သွယ်ရန် interface တစ်ခု။ `model.chat("prompt")` ကိုခေါ်ပြီး ဖြေကြားချက် စာကြောင်း တစ်ခု ရရှိနိုင်သည်။ ကျွန်ုပ်တို့သည် OpenAI နှင့်တိုက်ဆိုင်သော API များ (GitHub Models လည်းပါဝင်သည်) နှင့် အလုပ်လုပ်နိုင်သည့် `OpenAiOfficialChatModel` ကို အသုံးပြုသည်။

**AiServices** - Type-safe AI service interfaces များ ဖန်တီးပေးသည်။ method များကို သတ်မှတ်ပြီး `@Tool` ဖြင့် မှတ်သားပါက LangChain4j က လိုအပ်သလို စနစ်ဆွဲပေးသည်။ AI ဟာ လိုအပ်သည့်အခါ Java method များကို ကိုယ်တိုင် ခေါ်ယူသွားသည်။

**MessageWindowChatMemory** - စကားပြော အမှတ်တရ ကို ထိန်းသိမ်းသည်။ ဤ memory မရှိမဖြစ်ဖြင့် တစ်ခုချင်းစီသော မေးခွန်းတွေဟာ လွတ်လပ်ပြီး ရှေ့ဆုံး ပြောခဲ့တာကို မမှတ်မိပါ။ MessageWindowChatMemory ပါဝင်သည်နှင့် AI သည် ယခင် စကားများကို မှတ်မိကာ context ထိန်းသိမ်းထားနိုင်သည်။

<img src="../../../translated_images/my/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j architecture - အဓိက component များပေါင်းစပ်၍ သင်၏ AI အပလီကေးရှင်းများကို လုပ်ဆောင်ပေးသည်*

## LangChain4j အခြေခံလိုအပ်ချက်များ

ဒီ quickstart တွင် Maven dependency နှစ်ခုကို [`pom.xml`](../../../00-quick-start/pom.xml) မှာ အသုံးပြုသည်။

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

`langchain4j-open-ai-official` module ဟာ OpenAI-compatible API များနှင့် ဆက်သွယ်နိုင်သည့် `OpenAiOfficialChatModel` class ကို ပေးဆောင်သည်။ GitHub Models များသည် API ပုံစံ တူညီသောကြောင့် အထူး adapter မလိုတော့ပါ - base URL ကို `https://models.github.ai/inference` သို့ သတ်မှတ်ပေးရုံ ဖြစ်သည်။

## လိုအပ်ချက်များ

**Dev Container သုံးနေပါသလား?** Java နှင့် Maven များက တပ်ဆင်ပြီးသား ဖြစ်ပြီး GitHub Personal Access Token တစ်ခုသာ လိုအပ်သည်။

**Local Development:**
- Java 21+ နှင့် Maven 3.9+
- GitHub Personal Access Token (အောက်တွင် အသေးစိတ် ဖော်ပြထားသည်)

> **မှတ်ချက် -** ဒီ module မှာ GitHub Models ၏ `gpt-4.1-nano` ကို အသုံးပြုထားသည်။ ကုဒ်အတွင်း မော်ဒယ်အမည်ကို ပြောင်းလဲမပြင်ပါနှင့် - GitHub ရှိ မော်ဒယ်များနှင့် ပတ်သက်သည့် စနစ်ဖြင့် လုပ်ဆောင်ထားသောကြောင့် ဖြစ်သည်။

## တပ်ဆင်ခြင်း

### 1. GitHub Token ကိုရယူရန်

1. [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens) သို့ သွားပါ
2. "Generate new token" ကို နှိပ်ပါ
3. အမည်တစ်ခု သတ်မှတ်ပေးပါ (ဥပမာ - "LangChain4j Demo")
4. သက်တမ်းသတ်မှတ်ပါ (7 ရက် အကြံပြုသည်)
5. "Account permissions" အောက်တွင် "Models" ကို "Read-only" သတ်မှတ်ပါ
6. "Generate token" ကို နှိပ်ပါ
7. Token ကို ကူးယူထားပြီး ပြန်မမြင်ရတော့ပါ

### 2. Token ကိုတပ်ဆင်ရန်

**ရွေးချယ်မှု ၁: VS Code အသုံးပြုခြင်း (အကြံပြုသည်)**

VS Code အသုံးပြုပါက project အခြေခံ `.env` ဖိုင်ထဲသို့ သင်၏ Token ကို ထည့်သည်။

`.env` ဖိုင် မရှိလျှင် `.env.example` ကို ကူးယူ၍ `.env` အမည်ဖြင့်ထားပါ၊ မဟုတ်လျှင် project အခြေခံတွင် `.env` ဖိုင်သစ် ဖန်တီးပါ။

**ဥပမာ `.env` ဖိုင်:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env မှာ
GITHUB_TOKEN=your_token_here
```

ထို့နောက် Explorer မှ မည်သည့် demo ဖိုင်ကိုမဆို တစ်ချက်နှိပ်ပြီး **"Run Java"** ကို ရွေးချယ်နိုင်ပြီး Run and Debug panel မှာ ပါသော launch configurations များကို အသုံးပြုနိုင်သည်။

**ရွေးချယ်မှု ၂: Terminal သုံးခြင်း**

Token ကို environment variable အဖြစ် သတ်မှတ်ပါ -

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## ဥပမာများကို လည်ပတ်ရန်

**VS Code အသုံးပြုပါက:** Explorer တွင် မည်သည့် demo ဖိုင်ကိုမဆို right-click လုပ်ပြီး **"Run Java"** ရွေးချယ်ပါ၊ ဒါမှမဟုတ် Run and Debug panel မှ launch configurations များကို အသုံးပြုပါ (Token ကို `.env` မှာ ထည့်ထားပြီးဖြစ်ရပါမည်)။

**Maven အသုံးပြုပါက:** Command line မှ အောက်ပါအတိုင်း လည်ပတ်နိုင်သည်-

### 1. အခြေခံ စကားပြောခြင်း

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. Prompt နမူနာများ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

zero-shot, few-shot, chain-of-thought နှင့် role-based prompting များ ပြသထားသည်။

### 3. Function ခေါ်ဆိုခြင်း

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

လိုအပ်သည့်အခါ AI က ကိုယ့် Java method များကို ကိုယ်တိုင် ခေါ်ယူသည်။

### 4. စာရွက်စာတမ်း မေးမြန်း-ဖြေကြားခြင်း (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt` ထဲရှိ အကြောင်းအရာအပေါ် မေးခွန်းများ မေးနိုင်သည်။

### 5. တာဝန်ခံ AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI ဘေးကင်းမှု filter များက အန္တရာယ်မှ content များကို ဘာသာစကားဖြင့် ကာကွယ်ပေးသည်။

## ဥပမာတစ်ခုစီ ပြသမှုများ

**အခြေခံ စကားပြောခြင်း** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j ၏ အခြေခံကို ဒီနေရာမှာ စတင်မြင်ရပါမယ်။ `OpenAiOfficialChatModel` တည်ဆောက်ပြီး `.chat()` ဖြင့် prompt ပို့ကာ ဖြေကြားချက် မက်ဆေ့ချ် ရယူခြင်းကို ပြသသည်။ မော်ဒယ်သတ်မှတ်ခြင်း၊ custom endpoint များ နှင့် API key များ ဖြင့် စတင်အသုံးပြုနည်းကို ဖော်ပြထားသည်။ ဒီနမူနာကို နားလည်ပြီးနောက် အခြား အရာများကို သင်ယူနိုင်ပါမယ်။

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ကြည့်ပါ:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) ကိုဖွင့်ပြီး မေးမြန်းပါ-
> - "GitHub Models မှ Azure OpenAI သို့ ဘယ်လိုပြောင်းလဲရမလဲ?"
> - "OpenAiOfficialChatModel.builder() ထဲမှာ ဘယ်လို parameter တွေ configure လုပ်လို့ရပါသလဲ?"
> - "Streaming response တွေကို စောင့်မနေဘဲ ဘယ်လို ထည့်သွင်းရမလဲ?"

**Prompt Engineering** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

အခု သင်ဟာ မော်ဒယ်နဲ့ စကားပြောတယ်ဆိုတော့ ဘာနည်းလမ်း နဲ့ ပြောဆိုရမလဲဆိုတာ ရှာဖွေကြမယ်။ ဒီ demo မှာ မော်ဒယ်တူညီပုံစံဖြင့် ဟာ လုပ်ပုံမှန်ဖြစ်နေပြီး prompt patterns ငါးမျိုးကို ပြသထားသည်။ zero-shot တိုက်ရိုက်ညွှန်ကြားမှု၊ few-shot နမူနာများမှ သင်ကြားမှု, chain-of-thought အတွေးလမ်းကြောင်းဖော်ပြမှု၊ role-based prompting စသည်ဖြင့် လုပ်ဆောင်မှုဖြစ်သည်။ မည်သို့ ပြုပြင်၍ မော်ဒယ်မှ ထွက်ရှိချက်မတူညီမှု သိရပါမည်။

ဒီ demo သည် prompt templates များကိုလည်း ပြသထားသည်။ PromptTemplate သည် စွမ်းဆောင်ရည်မြင့်နှင့် variable များဖြင့် ပြန်လည်အသုံးပြုနိုင်သော prompt များ ဖန်တီးပုံဖြစ်သည်။
အောက်မှာ LangChain4j ၏ `PromptTemplate` ကို အသုံးပြု၍ variable များထည့်သွင်းထားသော prompt ပုံစံတစ်ခုကို ဥပမာပြထားသည်။ AI က မြို့နယ်နှင့် လှုပ်ရှားမှုရပ်များအပေါ် အခြေခံ၍ ဖြေကြားမည်ဖြစ်သည်။

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ကြည့်ပါ:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) ကိုဖွင့်ပြီး မေးပါ-
> - "zero-shot နဲ့ few-shot prompting ကြားဘာကွာခြားချက်ရှိသလဲ၊ ဘယ်မှာ ဘာသုံးသင့်လဲ?"
> - "temperature parameter ကို မော်ဒယ်ထွက်လေ့လာမှုဘယ်လိုသက်ရောက်သလဲ?"
> - "prompt injection တိုက်ခိုက်မှု ကာကွယ်နည်း ဘာတွေရှိသလဲ?"
> - "ပုံမှန်မျိုးကို ပြန်လည်အသုံးပြုနိုင်သော PromptTemplate နည်းလမ်းများ ပြုလုပ်နည်း ဘာလဲ?"

**Tool Integration** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ဒီနေရာမှာ LangChain4j သည် ပြင်းထန်သည်။ AiServices ကိုအသုံးပြု၍ Java method များကို ခေါ်ယူနိုင်သော AI အကူ助手 တစ်ခု ဖန်တီးမည်။ method များတွင် `@Tool("ဖော်ပြချက်")` ကို မှတ်သားပေးရုံအစား LangChain4j မှ တာဝန်ယူပြုစုပေးသည် - user မေးလိုက်တော့ AI က tool တစ်ခုချင်းစီကို အလိုအလျောက်ခေါ်ယူသည်။ ဒီနမူနာတွင် function calling ဆိုသည်မှာ AI အရာများကို လုပ်ဆောင်စေရန်နည်းလမ်း ဖြစ်ပြီး မေးခွန်းတို့ကိုသာ ဖြေကာမဟုတ်ဘဲ လုပ်ဆောင်ချက်ယူပေးသည်။

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ကြည့်ပါ:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ကိုဖွင့်ပြီး မေးပါ-
> - "@Tool annotation ဘယ်လိုအလုပ်လုပ်ပြီး LangChain4j က ဘာလုပ်ကြသလဲ?"
> - "AI က tool များစွာကို အဆက်မပြတ်ဖြေရှင်းနိုင်လား?"
> - "Tool က Exception လွှတ်တင်ခဲ့ရင် ဘယ်လို error များကို ကိုင်တွယ်မလဲ?"
> - "ဒီ calculator နမူနာ အစား API တကယ်ကို ဘယ်လိုပေါင်းချိတ်မလဲ?"

**စာရွက်စာတမ်း မေးမြန်း-ဖြေကြားခြင်း (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

ဒီနေရာမှာ RAG (retrieval-augmented generation) အခြေခံ ကို မြင်ရမယ်။ မော်ဒယ် ကျွမ်းကျင်မှု ဒေတာအစား `document.txt` ထဲရှိ အကြောင်းအရာကို prompt တွင် ထည့်သွင်းထားပြီး AI ကို အခြေခံအနေဖြင့် ဖြေကြားပေးသည်။ ဒီ စနစ်သည် သင်၏တည်ဆောက်ထားသော ဒေတာကို အသုံးပြုကာ လုပ်ဆောင်နိုင်သည်။

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **မှတ်ပုံတွေ့:** ဒီနည်းလမ်းအရ စာရွက်စာတမ်းအားလုံးကို prompt ထဲသို့ ထည့်သွင်းသည်။ ဖိုင်ကြီးများ (>10KB) အတွက် context ကန့်သတ်ချက်ကျော်လွန်နိုင်သည်။ Module 03 မှာ chunking နဲ့ vector search ကို အသေးစိတ်ရှင်းပြပါမည်။

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ကြည့်ပါ:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) ကိုဖွင့်ပြီး မေးပါ-
> - "RAG က AI hallucination ကာကွယ်ပေးမှု မော်ဒယ် ကျွမ်းကျင်မှုဒေတာအသုံးပြုမှုထက် ဘယ်လိုကွာခြားသလဲ?"
> - "ဒီနည်းနှင့် vector embeddings အသုံးပြုမှု ကြားကွာခြားချက်ဘာလဲ?"
> - "စာရွက်စာတမ်း များစွာသိုလှောင်ရန် သို့မဟုတ် အသိပညာအဆောက်အအုံကြီးများ များစွာကို ဘယ်လိုလည်ပတ်သင့်သလဲ?"
> - "AI သည် ရေးထိုးသော context ကိုသာ အသုံးပြုဖို့ prompt ဖွဲ့စည်းမှုအတွက် အကောင်းဆုံးနည်းလမ်းဘာလဲ?"

**တာဝန်ခံ AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

AI လုံခြုံရေးကို တပ်ဆင်ပါ။ ဒီ demo မှာ ကာကွယ်မှု အလွှာ နှစ်ခုကို ဖော်ပြထားသည်။

**ပိုင်း ၁: LangChain4j Input Guardrails** - LLM ထံ မပို့မီ အန္တရာယ်ရှိသော prompts များကို ပိတ်ပင်သည်။ custom guardrails များ ဖန်တီး၍ မလိုအပ်သော keyword များ သို့မဟုတ် pattern များကို စစ်ဆေးကြသည်။ ဤ guardrails များ code တွင် ဆောင်ရွက်သဖြင့် မြန်ဆန်ပြီး အခမဲ့ဖြစ်သည်။

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

**ပိုင်း ၂: Provider Safety Filters** - GitHub Models ရဲ့ built-in filter များသည် guardrails မတွေ့ရသေးသောအရာများကို ဖမ်းဆီးသည်။ ပြစ်ဒဏ်စိုးရိမ်ဖွယ်မူကြီးများအတွက် hard blocks (HTTP 400 error များ) နှင့် AI က သေချာ ငြင်းဆန်သည့် soft refusals များကို ပြသသည်။

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ကြည့်ပါ:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) ကိုဖွင့်ပြီး မေးပါ-
> - "InputGuardrail ဆိုတာဘာလဲ၊ ကိုယ့်ကိုယ်ကို ဘယ်လိုဖန်တီးမလဲ?"
> - "hard block နဲ့ soft refusal ကြားကွာခြားချက် ဘာလဲ?"
> - "guardrails နဲ့ provider filters ကို အတူသုံးရခြင်း အကျိုး နေရာရောပါသလဲ?"

## နောက်တစ်ဆင့်များ

**နောက်တစ် module:** [01-introduction - LangChain4j နှင့် gpt-5 အတွက် Azure တွင် စတင်ခြင်း](../01-introduction/README.md)

---

**Navigation:** [← မူလစာမျက်နှာသို့ ပြန်သွားရန်](../README.md) | [နောက်တစ် module: Module 01 - နိဒါန်း →](../01-introduction/README.md)

---

## ပြဿနာဖြေရှင်းခြင်း

### ပထမဆုံး Maven Build

**ပြဿနာ**: ပထမဆုံး `mvn clean compile` သို့မဟုတ် `mvn package` လည်ပတ်စဉ် အချိန်ကြာသည် (10-15 မိနစ်)

**အကြောင်းအရင်း**: Maven သည် ပထမဆုံးသုံးတွင် စီမံကိန်းလိုအပ်ချက်များ (Spring Boot, LangChain4j libraries, Azure SDK များ) ကို ဒေါင်းလုပ်လုပ်ရန် လိုအပ်သည်။

**ဖြေရှင်းနည်း**: ဤအပြုအမူသည် သဘာဝဖြစ်ပုံ ဖြစ်သည်။ နောက်တစ်ကြိမ်များ ကျော်သွားလျှင် ဒေါင်းလုပ်ရတတ်သောအရာများကို cache ထားပြီး ပိုမြန်သည်။ ဒေါင်းလုပ်အချိန်သည် သင့်ကွန်ရက် မြန်နှုန်းပေါ် မူတည်သည်။
### PowerShell Maven အမိန့် စာနယ်ဇင်း

**ပြသနာ**: Maven အမိန့်များသည် error `Unknown lifecycle phase ".mainClass=..."` ဖြင့် မအောင်မြင်ခြင်း

**အကြောင်းရင်း**: PowerShell သည် `=` ကို variable သတ်မှတ်ခြင်း operator အဖြစ် ထောက်လှမ်းကာ Maven property စာ法ကို ခြေဖျက်သည်

**ဖြေရှင်းနည်း**: Maven အမိန့်မတိုင်မှီ stop-parsing operator `--%` ကို အသုံးပြုပါ။

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` operator သည် PowerShell ကို လက်ကျန် argument များအားလုံးကို အဓိပ္ပါယ်မပြောင်းလဲဘဲ Maven ဆီသို့ တိုက်ရိုက် ပေးပို့ရန် အားပေးသည်။

### Windows PowerShell Emoji ပြသမှု

**ပြသနာ**: PowerShell တွင် AI တုန့်ပြန်ချက်များသည် emoji များအစား အချို့ ပျက်ပြီး စာလုံးများ (ဥပမာ `????` သို့မဟုတ် `â??`) ပြသခြင်း

**အကြောင်းရင်း**: PowerShell ၏ ပုံမှန် အင်ကုဒင်းစနစ်သည် UTF-8 emoji များကို ထောက်ပံ့မှုမရှိခြင်း

**ဖြေရှင်းနည်း**: Java application များ ပြေးသည့် မတိုင်မှီ ဤ command ကို အကောင်အထည်ဖော်ပါ။
```cmd
chcp 65001
```

ဤကိစ္စသည် terminal တွင် UTF-8 အင်ကုဒင်းအား ဖိအားပေးပါသည်။ မဟုတ်လျှင် Unicode ကို ပိုမိုထောက်ပံ့သော Windows Terminal ကို အသုံးပြုပါ။

### API Call များကို စစ်ဆေးခြင်း

**ပြသနာ**: Authentication error များ၊ rate limit များ သို့မဟုတ် မမျှော်လင့်သော တုန့်ပြန်ချက်များ AI မော်ဒယ်ထံမှ ရရှိခြင်း

**ဖြေရှင်းနည်း**: ဥပမာများတွင် `.logRequests(true)` နှင့် `.logResponses(true)` ကို console တွင် API call များ ပြသရန် ထည့်သွင်းထားသည်။ ယင်းသည် authentication error များ၊ rate limit များ သို့မဟုတ် မမျှော်လင့်သော တုန့်ပြန်ချက်များကို ပြဿနာရှာဖွေရန် အထောက်အကူပြုသည်။ ထုတ်လုပ်ရေးတွင် အဆိုပါ flag များကို ဖယ်ရှားရန် log အငြင်းပွားမှုကို လျှော့ချပေးရန် ဖြစ်သည်။

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**အကြောင်းကြားချက်**  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်မှုဆော့ဖ်ဝဲဖြစ်သည့် [Co-op Translator](https://github.com/Azure/co-op-translator) အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည်မှန်ကန်မှုအတွက် ကြိုးပမ်းထားသော်လည်း၊ အလိုအလျောက် ဘာသာပြန်မှုတွင် အမှားများ သို့မဟုတ် မှန်ကန်မှုမရှိမှုများ ပါဝင်နိုင်ကြောင်း သတိပြုပါရန် မေတ္တာရပ်ခံအပ်ပါသည်။ မူရင်းစာရွက်စာတမ်းကို မိမိဘာသာစကားဖြင့် စီးပွားရေးအချက်အလက်အဖြစ် သတ်မှတ်သင့်ပါသည်။ အရေးကြီးသော သတင်းအချက်အလက်များအတွက် သမားရိုးကျ လူသားဘာသာပြန်ခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုသောကြောင့် ဖြစ်ပေါ်လာသော ငြင်းပယ်မှုများ သို့မဟုတ် မမှန်ကန်သော နားလည်မှုများအတွက် ကျွန်ုပ်တို့ တာဝန်မကျေပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->