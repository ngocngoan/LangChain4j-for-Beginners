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

သင် conversational AI တည်ဆောက်ခဲ့ပြီး၊ prompts များကို ကျွမ်းကျင်စွာ အသုံးပြုနိုင်ခဲ့ပြီး၊ responses များကို အထောက်အထားစာရွက်များနှင့် အခြေခံ၍ agents များကို tools များနှင့် တည်ဆောက်နိုင်ခဲ့ပါသည်။ သို့သော် ထို tools များအားလုံးသည် သင်၏ အထူးသင့်လျော်သော application အတွက် တစ်ဦးချင်း custom-built ဖြစ်ပါသည်။ သင်၏ AI ကို အများသူငါ ဖန်တီး၍ မျှဝေနိုင်သော standard ရှိသော tools များ ဝန်းကျင် (ecosystem) တစ်ခုသို့ access အောင် ပြုလုပ်နိုင်ခဲ့လျှင်? ဤ module တွင် Model Context Protocol (MCP) နှင့် LangChain4j ၏ agentic module ကို အသုံးပြုပြီး ထိုအတိုင်း ပြုလုပ်ရန် နည်းလမ်းကို သိရှိပါမည်။ မူလတန်းတွင် ရိုးရှင်းသည့် MCP ဖိုင်ဖတ်စက်တစ်ခုကို ဖော်ပြပြီး၊ နောက်တစ်ကြိမ်မှာ Supervisor Agent ပုံစံဖြင့် အဆင့်မြင့်သော agentic workflows ထဲသို့ လွယ်ကူစွာ ပေါင်းသီးပေါင်းသန့် လုပ်ဆောင်နိုင်မှုကို ပြပါမည်။

## What is MCP?

Model Context Protocol (MCP) သည် AI applications များအတွက် အပြည့်အဝတိကျသော - ပြင်ပ tools များ ရှာဖွေ၍ အသုံးပြုနိုင်ရန် စံအတန်းတစ်ခုကို ပေးသည်။ ဒါနည်းမဟုတ် ပိုင်းခြား integration များကို မှန်ကန်စွာ ဖန်တီးရန် အစား၊ MCP servers များထံ ဆက်သွယ်၍ ၎င်းတို့၏ လုပ်ဆောင်နိုင်သော စွမ်းဆောင်ရည်များကို တစ်စိတ်တစ်ပိုင်း အနေနှင့် ဖော်ပြထားသည့် ဖော်ပြချက် အတိုင်း ပြပါသည်။ သင်၏ AI agent သည် ထို tools များကို အလိုအလျောက် ရှာဖွေ၍ အသုံးပြုနိုင်သည်။

အောက်ပါ ပုံသည် MCP မရှိသောကာလနှင့် MCP ပေါင်းစပ်ထားသောကာလတို့၏ ကွာခြားချက်ကို ပြသသည် - MCP မရှိရင် အပေါင်းအသင်းတိုင်းသည် point-to-point integration ပုံစံဖြင့် ဖန်တီးရမည်ဖြစ်ပြီး၊ MCP သုံးရင် တစ်ခုတည်းသော protocol ကို အသုံးပြုပြီး သင့် app ကို မည်သည့် tool အတွက်မဆို ချိတ်ဆက်နိုင်သည်။

<img src="../../../translated_images/my/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*MCP မရှိခင် - စုစည်းခက်ခဲသော point-to-point integrations။ MCP ရှိနောက် - တစ်ခုတည်းသော protocol နှင့် အကန့်အသတ်မဲ့ အလားအလာများ။*

MCP သည် AI ဖွံ့ဖြိုးတိုးတက်မှု၌ အဓိက ပြဿနာတစ်ခုဖြစ်သည့် အားလုံးသည် custom integration ဖြစ်နေသည်ကို ဖြေရှင်းပေးပါသည်။ GitHub သို့ ဝင်ရောက်ချင်ပါသလား? Custom code ရေးရမည်။ ဖိုင်များ ဖတ်ချင်ပါသလား? Custom code ရေးရမည်။ ဒေတာဘေ့စ် မေးမြန်းချင်ပါသလား? Custom code ရေးရမည်။ ထို့အပြင် ဤ integration များသည် တခြား AI applications များနှင့် မသုံးနိုင်ပါ။

MCP သည် ဤအရာကို စံအတန်းဖြင့် တည်ဆောက်ပေးသည်။ MCP server သည် tools များကို ဖော်ပြချက်ရှင်းလင်းပြီး schema များ ဖြင့် ဖော်ပြသည်။ မည်သည့် MCP client မဆို ဆက်သွယ်ကာ ရနိုင်သော tools များကို ရှာဖွေပြီး အသုံးပြုနိုင်သည်။ တစ်ကြိမ်တည်ဆောက်ပြီး နေရာတိုင်းတွင် အသုံးပြုနိုင်ပါသည်။

အောက်ပါ ပုံသည် ဒီဆောက်လုပ်မှုကို ဖော်ပြသည် - တစ်ခုတည်းသော MCP client (သင့် AI application) သည် များစွာသော MCP servers များနှင့် ချိတ်ဆက်ပြီး စံတော်ချိန် protocol မှတဆင့် မိမိတို့၏ tools များကို ဖော်ပြထားသည်။

<img src="../../../translated_images/my/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol architecture - စံတော်ချိန်ဝေဖန်သော သုံးစွဲသူ tool ရှာဖွေမှုနှင့် လုပ်ဆောင်မှု*

## How MCP Works

နောက်ခံတွင် MCP သည် အလွှာစီသော ပုံစံဖြင့် လည်ပတ်သည်။ သင့် Java application (MCP client) သည် ရနိုင်သော tools များကို ရှာဖွေပြီး JSON-RPC requests များကို transport layer (Stdio သို့မဟုတ် HTTP) မှတဆင့် ပေးပို့သည်။ MCP server သည် လုပ်ဆောင်ချက်များကို အကောင်အထည်ဖော်ပြီး ရလဒ်များ ပြန်ပေးသည်။ အောက်ပါပုံမှာ protocol ၏ အလွှာတိုင်းကို ဖေါ်ပြသည်။

<img src="../../../translated_images/my/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP အတွင်းလွှာ လည်ပတ်ပုံ - clients များသည် tools များ ရှာဖွေပြီး JSON-RPC သတင်းစကားများ လဲလှယ်ကာ transport layer မှတဆင့် လုပ်ဆောင်ချက်များ လုပ်ငန်းဆောင်ရွက်သည်။*

**Server-Client Architecture**

MCP သည် client-server ပုံစံကို အသုံးပြုသည်။ Servers များသည် tools များ ပံ့ပိုးပေးသည် - ဖိုင်ဖတ်ခြင်း၊ ဒေတာဘေ့စ် မေးမြန်းခြင်း၊ APIs ခေါ်ဆိုခြင်း။ Clients (သင့် AI application) သည် servers များနှင့် ချိတ်ဆက်ကာ ၎င်းတို့၏ tools များကို အသုံးပြုသည်။

LangChain4j နှင့် MCP ကို သုံးရန် Maven dependency ကို ထည့်ပါ။

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```

**Tool Discovery**

သင့် client သည် MCP server တစ်ခုသို့ ချိတ်ဆက်သည့်အခါ “What tools do you have?” ဟု မေးသည်။ Server သည် ရနိုင်သည့် tools များ၊ ဖော်ပြချက်များနှင့် parameter schemas များပါဝင်သည့် စာရင်းအား ပြန်ပေးသည်။ သင့် AI agent သည် အသုံးပြုသူရဲ့ တောင်းဆိုချက်အပေါ် မူတည်၍ မည်သည့် tools ကို သုံးမည်ဆိုသည်ကို ဆုံးဖြတ်နိုင်သည်။ အောက်ပုံသည် handshake အကြောင်း ကိုပြသည် - client သည် `tools/list` ကြေငြာချက် ပို့ပြီး server သည် tools များအား ဖော်ပြချက်များနှင့် parameter schemas များဖြင့် ပြန်ပေးသည်။

<img src="../../../translated_images/my/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI သည် စတင်တည်ဆောက်ချိန်တွင် ရနိုင်သည့် tools များကို ရှာဖွေသည် - ယခု ၎င်းသည် ဘာများ ရနိုင်ကြောင်း သိပြီး မည်သည့် tools များကို အသုံးပြုမည်ဆိုသည်ကို ဆုံးဖြတ်နိုင်သည်။*

**Transport Mechanisms**

MCP သည် အမျိုးမျိုးသော transport mechanisms များကို ပံ့ပိုးသည်။ အခြားနည်းလမ်းနှစ်ခုမှာ Stdio (ယေဘူယျနည်းဖြင့် local subprocess ဆက်သွယ်ရန်) နှင့် Streamable HTTP (remote servers များအသုံးပြုရန်) ဖြစ်သည်။ ဤ module တွင် Stdio transport ကို ပြသသည်။

<img src="../../../translated_images/my/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP transport mechanisms: HTTP သည် remote servers များအတွက်၊ Stdio သည် local processes များအတွက်*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

local processes အတွက်။ သင့် application သည် server ကို subprocess တစ်ခုအဖြစ် ဖန်တီးပြီး standard input/output တို့မှတဆင့် ဆက်သွယ်သည်။ filesystem access သို့မဟုတ် command-line tools များအတွက် အသုံးပြုနိုင်သည်။

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

`@modelcontextprotocol/server-filesystem` server သည် မိမိ သတ်မှတ်ထားသော directories များအတွင်း Sandbox မှာ ထည့်သွင်းထားသော tools များကို ဖော်ပြသည်။

| Tool | Description |
|------|-------------|
| `read_file` | ဖိုင် တစ်ခု၏ အတွင်းအကြောင်းကို ဖတ်ပါ |
| `read_multiple_files` | တစ်ခါတည်း ဖိုင်များစွာကို ဖတ်ပါ |
| `write_file` | ဖိုင်တစ်ခု ဖန်တီးခြင်း သို့မဟုတ် အသစ်ရေးခြင်း |
| `edit_file` | တိကျသော ရှာဖွေပြုပြင်ဆင်မှုများ ပြုလုပ်သည် |
| `list_directory` | ဖိုင်များနှင့် ဒါရိုက်တာရီများကို လမ်းကြောင်းတစ်ခုတွင် စာရင်းပြုစုသည် |
| `search_files` | ပြုံPattern တစ်ခုနှင့် ကိုက်ညီသော ဖိုင်များကို တိုးတက်စွာ ရှာဖွေသည် |
| `get_file_info` | ဖိုင် metadata (အရွယ်အစား၊ အချိန်ရက်စွဲများ၊ ခွင့်ပြုချက်များ) ရယူသည် |
| `create_directory` | ဒါရိုက်တာရီ ဖန်တီးခြင်း (မိဘဒါရိုက်တာရီများပါဝင်သည်) |
| `move_file` | ဖိုင် သို့မဟုတ် ဒါရိုက်တာရီအမည်ပြောင်းခြင်း သို့မဟုတ် ဆွဲယူနေရာပြောင်းခြင်း |

အောက်ပါပုံသည် Stdio transport သည် runtime တွင် မည်သို့ လုပ်ဆောင်ကြောင်း ပြသည် - သင့် Java application သည် MCP server ကို child process အဖြစ် spawn လုပ်ပြီး stdin/stdout pipe များမှတဆင့် ဆက်သွယ်သည်၊ network သို့မဟုတ် HTTP မပါပါ။

<img src="../../../translated_images/my/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport လည်ပတ်ပုံ - application သည် MCP server ကို child process အဖြစ် ဖန်တီး၍ stdin/stdout pipe များမှတဆင့် ဆက်သွယ်သည်။*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) စကားဝိုင်းနှင့် စမ်းသပ်ပါ:** [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ဖိုင်ကို ဖွင့်ပြီး မေးမြန်းပါ -
> - "Stdio transport က မည်သို့ လည်ပတ်ပြီး HTTP နှင့် ဘယ်အချိန်သုံးသင့်သနည်း?"
> - "LangChain4j သည် spawned MCP server process များ၏ lifecycle ကို မည်သို့ စီမံကိန်းချရေးသနည်း?"
> - "AI ကို ဖိုင်စနစ်သို့ ဝင်ခွင့်ပေးခြင်း၏ ဘေးကင်းလုံခြုံမှုဆိုင်ရာ အကျိုးဆက်များရှိသနည်း?"

## The Agentic Module

MCP သည် standard tools များ ပေးသည်မှသာ LangChain4j ၏ **agentic module** သည် ထို tools များကို မောင်းနှင်သည့် agents များကို ကြေညာပုံစံဖြင့် တည်ဆောက်နေရာဖြစ်သည်။ `@Agent` annotation နှင့် `AgenticServices` တို့ကို အသုံးပြုပြီး interface များမှ တစ်ဆင့် agent ဝါရင့်မှုကို သတ်မှတ်နိုင်ပါသည်၊ imperative code မလိုပါ။

ဤ module တွင် "Supervisor Agent" ပုံစံ ဖြစ်သော အဆင့်မြင့် agentic AI နည်းလမ်းကို စတင်လေ့လာမည်။ ထိုစနစ်တွင် "supervisor" agent သည် အသုံးပြုသူတောင်းဆိုချက်အပေါ် အခြေခံ၍ dynamic ဖြစ်စေသော sub-agents များကို အသုံးပြုရန် ဆုံးဖြတ်သည်။ နှစ်ခုလုံးကို ပေါင်းပြီး sub-agent တစ်ခုအား MCP ပါဝင်သော ဖိုင်ရယူရေးကိရိယာတို့နှင့် ပေးသော နည်းလမ်းကို ချိတ်ဆက်ပြသပါမည်။

agentic module ကိုသုံးရန် Maven dependency ကို ထည့်ပါ။

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
> **မှတ်ချက်:** `langchain4j-agentic` module သည် `langchain4j.mcp.version` ဟူသော version property ကို သီးခြား သတ်မှတ်ထားသည်၊ လို့ core LangChain4j library များနှင့် မတူညီသော release အချိန်ဇယားရှိသည်။

> **⚠️ စမ်းသပ်ဖြစ်နေသည့် module:** `langchain4j-agentic` သည် **စမ်းသပ်ဖြစ်ပြီး** အသေးစား ပြုပြင်မှုများ ဖြစ်နိုင်ပါသည်။ AI assistants များ တည်ဆောက်ရာတွင် အတည်ရှိသောနည်းလမ်းမှာ `langchain4j-core` နှင့် custom tools များဖြစ်သည် (Module 04 ကို ကြည့်ပါ)။

## Running the Examples

### Prerequisites

- [Module 04 - Tools](../04-tools/README.md) ဖြတ်ပြီးဖြစ်ရမည် (ဤ module သည် custom tools ဆိုင်ရာ အကြောင်းအရာများနှင့် MCP tools တို့ကို နှိုင်းယှဉ်ထားသည်)
- Azure အတွက် အတည်ပြုပြီး `.env` ဖိုင် ကို root directory တွင် ထည့်ထားရမည် (Module 01 တွင် `azd up` ဖြင့် ဖန်တီးသည်)
- Java 21+ နှင့် Maven 3.9+
- Node.js 16+ နှင့် npm (MCP servers များအတွက်)

> **မှတ်ချက်:** ဤအချိန်တွင် သင့် environment variables မသတ်မှတ်ထားရသေးပါက [Module 01 - Introduction](../01-introduction/README.md) တွင် deployment လမ်းညွှန်ချက်များ လေ့လာပါ (`azd up` သည် `.env` ကို အလိုအလျောက် ဖန်တီးသည်)၊ မဟုတ်လျှင် root directory တွင် `.env.example` ၏ မိတ္တူအား `.env` ဟူ၍ အမည်ပြောင်းထည့်သွင်းပြီး သင့်အချက်အလက်များဖြည့်သွင်းပါ။

## Quick Start

**VS Code ကိုအသုံးပြုနေပါက:** Explorer တွင် မည်သည့် demo ဖိုင်မဆို right-click ပြီး **"Run Java"** ကိုရွေးချယ်ပါ၊ လည်းရ Run and Debug panel မှ Launch configuration များကို အသုံးပြုနိုင်သည် (ရှေ့ဆုံး `.env` ဖိုင်မှာ Azure မှအတည်ပြုပြီးကိုအတည်ပြုပါ)။

**Maven အသုံးပြုရန်:** သင် သက်ဆိုင်ရာ အောက်ပါ commands များဖြင့် command line မှ run နိုင်သည်။

### File Operations (Stdio)

ဤနမူနာသည် local subprocess-based tools များကို ပြသသည်။

**✅ လိုအပ်ချက် မရှိပါ** - MCP server ကို အလိုအလျောက် spawn ထားသည်။

**Start Scripts (အကြံပြု):**

Start scripts သည် root `.env` ဖိုင်မှ environment variables များကို အလိုအလျောက် ပြင်ဆင်တင်သွင်းသည်။

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

**VS Code ထဲတွင်:** `StdioTransportDemo.java` ဖိုင်ကို right-click ပြီး **"Run Java"** ကိုရွေးချယ်ပါ (သင့် `.env` ကို အတည်ပြုထားရန် အာမခံပါ)။

Application သည် filesystem MCP server ကို autospawn ပြုလုပ်ကာ တစ်ခုသော local ဖိုင်ကို ဖတ်သည်။ subprocess စီမံခန့်ခွဲမှုကို သင့်အတွက် စီရင်ပေးသည်ကို သတိပြုပါ။

**မျှော်မှန်းသည့် output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```

### Supervisor Agent

**Supervisor Agent pattern** သည် **အလွယ်တကူပြောင်းလဲစေရန်** ရည်ရွယ်သော agentic AI ပုံစံတစ်ခု ဖြစ်သည်။ Supervisor သည် LLM ကို အသုံးပြုပြီး အသုံးပြုသူ၏ တောင်းဆိုချက်အားအခြေခံကာ sub-agents များကို အလိုအလျောက် ဆုံးဖြတ်ခေါ်ယူသည်။ နမူနာတွင် MCP ပါဝင်သော ဖိုင်ရယူရေးနည်းလမ်းနှင့် LLM agent ကို ပေါင်းစပ်၍ supervised ဖိုင်ဖတ်ခြင်း → အစီရင်ခံစာ workflow ဖန်တီးထားသည်။

Demo တွင် `FileAgent` သည် MCP filesystem tools အသုံးပြုကာ ဖိုင် ဖတ်သည်၊ `ReportAgent` သည် အမှုန်းသိမ်းချက် (စကားစု ၁ကြောင်း), အချက်အရေးပါတ် ၃ ခု နှင့် အကြံပြုချက်များ ပါရှိသော ဖွဲ့စည်းထားသော အစီရင်ခံစာကို ထုတ်ပေးသည်။ Supervisor သည် ၎င်း flow ကို အလိုအလျောက် စီမံသွားသည်။

<img src="../../../translated_images/my/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor သည် ၎င်း၏ LLM ကို အသုံးပြုပြီး အသုံးပြုရန် agents များနှင့် အစီအစဉ်ကွက်ကို ဆုံးဖြတ်သည် - hardcoded routing မလိုတော့ပါ။*

ဒီမှာ ကျွန်ုပ်တို့၏ ဖိုင်မှအစီရင်ခံစာ pipeline ၏ ပုံမှန် workflow ဖြစ်သည်။

<img src="../../../translated_images/my/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent သည် MCP tools ဖြင့် ဖိုင်ကို ဖတ်ပြီး၊ ReportAgent သည် အမှန်တကယ်ရှိသော အကြောင်းအရာကို ဖွဲ့စည်းထားသော အစီရင်ခံစာအဖြစ် ပြောင်းလဲပေးသည်။*

Agent တစ်ခုချင်း output ကို **Agentic Scope** (မှတ်ဉာဏ် ဝေမျှထားသည်) တွင် သိမ်းထားသောကြောင့် downstream agents များသည် ယခင်ရလဒ်များကို စိတ်တိုင်းကျ အသုံးပြုနိုင်သည်။ ဤသည်ဖြင့် MCP tools များကို agentic workflows အတွင်း မဖြစ်မနေ ချိတ်ဆက်ထားသည့် ပုံကို ထင်ဟပ်စေသည် - Supervisor သည် ဖိုင်များကို မည်သို့ ဖတ်သည့်အကြောင်း သိရန် မလိုပဲ `FileAgent` က ၎င်းကို အားထားနိုင်သည်။

#### Running the Demo

Start scripts သည် root `.env` ဖိုင်မှ environment variables များကို အလိုအလျောက် ရယူသည်။

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

**VS Code မှာ:** `SupervisorAgentDemo.java` ကို right-click လုပ်ပြီး **"Run Java"** ကိုရွေးပါ (သင့် `.env` သေချာ ထည့်သွင်းထားရန်)။

#### How the Supervisor Works

Agents များ ဖန်တီးရမယ့်အခါ MCP transport ကို client နှင့် ချိတ်ဆက်ကာ `ToolProvider` အနေနဲ့ wrap လုပ်ဖို့ လိုသည်။ MCP server ၏ tools များကို ကိုယ်စားပြုသောအချိန် ဒီလိုဖြစ်သည်။

```java
// သယ်ယူပို့ဆောင်မှုမှ MCP client ကိုဖန်တီးပါ
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// client ကို ToolProvider အဖြစ် အပတ်ဝန်းကျင်ဖြင့် အသုံးပြုပါ - ၎င်းသည် MCP tools များကို LangChain4j နှင့် ဆက်သွယ်ပေးသည်
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```

ယခု `mcpToolProvider` ကို MCP tools လိုအပ်သည့် မည်သည့် agent ထဲသို့မဆို ထည့်သွင်း အသုံးပြုနိုင်ပါပြီ။

```java
// အဆင့် ၁: FileAgent က MCP ကိရိယာများကို အသုံးပြု၍ ဖိုင်များကို ဖတ်ခြင်း
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // ဖိုင်လုပ်ဆောင်မှုများအတွက် MCP ကိရိယာများ ရှိသည်
        .build();

// အဆင့် ၂: ReportAgent က ဖွဲ့စည်းတည်ဆောက်ထားသော အစီရင်ခံစာများကို ဖန်တီးသည်
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor က ဖိုင် → အစီရင်ခံစာ လုပ်ငန်းစဉ်ကို စီမံခန့်ခွဲသည်
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // နောက်ဆုံးအစီရင်ခံစာကို ပြန်တမ်းပေးသည်
        .build();

// Supervisor သည် တောင်းဆိုချက်အပေါ် မူတည်၍ ဂျင့်များကို ခေါ်ဆောင်ရန် ဆုံးဖြတ်သည်
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```

#### Response Strategies

`SupervisorAgent` ကို configure လုပ်ရာတွင် sub-agents များ လုပ်ဆောင်ပြီးနောက် အသုံးပြုသူထံ ပေးရန် နောက်ဆုံးဖြေချက်ကို မည်သို့ ဖော်စပ်ရမည်ကို သတ်မှတ်ပါသည်။ အောက်တွင်  ရနိုင်သည့် strategies သုံးခုကို  ဖော်ပြထားသည် - LAST သည် နောက်ဆုံး agent ၏ output ကို တိုက်ရိုက် ပြန်ပေးသည်၊ SUMMARY သည် အားလုံးကို LLM ဖြင့် စုပေါင်း အနှစ်ချုပ် ပြုလုပ်ပြီး ပေးသည်၊ SCORED သည် မူလတောင်းဆိုချက်အား အခြေခံ၍ ပိုမိုမြင့်မားသော score ရသော output ကိုရွေးပေးသည်။

<img src="../../../translated_images/my/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>

*Supervisor ၏ နောက်ဆုံးဖြေချက်ဖော်စပ်မှု အတွက် strategy သုံးမျိုး - နောက်ဆုံး agent output, စုပေါင်းအနှစ်ချုပ်, အကောင်းဆုံးအမှတ်ရရှိသည့် အဖြေမှ အသုံးပြုသူရွေးချယ်နိုင်သည်။*

ရနိုင်သော strategies များမှာ -

| Strategy | Description |
|----------|-------------|
| **LAST** | Supervisor သည် နောက်ဆုံး sub-agent သို့ tool မှ ရရှိသော output ကို ပြန်ထုတ်ပေးသည်။ workflow ၏ နောက်ဆုံး agent သည် အပြည့်အစုံနောက်ဆုံးဖြေချက် ထုတ်ပေးရန် အထူးဒီဇိုင်းထားပါက အသုံးဝင်သည် (ဥပမာ - သုတေသန pipeline တွင် "Summary Agent")။ |
| **SUMMARY** | Supervisor သည် ကိုယ်ပိုင် Language Model (LLM) ကို အသုံးပြုကာ interaction အားလုံးနှင့် sub-agent outputs များကို စုစည်း အနှစ်ချုပ် တစ်ခု ပြုလုပ်ပြီး ၎င်းကို နောက်ဆုံးဖြေချက်အဖြစ် ပြန်ပေးသည်။ အသုံးပြုသူအတွက် သန့်ရှင်းသည့် ပေါင်းစပ်ထားသော ဖြေချက် ပေးနိုင်သည်။ |
| **SCORED** | စနစ်သည် ကိုယ်ပိုင် LLM ကို နောက်ဆုံးဖြေချက် (LAST) နှင့် စုပေါင်းအနှစ်ချုပ် (SUMMARY) နှစ်ခုလုံးကို မူလတောင်းဆိုချက်နှင့် မည်သည်ကပိုမို သင့်တော်သော score ရရှိသည်ဟု ရောနှော နှိုင်းယှဉ်ပြီး၊ အကောင်းဆုံး ရလဒ်ကို ပြန်ပေးသည်။ |
See [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) for the complete implementation.

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) and ask:
> - "How does the Supervisor decide which agents to invoke?"
> - "What's the difference between Supervisor and Sequential workflow patterns?"
> - "How can I customize the Supervisor's planning behavior?"

#### Understanding the Output

When you run the demo, you'll see a structured walkthrough of how the Supervisor orchestrates multiple agents. Here's what each section means:

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```

**The header** introduces the workflow concept: a focused pipeline from file reading to report generation.

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

**Workflow Diagram** shows the data flow between agents. Each agent has a specific role:
- **FileAgent** reads files using MCP tools and stores raw content in `fileContent`
- **ReportAgent** consumes that content and produces a structured report in `report`

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```

**User Request** shows the task. The Supervisor parses this and decides to invoke FileAgent → ReportAgent.

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

**Supervisor Orchestration** shows the 2-step flow in action:
1. **FileAgent** reads the file via MCP and stores the content
2. **ReportAgent** receives the content and generates a structured report

The Supervisor made these decisions **autonomously** based on the user's request.

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

The example demonstrates several advanced features of the agentic module. Let's have a closer look at Agentic Scope and Agent Listeners.

**Agentic Scope** shows the shared memory where agents stored their results using `@Agent(outputKey="...")`. This allows:
- Later agents to access earlier agents' outputs
- The Supervisor to synthesize a final response
- You to inspect what each agent produced

The diagram below shows how Agentic Scope works as shared memory in the file-to-report workflow — FileAgent writes its output under the key `fileContent`, ReportAgent reads that and writes its own output under `report`:

<img src="../../../translated_images/my/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>

*Agentic Scope acts as shared memory — FileAgent writes `fileContent`, ReportAgent reads it and writes `report`, and your code reads the final result.*

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // FileAgent မှ မူလဖိုင်ဒေတာ
String report = scope.readState("report");            // ReportAgent မှ ဖွဲ့စည်းထားသောအစီရင်ခံစာ
```

**Agent Listeners** enable monitoring and debugging of agent execution. The step-by-step output you see in the demo comes from an AgentListener that hooks into each agent invocation:
- **beforeAgentInvocation** - Called when the Supervisor selects an agent, letting you see which agent was chosen and why
- **afterAgentInvocation** - Called when an agent completes, showing its result
- **inheritedBySubagents** - When true, the listener monitors all agents in the hierarchy

The following diagram shows the full Agent Listener lifecycle, including how `onError` handles failures during agent execution:

<img src="../../../translated_images/my/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>

*Agent Listeners hook into the execution lifecycle — monitor when agents start, complete, or encounter errors.*

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
        return true; // ကိုယ်စားလှယ်ငယ်အားလုံးထံသို့ ဖြန့်ဝေပါ
    }
};
```

Beyond the Supervisor pattern, the `langchain4j-agentic` module provides several powerful workflow patterns. The diagram below shows all five — from simple sequential pipelines to human-in-the-loop approval workflows:

<img src="../../../translated_images/my/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>

*Five workflow patterns for orchestrating agents — from simple sequential pipelines to human-in-the-loop approval workflows.*

| Pattern | Description | Use Case |
|---------|-------------|----------|
| **Sequential** | Execute agents in order, output flows to next | Pipelines: research → analyze → report |
| **Parallel** | Run agents simultaneously | Independent tasks: weather + news + stocks |
| **Loop** | Iterate until condition met | Quality scoring: refine until score ≥ 0.8 |
| **Conditional** | Route based on conditions | Classify → route to specialist agent |
| **Human-in-the-Loop** | Add human checkpoints | Approval workflows, content review |

## Key Concepts

Now that you've explored MCP and the agentic module in action, let's summarize when to use each approach.

One of MCP's biggest advantages is its growing ecosystem. The diagram below shows how a single universal protocol connects your AI application to a wide variety of MCP servers — from filesystem and database access to GitHub, email, web scraping, and more:

<img src="../../../translated_images/my/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>

*MCP creates a universal protocol ecosystem — any MCP-compatible server works with any MCP-compatible client, enabling tool sharing across applications.*

**MCP** is ideal when you want to leverage existing tool ecosystems, build tools that multiple applications can share, integrate third-party services with standard protocols, or swap tool implementations without changing code.

**The Agentic Module** works best when you want declarative agent definitions with `@Agent` annotations, need workflow orchestration (sequential, loop, parallel), prefer interface-based agent design over imperative code, or are combining multiple agents that share outputs via `outputKey`.

**The Supervisor Agent pattern** shines when the workflow isn't predictable in advance and you want the LLM to decide, when you have multiple specialized agents that need dynamic orchestration, when building conversational systems that route to different capabilities, or when you want the most flexible, adaptive agent behavior.

To help you decide between the custom `@Tool` methods from Module 04 and MCP tools from this module, the following comparison highlights the key trade-offs — custom tools give you tight coupling and full type safety for app-specific logic, while MCP tools offer standardized, reusable integrations:

<img src="../../../translated_images/my/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>

*When to use custom @Tool methods vs MCP tools — custom tools for app-specific logic with full type safety, MCP tools for standardized integrations that work across applications.*

## Congratulations!

You've made it through all five modules of the LangChain4j for Beginners course! Here's a look at the full learning journey you've completed — from basic chat all the way to MCP-powered agentic systems:

<img src="../../../translated_images/my/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>

*Your learning journey through all five modules — from basic chat to MCP-powered agentic systems.*

You've completed the LangChain4j for Beginners course. You've learned:

- How to build conversational AI with memory (Module 01)
- Prompt engineering patterns for different tasks (Module 02)
- Grounding responses in your documents with RAG (Module 03)
- Creating basic AI agents (assistants) with custom tools (Module 04)
- Integrating standardized tools with the LangChain4j MCP and Agentic modules (Module 05)

### What's Next?

After completing the modules, explore the [Testing Guide](../docs/TESTING.md) to see LangChain4j testing concepts in action.

**Official Resources:**
- [LangChain4j Documentation](https://docs.langchain4j.dev/) - Comprehensive guides and API reference
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - Source code and examples
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - Step-by-step tutorials for various use cases

Thank you for completing this course!

---

**Navigation:** [← Previous: Module 04 - Tools](../04-tools/README.md) | [Back to Main](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**အသိပေးချက်**  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှန်ကန်မှုအတွက် ကြိုးပမ်းနေသော်လည်း၊ အလိုအလျောက် ဘာသာပြန်ခြင်းသည် အမှားအယွင်းများ သို့မဟုတ် တိကျမှုနည်းပါးမှုများ ပါဝင်နိုင်သည်ကို သတိပြုပါ။ မူရင်းစာရွက်စာတမ်းကို မိခင်ဘာသာဖြင့်သာ ယုံကြည်စိတ်ချရသော အရင်းခံအဖြစ် ဂရုစိုက်စဉ်းစားသင့်သည်။ အရေးကြီးသောအချက်အလက်များအတွက် အသက်အကြီးဆုံး လူမူမဟုတ်ဘဲ ပရော်ဖက်ရှင်နယ် လူသားဘာသာပြန်နဲ့ ပြန်ဆိုစေခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်ချက်အသုံးပြုခြင်းကြောင့် ဖြစ်ပေါ်လာနိုင်သည့် နားလည်မှုကွဲပြားမှု သို့မဟုတ် မှားယွင်းမှုများအတွက် ကျွန်ုပ်တို့မှာ တာဝန်မရှိပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->