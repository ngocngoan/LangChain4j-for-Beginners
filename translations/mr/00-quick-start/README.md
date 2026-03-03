# Module 00: जलद प्रारंभ

## सामग्री सूची

- [परिचय](../../../00-quick-start)
- [LangChain4j म्हणजे काय?](../../../00-quick-start)
- [LangChain4j अवलंबित्व](../../../00-quick-start)
- [पूर्वापेक्षिता](../../../00-quick-start)
- [सेटअप](../../../00-quick-start)
  - [1. तुमचा GitHub टोकन मिळवा](../../../00-quick-start)
  - [2. तुमचा टोकन सेट करा](../../../00-quick-start)
- [उदाहरणे चालवा](../../../00-quick-start)
  - [1. मूलभूत संवाद](../../../00-quick-start)
  - [2. प्रॉम्प्ट नमुने](../../../00-quick-start)
  - [3. फंक्शन कॉलिंग](../../../00-quick-start)
  - [4. दस्तऐवज प्रश्नोत्तरे (सोपे RAG)](../../../00-quick-start)
  - [5. जबाबदार AI](../../../00-quick-start)
- [प्रत्येक उदाहरण काय दर्शवते](../../../00-quick-start)
- [पुढील टप्पे](../../../00-quick-start)
- [समस्या निवारण](../../../00-quick-start)

## परिचय

हा जलद प्रारंभ तुम्हाला LangChain4j सह शक्य तितक्या लवकर सुरू होण्यास मदत करतो. यामध्ये LangChain4j आणि GitHub मॉडेल्ससह AI अनुप्रयोग तयार करण्याच्या मूलभूत तत्त्वांचा समावेश आहे. पुढील मॉड्यूल्समध्ये तुम्ही Azure OpenAI आणि GPT-5.2 वर स्विच कराल आणि प्रत्येक संकल्पनेत अधिक खोलवर जाल.

## LangChain4j म्हणजे काय?

LangChain4j हा एक Java लायब्ररी आहे जो AI-शक्तीने चालणारे अनुप्रयोग तयार करणे सोपे बनवतो. HTTP क्लायंट्स आणि JSON पार्सिंग हाताळण्याऐवजी, तुम्ही स्वच्छ Java API सह काम करता.

LangChain मधील "चेन" म्हणजे अनेक घटक एकत्र जोडणे - तुम्ही प्रॉम्प्टला मॉडेलशी, नंतर पार्सरशी किंवा एकाधिक AI कॉल्स एकमेकांशी जोडू शकता जिथे एक आउटपुट पुढील इनपुटसाठी वापरला जातो. हा जलद प्रारंभ मूलभूत गोष्टींकडे लक्ष केंद्रित करतो, जास्त गुंतागुंतीच्या चेनवर जाण्यापूर्वी.

<img src="../../../translated_images/mr/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j मधील घटक जोडणे - शक्तिशाली AI कार्यप्रवाह तयार करण्यासाठी ब्लॉक जोडले जातात*

आपण तीन मुख्य घटक वापरणार आहोत:

**ChatModel** - AI मॉडेलशी संवाद साधण्यासाठी इंटरफेस. `model.chat("prompt")` कॉल करा आणि प्रतिसाद स्ट्रिंग मिळवा. आपण `OpenAiOfficialChatModel` वापरतो जे GitHub मॉडेल्ससारख्या OpenAI-तंत्रज्ञानसह कार्य करतो.

**AiServices** - टाइप-सेफ AI सेवा इंटरफेस तयार करतो. पद्धती परिभाषित करा, त्यावर `@Tool` अॅनोटेट करा, आणि LangChain4j व्यवस्थापन करते. AI आवश्यक तेव्हा आपोआप Java पद्धती कॉल करते.

**MessageWindowChatMemory** - संवादाची मागील इतिहास सांभाळते. याशिवाय प्रत्येक विनंती स्वतंत्र असते. यासहित AI पूर्वीच्या संदेशांची आठवण ठेवतो आणि अनेक वळणांमध्ये संदर्भ जतन करतो.

<img src="../../../translated_images/mr/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j आर्किटेक्चर - तुमच्या AI अनुप्रयोगांना सशक्त करण्यासाठी मुख्य घटक एकत्रित काम करतात*

## LangChain4j अवलंबित्व

हा जलद प्रारंभ [`pom.xml`](../../../00-quick-start/pom.xml) मध्ये तीन Maven अवलंबित्वे वापरतो:

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

`langchain4j-open-ai-official` मॉड्यूल `OpenAiOfficialChatModel` वर्ग पुरवते जो OpenAI-सुसंगत API शी जोडतो. GitHub मॉडेल्स हेच API फॉरमॅट वापरतात, त्यामुळे कोणत्याही विशेष अडॅप्टरची गरज नाही - फक्त बेस URL `https://models.github.ai/inference` कडे निर्देश करा.

`langchain4j-easy-rag` मॉड्यूल आपोआप दस्तऐवज विभागणी, एम्बेडिंग आणि पुनर्प्राप्ती प्रदान करते जेणेकरून तुम्ही प्रत्येक टप्पा मॅन्युअली कॉन्फिगर न करता RAG अनुप्रयोग तयार करू शकता.

## पूर्वापेक्षिता

**डेव्ह कंटेनर वापरत आहात?** Java आणि Maven आधीच स्थापित आहेत. तुम्हाला फक्त GitHub Personal Access Token ची आवश्यकता आहे.

**स्थानिक विकासासाठी:**
- Java 21+, Maven 3.9+
- GitHub व्यक्‍तिक टोकन (खालील सूचना पहा)

> **टिप:** हा मॉड्यूल GitHub मॉडेल्समधून `gpt-4.1-nano` वापरतो. कोडमधील मॉडेल नाव बदलू नका - हे GitHub उपलब्ध मॉडेल्ससाठी कॉन्फिगर केलेले आहे.

## सेटअप

### 1. तुमचा GitHub टोकन मिळवा

1. [GitHub सेटिंग्ज → Personal Access Tokens](https://github.com/settings/personal-access-tokens) येथे जा
2. "Generate new token" क्लिक करा
3. वर्णनात्मक नाव सेट करा (उदा. "LangChain4j Demo")
4. समाप्ती सेट करा (7 दिवस शिफारस केली)
5. "Account permissions" मध्ये "Models" शोधा आणि "Read-only" सेट करा
6. "Generate token" क्लिक करा
7. तुमचा टोकन कॉपी करा आणि जतन करा - तो पुन्हा दिसणार नाही

### 2. तुमचा टोकन सेट करा

**पर्याय 1: VS Code वापरून (शिफारस केली)**

VS Code वापरत असल्यास, प्रकल्प मूळामध्ये `.env` फाइलमध्ये तुमचा टोकन जोडा:

जर `.env` फाइल अस्तित्वात नसेल, तर `.env.example` ची कॉपी `.env` मध्ये करा किंवा नवीन `.env` फाइल तयार करा.

**उदाहरण `.env` फाइल:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env मध्ये
GITHUB_TOKEN=your_token_here
```

नंतर तुम्ही एक्सप्लोररमध्ये कोणत्याही डेमो फाईलवर (उदा. `BasicChatDemo.java`) उजवे-क्लिक करून **"Run Java"** निवडू शकता किंवा रन आणि डिबग पॅनेलमधून लॉन्च कॉन्फिगरेशन्स वापरू शकता.

**पर्याय 2: टर्मिनल वापरून**

टोकन पर्यावरणीय चल म्हणून सेट करा:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## उदाहरणे चालवा

**VS Code वापरत असल्यास:** एक्सप्लोररमधील कोणत्याही डेमो फाईलवर उजवे-क्लिक करा आणि **"Run Java"** निवडा, किंवा रन आणि डिबग पॅनेलमधून लॉन्च कॉन्फिगरेशन्स वापरा (पूर्वी तुमचा टोकन `.env` फाइलमध्ये जोडलेला असावा).

**Maven वापरत असल्यास:** पर्यायीरित्या, तुम्ही कमांड लाइनवरून चालवू शकता:

### 1. मूलभूत संवाद

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. प्रॉम्प्ट नमुने

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

शून्य-दावा, कमी-मधे, विचारांची साखळी आणि भूमिका-आधारित प्रॉम्प्टिंग दाखवते.

### 3. फंक्शन कॉलिंग

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI आवश्यक तेव्हा आपोआप तुमच्या Java पद्धती कॉल करतो.

### 4. दस्तऐवज प्रश्नोत्तरे (सोपे RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

आपल्या दस्तऐवजांविषयी प्रश्न विचारा Easy RAG वापरून ज्यामध्ये आपोआप एम्बेडिंग आणि पुनर्प्राप्ती होते.

### 5. जबाबदार AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

पहा AI सुरक्षा फिल्टर्स चुकीचा किंवा हानिकारक सामग्री कशी ब्लॉक करतात.

## प्रत्येक उदाहरण काय दर्शवते

**मूलभूत संवाद** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

इथेून सुरू करा आणि LangChain4j सोप्या स्वरूपात पाहा. तुम्ही `OpenAiOfficialChatModel` तयार कराल, `.chat()` सह प्रॉम्प्ट पाठवाल आणि प्रतिसाद मिळवाल. यामुळे पाया समजतो: कस्टम एंडपॉइंट्स आणि API कींसह मॉडेल्स कसे प्रारंभ करायचे. या नमुन्याचे समजल्यावर, सर्व काही यावर आधारलेले आहे.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) उघडा आणि विचार करा:
> - "या कोडमध्ये GitHub मॉडेल्सवरून Azure OpenAI वर कसे बदलायचे?"
> - "OpenAiOfficialChatModel.builder() मध्ये कोणती इतर पॅरामीटर्स सेट करू शकतो?"
> - "पूर्ण प्रतिसादाची वाट पाहण्याऐवजी स्ट्रीमिंग प्रतिसाद कसे जोडायचे?"

**प्रॉम्प्ट इंजिनिअरिंग** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

आता तुम्हाला मॉडेलशी बोलायला येते, तर चला पाहूयात तुम्ही त्याला काय सांगता. या डेमोत समान मॉडेल सेटअप वापरले आहे पण पाच वेगवेगळे प्रॉम्प्टिंग नमुने दाखवले आहेत. थेट सूचनांसाठी शून्य-दावा प्रॉम्प्ट, उदाहरणांमधून शिकणारे कमी-मधे प्रॉम्प्ट, कारणसंगती टप्पे उघड करणारे विचारसाखळी प्रॉम्प्ट, आणि संदर्भ सेट करणारे भूमिका-आधारित प्रॉम्प्ट यांचा प्रयत्न करा. तुम्हाला दिसेल की तुम्ही तुमच्या विनंती कशी मांडता त्यानुसार समान मॉडेल कसे वेगवेगळे उत्तर देते.

हा डेमो प्रॉम्प्ट टेम्पलेट्स देखील दाखवतो, जे चांगल्या पुनर्वापरयोग्य प्रॉम्प्टसाठी शक्तिशाली मार्ग आहे ज्यात चल (व्हेरिएबल) असतात.
खालील उदाहरणात LangChain4j `PromptTemplate` वापरून व्हेरिएबल भरणे दाखवले आहे. AI दिलेल्या गंतव्य आणि क्रियावर आधारित उत्तर देईल.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) उघडा आणि विचार करा:
> - "शून्य-दावा आणि कमी-मधे प्रॉम्प्टिंगमध्ये काय फरक आहे, आणि कधी काय वापरावे?"
> - "उष्णता पॅरामीटर मॉडेलच्या प्रतिसादांवर कसा परिणाम करतो?"
> - "प्रॉम्प्ट इंजेक्शन हल्ले टाळण्यासाठी उत्पादनात काही तंत्र कसे वापरू शकतो?"
> - "सामान्य नमुन्यांसाठी पुनर्वापरयोग्य PromptTemplate वस्तू कशा तयार करायच्या?"

**टूल इंटिग्रेशन** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

येथे LangChain4j शक्तिशाली होते. तुम्ही `AiServices` वापरून एक AI सहाय्यक तयार कराल जो तुमच्या Java पद्धती कॉल करू शकतो. फक्त पद्धतींवर `@Tool("description")` अॅनोटेट करा आणि LangChain4j बाकी सर्व व्यवस्थापित करते - AI वापरकर्त्याच्या विनंतीनुसार कोणते टूल वापरायचे ठरवतो. यामुळे फंक्शन कॉलिंगची झलक मिळते, जी AI कडे क्रिया घेण्याची क्षमता देते, फक्त प्रश्नांची उत्तरे देण्याऐवजी.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) उघडा आणि विचार करा:
> - "@Tool अॅनोटेशन कसे कार्य करते आणि LangChain4j त्यास मागून कसे हाताळते?"
> - "AI अनेक टूल्स एकमेकांनंतर कॉल करून क्लिष्ट समस्या सोडवू शकतो का?"
> - "जर टूलने अपवाद फेकला तर काय होते - त्रुटी कशी हाताळायची?"
> - "या कॅल्क्युलेटर उदाहरणाऐवजी वास्तविक API कसे इंटिग्रेट करायचे?"

**दस्तऐवज प्रश्नोत्तरे (सोपे RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

येथे तुम्हाला RAG (पुनर्प्राप्ती-समृद्ध जनरेशन) LangChain4j च्या "Easy RAG" पद्धतीने दाखवेल. दस्तऐवज लोड होतात, आपोआप विभागले जातात आणि एम्बेड केले जातात स्मृतीच्या संचयात, नंतर एक कंटेंट रिट्रीव्हर विचारणी वेळी AI ला संबंधित भाग पुरवतो. AI तुमच्या दस्तऐवजांवर आधारित उत्तर देते, त्याच्या सामान्य ज्ञानावर नाही.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) उघडा आणि विचार करा:
> - "RAG AI भास टाळण्यास कसे मदत करते, मॉडेलच्या प्रशिक्षण डेटाच्या वापराच्या तुलनेत?"
> - "या सोपनंतर आणि सानुकूल RAG पाईपलाइनमध्ये काय फरक आहे?"
> - "कसे मी याला अनेक दस्तऐवज व मोठ्या ज्ञानसंचासाठी परिचालनक्षम करू?"

**जबाबदार AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

गभरण्या सुरक्षेसाठी संरक्षण तयार करा. हा डेमो दोन संरक्षण थर दाखवतो जे एकत्र काम करतात:

**भाग 1: LangChain4j इनपुट गार्डरेइल्स** - LLM पर्यंत पोहोचण्यापूर्वी धोकादायक प्रॉम्प्ट्स ब्लॉक करा. निषिद्ध कीवर्ड्स किंवा नमुन्यांसाठी सानुकूल गार्डरेइल्स तयार करा. हे तुमच्या कोडमध्ये चालतात, त्यामुळे ते जलद आणि विनामूल्य आहेत.

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

**भाग 2: प्रदाता सुरक्षा फिल्टर्स** - GitHub मॉडेल्समध्ये अंतर्निर्मित फिल्टर्स आहेत जे तुमच्या गार्डरेइल्स कमी पडतील ते पकडतात. तुम्हाला कडक प्रतिबंध (HTTP 400 त्रुटी) आणि नम्र नकारात्मक प्रतिसाद दिसतील जिथे AI सभ्यपणे नकार देतो.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) उघडा आणि विचार करा:
> - "InputGuardrail म्हणजे काय आणि स्वतःचे कसे तयार करायचे?"
> - "कडक प्रतिबंध आणि सौम्य नकार यामध्ये काय फरक आहे?"
> - "गार्डरेइल्स आणि प्रदाता फिल्टर्स एकत्र का वापरावेत?"

## पुढील टप्पे

**पुढील मॉड्यूल:** [01-परिचय - LangChain4j सह प्रारंभ](../01-introduction/README.md)

---

**दिशा:** [← मुख्य पृष्ठाकडे परत](../README.md) | [पुढे: Module 01 - परिचय →](../01-introduction/README.md)

---

## समस्या निवारण

### पहिल्यांदाच्या Maven बिल्ड

**समस्या:** प्रारंभिक `mvn clean compile` किंवा `mvn package` ला बराच वेळ (10-15 मिनिटे) लागू शकतो

**कारण:** Maven ला सर्व प्रकल्प अवलंबित्वे (Spring Boot, LangChain4j लायब्ररी, Azure SDKs, इ.) प्रथम बिल्डवर डाउनलोड करावे लागतात.

**उपाय:** हे सामान्य वर्तन आहे. पुढील बिल्ड्स जास्त जलद होतील कारण अवलंबित्वे स्थानिकरीत्या कॅश केली जातात. डाउनलोड वेळ तुमच्या नेटवर्क स्पीडवर अवलंबून असते.

### PowerShell Maven कमांड सिंटॅक्स

**समस्या:** Maven कमांड्स `Unknown lifecycle phase ".mainClass=..."` या त्रुटीने अयशस्वी होतात
**कारण**: PowerShell `=` या चिन्हाला व्हेरिएबल असाइनमेंट ऑपरेटर म्हणून समजते, ज्यामुळे Maven प्रॉपर्टी सिंटॅक्स तुटतो

**सोल्यूशन**: Maven कमांडपूर्वी stop-parsing ऑपरेटर `--%` वापरा:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ऑपरेटर PowerShell ला सांगतो की Maven कडे उरलेले सर्व अर्ग्युमेंट्स अक्षरशः पाठवायचे आहेत, व्याख्या न करता.

### Windows PowerShell Emoji प्रदर्शन

**समस्या**: PowerShell मध्ये AI प्रतिसादांमध्ये इमोजीऐवजी कचर्‍या अक्षरे (उदा., `????` किंवा `â??`) दिसतात

**कारण**: PowerShell ची डीफॉल्ट एनकोडिंग UTF-8 इमोजींसाठी समर्थन करत नाही

**सोल्यूशन**: Java ऍप्लिकेशन चालण्यापूर्वी हा कमांड चालवा:
```cmd
chcp 65001
```

यामुळे टर्मिनलमध्ये UTF-8 एनकोडिंग लागू होते. पर्यायीरित्या, Windows Terminal वापरा ज्यामध्ये युनिकोड समर्थन अधिक चांगले आहे.

### API कॉल्स डीबगिंग

**समस्या**: Authentication त्रुटी, रेट लिमिट्स, किंवा AI मॉडेलकडून अनपेक्षित प्रतिसाद

**सोल्यूशन**: उदाहरणांमध्ये `.logRequests(true)` आणि `.logResponses(true)` समाविष्ट आहेत ज्यामुळे API कॉल्स कन्सोलमध्ये दाखवले जातात. यामुळे authentication त्रुटी, रेट लिमिट्स किंवा अनपेक्षित प्रतिसाद शोधण्यात मदत होते. प्रॉडक्शनमध्ये या फ्लॅग्स काढा ज्यामुळे लॉगची गडबड कमी होईल.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
हे दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून भाषांतरित केले आहे. आम्ही अचूकतेसाठी प्रयत्न करतो, तरी कृपया लक्षात ठेवा की स्वयंचलित भाषांतरांमध्ये चुका किंवा अचूकतेचे अभाव असू शकतात. मूळ दस्तऐवज त्याच्या स्वदेशी भाषेत अधिकृत स्रोत मानला जावा. महत्त्वाच्या माहितीसाठी व्यावसायिक मानवी भाषांतर करण्याचा सल्ला दिला आहे. या भाषांतराच्या वापरामुळे उद्भवलेल्या कोणत्याही गैरसमजुती किंवा चुकीच्या अर्थलागी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->