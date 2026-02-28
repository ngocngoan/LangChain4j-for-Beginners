# मॉड्यूल 00: त्वरित प्रारंभ

## सामग्री तालिका

- [परिचय](../../../00-quick-start)
- [LangChain4j क्या है?](../../../00-quick-start)
- [LangChain4j निर्भरता](../../../00-quick-start)
- [पूर्वापेक्षाएँ](../../../00-quick-start)
- [सेटअप](../../../00-quick-start)
  - [1. अपना GitHub टोकन प्राप्त करें](../../../00-quick-start)
  - [2. अपना टोकन सेट करें](../../../00-quick-start)
- [उदाहरण चलाएँ](../../../00-quick-start)
  - [1. बुनियादी चैट](../../../00-quick-start)
  - [2. प्रॉम्प्ट पैटर्न](../../../00-quick-start)
  - [3. फ़ंक्शन कॉलिंग](../../../00-quick-start)
  - [4. दस्तावेज़ प्रश्नोत्तर (इज़ी RAG)](../../../00-quick-start)
  - [5. जिम्मेदार AI](../../../00-quick-start)
- [हर उदाहरण क्या दिखाता है](../../../00-quick-start)
- [अगले कदम](../../../00-quick-start)
- [समस्या निवारण](../../../00-quick-start)

## परिचय

यह त्वरित प्रारंभ आपको LangChain4j के साथ यथाशीघ्र काम शुरू करने के लिए बनाया गया है। यह LangChain4j और GitHub Models के साथ AI अनुप्रयोग बनाने के बिल्कुल मूलभूत पहलुओं को कवर करता है। अगले मॉड्यूल में आप Azure OpenAI के साथ LangChain4j का उपयोग करके अधिक उन्नत अनुप्रयोग बनाएंगे।

## LangChain4j क्या है?

LangChain4j एक जावा लाइब्रेरी है जो AI-संचालित अनुप्रयोगों का निर्माण सरल बनाती है। HTTP क्लाइंट और JSON पार्सिंग से निपटने के बजाय, आप साफ-सुथरे जावा API के साथ काम करते हैं।

LangChain में "chain" का मतलब है कई घटकों को एक साथ जोड़ना — आप एक प्रॉम्प्ट को मॉडल से जोड़ सकते हैं या कई AI कॉल्स को एक के आउटपुट को अगले इनपुट के रूप में देते हुए जोड़ सकते हैं। यह त्वरित शुरुआत बुनियादी बातों पर केंद्रित है, इससे पहले कि जटिल चेन का पता किया जाए।

<img src="../../../translated_images/hi/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j में चेनिंग घटक - निर्माण खंड जो शक्तिशाली AI वर्कफ़्लो बनाने के लिए जुड़ते हैं*

हम तीन मुख्य घटकों का उपयोग करेंगे:

**ChatModel** - AI मॉडल इंटरैक्शन के लिए इंटरफ़ेस। `model.chat("prompt")` कॉल करें और एक प्रतिक्रिया स्ट्रिंग प्राप्त करें। हम `OpenAiOfficialChatModel` का उपयोग करते हैं जो OpenAI-संगत एंडपॉइंट्स जैसे GitHub Models के साथ काम करता है।

**AiServices** - प्रकार-सुरक्षित AI सेवा इंटरफ़ेस बनाता है। विधियाँ परिभाषित करें, उन्हें `@Tool` के साथ एनोटेट करें, और LangChain4j ऑर्केस्ट्रेशन को संभालता है। AI स्वचालित रूप से आपकी जावा विधियों को आवश्यकतानुसार कॉल करता है।

**MessageWindowChatMemory** - बातचीत का इतिहास बनाए रखता है। इसके बिना, प्रत्येक अनुरोध स्वतंत्र होता है। इसके साथ, AI पिछले संदेशों को याद रखता है और कई चरणों में संदर्भ बनाए रखता है।

<img src="../../../translated_images/hi/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j आर्किटेक्चर - कोर घटक जो आपके AI अनुप्रयोगों को पावर करने के लिए साथ काम करते हैं*

## LangChain4j निर्भरता

यह त्वरित शुरुआत [`pom.xml`](../../../00-quick-start/pom.xml) में तीन Maven निर्भरताओं का उपयोग करता है:

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

`langchain4j-open-ai-official` मॉड्यूल `OpenAiOfficialChatModel` क्लास प्रदान करता है जो OpenAI-संगत APIs से जुड़ता है। GitHub Models भी उसी API प्रारूप का उपयोग करते हैं, इसलिए कोई विशेष एडाप्टर आवश्यक नहीं — बस बेस URL को `https://models.github.ai/inference` पर सेट करें।

`langchain4j-easy-rag` मॉड्यूल स्वचालित दस्तावेज़ विभाजन, एम्बेडिंग, और पुनःप्राप्ति प्रदान करता है ताकि आप प्रत्येक चरण को मैन्युअली कॉन्फ़िगर किए बिना RAG अनुप्रयोग बना सकें।

## पूर्वापेक्षाएँ

**डेव कंटेनर का उपयोग कर रहे हैं?** Java और Maven पहले से इंस्टॉल हैं। आपको केवल एक GitHub Personal Access Token की आवश्यकता है।

**लोकल विकास:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (नीचे निर्देश)

> **नोट:** यह मॉड्यूल GitHub Models से `gpt-4.1-nano` का उपयोग करता है। कोड में मॉडल नाम को संशोधित न करें - यह GitHub के उपलब्ध मॉडलों के साथ काम करने के लिए कॉन्फ़िगर किया गया है।

## सेटअप

### 1. अपना GitHub टोकन प्राप्त करें

1. [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens) पर जाएं
2. "Generate new token" पर क्लिक करें
3. एक वर्णनात्मक नाम सेट करें (जैसे, "LangChain4j Demo")
4. समाप्ति अवधि सेट करें (7 दिन अनुशंसित)
5. "Account permissions" के अंतर्गत "Models" को "Read-only" पर सेट करें
6. "Generate token" पर क्लिक करें
7. अपना टोकन कॉपी करें और सुरक्षित रखें - यह फिर दिखाई नहीं देगा

### 2. अपना टोकन सेट करें

**विकल्प 1: VS Code का उपयोग कर (सिफारिश की गई)**

यदि आप VS Code का उपयोग कर रहे हैं, तो प्रोजेक्ट रूट में `.env` फ़ाइल में अपना टोकन जोड़ें:

यदि `.env` फ़ाइल मौजूद नहीं है, तो `.env.example` को `.env` में कॉपी करें या प्रोजेक्ट रूट में नई `.env` फ़ाइल बनाएं।

**`.env` फ़ाइल का उदाहरण:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env में
GITHUB_TOKEN=your_token_here
```

फिर आप एक्सप्लोरर में किसी भी डेमो फ़ाइल (जैसे, `BasicChatDemo.java`) पर राइट-क्लिक करके **"Run Java"** चुन सकते हैं या रन और डिबग पैनल से लॉन्च कॉन्फ़िगरेशन का उपयोग कर सकते हैं।

**विकल्प 2: टर्मिनल का उपयोग कर**

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

**VS Code का उपयोग:** एक्सप्लोरर में किसी भी डेमो फ़ाइल पर राइट-क्लिक करके **"Run Java"** चुनें, या रन और डिबग पैनल से लॉन्च कॉन्फ़िगरेशन का उपयोग करें (सुनिश्चित करें कि आपने अपनी टोकन `.env` फाइल में जोड़ी हो)।

**Maven का उपयोग:** वैकल्पिक रूप से, आप कमांड लाइन से रन कर सकते हैं:

### 1. बुनियादी चैट

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

यह ज़ीरो-शॉट, फ्यू-शॉट, चैन-ऑफ़-थॉट, और भूमिका-आधारित प्रॉम्प्टिंग दिखाता है।

### 3. फ़ंक्शन कॉलिंग

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI आवश्यकतानुसार स्वचालित रूप से आपकी जावा विधियों को कॉल करता है।

### 4. दस्तावेज़ प्रश्नोत्तर (इज़ी RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Easy RAG के साथ अपने दस्तावेज़ों के बारे में प्रश्न पूछें जिसमें स्वचालित एम्बेडिंग और पुनःप्राप्ति होती है।

### 5. जिम्मेदार AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

देखें कि AI सुरक्षा फ़िल्टर हानिकारक सामग्री को कैसे ब्लॉक करते हैं।

## हर उदाहरण क्या दिखाता है

**बुनियादी चैट** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

यहाँ से शुरू करें ताकि आप LangChain4j को इसके सबसे सरल रूप में देख सकें। आप एक `OpenAiOfficialChatModel` बनाएंगे, `.chat()` के साथ प्रॉम्प्ट भेजेंगे, और प्रतिक्रिया प्राप्त करेंगे। यह नींव दिखाता है: कस्टम एंडपॉइंट्स और API कुंजी के साथ मॉडल को कैसे प्रारंभ किया जाए। एक बार जब आप इस पैटर्न को समझ लें, तो बाकी सब इसी पर आधारित होगा।

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ प्रयास करें:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) खोलें और पूछें:
> - "इस कोड में GitHub Models से Azure OpenAI कैसे स्विच करूँ?"
> - "OpenAiOfficialChatModel.builder() में मैं और किन पैरामीटर को कॉन्फ़िगर कर सकता हूँ?"
> - "मैं स्ट्रिमिंग प्रतिक्रियाएँ कैसे जोड़ सकता हूँ बजाय पूरी प्रतिक्रिया के इंतजार के?"

**प्रॉम्प्ट इंजीनियरिंग** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

अब जब आपको पता है कि मॉडल से कैसे बात करनी है, तो आइए देखें कि आप उससे क्या कहते हैं। यह डेमो वही मॉडल सेटअप उपयोग करता है लेकिन पांच विभिन्न प्रॉम्प्टिंग पैटर्न दिखाता है। सीधे निर्देशों के लिए ज़ीरो-शॉट प्रॉम्प्ट, उदाहरणों से सीखने के लिए फ्यू-शॉट प्रॉम्प्ट, तर्क के चरण दिखाने के लिए चैन-ऑफ़-थॉट प्रॉम्प्ट, और संदर्भ सेट करने के लिए भूमिका-आधारित प्रॉम्प्ट आज़माएं। आप देखेंगे कि कैसे एक ही मॉडल आपके अनुरोध के फ्रेम के आधार पर बहुत अलग परिणाम देता है।

डेमो प्रॉम्प्ट टेम्प्लेट भी दिखाता है, जो वेरिएबल्स के साथ पुन:प्रयुक्त करने योग्य प्रॉम्प्ट बनाने का एक शक्तिशाली तरीका है।
नीचे का उदाहरण LangChain4j के `PromptTemplate` का उपयोग करते हुए वेरिएबल्स भरने वाला प्रॉम्प्ट दिखाता है। AI दिए गए गंतव्य और गतिविधि के आधार पर जवाब देगा।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ प्रयास करें:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) खोलें और पूछें:
> - "ज़रूरी रूप से ज़ीरो-शॉट और फ्यू-शॉट प्रॉम्प्टिंग में क्या अंतर है, और मैं कब कौन सा उपयोग करूँ?"
> - "टेम्परेचर पैरामीटर मॉडल की प्रतिक्रियाओं को कैसे प्रभावित करता है?"
> - "प्रोडक्शन में प्रॉम्प्ट इंजेक्शन हमलों को रोकने के कुछ तरीके क्या हैं?"
> - "आम पैटर्न के लिए पुन: उपयोग योग्य PromptTemplate ऑब्जेक्ट्स कैसे बनाऊं?"

**टूल इंटीग्रेशन** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

यहां LangChain4j शक्तिशाली हो जाता है। आप `AiServices` का उपयोग करके एक AI सहायक बनाएंगे जो आपकी जावा विधियों को कॉल कर सकता है। केवल विधियों को `@Tool("description")` से एनोटेट करें और LangChain4j बाकी संभालता है - AI स्वचालित रूप से तय करता है कि यूज़र क्या पूछता है उसके आधार पर कौन-सी टूल्स का उपयोग करना है। यह फ़ंक्शन कॉलिंग दिखाता है, जो AI के लिए कार्य करने के लिए एक मुख्य तकनीक है, केवल सवालों के जवाब देने के बजाय।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ प्रयास करें:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) खोलें और पूछें:
> - "@Tool एनोटेशन कैसे काम करता है और LangChain4j इसके साथ पीछे क्या करता है?"
> - "क्या AI जटिल समस्याएँ हल करने के लिए कई टूल्स को सिलसिलेवार कॉल कर सकता है?"
> - "अगर कोई टूल अपवाद फेंकता है तो मैं त्रुटियों को कैसे संभालूं?"
> - "मैं इस कैलकुलेटर उदाहरण के बजाय एक वास्तविक API कैसे इंटीग्रेट करूँ?"

**दस्तावेज़ प्रश्नोत्तर (इज़ी RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

यहां आप LangChain4j की "Easy RAG" विधि का उपयोग करते हुए RAG (रीट्रीवल-ऑगमेंटेड जनरेशन) देखेंगे। दस्तावेज़ लोड होते हैं, स्वचालित रूप से अलग और एम्बेड होकर मेमोरी स्टोर में रखे जाते हैं, फिर एक कंटेंट रिट्रीवर AI को क्वेरी समय पर प्रासंगिक टुकड़े प्रदान करता है। AI आपके दस्तावेज़ों के आधार पर जवाब देता है, न कि उसकी सामान्य जानकारी के आधार पर।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ प्रयास करें:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) खोलें और पूछें:
> - "RAG AI भ्रम को मॉडल के प्रशिक्षण डेटा की तुलना में कैसे रोकता है?"
> - "यह आसान तरीका और कस्टम RAG पाइपलाइन में क्या अंतर है?"
> - "मैं इसे कई दस्तावेज़ों या बड़े ज्ञान आधारों को संभालने के लिए कैसे स्केल करूँ?"

**जिम्मेदार AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

डिफेंस-इन-डेप्थ के साथ AI सुरक्षा बनाएं। यह डेमो दो सुरक्षा परतें दिखाता है जो साथ काम करती हैं:

**भाग 1: LangChain4j इनपुट गार्डरेल्स** - LLM तक पहुँचने से पहले खतरनाक प्रॉम्प्ट्स को ब्लॉक करें। कस्टम गार्डरेल्स बनाएं जो निषिद्ध कीवर्ड या पैटर्न की जांच करें। ये आपके कोड में चलते हैं, इसलिए ये तेज़ और मुफ्त हैं।

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

**भाग 2: प्रदाता सुरक्षा फ़िल्टर** - GitHub Models में अंतर्निहित फ़िल्टर होते हैं जो आपके गार्डरेल्स जो चूक सकते हैं उसे पकड़ते हैं। आप गंभीर उल्लंघनों के लिए हार्ड ब्लॉक (HTTP 400 त्रुटियाँ) और सौम्य इंकार देखेंगे जहाँ AI विनम्रता से इनकार करता है।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ प्रयास करें:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) खोलें और पूछें:
> - "InputGuardrail क्या है और मैं अपना खुद का कैसे बनाऊं?"
> - "हार्ड ब्लॉक और सॉफ्ट रिफ़्यूज़ल में क्या अंतर है?"
> - "गॉर्डरेल्स और प्रदाता फ़िल्टर्स दोनों को साथ में क्यों इस्तेमाल करें?"

## अगले कदम

**अगला मॉड्यूल:** [01-परिचय - LangChain4j और gpt-5 के साथ Azure पर शुरुआत](../01-introduction/README.md)

---

**नेविगेशन:** [← मुख्य पर वापस जाएं](../README.md) | [अगला: मॉड्यूल 01 - परिचय →](../01-introduction/README.md)

---

## समस्या निवारण

### पहली बार Maven बिल्ड

**समस्या**: प्रारंभिक `mvn clean compile` या `mvn package` में बहुत समय लगता है (10-15 मिनट)

**कारण**: Maven को पहली बिल्ड में सभी प्रोजेक्ट निर्भरताएँ (Spring Boot, LangChain4j लाइब्रेरीज, Azure SDKs आदि) डाउनलोड करनी होती हैं।

**समाधान**: यह सामान्य व्यवहार है। बाद की बिल्ड बहुत तेज़ होंगी क्योंकि निर्भरताएँ स्थानीय स्तर पर कैश हो جاتی हैं। डाउनलोड समय आपके नेटवर्क की गति पर निर्भर करता है।

### PowerShell Maven कमांड सिंटैक्स

**समस्या**: Maven कमांड में `Unknown lifecycle phase ".mainClass=..."` त्रुटि आती है।
**कारण**: PowerShell `=` को एक वेरिएबल असाइनमेंट ऑपरेटर के रूप में व्याख्यायित करता है, जिससे Maven प्रॉपर्टी सिंटैक्स टूट जाती है

**समाधान**: Maven कमांड से पहले stop-parsing ऑपरेटर `--%` का उपयोग करें:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ऑपरेटर PowerShell को शेष सभी आर्गुमेंट्स को Maven को बिना व्याख्या के शाब्दिक रूप में पास करने के लिए बताता है।

### Windows PowerShell Emoji प्रदर्शन

**समस्या**: PowerShell में AI प्रतिक्रियाएँ इमोजी के स्थान पर कचरा अक्षर दिखाती हैं (जैसे, `????` या `â??`)

**कारण**: PowerShell की डिफ़ॉल्ट एन्कोडिंग UTF-8 इमोजी का समर्थन नहीं करती

**समाधान**: Java एप्लिकेशन चलाने से पहले यह कमांड चलाएँ:
```cmd
chcp 65001
```

यह टर्मिनल में UTF-8 एन्कोडिंग को मजबूर करता है। वैकल्पिक रूप से, Windows Terminal का उपयोग करें जिसमें बेहतर यूनिकोड समर्थन है।

### API कॉल्स का डीबगिंग

**समस्या**: ऑथेंटिकेशन त्रुटियाँ, रेट लिमिट या AI मॉडल से अप्रत्याशित प्रतिक्रियाएँ

**समाधान**: उदाहरणों में `.logRequests(true)` और `.logResponses(true)` शामिल हैं ताकि API कॉल्स को कंसोल में दिखाया जा सके। यह ऑथेंटिकेशन त्रुटियाँ, रेट लिमिट या अप्रत्याशित प्रतिक्रियाओं को ट्रबलशूट करने में मदद करता है। उत्पादकता में लॉग शोर कम करने के लिए इन फ्लैग्स को हटा दें।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यह दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। हमने सटीकता के लिए प्रयास किया है, लेकिन कृपया ध्यान रखें कि स्वचालित अनुवादों में त्रुटियाँ या गलतियाँ हो सकती हैं। मूल दस्तावेज़ अपनी मूल भाषा में प्रामाणिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सलाह दी जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या भ्रामक अर्थ के लिए हम जिम्मेदार नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->