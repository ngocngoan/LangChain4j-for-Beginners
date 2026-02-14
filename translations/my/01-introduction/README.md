# Module 01: LangChain4j ဖြင့် စတင် လေ့လာခြင်း

## Table of Contents

- [သင် လေ့လာမည့်အချက်များ](../../../01-introduction)
- [လိုအပ်ချက်များ](../../../01-introduction)
- [အဓိက ပြဿနာကို နားလည်ခြင်း](../../../01-introduction)
- [တိုးကင်များအကြောင်း နားလည်ခြင်း](../../../01-introduction)
- [မှတ်ဉာဏ် အလုပ်ပုံ](../../../01-introduction)
- [LangChain4j ကို ဘယ်လို အသုံးပြုသည်](../../../01-introduction)
- [Azure OpenAI အင်ဖရာ များတပ်ဆင်ခြင်း](../../../01-introduction)
- [အပလီကေးရှင်းကို ဒေသံတွင်းတွင် စတင် စမ်းသပ်ခြင်း](../../../01-introduction)
- [အပလီကေးရှင်း အသုံးပြုခြင်း](../../../01-introduction)
  - [Stateless Chat (ဘယ်ဘက် ပနယ်)](../../../01-introduction)
  - [Stateful Chat (ညာဘက် ပနယ်)](../../../01-introduction)
- [နောက်တစ်ဆင့်များ](../../../01-introduction)

## သင် လေ့လာမည့်အချက်များ

အချက်အလတ် စတင်လေ့လာမှုကို ပြီးမြောက်စေခဲ့ပါက၊ prompt များပို့၍ တုံ့ပြန်ချက်များရရှိနည်းကို ကြည့်တွေ့ခဲ့ပြီးဖြစ်သည်။ ၎င်းသည် အခြေခံ ဖြစ်သော်လည်း၊ တကယ် ယုံကြည်စိတ်ချရသော အပလီကေးရှင်းများတွင် ပိုမိုလိုအပ်သည်။ ဤ module သည် context ကို မှတ်မှတ်သားသား မှတ်ဉာဏ်တွင် ထိန်းသိမ်းထားကာ conversation state ကို ထိန်းသိမ်းနိုင်သော conversational AI တစ်ခု တည်ဆောက်နည်းကို သင်ကြားပါမည် - ၎င်းသည် တစ်ကြိမ် သုံး demo နှင့် စက်ရုံ အသုံးပြုအဆင်ပြေနိုင်သော အပလီကေးရှင်းအကြား အနားလမ်း ဖြစ်သည်။

ဤလမ်းညွှန်လမ်းကြောင်းပတ်လမ်းလယ်တွင် Azure OpenAI ၏ GPT-5.2 ကို အသုံးပြုပါမည်၊ ၎င်း၏ တိုးတက်သော ပြဿနာများ ဆင်ခြင်နိုင်မှုကြောင့် အမျိုးမျိုးသော ပုံစံများ၏ သက်ရောက်မှုများ ပိုမို ပေါ်လွင်စေသည်။ မှတ်ဉာဏ် ပေါင်းထည့်လျှင် အခြားသောစက်မှုအမြင်များကို ပိုမိုရှင်းလင်းစွာ မြင်နိုင်ပါသည်။ ၎င်းသည် သင်၏ အပလီကေးရှင်း၏ ပိုင်းခြားချက်များကို နားလည်ရန် ပိုမိုလွယ်ကူစေသည်။

သင်သည် ပုံစံနှစ်မျိုးကို ပြသသော အပလီကေးရှင်းတစ်ခု တည်ဆောက်မည် -

**Stateless Chat** - တစ်ခုချင်း တောင်းဆိုမှုအားလုံး သာမာန်ဖြစ်ပါသည်။ မော်ဒယ်သည် ယခင် မက်ဆေ့ခ်ျများအားမှတ်မရပါ။ ၎င်းသည် အချက်အလက် စတင် လေ့လာမှုတွင် အသုံးပြုခဲ့သော ပုံစံ ဖြစ်သည်။

**Stateful Conversation** - တစ်ခုချင်း တောင်းဆိုမှုတွင် စကားပြောမှတ်တမ်း ပါဝင်သည်။ မော်ဒယ်သည် စကားတွေပြောဆိုမှုအတွင်း အကြောင်းအရာကို ထိန်းသိမ်းထားသည်။ ၎င်းသည် ထုတ်လုပ်မှု အပလီကေးရှင်းများ လိုအပ်ချက်ဖြစ်သည်။

## လိုအပ်ချက်များ

- Azure subscription နှင့် Azure OpenAI ခွင့်ပြုချက်
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** Java, Maven, Azure CLI နှင့် Azure Developer CLI (azd) ကို ပေးထားသော devcontainer တွင် အလိုအလျောက် ထည့်သွင်းထားသည်။

> **Note:** ဤ module တွင် Azure OpenAI ၏ GPT-5.2 ကို အသုံးပြုသည်။ Deployment ကို `azd up` နည်းဖြင့် အလိုအလျောက် ညှိနှိုင်းထားပြီး၊ code တွင် မော်ဒယ်နာမည် ကို မပြောင်းလဲရ။

## အဓိက ပြဿနာကို နားလည်ခြင်း

ဘာသာစကား မော်ဒယ်များမှာ stateless ဖြစ်သည်။ တစ်ခုချင်း API ခေါ်ဆိုမှုအားလုံး သာမာန်ဖြစ်သည်။ ဥပမာ "My name is John" ဟု ပို့ပြီးနောက် "What's my name?" ဟု မေးလျှင် မော်ဒယ်သည် သင် မိတ်ဆက်ခဲ့သည်ကို မသိကြောင်း ဖြစ်သည်။ တောင်းဆိုရုံသည် စကားပြောမှု ပထမဆုံး ပြောဆိုပုံတိုင်းလို ဖြစ်စေသည်။

ဤနည်းလမ်းမှာ အလွယ်တကူ မေးမြန်းခြင်းနှင့် ဖြေကြောင်းအတွက် သင့်လျော်သော်လည်း သုံးစွဲမှု များသော အပလီကေးရှင်းများအတွက် မလုံလောက်ပါ။ ဖောက်သည်ဝန်ဆောင်မှု bot များသည် မိမိ ပြောကြားသော အကြောင်းအရာကို မှတ်မိ ရပါမည်။ ပုဂ္ဂိုလ်ရေး အထောက်အကူပြုသူများသည် context လိုအပ်သည်။ စကားပြောဆိုမှု တစ်ချက်နှစ်ချက် ပြန်လည်ဆက်လက်မှုများသည် မှတ်ဉာဏ် လိုအပ်သည်။

<img src="../../../translated_images/my/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*stateless (လွတ်လပ်ခေါ်ဆိုမှု) နှင့် stateful (context သိရှိသည့်) စကားပြောဆိုမှု မတူကွဲပြားချက်*

## တိုးကင်များအကြောင်း နားလည်ခြင်း

စကားပြောဆိုမှုပေါင်းစည်းခြင်း မတိုင်မီ၊ ဗဟုသုတတစ်ခုအနေဖြင့် token များကို နားလည်ရမည် - ဘာသာစကား မော်ဒယ်များ အသုံးပြုသော အခြေခံ စာသားအချက်အလက်များဖြစ်သည်။

<img src="../../../translated_images/my/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*“I love AI!” တွင် စာသားများကို token ကွဲခြားသည့် နမူနာ - အပိုင်း ၄ ခုပြောင်းသည်*

Token များသည် AI မော်ဒယ်များအတွက် စာသားကို ခွဲခြားတိုင်းတာခြင်း သုံးသော အချက်များ ဖြစ်သည်။ စကားလုံးများ၊ အချက်ပေါင်းများ နှင့် နေရာများပြီးစီးပါသည်။ မော်ဒယ်တွင် တစ်ကြိမ် တည်း ပိုက်ဆံဆပ်နိုင်မှု အကြွင်းမဲ့ အရေအတွက် ရှိပြီး (GPT-5.2 သည် ၄၀၀,၀၀၀ token ကန့်သတ်ချက်ရှိသည့်အတွက် ၂၇၂,၀၀၀ input token နှင့် ၁၂၈,၀၀၀ output token ပါ) ။ tokens နားလည်ခြင်းက စကားပြောဆိုမှု ကြာရှည်မှု နှင့် ကုန်ကျစရိတ် ထိန်းချုပ်ရာတွင် အထောက်အကူပြုသည်။

## မှတ်ဉာဏ် အလုပ်ပုံ

Chat memory သည် stateless ပြဿနာကို conversation မှတ်တမ်း ထိန်းသိမ်းခြင်းဖြင့် ဖြေရှင်းသည်။ မော်ဒယ်သို့ ဖောက်သည်တောင်းဆိုမှု ပို့မည်မဆို၊ framework သည် ညွန်ကြားမှုမီ မစကားပြောချက်များ ထည့်ပေးသည်။ "What's my name?" ဟု မေးစဉ်၊ စနစ်သည် အပြည့်အစုံ စကားပြောချက်မှတ်တမ်း ပို့ပြီးဖြစ်သောကြောင့် မော်ဒယ်အနေဖြင့် "My name is John" ဟူ၍ သင် ပြောခဲ့သည်ကို မြင်မြင်ရသည်။

LangChain4j သည် ၎င်းကို အလိုအလျောက် စီမံခန့်ခွဲနိုင်သော memory implementation များပံ့ပိုးသည်။ သင့်အား စကားပြောချက် အရေအတွက် ရွေးချယ်ခြင်းနှင့် framework သည် context window ကို စီမံခန့်ခွဲပေးသည်။

<img src="../../../translated_images/my/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory သည် နောက်ဆုံးပေါ် စကားများ ကို sliding window ဖြင့် ထိန်းသိမ်း၍ အဟောင်းများကို အလိုအလျောက် ပယ်ဖျက်ပေးသည်*

## LangChain4j ကို ဘယ်လို အသုံးပြုသည်

ဤ module သည် quick start ကို ကျယ်ပြန့်စွာ ပြန်လည်တိုးချဲ့ကာ Spring Boot နှင့် conversation memory ကို ပေါင်းစပ် ထည့်သွင်းသည်။ အစိတ်အပိုင်း များသည် အောက်ပါ ပုံစံဖြင့် ပေါင်းစပ်ထားသည် -

**Dependencies** - LangChain4j အုပ်စုနှစ်ခု ထည့်သွင်းရန် -

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

**Chat Model** - Azure OpenAI ကို Spring bean အဖြစ် ခွန်အားပေးရန် ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder သည် `azd up` ဖြင့် စက်ပေါ်သတ်မှတ်ထားသော environment variable များမှ အချက်အလက်များကိုဖတ်ယူပါသည်။ `baseUrl` ကို Azure endpoint သို့ တိုက်ရိုက် ဆက်သွယ်စေရန် ရေးဆွဲထားသည်။

**Conversation Memory** - MessageWindowChatMemory ဖြင့် စကားပြောမှတ်တမ်းကို စောင့်ကြည့်ရန် ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

withMaxMessages(10) ဖြင့် နောက်ဆုံး ၁၀ မက်ဆေ့ခ်ျများ ထိန်းသိမ်းပါသည်။ အသုံးပြုသူ နှင့် AI မက်ဆေ့ခ်ျများအား `UserMessage.from(text)` နှင့် `AiMessage.from(text)` ဖြင့် ထည့်ပါ။ သမိုင်းမှတ်တမ်းကို memory.messages() ဖြင့် ရယူပြီး မော်ဒယ်သို့ ပို့ပါ။ service သည် Conversation ID အလိုက် memory instances များကို ခွဲခြားစီမံထိန်းသိမ်းလျှက်ရှိပါသည်၊ ချစ်သူများစွာ လည်း တစ်ပြိုင်နက် စကားပြောနိုင်ပါသည်။

> **🤖 GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ရန်:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ဖွင့်၍ မေးမြန်းပါ -
> - "MessageWindowChatMemory သည် window ပြည့်သောအခါ မက်ဆေ့ခ်ျ များကို မည်သို့ဆုံးဖြတ် ပယ်ဖျက်သနည်း?"
> - "In-memory မဟုတ်ဘဲ database သိုလှောင်မှုအသစ် ဖန်တီးနိုင်မလား?"
> - "ပဟေဋိ နှစ်ပေါင်းသည့် စကားပြောမှတ်တမ်း များကို စုစည်း စနစ် ပေါင်းထည့်ရန် ဘယ်လို ပြုလုပ်ရမလဲ?"

Stateless chat endpoint သည် memory ကို လုံးဝ မသုံးဘဲ quick start နှင့် တူသည် - `chatModel.chat(prompt)` ကြိုက်သလို သုံးသည်။ Stateful endpoint မှာ memory ထဲ သို့ ပို့၍ သမိုင်းမှတ်တမ်း ရယူပြီး request တစ်ခုချင်းစီတွင် context ဖြင့် သွင်းပါသည်။ မော်ဒယ် ညှိနှိုင်းမှု တူညီ၊ ပုံစံကွဲပြားခြားနားမှုသာ ရှိသည်။

## Azure OpenAI အင်ဖရာ များတပ်ဆင်ခြင်း

**Bash:**
```bash
cd 01-introduction
azd up  # မဝယ်သည့် အကြောင်းအရာနှင့် တည်နေရာကို (eastus2 အကြံပြုသည်) ရွေးပါ။
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # စာရင်းသွင်းမှုနှင့် တည်နေရာ (eastus2 အကြံပြုသည်) ကိုရွေးချယ်ပါ။
```

> **Note:** timeout error (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) ဖြစ်လျှင် `azd up` ကို ထပ်မံ သုံးရန်။ Azure resource များ နောက်ခံတွင် ထပ်မံ provisioning ဖြစ်နေပြီး ပြန်စမ်းသုံးသည်ဖြင့် resource များ terminal အခြေအနေသို့ ပြောင်းလဲသည့်အခါ deployment ပြီးမြောက်သည်။

ဒီအကြောင်းအရာမှာ:
1. GPT-5.2 နှင့် text-embedding-3-small မော်ဒယ်များပါဝင်သော Azure OpenAI resource တပ်ဆင်ပေးသည်
2. project root တွင် .env ဖိုင်ကို အလိုအလျောက်ဖန်တီးထားသည်
3. လိုအပ်သည့် environment variables များအားလုံးကို အပြည့်အဝ ဖော်ဆောင်ပေးသည်

**Deployment ပြဿနာ ရှိပါသလား?** [Infrastructure README](infra/README.md) တွင် နောက်ဆုံးဖြေရှင်းနည်းများ၊ subdomain conflict များ၊ Azure Portal တွင် လက်ဖြင့် တပ်ဆင်နည်းများ၊ မော်ဒယ် ညှိနှိုင်းမှု လမ်းညွှန်ချက်များ ပါရှိသည်။

**Deployment အောင်မြင်မကြောင်း စစ်ဆေးရန်:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY စသည်တို့ကို ပြသသင့်သည်။
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY လို အချက်အလက်များကို ပြသရမည်။
```

> **Note:** `azd up` က .env ဖိုင်ကို အလိုအလျောက် ဖန်တီးပေးသည်။ နောက်တွင် ပြောင်းလဲလိုပါက၊ ကိုယ်တိုင် .env ဖိုင်ကို ပြင်၊ သို့မဟုတ် အောက်ပါ commands ဖြင့် ထပ်မံဖန်တီးနိုင်သည် -
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


## အပလီကေးရှင်းကို ဒေသံတွင်းတွင် စတင် စမ်းသပ်ခြင်း

**Deployment စစ်ဆေးရန်:**

Root ဖိုဒါတွင် Azure ခြင်းများ .env ဖိုင် ရှိကြောင်း သေချာစေပါ -

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကို ပြရန် လိုသည်
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT၊ API_KEY၊ DEPLOYMENT ကို ပြသသင့်သည်။
```

**အပလီကေးရှင်းများ စတင်ရန်:**

**ရွေးချယ်မှု ၁: VS Code သုံးသူများအတွက် Spring Boot Dashboard အသုံးပြုခြင်း (အကြံပြု)**

Dev container တွင် Spring Boot Dashboard extension ပါသဖြင့် Spring Boot အပလီကေးရှင်းအားလုံးကို ရိုက်ကူးခွင့်ရှိသော UI ဖြင့် ထိန်းချုပ်နိုင်သည်။ VS Code ၏ ဘယ်ဖက်ဘားရီး လမ်းကြောင်းမှ ကိုယ်စားလှယ်အဖြစ် တွေ့ရမည် (Spring Boot အိုင်ကွန်ဖြင့် ခြေရာခံရမည်)။

Spring Boot Dashboard မှ -
- Workspace မှ Spring Boot အပလီကေးရှင်းများအားလုံး ကြည့်ရှုနိုင်သည်
- တစ်ချက်နှိပ် တည်းဖြင့် စတင်/ရပ်နားနိုင်သည်
- အပလီကေးရှင်း Log များကို အချိန်နှင့်တပြေးညီ ကြည့်ရှုနိုင်သည်
- အပလီကေးရှင်း status များကို ကြည့်အောင် စောင့်ကြည့်နိုင်သည်

"introduction" အပလီကေးရှင်း ဘယ်ဘက်တွင် ရှိသော Play button ကို နှိပ်ခြင်းဖြင့် ဤ module ကို စတင်နိုင်ပြီး၊ module များအားလုံးကို တပြိုင်တည်း စတင်လည်း ရင်ဆိုင်နိုင်သည်။

<img src="../../../translated_images/my/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**ရွေးချယ်မှု ၂: shell script များ အသုံးပြုခြင်း**

Web အပလီကေးရှင်းများအားလုံး စတင်ရန် (module 01 မှ 04):

**Bash:**
```bash
cd ..  # မူရင်း directory မှ
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # မူလ directory မှ ပေါ်ကနေ
.\start-all.ps1
```

သို့မဟုတ် ဤ module သာ စတင်ရန် -

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

ကိုယ်တိုင် shell scripts တွင် environment variables ကို root .env ဖိုင်မှ အလိုအလျောက် load လုပ်ပြီး၊ JAR မတွေ့ပါက တည်ဆောက်ပေးပါသည်။

> **Note:** စတင်ပြီးမှ  အားလုံး၏ modules ကို လက်ဖြင့် တည်ဆောက်လိုပါက -
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


http://localhost:8080 ကို web browser မှ ဖွင့်ကြည့်ပါ။

**ရပ်တန့်ရန်:**

**Bash:**
```bash
./stop.sh  # ဒီမော်ဒျူးတစ်ခုကိုသာသာ
# သို့မဟုတ်
cd .. && ./stop-all.sh  # အားလုံးသော မော်ဒျူးများ
```

**PowerShell:**
```powershell
.\stop.ps1  # ဒီမော်ဂျူးသာ
# သို့မဟုတ်
cd ..; .\stop-all.ps1  # မော်ဂျူးအားလုံး
```


## အပလီကေးရှင်း အသုံးပြုခြင်း

အပလီကေးရှင်းတွင် အသုံးပြုရန် ဘာသာစကားစကားပြောဆိုမှု နှစ်မျိုးကို ဘက်စုံတစ်ပြိုင်နက်တွင် တပ်ဆင်ထားသည်။

<img src="../../../translated_images/my/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Dashboard တွင် Simple Chat (stateless) နှင့် Conversational Chat (stateful) များကို ကြည့်ရှုနိုင်ပါသည်*

### Stateless Chat (ဘယ်ဘက် ပနယ်)

ဤကို အရင်စမ်းပါ။ "My name is John" ဟု မေးပြီးနောက် "What's my name?" ဟု မေးပါ။ မော်ဒယ်သည် ယခင် မှတ်ချက်ကို မှတ်မိပါမည်။ ၎င်းသည် ဘာသာစကား မော်ဒယ် ပုံစံ အခြေခံ ပြဿနာကို ပြသသည် - conversation context မရှိခြင်း။

<img src="../../../translated_images/my/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI သည် ယခင် မက်ဆေ့ချ်မှ သင့်အမည်ကို မှတ်မိခြင်း မရှိပါ*

### Stateful Chat (ညာဘက် ပနယ်)

ယခု ဒီဇိုင်းတွင် အလားတူ စဉ်ဆက်တွဲ ဖြေဆိုပါ။ "My name is John" ဟု မေးပြီးနောက် "What's my name?" ဟု မေးလျှင် ဒီတစ်ကြိမ် မှတ်မှတ်သားသား မှတ်မိသည်။ အဓိကက MessageWindowChatMemory ဖြစ်သည် - စကားပြောမှတ်တမ်း ထိန်းသိမ်း၍ တောင်းဆိုမှုတစ်ခုချင်းစီအတွက် context ထည့်ပေးသည်။ ထုတ်လုပ်မှု conversational AI ပုံစံကို ဤနည်းဖြင့် ဖန်တီးသည်။

<img src="../../../translated_images/my/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI သည် ယခင် စကားပြောမှတ်တမ်းမှ သင့်နာမည်ကို မှတ်သားထားသည်*

နှစ်ဖက် ပနယ်လုံးတွင် GPT-5.2 မော်ဒယ် တစ်ခုတည်းကို ပြုလုပ်ထားသည်။ တစ်ခုတည်း ကွာခြားချက်မှာ memory ဖြစ်ပြီး၊ ၎င်းသည် သင့်အပလီကေးရှင်းတွင် ရရှိသော အကျိုးသက်ရောက်မှုကို ဖော်ပြသည်။

## နောက်တစ်ဆင့်များ

**Next Module:** [02-prompt-engineering - GPT-5.2 နှင့် Prompt Engineering](../02-prompt-engineering/README.md)

---

**Navigation:** [← ယခင်: Module 00 - Quick Start](../00-quick-start/README.md) | [အဓိက သို့ ပြန်သွားရန်](../README.md) | [ရှေ့ဆက်: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**သတိပြုရန်**  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ဖြင့် ဘာသာပြန်ထားသည်။ ကျွန်ုပ်တို့သည် တိကျမှုကို ကြိုးစားဆောင်ရွက်ထားသော်လည်း ကွန်ပျူတာဘာသာပြန်မှုတွင် အမှားများ သို့မဟုတ် မှန်ကန်မှုမရှိမှုများ ရှိနိုင်သည်ကို သတိပြုပါ။ မူရင်းစာရွက်စာတမ်းကို အတည်ပြုနိုင်သော အဆိုအရင်းအမြစ်အဖြစ် ယူဆရန် လိုအပ်ပါသည်။ အရေးကြီးသည့် သတင်းအချက်အလက်များအတွက် လူမှန် ဘာသာပြန်သူများ၏ ထူးချွန်သော ဘာသာပြန်ခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်မှုမှ ဖြစ်ပေါ်လာသော မလွယ်ကူမှုများ သို့မဟုတ် မှားယွင်း နားလည်မှုပြဿနာများအတွက် ကျွန်ုပ်တို့ အာမခံချက် မရှိပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->