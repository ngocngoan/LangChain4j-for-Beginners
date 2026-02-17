# Module 02: GPT-5.2 ဖြင့် Prompt Engineering

## မျက်နှာစာ စာရင်း

- [သင်တန်းတွင် သင်ယူမည့်အကြောင်းအရာများ](../../../02-prompt-engineering)
- [လိုအပ်သောမူလအချက်အလက်များ](../../../02-prompt-engineering)
- [Prompt Engineering ကိုနားလည်ခြင်း](../../../02-prompt-engineering)
- [Prompt Engineering အခြေခံများ](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [အဆင့်မြင့်နည်းစနစ်များ](../../../02-prompt-engineering)
- [ပိုင်ဆိုင်ပြီးသား Azure အရင်းအမြစ်များ အသုံးပြုခြင်း](../../../02-prompt-engineering)
- [အက်ပလီကေးရှင်း အပုံရိပ်များ](../../../02-prompt-engineering)
- [နည်းစနစ်များကို လေ့လာခြင်း](../../../02-prompt-engineering)
  - [အနည်းငယ်နဲ့ အမြင့် Eagerness ခွဲခြားခြင်း](../../../02-prompt-engineering)
  - [အလုပ်လုပ်ဆောင်မှု (ကိရိယာ စတင်ရန်စာများ)](../../../02-prompt-engineering)
  - [ကိုယ်တိုင် ပြန်လည်စဉ်းစားခြင်း ကုဒ်](../../../02-prompt-engineering)
  - [ဖွဲ့စည်းတည်ဆောက်ထားသော ခွဲခြမ်းစိတ်ဖြာခြင်း](../../../02-prompt-engineering)
  - [ခရီးဆက် စကားပြော](../../../02-prompt-engineering)
  - [ခြေလှမ်းခြေလှမ်း ဖြေရှင်းခြင်း](../../../02-prompt-engineering)
  - [ကန့်သတ်ထားသည့် ထုတ်လွှင့်ချက်](../../../02-prompt-engineering)
- [သင့်ရဲ့ လေ့လာနေမှု မှန်သမျှ](../../../02-prompt-engineering)
- [နောက်တွဲ အဆင့်များ](../../../02-prompt-engineering)

## သင်တန်းတွင် သင်ယူမည့်အကြောင်းအရာများ

<img src="../../../translated_images/my/what-youll-learn.c68269ac048503b2.webp" alt="သင်တန်းတွင် သင်ယူမည့်အကြောင်းအရာများ" width="800"/>

ယခင် module တွင် သင်တွေ့ရှိခဲ့သည်မှာ မေမရီက စကားပြော AI ကို ပြုလုပ်ရန် ဘယ်လို ကူညီပေးမလဲဆိုတာဖြစ်ပြီး GitHub Models ကို အသုံးပြုပြီး အခြေခံအပြန်အလှန်များ ပြုလုပ်ထားသည်။ ယခုတွင် သင်မေးခွန်းတွေကို မေးနည်း - အဲဒီ prompt များကို Azure OpenAI ရဲ့ GPT-5.2 ကို အသုံးပြုပြီး မေးဖို့အဓိကထားမည်ဖြစ်သည်။ သင်၏ prompt များကို ဖွဲ့စည်းပုံသည် ရယူမည့်တုံ့ပြန်မှု၏ အရည်အသွေးကို အလွန်ထိခိုက်စေပါသည်။ အခြေခံ prompt နည်းစနစ်များကို ပြန်လည်ဆန်းစစ်ပြီးနောက် GPT-5.2 ၏ အင်အားများကို ပြည့်စုံသုံးစွဲနိုင်စေရန် အဆင့်မြင့် ပုံစံရှစ်ခုသို့ ရောက်ရောက် သွားပါမယ်။

GPT-5.2 ကို အသုံးပြုမည်ကတော့ သင်၏ စဉ်းစားမှုကို ထိန်းချုပ်နိုင်စေသည် - မေးခွန်းတုံ့ပြန်ရန်မတိုင်မီ စဉ်းစားရန် ကုဒ်းချိန်ကို သတ်မှတ်နိုင်ပါသည်။ ၎င်းက မတူညီသော prompting နည်းလမ်းများကို ပိုဆန်းစစ်နိုင်စေပြီး ဘယ်နည်းချက်ကို ဘယ်အချိန်အသုံးပြုရမည်ဆိုတာနားလည်ကူညီပါသည်။ ထို့အပြင် GitHub Models နှင့်နှိုင်းယှဉ်ပြီး Azure မှ GPT-5.2 အတွက် အနည်းငယ်သော နှုန်းကန့်သတ်ချက်များကိုလည်း အကျိုးရှိစွာ ရရှိနိုင်ပါသည်။

## လိုအပ်ချက်များ

- Module 01 ပြီးစီးပြီး (Azure OpenAI အရင်းအမြစ်များ တပ်ဆင်ထားသည်)
- `.env` ဖိုင် မျိုးစုံ root ဖိုလ်ဒါတွင် သင့် Azure မက်ဆေ့အစလက်မှတ်များပါရှိသည်။ (Module 01 တွင် `azd up` သုံးပြီး ဖန်တီးထားသည်)

> **မှတ်ချက်။** Module 01 ကို မပြီးစီးသေးပါက၊ အဲ့ဒီမှာရှိသော တပ်ဆင်ခြင်းညွှန်ကြားချက်များကို ပထမဦးစွာ လိုက်နာပါ။

## Prompt Engineering ကိုနားလည်ခြင်း

<img src="../../../translated_images/my/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Prompt Engineering ဆိုတာဘာလဲ?" width="800"/>

Prompt engineering ဆိုသည်မှာ သင်လိုချင်သည့်ရလဒ်များကို အမြဲတမ်းရရှိစေရန် ဝင်ရောက်မှတ်သားရန် စာသားကို ဒီဇိုင်းထုတ်ခြင်းဖြစ်သည်။ မေးခွန်းများမေးခြင်းသာမက ဖော်ပြချက်များကို ယဉ်ကျေးစွာ ဖွဲ့စည်းပုံက ပုံမှန်သည် AI မော်ဒယ်အနေနဲ့ သင်လိုချင်တာကို သေချာနားလည်ပြီး မည်ကဲ့သို့ဖြေလုပ်မလဲဆိုတာသိထားစေသည်မှာ အရေးကြီးသည်။

ဒါကို ကိုယ်ပိုင်အလုပ်သမားကို ညွှန်ကြားချက်ပေးသည်နှင့်တူ। “Bug ကိုပြင်ပါ” ဆိုတာ မမှန်ကန်ပါ။ “UserService.java ဖိုင် ၄၅ မျဉ်းတွင် null pointer exception ကို null check ဖြင့် ပြင်ပါ” ဆိုရင် ပိုသေချာသည်။ ဘာသာစကားမော်ဒယ်များကလည်း ဒီလိုပဲ ဖြစ်သည် - အသေးစိတ်သတ်မှတ်ချက်နဲ့ ဖွဲ့စည်းမှုတွေက အရေးကြီးသည်။

<img src="../../../translated_images/my/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j ၏ တည်နေရာ" width="800"/>

LangChain4j သည် အဆောက်အအုံကို ပံ့ပိုးပေးသည် — မော်ဒယ်ချိတ်ဆက်မှုများ၊ မှတ်ဉာဏ်နှင့် မက်ဆေ့ခ််အမျိုးအစားများ၊ ကျန် prompt pattern များသည် အဲဒီအဆောက်အအုံမှတဆင့် လက်လှမ်းမီစေသည်။ အဓိက တည်ဆောက်မှုပစ္စည်းများမှာ `SystemMessage` (AI ၏ အပြုအမူနဲ့ အခန်းကဏ္ဍကိုသတ်မှတ်သည်) နှင့် `UserMessage` (သင့်တောင်းဆိုချက်ကို ထောက်ပံ့သည်) ဖြစ်သည်။

## Prompt Engineering အခြေခံများ

<img src="../../../translated_images/my/five-patterns-overview.160f35045ffd2a94.webp" alt="Prompt Engineering အခြေခံပုံစံငါးခု" width="800"/>

ဒီ module မှာ အဆင့်မြင့်နည်းစနစ်အထိ ဝင်မပြီးမီ အခြေခံ prompting နည်းလမ်းငါးခုကို ပြန်လည် သုံးသပ်ကြရအောင်။ ၎င်းတို့သည် prompt engineer တစ်ဦးစီ လိုအပ်သော အခြေခံအကျြားများ ဖြစ်သည်။ သင် [Quick Start module](../00-quick-start/README.md#2-prompt-patterns) ကို ရှေ့ကပြီးသားဖြစ်ပါက ဒီနည်းစနစ်တွေကို လက်တွေ့ ကြည့်ရှုကြပါပြီ — ယခုမှာ အကြောင်းအရာအခြေခံအဖွဲ့အစည်းကို ဖော်ပြပါသည်။

### Zero-Shot Prompting

လက်ကမ်းအသစ်ပြီး ရိုးရှင်းဆုံးနည်းလမ်းဖြစ်သည် - မော်ဒယ်အား ရိုးရှင်းသော တိုက်ရိုက်ညွှန်ကြားချက်ပေးခြင်းဖြစ်ပါတယ်။ မော်ဒယ်သည် တာဝန်ကျေမှုအတွက် လေ့ကျင့်မှုအပေါ် တာဝန်ခံပြီး ဖော်ပြချက်မပါဘဲ အမှန်အတိုင်း ဆောင်ရွက်သွားသည်။ ရိုးရှင်းသော တောင်းဆိုမှုများအတွက် ကောင်းစွာအလုပ်လုပ်သည်။

<img src="../../../translated_images/my/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*ဥပမာမပါဝင်ပဲ တိုက်ရိုက်ညွှန်ကြားချက်ပေးခြင်း — မော်ဒယ်သည် တာဝန်ကို ညွှန်ကြားချက်မှ တိုက်ရိုက် ခန့်မှန်းသည်*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// တုံ႔ပြန္ခ်က္: "အေအာင္ျမင္"
```

**အသုံးပြုရန် အချိန်:** ရိုးရှင်းသော အမျိုးအစားခြားနားခြင်း၊ တိုက်ရိုက်မေးခွန်းများ၊ ဘာသာပြန်ဆိုခြင်း သို့မဟုတ် ယင်းမလိုအပ်ပဲ မော်ဒယ်က ကိုင်တွယ်နိုင်သည့် တာဝန်များ။

### Few-Shot Prompting

မော်ဒယ်ကို လိုချင်သည့် ပုံစံကို ပြသသော ဥပမာများပေးပါ။ မော်ဒယ်သည် ဥပမာများမှ input-output ပုံစံကို သင်ယူပြီး နောက်မှာ ထပ်မံလာသော input များသို့ အခြေစိုက် ဆောင်ရွက်သည်။ ဒီနည်းလမ်းက လိုချင်သော ပုံစံ သို့မဟုတ် အပြုအမူ ရှင်းလင်းမရှိသော တာဝန်များအတွက် တိကျမှုကို တိုးမြှင့်ပေးသည်။

<img src="../../../translated_images/my/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*ဥပမာများမှ သင်ယူခြင်း — မော်ဒယ်သည် ဥပမာပုံစံကို ဖော်ထုတ်ပြီး နောက် input များတွင် အသုံးပြုသည်*

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

**အသုံးပြုရန် အချိန်:** ပြုပြင်ထုံးစံ မတူညီသော အမျိုးအစားခြားနားခြင်းများ၊ တိကျလက်ခံနိုင်သောပုံစံများ၊ domain စိတ်ဝင်စားဖွယ် ဝန်ဆောင်မှုများ သို့မဟုတ် zero-shot ရလဒ်မမြင့်မားသော အခါ။

### Chain of Thought

မော်ဒယ်ကို စဉ်းစားမှုကို ခြေလှမ်းနဲ့ ခြေလှမ်း ဖြောင့်ဖြူး ပြပြရန် မေးပါ။ တုံ့ပြန်ချက်ကို တိုက်ရိုက်ပြန်ပေးရခြင်း မဟုတ်ဘဲ ပြဿနာကို ခြေလှမ်းခြေလှမ်း ခွဲခြမ်းပြီး တစ်ခုခြင်းဆက်တိုက် လုပ်ဆောင်သည်။ ဂဏန်း၊ ရိုးရိုးဖြေရှင်းမှုနဲ့ ခြေလှမ်းမြောက်သော စဉ်းစားစဉ်ကိန်းများအတွက် တိကျမှု တိုးတက်စေသည်။

<img src="../../../translated_images/my/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*ခြေလှမ်းခြေလှမ်း စဉ်းစားမှု — ရှုပ်ထွေးသော ပြဿနာများကို ပိုမိုရှင်းလင်းသော နည်းဖြင့် ခွဲခြမ်းခြင်း*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// ဒီမော်ဒယ်မှာ ပြထားတာက ၁၅ - ၈ = ၇၊ ထို့နောက် ၇ + ၁၂ = ၁၉ ပန်းသီး ဖြစ်ပါတယ်။
```

**အသုံးပြုရန် အချိန်:** ဂဏန်းပြဿနာများ၊ ရိုးရွင်းသော စဉ်းစားမှု ပဟေဠိများ၊ debug လုပ်ခြင်း သို့မဟုတ် စဉ်းစားမှုဖြစ်စဉ် ပြသခြင်းဖြင့် တိကျမှုနှင့် ယုံကြည်မှု တိုးစေချင်သော တာဝန်များ။

### Role-Based Prompting

မေးခွန်းမေးရခင် AI အတွက် စာနယ်ဇင်း သို့မဟုတ် အခန်းကဏ္ဍ တစ်ခုသတ်မှတ်ပါ။ ၎င်းမှာ တုံ့ပြန်ချက်၏ အသံသဏ္ဍာန်၊ အနက်ရည်နှင့် အာရုံစိုက်မှုတို့ကို ပြောင်းလဲစေသည်။ “software architect” နှင့် “junior developer” သို့မဟုတ် “security auditor” တို့၏ အကြံပေးချက်က ကွဲပြားသည်။

<img src="../../../translated_images/my/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*အခန်းကဏ္ဍနှင့် persona သတ်မှတ်ခြင်း — တစ်ခုတည်းသော မေးခွန်းအတွက် သတ်မှတ်ထားသော အခန်းကဏ္ဍအလိုက် တုံ့ပြန်ချက်ကွဲပြားမှုရှိသည်*

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

**အသုံးပြုရန် အချိန်:** ကုဒ်စတင်ကြည့်ရှုခြင်း၊ သင်ကြားမှု၊ domain-specific ခွဲခြမ်းစိတ်ဖြာမှု သို့မဟုတ် တစ်ဧရိယာ သို့မဟုတ် ပြဿနာရှာဖွေရေး အဆင့်အတန်းတစ်ခုအတွက် အထူးပြု တုံ့ပြန်မှု လိုအပ်သောအခါ။

### Prompt Templates

ပြောင်းလဲနိုင်သော အချက်များ ပါဝင်သော ပြန့်ပြန်အသုံးပြုနိုင်သော prompt များ ဖန်တီးပါ။ တစ်ခါတိုင်း prompt အသစ်ရေးရန် မဖြစ်ကြောင်း တစ်ကြိမ် template သတ်မှတ်၍ အတန်းအစားတစ်ခုချင်းစီအတွက် တန်ဖိုးများ ဖြည့်စွက်ပြီးအသုံးပြုနိုင်သည်။ LangChain4j ၏ `PromptTemplate` class သည် `{{variable}}` သင်္ကေတဖြင့် ဒီလိပ်စာကို လွယ်ကူစေပါသည်။

<img src="../../../translated_images/my/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*ပြန်လည်အသုံးပြုနိုင်သော prompt များ တန်ဖိုးပြောင်းလဲနိုင်သောနေရာနှင့်အတူ — တစ် ချပ် template, အသုံးများစွာ*

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

**အသုံးပြုရန် အချိန်:** အပြောင်းအလဲ input များဖြင့် ထပ်မံမေးခွန်းများ၊ batch processing, ပြန်လည်အသုံးပြုရိုးရာ AI workflow များ တည်ဆောက်ခြင်း သို့မဟုတ် prompt ပြင်ဆင်မှုတစ်သင်းမှာ တူညီသော်လည်း ဒေတာက မတူညီသောအခါ။

---

ဒီအခြေခံငါးခုက prompt များအတွက် အတွင်းတော်ကိရိယာကောင်းတစ်ခုဖြစ်ပါသည်။ ဒီ module ရဲ့ ကျန်နောက်ပိုင်းမှာ GPT-5.2 ၏ reasoning control၊ ကိုယ်တိုင် တန်ဖိုးသုံးသပ်ခြင်းနဲ့ ဖွဲ့စည်းတည်ဆောက်ထားသော ထုတ်လွှင့်ချက် အင်အားများကို အသုံးပြုသည့် **အဆင့်မြင့်နည်းစနစ်ရှစ်ခု** အပေါ် အခြေခံထားသည်။

## အဆင့်မြင့်နည်းစနစ်များ

အခြေခံ အကြောင်းအရာများပြည့်စုံပြီးနောက်၊ ဒီ module အထူးလက္ခဏာဖြစ်သည့် အဆင့်မြင့် နည်းစနစ် ရှစ်ခုကို တက်ကြွစွာ မိတ်ဆက်ပေးချင်ပါတယ်။ ပြဿနာတိုင်းအတွက် နည်းလမ်းတူညီမဟုတ်ပါဘူး။ မေးခွန်းတချို့က အမြန်ပြန်ချက်လိုအပ်ပြီး၊ တချို့က စဉ်းစားချင်းတွေလိုတယ်။ reasoning ပြသချက်လည်းလိုအပ်ချင်သူရှိပြီး၊ နောက်ထပ်မှာ ရလဒ်ပဲလိုချင်တာရှိတယ်။ ဒီပုံစံတစ်ခုချင်းစီသည် အခြေအနေအမျိုးမျိုးအတွက် optimize ပြုလုပ်ထားပြီး GPT-5.2 ၏ reasoning control ပြောချက်နိုင်မှုက ဖော်ပြချက်တွေကို ပိုသက်သက်သာသာ ပြသပေးပါသည်။

<img src="../../../translated_images/my/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Prompting အဆင့်မြင့်ပုံစံရှစ်ခု" width="800"/>

*Prompt engineering အဆင့်မြင့် နည်းစနစ်ရှစ်ခုနှင့် ၎င်းတို့၏ အသုံးပြုမှုအခြေအနေများ အကျဉ်းချုပ်*

<img src="../../../translated_images/my/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 နှင့် reasoning control" width="800"/>

*GPT-5.2 ၏ reasoning control က မော်ဒယ်က ဘယ်နှစ်ဆင့် သတိပြုစဉ်းစားရမလဲဆိုတာ သတ်မှတ်နိုင်စေသည် - အမြန်နှင့် တိုက်ရိုက်ဖြေဆိုမှ စတင်ရှာဖွေမှု အထိ*

<img src="../../../translated_images/my/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort နှိုင်းယှဉ်ခြင်း" width="800"/>

*အနိမ့် eagerness (မြန်ဆန်၊ တိုက်ရိုက်) နှင့် အမြင့် eagerness (သေချာ၊ နက်ရှိုင်း) reasoning နည်းလမ်းများ*

**အနိမ့် Eagerness (မြန်နှင့် အာရုံစိုက်ထားခြင်း)** - ရိုးရှင်းသောမေးခွန်းများအတွက် အမြန်၊ တိုက်ရိုက်ဖြေဆိုချက်ပြုမည်။ မော်ဒယ်သည် reasoning အဆင့်အနည်းဆုံး ၂ ဆင့်သာ လုပ်ဆောင်သည်။ ကိန်းဂဏန်းတွက်ချက်ခြင်း၊ ရှာဖွေမှု သို့မဟုတ် ရိုးရှင်းသော မေးခွန်းများအတွက် အသုံးပြုပါ။

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

> 💡 **GitHub Copilot နဲ့ စမ်းသပ်ကြည့်ပါ။** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ဖိုင်ကိုဖွင့်၍ မေးမြန်းပါ။
> - "အနိမ့် eagerness နဲ့ အမြင့် eagerness prompting pattern များကြား ဘာကွာခြားမှုရှိပါသလဲ?"
> - "prompt တွင် XML tag များက AI ၏တုံ့ပြန်ချက် ဖွဲ့စည်းရာ ဘယ်လိုကူညီပါသလဲ?"
> - "ကိုယ်တိုင်ပြန်လည်စဉ်းစားမှု pattern များကို တိုက်ရိုက်ညွှန်ကြားမှုနဲ့ ဘယ်အချိန်အသုံးပြုသင့်သလဲ?"

**အမြင့် Eagerness (နက်ရှိုင်းပြီး စဉ်းစားသေချာမှု)** - အခက်ခဲပြဿနာများအတွက် အသုံးပြုပါ။ မော်ဒယ်သည် နက်ရှိုင်းစွာ စမ်းသပ်လေ့လာပြီး အချက်အလက်အသေးစိတ်များ ပါဝင်သော reasoning ပြသချက်များ ပေးသည်။ စနစ်ဒီဇိုင်း၊ ပြုပြင်လုပ်ဆောင်ချက် ဆုံးဖြတ်ချက်များ သို့မဟုတ် ရှာဖွေမှုများအတွက် သင့်တော်သည်။

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**အလုပ်လုပ်ဆောင်မှု (ခြေလှမ်းခြေလှမ်း ဆက်လက်လုပ်ဆောင်ခြင်း)** - လုပ်ငန်းစဉ် များသောအဆင့်လုပ်ငန်းများအတွက်ဖြစ်သည်။ မော်ဒယ်သည် စီမံကိန်းတစ်ရပ် ပေးပြီး၊ လုပ်ဆောင်သည့်ခြေလှမ်းကို ဆက်တိုက်ဖော်ပြပြီး၊ နောက်ဆုံးတွင် အကျဉ်းချုပ်ပေးသည်။ မိုင်ဂရိတ်မှု၊ တပ်ဆင်မှု သို့မဟုတ် လုပ်ငန်းအဆင့်စုံ လုပ်ငန်းစဉ်များအတွက်အသုံးပြုနိုင်သည်။

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

Chain-of-Thought prompting သည် မော်ဒယ်အား reasoning ဖော်ပြရန် တိုက်ရိုက်တောင်းဆိုခြင်းဖြစ်ပြီး ရှုပ်ထွေးသော တာဝန်များအတွက် တိကျမှုမြှင့်တင်သည်။ ခြေလှမ်းခြေလှမ်းခွဲခြမ်းခြင်းက လူနှင့် AI နှစ်ဦးလုံး reasoning ကို နားလည်ဖို့ ကူညီပေးသည်။

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ကြည့်ပါ။** ဒီ pattern နဲ့ ပတ်သက်၍ မေးမြန်းပါ။
> - "အလုပ်လုပ်ဆောင်မှု pattern ကို အချိန်ကြာမြင့်တဲ့ operation များအတွက် ဘယ်လိုညှိနှိုင်းမလဲ?"
> - "ထုတ်လုပ်မှု အက်ပလီကေးရှင်းများ၌ ကိရိယာ preambles များ စီမံဖွဲ့စည်းခြင်းအတွက် အကောင်းဆုံးအနေအထားများကဘာတွေလဲ?"
> - "UI တွင် အလယ်အလတ် တိုးတက်မှု အချက်အလက်များကို ဘယ်လိုဖမ်းယူ ပြသနိုင်မလဲ?"

<img src="../../../translated_images/my/task-execution-pattern.9da3967750ab5c1e.webp" alt="အလုပ်လုပ်ဆောင်မှု ပုံစံ" width="800"/>

*Multi-step task များအတွက် စီမံကိန်း → ဆောင်ရွက်ခြင်း → အကျဉ်းချုပ် အလုပ်စဉ်*

**ကိုယ်တိုင် ပြန်လည်စဉ်းစားခြင်း ကုဒ်** - ထုတ်လုပ်မှု အရည်အသွေးကို မြှင့်တင်ရန် သုံးသည်။ မော်ဒယ်သည် error handling မှန်ကန်စွာ ဆောင်ရွက်ပြီး ထုတ်လုပ်မှု စံချိန်စံညွှန်းနှင့် ကိုက်ညီသော ကုဒ် များ ထုတ်လုပ်ပေးသည်။ နောက်ဆုံး features သို့ ဝန်ဆောင်မှုများ ဖန်တီးရာတွင် သုံးနိုင်သည်။

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/my/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="ကိုယ်တိုင်ပြန်လည်စဉ်းစားမှု လည်ပတ်မှု" width="800"/>

*အကြိမ်ကြိမ်တိုးတက်မှု စက်ဝိုင်း - generate → evaluate → issues ဖော်ထုတ် → တိုးတက်မှု → ပြန်လုပ် (repeat)*

**ဖွဲ့စည်းတည်ဆောက်သော ခွဲခြမ်းစိတ်ဖြာခြင်း** - လေ့လာမှုကို စနစ်တကျ ပြုလုပ်ရန်။ မော်ဒယ်သည် code ကို တိကျမှု၊ သုံးစွဲမှုစံနှုန်းများ၊ လုပ်ဆောင်ချက်၊ လုံခြုံရေးနှင့် ပြုပြင်ထိန်းသိမ်းမှုများအရ သုံးသပ်မှု ဆောင်ရွက်သည်။ ကုဒ်စစ်ဆေးမှု သို့မဟုတ် အရည်အသွေး အကဲဖြတ်ရာတွင် အသုံးပြုသည်။

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ကြည့်ပါ။** ဖွဲ့စည်းတည်ဆောက်ခြင်းအပေါ် မေးမြန်းပါ။
> - "ပြုပြင်ခြင်း မျိုးစုံအတွက် ခွဲခြမ်းစိတ်ဖြာမှု သင်္ချာကဲ့သို့ ကွဲပြားသည်ကို နည်းလမ်းများ"
> - "ဖွဲ့စည်းတည်ဆောက် output ကို ကုဒ်ဖြင့် ဘယ်လို အမှန်တကယ် ဖော်ထုတ် လုပ်ဆောင်ပါသလဲ?"
> - "အမျိုးအစား ကွဲပြားခြင်း review sessions များအတွင်း တင်းကျပ်မှု အဆင့် များကို ဘယ်လိုထားရှိရမလဲ?"

<img src="../../../translated_images/my/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="ဖွဲ့စည်းတည်ဆောက် ခွဲခြမ်းစိတ်ဖြာ ပုံစံ" width="800"/>

*တင်းကျပ်သော မျှတသော severity အဆင့်များဖြင့် ဖွဲ့စည်းတည်ဆောက်ထားသော code စစ်ဆေးမှု*

**ခရီးဆက် စကားပြော** - context လိုအပ်သော စကားပြောပေါင်းများစွာအတွက်။ မော်ဒယ်သည် ယခင်ပို messages များကို မှတ်ဉာဏ် ထားပြီး အဆက်အသွယ် ဖွဲ့စည်းသည်။ မိတ်ဆက်ကူညီမှုများ သို့မဟုတ် ရှုပ်ထွေးသော ဖြေကြားမှုများအတွက် အသုံးပြုသည်။

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/my/context-memory.dff30ad9fa78832a.webp" alt="စကားပြော စကားဝိုင်း မှတ်ဉာဏ်" width="800"/>

*စကားပြော အကြောင်းအရာများမှာ မကြာခဏ လည်ပတ်ပြီး token အကန့်အသတ်သို့ တစ်ဆင့်ကနေ တစ်ဆင့် တွဲလက်ထားသည်*

**ခြေလှမ်းခြေလှမ်း ဖြေရှင်းမှု** - အရည်အချင်း ပြများသည့် ပြဿနာများအတွက်သုံးသည်။ မော်ဒယ်သည် ခြေလှမ်းတိုင်းအတွက် reasoning ကို သေချာ ဖော်ပြသည်။ ဂဏန်းပြဿနာများ၊ ရိုးရွင်းသော logic puzzles သို့မဟုတ် စဉ်းစားနည်းကို နားလည်လိုသောအခါ အသုံးပြုပါ။

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/my/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="ခြေလှမ်းခြေလှမ်း ပုံစံ" width="800"/>

*ပြဿနာများကို သေချာသော logic ခြေလှမ်းများအဖြစ် ခွဲခြမ်းခြင်း*

**ကန့်သတ်ထားသည့် ထုတ်လွှင့်ချက်** - ဖော်ပြချက်ပုံစံ နှင့် အရှည်တူညီမှု လိုအပ်သော တုံ့ပြန်ချက်များအတွက် ျပုလုပ်သည်။ မော်ဒယ်သည် ပုံစံ နှင့် အရှည်စည်းမျဉ်းများကို တိကျစွာ လိုက်နာပါသည်။ အကျဉ်းချုပ်များ သို့မဟုတ် တိကျသော output စနစ်လိုအပ်သောအခါ အသုံးပြုပါ။

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

<img src="../../../translated_images/my/constrained-output-pattern.0ce39a682a6795c2.webp" alt="ကန့်သတ်ထုတ်လွှင့်ခြင်း ပုံစံ" width="800"/>

*တိကျသော ပုံစံ၊ အရှည်နှင့် ဖွဲ့စည်းမှု စည်းမျဉ်းများကို အကောင်အထည်ဖော်စေခြင်း*

## ပိုင်ဆိုင်ပြီးသား Azure အရင်းအမြစ်များ အသုံးပြုခြင်း

**တပ်ဆင်မှု အတည်ပြုခြင်း:**

root ဖိုလ်ဒါတွင် Azure မက်ဆေ့၊ ကိုယ်စားလှယ်ရေး `.env` ဖိုင် ရှိနေသည်ကို သေချာစေရန် (Module 01 အတွင်း ဖန်တီးထားသည်)-
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကို ဖော်ပြသင့်သည်။
```

**အက်ပလီကေးရှင်း စတင်ခြင်း:**

> **မှတ်ချက်။** Module 01 ထဲမှ `./start-all.sh` ဖြင့် အက်ပလီကေးရှင်းအားလုံးကို စတင်ထားပါက ဒီ module သည် ယခင် port 8083 တွင် ကြာရှည်ပြေးနေပါပြီ။ အောက်ပါ စတင်ကမ်းလှမ်းချက်ကို လုပ်ဆောင်စရာမလိုဘဲ http://localhost:8083 ကို တိုက်ရိုက်သွားနိုင်သည်။

**နည်းလမ်း ၁: Spring Boot Dashboard အသုံးပြုခြင်း (VS Code အသုံးပြုသူများအတွက် အကြံပြုသည်)**

dev container တွင် Spring Boot Dashboard extension ပါဝင်ပြီး အဲဒီအရာက Spring Boot application အားလုံးကို မျက်မြင်အားဖြင့် စီမံရန် Interface ပေးသည်။ VS Code ၏ ဘယ်ဘက် Activity Bar တွင် Spring Boot အိုင်ကွန်ကို ရှာနိုင်သည်။
Spring Boot Dashboard မှအောက်ပါအရာများကိုလုပ်ဆောင်နိုင်သည် -
- အလုပ်လုပ်နေသော Spring Boot applications များအားလုံးကိုမြင်နိုင်သည်
- အပိုင်း(Modules) များကိုတစ်ချက်နှိပ်၍စတင်/ရပ်တန့်နိုင်သည်
- Application log များကို real-time တွင်ကြည့်ရှုနိုင်သည်
- Application အခြေအနေများကိုစောင့်ကြည့်နိုင်သည်

"prompt-engineering" အနားရှိ play button ကိုကလစ်နှိပ်၍ module ကိုစတင်နိုင်ပြီး၊ အပိုင်း(Modules) အားလုံးကိုတစ်ပြိုင်နက်စတင်နိုင်သည်။

<img src="../../../translated_images/my/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**ရွေးချယ်မှု ၂: shell script များအသုံးပြုခြင်း**

Web Application (Module 01-04) အားလုံးကိုစတင်ရန် -

**Bash:**
```bash
cd ..  # မူလ directory မှတဆင့်
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # အမြစ်ဖိုင်လမ်းကြောင်းမှ
.\start-all.ps1
```

သို့မဟုတ် ဒီ module ပဲစတင်ရန် -

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

၂ script တွေက root `.env` ဖိုင်ကနေ environment variables ကိုအလိုအလျောက် load လုပ်ပြီး JAR ဖိုင်တွေမရှိရင် build လုပ်ပေးမှာ ဖြစ်သည်။

> **မှတ်ချက်:** စတင်ခင်မှာ ဆရာကြီးကိုယ်တိုင် modules အားလုံးကို build လုပ်ချင်ရင် -
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

သင့်ရဲ့ browser မှာ http://localhost:8083 ကိုဖွင့်ပါ။

**ရပ်တန့်ရန်**

**Bash:**
```bash
./stop.sh  # ဤမော်ဂျူးလ်ဖြစ်သည်
# သို့မဟုတ်
cd .. && ./stop-all.sh  # မော်ဂျူးလ်အားလုံး
```

**PowerShell:**
```powershell
.\stop.ps1  # ဒီမော်ဂျူးသာ
# ဒါမှမဟုတ်
cd ..; .\stop-all.ps1  # မော်ဂျူးအားလုံး
```

## Application Screenshot များ

<img src="../../../translated_images/my/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*လုပ်ငန်းစဉ်များနှင့် အသုံးချမှုများပါရှိသည့် ၈ မျိုးလုံး prompt engineering ပုံစံများကိုပြသသည့် အဓိက dashboard*

## ပုံစံများ စူးစမ်းလေ့လာခြင်း

Web interface မှာ prompting strategy များစမ်းသပ်နိုင်သည်။ ပုံစံအမျိုးအစားတိုင်းက ပြဿနာအမျိုးမျိုးကိုဖြေရှင်းပေးသည် - မည်သည့်နည်းလမ်းက ထင်ရှားသည်ဆိုတာကြည့်ရန် ကြိုးစားပါ။

### Low Eagerness နှင့် High Eagerness

Low Eagerness ဖြင့် "200 ရဲ့ 15% ဘာလဲ?" ဆိုတဲ့ ရိုးရှင်းတဲ့မေးခွန်းကိုမေးပါ။ တိုက်ရိုက်အဖြေကို လျင်မြန်စွာ ရရှိမှာပါ။ အခုတော့ High Eagerness သုံးပြီး "High-traffic API အတွက် caching strategy တစ်ခု ဒီဇိုင်းဆွဲပါ" ဆိုတဲ့ရှုပ်ထွေးတဲ့မေးခွန်းတစ်ခုအတွက် စမ်းပါ။ Model က တိကျမှုနဲ့ စဉ်းစားရန်ကြာမြင့်ပြီး အကြောင်းပြချက်များပြန်လည်ပေးပါလိမ့်မယ်။ Model တူတူ၊ မေးခွန်းဖွဲ့စည်းမှုတူသော်လည်း prompt က ဘယ်လောက်စဉ်းစားရမလဲ ပြောပြနေတယ်။

<img src="../../../translated_images/my/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*အနည်းငယ်စဉ်းစားမှုဖြင့် တိုတောင်းမြန်ဆန်သောတွက်ချက်မှု*

<img src="../../../translated_images/my/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*အသေးစိတ် caching strategy (2.8MB)*

### Task Execution (Tool Preambles)

အဆင့်ဆင့် workflow များအတွက် ရှေ့တန်ဆာသတ်မှတ်ချက်နှင့် လုပ်ဆောင်ချက်များ ဖော်ပြခြင်း အကျိုးရှိသည်။ Model က မည်သည့်အရာများလုပ်မည်ကို ရှင်းလင်းတင်ပြပြီး နောက်တစ်ဆင့်လုပ်ဆောင်မှုတိုင်းကိုတင်ပြ၊ နောက်ဆုံးမှာ ရလဒ်ကိုအနှစ်ချုပ်တင်ပြသည်။

<img src="../../../translated_images/my/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*Step-by-step ဖော်ပြချက်နှင့် REST endpoint တည်ဆောက်ခြင်း (3.9MB)*

### ကိုယ်ပိုင်သုံးသပ်ချက် Code

"Create an email validation service" ကိုစမ်းကြည့်ပါ။ Code တက်မြှောက်ပြီး ရပ်တန့်တာမဟုတ်ဘဲ Model က generated code ကို အရည်အသွေးစံချိန်နဲ့ ဆန်းစစ်၊ အားနည်းချက်တွေ ရှာဖွေပြီး တိုးတက်အောင် ဆောင်ရွက်ပါတယ်။ Production စံနှုန်း မြောက်အောင် လုပ်ဆောင်တဲ့အထိ မကြာခဏ iteration လုပ်ပေးပါလိမ့်မယ်။

<img src="../../../translated_images/my/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*အပြည့်အစုံ email validation ဝန်ဆောင်မှု (5.2MB)*

### သက်သေပြသည့် စနစ်

Code review သည် အဆင့်မြှင့်စံချိန်များဖြင့် စနစ်တကျ အကဲဖြတ်သုံးသပ်ရန်လိုအပ်သည်။ Model က fixed category များ (မှန်ကန်မှု, လုပ်ထုံးလုပ်နည်း, စွမ်းဆောင်ရည်, လုံခြုံမှု) တိုင်းတာမှုကြိမ်နှုန်းများဖြင့် စတင်စစ်ဆေးပေးသည်။

<img src="../../../translated_images/my/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*စနစ်တကျ code review ဖန်တီးခြင်း*

### Multi-Turn Chat

"Spring Boot ဆိုတာဘာလဲ?" မေးပြီးနောက် "ဥပမာပြပါ" ကိုချက်ချင်းဆက်မေးပါ။ Model က အလျင်အမြန် ပထမမေးခွန်းကိုမှတ်သားထားပြီး Spring Boot ပုံသဏ္ဍာန်အတိအကျကိုပေးပါသည်။ သတိမပါခဲ့ရင် ဒုတိယမေးခွန်းဟာ too vague ျဖစ္သွားမယ်။

<img src="../../../translated_images/my/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*မေးခွန်းများအတွင်း context ကိုသိုလှောင်ထားခြင်း*

### အဆင့်ဆင့် အာရုံစူးစိုက်မှု

ဂဏန်းပြဿနာတစ်ခုရွေးပြီး Step-by-Step Reasoning နဲ့ Low Eagerness နှစ်မျိုးနည်းလမ်းဖြင့် စမ်းပါ။ Low eagerness မှာ တိုက်ရိုက်ဖြေဆိုပေးတာဖြစ်ပြီး မြန်တယ်၊ ဒါပေမဲ့ရှင်းလင်းမူနည်းပါးတယ်။ Step-by-step နည်းလမ်းမှာ တွက်ချက်မှုနှင့် ဆုံးဖြတ်ချက်တိုင်းကို သေချာပြသပေးပါသည်။

<img src="../../../translated_images/my/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*အဆင့်ဆင့်ရှင်းပြချက်ဖြင့် ဂဏန်းပြဿနာ*

### ကန့်သတ်ထားသော ထွက်ရှိမှု

သတ်မှတ်ထားသောဖော်မာနှင့် စကားလုံးရေလိုအပ်ချိန်တွင် ဒီ pattern က လိုက်နာမှုကိုကာကွယ်ပေးသည်။ တိတိကျကျ ၁၀၀ စကားလုံးပါသော စာတမ်းကို bullet point ဖော်မာဖြင့် ဖန်တီးဖို့ ကြိုးစားပါ။

<img src="../../../translated_images/my/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*format ထိန်းချုပ်ထားသော machine learning အနှစ်ချုပ်*

## သင်လေ့လာနေတဲ့အရာ

**စဉ်းစားမှုကြိုးပမ်းမှုက အရာအားလုံးကို ပြောင်းလဲစေတယ်**

GPT-5.2 က prompt များကနေ computation ကြိုးပမ်းမှုကို ထိန်းချုပ်နိုင်စေသည်။ ကြိုးပမ်းမှုနည်းရင် အဖြေတွေမြန်ဆန်ပြီး လေ့လာမှုနည်းပါးသည်။ ကြိုးပမ်းမှုများရင်တော့ model က အမြဲစဉ်းစားပြီး သေချာစွာလုပ်ဆောင်ရန် ကြာမြင့်သည်။ အလုပ်ရှုပ်ထွေးမှုနှင့် ကိုက်ညီသော ကြိုးပမ်းမှုကို သင်တတ်လာပါပြီ - ရိုးရိုးမေးခွန်းတွေအတွက် အချိန်မပေး၊ အလုပ်ရှုပ်တဲ့ ဆုံးဖြတ်မှုတွေထဲမှာလည်း မရှုပ်ပါနှုတ်။

**ဖွဲ့စည်းမှုပဲ မှီငြမ်းတဲ့ အပြုအမူကို ညွှန်ပြတယ်**

prompt များထဲမှာ XML tag တွေမြင်ရမယ်။ ဒါဟာ အလှ Decoration မဟုတ်ပါဘူး။ Model များက ဖွဲ့စည်းထားတဲ့ ညွှန်ကြားချက်တွေကို ပိုမိုယုံကြည်စွာ လိုက်နာတတ်ကြသည်။ multi-step လုပ်ငန်းစဉ်တွေ သို့မဟုတ် ရှုပ်ထွေး Logic တွေလိုအပ်ရင် ဖွဲ့စည်းမှုက Model ကို အရန်တွက်သိမှတ်နေရာကိုမှုစေပြီး နောက်တစ်ဆင့် လုပ်ဆောင်မယ့်အရာကို ပြောကြားနိုင်လိမ့်မယ်။

<img src="../../../translated_images/my/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*အပိုင်းတိတိကျကျရှိပြီး XML စတိုင်ဖြင့် ပုံဖော်ထားသည့် prompt ၁ ခု၏ anatomy*

**ကိုယ်ဆောင်ရွက်ချက်မှ စွမ်းဆောင်ရည်တိုးတက်လာသည်**

Self-reflecting patterns များသည် quality criteria များကို ထင်ဟပ်စေခြင်းဖြင့် လုပ်ဆောင်သည်။ Model က “မှန်ကန်စွာလုပ်တယ်” ဆိုတာမျှော်လင့်မနေဘဲ “မှန်ကန်” ဆိုပါသည့် အရာအားလုံး - မှန်ကန်သော logic, error handling, performance, security စသည့်အခြေအနေများကို ပြောပြသည်။ Model က ဂုဏ်သတ္တိကို ကိုယ်တိုင် တိုင်းတာပြီး အမြှင့်တင်နိုင်သည်။ ဒါက coding process ကို တစ်ခုက ကျပ်တည်းတဲ့ ပုံစံမှ လုပ်ဆောင်မှု တစ်ခုအဖြစ် ပြောင်းလဲပေးသည်။

**Context က ကြီးမားမှုရှိသည်**

Multi-turn chat များက သေချာ Message history ပါဝင်သည့်request များဖြင့် အလုပ်လုပ်သည်။ ဒါပေမဲ့ ကန့်သတ်ချက်ရှိပြီး - Model အားလုံးမှာ token အများဆုံး များအကန့်အသတ်ရှိသည်။ စကားပြောနောင်တစ်ဆက်တည်းတိုးလာတာနဲ့ အဆင်ပြေတဲ့ context ကို နေအာက်ထားဖို့ နည်းလမ်းများ လိုအပ်စေသည်။ ဒီ module က memory ကဘာလဲ၊ ဘယ်အချိန်မှာ အနှစ်ချုပ်ရမလဲ၊ ဘယ်အချိန်မှာ မေ့ထားရမလဲ၊ ဘယ်အချိန်မှာ ရှာထုတ်ရမလဲဆိုတာသင်ပေးမှာဖြစ်သည်။

## နောက်တစ်ဆင့်များ

**နောက်ပိုင်း Module:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**Navigation:** [← အရင်ဆုံး အပိုင်း 01 - နိဒါန်း](../01-introduction/README.md) | [ပြန်သွားရန် Main](../README.md) | [ရှေ့တော် Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ကန့်သတ်ချက်**  
ဤစာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) သုံးပြီး ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှန်ကန်မှုအတွက် ကြိုးပမ်းသော်လည်း အလိုအလျှောက်ဘာသာပြန်မှုများတွင် အမှားများ သို့မဟုတ် မှားယွင်းချက်များ ပါဝင်နိုင်ကြောင်း သတိပြုရန် လိုအပ်ပါသည်။ မူလစာတမ်းသည် မူရင်းဘာသာဖြင့် အတည်ပြုရမည့် စံချိန်စံညွှန်း ဖြစ်ပါသည်။ အရေးကြီးသောသတင်းအချက်အလက်များအတွက်တော့ ပညာရှင် လူပညာတတ် ဘာသာပြန်သူ၏ ဘာသာပြန်မှုကို အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုရာမှ ဖြစ်ပေါ်လာနိုင်သည့် နားလည်မှုမှားခြင်းများ သို့မဟုတ် မှားယွင်းစိတ်ခံစားမှုများအပေါ် ကျွန်ုပ်တို့ တာဝန်မယူပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->