# Module 00: छिटो सुरु गर्नुहोस्

## सूची

- [परिचय](../../../00-quick-start)
- [LangChain4j के हो?](../../../00-quick-start)
- [LangChain4j निर्भरता](../../../00-quick-start)
- [पूर्वआवश्यकताहरू](../../../00-quick-start)
- [सेटअप](../../../00-quick-start)
  - [1. आफ्नो GitHub टोकन प्राप्त गर्नुहोस्](../../../00-quick-start)
  - [2. आफ्नो टोकन सेट गर्नुहोस्](../../../00-quick-start)
- [उदाहरणहरू चलाउनुहोस्](../../../00-quick-start)
  - [1. आधारभूत कुराकानी](../../../00-quick-start)
  - [2. प्रॉम्प्ट ढाँचाहरू](../../../00-quick-start)
  - [3. फङ्क्शन कलिङ](../../../00-quick-start)
  - [4. दस्तावेज Q&A (सजिलो RAG)](../../../00-quick-start)
  - [5. जिम्मेवार AI](../../../00-quick-start)
- [हरेक उदाहरणले के देखाउँछ](../../../00-quick-start)
- [अगाडि के गर्ने](../../../00-quick-start)
- [समस्या समाधान](../../../00-quick-start)

## परिचय

यो छिटो सुरु गर्नु भनेको तपाईंलाई सक्दो छिटो LangChain4j सँग सुरु गराउनु हो। यसले LangChain4j र GitHub मोडेलहरू संग AI अनुप्रयोगहरू निर्माण गर्ने आधारभूत कुराहरू समेट्छ। आगामी मोड्युलहरूमा तपाईं Azure OpenAI सँग LangChain4j प्रयोग गरी थप उन्नत अनुप्रयोगहरू बनाउनु हुनेछ।

## LangChain4j के हो?

LangChain4j एक Java पुस्तकालय हो जसले AI-सन्चालित अनुप्रयोगहरू निर्माण गर्न सजिलो बनाउँछ। HTTP क्लाइएन्ट र JSON पार्सिङ्ग सँग जुझ्नुको सट्टा तपाईले सफा Java API हरू सँग काम गर्नुहुन्छ।

LangChain मा "chain" भन्नाले धेरै कम्पोनेन्टहरूलाई सँगै चिन्हो मिलाउनु हो - तपाईंले एक प्रॉम्प्टलाई मोडेलमा, त्यसपछि पार्सरमा जोड्न सक्नुहुन्छ, वा धेरै AI कलहरूलाई क्रमशः एक अर्कामा आउटपुटले अर्को इनपुटलाई पूरा गर्ने तरिकाले लिंक गर्न सक्नुहुन्छ। यो छिटो सुरु आधारभूत्वमा केन्द्रित छ र पछि जटिल चेनहरू अन्वेषण गर्नेछ।

<img src="../../../translated_images/ne/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j मा कम्पोनेन्टहरू मिलाउने - निर्माण ब्लकहरूले शक्तिशाली AI कार्यप्रवाहहरू सिर्जना गर्छन्*

हामी तीन मुख्य कम्पोनेन्टहरू प्रयोग गर्नेछौं:

**ChatModel** - AI मोडेल अन्तरक्रियाका लागि इन्टरफेस। `model.chat("prompt")` कल गर्नुहोस् र प्रतिक्रिया स्ट्रिङ पाउनुहोस्। हामी `OpenAiOfficialChatModel` प्रयोग गर्छौं जुन OpenAI-अनुकूल एन्डप्वाइन्टहरू जस्तै GitHub Models सँग काम गर्छ।

**AiServices** - प्रकार-सुरक्षित AI सेवा इन्टरफेसहरू सिर्जना गर्छ। मेथडहरू परिभाषित गर्नुहोस्, तिनीहरूलाई `@Tool` ले एनोटेट गर्नुहोस्, र LangChain4j ले व्यवस्थापन गर्छ। AI आवश्यक पर्दा स्वचालित रूपमा तपाईंका Java मेथडहरू कल गर्छ।

**MessageWindowChatMemory** - संवाद इतिहास जोगाउँछ। यसको बिना, प्रत्येक अनुरोध स्वतन्त्र हुन्छ। यसको साथ, AI ले पहिलेका सन्देशहरू सम्झन्छ र धेरै वाक्यहरूमा सन्दर्भ कायम राख्छ।

<img src="../../../translated_images/ne/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j वास्तुकला - मुख्य कम्पोनेन्टहरूले सँगै काम गरी तपाईंका AI अनुप्रयोगहरूलाई शक्ति दिन्छ*

## LangChain4j निर्भरता

यो छिटो सुरु Maven मा तीन निर्भरता प्रयोग गर्दछ जुन [`pom.xml`](../../../00-quick-start/pom.xml) मा छन्:

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

`langchain4j-open-ai-official` मोड्युलले `OpenAiOfficialChatModel` क्लास प्रदान गर्दछ जुन OpenAI-अनुकूल API हरूसँग जडान हुन्छ। GitHub Models पनि उही API ढाँचा प्रयोग गर्दछ, त्यसैले कुनै विशेष एडेप्टर आवश्यक छैन - बस आधार URL लाई `https://models.github.ai/inference` मा सेट गर्नुहोस्।

`langchain4j-easy-rag` मोड्युलले स्वतः दस्तावेज विभाजन, एम्बेडिङ्ग, र पुनःप्राप्ति प्रदान गर्दछ जसले तपाईंलाई प्रत्येक चरण म्यानुअली कन्फिगर नगरी RAG अनुप्रयोगहरू निर्माण गर्न मद्दत गर्छ।

## पूर्वआवश्यकताहरू

**Dev Container प्रयोग गर्दै हुनुहुन्छ?** Java र Maven पहिले नै इन्स्टल छन्। तपाईंलाई केवल GitHub व्यक्तिगत पहुँच टोकन चाहिन्छ।

**स्थानीय विकास:**
- Java 21+ , Maven 3.9+
- GitHub व्यक्तिगत पहुँच टोकन (तलका निर्देशनहरू)

> **सूचना:** यो मोड्युलले GitHub Models बाट `gpt-4.1-nano` प्रयोग गर्दछ। कोडमा मोडेल नाम परिवर्तन नगर्नुहोस् - यो GitHub का उपलब्ध मोडेलहरूसँग काम गर्न कन्फिगर गरिएको छ।

## सेटअप

### 1. आफ्नो GitHub टोकन प्राप्त गर्नुहोस्

1. जानुहोस् [GitHub सेटिङ → व्यक्तिगत पहुँच टोकनहरू](https://github.com/settings/personal-access-tokens)
2. "Generate new token" क्लिक गर्नुहोस्
3. व्याख्यात्मक नाम राख्नुहोस् (उदाहरणका लागि, "LangChain4j Demo")
4. म्याद निर्धारण गर्नुहोस् (7 दिन सिफारिस)
5. "Account permissions" अन्तर्गत "Models" खोज्नुहोस् र "Read-only" सेट गर्नुहोस्
6. "Generate token" क्लिक गर्नुहोस्
7. टोकन कपी गरेर सुरक्षित गर्नुहोस् - यो फेरि देखिँदैन

### 2. आफ्नो टोकन सेट गर्नुहोस्

**विकल्प 1: VS Code प्रयोग गर्दै (सिफारिस गरिएको)**

यदि तपाईं VS Code प्रयोग गर्दै हुनुहुन्छ भने, प्रोजेक्ट मूलमा रहेको `.env` फाइलमा टोकन थप्नुस्:

यदि `.env` फाइल छैन भने `.env.example` बाट `.env` मा कपी गर्नुहोस् वा नयाँ `.env` फाइल बनाउनुहोस्।

**उदाहरण `.env` फाइल:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env मा
GITHUB_TOKEN=your_token_here
```

यसपछि तपाईं कुनै पनि डेमो फाइल (जस्तै, `BasicChatDemo.java`) मा राइट-क्लिक गरेर **"Run Java"** चयन गर्न सक्नुहुन्छ वा Run र Debug प्यानलबाट लन्च कन्फिगरेसनहरू प्रयोग गर्न सक्नुहुन्छ।

**विकल्प 2: टर्मिनल प्रयोग गर्दै**

टोकनलाई वातावरण चरको रूपमा सेट गर्नुहोस्:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## उदाहरणहरू चलाउनुहोस्

**VS Code प्रयोग गर्दै:** कुनै पनि डेमो फाइलमा राइट-क्लिक गरेर **"Run Java"** क्लिक गर्नुहोस्, वा Run र Debug प्यानलबाट लन्च कन्फिगरेसन प्रयोग गर्नुहोस् (पहिले `.env` फाइलमा टोकन थप्न नभुल्नुहोस्)।

**Maven प्रयोग गर्दै:** वैकल्पिक रूपमा, कमाण्ड लाइनबाट चलाउन सक्नुहुन्छ:

### 1. आधारभूत कुराकानी

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. प्रॉम्प्ट ढाँचाहरू

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

यहाँ शून्य-शट, केही-शट, चिन्तन श्रृंखला, र भूमिका-आधारित प्रॉम्प्टिङ देखाइन्छ।

### 3. फङ्क्शन कलिङ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI आवश्यक पर्दा तपाईंका Java मेथडहरूलाई स्वचालित रूपमा कल गर्छ।

### 4. दस्तावेज Q&A (सजिलो RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

आफ्ना दस्तावेजहरू बारे प्रश्न सोध्नुहोस् सजिलो RAG प्रयोग गरी जसमा स्वतः एम्बेडिङ र पुनःप्राप्ति हुन्छ।

### 5. जिम्मेवार AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

हेर्नुहोस् कसरी AI सुरक्षा फिल्टरले हानिकारक सामग्रीलाई अवरुद्ध गर्छ।

## हरेक उदाहरणले के देखाउँछ

**आधारभूत कुराकानी** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

यहाँबाट सुरु गर्नुहोस् र LangChain4j को सबैभन्दा साधारण रूप हेर्नुहोस्। तपाईंले `OpenAiOfficialChatModel` सिर्जना गर्नुहुनेछ, `.chat()` प्रयोग गरेर प्रॉम्प्ट पठाउनुहुनेछ, र प्रतिक्रिया प्राप्त गर्नुहुनेछ। यसले आधारभूत कुरा देखाउँछ: कसरी कस्टम एन्डप्वाइन्ट र API कुञ्जीहरूका साथ मोडेलहरू आरम्भ गर्ने। यो ढाँचा बुझिसकेपछि सबैकुरा यसको आधारमा निर्माण हुनेछ।

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैटसँग प्रयास गर्नुहोस्:** खोल्नुहोस् [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) र सोध्नुहोस्:
> - "यो कोडमा GitHub Models बाट Azure OpenAI मा कसरि सर्न सक्छु?"
> - "OpenAiOfficialChatModel.builder() मा कस्ता अन्य प्यारामिटरहरू निर्धारण गर्न सकिन्छ?"
> - "पूर्ण प्रतिक्रिया कुर्नुको सट्टा कसरी स्ट्रिमिंग प्रतिक्रिया थप्न सकिन्छ?"

**प्रॉम्प्ट इन्जिनियरिङ** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

अब जब तपाईं मोडेलसँग कुरा गर्न जान्नु हुन्छ, यसलाई के भन्नु पर्ने कुरा अन्वेषण गरौं। यो डेमोमा एउटै मोडेल सेटअप प्रयोग गरेर पाँच विभिन्न प्रॉम्प्टिङ ढाँचाहरू देखाइन्छ। छोटो निर्देशहरूका लागि शून्य-शट प्रॉम्प्ट, उदाहरणहरूबाट सिक्ने केही-शट प्रॉम्प्ट, चिन्तनको श्रृंखला प्रॉम्प्ट, र सन्दर्भ सेट गर्ने भूमिका-आधारित प्रॉम्प्ट प्रयास गर्नुहोस्। तपाईं हेर्नुहुनेछ कि एउटै मोडेलले फरक-फरक परिणामहरू कसरि दिन्छ आधारित तपाईंको अनुरोधको फ्रेमिंगमा।

यो डेमो प्रॉम्प्ट टेम्प्लेटहरू पनि देखाउँछ, जुन चरहरू सहित पुनः प्रयोगयोग्य प्रॉम्प्टहरू बनाउन शक्तिशाली तरिका हो।
तलको उदाहरणले LangChain4j `PromptTemplate` प्रयोग गरी चरहरू भर्ने प्रॉम्प्ट देखाउँछ। AI ले प्रदान गरिएको गन्तव्य र क्रियाकलापको आधारमा उत्तर दिनेछ।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैटसँग प्रयास गर्नुहोस्:** खोल्नुहोस् [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) र सोध्नुहोस्:
> - "शून्य-शट र केही-शट प्रॉम्प्टिङमा के फरक छ, र कहिले कुन प्रयोग गर्ने?"
> - "तापक्रम प्यारामिटरले मोडेलको प्रतिक्रियामा कस्तो असर गर्छ?"
> - "उत्पादनमा प्रॉम्प्ट इन्जेक्सन आक्रमण रोक्न के के उपायहरू छन्?"
> - "साधारण ढाँचाहरूका लागि पुनः प्रयोगयोग्य PromptTemplate वस्तुहरू कसरी बनाउन सकिन्छ?"

**टुल एकीकरण** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

यहाँ LangChain4j शक्तिशाली हुन्छ। तपाईं `AiServices` प्रयोग गरी AI सहायक बनाउनुहुनेछ जुन तपाईंका Java मेथडहरू कल गर्न सक्छ। मेथडहरूलाई `@Tool("description")` एनोटेट गर्नुहोस् र LangChain4j बाँकी काम गर्छ - AI ले प्रयोगकर्ता के सोध्छ त्यस आधारमा कुन उपकरण प्रयोग गर्ने स्वतः निर्णय गर्दछ। यसले फङ्क्शन कलिङ देखाउँछ, जुन AI लाई क्रियाहरू लिन सक्षम बनाउनका लागि मुख्य प्रविधि हो, केवल उत्तर दिन मात्र होइन।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैटसँग प्रयास गर्नुहोस्:** खोल्नुहोस् [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) र सोध्नुहोस्:
> - "@Tool एनोटेसन कसरी काम गर्छ र यसपछि LangChain4j के गर्छ?"
> - "AI ले जटिल समस्याहरू समाधान गर्न सिक्वेन्समा धेरै उपकरणहरू कल गर्न सक्छ?"
> - "यदि उपकरणले अपवाद फाल्यो भने के हुन्छ - त्रुटिहरू कसरी व्यवस्थापन गर्ने?"
> - "यो क्याल्कुलेटर उदाहरणको सट्टा असली API कसरी एकीकृत गर्ने?"

**दस्तावेज Q&A (सजिलो RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

यहाँ LangChain4j को "Easy RAG" विधि प्रयोग गरी RAG (retrieval-augmented generation) देख्नुहुनेछ। दस्तावेज लोड गरी स्वतः विभाजन र एम्बेड गरिएको छ, त्यसपछि सामग्री पुन:प्राप्ति प्रणालीले सर्च समयमा AI लाई उपयुक्त हिस्सा(चंक) दिन्छ। AI तपाईंका दस्तावेजहरूमा आधारित उत्तर दिन्छ, सामान्य ज्ञानमा होइन।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैटसँग प्रयास गर्नुहोस्:** खोल्नुहोस् [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) र सोध्नुहोस्:
> - "RAG ले AI हल्युसिनेशनलाई मोडेलको तालिम डाटाको तुलनामा कसरी रोक्छ?"
> - "यो सजिलो तरिका र कस्टम RAG पाइपलाइनमा के फरक छ?"
> - "कसरी मल्टिपल दस्तावेजहरू वा ठूलो ज्ञान भण्डारलाई यो विधि स्केल गर्ने?"

**जिम्मेवार AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

गहिराइमा सुरक्षा निर्माण गर्नुहोस्। यस डेमोले दुई तहको सुरक्षा देखाउँछ जुन सँगै काम गर्छ:

**भाग 1: LangChain4j इनपुट गार्डरेल्स** - खतरनाक प्रॉम्प्टहरू LLM सम्म पुग्नुअघि रोक्नुहोस्। प्रतिबन्धित कुञ्जीशब्द वा ढाँचाहरू जाँच्ने कस्टम गार्डरेल्स सिर्जना गर्नुहोस्। यी तपाईंको कोडमा चल्छन्, त्यसैले द्रुत र निःशुल्क छन्।

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

**भाग 2: प्रदायक सुरक्षा फिल्टरहरू** - GitHub Models मा निर्मित फिल्टरहरू छन् जसले तपाईंका गार्डरेल्सले छुटाउन सक्ने कुरा समात्छ। तपाईंलाई कडा ब्लकहरू (HTTP 400 त्रुटिहरू) र मृदु अस्वीकारहरू देखिनेछन् जहाँ AI विनम्रतापूर्वक अस्वीकार गर्छ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैटसँग प्रयास गर्नुहोस्:** खोल्नुहोस् [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) र सोध्नुहोस्:
> - "InputGuardrail के हो र मैले मेरो बनाउन कसरी सक्छु?"
> - "कडा ब्लक र मृदु अस्वीकारमा के फरक छ?"
> - "गर्डरेल्स र प्रदायक फिल्टरहरूलाई एकसाथ किन प्रयोग गर्ने?"

## अगाडि के गर्ने

**अगिल्लो मोड्युल:** [01-introduction - LangChain4j र gpt-5 सँग Azure मा सुरु](../01-introduction/README.md)

---

**नेभिगेसन:** [← मुख्यमा फर्कनुहोस्](../README.md) | [अर्को: Module 01 - परिचय →](../01-introduction/README.md)

---

## समस्या समाधान

### पहिलो पटक Maven बिल्ड

**समस्या:** पहिलो `mvn clean compile` वा `mvn package` धेरै समय (10-15 मिनेट) लिन सक्छ

**कारण:** Maven ले सबै निर्भरता (Spring Boot, LangChain4j पुस्तकालयहरू, Azure SDK हरू आदि) पहिलो पटक डाउनलोड गर्नुपर्छ।

**समाधान:** यो सामान्य प्रक्रिया हो। पछिल्ला बिल्डहरू छिटो हुन्छन् किनकि निर्भरता स्थानीय रूपमा क्यास हुन्छ। डाउनलोड समय तपाईंको नेटवर्क गति मा निर्भर गर्दछ।

### PowerShell Maven कमाण्ड सिंट्याक्स

**समस्या:** Maven कमाण्डहरू `Unknown lifecycle phase ".mainClass=..."` त्रुटि देखाउँछन्।
**कारण**: PowerShell ले `=` लाई भेरिएबल असाइनमेन्ट अपरेटरको रूपमा व्याख्या गर्दछ, जसले Maven सम्पत्ति सिन्ट्याक्स टुक्रिन्छ

**समाधान**: Maven आदेश भन्दा पहिले स्टप-पार्सिंग अपरेटर `--%` प्रयोग गर्नुहोस्:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` अपरेटरले PowerShell लाई बाँकी रहेका सबै आर्गुमेन्टहरू Maven लाई बिना व्याख्या अक्षरशः पठाउन सूचित गर्छ।

### Windows PowerShell इमोजी प्रदर्शन

**समस्या**: PowerShell मा इमोजीहरूको सट्टा फोहोर अक्षरहरू (जस्तै `????` वा `â??`) देखिन्छन्

**कारण**: PowerShell को डिफल्ट इन्कोडिङ UTF-8 इमोजी समर्थन गर्दैन

**समाधान**: Java एप्लीकेशनहरू चलाउनुभन्दा पहिले यस आदेशलाई चलाउनुहोस्:
```cmd
chcp 65001
```

यसले टर्मिनलमा UTF-8 इन्कोडिङलाई जबरजस्ती लागू गर्छ। वैकल्पिक रूपमा, राम्रो युनिकोड समर्थन भएको Windows Terminal प्रयोग गर्नुहोस्।

### API कलहरू डिबग गर्ने

**समस्या**: प्रमाणीकरण त्रुटिहरू, दर सीमाहरू, वा AI मोडेलबाट अनपेक्षित प्रतिक्रियाहरू

**समाधान**: उदाहरणहरूमा `.logRequests(true)` र `.logResponses(true)` समावेश गरिएको छ जसले API कलहरू कन्सोलमा देखाउँछ। यसले प्रमाणीकरण त्रुटिहरू, दर सीमाहरू वा अनपेक्षित प्रतिक्रियाहरूको समस्या समाधान गर्न मद्दत गर्छ। उत्पादनमा यी झण्डाहरू हटाएर लग शोर कम गर्न सकिन्छ।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यस कागजातलाई AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) को प्रयोग गरी अनुवाद गरिएको हो। जबकि हामी सटीकता सुनिश्चित गर्ने प्रयास गर्दछौं, कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादमा त्रुटिहरू वा असमझदारीहरू हुन सक्दछन्। मूल कागजात यसको मूल भाषामा आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीको लागि व्यावसायिक मानव अनुवादको सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट हुने कुनै पनि गलतफहमी वा त्रुटिका लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->