# LangChain4j အဓိပ္ပာယ်ရှင်းလင်းချက်

## အကြောင်းအရာဇယား

- [အခြေခံ အတွေးအခေါ်များ](../../../docs)
- [LangChain4j အစိတ်အပိုင်းများ](../../../docs)
- [AI/ML အတွေးအခေါ်များ](../../../docs)
- [အသုံးတားရေး](../../../docs)
- [Prompt Engineering](../../../docs)
- [RAG (Retrieval-Augmented Generation)](../../../docs)
- [Agents နှင့် Tools](../../../docs)
- [Agentic Module](../../../docs)
- [Model Context Protocol (MCP)](../../../docs)
- [Azure ဝန်ဆောင်မှုများ](../../../docs)
- [စမ်းသပ်ခြင်းနှင့် ဖွံ့ဖြိုးတိုးတက်မှု](../../../docs)

သင်ကြားမှုအားလုံးတွင် အသုံးပြုသော စကားလုံးများနှင့် အတွေးအခေါ်များအတွက် အလျင်အမြန် ရှာဖွေရန်။

## အခြေခံ အတွေးအခေါ်များ

**AI Agent** - AI ကို အသုံးပြုကာ ကိုယ်တိုင် အတွေးထုတ်လုပ်ပြီး ရပ်တည်ဆောင်ရွက်သော စနစ်။ [Module 04](../04-tools/README.md)

**Chain** - လုပ်ဆောင်ချက်စီးရီးတစ်ခုမဟုတ်ပါက နောက်တစ်ဆင့်အဖြစ် ထွက်ရှိမှု ရသည်။

**Chunking** - စာရွက်များကို အပိုင်းပိုင်း သို့ ခွဲခြားခြင်း။ မကြာခဏ: ၃၀၀-၅၀၀ တokens နှင့် အနှောင့်အယှက်။ [Module 03](../03-rag/README.md)

**Context Window** - မော်ဒယ်အတွက် အမြင့်ဆုံး တokens အရေအတွက်။ GPT-5.2: 400K တokens (ဝင်ပေါက် ၂၇၂K, ထွက်ပေါက် ၁၂၈K)။

**Embeddings** - စာသား၏ အဓိပ္ပာယ်ကို ကိုယ်စားပြုပြတဲ့ နံပါတ် vectors။ [Module 03](../03-rag/README.md)

**Function Calling** - မော်ဒယ်က ပြင်ပ function များကို ခေါ်ရန် ဖွဲ့စည်းထားသော အကောင်အထည်တစ်ခု ဖန်တီးခြင်း။ [Module 04](../04-tools/README.md)

**Hallucination** - မော်ဒယ်များမှ မှားယွင်းသော်လည်း လူမှတ်မီနိုင်သော အချက်အလက်များ ထုတ်ပေးခြင်း။

**Prompt** - ဘာသာစကားမော်ဒယ်ထံ ရိုက်ထည့်သော စာသား။ [Module 02](../02-prompt-engineering/README.md)

**Semantic Search** - keywords မဟုတ်ပဲ embedding ကို အသုံးပြု၍ အဓိပ္ပာယ် အလိုက် ရှာဖွေခြင်း။ [Module 03](../03-rag/README.md)

**Stateful vs Stateless** - Stateless: မှတ်ဉာဏ်မရှိ။ Stateful: ပြောဆိုမှု သမိုင်းကို ထိန်းသိမ်းထားသည်။ [Module 01](../01-introduction/README.md)

**Tokens** - မော်ဒယ်များ အလုပ်လုပ်ရာတွင် အခြေခံ စာသားအပိုင်းများ။ ကုန်ကျစရိတ်နှင့် ကန့်သတ်ချက်များကို ထိခိုက်စေသည်။ [Module 01](../01-introduction/README.md)

**Tool Chaining** - အသုံးပြုရန် သတ်မှတ်ထားသော tools များကို တစ်ခုချင်းဆက်တိုက် အသုံးပြုခြင်း။ [Module 04](../04-tools/README.md)

## LangChain4j အစိတ်အပိုင်းများ

**AiServices** - အမျိုးအစားလုံခြုံသော AI ဝန်ဆောင်မှု အင်တာဖေ့စ်များ ဖန်တီးသော အစိတ်အပိုင်း။

**OpenAiOfficialChatModel** - OpenAI နှင့် Azure OpenAI မော်ဒယ်များ အတွက် စုပေါင်း ဖောက်သည်။

**OpenAiOfficialEmbeddingModel** - OpenAI Official client ကို အသုံးပြုကာ embedding များ ဖန်တီးခြင်း (OpenAI နှင့် Azure OpenAI နှစ်ခုလုံး ပံ့ပိုးသည့်)။

**ChatModel** - ဘာသာစကားမော်ဒယ်များအတွက် အဓိက အင်တာဖေ့စ်။

**ChatMemory** - ပြောဆိုမှု သမိုင်းကို ထိန်းသိမ်းထားသည်။

**ContentRetriever** - RAG အတွက် သက်ဆိုင်ရာ စာရွက် အပိုင်းများ ရှာဖွေသွားသော ကိရိယာ။

**DocumentSplitter** - စာရွက်များကို အပိုင်းသိုက်သိုက် ခွဲခြားမှု။

**EmbeddingModel** - စာသားကို နံပါတ် vectors သို့ ပြောင်းလဲခြင်း။

**EmbeddingStore** - Embeddings များကို သိမ်းဆည်းနှင့် ရယူခြင်း။

**MessageWindowChatMemory** - နောက်ဆုံးစာသေးများ မှ တစ်ဆင့် Sliding Window ကို ထိန်းသိမ်းခြင်း။

**PromptTemplate** - `{{variable}}` နေရာမှတ်တမ်းများဖြင့် ထပ်မံအသုံးပြုနိုင်သော prompt များ ဖန်တီးခြင်း။

**TextSegment** - RAG တွင် အသုံးပြုသော စာသား အပိုင်းနှင့် မီတာဒေတာ။

**ToolExecutionRequest** - Tool အကောင်အထည်ဖော်ရန် တောင်းဆိုမှု ကို ကိုယ်စားပြုမှု။

**UserMessage / AiMessage / SystemMessage** - ပြောဆိုစကားသွားအမျိုးအစားများ။

## AI/ML အတွေးအခေါ်များ

**Few-Shot Learning** - Prompt များတွင် နမူနာများ ပေးသွင်းခြင်း။ [Module 02](../02-prompt-engineering/README.md)

**Large Language Model (LLM)** - မြောက်မားသော စာသားဒေတာဖြင့် လေ့ကျင့်ထားသော AI မော်ဒယ်များ။

**Reasoning Effort** - GPT-5.2 ၏ စဉ်းစားပထမအဆင့် ထိန်းချုပ်မှု ပြားမီတာ။ [Module 02](../02-prompt-engineering/README.md)

**Temperature** - ထွက်ရှိမှု အလွတ်တမ်းအတုအယောင် ထိန်းညှိမှု။ အနိမ့်= သတ်မှတ်ထားသော၊ အမြင့်= ဖန်တီးမှုအမြင့်။

**Vector Database** - Embeddings များအတွက် အထူးပြု ဒေတာအချက်အလက် တိုက်ဆိုင်ရာသိုလှောင်မှု။ [Module 03](../03-rag/README.md)

**Zero-Shot Learning** - နမူနာ အသုံးပြုခြင်းမဟုတ်ဘဲ အလုပ်များ ဆောင်ရွက်ခြင်း။ [Module 02](../02-prompt-engineering/README.md)

## အသုံးတားရေး - [Module 00](../00-quick-start/README.md)

**Defense in Depth** - ဝန်ဆောင်မှုနှင့် ပေးသူ လုံခြုံရေးထိန်းသိမ်းမှုများ ပေါင်းစပ်ထားသော စွဲမြဲသော ခြုံငုံကာကွယ်ရေး။

**Hard Block** - အကြမ်းဖက်သော အကြောင်းအရာ များဖြစ်လာလျှင် ပေးသူမှ HTTP 400 error ပေးပိတ်တားဆီးခြင်း။

**InputGuardrail** - LangChain4j LLM ထံ ရောက်ရှိမီ အသုံးပြုသူ input ကို စစ်ဆေးသွားသော အင်တာဖေ့စ်။ မကောင်းသော prompt များကို စောင့်ပိတ်ကြောင်း ကုန်ကျစရိတ်နှင့် နောက်နှောင့်နှေးမှု ကို လျော့ချတယ်။

**InputGuardrailResult** - guardrail စစ်ဆေးမှု ရလဒ်: `success()` သို့မဟုတ် `fatal("reason")`။

**OutputGuardrail** - အသုံးပြုသူထံ ပြန်ပေးရန် မတိုင်မီ AI တုံ့ပြန်ချက်များကို စစ်ဆေးသော အင်တာဖေ့စ်။

**Provider Safety Filters** - AI ပေးသူများထံမှ အကြောင်းအရာ စစ်ထုတ်မှုများ API အဆင့်တွင် ဖမ်းဆီးထားသည်။

**Soft Refusal** - မော်ဒယ် အမှားမထုတ်ဘဲ ရှင်းပြ၍ ဖြေကြားရန် ဖျက်ပယ်ခြင်း။

## Prompt Engineering - [Module 02](../02-prompt-engineering/README.md)

**Chain-of-Thought** - မြန်ဆန်စွာ မှန်ကန်မှုရရှိစေရန် တစ်ဆင့်ချင်း စဉ်းစားခြင်း။

**Constrained Output** - သတ်မှတ်ထားသော ပုံစံ သို့မဟုတ် ဖွဲ့စည်းမှု အပ်နာခြင်း။

**High Eagerness** - GPT-5.2 မှ ခွဲခြမ်းစိတ်ဖြာမှု ပြည့်စုံသော ပုံစံ။

**Low Eagerness** - GPT-5.2 မှ လျင်မြန်သော ဖြေကြားချက်ပုံစံ။

**Multi-Turn Conversation** - လွှဲပြောင်းဆက်ဆံမှုများအတွင်း စပ်ဆိုင်မှုပေးခြင်း။

**Role-Based Prompting** - စနစ် သတင်းအချက်အလက်ဖြင့် မော်ဒယ်ဘာသာရပ်ကို သတ်မှတ်ခြင်း။

**Self-Reflection** - မော်ဒယ်က ကိုယ့်ရလဒ်ကို သုံးသပ် လှှိမ့်ပြောင်းခြင်း။

**Structured Analysis** - သတ်မှတ်ထားသော သုံးသပ်မှု ပြုလုပ်ပုံ။

**Task Execution Pattern** - အစီအစဉ် → အကောင်အထည် → အနှစ်ချုပ်။

## RAG (Retrieval-Augmented Generation) - [Module 03](../03-rag/README.md)

**Document Processing Pipeline** - အသုံးပြုလိုက်ပါ → အပိုင်းခွဲ → embedding ထည့် → သိမ်းဆည်း။

**In-Memory Embedding Store** - စမ်းသပ်မှုအတွက် မဟုတ်သော သိမ်းဆည်းမှု။

**RAG** - ရှာဖွေရေးနှင့် ထုတ်လုပ်ခြင်း ဆက်စပ်ပေးသည်။

**Similarity Score** - အဓိပ္ပာယ်ဆက်နွယ်မှု တိုင်းတာမှု (0-1)။

**Source Reference** - ရှာဖွေတွေ့ရှိသောအရာ၏ မီတာဒေတာ။

## Agents နှင့် Tools - [Module 04](../04-tools/README.md)

**@Tool Annotation** - Java ဗဟိုများကို AI မှ ခေါ်နိုင်သည့် tool အဖြစ် သတ်မှတ်ခြင်း။

**ReAct Pattern** - စဉ်းစားပါ → လုပ်ဆောင်ပါ → လေ့လာခြင်း → ထပ်လုပ်ပါ။

**Session Management** - အသုံးပြုသူ သီးခြား context များ ထိန်းသိမ်းခြင်း။

**Tool** - AI agent က ခေါ်နိုင်သော လုပ်ဆောင်ချက်။

**Tool Description** - ကိရိယာရဲ့ ရည်ရွယ်ချက်နှင့် ပာရမီတာများ အကြောင်း ဖော်ပြချက်။

## Agentic Module - [Module 05](../05-mcp/README.md)

**@Agent Annotation** - AI agent များကို အပြောအဆို သတ်မှတ်မှုဖြင့် သတ်မှတ်ခြင်း။

**Agent Listener** - `beforeAgentInvocation()` နှင့် `afterAgentInvocation()` ဖြင့် agent လုပ်ဆောင်မှု ကြည့်ရှုခံစားခြင်း။

**Agentic Scope** - agents များ ရလာဒ်များ သိမ်းဆည်းပါသော မျှဝေရန် memory နေရာ။

**AgenticServices** - `agentBuilder()` နှင့် `supervisorBuilder()` ဖြင့် agent များ ဖန်တီးရန် factory။

**Conditional Workflow** - ကန့်သတ်ချက်များအရ အထူးပြု agent များသို့ လမ်းညွှန်ခြင်း။

**Human-in-the-Loop** - လူ ဆန်းစစ်အတည်ပြုချက် သို့မဟုတ် အကြောင်းအရာ သုံးသပ်ခြင်း အပြင်အဆင်။

**langchain4j-agentic** - 선언형 agent building အတွက် Maven dependency (စမ်းသပ်မှုအဆင့်)။

**Loop Workflow** - ချက်ချင်းလိုအပ်ချက် ပြည့်မှ မျှောက်တိုက် agent လုပ်ငန်းစဉ်။

**outputKey** - Agent များ ရယ်ငယ်ရာ Agentic Scope သို့ ရလဒ် သိုလှောင်ရာ ချက်။

**Parallel Workflow** - လွတ်လပ်စွာ လုပ်ငန်းကို ဘာသာရေး agent များအပြား အချိန်တစ်ပြိုင်နက်မှာ လုပ်ခြင်း။

**Response Strategy** - Supervisor က နောက်ဆုံးဖြေရှင်းချက် အကြောင်း ရေးဆွဲပုံ - LAST, SUMMARY, သို့မဟုတ် SCORED။

**Sequential Workflow** - Agent အလိုက် တစ်ဆင့်ဆက်တိုက်ဆောင်ရွက်ခြင်း။

**Supervisor Agent Pattern** - Supervisor LLM ထံမှ အခြေခံ၍ sub-agent များ ရွေးချယ်ခေါ်ယူစေသော agentic အဆင့်မြင့် ပုံစံ။

## Model Context Protocol (MCP) - [Module 05](../05-mcp/README.md)

**langchain4j-mcp** - LangChain4j အတွက် MCP တွဲဖက်အသုံးပြုရန် Maven dependency။

**MCP** - Model Context Protocol: AI app များနှင့် ပြင်ပ Tools များ ဆက်သွယ်ရန် စံချိန်တစ်ခု။ တစ်ကြိမ်တည်ဆောက်၍ မည်နေရာတွင်မဆို အသုံးပြုရန်။

**MCP Client** - MCP server များဆီ သွား၍ Tools ရှာဖွေ အသုံးပြုနိုင်သော app။

**MCP Server** - MCP ဖြင့် Tool များကို ဖော်ပြထားပြီး parameter schemas ဖြင့် ဝန်ဆောင်မှုပေးသည့် ဆာဗာ။

**McpToolProvider** - MCP tools များကို AI ဝန်ဆောင်မှုများနှင့် agent များတွင် အသုံးပြုရန် LangChain4j အစိတ်အပိုင်း။

**McpTransport** - MCP ဆက်သွယ်မှု အင်တာဖေ့စ်။ Stdio နှင့် HTTP ကဲ့သို့ ထုတ်ဖော်ချက် ရှိသည်။

**Stdio Transport** - stdin/stdout ဖြင့် ဒေသတွင်း လုပ်ထုံးလုပ်နည်း။ ဖိုင်စနစ် ဝင်ရောက်မှု သို့မဟုတ် command-line tool များအတွက် အသုံးပြုသည်။

**StdioMcpTransport** - MCP ဆာဗာ ကို subprocess အနေနှင့် အလုပ်လုပ်စေရန် LangChain4j က အကောင်အထည် ဖော်ထားသည်။

**Tool Discovery** - Client က server ဆီသို့ လက်ရှိ အသုံးပြုနိုင်သော tools များအား ဖော်ပြချက်နှင့် schemas အပါအဝင် မေးမြန်းရှာဖွေခြင်း။

## Azure ဝန်ဆောင်မှုများ - [Module 01](../01-introduction/README.md)

**Azure AI Search** - vector စွမ်းရည်ပါဝင်သည့် မိုးကောင်းကင် ရှာဖွေမှု။ [Module 03](../03-rag/README.md)

**Azure Developer CLI (azd)** - Azure အရင်းအမြစ်များ ထည့်သွင်းပေးသည်။

**Azure OpenAI** - Microsoft ၏ စီးပွားဖြစ် AI ဝန်ဆောင်မှု။

**Bicep** - Azure အောက်ခံ အဆောက်အအုံ ကုဒ်ရေးခြင်း ဘာသာစကား။ [Infrastructure Guide](../01-introduction/infra/README.md)

**Deployment Name** - Azure တွင် မော်ဒယ် deployment အတွက် အမည်။

**GPT-5.2** - Reasoning ထိန်းချုပ်မှု ပါရှိသည့် နောက်ဆုံးပေါ် OpenAI မော်ဒယ်။ [Module 02](../02-prompt-engineering/README.md)

## စမ်းသပ်ခြင်းနှင့် ဖွံ့ဖြိုးတိုးတက်မှု - [Testing Guide](TESTING.md)

**Dev Container** - ကုဒ်ဖန်တီးရေးအတွက် container ပတ်ဝန်းကျင်။ [Configuration](../../../.devcontainer/devcontainer.json)

**GitHub Models** - အခမဲ့ AI မော်ဒယ် ကစားကွင်း။ [Module 00](../00-quick-start/README.md)

**In-Memory Testing** - မီမာရီ သိုလှောင်မှု ဖြင့် စမ်းသပ်ခြင်း။

**Integration Testing** - ပိုင်းစပ်နှင့်အတူ စမ်းသပ်ခြင်း။

**Maven** - Java ဖန်တီးမှု အလိုအလျောက် ကိရိယာ။

**Mockito** - Java mocking framework။

**Spring Boot** - Java application framework။ [Module 01](../01-introduction/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**စည်းမျဉ်းချက်**  
ဤစာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှုဖြစ်သည့် [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြုပြီး ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှန်ကန်မှုအတွက် ကြိုးစားသော်လည်း အလိုအလျောက် ဘာသာပြန်ခြင်းသည် အမှားများ သို့မဟုတ် မှားယွင်းမှုများ ပါဝင်နိုင်ကြောင်း ကျေးဇူးပြု၍ လေးစားရရှိပါ။ မူလစာတမ်းကို မိမိဘာသာစကားဖြင့် အမှန်တကယ် အရာဦးတည်ချက်အဖြစ် ယူဆရန် အကြံပြုပါသည်။ အရေးပါသည့် အချက်အလက်များအတွက် မူရင်း လူတစ်ဦးချင်း ဘာသာပြန်ခြင်းကို သင့်တော်ပါသည်။ ဤဘာသာပြန်ချက်အသုံးပြုမှုကြောင့် ဖြစ်ပေါ်လာသော နားမလည်မှုများ သို့မဟုတ် မှားယွင်း ဖတ်ရှုမှုများအတွက် ကျွန်ုပ်တို့သည် တာဝန်မယူပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->