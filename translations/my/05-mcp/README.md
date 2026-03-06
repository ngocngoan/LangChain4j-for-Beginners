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
    - [How FileAgent Discovers MCP Tools at Runtime](../../../05-mcp)
    - [Response Strategies](../../../05-mcp)
    - [Understanding the Output](../../../05-mcp)
    - [Explanation of Agentic Module Features](../../../05-mcp)
- [Key Concepts](../../../05-mcp)
- [Congratulations!](../../../05-mcp)
  - [What's Next?](../../../05-mcp)

## What You'll Learn

သင်ဟာ စကားပြော AI တစ်ခု တည်ဆောက်ပြီး၊ prompt များကိုကျွမ်းကျင်စွာ အသုံးပြုပြီး၊ စာရွက်စာတမ်းထဲမှာ အကြောင်းအရာကိုခံရပြီး၊ tools တွေနဲ့ agents များ ဖန်တီးထားပါတယ်။ ဒါပေမဲ့ အဲဒီ tools အကုန်လုံးဟာ သင့်ရဲ့ application အတွက် သီးသန့် custom ထုတ်ထားတာတွေဖြစ်ပါတယ်။ သင့် AI ကို လူတိုင်း ဖန်တီး၍မျှဝေခွင့်ရနိုင်သော စံချိန်စနစ်(tool ecosystem) တစ်ခုရရှိစေလိုလားပါသလား? ဒီ module မှာ သင်ရှင်းလင်းကျယ်ပြန့်စွာ သိရှိသွားမယ့် Model Context Protocol (MCP) နဲ့ LangChain4j ရဲ့ agentic module အသုံးပြုပုံတွေ ကို သင်ယူပါလိမ့်မယ်။ ပထမဦးဆုံးမှာတော့ ရိုးရှင်းတဲ့ MCP ဖိုင်ဖတ်စက်ကို ပြသပြီးနောက်၊ Supervisor Agent ပုံစံကို အသုံးပြုကာ advanced agentic workflow တွေနဲ့ အလွယ်တကူ ပေါင်းစည်းသတ်မှတ်ပြပါမယ်။

## What is MCP?

Model Context Protocol (MCP) ဆိုတာ AI applications များအတွက် အထူးသဖြင့် ဘယ် tools မဆို ရှာဖွေရန်နဲ့ အသုံးပြုရန် ရိုးရှင်းတဲ့ စံချိန်စနစ်တစ်ခု ဖြစ်ပါတယ်။ ဒီလို ဖန်တီးမှုတွေမှာ data source တစ်ခုချင်းစီကို custom integration ရေးဖို့မလိုဘဲ MCP servers များ ဆက်သွယ်ပါက သက်ဆိုင်ရာစွမ်းရည်များကို တူညီသော ဖော်ပြပုံနဲ့ ဖော်ပြထားပါတယ်။ သင့် AI agent ကတစ်ဆင့် အဲဒီ tools တွေကို အလိုအလျောက် ရှာဖွေ၍ အသုံးပြုနိုင်ပါတယ်။

အောက်ပါ diagram မှာ MCP မပါရင်က အကြောင်းပြချက်ကို ဖော်ပြထားပြီး MCP ပါရင် ကွာခြားချက်ကိုပြနေပါတယ်- MCP မပါရင် တစ်ခုချင်း စိတ်ကြိုက် ချိတ်ဆက်ရခြင်း ဖြစ်ပြီး၊ MCP ပါရင် protocol တစ်ခုနဲ့ သင့် app ကို မည်သည့် tool နဲ့မဆို ချိတ်ဆက်နိုင်ပါတယ်။

<img src="../../../translated_images/my/mcp-comparison.9129a881ecf10ff5.webp" alt="MCP Comparison" width="800"/>

*Before MCP: အပြင်းအထန် point-to-point ချိတ်ဆက်မှုများ။ After MCP: protocol တစ်ခုနဲ့ မျိုးစုံ အခွင့်အလမ်းများ။*

MCP က AI ဖွံ့ဖြိုးတိုးတက်မှုမှာ အဓိကဖြေရှင်းချက် တစ်ခု ပေးပါတယ်- အားလုံးက မူရင်း အထူးရေးသားထားတဲ့ integration တွေ ဖြစ်နေပါတယ်။ GitHub ကိုเข้าถึงချင်သလား? Custom code. ဖိုင်တွေ ဖတ်ချင်သလား? Custom code. အချက်အလက် သိုလှောင်မှုကို မေးမြန်းချင်သလား? Custom code. ပြီးတော့ ဒီ integration များဟာ အခြား AI applications တွေနဲ့ မသုံးနိုင်ဘူး။

MCP ဟာဒီကို စံချိန်စနစ်တစ်ခု ချထားပါတယ်။ MCP server တစ်ခုက Tools တွေကိုရှင်းလင်းတဲ့ ဖော်ပြချက်နဲ့ Schema တွေနဲ့ ပြသပြီး MCP client များက ချိတ်ဆက်ပြီး ရရှိနိုင်တဲ့ tools များကို ရှာဖွေနိုင်ပါတယ်။ တစ်ခေါက်ဖန်တီးပြီး နေရာတိုင်းမှာသုံးနိုင်ပါတယ်။

အောက်က ပုံစံမှာ MCP client တစ်ခု (သင့် AI application) က MCP servers များစွာကို ချိတ်ဆက်ပြီး ၎င်းတို့၏ စံ protocol ဖြင့် သူတို့ရဲ့ tools များကို ပေးထားတာကို ပြထားပါတယ်။

<img src="../../../translated_images/my/mcp-architecture.b3156d787a4ceac9.webp" alt="MCP Architecture" width="800"/>

*Model Context Protocol အဆောက်အအုံ – စံတော်တစ်ခုဖြင့် tool ရှာဖွေရေး နှင့် အရေးဆွဲခြင်း။*

## How MCP Works

အောက်ခံမှာ MCP ဟာ အလွှာတင်အဆောက်အအုံကို အသုံးပြုပါတယ်။ သင့် Java application (MCP client) ဟာ ရရှိနိုင်တဲ့ tools များကို ရှာဖွေပေးပြီး JSON-RPC အမိန့်များကို transport layer (Stdio သို့မဟုတ် HTTP) မှတဆင့် ပေးပို့ပါတယ်။ MCP server က အဆိုပါခြွင်းချက်များကို ဆောင်ရွက်ပြီး ရလဒ်တွေရယူစေပါတယ်။ အောက်ပါ ပုံစံမှာ protocol ၏ အလွှာတစ်ခုချင်း ဖေါ်ပြထားပါတယ်။

<img src="../../../translated_images/my/mcp-protocol-detail.01204e056f45308b.webp" alt="MCP Protocol Detail" width="800"/>

*MCP အပြင်အဆင် — clients များက tools တွေကို ရှာဖွေပြီး JSON-RPC မက်ဆေ့ဂျ်များလဲလှယ်ခြင်းနှင့် transport အလွှာမြောက် operation များ ဆောင်ရွက်ခြင်း။*

**Server-Client Architecture**

MCP က client-server ပုံစံကို အသုံးပြုပါတယ်။ Servers တွေက tools များ ပံ့ပိုးပေးသည် - ဖိုင်ဖတ်ခြင်း၊ ဒေတာဘေ့စ် မေးမြန်းခြင်း၊ API ခေါ်ဆိုခြင်း။ Clients (သင့် AI application) က servers များဆီသို့ ချိတ်ဆက်ပြီး tools များကို အသုံးပြုသည်။

LangChain4j နှင့် MCP ကို အသုံးပြုရန် ဒီ Maven dependency ကို ထည့်ပါ။

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-mcp</artifactId>
    <version>${langchain4j.version}</version>
</dependency>
```
  
**Tool Discovery**

သင့် client က MCP server ကို ချိတ်ဆက်ချိန် "မင်းမှာ ဘယ် tools တွေရှိလဲ?" ဟု မေးမြန်းတော့ server က ရရှိနိုင်တဲ့ tools စာရင်းကို ကျနော်တို့ကို ထုတ်ပြန်မယ်။ ၎င်းတွင် ဖော်ပြချက်နှင့် parameter schema တို့ ပါရှိသည်။ သင့် AI agent က user အမိန့်များအပေါ် မူတည်ကာ ဘယ် tools ကို အသုံးပြုမလဲ ဆုံးဖြတ်နိုင်သည်။ အောက်ပုံတွင် ဒီ handshake ကို ဖော်ပြထားသည် — client က `tools/list` အမိန့်ပို့ပြီး server က နေရပ်ရှိ tools များအတွက် ဖော်ပြချက်များနှင့် parameter schema များဖြင့် ပြန်လည်ပေးပို့သည်။

<img src="../../../translated_images/my/tool-discovery.07760a8a301a7832.webp" alt="MCP Tool Discovery" width="800"/>

*AI က စတင်ကနေ ရနိုင်သော tools များကို ရှာဖွေပြီး ယခု စွမ်းရည်များကို သိရှိကာ ဘယ် tools ကို အသုံးပြုမလဲ ဆုံးဖြတ်နိုင်သည်။*

**Transport Mechanisms**

MCP က သင့်တော်သော transport mechanism များကို ပံ့ပိုးသည်။ ရွေးချယ်မှုများမှာ Stdio (ဒေသတွင်း subprocess ဆက်သွယ်မှုအတွက်) နှင့် Streamable HTTP (အဝေးမှ server များအတွက်) ဖြစ်သည်။ ဒီ module မှာ Stdio transport ကို သင်ခန်းစာပြပါတယ်။

<img src="../../../translated_images/my/transport-mechanisms.2791ba7ee93cf020.webp" alt="Transport Mechanisms" width="800"/>

*MCP ၏ transport များ - အဝေးမှ server များအတွက် HTTP, ဒေသတွင်း processes အတွက် Stdio*

**Stdio** - [StdioTransportDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java)

ဒေသတွင်း processes များ အတွက်။ သင့် application က subprocess အဖြစ် MCP server ကို စတင်ပြီး standard input/output မှတဆင့် ဆက်သွယ်သည်။ ဖိုင်စနစ်ရယူခြင်း သို့မဟုတ် command-line tools များအတွက် အသုံးဝင်သည်။

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
  
`@modelcontextprotocol/server-filesystem` server သည် သင့် သတ်မှတ်ထားသော directory များ၏ sandbox အတွင်းရှိ အောက်ပါ tools များအား ဖော်ပြထားသည်-

| Tool | Description |
|------|-------------|
| `read_file` | တစ်ခုတည်းသော ဖိုင်ပါဝင်မှုကို ဖတ်ရန် |
| `read_multiple_files` | တစ်ကြိမ်လောက် ပေါင်းစပ်ပြီးဖိုင်များစွာ ဖတ်ရန် |
| `write_file` | ဖိုင်အသစ် တင်ရန် သို့မဟုတ် ဖိုင် ပုံစံပြင်ရန် |
| `edit_file` | ရှာဖွေပြီး အစားထိုး ပြင်ဆင်ခြင်းများပြုရန် |
| `list_directory` | လမ်းကြောင်းတစ်ခုသို့ ဖိုင်များနဲ့ directory များစာရင်းပြုစုရန် |
| `search_files` | ပုံစံနှင့် ကိုက်ညီသော ဖိုင်များကို ထပ်တလဲလဲ ရှာဖွေရန် |
| `get_file_info` | ဖိုင် metadata (အရွယ်အစား၊ အချိန်မှတ်တမ်းများ၊ ခွင့်ပြုချက်များ) ရယူရန် |
| `create_directory` | directory တစ်ခု ဖန်တီးရန် (မိထွက် directory များပါဝင်) |
| `move_file` | ဖိုင် သို့မဟုတ် directory ကို ရွှေ့လျားရန် သို့မဟုတ် အမည်ပြောင်းရန် |

အောက်ပါ diagram မှာ Stdio transport ဟာ runtime မှာ ဘယ်လို လည်ပတ်သလဲ ဖော်ပြထားပြီး- သင့် Java application က MCP server ကို child process အဖြစ် စတင်ပြီး stdin/stdout pipe များမှတဆင့် ဆက်သွယ်ပါတယ်၊ network သို့မဟုတ် HTTP မပါဝင်ပါ။

<img src="../../../translated_images/my/stdio-transport-flow.45eaff4af2d81db4.webp" alt="Stdio Transport Flow" width="800"/>

*Stdio transport လည်ပတ်နေစဉ် - သင့် application က MCP server ကို child process အဖြစ် စတင်ပြီး stdin/stdout pipe များမှတဆင့် ဆက်သွယ်သည်။*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် ကြိုးစားကြည့်ရန်:** [`StdioTransportDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/StdioTransportDemo.java) ဖိုင်ကို ဖွင့်ပြီး မေးမြန်းပါ-
> - "Stdio transport ဘယ်လို လည်ပတ်ပြီး ဘယ်အချိန်တွင် HTTP နဲ့ တွဲသုံးသင့်ပါသလဲ?"
> - "LangChain4j က MCP server process များ၏ lifecycle ကို ဘယ်လို စီမံခန့်ခွဲသလဲ?"
> - "AI ကို ဖိုင်စနစ် အသုံးပြုခွင့် ပေးတဲ့အတွက် ဘာလုံခြုံရေး ဆိုးကျိုးများ ရှိနိုင်သလဲ?"

## The Agentic Module

MCP က standardized tools တွေကို ပံ့ပိုးပေးသော်လည်း LangChain4j ရဲ့ **agentic module** က tools တွေကို စီမံရင်း agents တွေ ဖြစ်အောင် လုပ်ဆောင်ပုံများကို ထိန်းချုပ်ခြင်းအတွက် အကြောင်းကြားသေချာပြီ ဖန်တီးပေးပါတယ်။ `@Agent` annotation နဲ့ `AgenticServices` က imperative code မလိုပဲ interface များ နှင့် agent ၏ သွင်ပြင်ပြောင်းလဲမှုကို သတ်မှတ်နိုင်စေပါတယ်။

ဒီ module မှာ သင်ကြုံတွေ့မယ့် အဆင့်မြင့် agentic AI နည်းလမ်းတစ်ခုက **Supervisor Agent** ပုံစံ ဖြစ်သည် — user ရဲ့ တောင်းဆိုချက်အပေါ်တွင် dynamic လုပ်ပြီး sub-agents များကို ဖေါ်ထုတ်သုံးဆောင်ပေးပါတယ်။ MCP support ပါတဲ့ file access အင်အားမြှင့် sub-agent တစ်ခုကို သုံးပြီး ဒီနှစ်ခုကို ပေါင်းစပ် ပြသပါမယ်။

Agentic module အသုံးပြုနိုင်ရန် ဒီ Maven dependency ကို ထည့်ပါ-

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-agentic</artifactId>
    <version>${langchain4j.mcp.version}</version>
</dependency>
```
  
> **မှတ်ချက်:** `langchain4j-agentic` module က အခြား LangChain4j core library များထက် ထွက်ရှိချိန်ကွာခြားမှု ရှိသဖြင့် version property (`langchain4j.mcp.version`) ကို သီးခြားသတ်မှတ်ထားသည်။

> **⚠️ စမ်းသပ်မှုနည်း:** `langchain4j-agentic` module တွင် သူ့ဘာသာအရ ပြောင်းလဲနိုင်မှုများ ရှိသေးသော experimental အဆင့်ဖြစ်သည်။ AI assistants များ ဖန်တီးရာတွင် အတည်ပြုထားသောနည်းလမ်းမှာ `langchain4j-core` module အတွက် custom tools များ အသုံးပြုခြင်းဖြစ်သည် (Module 04)။

## Running the Examples

### Prerequisites

- [Module 04 - Tools](../04-tools/README.md) ဆောင်ရွက်ပြီးဖြစ်ရန် (ဒီ module မှာတော့ custom tools concept တွေကို MCP tools တွေနဲ့ နှိုင်းယှဉ် ပြသထားသည်)
- Root စာမျက်နှာထဲ `.env` ဖိုင် ကို Azure အမည်လက်မှတ် များဖြင့် ပြည့်စုံထားရန် (`azd up` ကို Module 01 မှာ အသုံးပြု၍ ဖန်တီးထားသည်)
- Java 21+ နှင့် Maven 3.9+
- Node.js 16+ နဲ့ npm (MCP servers များအတွက်)

> **မှတ်ချက်:** ဖတ်ရှုသူ မည်သည့်အခါမဆို သင့် environment variables များကို စတင်ချိန်လာလျှင် [Module 01 - Introduction](../01-introduction/README.md) တွင် ဖော်ပြထားသော deployment လမ်းညွှန်ချက်များကို လိုက်နာပါ (`azd up` က `.env` ဖိုင်ကို အလိုအလျောက် ဖန်တီးပေးသည်)၊ ဒါမှမဟုတ် root directory ထဲ `.env.example` ကို `.env` ဟုပြောင်းကူးပြီး သင့်တန်ဖိုးများ ဖြည့်စွက်ပါ။

## Quick Start

**VS Code အသုံးပြုခြင်း:** Explorer မှ demo ဖိုင်တစ်ခုကို right-click ပြီး **"Run Java"** ကို ရွေးချယ်ပါ၊ ဒါမှမဟုတ် Run and Debug panel မှာ ရှိတဲ့ launch configurations တွေကိုအသုံးပြုပါ (နောက်ဆုံး `.env` ဖိုင် မှာ Azure အမှတ်ပြုချက်များကို ပြည့်စုံစေပါ).

**Maven သုံး၍:** သို့မဟုတ် command line မှနေပြီး အောက်ပါ ဥပမာများအတိုင်း run လုပ်နိုင်ပါသည်။

### File Operations (Stdio)

ဒီနမူနာက ဒေသတွင်း subprocess-based tools များကို ပြသသည်။

**✅ မည်သည့် အရင်က ပြင်ဆင်မှု မလိုအပ်ပါ** - MCP server ကို အလိုအလျောက် သင့် application မှ စတင်မြှောက်သည်။

**Start script များအသုံးပြုခြင်း (အကြံပြု):**

Start script များက root `.env` ဖိုင်မှ environment variables များကို အလိုအလျောက် load လုပ်ပေးသည်-

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
  
**VS Code အသုံးပြုခြင်း:** `StdioTransportDemo.java` ကို right-click ပြီး **"Run Java"** ကို ရွေးချယ်ပါ (`.env` ဖိုင်ရှိမှသာ)။

Application က MCP filesystem server ကို အလိုအလျောက် စတင်ပြီး ဒေသတွင်း ဖိုင်တစ်ခု ဖတ်တယ်။ Subprocess များစီမံခြင်းကို သင့်အတွက် တာဝန်ယူထားသည်။  

**မျှော်မှန်းထားသော output:**
```
Assistant response: The file provides an overview of LangChain4j, an open-source Java library
for integrating Large Language Models (LLMs) into Java applications...
```
  
### Supervisor Agent

**Supervisor Agent pattern** ဟာ agentic AI ၏ **ပေါ်လွင်သော** ပုံစံဖြစ်သည်။ Supervisor က LLM ကိုအသုံးပြုပြီး user ရဲ့ တောင်းဆိုချက်အပေါ်တွင် အလိုအလျောက် ကိုယ်စားလှယ် (agents) များကို ဖော်ထုတ်ခေါ်ရန် ဆုံးဖြတ်သည်။ နောက်နမူနာမှာ MCP-powered ဖိုင် အသုံးပြုခွင့်ရတဲ့ LLM agent တစ်ခုနဲ့ သုံး၍ supervised ဖိုင်ဖတ် → စီမံခန့်ခွဲချက် workflow တစ်ခု ဖန်တီးထားပါတယ်။

Demo အတွက် `FileAgent` ဟာ MCP filesystem tools နဲ့ ဖိုင် ဖတ်ပြီး `ReportAgent` က ဆုံးဖြတ်ချက်အနှစ်ချုပ် (ဝေါဟာရတစ်ဝေါဟာရ၊ အချက်အလက် ၃ချက်နဲ့ အကြံပြုချက်များ) အပါအဝင် ဖော်ပြချက်တစ်ခု ထုတ်လုပ်ပါတယ်။ Supervisor က ဒီ အစီအစဉ်ကို အလိုအလျောက် စီမံခန့်ခွဲပါတယ်-

<img src="../../../translated_images/my/supervisor-agent-pattern.06275a41ae006ac8.webp" alt="Supervisor Agent Pattern" width="800"/>

*Supervisor က ၎င်း၏ LLM ကို အသုံးပြုပြီး ဘယ် agent တွေကို ဘယ်အဆင့်တွင် ဖေါ်ထုတ်သုံးမလဲ ဆုံးဖြတ်သည် — hardcoded routing မလိုအပ်ပါ။*

file-to-report pipeline ၏ အတွင်းအခန်းတွေ ဒီအတိုင်းပဲ ဖြစ်တယ်

<img src="../../../translated_images/my/file-report-workflow.649bb7a896800de9.webp" alt="File to Report Workflow" width="800"/>

*FileAgent ဟာ MCP tools တွေကနေ ဖိုင်ကို ဖတ်ပြီး၊ ReportAgent က အကြောင်းအရာကို ဖော်ပြစပ် စာတမ်းတစ်စောင် ဖြစ်အောင် ပြောင်းလဲသည်။*

အောက်ပါ sequence diagram ဟာ Supervisor အလုပ်လုပ်ပုံအပြည့်အစုံကို ပြသထားသည်- MCP server ကို spawn လုပ်ခြင်း၊ Supervisor ၏ agent ရွေးချယ်မှု၊ stdio များကနေ tool call များနှင့် နောက်ဆုံးမှာ စာတမ်းပုံစံ အဖြစ် ထုတ်ပေးခြင်းတို့ဖြစ်သည်။

<img src="../../../translated_images/my/supervisor-agent-sequence.1aa389b3bef99956.webp" alt="Supervisor Agent Sequence Diagram" width="800"/>

*Supervisor က FileAgent ကို အလိုအလျောက် ဖေါ်ထုတ်ခေါ်ပြီး (MCP server ကို stdio နှင့် ဖိုင်ဖတ်ရန် ခေါ်ဆိုခြင်း) ပြီးခဲ့လျှင် ReportAgent ကို ဖော်ထုတ်ခေါ်ပြီး စာတမ်းဖန်တီးသည်- တစ်ဦးခြင်းတစ်ဦး Agentic Scope တွင် အဖြေများသိမ်းဆည်းထားသည်။*

Agent တစ်ခုချင်း၏ output များကို **Agentic Scope** (မျှဝေထားသော မှတ်ဉာဏ်) ထဲ သိမ်းဆည်းရင်း downstream agent များဟာ ယခင်ရလဒ်များကို အသုံးပြုနိုင်သည်။ ဒီဟာက MCP tools များကို agentic workflows တွေမှာ အကွာအဝေးမရှိစွာ ဝင်ရောက် ပေါင်းစည်းနိုင်တယ်ဆိုတာကို သက်သေပြတာပါ- Supervisor ဟာ ဖိုင်တွေကို ဘယ်လိုဖတ်တယ်ဆိုတာ မသိရင်ရော `FileAgent` ဟာ နည်းလမ်းဖြစ်တာကိုသာ သိရုံပါပဲ။

#### Running the Demo

Start scripts များက root `.env` ဖိုင်မှ environment variables များကို အလိုအလျောက် load လုပ်ပေးသည်-

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
  
**VS Code အသုံးပြုခြင်း:** `SupervisorAgentDemo.java` ကို right-click ပြီး **"Run Java"** ရွေးပါ ('.env' ဖိုင် ထည့်ထားရှိရန် သေချာပါ)။

#### How the Supervisor Works

Agent များတည်ဆောက်ရန်မပြုမီ MCP transport ကို client နှင့် ချိတ်ဆက်ပြီး `ToolProvider` အဖြစ် wrap လုပ်ရန် လိုအပ်သည်။ ဒီလိုနည်းတူ MCP server ထဲက tools တွေဟာ သင့် agent များအတွက် ရရှိနိုင်ပါတယ်-

```java
// ပို့ဆောင်မှုမှ MCP client တစ်ခု ဖန်တီးပါ
McpClient mcpClient = new DefaultMcpClient.Builder()
        .transport(stdioTransport)
        .build();

// client ကို ToolProvider အဖြစ် ထည့်ပေးပါ — ဒါက MCP ကိရိယာများကို LangChain4j သို့ ညှိပေးပါသည်
ToolProvider mcpToolProvider = McpToolProvider.builder()
        .mcpClients(List.of(mcpClient))
        .build();
```
  
အခုက `mcpToolProvider` ကို MCP tools လိုအပ်တဲ့ agent တစ်ခုချင်းနဲ့ inject လုပ်လို့ရပြီ-

```java
// အဆင့် ၁: FileAgent သည် MCP ကိရိယာများကို အသုံးပြု၍ ဖိုင်များကို ဖတ်သည်
FileAgent fileAgent = AgenticServices.agentBuilder(FileAgent.class)
        .chatModel(model)
        .toolProvider(mcpToolProvider)  // ဖိုင်ဆိုင်ရာ လုပ်ဆောင်ချက်များအတွက် MCP ကိရိယာများ ရှိသည်
        .build();

// အဆင့် ၂: ReportAgent သည် ဖွဲ့စည်းထားသော အစီရင်ခံစာများ ထုတ်လုပ်သည်
ReportAgent reportAgent = AgenticServices.agentBuilder(ReportAgent.class)
        .chatModel(model)
        .build();

// Supervisor သည် ဖိုင် → အစီရင်ခံစာ လုပ်ငန်းစဉ်ကို စီမံခန့်ခွဲသည်
SupervisorAgent supervisor = AgenticServices.supervisorBuilder()
        .chatModel(model)
        .subAgents(fileAgent, reportAgent)
        .responseStrategy(SupervisorResponseStrategy.LAST)  // အပြီးသတ် အစီရင်ခံစာကို ပြန်ပေးပို့သည်
        .build();

// Supervisor သည် တောင်းဆိုမှုအပေါ် မူတည်၍ မည်သည့် agent များကို ခေါ်အပ်မည်ကို ဆုံးဖြတ်သည်
String response = supervisor.invoke("Read the file at /path/file.txt and generate a report");
```
  
#### How FileAgent Discovers MCP Tools at Runtime

သင်စိတ်ပူနေရင်- **`FileAgent` က npm filesystem tools များကို ဘယ်လိုသိသလဲ?** အဖြေက အကဲဖြတ်ချက်မှာ မသိပါဘူး- **LLM** က tool schema များတစ်ဆင့် runtime မှာ ထုတ်ဖော်တင်ပြလိုက်ပါတယ်။

`FileAgent` interface ဟာ **prompt definition** ပဲဖြစ်ပြီး `read_file`, `list_directory`, သို့မဟုတ် အခြား MCP tool များအကြောင်း knowledge မဲ့ပါ။ အဆုံးသတ်လုပ်ဆောင်ချက်က ဒီလို ဖြစ်တယ်-
1. **ဆာဗာ spawn လုပ်ခြင်း:** `StdioMcpTransport` သည် `@modelcontextprotocol/server-filesystem` npm package ကို ချေးလျှောက်လုပ်ငန်းတစ်ခုအဖြစ် စတင်တင်သွင်းသည်  
2. **ကိရိယာ ရှာဖွေခြင်း:** `McpClient` သည် `tools/list` JSON-RPC တောင်းဆိုချက်ကို ဆာဗာထံ ပေးပို့ပြီး၊ ဆာဗာက ကိရိယာအမည်များ၊ ဖော်ပြချက်များနှင့် ပါရာမီတာ schema များကို တုံ့ပြန်ပေးသည် (ဥပမာ၊ `read_file` — *"ဖိုင်တစ်ခု၏ အပြည့်အစုံကို ဖတ်ပါ"* — `{ path: string }`)  
3. **Schema ထည့်သွင်းခြင်း:** `McpToolProvider` သည် ရှာဖွေတွေ့ရှိထားသော schema များကို လှုပ်ရှားပြီး LangChain4j သို့ ရရှိစေရန်ပြုလုပ်သည်  
4. **LLM ဆုံးဖြတ်ချက်:** `FileAgent.readFile(path)` ကို ခေါ်သည့်အခါ LangChain4j သည် စနစ်စာတမ်း၊ အသုံးပြုသူစာတမ်းနှင့် **ကိရိယာ schema စာရင်းကို** LLM သို့ ပို့သည်။ LLM က ကိရိယာဖော်ပြချက်များကို ဖတ်ပြီး ကိရိယာခေါ်ဆိုမှုတစ်ခု ဖန်တီးသည် (ဥပမာ၊ `read_file(path="/some/file.txt")`)  
5. **အကောင်အထည်ဖော်ခြင်း:** LangChain4j သည် ကိရိယာခေါ်ဆိုမှုကို ဖမ်းယူပြီး MCP client မှတဆင့် Node.js ချေးလျှောက်လုပ်ငန်းသို့ လမ်းညွှန်ထားကာ ဖြေကြောင်းရရှိပြီး LLM ထံ ပြန်ပို့သည်  

ဤသည်မှာ အထက်တွင် ဖော်ပြခဲ့သည့် [Tool Discovery](../../../05-mcp) မက်ကနစ်ဖြစ်ပြီး၊ အထူးသဖြင့် agent workflow အတွက် အသုံးပြုထားခြင်းဖြစ်သည်။ `@SystemMessage` နှင့် `@UserMessage` annotation များက LLM ၏ အပြုအမူကို လမ်းညွှန်ပေးပြီး၊ ထည့်သွင်းထားသော `ToolProvider` သည် **စွမ်းရည်များ** ကိုပေးသည် — LLM သည် အသုံးပြုချိန်တွင် နှစ်ခုကို တိုက်ဆိုင်စေသည်။  

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ကြည့်ပါ။** [`FileAgent.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/agents/FileAgent.java) ဖွင့်၍ မေးမြန်းပါ။
> - "ဤ agent သည် မည်သည့် MCP ကိရိယာကို ခေါ်ဆိုရမည်ကို မည်သို့သိနိုင်သနည်း?"
> - "agent builder မှ ToolProvider ကို ဖယ်ရှားလျှင် ဘာဖြစ်မလဲ?"
> - "Tool schema များကို LLM သို့ မည်သို့ ပို့သနည်း?"  

#### တုံ့ပြန်မှု မဟာဗျူဟာများ  

`SupervisorAgent` ကို ပြင်ဆင်သောအခါ၊ sub-agent များ မူလ တာဝန်များ ပြီးဆုံးပြီးနောက် အသုံးပြုသူထံ အဆုံးဖြတ်ဖြေကြောင်းကို မည်သို့ဖန်တီးမည်ကို သတ်မှတ်ရမည်။ အောက်တွင် များသောအားဖြင့် ရရှိနိုင်သည့် မဟာဗျူဟာ ၃ မျိုးကို ပြထားသည် — LAST သည် နောက်ဆုံး agent ၏ output ကို တိုက်ရိုက်ပြန်လည်ပေးပြီး၊ SUMMARY သည် အားလုံး output များကို LLM ဖြင့် စုစည်း၊ ကောက်နုတ်ပြီးပြန်ကြားသည်၊ SCORED သည် မူရင်းတောင်းဆိုချက်နှင့် နှိုင်းယှဉ်ပြီး rating ပိုမိုမြင့်သော output ကို ရွေးခွင့်ပြုသည်။  

<img src="../../../translated_images/my/response-strategies.3d0cea19d096bdf9.webp" alt="Response Strategies" width="800"/>  

*Supervisor ၏ အဆုံးဖြတ်တုံ့ပြန်မှု ဖန်တီးပုံ ၃ မျိုး — နောက်ဆုံး agent output, စုစည်းထားသော အနှစ်ချုပ်, သို့မဟုတ် အဆင့်အတန်း အကောင်းဆုံးကို ရွေးချယ်နိုင်သည်။*  

ရရှိနိုင်သော မဟာဗျူဟာများမှာ-  

| မဟာဗျူဟာ | ဖော်ပြချက် |
|----------|-------------|
| **LAST** | Supervisor သည် နောက်ဆုံး sub-agent သို့ ကိရိယာမှ ထုတ်လွှင့်သည့် output ကို ပြန်လည်ပေးသည်။ လုပ်ငန်းစဉ်အတွင်း နောက်ဆုံး agent သည် ပြည့်စုံသော အဆုံးဖြတ်ဖြေကြောင်းထုတ်ပေးရန် အထူးဒီဇိုင်းထားပါက အသုံးဝင်သည် (ဥပမာ၊ သုတေသန မျဉ်းစောင်းရှိ "Summary Agent")။ |
| **SUMMARY** | Supervisor သည် သီးသန့် Language Model (LLM) ကို အသုံးပြု၍ ဆက်သွယ်မှုတစ်လျှောက်လုံးနှင့် sub-agent output များအားလုံးကို စုစည်းကောက်နုတ်ပြီး၊ ၎င်းအနှစ်ချုပ်ကို အဆုံးဖြတ်ပြန်လည်ပေးသည်။ ၎င်းသည် အသုံးပြုသူအတွက် သန့်ရှင်းပြီး ရောထွေးမှု မရှိသော ဖြေကြောင်းတစ်ခု ပေးစွမ်းပေးပါသည်။ |
| **SCORED** | စနစ်သည် သီးသန့် LLM ကို အသုံးပြု၍ LAST ပြန်လည်ဖြေကြောင်းနှင့် ဆက်သွယ်မှုအနှစ်ချုပ်ကို မူရင်းအသုံးပြုသူ တောင်းဆိုချက်နှင့် နှိုင်းယှဉ်ကာ၊ အဆင့်မြင့်သော output ကို ပြန်လည်ပေးသည်။ |  

ဖော်ပြချက်ပြီးပြည့်စုံသော လုပ်ငန်းစဉ်အတွက် [SupervisorAgentDemo.java](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ကို ကြည့်ရှုနိုင်သည်။  

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ပါ။** [`SupervisorAgentDemo.java`](../../../05-mcp/src/main/java/com/example/langchain4j/mcp/SupervisorAgentDemo.java) ဖွင့်ပြီး မေးမြန်းပါ။  
> - "Supervisor သည် မည်သည့် agents များကို ခေါ်စဉ်းစားသနည်း?"  
> - "Supervisor နှင့် Sequential workflow ပုံစံများ ကြားက ကိုယ်တိုင်ကွာခြားချက် မည်သနည်း?"  
> - "Supervisor ၏ စီမံခန့်ခွဲမှု ခြေလှမ်းကို မည်သို့ စိတ်တိုင်းကျ ပြင်ဆင်နိုင်သနည်း?"  

#### ထွက်ရှိသည့်အဖြေကို နားလည်ခြင်း  

demo ကို အားလုံးလည်ပတ်သောအခါ Supervisor သည် အများ agent များကို မည်သို့ စီမံခန့်ခွဲသည်ကို ဖော်ပြထားသော အစီအစဉ်တခုကို မြင်ရမည်။ အောက်တွင် မူလနှင့် အဓိက ဖော်ပြချက်များဖြစ်သည်-  

```
======================================================================
  FILE → REPORT WORKFLOW DEMO
======================================================================

This demo shows a clear 2-step workflow: read a file, then generate a report.
The Supervisor orchestrates the agents automatically based on the request.
```
  
**ခေါင်းစဉ်ပုံစံ** သည် workflow အတွင်း စာဖတ်ခြင်းမှအစ သို့ ရုပ်သံထုတ်ဖော်ခြင်းအထိ အာရုံစူးစိုက်မှုရှိ သွားသော pipeline ကို မိတ်ဆက်သည်။  

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
  
**Workflow diagram** သည် agents များအကြား ဒေတာဦးတည်မှုကို ပြသသည်။ Agent တစ်ဦးချင်းစီသည် တာဝန် အထူးပြုထားသည်-  
- **FileAgent** သည် MCP ကိရိယာများကို အသုံးပြုပြီး ဖိုင်များဖတ်ကာ မူရင်းအကြောင်းအရာများကို `fileContent` တွင် သိမ်းဆည်းသည်  
- **ReportAgent** သည် အဆိုပါအကြောင်းအရာကို စားသုံးပြီး စီစဉ်ထားသော အစီရင်ခံစာကို `report` တွင် ထုတ်ပေးသည်  

```
--- USER REQUEST -----------------------------------------------------
  "Read the file at .../file.txt and generate a report on its contents"
```
  
**အသုံးပြုသူ တောင်းဆိုချက်** သည် လုပ်ငန်းတာဝန်ကို ပြသည်။ Supervisor သည် ၎င်းကို ပုံသေနည်းဖြင့် ခွဲခြားပြီး FileAgent → ReportAgent ကို ခေါ်ရန် ဆုံးဖြတ်သည်။  

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
  
**Supervisor အစီအစဉ် ဖော်ပြချက်** သည် ၂-အဆင့် စနစ်ကို ပြသည်-  
1. **FileAgent** သည် MCP ဖြင့် ဖိုင်ဖတ်ကာ ကျန်ရှိမှုများ သိမ်းဆည်းသည်  
2. **ReportAgent** သည် ထိုအကြောင်းအရာကို လက်ခံပြီး စီစဉ်ထားသော အစီရင်ခံစာထုတ်ပေးသည်  

Supervisor သည် အသုံးပြုသူတောင်းဆိုမှုအပေါ် အလိုအလျောက်ဤဆုံးဖြတ်ချက်များ ပြုလုပ်ခဲ့သည်။  

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
  
#### Agentic Module အင်္ဂါရပ်များ ရှင်းလင်းချက်  

ဥပမာတွင် agentic module ၏ တိုးတက်သော အင်္ဂါရပ် အချို့ကို ပြသထားသည်။ Agentic Scope နှင့် Agent Listeners ကို အသေးစိတ်ကြည့်ပါ-  

**Agentic Scope** သည် agents တစ်ဦးချင်း၏ ရလဒ်များကို `@Agent(outputKey="...")` ဖြင့် သိမ်းဆည်းထားသည့် မျှဝေသော မှတ်ဉာဏ်တစ်ခုကို ပြသသည်။ ၎င်းက-  
- နောက်ဆုံး agent များသည် ရှေ့ agents များ၏ output များအား ဝင်ရောက်အသုံးပြုနိုင်ခြင်း  
- Supervisor သည် အဆုံးသတ် ဖြေကြောင်းကို စုစည်းကောက်နုတ်နိုင်ခြင်း  
- သင့်ကို agent တစ်ဦးချင်းထုတ်ပေးသည့် အကြောင်းအရာကို သုံးသပ်နိုင်ခြင်း  

အောက်တွင် Agentic Scope သည် ဖိုင်မှအစီရင်ခံစာ workflow တွင် မျှဝေသော မှတ်ဉာဏ်အဖြစ် မည်သို့ လှုပ်ရှားသည်ကို ပြထားသည် — FileAgent သည် `fileContent` အောက်တွင် အချက်အလက် သိမ်းပြီး ReportAgent သည် အဲဒီကို ဖတ်ကာ `report` အောက်တွင် ယခုပင်ထည့်သွင်းသည်။  

<img src="../../../translated_images/my/agentic-scope.95ef488b6c1d02ef.webp" alt="Agentic Scope Shared Memory" width="800"/>  

*Agentic Scope သည် မျှဝေမှတ်ဉာဏ်အဖြစ် လုပ်ဆောင်သည် — FileAgent သည် `fileContent` ကို ရေးသားပြီး ReportAgent သည် ဖတ်ကာ `report` ကို ရေးသားပေးကာ သင်၏ ကုဒ်သည် နောက်ဆုံးရလဒ်ကို ဖတ်ရှုသည်။*  

```java
ResultWithAgenticScope<String> result = supervisor.invokeWithAgenticScope(request);
AgenticScope scope = result.agenticScope();
String fileContent = scope.readState("fileContent");  // FileAgent မှ မပါသော ဖိုင်ဒေတာများ
String report = scope.readState("report");            // ReportAgent မှ ဖွဲ့စည်းထားသောအစီရင်ခံစာ
```
  
**Agent Listeners** သည် agent ၏ လည်ပတ်မှုကို ကြည့်ရှုခြင်းနှင့် ဒိတ်ဘက် ပြုပြင် ပြောင်းလဲရာတွင် အသုံးပြုသည်။ demo တွင် မြင်ရသည့် အဆင့်-အဆင့် ထွက်သောအချက်အလက်များသည် AgentListener တစ်ခုမှ အရင်းအမြစ် ဖြစ်သည်-  
- **beforeAgentInvocation** - Supervisor သည် agent တစ်ဦးကို ရွေးချယ်သောအခါ ခေါ်ပြီ၊ ဘယ် agent ရွေးချယ်ခဲ့ပြီး မကြာမီ နောက်တစ်ကြိမ် ယင်းတရား အကြောင်းကို ကြည့်ရှုနိုင်သည်  
- **afterAgentInvocation** - agent တစ်ဦး ပြီးစီးချိန်တွင် ခေါ်ပြီ၊ ၎င်းရလဒ်ကို ပြသသည်  
- **inheritedBySubagents** - true ဖြစ်သည်ဆိုရင် hierarchy အတွင်းရှိ agent များအားလုံးကို ကလစ်ကာ ကြည့်ရှုသည်  

အောက်တွင် Agent Listener လည်ပတ်မှု ပိုင်းအစုံကို ပြထားပြီး `onError` က agent လည်ပတ်မှုအတွင်း မမှန်မှန် အမှားဖြစ်စဉ်ကို မည်သို့ ကိုင်တွယ်သည်ကို ဖော်ပြသည်-  

<img src="../../../translated_images/my/agent-listeners.784bfc403c80ea13.webp" alt="Agent Listeners Lifecycle" width="800"/>  

*Agent Listener များသည် လည်ပတ်မှု ဇီဝအခ်က္များကို ချိတ်ဆက်ထားသည် — agents စတင်ခြင်း၊ ပြီးဆုံးခြင်း၊ သို့မဟုတ် အမှားများ ကြုံတွေ့မှုကို ကြည့်ရှုသည်။*  

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
        return true; // အောက်ပါအေးဂျင့်အားလုံးသို့ တိုးပွားပေးပါ
    }
};
```
  
Supervisor pattern အပြင် `langchain4j-agentic` module သည် workflow ပုံစံ စုံလင်စွာ ထောက်ပံ့သည်။ အောက်တွင် pipeline အလွယ်တကူစီစဉ်မှုမှ လူအခန်းကဏ္ဍ ထည့်သွင်း ခံယူမှု approval workflow အထိ pattern ၅ မျိုးကို ပြထားသည်-  

<img src="../../../translated_images/my/workflow-patterns.82b2cc5b0c5edb22.webp" alt="Agent Workflow Patterns" width="800"/>  

*agents များ စီမံခန့်ခွဲရာတွင် workflow ပုံစံ ၅ မျိုး — အလွယ်တကူ တန်းလိုက်လည်ပတ်မှုမှ လူအခန်းကဏ္ဍ အသုံးပြုမှုပေါင်းသင်း approval workflows အထိ။*  

| ပုံစံ | ဖော်ပြချက် | အသုံးပြုမှု |
|---------|-------------|----------|
| **Sequential** | agents ကို အတန်းတကြောင်း လည်ပတ်စေပြီး output သည် နောက်ထပ် agent ထံသို့ စီးဆင်းသည် | ပိုင်းလိုင်းများ: သုတေသန → အနုသာရ → အစီရင်ခံစာ |
| **Parallel** | agents များကို တပြိုင်နက် လည်ပတ်စေသည် | အချိန်နှင့်တပြိုင်နက် လုပ်ရမည့်တာဝန်များ: မိုးလေဝသ + သတင်း + စတော့ရှယ်ယာများ |
| **Loop** | အခြေအနေတစ်ခု ပြည့်စုံသည်အထိ ထပ်ခါထပ်ခါ လည်ပတ်သည် | အရည်အသွေး ဆန်းစစ်ခြင်း: အဆင့် 0.8 အထိ ပြန်လည်တိုးတက်အောင် |
| **Conditional** | အခြေအနေပေါ်မူတည်၍ လမ်းညွှန်သည် | စိတ်ကြိုက်အဖွဲ့သို့ ဂဏန်းခွဲခြားခြင်း → special agent ထံ ပို့ဆောင်ခြင်း |
| **Human-in-the-Loop** | လူကို အတည်ပြုခွင့် ရရှိစေရန် checkpoint ထည့်သွင်းခြင်း | အသိအမှတ်ပြု workflows, အကြောင်းအရာ စစ်ဆေးခြင်း |  

## သဘောတရား အချက်များ  

MCP နှင့် agentic module ကို ပြီးဆုံးစူးစမ်းပြီးနောက်၊ သင့်တော်သော အသုံးပြုမှုကို အောက်ပါအတိုင်း အနှစ်ချုပ်ထားသည်။  

MCP ၏ အကြီးမားဆုံးအားသာချက်သည် ၎င်း၏ တိုးတက်ဆဲ ပတ်ဝန်းကျင်ဖြစ်သည်။ အောက်တွင် သင့် AI အပလီကေးရှင်းကို MCP universal protocol တစ်ခုဖြင့် အမျိုးမျိုးသော MCP ဆာဗာ (filesystem, database, GitHub, email, web scraping နှင့် အခြားများ) နှင့် ချိတ်ဆက်နိုင်သည့်ပုံ ဖော်ပြထားသည်။  

<img src="../../../translated_images/my/mcp-ecosystem.2783c9cc5cfa07d2.webp" alt="MCP Ecosystem" width="800"/>  

*MCP သည် စံချိန်တက် protocol ပတ်ဝန်းကျင် တစ်ခု ဖန်တီးသည် — MCP ကိုက်ညီသည့် ဘာရပ် မဆို MCP ကိုက်ညီသည့် client နှင့် ဆက်သွယ်နိုင်ပြီး၊ ကိရိယာများကို အပလီကေးရှင်းများအကြား ဝေမျှနိုင်သည်။*  

**MCP** သည် သင်၏ AI application မှာ လက်ရှိ ရှိပြီးသား ကိရိယာ ပတ်ဝန်းကျင်ကို အကျိုးရှိစွာ အသုံးချလိုသည့်အခါ၊ tools များကို အမျိုးမျိုးသော အပလီကေးရှင်းများနှင့် မျှဝေချင်သောအခါ၊ သတ်မှတ် protocol များဖြင့် တတိယပါတီ ဝန်ဆောင်မှုများပေါင်းစပ်လိုသည့်အခါ၊ သို့မဟုတ် code မပြောင်းလဲဘဲ tools implementations များ အစားထိုးလိုသောအခါ အထူးသင့်တော်သည်။  

**Agentic Module** သည် `@Agent` annotation တွေနဲ့ ကြေညာသည့် agent များလိုသည့်အခါ၊ workflow စီမံခန့်ခွဲမှု (တန်းလိုက်၊ loop, parallel) လိုအပ်သောအခါ၊ imperative ကုဒ်မဟုတ်ဘဲ interface အခြေပြု agent design ကို ဦးစားပေးသုံးလိုသောအခါ၊ ထပ်တူ output များကို `outputKey` ဖြင့် မျှဝေသော agents များ ပေါင်းစပ်လိုသောအခါ အကောင်းဆုံး ဖြစ်သည်။  

**Supervisor Agent pattern** သည် workflow ကို နောက်ရှေးမကြိုတင်ခန့်မှန်းနိုင်သောအခါ၊ dynamic orchestration လိုသော အသီးသီးအထူးပြု agents များ ရှိသောအခါ၊ capability များစွာသို့ စကားပြောစနစ်တွင် လမ်းညွှန်လိုသောအခါ၊ ထိရောက်ဆုံး၊ ကိုက်ညီဆန်းသစ်သော agent လုပ်ဆောင်ချက်လိုသည့်အခါ ရှည်လျားစွာ အသုံးပြုရန် သင့်တော်သည်။  

ပုဂ္ဂိုလ်ရေး `@Tool` နည်းလမ်းများ (Module 04) နှင့် MCP ကိရိယာများ (Module 05) ကြား ရွေးချယ်မှုအတွက် အောက်ပါ နှိုင်းယှဉ်မှုမှာ အဓိက ကွာခြားချက်များကို ပြထားသည် — ပြုလုပ်သူ tool များသည် tightly coupled ဖြစ်ပြီး app ရေးသားချက်အပြည့်အဝ အာမခံမှု ရရှိပေးသော်လည်း MCP ကိရိယာများသည် စံမီခွင့်ရ integration များကို ပြန်လည်အသုံးပြုနိုင်စေရန် ရည်ရွယ်သည်။  

<img src="../../../translated_images/my/custom-vs-mcp-tools.c4f9b6b1cb65d8a1.webp" alt="Custom Tools vs MCP Tools" width="800"/>  

*မည်တော့ custom @Tool နည်းလမ်းများကို အသုံးပြုရမည်နည်း၊ MCP ကိရိယာများကို အသုံးပြုရမည်နည်း — app-specific logic အတွက် tight coupling နှင့် full type safety ရရှိစေရန် custom tools, application အတွင်း စံချိန်မီ integration များအတွက် MCP tools*  

## ဂုဏ်ပြုပါတယ်!  

LangChain4j for Beginners အတန်း၏ module ၅ ခုလုံးအဆုံးသတ်ပြီးပြီ! အောက်တွင် သင်ဆုံးဖြတ်ပြီးသော လေ့လာမှု ခရီးစဉ် မြင်ကွင်းကို ကြည့်ရှုနိုင်သည် — မိမိ စကားပြောထိန်းသိမ်းမှုမှ စ၍ MCP ဖြင့် အားဖြည့် agentic စနစ်များထိ-  

<img src="../../../translated_images/my/course-completion.48cd201f60ac7570.webp" alt="Course Completion" width="800"/>  

*သင့် လေ့လာမှုခရီးစဉ် — အခြေခံစကားပြောမှ MCP ဖြင့် အားဖြည့် agentic စနစ်များအထိ module ၅ ခု*  

သင်သည် LangChain4j for Beginners ကို အောင်မြင်စွာ ပြီးစီးခဲ့သည်။ သင် သင်ယူခဲ့သည်-  

- မှတ်ဉာဏ်ပါ conversational AI တည်ဆောက်နည်း (Module 01)  
- အလုပ်တာဝန်အမျိုးမျိုးအတွက် prompt engineering ပုံစံများ (Module 02)  
- သင့်စာတမ်းများအာရုံစူးစိုက်မှုဖြင့် ground ပြုလုပ်ခြင်း (RAG) (Module 03)  
- စိတ်တိုင်းကျ tools များဖြင့် အခြေခံ AI agent များ တည်ဆောက်ခြင်း (Module 04)  
- LangChain4j MCP နှင့် Agentic module များဖြင့် စံချိန်မီ tools ပေါင်းစပ်ခြင်း (Module 05)  

### ဒါမှမဟုတ် ဘာနောက်တစ်ခါ?  

modules ပြီးစီးပြီးနောက် [Testing Guide](../docs/TESTING.md) တွင် LangChain4j စမ်းသပ်မှု အကြောင်းအရာများကို စမ်းသပ်ကြည့်ပါ။  

**တရားဝင် အရင်းအမြစ်များ-**  
- [LangChain4j Documentation](https://docs.langchain4j.dev/) - သေချာပြီး ပေးအပ်သော ညွှန်ကြားချက်များနှင့် API ကိုးကားချက်  
- [LangChain4j GitHub](https://github.com/langchain4j/langchain4j) - ကိုးကားကုဒ်နှင့် ဥပမာများ  
- [LangChain4j Tutorials](https://docs.langchain4j.dev/tutorials/) - အသုံးပြုမှုအမျိုးမျိုးအတွက် အဆင့်ဆင့် သင်ခန်းစာများ  

ဤသင်တန်းကို အောင်မြင်စွာ ပြီးစီးရန် ကျေးဇူးတင်ပါသည်!  

---  

**လမ်းညွှန်:** [← အရင်တစ်ခု: Module 04 - Tools](../04-tools/README.md) | [ပြန်သွား Main](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免责声明**:
本文件已使用 AI 翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 进行翻译。尽管我们致力于确保准确性，但请注意自动翻译可能包含错误或不准确之处。原始文件的原文应被视为权威来源。对于重要信息，建议采用专业人工翻译。我们对因使用本翻译而引起的任何误解或误释概不负责。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->