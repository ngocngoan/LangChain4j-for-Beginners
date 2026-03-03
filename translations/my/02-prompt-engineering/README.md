# Module 02: GPT-5.2 ဖြင့် Prompt Engineering

## Table of Contents

- [ဗီဒီယို လမ်းညွှန်](../../../02-prompt-engineering)
- [သင်လေ့လာမည့်အရာများ](../../../02-prompt-engineering)
- [လိုအပ်သောအကြောင်းအရာများ](../../../02-prompt-engineering)
- [Prompt Engineering ကို နားလည်ခြင်း](../../../02-prompt-engineering)
- [Prompt Engineering အခြေခံများ](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [အဆင့်မြင့် ပုံစံများ](../../../02-prompt-engineering)
- [အပလီကေးရှင်းကို စတင် လုပ်ဆောင်ခြင်း](../../../02-prompt-engineering)
- [အပလီကေးရှင်း ဖန်တီးမှုများ](../../../02-prompt-engineering)
- [ပုံစံများကို ရှာဖွေခြင်း](../../../02-prompt-engineering)
  - [နိမ့်သော စိတ်အားထက်သန်မှုနဲ့ မြင့်သော စိတ်အားထက်သန်မှု](../../../02-prompt-engineering)
  - [တာဝန် အဆင့်ဆင့် လုပ်ဆောင်ခြင်း (ကိရိယာစတင်အပိုင်းများ)](../../../02-prompt-engineering)
  - [ကိုယ်တိုင် သုံးသပ်သော ကုဒ်](../../../02-prompt-engineering)
  - [ဖွဲ့စည်းထားသည့် ချဉ်းကပ်ချက်](../../../02-prompt-engineering)
  - [အကြိမ်ရေ များသော စကားပြော](../../../02-prompt-engineering)
  - [အဆင့်ဆင့် ဟန်ချက်ညီသော စဉ်းစားခြင်း](../../../02-prompt-engineering)
  - [ကန့်သတ်ထားသော အထွက်](../../../02-prompt-engineering)
- [သင် တကယ်မြင်သာလေ့လာနေသည်များ](../../../02-prompt-engineering)
- [နောက်တစ်ဆင့်များ](../../../02-prompt-engineering)

## ဗီဒီယို လမ်းညွှန်

ဤ module ကို စတင်သည့် နည်းလမ်းကို ရှင်းပြသည့် live session ကို ကြည့်ပါ။

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## သင်လေ့လာမည့်အရာများ

အောက်ပါ ပုံက ဤ module တွင် သင် မြင်သာသည့် အဓိက ခေါင်းစဉ်များနှင့် ကျွမ်းကျင်မှုများ၏ ပြည့်စုံ ပြထားသည် - prompt ပြုပြင်မှုနည်းလမ်းများမှ စ၍ သင် လိုက်နာရမည့် အဆင့်ဆင့် လုပ်ငန်းစဉ်အထိ။

<img src="../../../translated_images/my/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

ယခင် modules များတွင် LangChain4j ၏ ဘေစစ် အင်တာလုပ်ရှင်းများကို GitHub Models နှင့် တွဲဖက် လေ့လာပြီး၊ Azure OpenAI ဖြင့် စကားပြော AI ကို ထောက်ပံ့သော မှတ်ဉာဏ်ကို မြင်တွေ့ခဲ့သည်။ ယခုမှာ သင်မေးမြန်းတဲ့ ပြဿနာများ --- prompt များကို Azure OpenAI ၏ GPT-5.2 နှင့် ဘယ်လို တင်သွင်းမလဲတွင် အာရုံစိုက်မည်။ သင်၏ prompt ဖွဲ့စည်းပုံသည် မျှော်မှန်းထားသော အဖြေ၏ အရည်အသွေးကို ထိခိုက်စေသည်။ အခြေခံ prompting နည်းလမ်းများကို ပြန်လည်ဆွေးနွေးပြီးနောက် GPT-5.2 ၏ ချောမွေ့မှုများကို အသုံးချအသေးစိတ် ဖော်ပြသည့် အဆင့်မြင့် ပုံစံ အရှစ်ခုသို့ ရောက်ရှိမည်။

GPT-5.2 ကို အသုံးပြုသည့်အကြောင်းက reasoning control ကို မိတ်ဆက်ပေးခြင်းဖြစ်ပြီး model ကို စဉ်းစားမှု အဆင့်ကို ထိန်းချုပ်နိုင်ပါသည်။  ၎င်းသည် အမျိုးမျိုးသော prompting များကို သက်ရောက်မှုကို ကြည်နူးစေပြီး ဘယ်အချိန် မည်သည့်နည်းလမ်းကို အသုံးပြုသင့်ကြောင်း နားလည် မိစေပါသည်။ GitHub Models နှင့် နှိုင်းယှဉ်လျှင် Azure ၏ GPT-5.2 အတွက် ကန့်သတ်ချက် နည်းပါးမှုများမှ အကျိုးတိုးရရှိမည်။

## လိုအပ်သောအကြောင်းအရာများ

- Module 01 ပြီးစီးထားခြင်း (Azure OpenAI အရင်းအမြစ်များ တပ်ဆင်ထားသည်)
- `.env` ဖိုင်ကို  root directory တွင် Azure အတည်ပြုချက်များဖြင့် စီစဉ်ထားခြင်း (`azd up` ကဲ့သို့ Module 01 တွင် ဖန်တီးထားသည်)

> **မှတ်ချက်:** Module 01 ကို မပြီးစီးသေးပါက အရင်ဆုံး ၎င်းအပိုင်းတွင် ဖော်ပြထားသည့် တပ်ဆင်ခြင်း လမ်းညွှန်ချက်များကို လိုက်နာပါ။

## Prompt Engineering ကို နားလည်ခြင်း

Prompt engineering သည် ရှင်းလင်းမှု မရှိသော အမှာစကားနှင့် တိကျသည့် အမှာစကားတို့အကြား ခြားနားချက် ဖြစ်ပြီး အောက်ပါ ဥပမာက ဖြင့် ရှင်းပြထားသည်။

<img src="../../../translated_images/my/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering ဆိုသည်မှာ သင်လိုအပ်သော အဖြေများကို တိတိကျကျ ရရှိစေရန် ထည့်သွင်းသော စာသားကို ဒီဇိုင်းဆွဲခြင်းဖြစ်သည်။ မေးခွန်းမေးခြင်း မဟုတ်ဘဲ မော်ဒယ်က သင့်လိုချင်သည့်အရာကို တိတိကျကျ နားလည်ပြီး ဘယ်လို ဖြေဆိုရမည်ကို ဆောင်ရွက်ရန် ကြိုးပမ်းခြင်းဖြစ်သည်။

၎င်းက ကိုယ်စားလှယ်း တစ်ဦးအား ညွှန်ကြားချက် ပေးသလိုဖြစ်သည်။ "ဘတ်တွေကို ပြင်ပါ" ဆိုသည်မှာ မရှင်းလင်းပေ။ "UserService.java ၏ ၄၅ လိုင်းမှ null pointer exception ကို null check ထည့်ပြီး ပြင်ပါ" ဆိုသည်မှာ တိတိကျကျ ဖြစ်သည်။ ဘာသာစကား မော်ဒယ်များကိုလည်း အတူတူပါပဲ - တိတိကျကျ ဖြစ်ရမည်။

အောက်ပါတို့ ပုံက LangChain4j ကို ပုံမှာ ဘယ်လို သွင်ပြင်မှုရှိသနည်း ဆိုတာကို ပြသသည် - SystemMessage နှင့် UserMessage စိတ်ကြိုက် block များဖြင့် prompt အား မော်ဒယ်သို့ ချိတ်ဆက်ပေးခြင်း။

<img src="../../../translated_images/my/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j သည် အခြေခံ အဆောက်အအုံ (model များချိတ်ဆက်ခြင်း၊ မှတ်ဉာဏ်၊ နှင့် စာတိုက်ပို့နည်းများ) ပံ့ပိုးပေးပြီး prompt ပုံစံများဟာ တိတိကျကျဖွဲ့စည်းထားသော စာသားများဖြစ်ပြီး လမ်းကြောင်းအတိုင်းပို့ခြင်းဖြစ်သည်။ အဓိက Block များမှာ AI ၏ ပြုမူနှင့် အခန်းကဏ္ဍ သတ်မှတ်သည့် `SystemMessage` နှင့် သင့်တောင်းဆိုချက်ကို သိမ်းဆည်းသည့် `UserMessage` တို့ ဖြစ်သည်။

## Prompt Engineering အခြေခံများ

အောက်တွင် ဖော်ပြထားသော prompt engineering ၏ အဓိကနည်းလမ်းငါးမျိုးသည် ထိရောက်သော prompt ဖွဲ့စည်းမှုပုံစံ၏ အခြေခံဖြစ်သည်။ မည်သည့်နည်းဖြင့် ဘာသာစကား မော်ဒယ်များနှင့် ဆက်သွယ်မလဲဆိုတာအား အကွာအဝေးကွဲပြားသော အချက်များကို ကွက်တိအနှိပ် ဖြေရှင်းပေးသည်။

<img src="../../../translated_images/my/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

ဤ module ၌ အဆင့်မြင့် ပုံစံများသို့ ဝင်ကြမည်မတိုင်မီ အခြေခံ prompting နည်းလမ်းငါးခုကို ပြန်လည်သုံးသပ်ကြပါစို့။ ၎င်းများသည် prompt engineering ၏ အခြေခံ ပလက်ဖောင်းဖြစ်ပြီး prompt engineer တစ်ဦးစီ ကျွမ်းကျင်သင့်သည်။ သင်သည် ပြီးခဲ့သော [Quick Start module](../00-quick-start/README.md#2-prompt-patterns) ကို လေ့လာပြီးဖြစ်လျှင် ဤနည်းလမ်းများ၏ မှန်ကန်မှုကို သဘောပေါက်ထားသည်။

### Zero-Shot Prompting

အလွန်လွယ်ကူသောနည်းလမ်း - တစ်ခုခုနေရာပေးခြင်း မပါဘဲ တိုက်ရိုက် ညွှန်ကြားချက်ပေးခြင်း ဖြစ်သည်။ မော်ဒယ်သည် ရှေ့အစီအစဉ်ကျမှ သင်၏လုပ်ငန်းကို နားလည်ပြီး ဆောင်ရွက်တော့သည်။ ရိုးရှင်းသော တောင်းဆိုမှုများအတွက် အသုံးဝင်သည်။

<img src="../../../translated_images/my/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*နမူနာမပါဘဲ တိုက်ရိုက် ညွှန်ကြားချက်ပေးခြင်း - မော်ဒယ်သည် ညွှန်ကြားချက်မှသာ သတိပြုကာ တာဝန်တင်ယူသည်*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// တုံ့ပြန်ချက်: "အနာဂတ်ချက်"
```
  
**အသုံးပြုသင့်သည့် အချိန်:** ရိုးရာသတ်မှတ်ချက်များ၊ တိုက်ရိုက်မေးခွန်းများ၊ ဘာသာပြန်ခြင်းများ၊ သို့မဟုတ် အပိုအကူအညီမလိုအပ်သော တာဝန်များ။

### Few-Shot Prompting

မော်ဒယ်ကို လိုလားသော ပုံစံကို ပြသသော နမူနာများ ပေးသည်။ မော်ဒယ်သည် သင်ပေးထားသော နမူနာများမှ အသုံးပြုရမည့် input-output ပုံစံကို သိရှိ၍ နောက်ထပ် input များတွင်လည်း သက်ဆိုင်စေရန် အသုံးပြုသည်။ ဒီနည်းလမ်းသည် ပုံစံ ပိုမိုတိကျမှုရှိရာမှာ အထောက်အကူပြုသည်။

<img src="../../../translated_images/my/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*နမူနာများမှ လေ့လာခြင်း - မော်ဒယ်သည် ပုံစံသတ်မှတ်ချက်ကို ဖော်ထုတ်၍ အသစ်များတွင် ကျင့်သုံးသည်*

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
  
**အသုံးပြုသင့်သည့် အချိန်:** ကိုယ်ပိုင်သတ်မှတ်ချက်များ၊ တိုးတက်မြှင့်တင်သော ဖော်မတ်များ၊ ဆိုင်ရာနယ်ပယ်အတွင်းတာဝန်များ၊ Zero-Shot ၏ ရလဒ် မတည်ငြိမ်သော အခါ။

### Chain of Thought

မော်ဒယ်အား တည်ရှိသော အစိတ်အပိုင်းများကို အဆင့်ဆင့်ဖော်ပြတိုက်ရိုက် နည်းလမ်းဖြင့် တင်ပြရန် တောင်းဆိုသည်။ တုံ့ပြန်ချက်ကို တုံ့ပြန်ခြင်းမပြုမီ မြန်မာနိုင်ငံ ရှင်းလင်းမှုနှင့် တိကျမှု ပြုလုပ်သည်။ ဂဏန်း၊ သရုပ်ပြ ရည်ရွယ်ချက်များတွင် ထူးခြားစွာ အသုံးဝင်သည်။

<img src="../../../translated_images/my/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*အဆင့်ဆင့် စဉ်းစားခြင်း - ရှင်းလင်းထားသည့် အချက်အလက်အစိတ်အပိုင်းများကို ခွဲခြား ပြသခြင်း*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// မော်ဒယ်မှာပြပါတယ် - ၁၅ - ၈ = ၇, ထို့နောက် ၇ + ၁၂ = ၁၉ စိပ်ပန်းသီးများ။
```
  
**အသုံးပြုသင့်သည့် အချိန်:** သင်္ချာပြဿနာများ၊ တရားဟောင်း ပဟေဠိများ၊ အမှားရှာဖွေခြင်း၊ သို့မဟုတ် စဉ်းစားမှုကို ထည့်သွင်းရန် လိုသော တာဝန်များတွင်။

### Role-Based Prompting

မေးခွန်းမေးခြင်းမပြုမီ AI အတွက် သရုပ်သမား သတ်မှတ်မှု ပေးသည်။ ၎င်းသည် အကြောင်းအနက်ပြုလုပ်ခြင်း၊ အကြောင်းအရာအခြေခံ အဖြေအား ဖွဲ့စည်းပေးသည်။ "software architect" နှင့် "junior developer" သို့မဟုတ် "security auditor" တို့ ဆွေးနွေးချက် တစ်ခုရဲ့ အတုအယောင် ကွာခြားမှု ရှိသည်။

<img src="../../../translated_images/my/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*အကွက်အတိအကျ သတ်မှတ်ခြင်း - တူညီသောမေးခွန်းက သတ်မှတ်ထားသော အခန်းကဏ္ဍပြီးနောက် မတူညီသော တုံ့ပြန်မှုရရှိသည်*

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
  
**အသုံးပြုသင့်သည့် အချိန်:** ကုဒ် သုံးသပ်ခြင်း၊ သင်ကြားမှု၊ နယ်ပယ်ပေါ် မူတည်သည့် စာရင်းစစ်ခြင်း၊ အသေးစိတ် ဦးစားပေးမှု လိုအပ်သော အခါ။

### Prompt Templates

ပြန်လည် အသုံးပြုနိုင်သော prompt များကို မတူညီသောတန်ဖိုးများ ဖြည့်၍ ဖန်တီးသည်။ တစ်ခါတည်း ဖန်တီးပြီး မတူညီသော တန်ဖိုးများဖြင့် အသုံးပြုလိုသည်။ LangChain4j ၏ `PromptTemplate` အတန်းသည် `{{variable}}` စနစ်ဖြင့် ထိန်းချုပ်ရန် လွယ်ကူစေသည်။

<img src="../../../translated_images/my/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*ပြန်လည် အသုံးပြုနိုင်သော prompt များ - တစ်ခုတည်းသော template၊ အသုံးများစွာ*

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
  
**အသုံးပြုသင့်သည့် အချိန်:** မတူညီသော input များဖြင့် မေးခွန်းများ ထပ်ဖန်တီး၊ စုစည်းဖြတ်တောက်ခြင်းလုပ်ငန်းများ၊ ပြန်လည်အသုံးပြုနိုင်သော AI လုပ်ငန်းစဉ်တည်ဆောက်ရာ၌ တည်ငြိမ်သော ဖွဲ့စည်းပုံပြောင်းလဲခြင်းမရှိသောအခါ။

---

ဤနည်းလမ်းငါးခုသည် prompt များအတွက်  ထိရောက်စွာ အသုံးပြုနိုင်သော ကိရိယာပရိသတ် တစ်ခုကို ပေးသည်။ ကျန်ပိုင်းကို GPT-5.2 ၏ reasoning control၊ ကိုယ်တိုင် သုံးသပ်မှုနှင့် ဖွဲ့စည်းထားသော output ကြောင့် အသုံးချနိုင်သော **အဆင့်မြင့် ပုံစံ အရှစ်ခု** နဲ့ ဆက်လက်တည်ဆောက်မည်။

## အဆင့်မြင့် ပုံစံများ

အခြေခံများ ပြီးဆုံးသွားသောအခါ ဒီ module ကို ထူးခြားတဲ့အရာကြောင့် အဆင့်မြင့် ပုံစံအရှစ်ခုဆီ သွားကြမယ်။ ပြဿနာတိုင်းမှာ နည်းလမ်းတူညီမှု မရှိပါ။ ဒီမေးခွန်းတချို့သည် အမြန်ဖြေကြားမှု လိုအပ်သည်၊ အခြားတချို့က အကြောင်းအရာ တွေးတောမှုမပြတ်ဖို့လိုသည်။ လက်တွေ့ သက်ရှိ reasoning လိုအပ်သောနည်းလမ်းများ၊ တခြား ကိစ္စတွင် တုံ့ပြန်ချက်ချက်ရ ဖြစ်သော်လည်း အဖြေများ လိုအပ်သည်။ အောက်ပါ အတိုင်းပုံစံနှင့် GPT-5.2 ၏ reasoning control ဖြစ်စဉ်ကြောင့် ကွဲပြားချက်များ ပိုသိသာသွားသည်။

<img src="../../../translated_images/my/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*ပုံစံ အရှစ်ခု၏ အနှစ်ချုပ်နှင့် အသုံးပြုမှု*

GPT-5.2 သည် ပုံစံများတွင် အခြား အတိုင်းအတာတစ်ခုဖြစ်သည်။ *reasoning control* ဖြစ်ပြီး စက်ရုပ်၏ စဉ်းစားမှုအား အောက်ပါ slider မှ ဖြင့် ချိန်ညှိနိုင်သည် - မြန်ဆန် တိုက်ရိုက်ဖြေချင်ခြင်းမှ စ၍ နက်နက်ရှိုင်းရှိုင်း စဉ်းစားမှုအထိ။

<img src="../../../translated_images/my/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 ၏ reasoning control သုံး၍ စက်ရုပ် စဉ်းစားမှု အရည်အချင်းကို မြန်မြန် ဆုံဆန်နှင့် နက်နက်ရှိုင်းရှိုင်းအထိ ချိန်ညှိခြင်း*

**နိမ့်သော စိတ်အားထက်သန်မှု (မြန်နှုန်းမြန်ပြီး အာရုံစူးစိုက်မှုနည်း)** - ရိုးရှင်းသောမေးခွန်းများအတွက် မြန်ဆန်၍ တိုက်ရိုက်ဖြေချင်သောအခါ အသုံးပြုရန်။ စက်ရုပ်သည် reasoning ကို အနည်းဆုံး လုပ်ဆောင် - အဆင့် ၂ ထိသာ ဖြစ်သည်။ ကိန်းဂဏန်းတွက်ချက်ခြင်း၊ စစ်ဆေးခြင်း သို့မဟုတ် ရိုးရှင်းသော ပြဿနာများအတွက် သုံးပါ။

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
  
> 💡 **GitHub Copilot ဖြင့် ရှာဖွေကြည့်ပါ:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) ဖိုင်ကို ဖွင့်ပြီး မေးခွန်းများ မေးပါ။
> - "နိမ့်စိတ်အားထက်သန်မှုနဲ့ မြင့်စိတ်အားထက်သန်မှု prompting ပုံစံများကြား ရှားနည်းခြားနားချက် ဘာလဲ?"
> - "Prompt များမှ XML <tags> များက AI ၏ တုံ့ပြန်မှု ဖွဲ့စည်းပုံကို ဘယ်လို ကူညီသလဲ?"
> - "ကိုယ်တိုင် သုံးသပ်မှု pattern များ နဲ့ တိုက်ရိုက် ညွှန်ကြားခြင်း pattern များ ဘယ်အချိန်သုံးသင့်သလဲ?"

**မြင့်သော စိတ်အားထက်သန်မှု (နက်နက်ရှိုင်းရှိုင်း & သေချာသပ်ရပ်စွာ)** - ပြဿနာရှုပ်ထွေးသော အခါ ဝေဖန်စိတ်ရှုမောင်းပြီး ဂရုတစိုက် စိစစ်လိုသောအခါ အသုံးပြုပါ။ စက်ရုပ်သည် အပြည့်အစုံ ဖြေရှင်းမှုနှင့် အသေးစိတ် reasoning ပြသသည်။ စနစ်ဒီဇိုင်း၊ အဆောက်အအုံ ဆုံးဖြတ်ချက်များ၊ ရှုပ်ထွေးသော သုတေသနများအတွက် သင့်တော်သည်။

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```
  
**တာဝန် အဆင့်ဆင့် လုပ်ဆောင်ခြင်း (ခြေလှမ်းအလိုက် တိုးတက်မှု)** - အဆင့်များစွာပါတဲ့ workflow များအတွက်။ စက်ရုပ်သည် အစီအစဉ်တစ်ခုကို ရှေ့တ’avance ပြသပြီး လုပ်ဆောင်ချက် တစ်ခုချင်းစီကို ဖော်ပြခြင်း၊ နောက်ဆုံးတွင် သရုပ်ပြချက် ပေးသည်။ ကျယ်ပြန့်သော ပြောင်းရွှေ့ရေး၊ တည်ဆောက်ခြင်း စနစ်များအတွက် အသုံးပြုသည်။

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
  
Chain-of-Thought prompting သည် မော်ဒယ်ကို reasoning လုပ်ဆောင်ရန် တောင်းဆိုခြင်းဖြစ်ပြီး ရှုပ်ထွေးသော တာဝန်များ၌ တိကျမှန်ကန်မှု ကို တိုးတက်စေသည်။ အဆင့်အလိုက် ပိုင်းခြားထားခြင်းသည် လူနှင့် AI နှစ်ဖက်လုံးအတွက် ရိုးရှင်းစေသည်။

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ကြည့်ပါ။** ဤပုံစံအကြောင်း မေးရန်
> - "ရှည်လျားစွာ ကြာမြင့်သော လုပ်ငန်းဆောင်တာများတွင် task execution pattern ကို မည်သို့ သုံးမည်နည်း?"
> - "ထုတ်လုပ်မှု အပလီကေးရှင်းများတွင် ကိရိယာ preamble များ စနစ်တကျ အဖွဲ့ဖွဲ့ခြင်းအတွက် အကောင်းဆုံး လုပ်နည်းများဘာတွေလဲ?"
> - "UI တွင် အလယ်အလတ် တိုးတက်မှုများကို တွဲဖက် ဖော်ပြရန် မည်သို့ ဖမ်းဆီးထိန်းသိမ်းသနည်း?"

အောက်ပါ ဓာတ်ပုံသည် Plan → Execute → Summarize workflow ကို ရှင်းပြထားသည်။

<img src="../../../translated_images/my/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*အဆင့်များစွာ တာဝန်များအတွက် Plan → Execute → Summarize workflow*

**ကိုယ်တိုင် သုံးသပ်သော ကုဒ်** - ထုတ်လုပ်မှု အရည်အသွေးမြင့် ကုဒ် ဖန်တီးရာတွင် အသုံးပြုသည်။ စက်ရုပ်သည် သင့်တော်သော အမှားအသိပေးများနှင့်အတူ ထုတ်လုပ်မှု စံနှုန်းများနှင့် ကိုက်ညီသည့် ကုဒ် ထုတ်လုပ်သည်။ အသစ်သော features သို့ ဝန်ဆောင်မှုများ တည်ဆောက်ရာ၌ သုံးပါ။

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```
  
အောက်ပါ ဒီဇိုင်းက ဒီ iterative တိုးတက်မှု လည်ပတ်မှု - ဖန်တီးသည်၊ သုံးသပ်သည်၊ အားနည်းချက်များ ရှာဖွေသည်၊ ထပ်တိုးသည် (code ကို ထုတ်လုပ်မှု စံနှုန်းသို့ ညှိညွတ်သည်) ကို ပြသသည်။

<img src="../../../translated_images/my/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*တိုးတက်မှု လည်ပတ်မှု - ဖန်တီး၊ သုံးသပ်၊ ပြဿနာ ရှာဖွေ၊ တိုးတက်*

**ဖွဲ့စည်းထားသည့် ချဉ်းကပ်ချက်** - အတိအကျ သုံးသပ်မှုအတွက်။ စက်ရုပ်သည် ကုဒ်အတွက် တိတိကျကျ ဖော်ကြားချက်များ (မှန်ကန်မှု၊ လေ့ကျင့်မှုများ၊ တုံ့ပြန်မှု၊ လုံခြုံမှု၊ ထိန်းသိမ်းမှု) နဲ့ အတူ ပြန်လည်သုံးသပ်သည်။ ကုဒ် သုံးသပ်မှု သို့မဟုတ် အရည်အသွေး သုံးသပ်ရာတွင် သုံးပါ။

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
  
> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ကြည့်ပါ။** ဖွဲ့စည်းထားသော ချဉ်းကပ်ချက်အကြောင်း မေးရန်
> - "ကုဒ် သုံးသပ်ခြင်း အမျိုးအစားများအတွက် သုံးသပ်မှု ဖွဲ့စည်းပုံကို မည်သို့ ကိုယ်တိုင် သတ်မှတ်နိုင်သနည်း?"
> - "ဖွဲ့စည်းထားသော အထွက် output ကို ပရိုဂရမ်မာတစ်ယောက် အနေဖြင့် အကောင်အထည်ဖော်မှု မြင့်မားစေရန် နည်းလမ်းများ?"
> - "ကွဲပြားသော သုံးသပ်မှု အစည်းအဝေးများတွင် တူညီသော အလေးအနက် အဆင့်ကို မည်သို့ သေချာ ထိန်းသိမ်းနိုင်သနည်း?"

အောက်ပါ ပုံသည် ဖွဲ့စည်းထားသည့် framework ဖြင့် တိကျစွာ ဖြစ်စေရန် ဒါနဲ့ severity အဆင့်များဖြင့် ကုဒ် သုံးသပ်ခြင်းကို စီမံပုံ ကို ဖော်ပြသည်။

<img src="../../../translated_images/my/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*အတိအကျ သုံးသပ်မှုများအတွက် framework နှင့် severity အဆင့်များ*

**အကြိမ်ရေ များသော စကားပြော** - Context များလိုအပ်သော စကားပြော တွေအတွက်။ စက်ရုပ်သည် ယခင်စာတိုက်ပို့မှုများကို မှတ်မိပြီး တိုးချဲ့ပြောဆိုသည်။ ဖော်ပြချက် ကူညီမှု၊ ရှုပ်ထွေးသော Q&A အတွက် သုံးပါ။

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```
  
အောက်ပါ ပုံက စကားပြော context သည် အကြိမ်များစွာ မိတ်ဆက်မှုဖြင့် တိုးတက်လာပြီး model ၏ token ကန့်သတ်မှုနှင့် မည်သို့ ဆက်နွယ်မှု ရှိသနည်းကို ပြသသည်။

<img src="../../../translated_images/my/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*စကားပြော context သည် ကျယ်ပြန့်စွာ ကြာရှည်မှုသို့ တိုးတက်လာသည့် ဂရုစိုက်မှု*
**အဆင့်စီ အတွေးအခေါ်** - မြင်သာတဲ့ လူ့သဘောထား လိုအပ်တဲ့ ပြဿနာများအတွက်။ မော်ဒယ်သည် အဆင့်တိုင်းအတွက် သေချာ လေ့လာရှုမှုပြုသည်။ ဤသည်ကို သင်္ချာပြဿနာများ၊ အတွေးအခေါ် ပဟေဋိများ သို့မဟုတ် စဉ်းစားမှု လုပ်ပုံကို နားလည်ရန် အသုံးပြုပါ။

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```
  
အောက်ပါ ပုံက မော်ဒယ်သည် ပြဿနာများကို သေချာ၍ နံပါတ်စဉ်တိကျသော အဖြေရှာချက် အဆင့်များအဖြစ် မျှဝေသည့် မျက်နှာပြင်ကို ဖော်ပြသည်။

<img src="../../../translated_images/my/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*ပြဿနာများကို သေချာသော အတွေးအခေါ်ခြေလှမ်းများသို့ ခွဲထုတ်ခြင်း*

**ကန့်သတ်ထားသော အထွက်** - အကြောင်းအရာသတ်မှတ်ချက်နှင့် အရွယ်အစားလိုအပ်ချက်ရှိသော တုံ့ပြန်ချက်များအတွက်။ မော်ဒယ်သည် ပုံစံနှင့် အရွယ်အစား စည်းကမ်းများကို တိကျစွာလိုက်နာသည်။ အကျဉ်းချုပ်များ သို့မဟုတ် တိကျသော အထွက်ဖွဲ့စည်းပုံလိုအပ်သော စဉ်ဆက် ရေးရာတွင် အသုံးပြုပါ။

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
  
အောက်ပါ ပုံက ကန့်သတ်ချက်များက မော်ဒယ်အား သင့်ဖွဲ့စည်းပုံနှင့် အရွယ်အစား အတက်လျော်အောင် ထုတ်လုပ်ရန် ဦးတည်ပေးသည့် ပုံစံကို ပြသည်။

<img src="../../../translated_images/my/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*ပုံစံ၊ အရွယ်အစား၊ ဖွဲ့စည်းမှုလိုအပ်ချက်များကို ကန့်သတ်အားပေးခြင်း*

## အပလီကေးရှင်း စတင်မယ်

**ဖြန့်ဖြူးမှုကို အတည်ပြုပါ။**

`.env` ဖိုင်သည် အမျိုးအစားတစ်ခုအနေဖြင့် မူလညွှန်ကြားထားသည့် ဒါရိုက်ထရီအတွင်း၊ Azure အတည်ပြုချက်များနှင့် တည်ရှိမှုရှိကြောင်း သေချာပါစေ (Module 01 တြင် ဖန်တီးထားသည်)။ ဤတွင် မော်ဒယ် ဒါရိုက်ထရီ (`02-prompt-engineering/`) မှ စတင်ဆောင်ရွက်ပါ။

**Bash:**  
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကို ပြရန်ဖြစ်သည်။
```
  
**PowerShell:**  
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကို ပြသသင့်သည်။
```
  
**အပလီကေးရှင်းကို စတင်ပါ။**

> **မှတ်ချက်။** မူရင်း ဒါရိုက်ထရီမှ `./start-all.sh` အသုံးပြုကာ အပလီကေးရှင်းအားလုံးကို ရှေ့မှ စတင်ထားပြီးဖြစ်ပါက (Module 01 တွင် ဖော်ပြထားသလို) ဤမော်ဒယ်သည် 8083 ဆိပ်ကမ်းတွင် လည်ပတ်နေပါပြီ။ အောက်တွင် ဖော်ပြထားသည့် စတင်ရန် မှတ်တမ်းကို ကျော်သွားကာ တိုက်ရိုက် http://localhost:8083 သို့ သွားပါ။

**ရွေးချယ်စရာ ၁: Spring Boot Dashboard အသုံးပြုခြင်း (VS Code အသုံးပြုသူများအတွက် အကြံပြုသည်)**

Dev container တွင် Spring Boot Dashboard extension ပါရှိပြီး ယင်းက Spring Boot အပလီကေးရှင်းများအားလုံးကို မျက်နှာကြောင်းဖြင့် စီမံခန့်ခွဲရန် အဆင့်မြှင့် အင်တာဖေ့စ် ပေးသည်။ VS Code ဘယ်ဘက်က Activity Bar တွင် (Spring Boot အိုင်ကွန်ကို ကြည့်ပါ) တွေ့နိုင်သည်။

Spring Boot Dashboard မှ အောက်ပါအရာများကို ပြုလုပ်နိုင်ပါသည် -  
- စာကြည့်တိုက်အတွင်းရှိ Spring Boot အပလီကေးရှင်းများအားလုံးကို ကြည့်ရှုနိုင်သည်  
- အသုံးပြုသူတစ်ဦးတည်း နှိပ်ခြင်းဖြင့် အပလီကေးရှင်းများကို စတင်/ရပ်ဆိုင်းနိုင်သည်  
- အပလီကေးရှင်းမှတ်တမ်းများကို တိုက်ရိုက် ကြည့်ရှုနိုင်သည်  
- အပလီကေးရှင်း အခြေအနေများကို စောင့်ကြည့်နိုင်သည်  

"prompt-engineering" ထံတွင်ရှိသော play ခလုတ်ကို နှိပ်ကာ မော်ဒယ်ကို စတင်ပါ၊ ဒါမှမဟုတ် အားလုံးကို တစ်ပြိုင်တည်း စတင်နိုင်ပါသည်။

<img src="../../../translated_images/my/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code တွင် Spring Boot Dashboard — မော်ဒယ်အားလုံးကို တစ်နေရာက စတင်၊ ရပ်ဆိုင်း၊ စောင့်ကြည့်နိုင်သည်*

**ရွေးချယ်စရာ ၂: Shell script များ အသုံးပြုခြင်း**

ဝက်ဘ်အပလီကေးရှင်းများအားလုံး (Modules 01-04) ကို စတင်ပါ -

**Bash:**  
```bash
cd ..  # အမြစ်ဖိုင်တိုက်မှနေ၍
./start-all.sh
```
  
**PowerShell:**  
```powershell
cd ..  # အမြစ်ဖိုင်လ်ဒ်တွင်းမှ
.\start-all.ps1
```
  
သို့မဟုတ် ဤ မော်ဒယ်ကို သီးသန့် စတင်ပါ -

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
  
ကလစ်တိုင်းမှာ Root `.env` ဖိုင်မှ ပတ်ဝန်းကျင် အပြောင်းအလဲများကို အလိုအလျောက် Load လုပ်ပြီး JAR မရှိလျှင် ကောက်နှိပ်ပေးပါမည်။

> **မှတ်ချက်။** စတင်မပြုမီ မော်ဒယ်အားလုံးကို လက်စွဲ အသုံးပြုကောက်နှိပ်လိုပါက -  
>
> **Bash:**  
> ```bash
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
> **PowerShell:**  
> ```powershell
> cd ..  # Go to root directory
> mvn clean package -DskipTests
> ```
  
http://localhost:8083 ကို ဘရောက်ဇာတွင် ဖွင့်ပါ။

**ရပ်ရန် -**

**Bash:**  
```bash
./stop.sh  # ဤမော်ဂျုမ်းတစ်ခုသာ
# သို့မဟုတ်
cd .. && ./stop-all.sh  # မော်ဂျုမ်းအားလုံး
```
  
**PowerShell:**  
```powershell
.\stop.ps1  # ဒီမော်ဂျူးသာ
# ဒါမှမဟုတ်
cd ..; .\stop-all.ps1  # မော်ဂျူးအားလုံး
```
  
## အပလီကေးရှင်း မျက်နှာကြည့်ကွက်များ

ဒီမှာ prompt engineering မော်ဒယ်၏ ပင်မ အင်တာဖေ့စ်ဖြစ်ပြီး Pattern ၈ မျိုးကို တစ်ပြိုင်နက်တည်း စမ်းသပ်နိုင်သည်။

<img src="../../../translated_images/my/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*prompt engineering pattern အားလုံး၊ သူတို့၏ အင်္ဂါရပ်များနှင့် အသုံးချမှုကို ပြသသည့် ပင်မ Dashboard*

## Pattern များ ကို စူးစမ်းဖြေရှင်းခြင်း

ဝက်ဘ်အင်တာဖေ့စ်က တိုက်ရိုက် ဖေါ်ပြချက် များကို စမ်းသပ်ခွင့်ပြုသည်။ Pattern တစ်ခုခြင်းသည် မတူညီသော ပြဿနာများကို ဖြေရှင်းသည် အသုံးပြုကြည့်ပါ၊ ဘယ်အခါ pattern တစ်ခု ဖြစ်တည်မှုအား ရယူမည်ကို သေချာနားမလည်နိုင်ပါ။

> **မှတ်ချက်။ Streaming နှင့် Non-Streaming** — Pattern များ စာမျက်နှာ တစ်ခုရဲ့ နောက်ဆုံးမှာ နှစ်ချက်ခလုတ်များရှိသည်။ **🔴 Stream Response (Live)** နှင့် **Non-streaming** ရွေးချယ်စရာ။ Streaming သည် Server-Sent Events (SSE) အသုံးပြုကာ မော်ဒယ် ဖန်တီးနေစဉ် တိုက်ရိုက် token များကို ကြည့်ရှုနိုင်သည်။ Non-streaming သည် တုံ့ပြန်ချက် ပြည့်စုံမှ ကြည့်ရှုရန် စောင့်ဆိုင်းလျက်ရှိသည်။ အတွေးပညာ အတက်လျော်များအတွက် (ဥပမာ - High Eagerness, Self-Reflecting Code) Non-streaming ကို ခါတလေ အချိန်ကြာမြင့်နိုင်ပြီး မည်သည့် တုံ့ပြန်မှုမှ မကြည့်ရနိုင်ပါ။ **ရှုပ်ထွေးသော prompt များတွင် streaming ကို အသုံးပြုပါ**၊ မော်ဒယ် လုပ်ဆောင်နေပြီးကြောင်း ကို မြင်နိုင်ပြီး တောင်းဆိုချက် တားကွယ်ခံရသည်ဟု ထင်မှားမှုကို လျော့နည်းစေပါသည်။  
>  
> **မှတ်ချက်။ ဘရောက်ဇာ လိုအပ်ချက်** — Streaming ဟာ Fetch Streams API (`response.body.getReader()`) ကိုအသုံးပြုသည်၊ ဤသည်သည် Chrome, Edge, Firefox, Safari ကဲ့သို့ ပြည့်စုံသော ဘရောက်ဇာလိုအပ်သည်။ VS Code built-in Simple Browser တွင် မလုပ်ဆောင်ပါခြင်း၊ ၎င်း၏ webview တွင် ReadableStream API အတွက် မပံ့ပိုးပါ။ Simple Browser အသုံးပြုလျှင် Non-streaming ခလုတ်များ ပုံမှန် လုပ်ဆောင်သော်လည်း Streaming ခလုတ်တွေ အလုပ်မလုပ်ပါ။ ပြည့်စုံသော အတွင်းခံအတွေ့အကြုံအတွက် `http://localhost:8083` ကို ပြင်ပ ဘရောက်ဇာတွေတွင် ဖွင့်ပါ။  

### အနိမ့် နှင့် အမြင့် စိတ်အားထက်သန်မှု (Eagerness)

"200 ၏ 15% ဘာလဲ?" ဆိုပြီး အနိမ့် စိတ်အားထက်သန်မှု ဖြင့် မေးပါ။ အလျင်အမြန် တိုတိုတောင်းသော တုံ့ပြန်ချက်ကို ရယူမည်။ အခုတော့ "အမြင့် လှုပ်ရှားမှု API အတွက် cache မဟာဗျူဟာ တစ်ခု အပြည့်အစုံ အကြံပြုပါ" ကို အမြင့် စိတ်အားထက်သန်မှုဖြင့် မေးပါ။ **🔴 Stream Response (Live)** ခလုတ်ကို နှိပ်ပြီး မော်ဒယ်၏ အသေးစိတ် ဆန်းစစ်ချက်များကို token တစ်ခုချင်း ပြသမှုဖြင့်ကြည့်ပါ။ မော်ဒယ်တစ်ခုတည်း၊ မေးခွန်းမျိုးနှုန်း တူညီသော်လည်း နောက်ခံ prompt က ဟောရန် စိတ်အတက်အကျကို သတ်မှတ်ပေးသည်။

### တာဝန် ဆောင်ရွက်ခြင်း (ကိရိယာ မူရင်းများ)

အဆင့်စဉ်များ ပါဝင်သည့် စီးဆင်းမှုများသည် မျှော်မှန်းချက် နှင့် တိုးတက်မှု ရေးရာ ပြောပြချက်များ ပါဝင်သင့်သည်။ မော်ဒယ်သည် ဘာလုပ်မည်ကို မရှင်းလင်း၍ ပြောကြားပြီး၊ အဆင့်တိုင်းကို ဖော်ပြ၊ နောက်ဆုံးတွင် ရလဒ် အကျဉ်းချုပ် ထုတ်ပြန်သည်။

### ကိုယ်တိုင် သုံးသပ်ခြင်း(Coding)

"အီးမေးလ် မှားချက်သတ်မှတ်သူ ဝန်ဆောင်မှု တည်ဆောက်ပါ" ဟုပြောရင်း စမ်းကြည့်ပါ။ ကုဒ် ဖန်တီးခြင်းဖြင့် ပြတ်တောက်ခြင်း မဟုတ်ပဲ မော်ဒယ်သည် ဖန်တီးပြီးနောက် အရည်အသွေး စံများနှင့် နှိုင်းယှဥ်၊ အားနည်းချက်များ ရှာဖွေပြီး တိုးတက်အောင် လုပ်ဆောင်သည်။ မော်ဒယ်သည် အင်မတန် iteration ဖြင့် ကုဒ်သည် ထုတ်လုပ်မှု စံနှင့် ချိန်ညှိသည်ထိ လုပ်ဆောင်သည်။

### ဖွဲ့စည်းမှုပုံစံ သုံးသပ်ခြင်း

ကုဒ် ပြန်လည်စစ်ဆေးခြင်းသည် တူညီသော သုံးသပ်မှုဖြစ်ရမည်။ မော်ဒယ်သည် ကုဒ်အား တိတိကျကျ အမျိုးအစားများဖြင့် ဆန်းစစ်သည် (တိကျမှု၊ လေ့လာမှု၊ စွမ်းဆောင်မှု၊ လုံခြုံမှု) နှင့် ပြင်းထန်မှု အဆင့်များဖြင့် သုံးသပ်သည်။

### Multi-Turn စကားပြောခြင်း

"Spring Boot ဆိုတာ ဘာလဲ?" ဟုပြောပြီးနောက် "ဥပမာ တစ်ခု ပြပါ" ဟု ချက်ချင်း မေးပါ။ မော်ဒယ်သည် သင်မေးခွန်း ပထမအကြိမ်ကို သတိရပြီး Spring Boot ပုံစံကို တိတိကျကျ ပေးပါတယ်။ မှတ်ဉာဏ်မရှိလျှင် ဒုတိယ မေးခွန်းသည် မသေချာသည့် စကားလုံးဖြစ်မည်။

### အဆင့်စီ အတွေးအခေါ်

သင်္ချာပြဿနာတစ်ခု ချမ်းသာစွာ ဖော်ပြခြင်းနှင့်အတူ အဆင့်စီအတွေးအခေါ်နှင့် အနိမ့်စိတ်အားထက်သန်မှု နှစ်မျိုးစမ်းပါ။ အနိမ့် စိတ်အားထက်သန်မှုသည် အဖြေကို ဆုံးဖြတ်ပြီး ပေးသည် - မြန်ဆန်ပေမယ့် ရှင်းလင်းမှုမရှိ။ အဆင့်စီသည် ဂဏန်းတွက်ချက်မှုနှင့် ဆုံးဖြတ်ချက်အားလုံးကို ပြသကာ တိကျစွာ သိရှိစေသည်။

### ကန့်သတ်ထားသော အထွက်

သတ်မှတ်ထားသော ပုံစံများ သို့မဟုတ် စာလုံး အရေအတွက်လိုအပ်သောအခါ ဤ pattern သည် စည်းကမ်းများကို တင်းကျပ်စွာ လိုက်နာရန် ကြိုးပမ်းသည်။ အစီအစဉ်တစ်ခုမှာ ပြီးပြည့်စုံသော စာလုံး ၁၀၀ ကို bullet point ပုံစံဖြင့် ထုတ်ပေးရန် မျှော်လင့်ပါ။

## သင် သင်ယူနေတာ က ဘာလဲ?

**အတွေးပညာ ကြိုးပမ်းမှု တစ်ခုလုံးကို ပြောင်းလဲသည်**

GPT-5.2 သည် သင်၏ prompt များမှတဆင့် ကွန်ပြူတာ ကြိုးပမ်းမှုကို ထိန်းချုပ်နိုင်စေသည်။ အနိမ့် ကြိုးပမ်းမှုသည် အမြန်တုံ့ပြန်မှုဖြင့် အနည်းငယ် စူးစမ်းသုံးသပ်မှုသာ ပါဝင်သည်။ အမြင့် ကြိုးပမ်းမှုသည် မော်ဒယ်ကို နက်ရှိုင်းစွာ စဉ်းစားရန် အချိန်ယူစေသည်။ သင်သည် တာဝန်၏ ပြဿနာရှင်းလင်းမှုအပေါ် မျှတသည့် ကြိုးပမ်းမှုနှင့် ကိုက်ညီတတ်တော့မည် - ရိုးရိုး မေးခွန်းများကို အချိန်ဖြုန်းမထား၊ ရှုပ်ထွေးသော ဆုံးဖြတ်ချက်များကို ကာလမတိုအောင် မျာဖြတ်။

**ဖွဲ့စည်းမှုသည် လုပ်ဆောင်မှုကို ဦးတည်သည်**

Prompt များရှိ XML tag များကို သတိပြုပါ။ ၎င်းတို့သည် အလှအပ မဟုတ်ပါ။ မော်ဒယ်များသည် ဖွဲ့စည်းမှုရှိသော အညွှန်းများကို လွယ်ကူစွာ လိုက်နာနိုင်ကြသည်။ အဆင့်စီလုပ်ငန်းစဉ်များ သို့မဟုတ် ရှုပ်ထွေးသော သဘောထားများ လိုအပ်လျှင် ဖွဲ့စည်းမှုက မော်ဒယ်ကို ၎င်း၏ မည်သည့်အဆင့်တွင် ရှိသည်နဲ့ မည်သည့်အဆင့်သို့ သွားမည်ကို သေချာ ပြီးမြောက်စေသည်။ အောက်ပါ ပုံက အဆင့်မြင့် ဖွဲ့စည်းထားသော prompt တစ်ခုကို မျက်နှာပြထားပြီး `<system>`, `<instructions>`, `<context>`, `<user-input>`, နှင့် `<constraints>` ကဲ့သို့ XML ပုံစံ tag များဖြင့် သင်၏ အညွှန်းများကို ပိုမိုရှင်းလင်းသော အပိုင်းများသို့ ဖွဲ့စည်းပုံကို ပြသည်။

<img src="../../../translated_images/my/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*ဖွဲ့စည်းမှုတိကျသည့် prompt ၏ ခေါင်းစဉ်များနှင့် XML ပုံစံအဖွဲ့အစည်း*

**ကိုယ်တိုင် သုံးသပ်ခြင်းမှ အရည်အသွေး**

ကိုယ်တိုင် သုံးသပ်ခြင်းပုံစံများသည် အရည်အသွေး စံနှုန်းများကို တိတိကျကျ ဖော်ပြထားသည်။ မော်ဒယ်သည် "မှန်ကန်သည်" ဟု မျှော်လင့်ခြင်းမပြုဘဲ၊ "မှန်ကန်ခြင်း" ဆိုသည်မှာ မည်သည်ကို ရည်ညွှန်းသနည်း (တိကျသော စဉ်းစားမှု၊ အမှား ကျော်လွှားမှု၊ စွမ်းဆောင်မှု၊ လုံခြုံမှု) ကို တိကျစွာ ပြောပြသည်။ ထို့နောက် မော်ဒယ်သည် ၎င်း၏ ထုတ်လွှတ်ချက်ကို သုံးသပ်ပြီး တိုးတက်စေသွားသည်။ ၎င်းသည် ကုဒ် ဖန်တီးမှုကို လောင်းကစားကွင်းမှ လုပ်ငန်းစဉ် တစ်ခုသို့ ပြောင်းလဲစေသည်။

**အခြေအနေ သတ်မှတ်ချက်သည် အကန့်အသတ် ရှိသည်**

Multi-turn စကားဝိုင်းများသည် တောင်းဆိုချက်တစ်ခုစီနှင့် စကားမှတ်တမ်း သမိုင်းကို ပါဝင်ပေးမှုကြောင့် လုပ်ဆောင်သည်။ သို့သော် အကန့်အသတ် ရှိသည် - မော်ဒယ်တစ်ခုလုံးတွင် token အများဆုံး ရှိနိုင်မှု ဖြစ်သည်။ စကားနှင့် ပြည့်စုံမှု မြင့်မားလာသည့်အခါ လိုအပ်သော အကြောင်းအရာကို သိမ်းဆည်းထားခြင်းနှင့် အကန့်အသတ်မထိခိုက်စေရန် မူဝါဒများ လိုအပ်ပါသည်။ ဤမော်ဒယ်သည် သင်အား မှတ်ဉာဏ် အလုပ်လုပ်ပုံကို ပြသည်၊ နောက်ဆုံးတွင် ရှင်းလင်းချက် မည်သည့်အခါ၊ မေ့တမ်းမည်၊ ထုတ်ယူမည်ကို သင် သင်ယူမည်ဖြစ်သည်။

## နောက်တစ်ဆင့်များ

**နောက်တစ်ပြည့် Modules:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**လမ်းညွှန်မှု:** [← နောက်တစ်ခု: Module 01 - အနှောက်အယှက်ကင်း ရောက်ရှိမှု](../01-introduction/README.md) | [ပင်မသို့ ပြန်သွားရန်](../README.md) | [နောက်တစ်ခု: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ထုတ်ပြန်ချက်**  
ဤစာတမ်းကို AI ဘာသာပြန်မှု 서비스ဖြစ်သည့် [Co-op Translator](https://github.com/Azure/co-op-translator) ဖြင့် ဘာသာပြန်ထားပါသည်။ တိကျမှုအတွက် ကြိုးစားနေပြီးဖြစ်သော်လည်း၊ အလိုအလျောက် ဘာသာပြန်မှုများတွင် အမှားများ သို့မဟုတ် မှန်ကန်မှုနည်းပါးမှုများ ရှိနိုင်သည်ကို သတိပြုပါရန် တိုက်တွန်းပါသည်။ မူလစာတမ်းကို မူရင်းဘာသာစကားဖြင့်သာ တရားဝင်အမြင်ခံယူသင့်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက်မှ ဖြစ်ပါက ပညာရှင်လက်ထောက် ဘာသာပြန်သူ၏ ဘာသာပြန်ချက်ကိုသာ အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြု၍ ဖြစ်ပေါ်လာနိုင်သည့် နားမြင်မှု ခြွင်းချက်များ သို့မဟုတ် မှားယွင်းချက်များအတွက် ကျွန်ုပ်တို့ ဘာသာမပြုပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->