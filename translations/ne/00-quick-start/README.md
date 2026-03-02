# मोड्युल 00: छिटो सुरु

## सामग्री तालिका

- [परिचय](../../../00-quick-start)
- [LangChain4j के हो?](../../../00-quick-start)
- [LangChain4j निर्भरता](../../../00-quick-start)
- [आवश्यकताहरू](../../../00-quick-start)
- [सेटअप](../../../00-quick-start)
  - [1. आफ्नो GitHub टोकन प्राप्त गर्नुहोस्](../../../00-quick-start)
  - [2. आफ्नो टोकन सेट गर्नुहोस्](../../../00-quick-start)
- [उदाहरणहरू चलाउनुहोस्](../../../00-quick-start)
  - [1. आधारभूत कुरा](../../../00-quick-start)
  - [2. प्रॉम्प्ट ढाँचा](../../../00-quick-start)
  - [3. फंक्शन कलिङ](../../../00-quick-start)
  - [4. कागजात प्रश्नोत्तर (Easy RAG)](../../../00-quick-start)
  - [5. जिम्मेवार AI](../../../00-quick-start)
- [हरेक उदाहरणले के देखाउँछ](../../../00-quick-start)
- [अर्को कदमहरू](../../../00-quick-start)
- [समस्या समाधान](../../../00-quick-start)

## परिचय

यो छिटो सुरु तपाईंलाई LangChain4j सँग छिटो काम सुरु गर्नका लागि बनाइएको हो। यसले LangChain4j र GitHub मोडलहरूसँग AI अनुप्रयोगहरू निर्माण गर्ने आधारभूत कुराहरू समेट्छ। अर्को मोड्युलहरूमा तपाईं Azure OpenAI र GPT-5.2 मा स्विच गर्नुहुनेछ र प्रत्येक अवधारणामा गहिराइमा जानुहुनेछ।

## LangChain4j के हो?

LangChain4j एक जाभा लाइब्रेरी हो जसले AI चलित अनुप्रयोगहरू बनाउन सजिलो बनाउँछ। HTTP क्लाइन्ट र JSON पार्सिङ गर्नुपर्ने ठाउँमा तपाईं सफा जाभा API हरूसँग काम गर्नुहुन्छ।

LangChain मा "चेन" ले धेरै कम्पोनेन्टहरू जोड्ने प्रक्रिया जनाउँछ - तपाईं एउटा प्रॉम्प्टलाई मोडलमा, त्यसपछि पार्सरमा जोड्न सक्नुहुन्छ, वा धेरै AI कलहरूलाई एकआपसमा श्रृंखलाबद्ध गर्न सक्नुहुन्छ जहाँ एउटा आउटपुट अर्को इनपुटमा जान्छ। यो छिटो सुरु आधारभूत कुरामा केन्द्रित छ र जटिल श्रृंखलाहरू अन्वेषण गर्नु अघि।

<img src="../../../translated_images/ne/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j मा कम्पोनेन्टहरू श्रृंखला - निर्माण ब्लकहरूलाई जोडेर शक्तिशाली AI वर्कफ्लोहरू सिर्जना*

हामी तीन मुख्य कम्पोनेन्टहरू प्रयोग गर्नेछौं:

**ChatModel** - AI मोडलसँग अन्तरक्रिया गर्ने इन्टरफेस। `model.chat("prompt")` कल गरेर प्रतिक्रिया स्ट्रिङ प्राप्त गर्नुहोस्। हामी `OpenAiOfficialChatModel` प्रयोग गर्छौं जुन OpenAI-अनुकूल API अन्तर्वर्तीहरूसँग काम गर्छ जस्तै GitHub Models।

**AiServices** - प्रकार-सुरक्षित AI सेवा इन्टरफेसहरू सिर्जना गर्छ। मेथडहरू परिभाषित गर्नुहोस्, तिनीहरूलाई `@Tool` ले एनोटेट गर्नुहोस् र LangChain4j सञ्चालन सम्हाल्छ। आवश्यक पर्दा AI ले तपाईंका Java मेथडहरू स्वचालित रूपमा कल गर्छ।

**MessageWindowChatMemory** - संवाद इतिहास राख्छ। यसको बिना, प्रत्येक अनुरोध स्वतन्त्र हुन्छ। यसको साथमा, AI ले पहिलेका सन्देशहरू सम्झन्छ र धेरै पटकको कुराकानीमा सन्दर्भ कायम राख्छ।

<img src="../../../translated_images/ne/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j संरचना - मुख्य कम्पोनेन्टहरू मिलेर तपाईंको AI अनुप्रयोगहरूलाई शक्ति दिँदै*

## LangChain4j निर्भरता

यो छिटो शुरू [`pom.xml`](../../../00-quick-start/pom.xml) मा तीन Maven निर्भरता प्रयोग गर्दछ:

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

`langchain4j-open-ai-official` मोड्युल `OpenAiOfficialChatModel` वर्ग प्रदान गर्छ जुन OpenAI अनुकूल API सँग जडान गर्छ। GitHub Models ले त्यहि API ढाँचा प्रयोग गर्दछ, त्यसैले कुनै विशेष एडाप्टर आवश्यक छैन - मात्र बेस URL लाई `https://models.github.ai/inference` मा सेट गर्नुहोस्।

`langchain4j-easy-rag` मोड्युलले स्वचालित रूपमा कागजात छुट्याउने, एम्बेड गर्ने, र पुनःप्राप्ति गर्ने सुविधा दिन्छ ताकि तपाईं RAG अनुप्रयोगहरू म्यानुअली सेट अप नगरी बनाउन सक्नुहुन्छ।

## आवश्यकताहरू

**डेभ कन्टेनर प्रयोग गर्दै हुनुहुन्छ?** Java र Maven पहिले नै स्थापना गरिएको छ। तपाईंलाई केवल GitHub Personal Access Token चाहिए।

**स्थानीय विकास:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (निर्देश तल)

> **ध्यान दिनुहोस्:** यो मोड्युलले GitHub Models बाट `gpt-4.1-nano` प्रयोग गर्छ। कोडमा मोडल नाममा परिवर्तन नगर्नुहोस् - यो GitHub को उपलब्ध मोडलहरूसँग काम गर्न कन्फिगर गरिएको छ।

## सेटअप

### 1. आफ्नो GitHub टोकन प्राप्त गर्नुहोस्

1. [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens) मा जानुहोस्
2. "Generate new token" मा क्लिक गर्नुहोस्
3. वर्णनात्मक नाम दिनुहोस् (जस्तै, "LangChain4j Demo")
4. समाप्ति अवधि सेट गर्नुहोस् (७ दिन सिफारिस गरिएको)
5. "Account permissions" अन्तर्गत "Models" खोज्नुहोस् र "Read-only" मा सेट गर्नुहोस्
6. "Generate token" मा क्लिक गर्नुहोस्
7. टोकन कपी गरी सुरक्षित राख्नुहोस् - यसलाई फेरि हेर्न सकिन्न

### 2. आफ्नो टोकन सेट गर्नुहोस्

**विकल्प 1: VS Code प्रयोग गर्दै (सिफारिस गरिएको)**

यदि तपाईं VS Code प्रयोग गर्दै हुनुहुन्छ भने, आफ्नो टोकन परियोजना मूलमा `.env` फाइलमा राख्नुहोस्:

यदि `.env` फाइल छैन भने, `.env.example` बाट `.env` मा कपी गर्नुहोस् वा नयाँ `.env` फाइल सिर्जना गर्नुहोस्।

**उदाहरण `.env` फाइल:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env मा
GITHUB_TOKEN=your_token_here
```

त्यसपछि तपाईं कुनै पनि डेमो फाइल (जस्तै, `BasicChatDemo.java`) मा राइट-क्लिक गरी **"Run Java"** चयन गर्न सक्नुहुन्छ वा Run र Debug प्यानलबाट लन्च कन्फिगरेसन प्रयोग गर्न सक्नुहुन्छ।

**विकल्प 2: टर्मिनल प्रयोग गर्दै**

टोकनलाई वातावरण चर रूपमा सेट गर्नुहोस्:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## उदाहरणहरू चलाउनुहोस्

**VS Code प्रयोग गरी:** कुनै पनि डेमो फाइलमा राइट-क्लिक गरी **"Run Java"** चयन गर्नुहोस्, वा Run र Debug प्यानलबाट लन्च कन्फिगरेसन प्रयोग गर्नुहोस् (पहिले `.env` फाइलमा टोकन थपिएको हुनुपर्छ)।

**Maven प्रयोग गरी:** वा तपाईं कमाण्ड लाइनबाट चलाउन सक्नुहुन्छ:

### 1. आधारभूत कुरा

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. प्रॉम्प्ट ढाँचा

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

जिरो-शट, फ्यु-शट, चेन-ऑफ-थट र रोल-आधारित प्रॉम्प्टिङ देखाउँछ।

### 3. फंक्शन कलिङ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI आवश्यक पर्दा स्वचालित रूपमा तपाईंका Java मेथडहरू कल गर्छ।

### 4. कागजात प्रश्नोत्तर (Easy RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

Easy RAG प्रयोग गरेर तपाईंका कागजातहरूको बारेमा प्रश्न सोध्नुहोस् - स्वचालित एम्बेडिङ र पुनःप्राप्ति सहित।

### 5. जिम्मेवार AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI सुरक्षा फिल्टरहरूले हानिकारक सामग्री कसरी रोक्छन् हेर्नुहोस्।

## हरेक उदाहरणले के देखाउँछ

**आधारभूत कुरा** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

यहाँबाट सुरु गर्नुहोस् र LangChain4j को आधारभूत परिचय पाउनुहोस्। तपाईं `OpenAiOfficialChatModel` सिर्जना गर्नुहुनेछ, `.chat()` मार्फत प्रॉम्प्ट पठाउनुहुनेछ, र प्रतिक्रिया पाउनुहुनेछ। यसले आधारभूत कुरा देखाउँछ: कसरी कस्टम अन्तर्वर्ती र API कुञ्जीहरूसँग मोडल आरम्भ गर्ने। जब तपाईं यो प्रक्रिया बुझ्नुहुन्छ, बाँकी सबै त्यही आधारमा निर्माण हुन्छ।

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 GitHub Copilot संग प्रयास गर्नुहोस्:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) खोल्नुहोस् र सोध्नुहोस्:
> - "यस कोडमा GitHub Models बाट Azure OpenAI मा कसरि स्विच गर्ने?"
> - "OpenAiOfficialChatModel.builder() मा अरु के-के प्यारामिटरहरू सेट गर्न सकिन्छ?"
> - "पूर्ण प्रतिक्रिया कुर्नुको सट्टा स्ट्रीमिङ प्रतिक्रिया कसरी थप्ने?"

**प्रॉम्प्ट इन्जिनियरिङ** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

अब तपाईं मोडलसँग कसरी कुरा गर्ने बुझ्नुभएपछि, हामी हेर्ने छौं के तपाईं मोडललाई भन्नुहुन्छ। यो डेमोले तिनै मोडल सेटअप प्रयोग गरेर पाँच फरक प्रॉम्प्ट ढाँचाहरू देखाउँछ। सिधा निर्देशनका लागि जिरो-शट प्रॉम्प्ट, उदाहरणबाट सिक्न फ्यु-शट, कारण देखाउने चेन-ऑफ-थट, र सन्दर्भ सेट गर्ने रोल-आधारित प्रॉम्प्ट प्रयास गर्नुहोस्। तपाईं देख्नुहुनेछ कि एउटै मोडलले अनुरोध राख्ने तरिकामा धेरै भिन्न नतिजा दिन्छ।

डेमोले प्रॉम्प्ट टेम्प्लेटहरू पनि देखाउँछ, जुन चलायमान भेरिएबलहरू सहित पुन: प्रयोग गर्न मिल्ने प्रॉम्प्टहरू बनाउने शक्तिशाली विधि हो।
तलको उदाहरणले LangChain4j `PromptTemplate` प्रयोग गरेर भेरिएबलहरू भर्ने तरिका देखाउँछ। AI ले दिएका गन्तव्य र गतिविधिको आधारमा उत्तर दिन्छ।

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

> **🤖 GitHub Copilot सँग प्रयास गर्नुहोस्:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) खोल्नुहोस् र सोध्नुहोस्:
> - "जिरो-शट र फ्यु-शट प्रॉम्प्टिङमा के फरक छ र कुन बेला कुन प्रयोग गर्ने?"
> - "मोडलका प्रतिक्रियामा तापक्रम प्यारामिटरले कसरि असर गर्छ?"
> - "उत्पादनमा प्रॉम्प्ट इन्जेक्सन आक्रमण रोक्न के-के प्रविधिहरू छन्?"
> - "साझा प्रॉम्प्ट ढाँचाहरूका लागि कसरी पुन: प्रयोगयोग्य PromptTemplate वस्तुहरू बनाउन सकिन्छ?"

**टूल एकीकरण** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

यहाँ LangChain4j शक्ति देखाइन्छ। तपाईं `AiServices` प्रयोग गरेर यस्तो AI सहायक बनाउन सक्नुहुन्छ जसले तपाईंका Java मेथडहरू कल गर्न सक्छ। मेथडहरूलाई `@Tool("description")` ले एनोटेट गर्नुहोस् र बाँकी LangChain4j ले सँभाल्छ - AI प्रयोगकर्ताले के सोध्छन् अनुसार कुन टूल प्रयोग गर्ने निर्णय स्वतः गर्छ। यसले फंक्शन कलिङ देखाउँछ, जुन AI लाई क्रियाकलाप गर्न सक्षम बनाउने मुख्य प्रविधि हो, मात्र प्रश्नको उत्तर दिन मात्र होइन।

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

> **🤖 GitHub Copilot सँग प्रयास गर्नुहोस्:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) खोल्नुहोस् र सोध्नुहोस्:
> - "@Tool एनोटेशन कसरी काम गर्छ र LangChain4j ले यसलाई पछाडि कसरी ह्यान्डल गर्छ?"
> - "के AI ले जटिल समस्याहरू समाधान गर्न धेरै टूलहरू अनुक्रममा कल गर्न सक्छ?"
> - "यदि कुनै टूलले अपवाद फ्याँक्छ भने के हुन्छ - त्रुटिहरू कसरी ह्यान्डल गर्ने?"
> - "यस क्याल्कुलेटर उदाहरणको सट्टा वास्तविक API कसरी एकीकृत गर्ने?"

**कागजात प्रश्नोत्तर (Easy RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

यहाँ LangChain4j को "Easy RAG" विधि प्रयोग गरेर RAG (retrieval-augmented generation) देख्नुहुनेछ। कागजातहरू लोड गरिन्छ, स्वचालित रूपमा भागमा विभाजित हुँदै इन-मेमोरी स्टोरमा एम्बेड गरिन्छ, अनि सामग्री पुनःप्राप्तिकर्ता सोधपुछको समयमा AI लाई सान्दर्भिक भागहरू आपूर्ति गर्छ। AI तपाईंका कागजातहरूमा आधारित जवाफ दिन्छ, सामान्य ज्ञानमा होइन।

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

> **🤖 GitHub Copilot सँग प्रयास गर्नुहोस्:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) खोल्नुहोस् र सोध्नुहोस्:
> - "RAG ले मोडलको तालिम डाटाको सट्टा AI भ्रमलाई कसरी रोक्छ?"
> - "यस सजिलो विधि र कस्टम RAG पाइपलाइनमा के फरक छ?"
> - "धेरै कागजातहरू वा ठूलो ज्ञान आधारको लागि यसलाई कसरी मापन गर्ने?"

**जिम्मेवार AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

सुरक्षा मा गहिराइ सहित AI सुरक्षा बनाउनुहोस्। यो डेमोले दुई तहको सुरक्षात्मक उपायहरू देखाउँछ:

**भाग 1: LangChain4j इनपुट गार्डरेल्स** - खतरनाक प्रॉम्प्टहरूलाई LLM मा पुग्नु अघि रोक्नुहोस्। प्रतिबन्धित कीवर्ड वा ढाँचाहरू जाँच गर्न कस्टम गार्डरेलहरू बनाउनुहोस्। यी तपाईंको कोडमा चल्छन्, त्यसैले छिटो र निःशुल्क छन्।

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

**भाग 2: प्रदायक सुरक्षा फिल्टरहरू** - GitHub Models मा निर्मित फिल्टरहरू छन् जसले तपाईंको गार्डरेलहरूले छुटाउन सक्ने कुराहरू समात्छ। तपाईं कडाइका कारण कडा ब्लकहरू (HTTP 400 त्रुटिहरू) र नम्र अस्वीकृतिहरू जहाँ AI विनम्रतापूर्वक अस्वीकार्छ, दुवै देख्नुहुनेछ।

> **🤖 GitHub Copilot सँग प्रयास गर्नुहोस्:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) खोल्नुहोस् र सोध्नुहोस्:
> - "InputGuardrail के हो र म कसरी आफ्नो बनाउने?"
> - "कडा ब्लक र नम्र अस्वीकृतिमा के फरक छ?"
> - "किन दुवै गार्डरेल र प्रदायक फिल्टर एकसाथ प्रयोग गर्ने?"

## अर्को कदमहरू

**अर्को मोड्युल:** [01-परिचय - LangChain4j सँग सुरु](../01-introduction/README.md)

---

**नेभिगेसन:** [← मुख्य पृष्ठमा फर्कनुहोस्](../README.md) | [अर्को: मोड्युल 01 - परिचय →](../01-introduction/README.md)

---

## समस्या समाधान

### पहिलोपटक Maven बिल्ड

**समस्या:** प्रारम्भिक `mvn clean compile` वा `mvn package` धेरै समय लिन्छ (१०-१५ मिनेट)

**कारण:** Maven ले पहिलोपटक सबै परियोजना निर्भरता (Spring Boot, LangChain4j लाइब्रेरीहरू, Azure SDKs, आदि) डाउनलोड गर्नुपर्छ।

**समाधान:** यो सामान्य हुन्छ। अर्को बिल्डहरू धेरै छिटो हुनेछ, किनभने निर्भरता स्थानीय रूपमा क्यास हुन्छन्। डाउनलोड समय तपाईंको इन्टरनेट स्पीडमा निर्भर गर्दछ।

### PowerShell Maven कमाण्ड सिन्ट्याक्स

**समस्या:** Maven कमाण्डहरूले `Unknown lifecycle phase ".mainClass=..."` त्रुटि दिन्छ।
**कारण**: PowerShell ले `=` लाई भेरिएबल असाइनमेन्ट अपरेटरको रूपमा व्याख्या गर्छ, जसले Maven प्रोपर्टी सिन्ट्याक्सलाई तोड्छ

**समाधान**: Maven कमाण्ड अघि स्टप-पार्सिङ अपरेटर `--%` प्रयोग गर्नुहोस्:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` अपरेटरले PowerShell लाई बाँकी सबै आर्गुमेन्टहरू Maven लाइ उद्धृत नगरिकन सिधा पास गर्न निर्देशन दिन्छ।

### Windows PowerShell इमोजी प्रदर्शन

**समस्या**: PowerShell मा AI प्रतिक्रियाहरूमा इमोजीको सट्टा बेकार पात्रहरू (जस्तै `????` वा `â??`) देखिन्छ

**कारण**: PowerShell को डिफल्ट एन्कोडिङ UTF-8 इमोजी समर्थन गर्दैन

**समाधान**: Java एप्लिकेसनहरू चलाउन अघिल्लो यो कमाण्ड चलाउनुहोस्:
```cmd
chcp 65001
```

यसले टर्मिनलमा UTF-8 एन्कोडिङ बलियो बनाउँछ। विकल्पका रूपमा, Windows Terminal प्रयोग गर्नुहोस् जुन राम्रो युनिकोड समर्थन छ।

### API कलहरू डिबग गर्ने

**समस्या**: प्रमाणिकरण त्रुटिहरू, रेट लिमिटहरू, वा AI मोडलबाट अप्रत्याशित प्रतिक्रिया

**समाधान**: उदाहरणहरूमा `.logRequests(true)` र `.logResponses(true)` समावेश छन् जसले कन्सोलमा API कलहरू देखाउँछ। यसले प्रमाणिकरण त्रुटि, रेट लिमिट, वा अप्रत्याशित प्रतिक्रियाहरू ट्रबलशूट गर्न मद्दत गर्छ। उत्पादनमा यी फ्ल्यागहरू हटाएर लगिङ आवाज कम गर्न सकिन्छ।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
यस दस्तावेजलाई AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरी अनुवाद गरिएको हो। हामी शुद्धताका लागि प्रयासरत छौं भने पनि, कृपया बुझ्नुहोस् कि स्वचालित अनुवादहरूमा त्रुटि वा असत्यता हुन सक्छ। मूल दस्तावेज यसको स्वदेशी भाषामा आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीका लागि व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न कुनै पनि गलत बुझाइ वा गलत व्याख्याप्रति हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->