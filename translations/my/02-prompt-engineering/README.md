# Module 02: GPT-5.2 နဲ့ Prompt Engineering

## အကြောင်းအရာ အကျဉ်းချုပ်

- [သင်ဘာတွေသင်ယူမှာလဲ](../../../02-prompt-engineering)
- [လိုအပ်ချက်များ](../../../02-prompt-engineering)
- [Prompt Engineering ကိုနားလည်ခြင်း](../../../02-prompt-engineering)
- [Prompt Engineering အခြေခံပညာ](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [ကြီးမားသော ပုံစံများ](../../../02-prompt-engineering)
- [ရှိပြီးသား Azure အရင်းအမြစ်များကို အသုံးပြုခြင်း](../../../02-prompt-engineering)
- [အပလီကေးရှင်း Screenshot များ](../../../02-prompt-engineering)
- [ပုံစံများကို ရှာဖွေခြင်း](../../../02-prompt-engineering)
  - [နည်းနည်း စိတ်ဝင်စားမှု နှင့် အလွန်စိတ်ဝင်စားမှု](../../../02-prompt-engineering)
  - [အလုပ်လုပ်မှု (ကိရိယာ မိတ်ဆက်စာများ)](../../../02-prompt-engineering)
  - [ကိုယ်ပိုင် သုံးသပ်ခြင်းကုဒ်](../../../02-prompt-engineering)
  - [ဖွဲ့စည်းထားသော သုံးသပ်မှု](../../../02-prompt-engineering)
  - [တစ်နှစ်ကြိမ် မက်ဆေ့ချ်](../../../02-prompt-engineering)
  - [တစ်ဆင့်ချင်း ပြဿနာ ဖြေရှင်းနည်း](../../../02-prompt-engineering)
  - [နည်းနှင့် နယ်နိမိတ္ထိန်းချုပ်ထားသော output](../../../02-prompt-engineering)
- [သင်တကယ် သင်ယူနေတာ](../../../02-prompt-engineering)
- [နောက်တစ်ဆင့်များ](../../../02-prompt-engineering)

## သင်ဘာတွေသင်ယူမှာလဲ

<img src="../../../translated_images/my/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Module အရင်ကမှာ သင်ဟာ memory က conversational AI ကိုဘယ်လို အထောက်အကူပြုတာကို မြင်တွေ့ခဲ့ပြီး GitHub Models ဖြင့် အခြေခံ အပြန်အလှန်တွေကို သုံးခဲ့တယ်။ ယခုမှာ Azure OpenAI ရဲ့ GPT-5.2 ကို အသုံးပြုပြီး မေးမြန်းချက် (prompt) များကို ဘယ်လို မေးမြန်းရမလဲဆိုတာကို ဦးတည်လုပ်ဆောင်မှာဖြစ်တယ်။ သင့်မေးမြန်းချက်များကို အဆင့်တကျ ဖွဲ့စည်းပုံသည် တုံ့ပြန်ချက်အရည်အသွေးကို ထိရောက်စေသည်။ အခြေခံ prompting နည်းများကို ပြန်လည် သုံးသပ်ပြီးနောက် GPT-5.2 ၏ စွမ်းဆောင်ရည်အပြည့်အဝကို အသုံးချနိုင်သော အဆင့်မြင့် ပုံစံ ရှစ်မျိုးကို ကြည့်ရှုမှာဖြစ်ပါတယ်။

GPT-5.2 ကို ရွေးချယ်သည့် အကြောင်းအရင်းမှာ reasoning control ကို ထည့်သွင်းပေးထားတာကြောင့်ဖြစ်ပြီး သင်က မေးခွန်းကို ဘယ်လောက်စဉ်းစားမှန်း မော်ဒယ်ကို ညွှန်ကြားနိုင်ပါတယ်။ ဒါက prompting နည်းပညာ မျိုးစုံကို ပိုမိုရှင်းလင်းစေပြီး မည်သည့်နည်းလမ်းကို ဘယ်အခါအသုံးပြုရမည့် အချက်များကို နားလည်ရန် အထောက်အကူဖြစ်စေသည်။ Azure ရဲ့ GPT-5.2 အတွက် rate limits နည်းသော အားသာချက်ကိုလည်း GitHub Models နှင့်နှိုင်းယှဉ်လို့ ရယူနိုင်ပါတယ်။

## လိုအပ်ချက်များ

- Module 01 ကို ပြီးမြောက်ပြီးသားဖြစ်ရမည် (Azure OpenAI အရင်းအမြစ်များ တပ်ဆင်ပြီး)
- Root directory ထဲတွင် `.env` ဖိုင်ရှိပြီး Azure လက်မှတ်များ ထည့်သွင်းထားရမည် (Module 01 မှ `azd up` ဖြင့် ဖန်တီးသည်)

> **မှတ်ချက်**: Module 01 မပြီးမြောက်သေးပါက အဲ့ဒီမှာရှိသော တပ်ဆင်နည်းများကို ပထမဦးဆုံး လိုက်နာပါ။

## Prompt Engineering ကိုနားလည်ခြင်း

<img src="../../../translated_images/my/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering ဆိုတာ သင်လိုချင်တဲ့ ရလဒ်ကို ခိုင်မာစွာ ရရှိနိုင်ရန် သတင်းအချက်အလက် input ကို ဒီဇိုင်းဆွဲပုံဖြစ်ပါတယ်။ မေးခွန်း မေးတာတစ်ခုတည်း မဟုတ်ဘဲ မော်ဒယ်က သဘောပေါက်တတ်၊ ဖြေဆိုနည်းကို ကျွမ်းကျင်စေရန် ဖွဲ့စည်းမှုများ ပါဝင်သည်။

ဒါကို အလုပ်ချင်းဦးဆောင်သူအား ညွှန်ကြားချက်ပေးသလို ထင်နိုင်ပါတယ်။ “Bug ကို ပြင်ပါ” ဆိုတာ မရှင်းလင်းသေးပါဘူး။ “UserService.java အတွက် line 45 တွင် null pointer exception ကို null check ထည့်၍ ပြင်ပါ” ဆိုတာ ပိုမို သေချာပေါက်တယ်။ Language model များလည်း အတူတူပဲ specificity နဲ့ ဖွဲ့စည်းမှုတွေ အရေးကြီးပါတယ်။

<img src="../../../translated_images/my/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j သည် infrastructure ကို ပံ့ပိုးပေးပြီး — မော်ဒယ်ချိတ်ဆက်မှု၊ memory နှင့် message အမျိုးအစားများကို ထောက်ပံ့သည်။ prompt ပုံစံများကတော့ ကောင်းမွန်စွာ ဖော်ပြထားသော စာသားများသာ ဖြစ်ပါသည်။ အချက်အသား အခြေဆောက်မှုများမှာ `SystemMessage` (AI ၏ လုပ်ဆောင်ချက်နှင့် person နေရာ သတ်မှတ်ခြင်း) နှင့် `UserMessage` (သင်၏ တုံ့ပြန်ချက် actual များကို သယ်ဆောင်ခြင်း) တို့ ဖြစ်ပါတယ်။

## Prompt Engineering အခြေခံပညာ

<img src="../../../translated_images/my/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

ဒီ module ၏ advanced ပုံစံများကို စတင်ခြင်းမပြုမီ အခြေခံ prompting နည်းလမ်းငါးမျိုးကို ပြန်လည်ရှုမြင်ကြရအောင်။ ၎င်းတို့သည် prompt engineer တစ်ယောက် အမြဲသိထားသင့်သော အခြေခံ ပစ္စည်းများ ဖြစ်သည်။ [Quick Start module](../00-quick-start/README.md#2-prompt-patterns) ကို ကြည့်ရှုပြီးသားလျှင် ဒီပုံစံများကို လုပ်ဆောင်နည်း စနစ်တကျ သိရှိထားပါပြီ။

### Zero-Shot Prompting

အလွယ်ဆုံး နည်းလမ်း - မော်ဒယ်ကို နမူနာမပေးဘဲ တိုက်ရိုက်ညွှန်ကြားချက်ပေးခြင်း။ မော်ဒယ်သည် သူ၏လေ့လာမှုပေါ် မူတည်ပြီး တာဝန်ကို သဘောပေါက်ကာ ဆောင်ရွက်သည်။ ရိုးရှင်းသော မေးခွန်းများ အတွက် အဆင်ပြေတယ်။

<img src="../../../translated_images/my/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*နမူနာမပါဘဲ တိုက်ရိုက်ညွှန်ကြားချက် — မော်ဒယ်သည် အညွှန်းပေးချက်မှ တာဝန်အား ခန့်မှန်းသည်*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// တုံ့ပြန်ချက် - "အတည်ပြုသည်"
```

**အသုံးပြုသင့်ချိန်**: ရိုးရှင်းသော အမျိုးအစားခွဲခြားမှုများ၊ တိုက်ရိုက်မေးခွန်းများ၊ ဘာသာပြန်များ သို့မဟုတ် မော်ဒယ်က လမ်းညွှန်ချက်အပိုပေးစရာ မလိုအပ်တဲ့ တာဝန်များ။

### Few-Shot Prompting

မော်ဒယ်အား လိုအပ်သည့် နမူနာတွေ ပေးပြီး မော်ဒယ်သည် နမူနာများမှသည် input-output ပုံစံကို သင်ယူကာ အသစ်လုပ်ဆောင်သည်။ ရည်ရွယ်ထားသော ဖော်မြူလာ သို့မဟုတ် လုပ်ဆောင်မှု မရှင်းလင်းသေးသော အလုပ်များတွင် အသုံးဝင်သည်။

<img src="../../../translated_images/my/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*နမူနာများမှ သင်ယူခြင်း — မော်ဒယ်သည် ပုံစံကို ကျင့်သုံးသည်*

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

**အသုံးပြုသင့်ချိန်**: မတူညီသော အမျိုးအစားခွဲခြားမှုများ၊ စနစ်တကျ ဖော်မတ်များ၊ domain-specific task များ သို့မဟုတ် zero-shot အဖြေများ မတိကျသည့်အခါ။

### Chain of Thought

မော်ဒယ်ကို အဆင့်လိုက် သီဖြေခွင့်ပေး၍ စဉ်းစားမှုကို ပြသစေသည်။ ဖြေဆိုချက်သို့ တိုက်ရိုက် ရောက်တော့မည်မဟုတ်ဘဲ ပြဿနာကို အပိုင်းပိုင်း ခွဲ၍ ဖြေရှင်းသည်။ သင်္ချာ၊ ယုတျ၊ နှင့် multi-step reasoning အတွက် တိကျမှု မြှင့်တင်သည်။

<img src="../../../translated_images/my/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*အဆင့်လိုက် စိတ်ကြိုက် လက်တွေ့ ရှင်းလင်းချက်ပေးသည်*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// မော်ဒယ်က ပြထားတာက ၁၅ - ၈ = ၇, ထို့နောက် ၇ + ၁၂ = ဆမ်ပယ် ၁၉ လုံး ဖြစ်သည်။
```

**အသုံးပြုသင့်ချိန်**: သင်္ချာပြဿနာများ၊ ယုတျပြဿနာများ၊ debugging၊ သို့မဟုတ် စိတ်ကြိုက် လက်တွေ့ဖော်ပြချက်များ လိုအပ်သော တာဝန်များ။

### Role-Based Prompting

မေးခွန်းမေးမီ AI ၏ တာဝန်သတ်မှတ်ချက် သို့မဟုတ် နေရာ အသတ်မှတ်ပေးခြင်းဖြင့် ပြဿနာ၏ တုံ့ပြန်မှုသဏ္ဍာန်များ၊ အနက်ဖြင့် ပြောင်းလဲစေနိုင်သည်။ “software architect” နှင့် “junior developer” သို့မဟုတ် “security auditor” များ၏ တုံ့ပြန်ချက် မတူကြောင်း တွေ့ရသည်။

<img src="../../../translated_images/my/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*အကြောင်းအရာနှင့် တာဝန်သတ်မှတ်ခြင်း — တူညီသောမေးခွန်းကို လုပ်ရပ်ကွဲဟန် အမျိုးမျိုးဖြင့် ဖြေဆိုသည်*

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

**အသုံးပြုသင့်ချိန်**: ကုဒ်စိစစ်ခြင်းများ၊ သင်ကြားရေး၊ domain-specific သုံးသပ်မှုများ၊ သို့မဟုတ် အတတ်ပညာအဆင့် သို့မဟုတ် ရှုမြင်ချက်အပေါ် မူတည်၍ ဖြေချက်များ လိုအပ်သည့်အခါ။

### Prompt Templates

ပြန်လည်အသုံးပြုနိုင်သော prompt များကို variable placeholders ဖြင့် ဖန်တီးပါ။ မည်သည့်အခါမဆို prompt အသစ်ရေးသားရန်မလိုဘဲ template တစ်ခု သတ်မှတ်ပြီး အမျိုးမျိုးသောတန်ဖိုးများ ထည့်သွင်းအသုံးပြုနိုင်သည်။ LangChain4j ရဲ့ `PromptTemplate` အတန်းက `{{variable}}` syntax ဖြင့် လွယ်ကူစေသည်။

<img src="../../../translated_images/my/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*variable placeholder များ ပါသော ပြန်လည်အသုံးပြုနိုင်သော prompt များ*

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

**အသုံးပြုသင့်ချိန်**: အမျိုးမျိုးသော အဝင်များနှင့် မေးခွန်းများ၊ batch processing၊ ပြန်လည်အသုံးပြုနိုင်သော AI workflow များ ဖန်တီးစဉ်၊ prompt ဖော်စပ်မှု မပြောင်းလဲပဲ ဒေတာသာ ပြောင်းလဲသည့် အခြေအနေများတွင်။

---

ဒီအခြေခံနည်းလမ်းငါးမျိုးက prompting task များအတွက် မုခ်တကာပစ္စည်းပေးတယ်။ ဒီ module မှာတော့ GPT-5.2 ၏ reasoning control, self-evaluation နှင့် ဖွဲ့စည်းထားသော output စွမ်းဆောင်ချက်များကို အပြည့်အဝ အသုံးချနိုင်မယ့် **အဆင့်မြင့် ပုံစံရှစ်မျိုး** နဲ့ ဆက်လက်ဖွံ့ဖြိုးတည်ဆောက်ထားပါတယ်။

## ကြီးမားသော ပုံစံများ

အခြေခံနည်းလမ်းများကို ဖို့၍ module ၏ထူးခြားသော ပုံစံရှစ်မျိုးသို့ ရောက်ရှိပါပြီ။ ပြဿနာအားလုံးအတွက် တူညီသော နည်းလမ်း မလိုအပ်ပါ။ မေးခွန်းတချို့မှာ အမြန်ဆုံး ဖြေချက်လိုအပ်ပြီး၊ တချို့မှာတော့ အနက်ရှိုင်းစွာ စဉ်းစားရန်လိုသည်။ တချို့မှာ reasoning တင်ပြနည်း လိုအပ်ပြီး တချို့မှာ ရလဒ်ပဲ လိုအပ်သည်။ အောက်ပါ ပုံစံစုံက စီမံချက်အမျိုးအစားတစ်ခုချင်းစီအတွက် တိကျစွာ အဆင်သင့်ဖြစ်ပြီး GPT-5.2 ၏ reasoning control ကလည်း ကွဲပြားမှုတွေကို ပိုမိုထင်ရှားစေသည်။

<img src="../../../translated_images/my/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*prompt engineering ပုံစံရှစ်မျိုး နှင့် အသုံးပြုမှုများ အနှံ့အပြားကို သိရှိမှု*

<img src="../../../translated_images/my/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 ၏ reasoning control သည် မော်ဒယ်ကို ဘယ်လောက် စဉ်းစားရမည်ကို သတ်မှတ်နိုင်စေသည် — အမြန် ဖြေချက် ပေးမယ်၊ အနက်ရှိုင်း စူးစမ်းမယ်*

**နည်းနည်း စိတ်ဝင်စားမှု (အမြန်နှုန်းနှင့် ပစ်ခတ်ချက်)** - ရိုးရှင်းသော မေးခွန်းများအတွက် အမြန် တိုက်ရိုက် ဖြေချက်လိုအပ်သော အခါ အသုံးပြုပါ။ မော်ဒယ်သည် reasoning အနည်းဆုံး — အဆင့် ၂ ထိသာ ကြိုးစားသည်။ တွက်ချက်မှု၊ ရှာဖွေမှု သို့မဟုတ် ရိုးရှင်းသော မေးခွန်းများ အတွက် အသုံးပြုနိုင်သည်။

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

> 💡 **GitHub Copilot ဖြင့် စမ်းသပ်ရန်:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ဖိုင်ကို ဖွင့်ပြီး မေးမြန်းပါ။
> - "နည်းနည်း စိတ်ဝင်စားမှုနဲ့ အလွန်စိတ်ဝင်စားမှု prompting ပုံစံကွာခြားချက်ဘာလဲ?"
> - "Prompt များထဲ XML tags များက AI ရဲ့ တုံ့ပြန်မှု ဖွဲ့စည်းပုံကို ဘယ်လို ကူညီပေးသလဲ?"
> - "Self-reflection ပုံစံတွေကို မည်သို့ အသုံးပြုသင့်ပြီး တိုက်ရိုက်ညွှန်းကြားချက်နဲ့ ဘယ်အခါအသုံးပြုသင့်လဲ?"

**အလွန်စိတ်ဝင်စားမှု (နက်ရှိုင်းပြီး ပြည့်စုံမှု)** - ရည်းစား ပြဿနာများတွင် စုံလင်သော သုံးသပ်ချက်လိုအပ်သောအခါ အသုံးပြုပါ။ မော်ဒယ်သည် အသေးစိတ် စဉ်းစားမှု ပြသပြီး ပိုမိုတွေ့မြင်မှုရစေသည်။ စနစ်ဒီဇိုင်း၊ ပုံသေ ဆုံးဖြတ်ချက်များ သို့မဟုတ် ရှုပ်ထွေးသော သုတေသနများအတွက် အသုံးပြုနိုင်သည်။

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**အလုပ်လုပ်မှု (တစ်ဆင့်ချင်း တိုးတက်မှု)** - အဆင့်စီ workflow များအတွက်။ မော်ဒယ်သည် မူကြမ်းအစီအစဉ်တစ်ခု ပေးပြီး တစ်ဆင့်ချင်း ရှင်းပြသည်နှင့် နောက်ဆုံးမှာ စုပေါင်း တင်ပြချက် ပေးသည်။ စီမံကိန်းပြောင်းရွှေ့ခြင်း၊ အကောင်အထည်ဖော်ခြင်း သို့မဟုတ် များစွာသော အဆင့်အစီအစဉ် လုပ်ငန်းများအတွက် အသုံးပြုသည်။

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

Chain-of-Thought prompting သည် reasoning လုပ်ငန်းစဉ်ကို ဖော်ပြရန် မော်ဒယ်အား တိုက်တွန်းထားပြီး ရှုပ်ထွေးသော တာဝန်များအတွက် တိကျမှု မြှင့်တင်ပေးသည်။ အဆင့်လိုက် ခွဲခြမ်းစိတ်ဖြာမှုက လူနဲ့ AI နှစ်ဦးလုံး logic ကို နားလည်ရန် အထောက်အကူဖြစ်သည်။

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ပါ:** ဒီပုံစံအကြောင်း မေးမြန်းပါ။
> - "Task execution pattern ကို အချိန်ကြာခံ အလုပ်တွင် မည်သို့ အလိုက်ဖက်စေမလဲ?"
> - "ထုတ်လုပ်မှုပြုသော အပလီကေးရှင်းများမှာ tool preambles ကို စနစ်တကျ ဖွဲ့စည်းရာတွင် ဘာတွေ သင်ယူရမလဲ?"
> - "UI အတွင်း အတန်းလိုက် တိုးတက်မှု အသွားအလာများ ကို မည်သို့ စောင့်ကြည့် ပြသနိုင်မလဲ?"

<img src="../../../translated_images/my/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*စီမံချက် → လိုက်နာမှု → စုပေါင်းတင်ပြခြင်း workflow*

**ကိုယ်ပိုင် သုံးသပ်ခြင်းကုဒ်** - ထုတ်လုပ်မှုအဆင့်အရည်အသွေးကုဒ်ဖန်တီးရာတွင် အသုံးပြုသည်။ မော်ဒယ်သည် အမှားများကို ဂရုပြု၍ ထုတ်လုပ်မှုစံချိန်များနှင့် ကိုက်ညီသော ကုဒ်ဖိုင်များပြန်လည်ထုတ်လုပ်သည်။ အသစ် feature များ သို့ဝန်ဆောင်မှုများ အတွက် အသုံးပြုသည်။

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/my/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*သွယ်ဝိုက်တောက်ပြော အဆင့်မြှင့်တင်မှု လည်ပတ်မှု - ထုတ်လုပ်ခြင်း၊ သုံးသပ်ခြင်း၊ ပြဿနာ ဖော်ထုတ်ခြင်း၊ မြှင့်တင်ခြင်း၊ ထပ်မံလုပ်ခြင်း*

**ဖွဲ့စည်းထားသော သုံးသပ်မှု** - တိကျမွန်ကန်ပြီး အဆင့်တက်ညီသော သုံးသပ်မှု။ မော်ဒယ်သည် ကုဒ်ကို စနစ်တကျ ပြန်လည်စစ်ဆေးပြီး လုပ်ထုံးလုပ်နည်းများ၊ စွမ်းဆောင်ရည်၊ လုံခြုံရေးနှင့် တာဝန်ခံမှုများကို သုံးသပ်သည်။ ကုဒ်စိစစ်မှု သို့မဟုတ် အရည်အသွေး တုံ့ပြန်မှုများအတွက် အသုံးပြုသည်။

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ပါ:** ဖွဲ့စည်းထားသော သုံးသပ်မှုအကြောင်း မေးမြန်းပါ။
> - "အမျိုးမျိုးသော ကုဒ်စစ်ဆေးမှု အတွက် သုံးသပ်မှု framework ကို မည်သို့ သိသာထင်ရှားစွာ ချိန်ညှိစေရမလဲ?"
> - "ဖွဲ့စည်းထားသော output ကို ဘယ်လို တ برنامهmatically ရေထန်ပြီး လုပ်ဆောင်မလဲ?"
> - "အမျိုးမျိုးသော သုံးသပ်မှုများတွင် severity level ကို သွက်လက်ညီတောက်စွာ ကြောင့် မည်သို့ဆောင်ရွက်မလဲ?"

<img src="../../../translated_images/my/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*တပ်ဆင်ထားသော severity level အားဖြင့် အဆင့်တကာ ကုဒ်စစ်ဆေးမှု ဖွဲ့စည်းမှု*

**တစ်နှစ်ကြိမ် မက်ဆေ့ချ်** - အကြောင်းအရာလိုအပ်သော စကားပြော။ မော်ဒယ်သည် ရှေ့က မက်ဆေ့များကို မှတ်ထားပြီး ထပ်ဆင့်ဆက်သွယ်နေသည်။ ကူညီရေးခြင်း နည်းနည်းများ သို့မဟုတ် ရှုပ်ထွေးသော Q&A များတွင် အသုံးပြုသည်။

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

*စကားပြောအကြောင်းအရာ မက်ဆေ့မြည်းကြောင့် Token ကန့်သတ်မှတ်ချက် ရောက်ရန် စုပေါင်းမှု*

**တစ်ဆင့်ချင်း ပြဿနာ ဖြေရှင်းနည်း** - သိသာထင်ရှားသော logic ကြိုပြရသောပြဿနာများအတွက်။ မော်ဒယ်သည် အဆင့်တစ်ဆင့် အသေအချာ reasoning ပြသသည်။ သင်္ချာဖြေရှင်းမှုများ၊ ယုတျများ သို့မဟုတ် စဉ်းစားပုံကို နားလည်လိုသော အခါ အသုံးပြုသည်။

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

*ပြဿနာများကို သိသာထင်ရှားသော လိုဂစ်ဆ့်ဆင့်များ ခွဲခြမ်းသည်*

**နည်းနှင့် နယ်နိမိတ် ထိန်းချုပ်ထားသော output** - တိကျသော ဖော်မြူလာလိုအပ်သော အကြောင်းအရာများအတွက် ရိုက်ပုံစံကြောင့် output ကို ထိန်းသိမ်းသည်။ အကျဉ်းချုပ်များ သို့မဟုတ် ပုံစံတိကျစွာ output လိုအပ်သောအခါ အသုံးပြုသည်။

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

*ဖော်မြူလာ၊ အရွယ်အစားနှင့် ဖွဲ့စည်းမှု အတိအကျ ထိန်းသိမ်းခြင်း*

## ဖြစ်ပြီးသား Azure အရင်းအမြစ်များကို အသုံးပြုခြင်း

**တပ်ဆင်မှု အတည်ပြုရန်:**

Root directory တွင် Azure လက်မှတ်ပါ `.env` ဖိုင် ရှိပြီးသားဖြစ်သည်ကို အတည်ပြုပါ (Module 01 အတွင်း ဖန်တီးထားသည်) -
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT၊ API_KEY၊ DEPLOYMENT ကို ပြသသင့်သည်။
```

**အပလီကေးရှင်း စတင်ရန်:**

> **မှတ်ချက်:** Module 01 မှ `./start-all.sh` ဖြင့် အားလုံး အပလီကေးရှင်းများ စတင်ပြီးသားဖြစ်ပါက အဆိုပါ Module ကို port 8083 တွင် စတင်ပြီးဖြစ်သည်။ အောက်တွင် ဖော်ပြထားသော စတင်ရန် အမိန့်များကို ကျော်လွှားပြီး http://localhost:8083 သို့ တိုက်ရိုက်သွားနိုင်ပါသည်။

**ရွေးချယ်မှု ၁: Spring Boot Dashboard ဖြင့် အသုံးပြုခြင်း (VS Code အသုံးပြုသူများအတွက် အကြံပြုချက်)**

Dev container တွင် Spring Boot Dashboard extension ပါဝင်ပြီး Spring Boot အပလီကေးရှင်းအားလုံးကို တစ်နေရာတည်း မှတ်တမ်း ထိန်းသိမ်းကာ မျှဝေမှု ရရှိစေရန် ဗီဇွယ် အ(interface) ပေးသည်။ VS Code ၏ ဘယ်ဘက်လုပ်ဆောင်မှုဘားတွင် Spring Boot အိုင်ကွန်ကို ရှာဖွေပါ။

Spring Boot Dashboard မှ အောက်ခံသည့် အလုပ်များပြုနိုင်သည် -
- ပတ်ဝန်းကျင်ရှိရှိ Spring Boot အပလီကေးရှင်းအားလုံး ကြည့်ရှုနိုင်သည်
- တစ်ချက်နှိပ်၍ အပလီကေးရှင်းကို စတင်/ပိတ်နိုင်သည်
- အမှာစာများကို အချိန်နှင့်တပြေးညီ ကြည့်ရှုနိုင်သည်
- အပလီကေးရှင်း အခြေအနေကို စောင့်ကြည့်နိုင်သည်
ပရောမ့်အင်ဂျင်နီယာရီးိုင်န့် "prompt-engineering" အနားရှိ Play ခလုတ်ကိုနှိပ်ပြီး ဤမော်ဂျူးကို စတင်လုပ်ဆောင်နိုင်သည်၊ သို့မဟုတ် မော်ဂျူးအားလုံးကို တစ်ပြိုင်နက်စတင်နိုင်သည်။

<img src="../../../translated_images/my/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ရွေးချယ်မှု ၂: shell script များအသုံးပြုခြင်း**

ဝဘ်အက်ပလီကေးရှင်းအားလုံး (မော်ဂျူး ၀၁-၀၄) ကို စတင်ရန် -

**Bash:**
```bash
cd ..  # မူလဖိုလ်ဒါမှ
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # မူလ ဖိုင်တိုက်မှ
.\start-all.ps1
```

သို့မဟုတ် ဤမော်ဂျူးကိုသာ စတင်ပါ -

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

အဆိုပါ script နှစ်ခုလုံးသည် root `.env` ဖိုင်မှ ပတ်ဝန်းကျင်အချက်အလက်များကို အလိုအလျောက် load လုပ်ပြီး JAR ဖိုင်များ မရှိပါက တည်ဆောက်ပေးမည် ဖြစ်သည်။

> **မှတ်ချက်:** မော်ဂျူးအားလုံးကို စတင်ရန် မတိုင်မှီ ကိုယ်တိုင်တည်ဆောက်လိုပါက -
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

သင့် browser တွင် http://localhost:8083 ကို ဖွင့်လိုက်ပါ။

**ရပ်နားရန်:**

**Bash:**
```bash
./stop.sh  # ဒီမော်ဂျူးသာ
# ဒါမှမဟုတ်
cd .. && ./stop-all.sh  # အားလုံးသောမော်ဂျူးများ
```

**PowerShell:**
```powershell
.\stop.ps1  # ဤမော်ဒျူးသာ
# သို့မဟုတ်
cd ..; .\stop-all.ps1  # မော်ဒျူးအားလုံး
```

## Application Screenshots

<img src="../../../translated_images/my/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*အဓိက dashboard သည် အားလုံး ၈ ခုပရောမ့်အင်ဂျင်နီယာရီးိုင်န့် ပုံစံများကို ၎င်းတို့၏ လက္ခဏာများနှင့် အသုံးပြုမှုကိစ္စများဖြင့်ပြသည်*

## ပုံစံများကို ရှာဖွေသုံးသပ်ခြင်း

ဝဘ်အင်တာဖေ့စ်သည် မတူညီသော prompting များကို စမ်းသပ်နိုင်ပါသည်။ ပုံစံတိုင်းသည် ကိစ္စကွဲပြားမှုများကို ဖြေရှင်းသည် - ၎င်းတို့ကို စမ်းသပ်ပြီး ဘယ်အချိန်၌ မည်သည့်နည်းလမ်းက အချိန်ကောင်းစွာ စွမ်းဆောင်ထိထိရောက်ရောက် ဖြစ်သည်ကို ကြည့်ရှုနိုင်သည်။

> **မှတ်ချက်: Streaming နှင့် Non-Streaming** — ပုံစံများအားလုံး စာမျက်နှာတွင် နှစ်မျိုးသော ခလုတ်နှစ်ခု ရှိသည်။ **🔴 Stream Response (Live)** နှင့် **Non-streaming** ရွေးချယ်မှု။ Streaming သည် Server-Sent Events (SSE) ကို သုံးပြီး မော်ဒယ်သည် တိုးတက်သော token များကို ခဏခဏပြသပေးသဖြင့်၊ တုံ့ပြန်မှုကိုချက်ချင်းမြင်သာစေရန် ဖြစ်သည်။ Non-streaming ရွေးစရာသည် တုံ့ပြန်မှုပုံစံ တစ်ခုလုံး ပြီးမှ ပြသပေးသည်။ အထူးသဖြင့် နက်ရှိုင်းသော စဉ်းစားမှု လိုအပ်သော prompting များ (ဥပမာ - High Eagerness, Self-Reflecting Code) တွင် non-streaming ခေါ်ဆိုမှုမှတစ်ခါတစ်ရံ မိနစ်များကြာကြာကြာရှည်နိုင်ပြီး မြင်သာသည့် feedback မရနိုင်ပါ။ **ပရောမ့်တိုးတက်သော စမ်းသပ်မှုများတွင် streaming ကို အသုံးပြုရန်** သောကြောင့် မော်ဒယ် အလုပ်လုပ်နေသည်ကို မြင်နိုင်ပြီး သတင်းမရသော အခက်အခဲဖြစ်ပေါ်မှုကို ရှောင်ရှားနိုင်ပါသည်။
>
> **မှတ်ချက်: Browser လိုအပ်ချက်** — Streaming feature သည် Fetch Streams API (`response.body.getReader()`) ကို အသုံးပြုသည်၊ ၎င်းသည် Chrome, Edge, Firefox, Safari ကဲ့သို့သော မူကြမ်း browser များလိုအပ်သည်။ VS Code built-in Simple Browser တွင် မအလုပ်လုပ်ပါ၊ အကြောင်းက သူ၏ webview သည် ReadableStream API ကို မပံ့ပိုးပါ။ Simple Browser သုံးလျှင် non-streaming ခလုတ်များ အများအားဖြင့် အလုပ်လုပ်သော်လည်း streaming ခလုတ်များသာ ထိခိုက်သည်။ အပြည့်အစုံ အတွေ့အကြုံရရန် `http://localhost:8083` ကို အပြင် browser တွင် ဖွင့်ပါ။

### Low vs High Eagerness

"200 ရဲ့ ၁၅% ဘာလဲ?" ဆိုသော ရိုးရှင်းသောမေးခွန်းကို Low Eagerness ဖြင့် မေးပါ။ ချက်ချင်း ဝင်ရိုးတန်းဖြေကြားချက်ရမည်။ အခုတော့ "Traffic များသော API အတွက် caching strategy တည်ဆောက်ပုံ" ဆိုသော ရှုပ်ထွေးသော မေးခွန်းကို High Eagerness ဖြင့် မေးပါ။ **🔴 Stream Response (Live)** ကိုနှိပ်ပြီး မော်ဒယ်၏ အသေးစိတျး စဉ်းစားမှုများကို token အလိုက် ကြည့်ရှုနိုင်မည်။ တူညီသော မော်ဒယ်၊ တူညီသော မေးခွန်းဘောင်များ ဖြစ်ပေမယ့် prompt က စဉ်းစားမှု အဆင့်ကို သတ်မှတ်ပေးသည်။

### Task Execution (Tool Preambles)

အဆင့်အချင်းများရှိ workflow များသည် အစီအစဉ် တင်ပြခြင်းနှင့် ပြဿနာဆွေးနွေးခြင်းကို လိုအပ်သည်။ မော်ဒယ်သည် ဆောင်ရွက်မည့်အကြောင်းအရာကို အကြံပြု၊ အဆင့်လိုက် ရှင်းပြပြီး နောက်ဆုံးတွင်ရလဒ်များကို အကျဉ်းချုပ် ပြောကြားသည်။

### Self-Reflecting Code

"အီးမေးလ် မှန်ကန်မှု ဆာဗစ်တစ်ခု ဖန်တီးပါ" ဆိုပြီး စမ်းသပ်ပါ။ ကုဒ်ကို တိုက်ရိုက်ဖန်တီးပြီး ရပ်တန့်ခြင်း မလုပ်ပဲ၊ မော်ဒယ်သည် ဖန်တီး၊ အရည်အသွေးဆိုင်ရာ စံချိန်များနှင့်နှိုင်းယှဉ်သုံးသပ်၊ အားနည်းချက်များ ပြသပြီး တိုးတက်မှု ဖန်တီးသည်။ အဝါရောင်ထိန်းသိမ်းမှု အဆင့်မှ ရှင်းဝါးသည်အထိ ထပ်တူထပ်ခြား iteration များကြည့်ရှုနိုင်မည်။

### Structured Analysis

ကုဒ် စစ်ဆေးခြင်းသည် တည်ငြိမ်သော သတ်မှတ်ချက်များ လိုအပ်သည်။ မော်ဒယ်သည် ကုဒ်ကို မှန်ကန်မှု၊ အလေ့အထ၊ စွမ်းဆောင်ရည်၊ လုံခြုံရေး ကဏ္ဍများဖြင့် စစ်ဆေးသည်၊ ပြင်းထန်မှု အဆင့်ဖြင့် သတ်မှတ်သည်။

### Multi-Turn Chat

"Spring Boot ဆိုတာဘာလဲ?" ဟု မေးပြီးနောက် "ဥပမာတစ်ခုပြပါ" ဟု ချက်ချင်း ဆက်မေးနိုင်သည်။ မော်ဒယ်သည် သင့်ပထမမေးခွန်းကို မှတ်ဉာဏ်၌ ထိန်းသိမ်းထားပြီး Spring Boot ဥပမာကို သီးသန့် ပေးသည်။ မှတ်ဉာဏ်မရှိရင် ဒုတိယမေးခွန်းသည် မရှင်းလင်းနိုင်သကဲ့သို့ ဖြစ်လိမ့်မည်။

### Step-by-Step Reasoning

ဂဏန်းတွက်ခေါ်ပြဿနာတစ်ခု ရွေးပြီး Step-by-Step Reasoning နှင့် Low Eagerness နှစ်ခုစလုံးဖြင့် စမ်းပါ။ Low Eagerness နှင့် စမ်းပါက ဖြေချက်ကို ချက်ချင်းရသည်၊ တော့ ဖော်ပြချက်ပဲမပါသော နည်းလမ်းဖြစ်သည်။ Step-by-step နည်းလမ်းဖြင့် တစ်ဆင့်ခြင်း တွက်ချက်ချက်များနှင့် ဆုံးဖြတ်ချက်အားလုံး ပြသသည်။

### Constrained Output

အတိအကျ ပုံစံ သို့မဟုတ် စကားလုံးရေလိုအပ်ပါက ဤပုံစံသည် တိကျစွာလိုက်နာစေသည်။ တိတိကျကျ ၁၀၀ စကားလုံး အချက်အလက် စာရင်းပြုလုပ်မှုကို စမ်းပါ။

## မင်း အမှန်တကယ် သင်ယူနေသည့်အရာ

**စဉ်းစားစရာ ကြိုးပမ်းမှု က အရာအားလုံးပြောင်းလဲစေသည်**

GPT-5.2 သည် မင်း၏ prompt များမှတဆင့် ကွန်ပျူးတေးရှင်း ကြိုးပမ်းမှုကို ထိန်းချုပ်ခွင့် ပေးသည်။ ကြိုးပမ်းအားနည်းလျှင် မြန်ဆန်သော တုံ့ပြန်မှုများရရှိပြီး ရှာဖွေမှုနည်းပါးသည်။ ကြိုးပမ်းအားပြင်း အခါမှာ မော်ဒယ်သည် နက်ရှိုင်းစွာစဉ်းစားရန်အချိန်ယူသည်။ မင်းသည် ရိုးရှင်းသောမေးခွန်းများအတွက်အချိန်နှောင့်ယှက်မှုမယူဘဲ၊ ရှုပ်ထွေးသော ဆုံးဖြတ်ချက်များအတွက် အချိန်မလျှော့ပဲ ကြိုးစားရမည်ကို သင်ယူလျက်ရှိသည်။

**ဖွဲ့စည်းမှုသည် အပြုအမူကို လမ်းညွှန်သည်**

prompt များရှိ XML tag များကို သတိပြုပါ။ ၎င်းတို့သည် အလှဆင်ခြင်း မဟုတ်ပါ။ မော်ဒယ်များသည် ဖွဲ့စည်းထားသော ညွှန်ကြားချက်များကို လွယ်ကူစွာ လိုက်နာသည်။ အဆင့်အပိုင်းများနှင့် ရှုပ်ထွေးသော လိုဂစ်များ လိုအပ်သည့်အခါ ဖွဲ့စည်းမှုသည် မော်ဒယ်အား နေရာနှင့် နောက်တစ်ဆင့်ကို စောင့်ကြည့်နိုင်စေနိုင်သည်။

<img src="../../../translated_images/my/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*ရှင်းလင်းသော အပိုင်းများနှင့် XML ပုံစံ ဖွဲ့စည်းထားသော ပရောမ့်၏ အစိတ်အပိုင်းပြင်ပ*

**အရည်အသွေးသည် ကိုယ်တိုင်အကဲဖြတ်မှုမှတဆင့် ရရှိသည်**

self-reflecting ပုံစံများသည် အရည်အသွေး ထိန်းသိမ်းသည့် ကိန်းဂဏန်းများကို ဖော်ပြသည်။ မော်ဒယ်အား "မှန်ကန်" ဆောင်ရွက်မည်ဟု မမျှော်လင့်ဘဲ "မှန်ကန်" ဆိုသည်မှာ ဘာမှန်သည်ကို တိတိကျကျ ပြောပြသည်။ မှန်ကန်သော လိုဂစ်၊ အမှားစစ်ဆေးမှု၊ စွမ်းဆောင်ရည်၊ လုံခြုံရေး စသည်တို့ပါဝင်သည်။ ထို့နောက် မော်ဒယ်သည် ကိုယ်ပိုင်ထုတ်ကုန်ကို အကဲဖြတ်ပြီး တိုးတက်စေသည်။ ၎င်းဖြင့် ကုဒ်ဖန်တီးခြင်းကို အလောင်းအလ៊ံထဲမှ ပေါင်းစည်းနည်းတစ်ခု ဖြစ်စေသည်။

**အကြောင်းအရာ သတ်မှတ်ထားသည်**

Multi-turn စကားပြောဆိုမှုများတွင် မက်ဆေ့ချ်သမိုင်းကို တစ်စောင်ချင်းစီတွင် ထည့်သွင်းပေးသည်။ သို့သော် အကန့်အသတ်ရှိသည် - မော်ဒယ်တိုင်းမှာ token အများဆုံးအရေအတွက် တစ်ခုရှိသည်။ စကားပြောဆိုမှု ကြီးထွားလာသည့်အခါ ထိရောက်သော အကြောင်းအရာများကို ထိန်းသိမ်းရန် နည်းလမ်းများလိုအပ်သည်။ ဤမော်ဂျူးမှာ မှတ်ဉာဏ် အလုပ်လုပ်ပုံ၊ စုစည်းပုံ၊ မေ့ပစ်ပုံနှင့် ပြန်ယူပုံကို သင်သွားမည် ဖြစ်သည်။

## နောက်တစ်ဆင့်များ

**နောက်ဆုံး မော်ဂျူး:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**သွားလာခြင်း:** [← ပြီးခဲ့သည်: မော်ဂျူး 01 - နိဒါန်း](../01-introduction/README.md) | [ပြန်သွားမယ် - Main](../README.md) | [နောက်တစ်ဆင့်: မော်ဂျူး 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**တိမ်းမျှော်ချက်**  
ဤစာတမ်းကို AI ဘာသာပြန်ခြင်းဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် မှန်ကန်မှုအတွက် ကြိုးပမ်းဆောင်ရွက်ပါသည်၊ သို့သော် အလိုအလျောက်ဘာသာပြန်ခြင်းတွင် အမှားများ သို့မဟုတ် မှန်ကန်မှုမပြည့်စုံမှုများ ပါဝင်နိုင်ကြောင်း သိရှိရန် လိုအပ်ပါသည်။ မူရင်းစာတမ်းကို မူလဘာသာဖြင့်သာ ယုံကြည်စိတ်ချရသော အရင်းအမြစ်ဟု တွက်ဆ့ အပ်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက် အတည်ပြုရန် လူ့ဘာသာပြန်သူတစ်ဦး၏ ပရော်ဖက်ရှင်နယ် ဘာသာပြန်ခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်ချက် အသုံးပြုမှုမှ ဖြစ်ပေါ်နိုင်သည့် မှားယွင်းခြင်းများ သို့မဟုတ် အလွဲအချော်အောင်ရလဒ်များအတွက် ကျွန်ုပ်တို့အနေဖြင့် တာဝန်မယူပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->