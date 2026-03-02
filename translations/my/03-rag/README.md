# Module 03: RAG (Retrieval-Augmented Generation)

## Table of Contents

- [Video Walkthrough](../../../03-rag)
- [What You'll Learn](../../../03-rag)
- [Prerequisites](../../../03-rag)
- [Understanding RAG](../../../03-rag)
  - [Which RAG Approach Does This Tutorial Use?](../../../03-rag)
- [How It Works](../../../03-rag)
  - [Document Processing](../../../03-rag)
  - [Creating Embeddings](../../../03-rag)
  - [Semantic Search](../../../03-rag)
  - [Answer Generation](../../../03-rag)
- [Run the Application](../../../03-rag)
- [Using the Application](../../../03-rag)
  - [Upload a Document](../../../03-rag)
  - [Ask Questions](../../../03-rag)
  - [Check Source References](../../../03-rag)
  - [Experiment with Questions](../../../03-rag)
- [Key Concepts](../../../03-rag)
  - [Chunking Strategy](../../../03-rag)
  - [Similarity Scores](../../../03-rag)
  - [In-Memory Storage](../../../03-rag)
  - [Context Window Management](../../../03-rag)
- [When RAG Matters](../../../03-rag)
- [Next Steps](../../../03-rag)

## Video Walkthrough

ဒီ module နဲ့ စတင်သွားမယ့် နည်းလမ်းကို ရှင်းလင်းပြတဲ့ live session ကို ကြည့်ပါ။

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## What You'll Learn

ယခင် module တွေမှာ၊ AI နဲ့ စကားပြောနည်းနဲ့ prompt များကို ထိရောက်စွာ ဖွဲ့စည်းနည်းကို သင်ယူခဲ့ပါပြီ။ ဒါပေမယ့် အခြေခံ ကန့်သတ်ချက်တစ်ခုရှိတယ်။ ဘာလဲ ဆိုတော့ ဘာသာစကားမော်ဒယ်တွေဟာ သင်ကြားပေးခဲ့သမျှ သတင်းအချက်အလက်ကပဲ သိသည်။ ကုမ္ပဏီ၏ ကိုယ်ပိုင်မူဝါဒများ၊ သင့်ရဲ့ project မှတ်တမ်းများ သို့မဟုတ် မသင်ကြားပေးထားတဲ့ သတင်းအချက်အလက်များအကြောင်း မေးခွန်းများကို ဖြေဆိုပေးနိုင်ခြင်း မရှိပါ။

RAG (Retrieval-Augmented Generation) က ဒီပြဿနာကို ဖြေရှင်းပေးတယ်။ မော်ဒယ်ကို သင်၏ သတင်းအချက်အလက်တွေကိုသင်ကြားဖို့ကြိုးစားတဲ့အစား (ဒါက ဈေးကြီးပြီး အသုံးဝင်မှုနည်းပါ), သင့်ရဲ့ စာတမ်းတွေကနေ ရှာဖွေနိုင်သမျှ လုပ်ငန်းစွမ်းအား ပေးပါတယ်။ တစုံတယောက် မေးလျှင် စနစ်က သင့်စာတမ်းတွေထဲက သက်ဆိုင်ရာ အချက်အလက်တွေကို ရှာကာ prompt တွင် ထည့်သွင်းပေးပါတယ်။ မော်ဒယ်က အဲဒီ ရရှိထားတဲ့ context အပေါ် မူတည်ပြီး ဖြေကြားတယ်။

RAG ကို မော်ဒယ်ကို ရည်ညွှန်းစာကြည့်တိုက်တစ်ခု ပေးခြင်းလို့ စဉ်းစားပါ။ မေးချက်တစ်ခု မေးတဲ့အခါ စနစ်က -

1. **User Query** - သင် မေးခွန်းမေးသည်
2. **Embedding** - မေးခွန်းကို vector အဖြစ် ပြောင်းပေးသည်
3. **Vector Search** - ဆင်တူစာတမ်းအပိုင်းများ ရှာဖွေသည်
4. **Context Assembly** - သက်ဆိုင်ရာ စာတမ်းအပိုင်းများကို prompt ထဲထည့်သည်
5. **Response** - LLM က context အပေါ်မူတည်ပြီး ဖြေကြားသည်

ဒီလိုနဲ့ မော်ဒယ်ရဲ့ ဖြေဆိုချက်တွေဟာ သင့်ရဲ့ ထင်ရှားသော ဒေတာပေါ် အခြေခံထားခြင်းဖြစ်ပြီး သင်ကြားမှုသိထားချက် သို့မဟုတ် စီမံကိန်းဖြေဆိုချက် ပြုလုပ်ခြင်းမဟုတ်ပါ။

## Prerequisites

- ပြီးစီးထားသော [Module 00 - Quick Start](../00-quick-start/README.md) (Easy RAG ဥပမာအတွက်)
- ပြီးစီးထားသော [Module 01 - Introduction](../01-introduction/README.md) (Azure OpenAI အရင်းအမြစ်များ တပ်ဆင်ပြီးတည်ဆောက်ထားသည်၊ အထူးသဖြင့် `text-embedding-3-small` embedding မော်ဒယ်ပါဝင်ရန်)
- Azure အတည်ပြုချက်များပါဝင်သော `.env` ဖိုင်ကို root directory မှာရှိရန် (Module 01 မှာ `azd up` က ဖန်တီးပေးသည်)

> **Note:** Module 01 မပြီးစီးရသေးပါက အဆိုပါ deployment အဆင့်များကို နောက်ဆုံးစတင်ထားရှိပါ။ `azd up` က GPT chat မော်ဒယ်နဲ့ embedding မော်ဒယ်နှစ်ခုလုံးကို တပြိုင်နက် တပ်ဆင်ပေးပါတယ်။

## Understanding RAG

အောက်ဖော်ပြပါပုံဆွဲမှာ အဓိကခံယူချက်ကို ပုံဖော်ထားပါတယ်။ မော်ဒယ်ရဲ့ သင်ကြားမှုဒေတာကိုတစ်ခုတည်း မူတည်ခဲြမထားဘဲ RAG က သင့်စာတမ်းရည်ညွှန်းစာကြည့်တိုက်ကို မေးခွန်းတိုင်းမထွက်မီ စစ်ဆေးခိုင်းပါတယ်။

<img src="../../../translated_images/my/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*ဤပုံဆွဲဟာ စံ LLM တစ်ခု (သင်ကြားမှုဒေတာမှ ခန့်မှန်းချက်ပြုသည်) နှင့် RAG-reinforced LLM (သင့်စာတမ်းများကို ပထမဦးစွာ စစ်ဆေးခြင်း) အကြား ကွာခြားမှုကို ပြထားသည်။*

အောက်ပါပုံအတိုင်း ဒီအစိတ်အပိုင်းတွေ ဆက်သွယ်ဖို့အတွက် - အသုံးပြုသူမေးခြင်းဟာ embedding, vector search, context assembly နဲ့ answer generation အဆင့်(၄)ဆင့်ဖြတ်သွားပါတယ်။

<img src="../../../translated_images/my/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*ဤပုံဆွဲမှာ အသုံးပြုသူမေးခွန်း က embedding, vector search, context assembly နဲ့ answer generation ဆင့်ဆင့်ဖြတ်သွားတဲ့ RAG pipeline ကို ဖော်ပြထားသည်။*

ဒီ module ရဲ့ ကျန်သည့်အပိုင်းတွေမှာ အဆင့်တိုင်းကို သင်ယူနိုင်ရန် နမူနာ code များနဲ့အတူ လေ့လာနိုင်ပါတယ်။

### Which RAG Approach Does This Tutorial Use?

LangChain4j မှ RAG ကို တက်အောင်သုံးရန် နည်းလမ်း သုံးမျိုး ရှိပြီး abstraction အဆင့် ကွာခြားသည်။ အောက်ဖော်ပြပါပုံတွင် အသေးစိတ်နှိုင်းယှဉ်ထားသည်။

<img src="../../../translated_images/my/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*ဤပုံသည် LangChain4j ၏ Easy, Native, Advanced RAG နည်းလမ်း သုံးမျိုး၏ အဓိကအစိတ်အပိုင်းများ နှင့် အသုံးပြုရမည့် ဖော်ပြချက်များကို နှိုင်းယှဉ်ပြထားသည်။*

| Approach | What It Does | Trade-off |
|---|---|---|
| **Easy RAG** | `AiServices` နှင့် `ContentRetriever` ကို အလိုအလျောက် ချိတ်ဆက်ပေးပြီး အင်တာဖေ့စ်တွင် ဟုတ်မှတ်ချက် တစ်ခု ပေးလိုက်သလို retriever ချိတ်ဆက်ထားပြီး embedding, ရှာဖွေခြင်း နဲ့ prompt ဖန်တီးခြင်းကို LangChain4j က နောက်ခံ ပြုလုပ်ခြင်း ဖြစ်သည်။ | ကုဒ်အနည်းဆုံး၊ အဆင့်တိုင်းမှာ ဘာဖြစ်နေသည်ကို မမြင်ရ။ |
| **Native RAG** | embedding မော်ဒယ်ကို ကိုယ်တိုင် ခေါင်းပြီး၊ store ကို ရှာဖွေရေးလုပ်၊ prompt ကိုဖန်တီးပြီး ဖြေဆိုချက်ထုတ်သည်။ တစ်ဆင့်စီကို တိတိကျကျ ချမှတ်ထားသည်။ | ကုဒ်များ ပိုများသော်လည်း အဆင့်တိုင်းကို မြင်နိုင် ၊ ပြင်ဆင်နိုင်သည်။ |
| **Advanced RAG** | `RetrievalAugmentor` framework ကို အသုံးပြု၍ query transformer များ၊ routers, re-rankers, ရှာဖွေမှု injectors စတဲ့ production-level pipeline များ ထောက်ပံ့သည်။ | အလွန်ပြည့်စုံသေချာခြင်းရှိသော်လည်း နှစ်သက်မှု ပိုများပြီး ရှုပ်ထွေးသည်။ |

**ဒီ tutorial မှာ Native approach ကို သုံးထားပါတယ်။** RAG pipeline ၏ အဆင့်တိုင်း (query embedding, vector store ရှာဖွေရေး, context ဖွဲ့စည်းမှုနဲ့ ဖြေဆိုချက်ထုတ်မှု) ကို [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) မှာ တိတိကျကျ ရေးသားထားပါတယ်။ ဒါဟာ သင်ယူခြင်းအရင်းအမြစ်အဖြစ် အဆင့်တိုင်းကို ကြည့်ပြီး နားလည်ရပါမယ်၊ ကုဒ်အနည်းဆုံး ဖြစ်စေခြင်းထက် မာနရှိတာ ပိုရေးထားတာ ပိုအရေးကြီးပါတယ်။ အဆင့်အတန်းအတိုင်း သဘောအရားကို သိရှိရင် မကြာမီလွယ်ကူသော Easy RAG သို့မဟုတ် လုပ်ငန်းတွင် အသုံးပြုနိုင်သော Advanced RAG သို့ ထပ်မံတက်လှမ်းနိုင်ပါသည်။

> **💡 Easy RAG ကို တွေ့မြင်ပြီးပြီလား?** [Quick Start module](../00-quick-start/README.md) မှာ `SimpleReaderDemo.java` ([Document Q&A ဥပမာ]) က Easy RAG ကို အသုံးပြုထားပြီး LangChain4j က embedding, ရှာဖွေရေး နဲ့ prompt ဖန်တီးခြင်းကို အလိုအလျောက် လုပ်ပေးသည်။ ဒီ module က လူကြီးမင်းကို အဆင့်တိုင်း ရုပ်သိမ်းပြထားတဲ့ pipeline ကို ခွဲထုတ်ပြီး တစ်ဆင့်စီကို ကိုယ်တိုင် မြင်၊ ထိန်းချုပ်ရန်အတွက်ဖြစ်ပါသည်။

<img src="../../../translated_images/my/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*ဤပုံသည် `SimpleReaderDemo.java` ထဲရှိ Easy RAG pipeline ကို ပြထားသည်။ ဒီ module မှာ သုံးထားတဲ့ Native approach နဲ့ နှိုင်းယှဉ်ပါ။ Easy RAG ဟာ embedding, retrieval နဲ့ prompt assembly ကို `AiServices` နဲ့ `ContentRetriever` မှာ ဖုံးကွယ်ထားပြီး သင်စာတမ်းတင်၊ retriever ချိတ်ပြီး ဖြေပေးချက်ရယူသည်။ ဒီ module ရဲ့ Native approach မှာတော့ embedding, ရှာဖွေရေး, context ဖြည်းဖြည်းဆောက်ခြင်း နှင့် generate လုပ်ခြင်း စသဖြင့် အဆင့်တိုင်းကို သင်ကိုယ်တိုင်ခေါ်ယူ လုပ်ဆောင်ရုံဖြစ်၍ ကြည့်ရှု ထိန်းချုပ် ပိုင်ခွင့်ရှိစေသည်။*

## How It Works

ဒီ module ရဲ့ RAG pipeline က အသုံးပြုသူ မေးခွန်းတစ်ခုမေးတိုင်းမှာ အဆင့်(၄) ဆင့်ကို ဖော်ပြထားတယ်။ ပထမဆုံး သင် upload လုပ်တဲ့ စာရွက်စာတမ်းကို **ခွဲခြားပြီး ချွတ်ချပြီး** မော်ဒယ်ရဲ့ context window မှာ တင်းအားခံနိုင်အောင် မျှတစွာ သွားဖြန့်တယ်။ ထိုအပိုင်းများကို **vector embedding** ဖြင့် ပြောင်းပြီး သိမ်းဆည်းသည်။ မေးခွန်းတစ်ခု လာရင် **semantic ရှာဖွေမှု** လုပ်ပြီး သက်ဆိုင်ရာ မြဴးခွဲများကို ရှာဖွေသည်။ နောက်ဆုံးမှာ context အဖြစ် LLM မှာ ပေးပြီး **ဖြေဆိုချက်ထုတ်ပေးသည်**။ အောက်တွင် အဆင့်စီကိုနမူနာကုဒ်နဲ့ ပုံဆွဲဖြင့်ရှင်းပြထားပါတယ်။ ပထမအဆင့်ကို ကြည့်ကြရအောင်။

### Document Processing

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

သင် စာရွက်စာတမ်းတင်လိုက်သောအခါ၊ စနစ်က တို့ဖတ်၍ (PDF သို့မဟုတ် စာသားချောချောမွေ့မွေ့), ဖိုင်နာမည် အပါအဝင် metadata များ ဆက်စပ်ပေးပြီး၊ မော်ဒယ် context window ထဲ ကို အဆင်ပြေစွာ ထည့်နိုင်ရန် အပိုင်းသေးသေးများ ခွဲထုတ်ပေးသည်။ ချွတ်ခွဲခြင်းမှာ အနည်းငယ်တောင် မျိုးတူ Lexer နှင့် ချိတ်ဆက်ထားသော token အချိုးအစားကို မျှော်လင့်ပြီး အနီးကပ် context မဆုံးရှုံးစေတာ ဖြစ်စေပါတယ်။

```java
// အပ်လုဒ်လုပ်ပြီးသောဖိုင်ကို ပတ်သက်စဉ် LangChain4j Document အဖြစ် အုပ်ချုပ်ပါ
Document document = Document.from(content, metadata);

// ၃၀၀ တုကင်အရွယ် အပိုင်းများသို့ ခွဲပြီး ၃၀ တုကင် ပြန်လည်နှိုင်းယှဉ်မှုရှိပါသည်
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```

အောက် ဖော်ပြပါပုံရိပ်က ဒီ ဆောင်ရွက်မှုကို မျက်မြင်ရသလို ဖော်ပြထားတယ်။ ချွတ်ချွတ်ငယ်ငယ် တစ်ချိုက်ချိုက် မြောက်တစ်နေရာရှိ tokens များ တစ်နည်းတစ်ဖြင့် တွဲဖက် ထားသည့် token အပိုင်း ၃၀ ဟာ အရေးကြီး context မဆုံးရှုံးစေဖို့ ကာကွယ်ထားပါတယ်။

<img src="../../../translated_images/my/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*ဤပုံသည် စာရွက်စာတမ်းကို ၃၀၀ token အပိုင်းအသီးသီး သို့ ခွဲခြားပြပြီး စာပိုင်းရဲ့ မျက်နှာပြင်တိုင်းမှာ ၃၀ token တို့ လိုက်ဖက်မှုရှိနေသည်ကို ပြသသည်။*

> **🤖 GitHub Copilot Chat နဲ့ စမ်းကြည့်ပါ:** [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) ကို ဖွင့်ပြီး မေးပါ -
> - "LangChain4j က စာရွက်စာတမ်းတွဲ မျှတစွာ ခွဲခြားသောနည်းကို ဘယ်လိုလုပ်သလဲ၊ အလွှာတူ token တွေ ပေးတာ ဘာကြောင့် အရေးကြီးတာလဲ?"
> - "စာရွက်စာတမ်းအမျိုးအစား မတူပေါင်းများစွာကဲ့သို့ chunk အရွယ်အစား အကောင်းဆုံး ဒီထဲမှာ ဘာနည်းလမ်းလဲ?"
> - "ဘာသာစကားများစုံ သို့မဟုတ် format အထူးသော စာရွက်စာတမ်းများကို ကျွန်တော်ဘယ်လို ကိုင်တွယ်သင့်လဲ?"

### Creating Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

အပိုင်းအနှစ်သက်ဆုံးတိုင်းကို numerical ဖော်ပြချက်တစ်ခုဖြစ်သော embedding အဖြစ် ပြောင်းသည် - အဓိကအားဖြင့် အဓိပ္ပါယ်မှ နံပါတ်သို့ ပြောင်းလဲသည့် စနစ်။ embedding မော်ဒယ်က chat မော်ဒယ်လို အသိပညာရှိတာ မဟုတ်သလို အမိန့်များနားမလည်၊ ကရုဏာမပြု၊ မေးခွန်းများကို မဖြေပေးနိုင်ပါ။ လုပ်ဆောင်ချက်ဟာ စာသားတွေကို သက်ဆိုင်ရာ အဓိပ္ပါယ်တူ vector များအနီးတွင် သွားရောက်တည်ရှိသည့် space ပြောင်းပေးခြင်း ဖြစ်ပါတယ်။ "ကား" နှင့် "automobile" တို့ပြင်ပ၊ "ပြန်အမ်းမူဝါဒ" နှင့် "ငွေပြန်အမ်းဖို့" တို့ ဆီလျော်အောင် ကြိုက်နှစ်သက်ချက်အရ vector space ပိုင်းတွင်တည်ရာယူသည်။ ချတ်နဲ့ ပြောဆိုလိုသော လူတစ်ယောက်လို၊ embedding မော်ဒယ်ကတော့ အလွန်ကောင်းမွန်သော စာရင်းသွင်း ကိရိယာတစ်ခုလို ဖြစ်ပါတယ်။

<img src="../../../translated_images/my/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*ဤပုံသည် အဓိပ္ပါယ်တူသော စကားလုံးများ (ဥပမာ "ကား" နှင့် "automobile") ကို vector space တည်နေရာတူတူ ဖြစ်အောင် ဖော်ပြသည်။*

```java
@Bean
public EmbeddingModel embeddingModel() {
    return OpenAiOfficialEmbeddingModel.builder()
        .baseUrl(azureOpenAiEndpoint)
        .apiKey(azureOpenAiKey)
        .modelName(azureEmbeddingDeploymentName)
        .build();
}

EmbeddingStore<TextSegment> embeddingStore = 
    new InMemoryEmbeddingStore<>();
```

အောက်ဖော်ပြပါ class diagram ဟာ RAG pipeline မှာ လည်ပတ်သော နှစ်မျိုးသော လမ်းကြောင်းများ နှင့် LangChain4j မှ ထမ်းဆောင်သော class များကို ပြသည်။ **Ingestion flow** (upload တစ်ခါတည်း လည်ပတ်) မှာ စာရွက်စာတမ်းခွဲခြားခြင်း၊ chunk များကို embed လုပ်ခြင်းနှင့် `.addAll()` ဖြင့် သိမ်းဆည်းခြင်း ဖြစ်သည်။ **Query flow** (အသုံးပြုသူ မေးခွန်း မေးတိုင်း လည်ပတ်) မှာ မေးခွန်းကို embed လုပ်၊ store ကို `.search()` ဖြင့် ရှာဖွေပြီး တူညီသူ context ရဲ့ အပိုင်းလေးများကို chat မော်ဒယ်သို့ ပေးပို့ပြီး ဖြေဆိုချက်ထုတ်သည်။ နှစ်ခုလုံးဟာ `EmbeddingStore<TextSegment>` interface တစ်ခုကို ပြန်မွာယူသည်။

<img src="../../../translated_images/my/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*ဤပုံသည် RAG pipeline ၏ ingestion နဲ့ query လမ်းကြောင်းများကို ပြပြီး shared EmbeddingStore မှတဆင့် ချိတ်ဆက်ထားသည်။*

embedding များသိမ်းဆည်းပြီးတာနဲ့ ဆင်တူသောအသေးစိတ်အချက်အလက်များဟာ vector space မှာ အနီးကပ်စုစည်းသွားတာဟာ semantic ရှာဖွေမှုကို ဖြစ်လာစေတယ်။

အောက်ပါအတိုင်း visual အဖြစ် ပြထားသည်မှာ ကိုယ်စားပြုသော အကြောင်းအရာများအလိုက် စာရွက်စာတမ်းများ ၃D vector space တွင် မျက်နှာချင်းဆိုင် အတူတက်နေကြတာကို ပြလိုက်သည် (နည်းပညာစာတမ်းများ၊ စီးပွားရေးစည်းမျဉ်းများ၊ မကြာခဏ မေးလေ့ ရှေးမှုများ စသည်ဖြင့် အုပ်စုများ ဖွဲ့စည်းထားသည်)။

<img src="../../../translated_images/my/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*ဤ visual က ချိတ်ဆက်သည့် စာတမ်းများဟာ ၃D vector space တွင် တွဲဖက်စုစည်းထားပြီး သက်ဆိုင်ရာ အုပ်စုများ ဖြစ်ပေါ်နေကြသည်။*

အသုံးပြုသူ ရှာဖွေတဲ့အခါ စနစ်က လေးဆင့် တလျှောက် လုပ်ဆောင်သည်။ စာရွက်စာတမ်းကို တစ်ကြိမ် embed လုပ်ပြီး၊ မေးခွန်းကိုရှာဖွေရန်အတွက် ပို embed လုပ်ပြီး၊ cosine similarity ဖြင့်မေးခွန်း vector ကို ရှိသည့် vector တွေနဲ့ နှိုင်းယှဉ်ပြီး၊ Top-K အဆင့်မြင့် အပိုင်းများကို ပြန်အသုံးပြုသည်။

အောက်ဖော်ပြပါ ပုံမှာ အဆင့်တိုင်းနဲ့ သက်ဆိုင်တဲ့ LangChain4j class များကို ပြထားပါတယ်။

<img src="../../../translated_images/my/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*ဤပုံသည် စာရွက်စာတမ်း embed လုပ်ခြင်း၊ မေးခွန်း embed လုပ်ခြင်း၊ cosine similarity ဖြင့် နှိုင်းယှဉ်ခြင်း၊ Top-K ရလဒ် ပြန်ပေးခြင်း ဆိုသော embedding ရှာဖွေရေး လုပ်ငန်းစဉ် လေးခုကို ဖော်ပြတယ်။*

### Semantic Search

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

သင်မေးခွန်းမေးလိုက်ပြီဆိုလျှင် မေးခွန်းသုံးကိုပါ embedding ပြုလုပ်သည်။ စနစ်က မေးခွန်းရဲ့ embedding နဲ့ စာရွက်စာတမ်း chunk များ၏ embedding တို့ကို နှိုင်းယှဉ်သည်။ ဘာသာတူ similarity ဖြစ်ပြီး စကားလုံး keywords တွေနဲ့ မတူညီတဲ့အချင်းကား semantic similarity ရှိနေသည့် ချွတ်တန်းများကို ရှာဖွေသည်။

```java
Embedding queryEmbedding = embeddingModel.embed(question).content();

EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
    .queryEmbedding(queryEmbedding)
    .maxResults(5)
    .minScore(0.5)
    .build();

EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);
List<EmbeddingMatch<TextSegment>> matches = searchResult.matches();

for (EmbeddingMatch<TextSegment> match : matches) {
    String relevantText = match.embedded().text();
    double score = match.score();
}
```

အောက်ပါ ပုံဆွဲဟာ semantic search နဲ့ သာမာန် keyword search ကို နှိုင်းယှဉ်ပြထားတယ်။ "vehicle" လို့ keyword ရှာဖွေမယ်ဆိုရင် "cars and trucks" အကြောင်း chunk ကို မတွေ့နိုင်ပေမဲ့ semantic search က အကြောင်းအရ အထွေထွေနဲ့ အဓိပ္ပါယ် တူညီသည်ကို နားလည်ပြီး အဆင့်မြင့် အဖြေမှာ ထုတ်ပေးသည်။

<img src="../../../translated_images/my/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*ဤပုံသည် keyword-based ရှာဖွေမှုနဲ့ semantic search ကို နှိုင်းယှဉ်ပြပြီး keyword မတူပေမဲ့ အဓိပ္ပါယ် တူညီသော အကြောင်းအရာတွေကို semantic search က ရှာဖွေ ဖြေဆိုပေးတယ်ဆိုတာ ဖော်ပြသည်။*

နောက်ခံမှာ similarity ကို cosine similarity နည်းဖြင့် တိုင်းတာတယ်၊ "ဒီ မြှောင်နှစ်ခု ညီမျှလား?" ဆိုတဲ့ မေးခွန်းဖြစ်သည်။ သုံးଟွက်စကားများက မတူဘူး၊ ဒါပေမယ့် အဓိပ္ပါယ် တူသောကြောင့် နိမ့်မြင့်တူညီလိုက်သည့် vector များဖြစ်ပြီး ၁.၀ အနီးတဝိုက်တွင် ရคะแนน ရမည်။

<img src="../../../translated_images/my/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>
*ဤပုံပြင်မှာ embedding ဗက်တာများအကြားထောင့်တစ်ခုအဖြစ် cosine အားတူမှုကိုဖော်ပြထားသည် — ဗက်တာများပိုတူညီလျှင် 1.0 အနီးတွင်မှတ်တမ်းတင်ပြီး အဓိပ္ပါယ်ဆိုင်ရာ တူညီမှုမြင့်မားမှုကိုပြသည်။*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat ဖြင့် စမ်းသပ်ကြည့်ပါ:** [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) ဖိုင်ကိုဖွင့်ပြီး မေးမြန်းပါ-
> - "embedding တွေနဲ့ similarity search ဘယ်လိုအလုပ်လုပ်သလဲ၊ score ကို ဘာတွေဆုံးဖြတ်သလဲ?"
> - "similarity threshold ကို ဘယ်လောက်သုံးသင့်ပြီး ဒါက ရလဒ်တွေကို ဘယ်လိုသက်ရောက်သလဲ?"
> - "သက်ဆိုင်ရာစာရွက်စာတမ်းမတွေ့တဲ့အခါ ဘယ်လို ကိုင်တွယ်ရမလဲ?"

### ဖြေကြားချက်ထုတ်လုပ်ခြင်း

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

အဆက်မပြတ်သက်ဆိုင်ရာ chunks များကို နောက်ထပ်ညွှန်ကြားချက်များ၊ ရရှိထားသောဘာသာရပ်များနှင့် အသုံးပြုသူ၏မေးခွန်းပါဝင်သော ဖွဲ့စည်းထားသော prompt အဖြစ် စုပေါင်းထားသည်။ မော်ဒယ်သည် ထိုသတ်မှတ်ထားသော chunks များသာဖတ်ရှုပြီး အချက်အလက်အလျောက်သာဖြေကြားပါသည် — hallucination ဖြစ်ပေါ်ခြင်းကို ကာကွယ်ပေးသည်။

```java
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));

String prompt = String.format("""
    Answer the question based on the following context.
    If the answer cannot be found in the context, say so.

    Context:
    %s

    Question: %s

    Answer:""", context, request.question());

String answer = chatModel.chat(prompt);
```

အောက်တွင်ဖော်ပြထားသည့်ပုံသည် ဤဖွဲ့စည်းခြင်းကို လုပ်ဆောင်နေစဉ်ကို ပြသည် — ရှာဖွေရေးခြေလှမ်းမှ ထိပ်တန်း score ရရှိသော chunks များကို prompt template အတွင်း ထည့်သွင်းပြီး `OpenAiOfficialChatModel` သည် အခြေခံထားသော ဖြေကြားချက်တစ်ခု ဖန်တီးပေးသည်။

<img src="../../../translated_images/my/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*ဤပုံသည် ထိပ်တန်း score ရရှိသော chunks များကို ဖွဲ့စည်းထားသော prompt အဖြစ် စုစည်းပုံနှင့် မော်ဒယ်အနေဖြင့် သင့်ဒေတာမှ အခြေခံဖြေကြားချက် တစ်ခု ဖန်တီးနိုင်ရေးကို ပြသည်။*

## အပလီကေးရှင်းကို လည်ပတ်ခြင်း

**သတ်မှတ်ချက်ကို စစ်ဆေးခြင်း-**

Module 01 တွင်ဖန်တီးထားသည့် Azure လိုင်စင်နှင့် root directory တွင် `.env` ဖိုင်ရှိမရှိ သေချာစေရန်–

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ပြသသင့်သည်
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT ကိုပြသသင့်သည်။
```

**အပလီကေးရှင်း စတင်ရန်:**

> **မှတ်ချက်:** Module 01 မှ `./start-all.sh` ဖြင့် အပလီကေးရှင်းအားလုံးကို ရှေ့ကစတင်ပြီးသားဖြစ်ပါက ဤ module သည် port 8081 တွင် လည်ပတ်နေပြီးဖြစ်သည်။ အောက်ပါ start command မလိုအပ်ဘဲ http://localhost:8081 သို့တိုက်ရိုက်သွားနိုင်ပါသည်။

**ရွေးချယ်မှု ၁: Spring Boot Dashboard အသုံးပြုခြင်း (VS Code အသုံးပြုသူများအတွက် အကြံပြုချက်)**

dev container တွင် Spring Boot Dashboard extension ပါဝင်ပြီး၊ ၎င်းက Spring Boot အပလီကေးရှင်းအားလုံးကို မျက်နှာပြင်တွင် စီမံခန့်ခွဲရန် အချက်ပြအစီအစဉ်ပေးသည်။ VS Code ၏ ဘယ်ဘက် Activity Bar တွင် (Spring Boot icon ရှိရာ) တွေ့နိုင်ပါသည်။

Spring Boot Dashboard မှ –
- workspace တွင် ရနိုင်သမျှ Spring Boot အပလီကေးရှင်းအားလုံးကို ကြည့်နိုင်သည်
- တစ်ခါတည်း Single click ဖြင့် စတင်/ရပ်တန့်နိုင်သည်
- အပလီကေးရှင်းများ၏ log များကို တည်တဆင့်ကြည့်ရှုနိုင်သည်
- အပလီကေးရှင်း အခြေအနေများကို စောင့်ကြည့်နိုင်သည်

"rag" အနီးရှိ play ခလုတ်ကို နှိပ်၍ ဤ module ကို စတင်လိုက်ပါ၊ မဟုတ်လျှင် module အားလုံးကို တစ်ပြိုင်နက် စတင်နိုင်သည်။

<img src="../../../translated_images/my/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*ဤ screenshot သည် VS Code တွင်ရှိသည့် Spring Boot Dashboard ကို ပြသည်၊ မည်သည့်အပလီကေးရှင်းကို စတင်၊ ရပ်တန့်၊ မျက်နှာပြင်ပေါ်တွင် ကြည့်ရှုနိုင်သည်။*

**ရွေးချယ်မှု ၂: shell script များ အသုံးပြုခြင်း**

ကျွန်ုပ်တို့၏ web applications အားလုံး (modules 01-04) ကို စတင်ရန် -

**Bash:**
```bash
cd ..  # မှတ်တမ်းရာမြစ်မှ directory မှစ၍
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # အမြစ်ဖိုင်လ်ဒါမှ
.\start-all.ps1
```

သို့မဟုတ် ဤ module ကိုသာ စတင်ရန်-

**Bash:**
```bash
cd 03-rag
./start.sh
```

**PowerShell:**
```powershell
cd 03-rag
.\start.ps1
```

script နှစ်ခုစလုံးသည် root `.env` ဖိုင်မှ environment variables များကို အလိုအလျောက် load ပြီး JAR မရှိပါက ဒီအချိန်တွင် build လုပ်မည်။

> **မှတ်ချက်:** စတင်မတိုင်မီ မိမိလိုအပ်သလို module အားလုံးကို manual နည်းဖြင့် build လုပ်ချင်လျှင်-
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

http://localhost:8081 ကို browser တွင် ဖွင့်ပါ။

**ရပ်ရန်-**

**Bash:**
```bash
./stop.sh  # ဒီမော်ဂျူးသာ
# သို့မဟုတ်
cd .. && ./stop-all.sh  # အားလုံးသောမော်ဂျူးများ
```

**PowerShell:**
```powershell
.\stop.ps1  # ဒီမော်ဒျူးသာ
# မှမဟုတ်
cd ..; .\stop-all.ps1  # အားလုံးသောမော်ဒျူးများ
```

## အပလီကေးရှင်း အသုံးပြုခြင်း

ဤအပလီကေးရှင်းသည် စာရွက်တင်ရန်နှင့် မေးခွန်းမေးရန် web interface ပံ့ပိုးပေးသည်။

<a href="images/rag-homepage.png"><img src="../../../translated_images/my/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*ဤ screenshot သည် RAG application interface ကိုပြသည်၊ သင်သည် စာရွက်များတင်ပြီး မေးခွန်းမေးနိုင်သည်။*

### စာရွက်တင်ခြင်း

စာရွက်တစ်စောင်တင်ခြင်းဖြင့် စတင်ပါ - စမ်းသပ်ရန်အတွက် TXT ဖိုင်များအတွက် အကောင်းဆုံးဖြစ်သည်။ ဒီ directory တွင် LangChain4j ၏ လက္ခဏာများ၊ RAG ဆောင်ရွက်ပုံများနှင့် အကောင်းဆုံးလေ့ကျင့်စနစ်များ ပါရှိသည့် `sample-document.txt` ကိုပါ ထည့်သွင်းထားပါသည် — စနစ်ကို စမ်းသပ်ရန် သင့်တော်သည်။

စနစ်သည် သင့်စာရွက်ကို လက်ခံ၊ ထိုစာရွက်ကို chunks များဖြင့် ခွဲထုတ်ပြီး၊ chunk တစ်ခုခြင်းစီအတွက် embeddings ဖန်တီးပေးသည်။ ဒါကို စာရွက် တင်တဲ့အချိန်မှာ အလိုအလျောက် လုပ်ဆောင်ပေးသည်။

### မေးခွန်းမေးရန်

ယခုအကြောင်းအရာအပေါ် အထူးသတ်မှတ်ထားသည့် မေးခွန်းများကို မေးမြန်းပါ။ စာရွက်တွင် ၎င်းအညွှန်းထားသည့် အချက်အလက်မှတ်ရှိသည့် အချက်အလက်ဖြစ်စေရန် ကြိုးစားပါ။ စနစ်သည် သက်ဆိုင်သော chunks များ ရှာဖွေ၊ ၎င်းများအား prompt အတွင်း ထည့်သွင်းပြီး ဖြေကြားချက် ဖန်တီးစေသည်။

### ရင်းမြစ်ကို တိုင်းတာကြည့်ရန်

တစ်ခုချင်းစီ၏ ဖြေကြားချက်တွင် similarity score များနှင့် ရင်းမြစ်ဆိုင်ရာ ရည်ညွှန်းချက် ပါရှိသည်ကို မှတ်သားပါ။ ၎င်း score များ (0 မှ 1) သည် မေးခွန်းနှင့် ကွဲပြားမှုကို ဖော်ပြသည်။ score မြင့်မားခြင်းသည် ပိုမိုလိုက်ဖက်မှုကို သက်မှတ်သည်။ ၎င်းက ဖြေကြားချက်ကို ရင်းမြစ်အတိုင်း စစ်ဆေးနိုင်စေသည်။

<a href="images/rag-query-results.png"><img src="../../../translated_images/my/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*ဤ screenshot သည် မေးခွန်း ရလဒ်များ၊ ဖန်တီးထားသော ဖြေကြားချက်၊ ရင်းမြစ် နှင့် တစ်ခုချင်း chunks များ၏ သက်ဆိုင်မှု score များကို ပြသသည်။*

### မေးခွန်းမျိုးစုံ စမ်းသပ်ကြည့်ရန်

မေးခွန်း အမျိုးအစား ကွဲပြားမှုများ စမ်းသပ်ကြည့်ပါ-
- သီးသန့်အချက်အလက်များ: "အဓိကခေါင်းစဉ်ကဘာလဲ?"
- နှိုင်းယှဉ်ချက်များ: "X နဲ့ Y မှာ ဘာကွာခြားတာလဲ?"
- အကျဉ်းချုပ်များ: "Z အကြောင်း အဓိကအချက်များကို အကျဉ်းချုပ်ပြပါ"

မေးခွန်းနှင့် စာရွက်အကြောင်းအရာ၏လိုက်ဖက်မှုအပေါ် မူတည်၍ သက်ဆိုင်မှု score များပြောင်းလဲမှုကို ကြည့်ရှုပါ။

## အထူးအယူအဆများ

### Chunking မဟာဗျူဟာ

စာရွက်များကို ၃၀၀ token ရှိသော chunks များ အပိုင်းခြားထားပြီး ၃၀ tokens ပြန်လည်ဖြည်းညှိထားသည်။ ၎င်းသည် chunk တစ်ခုစီတွင် စာသားအကြောင်းအရာ ရှိမှု လုံလောက်စေရန်နှင့် prompt တွင် chunks များပါဝင်နိုင်စေရန်ဖြစ်သည်။

### Similarity Scores

ရရှိသည့် chunks အားလုံးသည် similarity score တစ်ခုဖြင့် နှိုင်းယှဉ်ထားသည်၊ ၎င်းသည် အသုံးပြုသူမေးခွန်းနှင့် မတူညီမှုရှယ်ယာကို ဖော်ပြသည်။ အောက်ပိုင်းပုံတွင် score ကွက်တိများနှင့် စနစ်၏ filter ဖယ်ရှားခြင်းသဘောထားများကို မြင်ကြရသည်။

<img src="../../../translated_images/my/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*ဤပုံသည် 0 မှ 1 အထိ score ကွက်တိများနှင့် မလိုအပ်သော chunks များ ဖယ်ရှားသော 0.5 အနည်းဆုံး ထိန်းသိမ်းမှုကို ပြသည်။*

Score များသည် 0 မှ 1 အတွင်းရှိ-
- 0.7-1.0: အလွန်သက်ဆိုင်သည်၊ တိတိကျကျ ကိုက်ညီသည်
- 0.5-0.7: သက်ဆိုင်သည်၊ အကြောင်းအရာကောင်းမွန်သည်
- 0.5 အောက်: ဖယ်ရှားသည်၊ မထိန်းသိမ်းစရာ

စနစ်သည် အနည်းဆုံး ထိန်းသိမ်းမှုကို ကျော်လွန်သော chunks များကိုသာ ရှာဖွေထုတ်ယူသည်။

Embedding များသည် အဓိပ္ပါယ်များ သန့်ရှင်းစွာ အသားတင်cluster လုပ်နိုင်သော်လည်း အချို့မှလွဲမှားမှုများ ရှိနေလိမ့်မည်။ အောက်ပုံသည် embedding မှားယွင်းမှု သတ္တိများ ကို ပြသည် — အကြီးအကျယ် chunks များကြောင့် vector မရှင်းလင်းမှု၊ အပြီးအစီး context မပါ chunks များ၊ အဓိပ္ပာယ်မရှင်းလင်းသော စကားလုံးများကြောင့် အတိအကျ cluster မသိရှိနိုင်ခြင်းနှင့် ID၊ အပိုင်းနံပါတ်များစသဖြင့် တိကျသော ရှာဖွေမှု မ သက်ဆိုင်ခြင်းတို့ ဖြစ်သည်။

<img src="../../../translated_images/my/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*ဤပုံသည် embedding မှားယွင်းမှုများအား ဖော်ပြသည် — chunks များကြီး/သေးနိုင်မှတ်၊ အတိအကျ မသိရှိနိုင်သော terms များ၊ နှင့် တိတိကျကျ lookup မဖြစ်သော IDs စသဖြင့်။*

### In-Memory စုစည်းမှု

ဤ module သည် ရိုးရှင်းရေးအတွက် in-memory storage ကို အသုံးပြုသည်။ အပလီကေးရှင်းကို ပြန်စတင်လျှင်၊ တင်ထားသော စာရွက်များ ပြန်မရှိတော့ပါ။ ထုတ်လုပ်မှုစနစ်များတွင် Qdrant သို့မဟုတ် Azure AI Search ကဲ့သို့ persistent vector database များ အသုံးပြုသည်။

### Context Window စီမံခန့်ခွဲမှု

မော်ဒယ်တစ်ခုချင်းစီမှာ context window အများဆုံးရှိသည်။ စာရွက်ကြီးများမှ တစ်ချက်လုံး chunks များ ပါဝင်စေရန် မဖြစ်နိုင်ပါ။ စနစ်သည် ထိပ်ဆုံး N ထက်ပိုပြီး သက်ဆိုင်သော chunks များ (default ၅) ကို ရှာဖွေထုတ်ယူပြီး ကန့်သတ်ချက်အတွင်း ဖြစ်စေသည်နှင့် တိကျသော ဖြေကြားချက်ဖန်တီးရန် context လုံလောက်စေသည်။

## RAG အရေးကြီးသော အချိန်များ

RAG သည် အမြဲတမ်းမှန်ကန်သောနည်းလမ်း မဟုတ်ပါ။ အောက်တွင် ဖော်ပြထားသော ဆုံးဖြတ်ချက် လမ်းညွှန်က RAG သုံးသင့်ချိန်နှင့် ရိုးရှင်းနည်းလမ်းများ (prompt မှာ ပါဝင်စေခြင်း သို့မဟုတ် မော်ဒယ်ထဲbuilt-in အသိပညာ အသုံးပြုခြင်း) များ သင့်လျော်သောအချိန်ကို ခွဲခြားဖော်ပြသည်။

<img src="../../../translated_images/my/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*ဤပုံသည် RAG သုံးရမည့်အချိန်နှင့် ရိုးရှင်းနည်းလမ်းများ ချိတ်ဆက်အသုံးပြုသင့်သည့်အချိန်အား ဆုံးဖြတ်ချက် လမ်းညွှန်အဖြစ် ပြသည်။*

**RAG သုံးသင့်သောအချိန်များ:**
- ပိုင်ဆိုင်မှုရှိသော စာရွက်စာတမ်းများအတွက် မေးခွန်းများ ဖြေဆိုရာတွင်
- သတင်းအချက်အလက်များ ကြိမ်ကြိမ် ပြောင်းလဲမှုပေါ်မူတည်သောအခါ (မူဝါဒများ၊ စျေးနှုန်းများ၊ သတ်မှတ်ချက်များ)
- တိကျမှုသည် အရင်းအမြစ် သက်ဆိုင်မှု လိုအပ်သည့်အခါ
- စာသားအရွယ်အစား ကြီးပြီး တစ်ခုတည်းသော prompt ထဲသို့ ထည့်ပါမရသောအခါ
- အတည်ပြုနိုင်ပြီး အခြေခံထားသော ဖြေကြားချက်များ လိုအပ်သောအခါ

**RAG မသုံးသင့်သောအချိန်များ:**
- မေးခွန်းများသည် မော်ဒယ်အတွင်းရှိ အသိပညာနှင့်သာ ဖြေဆိုနိုင်သောအခါ
- အချိန်နှင့်တပြေးညီ data လိုအပ်သောအခါ (RAG သည် တင်ထားသော စာရွက်များအပေါ် မူတည်သည်)
- စာသား အရွယ်အစားငယ်၍ တိုက်ရိုက် prompt ထဲ ထည့်နိုင်သည့်အခါ

## နောက်ဆက်တွဲ အဆင့်များ

**နောက်တစ်ခု Module:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**လမ်းညွှန်:** [← ယခင်: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [ပင်မသို့ ပြန်သွားရန်](../README.md) | [နောက်တစ်ခု: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**အကြောင်းကြားချက်**  
ဤစာရွက်ကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် ဂရုပြု၍ တိကျမှုကိုကြိုးစားနေသော်လည်း၊ အလိုအလျောက်ဘာသာပြန်ချက်များတွင် အမှားများ သို့မဟုတ် မှန်ကန်မှုမရှိမှုများ ဖြစ်ပေါ်နိုင်ကြောင်း သတိပြုရန် လိုအပ်ပါသည်။ မူလစာရွက်ကို ထိုဘာသာစကားဖြင့် ဖြစ်သည့်အတိုင်းသာ တရားဝင်အချက်အလက် အရင်းအမြစ်အဖြစ် ယူဆသင့်ပါသည်။ အရေးပါတဲ့ အချက်အလက်များအတွက် လူကိုယ်တိုင် အတိုင်ပင်ခံ ဘာသာပြန်ခြင်းကို သတင်းပေးပါသည်။ ဤဘာသာပြန်ချက် အသုံးပြုမှုကြောင့် ဖြစ်ပေါ်လာသော နားမလည်မှုများ သို့မဟုတ် မှားယွင်းသော နားလည်မှုများအတွက် ကျွန်ုပ်တို့မှာ တာဝန်မရှိပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->