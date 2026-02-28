# Module 02: GPT-5.2 सह प्रॉम्प्ट इंजिनिअरिंग

## अनुक्रमणिका

- [व्हिडिओ वॉकथ्रू](../../../02-prompt-engineering)
- [तुम्ही काय शिकाल](../../../02-prompt-engineering)
- [आवश्यकता](../../../02-prompt-engineering)
- [प्रॉम्प्ट इंजिनिअरिंग समजून घेणे](../../../02-prompt-engineering)
- [प्रॉम्प्ट इंजिनिअरिंग मुलतत्त्वे](../../../02-prompt-engineering)
  - [झिरो-शॉट प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [फ्यू-शॉट प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [चेन ऑफ थॉट](../../../02-prompt-engineering)
  - [भूमिका-आधारित प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [प्रॉम्प्ट टेम्पलेट्स](../../../02-prompt-engineering)
- [उन्नत पॅटर्न्स](../../../02-prompt-engineering)
- [विद्यमान Azure संसाधने वापरणे](../../../02-prompt-engineering)
- [अॅप्लिकेशन स्क्रीनशॉट्स](../../../02-prompt-engineering)
- [पॅटर्न्स एक्सप्लोर करणे](../../../02-prompt-engineering)
  - [कमी विरूद्ध जास्त उत्सुकता](../../../02-prompt-engineering)
  - [टास्क एक्सिक्युशन (टूल प्रीअँबल्स)](../../../02-prompt-engineering)
  - [स्व-प्रतिबिंबित कोड](../../../02-prompt-engineering)
  - [संरचित विश्लेषण](../../../02-prompt-engineering)
  - [मल्टी-टर्न चॅट](../../../02-prompt-engineering)
  - [स्टेप-बाय-स्टेप विचारसरणी](../../../02-prompt-engineering)
  - [मर्यादित आउटपुट](../../../02-prompt-engineering)
- [तुम्ही खरंच काय शिकत आहात](../../../02-prompt-engineering)
- [पुढील टप्पे](../../../02-prompt-engineering)

## व्हिडिओ वॉकथ्रू

हा थेट सत्र पहा ज्यात दाखवले आहे की या मॉड्यूलसह कसे सुरुवात करायची:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## तुम्ही काय शिकाल

<img src="../../../translated_images/mr/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

मागील मॉड्यूलमध्ये, तुम्ही पाहिले की मेमरी कशी संभाषणात्मक AI सक्षम करते आणि GitHub मॉडेल्सचा वापर करून मूलभूत संवाद साधले गेले. आता आपण कसे प्रश्न विचारायचे यावर लक्ष केंद्रित करू — म्हणजे प्रॉम्प्ट्स — Azure OpenAI च्या GPT-5.2 वापरून. तुम्ही तुमचे प्रॉम्प्ट कसे तयार करता हे प्रतिसादांच्या गुणवत्तेवर प्रचंड प्रभाव टाकते. आपण मूलभूत प्रॉम्प्टिंग तंत्रे पुनरावलोकन करून सुरुवात करतो, त्यानंतर GPT-5.2 च्या क्षमतांचा पूर्ण फायदा घेणाऱ्या आठ उन्नत पॅटर्नमध्ये जातो.

आम्ही GPT-5.2 वापरू कारण ते तर्कशक्ती नियंत्रण (reasoning control) आणते — तुम्ही मॉडेलला सांगू शकता की उत्तर देण्यापूर्वी किती विचार करायचा आहे. हे विविध प्रॉम्प्टिंग धोरणे अधिक स्पष्ट करते आणि तुम्हाला समजायला मदत करते की कोणता दृष्टिकोन कधी वापरायचा. शिवाय, GitHub मॉडेल्सच्या तुलनेत Azure कडे GPT-5.2 साठी कमी रेट लिमिट्स आहेत ज्याचा आम्हाला फायदा होईल.

## आवश्यकता

- Module 01 पूर्ण केलेले असणे (Azure OpenAI संसाधने डिप्लॉय केलेली)
- रूचिक डिरेक्टरीमध्ये `.env` फाइल असणे ज्यात Azure क्रेडेन्शियल्स आहेत (Module 01 मध्ये `azd up` वापरून तयार केलेले)

> **टीप:** जर तुम्ही Module 01 पूर्ण केलेले नसेल तर आधी तिथल्या डिप्लॉयमेंट सूचना फॉलो करा.

## प्रॉम्प्ट इंजिनिअरिंग समजून घेणे

<img src="../../../translated_images/mr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

प्रॉम्प्ट इंजिनिअरिंग म्हणजे अशा इनपुट मजकूराचे डिझाइन करणे जे सातत्याने तुम्हाला हवे ते निकाल देते. हे फक्त प्रश्न विचारण्याबद्दल नाही - हे अशा विनंत्यांची रचना करण्याबाबत आहे ज्यामुळे मॉडेल नक्की समजून घेते की तुम्हाला काय हवय आणि ते कसे देयचे.

याला समजू शकतील अशा सहकाऱ्याला सूचना देण्यासारखे समजा. "बग दुरुस्त करा" असा एखादा संदेश अस्पष्ट आहे. "UserService.java च्या ओळ 45 मध्ये null pointer exception दुरुस्त करा null check समाविष्ट करून" असा संदेश विशिष्ट आहे. भाषा मॉडेल्स सुद्धा तितक्याच प्रकारे काम करतात — विशिष्टता आणि रचना महत्त्वाची आहे.

<img src="../../../translated_images/mr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j ही इन्फ्रास्ट्रक्चर पुरवते — मॉडेल कनेक्शन्स, मेमरी, आणि मेसेज प्रकार — तर प्रॉम्प्ट पॅटर्न्स फक्त व्यवस्थित रचलेले मजकूर आहेत जे त्या इन्फ्रास्ट्रक्चरमधून पाठवले जातात. प्रमुख आधारस्तंभ म्हणजे `SystemMessage` (जो AI चे वर्तन आणि भूमिका सेट करतो) आणि `UserMessage` (जो तुमची खरे विनंती वाहून आणतो).

## प्रॉम्प्ट इंजिनिअरिंग मुलतत्त्वे

<img src="../../../translated_images/mr/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

या मॉड्यूलमधील उन्नत पॅटर्न्समध्ये बेरंगून जाण्यापूर्वी, चला प्रॉम्प्टिंगच्या पाच मूलभूत तंत्रांची पुनरावृत्ती करूया. ही त्या बेसिक पण महत्त्वाच्या तांत्रिकांची पायाभूत रचना आहे जी प्रत्येक प्रॉम्प्ट इंजिनिअरला माहित असावी. तुम्ही आधीच [Quick Start module](../00-quick-start/README.md#2-prompt-patterns) वापरत असाल तर तुम्हाला हे तंत्रळे प्रत्यक्षात पाहायला मिळाले असतील — येथे त्यांचा सैद्धांतिक फ्रेमवर्क आहे.

### झिरो-शॉट प्रॉम्प्टिंग

सर्वात सोपी पद्धत: मॉडेलला कोणतेही उदाहरण न देता थेट सूचना देणे. मॉडेल पूर्णपणे त्याच्या प्रशिक्षणावर अवलंबून राहते जेणेकरून ते कार्य समजून घेते आणि पार पाडते. जेथे अपेक्षित वर्तन स्पष्ट असते अशा थेट विनंत्यांसाठी हे चांगले काम करते.

<img src="../../../translated_images/mr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*कोणतेही उदाहरण न देता थेट सूचना — मॉडेल फक्त सूचनेवरून कार्याचा अर्थ लावते*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// प्रतिसाद: "सकारात्मक"
```

**कधी वापरायचे:** सोप्या वर्गीकरणांसाठी, थेट प्रश्नांसाठी, भाषांतरांसाठी किंवा कोणत्याही अशा कार्यासाठी ज्यासाठी मॉडेलला अतिरिक्त मार्गदर्शनाची गरज नाही.

### फ्यू-शॉट प्रॉम्प्टिंग

तुम्हाला ज्याप्रमाणे मॉडेलने काम करायला हवे ते दाखवणारी उदाहरणे द्या. मॉडेल तुमच्या उदाहरणांमधून अपेक्षित इनपुट-आउटपुट फॉरमॅट शिकते आणि नवीन इनपुटवर लागू करते. हे अशा कार्यांसाठी सातत्य सुधारण्यासाठी खूप उपयुक्त आहे जिथे हवा असलेला फॉरमॅट किंवा वर्तन स्पष्ट नसते.

<img src="../../../translated_images/mr/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*उदाहरणांद्वारे शिकणे — मॉडेल पॅटर्न ओळखते आणि नवीन इनपुटवर लागू करते*

```java
String prompt = """
    Classify the sentiment as positive, negative, or neutral.
    
    Examples:
    Text: "This product exceeded my expectations!" → Positive
    Text: "It's okay, nothing special." → Neutral
    Text: "Waste of money, very disappointed." → Negative
    
    Now classify this:
    Text: "Best purchase I've made all year!"
    """;
String response = model.chat(prompt);
```

**कधी वापरायचे:** सानुकूल वर्गीकरणांसाठी, सातत्यपूर्ण फॉरमॅटिंगसाठी, डोमेन-विशिष्ट कामांसाठी, किंवा जेथे झिरो-शॉट निकाल विसंगत असतात.

### चेन ऑफ थॉट

मॉडेलला त्याचा विचार क्रमशः सादर करण्यास सांगा. उत्तरावर सरळ उडी मारण्याऐवजी मॉडेल समस्या तुकड्यांत विभागते आणि प्रत्येक भाग स्पष्टपणे हाताळते. हे गणित, लॉजिक, आणि मल्टी-स्टेप विचारसरणीसाठी अचूकता सुधारते.

<img src="../../../translated_images/mr/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*स्टेप-बाय-स्टेप विचार — गुंतागुंतीच्या समस्यांना स्पष्ट लॉजिकल स्टेप्समध्ये विभागणे*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// मॉडेल दर्शवितो: 15 - 8 = 7, नंतर 7 + 12 = 19 सफरचंदे
```

**कधी वापरायचे:** गणिताच्या समस्यांसाठी, तर्कशास्त्रीय कोडींसाठी, डिबगिंगसाठी किंवा जिथे विचार प्रक्रिया दाखवल्याने अचूकता आणि विश्वास वाढतो.

### भूमिका-आधारित प्रॉम्प्टिंग

तुमचा प्रश्न विचारण्यापूर्वी AI साठी व्यक्तीमत्व किंवा भूमिका सेट करा. त्यामुळे प्रतिसादाचा टोन, खोली, आणि फोकस ठरतो. "सॉफ्टवेअर आर्किटेक्ट" आणि "जुनियर डेव्हलपर" किंवा "सुरक्षा परीक्षक" यांचा सल्ला भिन्न असतो.

<img src="../../../translated_images/mr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*संदर्भ आणि भूमिका सेट करणे — एकाच प्रश्नाला वेगवेगळ्या भूमिकांनुसार वेगळे उत्तर मिळते*

```java
String prompt = """
    You are an experienced software architect reviewing code.
    Provide a brief code review for this function:
    
    def calculate_total(items):
        total = 0
        for item in items:
            total = total + item['price']
        return total
    """;
String response = model.chat(prompt);
```

**कधी वापरायचे:** कोड पुनरावलोकने, ट्युटोरियल्स, डोमेन-विशिष्ट विश्लेषणांसाठी, किंवा जिथे एका विशिष्ट कौशल्य पातळी किंवा दृष्टिकोनानुसार उत्तर पाहिजे.

### प्रॉम्प्ट टेम्पलेट्स

वेरिएबल प्लेसहोल्डर्ससह पुनर्वापरायोग्य प्रॉम्प्ट्स तयार करा. प्रत्येक वेळी नवीन प्रॉम्प्ट लिहिण्याऐवजी एकदा टेम्पलेट तयार करा आणि वेगवेगळे मूल्ये भरा. LangChain4j चा `PromptTemplate` वर्ग `{{variable}}` सिंटॅक्ससह हे सहज बनवतो.

<img src="../../../translated_images/mr/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*वेरिएबल प्लेसहोल्डर्ससह पुनर्वापरायोग्य प्रॉम्प्ट्स — एक टेम्पलेट, अनेक उपयोग*

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

**कधी वापरायचे:** परत-परत प्रश्नांसाठी वेगवेगळ्या इनपुटसह, बॅच प्रोसेसिंगसाठी, पुन्हा वापराता येणाऱ्या AI वर्कफ्लोजसाठी, किंवा जिथे प्रॉम्प्ट रचना सारखी राहते पण डेटा बदलतो.

---

ही पाच मुलतत्त्वे तुम्हाला बहुतांश प्रॉम्प्टिंग कामांसाठी खात्रीशीर किट देते. या मॉड्यूलचा उरलेला भाग GPT-5.2 च्या तर्कशक्ती नियंत्रण, स्व-मूल्यांकन, आणि संरचित आउटपुट क्षमतांचा वापर करणाऱ्या **आठ उन्नत पॅटर्न्स** वर आधारित आहे.

## उन्नत पॅटर्न्स

मूलतत्त्वे आच्छादित केल्यानंतर, चला त्या आठ उन्नत पॅटर्नकडे वळू जे या मॉड्यूलला वेगळे बनवतात. सर्व समस्या एकसमान दृष्टीकोनाची गरज ठेवत नसतात. काही प्रश्नांना वेगवान उत्तर हवे असते, काहींना खोल विचार हवा असतो. काहींना दृश्यमान तर काहींना फक्त निकाल हवा असतो. खालील प्रत्येक पॅटर्न वेगळ्या परिस्थितीसाठी ऑप्टिमाइझ केलेले आहे — आणि GPT-5.2 च्या तर्क नियंत्रणामुळे फरक अधिक स्पष्ट होतो.

<img src="../../../translated_images/mr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*आठ प्रॉम्प्ट इंजिनिअरिंग पॅटर्न्सचा अवलोकन व त्यांचे उपयोग केसेस*

<img src="../../../translated_images/mr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 चा तर्क नियंत्रण तुम्हाला मॉडेलस किती विचार करायचा आहे हे ठरवण्याची मुभा देतो — जलद थेट उत्तरांपासून खोल शोधाशोधीसाठी*

**कमी उत्सुकता (जलद व केंद्रित)** - सोप्या प्रश्नांसाठी ज्यांना जलद, थेट उत्तर हवं आहे. मॉडेल कमीच विचार करते - जास्तीत जास्त 2 पावले. गणना, लुकअप्स, किंवा सोप्या प्रश्नांसाठी वापरा.

```java
String prompt = """
    <context_gathering>
    - Search depth: very low
    - Bias strongly towards providing a correct answer as quickly as possible
    - Usually, this means an absolute maximum of 2 reasoning steps
    - If you think you need more time, state what you know and what's uncertain
    </context_gathering>
    
    Problem: What is 15% of 200?
    
    Provide your answer:
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilot सह एक्सप्लोर करा:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) उघडा आणि विचार करा:
> - "कमी उत्सुकता आणि जास्त उत्सुकता प्रॉम्प्टिंग पॅटर्नमध्ये काय फरक आहे?"
> - "प्रॉम्प्ट्समधील XML टॅग्ज AI च्या प्रतिसादाच्या रचनेस कसे मदत करतात?"
> - "स्व-प्रतिबिंबित पॅटर्न्स कधी वापरावे आणि थेट सूचना कधी?"

**जास्त उत्सुकता (खूप खोल आणि सखोल)** - गुंतागुंतीच्या समस्यांसाठी जिथे तुम्हाला व्यापक विश्लेषण हवा आहे. मॉडेल सखोल शोध घेतो आणि तपशीलवार तर्क सादर करतो. प्रणाली डिझाईन, आर्किटेक्चर निर्णय, किंवा गुंतागुंतीच्या संशोधनासाठी वापरा.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**टास्क एक्सिक्युशन (स्टेप-बाय-स्टेप प्रगती)** - बहु-पावली प्रक्रियांसाठी. मॉडेल सुरुवातीला योजना देते, प्रत्येक पावलाचे वर्णन करत काम करते, आणि नंतर सारांश देते. स्थलांतर, अंमलबजावणी, किंवा कोणत्याही मल्टी-स्टेप प्रक्रियेसाठी वापरा.

```java
String prompt = """
    <task_execution>
    1. First, briefly restate the user's goal in a friendly way
    
    2. Create a step-by-step plan:
       - List all steps needed
       - Identify potential challenges
       - Outline success criteria
    
    3. Execute each step:
       - Narrate what you're doing
       - Show progress clearly
       - Handle any issues that arise
    
    4. Summarize:
       - What was completed
       - Any important notes
       - Next steps if applicable
    </task_execution>
    
    <tool_preambles>
    - Always begin by rephrasing the user's goal clearly
    - Outline your plan before executing
    - Narrate each step as you go
    - Finish with a distinct summary
    </tool_preambles>
    
    Task: Create a REST endpoint for user registration
    
    Begin execution:
    """;

String response = chatModel.chat(prompt);
```

चेन-ऑफ-थॉट प्रॉम्प्टिंग स्पष्टपणे मॉडेलला त्याचा तर्कशक्ती प्रक्रिया दाखवण्यास सांगते, ज्यामुळे गुंतागुंतीच्या कामांसाठी अचूकता वाढते. स्टेप-बाय-स्टेप ब्रेकडाउन माणूस आणि AI दोघांनाही लॉजिक समजण्यासाठी मदत करतो.

> **🤖 GitHub Copilot [Chat](https://github.com/features/copilot) सह प्रयत्न करा:** या पॅटर्नबद्दल विचार करा:
> - "दीर्घकालीन ऑपरेशन्ससाठी टास्क एक्सिक्युशन पॅटर्न कसा अनुकूलित करेन?"
> - "उत्पादन अनुप्रयोगांमध्ये टूल प्रीअँबल्स संरचित करण्याच्या सर्वोत्तम पद्धती काय आहेत?"
> - "UI मध्ये मध्यवर्ती प्रगती अद्यतने कशी पकडू आणि दाखवू शकतो?"

<img src="../../../translated_images/mr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*मल्टी-स्टेप टास्कसाठी योजना → अंमलबजावणी → सारांश वर्कफ्लो*

**स्व-प्रतिबिंबित कोड** - उत्पादन-गुणवत्तेचा कोड तयार करण्यासाठी. योग्य त्रुटी हाताळणीसह उत्पादन मानकांनुसार कोड तयार करतो. नवीन फिचर्स किंवा सेवा तयार करताना वापरा.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/mr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*पुनरावृत्ती सुधारणा चक्र - तयार करा, मूल्यांकन करा, समस्या ओळखा, सुधारणा करा, पुनरावृत्ती करा*

**संरचित विश्लेषण** - सातत्यपूर्ण मूल्यांकनासाठी. मॉडेल कोड पुनरावलोकन करते निर्धारित फ्रेमवर्क वापरून (योग्यता, प्रथापद्धती, कार्यक्षमता, सुरक्षा, देखभाल योग्यता). कोड पुनरावलोकने किंवा गुणवत्ता मूल्यांकनासाठी वापरा.

```java
String prompt = """
    <analysis_framework>
    You are an expert code reviewer. Analyze the code for:
    
    1. Correctness
       - Does it work as intended?
       - Are there logical errors?
    
    2. Best Practices
       - Follows language conventions?
       - Appropriate design patterns?
    
    3. Performance
       - Any inefficiencies?
       - Scalability concerns?
    
    4. Security
       - Potential vulnerabilities?
       - Input validation?
    
    5. Maintainability
       - Code clarity?
       - Documentation?
    
    <output_format>
    Provide your analysis in this structure:
    - Summary: One-sentence overall assessment
    - Strengths: 2-3 positive points
    - Issues: List any problems found with severity (High/Medium/Low)
    - Recommendations: Specific improvements
    </output_format>
    </analysis_framework>
    
    Code to analyze:
    ```
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    ```
    Provide your structured analysis:
    """;

String response = chatModel.chat(prompt);
```

> **🤖 GitHub Copilot [Chat](https://github.com/features/copilot) सह प्रयत्न करा:** संरचित विश्लेषणाबद्दल विचार करा:
> - "विविध प्रकारच्या कोड पुनरावलोकनांसाठी विश्लेषण फ्रेमवर्क कसे सानुकूल करू?"
> - "संरचित आउटपुट प्रोग्रामॅटिकली पार्स व वापरल्यास सर्वोत्तम मार्ग कोणता?"
> - "भिन्न पुनरावलोकन सत्रांमध्ये सातत्यपूर्ण गंभीरता पातळी कशी ठेवता येईल?"

<img src="../../../translated_images/mr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*गंभीरतेच्या स्तरांसह सातत्यपूर्ण कोड पुनरावलोकनासाठी फ्रेमवर्क*

**मल्टी-टर्न चॅट** - संदर्भ आवश्यक असलेल्या संभाषणांसाठी. मॉडेल पूर्वीचे संदेश आठवते आणि त्यावर आधारित उत्तर वाढवते. संवादात्मक मदत सत्रांसाठी किंवा गुंतागुंतीच्या प्रश्नोत्तरेसाठी वापरा.

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

*बहु टर्नमध्ये संभाषणाचा संदर्भ जमतो जोपर्यंत टोकन मर्यादा पूर्ण होते*

**स्टेप-बाय-स्टेप विचारसरणी** - दृश्यमान तर्कशक्तीसाठी. मॉडेल प्रत्येक टप्प्यासाठी स्पष्ट तर्क सादर करतो. गणित समस्यांसाठी, तर्कशास्त्रीय कोडींसाठी किंवा विचार प्रक्रिया समजून घ्यायची असल्यास वापरा.

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

*समस्या स्पष्ट लॉजिकल स्टेप्समध्ये विभागणे*

**मर्यादित आउटपुट** - विशिष्ट फॉरमॅटची गरज असलेल्या प्रतिसादांसाठी. मॉडेल फॉरमॅट व लांबी नियम काटेकोरपणे पाळतो. सारांशांसाठी किंवा नेमका आउटपुट स्ट्रक्चर हवा असल्यास वापरा.

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

*विशिष्ट फॉरमॅट, लांबी, आणि रचना अटींचे पालन*

## विद्यमान Azure संसाधने वापरणे

**डिप्लॉयमेंट तपासा:**

सुनिश्चित करा की `.env` फाइल मूळ डिरेक्टरीमध्ये आहे आणि त्यात Azure क्रेडेन्शियल्स आहेत (Module 01 मध्ये तयार केलेली):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दाखवायला हवे
```

**अॅप्लिकेशन सुरू करा:**

> **टीप:** जर तुम्ही आधीच Module 01 मधील `./start-all.sh` वापरून सर्व अनुप्रयोग सुरू केले असतील, तर हा मॉड्यूल पोर्ट 8083 वर चालू आहे. खालील स्टार्ट कमांड्स वगळून थेट http://localhost:8083 वर जा.
**पर्याय 1: स्प्रिंग बूट डॅशबोर्ड वापरणे (VS Code वापरकर्त्यांसाठी शिफारस)**

डेव्ह कंटेनरमध्ये स्प्रिंग बूट डॅशबोर्ड विस्तार समाविष्ट आहे, जो सर्व स्प्रिंग बूट अनुप्रयोग व्यवस्थापित करण्यासाठी एक दृष्य इंटरफेस प्रदान करतो. आपण ते VS Code च्या डाव्या बाजूच्या अ‍ॅक्टिव्हिटी बारमध्ये (स्प्रिंग बूट आयकॉन शोधा) शोधू शकता.

स्प्रिंग बूट डॅशबोर्डमधून तुम्ही:
- कार्यक्षेत्रातील उपलब्ध सर्व स्प्रिंग बूट अनुप्रयोग पाहू शकता
- एकाच क्लिकने अनुप्रयोग सुरू/थांबवू शकता
- अनुप्रयोग लॉग रिअल-टाइममध्ये पाहू शकता
- अनुप्रयोग स्थिती देखरेख करू शकता

फक्त "prompt-engineering" च्या शेजारील प्ले बटणावर क्लिक करा हा मॉड्यूल सुरू करण्यासाठी, किंवा सर्व मॉड्यूल एकत्र सुरू करा.

<img src="../../../translated_images/mr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**पर्याय 2: शेल स्क्रिप्ट वापरणे**

सर्व वेब अनुप्रयोग (मॉड्यूल 01-04) सुरू करा:

**Bash:**
```bash
cd ..  # मुख्य निर्देशिके मधून
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # रूट डिरेक्टरीपासून
.\start-all.ps1
```

किंवा फक्त हा मॉड्यूल सुरू करा:

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

दोन्ही स्क्रिप्ट्स स्वयंचलितपणे मूळ `.env` फाइलमधून पर्यावरण चल लोड करतात आणि जर JARs अस्तित्वात नसतील तर तयार करतील.

> **सूचना:** तुम्हाला सुरू करण्यापूर्वी सर्व मॉड्यूल मॅन्युअली बनवायचे असल्यास:
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

तुमच्या ब्राऊझरमध्ये http://localhost:8083 उघडा.

**थांबवण्यासाठी:**

**Bash:**
```bash
./stop.sh  # फक्त हा मॉड्यूल
# किंवा
cd .. && ./stop-all.sh  # सर्व मॉड्यูล्स
```

**PowerShell:**
```powershell
.\stop.ps1  # फक्त हा मॉड्यूल
# किंवा
cd ..; .\stop-all.ps1  # सर्व मॉड्यूल्स
```

## अनुप्रयोग स्क्रिनशॉट्स

<img src="../../../translated_images/mr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*मुख्य डॅशबोर्ड, ज्यात सर्व 8 प्रॉम्प्ट इंजिनीयरिंग पॅटर्न्स त्यांचे वैशिष्ट्ये आणि वापर प्रकरणांसह दाखवले आहेत*

## पॅटर्न्सचा शोध

वेब इंटरफेस तुम्हाला वेगवेगळ्या प्रॉम्प्टिंग धोरणांसोबत प्रयोग करण्याची परवानगी देतो. प्रत्येक पॅटर्न वेगळ्या समस्या सोडवतो - ते वापरून पाहा आणि कोणत्या परिस्थितीत कोणता दृष्टिकोन चांगला आहे ते पहा.

> **सूचना: स्ट्रीमिंग विरुद्ध नॉन-स्ट्रीमिंग** — प्रत्येक पॅटर्न पानावर दोन बटणे असतात: **🔴 स्ट्रीम रिस्पॉन्स (लाइव्ह)** आणि **नॉन-स्ट्रीमिंग** पर्याय. स्ट्रीमिंगमध्ये सर्व्हर-सेंड इव्हेंट्स (SSE) वापरून मॉडेलने तयार केलेले टोकन्स रिअल-टाइममध्ये दाखवले जातात, त्यामुळे प्रगती ताबडतोब दिसते. नॉन-स्ट्रीमिंग पर्याय संपूर्ण प्रतिसाद येईपर्यंत थांबतो. जे प्रॉम्प्ट डीप रीझनिंग करतात (उदा. हाई ईगरनेस, सेल्फ-रिफ्लेक्टिंग कोड), त्यासाठी नॉन-स्ट्रीमिंग कॉल खूप वेळ लागू शकतो — कधी कधी मिनिटे — आणि कोणतीही दृश्य अभिप्रत्यय नाही. **कॉम्प्लेक्स प्रॉम्प्टसह प्रयोग करताना स्ट्रीमिंग वापरा** जेणेकरून तुम्हाला मॉडेल काम करताना दिसेल आणि विनंती वेळोवेळी ठरल्यासारखे वाटू नये.
>
> **सूचना: ब्राऊझर आवश्यकता** — स्ट्रीमिंग फिचर Fetch Streams API (`response.body.getReader()`) वापरतो, ज्यासाठी पूर्ण ब्राऊझर (Chrome, Edge, Firefox, Safari) आवश्यक आहे. VS Code च्या अंतर्गत Simple Browser मध्ये हे काम करत नाही, कारण त्याचा वेबव्ह्यू ReadableStream API ला सपोर्ट करत नाही. Simple Browser वापरल्यास, नॉन-स्ट्रीमिंग बटणे सामान्यपणे काम करतील — फक्त स्ट्रीमिंग बटणांवर परिणाम होतो. अधिक चांगल्या अनुभवासाठी `http://localhost:8083` बाह्य ब्राऊझरमध्ये उघडा.

### कमी ईगरनेस विरुद्ध जास्त ईगरनेस

"200 चे 15% काय आहे?" असा सोपा प्रश्न कमी ईगरनेस वापरून विचारा. तुम्हाला त्वरित, थेट उत्तर मिळेल. आता "हाय-ट्रॅफिक API साठी कॅशिंग धोरण डिझाइन करा" असा जटिल प्रश्न जास्त ईगरनेस वापरून विचारा. **🔴 स्ट्रीम रिस्पॉन्स (लाइव्ह)** क्लिक करा आणि मॉडेलचे सविस्तर रीझनिंग टोकन-बाय-टोकन बघा. एकच मॉडेल, एकाच प्रश्न संरचना - पण प्रॉम्प्ट त्याला किती विचार करायचा आहे ते सांगतो.

### टास्क कार्यान्वयन (टूल प्रीअँबल्स)

मल्टि-स्टेप वर्कफ्लोला आगाऊ नियोजन आणि प्रगतीचे वर्णन उपयुक्त असते. मॉडेल काय करणार आहे हे सांगते, प्रत्येक टप्पा सांगते, नंतर निकालांचे सारांश देते.

### सेल्फ-रिफ्लेक्टिंग कोड

"ईमेल व्हॅलिडेशन सेवा तयार करा" असा प्रयत्न करा. फक्त कोड तयार करून थांबण्याऐवजी, मॉडेल कोड तयार करते, गुणवत्ता निकषांवर मूल्यांकन करते, कमकुवत भाग ओळखते आणि सुधारणा करते. तुम्हाला ते कोड उत्पादन मानकांपर्यंत पोहोचेल तोपर्यंत ते पुनरावृत्ती करताना दिसेल.

### संरचित विश्लेषण

कोड पुनरावलोकनासाठी सुसंगत मूल्यांकन फ्रेमवर्क आवश्यक असतो. मॉडेल कोड निश्चित श्रेण्या (योग्यता, सराव, कार्यक्षमता, सुरक्षा) वापरून त्याचा विश्लेषण करते आणि गंभीरता स्तरांसह.

### मल्टि-टर्न चॅट

"स्प्रिंग बूट म्हणजे काय?" असा प्रश्न विचारा आणि लगेच "मला एक उदाहरण दाखवा" असा पुढील प्रश्न विचारा. मॉडेल तुमचा पहिला प्रश्न लक्षात ठेवते आणि तुम्हाला स्प्रिंग बूटचे उदाहरण देते. स्मृती नसल्यास, दुसरा प्रश्न खूप अस्पष्ट असेल.

### पायरी-पायरीने रीझनिंग

एक गणितीय प्रश्न निवडा आणि दोन्ही Step-by-Step Reasoning आणि कमी ईगरनेस वापरून प्रयत्न करा. कमी ईगरनेस फक्त उत्तर देते - जलद पण अस्पष्ट. पायरी-पायरीने प्रत्येक गणना आणि निर्णय दाखवते.

### मर्यादित आउटपुट

जेव्हा तुम्हाला विशिष्ट स्वरूप किंवा शब्द संख्या आवश्यक असते, तेव्हा हा पॅटर्न कडक पालन करतो. अचूक 100 शब्दांचे बुलेट पॉइंट स्वरूपात सारांश तयार करण्याचा प्रयत्न करा.

## तुम्ही प्रत्यक्षात काय शिकत आहात

**रीझनिंग प्रयत्न सर्वकाही बदलतो**

GPT-5.2 तुम्हाला तुमच्या प्रॉम्प्टद्वारे संगणकीय प्रयत्न नियंत्रित करण्याची परवानगी देते. कमी प्रयत्न म्हणजे जलद प्रतिसाद आणि कमी अन्वेषण. जास्त प्रयत्न म्हणजे मॉडेल खोल विचार करण्यासाठी वेळ घेतो. तुम्ही प्रयत्नांची पातळी कामाच्या गुंतागुंतीशी जुळवायला शिकत आहात — सोप्या प्रश्नांवर वेळ वाया घालवू नका, पण क्लिष्ट निर्णयांवरही घाई करू नका.

**रचना वर्तन मार्गदर्शित करते**

प्रॉम्प्टमधील XML टॅग्स तुम्हाला दिसलेत का? ते फक्त सजावट नाहीत. मॉडेल्स संरचित सूचना मोकळ्या मजकुरापेक्षा अधिक विश्वासार्हपणे पाळतात. मल्टि-स्टेप प्रक्रियेची किंवा क्लिष्ट लॉजिकची गरज असल्यास, रचना मॉडेलला कुठे आहे आणि पुढे काय करायचे आहे हे ट्रॅक करण्यात मदत करते.

<img src="../../../translated_images/mr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*स्पष्ट विभागांसह आणि XML-शैलीतील संघटनेसह चांगल्या रचनेचा प्रॉम्प्टचे रचना*

**स्व-मूल्यमापनाद्वारे गुणवत्ता**

सेल्फ-रिफ्लेक्टिंग पॅटर्न्स गुणवत्ता निकष स्पष्ट करून काम करतात. मॉडेल "योग्य" कसे करावे अशी आशा करण्याऐवजी, आपण त्याला "योग्य" म्हणजे काय ते नेमके सांगता: बरोबर लॉजिक, त्रुटी हाताळणी, कार्यक्षमता, सुरक्षा. नंतर मॉडेल स्वतःच्या आउटपुटचे मूल्यमापन करू शकते आणि सुधारणा करू शकते. हे कोड तयार करणे लॉटरीऐवजी प्रक्रिया बनवते.

**संदर्भ मर्यादित आहे**

मल्टि-टर्न संभाषणे प्रत्येक विनंतीसह संदेश इतिहास समाविष्ट करून काम करतात. पण त्याला मर्यादा आहे - प्रत्येक मॉडेलची कमाल टोकन संख्या असते. संभाषणे वाढल्यावर, तुम्हाला संबंधित संदर्भ ठेवण्यासाठी धोरणे वापरावी लागतात आणि त्याच्या मर्यादेपलीकडे जाऊ नको. हा मॉड्यूल तुम्हाला स्मृती कशी काम करते ते दाखवतो; नंतर तुम्हाला कधी सारांश करायचा, कधी विसरायचं, आणि कधी पुनःप्राप्त करायचं ते शिकवेल.

## पुढील पावले

**पुढील मॉड्यूल:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**नेव्हिगेशन:** [← मागील: मॉड्यूल 01 - परिचय](../01-introduction/README.md) | [मुख्यपृष्ठावर परत](../README.md) | [पुढे: मॉड्यूल 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) चा वापर करून भाषांतरित केला आहे. आम्ही अचूकतेसाठी प्रयत्न करतो, तरी कृपया लक्षात ठेवा की स्वयंचलित भाषांतरांमध्ये त्रुटी किंवा चुकीची समज असू शकतात. मूळ दस्तऐवज त्याच्या स्थानिक भाषेत अधिकृत स्रोत मानला जावा. महत्त्वपूर्ण माहिती साठी व्यावसायिक मानवी भाषांतराची शिफारस केली जाते. या भाषांतराच्या वापरामुळे उद्भवणाऱ्या कोणत्याही गैरसमजुती किंवा चुकीच्या अर्थ निष्कर्षांसाठी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->