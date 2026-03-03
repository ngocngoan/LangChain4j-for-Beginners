# Module 03: RAG (रिट्रीवल-ऑगमेंटेड जनरेशन)

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

देखें यह लाइव सेशन जो बताता है कि इस मॉड्यूल के साथ कैसे शुरुआत करें:

<a href="https://www.youtube.com/watch?v=_olq75ZH_eY"><img src="https://img.youtube.com/vi/_olq75ZH_eY/maxresdefault.jpg" alt="RAG with LangChain4j - Live Session" width="800"/></a>

## What You'll Learn

पिछले मॉड्यूल्स में, आपने सीखा कि AI से बातचीत कैसे करें और अपने प्रॉम्प्ट्स को प्रभावी ढंग से कैसे संरचित करें। लेकिन एक मूलभूत सीमा है: भाषा मॉडल केवल वही जानते हैं जो उन्होंने प्रशिक्षण के दौरान सीखा है। वे आपकी कंपनी की नीतियों, आपके प्रोजेक्ट दस्तावेज़ या उन जानकारियों पर उत्तर नहीं दे सकते जिन पर वे प्रशिक्षित नहीं हुए।

RAG (रिट्रीवल-ऑगमेंटेड जनरेशन) इस समस्या को हल करता है। मॉडल को आपकी जानकारी सिखाने की कोशिश करने के बजाय (जो महंगा और व्यावहारिक नहीं है), आप इसे अपने दस्तावेज़ों के माध्यम से खोज करने की क्षमता देते हैं। जब कोई प्रश्न पूछता है, तो सिस्टम प्रासंगिक जानकारी ढूंढता है और उसे प्रॉम्प्ट में शामिल करता है। मॉडल फिर उस प्राप्त संदर्भ के आधार पर उत्तर देता है।

RAG को एक संदर्भ पुस्तकालय देने के रूप में सोचें। जब आप प्रश्न पूछते हैं, तो सिस्टम:

1. **यूज़र क्वेरी** - आप सवाल पूछते हैं
2. **एम्बेडिंग** - आपके प्रश्न को एक वेक्टर में परिवर्तित करता है
3. **वेक्टर खोज** - समान दस्तावेज़ के टुकड़े खोजता है
4. **संदर्भ संयोजन** - प्रॉम्प्ट में प्रासंगिक टुकड़ों को जोड़ता है
5. **प्रतिक्रिया** - LLM संदर्भ के आधार पर उत्तर उत्पन्न करता है

यह मॉडल के उत्तरों को आपके वास्तविक डेटा पर आधारित करता है बजाय इसके कि वह केवल अपने प्रशिक्षण ज्ञान पर निर्भर करे या उत्तर गढ़े।

## Prerequisites

- पूरा किया हुआ [Module 00 - Quick Start](../00-quick-start/README.md) (इस मॉड्यूल में बाद में संदर्भित Easy RAG उदाहरण के लिए)
- पूरा किया हुआ [Module 01 - Introduction](../01-introduction/README.md) (Azure OpenAI संसाधन तैनात, जिसमें `text-embedding-3-small` एम्बेडिंग मॉडल शामिल है)
- Azure क्रेडेंशियल्स के साथ रूट डायरेक्टरी में `.env` फ़ाइल (Module 01 में `azd up` द्वारा बनाई गई)

> **Note:** यदि आपने Module 01 पूरा नहीं किया है, तो वहां तैनाती निर्देशों का पालन करें। `azd up` कमांड GPT चैट मॉडल और इस मॉड्यूल द्वारा उपयोग किए जाने वाले एम्बेडिंग मॉडल दोनों को तैनात करता है।

## Understanding RAG

नीचे चित्र में मूल अवधारणा दर्शाई गई है: मॉडल के प्रशिक्षण डेटा पर केवल निर्भर रहने के बजाय, RAG प्रत्येक उत्तर उत्पन्न करने से पहले इसे आपकी दस्तावेजों की एक संदर्भ पुस्तकालय देता है।

<img src="../../../translated_images/hi/what-is-rag.1f9005d44b07f2d8.webp" alt="What is RAG" width="800"/>

*यह चित्र एक मानक LLM (जो प्रशिक्षण डेटा से अनुमान लगाता है) और एक RAG-संवर्धित LLM (जो पहले आपकी दस्तावेजों से सलाह-मशविरा करता है) के बीच का अंतर दिखाता है।*

यहाँ टुकड़े अंत-से-अंत कैसे जुड़े हैं। उपयोगकर्ता का प्रश्न चार चरणों से होकर गुजरता है — एम्बेडिंग, वेक्टर खोज, संदर्भ संयोजन, और उत्तर जनरेशन — प्रत्येक पिछले पर आधारित:

<img src="../../../translated_images/hi/rag-architecture.ccb53b71a6ce407f.webp" alt="RAG Architecture" width="800"/>

*यह चित्र अंत-से-अंत RAG पाइपलाइन दिखाता है — एक उपयोगकर्ता क्वेरी एम्बेडिंग, वेक्टर खोज, संदर्भ संयोजन, और उत्तर उत्पादन प्रक्रिया से गुजरती है।*

इस मॉड्यूल के बाकी हिस्से में प्रत्येक चरण को विस्तार से दिखाया गया है, जिसमें कोड है जिसे आप चला और संशोधित कर सकते हैं।

### Which RAG Approach Does This Tutorial Use?

LangChain4j RAG को लागू करने के तीन तरीके प्रदान करता है, प्रत्येक अलग स्तर की अमूर्तता के साथ। नीचे दिए गए चित्र में ये एक-दूसरे के साथ तुलना की गई हैं:

<img src="../../../translated_images/hi/rag-approaches.5b97fdcc626f1447.webp" alt="Three RAG Approaches in LangChain4j" width="800"/>

*यह चित्र तीन LangChain4j RAG दृष्टिकोणों — Easy, Native, और Advanced — को उनके मुख्य घटकों और उपयोग के समय के साथ तुलना करता है।*

| Approach | What It Does | Trade-off |
|---|---|---|
| **Easy RAG** | `AiServices` और `ContentRetriever` के माध्यम से सब कुछ स्वचालित रूप से चलाता है। आप एक इंटरफ़ेस एनोटेट करते हैं, एक रिट्रीवर संलग्न करते हैं, और LangChain4j एम्बेडिंग, खोज, और प्रॉम्प्ट संयोजन को संभालता है। | न्यूनतम कोड, लेकिन आप हर चरण नहीं देख पाते। |
| **Native RAG** | आप स्वयं एम्बेडिंग मॉडल कॉल करते हैं, स्टोर में खोज करते हैं, प्रॉम्प्ट बनाते हैं, और उत्तर उत्पन्न करते हैं — हर चरण स्पष्ट रूप से। | अधिक कोड, लेकिन हर चरण स्पष्ट और संशोधित करने योग्य है। |
| **Advanced RAG** | प्लगनेबल क्वेरी ट्रांसफॉर्मर्स, राउटर्स, री-रैंकर्स और कंटेंट इंजेक्टर के साथ प्रोडक्शन-ग्रेड पाइपलाइनों के लिए `RetrievalAugmentor` फ्रेमवर्क का उपयोग करता है। | अधिकतम लचीलापन, लेकिन काफी अधिक जटिलता। |

**यह ट्यूटोरियल Native दृष्टिकोण का उपयोग करता है।** RAG पाइपलाइन के प्रत्येक चरण — क्वेरी का एम्बेडिंग, वेक्टर स्टोर में खोज, संदर्भ संयोजन, और उत्तर उत्पन्न करना — [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) में स्पष्ट रूप से लिखा गया है। यह जानबूझकर किया गया है: एक सीखने वाले संसाधन के रूप में, यह ज़्यादा महत्वपूर्ण है कि आप हर चरण देखें और समझें बजाय इसके कि कोड न्यूनतम हो। एक बार जब आप समझ जाएं कि टुकड़े कैसे फिट होते हैं, तो आप द्रुत प्रोटोटाइप के लिए Easy RAG या प्रोडक्शन सिस्टम के लिए Advanced RAG पर जा सकते हैं।

> **💡 Easy RAG पहले देख चुके हैं?** [Quick Start module](../00-quick-start/README.md) में एक Document Q&A उदाहरण ([`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)) शामिल है जो Easy RAG दृष्टिकोण का उपयोग करता है — LangChain4j स्वचालित रूप से एम्बेडिंग, खोज, और प्रॉम्प्ट संयोजन को संभालता है। यह मॉड्यूल उस पाइपलाइन को खोलता है ताकि आप देख सकें और हर चरण को नियंत्रित कर सकें।

नीचे चित्र उस Quick Start उदाहरण की Easy RAG पाइपलाइन दिखाता है। ध्यान दें कि कैसे `AiServices` और `EmbeddingStoreContentRetriever` सारी जटिलता को छुपाते हैं — आप दस्तावेज़ लोड करते हैं, एक रिट्रीवर संलग्न करते हैं, और उत्तर प्राप्त करते हैं। इस मॉड्यूल में Native दृष्टिकोण उन छुपे हुए चरणों को खोलता है:

<img src="../../../translated_images/hi/easy-rag-pipeline.2e1602e2ad2ded42.webp" alt="Easy RAG Pipeline - LangChain4j" width="800"/>

*यह चित्र `SimpleReaderDemo.java` से Easy RAG पाइपलाइन दिखाता है। इसे इस मॉड्यूल के Native दृष्टिकोण से तुलना करें: Easy RAG एम्बेडिंग, रिट्रीवल, और प्रॉम्प्ट संयोजन को `AiServices` और `ContentRetriever` के पीछे छुपा देता है — आप एक दस्तावेज़ लोड करते हैं, रिट्रीवर संलग्न करते हैं, और उत्तर प्राप्त करते हैं। इस मॉड्यूल में Native दृष्टिकोण उस पाइपलाइन को खोलता है ताकि आप हर चरण (एम्बेड, खोजें, संदर्भ संयोजन करें, उत्तर उत्पन्न करें) स्वयं कॉल करें, जिससे पूरी दृश्यता और नियंत्रण मिलता है।*

## How It Works

इस मॉड्यूल की RAG पाइपलाइन में चार चरण होते हैं जो हर बार उपयोगकर्ता प्रश्न पूछने पर क्रमशः चलते हैं। सबसे पहले, अपलोड किया हुआ दस्तावेज़ **पार्स और चंक** किया जाता है ताकि प्रबंधनीय टुकड़े बन सकें। फिर उन टुकड़ों को **वेक्टर एम्बेडिंग** में बदला जाता है और संग्रहीत किया जाता है ताकि उनकी गणितीय तुलना की जा सके। जब कोई क्वेरी आती है, तो सिस्टम एक **सिमेंटिक खोज** करता है ताकि सबसे प्रासंगिक टुकड़े मिल सकें, और अंत में इन्हें संदर्भ के रूप में LLM को पास करता है ताकि **उत्तर उत्पन्न किया जाए**। नीचे के अनुभागों में वास्तविक कोड और आरेखों के साथ प्रत्येक चरण के बारे में बताया गया है। पहले चरण को देखें।

### Document Processing

[DocumentService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java)

जब आप कोई दस्तावेज़ अपलोड करते हैं, तो सिस्टम उसे पार्स करता है (PDF या साधारण टेक्स्ट), फ़ाइलनाम जैसी मेटाडेटा संलग्न करता है, और फिर उसे टुकड़ों में तोड़ता है — छोटे भाग जो मॉडल के संदर्भ विंडो में आराम से फिट हो सकें। ये टुकड़े थोड़े ओवरलैप होते हैं ताकि सीमाओं पर संदर्भ खो न जाए।

```java
// अपलोड की गई फ़ाइल को पार्स करें और इसे LangChain4j दस्तावेज़ में लपेटें
Document document = Document.from(content, metadata);

// 300-टोकन के टुकड़ों में विभाजित करें जिसमें 30-टोकन का ओवरलैप हो
DocumentSplitter splitter = DocumentSplitters
    .recursive(300, 30);

List<TextSegment> segments = splitter.split(document);
```
  
नीचे दिया गया चित्र इसे विज़ुअली दिखाता है। ध्यान दें कि हर टुकड़ा अपने पड़ोसियों के साथ कुछ टोकन साझा करता है — 30 टोकन का ओवरलैप सुनिश्चित करता है कि कोई महत्वपूर्ण संदर्भ बीच में न खो जाए:

<img src="../../../translated_images/hi/document-chunking.a5df1dd1383431ed.webp" alt="Document Chunking" width="800"/>

*यह चित्र दिखाता है कि कैसे 300-टोकन के टुकड़ों में 30-टोकन ओवरलैप के साथ दस्तावेज़ को विभाजित किया जाता है, जिससे टुकड़ों की सीमाओं पर संदर्भ संरक्षित रहता है।*

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ कोशिश करें:** Open [`DocumentService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/DocumentService.java) और पूछें:
> - "LangChain4j दस्तावेज़ों को टुकड़ों में कैसे विभाजित करता है और ओवरलैप क्यों महत्वपूर्ण है?"
> - "विभिन्न दस्तावेज़ प्रकारों के लिए आदर्श टुकड़ा आकार क्या है और क्यों?"
> - "मैं कई भाषाओं या विशेष स्वरूपण वाले दस्तावेज़ों को कैसे संभाल सकता हूँ?"

### Creating Embeddings

[LangChainRagConfig.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/config/LangChainRagConfig.java)

हर टुकड़े को एक संख्यात्मक प्रतिनिधित्व में बदला जाता है जिसे एम्बेडिंग कहा जाता है — मूल रूप से एक अर्थ-से-संख्या कनवर्टर। एम्बेडिंग मॉडल "बुद्धिमान" नहीं होता जैसे कि चैट मॉडल होता है; यह आदेशों का पालन नहीं कर सकता, तर्क नहीं कर सकता, या प्रश्नों का उत्तर नहीं दे सकता। जो यह कर सकता है वह टेक्स्ट को गणितीय स्थान में मैप करना है जहाँ समान अर्थ नज़दीक होते हैं — जैसे "कार" के पास "ऑटोमोबाइल", "रिफंड पॉलिसी" के पास "मेरे पैसे वापस" के वेक्टर होते हैं। चैट मॉडल को आप एक व्यक्त‍ि समझें जिससे बात करें; एक एम्बेडिंग मॉडल एक अत्यंत अच्छा फाइलिंग सिस्टम है।

नीचे चित्र इस अवधारणा को दिखाता है — टेक्स्ट इनपुट होता है, संख्यात्मक वेक्टर निकलते हैं, और समान अर्थ के वेक्टर पास-पास होते हैं:

<img src="../../../translated_images/hi/embedding-model-concept.90760790c336a705.webp" alt="Embedding Model Concept" width="800"/>

*यह चित्र दिखाता है कि कैसे एक एम्बेडिंग मॉडल टेक्स्ट को संख्यात्मक वेक्टर्स में परिवर्तित करता है, जिससे समान अर्थ जैसे "कार" और "ऑटोमोबाइल" वेक्टर स्पेस में एक-दूसरे के निकट स्थानित होते हैं।*

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
  
नीचे क्लास डायरैक्ट दिखाता है कि RAG पाइपलाइन में दो अलग प्रवाह होते हैं और LangChain4j की कौन-कौन सी क्लासेज़ उन्हें लागू करती हैं। **इंजेशन फ्लो** (जो अपलोड के समय एक बार चलता है) दस्तावेज़ को विभाजित करता है, टुकड़ों को एम्बेड करता है, और `.addAll()` के माध्यम से उन्हें स्टोर करता है। **क्वेरी फ्लो** (जो हर बार उपयोगकर्ता प्रश्न पूछे जाता है) क्वेरी को एम्बेड करता है, स्टोर में `.search()` करता है, और मेल खाए हुए संदर्भ को चैट मॉडल को भेजता है। दोनों फ्लो एक साझा `EmbeddingStore<TextSegment>` इंटरफेस पर मिलते हैं:

<img src="../../../translated_images/hi/rag-langchain4j-classes.bbf3aa9077ab443d.webp" alt="LangChain4j RAG Classes" width="800"/>

*यह चित्र RAG पाइपलाइन में दो प्रवाह — इंजेशन और क्वेरी — दिखाता है और यह कि वे साझा EmbeddingStore के माध्यम से कैसे जुड़े हुए हैं।*

एक बार एम्बेडिंग स्टोर हो जाने पर समान सामग्री स्वाभाविक रूप से वेक्टर स्पेस में क्लस्टर होती है। नीचे विज़ुअलाइज़ेशन दिखाता है कि संबंधित विषयों वाले दस्तावेज़ निकट बिंदुओं के रूप में समूहित होते हैं, जो सिमेंटिक खोज को संभव बनाता है:

<img src="../../../translated_images/hi/vector-embeddings.2ef7bdddac79a327.webp" alt="Vector Embeddings Space" width="800"/>

*यह विज़ुअलाइज़ेशन दिखाता है कि कैसे संबंधित दस्तावेज़ 3D वेक्टर स्पेस में समूहित होते हैं, जैसे टेक्निकल डॉक्स, बिजनेस रूल्स और FAQs अलग-अलग समूह बनाते हैं।*

जब उपयोगकर्ता खोज करता है, तो सिस्टम चार चरणों का पालन करता है: दस्तावेजों को एक बार एम्बेड करता है, हर खोज पर क्वेरी को एम्बेड करता है, क्वेरी वेक्टर की तुलना कोसाइन सिमिलैरिटी के साथ सभी संग्रहीत वेक्टरों से करता है, और टॉप-के उच्चतम स्कोर वाले टुकड़े लौटाता है। नीचे चित्र हर चरण और LangChain4j क्लासेज़ को दिखाता है:

<img src="../../../translated_images/hi/embedding-search-steps.f54c907b3c5b4332.webp" alt="Embedding Search Steps" width="800"/>

*यह चित्र चार-चरण एम्बेडिंग खोज प्रक्रिया दिखाता है: दस्तावेज़ एम्बेड करें, क्वेरी एम्बेड करें, कोसाइन सिमिलैरिटी से वेक्टर तुलना करें, और टॉप-के परिणाम लौटाएं।*

### Semantic Search

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

जब आप सवाल पूछते हैं, तो आपका सवाल भी एम्बेडिंग बन जाता है। सिस्टम आपके सवाल के एम्बेडिंग की तुलना दस्तावेज़ के सभी टुकड़ों के एम्बेडिंग से करता है। यह उन टुकड़ों को खोजता है जिनका अर्थ सबसे अधिक समान होता है - केवल मेल खाने वाले कीवर्ड नहीं, बल्कि वास्तविक सिमेंटिक समानता।

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
  
नीचे चित्र सिमेंटिक खोज और पारंपरिक कीवर्ड खोज के बीच तुलना करता है। "vehicle" के लिए कीवर्ड खोज "cars and trucks" वाले टुकड़े को छोड़ देती है, लेकिन सिमेंटिक खोज समझती है कि उनका अर्थ समान है और उसे उच्च श्रेणी के मेल के रूप में लौटाती है:

<img src="../../../translated_images/hi/semantic-search.6b790f21c86b849d.webp" alt="Semantic Search" width="800"/>

*यह चित्र कीवर्ड-आधारित खोज की तुलना सिमेंटिक खोज से करता है, दिखाता है कि सिमेंटिक खोज कैसे तब भी सैद्धांतिक रूप से संबंधित सामग्री पुनः प्राप्त करती है जब सटीक कीवर्ड भिन्न हों।*
Under the hood, similarity is measured using cosine similarity — essentially asking "are these two arrows pointing in the same direction?" Two chunks can use completely different words, but if they mean the same thing their vectors point the same way and score close to 1.0:

<img src="../../../translated_images/hi/cosine-similarity.9baeaf3fc3336abb.webp" alt="Cosine Similarity" width="800"/>

*This diagram illustrates cosine similarity as the angle between embedding vectors — more aligned vectors score closer to 1.0, indicating higher semantic similarity.*

> **🤖 Try with [GitHub Copilot](https://github.com/features/copilot) Chat:** Open [`RagService.java`](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java) and ask:
> - "How does similarity search work with embeddings and what determines the score?"
> - "What similarity threshold should I use and how does it affect results?"
> - "How do I handle cases where no relevant documents are found?"

### Answer Generation

[RagService.java](../../../03-rag/src/main/java/com/example/langchain4j/rag/service/RagService.java)

The most relevant chunks are assembled into a structured prompt that includes explicit instructions, the retrieved context, and the user's question. The model reads those specific chunks and answers based on that information — it can only use what's in front of it, which prevents hallucination.

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

The diagram below shows this assembly in action — the top-scoring chunks from the search step are injected into the prompt template, and the `OpenAiOfficialChatModel` generates a grounded answer:

<img src="../../../translated_images/hi/context-assembly.7e6dd60c31f95978.webp" alt="Context Assembly" width="800"/>

*This diagram shows how the top-scoring chunks are assembled into a structured prompt, allowing the model to generate a grounded answer from your data.*

## Run the Application

**Verify deployment:**

Ensure the `.env` file exists in the root directory with Azure credentials (created during Module 01). Run this from the module directory (`03-rag/`):

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दिखाना चाहिए
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दिखाना चाहिए
```

**Start the application:**

> **Note:** If you already started all applications using `./start-all.sh` from the root directory (as described in Module 01), this module is already running on port 8081. You can skip the start commands below and go directly to http://localhost:8081.

**Option 1: Using Spring Boot Dashboard (Recommended for VS Code users)**

The dev container includes the Spring Boot Dashboard extension, which provides a visual interface to manage all Spring Boot applications. You can find it in the Activity Bar on the left side of VS Code (look for the Spring Boot icon).

From the Spring Boot Dashboard, you can:
- See all available Spring Boot applications in the workspace
- Start/stop applications with a single click
- View application logs in real-time
- Monitor application status

Simply click the play button next to "rag" to start this module, or start all modules at once.

<img src="../../../translated_images/hi/dashboard.fbe6e28bf4267ffe.webp" alt="Spring Boot Dashboard" width="400"/>

*This screenshot shows the Spring Boot Dashboard in VS Code, where you can start, stop, and monitor applications visually.*

**Option 2: Using shell scripts**

Start all web applications (modules 01-04):

**Bash:**
```bash
cd ..  # मूल निर्देशिका से
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # रूट निर्देशिका से
.\start-all.ps1
```

Or start just this module:

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

Both scripts automatically load environment variables from the root `.env` file and will build the JARs if they don't exist.

> **Note:** If you prefer to build all modules manually before starting:
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

Open http://localhost:8081 in your browser.

**To stop:**

**Bash:**
```bash
./stop.sh  # केवल यह मॉड्यूल
# या
cd .. && ./stop-all.sh  # सभी मॉड्यूल्स
```

**PowerShell:**
```powershell
.\stop.ps1  # केवल यह मॉड्यूल
# या
cd ..; .\stop-all.ps1  # सभी मॉड्यूल
```

## Using the Application

The application provides a web interface for document upload and questioning.

<a href="images/rag-homepage.png"><img src="../../../translated_images/hi/rag-homepage.d90eb5ce1b3caa94.webp" alt="RAG Application Interface" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*This screenshot shows the RAG application interface where you upload documents and ask questions.*

### Upload a Document

Start by uploading a document - TXT files work best for testing. A `sample-document.txt` is provided in this directory that contains information about LangChain4j features, RAG implementation, and best practices - perfect for testing the system. 

The system processes your document, breaks it into chunks, and creates embeddings for each chunk. This happens automatically when you upload.

### Ask Questions

Now ask specific questions about the document content. Try something factual that's clearly stated in the document. The system searches for relevant chunks, includes them in the prompt, and generates an answer.

### Check Source References

Notice each answer includes source references with similarity scores. These scores (0 to 1) show how relevant each chunk was to your question. Higher scores mean better matches. This lets you verify the answer against the source material.

<a href="images/rag-query-results.png"><img src="../../../translated_images/hi/rag-query-results.6d69fcec5397f355.webp" alt="RAG Query Results" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/></a>

*This screenshot shows query results with the generated answer, source references, and relevance scores for each retrieved chunk.*

### Experiment with Questions

Try different types of questions:
- Specific facts: "What is the main topic?"
- Comparisons: "What's the difference between X and Y?"
- Summaries: "Summarize the key points about Z"

Watch how the relevance scores change based on how well your question matches document content.

## Key Concepts

### Chunking Strategy

Documents are split into 300-token chunks with 30 tokens of overlap. This balance ensures each chunk has enough context to be meaningful while staying small enough to include multiple chunks in a prompt.

### Similarity Scores

Every retrieved chunk comes with a similarity score between 0 and 1 that indicates how closely it matches the user's question. The diagram below visualizes the score ranges and how the system uses them to filter results:

<img src="../../../translated_images/hi/similarity-scores.b0716aa911abf7f0.webp" alt="Similarity Scores" width="800"/>

*This diagram shows score ranges from 0 to 1, with a minimum threshold of 0.5 that filters out irrelevant chunks.*

Scores range from 0 to 1:
- 0.7-1.0: Highly relevant, exact match
- 0.5-0.7: Relevant, good context
- Below 0.5: Filtered out, too dissimilar

The system only retrieves chunks above the minimum threshold to ensure quality.

Embeddings work well when meaning clusters cleanly, but they have blind spots. The diagram below shows the common failure modes — chunks that are too large produce muddy vectors, chunks that are too small lack context, ambiguous terms point to multiple clusters, and exact-match lookups (IDs, part numbers) don't work with embeddings at all:

<img src="../../../translated_images/hi/embedding-failure-modes.b2bcb901d8970fc0.webp" alt="Embedding Failure Modes" width="800"/>

*This diagram shows common embedding failure modes: chunks too large, chunks too small, ambiguous terms that point to multiple clusters, and exact-match lookups like IDs.*

### In-Memory Storage

This module uses in-memory storage for simplicity. When you restart the application, uploaded documents are lost. Production systems use persistent vector databases like Qdrant or Azure AI Search.

### Context Window Management

Each model has a maximum context window. You can't include every chunk from a large document. The system retrieves the top N most relevant chunks (default 5) to stay within limits while providing enough context for accurate answers.

## When RAG Matters

RAG isn't always the right approach. The decision guide below helps you determine when RAG adds value versus when simpler approaches — like including content directly in the prompt or relying on the model's built-in knowledge — are sufficient:

<img src="../../../translated_images/hi/when-to-use-rag.1016223f6fea26bc.webp" alt="When to Use RAG" width="800"/>

*This diagram shows a decision guide for when RAG adds value versus when simpler approaches are sufficient.*

## Next Steps

**Next Module:** [04-tools - AI Agents with Tools](../04-tools/README.md)

---

**Navigation:** [← Previous: Module 02 - Prompt Engineering](../02-prompt-engineering/README.md) | [Back to Main](../README.md) | [Next: Module 04 - Tools →](../04-tools/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
इस दस्तावेज़ का अनुवाद एआई अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके किया गया है। हम सटीकता के लिए प्रयासरत हैं, लेकिन कृपया ध्यान रखें कि स्वचालित अनुवादों में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल दस्तावेज़ अपनी मातृभाषा में ही अधिकारिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए पेशेवर मानव अनुवाद की सलाह दी जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या भ्रांतियों के लिए हम जिम्मेदार नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->