# Module 02: GPT-5.2 နှင့် Prompt Engineering

## ဇယားအစီအစဉ်

- [သင်ဘာတွေသင်မလဲ](../../../02-prompt-engineering)
- [လိုအပ်ချက်များ](../../../02-prompt-engineering)
- [Prompt Engineering ကိုနားလည်ခြင်း](../../../02-prompt-engineering)
- [Prompt Engineering အခြေခံများ](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [အဆင့်မြင့် ပုံစံများ](../../../02-prompt-engineering)
- [ရှိပြီးသား Azure အရင်းအမြစ်များ အသုံးပြုခြင်း](../../../02-prompt-engineering)
- [အက်ပလီကေးရှင်း Screenshots](../../../02-prompt-engineering)
- [ပုံစံများ ရှာဖွေခြင်း](../../../02-prompt-engineering)
  - [နည်းနည်းစိတ်အားထက်သန်ခြင်းနှင့် အလွန်စိတ်အားထက်သန်ခြင်း](../../../02-prompt-engineering)
  - [Task Execution (Tool Preambles)](../../../02-prompt-engineering)
  - [ကိုယ်ကိုယ်ကို သုံးသပ်သည့် ကုဒ်](../../../02-prompt-engineering)
  - [ဖွဲ့စည်းသည့် သုံးသပ်ချက်](../../../02-prompt-engineering)
  - [ပြန်ပြောဆိုစကားဝိုင်းများ](../../../02-prompt-engineering)
  - [အဆင့်လိုက် အကြောင်းရင်းရှင်းလင်းချက်](../../../02-prompt-engineering)
  - [ကန့်သတ်ထားသော ထုတ်လုပ်မှု](../../../02-prompt-engineering)
- [သင် အမှန်တကယ် သင်ယူနေသည်](../../../02-prompt-engineering)
- [နောက်ဆက်တွဲအဆင့်များ](../../../02-prompt-engineering)

## သင်ဘာတွေသင်မလဲ

<img src="../../../translated_images/my/what-youll-learn.c68269ac048503b2.webp" alt="သင်ဘာတွေသင်မလဲ" width="800"/>

ယခင်မော်ဒျူးတွင် သင်သည် memory က conversational AI ကို ဘယ်လိုဖြစ်စေသည်ကို မြင်တွေ့ပြီး GitHub Models ကို အသုံးပြု၍ အခြေခံ အပြန်အလှန်ဆက်ဆံမှုများ ပြုလုပ်ခဲ့သည်။ ယခုမှာတော့ Azure OpenAI ၏ GPT-5.2 ကိုအသုံးပြုပြီး မေးခွန်းမေးနည်းများ၊ မိမိသုံး prompt များကို အာရုံစိုက်သွားမည်ဖြစ်သည်။ သင့်လိုအပ်ချက်အလိုက် prompt များကိုဖွဲ့စည်းမှုက တုံ့ပြန်မှုအရည်အသွေးကို ကျယ်ပြန့်စွာသက်ရောက်စေသည်။ ဤမော်ဒျူးတွင် အခြေခံ prompt များကို ပြန်လည်ဆန်းစစ်ပြီး မြင့်မားသော ၈ မျိုး Advanced Pattern များကို GPT-5.2 ၏ စွမ်းဆောင်ရည်အပြည့်အသုံးပြုပုံကို ဖော်ပြပါမည်။

Reasoning control ကို GPT-5.2 မှ ထည့်သွင်းသည့်အတွက် မေးခွန်းများကို ဖြေဆိုရန် မျှော်မှန်းခြင်းအတိုင်း စဉ်းစားခွင့်ပြုခြင်းကိုဖော်ပြနိုင်သည်။ ၎င်းက prompt အမျိုးအစားများကို ပိုမိုရှင်းလင်းစေပြီး ဘယ်အချိန် ဘယ် prompt ကို အသုံးပြုရမည်ကို နားလည်စေသည်။ အနည်းဆုံး Rate limits ရှိခြင်းကြောင့် Azure OpenAI ၏ GPT-5.2 သို့ အလားတူ GitHub Models ထက် ပိုမိုအကျိုးရှိသည်။

## လိုအပ်ချက်များ

- Module 01 ကိုပြီးမြောက်ထား (Azure OpenAI အရင်းအမြစ်များ တပ်ဆင်ပြီး)
- Root ဖိုင်တွင် `.env` ဖိုင် ရှိပြီး Azure ကို login အသေးစိတ် များ ပါဝင်သည် (Module 01 တွင် `azd up` ဖြင့်ဖန်တီးထား)

> **မှတ်ချက်။** Module 01 ကို မပြီးမြောက်ရသေးပါက အထက်ပါ တပ်ဆင်ခြင်း လမ်းညွှန်ချက်များကို မူရင်းအတိုင်းလိုက်နာပါ။

## Prompt Engineering ကိုနားလည်ခြင်း

<img src="../../../translated_images/my/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Prompt Engineering ဆိုတာဘာလဲ?" width="800"/>

Prompt engineering သည် လိုအပ်သောရလဒ်များကို အမြဲတမ်းရရှိစေရန် အတွက် input စာသားကို ဒီဇိုင်းဆွဲခြင်းဖြစ်သည်။ မေးခွန်းမေးခြင်းသာမက မိမိလိုချင်သည့်အတိုင်း မော်ဒယ်ကိုနားလည်စေရန်နှင့် ထုတ်ပေးရန် ပြင်ဆင်တောင်းဆိုခြင်းဖြစ်သည်။

အလုပ်ဖော်တစ်ယောက်အား ညွှန်ပြသည့်အနေဖြင့်မြင်နိုင်သည်။ "Bug ကိုပြင်ပါ" ဆိုခြင်းမှာ ဖော်ပြချက်မ ပြည့်စုံပါ။ "UserService.java ဖိုင် ၄၅ ရောင်းက null pointer exception ကို null check တစ်ခုထည့်ပြီး ပြင်ပါ" ဆိုသည်မှာ အသေးစိတ်တိကျသည်။ ဘာသာစကားမော်ဒယ်များသည် ဒီလိုတိကျမှုနှင့် ဖွဲ့စည်းမှုများကို အလွန်လေးနက်စွာထောက်ထားသည်။

<img src="../../../translated_images/my/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j ဘယ်လို ကိုက်ညီသလဲ" width="800"/>

LangChain4j သည် မော်ဒယ်ချိတ်ဆက်မှု၊ memory နှင့် မက်ဆေ့ခ်ျအမျိုးအစားများကိုပံ့ပိုးပေးသော အခြေခံအဆောက်အအုံဖြစ်ပြီး prompt pattern များမှာ ထိုအခြေခံအဆောက်အအုံမှတဆင့် ထိုအတိုင်း တိတိကျကျ ဖွဲ့စည်းထားသောစာသား ဖြစ်သည်။ အဓိကပိတ်ဆို့မှုမှာ `SystemMessage` (AI ၏ အပြုအမူနှင့် အခန်းကဏ္ဍသတ်မှတ်ခြင်း) နှင့် `UserMessage` (သင့်တောင်းဆိုချက်ကို ကယ်ဆယ်ပေးသည်) ဖြစ်သည်။

## Prompt Engineering အခြေခံများ

<img src="../../../translated_images/my/five-patterns-overview.160f35045ffd2a94.webp" alt="Prompt Engineering မဟာဗျူဟာ ၅ မျိုး အကျဉ်းချုပ်" width="800"/>

ဤမော်ဒျူးအတွင်းရှိ အဆင့်မြင့်ပုံစံများသို့ ရောက်ခါနီး ပထမဆုံး အခြေခံ prompt နည်းလမ်း ၅ မျိုးကို ပြန်လည်စစ်ဆေးကြစို့။ ၎င်းများဟာ prompt engineer တစ်ဦးတိုင်း သိရှိထားသင့်သော အခြေခံအကြောင်းအရာများဖြစ်သည်။ သင်သည် [Quick Start မော်ဒျူး](../00-quick-start/README.md#2-prompt-patterns) ကို အရင်ပြုလုပ်ပြီးဖြစ်ပါက ကျန်ကျော်ထားခဲ့သော အကြောင်းအရာများဖြစ်ပါတယ်။

### Zero-Shot Prompting

အလွယ်ဆုံးနည်းလမ်း အဖြစ် မော်ဒယ်ကို နမူနာ မရှိဘဲ တိုက်ရိုက် ညွှန်ကြားချက်ပေးသည်။ မော်ဒယ်သည် အလုပ်ကို လုပ်ဆောင်ရန် မေးခွန်းကို မသိသေးဘဲ သင်တန်းကြားမှုများအပေါ်မှ အားမဲ့ပွဲထားသည်။ ရိုးရှင်းသော တောင်းဆိုမှုများတွင် ကောင်းမွန်စွာ အလုပ်လုပ်ပါသည်။

<img src="../../../translated_images/my/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*နမူနာမပါဘဲ တိုက်ရိုက်ညွှန်ကြားချက်ပေးခြင်း — တာဝန်ကို ညွှန်ကြားချက်မှသာ မော်ဒယ် သဘောပေါက်သည်*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// အဖြေ: "အဆိုးမရှိ"
```

**ဘယ်အချိန်သုံးမလဲ:** ရိုးရှင်းသော categories, တိုက်ရိုက်မေးခွန်းများ, ဘာသာပြန်မှုများ သို့မဟုတ် မော်ဒယ်သည် အပိုညွှန်ကြားချက်မလိုအပ်ဘဲ ကျွမ်းကျင်စွာရိုက်နှိပ်နိုင်သော အလုပ်များ။

### Few-Shot Prompting

မော်ဒယ်လိုက်နာရန် မိမိကြိုတင်သိရှိသည့် ပုံစံကို ကိုယ်စားပြုသော နမူနာများကို ပေးသည်။ မော်ဒယ်သည် နမူနာ inputs နှင့် outputs အတိုင်း အသိပညာ ရယူပြီး၊ အသစ်သော inputs များသို့လည်း လိုက်နာပြီး အသုံးပြုသည်။ ဤနည်းဟာ စနစ်တကျမရှိ သောအလုပ်များတွင် ထူးခြားမှုများ ကောင်းမွန်စွာတိုးမြှင့်သည်။

<img src="../../../translated_images/my/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*နမူနာများမှ သင်ယူသည် — မော်ဒယ်သည် ပုံစံကို သိရှိၿပီး အသစ်သော inputs များတွင်လည်း အကောင်အထည်ဖော်သည်*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**ဘယ်အချိန်သုံးမလဲ:** မိမိရေးစရာကိုယ်တိုင်သော categories, တိကျသောဖော်စပ်မှု, domain ပေါ်မူတည်သည့် အလုပ်များ သို့မဟုတ် zero-shot ရလဒ် မကျေမနပ်သောအခါ။

### Chain of Thought

မော်ဒယ်အား တစ်အဆင့်ချင်းစီ စဉ်းစားရန် နှိုးဆော်သည်။ ဖြေဆိုချက်ကို တိုက်ရိုက် ရှင်းပြရန်မဟုတ်ဘဲ၊ မောဟွာယ်ကို ခွဲခြမ်းဆေးစစ်ပြီး အစိတ်အပိုင်းတစ်ခုချင်းဆီလိုက် ပြသသည်။ ဤနည်းသည် သင်္ချာ၊ lojic နှင့် စဉ်ဆက်မပြတ် reasoning အလုပ်များတွင် မှန်ကန်မှုကို တိုးတက်စေသည်။

<img src="../../../translated_images/my/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*အဆင့်လိုက်စဉ်းစားချက် — ရှင်းလင်းသည့် lojitcal အဆင့်များသို့ ပြိုကွဲခြင်း*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// ပုံစံသည် ပြသည် - ၁၅ မှ ၈ ကို အနုတ်လိုက်၍ ၇ ရသည်၊ ထို့နောက် ၇ နဲ့ ၁၂ ကို ပေါင်း၍ အသီး ၁၉ ကိုရသည်။
```

**ဘယ်အချိန်သုံးမလဲ:** သင်္ချာပြဿနာများ၊ lojic puzzles, ဒါမှ မဟုတ် ဖြေရှင်းနည်း ထုတ်ပေးခြင်းက တိုးတက်မှုရှိသော အခြေအနေများ။

### Role-Based Prompting

မေးခွန်းမေးရန်မတိုင်မီ AI ၏ ကိုယ်စားလှယ် သို့မဟုတ် အခန်းကဏ္ဍ တစ်ခု သတ်မှတ်ပေးသည်။ ဤကဏ္ဍသည် ဖြေရှင်းမှု၏ ဗဟိုပြုခံစားချက်၊ နက်ရှိုင်းမှုနှင့် အာရုံစိုက်မှုကို ဖွဲ့စည်းပေးသည်။ "Software architect" ဟာ "Junior developer" သို့ "Security auditor" ထက် မတူပါ။

<img src="../../../translated_images/my/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*Context နှင့် ကိုယ်စားလှယ်သတ်မှတ်ခြင်း — ထိုအခန်းကဏ္ဍအရ အတူတူမေးခွန်းကို မတူညီသော ဖြေဆိုချက် ရရှိသည့် အခြေအနေ*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**ဘယ်အချိန်သုံးမလဲ:** ကုဒ်စစ်ဆေးမှုများ, တက္ကသိုလ်ဆရာ, domain-specific ဝေဖန်ချက်များ, သို့မဟုတ် နည်းပညာပိုင်း ဘွဲ့ရည်သာမှု သို့ မြင်မြင်သာသာ မျှော်လင့်ချက်များ လိုအပ်သော အခါ။

### Prompt Templates

ပြန်လည်အသုံးပြုနိုင်သော prompt များကို Variable placeholders ဖြင့် ဖန်တီးသည်။ တစ်ခါတည်း prompt တစ်ခုရေးသားပြီး အသုံးပြုလိုသည့်အချိန် မှာ `{ {variable} }` အတိုင်း လွယ်ကူစွာ ပြောင်းလဲနိုင်သည်။ LangChain4j ၏ `PromptTemplate` class က ၎င်းကို လွယ်ကူစေသည်။

<img src="../../../translated_images/my/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*Variable placeholders ဖြင့် ပြန်လည်အသုံးပြုနိုင်သော prompt များ*

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

**ဘယ်အချိန်သုံးမလဲ:** အမျိုးမျိုးသော inputs များဖြင့် မေးခွန်းများ ထပ်မံမေးခြင်း, batch processing, ပြန်လည်အသုံးပြုနိုင်သော AI workflow တည်ဆောက်ခြင်း သို့မဟုတ် prompt ဖွဲ့စည်းမှု တည်ငြိမ်ပြီး ဒေတာများသာ အပြောင်းအလဲ ဖြစ်သော အခြေအနေများ။

---

ဤအခြေခံ ၅ မျိုးသည် prompt ကျွမ်းကျင်သူအပြင် မည်သည့် prompt အလုပ်များတွင်မဆို ကျယ်ပြန့်စွာအသုံးချနိုင်သော ကိရိယာအိတ် ဖြစ်သည်။ Module ၂ တွင် ဤအခြေခံအပေါ် အခြေခံ၍ GPT-5.2 ၏ reasoning control, ကိုယ်တိုင်သုံးသပ်မှုနှင့် ဖွဲ့စည်းထားသော ထုတ်ပေးမှု စွမ်းဆောင်ရည်များကို ယူ၍ **အဆင့်မြင့် ၈ မျိုး**ဖြင့် တိုးချဲ့ဆောင်ရွက်ပါမည်။

## အဆင့်မြင့် ပုံစံများ

အခြေခံများကို ပြီးမြောက်ပြီးနောက် ဒီ module ၏ ထူးခြားချက်ဖြစ်သော အဆင့်မြင့် ၈ ပုံစံသို့ ရောက်ပါပြီ။ တစ်ခုတည်းသော ဖြေရှင်းနည်းသာ မလိုအပ်ခဲ့သည်။ တချို့ မေးခွန်းများမှာ အမြန် ဖြေရှင်းစရာလို၊ တချို့က နက်ရှိုင်းစွာစဉ်းစားရန်လို၊ တချို့မှာ reasoning ကို မြင်သာဖော်ပြရမည်။ အောက်ပါ ပုံစံတိုင်းသည် ကွဲပြားသော အခါသမယများအတွက် ဖြည့်စွက်ထားခြင်းဖြစ်ပြီး GPT-5.2 ၏ reasoning control ကွာခြားချက်များကို ပိုမိုဖော်ပြပေးသည်။

<img src="../../../translated_images/my/eight-patterns.fa1ebfdf16f71e9a.webp" alt="အခေါ် ၈ မျိုး အကြောင်း အကျဉ်းချုပ်" width="800"/>

*အခေါ် ၈ မျိုး အရေးပါမှုနှင့် အသုံးပြုချက်များ*

<img src="../../../translated_images/my/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 နှင့် reasoning control" width="800"/>

*GPT-5.2 ၏ reasoning control က မော်ဒယ်အား မည်မျှစဉ်းစားရန် ပြောကြားနိုင်သည် — မြန်ဆန်သော တိုက်ရိုက် ဖြေရှင်းမှုမှ နက်ရှိုင်းစိတ်ဖြောင့်ဖောက်မှုအထိ*

**နည်းနည်းစိတ်အားထက်သန်ခြင်း (မြန်ဆန်ပြီး အာရုံစိုက်):** ရိုးရှင်းသော မေးခွန်းများအတွက် စိတ်အားနည်းစွာ reasoning လုပ်ရန်မလိုအပ်သောဖြေရှင်းချက်။ မော်ဒယ်က reasoning အနည်းငယ်သာလုပ်သည် — အဆင့် ၂ ခြေလှမ်း အများဆုံး။ စဉ်ချက်၊ ရှာဖွေရေး သို့မဟုတ် ရိုးရှင်းသော မေးခွန်းများအတွက် အသုံးပြုပါ။

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilot နှင့် စူးစမ်းကြည့်ရန်:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ဖိုင်ကို ဖွင့်ပြီး မေးပါ။
> - "နည်းနည်းစိတ်အားထက်သန်ခြင်းနဲ့ အလွန်စိတ်အားထက်သန်ခြင်း prompt ပုံစံတွေအကြား ဘာကွာခြားလဲ?"
> - "Prompts ထဲတွင် XML tag များက AI ၏ တုံ့ပြန်မှုကို ဘယ်လို ဖွဲ့စည်းရာတွင် အကူအညီပေးသလဲ?"
> - "ကိုယ်တိုင်သုံးသပ်မှု ပုံစံတွေ၊ တိုက်ရိုက်ညွှန်ကြားချက်ပုံစံတွေရဲ့ အရပ်ကွက် ဘယ်အချိန်သုံးမလဲ?"

**အလွန်စိတ်အားထက်သန်ခြင်း (နက်ရှိုင်းပြီး တိကျ):** ပြဿနာများ ကာကျင်လည်စဉ်းစားပြီး နက်ရှိုင်းသော သုံးသပ်ချက်လိုအပ်သော အခါ။ မော်ဒယ် အပြည့်အဝ သုံးသပ်ပြီး အသေးစိတ် reasoning ပြသသည်။ System design ၊ အင်ဂျင်နီယာ ဆုံးဖြတ်ချက်များ သို့မဟုတ် ရှုပ်ထွေးသော သုတေသနများအတွက် အသုံးပြုသည်။

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (အဆင့်လိုက် တိုးတက်မှု)** — အဆင့်များစွာ ပါဝင်သော workflow များအတွက်။ မော်ဒယ်က ရှေ့နေရာတွင် စီမံကိန်းတစ်ခု ပေးချင်၊ ပြီးနောက် အဆင့်တိုင်းတွင် အလုပ်လုပ်ကြောင်း ဖော်ပြပြီး နောက်ဆုံးတွင် အကျဉ်းချုပ် ပေးသည်။ မိုက်ဂရိတ်လုပ်ငန်းများ၊ အကောင်အထည်ဖော်ခြင်း သို့မဟုတ် အဆင့်များစွာ ပါသော လုပ်ငန်းစဉ်များတွင် အသုံးပြုပါ။

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

Chain-of-Thought prompting သည် reasoning process ကိုထင်ရှားစွာ ပြသရန် မော်ဒယ်အား တောင်းဆိုခြင်းဖြစ်ပြီး ရှုပ်ထွေးသော တာဝန်များ၌ မှန်ကန်မှုကို တိုးတက်စေသည်။ တစ်ဆင့်ခြင်းဖြင့် ခွဲခြမ်းလေ့လာမှုသည် လူကြီးမင်းနှင့် AI နှစ်ဦးလုံးအတွက် တောင့်လျားသော logic ကို နားလည်ရန် ကူညီပေးသည်။

> **🤖 GitHub Copilot [Chat](https://github.com/features/copilot) ဖြင့် စမ်းကြည့်ပါ:** ဤပုံစံနှင့်ပတ်သက်၍ မေးပါ။
> - "Task execution ပုံစံကို ရေရှည်အသက်ရှင်မှု လုပ်ငန်းများအတွက် မည်သို့ ပြင်ဆင်ရမည်နည်း?"
> - "ထုတ်လုပ်မှုအက်ပလီကေးရှင်းများတွင် tool preambles များ ဖွဲ့စည်းရာတွင် အကောင်းဆုံးလမ်းညွှန်ချက်များက ဘာတွေလဲ?"
> - "UI တစ်ခုတွင် အလယ်အလတ် တိုးတက်မှုများကို မည်သို့ ဖမ်းဆီးပြသရမည်နည်း?"

<img src="../../../translated_images/my/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*စီမံချက် → လုပ်ဆောင်ချက် → အကျဉ်းချုပ် Workflow သည် အဆင့်စဉ်များ ပါသော အလုပ်များအတွက်*

**ကိုယ်တိုင်သုံးသပ်သော ကုဒ်** — ထုတ်လုပ်မှု အရည်အသွေး ကောင်းမွန်သော ကုဒ်ထုတ်ပေးရန်။ မော်ဒယ်က ထုတ်လုပ်မှုစံချိန်စံညွှန်းများအတိုင်း ကုဒ်ကို error handling ဖြင့် ထုတ်ပေးသည်။ နောက်ထပ် feature သို့ ဝန်ဆောင်မှုအသစ်များ တည်ဆောက်ရာတွင် အသုံးပြုသည်။

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/my/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="ကိုယ်တိုင်သုံးသပ်ခြင်း လည်ပတ်မှု" width="800"/>

*တကြိမ်ထုတ်၊ သုံးသပ်၊ ပြဿနာတွေ့ရှိ၊ တိုးတက်အောင်လုပ်၊ ထပ်မံလုပ်ခြင်း*

**ဖွဲ့စည်းသည့် သုံးသပ်ချက်** — တည်ငြိမ်သော သုံးသပ်မှုအတွက်။ မော်ဒယ်သည် ကုဒ်ကို တစ်စိတ်တစ်ပိုင်းဖြင့် အမှန်တကယ်ရှုထောင့်ထားသော နည်းလမ်းဖြင့် (မှန်ကန်မှု၊ လေ့လာမှုများ၊ စွမ်းဆောင်ရည်, လုံခြုံမှု၊ ထိန်းသိမ်းမှု) တွက်ချက်သည်။ ကုဒ်ပြန်လည်သုံးသပ်ခြင်း သို့မဟုတ် အရည်အသွေး သုံးသပ်မှုများအတွက် အသုံးပြုသည်။

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 GitHub Copilot [Chat](https://github.com/features/copilot) ဖြင့် စမ်းကြည့်ပါ:** ဖွဲ့စည်းထားသော သုံးသပ်မှုအကြောင်း မေးပါ။
> - "အမျိုးမျိုးသော ကုဒ်ပြန်လည်သုံးသပ်ခြင်းအတွက် သုံးသပ်မှု ဖွဲ့စည်းပုံကို မည်သို့ စိတ်တိုင်းကျပြုပြင်နိုင်မလဲ?"
> - "ဖွဲ့စည်းထားသော ထုတ်လွှင့်မှုကို အလိုအလျောက် ချဉ်းကပ်ပြီး တုံ့ပြန်မှုများကို မည်သို့အကောင်အထည်ဖော်မလဲ?"
> - "Review session များအတွင်း တူညီသော အရေးကြီးမှု အဆင့် သေချာစေရန် ဘယ်လိုလုပ်မလဲ?"

<img src="../../../translated_images/my/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="ဖွဲ့စည်းထားသော သုံးသပ်မှု ပုံစံ" width="800"/>

*Severity Level များဖြင့် တည်ငြိမ်သော ကုဒ်ပြန်လည်သုံးသပ်မှုအတွက် framework*

**ပြန်ပြောဆိုစကားဝိုင်းများ** — context လိုအပ်သော ဆက်သွယ်မှုများအတွက်။ မော်ဒယ်သည် ယခင် စကားပြောများကို မှတ်မိပြီး တိုးတက်စွာပေါင်းစပ်သည်။ အပြန်အလှန် အကူအညီ အစည်းအဝေးတွေ၊ ခက်ခဲသော Q&A အတွက် အသုံးပြုနိုင်သည်။

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

*စကားဝိုင်း context ကို တစ်ချက်ချင်း သိမ်းဆည်း၍ token ကန့်သတ်မှုထိ စုဆောင်းခြင်း*

**အဆင့်လိုက် reasoning** — မော်ဒယ်က အဆင့်တိုင်းတွင် တိကျသော reasoning ကို ပြသသော ပုံစံ။ သင်္ချာ၊ lojic ပဟေဋ္ဌာန်များ သို့မဟုတ် စဉ်းစားစေချင်သော အခါတွင် အသုံးချသည်။

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/my/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="အဆင့်လိုက် ပုံစံ" width="800"/>

*ပြဿနာများကို သက်ဆိုင်ရာ lojic အဆင့်များသို့ ခွဲခြမ်းခြင်း*

**ကန့်သတ်ထားသော ထုတ်လုပ်မှု** — အသေးစိတ် format လိုအပ်ချက်များနှင့်အတူ ဖြေဆိုလိုသော အခါ။ မော်ဒယ်က ဂရုပြုလာသည့်အတိုင်း format နှင့် အရှည်ကို စည်းကမ်းချက်များအားလုံး လိုက်နာသည်။ စုစည်းချက် သို့မဟုတ် စနစ်တကျထုတ်လွှင့်ချက် လိုအပ်သောအခါ အသုံးပြုသည်။

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

<img src="../../../translated_images/my/constrained-output-pattern.0ce39a682a6795c2.webp" alt="ကန့်သတ်ထားသော ထုတ်လုပ်မှု ပုံစံ" width="800"/>

*သတ်မှတ်ထားသော format၊ အရှည် နှင့် ဖွဲ့စည်းမှု စည်းကမ်းများကို ထိန်းသိမ်းခြင်း*

## ရှိပြီးသား Azure အရင်းအမြစ်များ အသုံးပြုခြင်း

**တပ်ဆင်မှုကို အတည်ပြုပါ:**

Module 01 တွင် တပ်ဆင်ရာတွင် ဖန်တီးထားသည့် Azure အသိမှတ်ပြုချက် ပါဝင်သည့် `.env` ဖိုင်သည် root directory တွင် မရှိမဖြစ် ရှိရမည်။  
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကို ပြရန်လိုသည်
```

**အက်ပလီကေးရှင်း စတင်ပါ:**

> **မှတ်ချက်:** Module 01 မှ `./start-all.sh` ဖြင့် အက်ပလီကေးရှင်းအားလုံး စတင်ထားပြီးဖြစ်ပါက ဤမော်ဒျူးတွင် ပေါ့ 8083ပေါ်တွင် ရှိပြီးသား ဖြစ်သည်။ အောက်ပါ စတင်ခေါ်ယူလမ်းညွှန်များကို ကျော်လွှားကာ တိုက်ရိုက် http://localhost:8083 သို့ သွားနိုင်ပါသည်။

**ရွေးချယ်မှု ၁: Spring Boot Dashboard အသုံးပြုခြင်း (VS Code အသုံးပြုသူများ အတွက် အကြံပြုချက်)**

Dev container တွင် Spring Boot Dashboard extension ပါဝင်ပြီး၊ Visual Interface ဖြင့် Spring Boot applications များအား စီမံခန့်ခွဲနိုင်စေသည်။ VS Code Activity Bar ၏ ဘယ်ဘက်ဖက်တွင် (Spring Boot သင်္ကေတ) တွေ့နိုင်သည်။

Spring Boot Dashboard မှ:

- အလုပ်လုပ်နိုင်သော Spring Boot applications များအား ကြည့်ရှုနိုင်သည်
- တစ်ချက်နှိပ်၍ အက်ပလီကေးရှင်းများ စတင်/ရပ်နားနိုင်သည်
- အက်ပလီကေးရှင်း log များကို တိုက်ရိုက်ကြည့်ရှုနိုင်သည်
- အက်ပလီကေးရှင်း အခြေအနေ ကို မျက်မှောက်ကြည့်ရှုနိုင်သည်
"prompt-engineering" အနားက play ခလုတ်ကို နှိပ်ပြီး ဒီ module ကို စတင်ရန် သို့မဟုတ် လုံးဝ modules တွေအားလုံးကို တစ်ပြိုင်နက် စတင်နိုင်သည်။

<img src="../../../translated_images/my/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ရွေးချယ်စရာ ၂: shell script များအသုံးပြုခြင်း**

ဝဘ်အက်ပလิเคရှင်းများအားလုံး (modules 01-04) ကို စတင်ရန်:

**Bash:**
```bash
cd ..  # မူလဖိုလ်ဒါမှဆုံးဖြတ်ခြင်း
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # ရင်းမြစ်ဖိုင်စနစ်မှ
.\start-all.ps1
```

သို့မဟုတ် ဒီ module ကို သီးသန့်စတင်ရန်:

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

နှစ်ခုစလုံး script များသည် root `.env` ဖိုင်မှ ပတ်ဝန်းကျင် မူလတန်းများကို အလိုအလျောက် load လုပ်ပြီး JAR မရှိလျှင် ဖိုင်များကို တည်ဆောက်ပေးပါသည်။

> **မှတ်ချက်:** စတင်ခမဲ့ ဖိုင်များအားလုံးကို ကိုယ်တိုင် build လုပ်လိုလျှင်:
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

သင့် browser တွင် http://localhost:8083 ကိုဖွင့်ပါ။

**ရပ်ချင်သည်ဆိုပါက:**

**Bash:**
```bash
./stop.sh  # ဒီမော်ဌူးတစ်ခုတည်းသာ
# သို့မဟုတ်
cd .. && ./stop-all.sh  # မော်ဌူးအားလုံး
```

**PowerShell:**
```powershell
.\stop.ps1  # ဒီမော်ဂျူးသာ
# ဒါမှမဟုတ်
cd ..; .\stop-all.ps1  # မော်ဂျူးအားလုံး
```

## အက်ပလิเคရှင်း မျက်နှာပြင်များ

<img src="../../../translated_images/my/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

* အဓိက dashboard မှ ပရောမ့် အင်ဂျင်နီယာ နည်းပညာ pattern ၈ မျိုးနှင့် ၎င်းတို့၏ အင်္ဂါရပ်များ၊ အသုံးပြုမှုကိစ္စများကို ပြသသည် *

## Pattern များကို ရှာဖွေခြင်း

ဝဘ်အင်တာဖေ့စ်သည် prompt စဉ်များကို စမ်းသပ်ကြည့်ရန် ခွင့်ပြုသည်။ pattern တစ်ခုချင်းစီသည် မတူညီသော ပြဿနာများကို ဖြေရှင်းသည် - တိုက်ရိုက် ကြည့်ရှုခြင်းအားဖြင့် အချိန်ပိုင်း များတိုင်း၌ ပုံသေကြောင်းတွေ့ရပါမည်။

### ကျယ်စွာ vs နည်းနည်းအားပါးခြင်း

"200 ရဲ့ 15% က ဘာလဲ?" ဆိုတဲ့ ရိုးရှင်းတဲ့မေးခွန်းကို အားနည်း Eagerness နဲ့ မေးပါ။ ချက်ချင်း တိုက်ရိုက် ဖြေကြားချက်ရမှာ ဖြစ်ပါတယ်။ အခုတော့ "အမြင့် Traffic ရှိသည့် API အတွက် caching နည်းဗျူဟာတစ်ခုကို ဒီဇိုင်းဆွဲပါ" ဆိုတာကို အားကြီး Eagerness ဖြင့် မေးမြန်းပါ။ မော်ဒယ်သည် နည်းနည်းနှေးနှေးဖြစ်ကာ အသေးစိတ် အကြောင်းပြချက်များ ပေးပါလိမ့်မည်။ တူညီသော မော်ဒယ်၊ တူညီသော မေးခွန်း ဖွဲ့စည်းချက် ဖြစ်သော်လည်း prompt က စဉ်းစားရန် ဘယ်လောက်ကြာမှန်း ဖော်ပြထားသည်။

<img src="../../../translated_images/my/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

* အမြန်တွက်ချက်မှု နှင့် အနည်းငယ် ထင်မြင်ချက်ငယ်များ*

<img src="../../../translated_images/my/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

* အသေးစိတ် caching နည်းဗျူဟာ (2.8MB)*

### Task Execution (Tool Preambles)

အဆင့်အတန်းများစနစ်ဖြင့် လုပ်ငန်းစဉ်များတွင် ရှေ့နောက်အစီအစဉ်နှင့် အဆင့်ဆင့် မျက်နှာဖုံးပြောဆိုခြင်းကို အသုံးပြုသည်။ မော်ဒယ်သည် မည်သို့လုပ်မည်ကို ဖော်ပြကာ အဆင့်အားလုံးကို တစ်ဆင့်ဆင့် ဖော်ပြ၍ နောက်ဆုံးတွင် ရလဒ်များကို ကိုယ်စားပြုသည်။

<img src="../../../translated_images/my/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*အဆင့်စီအပလီကေးရှင်း REST endpoint တည်ဆောက်ခြင်း (3.9MB)*

### ကိုယ်တိုင် သုံးသပ်ခြင်း (Self-Reflecting Code)

"အီးမေးလ် စစ်ဆေးရေး ဝန်ဆောင်မှု တည်ဆောက်ပါ" ကို စမ်းသပ်ကြည့်ပါ။ ကုဒ်များကိုသာ ဖန်တီးပြီး ရပ်မသည့်အစား မော်ဒယ်သည် ဖန်တီး၊ အရည်အသွေး စံချိန်များနှင့် မြှင့်တင်မှုများကို မျှဝေပေးပြီး ပြန်လည်မြှင့်တင်သည်။ ထွက်ကုဒ်သည် ထုတ်လုပ်မှုအဆင့်များကို ဖြတ်သန်းသည်အထိ ကြည့်ရမှာပါ။

<img src="../../../translated_images/my/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

* အီးမေးလ် စစ်ဆေးရေး ဝန်ဆောင်မှု လုံး၀ (5.2MB)*

### ဖွဲ့စည်းထားသော သုံးသပ်မှု

ကုဒ်ရှုမြင်ခြင်းတွင် ရှုပ်ထွေးမှု ပြဿနာများအတွက် တိတိကျကျ ဖော်ပြနိုင်ရန်လုပ်ကိုင်ရမည်။ မော်ဒယ်သည် အမှားကင်းခြင်း၊ လေ့လာမှု၊ လုပ်ဆောင်မှု၊ ပြဿနာကာကွယ်ခြင်း ဆိုပြီး fixed ကြောင်းများဖြင့် ကုဒ်ကို စစ်ဆေးသည်။

<img src="../../../translated_images/my/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*သုံးသပ်မှု ဂျာနယ်နယ်ပယ်များမှတဆင့် ကုဒ်ပြန်လည်သုံးသပ်ခြင်း*

### Multi-Turn Chat

"Spring Boot ဆိုတာ ဘာလဲ?"ဟု မေးပြီး ချက်ချင်း "ဥပမာတစ်ခု ပြပါ" ဟု နောက်ထပ်မေးပါ။ မော်ဒယ်သည် သင့်ပထမ မေးခွန်းကို မှတ်မိကာ Spring Boot ဥပမာကိုပေးသည်။ မှတ်စုမရှိပါက ဒုတိယမေးခွန်းသည် တိကျမှုမရှိတော့ပါ။

<img src="../../../translated_images/my/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

* မေးခွန်းများအကြား context ထိန်းသိမ်းမှု*

### အဆင့်ဆင့် သွယ်ဝိုက်စဉ်းစားခြင်း

သင်က သင်ဇယားပြသည့် သင်္ချာပြဿနာကို Step-by-Step Reasoning နှင့် Low Eagerness နှစ်ခုစလုံးဖြင့် စမ်းကြည့်ပါ။ Low eagerness သည် ဖြေချက်ကို မြန်မြန်ပေးပေမဲ့ ထိုးဖောက်ကြည့်ရန်ခက်ခဲသည်။ Step-by-step မှာ တုန့်ပြန်မှုများနှင့်ဆုံးဖြတ်ချက်များအားလုံးကို ပြသပေးပါသည်။

<img src="../../../translated_images/my/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

* ဖြေရှင်းနည်း အဆင့်ဆင့်ငြိမ့်ကြည့်မှု*

### ကန့်သတ်ထားသော ထုတ်လွှင့်မှု

သင်အား အတိအကျ ဖော်ပြချက်ပေးရန် ဖောင်မြန်၊ စကားလုံး ရေတိုင်းမျှ ဆီးရှင်းရေးဉပမာကို ပြုလုပ်လိုပါက ဒီ pattern က တိကျစွာ လိုက်နာမှုကို စောင့်ကြည့်ပေးသည်။

<img src="../../../translated_images/my/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*စက်နိုးရှင်းရေး အကျဥ်းချုပ် နှင့် ရေးဆွဲမှု ထိန်းချုပ်မှု*

## စစ်ကောင်းစွာ သင်ယူနေပုံ

**စူးစမ်းစဉ်းစားမှုသည် အားလုံးကို ပြောင်းပေးသည်**

GPT-5.2 သည် သင့် prompt များမှတဆင့် စဉ်းစားမှုအား ထိန်းချုပ်နိုင်စေသည်။ အနည်းငယ်သော စွမ်းအားဖြင့် မြန်မြန် ပြန်ကြားမှု ရရှိမည်၊ အမြင့်သော စွမ်းအားဖြင့် မော်ဒယ်သည် အနက်ရှိုင်းစွာ စဉ်းစားပေးမည်။ သင်သည် အလုပ်၏ ရှုပ်ထွေးမှုနှင့် ကိုက်ညီစေရန် ကြိုးစားနေသည် - ရိုးရှင်းသော မေးခွန်းများတွင် အချိန်စမီ အဖြေကို ရှာရန် သက်သာစေ၊ ရှုပ်ထွေးသော ဆုံးဖြတ်ချက်များတွင် မသိမ်းပိုက်အောင် သတိထားစေသည်။

**ဖွဲ့စည်းမှုသည် အပြုအမူကို ဦးတည်ပေးသည်**

prompt တွင် XML tag များ တွေ့ရပါသလား? ၎င်းများသည် အလှဆင် ချထားသော မဟုတ်ပါ။ မော်ဒယ်များသည် ဖွဲ့စည်းထားသော အမှာစာ/ညွှန်ကြားချက်များကို အစဉ်လိုက် ခိုင်မာစွာ လိုက်နာသည်။ အဆင့်များစိတ်ပိုင်းလုပ်ငန်း သို့မဟုတ် បានပုံ ထိုက်ရောက်မှုများ ရှိသောအခါ ဖွဲ့စည်းမှုသည် မော်ဒယ်ကို ဘယ်နေရာယူ၍ ဘာနောက်တစ်ဆင့် လုပ်မည်ကို စောင့်ကြည့်ရာတွင် အထောက်အကူဖြစ်သည်။

<img src="../../../translated_images/my/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

* ဖော်ပြချက်တိကျပြီး XML ပုံစံ စည်းမျဉ်းအတိုင်း ဖွဲ့စည်းထားသော prompt ၏ဖွဲ့စည်းပုံ*

**အရည်အသွေးကို ကိုယ်တိုင်သုံးသပ်ခြင်းဖြင့် မြှင့်တင်ခြင်း**

ကိုယ်တိုင် သုံးသပ်ရေး pattern များတွင် အရည်အသွေး စံနှုန်းများကို တိတိကျကျ ဖော်ပြပေးသည်။ မော်ဒယ်သည် "မှန်ကန်မှု" ကို မျှော်လင့်ခြင်းမဟုတ်ပဲ "မှန်ကန်ခြင်း" ဆိုသည်မှာ ဘာတွေလဲဆိုတာ (မှန်ကန်သော ဆင့်ကဲ, မှားယွင်းထိန်းချုပ်မှု, လုပ်ဆောင်မှု, လုံခြုံမှု) ပိုမိုရှင်းလင်းစွာ ဖော်ပြတယ်။ ထို့နောက် မော်ဒယ်သည် မိမိ ထုတ်ကုန်ကို သုံးသပ်ပြီး မြှင့်တင်ကောင်းမွန်စေသည်။ ၎င်းက ကုဒ်ဖန်တီးမှုတစ်ခုကို အရှေ့ရှ်စနစ် အဖြစ်ပြောင်းလဲပေးသည်။

**အကြောင်းအရာသည် ကန့်သတ်ထားသည်**

Multi-turn စကားဆိုကွင်းများတွင် မက်ဆေ့ချ်မှတ်တမ်းများကို တိုက်ရိုက် ထည့်သွင်းမှုဖြင့် အလုပ်လုပ်သည်။ သို့သော် မှတ်ချက်  ကန့်သတ်ချက်ရှိသည် - မော်ဒယ်တိုင်းတွင် အများဆုံး token ရရှိနိုင်မှုပမာဏ တည်ရှိသည်။ စကားသံများ တိုးလာသည်နှင့် ယင်း token ကန့်သတ်မှုကို ကျော်လွှားမှု မဖြစ်စေရန်နည်းလမ်းများ လိုအပ်ပါသည်။ ဒီ module သည် မှတ်ဥာဏ် အလုပ်လုပ်ပုံကို ပြသပေးပြီး နောက်ပိုင်း သင်မှုအတွဲများတွင် အကျဥ်းချုပ်ခြင်း၊ မေ့၍ ပယ်ဖျက်ခြင်း၊ ပြန်လည် ရယူခြင်း အချိန်များကို သင်ပါမည်။

## နောက်တစ်ဆင့်

**နောက်ထပ် Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**လမ်းညွှန်:** [← ယခင်: Module 01 - မိတ်ဆက်](../01-introduction/README.md) | [ထိပ်သို့ ပြန်သွားရန်](../README.md) | [နောက်တစ်ခု: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**အာမခံချက်**  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဆာဗစ်ဖြင့် [Co-op Translator](https://github.com/Azure/co-op-translator) အသုံးပြုပြီး ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် မှန်ကန်မှုကို ကြိုးစားမှုရှိသော်လည်း အလိုအလျောက် ဘာသာပြန်ချက်များတွင် အမှားယွင်းမှုများ သို့မဟုတ် မှန်ကန်မှုမရှိမှုများဖြစ်ပေါ်နိုင်ပါသည်။ မူရင်းစာရွက်စာတမ်းကို မိဘဘာသာဖြင့်သာ အတည်ပြု အရင်းအမြစ်အဖြစ် ယူဆသင့်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက်တော့ စျေးကွက်လက်တွေ့ ဖက်ရှင်ရှိသော လူကြီးမင်းမှ ဘာသာပြန်သူတစ်ဦး၏ လက်တွေ့ ဘာသာပြန်ချက်ကို အသုံးပြုရန် အကြံပြုပါသည်။ ဤဘာသာပြန်ချက်ကို အသုံးပြုခြင်းမှ ဖြစ်ပေါ်လာသော နားမလည်မှုများ သို့မဟုတ် မှားယွင်းနားလည်မှုများအတွက် ကျွန်ုပ်တို့မည်သည့် တာဝန်မရှိပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->