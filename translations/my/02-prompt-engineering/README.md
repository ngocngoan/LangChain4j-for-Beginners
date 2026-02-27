# Module 02: GPT-5.2 ဖြင့် Prompt Engineering

## အကြောင်းအရာ စာရင်း

- [ဗီဒီယို လမ်းညွှန်](../../../02-prompt-engineering)
- [သင်ဘာတွေသင်မလဲ](../../../02-prompt-engineering)
- [လိုအပ်ချက်များ](../../../02-prompt-engineering)
- [Prompt Engineering ကိုနားလည်ခြင်း](../../../02-prompt-engineering)
- [Prompt Engineering မူလအခြေခံများ](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [တိုးတက်မြှင့်တင်ထားသော ပုံစံများ](../../../02-prompt-engineering)
- [ရှိပြီးသား Azure အရင်းအမြစ်များ အသုံးပြုခြင်း](../../../02-prompt-engineering)
- [အပလီ케ရှင်း ဓာတ်ပုံများ](../../../02-prompt-engineering)
- [ပုံစံများ စူးစမ်းလေ့လာခြင်း](../../../02-prompt-engineering)
  - [အနည်းဆုံး vs အများဆုံး စိတ်အားထက်သန်မှု](../../../02-prompt-engineering)
  - [အလုပ်ဆောင်ရပ် ရေးဆွဲခြင်း (Tool Preambles)](../../../02-prompt-engineering)
  - [ကိုယ်တိုင်သုံးသပ်သော ကုဒ်](../../../02-prompt-engineering)
  - [ဖွဲ့စည်းတည်ဆောက်ထားသော ခြုံငုံသုံးသပ်ချက်](../../../02-prompt-engineering)
  - [တစ်ခါထပ်စလွှတ် စကားဖြောင့်ဖြားခြင်း](../../../02-prompt-engineering)
  - [ခန်းဆက်ခန်းဆကျသုံးသပ်ချက်](../../../02-prompt-engineering)
  - [ကန့်သတ်ထားသော output](../../../02-prompt-engineering)
- [သင်တကယ်သင်ယူနေသည့်အရာ](../../../02-prompt-engineering)
- [နောက်တစ်ဆင့်များ](../../../02-prompt-engineering)

## ဗီဒီယို လမ်းညွှန်

ဒီ module ကို စတင်အသုံးပြုနည်း ရှင်းပြတဲ့ Live session ကို ကြည့်ရှုပါ - [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## သင်ဘာတွေသင်မလဲ

<img src="../../../translated_images/my/what-youll-learn.c68269ac048503b2.webp" alt="သင်ဘာတွေသင်မလဲ" width="800"/>

ယခင် module တွင် စကားပြော AI အတွက် memory အရေးကြီးမှုကို ကြည့်ရှုပြီး GitHub Models ကို မူလ ဆက်သွယ်မှုများအတွက်အသုံးပြုသည်ကို တွေ့ခဲ့သည်။ ယခုတွင် သင်မေးခွန်း မေးနည်းများ - prompt များကို Azure OpenAI ၏ GPT-5.2 အသုံးပြုပြီး လေ့လာပါမည်။ ပုံစံသတ်မှတ်ပုံသည် အဖြေ အရည်အသွေးကို မျက်မှောက်လှိုက်လှဲစေသည်။ မူလ prompting နည်းလမ်းများကို ပြန်လည်သုံးသပ်ပြီး နောက်တစ်ဆင့်အဆင့်မြင့် ပုံစံ ၈ မျိုးကို GPT-5.2 ၏ အင်အားပြည့်မြောက်မှုတို့ဖြင့် လေ့လာမည်။

GPT-5.2 ကို Reasoning Control လုပ်ဆောင်နိုင်ခြင်းကြောင့် အသုံးပြုမည်ဖြစ်သည် - မိမိ မေးခွန်းကိုဖြေရာတွင် စဉ်းစားမှု ဘယ်လောက်လုပ်မလဲဆိုတာကို ရေးသားနိုင်သည်။ ထို့ကြောင့် prompting မျိုးစုံ၏ ကွာခြားမှုများ သိသာပြီး မည်သည့်နည်းလမ်းကို ဘယ်အချိန် အသုံးပြုရမလဲ ဆိုတာနားလည်နိုင်မှာ ဖြစ်သည်။ ထို့အပြင် Azure သည် GPT-5.2 အတွက် GitHub Models ထက် rate limit နည်းပါးသော အကျိုးခံစားခွင့် ထောက်ပံ့သည်။

## လိုအပ်ချက်များ

- Module 01 ပြီးဆုံးပြီးသား (Azure OpenAI resource များ တပ်ဆင်ပြီး)
- `.env` ဖိုင်ကို root directory တွင် Azure အသုံးပြုခွင့်စာရွက်နဲ့သိမ်းဆည်းထားပြီးဖြစ် (Module 01 မှ `azd up` ဖြင့်ဖန်တီး)

> **မှတ်ချက်။** Module 01 မပြီးရင် အရင်ကျရောက်ထားတဲ့ deployment လမ်းညွှန်ချက်တွေအတိုင်း လုပ်ဆောင်ပါ။

## Prompt Engineering ကိုနားလည်ခြင်း

<img src="../../../translated_images/my/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Prompt Engineering ဆိုတာဘာလဲ?" width="800"/>

Prompt engineering သည် မိမိလိုအပ်သော ရလဒ်များကို တိတိကျကျရရှိရန် input စာသားကို ဒီဇိုင်းဆွဲခြင်း ဖြစ်သည်။ ရှင်းရှင်းလင်းလင်း မေးခွန်းမေးတာသာမက မိမိလိုချင်သည့်အတိုင်း AI ကို နားလည်စေပြီး ပြန်လည်ပေးနိုင်ရန် ဖော်ပြပုံကို ဖန်တီးခြင်း ဖြစ်သည်။

အလုပ်ဖော်တစ်ဦးအားညွှန်ကြားချက်ပေးခြင်းကဲ့သို့ပါ။ "Bug ကိုပြင်ပါ" ဆိုတာ သိပ်ရှင်းလင်းမှုမရှိပါ။ "UserService.java ၏ line 45 တွင် null pointer exception အတွက် null check ထည့်ပါ" ဆိုတာ ပိုရှင်းလင်းသည်။ ဘာသာစကား မော်ဒယ်များမှ တစ်ပါတ် ကဲ့သို့ အတိအကျ ဖေါ်ပြချက်နှင့် ဖွဲ့စည်းပုံမှာ အရေးကြီးသည်။

<img src="../../../translated_images/my/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j ဘယ်လိုလိုက်ဖက်သလဲ" width="800"/>

LangChain4j သည် infrastructure ကို ပံ့ပိုးသည် — မော်ဒယ်ချိတ်ဆက်မှု၊ memory နှင့် message အမျိုးအစားများကို ထောက်ပံ့ပြီး prompt patterns များသည် infrastructure မှတဆင့် စနစ်တကျ သတ်မှတ်ထားသော စာသားများ ဖြစ်သည်။ အဓိက ကျတဲ့ အဆောက်အဦးပစ္စည်းများမှာ `SystemMessage` (AI ၏ အပြုအမူနှင့် အခန်းကဏ္ဍ ညှိနှိုင်း) နှင့် `UserMessage` (တောင်းဆိုချက်ကို သယ်ဆောင်) ဖြစ်သည်။

## Prompt Engineering မူလအခြေခံများ

<img src="../../../translated_images/my/five-patterns-overview.160f35045ffd2a94.webp" alt="Prompt Engineering ပုံစံ ၅ မျိုး အထွေထွေ" width="800"/>

ဒီ module တွင် တိုးတက်မြှင့်တင်ထားသော ပုံစံများသို့ ကျရောက်မည့်အချိန်အထိ နိုင်ငံတကာ prompt engineer တစ်ဦးအနေနှင့် သိထားသင့်သော အခြေခံ prompting နည်းလမ်း ၅ မျိုးကို ပြန်လည်သုံးသပ်ပါမည်။ သင်သည် [Quick Start module](../00-quick-start/README.md#2-prompt-patterns) တွင် လေ့လာပြီးသားဖြစ်ခဲ့လျှင် ဒီ ၅ မျိုးဟာ မူရင်း အကြံပြုအကြောင်းအရာ ဖြစ်ကြောင်း သိရှိသည်။

### Zero-Shot Prompting

အလွန်ရိုးရှင်းဆုံးနည်းလမ်းဖြစ်သည်။ မော်ဒယ်ထံ ပုံပြင် များမပေးဘဲ တိုက်ရိုက် ညွှန်ကြားချက်ပေးပါသည်။ မော်ဒယ်သည် တတ်မြောက်မှုအပေါ် မူတည်၍ တာဝန်ကို နားလည်ဆောင်ရွက်ပါသည်။ ရိုးရှင်းသော တောင်းဆိုမှုများအတွက် သင့်တော်သည်။

<img src="../../../translated_images/my/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*ဥပမာမပါဘဲ တိုက်ရိုက်ညွှန်ကြားချက်ပေးခြင်း - မော်ဒယ်သည် သာမန် instruction မှ တာဝန်ကို ကိုးကားသည်*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// တုံ့ပြန်ချက်။ "အရှိန်ပြင်း"
```

**အသုံးပြုသင့်သောအချိန်:** ရိုးရှင်းသော classification များ၊ တိုက်ရိုက်မေးခွန်းများ၊ ဘာသာပြန်၊ မော်ဒယ်ကို ထပ်ဆင့်ညွှန်ကြားချက် မလိုအပ်သော တာဝန်များ။

### Few-Shot Prompting

မော်ဒယ်ကို လိုက်နာရန် pattern ကို ထုတ်ဖော်ပြသထားသော ဥပမာများ ပေးပါသည်။ မော်ဒယ်သည် ဥပမာများမှ input-output ပုံစံကိုသင်ယူပြီး အသစ်များတွင် ကူးယူတယ်။ မူလတန်း ပုံစံသို့မဟုတ် အပြုအမူ မရှင်းလင်းသော တာဝန်များအတွက် တည်ငြိမ်မှု များအတွက် ထိရောက်သည်။

<img src="../../../translated_images/my/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*ဥပမာမှ သင်ယူခြင်း - မော်ဒယ်သည် ပုံစံကို သိပြီး အသစ်များတွင် သုံးစွဲသည်*

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

**အသုံးပြုသင့်သောအချိန်:** ပုံစံတိကျသော classification များ၊ ပုံစံတူညီမှုရှိသော ဖော်ပြချက်များ၊ domain-specific task များ၊ zero-shot ရလဒ် မတည်ငြိမ်သောအခါ။

### Chain of Thought

မော်ဒယ်ကို စဉ်ဆက်မပြတ် စဥ်းစား သုံးသပ်မှု ပြသရန် တောင်းဆိုပါ။ တိုက်ရိုက်ဖြေမပေးဘဲ ပြဿနာကို အစိတ်အပိုင်းစီ ဖော်ပြသည်။ သင်္ချာ၊ မှန်ကန်မှု၊ များစွာသော အဆင့် reasoning အတွက် တိုးတက်တိကျမှု တိုးပွားသည်။

<img src="../../../translated_images/my/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*အဆင့်လိုက် reasoning - ဒါရိုက်လော့ဖြင့် ပြဿနာရှင်းပုံ*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// မော်ဒယ်အနေဖြင့် ပြသထားသည်မှာ - ၁၅ - ၈ = ၇၊ ထို့နောက် ၇ + ၁၂ = ၁၉ စက်ဖူးများဖြစ်သည်။
```

**အသုံးပြုသင့်သောအချိန်:** သင်္ချာပြဿနာများ၊ မှန်ကန်မှု ပြဿနာများ၊ debugging၊ စဉ်းစားမှု ကိုတိုးတက်စေသော တာဝန်မျာ။

### Role-Based Prompting

မေးခွန်းမေးခါနီး AI ၏ persona သို့မဟုတ် အခန်းကဏ္ဍကို သတ်မှတ်ပါ။ ၎င်းသည် အကြောင်းအရာ၊ အနက်အသက်နှင့် အာရုံကို ဆယ်ခြားပေးသည်။ "software architect" သည် "junior developer" သို့မဟုတ် "security auditor" ထက် ကွဲပြားသော အကြံဉာဏ် ပေးသည်။

<img src="../../../translated_images/my/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*အခန်းကဏ္ဍနှင့် persona သတ်မှတ်ခြင်း - တူညီသော မေးခွန်းများကို လုပ်ဆောင်သူအရည်အချင်းအလိုက် ဟန်ချက်ပြောင်းသည်*

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

**အသုံးပြုသင့်သောအချိန်:** ကုဒ်စစ်ဆေးမှုများ၊ ကိုယ်ပိုင် အသုံးပြုသူ သင်ကြားမှုများ၊ domain-specific သုံးသပ်ချက်များ၊ အထူးပြုကျွမ်းကျင်မှု နှင့် အမြင်အာရုံ မူတည်၍ ဖြေကြားချက်လိုအပ်သောအချိန်။

### Prompt Templates

variable placeholders ဖြင့် ပြန်လည်အသုံးပြုနိုင်သော prompt များ ဖန်တီးပါ။ တစ်ခါရန် prompt ရေးသားဖို့ အစား template တစ်ခု သတ်မှတ်ပြီး တန်ဖိုးများပြောင်းတတ်သည်။ LangChain4j ၏ `PromptTemplate` က Klasse သည် `{{variable}}` syntax ဖြင့် လွယ်ကူစေရန်။

<img src="../../../translated_images/my/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*variable placeholder များပါရှိသော ပြန်လည်အသုံးပြုနိုင်သော prompt များ*

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

**အသုံးပြုသင့်သောအချိန်:** input မတူညီသော မေးခွန်းများ မကြာခဏ မေးတတ်သောအခါ၊ batch ပြုလုပ်မှုများ၊ ပြန်လည်အသုံးပြုနိုင်သော AI workflow များ တည်ဆောက်ချိန် သို့မဟုတ် prompt ၏ ဖွဲ့စည်းပုံသည် တူညီသော်လည်း ဒေတာကွဲပြားတဲ့ အခြေအနေများ။

---

ဒီ ငါး မျိုးအခြေခံနည်းလမ်းများက prompt များအတွက် သေချာသော ကိရိယာလက်နက်အဖြစ် သင့်အားပါဝင်စေမည်။ ဒီ module ရဲ့ ကျန်ရှိတာက GPT-5.2 ၏ reasoning control, self-evaluation, နှင့် ဖွဲ့စည်းတည်ဆောက်ထားသော output အင်အားများကိုအသုံးပြုသည့် **တိုးတက်မြှင့်တင်ထားသောပုံစံ ၈ မျိုး** ဖြစ်သည်။

## တိုးတက်မြှင့်တင်ထားသော ပုံစံများ

အခြေခံများကို ရှင်းပြီးနောက် ဒီ module ကို ထူးခြားအောင်လုပ်ထားတဲ့ တိုးတက်မြှင့်တင်ထားသော ပုံစံ ၈ မျိုး ဖြင့် ဆက်လက်လေ့လာမည်။ ပြဿနာအားလုံး အတူတူ နည်းလမ်းတူမဟုတ်ပါ။ မေးခွန်းတချို့သည် အမြန်ဖြေရှင်းလိုသည်၊ တချို့မှာ စိတ်ဝင်စားစရာ တုန့်ပြန်မှုရဖို့လိုသည်။ တချို့မှာ reasoning ထင်ရှားစေဖို့ လိုသည်၊ တချို့မှာ ရလဒ်သာလိုသည်။ အောက်ပါ ပုံစံ တစ်ခုချင်းစီသည် အခြေအနေကွဲပြားမှုအလားအလာအတွက် optimize လုပ်ထားပြီး GPT-5.2 ၏ reasoning control ကြောင့် ကွာခြားချက်များ ပိုသိသာစေသည်။

<img src="../../../translated_images/my/eight-patterns.fa1ebfdf16f71e9a.webp" alt="တိုက်ရိုက် prompting ပုံစံ ၈ မျိုး" width="800"/>

* prompting ပုံစံ ၈ မျိုး၏ အကြောင်းအရာ များနှင့် အသုံးပြုမှု များ*

<img src="../../../translated_images/my/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 ၏ Reasoning Control" width="800"/>

*GPT-5.2 ၏ reasoning control သည် မော်ဒယ်အတွက် စဉ်းစားမှု အမျိုးအစားကို ထောက်ပံ့နိုင်စေသည် - အမြန်ဖြေချက်တစ်ခုမှ အလွန်နက်ရှိုင်းစွာ စူးစမ်းလေ့လာခြင်းအထိ*

**နည်းသော စိတ်အားထက်သန်မှု (အမြန် နှင့် ဦးတည်ထားသော)** - နေရာတိုင်းတွင် မြန်ဆန်ပြီး တိုက်ရိုက်ဖြေရှင်းချက်လိုသော မေးခွန်းများအတွက်။ မော်ဒယ်သည် reasoning အနည်းငယ်သာလုပ်ဆောင်သည် - အများဆုံးအဆင့် ၂ ခု။ တွက်ချက်မှုများ၊ ရှာဖွေခြင်းများ သို့မဟုတ် ရိုးရှင်းသော မေးခွန်းများအတွက် အသုံးပြုပါ။

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

> 💡 **GitHub Copilot ဖြင့် စမ်းသပ်ခြင်း:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ဖွင့်ပြီး မေးပါ -
> - "နည်းသော စိတ်အားထက်သန်မှု နှင့် အများဆုံး စိတ်အားထက်သန်မှု prompting ပုံစံများ ကြားမှာ ဘာကွာခြားချက်ရှိသနည်း?"
> - "prompt များရှိ XML tag များက AI တုန့်ပြန်မှုကို ဘယ်လိုဖွဲ့စည်းပေးသလဲ?"
> - "ကိုယ်တိုင်လှည့်စားမှု ပုံစံများအစား တိုက်ရိုက်ညွှန်ကြားမှုနည်းလမ်း မည်သည့်အချိန်တွင် အသုံးပြုသင့်သနည်း?"

**အများဆုံး စိတ်အားထက်သန်မှု (နက်ရှိုင်း၍ သေချာ)** - နက်နက်ရှိုင်းရှိုင်း သုံးသပ်ချက်လိုသော ပြဿနာများအတွက်။ မော်ဒယ်သည် ကျယ်ပြန့်စွာ စူးစမ်းလေ့လာပြီး ကြေညာချက် အသေးစိတ် reasoning ပြသသည်။ စနစ်ဒီဇိုင်း၊ architecture ဆုံးဖြတ်ချက်များ သို့မဟုတ် ရှုပ်ထွေးသော သုတေသနများအတွက် အသုံးပြုပါ။

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**Task Execution (အဆင့်လိုက် တိုးတက်မှု)** - အဆင့်များစွာ ပါဝင်သည့် workflow များအတွက်။ မော်ဒယ်သည် အစီအစဉ် တစ်ခုကို အလွန်ရှင်းရှင်းလင်းလင်း တင်ပြပြီး လုပ်ဆောင်ရာတွင် တစ်ဆင့်ချင်းစီကို ပြောပြသည်၊ နောက်ပိုင်းတွင် သေချာသော အကျဉ်းသုံးစွဲမှု ပေးသည်။ ဥပမာ - migration များ၊ အကောင်အထည်ဖော်ခြင်း သို့မဟုတ် တစ်ဆင့်ဆင့်လုပ်ငန်းစဉ်များအတွက် အသုံးပြုပါ။

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

Chain-of-Thought prompting သည် မော်ဒယ်အား reasoning ဆက်လက်ပြသရန် တိုက်တွန်းကာ ရှုပ်ထွေးသော တာဝန်များအတွက် တိကျမှန်ကန်မှု တိုးပွားစေနိုင်သည့် အဆင့်လိုက် reasoning ဖော်ပြချက် ဖြစ်သည်။

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ပါ** - ဒီပုံစံအကြောင်း မေးပါ -
> - "ရွေ့လျားမှုရှည်ကြာသော လုပ်ငန်းများအတွက် task execution ပုံစံကို ဘယ်လိုသီးခြားပြင်ဆင်မလဲ?"
> - "ထုတ်ကုန် app အတွက် tool preambles ဖွဲ့စည်းရာတွင် နိုင်ငံအကောင်းဆုံးလုပ်နည်းများကဘာလဲ?"
> - "UI တွင် အလယ်အလတ်တိုးတက်မှုများကို အတင်ပြရန် နည်းလမ်းများ ဘယ်လို သိမ်းဆည်းနိုင်သလဲ?"

<img src="../../../translated_images/my/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*အစီအစဉ် → လုပ်ဆောင် → အကျဉ်းချုပ် workflow ကို အဆင့်လိုက် တာဝန်များအတွက်*

**ကိုယ်တိုင်သုံးသပ်သော ကုဒ်** - ထုတ်လုပ်မှုအဆင့်ကုဒ် ဖန်တီးရာတွင် အသုံးပြုသည်။ မော်ဒယ်သည် ဝယ်သားမှစပြီး error ကိုင်တွယ်မှု အပြည့်အဝပါဝင်သည့် ကုဒ်ထုတ်ပေးသည်။ function အသစ်များ၊ service အသစ်များ တည်ဆောက်ရာတွင် အသုံးပြုပါ။

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/my/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="ကိုယ်တိုင်သုံးသပ်မှု စက်ဝိုင်း" width="800"/>

*တပြိုင်နက်တည်း ထပ်တလဲလဲ ကောင်းမွန်မှု Loop - ဖန်တီးသည်၊ သုံးသပ်သည်၊ ပြဿနာ သတိပြုသည်၊ တိုးတက်အောင်ပြောင်းလဲသည်၊ ထပ်မံလုပ်ဆောင်သည်*

**ဖွဲ့စည်းတည်ဆောက်ထားသော ခြုံငုံသုံးသပ်ချက်** - တိကျညီညွတ်မှုရှိသော သုံးသပ်ချက်အတွက်။ မော်ဒယ်သည် ကုဒ်ကို တိကျ၍ စနစ်တကျ စစ်ဆေးသော framework (မှန်ကန်မှု၊ သုံးသပ်မှုကောင်းမှု၊ စွမ်းဆောင်ရည်၊ လုံခြုံမှု၊ ပြုပြင်ထိန်းသိမ်းရခက်ခဲမှု) အတိုင်း ကြည့်သုံးသည်။ ကုဒ်ပြန်လည်သုံးသပ်ခြင်း သို့မဟုတ် အရည်အသွေး သုံးသပ်ချက်များအတွက်အသုံးပြုသည်။

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

> **🤖 GitHub Copilot Chat ဖြင့် စမ်းသပ်ပါ:** ဖွဲ့စည်းတည်ဆောက်ထားသော ခြုံငုံသုံးသပ်ချက်အတွက် မေးပါ -
> - "အမျိုးအစားအလိုက် ကုဒ်ပြန်လည်သုံးသပ်မှု framework ကို မည်သို့ ဂရုစိုက်ပြုလုပ်မလဲ?"
> - "ပုံစံရှိသော output ကို programmatically မည်သို့ parse ပြီး လိုက်လျောညီထွေပြုမလဲ?"
> - "မျိုးစုံသော ပြန်လည်သုံးသပ်မှု အစည်းအဝေးများမှာ တွက်တိုက်မှု အဆင့်တစ်ခုစီကို မည်သို့ တည်ငြိမ်စေမလဲ?"

<img src="../../../translated_images/my/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

* အတိအကျ severity အဆင့်များ ဖြင့် တိတိကျကျကုဒ်ပြန်လည်သုံးသပ်ရေး framework *

**တစ်ခါထပ်စလွှတ် စကားဖြောင့်ဖြားခြင်း** - အကြောင်းအရာလိုအပ်သော စကားပြောဆိုမှုများအတွက်။ မော်ဒယ်သည် ယခင်စာသားများကို မှတ်မှတ်သားသား မှတ်ထားကာ ပိုမိုတိုးတက်စွာ တုံ့ပြန်သည်။ အကူအညီ စကားပြောဆိုခြင်း သို့မဟုတ် ရှုပ်ထွေးသော မေးခွန်းများ နှင့် ဖြေဆိုခြင်းအတွက် အသုံးပြုသည်။

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

*စကားပြောနောက်ခံအကြောင်းအရာသည် မျိုးစုံသော turn များတွင် စုစည်းလျက်ရှိပြီး token ကိုယ်စားလှယ် သတ်မှတ်ချက်အထိ ဆက်စပ် ထားသည်*

**ခန်းဆက်ခန်းဆ ငြင်းချက်ဖြေရှင်းခြင်း** - ထင်ရှားသော logic လိုသော ပြဿနာများအတွက်။ မော်ဒယ်သည် အဆင့်တိုင်းအတွက် reasoning ကို ဖော်ပြသည်။ သင်္ချာပြဿနာများ၊ မှန်ကန်သော အတိုင်းအတာ စိန်ခေါ်မှုများ သို့မဟုတ် စဉ်းစားပုံကိုနားလည်လိုသောအခါ အသုံးပြုသည်။

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

*ပြဿနာများကို အဆင့်လိုက် နည်းနည်းချက်များဖြင့် ခွဲခြမ်းဖော်ပြခြင်း*

**ကန့်သတ်ထားသော output** - အတိအကျ ဖော်ပြချက်ပုံစံလိုအပ်သော တုံ့ပြန်မှုများအတွက်။ မော်ဒယ်သည် ပုံစံနှင့် အရှည်ကို တင်းကြပ်စွာလိုက်နာသည်။ အကျဉ်းချုပ်များ သို့မဟုတ် သတ်မှတ်ဖော်ပြနည်း သေသပ်မှုလိုအပ်သောအခါ အသုံးပြုသည်။

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

*ဖော်ပြချက်ပုံစံ၊ အရှည်နှင့် ဖွဲ့စည်းပုံ တင်းကြပ်စွာ လိုက်နာမှု*

## ရှိပြီးသား Azure အရင်းအမြစ်များ အသုံးပြုခြင်း

**Deployment ကို တိကျစွာအတည်ပြုပါ:**

Module 01 အတွင်း ဖန်တီးထားသော Azure အသုံးပြုခွင့် လက်မှတ်ပါ `.env` ဖိုင်အား root directory တွင် ရှိကြောင်း အတည်ပြုပါ -
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကိုပြသသင့်သည်။
```

**အပလီကေးရှင်းကို စတင်ပါ:**

> **မှတ်ချက်:** Module 01 မှ `./start-all.sh` ဖြင့် အားလုံးအလုပ်ဆောင်နေပြီးဖြစ်ပါက ဒီ module သည် port 8083 တွင် ပြီးသားလည်ပတ်နေပါသည်။ အောက်ပါ စတင်လုပ်ဆောင်မှု ကုဒ်များကို ကျော်လွှား၍ http://localhost:8083 သို့တန်းရောက်ပါ။

**နည်းလမ်း ၁: Spring Boot Dashboard အသုံးပြုခြင်း (VS Code အသုံးပြုသူများအတွက် အကြံပြု)**
ဒီ dev container ဟာ Spring Boot Dashboard extension ကို ပါဝင်ပြီး Spring Boot applications အားလုံးကို ပြဿနာကင်းစွာ စီမံခန့်ခွဲဖို့ visual interface ကို ပေးပါတယ်။ VS Code ရဲ့ ဘယ်ဖက် Activity Bar မှာ (Spring Boot icon ရှာဖွေကြည့်ပါ) တွေ့နိုင်ပါတယ်။

Spring Boot Dashboard ကနေ၊ သင်မှာ:
- အလုပ်မလုပ်သေးတဲ့ Spring Boot applications အားလုံးကို ကြည့်ရှုနိုင်သည်
- တစ်ချက်နှိပ်ပြီး applications များကို စတင်/ရပ်နားနိုင်သည်
- application log များကို တိုက်ရိုက် ကြည့်ရှုနိုင်သည်
- application အခြေအနေကို စောင့်ကြည့်နိုင်သည်

"prompt-engineering" အနားမှာ play ခလုတ်ကို နှိပ်ပြီး ဒီ module ကို စတင်လိုက်ပါ၊ ဒါမှမဟုတ် module အားလုံးကို တစ်ပြိုင်နက် စတင်နိုင်ပါတယ်။

<img src="../../../translated_images/my/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**နည်းဗျူဟာ ၂: shell scripts အသုံးပြုခြင်း**

Web applications (modules 01-04) များအားလုံးကို စတင်ခြင်း:

**Bash:**
```bash
cd ..  # ရုပ်မြစ်ဖိုင်လ်တွဲမှ
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # မူလဖိုင်လမ်းကြောင်းမှ
.\start-all.ps1
```

ဒါမှမဟုတ် ဒီ module ကိုတစ်ခုတည်းစတင်ရန်:

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

ဒီ scripts နှစ်ခုစလုံး root `.env` ဖိုင်မှ ဂရုစိုက်မှုမရှိဘဲ environment variables ကို အလိုအလျောက် load လုပ်ပြီး JAR ဖိုင်များ မရှိသေးလျှင် compile လုပ်ပေးပါသည်။

> **မှတ်ချက်**: module အားလုံးကို စတင်မပြုလုပ်ခင် manually build လုပ်ချင်ပါက:
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

သင့် browser မှာ http://localhost:8083 ကို ဖွင့်ပါ။

**ရပ်နားရန်:**

**Bash:**
```bash
./stop.sh  # ဤမော်ဂျူးလ်သာ
# သို့မဟုတ်
cd .. && ./stop-all.sh  # မော်ဂျူးလ်အားလုံး
```

**PowerShell:**
```powershell
.\stop.ps1  # ဒီမော်ဂျူးလ်မှာသာ
# သို့မဟုတ်
cd ..; .\stop-all.ps1  # မော်ဂျူးလ်အားလုံး
```

## Application Screenshots

<img src="../../../translated_images/my/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*main dashboard က prompt engineering အမျိုးအစား ၈ မျိုးစလုံးကို သူတို့ရဲ့ လက္ခဏာတွေ နဲ့ အသုံးပြုမှုများနှင့်အတူ ပြထားသည်*

## ပုံစံများ မျက်နှာဖွင့်မှု

Web interface က သင့်ကို prompting strategies မျိုးစုံကို လက်တွေ့ကျကျ စမ်းသပ်စစ်ဆေးဖို့ အခွင့်အလမ်းပေးပါတယ်။ pattern တစ်ခုချင်းစီက မတူညီသဖြင့် ပြဿနာဖြေရှင်းချက် အမျိုးမျိုးရှိသည် - သင်စမ်းကြည့်ဖို့နှင့် ဘယ်အချိန်မှာ အသုံးဝင်မလဲလေ့လာပါ။

> **မှတ်ချက်: Streaming နှင့် Non-Streaming** — pattern များ စာမျက်နှာတိုင်းမှာ ခလုတ် ၂ ခကျ်: **🔴 Stream Response (Live)** နဲ့ **Non-streaming** ရွေးချယ်စရာရှိပါတယ်။ Streaming သည် Server-Sent Events (SSE) ကို အသုံးပြုပြီး model ထုတ်လုပ်တဲ့ token များကို တိုက်ရိုက် ပြသပေးသဖြင့် ချက်ချင်း ကြည့်ရှုနိုင်သည်။ Non-streaming သည် တုန့်ပြန်ချက် အပြည့်အစုံ ရလာမှ ပြသပေးသည်။ အလွန်ရှုပ်ထွေးသော prompting များ (ဥပမာ - High Eagerness, Self-Reflecting Code) တွင် non-streaming call သည် အချိန်ကြာနိုင်ပြီး မျက်စိမြင် feedback မပါဘဲ နာရီပေါင်းများစွာ ကြာနိုင်သည်။ **ရှုပ်ထွေးသော prompting များကို စမ်းသပ်စဉ် streaming ကို အသုံးပြုပါ** ထို့ကြောင့် model ပုံစံသေချာ ဖော်ပြနေသည့်အတိုင်း ကြည့်ရှုနိုင်ပြီး ရသက်တမ်းကုန်သွားသည်ဟု ခံစားနေရမှုရှောင်လျားနိုင်သည်။
>
> **မှတ်ချက်: Browser လိုအပ်ချက်** — streaming 기능은 Fetch Streams API(`response.body.getReader()`)를 사용하므로 완전한 브라우저 (Chrome, Edge, Firefox, Safari)가 필요합니다. VS Code 내장 Simple Browser에서는 작동하지 않으며, 이 웹뷰는 ReadableStream API를 지원하지 않습니다. Simple Browser를 사용하는 경우 non-streaming 옵션은 정상 작동하지만 streaming 버튼만 영향을 받습니다. 전체 기능을 이용하려면 외부 브라우저에서 `http://localhost:8083` 을 열어주세요.

### Low Eagerness နဲ့ High Eagerness

"200 ရဲ့ 15% ဘာလဲ?" ဆိုတဲ့ လွယ်ကူတဲ့မေးခွန်းကို Low Eagerness နဲ့မေးပါ။ ချက်ချင်း တုံ့ပြန်ချက်ကို ရရှိပါမည်။ ယခုတော့ "traffic များများသော API အတွက် caching strategy တစ်ခု ဒီဇိုင်းဆွဲပါ" ဆိုသောရှုပ်ထွေးသောမေးခွန်းကို High Eagerness နဲ့မေးပါ။ **🔴 Stream Response (Live)** ကို နှိပ်ပြီး model အကြောင်းအရာအသေးစိတ် reasoning ကို token တစ်ခုချင်းစီဖြင့် ကြည့်ရှုပါ။ ပုံစံတူပြီး မေးခွန်းတူပါပေမယ့် prompt က model ကို ဘယ်လောက်စဉ်းစားရမလဲ ဆိုတာ ပြောပြတာပါ။

### Task Execution (Tool Preambles)

စီမံကိန်းများတွင် planning နဲ့ အဆင့်အဆင့် narration များ လိုအပ်သည်။ model က ပြုလုပ်မည့် အတိုင်ပင်ခံချက်၊ အဆင့်ဆင့် အမူအရာရေးပြောဆိုမှုနှင့် ရလဒ်များကို တင်ပြသည်။

### Self-Reflecting Code

"အီမေးလ် လက်မှတ်စစ်ဆေးရေး ဝန်ဆောင်မှု တည်ဆောက်ပါ" ဆိုပြီး စမ်းကြည့်ပါ။ code ကို ဖန်တီးသည့်အစား၊ model က ရလဒ်ကို ထုတ်ပြီး၊ အရည်အသွေးမီတာများနှင့် နှိုင်းယှဉ်၊ အားနည်းချက်များရှာထုတ်ကာ မြှင့်တင်ပေးသည်။ သင်သည် code သည် ထုတ်လုပ်မှု စံနှုန်းများနှင့် ကိုက်ညီသောအထိ iteration ဆောင်ရွက်ဖွယ် ရှု့နိုင်ပါသည်။

### Structured Analysis

Code review များတွင် စံချိန်စနစ်တကျ တုံ့ပြန်မှု လိုအပ်သည်။ model က ကိုက်ညီမှုပမာဏ၊ လုပ်ထုံးလုပ်နည်းများ၊ ဆောင်ရွက်မှုနှင့် လုံခြုံမှု စတာအားဖြင့် code ကို သတ်မှတ်ချက်အရ လေ့လာသည်။

### Multi-Turn Chat

"Spring Boot ဆိုတာဘာလဲ?" မေးပြီး ချက်ချင်း "ဥပမာကို ပြပါ" ဟုပြန်မေးပါ။ model က သင့် မေးခွန်းများကို မှတ်မိပြီး သီးသန့် Spring Boot example တစ်ခုပေးသည်။ မှတ်ဉာဏ် မရှိသည့် နေရာတွင် ဒုတိယမေးခွန်းသည် သတ်မှတ်ချက်မပြည့်စုံနိုင်ပါ။

### Step-by-Step Reasoning

သင် သင်သားရေတွက်ပြဿနာတစ်ခုကို ရွေးပြီး Step-by-Step Reasoning နဲ့ Low Eagerness လိုမျိုး စမ်းကြည့်ပါ။ Low eagerness က မြန်ဆန် သို့သော် စိမ့်ကြဲသော တုံ့ပြန်ချက်ပေးသည်။ Step-by-step သည် မည်သည့်တွက်ချက်မှုနဲ့ ဆုံးဖြတ်ချက်ကို ဖော်ပြသည်။

### Constrained Output

သတ်မှတ်ထားသော ပုံစံများ သို့မဟုတ် စကားလုံးရေလိုအပ်သောအခါ ဤ pattern သည် တိကျစွာလိုက်နာရန် တင်းကျပ်စွာ သတ်မှတ်သည်။ ၁၀၀ စကားလုံး ရဲ့ bullet point စာရိုက် ထုတ်ပေးရန် စမ်းကြည့်ပါ။

## သင်လေ့လာနေသည့် အချက်များ

**Reasoning Effort သည် အားလုံးကို အပြောင်းအလဲ ပြုလုပ်သည်**

GPT-5.2 က သင့် prompt များမှတဆင့် သင့်လျော်သော စဉ်းစားမှု အဆင့်ကို ထိန်းချုပ်နိုင်သည်။ အနိမ့်ဆုံး စွမ်းဆောင်မှုဆိုသည်မှာ မြန်ဆန်ပြီး စစ်းမြန်မှု နည်းပါးခြင်းကိုဆိုလိုသည်။ အမြင့်ဆုံး စွမ်းဆောင်မှုက model ကို နက်နက်ရှိုင်းရှိုင်း စဉ်းစားရန် အချိန်ယူစေသည်။ သင်သည် အလုပ်စဉ်အရ ပြဿနာရှုပ်ထွေးမှုအလိုက် စွမ်းဆောင်ရည်ကို သက်ဆိုင်ဖို့ လေ့လာနေပါသည် - လွယ်ကူသော မေးခွန်းများ၌ အချိန်အလောင်းမဖြုန်းလိုက်နဲ့၊ ရှုပ်ထွေးသော ဆုံးဖြတ်ချက်များ၌လည်း အလျင်မမြန်မိပါနှင့်။

**ဖွဲ့စည်းမှုသည် အပြုအမူကို ဦးဆောင်သည်**

prompt များရှိ XML tag တွေ သတိပြုပါ။ ၎င်းတို့သည် အလှဆင် မဟုတ်ပါ။ အခွင့်အရေးလွယ်ကူသည့် စကားလုံးထက် ဖွဲ့စည်းစနစ်တကျ ပြုလုပ်ထားသော ရွေးချယ်ချက်များကို ပိုမိုယုံကြည်စိတ်ချစေသည်။ multi-step နှင့် ရှုပ်ထွေးသော နှုတ်ခွန်းဆက်လုပ်ငန်းများ လိုအပ်သောအခါ စနစ်တကျ ဖွဲ့စည်းမှုက model ကို ၎င်း၏ နေရာ ကိုယ်စား လုပ်ဆောင်ချက်များကိုလိုက်နာစေနိုင်သည်။

<img src="../../../translated_images/my/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*XML style ဖွဲ့စည်းတည့်တဲ့ prompt တစ်ခု၏ အစိတ်အပိုင်းများနှင့် ဖွဲ့စည်းမှု*

**အရည်အသွေးကို ကိုယ်တိုင် သုံးသပ်ခြင်းဖြင့် ရရှိသည်**

self-reflecting pattern များသည် အရည်အသွေး စံချိန်များကို ထုတ်ဖော်ပြသသည်။ model ကို “မှန်ကန်” ဟု မမျှော်လင့်ဘဲ၊  မိမိက “မှန်ကန်မှု” သတ်မှတ်ချက်များကို တိတိကျကျ ပြောဆိုပေးသည်။ မှန်ကန်သော logic၊ error handling၊ ဆောင်ရွက်မှု၊ လုံခြုံမှု အပိုင်းများမှတဆင့် model သည် ၎င်း၏ ရလဒ်ကို ကိုယ်တိုင် သုံးသပ်ပြီး တိုးတက်စေနိုင်သည်။ ၎င်းက code ဖန်တီးခြင်းကို ကံစမ်းမှုပုံစံမှ ပြောင်းလဲ၍ စနစ်တကျ လုပ်ငန်းစဉ်ဖြစ်စေသည်။

**ပတ်ဝန်းကျင်က အကန့်အသတ်ရှိသည်**

multi-turn စကားပြောဆိုမှုများသည် မက်ဆေ့ချ်မှတ်တမ်းများအပြည့်အစုံနှင့် ဖြေကြားမှုတစ်ခုစီတွင် ပါဝင်သည်။ သို့သော် အကန့်အသတ်တစ်ခု ရှိသည် - model များသည် token အများဆုံး အရေအတွက်ရှိသည်။ စကားပြောဆိုမှုများ တိုးလာသည်နှင့်အမျှ သက်ဆိုင်မှုရှိသော ပတ်ဝန်းကျင်ကို ထိန်းသိမ်းရန် နည်းဗျူဟာများ လိုအပ်သည်။ ဒီ module က သင်ကို မက်မုရီလည်ပတ်ပုံ၊ ဘယ်အချိန်မှာ စုစည်းဖို့၊ မေ့ပစ်ဖို့၊ ပြန်ရွာဖို့သင်ပေးပါမည်။

## နောက်တစ်ဆင့်များ

**နောက်ထပ် module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Previous: Module 01 - Introduction](../01-introduction/README.md) | [Back to Main](../README.md) | [Next: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ကျူးလွန်ချက်**  
ဤစာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ဖြင့် ဘာသာပြန်ထားပါသည်။ တိကျမှုအပေါ် အာရုံစိုက်ကြသော်လည်း အလိုအလျောက် ဘာသာပြန်မှုများတွင် အမှားသို့မဟုတ် တိတိကျကျမဟုတ်မှုများ ပါဝင်နိုင်ကြောင်း သတိပြုပါရန် လိုအပ်ပါသည်။ မူလစာတမ်းကို ထုံးစံဘာသာစကားဖြင့်သာ စာတမ်း၏ မှန်ကန်သောအရင်းအမြစ်အနေဖြင့် ယူဆသင့်ပါသည်။ အရေးကြီးသော သတင်းအချက်အလက်များအတွက် လူမှုအလုပ်အကိုင် ကျွမ်းကျင်သူ အသုံးပြုဘားသာပြန်မှုကို အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုမှုမှ ဖြစ်ပေါ်လာနိုင်သည့် နားလည်မမှန်မှုများ သို့မဟုတ် ဘာသာရပ်ဖွဲ့ချက် မှားယွင်းမှုများအတွက် ကျွန်ုပ်တို့၏ တာဝန်မရှိပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->