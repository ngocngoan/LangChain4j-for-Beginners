# Module 01: LangChain4j နဲ့ စတင်သုံးစွဲခြင်း

## အကြောင်းအရာ အပေါ်လွှာ

- [ဗီဒီယို လမ်းညွှန်](../../../01-introduction)
- [သင်ယူရမယ့်အရာများ](../../../01-introduction)
- [လိုအပ်ချက်များ](../../../01-introduction)
- [အဓိက ပြဿနာကို နားလည်ခြင်း](../../../01-introduction)
- [တိုးကင်များကို နားလည်ခြင်း](../../../01-introduction)
- [မှတ်ဉာဏ် ဘယ်လို လည်ပတ်သလဲ](../../../01-introduction)
- [ဒီအရာက LangChain4j ကို ဘယ်လို အသုံးပြုလဲ](../../../01-introduction)
- [Azure OpenAI အဆောက်အအုံ တပ်ဆင်ခြင်း](../../../01-introduction)
- [အက်ပ်ကို ဒေသခံမှာ လည်ပတ်ခြင်း](../../../01-introduction)
- [အက်ပ်ကို အသုံးပြုခြင်း](../../../01-introduction)
  - [Stateless Chat (ဝိဘ်နား ဘောင်အက်ဘက်)](../../../01-introduction)
  - [Stateful Chat (ညာဘောင်)](../../../01-introduction)
- [နောက်ဆက်တွဲ လုပ်ဆောင်ရန်](../../../01-introduction)

## ဗီဒီယို လမ်းညွှန်

ဤမော်ဂျူးကို စတင်သုံးစွဲမည်ကိုရှင်းပြသည့် တိုက်ရိုက်အစီအစဉ်ကို ကြည့်ရှုပါ-

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## သင်ယူရမယ့်အရာများ

Quick start မှပြီးမြောက်ပြီးသားဆိုရင် စေနေ၊ ပြန်လည်ထုတ်ပေးခြင်းနဲ့ prompt ပို့နည်းကို ကြည့်ရှုဖူးပါပြီ။ ဒါက အခြေခံပုံစံပါ၊ သို့သော် လက်တွေ့ အသုံးပြုမှုတွေအတွက်ပိုတာတွေလိုအပ်ပါတယ်။ ဤမော်ဂျူးအတွင်းမှာ သင်တွေ့ကြုံရမယ့် ကောလဟာလစနစ်တစ်ခုကို တည်ဆောက်နည်းသင်ကြားပေးမှာဖြစ်ပြီး၊ context ကို မှတ်တမ်းတင်ပြီး state ကို ထိန်းသိမ်းနိုင်မယ် - တစ်ကြိမ်တည်း ပြသမှုနဲ့ ထုတ်လုပ်မှုအသုံးပြုမှုကြားက ဆွဲဆောင်ချက်ကွဲပြားခြားနားမှု။

ဒီလမ်းညွှန်မှာ Azure OpenAI ၏ GPT-5.2 ကို ဆက်တိုက်အသုံးပြုမယ်။ ၎င်းရဲ့ များပြားသည့် စဉ်းစားနိုင်မှုများကြောင့် ဤပုံစံမတူ အပြုအမူများထဲက မတူညီမှုတွေ ပိုမြင်သာမယ်။ မှတ်ဉာဏ်အသုံးပြုရင် ကွာခြားချက်တွေ ပိုရှင်းလင်းစွာမြင်နိုင်သည်။ ဒါက သင့်အက်ပ်အတွက် အစိတ်အပိုင်းတစ်ခုချင်းစီ ဘာတွေလုပ်တယ်ဆိုတာ နားလည်ရလွယ်ကူစေပါတယ်။

ဒီပုံစံ နှစ်မျိုးကို ပြသသည့် အက်ပ်တစ်ခုတည်ဆောက်မှာ ဖြစ်ပါတယ်။

**Stateless Chat** - မည်သည့်တောင်းဆိုမှုမျိုးနှင့်မဆို လွတ်လပ်သော တစ်ခုချင်း request ဖြစ်သည်။ မော်ဒယ်က အရင်ပိုင်းအကြောင်းအရာများကို မှတ်မထားပါဘူး။ Quick start မှာသုံးခဲ့တဲ့ ပုံစံဖြစ်ပါတယ်။

**Stateful Conversation** - တောင်းဆိုချက်တိုင်းမှာ စကားပြောအတိတ်မှတ်တမ်းပါရှိသည်။ မော်ဒယ်က အကျဉ်းချုပ် context ကို ပြန်လည်သိမ်းဆည်းထားသည်။ ဒီပုံစံက ထုတ်လုပ်မှုအသုံးပြုမှုတွေအတွက်လိုအပ်သည်။

## လိုအပ်ချက်များ

- Azure subscription ဖြင့် Azure OpenAI အကောင့်ရှိမှု
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **မှတ်ချက်:** Java, Maven, Azure CLI နှင့် Azure Developer CLI (azd) များကို ပေးထားသည့် devcontainer တွင် အလိုအလျောက် ထည့်သွင်းထားပါသည်။

> **မှတ်ချက်:** ဤမော်ဂျူးသည် Azure OpenAI တွင် GPT-5.2 ကို အသုံးပြုသည်။ `azd up` ဖြင့် deployment ကို အလိုအလျောက် ပြုလုပ်သည့်ကြောင့် ကိုးဒ်ထဲရှိ မော်ဒယ်အမည်ကို မပြင်ဆင်ရပါ။

## အဓိက ပြဿနာကို နားလည်ခြင်း

ဘာသာစကားမော်ဒယ်များသည် stateless ဖြစ်သည်။ တစ်ခုချင်း API ခေါ်ဆိုမှုတို့သည် လွတ်လပ်ခြင်းသာရှိကြသည်။ "ကျွန်ုပ်နာမည်က John ဖြစ်တယ်" ဟုပြောပြီး "ကျွန်ုပ်နာမည် ဘာလဲ?" ဟုမေးလျှင် မော်ဒယ်က ကိုယ့်နာမည်ပြောပြတာကို မသိပါဘူး။ မည်သည့်တောင်းဆိုမှုမျိုးကိုမဆို ပထမဆုံး စကားပြောချက်ပုံစံဖြင့် သွယ်ဝိုက်ပါတယ်။

ရိုးရှင်းသော Q&A အတွက်တော့ ကောင်းပါတယ်၊ သို့သော် လက်တွေ့အသုံးပြုမှုများအတွက် မထိရောက်ပါ။ အထောက်အကူဖြစ်ရန် ကုန်သည်ဝန်ဆောင်မှုပေးသော bot များ သင် ပြောတာကို မှတ်ရတယ်မှ မဟုတ်ရင် မလုပ်နိုင်ပါ။ ကိုယ်ရေးအကူအညီရှာတဲ့ assistant များအတွက် context က လိုအပ်ပါတယ်။ တစ်ခါထက်ပိုသော စကားပြောဆိုမှုများအတွက် ရှိထားသော မှတ်ဉာဏ် တစ်ခု လိုအပ်ပါတယ်။

<img src="../../../translated_images/my/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Stateless (လွတ်လပ်သော ခေါ်ဆိုမှုများ) နှင့် Stateful (context ရှိသော) စကားပြောဆိုမှုများအကြား ကွာခြားချက်*

## တိုးကင်များကို နားလည်ခြင်း

စကားပြောဆိုမှုများကို တိုက်ရိုက် ဝင်ရောက်မှုမပြုမီ တိုးကင်များကို နားလည်ထားသင့်သည်၊ ၎င်းသည် ဘာသာစကားမော်ဒယ်များ ဖြတ်သန်းတင်ပြသည့် အကြောင်းအရာ အခြေခံယူနစ်များဖြစ်သည်။

<img src="../../../translated_images/my/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*စာသားကို တိုးကင်များသို့ ခွဲခြားမှု ဥပမာ - "I love AI!" သည် processing unit ၄ ခုအဖြစ် ခွဲခြားသည်*

Tokens သည် AI မော်ဒယ်များ စာသားကို အတိုင်းအတာချထားပြီး ခွဲခြား ဖတ်ရှု ထားသည့်ယူနစ်များဖြစ်သည်။ စကားလုံးများ၊ စာလုံးပေါင်းဗျည်း၊ နှင့် သင်္ကေတများ၊ နေရာများ အားလုံးကို တိုးကင်အဖြစ်ယူနိုင်ပါသည်။ မော်ဒယ်မှာ တစ်ကြိမ်လျှင် process လုပ်နိုင်သော တိုးကင် အရေအတွက်ကန့်သတ်ချက် ရှိပါသည် (GPT-5.2 အတွက် 400,000 တိုးကင်၊ input 272,000 နဲ့ output 128,000 တိုးကင်အထိ)။ တိုးကင်များကို နားလည်ခြင်းက စကားပြောရှည်လျားမှုနှင့် ကျသင့်ငွေအတိအကျ စီမံခန့်ခွဲရာမှာ အထောက်အကူဖြစ်ပါတယ်။

## မှတ်ဉာဏ် ဘယ်လို လည်ပတ်သလဲ

Chat မှတ်ဉာဏ်သည် stateless စိတ်ပိုင်းပြဿနာကိုဖယ်ရှားရန် နှုတ်ထွက်မှုကို မှတ်တမ်းတင်ထားသည်။ မော်ဒယ်ထံ request ပေးပို့ရန်မတိုင်မီ ယခင်ဆိုင်းငံ့ခဲ့သော မက်ဆေ့ဂျ်များကို ဘာသာဖြည့် ပေးသည်။ "ကျွန်ုပ်နာမည်ဘာလဲ?" ဟုမေးလျှင် စကားပြောအတိတ် ပေါင်းစပ်ထားသော စကားဝိုင်းများကို ပေးပို့သည်၊ ဒါကြောင့် မော်ဒယ်က "ကျွန်တော်နာမည်က John" ဟုပြောခဲ့တာ ကို မှတ်ထားတယ်။

LangChain4j သည် မှတ်ဉာဏ်ထိန်းသိမ်းမှုများကို အလိုအလျောက် စီမံထိန်းသိမ်းရေး အတွက် ပံ့ပိုးပေးသည်။ မက်ဆေ့ဂျ်များ အရေနှင့် context window ကို ကိုယ်တိုင် ရွေးချယ်နိုင်ပါသည်။

<img src="../../../translated_images/my/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory က နောက်ဆုံးမက်ဆေ့ဂျ်များစေ့စပ်သည့် sliding window ထိန်းသိမ်း၍ အဟောင်းများကို အလိုအလျောက် ဖယ်ရှားပေးသည်*

## ဒီအရာက LangChain4j ကို ဘယ်လို အသုံးပြုလဲ

ဒီမော်ဂျူးက Quick Start ကို တိုးချဲ့ပြီး Spring Boot ကို ပေါင်းစည်းပြီး စကားပြောအတိတ်မှတ်ဉာဏ်ထည့်သွင်းထားပါတယ်။ ပုံစံတွင် အစိတ်အပိုင်းများ ပိုလိုက်ပါသည်-

**ပူးပေါင်းအသုံးပြုရန် သီးခြားများ** - LangChain4j libraries နှစ်ခု ထည့်သွင်းပါ-

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

**Chat မော်ဒယ်** - Azure OpenAI ကို Spring bean အဖြစ် ဖြည့်စွက် ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

Builder သည် ဖော်ပြထားသော environment variables များမှ credentials ကို ဖတ်ယူပါသည်။ `baseUrl` ကို သင့် Azure endpoint ထည့်သွင်းခြင်းဖြင့် OpenAI client ကို Azure OpenAI နှင့် သင့်တော်စေပါသည်။

**စကားပြောအတိတ်မှတ်ဉာဏ်** - MessageWindowChatMemory ဖြင့် စကားပြောသမိုင်းကို သိမ်းဆည်းပါ ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

အများဆုံး 10 မက်ဆေ့ဂျ်ထားရှိရန် `withMaxMessages(10)` ဖြင့် memory ရေးဆွဲပါ။ အသုံးပြုသူနှင့် AI မက်ဆေ့ဂျ် များကို `UserMessage.from(text)` နှင့် `AiMessage.from(text)` ဖြင့် ထည့်သွင်း၍ အသုံးပြုပါ။ history ကို `memory.messages()` ဖြင့် ဖော်ထုတ်ပြီး မော်ဒယ်ထံ တောင်းဆိုပါ။ service က စကားပြော ID တစ်ခုစီအတွက် သီးခြား memory instance များ ထိန်းသိမ်း ရှိနေနိုင်ပြီး၊ အသုံးပြုသူများ များစွာ အပြန်အလှန် စကားပြောနိုင်သည်။

> **🤖 GitHub Copilot Chat ဖြင့် စမ်းသပ်ရန်:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ဖိုင်ကို ဖွင့်၍ မေးမြန်းပါ -
> - "MessageWindowChatMemory က မက်ဆေ့ဂျ်များ ပေါ်လွင်မှာ ဘယ်လိုဆုံးဖြတ်သလဲ၊ သွားရတယ်ဆိုရင်?"
> - "In-memory နေရာမှာ ဒေတာဘေ့စ် ကို အသုံးပြုပြီး ကိုယ်ပိုင် မှတ်ဉာဏ်သိုလှောင်မှု တည်ဆောက်လို့ ရမလား?"
> - "အဟောင်း စကားပြောသမိုင်းကို စုစည်းဖို့ summarization ကို ဘယ်လိုထည့်မလဲ?"

Stateless chat endpoint သည် memory ကို သုံးမည်မဟုတ်ဘဲ `chatModel.chat(prompt)` လို quick start နည်းလမ်းတူ ပြုလုပ်သည်။ Stateful endpoint သည် memory ထဲတွင် မက်ဆေ့ဂျ်များ ထည့်သွင်းထားပြီး history ကို ဖြင့်ထုတ်ကာ တောင်းဆိုမှုတိုင်းကို အတူတကွ ပေးပို့သည်။ မော်ဒယ်ပုံစံတူ၊ ပုံစံကွဲခြား။

## Azure OpenAI အဆောက်အအုံ တပ်ဆင်ခြင်း

**Bash:**
```bash
cd 01-introduction
azd up  # စာရင်းသွင်းခြင်းနှင့် တည်နေရာကို ရွေးချယ်ပါ (eastus2 ကို အကြံပြုသည်)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # စာရင်းပေးသွင်းမှုနှင့်တည်နေရာ (eastus2 ကိုအကြံပြု) ရွေးချယ်ပါ။
```

> **မှတ်ချက်:** timeout error (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) ဖြစ်နိုင်ပါက `azd up` ကို ထပ်မံဖြင့် ရိုက်ထည့်ပါ။ Azure အရင်းအမြစ်များ များစွာ မပြီးစီးသေးဘဲ provision လုပ်နေစဉ်ဖြစ်၍ retry ပြုလုပ်ရာ အောင်မြင်ခြင်း ရှိသည်။

ဤသည်မှာ -
1. Azure OpenAI resource ကို GPT-5.2 နှင့် text-embedding-3-small မော်ဒယ်များဖြင့် တပ်ဆင်သည်
2. project root တွင် `.env` ဖိုင်ကို credentials များဖြင့် အလိုအလျောက် ပြုလုပ်ပေးသည်
3. လိုအပ်သော environment variables အားလုံးကို သတ်မှတ်ပေးသည်

**တပ်ဆင်ရာတွင် ပြဿနာရှိပါသလား?** [Infrastructure README](infra/README.md) တွင် subdomain name conflicts, Azure Portal မှ အလက်မန်တပ်ဆင်နည်းဖော်ပြချက်များ၊ ဖန်တီးရေးဆွဲမှု အကြံပြုချက်များ ပါရှိသည်။

**တပ်ဆင်မှုကွဲပြားမှုကို အတည်ပြုပြီးကြောင်း စစ်ဆေးရန်:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT၊ API_KEY စသည့်အချက်အလက်များကို ပြသသင့်သည်။
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, စသည်တို့ကို ပြသသင့်သည်။
```

> **မှတ်ချက်:** `azd up` က `.env` ဖိုင်ကို အလိုအလျောက် ဖန်တီးပေးသည်။ နောက်ခေါက် ပြင်ဆင်လိုပါက မိမိဖြစ်သော `.env` ဖိုင်ကို အတူတူ သို့မဟုတ် တစ်ခုသစ်ဖန်တီးနိုင်ရန် အောက်ပါပုံစံဖြင့် လုပ်ဆောင်နိုင်သည် -
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```


## အက်ပ်ကို ဒေသခံမှာ လည်ပတ်ခြင်း

**တပ်ဆင်မှုကို အတည်ပြုရန်:**

Azure credentials ပါရှိသော `.env` ဖိုင်သည် root ဖိုလ်ဒါတွင် ရှိနေရမည်-

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကို ပြသရမည်။
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကိုပြသသင့်သည်။
```

**အက်ပ်များ စတင်ခြင်း:**

**ရွေးချယ်စရာ ၁ - Spring Boot Dashboard အသုံးပြုမှု (VS Code အသုံးပြုသူများအတွက် အကြံပြုသည်)**

Dev container မှာ Spring Boot Dashboard extension ပါဝင်ပြီး Spring Boot အက်ပ်များကို ကွန်ယက်ထဲမှ တိုက်ရိုက် စီမံခန့်ခွဲနိုင်သည်။ VS Code အာကာသမှ ဘယ်ဘက်သား Activity Bar အတွင်းရှိ Spring Boot icon ကို ရှာတွေ့နိုင်ပါသည်။

Spring Boot Dashboard မှတဆင့် -
- အလုပ်လုပ်နေသော Spring Boot အက်ပ်များအားလုံးကို ကြည့်ရှုနိုင်သည်
- တစ်နှိပ်ချင်း စတင်/ရပ်တန့်နိုင်သည်
- အက်ပ်လော့ဂ်များကို အချိန်နှင့်တပြေးညီ ကြည့်ရှုနိုင်သည်
- အက်ပ်အခြေအနေ သိရှိနိုင်သည်

Module အမည် "introduction" နှင့် သက်ဆိုင်သော play button ကို နှိပ်ပြီး စတင်နိုင်သည်။ မော်ဂျူးအားလုံးကို တပြိုင်နက် ဖြင့် စတင်နိုင်ပါသည်။

<img src="../../../translated_images/my/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**ရွေးချယ်စရာ ၂ - shell script များ အသုံးပြုခြင်း**

ဝဘ်အက်ပ်များအားလုံး စတင်ရန် (module 01-04) -

**Bash:**
```bash
cd ..  # အခြေခံဖိုင်ထုတ်လမ်းကြောင်းမှ
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # မူလ ဒိုင်ရက်တရီမှ
.\start-all.ps1
```

သို့မဟုတ် ဒီမော်ဂျူးတစ်ခုတည်း စတင်ရန် -

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

script နှစ်ခုလုံးသည် root `.env` ဖိုင်မှ environment variable များကို အလိုအလျောက် တင်ပေးပြီး jar ဖိုင် မရှိလျှင် ပြုလုပ်ပေးပါသည်။

> **မှတ်ချက်:** manual ကဲ့သို့ build လုပ်ပြီး စတင်လိုပါက -
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


http://localhost:8080 ကို ဘရောက်ဇာတွင် ဖွင့်ပါ။

**ရပ်တန့်ရန်:**

**Bash:**
```bash
./stop.sh  # ဒီမော်ဂျူးတစ်ခုတည်းပဲ
# ဒါမှမဟုတ်
cd .. && ./stop-all.sh  # မော်ဂျူးအားလုံး
```

**PowerShell:**
```powershell
.\stop.ps1  # ဒီမော်ဂျူးလ်ပဲ
# ဒါမှမဟုတ်
cd ..; .\stop-all.ps1  # မော်ဂျူးလ်အားလုံး
```


## အက်ပ်ကို အသုံးပြုခြင်း

အက်ပ်တွင် နှစ်မျိုးသော စကားပြောဆက်သွယ်မှု UI ကို ဘာသာပြန်ထားသည်။

<img src="../../../translated_images/my/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard မှ Stateless Chat နှင့် Stateful Chat နှစ်မျိုးကို ကြည့်ရှုနိုင်သည်*

### Stateless Chat (ဝါးဘက် နား)

အရင် စမ်းကြည့်ပါ။ "ကျွန်တော်နာမည် John ပါ" ဟု မေးပြီး ချက်ချင်း "ကျွန်တော့်နာမည် ဘာလဲ?" ဟုပြန်မေးပါ။ မော်ဒယ်က ရှေးပို့ပြီးသော မက်ဆေ့ဂျ်ကို မှတ်မထားပါဘူး။ Stateless chat နှင့် မော်ဒယ်ပုံစံ integration တွင် အဓိက ပြဿနာကို ပြသသည်။

<img src="../../../translated_images/my/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI က ယခင်မက်ဆေ့ဂျ်ကနေ နာမည်ကို မမှတ်မိပါ*

### Stateful Chat (ညာဘက်)

တူညီသော စဉ်ဆက်ကို ၎င်းမှာ ပြန်လည်ကြိုးစားပါ။ "ကျွန်တော်နာမည် John ပါ" ဟုပြောသည်နှင့် "ကျွန်တော်နာမည် ဘာလဲ?" ဟုပြန်မေးတဲ့အခါ ဒီခါမှာတော့ မှတ်သားထားပါတယ်။ ကွာခြားချက်မှာ MessageWindowChatMemory ဖြစ်ပြီး၊ စကားပြောအတိတ်သမိုင်းကို ထိန်းသိမ်းထားသည်။ ထို့ကြောင့် တောင်းဆိုမှုတိုင်းတွင် ကိုယ်စားပြုချက်အတိတ်ကို ပေါင်းထည့်ပေးပါသည်။ ထုတ်လုပ်မှု conversational AI တွင် ဒါပေမဲ့ ရေပန်းစားသည်။

<img src="../../../translated_images/my/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI က စကားပြောဝိုင်းထဲက မြင်သာစေသည့် နာမည်ကို မှတ်သားထားသည်*

နှစ်ဘောင်လုံးတွင် GPT-5.2 မော်ဒယ် တူညီသည်။ ကွာခြားချက်က memory ဖြစ်သည်။ ၎င်းသည် memory သည် သင့် အက်ပ်ကို ဘာတွေလုပ်စေသည်ကို ချက်ချင်း ဖြေရှင်းပေးပြီး လက်တွေ့အသုံးပြုရလွယ်ကူစေသည်။

## နောက်ဆက်တွဲ လုပ်ဆောင်ရန်

**နောက် Module:** [02-prompt-engineering - GPT-5.2 နှင့် Prompt Engineering](../02-prompt-engineering/README.md)

---

**လမ်းညွှန်:** [← ယခင်: Module 00 - Quick Start](../00-quick-start/README.md) | [Main သို့ ပြန်သွားမည်](../README.md) | [နောက်တစ်ခု: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ကြိုတင်အသိပေးချက်**  
ဤစာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှုဖြစ်သော [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှန်ကန်မှုအတွက် ကြိုးပမ်းသော်လည်း အလိုအလျောက်ဘာသာပြန်ချက်များတွင် ကျွတ်ယွင်းချက် သို့မဟုတ် အမှားများ ပါဝင်နိုင်ကြောင်း သတိပြုရန် မေတ္တာရပ်ခံပါသည်။ မူရင်းစာတမ်းကို ၎င်း၏ မူလဘာသာဖြင့်သာ တရားဝင်ရမ်းခံရမည့်အရင်းအမြစ်ဟု မှတ်ယူပါ။ အရေးကြီးသော သတင်းအချက်အလက်များအတွက် ကုလသမဂ္ဂအတည်ပြု လူ့ဘာသာပြန်သော ဝန်ဆောင်မှုကို အသုံးပြုရန် မျှော်လင့်ပါသည်။ ဤဘာသာပြန်ချက် အသုံးပြုပြီး ဖြစ်ပေါ်လာနိုင်သည့် မှားနားမှုများသို့မဟုတ် နားလည်မှုပျက်ပြားမှုများအတွက် ကျွန်ုပ်တို့ တာဝန်မရှိပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->