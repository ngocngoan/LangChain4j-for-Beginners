# Module 00: क्विक स्टार्ट

## Table of Contents

- [परिचय](../../../00-quick-start)
- [LangChain4j क्या है?](../../../00-quick-start)
- [LangChain4j निर्भरताएँ](../../../00-quick-start)
- [पूर्वापेक्षाएँ](../../../00-quick-start)
- [सेटअप](../../../00-quick-start)
  - [1. अपना GitHub टोकन प्राप्त करें](../../../00-quick-start)
  - [2. अपना टोकन सेट करें](../../../00-quick-start)
- [उदाहरण चलाएँ](../../../00-quick-start)
  - [1. बेसिक चैट](../../../00-quick-start)
  - [2. प्रॉम्प्ट पैटर्न](../../../00-quick-start)
  - [3. फंक्शन कॉलिंग](../../../00-quick-start)
  - [4. दस्तावेज़ प्रश्नोत्तर (आसान RAG)](../../../00-quick-start)
  - [5. ज़िम्मेदार AI](../../../00-quick-start)
- [प्रत्येक उदाहरण क्या दिखाता है](../../../00-quick-start)
- [अगले कदम](../../../00-quick-start)
- [समस्या निवारण](../../../00-quick-start)

## परिचय

यह क्विकस्टार्ट आपको LangChain4j के साथ यथाशीघ्र शुरूआत करने के लिए बनाया गया है। यह LangChain4j और GitHub मॉडल के साथ AI एप्लिकेशन बनाने के सबसे बुनियादी पहलुओं को कवर करता है। अगले मॉड्यूल में आप Azure OpenAI और GPT-5.2 पर स्विच करेंगे और प्रत्येक अवधारणा में गहराई से जाएँगे।

## LangChain4j क्या है?

LangChain4j एक Java लाइब्रेरी है जो AI-पावर्ड एप्लिकेशन बनाने को सरल बनाती है। HTTP क्लाइंट और JSON पार्सिंग से निपटने के बजाय, आप साफ़-सुथरे Java API के साथ काम करते हैं।

LangChain में "चेन" का मतलब है कई घटकों को जोड़ना - आप प्रॉम्प्ट को मॉडल, फिर पार्सर से जोड़ सकते हैं, या कई AI कॉल्स को इस तरह जोड सकते हैं कि एक आउटपुट अगले इनपुट में जाए। यह क्विक स्टार्ट बुनियादी बातों पर केंद्रित है इससे पहले कि हम अधिक जटिल चेन पर जाएँ।

<img src="../../../translated_images/hi/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j में घटकों का चेनिंग - शक्तिशाली AI वर्कफ़्लोज़ बनाने के लिए बिल्डिंग ब्लॉक्स जुड़ रहे हैं*

हम तीन मुख्य घटकों का उपयोग करेंगे:

**ChatModel** - AI मॉडल इंटरैक्शन का इंटरफ़ेस। `model.chat("prompt")` कॉल करें और प्रतिक्रिया प्राप्त करें। हम `OpenAiOfficialChatModel` का उपयोग करते हैं जो OpenAI-संगत एंडपॉइंट्स जैसे GitHub मॉडल के साथ काम करता है।

**AiServices** - प्रकार-सुरक्षित AI सेवा इंटरफेस बनाता है। मेथड्स परिभाषित करें, उन्हें `@Tool` से एनोटेट करें, और LangChain4j ऑर्केस्ट्रेशन संभालता है। AI आवश्यकता होने पर आपके Java मेथड्स को स्वचालित रूप से कॉल करता है।

**MessageWindowChatMemory** - बातचीत का इतिहास बनाए रखता है। इसके बिना, प्रत्येक अनुरोध स्वतंत्र होता है। इसके साथ, AI पहले के संदेश याद रखता है और कई टर्न्स में संदर्भ बनाए रखता है।

<img src="../../../translated_images/hi/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j आर्किटेक्चर - मुख्य घटक एक साथ मिलकर आपके AI एप्लिकेशन को चलाते हैं*

## LangChain4j निर्भरताएँ

यह क्विकस्टार्ट तीन Maven निर्भरताओं का उपयोग करता है [`pom.xml`](../../../00-quick-start/pom.xml) में:

```xml
<!-- Core LangChain4j library -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- OpenAI integration (works with GitHub Models) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>

<!-- Easy RAG: automatic splitting, embedding, and retrieval -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

`langchain4j-open-ai-official` मॉड्यूल `OpenAiOfficialChatModel` क्लास प्रदान करता है जो OpenAI-संगत API से जुड़ता है। GitHub मॉडल्स इसी API फॉर्मेट का उपयोग करते हैं, इसलिए किसी विशेष अडैप्टर की आवश्यकता नहीं - बस बेस URL को `https://models.github.ai/inference` पर सेट करें।

`langchain4j-easy-rag` मॉड्यूल दस्तावेज़ों को स्वचालित रूप से विभाजित, एम्बेड और पुनः प्राप्त करने की सुविधा देता है ताकि आप बिना मैनुअल कॉन्फ़िगरेशन के RAG एप्लिकेशन बना सकें।

## पूर्वापेक्षाएँ

**डेव कंटेनर का उपयोग कर रहे हैं?** Java और Maven पहले से इंस्टॉल हैं। आपको केवल GitHub पर्सनल एक्सेस टोकन चाहिए।

**लोकल विकास:**
- Java 21+, Maven 3.9+
- GitHub पर्सनल एक्सेस टोकन (नीचे निर्देश)

> **नोट:** इस मॉड्यूल में GitHub मॉडल से `gpt-4.1-nano` का उपयोग होता है। कोड में मॉडल नाम को संशोधित न करें - यह GitHub के उपलब्ध मॉडलों के साथ काम करने के लिए कॉन्फ़िगर किया गया है।

## सेटअप

### 1. अपना GitHub टोकन प्राप्त करें

1. जाएँ [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens)
2. "Generate new token" पर क्लिक करें
3. एक वर्णनात्मक नाम सेट करें (जैसे "LangChain4j Demo")
4. समय समाप्ति सेट करें (7 दिन अनुशंसित)
5. "Account permissions" में "Models" खोजें और इसे "Read-only" पर सेट करें
6. "Generate token" क्लिक करें
7. टोकन कॉपी करें और सुरक्षित रखें — आप इसे फिर नहीं देख पाएंगे

### 2. अपना टोकन सेट करें

**विकल्प 1: VS Code का उपयोग करके (अनुशंसित)**

अगर आप VS Code उपयोग कर रहे हैं, तो अपनी टोकन प्रोजेक्ट रूट में `.env` फ़ाइल में जोड़ें:

अगर `.env` फ़ाइल मौजूद नहीं है, तो `.env.example` को `.env` में कॉपी करें या प्रोजेक्ट रूट में नई `.env` फ़ाइल बनाएं।

**`.env` फ़ाइल का उदाहरण:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env में
GITHUB_TOKEN=your_token_here
```

फिर आप आसानी से Explorer में किसी भी डेमो फ़ाइल (जैसे `BasicChatDemo.java`) पर राइट-क्लिक कर **"Run Java"** चुन सकते हैं या रन और डिबग पैनल से लॉन्च कॉन्फ़िगरेशन का उपयोग कर सकते हैं।

**विकल्प 2: टर्मिनल का उपयोग करके**

टोकन को पर्यावरण चर के रूप में सेट करें:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## उदाहरण चलाएँ

**VS Code का उपयोग करते हुए:** Explorer में किसी भी डेमो फ़ाइल पर राइट-क्लिक करके **"Run Java"** चुनें, या रन और डिबग पैनल से लॉन्च कॉन्फ़िगरेशन का उपयोग करें (पहले सुनिश्चित करें कि आपने अपना टोकन `.env` फ़ाइल में जोड़ दिया है)।

**Maven का उपयोग करते हुए:** वैकल्पिक रूप से, आप कमांड लाइन से चला सकते हैं:

### 1. बेसिक चैट

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. प्रॉम्प्ट पैटर्न

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

यह ज़ीरो-शॉट, फ्यू-शॉट, चेन-ऑफ-थॉट और रोल-आधारित प्रॉम्प्टिंग दिखाता है।

### 3. फंक्शन कॉलिंग

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI आपकी Java विधियों को आवश्यकतानुसार स्वचालित कॉल करता है।

### 4. दस्तावेज़ प्रश्नोत्तर (आसान RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

आसानी से RAG के साथ अपने दस्तावेज़ों के बारे में प्रश्न पूछें जिसमें स्वचालित एम्बेडिंग और पुनः प्राप्ति होती है।

### 5. ज़िम्मेदार AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

देखें कि AI सुरक्षा फ़िल्टर हानिकारक सामग्री को कैसे ब्लॉक करते हैं।

## प्रत्येक उदाहरण क्या दिखाता है

**बेसिक चैट** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

यहाँ से शुरू करें ताकि LangChain4j को सबसे सरल रूप में देखें। आप एक `OpenAiOfficialChatModel` बनाएंगे, `.chat()` के साथ एक प्रॉम्प्ट भेजेंगे, और प्रतिक्रिया प्राप्त करेंगे। यह आधार दिखाता है: कस्टम एंडपॉइंट और API कुंजी के साथ मॉडल को कैसे इनिशियलाइज़ करें। एक बार जब आप इस पैटर्न को समझ लेते हैं, तो बाकी सब कुछ इसी पर बनता है।

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ कोशिश करें:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) खोलें और पूछें:
> - "मैं इस कोड में GitHub Models से Azure OpenAI पर कैसे स्विच करूं?"
> - "OpenAiOfficialChatModel.builder() में मैं कौन से अन्य पैरामीटर कॉन्फ़िगर कर सकता हूँ?"
> - "पूरी प्रतिक्रिया की प्रतीक्षा करने के बजाय स्ट्रीमिंग प्रतिक्रिया कैसे जोड़ूं?"

**प्रॉम्प्ट इंजीनियरिंग** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

अब जब आप मॉडल से बात करना जानते हैं, चलिए देखते हैं कि आप उससे क्या कहते हैं। यह डेमो उसी मॉडल सेटअप का उपयोग करता है लेकिन पाँच अलग-अलग प्रॉम्प्टिंग पैटर्न दिखाता है। सीधे निर्देशों के लिए ज़ीरो-शॉट प्रॉम्प्ट, उदाहरणों से सीखने वाले फ्यू-शॉट प्रॉम्प्ट, तर्क के चरण दिखाने वाले चेन-ऑफ-थॉट प्रॉम्प्ट, और संदर्भ सेट करने वाले रोल-आधारित प्रॉम्प्ट ट्राई करें। आप देखेंगे कि आपकी फ्रेमिंग के आधार पर एक ही मॉडल कैसे काफी अलग परिणाम देता है।

यह डेमो प्रॉम्प्ट टेम्प्लेट्स को भी दिखाता है, जो वेरिएबल्स के साथ पुन: प्रयोज्य प्रॉम्प्ट बनाने का एक शक्तिशाली तरीका है। नीचे का उदाहरण LangChain4j `PromptTemplate` का उपयोग कर वेरिएबल्स भरने वाला प्रॉम्प्ट दिखाता है। AI प्रदान किए गए गंतव्य और गतिविधि के आधार पर जवाब देगा।

```java
PromptTemplate template = PromptTemplate.from(
    "What's the best time to visit {{destination}} for {{activity}}?"
);

Prompt prompt = template.apply(Map.of(
    "destination", "Paris",
    "activity", "sightseeing"
));

String response = model.chat(prompt.text());
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ कोशिश करें:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) खोलें और पूछें:
> - "ज़ीरो-शॉट और फ्यू-शॉट प्रॉम्प्टिंग में क्या अंतर है, और मुझे कब कौन सा उपयोग करना चाहिए?"
> - "तापमान पैरामीटर मॉडल की प्रतिक्रियाओं को कैसे प्रभावित करता है?"
> - "प्रोडक्शन में प्रॉम्प्ट इंजेक्शन हमलों को रोकने के लिए कौन सी तकनीकें हैं?"
> - "सामान्य पैटर्न के लिए पुन: प्रयोज्य PromptTemplate ऑब्जेक्ट कैसे बनाएं?"

**टूल इंटीग्रेशन** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

यहाँ LangChain4j शक्तिशाली हो जाता है। आप `AiServices` का उपयोग करके एक AI सहायक बनाएंगे जो आपकी Java मेथड्स कॉल कर सके। बस मेथड्स को `@Tool("description")` से एनोटेट करें और LangChain4j बाकी संभालता है - AI उपयोगकर्ता के प्रश्नों के आधार पर स्वचालित रूप से प्रत्येक टूल का उपयोग करता है। यह फंक्शन कॉलिंग प्रदर्शित करता है, AI को कार्रवाई करने में सक्षम बनाने की एक प्रमुख तकनीक, केवल प्रश्नों के जवाब देने के बजाय।

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.builder(MathAssistant.class)
    .chatModel(model)
    .tools(new Calculator())
    .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
    .build();
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ कोशिश करें:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) खोलें और पूछें:
> - "`@Tool` एनोटेशन कैसे काम करता है और LangChain4j इसके साथ पीछे क्या करता है?"
> - "क्या AI जटिल समस्याओं को हल करने के लिए कई टूल्स को अनुक्रम में कॉल कर सकता है?"
> - "अगर कोई टूल अपवाद फेंकता है तो क्या होता है - मुझे त्रुटियों को कैसे संभालना चाहिए?"
> - "मैं इस कैलकुलेटर उदाहरण की जगह वास्तविक API कैसे जोड़ूँ?"

**दस्तावेज़ प्रश्नोत्तर (आसान RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

यहाँ आप LangChain4j के "Easy RAG" दृष्टिकोण का उपयोग करते हुए RAG (रिट्रीवल-ऑगमेंटेड जनरेशन) देखेंगे। दस्तावेज़ लोड किए जाते हैं, स्वचालित रूप से विभाजित और एम्बेडेड होते हैं एक मेमोरी स्टोर में, फिर एक कंटेंट रिट्रीवर AI को क्वेरी के समय प्रासंगिक टुकड़े देता है। AI आपके दस्तावेज़ों के आधार पर उत्तर देता है, अपनी सामान्य जानकारी के नहीं।

```java
Document document = loadDocument(Paths.get("document.txt"));

InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
EmbeddingStoreIngestor.ingest(List.of(document), embeddingStore);

Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
        .build();

String answer = assistant.chat("What is the main topic?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ कोशिश करें:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) खोलें और पूछें:
> - "मॉडल के प्रशिक्षण डेटा की तुलना में RAG AI मतिभ्रम कैसे रोकता है?"
> - "यह आसान तरीका और कस्टम RAG पाइपलाइन में क्या अंतर है?"
> - "मैं इसे कई दस्तावेज़ों या बड़े ज्ञान भंडारों के लिए कैसे स्केल करूँ?"

**ज़िम्मेदार AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

गतिरोध में रक्षा के साथ AI सुरक्षा बनाएं। यह डेमो दो सुरक्षा स्तर दिखाता है जो एक साथ काम करते हैं:

**भाग 1: LangChain4j इनपुट गार्डरेल्स** - खतरनाक प्रॉम्प्ट्स को LLM तक पहुँचने से पहले ब्लॉक करें। कस्टम गार्डरेल्स बनाएं जो निषिद्ध कीवर्ड या पैटर्न की जांच करते हैं। ये आपके कोड में चलते हैं, इसलिए तेज़ और मुफ़्त होते हैं।

```java
class DangerousContentGuardrail implements InputGuardrail {
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String text = userMessage.singleText().toLowerCase();
        if (text.contains("explosives")) {
            return fatal("Blocked: contains prohibited keyword");
        }
        return success();
    }
}
```

**भाग 2: प्रदाता सुरक्षा फ़िल्टर** - GitHub मॉडल्स में अंतर्निहित फ़िल्टर हैं जो आपकी गार्डरेल्स से छूट गए मुद्दों को पकड़ते हैं। आप कड़े ब्लॉक्स (HTTP 400 त्रुटियां) गंभीर उल्लंघनों के लिए और सौम्य इनकार जहाँ AI विनम्रता से मना करता है, देखेंगे।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ कोशिश करें:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) खोलें और पूछें:
> - "InputGuardrail क्या है और मैं अपना खुद का कैसे बनाऊँ?"
> - "कठोर ब्लॉक और सौम्य इनकार में क्या अंतर है?"
> - "गार्डरेल्स और प्रदाता फ़िल्टर दोनों एक साथ क्यों उपयोग करें?"

## अगले कदम

**अगला मॉड्यूल:** [01-परेचय - LangChain4j के साथ शुरुआत](../01-introduction/README.md)

---

**नेपालिकेशन:** [← मुख्य पृष्ठ पर वापस](../README.md) | [अगला: मॉड्यूल 01 - परिचय →](../01-introduction/README.md)

---

## समस्या निवारण

### पहली बार Maven बिल्ड

**समस्या**: शुरुआत में `mvn clean compile` या `mvn package` में लंबा समय लगता है (10-15 मिनट)

**कारण**: Maven को पहली बार सभी प्रोजेक्ट निर्भरताएँ (Spring Boot, LangChain4j लाइब्रेरीज़, Azure SDK, आदि) डाउनलोड करनी होती हैं।

**समाधान**: यह सामान्य व्यवहार है। अगली बार बिल्ड बहुत तेज़ होंगे क्योंकि निर्भरताएँ स्थानीय रूप से कैश हो जाती हैं। डाउनलोड समय आपके नेटवर्क की गति पर निर्भर करता है।

### PowerShell Maven कमांड सिंटैक्स

**समस्या**: Maven कमांड विफल होती हैं और त्रुटि देती हैं `Unknown lifecycle phase ".mainClass=..."`
**कारण**: PowerShell `=` को एक वैरिएबल असाइनमेंट ऑपरेटर के रूप में समझता है, जिससे Maven प्रॉपर्टी सिंटैक्स टूट जाता है

**समाधान**: Maven कमांड से पहले स्टॉप-पार्सिंग ऑपरेटर `--%` का उपयोग करें:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ऑपरेटर PowerShell को बताता है कि वह शेष सभी तर्कों को बिना किसी व्याख्या के सीधे Maven को भेज दे।

### Windows PowerShell इमोजी डिस्प्ले

**समस्या**: AI प्रतिक्रियाएं PowerShell में इमोजी के बजाय गड़बड़ अक्षर (जैसे `????` या `â??`) दिखाती हैं

**कारण**: PowerShell की डिफ़ॉल्ट एन्कोडिंग UTF-8 इमोजी का समर्थन नहीं करती

**समाधान**: Java एप्लिकेशन चलाने से पहले यह कमांड चलाएं:
```cmd
chcp 65001
```

यह टर्मिनल में UTF-8 एन्कोडिंग को मजबूर करता है। विकल्प के रूप में, Windows Terminal का उपयोग करें जिसमें बेहतर यूनिकोड समर्थन होता है।

### API कॉल्स डिबगिंग

**समस्या**: AI मॉडल से ऑथेंटिकेशन त्रुटियां, रेट लिमिट्स, या अप्रत्याशित प्रतिक्रियाएं

**समाधान**: उदाहरणों में `.logRequests(true)` और `.logResponses(true)` शामिल हैं ताकि API कॉल्स कंसोल में दिखाई दें। यह ऑथेंटिकेशन त्रुटियों, रेट लिमिट्स, या अप्रत्याशित प्रतिक्रियाओं की जाँच में मदद करता है। प्रोडक्शन में यह फ्लैग हटा दें ताकि लॉग शोर कम हो।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
इस दस्तावेज़ का अनुवाद AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके किया गया है। जबकि हम सटीकता के लिए प्रयासरत हैं, कृपया ध्यान दें कि स्वचालित अनुवादों में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल दस्तावेज़ अपनी मूल भाषा में ही प्राधिकृत स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सलाह दी जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->