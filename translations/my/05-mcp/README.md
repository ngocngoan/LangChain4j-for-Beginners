# Module 05: Model Context Protocol (MCP)

## Table of Contents

- [What You'll Learn](../../../05-mcp)
- [What is MCP?](../../../05-mcp)
- [How MCP Works](../../../05-mcp)
- [The Agentic Module](../../../05-mcp)
- [Running the Examples](../../../05-mcp)
  - [Prerequisites](../../../05-mcp)
- [Quick Start](../../../05-mcp)
  - [File Operations (Stdio)](../../../05-mcp)
  - [Supervisor Agent](../../../05-mcp)
    - [Running the Demo](../../../05-mcp)
    - [How the Supervisor Works](../../../05-mcp)
    - [Response Strategies](../../../05-mcp)
    - [Understanding the Output](../../../05-mcp)
    - [Explanation of Agentic Module Features](../../../05-mcp)
- [Key Concepts](../../../05-mcp)
- [Congratulations!](../../../05-mcp)
  - [What's Next?](../../../05-mcp)

## What You'll Learn

သင်သည် စကားပြောအိုင်အေတစ်ခု ကို တည်ဆောက်ပြီး၊ prompt များကို ကျွမ်းကျင်စွာ အသုံးပြုနိုင်ကာ၊ စာရွက်စာတမ်းများအတွင်းရှိ တုံ့ပြန်ချက်များကို အခြေခံကာ၊ ကိရိယာများဖြင့် ကူညီဆောင်ရွက်သော agent များကို ဖန်တီးခဲ့ပြီးဖြစ်သည်။ သို့သော် စာရင်းပါ ကိရိယာအားလုံးသည် သင့်အပလီကေးရှင်းအတွက် အစီအစဉ်သီးသန့်ဖြစ်သည်။ သင့်အိုင်အေကို သာမန်တစ်သင်းသော ကိရိယာများ အဆက်မပြတ် ဖန်တီး၍ မည်သူမဆို မျှဝေသုံးစွဲနိုင်သော စနစ်တစ်ရပ် ပါဝင်စေရန် လုပ်ဆောင်နိုင်ပါက? ဒီ module တွင် သင်သည် Model Context Protocol (MCP)နှင့် LangChain4j ၏ agentic module အသုံးပြုခြင်းဖြင့် အဲကဲ့သို့ လုပ်ဆောင်နိုင်သည်ကို လေ့လာပါလိမ့်မည်။ ပထမဦးဆုံးမှာ ရိုးရှင်းသည့် MCP ဖိုင် ဖတ်စက် တစ်ခုကို ပြသပြီး ၎င်းသည် Supervisor Agent ပုံစံတစ်ခုဖြင့် ကြီးမှူးခြင်းဖြင့် အဆင့်မြင့် agentic workflow များတွင် ပေါင်းစပ်အသုံးပြုနိုင်သည့် နည်းလမ်းကို ပြသပါသည်။

## What is MCP?

Model Context Protocol (MCP) သည် အဲဒီလို စနစ်တကျ AI application များအတွက် ပြင်ပကိရိယာများကို ရှာဖွေ အသုံးပြုရန် အခြေခံစံနစ်တစ်ခုကို ပေးသည်။ အချက်အလက်အရင်းစွဲ သို့မဟုတ် ဝန်ဆောင်မှုတစ်ခုချင်းစီအတွက် အသုံးပြု စိတ်ကြိုက် ပေါင်းစပ်ဆက်သွယ်မှုများ ရေးသားရန်မလိုဘဲ MCP server များနှင့် ချိတ်ဆက်နိုင်ပြီး ၎င်းတို့၏ စွမ်းရည်များကို တစ်မျိုးတည်း ပုံစံဖြင့် ဖော်ပြပါသည်။ သင်၏ AI agent သည် ထိုကိရိယာများကို အလိုအလျောက် ရှာဖွေပြီး အသုံးပြုနိုင်သည်။

<img src="../../../translated_images/my/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP မပြုမီ: မျိုးစုံနှင့် ခက်ခဲသော ပရှိုက်ပတ်ဆက်မှုများ။ MCP ပြုလုပ်ပြီးနောက်: Protocol တစ်ခု၊ နိုင်ငံတကာ နယ်ပယ်များစွာ။

MCP သည် AI တိုးတက်မှုတွင် အဓိကပြဿနာတစ်ခုဖြစ်သော မည်သည့် ပေါင်းစည်းမှုမျိုးကိုမဆို စိတ်ကြိုက် ဖြစ်နေခြင်း ပြဿနာကို ဖြေရှင်းသည်။ GitHub ကို အသုံးပြုလိုပါက - စိတ်ကြိုက်ကုဒ်။ ဖိုင်များ ဖတ်လိုပါက - စိတ်ကြိုက်ကုဒ်။ ဒေတာဘေ့စ် မေးမြန်းလိုပါက - စိတ်ကြိုက်ကုဒ်။ ထိုပေါင်းစပ်မှုများသည် အခြား AI application များနှင့် မလိုက်ဖက်ပါ။

MCP သည် ထိုအခက်အခဲများကို စံနှုန်းတကျ ပြုပြင်ပေးသည်။ MCP server တစ်ခုသည် သတ်မှတ်ချက်များနှင့် Schema များပါသော ကိရိယာများကို ဖော်ပြသည်။ မည်သည့် MCP client မဆို ချိတ်ဆက်၍ ရနိုင်သော ကိရိယာများကို ရှာဖွေ၊ အသုံးပြုနိုင်သည်။ တစ်ကြိမ်တည်ဆောက်လျှင် တစ်နေရာမှာ အသုံးပြုပေးနိုင်သည်။

<img src="../../../translated_images/my/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol သေချာစိစစ်မှုနှင့် စမ်းသပ်မှုများအတွက် စနစ်တကျ ကိရိယာ ရှာဖွေခြင်းနှင့် စနစ်တကျ လုပ်ဆောင်ခြင်း*

## How MCP Works

<img src="../../../translated_images/my/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP သည် ဘယ်လို လည်ပတ်သည် - clients များသည် tools ကို ရှာဖွေ၊ JSON-RPC စာတိုက်ပြောဆိုပြီး မြှုပ်ကွက်ဖြင့် လုပ်ဆောင်ချက်များ ဆောင်ရွက်သည်။*

**Server-Client Architecture**

MCP သည် client-server မော်ဒယ်ကို အသုံးပြုသည်။ Server များသည် ဖိုင်ဖတ်ခြင်း၊ ဒေတာဘေ့စ် မေးမြန်းခြင်း၊ API ခေါ်ယူခြင်းကဲ့သို့သော ကိရိယာများ ပေးသည်။ Client များ (သင်၏ AI application) သည် server များနှင့် ချိတ်ဆက်ပြီး ၎င်းတို့၏ ကိရိယာများကို အသုံးပြုသည်။

LangChain4j ဖြင့် MCP အသုံးပြုရန် Maven dependency ကို ထည့်သွင်းပါ။

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Tool Discovery**

သင့် client သည် MCP server တစ်ခုနှင့် ချိတ်ဆက်သောအခါ "မည်သည့် ကိရိယာများ ရှိနေလဲ?" ဟုမေးသည်။ Server သည် ဖော်ပြချက်များနှင့် ပါရာမီတာ schemas ထည့်သွင်းပြီး ရနိုင်သည့် ကိရိယာစာရင်းကို ပြန်ပေးသည်။ သင်၏ AI agent သည် အသုံးပြုသူ၏ တောင်းဆိုချက်အပေါ် စီစဉ်၍ အထူးသင့်တော်သည့် ကိရိယာကို ရွေးနိုင်သည်။

<img src="../../../translated_images/my/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI သည် စတင်ထားသည့်အချိန်တွင် ရနိုင်သော ကိရိယာများကို ရှာဖွေသည် — ယခုမည်သည့် စွမ်းဆောင်ချက်များ ရရှိနိုင်ကြောင်း သိရှိပြီး ၎င်းတို့ကို ရွေးချယ် အသုံးပြုနိုင်သည်။*

**Transport Mechanisms**

MCP သည် ထူးခြားသော ဆက်သွယ်မှုနည်းလမ်းများအား ထောက်ပံ့သည်။ ဤ module သည် လုပ်ငန်းတစ်ခုကို ဒေသခံစနစ်အဖြစ် ပံ့ပိုးသည့် Stdio transport ကို ပြသသည်။

<img src="../../../translated_images/my/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP ဆက်သွယ်မှုနည်းလမ်းများ - HTTP သည် ရှေ့နေ+/ မူလတန်း ဆာဗာများအတွက်၊ Stdio သည် ဒေသခံလုပ်ငန်းများအတွက်။*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

ဒေသခံလုပ်ငန်းများအတွက်။ သင့်အပလီကေးရှင်းသည် server ကို subprocess အဖြစ် ရှေးပြီး stdin/output မှတဆင့် ဆက်သွယ်သည်။ ဖိုင်စနစ် သို့မဟုတ် command-line ကိရိယာများအတွက် အသုံးဝင်သည်။

```java
McpTransport stdioTransport = new StdioMcpTransport.Builder()
    .command(List.of(
        npmCmd, "exec",
        "@modelcontextprotocol/server-filesystem@2025.12.18",
        resourcesDir
    ))
    .logEvents(false)
    .build();
```

<img src="../../../translated_images/my/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport လုပ်ငန်းဆောင်တာ — သင့် application သည် MCP server ကို သားသမီး လုပ်ငန်းအဖြစ် စတင်ပြီး stdin/stdout ပိုင်း လမ်းကြောင်းဖြင့် ဆက်သွယ်သည်။*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် ကြိုးစားကြည့်ပါ:** [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ဖိုင်ကို ဖွင့်ပြီး မေးမြန်းပါ။
> - "Stdio transport ဘယ်လို ကျင်းပပြီး ဘယ်အချိန် HTTP ထက် သုံးသင့်သလဲ?"
> - "LangChain4j သည် MCP server လုပ်ငန်းများ၏ အသက်ကြီးရှည်သက်တမ်းကို မည်သို့ စီမံခန့်ခွဲသနည်း?"
> - "AI ကို ဖိုင်စနစ် အသုံးပြုခွင့် ပေးသောအခါ ဘေးလွှမ်းမိုးနိုင်သော လုံခြုံရေးအချက်အလက်များ ရှိပါသလား?"

## The Agentic Module

MCP သည် စံနှုန်းတူကိရိယာများကို ပေးပို့သော် LangChain4j ၏ **agentic module** သည် အဲကဲ့သို့ ကိရိယာများကို စီမံသုံးဆောင်သည့် agent များ ဖန်တီးရန် ဖြေဆိုသည့် ရည်ရွယ်ချက်ပေါ် မူတည်သော နည်းလမ်းစ်တစ်ခုကို ပေးသည်။ `@Agent` annotation နှင့် `AgenticServices` သည် imperative ကုဒ်မဟုတ်သော အင်တာဖေ့စ်များမှတစ်ဆင့် agent အပြုအမူကို သတ်မှတ်ရန် ခွင့်ပြုသည်။

ဤ module တွင် သင်သည် **Supervisor Agent** ပုံစံကို ရှင်းလင်းလေ့လာမည်ဖြစ်ပြီး ၎င်းသည် အသုံးပြုသူ တောင်းဆိုမှု အပေါ် မူတည်ပြီး sub-agent များကို ကျယ်ပြန့်စွာ ထိန်းချုပ်သုံးဆောင်ရာတွင် အသုံးပြုသည့် ထူးခြားသော agentic AI နည်းပညာဖြစ်သည်။ သင်တို့ sub-agent တစ်ခုအား MCP စွမ်းအားမြှင့်ဖိုင်အတွင်း သုံးစွဲနိုင်မှုများ ပေးပြီးနှစ်ခုလုံး ပေါင်းစပ်သုံးရန် ဖြစ်သည်။

agentic module အသုံးပြုရန် Maven dependency ကို ထည့်သွင်းပါ။

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```

> **⚠️ စမ်းသပ်မှုခံယူချက်:** `langchain4j-agentic` module သည် **စမ်းသပ်မှုအဆင့်** နောက်ဆုံးပြောင်းလဲမှု ရှိနိုင်သည်။ အပလိကေးရှင်း AI ကို တည်ဆောက်ရာတွင် တည်ငြိမ်သည့် နည်းလမ်းမှာ `langchain4j-core` နှင့် စိတ်ကြိုက် ကိရိယာများ အသုံးပြုခြင်း (Module 04) ဖြစ်သည်။

## Running the Examples

### Prerequisites

- Java 21+, Maven 3.9+
- Node.js 16+ နှင့် npm (MCP server များအတွက်)
- `.env` ဖိုင်တွင် ပတ်ဝန်းကျင် အပြောင်းအလဲများ ထည့်သွင်းထားခြင်း (Root directory မှ):
  - `AZURE_OPENAI_ENDPOINT`, `AZURE_OPENAI_API_KEY`, `AZURE_OPENAI_DEPLOYMENT` (Module 01-04 များနှင့် အတူတူ)

> **မှတ်ချက်:** သင်သည် ပတ်ဝန်းကျင် အပြောင်းအလဲများကို မဆက်သွယ်ထားသေးပါက [Module 00 - Quick Start](../00-quick-start/README.md) တွင် လမ်းညွှန်ချက်များကို ကြည့်ရှုပါ၊ ဒါမှမဟုတ် root directory ရှိ `.env.example` ဖိုင်ကို `.env` ဟူ၍ မိတ္တူကူးပြီး သင်၏ တန်ဖိုးများ ဖြည့်ပါ။

## Quick Start

**VS Code ကို အသုံးပြုသူများ:** Explorer တွင် မည်သည့် demo ဖိုင်ကိုမဆို ညာနှိပ်၍ **"Run Java"** ကို ရွေးချယ်ပါ၊ ဒါမှမဟုတ် Run and Debug panel မှ launch configuration များကို အသုံးပြုပါ (အရင်ဆုံး `.env` ဖိုင် ထည့်သွင်းထားရန် သေချာပါစေ)။

**Maven ကို အသုံးပြုသူများ:** နောက်တစ်နည်းအား command line မှ များအောက်ပါ အတိုင်း ကိုယ်ကျင့်ကြည့်မည်။

### File Operations (Stdio)

ဤသည်မှာ ဒေသခံ subprocess မှာ အခြေခံ tool များအား ပြသသည်။

**✅ မလိုအပ်သော ကြိုတင်အဆင်ပြေမှုများ** - MCP server သည် automatisch အလုပ်စတင်ပါသည်။

**Start Scripts အသုံးပြုခြင်း (အကြံပြု):**

start scripts များသည် root `.env` ဖိုင်မှ ပတ်ဝန်းကျင် အပြောင်းအလဲများကို အလိုအလျောက် โหลดသတ်မှတ်သည်။

**Bash:**
```bash
cd 05-mcp
chmod +x start-stdio.sh
./start-stdio.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-stdio.ps1
```

**VS Code အသုံးပြုသူများ:** `StdioTransportDemo.java` ဖိုင်ကို ညာနှိပ်၍ **"Run Java"** ကို ရွေးချယ်ပါ (သင်၏ `.env` ဖိုင် အပြင်အဆင်ရှိကြောင်း သေချာပါစေ).

application သည် ဖိုင်စနစ် MCP server ကို Subprocess အဖြစ် စတင်၍ ဒေသခံဖိုင်တစ်ခုကို ဖတ်ပေးသည်။ subprocess စီမံခန့်ခွဲမှုကို သင်အတွက် ကိုင်တွယ်ပေးသည်ကို တွေ့မြင်ရမည်။

**မျှော်မှန်းထားသည့် output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

**Supervisor Agent ပုံစံ** သည် agentic AI ၏ **ပြောင်းလဲနိုင်မှုရှိသော** ပုံစံတစ်ခုဖြစ်သည်။ Supervisor သည် LLM တစ်ခုကို အသုံးပြုပြီး အသုံးပြုသူ တောင်းဆိုချက်အရ sub-agent များကို ကြားဖြတ်ခေါ်ယူရန် အလိုအလျောက်ဆုံးဖြတ်သည်။ နောက်တစ်ခု demo တွင် MCP စွမ်းအားဖြင့် ဖိုင်အသုံးပြုပြီး LLM agent တစ်ခုနှင့် ပေါင်းစပ်၍ supervised ဖိုင်ဖတ်ပြီး → အစီရင်ခံစာ ထုတ်ပေးမည့် workflow ကို တည်ဆောက်ခဲ့သည်။

Demo တွင် `FileAgent` သည် MCP ဖိုင်စနစ်ကိရိယာများကို အသုံးပြု၍ ဖိုင်ဖတ်ပြီး `ReportAgent` သည် အမည်စာတမ်းအကျဉ်း (ဝေါဟာရတစ်ကြောင်း), သုံးချက် အချက်အလက်များနှင့် အကြံပြုချက်များပါဝင်သည့် ဖွဲ့စည်းသည့် အစီရင်ခံစာ ပြုလုပ်သည်။ Supervisor သည် ၎င်း flow ကို အလိုအလျောက် ကြီးမှူးခိုင်းသည်။

<img src="../../../translated_images/my/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor သည် ကိုယ်ပိုင် LLM ကို အသုံးပြု၍ မည်သည့် agent များ ခေါ်ယူမည်ကို နှင့် အသေးစိတ် အရေအတွက်ကို ဆုံးဖြတ်သည် — စနစ်တကျ ချိတ်ဆက်ခြင်း မလိုအပ်ပါ။*

ဤမှာ ဖိုင်မှအစီရင်ခံစာသို့ ပိုင်းစည်းမှု အလုပ်စဉ် တိတိကျကျ အောက်ပါအတိုင်း ဖြစ်ပါသည်။

<img src="../../../translated_images/my/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent သည် MCP ကိရိယာများဖြင့် ဖိုင်ဖတ်ပြီး ReportAgent သည် ထိုအကြောင်းအရာကို ဖွဲ့စည်းထားသော အစီရင်ခံစာ အဖြစ် ပြောင်းလဲပေးသည်။*

agent တစ်ခုချင်းစီသည် **Agentic Scope** (မျှဝေသည့် မေမရီ) တွင် ၎င်း၏ ထုတ်လွှင့်ချက်များကို သိမ်းဆည်းထားကာ ကျန်ကြောင်း agent များသည် အရင် agent များ၏ ရလဒ်များကို ရယူနိုင်သည်။ ၎င်းဖြင့် MCP ကိရိယာများသည် agentic workflow တို့နှင့် ကိုက်ညီစွာ ပေါင်းစပ်နေသည်ကို ပြသသည် – Supervisor သည် ဖိုင်များ ဘယ်လိုဖတ်သည်ကို မသိလိုပါ၊ `FileAgent` သည် ဖတ်နိုင်သည်ကိုသာ သိရန် လိုသည်။

#### Running the Demo

start scripts များသည် root `.env` ဖိုင်မှ ပတ်ဝန်းကျင် အပြောင်းအလဲများကို အလိုအလျောက် โหลดသတ်မှတ်သည်။

**Bash:**
```bash
cd 05-mcp
chmod +x start-supervisor.sh
./start-supervisor.sh
```

**PowerShell:**
```powershell
cd 05-mcp
.\start-supervisor.ps1
```

**VS Code အသုံးပြုသူများ:** `SupervisorAgentDemo.java` ဖိုင်ကို ညာနှိပ်ပြီး **"Run Java"** ကို ရွေးချယ်ပါ (သင်၏ `.env` ဖိုင်အတည်ပြုထားသည်။

#### How the Supervisor Works

```java
// အဆင့် ၁: FileAgent သည် MCP ကိရိယာများကို အသုံးပြု၍ ဖိုင်များကို ဖတ်သည်
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // ဖိုင်ဆိုင်ရာ လုပ်ငန်းများအတွက် MCP ကိရိယာများရှိသည်
        .build();

// အဆင့် ၂: ReportAgent သည် ဖွဲ့စည်းထားသောအစီရင်ခံစာများ ထုတ်ပေးသည်
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor သည် ဖိုင် → အစီရင်ခံစာ လုပ်ငန်းစဉ်ကို စီမံမှု၊ ဦးဆောင်သည်
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // နောက်ဆုံးအစီရင်ခံစာကို ပြန်လိုက်ပါ
        .build();

// Supervisor သည် မေးမြန်းချက်အပေါ် မူတည်၍ ဘယ်အေးဂျင့်များကို ခေါ်ရန် ဆုံးဖြတ်သည်
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Response Strategies

`SupervisorAgent` ကို ဖန်တီးသောအခါ၊ သင့်အား sub-agent များ အားလုံး တာဝန်ပြီးဆုံးပြီးနောက် အသုံးပြုသူအတွက် အဆုံးသတ်ဖြေကြားချက်ကို မည်သို့ ဖန်တီးမည်ကို သတ်မှတ်နိုင်သည်။

<img src="../../../translated_images/my/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Supervisor သည် သို့မဟုတ် ဒါနဲ့သက်ဆိုင်သော (LAST agent output), စုစည်းထားသော အကျဉ်းချုပ်, ဒါမှမဟုတ် အကောင်းဆုံး အဆင့်ပြေး ရလဒ်ကို အခြေခံကာ အဆုံးဖြေပြန်လည်ဖြေကြားခြင်း အတွက် မျိုးစုံနည်းလမ်းသုံးသည်။*

ရရှိနိုင်သည့် နည်းလမ်းများမှာ -

| နည်းလမ်း | ဖော်ပြချက် |
|----------|-------------|
| **LAST** | Supervisor သည် နောက်ဆုံးခေါ်ယူသော sub-agent သို့ကိရိယာထွက်ရှိချက်ကို ပြန်ပေးသည်။ ၎င်းသည် အကွောင့္ဆုံး agent သည် ပြည့်စုံသော အဆုံးအဖြေ ထုတ်ပေးရန် သီးသန့်ဒီဇိုင်းထားပါက အသုံးဝင်သည် (ဥပမာ - သုတေသနအတွက် "စုစည်း Agent") |
| **SUMMARY** | Supervisor သည် ကိုယ်ပိုင် internal Language Model(LLM) ကို အသုံးပြု၍ အပြင်အဆင်သည့် interactionနှင့် sub-agent output များအားလုံးကို ထပ်စပ် စုစည်းပြီး အဆုံးဖြေ အနေနှင့် ပြန်ပေးသည်။ ၎င်းသည် အသုံးပြုသူအတွက် သန့်ရှင်း၍ စုစည်းထားသော ဖြေရှင်းချက်ဖြစ်သည်။ |
| **SCORED** | internal LLM ကို တင်သွင်း၍ LAST ဖြေကြားချက်နှင့် SUMMARY အားလုံးကို အသုံးပြုသူ တောင်းဆိုမှုအရ မည်သည့် output ကို အကောင်းဆုံးဖြစ်သည့် အဆင့်ပေး၍ ပြန်ပေးသည်။ |

အပြည့်အစုံအတွက် [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ကို ကြည့်ပါ။

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် ကြိုးစားကြည့်ပါ:** [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ဖိုင်ကို ဖွင့်ပြီး မေးမြန်းပါ။
> - "Supervisor သည် မည်သူ့ agent တွေကို ခေါ်ယူမည့်အပေါ် ဘယ်လိုဆုံးဖြတ်သနည်း?"
> - "Supervisor နှင့် Sequential workflow pattern များအကြား ကွာခြားချက်က ဘာလဲ?"
> - "Supervisor ၏ စီမံကိန်းပြုလုပ်မှု ကို ဘယ်လို အပြုသဘော ပြောင်းလဲနိုင်မလဲ?"

#### Understanding the Output

Demo ကို တင်စားသောအခါ Supervisor သည် multiple agent များကို မည်သို့ စီမံကြီးမှူးသည်ကို စနစ်တကျ ပြတင်းပေါ်မှ ကြည့်မြင်ရမည်။ အောက်ပါအစိတ်အပိုင်းတိုင်း၏ အဓိပ္ပာယ်မှာ -

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**ခြေနေစာမျက်နှာ** သည် workflow အယူအဆကို မိတ်ဆက်သည်။ ဖိုင်ဖတ်ခြင်းမှ အစီရင်ခံစာထုတ်လုပ်မှုအထိ တစ်ပြိုင်နက်တည်း ပြုလုပ်ခြင်းဖြစ်သည်။

```
--- WORKFLOW ---------------------------------------------------------
  ┌─────────────┐      ┌──────────────┐
  │  FileAgent  │ ───▶ │ ReportAgent  │
  │ (MCP tools) │      │  (pure LLM)  │
  └─────────────┘      └──────────────┘
   outputKey:           outputKey:
   'fileContent'        'report'

--- AVAILABLE AGENTS -------------------------------------------------
  [FILE]   FileAgent   - Reads files via MCP → stores in 'fileContent'
  [REPORT] ReportAgent - Generates structured report → stores in 'report'
```

**Workflow Diagram** သည် agent များအကြား ဒေတာလွှဲပြောင်းမှုကို ဖော်ပြသည်။ agent တစ်စ်ယောက်ချင်း၏ တာဝန်မှာ -
- **FileAgent** သည် MCP ကိရိယာများဖြင့် ဖိုင်ဖတ်ကာ raw content ကို `fileContent` တွင် သိမ်းဆည်းသည်
- **ReportAgent** သည် ထို အကြောင်းအရာကို အသုံးပြုပြီး ဖွဲ့စည်းထားသော အစီရင်ခံစာကို `report` တွင် ထုတ်ပေးသည်။

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**User Request** သည် အလုပ်တာဝန်ကို ပြသသည်။ Supervisor သည် ဤချက်ကို ဖြဲစည်း၍ FileAgent → ReportAgent ကို ခေါ်ယူမည်ဟု ဆုံးဖြတ်သည်။

```
--- SUPERVISOR ORCHESTRATION -----------------------------------------
  The Supervisor decides which agents to invoke and passes data between them...

  +-- STEP 1: Supervisor chose -> FileAgent (reading file via MCP)
  |
  |   Input: .../file.txt
  |
  |   Result: LangChain4j is an open-source, provider-agnostic Java framework for building LLM...
  +-- [OK] FileAgent (reading file via MCP) completed

  +-- STEP 2: Supervisor chose -> ReportAgent (generating structured report)
  |
  |   Input: LangChain4j is an open-source, provider-agnostic Java framew...
  |
  |   Result: Executive Summary...
  +-- [OK] ReportAgent (generating structured report) completed
```

**Supervisor Orchestration** သည် ၂အဆင့် အလုပ်စဉ်ကို ပြသသည်။
1. **FileAgent** သည် MCP ကိရိယာများဖြင့် ဖိုင်ဖတ်ကာ အကြောင်းအရာကို သိမ်းဆည်းသည်။
2. **ReportAgent** သည် ထို အကြောင်းအရာကို လက်ခံပြီး ဖွဲ့စည်းထားသော အစီရင်ခံစာ ထုတ်ပေးသည်။

Supervisor သည် အသုံးပြုသူ တောင်းဆိုချက်အပေါ် အလိုအလျောက် ဤဆုံးဖြတ်ချက်များကို ချမှတ်ခဲ့သည်။

```
--- FINAL RESPONSE ---------------------------------------------------
Executive Summary
...

Key Points
...

Recommendations
...

--- AGENTIC SCOPE (Data Flow) ----------------------------------------
  Each agent stores its output for downstream agents to consume:
  * fileContent: LangChain4j is an open-source, provider-agnostic Java framework...
  * report: Executive Summary...
```

#### Explanation of Agentic Module Features

ဤ ဥပမာတွင် agentic module ၏ အဆင့်မြင့် features များကို ပြသသည်။ Agentic Scope နှင့် Agent Listeners ကို ဖော်ပြပြောကြားပါမည်။

**Agentic Scope** သည် agent များမှ `@Agent(outputKey="...")` ဖြင့် သိမ်းဆည်းသော မျှဝေသော မှတ်ဉာဏ်ဖြစ်သည်။ ၎င်းသည်
- နောက်ထပ်အေဂျင့်များကို မတိုင်မီ agent များ၏ ထွက်ရှိမှုများရယူနိုင်ရန်
- Supervisor သည် အဆုံးဆုံးဖြေချက် တည်းဖြတ်နိုင်ရန်
- သငျသညျ မည်သည့် agent ထုတ်လုပ်ချက်ကို စစ်ဆေးကြည့်ရှုနိုင်ရန်

<img src="../../../translated_images/my/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope သည် မျှဝေသော မှတ်ဉာဏ်ပါဝင်သည် - FileAgent သည် `fileContent` ကို ရေးဖြည့်ကာ ReportAgent သည် ၎င်းကို ဖတ်ပြီး `report` ကို ရေးထုတ်၍ သင်၏ကုဒ်သည် နောက်ဆုံးရလဒ်ကို ဖတ်သည်။*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // FileAgent မှ မူရင်းဖိုင်ဒေတာ
String report = scope.readState("report");            // ReportAgent မှ ဖွဲ့စည်းတည်ဆောက်ထားသောအစီရင်ခံစာ
```

**Agent Listeners** သည် agent လည်ပတ်မှုကို ကြည့်ရှု ထိန်းသိမ်းရန် အခွင့်အရေး ပေးသည်။ Demo တွင် မြင်တွေ့ရသော ဒီဇိုင်း ကြောင်းရှင်းလင်းမှုများသည် อายุ tote log AgentListener တစ်ခုမှ အမျိုးမျိုးရှိမည့် agent ဖုန်းခေါ်မှုများကို အလှည့်ကျ ဖြတ်သန်းခြင်းဖြစ်သည်။
- **beforeAgentInvocation** - Supervisor သည် agent ကိုရွေးချယ်သောအခါခေါ်ဆိုသည်၊ ရွေးချယ်ထားသော agent နှင့် အကြောင်းရင်းကိုကြည့်ရှူနိုင်သည်
- **afterAgentInvocation** - agent တစ်ခုပြီးဆုံးသည့်အခါခေါ်ဆိုသည်၊ ၎င်း၏ရလဒ်ကိုပြသသည်
- **inheritedBySubagents** - true ဖြစ်ပါက listener သည် hierarchy မှ agent အားလုံးကိုစောင့်ကြည့်သည်

<img src="../../../translated_images/my/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners သည် အကောင်အထည်ဖော်မှုလည်ပတ်မှုသက်တမ်းထဲသို့ ဆက်သွယ်ကပ်နေပြီး၊ agent များစတင်ခြင်း၊ ပြီးစီးခြင်း သို့မဟုတ် အမှားတွေ့ရှိခြင်းများကို စောင့်ကြည့်သည်။*

```java
AgentListener monitor = new AgentListener() {
    private int step = 0;
    
    @Override
    public void beforeAgentInvocation(AgentRequest request) {
        step++;
        System.out.println("  +-- STEP " + step + ": " + request.agentName());
    }
    
    @Override
    public void afterAgentInvocation(AgentResponse response) {
        System.out.println("  +-- [OK] " + response.agentName() + " completed");
    }
    
    @Override
    public boolean inheritedBySubagents() {
        return true; // အောက်စဖ်ရေးရှင်းအားလုံးသို့ ဖြန့်ဝေပါ
    }
};
```

Supervisor pattern ထက်ပြီး၍ `langchain4j-agentic` module သည် စွမ်းဆောင်ရည်မြင့် workflow pattern များနှင့် features များစွာကို ပေးဆောင်သည်-

<img src="../../../translated_images/my/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Agent များကို စီမံခန့်ခွဲရန် workflow pattern ငါးမျိုး—ရိုးရှင်းသောအဆင့်လိုက် pipeline များမှ လူထည့်သွင်းလိုက် aprobación workflows များထိ။*

| Pattern | ဖော်ပြချက် | အသုံးပြုမှုအခြေအနေ |
|---------|-------------|-----------------------|
| **Sequential** | agent များကို အကြောင်းအမျိုးအစားလိုက် ဆောင်ရွက်သည်၊ output သည် နောက်တစ်ခုသို့ တဆက်တည်းစီးဆင်းသည် | Pipeline များ: ရှာဖွေ → အခြေချ → စာရင်းတင် |
| **Parallel** | agent များကို တပြိုင်နက် ဆောင်ရွက်သည် | ပိုမိုလွတ်လပ်သောတာဝန်များ: ရာသီဥတု + သတင်း + စတော့ရှယ်ယာ |
| **Loop** | အခြေအနေပတ်လည်ထိ ဝိုင်းလှည့်လုပ်ဆောင်သည် | အရည်အသွေးအဆင့်သတ်မှတ်ခြင်း: rating ≥ 0.8 ထိ လုပ်ဆောင် |
| **Conditional** | အခြေအနေပေါ်မူတည်၍ လမ်းညွှန်သည် | အမျိုးအစားခွဲခြား → အထူးပြု agent သို့ လမ်းညွှန် |
| **Human-in-the-Loop** | လူမှုစစ်ဆေးမှုများထည့်သွင်းသည် | လက်မှတ်၊ အကြောင်းအရာပြန်လည်သုံးသပ်မှု |

## အဓိကအယူအဆများ

MCP နှင့် agentic module ကို လေ့လာပြီးနောက်၊ အနည်းဆုံး မည်သည့်နည်းလမ်း အသုံးပြုမှုပုံစံကို သဘောတူဆုံးဖြတ်ကြပါစို့။

<img src="../../../translated_images/my/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP သည် အတူတကွ အသုံးပြုနိုင်သော protocol ecosystem တစ်ခု ဖန်တီးသည် — MCP ကို ကျေနပ်သော server တစ်ခုသည် MCP ကိုကျေနပ်သော client တစ်ခုနှင့် မည်သည့် tool မဆို အခြေခံ၍ မျှဝေရန် အဆင်ပြေသည်။*

**MCP** သည် ရှိပြီးသား tool ecosystem များကို အကောင်းဆုံး အသုံးချလိုသောအခါ၊ များစွာသော application များတွင် မျှဝေပါသော tool များတည်ဆောက်လိုသောအခါ၊ သတ်မှတ် protocol များဖြင့် third-party ဝန်ဆောင်မှုများကို ပေါင်းစည်းလိုသောအခါ၊ သို့မဟုတ် code မပြောင်းလဲဘဲ tool implementation များကို လဲလှယ်လိုသောအခါ အကောင်းဆုံးဖြစ်သည်။

**Agentic Module** သည် `@Agent` annotation များဖြင့် အဓိပ္ပာယ်ပြတဲ့ agent သတ်မှတ်ချက်များလိုအပ်သောအခါ၊ workflow orchestration (sequential, loop, parallel) ကို လိုအပ်သောအခါ၊ imperative code ထက် interface-based agent design ကို 선호သောအခါ၊ ပေါင်းစပ်ပြီး `outputKey` ဖြင့် output များကို မျှဝေသည့် agent များစုစည်းသည့်အခါ အကောင်းဆုံးလုပ်ဆောင်သည်။

**Supervisor Agent pattern** သည် workflow များကို မျှော်မှန်းထောက်လှမ်း၍မရသောအခါ၊ LLM ကို ဆုံးဖြတ်ခိုင်းလိုသောအခါ၊ အမျိုးမျိုးအထူးပြု agent များရှိပြီး dynamic orchestration လိုအပ်သောအခါ၊ ကွာခြားသောစွမ်းရည်များသို့ လမ်းညွှန်သော ပြောဆိုဆက်သွယ်မှုစနစ်များ တည်ဆောက်လိုသောအခါ၊ သို့မဟုတ် အကြီးစား၊ သက်ဆိုင်မှုမြင့် agent အပြုအမူကိုလိုချင်သောအခါ ထူးချွန်သည်။

<img src="../../../translated_images/my/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*custom @Tool method များနှင့် MCP tools များကို မည်သည့်အခါ အသုံးပြုမည် - app သီးသန့် logic များအတွက် custom tool များ, စံသတ်မှတ်များနှင့်ပြည်တွင်းသုံး များအတွက် MCP tool များ။*

## ဂုဏ်ယူလိုက်ပါ!

<img src="../../../translated_images/my/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*အခန်းငါးခုလုံးမှတဆင့် သင်ကြားမှုခရီး — ရိုးရှင်းသော chat မှ MCP တာဝန်ရှိ agentic စနစ်များအထိ။*

LangChain4j for Beginners သင်တန်းကို ပြီးစီးပြီးပါပြီ။ သင်သိရှိခဲ့သည်-

- အသိပညာအားဖြင့် ဆက်သွယ်ရန် AI တည်ဆောက်နည်း (Module 01)
- အလုပ်အဆင့်အမျိုးအစားအတွက် prompt engineering ကိုင်တွယ်နည်း (Module 02)
- သင့်စာတမ်းများမှ တုံ့ပြန်ချက်များကို RAG ဖြင့် အခြေချခြင်း (Module 03)
- custom tool များဖြင့် အခြေခံ AI agent များ ဖန်တီးခြင်း (Module 04)
- LangChain4j MCP နှင့် Agentic module များဖြင့် စံပြ tool များ ပေါင်းစည်းခြင်း (Module 05)

### နောက်တစ်ဆင့်?

အခန်းများကို ပြီးစီးပြီးနောက်၊ [Testing Guide](../docs/TESTING.md) အား လေ့လာ၍ LangChain4j စမ်းသပ်မှု အယူအဆများကို လက်တွေ့ချက်များအဖြစ် ကြည့်ရှုနိုင်ပါသည်။

**တရားဝင် အရင်းအမြစ်များ:**
- [LangChain4j Documentation](https://docs.langchain4j.dev/) - လမ်းညွှန်ချက်များနှင့် API အညွှန်းများ
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - ကုဒ်အရင်းအမြစ် နှင့် ဥပမာများ
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - အသုံးပြုမှုမျိုးစုံအတွက် အဆင့်လိုက် သင်ခန်းစာများ

ဤသင်တန်းကို ပြီးမြောက်လိုက်ကြောင်း ကျေးဇူးတင်ရှိပါသည်!

---

**Navigation:** [← Previous: Module 04 - Tools](../04-tools/README.md) | [Back to Main](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**အသိပေးချက်**  
ဤစာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှုဖြစ်သော [Co-op Translator](https://github.com/Azure/co-op-translator) ကိုအသုံးပြု၍ ဘာသာပြန်ထားခြင်းဖြစ်သည်။ ကျွန်ုပ်တို့သည် တိကျမှုအတွက် ကြိုးစားပေမယ့် အလိုအလျောက်ဘာသာပြန်ခြင်းများတွင် အမှားများ သို့မဟုတ် တိကျမှုမရှိမှုများ ပါဝင်နိုင်သည်ကို သတိပြုပါ။ မူရင်းစာတမ်းကို ၎င်း၏ မူလဘာသာဖြင့် အာဏာပိုင်အရင်းအမြစ်အဖြစ် ယူဆသင့်ပါသည်။ အရေးကြီးသော သတင်းအချက်အလက်များအတွက် ပရော်ဖက်ရှင်နယ် လူ့ဘာသာပြန်မှုကို လိုအပ်ပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုခြင်းကနေ ဖြစ်ပေါ်လာနိုင်သော မမှန်ကန်သည့်နားလည်မှုများ သို့မဟုတ် မမှန်ကန်သောဖော်ပြချက်များအတွက် ကျွန်ုပ်တို့သည် တာဝန်မခံပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->