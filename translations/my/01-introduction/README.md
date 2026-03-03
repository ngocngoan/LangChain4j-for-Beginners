# Module 01: LangChain4j ဖြင့် စတင်အသုံးပြုခြင်း

## အကြောင်းအရာ ဇယား

- [ဗွီဒီယို လမ်းညွှန်](../../../01-introduction)
- [သင်ယူမည့်အကြောင်းအရာများ](../../../01-introduction)
- [လိုအပ်ချက်များ](../../../01-introduction)
- [အဓိကပြဿနာနားလည်ခြင်း](../../../01-introduction)
- [တိုးကင်များနားလည်ခြင်း](../../../01-introduction)
- [မှတ်ဉာဏ်လုပ်ဆောင်ပုံ](../../../01-introduction)
- [LangChain4j ကို ဘယ်လိုအသုံးပြုသည်](../../../01-introduction)
- [Azure OpenAI အခြေခံအဆောက်အအုံ တပ်ဆင်ခြင်း](../../../01-introduction)
- [လက်ရှိ စက်ပေါ်တွင် အပလီကေးရှင်း ကို run ချခြင်း](../../../01-introduction)
- [အပလီကေးရှင်းအသုံးပြုခြင်း](../../../01-introduction)
  - [Stateless Chat (ဘယ်ဘက် Panel)](../../../01-introduction)
  - [Stateful Chat (ညာဘက် Panel)](../../../01-introduction)
- [နောက်တစ်ဆင့်](../../../01-introduction)

## ဗွီဒီယို လမ်းညွှန်

ဒီ module ကို စတင်အသုံးပြုနည်းကိုရှင်းပြထားတဲ့ လက်တွေ့မှတ်တမ်းကို ကြည့်ပါ။

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## သင်ယူမည့်အကြောင်းအရာများ

Quick start မှာ GitHub Models ကို prompt ပေးခြင်း၊ tools ခေါ်သုံးခြင်း၊ RAG pipeline တည်ဆောက်ခြင်းနှင့် guardrails စမ်းသပ်ခြင်းများကို သုံးခဲ့ပါတယ်။ ဒီ demos တွေက ဖြစ်နိုင်တာတွေကို ပြသထားသော်လည်း ယခုနေရာမှာ Azure OpenAI နဲ့ GPT-5.2 ကို သုံးပြီး ထုတ်လုပ်မှုစတိုင်းပုံစံ application တွေဖန်တီးမယ်။ ဒီ module က context ကို မှတ်မိထားပြီး state ကို ထိန်းသိမ်းထားတဲ့ စကားပြော AI အပေါ် အာရုံစိုက်ထားပါတယ်။ quick start demos တွေမှာ သုံးခဲ့တဲ့ concept တွေအကြောင်း ရှင်းပြခြင်းမရှိခဲ့ပါ။

ဒီအတွက် Azure OpenAI ရဲ့ GPT-5.2 ကို လမ်းညွှန်မှုတစ်လျှောက်လုံး သုံးမှာဖြစ်ပြီး ၎င်းရဲ့ မြင့်မားတဲ့ ယူဆချက်များကြောင့် မတူညီတဲ့ pattern များ၏ သဘောထားတွေ ပိုထင်ရှားစေပါတယ်။ မှတ်ဉာဏ် ပါဝင်လာတဲ့အခါ သင်မြင်နိုင်ပါလိမ့်မယ်။ ဒါဟာ သင့် application မှာ ဘာတွေ တင်ဆက်ပေးထားကြောင်း နားလည်ရခက်မယုံလောက်သေးတဲ့ ပြသာနာများကို ရှင်းလင်းဖို့ လွယ်ကူစေပါတယ်။

သင်တစ်ခုတည်းသော application တည်ဆောက်မှာဖြစ်ပြီး အောက်ပါနှစ်မျိုးသော pattern ကို ပြသပါမည်-

**Stateless Chat** - တစ်ခုချင်းစီသော တောင်းဆိုမှုမှာ လွတ်လပ်ပြီး အချို့သော ပြန်လည်မှတ်မိမှုမရှိပါ။ ၎င်းသည် quick start မှာ သုံးခဲ့သော pattern ဖြစ်သည်။

**Stateful Conversation** - တောင်းဆိုမှုတိုင်းတွင်စကားဝိုင်း ပုံပြင်ကိုပါ ပါဝင်သည်။ စက်ပညာသည် ပြန်ကြားသော turns များ တွင် context ကို ထိန်းသိမ်းထားသည်။ ၎င်းမှာ ထုတ်လုပ်မှု application များအတွက် လိုအပ်သည်။

## လိုအပ်ချက်များ

- Azure subscription နှင့် Azure OpenAI ဝင်ခွင့်
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **မှတ်ချက်။** Java, Maven, Azure CLI နဲ့ Azure Developer CLI (azd) များကို ပေးထားသော devcontainer မှာ ကြိုတင် ထည့်သွင်းပြီးပြီဖြစ်သည်။

> **မှတ်ချက်။** ဒီ module ဟာ Azure OpenAI ပေါ်မှာ GPT-5.2 ကို အသုံးပြုသည်။ deployment ကို `azd up` နဲ့ အလိုအလျောက်ပြုလုပ်ထားတာဖြစ်တဲ့အတွက် ကုဒ်အတွင်း model နာမည်ကို တည်းဖြတ်မသွားပါနဲ့။

## အဓိက ပြဿနာနားလည်ခြင်း

ဘာသာစကား မော်ဒယ်များသည် stateless ဖြစ်သည်။ API ခေါ်ဆိုမှုတစ်ခုစီ မတူညီမှုရှိသည်။ "ကျွန်တော့်နာမည်က John ပါ" လို့ ပေးပို့ပြီးနောက် "ကျွန်တော့်နာမည် ဘာလဲ?" ဟု မေးလျှင် မော်ဒယ်သည် မင်းနာမည် မသိအောင် ဖြစ်မနေခင် မိတ်ဆက်ချက်ကို မသတိရပါ။ တောင်းဆိုမှုတိုင်းကို ပထမဆုံး စကားပြောမှုလို မျှအောင် ပြုလုပ်သည်။

ဒါဟာ ရိုးရှင်းသော Q&A များအတွက် ပြဿနာမရှိပေမယ့် စတင်ကျောင်း application များအတွက် မသုံးနိုင်ပါ။ ဖောက်သည်ဝန်ဆောင်မှု bot များသည် သင့်ပြောသည်ကို မှတ်မိရန်လိုအပ်သည်။ ကိုယ်ပိုင် အကူအညီလက်ထောက်များတွင် သတင်းအချက်အလက်လိုအပ်သလိုပါ။ turn အနည်းငယ်ရှိသော စကားပြောမှုများတွင် မှတ်ဉာဏ်လိုအပ်ပါသည်။

အောက်ဖော်ပြပါ ရုပ်ပုံသည် နှစ်မျိုးသောနည်းလမ်းများနှိုင်းယှဉ်သည်- ဘယ်ဘက်တွင် stateless အခေါ်ဆိုမှုဖြစ်၍ နာမည် မမှတ်မိဘဲဖြစ်သည်။ ညာဘက်တွင် ChatMemory ဖြင့် မှတ်သားထားသော stateful call ဖြစ်သည်။

<img src="../../../translated_images/my/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Stateless (လွတ်လပ်ခေါ်ဆိုမှု) နှင့် Stateful (context သတိထားရှိမှု) စကားပြောမှုများအကြား ကွာခြားချက်*

## တိုးကင်များနားလည်ခြင်း

စကားပြောမှုများကို ရိုက်ကူးမတိုင်မီ token များကို နားလည်ရန် လိုအပ်သည် - ဘာသာစကား မော်ဒယ်များ လုပ်ဆောင်ရာတွင် သုံးသော စာသား ကဏ္ဍရပ်များဖြစ်သည်-

<img src="../../../translated_images/my/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*စာသားကို token အဖြစ် ခွဲထုတ်ညွှန်းနှိပ်ထားသော နမူနာ - "I love AI!" သည် ၄ ထပ်ခွဲစီသော တိုးကင်အဖြစ် ဖြစ်သည်*

Tokens တွေဟာ AI မော်ဒယ်များက စာသားကို တိုင်းတာသုံးသပ်ရာမှာ အသုံးပြုသောအရာ ဖြစ်ပါသည်။ စကားလုံးများ၊ ကန့်သတ်ချက်များနဲ့ နေရာများပါ token ဖြစ်သည့်အတွက် သင့်မော်ဒယ်တွင် တစ်ခုပြီးတစ်ခု စာသားကို တစ်ကြိမ်တည်းPROCESS လုပ်နိုင်မည့် token အရေအတွက် (GPT-5.2 အတွက် ၄သိန်းခန့်၊ input 272,000 နှင့် output 128,000 token ထိ) ရှိသည်။ token တွေက conversation ရဲ့ အရှည်နဲ့ ကုန်ကျစရိတ်ကို သုံးစွဲမြင်သာစေပါတယ်။

## မှတ်ဉာဏ်လုပ်ဆောင်ပုံ

Chat memory က stateless ပြဿနာကို ဖျောက်ပစ်ပေးပြီး စကားပြောမှုမှတ်တမ်းကို ထိန်းသိမ်းပါသည်။ မော်ဒယ်ကို တောင်းဆိုရန် မတိုင်မှီ framework သည် အရေးကြီးသော ယခင် စကားများကို နောက်တစ်ကြိမ် ပေါင်းထည့်လိမ့်မယ်။ "ကျွန်တော့်နာမည် ဘာလဲ?" ဟု မေးလျှင် စနစ်က စကားပြောမှတ်တမ်းအပြည့်အစုံကို ပေးပို့လိမ့်မယ်။ အဲနဲ့ မော်ဒယ်က မင်းဟာ "ကျွန်တော့်နာမည်က John ပါ" လို့ စကားပြောခဲ့တာကို မြင်ရမယ်။

LangChain4j က အဲ့ဒီ memory ကို အလိုအလျောက် ကိုင်တွယ်ပေးတဲ့ implementation များပေးထားပါတယ်။ သင် user message ယူရမယ့် အနည်းဆုံး အရေအတွက်ကို ရွေးချယ်ပြီး framework က context window ကို ကိုင်တွယ်ပေးတယ်။ အောက်ပါ ပုံတွင် MessageWindowChatMemory သည် မကြာသေးမီ message တွေကို sliding window နည်းဖြင့် ထိန်းထားပုံကို ရှင်းပြထားသည်။

<img src="../../../translated_images/my/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory သည် မကြာသေးတဲ့ message တွေကို sliding window နည်းဖြင့် ထိန်းသိမ်းရာ ဆိုးရွားသော message များကို အလိုအလျှောက် ဖြတ်တောက်ပေးသည်*

## LangChain4j ကို ဘယ်လိုအသုံးပြုသည်

ဒီ module က quick start ကို တိုးချဲ့ပြီး Spring Boot ကို ပေါင်းထည့်၊ စကားပြောမှု မှတ်ဉာဏ်ကို စွဲထိုးသည်။ အပိုင်းအစတွေ ဘယ်လိုပေါင်းသင့်ကြောင်း ဒီမှာရှင်းပြထားသည်-

**Dependencies** - LangChain4j library နှစ်ခု ထည့်သွင်းပါ:

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

**Chat Model** - Azure OpenAI ကို Spring bean အဖြစ် ပြင်ဆင်ပါ ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

Builder က `azd up` မှ လက်တွေ့သတ်မှတ်ထားသော environment variable က credential များကို ဖတ်ရှုသည်။ `baseUrl` ကို သင်ရဲ့ Azure endpoint သို့ သတ်မှတ်ခြင်းဟာ OpenAI client ကို Azure OpenAI နဲ့ သင့်ကိုက်ညီစေပါတယ်။

**Conversation Memory** - စကားပြောမှုသမိုင်းကို MessageWindowChatMemory ဖြင့် ပြုစုပါ ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` ဖြင့် မှတ်ဉာဏ်ကို တည်ဆောက်ကာ နောက်ဆုံး message ၁၀ ခုကို သိမ်းဆည်းပါ။ user message နဲ့ AI message တို့ကို အမျိုးအစားတိကျစွာ စာရွက်စာတမ်းပုံစံဖြင့် ထည့်ပါ- `UserMessage.from(text)` နဲ့ `AiMessage.from(text)` အသုံးပြုပြီး။ သမိုင်းဇာတ်ကောင်ကို `memory.messages()` ဖြင့် ရယူပြီး model ထံ ပို့ပါ။ service က စကားပြောမှု ID တိုင်းအတွက် အထူးသီးသန့် မှတ်ဉာဏ် instance များသိမ်းဆည်းလို့ အသုံးပြုသူ အများကြီး အချိန်တစ်ပြိုင်တည်း စကားပြောနိုင်သည်။

> **🤖 GitHub Copilot Chat ဖြင့် စမ်းသပ်ပါ:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) ဖွင့်ပြီးမေးပါ-
> - "MessageWindowChatMemory က window ပြည့်သောအခါ မက်ဆေ့ခ်ျတွေ ဘယ်လို ရွေးချယ် ဖယ်ရှားသလဲ?"
> - "In-memory မဟုတ်ဘဲ database ကို အသုံးပြုပြီး သေချာသော memory storage ကို ဖန်တီးနိုင်မလား?"
> - "ဟောင်းပြီး စကားပြောမှုသမိုင်း စုစည်းဖို့ သေချာတဲ့ စာရင်းချုပ်လုပ်ခြင်း (summarization) ထည့်နိုင်မလား?"

Stateless chat endpoint မှာ မှတ်ဉာဏ် မပါဝင်ပါ - quick start ကဲ့သို့ `chatModel.chat(prompt)` သာ အသုံးပြုသည်။ Stateful endpoint မှာ messages တွေကို memory ထဲထည့်ပြီး သမိုင်း ရယူကာ တောင်းဆိုမှုတိုင်းနှင့် context ပေါင်းထည့်သည်။ model setup တူပြီး pattern မတူပါ။

## Azure OpenAI အခြေခံအဆောက်အအုံ တပ်ဆင်ခြင်း

**Bash:**
```bash
cd 01-introduction
azd up  # စာရင်းသွင်းခြင်းနှင့် တည်နေရာကို ရွေးချယ်ပါ (eastus2 အကြံပြုချက်)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # အသင်းဝင်ပြီး တည်နေရာ (eastus2 အကြံပြုသည်) ရွေးချယ်ပါ။
```

> **မှတ်ချက်:** timeout ပြဿနာတက်ရင် (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) `azd up` ကို ထပ်မံ Run ပါ။ Azure resource များ နောက်ခံမှာ တပ်ဆင်နေဆဲဖြစ်တာကြောင့် retry လုပ်တာနဲ့ deployment အချိန်ပြီးသွားလိမ့်မယ်။

ဒီသည်များကို လုပ်ဆောင်ပါမည်-
1. GPT-5.2 နှင့် text-embedding-3-small မော်ဒယ်ပါ Azure OpenAI resource တပ်ဆင်ခြင်း
2. Credentials ပါ `.env` ဖိုင်ကို project root တွင် အလိုအလျောက် ထုတ်ပေးခြင်း
3. လိုအပ်သော environment variable အားလုံး စုပြီးသတ်မှတ်ခြင်း

**Deployment ပြဿနာရှိကျပါသလား?** subdomain မျိုးတွဲ အမည်ဖော်စားမှု၊ အက်မင်ကောင့် မှတဆင့် မန်နွယ် deployment လုပ်နည်းများနှင့် model setting လမ်းညွှန်ချက်များကို လေ့လာချင်ရင် [Infrastructure README](infra/README.md) ကို ကြည့်ပါ။

**Deployment ကအောင်မြင်သည်ကို သေချာစစ်ဆေးရန် -**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY စသည်တို့ကို ပြသသင့်သည်။
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY စသည့် အချက်အလက်များကို ပြသသင့်သည်။
```

> **မှတ်ချက်:** `azd up` က `.env` ဖိုင်ကို အလိုအလျောက် ပြုလုပ်ပေးသည်။ အချိန်ကြာပြီးနောက် update လုပ်ရန်လိုတယ်ဆိုရင် `.env` ဖိုင်ကိုလက်ဖြင့် ပြင်ဆင်နိုင်ပါသည်၊ ဒါမှမဟုတ် အောက်ပါ command များကို ပြန်လည် เรียပေးနိုင်ပါသည်-
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

## လက်ရှိ စက်ပေါ်တွင် အပလီကေးရှင်း ကို run ချခြင်း

**Deployment အောင်မြင်မှုကိုအတည်ပြုရန်:**

`.env` ဖိုင်သည် လက်ရှိ directory ထဲမှာ ရှိပြီး Azure credential များ ပါဝင်သည်ဟု သေချာစေပါ။ Module directory (`01-introduction/`) မှ run ပါ-

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကိုပြသသင့်သည်
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကိုပြသသင့်သည်
```

**Applications များစတင်ရန်:**

**နည်းလမ်း ၁: Spring Boot Dashboard အသုံးပြုခြင်း (VS Code အသုံးပြုသူများအတွက် အကြံပြုချက်)**

Dev container တွင် Spring Boot Dashboard extension ပါဝင်ပြီး၊ VS Code မှာ ဘယ်ဘက် Activity Bar တွင် Spring Boot icon မှတဆင့် မျက်နှာပြင် ထိန်းချုပ်နိုင်သည်။

Spring Boot Dashboard မှ
- Workspace ရှိ Spring Boot applications အားလုံးကိုကြည့်ရှုနိုင်သည်
- အပလီကေးရှင်းများကို တစ်ချက်အတွင်း Start/Stop နိုင်သည်
- လက်ရှိ အလုပ်လုပ်မှု log များကို real-time ကြည့်ရှုနိုင်သည်
- Application အခြေအနေကို စောင့်ကြည့်နိုင်သည်

"introduction" module အတွက် play ခလုတ်ကို click နှိပ်၍ စတင်ပါ၊ ဒါမှမဟုတ် အားလုံးသော modules များကို တပြိုင်နက် စတင်နိုင်သည်။

<img src="../../../translated_images/my/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code မှ Spring Boot Dashboard — module အားလုံးကို တစ်နေရာမှ စတင်၊ ရပ်နား နှင့် စောင့်ကြည့်ပေးခြင်း*

**နည်းလမ်း ၂: shell scripts အသုံးပြုခြင်း**

Web applications များအားလုံး စတင်ရန် (module 01 မှ 04 အထိ) -

**Bash:**
```bash
cd ..  # root ဂိုဏ်းမှ
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # မူလဖိုင်လမ်းကြောင်းမှ
.\start-all.ps1
```

သို့မဟုတ် ဤ module ကိုသာ စတင်ရန်-

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

script နှစ်ခုလုံးသည် root `.env` ဖိုင်ထဲက environment variable များကို သိမ်းဆည်းပြီး jar မရှိပါက build လုပ်ပေးပါသည်။

> **မှတ်ချက်:** မတိုင်မှီ မော်ဂျူးများအားလုံးကို လက်ဖြင့် build လုပ်ဖို့ စိတ်ဝင်စားတယ်ဆိုရင်-
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

http://localhost:8080 ကို browser မှ ဖွင့်ပါ။

**ရပ်ရန်:**

**Bash:**
```bash
./stop.sh  # ဒီမော်ဂျူးသာ
# ဒါမှမဟုတ်
cd .. && ./stop-all.sh  # အားလုံးမော်ဂျူးများ
```

**PowerShell:**
```powershell
.\stop.ps1  # ဒီမော်ဂျူးသာ
# ဒါမှမဟုတ်
cd ..; .\stop-all.ps1  # မော်ဂျူးအားလုံး
```

## အပလီကေးရှင်း အသုံးပြုခြင်း

အဆိုပါ application သည် နှစ်မျိုးသော chat implementation များ ရှိသည့် ဝက်ဘ် UI တစ်ခုကို ပေးသည်။

<img src="../../../translated_images/my/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*Simple Chat (stateless) နှင့် Conversational Chat (stateful) ပါဝင်သည့် Dashboard*

### Stateless Chat (ဘယ်ဘက် Panel)

ဤကို ပထမဆုံး စမ်းသပ်ပါ။ "ကျွန်တော့်နာမည်က John ပါ" ဟု ပြောပြီးနောက် "ကျွန်တော့်နာမည် ဘာလဲ?" ဟု မေးပါ။ မော်ဒယ်သည် မှတ်မိမရှိကာ တစ်ခုချင်းစီ ခွဲခြားသော message အနေနှင့် ၎င်းကိုဆင်ခြင်ပါသည်। ဤသည်သည် အခြေခံ language model integration တွင် ရရှိသော အဓိက ပြဿနာကို ပြသသည်- စကားပြောမှု context မပါခြင်း။

<img src="../../../translated_images/my/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI မှ မင်းနာမည်ကို ယခင် message မှ မမှတ်မိပါ*

### Stateful Chat (ညာဘက် Panel)

ယခုတစ်ချက်မှာ အလားတူ စမ်းသပ်ပါ။ "ကျွန်တော့်နာမည်က John ပါ"၊ "ကျွန်တော့်နာမည် ဘာလဲ?" မေးလိုက်သည်။ ဒီအကြိမ်မှာ မှတ်သားရပါသည်။ ကွာခြားချက်မှာ MessageWindowChatMemory ဖြစ်ပြီး စကားပြောမှတ်တမ်း ဖြစ်စဉ်ကို ထိန်းသိမ်းကာ တောင်းဆိုမှုတိုင်းတွင် ထည့်သွင်းသည်။ ထုတ်လုပ်မှု conversational AI များသည် ပုံမှန် အသုံးပြုနည်းဖြစ်သည်။

<img src="../../../translated_images/my/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI သည် စကားပြောမှု အစောပိုင်းက မင်းနာမည်ကို မှတ်သားထားသည်*

Panel နှစ်ခုစလုံးတွင် GPT-5.2 ကို အသုံးပြုသည်။ ကွာခြားချက်တစ်ခုတည်းမှာ မှတ်ဉာဏ်ဖြစ်သည်။ ၎င်းဟာ သင်၏ application တည်ဆောက်မှုကို ဘာတွေ ပေးရုံပဲ ဖြစ်စေ အရေးကြီးတယ်ဆိုတာ မျက်မှောက်ရှင်းပြပေးသည်။

## နောက်တစ်ဆင့်

**နောက် Module:** [02-prompt-engineering - GPT-5.2 ဖြင့် Prompt Engineering](../02-prompt-engineering/README.md)

---

**Navigation:** [← ယခင်: Module 00 - Quick Start](../00-quick-start/README.md) | [ပင်မစာမျက်နှာသို့ ပြန်မည်](../README.md) | [နောက်တစ်ခု: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**သတိပြုကြောင်း**  
ဤစာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှုဖြစ်သော [Co-op Translator](https://github.com/Azure/co-op-translator) အား အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ငါတို့သည် တိကျမှုအတွက် ကြိုးပမ်းသော်လည်း၊ စက်ရုပ်ဘာသာပြန်ခြင်းအတွက် မှားယွင်းမှုများ သို့မဟုတ် မတိကျမှုများ ပါဝင်နိုင်သည်ကို ကျေးဇူးပြု၍ သိရှိထားပါရန်။ မူရင်းစာတမ်းကို တိုင်းရင်းဘာသာဖြင့် အာမခံ စာရွက်စာတမ်းအဖြစ် ကြည့်ရန် သင့်တော်ပါသည်။ အရေးကြီးသတင်းအချက်အလက်များအတွက် မိမိလူ့ဘာသာပြန်သူ ပရော်ဖက်ရှင်နယ်က ဘာသာပြန်ခြင်း အကြံပြုပါသည်။ ဤဘာသာပြန်ချက် အသုံးပြုခြင်းမှ ဖြစ်ပေါ်လာနိုင်သည့် မစိမ်းမဒြပ်နားလည်မှုများ သို့မဟုတ် မှားလှည့်နားလည်မှုများအတွက် ကျွန်ုပ်တို့သည် တာဝန်မယူပါကြောင်း သတိပေးအပ်ပါသည်။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->