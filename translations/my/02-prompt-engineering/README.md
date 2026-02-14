# Module 02: GPT-5.2 နဲ့ Prompt Engineering

## အကြောင်းအရာ စာရင်း

- [သင်ယူမည့်အကြောင်းအရာများ](../../../02-prompt-engineering)
- [လိုအပ်ချက်များ](../../../02-prompt-engineering)
- [Prompt Engineering ကိုနားလည်ခြင်း](../../../02-prompt-engineering)
- [LangChain4j ကို ဘယ်လိုအသုံးပြုနေသလဲ](../../../02-prompt-engineering)
- [အဓိကပုံစံများ](../../../02-prompt-engineering)
- [အခုရှိပြီးသား Azure ရင်းမြစ်များ အသုံးပြုခြင်း](../../../02-prompt-engineering)
- [အပလီကေးရှင်း Screenshot များ](../../../02-prompt-engineering)
- [ပုံစံများကို စူးစမ်းလေ့လာခြင်း](../../../02-prompt-engineering)
  - [နိမ့်သော စိတ်ဓာတ်နှင့် အမြင့်သော စိတ်ဓာတ်](../../../02-prompt-engineering)
  - [တာဝန် ကျင်းပမှု (ကိရိယာ preambles)](../../../02-prompt-engineering)
  - [ကိုယ်တိုင် ပြန်လည် သုံးသပ်နေတာ](../../../02-prompt-engineering)
  - [ဖွဲ့စည်းထားသော ချဲ့ထွင်ချက်](../../../02-prompt-engineering)
  - [အတော်ကြာ နောက်ဆုံးပတ် စကားပြောဆိုမှု](../../../02-prompt-engineering)
  - [တစ်ဆင့်ချင်း စဉ်းစားခြင်း](../../../02-prompt-engineering)
  - [ကန့်သတ်ထားသော ထွက်ရှိမှု](../../../02-prompt-engineering)
- [သင်တကယ် သင်ယူနေသည့်အရာ](../../../02-prompt-engineering)
- [နောက်တစ်ဆင့်များ](../../../02-prompt-engineering)

## သင်ယူမည့်အကြောင်းအရာများ

Module အရင်က တွေ့မြင်ခဲ့သလို၊ မှတ်ဉာဏ်က စကားပြော AI ကို ကူညီပေးတာနဲ့ GitHub Models ကို အခြေခံ ဆက်သွယ်မှုများအတွက် သုံးခဲ့တာကိုပြန်လည်ကြည့်ပါ။ ယခုမှာတော့ သင်မေးမယ့် မေးခွန်းများကို အဓိကထားပါမယ်—prompt တွေကို Azure OpenAI ရဲ့ GPT-5.2 နဲ့ အသုံးပြုခြင်းဖြစ်ပြီး သင့် prompt ဖွဲ့စည်းပုံက ဆော့ဖ်ဝဲရဲ့ ဖြေကြားမှုတိကျမှုကို ထိရောက်စေပါတယ်။

GPT-5.2 ကိုအသုံးပြုမယ့်အကြောင်းက reasoning control ကို မိတ်ဆက်ပေးတာပါ—ကိုယ်မေးမယ့် မေးခွန်းမပြန်ပေးခင်မှာ စဥ်းစားမှု အဆင့်ရပ်တန့်ခွင့်နဲ့ ဖြေရှင်းပေးပါတယ်။ ဒါကြောင့် prompt နည်းလမ်းအမျိုးမျိုးရှင်းလင်းပြီး ဘယ်အချိန်အသုံးပြုစရာ ကောင်းကြောင်းသိနိုင်မယ်။ GitHub Models ထက် Azure ရဲ့ GPT-5.2 ကလည်း rate limit နည်းပြီး အသုံးပြုရ ပိုကောင်းစေပါတယ်။

## လိုအပ်ချက်များ

- Module 01 ကိုပြီးမြောက်ထားမှု (Azure OpenAI ရင်းမြစ်များ ပြင်ဆင်ပြီး)
- `.env` ဖိုင် root directory မှာ ရှိပြီး Azure အတည်ပြုချက်တွေ ပါရှိမှု (`azd up` နဲ့ Module 01 မှ ဖန်တီးထားသော)

> **မှတ်ချက်:** Module 01 မပြီးမြောက်ရင် အဲ့ဒီမှာ ပထမဦးဆုံး ဖော်ပြထားတဲ့ deployment လမ်းညွှန်ချက်တွေကို လိုက်နာပါ။

## Prompt Engineering ကို နားလည်ခြင်း

Prompt engineering ဆိုတာ သင့်ရဲ့လိုအပ်ချက်အတိုင်း အမြဲတမ်း ရလဒ်ကောင်းတွေကို လက်ရှိရရှိစေမယ့် input စာသားပုံစံ ဖန်တီးခြင်း ဖြစ်ပါတယ်။ မေးခွန်းမေးခြင်းတင်သာမက၊ မော်ဒယ်ကို သိချင်တာနဲ့ ပြန်ပေးနည်း တိတိကျကျ မှတ်သားအောင် ဖွဲ့စည်းပုံတည်ဆောက်ခြင်း ဖြစ်ပါတယ်။

အလုပ်လုပ်သူတစ်ဦးကို ညွှန်ကြားတဲ့နည်းလမ်းလိုပဲဖတ်ပါ။ "ဘယ် bug ကို ပြင်ပါ" ဆိုတာ ရှင်းလင်းမှုနည်းပါတယ်။ "UserService.java ဖိုင်တွင် လိုင်း ၄၅ က null pointer exception ကို null check ထည့်၍ ပြင်ပါ" ဆိုတာတော့ တိကျတဲ့ညွှန်ကြားချက်ဖြစ်ပါတယ်။ ဘာသာစကားမော်ဒယ်တွေကလည်း အဲ့ဒီလိုတပ်ဖွဲ့မှုနဲ့ထမ်းဆောင်ပါတယ်—တိကျမှုနဲ့ ဖွဲ့စည်းမှုက အရေးကြီးပါတယ်။

## LangChain4j ကို ဘယ်လိုအသုံးပြုနေသလဲ

ဒီ module က မတူညီတဲ့ prompting ပုံစံရှာဖွေမှုတွေကို ပြသပေးပြီး အရင် module တွေမှာ အသုံးပြုတဲ့ LangChain4j နဲ့တူတဲ့ အခြေခံ အဆောက်အအုံနဲ့ပဲ prompt ဖွဲ့စည်းပုံနဲ့ reasoning control ကို အဓိကထားပါတယ်။

<img src="../../../translated_images/my/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*LangChain4j က သင့် prompt တွေကို Azure OpenAI GPT-5.2 နဲ့ ချိတ်ဆက်ပုံ*

**အသုံးပြုထားသော Libraries** - Module 02 မှာ `pom.xml` ထဲသတ်မှတ်ထားတဲ့ langchain4j dependencies တွေကို အသုံးပြုထားပါတယ်။
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**OpenAiOfficialChatModel ဖွဲ့စည်းမှု** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

Chat model ကို Azure OpenAI endpoint များကို ထောက်ပံ့တဲ့ OpenAI Official client နဲ့ Spring bean အနေနဲ့ လက်ဖြင့် configure လုပ်ထားပါတယ်။ Module 01 နဲ့ မတူတာက `chatModel.chat()` သို့ ပို့တဲ့ prompt တွေကို ဘယ်လိုဖွဲ့စည်းတာပါ။ မော်ဒယ်ကို စီမံခြင်း သီးသန့် မဟုတ်ပါ။

**System နဲ့ User Messages** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j က message အမျိုးအစားအတွက် သံသယမရှိအောင် ခွဲခြားထားပါတယ်။ `SystemMessage` က AI ရဲ့ အပြုအမူနဲ့ အခြေအနေ (ဥပမာ "သင်က code reviewer ဖြစ်ပါတယ်") မှတ်ထားပြီး `UserMessage` မှာ မူရင်းတင်ပြချက်တွေ ပါပါတယ်။ ဒီခွဲခြားမှုက နောက်သုံးပြောမည့် စကားလုံးတွေမှာ AI စပ်ဆိုင်မှု တည်ငြိမ်စေပါတယ်။

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/my/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage က context တည်ငြိမ်စေပြီး UserMessages က တစ်ခုချင်းစီ တောင်းဆိုချက်တွေ ပါရှိတယ်*

**Multi-Turn Chat အတွက် MessageWindowChatMemory** - အတော်ကြာ စကားပြော ပုံစံအတွက် Module 01 မှ MessageWindowChatMemory ကို ပြန်လည်အသုံးပြုပါတယ်။ စုံစမ်းချက်တိုင်းနဲ့ ခြားနားတာ memory instance ကို `Map<String, ChatMemory>` ထဲသိမ်းဆည်းတာဖြစ်ပြီး အရာများကို ပေါင်းစပ်ခြင်းမရှိဘဲ စကားပြောများကို အချိန်နှီးမှုမပြတ်စွာ ထိန်းသိမ်းနိုင်ပါတယ်။

**Prompt Templates** - ဒီမှာ ဦးတည်ထားတာ prompt engineering ပါ၊ LangChain4j ရဲ့ အသစ် API မဟုတ်ပါ။ pattern တစ်ခုချင်းစီ (နိမ့်စိတ်ဓာတ်၊ မြင့်စိတ်ဓာတ်၊ task execution စသည်) များကို တူညီတဲ့ `chatModel.chat(prompt)` method ဖြင့် တိကျစွာ ဖွဲ့စည်းထားသော prompt စာသားပုံသဏ္ဍာန်များနဲ့ အသုံးပြုပါတယ်။ XML tag များ၊ မိန့်ကြားချက်များ၊ အတန်းစားများသည် prompt ရဲ့ အစိတ်အပိုင်း ဖြစ်ပြီး LangChain4j အင်္ဂါရပ် မဟုတ်ပါ။

**Reasoning Control** - GPT-5.2 ရဲ့ စဉ်းစားမှု အားကို "maximum 2 reasoning steps" သို့မဟုတ် "explore thoroughly" လို instruction များမှတဆင့် ထိန်းချုပ်နိုင်ပါတယ်။ ဒါတွေဟာ prompt engineering နည်းစနစ်ဖြစ်ပြီး LangChain4j configuration မဟုတ်ပါ။ library က တစ်သက်တည်း သင့် prompt များကို model ဆီ ပို့ပေးတာပါ။

အဓိက မှတ်ချက်မှာ: LangChain4j က infrastructure (model ချိတ်ဆက်မှု - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java), memory နဲ့ message ကိုင်တွယ်ခြင်း - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)) ကိုပေးပြီး ဒီ module က အဲ့ဒီ infrastructure ကိုအသုံးပြု prompt တွေကို ဘယ်လို ဖန်တီးရမလဲကို သင်ကြားပေးနေတယ်။

## အဓိက ပုံစံများ

ဒေါသတိမ်းချက် တူညီတဲ့နည်းလမ်း မလိုတာမျိုး ရှိတတ်ပါတယ်။ မေးခွန်းတစ်ချို့အတွက် အမြန်ဖြစ်တယ်၊ တစ်ချို့သည် အနက်ရှိုင်းသုံးသပ်ဖို့လိုတယ်။ တချို့အတွက် ကြည့်ရှုနိုင်စေဖို့တော့ လိုတယ်၊ တစ်ချို့အတွက် ရလဒ်သာလိုတယ်။ ဒီ module က ပုံစံ ၈ မျိုးဖြင့် ဖော်ပြထားပြီး အသုံးပြုမှုအမျိုးမျိုးအတွက် အထူးပြုထားပါတယ်။ သင့်ကိုယ်တိုင်လည်း အဲဒီ ပုံစံတွေကို စမ်းသပ်ပြီး ဘယ်အချိန်မှာ အသုံးပြုရမလဲ စူးစမ်းလေ့လာနိုင်ပါမယ်။

<img src="../../../translated_images/my/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*Prompt engineering ပုံစံ ၈ မျိုးနှင့် ၎င်း သက်ဆိုင်ရာ အသုံးချမှုများ*

<img src="../../../translated_images/my/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*နိမ့်စိတ်ဓာတ် (လျင်မြန် တိုတောင်း) နှင့် မြင့်စိတ်ဓာတ် (နက်ရှိုင်း သေချာ) reasoning နည်းလမ်းများ*

**နိမ့်စိတ်ဓာတ် (မြန်ဆန်၍အာရုံစူးစိုက်)** - သင့်အား အမြန်၊ တိုတိုဖြေချင်တဲ့ မေးခွန်းများအတွက်။ Model က သေးငယ်တဲ့ reasoning လုပ်ဆောင်မှု (အမြင့်ဆုံး ၂ ခြေလှမ်း) ပြုလုပ်သည်။ တွက်ချက်ခြင်း၊ ရှာဖွေခြင်း၊ သမားရိုးကျ မေးခွန်းများအတွက် အသုံးပြုပါ။

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilot နဲ့ စမ်းကြည့်ပါ:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ဖွင့်ပြီး မေးပါ:
> - "နိမ့်စိတ်ဓာတ်နဲ့ မြင့်စိတ်ဓာတ် prompting ပုံစံတွေ ဘယ်လိုကွာခြားသလဲ?"
> - "XML tag တွေက prompt မှာ AI ရဲ့ ဖြေရှင်းပုံ ကို ဘယ်လို ကူညီသနည်း?"
> - "ကိုယ်တိုင် ပြန်လည် သုံးသပ်မှု pattern နဲ့ တိုက်ရိုက်ညွှန်ကြားမှုကို ဘယ်လို ကွဲပြားအသုံးပြုသလဲ?"

**မြင့်စိတ်ဓာတ် (နက်ရှိုင်း သေချာ)** - စိုးရိမ်ဖွယ်ရာ ပြဿနာများအတွက် စမ်းသပ်ဖို့။ Model က အပြည့်အစုံသုံးသပ်မှု ပြုလုပ်ပြီး အသေးစိတ် အတောက်ပစေသော reasoning ကို ဖော်ပြသည်။ စနစ်ဒီဇိုင်း၊ စွယ်စုံ မေးခွန်းများ၊ ရှာဖွေစိစစ်မှုအတွက် အသုံးပြုပါ။

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**တာဝန် ကျင်းပမှု (တစ်ဆင့်ချင်း အခြေအနေပေါ်မှ ပြုလုပ်မှု)** - အဆင့်များစွာ ခွဲခြမ်းပြုပြင်မှုအတွက်။ Model က အစစအရာရာ အစီအစဉ်တစ်ခု တင်ပြ၊ အဆင့်အားလုံးကို ရောက်ရှိလာသလို အဆင့်ဆင့် အကြောင်းပြုမှတ်တမ်း တင်ကာ နောက်ဆုံးမှာ ရှင်းလင်းချက် ပေးသည်။ တင်သွင်းခြင်း၊ အကောင်အထည်ဖော်ခြင်း၊ အဆင့်မြင့် လုပ်ငန်းစဉ်များအတွက် အသုံးပြုပါ။

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting က model ကို reasoning လုပ်ပုံ လုပ်နည်း ပြသဖို့ တိုက်တွန်းထားသောနည်းစနစ်ဖြစ်ပြီး၊ ရှုပ်ထွေးတဲ့လုပ်ငန်းများအတွက် တိကျမှန်ကန်မှုအစိုးရမြှင့်တင်ပေးသည်။ အဆင့်လိုက် ခွဲခြမ်းစိတ်ဖြာခြင်းက လူနဲ့ AI နှစ်ဖက် အတွက် ဉာဏ်ရည်ကို နားလည်ခွင့်ပေးစေသည်။

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat နဲ့ စမ်းကြည့်ပါ:** ဒီ pattern အကြောင်းမေးပါ:
> - "တာဝန်ကျင်းပမှု pattern ကို တာရှည်လုပ်ဆောင်မှုတွေအတွက် ဘယ်လို လိုက်ဖက်တပ်ဆင်မလဲ?"
> - "ထုတ်ကုန် application တွေမှာ ကိရိယာ preambles ဖွဲ့စည်းတဲ့ နည်းဗျူဟာအကောင်းဆုံးနည်းများ ရှိသလား?"
> - "UI တွင် အလယ်အလတ် တိုးတက်မှု အချက်အလက်တွေကို ဘယ်လို ဖမ်းယူ ပြသနိုင်မလဲ?"

<img src="../../../translated_images/my/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*အဆင့်ဆင့် အစီအစဉ် → အကောင်အထည် → စာတမ်းချုပ် Workflow*

**ကိုယ်တိုင် ပြန်လည် သုံးသပ်နေတာ** - ထုတ်လုပ်မှု အရည်အသွေး code ဖန်တီးရန်။ Model က code ကို ဖန်တီး၊ အရည်အသွေး အသုံးပြုမှုများနှင့် နှိုင်းယှဉ် စစ်ဆေး၊ ပြုပြင်မွမ်းမံကာ ဆက်တိုက် တိုးတက်စေသည်။ လုပ်ဆောင်မှုအသစ်တည်ဆောက်ခြင်းများအတွက် အသုံးပြုပါ။

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/my/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*မကြာခဏ တိုးတက်အောင် loops — ဖန်တီး၊ စမ်းသပ်၊ ပြဿနာတွေရှာ၊ ပိုမိုကောင်းစေ၊ ထပ်ဆင့်လည်လုပ်*

**ဖွဲ့စည်းထားသော ချဲ့ထွင်ချက်** - အမြဲတမ်း တုံ့ပြန်မှု မှန်ကန်မှုရှိစေရန် အတည်ပြုခြင်း။ Model က fixed framework (တိကျမှန်ကန်မှု၊ လုပ်ထုံးလုပ်နည်း၊ ဆောင်ရွက်မှုပမာဏ၊ လုံခြုံရေး) အသုံးပြုသုံးသပ်သည်။ code review သို့မဟုတ် အရည်အသွေး သုံးသပ်မှုများအတွက် အသုံးပြုပါ။

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat နဲ့ စမ်းကြည့်ပါ:** ဖွဲ့စည်းထားသော ချဲ့ထွင်ချက်အကြောင်း မေးနိုင်ပါသည်:
> - "code review အမျိုးမျိုးအတွက် ချဲ့ထွင်ချက် framework ကို ဘယ်လို မိမိစိတ်ကြိုက်ပြင်ဆင်မလဲ?"
> - "ဖွဲ့စည်းထားသော ထွက်ရှိမှုကို programmatically အကောင်အထည် ဖော်ရာမှာ ဘယ်လို ကောင်းမွန်ဆုံး ပုံစံများ ရှိသလဲ?"
> - "review session တွေမှာ severity လက်မှတ်များ တစ်ပြိုင်နက်တည်း အတည်ပြုရန် ဘယ်လိုလုပ်နိုင်မလဲ?"

<img src="../../../translated_images/my/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*severity အဆင့်များနှင့် အတူ တုံ့ပြန်ချက် အမြဲတမ်းရှိစေရန် အမျိုးအစား ၄ ခု framework*

**အတော် ကြာ ပြန်လည် စကားပြောဆိုမှု** - နောက်ခံ context လိုအပ်တဲ့ စကားပြောဆိုမှုအတွက်။ Model က မေးမြန်းရေးသားချက်များကို မှတ်မိပြီး စကားပြော ဆက်လက်တည်ဆောက်သည်။ တုံ့ပြန်မှု သွားလာမှု မျိုးစုံ အကူအညီ၊ ရှုပ်ထွေးသော Q&A ကိစ္စများအတွက် အသုံးပြုနိုင်သည်။

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/my/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*စကားပြော ဆက်နွယ်မှုများ မျိုးစုံအထိ token ကန့်သတ်မှု ရောက်ရှိသည်အထိ တိုးတက်လာမှု*

**တစ်ဆင့်ချင်း စဉ်းစားမှု** - မြင်သာနိုင်အောင် တိကျသော logic လိုအပ်တဲ့ ပြဿနာများအတွက်။ Model က reasoning ပိုင်းကို အဆင့်အလိုက် ပြသသည်။ သင်္ချာ၊ logic puzzle များနှင့် စဉ်းစား သဘောနားလည်ရန် လိုအပ်တဲ့အခါ အသုံးပြုပါ။

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/my/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*ပြဿနာများကို တိကျသော reasoning ခြေလှမ်းများဖြင့် ခွဲခြားသည်*

**ကန့်သတ်ထားသော ထွက်ရှိမှု** - ပုံစံတိကျမှု ရှိရမယ့် ရုပ်ထွက် ပြန်လည်စာရင်းများအတွက်။ Model က ပုံစံ၊ အရှည် နဲ့ စည်းကမ်းတွေကို တိတိကျကျ လိုက်နာပေးပါသည်။ ရှင်းလင်းချက်များ သို့မဟုတ် တိကျမှန်ကန်သော ထုတ်ကုန်ဖော်ပြချက်များအတွက် အသုံးပြုသည်။

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/my/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*ပုံစံ၊ အရှည် နှင့် ဖွဲ့စည်းမှုလိုအပ်ချက်များကို တင်းကြပ်စွာလိုက်နာခြင်း*

## အခုရှိပြီးသား Azure ရင်းမြစ်များ အသုံးပြုခြင်း

**Deployment ကောင်းမွန်မှု စစ်ဆေးခြင်း:**

Module 01 မှာ ပြီးမြောက်စွာ ဖန်တီးထားတဲ့ Azure အတည်ပြုချက်ပါတဲ့ `.env` ဖိုင် root directory မှာ ရှိနေပါတယ်ဆိုတာ သေချာစေပါ။
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT၊ API_KEY၊ DEPLOYMENT ကိုပြသရမည်။
```

**အပလီကေးရှင်း စတင်ခြင်း:**

> **မှတ်ချက်:** Module 01 မှာ `./start-all.sh` နဲ့ အားလုံး စတင်ထားပြီးသားဆိုရင် ဒီ module ကို port 8083 မှာ အလုပ်လုပ်နေပါပြီ။ အောက်မှာ ရေးထားတဲ့ စတင်မှု command များ မသုံးသင့်ပါ၊ http://localhost:8083 ကို ချက်ချင်း သွားကြည့်နိုင်ပါတယ်။

**ရွေးချယ်စရာ ၁: Spring Boot Dashboard အသုံးပြုခြင်း (VS Code သုံးသူများအတွက် အကြံပြုပါသည်)**

Dev container ထဲမှာ Spring Boot Dashboard extension ပါရှိပြီး၊ VS Code ရဲ့ ဘယ်ဖက် Activity Bar မှာ (Spring Boot အိုင်ကွန်ကို ရှာပါ) ပြသပါတယ်။

Dashboard မှ စတင်၍:
- Workspace ထဲရှိ Spring Boot အားလုံးကို ကြည့်ရှုနိုင်သည်
- နှိပ်ပါက application များ စတင်/ရပ်နားနိုင်သည်
- ရုပ်မြင့်အောင် application log များကို ဖော်ပြနိုင်သည်
- Application status မှန်ကန်မှု စောင့်ကြည့်နိုင်သည်

"prompt-engineering" နှင့် ဆက်စပ် module ကို play button ကို နှိပ်ပြီး စတင်ပါ၊ သို့မဟုတ် modules အားလုံးကို တပြိုင်နက် စတင်နိုင်သည်။

<img src="../../../translated_images/my/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ရွေးချယ်စရာ ၂: Shell script အသုံးပြုခြင်း**

All Web application များ (modules 01-04) စတင်ရန်:

**Bash:**
```bash
cd ..  # အမြစ်ဖိုင်လမ်းကြောင်းမှ
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # root directory မှစ၍
.\start-all.ps1
```

သို့မဟုတ် ဒီ module သာ စတင်ရန်:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

Shell script နှစ်ခုလုံးက အလိုအလျောက် root `.env` ဖိုင်ထဲက environment variables များကို Load လုပ်ပြီး JAR မရှိပါက Build လုပ်ပေးပါသည်။

> **မှတ်ချက်:** မစတင်ခင်မှာ module အားလုံးကို လက်ဖြင့် build လုပ်ချင်ရင်:
>
> **Bash:**
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
>
> **PowerShell:**
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```

Web browser မှာ http://localhost:8083 ကို ဖွင့်ပါ။

**ရပ်ဆိုင်းရန်:**

**Bash:**
```bash
./stop.sh  # ဒီမော်ဂျူးလ်သာ
# သို့မဟုတ်
cd .. && ./stop-all.sh  # အားလုံးသောမော်ဂျူးလ်များ
```

**PowerShell:**
```powershell
.\stop.ps1  # ဒီမော်ဂျူးသာ
# သို့မဟုတ်
cd ..; .\stop-all.ps1  # အားလုံး မော်ဂျူးများ
```

## အပလီကေးရှင်း Screenshot များ

<img src="../../../translated_images/my/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*ပုံမှန် dashboard မှာ prompt engineering ပုံစံ ၈ မျိုးအားလုံးနဲ့ ၎င်းတို့ရဲ့ လက္ခဏာ၊ အသုံးချမှုများကို ပြသထားသည်*

## ပုံစံများကို စူးစမ်းလေ့လာခြင်း

Web interface က ၄င်း prompting နည်းလမ်းများကို စမ်းသပ်နိုင်စေပါတယ်။ ပုံစံတစ်ခုချင်းစီက မတူညီသော ပြဿနာများကို ဖြေရှင်းပေးတဲ့အတွက် ဘယ်အချိန်မှာ ဘယ်နည်းကိုသင့်တော်တာလဲ စမ်းလိုက်ပါ။

### နိမ့်သော စိတ်ဓာတ်နှင့် အမြင့်သော စိတ်ဓာတ်

နိမ့်စိတ်ဓာတ် Prompt နည်းလမ်းနဲ့ "200 ရဲ့ 15% ဘယ်လောက်လဲ?" ဆိုပြီး ရိုးရှင်းတဲ့ မေးခွန်း မေးပါ။ ချက်ချင်း တိုတောင်း ပါးလပ်တဲ့ ဖြေရှင်းချက်ရမှာပါ။ အခုမှာတော့ "အမြန်ဆန်းသစ် API အတွက် caching strategy တည်ဆောက်ပုံ ဒီဇိုင်းလုပ်ပါ" ဆိုပြီး မြင့်စိတ်ဓာတ်နဲ့ မေးပါ။ Model က တဖြည်းဖြည်း သေချာစွာ reasoning လုပ်ပြီး အသေးစိတ်ရှင်းပြမှာ ဖြစ်ပါတယ်။ တူညီတဲ့ model၊ တူညီတဲ့ မေးခွန်းဖွဲ့စည်းမှုဖြစ်ပေမဲ့ prompt က ကြိုးစားစိတ်ဓာတ်အဆင့်ကို အတိအကျ ပြောနေပါတယ်။
<img src="../../../translated_images/my/low-eagerness-demo.898894591fb23aa0.webp" alt="အနည်းငယ် စိတ်အားထက်သန်မှု နမူနာ" width="800"/>

*အနည်းငယ် အာရုံစိုက်၍ မြန်ဆန်စွာတွက်ချက်ခြင်း*

<img src="../../../translated_images/my/high-eagerness-demo.4ac93e7786c5a376.webp" alt="အများကြီး စိတ်အားထက်သန်မှု နမူနာ" width="800"/>

*လုံးဝစုံလင်သော Cache မျှဝေမှု များ (2.8MB)*

### တာဝန်ဆောင်ရွက်မှု (ကိရိယာ မျက်နှာဖုံးများ)

အဆင့်များစွာ ပါဝင်သည့် အလုပ်စဉ်များသည် မျိုးအစီအစဉ်ချခြင်းနှင့် တိုးတက်မှု ကိုဖေါ်ပြခြင်းမှ အကျိုးရှိသည်။ မော်ဒယ်သည် ၎င်းလုပ်မည့်အရာကို ဖေါ်ပြပြီး၊ အဆင့်ဆင့် ပြောကြားကာ နောက်ဆုံးတွင် ရလဒ်ကို အကျဉ်းချုပ်ပေးသည်။

<img src="../../../translated_images/my/tool-preambles-demo.3ca4881e417f2e28.webp" alt="တာဝန်ဆောင်ရွက်မှု နမူနာ" width="800"/>

*အဆင့်ဆင့် ဖော်ပြချက်ဖြင့် REST အနောက်ခံတည်ဆောက်ခြင်း (3.9MB)*

### ကိုယ့်ကိုယ်ကို ပြန်လည်စဉ်းစားသော ကုဒ်

"အီးမေးလ် သက်ဆိုင်မှု ဝန်ဆောင်မှုတစ်ခု ဖန်တီးပါ" ဟု စမ်းကြည့်ပါ။ ကုဒ်ကို ထုတ်ပေးပြီး ရပ်နားခြင်းမဟုတ်ဘဲ၊ မော်ဒယ်သည် ထုတ်လုပ်၊ အရည်အသွေး စံများနှင့် နှိုင်းယှဉ်သုံးသပ်၊ အားနည်းချက်များ ထုတ်ဖော်ပြီး တိုးတက်ကောင်းမွန်စေရန် ဆောင်ရွက်သည်။ ထိုကုဒ်သည် ထုတ်လုပ်မှုစံနှုန်းရောက်ရန်အထိ ထပ်တလဲလဲလုပ်နေသည်ကို သင်မြင်ရမည်။

<img src="../../../translated_images/my/self-reflecting-code-demo.851ee05c988e743f.webp" alt="ကိုယ်တိုင် ပြန်လည်စဉ်းစားသော ကုဒ် နမူနာ" width="800"/>

*အပြီးအစီး အီးမေးလ် သက်ဆိုင်မှု ဝန်ဆောင်မှု (5.2MB)*

### ဖွဲ့စည်း စိစစ်ခြင်း

ကုဒ် ပြန်လည်သုံးသပ်မှုတွင် တူညီသော သတ်မှတ်ချက်များ လိုအပ်သည်။ မော်ဒယ်သည် ကုဒ်ကို တည်ငြိမ်သော အမျိုးအစားများဖြင့် (မှန်ကန်မှု၊ လုပ်ထုံးလုပ်နည်းများ၊ စွမ်းဆောင်ရည်၊ လုံခြုံမှု) စိစစ်၍ အချို့ကို အကြမ်းခံအဆင့်များဖြင့် ဖော်ပြသည်။

<img src="../../../translated_images/my/structured-analysis-demo.9ef892194cd23bc8.webp" alt="ဖွဲ့စည်း စိစစ်ခြင်း နမူနာ" width="800"/>

*ဖွဲ့စည်းပုံ အခြေခံ ကုဒ် ပြန်လည်သုံးသပ်ခြင်း*

### အဆက်အစပ် စကား၀ိုင်း

"Spring Boot ဆိုတာဘာလဲ?" ဟု မေးပြီးနောက် "ဥပမာတစ်ခုပြပါ" ဟုပြောလိုက်ပါ။ မော်ဒယ်သည် သင့်မေးခွန်း ပထမတစ်ခုကို မှတ်မိကာ Spring Boot ဥပမာတစ်ခု သီးသန့် ပေးသည်။ မှတ်ဉာဏ်မရှိလျှင် ဒုတိယမေးခွန်းသည် မရှင်းလင်းသော မေးခွန်းဖြစ်နေမည်။

<img src="../../../translated_images/my/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="အဆက်အစပ် စကား၀ိုင်း နမူနာ" width="800"/>

*မေးခွန်းများအကြား အကြောင်းအရာ သိမ်းဆည်းခြင်း*

### အဆင့်ဆင့် ယံုၾကည္စိတ္ခ်ဖြတ်ခြင်း

ဂဏန်းပြဿနာတစ်ခုရွေးပြီး Step-by-Step Reasoning နှင့် Low Eagerness နှစ်ခုလုံးဖြင့် စမ်းကြည့်ပါ။ Low eagerness သည် နေရာတိုင်းဖြေကြားပေးသော်လည်း အလျင်မြန်ပြီး မရှင်းလင်းပေ။ Step-by-step သည် တစ်ခုစီ မှတ်ချက်များနှင့် တွက်ချက်မှုများ ပြသပေးသည်။

<img src="../../../translated_images/my/step-by-step-reasoning-demo.12139513356faecd.webp" alt="အဆင့်ဆင့် ယံုၾကည္စိတ္ခ်ဖြတ်ခြင်း နမူနာ" width="800"/>

*အဆင့်တွေ့လမ်းကြောင်း အမြင့်လေး ထုတ်ဖော် ပြဿနာ*

### ကန့်သတ်ထားသော ထုတ်လွှင့်ချက်

သတ်မှတ်ထားသော ပုံစံများ သို့မဟုတ် စကားလုံးရေလိုအပ်သောအခါ ဤပုံစံသည် တင်းကျပ်စွာ လိုက်နာမှုကို တင်းကြပ်စွာ နှုတ်ဆက်ပေးသည်။ တိတိကျကျ ၁၀၀ စကားလုံး ပါဝင်သော အချက်အလက် ဖော်ပြချက်ကို စမ်းပါ။

<img src="../../../translated_images/my/constrained-output-demo.567cc45b75da1633.webp" alt="ကန့်သတ်ထားသော ထုတ်လွှင့်ချက် နမူနာ" width="800"/>

*အမျိုးအစား ထိန်းချုပ်ထားသော မေရှင် လေ့လာမှု အကျဉ်းချုပ်*

## သင် လေ့လာနေရသော အရာများ

**အတွေးအခေါ် ကြိုးစားမှုသည် အရာအားလုံးကို ပြောင်းလဲစေသည်**

GPT-5.2 သည် သင်၏ ကြော်ငြာများမှတဆင့် သင်၏ မှုခင်းကြိုးစားမှုကို ထိန်းချုပ်နိုင်သည်။ အနိမ့်ကြိုးစားမှုမှာ အစုံစမ်းမှု အနည်းငယ်ဖြင့် မြန်ဆန်သော တုံ့ပြန်မှုကို ဆိုလိုသည်။ မြင့်မားသော ကြိုးစားမှုမှာ မော်ဒယ်၏ အတွေးအခေါ်ကို နက်နဲစွာ ထားပါဝ နာရီတစ်ခုလုံးရှည်စေသည်။ သင်သည် ဘယ်လို ကြိုးစားမှုကို အလုပ်အပေါ် အညီ ရွေးချယ်နိုင်သည်ကို သင်ယူနေသည်။ ရိုးရိုး မေးခွန်းများတွင် အချိန်ပိုမိုကုန်စရောက်စရာ မလို၊ ပြီးတော့ ရှုပ်ထွေးသော ဆုံးဖြတ်ချက်များကို ပြေးလွှားစွာ မလုပ်သင့်ပါ။

**ဖွဲ့စည်းပုံသည် ကိုယ်နှင့်အပြုအမူကို ဦးတည်စေသည်**

ကြော်ငြာများတွင် XML တက်များ တွေ့ရသည် သတိပြုမိပါသလား။ ၎င်းတို့သည် ဆင်ဆာပစ္စည်းမဟုတ်ပါ။ မော်ဒယ်များသည် ဖွဲ့စည်းထားသော ညွှန်ကြားချက်များအား သေသေချာချာ လိုက်နာရပြီး အခြား text များထက် ချောမွေ့သည်။ အဆင့်များစွာလုပ်ဆောင်မှု သို့မဟုတ် ရှုပ်ထွေးသော အလုပ်ရည်ရွယ်ချက်များလိုအပ်သောအခါ ဖွဲ့စည်းမှုသည် မော်ဒယ်ကို ယခုရာမှာ ရှိသလား၊ နောက်တစ်ခုဘာလဲဆိုသည်ကို ထိန်းသိမ်းပေးသည်။

<img src="../../../translated_images/my/prompt-structure.a77763d63f4e2f89.webp" alt="ကြော်ငြာ ဖွဲ့စည်းပုံ" width="800"/>

*သေချာဖွဲ့စည်းထားသော ကြော်ငြာတစ်ခု၏ ဖွဲ့စည်းမှုနှင့် XML ပုံစံအုပ်ချုပ်မှု*

**အရည်အသွေးသည် ကိုယ်တိုင်ချိန်ညှိခြင်းမှဖြစ်ပေါ်သည်**

ကိုယ်တိုင်ပြန်လည်စိစစ်ရေးပုံစံများသည် အရည်အသွေးစံများကို ထူထောက်ပေးသည်။ မော်ဒယ်သည် "မှန်ကန်စွာလုပ်ဆောင်ပါ" ဟု မမျှော်လင့်ဘဲ၊ "မှန်ကန်မှု" ဆိုသည်မှာ ဘာကြောင့်မှန်ကန်သနည်း။ မှန်ကန်သော အတွေးအခေါ်၊ error ကိုင်တွယ်ခြင်း၊ စွမ်းဆောင်ရည်၊ လုံခြုံမှုသည် အဘယ်နည်းဆိုသော သတ်မှတ်ချက်များကို မော်ဒယ်အား ပြောကြားပေးသည်။ ထိုမှတဆင့် ကိုယ်တိုင် ထုတ်လွှင့်ချက်ကို စိစစ်ပြီး တိုးတက်အောင် ပြုလုပ်နိုင်သည်။ ၎င်းသည် ကုဒ် ထုတ်ယူခြင်းကို တံဆိပ်ကံကောင်းလောင်းကစားမှ ကြိုးစားမှုဖြစ်စေသည်။

**အကြောင်းအရာသည် ကန့်သတ်ရှိသည်**

အဆက်အစပ် စကားဝိုင်းများသည် တစ်အကြောင်းအရာအတွင်း သတင်းစကားမှတ်တမ်းများ ပါဝင်သည်ဖြစ်၍ အလုပ်လုပ်သည်။ သို့သော် ကန့်သတ်ချက်ရှိသည် - မော်ဒယ်တိုင်းတွင် တကယ်တော့ အများဆုံး token ဂဏန်းရှိသည်။ စကားဝိုင်းများ တိုးလာလျှင် လူသားများသည် အကြောင်းအရာ သက်ဆိုင်ရာကို ထိန်းသိမ်းထားရန် မျဉ်းဆုံမှုရယူရန် လိုအပ်မည်။ ဤသင်ခန်းစာသည် သင်အမှတ်စုအား အလုပ်လုပ်နေသည်ကို ပြသသောအပြင် နောက်ပိုင်းတွင် သာဓကချုပ်ရန်၊ မေ့ရန်နှင့် ပြန်လည်ဖော်ထုတ်ရန် အချိန်များကို သင်ယူမည်။

## နောက်တစ်ဆင့်များ

**နောက်တစ်ခုကဏ္ဍ:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**လမ်းညွှန်:** [← ယခင်: ကဏ္ဍ 01 - နိဒါန်း](../01-introduction/README.md) | [အဓိကသို့ ပြန်မည်](../README.md) | [နောက်တစ်ခု: ကဏ္ဍ 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**စာကြောင်းသတိပေးချက်**  
ဤစာတမ်းကို AI ဘာသာပြန်မှုဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှုကို ကြိုးစားဆောင်ရွက်ပေမယ့်၊ အလိုအလျောက်ဘာသာပြန်မှုတွင် အမှားများ သို့မဟုတ် မှားယွင်းမှုများ ဖြစ်ပေါ်နိုင်ပါကြောင်း သတိပေးလိုပါသည်။ မူရင်းစာတမ်းကို သဘာဝဘာသာဖြင့်သာ အာဏာပိုင်ရင်းမြစ်ဟု ယူဆရန် လိုအပ်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက် လူမှုအလုပ်အကိုင်ဖြင့် ဘာသာပြန်ခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုမှုကြောင့် ဖြစ်ပေါ်နိုင်သော နားလည်မှုပုံမှားမှုများနှင့် မှားယွင်းသဘောဆောင်မှုများအတွက် သက်ဆိုင်သူများအနေဖြင့် တာဝန်မယူပါကြောင်း သတိပေးအပ်ပါသည်။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->