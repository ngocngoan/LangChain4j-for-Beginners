# मॉड्यूल 00: त्वरित प्रारंभ

## विषय सूची

- [परिचय](../../../00-quick-start)
- [LangChain4j क्या है?](../../../00-quick-start)
- [LangChain4j निर्भरताएँ](../../../00-quick-start)
- [पूर्वापेक्षाएँ](../../../00-quick-start)
- [सेटअप](../../../00-quick-start)
  - [1. अपना GitHub टोकन प्राप्त करें](../../../00-quick-start)
  - [2. अपना टोकन सेट करें](../../../00-quick-start)
- [उदाहरण चलाएँ](../../../00-quick-start)
  - [1. मूल चैट](../../../00-quick-start)
  - [2. प्रॉम्प्ट पैटर्न](../../../00-quick-start)
  - [3. फ़ंक्शन कॉलिंग](../../../00-quick-start)
  - [4. दस्तावेज Q&A (RAG)](../../../00-quick-start)
  - [5. जिम्मेदार AI](../../../00-quick-start)
- [प्रत्येक उदाहरण क्या दर्शाता है](../../../00-quick-start)
- [अगले चरण](../../../00-quick-start)
- [समस्या निवारण](../../../00-quick-start)

## परिचय

यह त्वरित प्रारंभ आपको LangChain4j के साथ यथाशीघ्र शुरू करने के लिए बनाया गया है। यह LangChain4j और GitHub मॉडल के साथ AI अनुप्रयोग बनाने के मूलभूत पहलुओं को कवर करता है। अगले मॉड्यूल में आप Azure OpenAI के साथ LangChain4j का उपयोग करके अधिक उन्नत अनुप्रयोग बनाएंगे।

## LangChain4j क्या है?

LangChain4j एक Java लाइब्रेरी है जो AI-संचालित अनुप्रयोग बनाना सरल बनाती है। HTTP क्लाइंट और JSON पार्सिंग से निपटने के बजाय, आप साफ-सुथरे Java API के साथ काम करते हैं।

LangChain में "चेन" का अर्थ है कई घटकों को एक साथ जोड़ना - आप एक प्रॉम्प्ट को एक मॉडल से एक पार्सर तक जोड़ सकते हैं, या कई AI कॉल को एक के बाद एक जोड़ सकते हैं जहाँ एक आउटपुट अगले इनपुट में जाता है। यह त्वरित प्रारंभ जटिल चेन की खोज करने से पहले मूलभूत बातों पर ध्यान केंद्रित करता है।

<img src="../../../translated_images/hi/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j में घटकों को जोड़ना - शक्तिशाली AI वर्कफ़्लो बनाने के लिए ब्लॉक्स को जोड़ना*

हम तीन मुख्य घटकों का उपयोग करेंगे:

**ChatLanguageModel** - AI मॉडल इंटरैक्शन के लिए इंटरफ़ेस। `model.chat("prompt")` कॉल करें और प्रतिक्रिया स्ट्रिंग प्राप्त करें। हम `OpenAiOfficialChatModel` का उपयोग करते हैं जो OpenAI-संगत एंडपॉइंट जैसे GitHub मॉडल के साथ काम करता है।

**AiServices** - टाइप-सुरक्षित AI सेवा इंटरफ़ेस बनाता है। विधियों को परिभाषित करें, उन्हें `@Tool` से एनोटेट करें, और LangChain4j संचालन का प्रबंधन करता है। आवश्यक होने पर AI स्वतः आपके Java मेथड्स को कॉल करता है।

**MessageWindowChatMemory** - बातचीत का इतिहास बनाए रखता है। इसके बिना, प्रत्येक अनुरोध स्वतंत्र होता है। इसके साथ, AI पिछली संदेशों को याद रखता है और कई टर्न में संदर्भ बनाए रखता है।

<img src="../../../translated_images/hi/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j आर्किटेक्चर - आपके AI अनुप्रयोगों को शक्तिशाली बनाने के लिए मुख्य घटक एक साथ काम करते हैं* 

## LangChain4j निर्भरताएँ

यह त्वरित प्रारंभ [`pom.xml`](../../../00-quick-start/pom.xml) में दो Maven निर्भरताओं का उपयोग करता है:

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
```

`langchain4j-open-ai-official` मॉड्यूल `OpenAiOfficialChatModel` क्लास प्रदान करता है जो OpenAI-संगत API से जुड़ता है। GitHub मॉडल उसी API फॉर्मेट का उपयोग करते हैं, इसलिए कोई विशेष एडाप्टर आवश्यक नहीं है - बस बेस URL को `https://models.github.ai/inference` पर सेट करें।

## पूर्वापेक्षाएँ

**डेव कंटेनर का उपयोग कर रहे हैं?** Java और Maven पहले से इंस्टॉल हैं। आपको केवल GitHub पर्सनल एक्सेस टोकन की आवश्यकता है।

**स्थानीय विकास:**
- Java 21+, Maven 3.9+
- GitHub पर्सनल एक्सेस टोकन (नीचे निर्देश)

> **नोट:** यह मॉड्यूल GitHub मॉडल से `gpt-4.1-nano` का उपयोग करता है। कोड में मॉडल नाम संशोधित न करें - यह GitHub के उपलब्ध मॉडलों के साथ काम करने के लिए कॉन्फ़िगर है।

## सेटअप

### 1. अपना GitHub टोकन प्राप्त करें

1. [GitHub सेटिंग्स → Personal Access Tokens](https://github.com/settings/personal-access-tokens) पर जाएं
2. "Generate new token" पर क्लिक करें
3. एक विवरणात्मक नाम सेट करें (जैसे, "LangChain4j Demo")
4. एक्सपायरेशन सेट करें (7 दिन सुझाए जाते हैं)
5. "Account permissions" के तहत, "Models" ढूंढें और "Read-only" सेट करें
6. "Generate token" पर क्लिक करें
7. अपना टोकन कॉपी करें और सुरक्षित रखें - यह फिर दिखाई नहीं देगा

### 2. अपना टोकन सेट करें

**विकल्प 1: VS Code का उपयोग करना (सुझावित)**

यदि आप VS Code का उपयोग कर रहे हैं, तो अपने टोकन को प्रोजेक्ट रूट में `.env` फ़ाइल में जोड़ें:

अगर `.env` फ़ाइल मौजूद नहीं है, तो `.env.example` को `.env` में कॉपी करें या प्रोजेक्ट रूट में नई `.env` फ़ाइल बनाएं।

**`.env` फ़ाइल का उदाहरण:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env में
GITHUB_TOKEN=your_token_here
```

फिर आप एक्सप्लोरर में किसी भी डेमो फ़ाइल (जैसे, `BasicChatDemo.java`) पर राइट-क्लिक कर सकते हैं और **"Run Java"** चुन सकते हैं या रन और डिबग पैनल से लॉन्च कॉन्फिगरेशन का उपयोग कर सकते हैं।

**विकल्प 2: टर्मिनल का उपयोग करना**

टोकन को एक पर्यावरण चर के रूप में सेट करें:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## उदाहरण चलाएँ

**VS Code का उपयोग करते हुए:** बस एक्सप्लोरर में किसी भी डेमो फ़ाइल पर राइट-क्लिक करें और **"Run Java"** चुनें, या रन और डिबग पैनल से लॉन्च कॉन्फिगरेशन का उपयोग करें (पहले सुनिश्चित करें कि आपने टोकन `.env` फ़ाइल में जोड़ा है)।

**Maven का उपयोग करते हुए:** वैकल्पिक रूप से, आप कमांड लाइन से चला सकते हैं:

### 1. मूल चैट

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

शून्य-शॉट, कुछ-शॉट, चेन-ऑफ-थॉट, और भूमिका-आधारित प्रॉम्प्टिंग दिखाता है।

### 3. फ़ंक्शन कॉलिंग

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI आवश्यक होने पर आपके Java मेथड्स को स्वतः कॉल करता है।

### 4. दस्तावेज Q&A (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt` की सामग्री के बारे में प्रश्न पूछें।

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

## प्रत्येक उदाहरण क्या दर्शाता है

**मूल चैट** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

यहाँ से शुरू करें ताकि LangChain4j को सबसे सरल रूप में देखें। आप `OpenAiOfficialChatModel` बनाएंगे, `.chat()` के साथ प्रॉम्प्ट भेजेंगे, और प्रतिक्रिया प्राप्त करेंगे। यह आधार दर्शाता है: कस्टम एंडपॉइंट और API कुंजी के साथ मॉडल को इनिशियलाइज़ कैसे करते हैं। इस पैटर्न को समझने के बाद बाकी सब कुछ इसी पर आधारित है।

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट का प्रयास करें:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) खोलें और पूछें:
> - "इस कोड में GitHub मॉडल से Azure OpenAI पर कैसे स्विच करूं?"
> - "OpenAiOfficialChatModel.builder() में और कौन से पैरामीटर कॉन्फ़िगर कर सकता हूँ?"
> - "पूरा जवाब आने की बजाय स्ट्रीमिंग प्रतिक्रियाएँ कैसे जोड़ूं?"

**प्रॉम्प्ट इंजीनियरिंग** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

अब जब आप मॉडल से बात करना जानते हैं, तो देखते हैं आप उससे क्या कहते हैं। यह डेमो समान मॉडल सेटअप का उपयोग करता है लेकिन पाँच अलग-अलग प्रॉम्प्टिंग पैटर्न दिखाता है। सीधे निर्देशों के लिए शून्य-शॉट प्रॉम्प्ट, उदाहरणों से सीखने वाले कुछ-शॉट प्रॉम्प्ट, तर्क के चरण दिखाने वाले चेन-ऑफ-थॉट प्रॉम्प्ट, और संदर्भ सेट करने वाले भूमिका-आधारित प्रॉम्प्ट आज़माएँ। आप देखेंगे कि आपके अनुरोध के फ्रेम के आधार पर एक ही मॉडल ड्रैमेटिक रूप से अलग परिणाम देता है।

यह डेमो प्रॉम्प्ट टेम्प्लेट भी दिखाता है, जो चर के साथ पुन: उपयोग योग्य प्रॉम्प्ट बनाने का एक शक्तिशाली तरीका है।
नीचे का उदाहरण LangChain4j `PromptTemplate` का उपयोग करके चर भरने वाला प्रॉम्प्ट दिखाता है। AI प्रदान किए गए गंतव्य और गतिविधि पर आधारित उत्तर देगा।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट का प्रयास करें:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) खोलें और पूछें:
> - "शून्य-शॉट और कुछ-शॉट प्रॉम्प्टिंग में क्या अंतर है, और मुझे कब क्या इस्तेमाल करना चाहिए?"
> - "टेम्परेचर पैरामीटर मॉडल के जवाबों को कैसे प्रभावित करता है?"
> - "प्रोडक्शन में प्रॉम्प्ट इंजेक्शन अटैक्स रोकने के लिए कौन सी तकनीकें हैं?"
> - "कॉमन पैटर्न के लिए पुन: उपयोग योग्य PromptTemplate ऑब्जेक्ट कैसे बनाऊं?"

**टूल इंटीग्रेशन** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

यहाँ LangChain4j शक्तिशाली होता है। आप `AiServices` का उपयोग करके एक AI सहायक बनाएंगे जो आपके Java मेथड्स को कॉल कर सकता है। बस मेथड्स को `@Tool("description")` से एनोटेट करें और LangChain4j बाकी का संचालन करेगा - AI उपयोगकर्ता के प्रश्न के आधार पर स्वचालित रूप से प्रत्येक टूल का उपयोग करने का निर्णय लेता है। यह फ़ंक्शन कॉलिंग को दर्शाता है, जो AI को केवल प्रश्नों का उत्तर देने के बजाय क्रियाएँ करने में सक्षम बनाता है।

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट का प्रयास करें:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) खोलें और पूछें:
> - "@Tool एनोटेशन कैसे काम करता है और LangChain4j इसके पीछे क्या करता है?"
> - "क्या AI जटिल समस्याओं को हल करने के लिए एक के बाद एक कई टूल कॉल कर सकता है?"
> - "अगर कोई टूल अपवाद फेंकता है तो क्या होता है - त्रुटियों को कैसे संभालना चाहिए?"
> - "मैं इस कैलकुलेटर उदाहरण के बजाय असली API कैसे इंटीग्रेट करूँ?"

**दस्तावेज Q&A (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

यहाँ आप RAG (retrieval-augmented generation) की नींव देखेंगे। मॉडल के प्रशिक्षण डेटा पर निर्भर रहने के बजाय, आप [`document.txt`](../../../00-quick-start/document.txt) से सामग्री लोड करते हैं और इसे प्रॉम्प्ट में शामिल करते हैं। AI आपके दस्तावेज़ के आधार पर उत्तर देता है, न कि उसके सामान्य ज्ञान पर। यह आपके अपने डेटा के साथ काम करने वाले सिस्टम बनाने की पहली कड़ी है।

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **नोट:** यह सरल तरीका पूरे दस्तावेज़ को प्रॉम्प्ट में लोड करता है। बड़े फ़ाइलों (>10KB) के लिए, आप संदर्भ सीमा से बाहर चले जायेंगे। मॉड्यूल 03 में प्रोडक्शन RAG सिस्टम के लिए चंकिंग और वेक्टर खोज कवर किया जाएगा।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट का प्रयास करें:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) खोलें और पूछें:
> - "RAG मॉडल के प्रशिक्षण डेटा की तुलना में AI हल्यूसिनेशन को कैसे रोकता है?"
> - "इस सरल तरीके और वेक्टर एम्बेडिंग से रिकॉल में क्या अंतर है?"
> - "मैं इसे कई दस्तावेज़ों या बड़े नॉलेज बेस को संभालने के लिए कैसे स्केल करूं?"
> - "प्रॉम्प्ट को इस तरह संरचित करने के सर्वोत्तम तरीके क्या हैं जिससे AI केवल प्रदत्त संदर्भ का उपयोग करे?"

**जिम्मेदार AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

डिफेंस इन डेप्थ के साथ AI सुरक्षा बनाएं। यह डेमो दो सुरक्षा परतें दिखाता है जो एक साथ काम करती हैं:

**भाग 1: LangChain4j इनपुट गार्डरेल्स** - LLM तक पहुँचने से पहले खतरनाक प्रॉम्प्ट रोकें। कस्टम गार्डरेल्स बनाएं जो निषिद्ध कीवर्ड या पैटर्न चेक करें। ये आपके कोड में चलते हैं, इसलिए ये तेज़ और मुफ़्त हैं।

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

**भाग 2: प्रदाता सुरक्षा फ़िल्टर्स** - GitHub मॉडल में बिल्ट-इन फ़िल्टर्स होते हैं जो आपके गार्डरेल्स से छूट गए मामलों को पकड़ते हैं। आप कड़े ब्लॉक्स (HTTP 400 त्रुटियाँ) और नरम मना करने वाले (AI विनम्रता से मना करता है) देखेंगे।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट का प्रयास करें:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) खोलें और पूछें:
> - "InputGuardrail क्या है और मैं अपना खुद का कैसे बनाऊं?"
> - "कड़ा ब्लॉक और नरम मना करने में क्या अंतर है?"
> - "गॉर्डरेल्स और प्रदाता फ़िल्टर दोनों एक साथ क्यों उपयोग करें?"

## अगले चरण

**अगला मॉड्यूल:** [01-introduction - LangChain4j और Azure पर gpt-5 के साथ शुरूआत](../01-introduction/README.md)

---

**नेविगेशन:** [← मुख्य पृष्ठ पर वापस](../README.md) | [अगला: मॉड्यूल 01 - परिचय →](../01-introduction/README.md)

---

## समस्या निवारण

### पहली बार Maven बिल्ड

**समस्या:** प्रारंभिक `mvn clean compile` या `mvn package` में काफी समय लगता है (10-15 मिनट)

**कारण:** Maven को पहली बिल्ड में सभी प्रोजेक्ट निर्भरताओं (Spring Boot, LangChain4j लाइब्रेरी, Azure SDK, आदि) को डाउनलोड करने की आवश्यकता होती है।

**समाधान:** यह सामान्य व्यवहार है। बाद की बिल्ड बहुत तेज़ होंगी क्योंकि निर्भरताएँ स्थानीय रूप से कैश हो जाती हैं। डाउनलोड समय आपकी नेटवर्क स्पीड पर निर्भर करता है।
### PowerShell Maven कमांड सिंटैक्स

**समस्या**: Maven कमांड में त्रुटि आती है `Unknown lifecycle phase ".mainClass=..."`

**कारण**: PowerShell `=` को वेरिएबल असाइनमेंट ऑपरेटर के रूप में समझता है, जिससे Maven प्रॉपर्टी सिंटैक्स टूट जाता है

**समाधान**: Maven कमांड से पहले stop-parsing ऑपरेटर `--%` का उपयोग करें:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ऑपरेटर PowerShell को कहता है कि शेष सभी आर्गुमेंट्स को बिना व्याख्या किए सीधे Maven को पास करें।

### Windows PowerShell में इमोजी डिस्प्ले

**समस्या**: PowerShell में एआई जवाबों में इमोजी के बजाय बेकार अक्षर (जैसे, `????` या `â??`) दिखते हैं

**कारण**: PowerShell का डिफ़ॉल्ट एनकोडिंग UTF-8 इमोजी को सपोर्ट नहीं करता

**समाधान**: Java एप्लिकेशन चलाने से पहले यह कमांड चलाएं:
```cmd
chcp 65001
```

यह टर्मिनल में UTF-8 एनकोडिंग को मजबूर करता है। वैकल्पिक रूप से, Windows Terminal का उपयोग करें जो बेहतर यूनिकोड सपोर्ट प्रदान करता है।

### API कॉल्स डिबग करना

**समस्या**: AI मॉडल से ऑथेंटिकेशन त्रुटियाँ, रेट लिमिट, या अनपेक्षित प्रतिक्रियाएँ आना

**समाधान**: उदाहरणों में `.logRequests(true)` और `.logResponses(true)` शामिल हैं जो एपीआई कॉल्स को कंसोल में दिखाते हैं। इससे ऑथेंटिकेशन त्रुटियों, रेट लिमिट, या अनपेक्षित प्रतिक्रियाओं का समाधान करने में मदद मिलती है। प्रोडक्शन में लॉग शोर कम करने के लिए इन फ्लैग्स को हटा दें।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
इस दस्तावेज़ का अनुवाद एआई अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके किया गया है। जबकि हम सटीकता के लिए प्रयासरत हैं, कृपया ध्यान दें कि स्वचालित अनुवाद में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल दस्तावेज़ को इसकी मातृ भाषा में ही आधिकारिक स्रोत माना जाना चाहिए। महत्त्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->