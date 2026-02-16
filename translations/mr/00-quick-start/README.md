# Module 00: क्विक स्टार्ट

## अनुक्रमणिका

- [परिचय](../../../00-quick-start)
- [LangChain4j म्हणजे काय?](../../../00-quick-start)
- [LangChain4j निर्भरता](../../../00-quick-start)
- [आवश्यक अटी](../../../00-quick-start)
- [सेटअप](../../../00-quick-start)
  - [1. तुमचा GitHub टोकन मिळवा](../../../00-quick-start)
  - [2. टोकन सेट करा](../../../00-quick-start)
- [उदाहरण चालवा](../../../00-quick-start)
  - [1. बेसिक चॅट](../../../00-quick-start)
  - [2. प्रॉम्प्ट पॅटर्न्स](../../../00-quick-start)
  - [3. फंक्शन कॉलिंग](../../../00-quick-start)
  - [4. डॉक्युमेंट प्रश्नोत्तरे (RAG)](../../../00-quick-start)
  - [5. जबाबदार AI](../../../00-quick-start)
- [प्रत्येक उदाहरण काय दाखवते](../../../00-quick-start)
- [पुढील पावले](../../../00-quick-start)
- [समस्या निवारण](../../../00-quick-start)

## परिचय

हा क्विकस्टार्ट तुमचा LangChain4j सह शक्य तितक्या लवकर सुरू होण्यासाठी तयार केला आहे. हा LangChain4j आणि GitHub Models सह AI अनुप्रयोग तयार करण्याच्या मूळ गोष्टी कव्हर करतो. पुढील मॉड्यूलमध्ये तुम्ही LangChain4j सोबत Azure OpenAI वापरून अधिक प्रगत अनुप्रयोग तयार कराल.

## LangChain4j म्हणजे काय?

LangChain4j ही एक Java लायब्ररी आहे जिला वापरून AI-शक्तीशाली अनुप्रयोग तयार करणे सुलभ होते. HTTP क्लायंट्स आणि JSON पार्सिंगशिवाय तुम्ही स्वच्छ Java API सह काम करू शकता. 

LangChain मधील "chain" म्हणजे अनेक घटकांना साखळीने जोडणे - जसे प्रॉम्प्ट ला मॉडेलला जोडणे, नंतर पार्सरशी जोडणे, किंवा अनेक AI कॉल्स एकमेकांना फीड देणे. हा क्विकस्टार्ट मूलभूत गोष्टींवर लक्ष केंद्रित करतो, अधिक जटिल साखळ्यांवर जाण्यापूर्वी.

<img src="../../../translated_images/mr/langchain-concept.ad1fe6cf063515e1.webp" alt="LangChain4j Chaining Concept" width="800"/>

*LangChain4j मधील साखळी घटक - शक्तिशाली AI कार्यप्रवाह तयार करणारे बिल्डिंग ब्लॉक्स*

आपण तीन मुख्य घटक वापरू:

**ChatLanguageModel** - AI मॉडेल संवादासाठी इंटरफेस. `model.chat("prompt")` कॉल करा आणि प्रतिसाद स्ट्रिंग मिळवा. आपण `OpenAiOfficialChatModel` वापरतो जो GitHub Models सारख्या OpenAI-सुसंगत एंडपॉइंट्सवर काम करतो.

**AiServices** - प्रकार-सुरक्षित AI सेवा इंटरफेस तयार करते. पद्धती व्याख्या करा, त्यांना `@Tool` ने नोटेट करा, आणि LangChain4j त्यांचे समन्वय करतो. गरज असताना AI आपोआप तुमच्या Java पद्धती कॉल करतो.

**MessageWindowChatMemory** - संभाषण इतिहास राखते. याशिवाय प्रत्येक विनंती स्वतंत्र असते. हे असल्यास AI आधीच्या संदेशांची आठवण ठेवतो आणि अनेक वळणांवर संदर्भ टिकवतो.

<img src="../../../translated_images/mr/architecture.eedc993a1c576839.webp" alt="LangChain4j Architecture" width="800"/>

*LangChain4j आर्किटेक्चर - मुख्य घटक एकत्र काम करून तुमचे AI अनुप्रयोग सक्षमी करतात*

## LangChain4j निर्भरता

हा क्विक स्टार्ट [`pom.xml`](../../../00-quick-start/pom.xml) मध्ये दोन Maven निर्भरता वापरतो:

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

`langchain4j-open-ai-official` मॉड्यूल `OpenAiOfficialChatModel` वर्ग पुरवतो जो OpenAI-सुसंगत API शी जोडतो. GitHub Models त्याच API स्वरूपाचा वापर करते, त्यामुळे कोणत्याही विशेष अडॅप्टरची गरज नाही - फक्त बेस URL `https://models.github.ai/inference` दाखवा.

## आवश्यक अटी

**Dev Container वापरत आहात?** Java आणि Maven आधीच इंस्टॉल आहेत. तुम्हाला फक्त GitHub वैयक्तिक प्रवेश टोकन आवश्यक आहे.

**स्थानिक विकास:**
- Java 21+, Maven 3.9+
- GitHub वैयक्तिक प्रवेश टोकन (खालील सूचना पहा)

> **टीप:** हा मॉड्यूल GitHub Models मधून `gpt-4.1-nano` वापरतो. कोडमधील मॉडेल नाव बदला नाही - तो GitHub च्या उपलब्ध मॉडेल्ससाठी कॉन्फिगर केला आहे.

## सेटअप

### 1. तुमचा GitHub टोकन मिळवा

1. [GitHub Settings → Personal Access Tokens](https://github.com/settings/personal-access-tokens) येथे जा
2. "Generate new token" क्लिक करा
3. वर्णनात्मक नाव सेट करा (उदा. "LangChain4j Demo")
4. कालबाह्यता सेट करा (7 दिवस शिफारसीय)
5. "Account permissions" मध्ये "Models" शोधा आणि "Read-only" सेट करा
6. "Generate token" क्लिक करा
7. टोकन कॉपी करा आणि जतन करा - तुम्हाला ते पुन्हा दिसणार नाही

### 2. टोकन सेट करा

**पर्याय 1: VS Code वापरून (शिफारसीय)**

VS Code वापरत असल्यास, प्रोजेक्ट रूटमधील `.env` फाईलमध्ये टोकन जोडा:

जर `.env` फाईल अस्तित्वात नसेल, तर `.env.example` कॉपी करा `.env` मध्ये किंवा नवीन `.env` तयार करा.

**उदाहरण `.env` फाईल:**
```bash
# /workspaces/LangChain4j-for-Beginners/.env मध्ये
GITHUB_TOKEN=your_token_here
```

नंतर तुम्ही एक्सप्लोररमध्ये एखाद्या डेमो फाईलवर (उदा. `BasicChatDemo.java`) उजवा-क्लिक करून **"Run Java"** निवडू शकता किंवा रन आणि डिबग पॅनलमधील लॉन्च कॉन्फिगरेशन वापरू शकता.

**पर्याय 2: टर्मिनल वापरून**

टोकन पर्यावरण चल म्हणून सेट करा:

**Bash:**
```bash
export GITHUB_TOKEN=your_token_here
```

**PowerShell:**
```powershell
$env:GITHUB_TOKEN=your_token_here
```

## उदाहरणे चालवा

**VS Code वापरताना:** एक्सप्लोररमध्ये एखाद्या डेमो फाईलवर उजवा-क्लिक करा आणि **"Run Java"** निवडा, किंवा रन आणि डिबग पॅनलमधील लॉन्च कॉन्फिगरेशन वापरा (इतर सर्वप्रथम टोकन `.env` मध्ये जोडलेले असावे).

**Maven वापरताना:** कमांड लाइनवरून चालवू शकता:

### 1. बेसिक चॅट

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

शून्य-शॉट, थोड्या-शॉट, विचारसाखळी, आणि भूमिका-आधारित प्रॉम्प्ट दाखवते.

### 3. फंक्शन कॉलिंग

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ToolIntegrationDemo
```

AI आपोआप तुमच्या Java पद्धती कॉल करतो जेव्हा आवश्यक असते.

### 4. डॉक्युमेंट प्रश्नोत्तरे (RAG)

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.SimpleReaderDemo
```

`document.txt` मधील सामग्रीविषयी प्रश्न विचारा.

### 5. जबाबदार AI

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.ResponsibleAIDemo
```

AI सुरक्षा फिल्टर्स कसे हानिकारक सामग्री अडवतात ते पहा.

## प्रत्येक उदाहरण काय दाखवते

**बेसिक चॅट** - [BasicChatDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java)

LangChain4j चे साधे स्वरूप येथे पहा. तुम्ही `OpenAiOfficialChatModel` तयार कराल, `.chat()` ने प्रॉम्प्ट पाठवाल, आणि प्रतिसाद मिळवाल. हे मॉडेलस प्रारंभ करण्यासाठी, कस्टम एंडपॉइंट्स आणि API की वापरण्यासाठी आधार दर्शवते. या पद्धती समजल्यावर बाकी सगळं त्यावर आधारित तयार होते.

```java
ChatLanguageModel model = OpenAiOfficialChatModel.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(System.getenv("GITHUB_TOKEN"))
    .modelName("gpt-4.1-nano")
    .build();

String response = model.chat("What is LangChain4j?");
System.out.println(response);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`BasicChatDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/BasicChatDemo.java) उघडा आणि विचारा:
> - "GitHub Models वरून Azure OpenAI कसे स्विच कराल या कोडमध्ये?"
> - "OpenAiOfficialChatModel.builder() मध्ये कोणते इतर पॅरामीटर्स कॉन्फिगर करू शकतो?"
> - "पूर्ण प्रतिसादाची वाट पाहण्याऐवजी स्ट्रिमिंग प्रतिसाद घालायचे कसे?"

**प्रॉम्प्ट इंजिनीअरिंग** - [PromptEngineeringDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/PromptEngineeringDemo.java)

तुम्हाला मॉडेलशी बोलायचे कसे कळले आहे, आता त्याला काय सांगायचे ते पाहूया. हे डेमो त्याच मॉडेल सेटअपचा वापर करून पाच वेगवेगळ्या प्रॉम्प्ट पॅटर्न्स दाखवते. शून्य-शॉट थेट सूचना, थोड्या-शॉट उदाहरणांतून शिकते, विचारसाखळी प्रॉम्प्ट्स ज्यात तर्क प्रक्रिया असते, आणि भूमिका-आधारित प्रॉम्प्ट्स ज्यात संदर्भ ठेवला जातो. तुम्हाला कसे एकाच मॉडेलमध्ये विनंतीच्या फ्रेमिंगनुसार नाट्यमय फरक दिसेल.

डेमोमध्ये प्रॉम्प्ट टेम्प्लेट्स देखील दाखवले आहेत, जे पुनर्वापरयोग्य प्रॉम्प्ट तयार करण्याचा शक्तिशाली मार्ग आहे.
खालील उदाहरण LangChain4j `PromptTemplate` वापरून व्हेरिएबल्स भरते. AI दिलेल्या गंतव्य आणि क्रियेनुसार उत्तर देतो.

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
> - "शून्य-शॉट आणि थोड्या-शॉट प्रॉम्प्टिंगमध्ये काय फरक आहे आणि कोणती कधी वापरावी?"
> - "तापमान पॅरामीटर मॉडेलच्या प्रतिसादावर कसा परिणाम करतो?"
> - "उत्पादनात प्रॉम्प्ट इंजेक्शन हल्ले टाळण्यासाठी कोणत्या तंत्रांचा वापर करावा?"
> - "सामान्य पॅटर्नसाठी पुनर्वापरयोग्य PromptTemplate वस्तू कश्या तयार करायच्या?"

**टूल इंटिग्रेशन** - [ToolIntegrationDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java)

इथे LangChain4j शक्तिशाली बनतो. तुम्ही `AiServices` वापरून AI सहाय्यक तयार कराल जो तुमच्या Java पद्धती कॉल करू शकतो. फक्त पद्धतींना `@Tool("description")` ने नोटेट करा आणि LangChain4j बाकी सांभाळतो - AI वापरकर्त्याचे प्रश्नानुसार कोणता टूल वापरायचा ते ठरवतो. हे फंक्शन कॉलिंग दाखवते, जे AI ला क्रिया करू देते, फक्त प्रश्नांची उत्तरे देणे नव्हे.

```java
@Tool("Performs addition of two numeric values")
public double add(double a, double b) {
    return a + b;
}

MathAssistant assistant = AiServices.create(MathAssistant.class, model);
String response = assistant.chat("What is 25 plus 17?");
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`ToolIntegrationDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ToolIntegrationDemo.java) उघडा आणि विचारा:
> - "@Tool अॅनोटेशन कसे काम करते आणि LangChain4j त्याबाबत मागे काय करते?"
> - "AI एकावेळेस अनेक टूल्स कॉल करू शकतो का क्लिष्ट समस्या सोडवण्यासाठी?"
> - "जर टूलने अपवाद दिला तर काय होईल - मी त्रुटी कशा हाताळू?"
> - "हा कॅलक्युलेटर उदाहरणाऐवजी खरा API कसा समाकलित करणार?"

**डॉक्युमेंट प्रश्नोत्तरे (RAG)** - [SimpleReaderDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java)

इथे तुम्हाला RAG (retrieval-augmented generation) ची पायाभूत मूळ संकल्पना दिसेल. मॉडेलच्या प्रशिक्षण डेटावर अवलंबून न राहता, तुम्ही [`document.txt`](../../../00-quick-start/document.txt) मधून सामग्री लोड करता आणि प्रॉम्प्टमध्ये समाविष्ट करता. AI फक्त तुमच्या दस्तऐवजावरून उत्तर देते, सामान्य ज्ञानावर नाही. ही आपली स्वतःची डेटा प्रणाली तयार करण्याची पहिली पायरी आहे.

```java
Document document = FileSystemDocumentLoader.loadDocument("document.txt");
String content = document.text();

String prompt = "Based on this document: " + content + 
                "\nQuestion: What is the main topic?";
String response = model.chat(prompt);
```

> **टीप:** ही सोपी पद्धत सर्व दस्तऐवज प्रॉम्प्टमध्ये लोड करते. मोठ्या फाइलसाठी (>10KB), तुम्ही संदर्भ मर्यादा ओलांडाल. मॉड्यूल 03 मध्ये चंकिंग आणि व्हेक्टर सर्च कशी वापरायची ते दिले आहे.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`SimpleReaderDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/SimpleReaderDemo.java) उघडा आणि विचारा:
> - "RAG कसे AI भ्रम टाळते जेव्हा मॉडेलच्या प्रशिक्षण डेटाचा वापर करते?"
> - "ही सोपी पद्धत आणि वेक्टर एम्बेडिंग वापरून पुनर्प्राप्तीमध्ये फरक काय आहे?"
> - "कसे अनेक दस्तऐवज किंवा मोठ्या ज्ञान आधारासाठी या पद्धतीचे प्रमाण वाढवू?"
> - "AI केवळ दिलेले संदर्भ वापरेल यासाठी प्रॉम्प्ट स्ट्रक्चरची सर्वोत्तम पद्धत काय?"

**जबाबदार AI** - [ResponsibleAIDemo.java](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java)

सुरक्षा सुलभ करण्यासाठी गहन संरक्षण तयार करा. हे डेमो दोन स्तरांचे संरक्षण एकत्र दाखवते:

**भाग 1: LangChain4j इनपुट गार्डरेइल्स** - धोकादायक प्रॉम्प्ट एस LLM पर्यंत पोहोचण्यापूर्वी ब्लॉक करतात. कस्टम गार्डरेइल्स तयार करा जे निषिद्ध कीवर्ड किंवा पॅटर्न तपासतात. हे तुमच्या कोडमध्ये चालतात, त्यामुळे जलद आणि मोफत आहे.

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

**भाग 2: पुरवठादार सुरक्षा फिल्टर्स** - GitHub Models मध्ये अंगभूत फिल्टर्स आहेत जे तुमच्या गार्डरेइल्सला चुकल्या जाऊ शकलेल्या गोष्टी पकडतात. तुम्हाला कठोर ब्लॉक (HTTP 400 त्रुटी) कडक उल्लंघनांसाठी आणि सौम्य नकार जेथे AI नम्रतेने नकार देते ते दिसेल.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** [`ResponsibleAIDemo.java`](../../../00-quick-start/src/main/java/com/example/langchain4j/quickstart/ResponsibleAIDemo.java) उघडा आणि विचारा:
> - "InputGuardrail काय आहे आणि माझे स्वतःचे कसे तयार करायचे?"
> - "कठोर ब्लॉक आणि सौम्य नकार यामध्ये काय फरक आहे?"
> - "गर्डरेइल्स आणि पुरवठादार फिल्टर्स एकत्र का वापराव्यात?"

## पुढील पावले

**पुढील मॉड्यूल:** [01-introduction - LangChain4j आणि gpt-5 सह Azure वर सुरू करणे](../01-introduction/README.md)

---

**नेव्हिगेशन:** [← मुख्य पानावर परत जा](../README.md) | [पुढील: Module 01 - परिचय →](../01-introduction/README.md)

---

## समस्या निवारण

### प्रथमच Maven बांधणी

**समस्या:** प्रथम `mvn clean compile` किंवा `mvn package` जास्त वेळ घेतो (१०-१५ मिनिटे)

**कारण:** Maven ला सर्व प्रोजेक्ट निर्भरता (Spring Boot, LangChain4j लायब्ररी, Azure SDKs, इ.) डाउनलोड कराव्या लागतात प्रथम बिल्डमध्ये.

**उपाय:** हे सामान्य आहे. पुढील बिल्ड अतिशय वेगवान होतील कारण निर्भरता स्थानिक कॅशेत असतील. डाउनलोड वेळ तुमच्या नेटवर्कच्या गतीवर अवलंबून आहे.
### PowerShell Maven कमांड सिंटॅक्स

**समस्या**: Maven कमांड `Unknown lifecycle phase ".mainClass=..."` त्रुटीसह अयशस्वी होते

**कारण**: PowerShell `=` हे व्हेरिएबल असाइनमेंट ऑपरेटर म्हणून समजते, ज्यामुळे Maven प्रॉपर्टी सिंटॅक्स तुटते

**उपाय**: Maven कमांडच्या आधी stop-parsing ऑपरेटर `--%` वापरा:

**PowerShell:**
```powershell
mvn --% compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

**Bash:**
```bash
mvn compile exec:java -Dexec.mainClass=com.example.langchain4j.quickstart.BasicChatDemo
```

`--%` ऑपरेटर PowerShell ला उर्वरित सर्व आर्ग्युमेंट्स Maven कडे व्याख्याशिवाय थेट पाठवण्यास सांगतो.

### Windows PowerShell Emoji डिस्प्ले

**समस्या**: PowerShell मध्ये AI प्रतिक्रियांमध्ये emojis ऐवजी खराब अक्षरं (उदा., `????` किंवा `â??`) दिसतात

**कारण**: PowerShell ची डीफॉल्ट एन्कोडिंग UTF-8 emojis ला सपोर्ट करत नाही

**उपाय**: Java अ‍ॅप्लिकेशन्स चालवण्यापूर्वी हा कमांड चालवा:
```cmd
chcp 65001
```

हे टर्मिनलमध्ये UTF-8 एन्कोडिंग बळकट करते. पर्यायी म्हणून, Windows Terminal वापरा ज्यात Unicode सपोर्ट चांगला आहे.

### API कॉल डीबगिंग

**समस्या**: Authentication त्रुटी, दर मर्यादा किंवा AI मॉडेलकडून अनपेक्षित प्रतिक्रिया

**उपाय**: उदाहरणांमध्ये `.logRequests(true)` आणि `.logResponses(true)` आहेत ज्यामुळे API कॉल्स कन्सोलमध्ये दाखवले जातात. हे authentication त्रुटी, दर मर्यादा किंवा अनपेक्षित प्रतिक्रिया शोधण्यास मदत करते. उत्पादनात या फ्लॅग्ज काढा जेणेकरून लॉगचा आवाज कमी होईल.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**मुक्तता सुचना**:
हा दस्तऐवज AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) चा वापर करून अनुवादित केला आहे. आम्ही अचूकतेसाठी प्रयत्न करतो, तरी कृपया लक्षात घ्या की स्वयंचलित अनुवादांमध्ये चुका किंवा अचूकतेची तफावत असू शकते. मूळ दस्तऐवज त्याच्या मूळ भाषेत अधिकृत स्रोत मानला जावा. महत्त्वाच्या माहितीसाठी व्यावसायिक मानवी अनुवाद शिफारस केली जाते. या अनुवादाच्या वापरामुळे झालेल्या कोणत्याही गैरसमजुती किंवा चुकीच्या समजुतीसाठी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->