# Module 04: AI Agents with Tools

## Table of Contents

- [Video Walkthrough](../../../04-tools)
- [What You'll Learn](../../../04-tools)
- [Prerequisites](../../../04-tools)
- [Understanding AI Agents with Tools](../../../04-tools)
- [How Tool Calling Works](../../../04-tools)
  - [Tool Definitions](../../../04-tools)
  - [Decision Making](../../../04-tools)
  - [Execution](../../../04-tools)
  - [Response Generation](../../../04-tools)
  - [Architecture: Spring Boot Auto-Wiring](../../../04-tools)
- [Tool Chaining](../../../04-tools)
- [Run the Application](../../../04-tools)
- [Using the Application](../../../04-tools)
  - [Try Simple Tool Usage](../../../04-tools)
  - [Test Tool Chaining](../../../04-tools)
  - [See Conversation Flow](../../../04-tools)
  - [Experiment with Different Requests](../../../04-tools)
- [Key Concepts](../../../04-tools)
  - [ReAct Pattern (Reasoning and Acting)](../../../04-tools)
  - [Tool Descriptions Matter](../../../04-tools)
  - [Session Management](../../../04-tools)
  - [Error Handling](../../../04-tools)
- [Available Tools](../../../04-tools)
- [When to Use Tool-Based Agents](../../../04-tools)
- [Tools vs RAG](../../../04-tools)
- [Next Steps](../../../04-tools)

## Video Walkthrough

ဒီ module ကိုဘယ်လိုစတင်ဖွင့်မယ်ဆိုတာ ရှင်းပြထားတဲ့ လိုင်း live video ကို ကြည့်ပါ။

<a href="https://www.youtube.com/watch?v=O_J30kZc0rw"><img src="https://img.youtube.com/vi/O_J30kZc0rw/maxresdefault.jpg" alt="AI Agents with Tools and MCP - Live Session" width="800"/></a>

## What You'll Learn

အခုတစ်ချိန်ထိ AI နဲ့ စကားပြောနည်း၊ prompt တွေကို သေချာဖန်တီးနည်း၊ နှင့် မေးချက်တွေကို သင့်စာရွက်နဲ့ချိတ်ဆက်ပြီးဖြေကြားပေးနည်းတွေကို သင်ယူခဲ့ပြီဖြစ်ပါတယ်။ ဒါပေမဲ့ အခြေခံကန့်သတ်ချက်တစ်ခု ရှိတယ်၊ ဘာလဲဆိုတော့ ဘာသာစကားမော်ဒယ်တွေက စာသားပဲ ထုတ်လွှင့်နိုင်တယ်။ ရာသီဥတု စစ်ဆေးခြင်း၊ တွက်ချက်ခြင်း၊ ဒေတာဘေ့စ်ကို ရှာဖွေရန်၊ အပြင်စနစ်တွေနဲ့ အလုပ်လုပ်ခြင်း တွေကို မလုပ်နိုင်ပါဘူး။

Tools တွေက အဲဒီကို ပြောင်းလဲပေးတယ်။ မော်ဒယ်ကို function တွေကို ခေါ်နိုင်ဖို့ လက်လှမ်းပေးခြင်းဖြင့်၊ စာသားထုတ်လုပ်သူကနေ နောက်ထပ် လုပ်ဆောင်မှုယူနိုင်တဲ့ agent အဖြစ် ပြောင်းလဲသွားတယ်။ မော်ဒယ်က တိတိကျကျ ဘယ် tool လိုအပ်မယ်၊ ဘယ် tool ကိုသုံးမယ်၊ parameter များကို ဘယ်လိုပေးမယ်ဆိုတာ ဆုံးဖြတ်တယ်။ သင့်ကုဒ်က function ကို ဖုန်းခေါ်ပြီး ရလာဒ်ကို ပြန်ပေးတယ်။ မော်ဒယ်က အဲဒီရလဒ်ကို မေးခွန်းဖြေဆိုချက်ထဲ ထည့်သွင်းပေးတယ်။

## Prerequisites

- [Module 01 - Introduction](../01-introduction/README.md) ကိုပြီးစီးထားပြီးဖြစ်ရပါမယ် (Azure OpenAI resource တွေ deploy လုပ်ထားပြီး)
- လက်တလော အခြေခံ modules များကို အကြံပြုထားပြီးဖြစ်ရမယ် (ဒီ module က [Module 03 RAG concepts](../03-rag/README.md) ကို Tools vs RAG ကို နှိုင်းယှဉ်ရာမှာ အသုံးပြုထားပါတယ်)
- `.env` ဖိုင်ကို root directory မှာ Azure credentials နဲ့ရှိပြီးဖြစ်ရမယ် (Module 01 မှာ `azd up` နဲ့ ဖန်တီးထားတာ)

> **Note:** Module 01 ကို မပြီးဆိုရင် အရင်ဆုံး deployment လုပ်ရန် ညွှန်ကြားချက်များကို လိုက်နာပါ။

## Understanding AI Agents with Tools

> **📝 Note:** ဒီ module မှာ "agents" ဆိုသည့် ဝေါဟာရသည် tool-calling နည်းပညာဖြင့်တိုးမြှင့်ထားသော AI အကူအညီပေးများကို ဆိုလိုသည်။ ဒါဟာ [Module 05: MCP](../05-mcp/README.md) မှာ အသုံးပြုမယ့် **Agentic AI** တွင်ပါဝင်သည့် (အစီအစဉ်ရေးဆွဲခြင်း၊ မှတ်ဉာဏ်၊ နှင့် မျိုးစုံ reasoning နည်းစနစ်) autonomous agents ကွဲပြားခြားနားနေတာ ဖြစ်ပါတယ်။

Tools မပါသည့်အခါ၊ ဘာသာစကားမော်ဒယ်က သူ့ training data မှာမလိုက်လျောညီထွေ စာသားပဲ ထုတ်လုပ်နိုင်မှာ ဖြစ်တယ်။ ယနေ့ရက် ရာသီဥတု တွေလေးကို မေးရင် ခန့်မှန်းပဲ ပြောနိုင်ပါတယ်။ Tools တွေ ပေးလိုက်ရင်တော့ ရာသီ API ကို ခေါ်လိုက်နိုင်တယ်၊ တွက်ချက်လုပ်နိုင်တယ်၊ ဒေတာဘေ့စ်ကိုအရောင်းအဝယ် ဆောင်ရွက်နိုင်တယ်၊ ထိုရမှန်သော အချက်အလတ်များကို ဖြေကြားချက်ထဲ ထည့်ပေးနိုင်ပါတယ်။

<img src="../../../translated_images/my/what-are-tools.724e468fc4de64da.webp" alt="Without Tools vs With Tools" width="800"/>

*Tools မပါလျှင် မော်ဒယ်က ခန့်မှန်းချက်ပဲ ပေးနိုင်ပါတယ် — Tools များဖြင့် API များ ခေါ်နိုင်ပြီး တွက်ချက်ပြီး အချိန်နှင့်တပြေးညီ ဒေတာပေးနိုင်ပါတယ်။*

AI agent ရှိတယ်ဆိုရင် ReAct pattern (Reasoning and Acting) ကို လိုက်နာတယ်။ မော်ဒယ်က တုံ့ပြန်ခြင်းပြင် မဟုတ်ဘဲ ကောင်းသော ဖော်ပြချက်အတွက် လိုအပ်သည့် အချက်အလက်ကို စဉ်းစားပြီး tool ကို ခေါ်ဆိုခြင်း၊ ရလဒ်ကို သတိထား စစ္ဆေးခြင်း၊ ပြန်လုပ်ဆောင်ခြင်း သို့မဟုတ် နောက်ဆုံးဖြေကြားချက် မပေးမီ ဆုံးဖြတ်ခြင်းတို့ကို လုပ်ဆောင်တယ်။

1. **Reason** — အသုံးပြုသူမေးခွန်းကို စူးစမ်းပြီး လိုအပ်တဲ့ အချက်အလက်နဲ့ ဆိုက်ထားတယ်
2. **Act** — တိကျတဲ့ tool ကို ရွေးချယ်ပြီး parameter တွေ ကို စနစ်တကျ ထုတ်လုပ်ကာ ခေါ်ဆိုတယ်
3. **Observe** — tool က ပြန်လာတဲ့ output ကို ရယူပြီး သုံးသပ်တယ်
4. **Repeat or Respond** — ဒေတာပိုမိုလိုအပ်နေဆဲဆိုရင် ချိတ်ဆက်ပြန်လုပ်; မဟုတ်ရင် သဘာဝဘာသာစကားဖြင့် ပြန်ဖြေတယ်

<img src="../../../translated_images/my/react-pattern-detail.96a5efeeb6dd2f61.webp" alt="ReAct Pattern" width="800"/>

*ReAct စနစ်အတိုင်း — အေးဂျင့်သည် အလုပ်လုပ်ရန် ထိုက်တန်မှုကို ဆုံးဖြတ်၊ tool ကို ခေါ်ဆိုပြီး ရလဒ်ကတော့ စစ်ဆေးသည်။ ပြီးရင် ထပ်လုပ်မလုပ် ဆုံးဖြတ်ပြီး အဆုံးဖြင့် သဘာဝဘာသာဖြင့် ဖြေဆိုသည်။*

ဒီလုပ္ငန္းစဥ္ဟာ အလိုအေလ်ာက္ျဖစ္ၿပီး သင္က tool မ်ားနဲ႔ ၎တို႔၏ ဖေါ်ပြချက်ေတြကို သတ်မှတ်ပေးရုံပါပဲ။ မော်ဒယ်က ဘယ် tool ကို ဘယ်အချိန် သုံးမယ် ဆိုတာကို ကိုယ်တိုင် အာရုံစိုက်မှု ယူပါတယ်။

## How Tool Calling Works

### Tool Definitions

[WeatherTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) | [TemperatureTool.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/TemperatureTool.java)

သင်၏ function များကို ဖော်ပြချက်ပုံစံနဲ့ parameter သတ်မှတ်ချက်ဖြင့် ကောင်းကောင်း မျှတစွာ ဖန်တီးရပါမယ်။ မော်ဒယ်က ဒီဖေါ်ပြချက်တွေကို system prompt မှာ မြင်ရပြီး ဘယ် tool က ဘာလုပ်တယ်ဆိုတာ နားလည်တယ်။

```java
@Component
public class WeatherTool {
    
    @Tool("Get the current weather for a location")
    public String getCurrentWeather(@P("Location name") String location) {
        // သင့်ရာသီဥတု ရှာဖွေရေး အတွေးအခေါ်
        return "Weather in " + location + ": 22°C, cloudy";
    }
}

@AiService
public interface Assistant {
    String chat(@MemoryId String sessionId, @UserMessage String message);
}

// အကူအညီပေးသည်ကို Spring Boot မှ အလိုအလျောက် ချိတ်ဆက်ပေးသည် -
// - ChatModel bean
// - @Component တို့မှ @Tool နည်းလမ်းများအားလုံး
// - အစည်းအဝေး စီမံခန့်ခွဲမှုအတွက် ChatMemoryProvider
```

အောက်ကပုံက annotation တစ်ခုချင်းစီ၏ အဓိပ္ပါယ် ပုံဖော်ထားပြီး AI ကို ဘယ်အချိန် tool ခေါ်မလဲ၊ ဘယ် parameter များ ထည့်ပေးမလဲ ဆိုတာ နားလည်စေဖို့ ဘယ်လို အကူအညီဖြစ်ပေးတယ်ဆိုတာ ပြထားပါတယ်။

<img src="../../../translated_images/my/tool-definitions-anatomy.f6468546037cf28b.webp" alt="Anatomy of Tool Definitions" width="800"/>

*Tool အဓိပ္ပါယ်ပုံဖော်ခြင်း — @Tool က AI ကို ဘယ်အချိန်သုံးမလဲ ပြောပြ၊ @P က parameter တစ်ခုချင်းဖေါ်ပြပြီး၊ @AiService က စနစ်အလုပ်စတင်တဲ့အခါ ဆက်စပ်ပေးတယ်။*

> **🤖 GitHub Copilot Chat နဲ့ လေ့လာကြည့်ပါ။** [`WeatherTool.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/tools/WeatherTool.java) ဖိုင်ကို ဖွင့်ပြီး မေးမြန်းပါ -
> - "OpenWeatherMap ကဲ့သို့ရော မော်ဒယ်မှာ မိတျ data မဟုတ်ဘဲ တကယ့် ရာသီ API ကို ဘယ်လို ပေါင်းစပ်မလဲ။"
> - "AI အကောင်းဆုံးသုံးစွဲဖို့ ကောင်းမွန်တဲ့ tool ဖေါ်ပြချက်ရဲ့ အရေးအကြောင်းပါလား?"
> - "API error များနှင့် rate limit များကို tool ျပုလုပ်ရာတွင် ဘယ်လို ကိုင်တွယ်ရမလဲ?"

### Decision Making

အသုံးပြုသူက "Seattle ရာသီဥတုဘာလဲ?" မေးတဲ့အခါ မော်ဒယ်က မတွန်းတင်ပစ်လို့ မဟုတ်ဘဲ သုံးစွဲသူရဲ့ ရည်ရွယ်ချက်နဲ့ သူ့စနစ်ထဲရှိ တစ်ချက်ချင်း tool ဖော်ပြချက်များကို နှိုင်းယှဉ်၊ သင့်တော်မှုအဆင့်သတ်မှတ်ပြီး အကောင်းဆုံးကို ရွေးချယ်တယ်။ ပြီးရင် function call ကို บาคาร่า parameter ကို `"Seattle"` လို့ သတ်မှတ်ပြီး ဖန်တီးပြသတယ်။

အသုံးပြုသူ မေးချက်နဲ့ ကိုက်တာ tool မရှိရင် မော်ဒယ်ဟာ သူ့ရဲ့ ဆောင်ရွက်ရမယ့် အချက်အလက်လွှမ်းခြုံ မူရင်းအရ ပြန်ဖြေဆိုတယ်။ တင့်ကား tool အများကြီးကိုက်ရင်တော့ အတိအကျဆုံးကို သတ်မှတ်တယ်။

<img src="../../../translated_images/my/decision-making.409cd562e5cecc49.webp" alt="How the AI Decides Which Tool to Use" width="800"/>

*မော်ဒယ်က အသုံးပြုသူ ရည်ရွယ်ချက်ကို နားလည်သဘောပေါက်ကာ ကျွန်မတွေ ပေးထားတဲ့ tool တစ်ခုချင်းစီနှင့် နှိုင်းယှဉ်ပြီး အကောင်းဆုံးကို ရွေးချယ်တယ်။ ဒါကြောင့် စာသား ဖေါ်ပြချက် တိကျ မှန်ကန်ဖို့ အရေးကြီးတယ်။*

### Execution

[AgentService.java](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java)

Spring Boot က declarative `@AiService` interface ကို register လုပ်ထားတဲ့ tool များနဲ့ အလိုအလျောက် ချိတ်ဆက်လိုက်တယ်။ LangChain4j က tool ခေါ်ဆိုမှုတွေကို အလိုအလျောက် ဆောင်ရွက်တယ်။ နောက်ကြောင်းမှာ အသုံးပြုသူ မေးခွန်းကနေ စပြီး tool ကို ရွေးချယ်ခြင်း၊ ကုဒ်ပြန်လည်ရေးဆွဲခြင်း မှ tool ဖိတ်ခေါ်မှု အဆင့်ခြောက်ခုဖြတ်ပြီး ပြန်ဖြေသောက်မှုသို့ ရောက်ရှိတယ်။

<img src="../../../translated_images/my/tool-calling-flow.8601941b0ca041e6.webp" alt="Tool Calling Flow" width="800"/>

* အသုံးပြုသူမေးရင် မော်ဒယ်က tool ရွေးချယ်၊ LangChain4j က tool ကို တည်ဆောက်ပြီး ပြန်လာသောရလဒ်ကို မော်ဒယ်က ဖြေကြားချက်ထဲ ထည့်ပေးတာ မျက်နှာဖုံးအားလုံးနှင့်အတူ ဦးတည်ချက်*

[Module 00 - Quick Start](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) ကို ပြေးချိန်မှာ ဒီတူသာပုံစံကို တွေ့မယ် — Calculator tool များကို အဲဒီလို တူတူ ခေါ်လိုက်တယ်။ အောက်မှာ ဆက်ခံ ပုံပြင်နဲ့ tool ခေါ်သုံးမှုကို သရုပ်ဖော်ထားသည်။

<img src="../../../translated_images/my/tool-calling-sequence.94802f406ca26278.webp" alt="Tool Calling Sequence Diagram" width="800"/>

*Quick Start demo က tool ခေါ်မှု တစ်ကြိမ်ကျရင် loop ပတ်တာ — `AiServices` က message နဲ့ tool schema များကို LLM သို့ ပို့ပြီး LLM က `add(42, 58)`လို function call ပြန်တယ်၊ LangChain4j က Calculator method ကို locally ထုတ်ဆောင်ကာ ပြန်လည် ဖြေကြားတယ်။*

> **🤖 GitHub Copilot Chat နဲ့ လေ့လာကြည့်ပါ။** [`AgentService.java`](../../../04-tools/src/main/java/com/example/langchain4j/agents/service/AgentService.java) ဖိုင်ကို ဖွင့်ပြီး မေးမြန်းပါ -
> - "ReAct pattern ဘယ်လို အလုပ်လုပ်ပြီး AI agents အတွက် ဘာကြောင့် ထိရောက်လဲ?"
> - "Agent က ဘယ် tool ကို ဘယ်လို နှုန်းအတိုင်း သုံးမလဲ ဆုံးဖြတ်မလဲ?"
> - "Tool execution ပျက်ကွက်ခဲ့ရင် ဘာဖြစ်မလဲ - error တွေကို ဘယ်လို ပြန်ကြားရမလဲ?"

### Response Generation

မော်ဒယ်က ရာသီဥတုဒေတာကို လက်ခံပြီး အသုံးပြုသူအတွက် သဘာဝဘာသာစကားဖြင့် ဖြေကြားချက် တစ်ခု ဖန်တီးတယ်။

### Architecture: Spring Boot Auto-Wiring

ဒီ module မှာ LangChain4j ရဲ့ Spring Boot ပေါင်းစည်းမှုကို အသုံးပြုပြီး declarative `@AiService` interface တွေနဲ့ စနစ်တကျဆက်စပ်တယ်။ စတင်တဲ့အခါမှာ Spring Boot က `@Tool` methods ပါဝင်တဲ့ `@Component` တွေ၊ သင့်ရဲ့ `ChatModel` bean နဲ့ `ChatMemoryProvider` ကို ရှာဖွေထားပြီး အားလုံးကို `Assistant` interface တစ်ခုထဲ ချိတ်ဆက်ပေးတယ်၊ manual boilerplate အလုံးစုံကင်းစင်စေပါတယ်။

<img src="../../../translated_images/my/spring-boot-wiring.151321795988b04e.webp" alt="Spring Boot Auto-Wiring Architecture" width="800"/>

*@AiService interface က ChatModel၊ tool components နဲ့ memory provider တွေကို ဆက်သွယ်ပြီး Spring Boot က အလိုအလျောက် wiring တာဝန်ယူပေးတယ်။*

HTTP request ကနေ controller၊ service၊ auto-wired proxy ကနေ tool execution အပြီး မပြန်တတ်ချိုင့် အားလုံးရဲ့ request life cycle ကို sequence diagram သရုပ်ဖော်ထားပါတယ်။

<img src="../../../translated_images/my/spring-boot-sequence.f83e3d485aa4a3c6.webp" alt="Spring Boot Tool Calling Sequence" width="800"/>

*Complete Spring Boot request life cycle — HTTP request ကနေ controller ဟာ service က auto-wired Assistant proxy ကိုဖြတ်ပြီး LLM နဲ့ tool calls ကို အလိုအလျောက် စီမံထိန်းချုပ်ပေးတယ်။*

ဒီဟာရဲ့ အားသာချက်အချို့ -

- **Spring Boot auto-wiring** — ChatModel နဲ့ tool များကို အလိုအလျောက် ထည့်သွင်းပေးတယ်
- **@MemoryId pattern** — session အခြေခံ memory ကို အလိုအလျောက် စီမံပေးတယ်
- **Single instance** — Assistant ကို တစ်ကြိမ်ဖန်တီးပြီး ပြန်လည်သုံးစွဲခြင်းဖြင့် ပေးဆောင်မှု မြန်ဆန်စေတယ်
- **Type-safe execution** — Java method များကို တိုက်ရိုက် call ပြုလုပ်ကာ type conversion ဖြင့် အမှားကင်းစေတယ်
- **Multi-turn orchestration** — tool chaining ကို အသုံးပြုမှု အလိုအလျောက်စီမံတယ်
- **Zero boilerplate** — manual `AiServices.builder()` ယူဆောင်မှု၊ memory HashMap ကင်းစင်တယ်

Manual `AiServices.builder()` အသုံးပြုမှုကတော့ပိုကုဒ်တွေ လိုအပ်ပြီး Spring Boot ပေါင်းစည်းမှုကအကျိုးအမြတ်တွေ မရနိုင်ပါ။

## Tool Chaining

**Tool Chaining** — Tool-based agents တွေဘယ်တော့ အချစ်ဆုံး ပါဝင်တာလဲ ဆိုတော့ single question တစ်ခု လည်း multiple tools လိုအပ်တဲ့အခါပါ။ "Seattle ရာသီဥတု Fahrenheit ဘယ်လောက်လဲ?" ဆိုလိုက်ရင် agent က tools နှစ်ခုလိုက်ပြီး ခေါ်တယ် — ပထမဆုံးမှာ `getCurrentWeather` ကို Celsius အပူချိန်ယူဖို့ခေါ်၊ ပြီးရင် အဲဒီ result ကို `celsiusToFahrenheit` ကို ပြောင်းရေးဖို့ ပေးတယ် — တစ်ချက် ပြောဆိုမှုတစ်ခုထဲမှာပဲ ဖြစ်တယ်။

<img src="../../../translated_images/my/tool-chaining-example.538203e73d09dd82.webp" alt="Tool Chaining Example" width="800"/>

*Tool chaining အသုံးပြုမှု — getCurrentWeather ကို ခေါ်ပြီး Celsius result ကို celsiusToFahrenheit သို့ ပေးပို့၊ နှစ်ခုရဲ့ ဖြေချက် ပေါင်းစပ်ဖေါ်ပြသည်။*

**Graceful Failures** — mock data မပါတဲ့မြို့တစ်မြို့ ရာသီကို မေးလိုက်ရင် tool က error message ပြန်ပေးတယ်၊ AI ကလည်း မဖြစ်နိုင်ပါဘူး လို့ ရိုးရှင်းပြောတယ်၊ အရေပြားကျဆင်းခြင်းမရှိပါဘူး။ Tool မအောင်မြင်တဲ့အခါ agent က ထိန်းသိမ်းခြင်း လုပ်တယ်။ အောက်ဖော်ပြထားသလို error ကိုင်တွယ်မှုရှိသလို၊ ကျန်မရှိဘဲ crash ဖြစ်နေတာ မဟုတ်ဘူး။

<img src="../../../translated_images/my/error-handling-flow.9a330ffc8ee0475c.webp" alt="Error Handling Flow" width="800"/>

*Tool မှားယွင်းရင် agent က error ကို ဖမ်းပြီး အကူအညီလိုအပ်လို့ ဖြစ်တယ်လို့ ရှင်းပြပြီး crash မဖြစ်စေပါ။*

ဒီအရာတွေ တစ်ခါတည်း စကားပြောနေစဉ် ပြုလုပ်တယ်။ Agent က tools အများအပြားကို ကိုယ်တိုင်စီမံခိုင်းပေးတယ်။

## Run the Application

**Deployment စစ်ဆေးခြင်း:**

Module 01 မှာ `.env` ဖိုင် Azure credentials တွေအပါအဝင် ကို root directory မှာ ဖန်တီးပြီးသား ဖြစ်မှသာ အတည်ပြုပါ။ ဒီ module folder ကနေ (`04-tools/`) ဒီ command တွေကို လည်ပတ်ပါ။

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကိုပြသသင့်သည်။
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကို ပြရန် လိုသည်။
```

**Application စတင်ခြင်း:**

> **Note:** မီတာ root directory မှာ `./start-all.sh` ပေးပြီး applications အားလုံးကို စတင်ပြီးသားဆိုရင် Module 04 က http://localhost:8084 မှာ စတင်ပြင်ဆင်ပီးဖြစ်ပါတယ်။ အောက်မှာ စတင် commands မလိုတော့ပါ၊ တိုက်ရိုက် အဲဒီလိပ်စာသို့ သွားလို့ရပါတယ်။

**Option 1: Spring Boot Dashboard အသုံးပြုခြင်း (VS Code အသုံးပြုသူများအတွက် အကြံပြုချက်)**

Dev container ထဲမှာ Spring Boot Dashboard extension ပါပြီး၊ တစ်နေရာတည်းက Spring Boot application တွေကို ကြည့်ရှု စီမံနို်င်ပါတယ်။ VS Code ရဲ့ ဘယ်ဘက် Activity Bar မှာ (Spring Boot အစီအစဉ် icon) ရှာနိုင်ပါတယ်။

Spring Boot Dashboard က -

- Workspace ထဲရှိ Spring Boot application အားလုံးကို ပြောပြပေးတယ်
- တစ်နှိပ်နဲ့ application အားလုံးကို စတင်/ရပ်တန့် စေတယ်
- Application log များကို real-time ထွက်ပေါ်မှုကြည့်ရှုနိုင်
- Application အခြေအနေသေပွဲကို စောင့်ကြည့်နို်င်စေတယ်
တစ်ခါတည်း ဒီမော်ဂျူးအား စတင်ရန် "tools" အနားရှိ play ခလုတ်ကို နှိပ်လိုက်ပါ၊ 又သိုတစ်ခါတည်း အားလုံးအတွက် မော်ဂျူးများအား စတင်နိုင်သည်။

VS Code တွင် Spring Boot Dashboard ယခုကဲ့သို့ ပေါ်ပေါက်ပါသည် -

<img src="../../../translated_images/my/dashboard.9b519b1a1bc1b30a.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code တွင် Spring Boot Dashboard — မော်ဂျူးအားလုံးကို တစ်နေရာကနေ စတင်၊ တားမြစ် နှင့် စောင့်ကြည့်နိုင်ပါသည်*

**နည်းလမ်း ၂: shell scripts အသုံးပြုမှု**

ဝက်ဘ်အပလီကေးရှင်းများအားလုံး စတင်ပါ (module 01-04):

**Bash:**
```bash
cd ..  # ရူ့တ် ဒါရက်ထရီမှ
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # အမြစ် ဖိုလ်ဒါမှ
.\start-all.ps1
```

သို့မဟုတ် ဒီမော်ဂျူးပဲ စတင်ရန်:

**Bash:**
```bash
cd 04-tools
./start.sh
```

**PowerShell:**
```powershell
cd 04-tools
.\start.ps1
```

နှစ်ခုစလုံးစစ်ဆေးသည့် shell script များသည် အေရာင်းလုပ်မြင်သူ .env ဖိုင်မှ ပတ်ဝန်းကျင်တွင်ရှိသည့်ပြောင်းလဲမှုများကို အလိုအလျောက်โหลดပြီး JAR မရှိပါက တည်ဆောက်ပေးပါမည်။

> **မှတ်ချက်:** မည်သည့်အချိန်တွင် မော်ဂျူးအားလုံးကို စခဲ့ရန် မျှော်လင့်လျှင်:
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

http://localhost:8084 ကို မိုဘိုင်း browser တွင် ဖွင့်ပါ။

**ရပ်စဲရန်:**

**Bash:**
```bash
./stop.sh  # ဤမော်ဒျူးသာ
# သို့မဟုတ်
cd .. && ./stop-all.sh  # မော်ဒျူးအားလုံး
```

**PowerShell:**
```powershell
.\stop.ps1  # ဤမော်ဂျူးလ်သာ
# သို့မဟုတ်
cd ..; .\stop-all.ps1  # မော်ဂျူးလ်အားလုံး
```

## အပလီကေးရှင်းအသုံးပြုခြင်း

အပလီကေးရှင်းတွင် AI agent အား အသုံးပြု၍ ရေရှည်ရာသီအခြေအနေများနှင့် အပူချိန်ပြောင်းလဲခြင်း ကိရိယာများသုံးနိုင်သော ဝက်ဘ်အင်တာဖေ့စ်ပါဝင်ပါတယ်။ အင်တာဖေ့စ်မှာ လျင်မြန်စတင်အသုံးပြုနိုင်သော ဥပမာများနှင့် ဂျုံပြန်ပေးပို့ရန် ချက်ပြား ပါဝင်ပါတယ် -

<a href="images/tools-homepage.png"><img src="../../../translated_images/my/tools-homepage.4b4cd8b2717f9621.webp" alt="AI Agent Tools Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*AI Agent Tools အင်တာဖေ့စ် - စတင်လျင်မြန်သောဥပမာများ နှင့် ကိရိယာများနှင့် ဆက်သွယ်ရန် ချက်ပြား*

### ရိုးရိုးလေး ကိရိယာအသုံးပြုမှု စမ်းသပ်ကြည့်ပါ

ရိုးရိုးလေးတောင်းဆိုမှုဖြင့် စတင်ပါ - "100 ဒီဂရီ Fahrenheit ကို Celsius သို့ ပြောင်းပါ" AI agent သည် အပူချိန်ပြောင်းလဲခြင်းအတွက် ကိရိယာတောင်းခံရန် လိုအပ်ကြောင်း သတိပြု၍ အထောက်အထားများနှင့် အတူ အသုံးပြု၍ ရလဒ်ပြန်လာသည်။ ရိုးရိုးစွာ ဘာ့လဲဆိုရင် သင်မှန်းဖြစ်သော ကိရိယာကို ဘယ်လိုသွားခေါ်မလဲဆိုတာကို မလုပ်ပေးပဲ သဘာဝကျစွာ ခံစားရပါလိမ့်မယ်။

### ကိရိယာချိတ်ဆက်မှု စမ်းသပ်ကြည့်ပါ

အခုတော့ ပိုရှုပ်ထွေးအောင် စမ်းပါ -"Seattle ရဲ့ ရာသီဥတု ဘာလိုရာ နဲ့ Fahrenheit သို့ ပြောင်းပါ" AI agent သည် အဆင့်ဆင့် လုပ်ဆောင်နှင့် ရာသီဥတုရဲ့ Celsius အချက်အလက်ကိုရရှိပြီး Fahrenheit သို့ ပြောင်းရန်လိုအပ်ကြောင်း သတိပြု၊ ပြောင်းလဲခြင်းကိရိယာကိုခေါ်ယူပြီး နှစ်ခု ဒေတာကိုပေါင်းစပ်ထားသော တုံ့ပြန်မှုတစ်ခုအဖြစ် ထုတ်ပေးသည်။

### စကားပြောဆက်ဆံမှု လည်ပတ်ပုံ ကြည့်ရှုပါ

ချက်ပြားအင်တာဖေ့စ်သည် စကားပြောမှတ်တမ်းကို ထိန်းသိမ်းထားပြီး မကြာခဏပြန်လည်ဆက်ဆံမှုများလုပ်နိုင်စေပါသည်။ ယခင်မေးခွန်းများနှင့် တုံ့ပြန်ချက်များအားလုံး ပြသထားပြီး သက်ဆိုင်ရာ အကြောင်းအရာကို လေ့လာသိရှိနားလည်ရန် လွယ်ကူစေပါသည်။

<a href="images/tools-conversation-demo.png"><img src="../../../translated_images/my/tools-conversation-demo.89f2ce9676080f59.webp" alt="Conversation with Multiple Tool Calls" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*အဆင့်မြင့်စကားပြောဆက်ဆံမှု - ရိုးရှင်းသော ပြောင်းလဲမှုများ၊ ရာသီဥတုသိရှိမှုများနှင့် ကိရိယာချိတ်ဆက်မှုများ*

### မကွက်မဆပ် မေးခွန်းများစမ်းသပ်ပါ

အမျိုးမျိုးဖြင့် စမ်းသပ်ကြည့်ပါ -
- ရာသီဥတုစစ်ဆေးခြင်း: "Tokyo ရဲ့ ရာသီဥတု ဘာလဲ?"
- အပူချိန်ပြောင်းလဲခြင်း: "25°C သည် Kelvin ဘယ်လောက်လဲ?"
- ပေါင်းစပ်မေးခွန်းများ: "Paris ရဲ့ ရာသီဥတုကိုစစ်ပြီး 20°C ထက် အပူဆုံးလား ပြောပါ"

AI agent သည် သဘာဝဘာသာစကားကို ဖြေလျှော့၍ သင့်တော်သော ကိရိယာခေါ်ယူမှုများဖြင့် အတူတကွ လုပ်ဆောင်ပေးပါသည်။

## အဓိက စကားဝိုင်းများ

### ReAct အေကြာင်းအရင်း (စဉ်းစားခြင်း နှင့် လုပ်ဆောင်ခြင်း)

AI agent သည် ဆုံးဖြတ်ချက်ချခြင်း (ဘာကိုလုပ်မလဲ) နှင့် ကိရိယာအသုံးပြုခြင်းမှ လှမ်းတန်းပြန်လည်သွားလာပါသည်။ ဒီစနစ်ဖြင့် AI သည် ညွှန်ကြားချက်များကို ကျရောက်ခြင်းမဟုတ်ဘဲ ကိုယ်ပိုင်ဖြေရှင်းနိုင်မှု ရရှိပါသည်။

### ကိရိယာဖော်ပြချက်များ အရေးကြီး

သင့်ကိရိယာ ဖော်ပြချက်များ၏ အရည်အသွေးသည် AI agent ၏ အသုံးပြုမှုကို တိုက်ရိုက် သက်သာစေသည်။ ပြတ်သားသပ်ရပ်အောင် ဖော်ပြချက်များမှာ သတ်မှတ်ထားသောအချိန်မှာ မည်သည့်ကိရိယာကို ဘယ်လိုခေါ်မလဲကို ပြတ်သားသေချာနားလည်စေသည်။

### အစည်းအဝေးစီမံခန့်ခွဲမှု

`@MemoryId` @သတ်မှတ်ချက်သည် အလိုအလျောက် အစည်းအဝေးအလိုက် မှတ်ဉာဏ်စီမံခန့်ခွဲမှုကို အတည်ပြုသည်။ တစ်ခုစီ Session ID အလိုက် `ChatMemory` အကွန်ရက်များကို `ChatMemoryProvider` ဝန်ဆောင်မှုခွဲကတည်းက စီမံပေးပြီး အသုံးပြုသူများ မတူညီသော စကားပြောမှတ်တမ်း ဖြစ်ရန် အလိုအလျောက် သတ်မှတ်ထားသည်။ အောက်ပါ အစားထိုးပုံက အမျိုးမျိုးအသုံးပြုသူများကို သီးခြားမှတ်ဉာဏ်နေရာများသို့ ခွဲပေးနေသည်ကို ပြသသည်။

<img src="../../../translated_images/my/session-management.91ad819c6c89c400.webp" alt="Session Management with @MemoryId" width="800"/>

*တစ်ဦးချင်းစီ Session ID သည် အထူးသီးသန့် စကားပြောမှတ်တမ်းများကို ပြန်လည်စီမံ — အသုံးပြုသူများသည် တစ်ဦးမှ တစ်ဦး၏ သတင်းစာ မမြင်ရပါ။*

### အမှားကိုင်တွယ်ခြင်း

ကိရိယာများ မအောင်မြင်နိုင်ခြင်း ရှိသည် — API မတုံ့ပြန်နိုင်ခြင်း၊ အချက်အလက်များမှားယွင်းခြင်း၊ ပြင်ပ ဝန်ဆောင်မှုများ ပျက်ကွက်ခြင်း စတာတွေ ဖြစ်နိုင်ပါသည်။ အမှည့်များကို လက်ခံနိုင်ခြင်းသည် ထုတ်လုပ်ရေး AI agent များအတွက် မရှိမဖြစ်လိုအပ်ပါသည်။ ကိရိယာတစ်ခုမှ exception ပစ်ချခဲ့ပါက LangChain4j သည် ထို error မက်ဆေ့ချ်ကို မော်ဒယ်ထံပြန်ပို့ပြီး သဘာဝဘာသာဖြင့် ပြဿနာကို ဖော်ပြပေးနိုင်သည်။

## ရနိုင်သော ကိရိယာများ

အောက်ပါကိရိယာ များအား သုံး၍ ဖန်တီးနိုင်သော ရိုးရာ ပတ်ဝန်းကျင်တစ်ခုကို တူညီ ပုံစံကနေ ပြသထားသည်။ ဒီမော်ဂျူးတွင် ရာသီဥတုနှင့် အပူချိန်ကိရိယာများကို ဖေါ်ပြထားပေမယ့် `@Tool` ပုံစံသည် Java Method များအားလုံးသုံးနိုင်သည် - ဒေတာဘေ့စ်မေးခွန်းမှ စ၍ ငွေပေးချေမှုဆိုင်ရာ တာဝန်များထိ။

<img src="../../../translated_images/my/tool-ecosystem.aad3d74eaa14a44f.webp" alt="Tool Ecosystem" width="800"/>

*Java method တစ်ခုချင်းစီကို @Tool ဖြင့် အမှတ်အသားပြု၍ AI အသုံးပြုနိုင် - ဒီပုံစံသည် ဒေတာဘေ့စ်များ၊ API များ၊ အီးမေးလ်၊ ဖိုင်လုပ်ဆောင်မှုများအထိ ထပ်မံသုံးနိုင်သည်။*

## မည်သည့်အခါ ကိရိယာအခြေခံ AI agent များအသုံးပြုမလဲ

လွှဲမှားမှု မရှိစေမည့် အကြောင်းအရာများအတွက် မလိုအပ်ပါ။ ဆုံးဖြတ်ချက်မှာ AI သည် ပြင်ပစနစ်များနှင့် အပြန်အလှန်ဆက်သွယ်ရမည်လား သို့မဟုတ် ကိုယ်ပိုင်မင်္ဂလာမေးချင်းဖြင့် ဖြေကြားနိုင်မလားအပေါ် ဆုံးဖြတ်သည်။ အောက်ပါလမ်းညွှန်သည် ကိရိယာ အသုံးပြုသင့်သည့် အခါများနှင့် မလိုအပ်ဘဲဖြစ်သည့် အခါများကို လေ့လာစေပါသည်။

<img src="../../../translated_images/my/when-to-use-tools.51d1592d9cbdae9c.webp" alt="When to Use Tools" width="800"/>

*စိတ်ရှည်စွမ်းဆောင်မှု လမ်းညွှန် — ကိရိယာများမှာ ကိုင်တွယ်သောအချိန်မှာ အချိန်နဲ့ တွက်ချက်ချက်ချက်နှင့် လုပ်ဆောင်မှု အတွက်သာ အရေးပါသည်။ စိတ်ကူးစိတ်သန်းနှင့် စာရင်းပေးမေးခွန်းများတွင် မလိုအပ်ပါ။*

## ကိရိယာများ နှင့် RAG

Module 03 နှင့် 04 မှာ AI ၏ လုပ်နိုင်စွမ်းများကို တိုးမြှင့်ပေးရန် ခြားနားသည့် နည်းလမ်းချင်းဖြစ်ပါသည်။ RAG သည် မော်ဒယ်အား **နည်းပညာ** ကို ဖတ်ယူနိုင်စွမ်းပေးသည်။ ကိရိယာများသည် မော်ဒယ်အား **လှုပ်ရှားမှုများ** ခေါ်ယူနိုင်စွမ်း ပေးသည်။ အောက်ပါပုံသည် နှစ်ခုကို နှိုင်းယှဉ်ပြီး လုပ်ငန်းစဉ်နည်းလမ်းများနှင့် ကန့်သတ်ချက်များကို ဖော်ပြထားပါသည်။

<img src="../../../translated_images/my/tools-vs-rag.ad55ce10d7e4da87.webp" alt="Tools vs RAG Comparison" width="800"/>

*RAG သည် တည်ငြိမ်သော စာရွက်စာတမ်းများထံမှ သတင်းအချက်အလက် ရယူပေးသည် - ကိရိယာများ သည် လှုပ်ရှားမှုများ အကောင်အထည်ဖော်ပြီး တိုက်ရိုက်ဒေတာကို ရယူပေးသည်။ ထုတ်လုပ်ရေးစနစ်များအများစု ကိုယ်ပိုင်အဆင့်နှစ်ခုလုံး ပေါင်းစပ်အသုံးပြုကြသည်။*

အလေ့အကျင့်အဖြစ် ထုတ်လုပ်ရေးစနစ်များမှာ RAG ကို စာရွက်စာတမ်းအတွက် အသုံးပြု၍ အဖြေများအာရုံစူးစိုက်ခြင်း၊ Tools များကို သက်ဆိုင်ရာ ဒေတာများရယူခြင်း သို့မဟုတ် လုပ်ဆောင်မှုများအတွက် ပေါင်းစပ်အသုံးပြုသည်။

## နောက်တစ်ဆင့်များ

**နောက်တစ်ခဏ: [05-mcp - Model Context Protocol (MCP)](../05-mcp/README.md)**

---

**သွားရာလမ်းညွှန်:** [← ယခင်: Module 03 - RAG](../03-rag/README.md) | [ထိပ်သို့ ပြန်သွားရန်](../README.md) | [နောက်: Module 05 - MCP →](../05-mcp/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**အကြောင်းကြားချက်**  
ဒီစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ဖြင့် ဘာသာပြန်ထားခြင်းဖြစ်ပါသည်။ တိကျမှုအတွက် ကြိုးပမ်းဆောင်ရွက်သော်လည်း အလိုအလျောက်ဘာသာပြန်ခြင်းမှ အမှားများ သို့မဟုတ် ဖြစ်နိုင်သောမှားယွင်းချက်များ ပါရှိနိုင်ကြောင်း သတိပြုလိုပါသည်။ မူရင်းစာရွက်စာတမ်းကို လူမှုဘာသာဖြင့်သာ ဉပဒေရေးရာအတိုင်း အတည်ပြုရမည့်အရင်းအမြစ်အဖြစ် ယူဆရန် လိုအပ်ပါသည်။ အရေးကြီးသော သတင်းအချက်အလက်များအတွက် လူကြီးမင်းတရားဝင် လုပ်သက်ရှိ ဘာသာပြန်သူများ၏ ဘာသာပြန်ချက်ကို အသုံးပြုရန် အကြံပြုပါသည်။ ဤဘာသာပြန်ချက် အသုံးပြုမှုကြောင့် ဖြစ်ပေါ်နိုင်သည့် နားလည်မှုမှားယွင်းခြင်းများမှ ကျွန်ုပ်တို့ တာဝန်မယူပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->