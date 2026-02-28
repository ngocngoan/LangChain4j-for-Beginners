# Module 00: जलद प्रारंभ

## विभागांची यादी

- [परिचय](../../../00-quick-start)
- [LangChain4j म्हणजे काय?](../../../00-quick-start)
- [LangChain4j अवलंबित्वे](../../../00-quick-start)
- [आवश्यक पूर्वअटी](../../../00-quick-start)
- [सेटअप](../../../00-quick-start)
  - [1. आपला GitHub टोकन मिळवा](../../../00-quick-start)
  - [2. आपला टोकन सेट करा](../../../00-quick-start)
- [उदाहरण चालवा](../../../00-quick-start)
  - [1. मूलभूत चॅट](../../../00-quick-start)
  - [2. प्रॉम्प्ट पॅटर्न्स](../../../00-quick-start)
  - [3. फंक्शन कॉलिंग](../../../00-quick-start)
  - [4. दस्तऐवज प्रश्नोत्तरे (सुलभ RAG)](../../../00-quick-start)
  - [5. जबाबदार AI](../../../00-quick-start)
- [प्रत्येक उदाहरण काय दाखवते](../../../00-quick-start)
- [पुढील पावले](../../../00-quick-start)
- [समस्यांची सोडवणूक](../../../00-quick-start)

## परिचय

हा जलद प्रारंभ तुम्हाला LangChain4j सह शक्य तितक्या लवकर सुरू आणि चालू करण्यासाठी आहे. हे LangChain4j आणि GitHub मॉडेल्ससह AI अनुप्रयोग तयार करण्याच्या अत्यावश्यक मूलभूत गोष्टी कव्हर करते. पुढील मॉड्यूल्समध्ये तुम्ही Azure OpenAI वापरुन LangChain4j सह अधिक प्रगत अनुप्रयोग तयार कराल.

## LangChain4j म्हणजे काय?

LangChain4j ही Java लायब्ररी आहे जी AI-शक्तीनिर्मित अनुप्रयोग तयार करणे सोपे करते. HTTP क्लायंट्स आणि JSON पार्सिंगसह संभाळण्याऐवजी, तुम्ही स्वच्छ Java API शी काम करता.

LangChain मधील "चेन" म्हणजे अनेक घटकांची साखळी एकत्र करणे - तुम्ही एक प्रॉम्प्ट मॉडेलवर किंवा पार्सरवर किंवा अनेक AI कॉल्स एकमेकांशी साखळीने लिंक करू शकता जिथे एक उत्पादन पुढील इनपुटमध्ये दिले जाते. हा जलद प्रारंभ मूलभूत गोष्टींवर लक्ष केंद्रित करतो ज्यापूर्वी अधिक जटिल साखळ्या शोधल्या जातील.

<img src="../../../translated_images/mr/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j साखळी संकल्पना" width="800"/>

*LangChain4j मधील साखळी घटक - शक्तिशाली AI कार्यप्रवाह तयार करणारे ब्लॉक्स*

आम्ही तीन मुख्य घटक वापरणार आहोत:

**ChatModel** - AI मॉडेल संवादासाठी इंटरफेस. `model.chat("prompt")` कॉल करा आणि प्रतिसाद स्ट्रिंग मिळवा. आपण `OpenAiOfficialChatModel` वापरतो जे GitHub मॉडेल्ससारख्या OpenAI-योग्य एंडपॉइंट्ससह कार्य करते.

**AiServices** - प्रकार-सुरक्षित AI सेवा इंटरफेस तयार करते. पद्धती परिभाषित करा, त्यावर `@Tool` एनोटेट करा, आणि LangChain4j आयोजन हाताळते. AI आवश्यक असल्यास तुमच्या Java पद्धती आपोआप कॉल करते.

**MessageWindowChatMemory** - संभाषण इतिहास राखते. शिवाय त्याशिवाय प्रत्येक विनंती स्वतंत्र असते. त्यासह AI पूर्वीच्या संदेशांची आठवण ठेवते आणि अनेक वळणांमध्ये संदर्भ राखते.

<img src="../../../translated_images/mr/architecture.eedc993a1c576839.webp" alt="LangChain4j अभियांत्रिकी" width="800"/>

*LangChain4j अभियांत्रिकी - AI अनुप्रयोग चालवण्यासाठी मुख्य घटक एकत्र काम करत आहेत*

## LangChain4j अवलंबित्वे

हा जलद प्रारंभ [`pom.xml`](../../../00-quick-start/pom.xml) मध्ये तीन Maven अवलंबित्वांचा वापर करतो:

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

`langchain4j-open-ai-official` मॉड्यूल `OpenAiOfficialChatModel` क्लास प्रदान करते जो OpenAI-योग्य API शी जोडतो. GitHub मॉडेल्स याच API फॉरमॅटचा वापर करतात, त्यामुळे कोणत्याही विशेष अॅडॉप्टरची आवश्यकता नाही - फक्त बेस URL `https://models.github.ai/inference` कडे निर्देश करा.

`langchain4j-easy-rag` मॉड्यूल स्वयंचलित दस्तऐवज विभागणी, एम्बेडिंग आणि पुनर्प्राप्ती प्रदान करते जेणेकरून तुम्ही प्रत्येक पायरी मॅन्युअली कॉन्फिगर न करता RAG अनुप्रयोग तयार करू शकता.

## आवश्यक पूर्वअटी

**Dev कंटेनर वापरत आहात का?** Java आणि Maven आधीच इन्स्टॉल केलेले आहेत. तुम्हाला फक्त GitHub Personal Access Token आवश्यक आहे.

**स्थानिक विकासासाठी:**
- Java 21+, Maven 3.9+
- GitHub Personal Access Token (खालील सूचना पहा)

> **सूचना:** हा मॉड्यूल GitHub मॉडेल्समधील `gpt-4.1-nano` वापरतो. कोडमधील मॉडेल नाव बदलू नका - ते GitHub उपलब्ध मॉडेल्ससाठी कॉन्फिगर केले गेले आहे.

## सेटअप

### 1. आपला GitHub टोकन मिळवा

1. [GitHub सेटिंग्ज → वैयक्तिक प्रवेश टोकन](https://github.com/settings/personal-access-tokens) वर जा
2. "Generate new token" क्लिक करा
3. वर्णनात्मक नाव सेट करा (उदा., "LangChain4j Demo")
4. कालबाह्यता सेट करा (7 दिवस सुचवले आहे)
5. "Account permissions" अंतर्गत "Models" शोधा आणि "Read-only" सेट करा
6. "Generate token" क्लिक करा
7. आपला टोकन कॉपी करा आणि साठवा - पुन्हा ते दिसणार नाही

### 2. आपला टोकन सेट करा

**पर्याय 1: VS Code वापरताना (शिफारसीय)**

जर तुम्ही VS Code वापरत असाल, तर प्रोजेक्टच्या मूळ फोल्डरमधील `.env` फाईलमध्ये आपला टोकन जोडा:

जर `.env` फाईल अस्तित्वात नसेल, तर `.env.example` कॉपी करा `.env` म्हणून किंवा नवीन `.env` फाईल तयार करा.

**उदाहरण `.env` फाईल:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env मध्ये
GITHUB_TOKEN=your_token_here
```

मग तुम्ही एक्सप्लोररमधील कोणत्याही डेमो फाईलवर उजवे क्लिक करून (उदा., `BasicChatDemo.java`) **"Run Java"** पर्याय निवडू शकता किंवा रन आणि डिबग पॅनेलमधील लाँच कॉन्फिगरेशन वापरू शकता.

**पर्याय 2: टर्मिनल वापरताना**

टोकन पर्यावरण चल म्हणून सेट करा:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## उदाहरण चालवा

**VS Code वापरताना:** एक्सप्लोररमधील कोणत्याही डेमो फाईलवर उजवे क्लिक करा आणि **"Run Java"** निवडा, किंवा रन आणि डिबग पॅनेलमधील लाँच कॉन्फिगरेशन वापरा (तुम्ही आधी टोकन `.env` फाईलमध्ये जोडले आहे याची खात्री करा).

**Maven वापरताना:** पर्याय म्हणून, तुम्ही कमांड लाइनवरून चालवू शकता:

### 1. मूलभूत चॅट

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

### 2. प्रॉम्प्ट पॅटर्न्स

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.PromptEngineeringDemo
```

शून्य-शॉट, काही-शॉट, चेन-ऑफ-थॉट आणि भूमिका-आधारित प्रॉम्प्टिंग दाखवते.

### 3. फंक्शन कॉलिंग

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI आवश्यक तेव्हा तुमच्या Java पद्धती स्वयंचलितपणे कॉल करते.

### 4. दस्तऐवज प्रश्नोत्तरे (सुलभ RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

सुलभ RAG वापरून आपले दस्तऐवज विचारून प्रश्न विचारा ज्यामध्ये स्वयंचलित एम्बेडिंग आणि पुनर्प्राप्ती आहे.

### 5. जबाबदार AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI सुरक्षा फिल्टर्स कसे हानिकारक सामग्री अवरोधित करतात ते पहा.

## प्रत्येक उदाहरण काय दाखवते

**मूलभूत चॅट** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

हेथे सुरू करा जेणेकरून तुम्हाला LangChain4j ची सर्वात सोपी रचना कळेल. तुम्ही `OpenAiOfficialChatModel` तयार कराल, `.chat()` सह प्रॉम्प्ट पाठवाल, आणि प्रतिसाद मिळवाल. हे मूळ सिद्धांत दाखवते: कस्टम एंडपॉइंट्स आणि API कीसह मॉडेल्स कसे प्रारंभ करायचे. जेव्हा हे नमुना समजून घ्याल तेव्हां पुढील सर्व काही त्यावर आधारित तयार होईल.

```java
OpenAiOfficialChatModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) उघडा आणि विचारा:
> - "या कोडमध्ये GitHub मॉडेल्सवरून Azure OpenAI कसे स्विच कराल?"
> - "OpenAiOfficialChatModel.builder() मध्ये आणखी कोणते पॅरामीटर्स कॉन्फिगर करू शकतो?"
> - "पूर्ण प्रतिसादाचा انتظار न करता स्ट्रीमिंग प्रतिसाद कसे जोडायचे?"

**प्रॉम्प्ट इंजिनिअरिंग** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

तुम्हाला आता मॉडेलशी संवाद कसा करायचा हे माहिती आहे, चला पाहूया तुम्ही मॉडेलशी काय बोलता. या डेमोमध्ये एकच मॉडेल सेटअप वापरून पाच वेगवेगळे प्रॉम्प्टिंग पॅटर्न दाखवले आहेत. थेट सूचना देण्यासाठी शून्य-शॉट प्रॉम्प्ट्स वापरा, उदाहरणांद्वारे शिकण्यासाठी काही-शॉट, विचार मांडण्यासाठी चेन-ऑफ-थॉट, आणि संदर्भ सेट करण्यासाठी भूमिका-आधारित प्रॉम्प्ट्स वापरा. तुम्हाला दिसेल की तुम्ही ज्या प्रकारे विनंती मांडता त्याप्रमाणेच त्याच मॉडेलचे परिणाम वेगवेगळे येतात.

डेमोमध्ये प्रॉम्प्ट टेम्प्लेट्सही आहेत, जे वेरिएबलसह पुन्हा वापरता येणारे प्रॉम्प्ट तयार करण्याचा शक्तिशाली मार्ग आहे.
खालील उदाहरण LangChain4j `PromptTemplate` वापरून वेरिएबल्स कसे भरणे हे दाखवते. AI दिलेल्या लक्ष्यस्थान आणि क्रियावर आधारित उत्तर देतो.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`PromptEngineeringDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java) उघडा आणि विचारा:
> - "शून्य-शॉट आणि काही-शॉट प्रॉम्प्टिंगमधील फरक काय आहे, आणि कोणती कधी वापरावी?"
> - "तापमान पॅरामीटर मॉडेलच्या प्रतिसादांवर कसा परिणाम करतो?"
> - "उत्पादनात प्रॉम्प्ट इंजेक्शन हल्ल्यांपासून प्रतिबंध करण्यासाठी काही तंत्रे कोणती आहेत?"
> - "सामान्य पॅटर्नसाठी पुन्हा वापरता येणारे PromptTemplate वस्तू कशा तयार कराव्या?"

**टूल एकत्रीकरण** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

ही जागा जिथे LangChain4j सामर्थ्यवान होते. तुम्ही `AiServices` वापरून AI सहायक तयार कराल जो तुमच्या Java पद्धती कॉल करू शकतो. फक्त पद्धतींवर `@Tool("वर्णन")` एनोटेट करा आणि LangChain4j बाकी सांभाळते - AI वापरकर्त्याच्या विनंतीनुसार कोणता टूल वापरायचा ते ठरवते. हे फंक्शन कॉलिंगचे उदाहरण आहे, AI जे फक्त प्रश्नांची उत्तरे देत नाही तर क्रिया देखील करू शकते.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) उघडा आणि विचारा:
> - "@Tool एनोटेशन कसे कार्य करते आणि LangChain4j त्यातील आत काय करते?"
> - "AI अनेक टूल्स एकमेकांनंतर कॉल करू शकतो का जटिल समस्या सोडवण्यासाठी?"
> - "जर एखादा टूल अपवाद नकोसा फेकला तर काय करावे?"
> - "हा कॅल्क्युलेटर उदाहरणाऐवजी एक वास्तविक API कसे समाकलित कराल?"

**दस्तऐवज प्रश्नोत्तरे (सुलभ RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

येथे तुम्हाला LangChain4j चा "सुलभ RAG" दृष्टिकोन वापरून RAG (रिट्रीव्हल-अगमेंटेड जनरेशन) पहायला मिळेल. दस्तऐवज लोड होतात, स्वयंचलितपणे विभागले जातात आणि एका इन-मेमरी संग्रहात एम्बेड केले जातात, नंतर एक कंटेंट पुनर्प्राप्त करणारा AI ला क्वेरी वेळेस संबंधित चुक्स प्रदान करतो. AI तुमच्या दस्तऐवजांनुसार उत्तर देतो, त्याच्या सामान्य ज्ञानावर नाही.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) उघडा आणि विचारा:
> - "RAG AI भ्रम टाळण्यासाठी कसा मदत करतो आणि मॉडेलच्या प्रशिक्षण डेटाशी तुलना करता कसा वेगळा आहे?"
> - "हा सुलभ दृष्टिकोन आणि सानुकूल RAG पाइपलाइन यामध्ये काय फरक आहे?"
> - "माहितीच्या अधिक दस्तऐवजांसाठी किंवा मोठ्या ज्ञानसंग्रहासाठी मी याचा विस्तार कसा करेन?"

**जबाबदार AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

सुरक्षणासाठी खोल संरक्षण तयार करा. हा डेमो दोन स्तरांची सुरक्षा दाखवतो जे एकत्र काम करतात:

**भाग 1: LangChain4j इनपुट गार्डरेल्स** - LLM पर्यंत पोहोचण्यापूर्वी धोकादायक प्रॉम्प्ट अवरोधित करा. निषिद्ध कीवर्ड किंवा नमुन्यांसाठी सानुकूल गार्डरेल्स तयार करा. हे तुमच्या कोडमध्ये चालतात, त्यामुळे वेगाने आणि विनामूल्य आहेत.

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

**भाग 2: प्रदाता सुरक्षा फिल्टर्स** - GitHub मॉडेल्समध्ये अंतर्निहित फिल्टर्स आहेत जे गार्डरेल्स कदाचित चुकवतील असे पकडतात. गंभीर उल्लंघनांसाठी हार्ड ब्लॉक्स (HTTP 400 त्रुटी) आणि सौम्य नकार (AI शिष्टतेने नाकारते) या प्रकारचे फिल्टर्स आहेत.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) उघडा आणि विचारा:
> - "InputGuardrail म्हणजे काय आणि माझे स्वतःचे कसे तयार करू?"
> - "हार्ड ब्लॉक आणि सौम्य नकार यामध्ये काय फरक आहे?"
> - "दोन्ही गार्डरेल्स आणि प्रदाता फिल्टर्स एकत्र का वापरावे?"

## पुढील पावले

**पुढील मॉड्यूल:** [01-परिचय - LangChain4j आणि Azure वर gpt-5 सह सुरू करणे](../01-introduction/README.md)

---

**नेव्हिगेशन:** [← मुख्याकडे परत जा](../README.md) | [पुढे: Module 01 - परिचय →](../01-introduction/README.md)

---

## समस्यांची सोडवणूक

### प्रथम-म्हणून Maven बिल्ड

**समस्या**: पहिल्या `mvn clean compile` किंवा `mvn package` मध्ये खूप वेळ लागतो (10-15 मिनिटे)

**कारण**: Maven ला सर्व प्रोजेक्ट अवलंबित्वे (Spring Boot, LangChain4j लायब्ररी, Azure SDKs, इ.) पहिल्या बिल्डमध्ये डाउनलोड करणे आवश्यक असते.

**उपाय**: ही सामान्य कल्पना आहे. पुढील बिल्ड्स अधिक जलद होतील कारण अवलंबित्वे स्थानिकरित्या कॅश केली जातील. डाउनलोड वेळ तुमच्या नेटवर्क स्पीडवर अवलंबून आहे.

### PowerShell Maven कमांड सिंटॅक्स

**समस्या**: Maven कमांड्समध्ये त्रुटी `Unknown lifecycle phase ".mainClass=..."` दिसते
**कारण**: PowerShell `=` ला एक व्हेरिएबल असाइनमेंट ऑपरेटर म्हणून समजते, ज्यामुळे Maven प्रॉपर्टी सिंटॅक्स तुटतो

**उपाय**: Maven कमांडच्या आधी stop-parsing ऑपरेटर `--%` वापरा:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ऑपरेटर PowerShell ला उर्वरित सर्व आर्ग्युमेंट्स Maven कडे अक्षरशः पारित करण्यास सांगतो, इंटरप्रिटेशन न करता.

### Windows PowerShell Emoji डिस्प्ले

**समस्या**: PowerShell मध्ये AI प्रतिसादांमध्ये emoji च्या ऐवजी garbage characters (उदा., `????` किंवा `â??`) दिसतात

**कारण**: PowerShell ची डीफॉल्ट एन्कोडिंग UTF-8 emoji ला समर्थन करत नाही

**उपाय**: Java अ‍ॅप्लिकेशन्स चालवण्यापूर्वी हा कमांड चालवा:
```cmd
chcp 65001
```

हे टर्मिनलमध्ये UTF-8 एन्कोडिंग बळजबरी लावते. पर्यायीपणे, Windows Terminal वापरा ज्यामध्ये चांगले Unicode समर्थन आहे.

### API कॉल्स डिबग करणे

**समस्या**: Authentication त्रुटी, दर मर्यादा, किंवा AI मॉडेलकडून अनपेक्षित प्रतिक्रिया

**उपाय**: उदाहरणांमध्ये `.logRequests(true)` आणि `.logResponses(true)` समाविष्ट आहेत जे API कॉल्स कन्सोलवर दर्शवतात. हे authentication त्रुटी, दर मर्यादा, किंवा अनपेक्षित प्रतिसाद शोधण्यात मदत करते. उत्पादनात या फ्लॅग्स काढा जेणेकरून लॉग आवाज कमी होईल.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**महोदय**:
हा दस्तऐवज AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून अनुवादित केलेला आहे. आम्ही अचूकतेसाठी प्रयत्न करत असलो तरी, कृपया लक्षात घ्या की स्वयंचलित अनुवादांमध्ये त्रुटी किंवा अपूर्णता असू शकते. मूळ भाषेतील दस्तऐवज अधिकृत स्रोत म्हणून मानला जावा. महत्त्वाची माहिती असल्यास, व्यावसायिक मानवी अनुवाद करण्यास सल्ला देण्यात येतो. या अनुवादाच्या वापरामुळे उद्भवलेल्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थव्यक्तीसाठी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->