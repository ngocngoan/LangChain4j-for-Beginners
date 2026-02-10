# Module 02: GPT-5.2 सह प्रॉम्प्ट इंजिनिअरिंग

## विषय सूची

- [तुम्हाला काय शिकायला मिळणार आहे](../../../02-prompt-engineering)
- [पूर्वअट](../../../02-prompt-engineering)
- [प्रॉम्प्ट इंजिनिअरिंग समजून घेणे](../../../02-prompt-engineering)
- [हे कसे LangChain4j वापरते](../../../02-prompt-engineering)
- [मुख्य पॅटर्न्स](../../../02-prompt-engineering)
- [मौजूदा Azure संसाधने वापरणे](../../../02-prompt-engineering)
- [अॅप्लिकेशन स्क्रीनशॉट्स](../../../02-prompt-engineering)
- [पॅटर्न्सचा अभ्यास](../../../02-prompt-engineering)
  - [कमी विरक्तता विरुध्द जास्त विरक्तता](../../../02-prompt-engineering)
  - [कार्याचा अंमलबजावणी (टूल प्रीलॉक्स)](../../../02-prompt-engineering)
  - [स्वतःचे प्रतिबिंबित करणारा कोड](../../../02-prompt-engineering)
  - [संचालित विश्लेषण](../../../02-prompt-engineering)
  - [बहु-टर्न चॅट](../../../02-prompt-engineering)
  - [टप्प्याटप्प्याने विचारसूत्र](../../../02-prompt-engineering)
  - [बांधिल केलेले आउटपुट](../../../02-prompt-engineering)
- [तुम्ही प्रत्यक्षात काय शिकत आहात](../../../02-prompt-engineering)
- [पुढील टप्पे](../../../02-prompt-engineering)

## तुम्हाला काय शिकायला मिळणार आहे

पूर्वीच्या module मध्ये, तुम्ही पाहिले की कसे स्मृती संभाषणात्मक AI सक्षम करते आणि GitHub मॉडेल्सचा वापर करून मूलभूत संवाद साधला जातो. आता आपण लक्ष केंद्रित करू की तुम्ही कसे प्रश्न विचारता – म्हणजेच प्रॉम्प्ट्स कसे तयार करता – Azure OpenAI च्या GPT-5.2 वापरून. तुमच्या प्रॉम्प्ट्सची रचना नक्की कशी असेल त्याचा प्रतिसादांच्या गुणवत्तेवर मोठा परिणाम होतो.

आम्ही GPT-5.2 वापरणार आहोत कारण त्यात विचार करण्यावर नियंत्रण ठेवण्याची क्षमता आहे – तुम्ही मॉडेलला उत्तर देण्यापूर्वी किती विचार करायचा आहे ते सांगू शकता. यामुळे वेगवेगळ्या प्रॉम्प्टिंग रणनीती स्पष्ट होतात आणि तुम्हाला समजेल की कोणत्या परिस्थितीत कोणता दृष्टिकोन वापरायचा. याशिवाय, GitHub मॉडेल्सच्या तुलनेत Azure GPT-5.2 वर कमी रेट लिमिट्स आहेत, ज्याचा आपण फायदा घेऊ.

## पूर्वअट

- Module 01 पूर्ण झालेले असणे (Azure OpenAI संसाधने तैनात केलेले)
- root डिरेक्टरीत `.env` फाइल, ज्यात Azure ची प्रमाणपत्रे आहेत (Module 01 मधील `azd up` ने तयार केलेली)

> **टिप:** जर तुम्ही Module 01 पूर्ण केले नसेल, तर प्रथम तिथल्या तैनातीसंबंधी सूचनांचे पालन करा.

## प्रॉम्प्ट इंजिनिअरिंग समजून घेणे

प्रॉम्प्ट इंजिनिअरिंग म्हणजे अशी इनपुट टेक्स्ट डिझाइन करणे जे तुम्हाला कायमस्वरूपी हव्या असलेल्या निकालांपर्यंत नेते. हा फक्त प्रश्न विचारण्याचा विषय नाही – तर असा विनंती तयार करण्याचा जो मार्ग मॉडेलला नेमके काय हवे आहे आणि ते कसे द्यायचे ते समजावून सांगतो.

हे समजा तुम्ही एखाद्या सहकाऱ्याला सूचना देता आहात. "बग दुरुस्त करा" ही अस्पष्ट सूचना आहे. पण "UserService.java च्या 45 व्या ओळीत null pointer exception दुरुस्त करण्यासाठी null check जोडा" ही विशेष माहिती देते. भाषिक मॉडेल्सही तशीच काम करतात –विशिष्टता आणि रचना महत्त्वाची आहे.

## हे कसे LangChain4j वापरते

हा module प्रगत प्रॉम्प्टिंग पॅटर्न LangChain4j च्या आधीच्या module मधील तत्त्वावर आधारित दाखवतो, मुख्यतः प्रॉम्प्ट रचना आणि विचार करण्यावर नियंत्रण यावर लक्ष केंद्रित करत.

<img src="../../../translated_images/mr/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*LangChain4j तुमचे प्रॉम्प्ट Azure OpenAI GPT-5.2 शी जोडते*

**आश्रितता** - Module 02 मध्ये `pom.xml` मध्ये परिभाषित खालील langchain4j आश्रितता वापरली जातात:
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**OpenAiOfficialChatModel कॉन्फिगरेशन** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

चॅट मॉडेल OpenAI अधिकृत क्लायंट वापरून स्प्रिंग बीन म्हणून मॅन्युअली कॉन्फिगर केलेले आहे, जे Azure OpenAI endpoints ला समर्थन देते. Module 01 पासून मुख्य फरक म्हणजे प्रॉम्प्ट्सच्या संरचनेमध्ये आहे, `chatModel.chat()` ला पाठवलेल्या प्रॉम्प्ट्समध्ये, मॉडेल सेटअपमध्ये नाही.

**System आणि User संदेश** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j स्पष्टतेसाठी मेसेज टाइप वेगळे करतो. `SystemMessage` AI चे वर्तन आणि संदर्भ सेट करतो (उदा. "तुम्ही एक कोड रिव्ह्युअर आहात"), तर `UserMessage` खरी विनंती ठेवते. ही वेगळी व्यवस्था विविध वापरकर्त्यांच्या क्वेरीजसाठी एकसारखे AI वर्तन राखण्यास मदत करते.

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/mr/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage कायमस्वरूपी संदर्भ प्रदान करते तर UserMessages वैयक्तिक विनंत्या ठेवतात*

**MessageWindowChatMemory बहु-टर्न साठी** - बहु-टर्न संभाषणासाठी आपण Module 01 मधील `MessageWindowChatMemory` पुनःवापर करतो. प्रत्येक सत्राचा स्वतःचा मेमरी इन्स्टन्स `Map<String, ChatMemory>` मध्ये संग्रहित केला जातो, ज्यामुळे अनेक एकाच वेळी संभाषणे होतात आणि संदर्भ गोंधळत नाही.

**प्रॉम्प्ट टेम्प्लेट्स** - येथे खरी लक्ष केंद्रित प्रॉम्प्ट इंजिनिअरिंगवर आहे, नव्या LangChain4j API नाहीत. प्रत्येक पॅटर्न (कमी विरक्तता, जास्त विरक्तता, कार्य अंमलबजावणी इ.) तेच `chatModel.chat(prompt)` पद्धत वापरते परंतु काळजीपूर्वक रचलेले प्रॉम्प्ट स्ट्रिंग्स वापरतात. XML टॅग्स, सूचना, आणि फॉरमॅटिंग हे सर्व प्रॉम्प्ट टेक्स्टचा भाग आहेत, LangChain4j वैशिष्ट्यांचा नाही.

**विचार नियंत्रण** - GPT-5.2 ची विचार करण्याची क्षमता प्रॉम्प्ट निर्देशांद्वारे नियंत्रित केली जाते, जसे की "कमाल 2 विचार पायऱ्या" किंवा "संपूर्णपणे तपासणे". हे प्रॉम्प्ट इंजिनिअरिंगचे तंत्र आहेत, LangChain4j ची सेटिंग्ज नाहीत. लायब्ररी फक्त तुमचे प्रॉम्प्ट मॉडेलकडे पाठवते.

मुख्य मुद्दा: LangChain4j संरचना पुरवते (मॉडेल कनेक्शन [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java) द्वारे, मेमरी, मेसेज हॅंडलिंग [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) द्वारे) आणि हा module तुम्हाला त्या संरचनेत प्रभावी प्रॉम्प्ट कसे तयार करायचे ते शिकवतो.

## मुख्य पॅटर्न्स

सर्व प्रश्नांनासाठी एकसारखा दृष्टिकोन लागू होत नाही. काही प्रश्नांना जलद उत्तर हवे, तर काहींना सखोल विचार हवा. काहीांना दिसणारा विचार हवा, तर काहींना फक्त निकाल हवा. हा module आठ प्रॉम्प्टिंग पॅटर्न्स कव्हर करतो - प्रत्येक वेगवेगळ्या परिस्थितीसाठी ऑप्टिमाइझ केलेला. तुम्ही सगळ्यांवर एक्सपेरिमेंट कराल आणि कळेल की कोणता दृष्टिकोन केव्हा प्रभावी आहे.

<img src="../../../translated_images/mr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*आठ प्रॉम्प्ट इंजिनिअरिंग पॅटर्न्स आणि त्यांचे वापर केसचे आढावा*

<img src="../../../translated_images/mr/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*कमी विरक्तता (जलद, थेट) विरुद्ध जास्त विरक्तता (संपूर्ण, शोधक) विचार करण्याच्या पद्धती*

**कमी विरक्तता (जलद आणि लक्ष केंद्रीत)** - सोप्या प्रश्नांसाठी, जिथे तुम्हाला जलद, थेट उत्तर हवेत. मॉडेल कमी विचार करते - कमाल 2 टप्पे. गणना, शोध किंवा सरळ प्रश्नांसाठी वापरा.

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilot सह एक्सप्लोर करा:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) उघडा आणि विचारा:
> - "कमी विरक्तता व जास्त विरक्तता प्रॉम्प्टिंग पॅटर्न्समध्ये काय फरक आहे?"
> - "प्रॉम्प्टमधील XML टॅग्स AI च्या प्रतिसादाच्या रचनेस कसे मदत करतात?"
> - "मी स्व-प्रतिबिंबित पॅटर्न्स आणि थेट सूचनांचा वापर कधी करणे योग्य आहे?"

**जास्त विरक्तता (सखोल आणि पूर्ण)** - जटिल प्रश्नांसाठी जिथे तुम्हाला सखोल विश्लेषण हवे. मॉडेल संपूर्णपणे तपासते आणि सविस्तर विचार दाखवते. सिस्टीम डिझाइन, आर्किटेक्चर निर्णय, किंवा जटिल संशोधनासाठी वापरा.

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**कार्याचा अंमलबजावणी (टप्प्याटप्प्याने प्रगती)** - बहु-टप्प्यातील कार्यपत्रकांसाठी. मॉडेल प्रारंभी योजना देते, प्रत्येक टप्पा सांगते, नंतर सारांश देते. मिग्रेशन्स, अंमलबजावणी किंवा एखाद्या बहु-टप्प्याच्या प्रक्रियेसाठी वापरा.

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

चेन-ऑफ-थॉट प्रॉम्प्टिंग मॉडेलला त्याचा विचार प्रक्रियेचा आढावा द्यायला सांगते, ज्यामुळे जटिल कामामध्ये अचूकता वाढते. टप्प्याटप्प्याने विश्लेषण मनुष्य आणि AI दोघांनाही लॉजिक समजण्यास मदत करते.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat सोबत प्रयत्न करा:** या पॅटर्नबद्दल विचारा:
> - "दीर्घकालीन ऑपरेशन्ससाठी कार्य अंमलबजावणी पॅटर्न कसा अनुकूल करायचा?"
> - "उत्पादन अॅप्लिकेशन्समध्ये टूल प्रीलॉक्सचे रचनेचे सर्वोत्तम मार्ग कोणते?"
> - "UI मध्ये मधल्या प्रगती अद्यतने कशी कॅप्चर आणि दाखवता येतील?"

<img src="../../../translated_images/mr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*योजना → अंमलबजावणी → सारांश, बहु-टप्प्यांसाठी कार्यप्रवाह*

**स्वतःचे प्रतिबिंबित करणारा कोड** - उत्पादन-गुणवत्तेचा कोड तयार करण्यासाठी. मॉडेल कोड तयार करते, गुणवत्ता निकषांशी तपासणी करते, आणि पुनरावृत्तीने सुधारणा करते. नवीन फिचर्स किंवा सेवा बांधताना वापरा.

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/mr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*पुनरावृत्ती सुधारणा लूप - तयार करा, मूल्यांकन करा, समस्या ओळखा, सुधारणा करा, पुनरावृत्ती करा*

**संचालित विश्लेषण** - सातत्यपूर्ण मूल्यांकनासाठी. मॉडेल कोड निश्चित चौकटीतून तपासते (योग्यता, सराव, कार्यक्षमता, सुरक्षा). कोड रिव्ह्यूसाठी किंवा गुणवत्ता मूल्यमापनासाठी वापरा.

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat सोबत प्रयत्न करा:** संचालित विश्लेषणावर विचार करा:
> - "वेगवेगळ्या प्रकारच्या कोड रिव्ह्यूसाठी विश्लेषण चौकट कशी सानुकूलित करावी?"
> - "संचालित आउटपुट प्रोग्रामॅटिकली कसा पार्स आणि वापरावा?"
> - "वेगवेगळ्या रिव्ह्यू सत्रांमध्ये सातत्यपूर्ण गंभीरता स्तर कसा राखावा?"

<img src="../../../translated_images/mr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*चौकटीनुसार चार श्रेणींसह सातत्यपूर्ण कोड रिव्ह्यूसाठी फ्रेमवर्क*

**बहु-टर्न चॅट** - संदर्भ आवश्यक असलेल्या संवादांसाठी. मॉडेल मागील संदेश लक्षात ठेवते आणि त्यावर आधारित वाढवते. संवादात्मक सहाय्यासाठी किंवा जटिल प्रश्नोत्तरे यासाठी वापरा.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/mr/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*कसे संभाषणाचा संदर्भ अनेक टर्नमध्ये जमा होतो जोपर्यंत टोकन मर्यादा पोहोचते*

**टप्प्याटप्प्याने विचारसूत्र** - दृश्यमान लॉजिक आवश्यक असलेल्या समस्यांसाठी. मॉडेल प्रत्येक टप्प्यासाठी स्पष्ट विचार दाखवते. गणिती प्रश्न, तर्कशास्त्रीय कोडे, किंवा विचार प्रक्रियेसाठी वापरा.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/mr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*समस्यांना स्पष्ट तार्किक टप्प्यांमध्ये विभागणे*

**बांधिल केलेले आउटपुट** - विशिष्ट फॉरमॅट आवश्यक असलेल्या प्रतिसादांसाठी. मॉडेल पारदर्शकपणे फॉरमॅट आणि लांबीच्या नियमांचे पालन करते. सारांश किंवा अचूक आउटपुट संरचनेसाठी वापरा.

```java
String prompt = """
    <constraints>
    - Exactly 100 words
    - Bullet point format
    - Technical terms only
    </constraints>
    
    Summarize the key concepts of machine learning.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/mr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*विशिष्ट फॉरमॅट, लांबी आणि रचना आवश्यकतांचा काटेकोरपणे पालन*

## मौजूदा Azure संसाधने वापरणे

**तैनाती तपासणी:**

root डिरेक्टरीत `.env` फाइल आहे याची खात्री करा, ज्यात Azure प्रमाणपत्रे आहेत (Module 01 मध्ये तयार केलेली):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दिसायला पाहिजे
```

**अॅप्लिकेशन सुरू करा:**

> **टिप:** जर तुम्ही Module 01 मधील `./start-all.sh` वापरून सर्व अॅप्लिकेशन आधीच सुरू केले असतील, तर हा module पोर्ट 8083 वर चालू आहे. पुढील सुरू करण्याच्या कमांड्स टाळा आणि थेट http://localhost:8083 वर जा.

**पर्याय 1: स्प्रिंग बूट डॅशबोर्ड वापरणे (VS Code वापरकर्त्यांसाठी शिफारस):**

डेव्ह कंटेनरमध्ये स्प्रिंग बूट डॅशबोर्ड विस्तार उपलब्ध आहे, जो सर्व स्प्रिंग बूट अॅप्लिकेशन्सचे दृश्य इंटरफेस व्यवस्थापित करतो. तुम्ही VS Code च्या डाव्या साइडच्या Activity Bar मध्ये (स्प्रिंग बूट आयकॉन शोधा) याला पाहू शकता.

स्प्रिंग बूट डॅशबोर्डमधून तुम्ही:
- कार्यक्षेत्रातील सर्व स्प्रिंग बूट अॅप्लिकेशन्स पाहू शकता
- एकाच क्लिकने अॅप्लिकेशन्स सुरू/थांबवू शकता
- अॅप्लिकेशनचे लॉग रिअल-टाइममध्ये पाहू शकता
- अॅप्लिकेशनची स्थिती निरीक्षण करू शकता

"prompt-engineering" जवळील प्ले बटणावर क्लिक करा हा module सुरू करण्यासाठी, किंवा एकाच वेळी सर्व module चालू करा.

<img src="../../../translated_images/mr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**पर्याय 2: शेल स्क्रिप्ट्स वापरणे**

सर्व वेब अॅप्लिकेशन्स (module 01-04) सुरू करा:

**Bash:**
```bash
cd ..  # मुळ निर्देशिकेतून
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # मूळ निर्देशिका पासून
.\start-all.ps1
```

किंवा फक्त हा module सुरू करा:

**Bash:**
```bash
cd 02-prompt-engineering
./start.sh
```

**PowerShell:**
```powershell
cd 02-prompt-engineering
.\start.ps1
```

दोन्ही स्क्रिप्ट्स स्वयंचलितपणे root `.env` मधील पर्यावरण चल लोड करतात आणि JAR फाईल्स अस्तित्वात नसेल तर तयार करतात.

> **टिप:** जर तुम्हाला सर्व module स्वतःहून बिल्ड करायचे असतील आणि नंतर सुरू करायचे असेल:
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

ब्राउझरमध्ये http://localhost:8083 उघडा.

**थांबवण्यासाठी:**

**Bash:**
```bash
./stop.sh  # हा फक्त मॉड्यूल आहे
# किंवा
cd .. && ./stop-all.sh  # सर्व मॉड्यूल्स
```

**PowerShell:**
```powershell
.\stop.ps1  # केवळ हा मॉड्यूल
# किंवा
cd ..; .\stop-all.ps1  # सर्व मॉड्यूल्स
```

## अॅप्लिकेशन स्क्रीनशॉट्स

<img src="../../../translated_images/mr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*मुख्य डॅशबोर्ड ज्यामध्ये सर्व 8 प्रॉम्प्ट इंजिनिअरिंग पॅटर्न्स त्यांच्या वैशिष्ट्यांसह आणि वापर केसेससह दिसत आहेत*

## पॅटर्न्सचा अभ्यास

वेब इंटरफेस तुम्हाला वेगवेगळ्या प्रॉम्प्टिंग रणनीतींसह प्रयोग करण्याची मुभा देतो. प्रत्येक पॅटर्न वेगवेगळी समस्या सोडवतो – त्यांना वापरून बघा आणि कोणती पद्धत कधी उत्तम काम करते ते पहा.

### कमी विरक्तता विरुध्द जास्त विरक्तता

"Kमी विरक्तता" वापरून "200 चा 15% काय आहे?" या साध्या प्रश्नाचा विचार करा. तुम्हाला त्वरित आणि सरळ उत्तर मिळेल. आता "जास्त विरक्तता" वापरून "उच्च-ट्रॅफिक API साठी कॅशिंग धोरण डिझाइन करा" असा जटिल प्रश्न विचारा. बघा कसं मॉडेल हळूहळू विचार करत, सविस्तर विश्लेषण देत आहे. मॉडेल तेच आहे, प्रश्न रचना तेच आहे – पण प्रॉम्प्ट सांगत आहे किती विचार करायचा आहे.
<img src="../../../translated_images/mr/low-eagerness-demo.898894591fb23aa0.webp" alt="कमी उत्सुकता डेमो" width="800"/>

*किमान विचारांसह जलद गणना*

<img src="../../../translated_images/mr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="उच्च उत्सुकता डेमो" width="800"/>

*संपूर्ण कॅशिंग धोरण (2.8MB)*

### कार्य संचालन (टूल प्रीअॅम्बल्स)

काही टप्प्यांतील कार्यप्रवाहांना अगोदर नियोजन आणि प्रगती कथनाचा फायदा होतो. मॉडेल काय करणार आहे हे स्पष्ट करते, प्रत्येक टप्प्याचे वर्णन करते, नंतर निकालाचे सारांश देते.

<img src="../../../translated_images/mr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="कार्य संचालन डेमो" width="800"/>

*टप्प्याटप्प्याने कथनासह REST एंडपॉइंट तयार करणे (3.9MB)*

### स्वप्रतिबिंब कोड

"एक ईमेल सत्यापन सेवा तयार करा" हा प्रयत्न करा. केवळ कोड तयार करून थांबण्याऐवजी, मॉडेल तयार करते, गुणवत्ता निकषांनुसार मूल्यांकन करते, त्रुटी ओळखते, आणि सुधारणा करते. आपण पाहाल की तो कोड उत्पादन मानकांपर्यंत कसा पुनरावृत्ती करतो.

<img src="../../../translated_images/mr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="स्वप्रतिबिंब कोड डेमो" width="800"/>

*पूर्ण ईमेल सत्यापन सेवा (5.2MB)*

### संरचित विश्लेषण

कोड समीक्षा सुसंगत मुल्यमापन फ्रेमवर्कची आवश्यकता असते. मॉडेल निश्चित श्रेण्यांमध्ये (योग्यता, सराव, कामगिरी, सुरक्षा) गंभीरतेच्या पातळ्यांसह कोडचे विश्लेषण करते.

<img src="../../../translated_images/mr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="संरचित विश्लेषण डेमो" width="800"/>

*फ्रेमवर्क-आधारित कोड समीक्षा*

### बहु-टर्न चॅट

"Spring Boot म्हणजे काय?" हा प्रश्न विचारा आणि लगेच "एक उदाहरण दाखवा" असे विचार करा. मॉडेल आपला पहिला प्रश्न लक्षात ठेवते आणि आपल्याला विशेषतः Spring Boot चे उदाहरण देते. जर स्मृती नसेल, तर दुसरा प्रश्न फारसा स्पष्ट नसता.

<img src="../../../translated_images/mr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="बहु-टर्न चॅट डेमो" width="800"/>

*प्रश्नांमध्ये संदर्भ जतन करणे*

### टप्प्याटप्प्याने विचार करणे

कोणतीही गणितची समस्या निवडा आणि Step-by-Step Reasoning आणि Low Eagerness या दोन्ही प्रकारांनी प्रयत्न करा. कमी उत्सुकता म्हणजे फक्त उत्तर देणे - जलद पण अस्पष्ट. टप्प्याटप्प्याने आपण प्रत्येक गणना आणि निर्णय पाहू शकता.

<img src="../../../translated_images/mr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="टप्प्याटप्प्याने विचार करणे डेमो" width="800"/>

*स्पष्ट टप्प्यांसह गणिताची समस्या*

### बंधित आउटपुट

जेव्हा आपल्याला ठराविक फॉरमॅट किंवा शब्दसंख्या आवश्यक असते, तेव्हा हे नमुना कडक पालन सुनिश्चित करते. अगदी 100 शब्दांच्या बुलेट पॉइंट स्वरूपात सारांश तयार करण्याचा प्रयत्न करा.

<img src="../../../translated_images/mr/constrained-output-demo.567cc45b75da1633.webp" alt="बंधनात्मक आउटपुट डेमो" width="800"/>

*मशीन लर्निंग सारांश फॉरमॅट नियंत्रणासह*

## आपण खरोखर काय शिकत आहात

**विचारशक्तीचे प्रयास सर्वकाही बदलतात**

GPT-5.2 आपल्याला आपल्या प्रॉम्प्ट्सद्वारे संगणकीय प्रयास नियंत्रित करण्याची परवानगी देते. कमी प्रयास म्हणजे जलद प्रतिक्रिया आणि किमान शोध. जास्त प्रयास म्हणजे मॉडेलला खोल विचार करण्यासाठी वेळ घेणे. आपण कार्याच्या गुंतागुंतीनुसार प्रयास जुळवायला शिकत आहात - सोप्या प्रश्नांवर वेळ वाया घालवू नका, पण गुंतागुंतीच्या निर्णयांमध्येही घाई करू नका.

**संरचना वर्तनाचे मार्गदर्शन करते**

प्रॉम्प्ट्समधील XML टॅग्स दिसतात का? ते फक्त सजावटीसाठी नाहीत. मुक्तपणे लिहिलेल्या टेक्स्टपेक्षा मॉडेल्स संरचित सूचना अधिक विश्वासार्हपणे पाळतात. बहु-टप्प्यांचे प्रक्रियांसाठी किंवा गुंतागुंतीच्या लॉजिकसाठी, संरचना मॉडेलला त्याच्या स्थितीचा आणि पुढे काय करायचं आहे हे ट्रॅक करण्यात मदत करते.

<img src="../../../translated_images/mr/prompt-structure.a77763d63f4e2f89.webp" alt="प्रॉम्प्ट संरचना" width="800"/>

*स्पष्ट विभागांसह आणि XML-शैलीच्या संघटनेसह उत्कृष्टप्रकारे रचलेला प्रॉम्प्ट*

**स्व-मूल्यांकनाद्वारे गुणवत्ता**

स्वप्रतिबिंब नमुने गुणवत्ता निकष स्पष्ट करून कार्य करतात. "मॉडेल योग्य प्रकारे करते" अशी आशा ठेवण्याऐवजी, आपण त्याला अचूक काय "योग्य" आहे ते सांगता: योग्य लॉजिक, त्रुटी हाताळणी, कामगिरी, सुरक्षा. मॉडेल आपला स्वतःचा आउटपुट मूल्यांकन करू शकते आणि सुधारणा करू शकते. यातून कोड तयार करणे लॉटरीमधून प्रक्रियेत रूपांतरित होते.

**संदर्भ मर्यादित आहे**

बहु-टर्न संवाद प्रत्येक विनंतीसह संदेश इतिहास समाविष्ट करून काम करतात. पण त्याला मर्यादा आहे - प्रत्येक मॉडेलचा जास्तीत जास्त टोकन लेखी असतो. संवाद वाढल्यावर, तुम्हाला संबंधित संदर्भ ठेवण्यासाठी धोरणे लागतात ज्याने तो मर्यादा भडकवू नये. हा मód्यूल आपल्याला स्मरणशक्ती कशी काम करते हे दाखवतो; नंतर आपण कधी सारांश करायचा, कधी विसरायचा, आणि कधी प्राप्त करायचा हे शिकाल.

## पुढील पायऱ्या

**पुढील मॉड्यूल:** [03-rag - RAG (रिट्रीव्हल-ऑगमेंटेड जनरेशन)](../03-rag/README.md)

---

**नॅव्हिगेशन:** [← मागील: मॉड्युल 01 - परिचय](../01-introduction/README.md) | [मुख्य पृष्ठावर परत जा](../README.md) | [पुढील: मॉड्युल 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
हा दस्तऐवज AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) चा वापर करून अनुवादित करण्यात आलेला आहे. आम्ही अचूकतेसाठी प्रयत्नशील असलो तरी, कृपया लक्षात ठेवा की स्वयंचलित अनुवादांमध्ये चुका किंवा अचूकतेच्या त्रुटी असू शकतात. मूळ दस्तऐवज त्याच्या मूळ भाषेत अधिकृत स्रोत म्हणून मानला पाहिजे. महत्त्वपूर्ण माहितीसाठी व्यावसायिक मानवी अनुवाद करण्याचा सल्ला दिला जातो. या अनुवादाच्या वापरामुळे उद्भवलेल्या कोणत्याही गैरसमजुती किंवा चुकीच्या अर्थान्वयाबद्दल आम्ही जबाबदार आहोत असे नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->