# LangChain4j အကြောင်းအရာစာအုပ်

## အကြောင်းအရာစာရင်း

- [အဓိက သဘောတရားများ](../../../docs)
- [LangChain4j အစိတ်အပေါ်များ](../../../docs)
- [AI/ML သဘောတရားများ](../../../docs)
- [ကာကွယ်ရေးစနစ်များ](../../../docs)
- [Prompt အင်ဂျင်နီယာရေး](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [အေးဂျင့်များနှင့် ကိရိယာများ](../../../docs)
- [Agentic Module](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure ဝန်ဆောင်မှုများ](../../../docs)
- [စစ်ဆေးခြင်းနှင့် ဖွံ့ဖြိုးတိုးတက်မှု](../../../docs)

သင်တန်းတစ်လျှောက် အသုံးပြုသည့် စကားလုံးများနှင့် သဘောတရားများအတွက် အမြန်လေ့လာနိုင်သော ရည်ညွှန်းစာမျက်နှာ။

## အဓိက သဘောတရားများ

**AI Agent** - AI ကို အသုံးပြုပြီး ကိုယ်ပိုင် သဘောဖြင့် ဆောင်ရွက်သည့် စနစ်။ [Module 04](../04-tools/README.md)

**Chain** - အလုပ်လုပ်ပုံအစဉ်ဖြစ်ပြီး အချက်အလက်ထွက်သည် နောက်တစ်ဆင့်အဖြစ် သယ်ဆောင်သည်။

**Chunking** - စာရွက်စာတမ်းများကို အပိုင်းအသေးများအဖြစ် ခွဲခြမ်းခြင်း။ ပုံမှန်အားဖြင့် 300-500 token များနီးပါးဖြစ်ပြီး သုံးသပ်ချက်တစ်ခုစီသည် အချိုးအတော်ရှိသည်။ [Module 03](../03-rag/README.md)

**Context Window** - မော်ဒယ်တစ်ခုအနေဖြင့် အများဆုံး ဆောင်ရွက်နိုင်သည့် tokens အရေအတွက်။ GPT-5.2: 400K tokens။

**Embeddings** - စာသား၏ အဓိပ္ပာယ် ကို ကိုယ်စားပြုသည့် ကိန်းများ။ [Module 03](../03-rag/README.md)

**Function Calling** - မော်ဒယ်က အပြင် functions များကို ဖိတ်ခေါ်ရန် ဖွဲ့စည်းထားသော တောင်းဆိုချက်များ ဖန်တီးသည်။ [Module 04](../04-tools/README.md)

**Hallucination** - မော်ဒယ်များမှ မွားယွင်းသော်လည်း ယုံကြည်စိတ်ချရသော အချက်အလက်များ ထုတ်ပေးခြင်း။

**Prompt** - ဘာသာစကားမော်ဒယ်ထံသို့ ပေးပို့သော စာသားထည့်သွင်းမှု။ [Module 02](../02-prompt-engineering/README.md)

**Semantic Search** - နှုတ်ဆက်ချက်များ မဟုတ်ဘဲ အဓိပ္ပာယ်အရ ရှာဖွေခြင်း။ [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: မှတ်ဉာဏ်မရှိသော။ Stateful: စကားဝိုင်းမှတ်တမ်းကို ထိန်းသိမ်းသည်။ [Module 01](../01-introduction/README.md)

**Tokens** - မော်ဒယ်များ ဆောင်ရွက်ရာတွင် အခြေခံ စာသားယူနစ်များ။ စရိတ်နှင့် ကန့်သတ်ချက်များကို ထိခိုက်စေသည်။ [Module 01](../01-introduction/README.md)

**Tool Chaining** - ကိရိယာများကို တစ်ဆင့်နောက်တစ်ဆင့်ပြုလုပ်ခြင်း၊ ထွက်ရှိချက်ဖြင့် နောက် ကိရိယာကို ခေါ်ယူစေခြင်း။ [Module 04](../04-tools/README.md)

## LangChain4j အစိတ်အပေါ်များ

**AiServices** - အမျိုးအစား-လုံခြုံသော AI ဝန်ဆောင်မှု မျက်နှာပြင်များ ဖန်တီးသည်။

**OpenAiOfficialChatModel** - OpenAI နှင့် Azure OpenAI မော်ဒယ်များအတွက် ပူးပေါင်းသော ကိုင်းလိန့် Client။

**OpenAiOfficialEmbeddingModel** - OpenAI Official client ကို အသုံးပြုပြီး embeddings ဖန်တီးသည် (OpenAI နှင့် Azure OpenAI နှစ်ခုစလုံးကို ထောက်ပံ့သည်)။

**ChatModel** - ဘာသာစကားမော်ဒယ်များအတွက် အဓိက မျက်နှာပြင်။

**ChatMemory** - စကားပြောဆိုမှုမှတ်တမ်း ထိန်းသိမ်းသည်။

**ContentRetriever** - RAG အတွက် သင့်လျော်သော စာရွက်စာတမ်း ပိုင်းများ ရှာဖွေသည်။

**DocumentSplitter** - စာရွက်စာတမ်းများကို ခွဲခြမ်းသည်။

**EmbeddingModel** - စာသားကို ကိန်းသတ် အကွက်များသို့ ပြောင်းလဲသည်။

**EmbeddingStore** - Embeddings များကို သိမ်းဆည်းနှင့် ရယူသည်။

**MessageWindowChatMemory** - လတ်တလော အကြောင်းအရာများကို စက္ကန့်ပြုစုထားသော sliding window ကို ထိန်းသိမ်းသည်။

**PromptTemplate** - `{{variable}}` placeholders ဖြင့် အသုံးပြုနိုင်သော prompts များ ဖန်တီးသည်။

**TextSegment** - မီတာဒေတာပါရှိသည့် စာသားပိုင်း။ RAG တွင် အသုံးပြုသည်။

**ToolExecutionRequest** - ကိရိယာ ဆောင်ရွက်မှု တောင်းဆိုချက် ကို ကိုယ်စားပြုသည်။

**UserMessage / AiMessage / SystemMessage** - စကားသုံးရာ မက်ဆေ့ချ် အမျိုးအစားများ။

## AI/ML သဘောတရားများ

**Few-Shot Learning** - Prompts ထဲ၌ နမူနာများပေးခြင်း။ [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - ကြီးမားသော စာအချက်အလက်များဖြင့် သင်ကြားထားသည့် AI မော်ဒယ်များ။

**Reasoning Effort** - စဉ်းစားမှု အနက်အတွက် GPT-5.2 တွင် ထိန်းချုပ်သည့် ပါရာမီတာ။ [Module 02](../02-prompt-engineering/README.md)

**Temperature** - ထွက်ရှိမှု ရဲ့ ထူးခြားမှုကို ထိန်းချုပ်သည်။ အနိမ့်= ရိုးရှင်း၊ အမြင့်= ဖန်တီးမှု။

**Vector Database** - Embeddings များအတွက် အထူးပြု ဒေတာဘေ့(စ်)။ [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - နမူနာမပါဘဲ တာဝန်များ ပြုလုပ်ခြင်း။ [Module 02](../02-prompt-engineering/README.md)

## ကာကွယ်ရေးစနစ်များ - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - application-level guardrails နှင့် provider safety filters ကို ပေါင်းစပ်ထားသည့် လုံခြုံရေး အတန်းဆက်။

**Hard Block** - အသံအကြိုးအသုံးပြုမှုဆိုးရွားမှုအတွက် provider က HTTP 400 အမှားပစ်ခြင်း။

**InputGuardrail** - LangChain4j interface က user input မော်ဒယ်သို့ သွားမီ စစ်ဆေးပေးသည်။ မဖြစ်မနေရန် cost နှင့် latency ကာကွယ်မှုပေးသည်။

**InputGuardrailResult** - guardrail စစ်ဆေးမှု ပြန်လည်ထုတ်ပေးမှု: `success()` သို့မဟုတ် `fatal("reason")`။

**OutputGuardrail** - AI ဖြေရှင်းချက်များကို user ထံ ပို့မီ စစ်ဆေးနိုင်သော interface။

**Provider Safety Filters** - API အဆင့်တွင် ဖမ်းဆီးနိုင်သော AI ပံ့ပိုးသူများ၏ built-in လုံခြုံရေး စစ်ထုတ်နည်းများ (ဥပမာ GitHub Models)။

**Soft Refusal** - မေးခွန်းကို error မပစ်ပေးပဲ နည်းနည်း ပြောကန်သော ဆန့်ကျင်မှု။

## Prompt အင်ဂျင်နီယာရေး - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - တစ်ဆင့်ချင်း စိတ်ခွန်အားဖြင့် reasoning ဆောင်ရွက်ခြင်း။

**Constrained Output** - သတ်မှတ်ထားသော ပုံစံ သို့မဟုတ် ဖွဲ့စည်းမှုထားရှိခြင်း။

**High Eagerness** - GPT-5.2 ၏ ပြည့်စုံသော စဉ်းစားမှု pattern။

**Low Eagerness** - GPT-5.2 ၏ မြန်မြန်ဆန်ဆန် တုံ့ပြန်မှု pattern။

**Multi-Turn Conversation** - အပြန်အလှန် စကားပြောဆိုမှုများတွင် context ထိန်းသိမ်းခြင်း။

**Role-Based Prompting** - မော်ဒယ်ကို system message များဖြင့် ကိုယ်စားပြုပုဂ္ဂိုလ် သတ်မှတ်ခြင်း။

**Self-Reflection** - မော်ဒယ်သည် မိမိထုတ်ပေးမှုကို အကဲဖြတ်ပြီး တိုးတက်အောင် ပြုလုပ်ခြင်း။

**Structured Analysis** - သတ်မှတ်ထားသော အကဲဖြတ်စနစ်။

**Task Execution Pattern** - အစီအစဉ် → ဆောင်ရွက် → အနှစ်ချုပ်။

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - โหลด → ချန့် → embed → သိမ်းဆည်း။

**In-Memory Embedding Store** - စမ်းသပ်ရေးအတွက် အမြမ်းသိုလှောင်မှုမရှိသော သိမ်းဆည်းခြင်း။

**RAG** - ဆွဲယူခြင်းနှင့် ဖန်တီးခြင်း ပေါင်းစပ်ပြီး မြေကြီးထဲသို့ ချိတ်ဆက်သော ဖြေကြားချက်။

**Similarity Score** - semantic ဆင်တူမှု အဆင့် (0-1)။

**Source Reference** - ဆွဲယူထားသော အကြောင်းအရာ metadata။

## အေးဂျင့်များနှင့် ကိရိယာများ - [Module 04](../04-tools/README.md)

**@Tool Annotation** - AI မှ ခေါ်ယူနိုင်သော Java method များ အဆိုအမူဖြင့် ခန့်မှန်းခြင်း။

**ReAct Pattern** - Reason → Act → Observe → Repeat ပုံစံ။

**Session Management** - အသုံးပြုသူ သီးခြား context များ ဖွဲ့စည်းခြင်း။

**Tool** - AI agent က ခေါ်ယူနိုင်သည့် function။

**Tool Description** - ကိရိယာရည်ရွယ်ချက်နှင့် ပါရာမီတာများကို ဖော်ပြသော စာတမ်း။

## Agentic Module - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - AI agent များအနေဖြင့် interface များကို အမူအမူ အကြောင်းအရာ သတ်မှတ်မှုဖြင့် မှတ်သားခြင်း။

**Agent Listener** - `beforeAgentInvocation()` နှင့် `afterAgentInvocation()` တို့ကို အသုံးပြုပြီး agent ဆော့စယွက်မှု ကို စောင့်ကြည့်နိုင်သည့် hook။

**Agentic Scope** - agents များ output များကို သိမ်းဆည်းရာ shared memory ဖြစ်ပြီး outputKey ဖြင့် downstream agents များ အသုံးပြုရန်။

**AgenticServices** - `agentBuilder()` နှင့် `supervisorBuilder()` ဖြင့် agents များ ဖန်တီးနိုင်သော factory။

**Conditional Workflow** - အခြေအနေများအရ အထူးဆရာ အေးဂျင့်များထံ လမ်းညွှန်ခြင်း။

**Human-in-the-Loop** - လူ့ထောက်ခံချက်များ ထည့်သွင်း၍ အတည်ပြုခြင်း သို့မဟုတ် အကြောင်းအရာ ဆွေးနွေးသုံးသပ်ခြင်း workflow pattern။

**langchain4j-agentic** - declarative agent ဖန်တီးမှုအတွက် Maven dependency (စမ်းသပ်ဆန်းစစ် သော)။

**Loop Workflow** - တစ်စိတ်တစ်ပိုင်း condition ဖြင့် (ဥပမာ အရည်အသွေးအမှတ် ≥ 0.8) agent ဆောင်ရွက်မှု ဆက်တိုက် စိစစ်ခြင်း။

**outputKey** - Agent annotation parameter အဖြစ် agentic scope တွင် ရလဒ်များ သိမ်းဆည်းရာလမ်းညွှန်ချက်။

**Parallel Workflow** - လွတ်လပ်သော တာဝန်များအတွက် အေးဂျင့်များကို တပြိုင်နက် ပြုလုပ်ခြင်း။

**Response Strategy** - ဦးစီးသူက မေးခွန်း ဖြေဆိုပုံ: LAST, SUMMARY, SCORED စသည်ဖြင့် သတ်မှတ်ခြင်း။

**Sequential Workflow** - အဆင့်လိုက် agent များကို ဆက်တိုက် လုပ်ဆောင်ခြင်း၊ ထွက်ရှိချက်သည် ရှေ့တစ်ဆင့်သို့ သယ်ဆောင်ခြင်း။

**Supervisor Agent Pattern** - ခေတ်မီ agentic ပုံစံ ဖြစ်ပြီး supervisor LLM သည် ကိုယ်စားပြု အေးဂျင့်များကို ထပ်မံ ခေါ်ယူရန် ရွေးချယ်သည်။

## Model Context Protocol (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j ရဲ့ MCP ပေါင်းစည်းမှုအတွက် Maven dependency။

**MCP** - Model Context Protocol: AI apps များကို အပြင်ကိရိယာများနှင့် ချိတ်ဆက်ရန် စံတော်ချိန်။ တစ်ခါသာ တည်ဆောက်၍ နေရာတိုင်းတွင် အသုံးပြုနိုင်သည်။

**MCP Client** - MCP ဆာဗာများနှင့် ချိတ်ဆက်ကိရိယာ များကို ရှာဖွေဆောင်ရွက်နိုင်သော လျှောက်လွှာ။

**MCP Server** - MCP ဖြင့် ကိရိယာများကို ဖော်ပြထားသည့် ဝန်ဆောင်မှု။

**McpToolProvider** - LangChain4j ၏ MCP ကိရိယာများကို AI ဝန်ဆောင်မှုများနှင့် အေးဂျင့်များတွင် အသုံးပြုနိုင်အောင် ထုပ်ပိုးထားသော အစိတ်အပိုင်း။

**McpTransport** - MCP ဆက်သွယ်မှုအတွက် interface။ Stdio နှင့် HTTP စသည့် အကောင်အထည်ဖော်မှုများ ပါဝင်သည်။

**Stdio Transport** - stdin/stdout ဖြင့် ဒေသတွင်းလုပ်ငန်းစဉ် ဆက်သွယ်မှု။ ဖိုင်စနစ် ဝင်ရောက်ခြင်း သို့မဟုတ် command-line ကိရိယာများအတွက် အသုံးဝင်သည်။

**StdioMcpTransport** - LangChain4j ၏ MCP ဆာဗာကို subprocess အဖြစ် စတင်ပေးသည့် အကောင်အထည်။

**Tool Discovery** - client အနေဖြင့် ရနိုင်သော ကိရိယာများ အကြောင်းအရာနှင့် စားဇာမစ်များကို ဆာဗာထံ မေးမြန်းခြင်း။

## Azure ဝန်ဆောင်မှုများ - [Module 01](../01-introduction/README.md)

**Azure AI Search** - Vector ထောက်ပံ့မှုပါရှိသည့် cloud ရှာဖွေရေး။ [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Azure အရင်းအမြစ် ထည့်သွင်းခြင်း။

**Azure OpenAI** - Microsoft ၏ စီးပွားရေး AI ဝန်ဆောင်မှု။

**Bicep** - Azure infrastructure-as-code ဘာသာစကား။ [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Name** - Azure တွင် မော်ဒယ် deployment အမည်။

**GPT-5.2** - reasoning ထိန်းချုပ်မှုရှိသည့် နောက်ဆုံး မော်ဒယ်။ [Module 02](../02-prompt-engineering/README.md)

## စစ်ဆေးခြင်းနှင့် ဖွံ့ဖြိုးတိုးတက်မှု - [Testing Guide](TESTING.md)

**Dev Container** - container ဖြင့် ဖွံ့ဖြိုးရေး ပတ်ဝန်းကျင်။ [Configuration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - အခမဲ့ AI မော်ဒယ် စမ်းသပ်ရေး။ [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - memory ထဲတွင် စမ်းသပ်ခြင်း။

**Integration Testing** - လက်တွေ့ infrastructure ဖြင့် စမ်းသပ်ခြင်း။

**Maven** - Java အလိုအလျောက် တည်ဆောက်ခြင်းကိရိယာ။

**Mockito** - Java mocking framework။

**Spring Boot** - Java application framework။ [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**အာမခံချက်**  
ဤစာတမ်းကို AI ဘာသာပြန်ခြင်းဝန်ဆောင်မှုဖြစ်သော [Co-op Translator](https://github.com/Azure/co-op-translator) ကိုအသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် မှန်ကန်မှုအတွက် ကြိုးပမ်းမည်ဖြစ်သော်လည်း အလိုအလျောက်ဘာသာပြန်ခြင်းတွင် အမှားများ သို့မဟုတ် မှန်ကန်မှုမရှိမှုများ ရှိနိုင်ကြောင်း အသိပေးလိုပါသည်။ မူလစာတမ်းကို မိခင်ဘာသာဖြင့်သာ ယုံကြည်စိတ်ချရသော ထောက်ခံချက်အရင်းအမြစ်အဖြစ် သတ်မှတ်သင့်ပါသည်။ အရေးကြီးသည့် အချက်အလက်များအတွက် တတ်ကျွမ်းသော လူ့ဘာသာပြန်ခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်ချက်ကို အသုံးပြုမှုကြောင့် ဖြစ်ပေါ်လာနိုင်သည့် နားလည်မှုလွဲခြင်းများ သို့မဟုတ် မှားယွင်းသဘောထားများအတွက် ကျွန်ုပ်တို့မှ တာဝန်ယူမထားပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->