# Module 02: GPT-5.2 နှင့် Prompt Engineering

## အကြောင်းအရာစာရင်း

- [ဗွီဒီယိုလမ်းညွှန်](../../../02-prompt-engineering)
- [သင်ဘာတွေကိုလေ့လာမှာလဲ](../../../02-prompt-engineering)
- [လိုအပ်ချက်များ](../../../02-prompt-engineering)
- [Prompt Engineering ကိုနားလည်ခြင်း](../../../02-prompt-engineering)
- [Prompt Engineering အခြေခံများ](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [အဆင့်မြင့် အညွန်းများ](../../../02-prompt-engineering)
- [လက်ရှိ Azure အရင်းအမြစ်များကို အသုံးပြုခြင်း](../../../02-prompt-engineering)
- [အက်ပ်ပလီကေးရှင်း စကရင်ရှော့များ](../../../02-prompt-engineering)
- [အညွန်းများကို စူးစမ်းလေ့လာခြင်း](../../../02-prompt-engineering)
  - [နိမ့်ဆန့်နှင့် မြင့်ဆန့် Eagerness](../../../02-prompt-engineering)
  - [လုပ်ငန်းဆောင်တာ လုပ်ငန်းစဉ် (Tool Preambles)](../../../02-prompt-engineering)
  - [ကိုယ်တွေ့ ပြန်လည်စဉ်းစားသုံးသပ်ခြင်းကုဒ်](../../../02-prompt-engineering)
  - [ဖွဲ့စည်းတည်ဆောက်ထားသော စိစစ်ခြင်း](../../../02-prompt-engineering)
  - [အကြိမ်ရေ များသော စကားပြောဆွေးနွေးမှုပုံစံ](../../../02-prompt-engineering)
  - [ဆင်ခြင်လေ့လာမှု အဆင့်ဆင့်](../../../02-prompt-engineering)
  - [ကန့်သတ်ထားသော ထုတ်ရန်](../../../02-prompt-engineering)
- [မင်းတကယ်ဘာတွေကိုလေ့လာနေသလဲ](../../../02-prompt-engineering)
- [နောက်တစ်ဆင့်များ](../../../02-prompt-engineering)

## ဗွီဒီယိုလမ်းညွှန်

ဒီ module ကိုဘယ်လိုဆောင်ရွက်ရမယ်ဆိုတာရှင်းပြသည့် live session ကို ကြည့်ပါ။

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## သင်ဘာတွေကိုလေ့လာမှာလဲ

<img src="../../../translated_images/my/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

Module မတိုင်မီမှာ၊ conversational AI လုပ်နိုင်စေတဲ့ memory ကိုတွေ့မြင်ခဲ့ပြီး GitHub Models ကို အသုံးပြုကာ အခြေခံအပြန်အလှန်ပြောဆိုမှုများကို အသုံးပြုခဲ့ပါသည်။ ယခုမှာတော့ မေးခွန်းတွေကိုမေးနည်းများ - prompt များကို Azure OpenAI ရဲ့ GPT-5.2 ကို အသုံးပြု၍ လေ့လာမှာ ဖြစ်ပါတယ်။ မင်း prompt ကို ဖွဲ့စည်းပုံက မင်းရတဲ့ဖြေကြားချက်များ အရည်အသွေးကို အလွန်အမင်း ထိခိုက်စေပါတယ်။ ကျွန်တော်တို့သည် အခြေခံ prompt နည်းပညာများကို ပြန်လည်သုံးသပ်ပြီးနောက် GPT-5.2 ၏ စွမ်းဆောင်ရည်များကို အပြည့်အဝ အသုံးချနိုင်သည့် အဆင့်မြင့် နည်းစနစ် ၈ မျိုးသို့ ရောက်ရှိပါမည်။

GPT-5.2 ကို အသုံးပြုသည့်အတွက် သင်္ချာရာဇဝင်ကို ထိန်းချုပ်နိုင်သည့် အင်္ဂါရပ်ကိုပါတင်ပြပေးသည်။ မော်ဒယ်ကို ပြန်ဖြေမည်မဟုတ်မီ စဉ်းစားပုံကို ပြောပြနိုင်ပြီး၊ ထိုကဲ့သို့သော prompt စနစ်များက ပိုမိုရှင်းလင်းကောင်းမွန်ခြင်းကို ဖော်ပြထားသည်။ ထို့အပြင် GitHub Models နှင့်နှိုင်းယှဉ်ပါက Azure ၏ GPT-5.2 အတွက် နည်းပတ်ခွင့်များ လျော့နည်းခြင်းကိုလည်း ရရှိမှာဖြစ်သည်။

## လိုအပ်ချက်များ

- Module 01 ပြီးစီးခြင်း (Azure OpenAI အရင်းအမြစ်များ တပ်ဆင်ပြီး)
- Root directory တွင် `.env` ဖိုင် (Module 01 တွင် `azd up` ဖြင့် ဖန်တီးပြီး)

> **မှတ်ချက်:** Module 01 မပြီးဖြစ်ရင် ထိုနည်းပြသည့် အဆင့်များကို အရင်ဆုံးလုပ်ဆောင်ပါ။

## Prompt Engineering ကိုနားလည်ခြင်း

<img src="../../../translated_images/my/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering ဆိုတာ မင်းလိုချင်သည့် ရလဒ်ကို မပြောင်းလဲပဲ ရှေ့ဆက်တဲ့ input စာသားကို ဒီဇိုင်းဆွဲခြင်းဖြစ်ပါတယ်။ မေးခွန်းမေးခြင်းပဲမဟုတ်ပဲ မော်ဒယ်က မင်းလိုချင်သမျှကိုဘယ်လိုနားလည် နောက်ဆုံး ဘယ်လိုပေးမလဲဆိုတာ ဖော်ပြပေးတာပါ။

အလုပ်အဖော်တစ်ယောက်ကို ညွှန်ကြားချက်ပေးတာလို ထင်ပါစေ။ "Bug ကို ပြင်ပါ" ဆိုတာက အကြောင်းအရာမရှင်းလင်းပါဘူး။ "UserService.java ဖိုင်၊ အကြောင်းကြောင်း ၄၅ လိုင်းမှာ Null Pointer Exception ကို Null Check ထည့်ပြီး ပြင်ပါ" ဆိုတာ သေချာရှင်းလင်းပါတယ်။ ဘာသာစကားမော်ဒယ်တွေကလည်း အဲဒီလိုပဲ - ရှင်းလင်းမှုနဲ့ ဖွဲ့စည်းမှု အရေးကြီးပါတယ်။

<img src="../../../translated_images/my/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j က မော်ဒယ်ချိတ်ဆက်မှု၊ မှတ်ဉာဏ်၊ စာတန်းထုတ်ပေးမှုအမျိုးအစားတွေကို ပံ့ပိုးပေးတဲ့ အခြေခံအဆောက်အအုံဖြစ်ပြီး၊ prompt pattern တွေကတော့ ထိုအခြေခံပေါ်မှာ မသက်ဆိုင်ဘဲသေချာစွာ ဖွဲ့စည်းထားတဲ့ စာသားမျိုးဖြစ်ပါတယ်။ အရေးကြီးသော အပိုင်းကတော့ `SystemMessage` (AI ၏ အပြုအမူနှင့် အခန်းကဏ္ဍကို သတ်မှတ်ပေး) နှင့် `UserMessage` (မင်းရဲ့ တကယ်လာသော အမိန့်များကို သယ်ဆောင်) ဖြစ်ပါသည်။

## Prompt Engineering အခြေခံများ

<img src="../../../translated_images/my/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

ဒီ module တွင် အဆင့်မြင့် pattern များကို မဝင်ခင် အခြေခံတဲ့ prompting နည်းလမ်း ၅ မျိုးကို ပြန်လည် သုံးသပ်ကြမယ်။ ဤနည်းလမ်းများမှာ prompt engineer တစ်ယောက်တိုင်း သိထားသင့်သော အခြေခံပစ္စည်းဖြစ်ပါသည်။ သင်သည် [Quick Start module](../00-quick-start/README.md#2-prompt-patterns) ကို အရင်ကြည့်ပြီးသားဖြစ်ပါက ဤနည်းလမ်းများ၏ အကြံဉာဏ် ဖွဲ့စည်းပုံကို တတ်မြောက်ပြီးဖြစ်သည်။

### Zero-Shot Prompting

အလွယ်တကူနည်းလမ်း - မော်ဒယ်ကို ဥပမာမပါဘဲ တိုက်ရိုက်အမိန့် ပေးခြင်း။ မော်ဒယ်သည် မိမိ သင်ကြားမှုမှသာ အလုပ် လုပ်ဆောင်သည်။ ရိုးရှင်းပြီး တိကျတဲ့ တောင်းဆိုချက်များအတွက် သင့်တော်သည်။

<img src="../../../translated_images/my/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*ဥပမာမပါဘဲ တိုက်ရိုက်အညွှန်း - အမိန့်မှသာ မောင်းနှင်သည်*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// တုံ့ပြန်ချက်: "အကောင်းပြု"
```

**အသုံးပြုရမည့် အချိန်:** ရိုးရှင်းသော ခွဲခြားမှုများ၊ တိုက်ရိုက်မေးခွန်းများ၊ ဘာသာပြန်ဆိုခြင်းများ ဒါမှမဟုတ် မော်ဒယ်အကူအညီမလိုသော အလုပ်များ။

### Few-Shot Prompting

မော်ဒယ်ကို လမ်းညွှန်သော pattern ကို ပြသသည့် ဥပမာများ ပေးပါ။ မော်ဒယ်သည် မင်းပေးထားသော input-output ပုံစံကို သင်ယူပြီး အသစ်များတွင် အသုံးပြုနိုင်ပါသည်။ မျှော်မှန်းထားသည့် ပုံစံ သို့မဟုတ် အပြုအမူ ရှင်းလင်းမဟုတ်သော အလုပ်များမှာ အထူးအသုံးဝင်သည်။

<img src="../../../translated_images/my/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*ဥပမာများမှ သင်ယူ - မော်ဒယ်သည် pattern ကိုခြေရာခံပြီး အသစ်ထည့်သွင်းချက်များတွင် အသုံးပြုသည်*

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

**အသုံးပြုရမည့် အချိန်:** စိတ်တိုင်းကျ ခွဲခြားမှုများ၊ ပုံစံတကျ အစီအစဉ်များ၊ ကွဲပြားသော နယ်ပယ်အလုပ်များ သို့မဟုတ် zero-shot ရလဒ် မတည်ငြိမ်သောအခါ။

### Chain of Thought

မော်ဒယ်အား ဆင်ခြင်မှုကို အဆင့်ဆင့် ပြရန် တောင်းဆိုပါ။ အဖြေသို့ တိုက်ရိုက်သွားမဟုတ်ဘဲ ပြဿနာကို ကွဲနက်စွာ ကျောက်တုံးတုံး ထင်ရှားစွာ စဉ်ဆက်မပြတ် ဖြေရှင်းသည်။ သင်္ချာ၊ ရင်ဆိုင်မှုပညာနှင့် အဆင့်များစွာ လိုအပ်သည့် အလုပ်များတွင် တိကျမှု ကောင်းမွန်စေသည်။

<img src="../../../translated_images/my/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*အဆင့်ဆင့် ဆင်ခြင်၍ - ပြဿနာရှုပ်ထွေးများကို သုံးသပ်သိမြင်ချက်ဖြင့် ဖြေရှင်း*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// မော်ဒယ်ကပြနေသည်မှာ- 15 - 8 = 7, ထို့နောက် 7 + 12 = 19 မက်ပျဉ်းများဖြစ်သည်။
```

**အသုံးပြုရမည့် အချိန်:** သင်္ချာပြဿနာများ၊ မွေ့လျော်မှုဉာဏ်ကစား၊ အမှားရှာခြင်းများ ဒါမှမဟုတ် ဆင်ခြင်သုံးသပ်မှု ကြေညာမှုကောင်းစေသော အလုပ်များ။

### Role-Based Prompting

မေးခွန်းမေးစမည့်မတိုင်မီ AI အတွက် persona သို့မဟုတ် အခန်းကဏ္ဍ ညွှန်ပြပါ။ ၎င်းသည် ဖြေကြားချက်၏ အသံလွှင့်ခြင်း၊ နက်ရှိုင်းမှုနှင့် အာရုံစူးစိုက်မှုကို ဖွဲ့စည်းပေးသည်။ "software architect" နှင့် "junior developer" သို့မဟုတ် "security auditor" က ပြောကြားပုံက ဟိုဟာက မတူကွဲပြားသည်။

<img src="../../../translated_images/my/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*အခန်းကဏ္ဍနှင့် persona သတ်မှတ်ခြင်း - တူညီသောမေးခွန်းတွင် မတူညီသော ဖြေကြားချက်များရရှိ*

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

**အသုံးပြုရမည့် အချိန်:** ကုဒ်ပြန်လည်သုံးသပ်ခြင်း၊ တိုင်းတာသင်ကြားခြင်း၊ နယ်ပယ်အလိုက် စိစစ်သုံးသပ်ခြင်း သို့မဟုတ် ထူးခြားသော ကျွမ်းကျင်မှုအဆင့်သို့ ရည်ရွယ်၍ ဖြေကြားချက်လိုအပ်သောအခါ။

### Prompt Templates

variable placeholders ပါဝင်သည့် ပြန်လည်အသုံးပြုနိုင်သော prompts တွေဖန်တီးပါ။ တစ်ခါတည်း template တစ်ခု ဖန်တီးပြီး ကွဲပြားသော တန်ဖိုးများဖြည့်ရေးပါ။ LangChain4j ၏ `PromptTemplate` class က `{{variable}}` syntax ဖြင့် လွယ်ကူစေသည်။

<img src="../../../translated_images/my/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*variable placeholders ပါသော ပြန်လည်အသုံးပြုနိုင်သော prompts - template တစ်ခု၊ အသုံးပြုမှုများစွာ*

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

**အသုံးပြုရမည့် အချိန်:** မတူညီသော အထည့်အချက်များဖြင့် ပြန်လည်မေးခြင်းများ၊ စုစုပေါင်းလုပ်ငန်းများ၊ ပြန်လည်အသုံးပြုနိုင်သော AI လုပ်ငန်းစဉ်များ တည်ဆောက်ခြင်း သို့မဟုတ် prompt ဖွဲ့စည်းပုံတူတူပဲ ဒေတာသာ ပြောင်းလဲမှုရှိသော အခြေအနေများ။

---

ဤ ၅ မျိုးအခြေခံ နည်းလမ်းများသည် မက်ဆေ့ခ်ျများ၏ နိုင်ငံတော်သတင်းအချက်အလက်များအတွက် အခြေခံ toolkit တစ်ခု ဖြစ်သည်။ တစ်သက်တာဒီ module မှာ GPT-5.2 ၏ reasoning control, self-evaluation, နှင့် စနစ်တကျထုတ်လုပ်သော စွမ်းရည်များကို အသုံးပြုသည့် **အဆင့်မြင့် pattern ၈ မျိုး** ဖြင့် ဆက်လက်တည်ဆောက်ပါမည်။

## အဆင့်မြင့် အညွန်းများ

အခြေခံများ ကျေရာ ဆက်သွားပြီးနောက် ဒီ module ကို ထူးခြားစေသော အဆင့်မြင့် အညွန်း ၈ မျိုးသို့ ရောက်ရှိကြပါစို့။ ပြဿနာတိုင်းတွင် တူညီသောနည်းလမ်းမလိုအပ်ပါ။ အချို့ မေးခွန်းများသည် ရိုးရှင်းလွယ်ကူသော ဖြေကြားချက်များလိုအပ်ပြီး၊ အချို့သည် နက်ရှိုင်းသေချာသောဆင်ခြင်မှုလိုသည်။ အချို့တွင် မြင်သာသော reasoning လိုအပ်ပြီး အချို့တွင်ရလဒ်သာလိုအပ်သည်။ အောက်ပါ pattern တစ်ခုချင်းစီသည် ကွဲပြားသော အခြေအနေများအတွက် အဆင်သင့်ဖြစ်ပြီး GPT-5.2 ၏ reasoning control က ကွဲပြားမှုများကို အထူးပြောပြသည်။

<img src="../../../translated_images/my/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*အဆင့်မြင့် prompt engineering အညွန်း ၈ မျိုး၏ အနှစ်ချုပ်နှင့် အသုံးပြုပုံများ*

<img src="../../../translated_images/my/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 ၏ reasoning control ဖြင့် မော်ဒယ်သဘောတရား ပိုမိုစူးစမ်းစမ်းရောက်မှုများ သတ်မှတ်နိုင်သည်။*

**နိမ့်ဆန့် Eagerness (လျင်မြန်ပြီး ဂရုစိုက်သော)** - ရိုးရှင်းသော မေးခွန်းများအတွက် လျင်မြန်ပြီး တိုက်ရိုက်ဖြေကြားချက်လိုချင်သောအခါ။ မော်ဒယ်သည် reasoning အနည်းငယ် ပြုလုပ်သည် - အဆင့် ၂ ထိ အများဆုံး။ တွက်ချက်ခြင်း၊ ရှာဖွေခြင်း သို့မဟုတ် ရိုးရှင်းသော မေးခွန်းများအတွက် အသုံးပြုပါ။

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

> 💡 **GitHub Copilot ဖြင့် စမ်းသပ်ကြည့်ပါ:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ဖိုင်ကို ဖွင့်ပြီး မေးမြန်းပါ -
> - "နိမ့်ဆန့် eagerness နဲ့ မြင့်ဆန့် eagerness prompting ကွာခြားချက် ဘာလဲ?"
> - "Prompt များအတွင်း XML tag များက AI ၏ ရုပ်သံကို ဘယ်လို ဖွဲ့စည်းပေးလဲ?"
> - "ကိုယ်တွေ့ ပြန်လည်စဉ်းစားသုံးသပ်မှု pattern နဲ့ တိုက်ရိုက်အညွှန်း pattern ကို ဘယ်အချိန် အသုံးပြုသင့်သလဲ?"

**မြင့်ဆန့် Eagerness (နက်ရှိုင်းပြီး ပြည့်စုံသော)** - ပြဿနာရှုပ်ထွေးသောအခါ နက်ရှိုင်းကျယ်ပြန့်သော စိစစ်မှုလိုလျှင်။ မော်ဒယ်သည် နက်ရှိုင်းစွာ လေ့လာသုံးသပ်ပြီး အကြောင်းပြချက်အသေးစိတ် ဖော်ပြသည်။ စနစ်ဒီဇိုင်း၊ ဆောက်လုပ်ရေး ဆုံးဖြတ်ချက်များ သို့မဟုတ် ရှာဖွေမှု ရှုပ်ထွေးသော အလုပ်များတွင် အသုံးပြုနိုင်သည်။

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**လုပ်ငန်းဆောင်တာ လုပ်ငန်းစဉ် (အဆင့်ဆင့် တိုးတက်မှု)** - အဆင့်အများသော အလုပ်များအတွက်။ မော်ဒယ်သည် အစီအစဉ်တစ်ခု ပေးပြီး အဆင့်တိုင်းကို ပြောကြားပြီးနောက် စုစုပေါင်းအကျဉ်းချုပ် ထုတ်ပေးသည်။ မိုက်ဂရိတ် လုပ်ငန်းများ၊ အသစ်တည်ဆောက်ခြင်း သို့မဟုတ် အဆင့်များစွာ လုပ်ငန်းစဉ်တွင် အသုံးပြုပါ။

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

Chain-of-Thought prompting က မော်ဒယ်အား reasoning ပြုလုပ်မှု အဆင့်ဆင့်ကို ပြရန် တိုက်တွန်းခြင်းဖြစ်ပြီး၊ ပြဿနာရှုပ်ထွေးမှုများတွင် တိကျမှုကို တိုးတက်စေပါသည်။ အဆင့်ဆင့် ခွဲခြားပြသခြင်းသည် လူနှင့် AI နှစ်ဖက်စလုံးအတွက် သဘောတရားကို နားလည်ရန် ကူညီပါသည်။

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ပါ:** ဒီ pattern နဲ့ ပတ်သက်ပြီး မေးပါ -
> - "လုပ်ငန်းဆောင်တာ pattern ကို ကြာရှည်တာဝန်အတွက် ဘယ်လိုညှိနှိုင်းမလဲ?"
> - "ထုတ်လုပ်မှု application များတွင် tool preambles ဖွဲ့စည်းရာတွင် အကောင်းဆုံးနည်းလမ်းများ မည်သို့ ရှိသနည်း?"
> - "UI တွင် အလယ်တန်းတိုးတက်မှု အကြောင်းကြားချက်များ ကို အမည်ဖော်စွာ ဖော်ပြပေးနိုင်မည်နည်း?"

<img src="../../../translated_images/my/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*အဆင့်ဆင့် လုပ်ငန်းစဉ်ကို အစီအစဉ် → ဆောင်ရွက် → အကျဉ်းချုပ် ပြုစုခြင်း*

**ကိုယ်တွေ့ ပြန်လည်စဉ်းစားသုံးသပ်ခြင်းကုဒ်** - ထုတ်လုပ်မှုအဆင့် စံများကိုလိုက်နာပြီး error handling ကောင်းမွန်သော code များထုတ်ပေးရန်။ အသစ် feature များ သို့မဟုတ် ဝန်ဆောင်မှုများတည်ဆောက်ရာတွင် အသုံးပြုပါ။

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/my/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*ကွာတလမှာ တိုးတက်ပြောင်းလဲခြင်း စက်ဝိုင်း - code ဖန်တီးခြင်း၊ သုံးသပ်ခြင်း၊ ပြဿနာရှာဖွေခြင်း၊ တိုးတက်မွမ်းမံခြင်း နှစ်ဆတွက်ချခြင်း*

**ဖွဲ့စည်းတည်ဆောက်ထားသော စိစစ်ခြင်း** - သေချာတိကျစွာ အကဲဖြတ်ရန်။ မော်ဒယ်သည် code ကို တည်ငြိမ်မှု၊ အကောင်းမွန်မှု၊ စွမ်းဆောင်ရည်၊ လုံခြုံရေး၊ ပြုပြင်ထိန်းသိမ်းမှု စသည့် ဖောက်အားချထားထားသော ဖရိမ်ဝပ်နှင့် တွက်ချက်သည်။ ကုဒ် ရှုမြင်ခြင်းများ သို့မဟုတ် အရည်အသွေး စိစစ်မှုများတွင် အသုံးပြုပါ။

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ပါ:** ဖွဲ့စည်းတည်ဆောက်ထားသော စိစစ်ချက်နှင့် ပတ်သက်ပြီး မေးမြန်းပါ -
> - "ကွဲပြားသော ကုဒ်သုံးသပ်မှုများအတွက် စိစစ် မျက်နှာပြင်ကို မည်သို့ စိတ်ကြိုက်ပြင်ဆင်မလဲ?"
> - "ဖွဲ့စည်းတည်ဆောက်ထားသော ထုတ်လွှင့်ချက်ကို စီမံခန့်ခွဲအသုံးပြုခြင်း အကောင်းဆုံးနည်းလမ်းများက ဘာလဲ?"
> - "ကွဲပြားသော လေ့လာရေးအစည်းအဝေးများတွင် ထိခိုက်မှု အဆင့်ကို မည်သို့ တည်ငြိမ်စေရမည်နည်း?"

<img src="../../../translated_images/my/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*သာမန်ရိုးရှင်းသော code review ဖန်တီးရေး ဖရိမ်ဝပ်နှင် severity များနှင့်*

**အကြိမ်ရေ များသော စကားပြောဆွေးနွေးမှု** - စကားဝိုင်းတွင် အစစဉ် အပြန်အလှန်ဖြစ်စေရန် context ကိုမှတ်ထားသည်။ အမှုဆောင်အကူအညီဆွေးနွေးခြင်း သို့မဟုတ် ရှုပ်ထွေးသော Q&A ပုံစံတွင် အသုံးပြုသည်။

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

*စကားပြောဆက်လက်မှု context များသည် အကြိမ်မြောက်တိုင်း အစုဆောင်းပြီး token အကန့်အသတ်ထိ ရောက်သည်*

**အဆင့်ဆင့် ဆင်ခြင်သုံးသပ်ခြင်း** - မြင်သာသော အတွေးအခေါ်လိုအပ်သော ပြဿနာများအတွက် ပြဿနာအား တစ်ဆင့်ချင်းစီ ဖြေရှင်းမှုကို ပြသည်။ သင်္ချာ၊ မှန်ကန်မှု ဉာဏ်ကစားများ သို့မဟုတ် စဉ်းစားတင်စားရန် လိုအပ်သည့်အခါ အသုံးပြုပါ။

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

*ပြဿနာများကို တိကျကျ သဘောထားချက်ဖြင့် တစ်ဆင့်ချင်း ဖျောက်ဖြေသည်*

**ကန့်သတ်ထားသော ထုတ်ရန်** - သတ်မှတ်ထားသော ပုံစံလိုက်နာမှု ဥပဒေစည်းကမ်းများနှင့်တပြေးညီ ဖြေကြားချက် ပေးသည်။ အကျဉ်းချုပ်များ သို့မဟုတ် ပြီးပြည့်စုံသော output ဖွဲ့စည်းမှု လိုအပ်သောအခါ အသုံးပြုပါ။

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

*သတ်မှတ်ထားသော ပုံစံ၊ အရှည်နှင့် ဖွဲ့စည်းပုံလိုက်နာမှုများ ဖြည့်ဆည်းပေးခြင်း*

## လက်ရှိ Azure အရင်းအမြစ်များကို အသုံးပြုခြင်း

**တပ်ဆင်မှု စစ်ဆေးခြင်း**

Root directory တွင် `.env` ဖိုင်ရှိပြီး Azure အချက်အလက်များပါရှိကြောင်း သေချာစေပါ (Module 01 တွင် ဖန်တီးထားသည်) -
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT၊ API_KEY၊ DEPLOYMENT ကို ပြသသင့်သည်။
```

**Application စတင်ခြင်း**

> **မှတ်ချက်:** Module 01 မှ `./start-all.sh` ဖြင့် အားလုံး စတင်ပြီးသားလျှင် ဒီ module သည် port 8083 တွင် ပြေးဆဲဖြစ်သည်။ အောက်ပါ စတင်မှု အမိန့်များကို ကျော်ပြီး http://localhost:8083 သို့ တိုက်ရိုက်သွားနိုင်ပါသည်။
**ရွေးချယ်မှု ၁: Spring Boot Dashboard အသုံးပြုခြင်း (VS Code အသုံးပြုသူများအတွက် အကြံပြုချက်)**

dev container တွင် Spring Boot Dashboard extension ပါဝင်ပြီး၊ Spring Boot application အားလုံးကို မျက်မှောက် interface ဖြင့် စီမံခန့်ခွဲနိုင်ပါတယ်။ VS Code ၏ ဘယ်ဘက်ဖက်ရှိ Activity Bar ထဲတွင် (Spring Boot အိုင်ကွန်ကို ရှာပါ) တွေ့နိုင်သည်။

Spring Boot Dashboard မှတဆင့်၊ သင်သည်:
- workspace ထဲတွင် ရှိသည့် Spring Boot applications အားလုံးကို ကြည့်ရှုနိုင်မည်
- အပလီကေးရှင်းများကို single click ဖြင့် စတင်/ရပ်နားနိုင်မည်
- application logs များကို အချိန်နှင့်တပြေးညီ ကြည့်ရှုနိုင်မည်
- application status ကို မျက်နှာပြင်တွင် ကြည့်ရှုနိုင်မည်

"prompt-engineering" အနားရှိ play ခလုတ်ကို နှိပ်ပြီး ဒီ module ကို စတင်ပါ၊ ဒါမှမဟုတ် အားလုံးကို တပြိုင်နက် စတင်နိုင်သည်။

<img src="../../../translated_images/my/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ရွေးချယ်မှု ၂: shell scripts အသုံးပြုခြင်း**

web applications (modules 01-04) အားလုံး စတင်ရန်:

**Bash:**
```bash
cd ..  # မူလ directory မှာမှ
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # အမြစ်ဖိုင္လမ်းကြောင်းမှ
.\start-all.ps1
```

ဒါမှမဟုတ် ဒီ module တစ်ခုတည်းကို စတင်ရန်:

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

script နှစ်ခုစလုံးသည် root `.env` ဖိုင်မှ environment variables များကို အလိုအလျောက် load လုပ်ပြီး၊ JAR မရှိသေးလျှင် build လုပ်ပေးမည်။

> **မှတ်ချက်:** စတင်မတိုင်မီ modules အားလုံးကို လက်မြန်တည်ဆောက်လိုပါက
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

**ရပ်နားရန်:**

**Bash:**
```bash
./stop.sh  # ဒီမော်ဂျူလ် သာ
# သို့မဟုတ်
cd .. && ./stop-all.sh  # မော်ဂျူလ်များ အားလုံး
```

**PowerShell:**
```powershell
.\stop.ps1  # ဒီမော်ဒျူးတစ်ခုခြင်းသာ
# သို့မဟုတ်
cd ..; .\stop-all.ps1  # မော်ဒျူးအားလုံး
```

## Application Screenshots

<img src="../../../translated_images/my/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*အဓိက dashboard မှ prompt engineering ၈ မျိုး၏ လက္ခဏာများနှင့် အသုံးပြုမှု လေ့လာချက်များကို ပြသထားသည်။*

## Patterns များကို လေ့လာခြင်း

web interface သည် အမျိုးမျိုးသော prompting strategy များကို စမ်းသပ်ရန် ခွင့်ပြုသည်။ pattern တစ်ခုချင်းစီသည် မတူညီသော ပြဿနာများကို ဖြေရှင်းပေးသည် - မည်သည့်နည်းလမ်းသည် ထူးချွန်သည့်အချိန်ကို စမ်းသပ်ကြည့်ပါ။

> **မှတ်ချက်: Streaming နှင့် Non-Streaming** — Pattern စာမျက်နှာတိုင်းတွင် နှစ်ခုသော ခလုတ်များ ရှိသည်။ **🔴 Stream Response (Live)** နှင့် **Non-streaming** အခြားရွေးချယ်မှု။ Streaming သည် Server-Sent Events (SSE) ကို သုံးပြီး 模型 生成 token များကို တိုက်ရိုက် ပြသခြင်းဖြစ်သည်။ Non-streaming သည် ပြန်လည်ဖြေဆိုမှု ပြီးဆုံးမှ ပြသသည်။ အထူးသဖြင့် High Eagerness, Self-Reflecting Code ကဲ့သို့ တင့်တယ်သော prompting များသည် non-streaming အချိန် များဆင်းနိုင်ပြီး အချို့ကောင်လေးများအတွက် မကြည့်ရှုရသော အချိန်ကြာမြင့်နိုင်သည်။ **ရှုပ်ထွေးသော prompting များတွင် streaming ဖြင့် စမ်းသပ်ရန် အကြံပြုသည်**၊ အချိန်လွန်သွားသည်ဟု ခံစားမိခြင်းမှ ရှောင်ရှားနိုင်သည်။
>
> **မှတ်ချက်: Browser လိုအပ်ချက်** — Streaming feature သည် Fetch Streams API (`response.body.getReader()`) ကို အသုံးပြုသည်၊ ၎င်းသည် ပြည့်စုံသော browser (Chrome, Edge, Firefox, Safari) များ ပံ့ပိုးထားပါသည်။ VS Code built-in Simple Browser တွင် မအောင်မြင်ပါ၊ အဲဒီ webview သည် ReadableStream API မပံ့ပိုးပါ။ Simple Browser ကို အသုံးပြုပါက non-streaming ခလုတ်များ သာလျှင် ဆက်လက်အလုပ်လုပ်နိုင်ပါသည်၊ streaming ခလုတ်များ သာ ပစ်ကွက်ခံရမည်။ ပြည့်စုံသော အတွေ့အကြုံအတွက် `http://localhost:8083` ကို အပြင် browser မှာ ဖွင့်ပါ။

### Low နှင့် High Eagerness

Low Eagerness ဖြင့် "What is 15% of 200?" ကဲ့သို့သော ရိုးရိုးရိုးရှင်းသော မေးခွန်းတစ်ခု မေးပါ။ အစာရှည်ရှည်ဖြင့် တိုက်ရိုက် ဖြေကြားချက်ရပါမယ်။ အခုတော့ "Design a caching strategy for a high-traffic API" ကဲ့သို့ ခက်ခဲသော မေးခွန်းကို High Eagerness ဖြင့် မေးနိင်သည်။ **🔴 Stream Response (Live)** ကို နှိပ်ပြီး 模型 ၏ အသေးစိတ် စဉ်းစားချက်များကို token အလိုက် ကြည့်ရှုနိုင်ပါတယ်။ 模型 တူညီသော်လည်း prompt မှာ သူ့ကို စဉ်းစားရန် ပြောသည်။

### Task Execution (Tool Preambles)

အဆင့်မြင့် workflows များအတွက် ကြိုစီစဉ်ခြင်းနှင့် ဖော်ပြချက် လိုအပ်ပါသည်။ 模型 သည် အလုပ်လုပ်မည့်အဆင့်များကို ကြိုပြောပြသည်၊ တစ်ဆင့်ချင်းကာလည်ပြောဆိုသည့်နည်းဖြင့် ပြီးဆုံးသည်။

### Self-Reflecting Code

"Create an email validation service" သို့ စမ်းပါ။ code ကို generate ကတည္းက ရပ်နားခြင်းမဟုတ်ဘဲ၊ quality criteria ဖြင့် အကဲဖြတ်၊ အားနည်းချက်များ ရှာဖွေပြီး တိုးတက်အောင် ပြင်ဆင်သည်။ code မွန်ကန်ရန် အထိ ပြန်ထပ်တုံ့ပြန်မှုများကို ကြည့်ရှုနိုင်ပါတယ်။

### Structured Analysis

code ကို အဆင့်သတ်မှတ်ထားသော သတ်မှတ်ချက်များဖြင့် (တိကျမှု၊ လုပ်ထုံးလုပ်နည်းများ၊ စွမ်းဆောင်ရည်၊ လုံခြုံမှု) အကဲဖြတ်ရန် လိုအပ်သည်။

### Multi-Turn Chat

"What is Spring Boot?" ဟု မေးပြီးနောက် "Show me an example" ဟုပြီးတိုက်ရိုက် မေးပါ။ 模型 သည် ပထမမေးခွန်းကို မှတ်မိပြီး Spring Boot နမူနာကို ပေးပါသည်။ မှတ်မိမှုမရှိပါက ဒုတိယမေးခွန်းသည် နက်နက်ရှိုင်းရှိုင်း မဟုတ်ပါ။

### Step-by-Step Reasoning

ဂဏန်းပြဿနာတစ်ခု ရွေး၍ Step-by-Step Reasoning နှင့် Low Eagerness နှစ်ခုစလုံး ဖြေလျှော့ပါ။ Low Eagerness သည် ဖြေချက်သာ ပေးပြီး အလွယ်တကူ ရရှိမည်။ Step-by-step သည် ချက်ချင်း တွက်ချက်ချက်များနှင့် ဆုံးဖြတ်ချက်များကို ပြသပါသည်။

### Constrained Output

တိကျသော ပုံစံများ သို့မဟုတ် စကားလုံး အရေအတွက် လိုအပ်ပါက ဤပုံစံသည် အတိအကျလိုက်နာရန် အာမခံသည်။ ၁၀၀ စကားလုံးရှိ bullet points အဖြစ် စုပေါင်းတင်ပြမှု စမ်းသပ်ကြည့်ပါ။

## သင်လေ့လာနေသော အရာများ

**စဉ်းစားမှု ကြိုးစားမှုသည် အရာအားလုံးကို ပြောင်းလဲသည်**

GPT-5.2 သည် သင်၏ prompt များမှတဆင့် ကွန်ပျူတာကြိုးစားမှုကို ထိန်းချုပ်နိုင်သည်။ ကြိုးစားမှုနည်းသည် အမြန်ဆုံးဖြေကြားမှုနှင့် အနည်းငယ်သာ စူးစမ်းသည့် များဖြစ်သည်။ ကြိုးစားမှုများသည် 模型 သည် ပြင်းထန်စွာ စဉ်းစားချိန်ယူသည်ကို ဆိုလိုသည်။ သင်သည် ကြိုးစားမှုနှင့် အလုပ်၏ ရှုပ်ထွေးမှုကို ကိုက်ညီစေနိုင်ရန် သင်ကြားနေသည် - ရိုးရိုးမေးခွန်းများတွင် အချိန်အလွန်မကုန်စေ၊ နှင့် ရှုပ်ထွေးသော ဆုံးဖြတ်ချက်များကိုလည်း အမြန်မလုပ်ပါနှင့်။

**ဖွဲ့စည်းမှုသည် အပြုအမူကို ဦးတည်စေနိုင်သည်**

prompt များရှိ XML တက်များကို သတိပြုပါ။ ၎င်းတို့သည် လှပသည့်အလှဆင်မှု မဟုတ်ပါ။ 模型 များသည် အဆင့်လိုက်ညွှန်ကြားမှုများကို အပ်နှံထားသည်။ အဆင့်မြင့်လုပ်ငန်းစဉ်များသို့မဟုတ် ရှုပ်ထွေးသော နည်းဗျူဟာများ လိုအပ်လျှင် ဖွဲ့စည်းမှုသည် 模型 ကို ၎င်းနေရာနှင့် နောက်မှာဘာလုပ်မည်ဆိုသည်ကို ပြောနိင်ရန် ကူညီသည်။

<img src="../../../translated_images/my/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*အပိုင်းပိုင်းရှင်းလင်းပြီး XML စတိုင်ဖွဲ့စည်းမှုရှိသော prompt ၏ ဖွဲ့စည်းတည်ဆောက်ပုံ*

**အရည်အသွေးကို ကိုယ်တိုင်အကဲဖြတ်ခြင်းဖြင့် စီမံခြင်း**

self-reflecting patterns များသည် quality criteria ကို ပြည့်စုံ ထုတ်ဖော်ပြသသည်။ 模型 သည် "မှန်ကန်သည်" ဟု မျှော်လင့်ခြင်းမဟုတ်ဘဲ၊ "မှန်ကန်သည်" မှာ တိကျစွာ ဘာဖြစ်ကြောင်း ပြောသည် - တိကျသော လုပ်ထုံးလုပ်နည်း၊ အမှားရှင်းခြင်း၊ စွမ်းဆောင်ရည်၊ လုံခြုံမှု။ 模型 သည် ၎င်း၏ ထုတ်ကုန်ကို သုံးသပ်ကာ တိုးတက်စေသည်။ ၎င်းသည် code generation ကို lottery ဖြစ်ခြင်းမှ လုပ်ငန်းစဉ်တစ်ခုအဖြစ် ပြောင်းလဲစေသည်။

**Context သည် ကန့်သတ်ထားသည်**

multi-turn စကားပြောဆိုမှုများသည် မက်ဆေ့ခ်ျ အတိတ် အချက်အလက်နှင့် တိုက်ရိုက် ပေါင်းစည်းမှုဖြင့် အလုပ်လုပ်သည်။ သို့သော် ကန့်သတ်ချက်ရှိသည် - 模型 တစ်ခုစီမှာ အများဆုံး token အရေအတွက်ရှိသည်။ စကားပြောများ တိုးတက်လာသည်နှင့် အလားတူ context ကို သိမ်းဆည်းထားရန် နည်းလမ်းများ လိုအပ်သည်။ ဒီ module သည် memory ရဲ့ လုပ်ငန်းစဉ်ကို ပြသပြီး၊ နောက်ပိုင်းတွင် မည်သည့်အချိန်တွင် စုစည်း၊ မေ့ထား၊ နှင့် မည်သည့်အချိန်တွင် ရယူရမည်ကို သင်ယူမည်။

## နောက်တစ်ဆင့်များ

**နောက် Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← Previous: Module 01 - Introduction](../01-introduction/README.md) | [Back to Main](../README.md) | [Next: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**အကြောင်းကြားချက်**:
ဤစာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) အသုံးပြု၍ ဘာသာပြန်ထားခြင်းဖြစ်ပါသည်။ ကျွန်ုပ်တို့သည် မှန်ကန်မှုအတွက် ကြိုးစားပေမယ့် အလိုအလျောက် ဘာသာပြန်မှုများတွင် အမှားများ သို့မဟုတ် မှားယွင်းမှုများ ပါရှိနိုင်ကြောင်း သိရှိကြပါစေ။ မူလ စာတမ်းအား အမွျတိယဘာသာဖြင့်သာ ယုံကြည်စိတ်ချရသော ရင်းမြစ်အဖြစ် ယူဆသင့်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက် လူမှုပညာရှင်များက ဘာသာပြန်ပေးခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုရာမှ ဖြစ်ပေါ်နိုင်သည့် နားလည်မှု မမှန်ကန်မှုများအတွက် ကျွန်ုပ်တို့အား တာဝန်ခံမည် မဟုတ်ပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->