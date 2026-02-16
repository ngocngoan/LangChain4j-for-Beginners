# Module 00: छिटो सुरु गर्नुहोस्

## विषय सूची

- [परिचय](../../../00-quick-start)
- [LangChain4j के हो?](../../../00-quick-start)
- [LangChain4j निर्भरता](../../../00-quick-start)
- [पूर्वआवश्यकताहरू](../../../00-quick-start)
- [सेटअप](../../../00-quick-start)
  - [1. आफ्नो GitHub टोकन प्राप्त गर्नुहोस्](../../../00-quick-start)
  - [2. आफ्नो टोकन सेट गर्नुहोस्](../../../00-quick-start)
- [उदाहरणहरू चलाउनुहोस्](../../../00-quick-start)
  - [1. आधारभूत च्याट](../../../00-quick-start)
  - [2. प्रॉम्प्ट प्याटर्नहरू](../../../00-quick-start)
  - [3. फंक्शन कलिङ](../../../00-quick-start)
  - [4. दस्तावेज् प्रश्नोत्तर (RAG)](../../../00-quick-start)
  - [5. जिम्मेवार AI](../../../00-quick-start)
- [प्रत्येक उदाहरणले के देखाउँछ](../../../00-quick-start)
- [अर्को कदमहरू](../../../00-quick-start)
- [समस्या समाधान](../../../00-quick-start)

## परिचय

यो छिटो सुरु गर्ने गाइडले तपाईंलाई LangChain4j को साथ सकेसम्म छिटो चलाउन र काम गर्ने बनाउन मद्दत पुर्‍याउँछ। यसले LangChain4j र GitHub मोडेलहरू प्रयोग गरी AI अनुप्रयोगहरू निर्माण गर्ने अत्यावश्यक आधारहरू समेट्छ। आगामी मोड्युलहरूमा तपाईं Azure OpenAI सँग LangChain4j प्रयोग गरी थप जटिल अनुप्रयोगहरू बनाउनेछ।

## LangChain4j के हो?

LangChain4j एउटा Java लाइब्रेरी हो जसले AI-सञ्चालित अनुप्रयोगहरू बनाउने कामलाई सजिलो बनाउँछ। HTTP क्लाइन्ट र JSON पार्सिङसँग जटिलता कम गर्दै, तपाईंले सफा Java API हरूसँग काम गर्नुहुन्छ।

LangChain भित्रको "chain" भनेको विभिन्न कम्पोनेन्टहरूलाई सँगै जोड्नु हो - तपाईं प्रॉम्प्टलाई मोडेलसँग, त्यसपछि पार्सरसँग जोड्न सक्छन्, वा धेरै AI कलहरूलाई सँगै जडान गर्न सकिन्छ जहाँ एक आउटपुट अर्को इनपुटमा जान्छ। यस छिटो सुरुमा आधारभूत कुरामा ध्यान केन्द्रित गरिएको छ, जटिल चेनहरू विस्तारमा जानु अगाडि।

<img src="../../../translated_images/ne/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j मा कम्पोनेन्टहरूलाई जोड्दै - बलियो AI कार्यप्रवाहहरू बनाउन ब्लकहरू जडान*

हामीले तीन मुख्य कम्पोनेन्टहरू प्रयोग गर्नेछौं:

**ChatLanguageModel** - AI मोडेल संग अन्तरक्रिया गर्ने इन्टरफेस। `model.chat("prompt")` कल गर्नुहोस् र प्रतिक्रिया स्ट्रिंग प्राप्त गर्नुहोस्। हामी `OpenAiOfficialChatModel` प्रयोग गर्छौं जुन OpenAI-अनुकूल एन्डपोइन्टहरू जस्तै GitHub मोडेलहरूसँग काम गर्छ।

**AiServices** - प्रकार-सुरक्षित AI सेवा इन्टरफेसहरू सिर्जना गर्छ। मेथडहरू परिभाषित गर्नुहोस्, तिनीहरूलाई `@Tool` ले अङ्कित गर्नुहोस्, र LangChain4j अर्गनाइजेसन गर्छ। AI ले आवश्यक पर्दा तपाईंका Java मेथडहरूलाई स्वतः कल गर्छ।

**MessageWindowChatMemory** - कुराकानी इतिहासलाई कायम राख्छ। यसबिना, हरेक अनुरोध स्वतन्त्र हुन्छ। यससँग, AI ले विगतका सन्देशहरू सम्झन्छ र धेरै पल्टको संवादमा सन्दर्भ कायम राख्दछ।

<img src="../../../translated_images/ne/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j संरचना - मुख्य कम्पोनेन्टहरू मिलेर AI अनुप्रयोगहरू चलाउछ*

## LangChain4j निर्भरता

यस छिटो सुरुमा दुई Maven निर्भरता [`pom.xml`](../../../00-quick-start/pom.xml) फाइलमा प्रयोग गरिएको छ:

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

`langchain4j-open-ai-official` मोड्युलले `OpenAiOfficialChatModel` क्लास प्रदान गर्दछ जुन OpenAI-अनुकूल API हरूसँग जडान हुन्छ। GitHub मोडेलहरूले पनि उस्तै API ढाँचा प्रयोग गर्छन्, त्यसैले कुनै विशेष एडाप्टर आवश्यक छैन - केवल बेस URL `https://models.github.ai/inference` मा संकेत गर्नुहोस्।

## पूर्वआवश्यकताहरू

**Dev Container प्रयोग गर्दै हुनुहुन्छ?** Java र Maven पहिले नै इन्स्टल छन्। तपाईंलाई केवल GitHub Personal Access Token चाहिन्छ।

**स्थानीय विकासका लागि:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (तल निर्देशन)

> **नोट:** यस मोड्युलमा GitHub मोडेलहरूबाट `gpt-4.1-nano` मोडेल प्रयोग गरिएको छ। कोडमा मोडेल नाम परिवर्तन नगर्नुहोस् - यो GitHub मा उपलब्ध मोडेलहरूसँग काम गर्न कन्फिगर गरिएको छ।

## सेटअप

### 1. आफ्नो GitHub टोकन प्राप्त गर्नुहोस्

1. [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens) मा जानुहोस्
2. "Generate new token" क्लिक गर्नुहोस्
3. वर्णनात्मक नाम सेट गर्नुहोस् (जस्तै, "LangChain4j Demo")
4. म्याद अवधि सेट गर्नुहोस् (7 दिन सिफारिस गरिएको)
5. "Account permissions" अन्तर्गत "Models" खोज्नुहोस् र "Read-only" सेट गर्नुहोस्
6. "Generate token" क्लिक गर्नुहोस्
7. आफ्नो टोकन कपी गरी सुरक्षित राख्नुहोस् - फेरि देख्ने छैन

### 2. आफ्नो टोकन सेट गर्नुहोस्

**विकल्प 1: VS Code प्रयोग गर्दै (सिफारिस गरिएको)**

यदि VS Code प्रयोग गर्दै हुनुहुन्छ भने, आफ्नो प्रोजेक्ट रुटमा `.env` फाइलमा टोकन थप्नुहोस्:

`.env` फाइल नभएमा `.env.example` लाई `.env` मा कपी गर्नुहोस् वा नयाँ `.env` फाइल सिर्जना गर्नुहोस्।

**उदाहरण `.env` फाइल:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env मा
GITHUB_TOKEN=your_token_here
```

पछि तपाईं Explorer मा कुनै पनि डेमो फाइल (जस्तै `BasicChatDemo.java`) माथि राइट-क्लिक गरी **"Run Java"** चयन गर्न सक्नुहुन्छ वा Run र Debug प्यानलबाट लञ्च कन्फिगरेसनहरू प्रयोग गर्न सक्नुहुन्छ।

**विकल्प 2: टर्मिनलबाट**

टोकनलाई वातावरण परिवर्तनशिल (environment variable) को रूपमा सेट गर्नुहोस्:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## उदाहरणहरू चलाउनुहोस्

**VS Code प्रयोग गर्दै:** Explorer मा कुनै पनि डेमो फाइलमा राइट-क्लिक गरी **"Run Java"** चयन गर्नुहोस् वा Run र Debug प्यानल मार्फत कन्फिगरेसन प्रयोग गर्नुहोस् (पहिले `.env` फाइलमा टोकन थपिएको हुनुपर्छ)।

**Maven प्रयोग गरेर:** तपाईं कमाण्ड लाइनबाट पनि चलाउन सक्नुहुन्छ:

### 1. आधारभूत च्याट

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. प्रॉम्प्ट प्याटर्नहरू

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

शून्य-शट, केही-शट, चेन-ऑफ-थट, र भूमिका-आधारित प्रॉम्प्टिङ देखाउँछ।

### 3. फंक्शन कलिङ

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI ले आवश्यक पर्दा तपाईंका Java मेथडहरूलाई स्वतः कल गर्छ।

### 4. दस्तावेज् प्रश्नोत्तर (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt` को सामग्री सम्बन्धी प्रश्न सोध्नुहोस्।

### 5. जिम्मेवार AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

हेर्नुहोस् कसरी AI सुरक्षा फिल्टरहरूले हानिकारक सामग्रीलाई रोक्छ।

## प्रत्येक उदाहरणले के देखाउँछ

**आधारभूत च्याट** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

यहाँबाट सुरु गर्नुहोस् LangChain4j लाई सबैभन्दा सरल रूपले हेर्न। तपाईं `OpenAiOfficialChatModel` सिर्जना गर्नुहुनेछ, `.chat()` मार्फत प्रॉम्प्ट पठाउनुहुनेछ, र प्रतिक्रिया प्राप्त गर्नुहुनेछ। यसले आधार देखाउँछ: कसरी कस्टम एन्डपोइन्ट र API कुञ्जीहरूका साथ मोडेलहरू सुरु गर्ने। यो पैटर्न बुझ्नासाथ बाँकी सबै कुरा यसमा आधारित हुन्छ।

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) च्याट प्रयोग गरेर प्रयास गर्नुहोस्:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) खोल्नुहोस् र सोध्नुहोस्:
> - "म यो कोडमा GitHub मोडेलबाट Azure OpenAI मा कसरी स्विच गर्छु?"
> - "OpenAiOfficialChatModel.builder() मा कुन अन्य प्यारामिटरहरू कन्फिगर गर्न सक्छु?"
> - "पूर्ण प्रतिक्रिया पर्खनुको सट्टा स्ट्रिमिङ प्रतिक्रियाहरू कसरी थप्न सक्छु?"

**प्रॉम्प्ट इन्जिनियरिङ** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

अब तपाईंले मोडेलसँग कसरी कुरा गर्ने जान्नुभयो, अब हेर्नुहोस् के भन्नुहुन्छ। यस डेमोले उस्तै मोडेल सेटअप प्रयोग गर्दछ तर पाँच फरक प्रॉम्प्टिङ प्याटर्नहरू देखाउँछ। सिधा निर्देशनका लागि शून्य-शट प्रॉम्प्टहरू, उदाहरणबाट सिक्ने केही-शट प्रॉम्प्टहरू, तर्क प्रकट गर्ने चेन-ऑफ-थट प्रॉम्प्टहरू, र सन्दर्भ सेट गर्ने भूमिका-आधारित प्रॉम्प्टहरू प्रयास गर्नुहोस्। तपाईं देख्नुहुनेछ एउटै मोडेलले तपाईंको अनुरोध प्रस्तुत गर्ने तरिकामा आधारित भिन्न परिणाम दिन्छ।

डेमोले प्रॉम्प्ट टेम्प्लेटहरू पनि देखाउँछ, जुन परिवर्तनशीलहरूसँग पुन: प्रयोग गर्न मिल्ने प्रॉम्प्टहरू बनाउन शक्तिशाली तरिका हो।
तलको उदाहरणले LangChain4j को `PromptTemplate` प्रयोग गरेर परिवर्तनशीलहरू भर्दै प्रॉम्प्ट देखाउँछ। AI ले दिइएको गन्तव्य र क्रियाकलापको आधारमा जवाफ दिनेछ।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) च्याट प्रयोग गरेर प्रयास गर्नुहोस्:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) खोल्नुहोस् र सोध्नुहोस्:
> - "शून्य-शट र केही-शट प्रॉम्प्टिङमा के अन्तर छ, र म कहिले कुन प्रयोग गर्नुपर्छ?"
> - "तापक्रम प्यारामिटरले मोडेलको प्रतिक्रियामाथि कसरी प्रभाव पार्छ?"
> - "उत्पादनमा प्रॉम्प्ट इन्जेक्सन आक्रमण रोक्न के के तरिका छन्?"
> - "सामान्य प्याटर्नका लागि पुन: प्रयोगयोग्य PromptTemplate वस्तुहरू कसरी बनाउने?"

**उपकरण एकीकरण** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

यहाँ LangChain4j शक्तिशाली हुन्छ। तपाईं `AiServices` प्रयोग गरेर AI सहायक बनाउनुहुनेछ जसले तपाईंका Java मेथडहरूलाई कल गर्न सक्छ। केवल मेथडहरूलाई `@Tool("description")` ले चिन्ह लगाउनुहोस् र LangChain4j बाँकी हेरचाह गर्छ - AI ले प्रयोगकर्ताले सोध्दा कहिले कुन उपकरण प्रयोग गर्ने स्वतः निर्णय गर्छ। यसले फंक्शन कलिङ देखाउँछ, AI लाई केवल प्रश्नको जवाफ मात्र हैन क्रियाकलाप गर्न सक्षम बनाउने मुख्य प्रविधि।

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) च्याट प्रयोग गरेर प्रयास गर्नुहोस्:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) खोल्नुहोस् र सोध्नुहोस्:
> - "@Tool अङ्कन कसरी काम गर्छ र LangChain4j यसलाई पृष्ठभूमिमा कसरी ह्यान्डल गर्छ?"
> - "के AI ले जटिल समस्या समाधान गर्न धेरै उपकरणहरू क्रमिक रूपमा कल गर्न सक्छ?"
> - "यदि उपकरणले अपवाद फ्याँक्छ भने के हुन्छ - म त्रुटिहरूलाई कसरी व्यवस्थापन गर्ने?"
> - "यस क्यालकुलेटर उदाहरणको सट्टा वास्तविक API कसरी एकीकृत गर्ने?"

**दस्तावेज् प्रश्नोत्तर (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

यहाँ तपाईं RAG (retrieval-augmented generation) को आधार हेर्नुहुनेछ। मोडेलको प्रशिक्षण डाटामा भरपर्दा साटो, तपाईं [`document.txt`](../../../00-quick-start/document.txt) बाट सामग्री लोड गर्नुहुन्छ र प्रॉम्प्टमा समावेश गर्नुहुन्छ। AI ले तपाईंको दस्तावेज् आधारमा जवाफ दिन्छ, यसको सामान्य ज्ञानमा आधारित छैन। यो आफ्नो डाटा प्रयोग गरेर प्रणालीहरू बनाउने पहिलो कदम हो।

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **नोट:** यस सरल तरिकाले सम्पूर्ण दस्तावेज् प्रॉम्प्टमा लोड हुन्छ। ठूलो फाइलहरू (>10KB) को लागि सन्दर्भ सीमा पार हुनेछ। मोड्युल 03 ले उत्पादन RAG सिस्टमहरूको लागि चङ्किङ र भेक्टर खोज समेट्छ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) च्याट प्रयोग गरेर प्रयास गर्नुहोस्:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) खोल्नुहोस् र सोध्नुहोस्:
> - "RAG ले AI हल्युसिनेसन कसरी रोक्छ मॉडलको प्रशिक्षण डाटाको साटो?"
> - "यो सरल तरिका र भेक्टर एम्बेडिङ प्रयोग गरेर पुनःप्राप्तिमा के अन्तर छ?"
> - "केसरी मैले यसलाई धेरै दस्तावेज वा ठूला ज्ञान आधारहरूमा विस्तार गर्ने?"
> - "AI ले केवल उपलब्ध सन्दर्भ मात्र प्रयोग गरोस् भन्नका लागि प्रॉम्प्ट संरचना गर्ने उत्तम अभ्यास के हुन्?"

**जिम्मेवार AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

प्रहरी तहमा AI सुरक्षा बनाउनुहोस्। यस डेमोले दुई तहको सुरक्षा देखाउँछ:

**भाग 1: LangChain4j इनपुट गार्डरेलहरू** - नकारात्मक प्रॉम्प्टहरू LLM सम्म पुग्नु अघि ब्लक गर्छ। प्रतिबन्धित कुञ्जीशब्द वा प्याटर्नहरूको लागि कस्टम गार्डरेलहरू सिर्जना गर्नुहोस्। यी तपाईंको कोडमा चल्छन्, त्यसैले छिटो र निःशुल्क।

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

**भाग 2: प्रदायक सुरक्षा फिल्टरहरू** - GitHub मोडेलहरूले बनेका फिल्टरहरू जसले गार्डरेलले छुटाउन सक्ने कुरा समात्छ। गंभीर उल्लङ्घनमा कडा ब्लक (HTTP 400 त्रुटि) र सौम्य अस्वीकृतिहरू जहाँ AI विनम्रतापूर्वक अस्वीकार गर्छन्।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) च्याट प्रयोग गरेर प्रयास गर्नुहोस्:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) खोल्नुहोस् र सोध्नुहोस्:
> - "InputGuardrail के हो र म आफ्नै कसरी बनाउने?"
> - "कडा ब्लक र सौम्य अस्वीकृतिमा के भिन्नता छ?"
> - "किन दुवै गार्डरेल र प्रदायक फिल्टरहरू सँगै प्रयोग गर्ने?"

## अर्को कदमहरू

**अर्को मोड्युल:** [01-introduction - LangChain4j र gpt-5 सँग Azure मा सुरु गर्ने](../01-introduction/README.md)

---

**नेभिगेसन:** [← मुख्य पृष्ठमा फर्कनुहोस्](../README.md) | [अर्को: Module 01 - परिचय →](../01-introduction/README.md)

---

## समस्या समाधान

### पहिलो पटक Maven बिल्ड

**समस्या:** पहिलो `mvn clean compile` वा `mvn package` लामो समय (10-15 मिनेट) लाग्छ

**कारण:** Maven ले पहिलो पटक सबै प्रोजेक्ट निर्भरता (Spring Boot, LangChain4j लाइब्रेरीहरू, Azure SDKहरू आदि) डाउनलोड गर्नुपर्छ।

**समाधान:** यो सामान्य व्यवहार हो। पछि बिल्डहरू धेरै छिटो हुनेछन् किनभने निर्भरता स्थानीय रूपमा क्यास हुन्छ। डाउनलोड समय तपाईँको नेटवर्क गतिको आधारमा निर्भर गर्दछ।
### PowerShell Maven कमाण्ड सिंट्याक्स

**समस्या**: Maven कमाण्डहरू `Unknown lifecycle phase ".mainClass=..."` त्रुटिसहित असफल हुन्छन्

**कारण**: PowerShell ले `=` लाई भेरिएबल असाइनमेन्ट अपरेटरको रूपमा व्याख्या गर्छ, जसले Maven को प्रोपर्टी सिंट्याक्स तोड्छ

**समाधान**: Maven कमाण्ड अघि stop-parsing अपरेटर `--%` प्रयोग गर्नुहोस्:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` अपरेटरले PowerShell लाई बाँकीका सबै आर्गुमेन्टहरू Maven लाई सिधा र बिना व्याख्या पास गर्न बताउँछ।

### Windows PowerShell Emoji प्रदर्शन

**समस्या**: PowerShell मा AI प्रतिक्रिया इमोजीको सट्टा फोहोर अक्षरहरू (जस्तै, `????` वा `â??`) देखाउँछ

**कारण**: PowerShell को पूर्वनिर्धारित इनकोडिङले UTF-8 इमोजी समर्थन गर्दैन

**समाधान**: Java अनुप्रयोगहरू चलाउनुअघि यो कमाण्ड चलाउनुहोस्:
```cmd
chcp 65001
```

यसले टर्मिनलमा UTF-8 इनकोडिङ अनिवार्य गर्छ। वैकल्पिक रूपमा, बेहतर युनिकोड समर्थन भएको Windows Terminal प्रयोग गर्नुहोस्।

### API कलहरू डिबगिङ

**समस्या**: AI मोडेलबाट प्रमाणीकरण त्रुटिहरू, दर सीमा, वा अप्रत्याशित प्रतिक्रियाहरू

**समाधान**: उदाहरणहरूमा `.logRequests(true)` र `.logResponses(true)` समावेश गरिएको छ जुन कन्सोलमा API कलहरू देखाउँछ। यसले प्रमाणीकरण त्रुटिहरू, दर सीमा, वा अप्रत्याशित प्रतिक्रियाहरूको समस्या समाधानमा मद्दत गर्छ। रेकर्डिङ आवाज घटाउन उत्पादनमा यी झण्डा हटाउनुहोस्।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यो दस्तावेज एआई अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) मार्फत अनुवाद गरिएको हो। हामी शुद्धताका लागि प्रयास गरिरहेका छौं भने पनि, कृपया जानकार रहनुहोस् कि स्वचालित अनुवादहरूमा त्रुटिहरू वा अशुद्धता हुनसक्छ। मूल भाषा मा रहेको दस्तावेजलाई आधिकारिक स्रोतको रूपमा लिनु पर्नेछ। महत्वपूर्ण जानकारीको लागि, विज्ञ मानवीय अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न हुने कुनै पनि गलतफहमी वा व्याख्या सम्बन्धी हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->