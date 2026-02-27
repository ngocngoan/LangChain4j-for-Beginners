# Module 01: LangChain4j ဖြင့် စတင်လေ့လာခြင်း

## အကြောင်းအရာ ဇယား

- [ဗီဒီယို လမ်းညွှန်](../../../01-introduction)
- [သင်လေ့လာမည့်အချက်များ](../../../01-introduction)
- [လိုအပ်ချက်များ](../../../01-introduction)
- [အဓိကပြသနာကိုနားလည်ခြင်း](../../../01-introduction)
- [တိုးကင်များကိုနားလည်ခြင်း](../../../01-introduction)
- [မှတ်ဉာဏ်၏ လည်ပတ်ပုံ](../../../01-introduction)
- [ဒီအပိုင်းမှာ LangChain4j ကို ဘယ်လိုအသုံးပြုထားတာလဲ](../../../01-introduction)
- [Azure OpenAI အခြေခံအဆောက်အအုံ တပ်ဆင်ခြင်း](../../../01-introduction)
- [အက်ပလီကေးရှင်းကို ဒေသীয়အားဖြင့် လည်ပတ်ခြင်း](../../../01-introduction)
- [အက်ပလီကေးရှင်း အသုံးပြုခြင်း](../../../01-introduction)
  - [Stateless Chat (ဘယ်ဘက် Panel)](../../../01-introduction)
  - [Stateful Chat (ညာဘက် Panel)](../../../01-introduction)
- [နောက်တစ်ဆင့်များ](../../../01-introduction)

## ဗီဒီယို လမ်းညွှန်

ဒီ module ကို စတင်သင်ယူရန် ရှင်းပြထားသည့် live session ကို ကြည့်ပါ - [Getting Started with LangChain4j - Live Session](https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9)

## သင်လေ့လာမည့်အချက်များ

Quick start ကိုပြီးစီးခဲ့ပါက prompt တွေစေလွှတ်ပြီး တုံ့ပြန်ချက်ရယူနည်းကို မြင်တွေ့နားလည်ခဲ့ရသည်။ ဒါဟာ အခြေခံဖြစ်သော်လည်း တကယ့်အက်ပလီကေးရှင်းတွေမှာ ပိုမိုလိုအပ်ချက်များ ရှိသည်။ ဒီ module သည် context ကို မှတ္မိပြီး state ကို ထိန်းသိမ်းထားသော စကားပြော AI ကို တည်ဆောက်နည်းကို သင်ပေးမှာဖြစ်သည်။ ဒီဟာက တစ်ကြိမ်သာ ပြသမှုပြုသော နမူနာနှင့် ထုတ်လုပ်အသုံးပြုနိုင်သော အက်ပလီကေးရှင်း တို့အကြား ကွာခြားချက်ဖြစ်သည်။

ဒီလမ်းညွှန်မှာ Azure OpenAI ရဲ့ GPT-5.2 ကို သုံးပါမည်။ ၎င်းရဲ့ အဆင့်မြင့် မှတ်ချက်တုံ့ပြန်နိုင်မှုများကြောင့် pattern အမျိုးမျိုး၏ လုပ်ဆောင်မှုများ ပိုရှင်းလင်းလတ်ဆတ်စေသည်။ မှတ်ဉာဏ် ထည့်သွင်းသည့်အခါ ကွာခြားချက်ကို ရှင်းလင်းပြတ်သားစွာ မြင်တွေ့နိုင်မည်ဖြစ်သည်။ ဒါဟာ အပိုင်းအစတစ်ခုချင်းစီက သင့်အက်ပလီကေးရှင်းပေါ်တွင် ဘာကို ပေးဆောင်နေသည်ကို နားလည်ရေး အလွယ်တကူကူညီပေးမည်။

သင်သည် နမူနာတစ်ခုတည်းကို တည်ဆောက်မည်ဖြစ်ပြီး ထို pattern နှစ်မျိုးလုံးကို ပြသမည်ဖြစ်သည်။

**Stateless Chat** - တစ်ခုချင်းစီသော တောင်းဆိုမှုများသည် လွတ်လပ်ပြီး ကြိုတင်မှတ်ဉာဏ်မရှိပါ။ ဒီ pattern ကို quick start တွင် အသုံးပြုခဲ့သည်။

**Stateful Conversation** - တောင်းဆိုမှုတိုင်းတွင် စကားပြောသမိုင်းပါဝင္သည်။ မော်ဒယ်သည် ပြောဆိုမှု နောက်တစ်ကြိမ်အတွက် context ကို ထိန်းသိမ်းထားသည်။ ထုတ်ကုန်အတွက် အကောင်အထည်ဖော်ထားသော အက်ပလီကေးရှင်းများအတွက် လိုအပ်သည်။

## လိုအပ်ချက်များ

- Azure subscription နှင့် Azure OpenAI ဝင်ခွင့်
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **မှတ်ချက်:** Java, Maven, Azure CLI နှင့် Azure Developer CLI (azd) တို့ကို ဖော်ပြထားသည့် devcontainer တွင် အသင့်တပ်ဆင်ထားပြီးဖြစ်ပါသည်။

> **မှတ်ချက်:** ဒီ module မှာ Azure OpenAI ပေါ် ဆောင်ရွက်သည့် GPT-5.2 ကို အသုံးပြုသည်။ azd up ဖြင့် deployment ကို အလိုအလျောက်ပြုလုပ်ထားသောကြောင့် ကုဒ်အတွင်း မော်ဒယ်နာမည်ကို ပြောင်းလဲမလုပ်ပါနှင့်။

## အဓိကပြသနာကိုနားလည်ခြင်း

ဘာသာစကားပုံစံများမှာ stateless ဖြစ်သည်။ API ဖုန်းခေါ်တိုင်းဟာ လွတ်လပ်စွာဖြစ်သည်။ "My name is John" လို့ ပြောပြီးနောက် "What's my name?" ဆိုသောအခါ မော်ဒယ်မှာ သင့်နာမည်ကို သတိမထားပါဘူး။ တောင်းဆိုမှုတိုင်းသည် သင့်စကားပြောပထမဆုံးအကြိမ် ဖြေဆိုသည်ဟု ထင်မြင်သဘောထားပါသည်။

စာသားအရိုးရှင်း မေးခွန်း-ဖြေခန်းများအတွက် ပြဿနာမရှိပေမဲ့ တကယ့်အက်ပလီကေးရှင်းများအတွက် အလွန်မရပါ။ ဖောက်သည်ဝန်ဆောင်မှုပြုသူ chatbot များသည် သင်ပြောခဲ့သောအချက်များကို မှတ်ထားရန် လိုအပ်သည်။ ကိုယ်ပိုင်အကူအညီပေးသူများသည် context လိုအပ်သည်။ စကားပြော ပိုများပြီး လှုပ်ရှားမှုများအတွက် မှတ်ဉာဏ် လိုအပ်သည်။

<img src="../../../translated_images/my/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*stateless (လွတ်လပ်သောခေါ်ဆိုချက်များ) နှင့် stateful (context-aware) စကားပြောမှုအကြား ကွာခြားခြင်း*

## တိုးကင်များကိုနားလည်ခြင်း

စကားပြောတွင် ကျော်လွန်မည့်မတိုင်မီ tokens ကိုနားလည်ခြင်းသည် အရေးကြီးသည် - ဘာသာစကားပုံစံများ အသုံးပြုသော စာသားအခြေခံယူနစ်များ။

<img src="../../../translated_images/my/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*စာသား ကို tokens များအဖြစ် ခွဲခြမ်းသုံးသပ်ခြင်း၏ နမူနာ - "I love AI!" သည် စတုတ္ထထောင့်ပွဲသို့ ပြောင်းလဲခြင်း*

tokens ဆိုသည်မှာ AI မော်ဒယ်များ စာသားကို တိုင်းတာအပ်နှံရာယူနစ်များဖြစ်သည်။ စကားလုံး၊ ပုဒ်မ၊ နှင့် အပေါက်များလည်း token ဖြစ်နိုင်သည်။ သင့်မော်ဒယ်တွင် တစ်ပြိုင်နက် maximum ใหม่ပြုလုပ်နိုင်သည့် token အရေအတွက် ရှိသည် (GPT-5.2 အတွက် ၄သိန်းခန့်, ၂၇၂,၀၀၀ ခု input token နှင့် ၁၂၈,၀၀၀ ခု output token ပါရှိသည်)။ tokens များနားလည်ခြင်းက စကားပြော အရှည်နှင့် ကုန်ကျစရိတ်ကို စီမံရန် အရေးကြီးသည်။

## မှတ်ဉာဏ်၏ လည်ပတ်ပုံ

စကားပြော မှတ်ဉာဏ်သည် stateless ပြသနာကို ဖြေရှင်းပါသည်။ မော်ဒယ်သို့ ရှေ့တတ်သော တောင်းဆိုမှုမတင်မီ နှင့် မက်ဆေ့ခ််များကို အလိုအလျောက် ဆက်စပ်ထည့်သွင်းပေးသည်။ "What's my name?" ရှင်းလင်းရန် စကားပြောသမိုင်းတစ်ခုလုံးကို စနစ်မှ ပို့သွား၍ မော်ဒယ်အရင်က "My name is John" ဆိုသည်ကို တွေ့နိုင်ပါသည်။

LangChain4j မှ memory implementation များကို မူလတန်းအလိုအလျောက် ကိုင်တွယ်ပေးသည်။ သင့်အား မက်ဆေ့ခ်် အရေအတွက်ကို ရွေးချယ်ခွင့်ပြုပြီး framework က context window ကို စီမံခန့်ခွဲပေးပါသည်။

<img src="../../../translated_images/my/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory သည် မက်ဆေ့ခ်် များကို ဇယားကွက်ပုံစံဖြင့် ထိန်းသိမ်းသည်၊ ဘယ်ဟာမှတ်ထားမယ် ဘယ်ဟာ ကျွတ်မှာဆိုတာ အလိုအလျောက် စီမံသည်*

## ဒီအပိုင်းမှာ LangChain4j ကို ဘယ်လိုအသုံးပြုထားတာလဲ

ဒီ module သည် quick start ကိုချဲ့ထွင်ပြီး Spring Boot ကို ပေါင်းစပ်အသုံးပြု၍ စကားပြော မှတ်ဉာဏ် ထည့်သွင်းထားသည်။ အပိုင်းအစများပေါင်းစပ်သည့်နည်းလမ်းမှာ -

**Dependencies** - LangChain4j ရဲ့ တစ်ချို့ libraries နှစ်ခုကို ထည့်သွင်းရန်

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
  
**Chat Model** - Azure OpenAI ကို Spring bean အဖြစ် ဆက်တင်လုပ်ရန် ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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
  
Builder က `azd up` ဖြင့်သတ်မှတ်ထားသော environment variable ကနေ အချက်အလက်များကို ဖတ်သည်။ `baseUrl` ကို သင့် Azure endpoint သို့ ပြောင်း၍ OpenAI client ကို Azure OpenAI နှင့် အတူ အလုပ်လည်စေသည်။

**Conversation Memory** - MessageWindowChatMemory ဖြင့် စကားပြောသမိုင်းကို ထိန်းသိမ်းရန် ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```
  
`withMaxMessages(10)` ဖြင့် နောက်ဆုံး ၁၀ မက်ဆေ့ခ််များ ထိန်းသိမ်းရန် memory ဖန်တီးသည်။ အသုံးပြုသူနှင့် AI မက်ဆေ့ခ််များကို `UserMessage.from(text)` နှင့် `AiMessage.from(text)` ဖြင့် ထည့်သွင်းသည်။ သမိုင်းကို `memory.messages()` ကနေ ရယူပြီး မော်ဒယ်ထံ ပို့သည်။ service သည် တစ်စကားပြော ID လျောက် memory instance များကို သီးခြားသိမ်းဆည်းကာ အတူတူ မဟုတ်သော အသုံးပြုသူများစကားပြောမှုများအတွက်အသုံးပြုနိုင်သည်။

> **🤖 GitHub Copilot နှင့် Chat လုပ်စမ်းရန်:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ဖိုင်ကို ဖွင့်ပြီး မေးမြန်းပါ -
> - "MessageWindowChatMemory က memory window ပြည့်သောအခါ မက်ဆေ့ခ််တွေ ဘယ်လိုဆုံးဖြတ်ပြီး ထုတ်မလဲ?"
> - "ကျွန်တော် Database အသုံးပြု၍ custom memory storage ကို ဘယ်လိုဆောင်ရွက်နိုင်မလဲ?"
> - "အဟောင်း စကားပြောသမိုင်းကို စုပုံဖော်မြူး ရှင်းလင်းအသွင်ပြောင်းဖို့ ဘယ်လို ထည့်နိုင်မလဲ?"

Stateless chat endpoint မှာ memory ကို အားမရေဘဲ `chatModel.chat(prompt)` ဖြင့် quick start နဲ့တူသလို အသုံးပြုသည်။ Stateful endpoint မှာ memory ထည့်သွင်း၍ သမိုင်းကို ယူပြီး တောင်းဆိုမှုတိုင်းနှင့် context ထည့်ပေးသည်။ မော်ဒယ်ဖွဲ့စည်းမှု တူသည်၊ pattern ကွာခြားသည်။

## Azure OpenAI အခြေခံအဆောက်အအုံ တပ်ဆင်ခြင်း

**Bash:**
```bash
cd 01-introduction
azd up  # စာရင်းသွင်းခြင်းနှင့် တည်နေရာကို ရွေးချယ်ပါ (eastus2 ကို အကြံပြုပါသည်)
```
  
**PowerShell:**
```powershell
cd 01-introduction
azd up  # စာရင်းသွင်းခြင်းနှင့် တည်နေရာ (eastus2 အကြံပြု) ကို ရွေးချယ်ပါ။
```
  
> **မှတ်ချက်:** timeout အမှားတွေ (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) ကြုံရပါက `azd up` ကို ထပ်မိန့်ပြီး ပြန်ပြေးပါ။ Azure အရင်းအမြစ်များ နောက်ခံတွင် သန့်စင်ဆောင်ရွက်နေသဖြင့် retry ဆောင်ရွက်ရင် deployment ပြီးဆုံးမည်ဖြစ်သည်။

ဒီဆောင်ရွက်မှုများမှာ -
1. Azure OpenAI resource ကို GPT-5.2 နှင့် text-embedding-3-small models ဖြင့် တပ်ဆင်သည်
2. လုပ်ဆောင်ရန်လက်မှတ်ထားပြီး `.env` ဖိုင်လည်း ဇယား Root တွင် ပြုလုပ်ပေးသည်
3. အားလုံးလိုအပ်သော environment variables များကို သတ်မှတ်ပေးသည်

**Deployment ပြဿနာများဖြစ်ပါက** [Infrastructure README](infra/README.md) တွင် subdomain နာမည်ပဋိပက္ခ၊ Azure Portal မှလက်ဖြင့် တပ်ဆင်နည်းများ နှင့် model ဖွဲ့စည်းမှုအတွက် လမ်းညွှန်ချက်များ ကြည့်ပါ။

**Deployment အောင်မြင်သည်ကို စစ်ဆေးခြင်း:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY နှင့် အခြား အချက်အလက်များကို ပြသရန် ဖြစ်သည်။
```
  
**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT၊ API_KEY နှင့် အခြားများကိုပြသသင့်သည်။
```
  
> **မှတ်ချက်:** `azd up` က `.env` ဖိုင်ကို အလိုအလျောက် ဖန်တီးပေးသည်။ နောက်တစ်ကြိမ် ပြင်ဆင်လိုပါက `.env` ကို လက်ဖြင့်ပြုပြင်ရန် သို့မဟုတ် အောက်ပါနည်းများဖြင့် ထပ်မံဖန်တီးနိုင်သည် -
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


## အက်ပလီကေးရှင်းကို ဒေသীয়အားဖြင့် လည်ပတ်ခြင်း

**Deployment ကို စစ်ဆေးပါ:**

Root ဖိုလ်ဒါတွင် Azure အချက်အလက်ပါ `.env` ဖိုင် ရှိမှုကို သေချာစေပါ -

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကိုပြသသင့်သည်
```
  
**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကိုပြသသင့်သည်
```
  
**အက်ပလီကေးရှင်းများကို စတင်ပါ:**

**နည်းလမ်း ၁: Spring Boot Dashboard ကို အသုံးပြုခြင်း (VS Code အသုံးပြုသူအတွက် အကြံပြုသည်)**

devcontainer တွင် Spring Boot Dashboard Extension များ ပါဝင်ပြီး Spring Boot အက်ပလီကေးရှင်း အားလုံးကို ထိန်းချုပ်နိုင်သော ဗျူအို Interface ဖြစ်သည်။ VS Code ၏ ဘယ်ဘက် Activity Bar တွင် Spring Boot အိုင်ကွန်ဖြင့် တွေ့နိုင်သည်။

Dashboard မှ -
- လုပ်ငန်းလည်ပတ်သော Spring Boot applications အားလုံးကို မြင်ရန်
- Click တစ်ချက်ဖြင့် စတင်/ပိတ်ရန်
- real-time logs ပြနေတတ်ခြင်း
- အက်ပလီကေးရှင်း၏ အခြေအနေ ကြည့်ရှုခြင်း

"introduction" အနီးရှိ Play ခလုတ်ကို နှိပ်ကာ ဒီ module ကို စတင်နိုင်သည်၊ သို့မဟုတ် modules အားလုံးကို တပြိုင်နက် စတင်နိုင်ပါသည်။

<img src="../../../translated_images/my/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**နည်းလမ်း ၂: shell script များ သုံး၍ စတင်ခြင်း**

Web applications (modules 01-04) အားလုံးကို စတင်ရန် -

**Bash:**
```bash
cd ..  # ရွှေ့ပြောင်းရာ မှတ်တမ်းအစဉ်မှ
./start-all.sh
```
  
**PowerShell:**
```powershell
cd ..  # မူရင်း directory မှ
.\start-all.ps1
```
  
သို့မဟုတ် ဒီ module ကိုသာ စတင်ရန် -

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
  
script နှစ်ခုလုံးသည် Root `.env` ဖိုင်မှ environment variables ကို အလိုအလျောက် load ပြုလုပ်ပြီး JAR မရှိလျှင် ဖန်တီးပါသည်။

> **မှတ်ချက်:** စတင်မပြုလုပ်မီ modules အားလုံးကို လက်ဖြင့် build ဆောင်ရွက်လိုပါက -
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
  
http://localhost:8080 ကို browser တွင် ဖွင့်၍ ကြည့်ရှုနိုင်သည်။

**ရပ်ရန်:**

**Bash:**
```bash
./stop.sh  # ဒီမော်ဂျူးတစ်ခုတည်းသာ
# သို့မဟုတ်
cd .. && ./stop-all.sh  # မော်ဂျူးအားလုံး
```
  
**PowerShell:**
```powershell
.\stop.ps1  # ဒီမော်ဂျူးတစ်ခုချင်းသာ
# သို့မဟုတ်
cd ..; .\stop-all.ps1  # အားလုံးသောမော်ဂျူးများ
```
  

## အက်ပလီကေးရှင်း အသုံးပြုခြင်း

အက်ပလီကေးရှင်းတွင် chat implementation နှစ်ခု ရှေ့ဆုံထားသော ဝဘ် မျက်နှာပြင် ပါရှိသည်။

<img src="../../../translated_images/my/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Simple Chat (stateless) နှင့် Conversational Chat (stateful) ရွေးချယ်စရာ ပေါ်ပေါက်ခြင်းနှင့် Dashboard*

### Stateless Chat (ဘယ်ဘက် Panel)

ဒီအရင်ဆုံး စမ်းသပ်ပါ။ "My name is John" လို့ မေးပြီးနောက် လျင်မြန်စွာ "What's my name?" မေးပါ။ မော်ဒယ်မှာ မှတ်မိမှု မရှိသောကြောင့် ဘာမှမသိကြပါဘူး။ ၎င်းသည် အခြေခံ language model ပေါင်းစည်းချက်တွင် ဖြစ်သော အဓိကပြသနာဖြစ်သည် - စကားပြော context မရှိခြင်း။

<img src="../../../translated_images/my/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI သည် ယခင်မက်ဆေ့ခ််မှ သင့်နာမည်ကို မှတ်မိခြင်းမရှိ*

### Stateful Chat (ညာဘက် Panel)

ယခု အတူတူ စစ်ဆေးပါ။ "My name is John" ပြောပြီးနောက် "What's my name?" မေးသည်။ ယခုမှာ မှတ်မိသည်။ ကွာခြားချက်မှာ MessageWindowChatMemory ဖြစ်ပြီး စကားပြောသမိုင်းကို ထိန်းသည်။ တောင်းဆိုမှုတိုင်းတွင် memory ပါဝင်သည်။ ထုတ်လုပ်မှုအတွက် စကားပြော AI တွင် ဒီလို လုပ်ဆောင်ကြသည်။

<img src="../../../translated_images/my/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI သည် စကားပြောအတွင်း သင့်နာမည်ကို မှတ်ထားသည်*

Panel နှစ်ခုလုံးသည် GPT-5.2 မော်ဒယ်တူညီတစ်ခုကို အသုံးပြုသည်။ ထင်ရှားသော ကွာခြားချက်မှာ memory ပါဝင်ခြင်း ချင်းသာဖြစ်သည်။ ဒီဟာက memory က သင့်အက်ပလီကေးရှင်းကို ဘယ်လိုကူညီပြီး မူလကျကျ အသုံးပြုမှုများတွင် အရေးကြီးမှုကို တိကျစွာ ပြသသည်။

## နောက်တစ်ဆင့်များ

**နောက်တစ်ပိုင်း:** [02-prompt-engineering - GPT-5.2 ဖြင့် Prompt Engineering](../02-prompt-engineering/README.md)

---

**သွားရမည့် လမ်းညွှန်:** [← အရင်ပိုင်း: Module 00 - Quick Start](../00-quick-start/README.md) | [နောက်ပြန်သွား Main](../README.md) | [ရှေ့ဆက်: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ဆိုင်းငံ့ချက်**  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှုဖြစ်သော [Co-op Translator](https://github.com/Azure/co-op-translator) ဖြင့် ဘာသာပြန်ထားခြင်းဖြစ်သည်။ ကျွန်ုပ်တို့သည် တိကျမှုအတွက် ကြိုးစားမှုရှိပါသော်လည်း၊ စက်ကိရိယာမှ ဘာသာပြန်ထားသောအတွက် မှားယွင်းမှုများ သို့မဟုတ် မှားလွဲချက်များ ပါဝင်နိုင်သည်ကို သတိပြုပါ။ မူရင်းစာရွက်စာတမ်းကို မိဘဘာသာဖြင့်သာ အတည်ပြုရမည့်အထောက်အထားအဖြစ် ယူဆသင့်ပါသည်။ အရေးပါသောအချက်အလက်များအတွက် ပညာရှင် လူသားများ၏ ဘာသာပြန်မှုကို အကြံပြုပါသည်။ ဤဘာသာပြန်မှုအသုံးပြုမှုကြောင့် ဖြစ်ပေါ်လာသော နားလည်မှုမှားယွင်းခြင်းများ သို့မဟုတ် မှားဖတ်ခြင်းများအတွက် ကျွန်ုပ်တို့ တာဝန်မယူပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->