# Module 02: GPT-5.2 सह प्रॉम्प्ट इंजिनीअरिंग

## अनुक्रमणिका

- [तुम्ही काय शिकाल](../../../02-prompt-engineering)
- [पूर्वअटी](../../../02-prompt-engineering)
- [प्रॉम्प्ट इंजिनीअरिंग समजून घेणे](../../../02-prompt-engineering)
- [प्रॉम्प्ट इंजिनीअरिंग मूलतत्त्वे](../../../02-prompt-engineering)
  - [झिरो-शॉट प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [फ्यू-शॉट प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [चेन ऑफ थॉट](../../../02-prompt-engineering)
  - [भूमिका-आधारित प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [प्रॉम्प्ट टेम्पलेट्स](../../../02-prompt-engineering)
- [प्रगत नमुने](../../../02-prompt-engineering)
- [अस्तित्वातील Azure संसाधने वापरणे](../../../02-prompt-engineering)
- [अॅप्लिकेशन स्क्रीनशॉट्स](../../../02-prompt-engineering)
- [नमुने तपासणे](../../../02-prompt-engineering)
  - [कमी विरूद्ध जास्त उत्सुकता](../../../02-prompt-engineering)
  - [टास्क अंमलबजावणी (टूल प्रीअॅम्बल्स)](../../../02-prompt-engineering)
  - [स्वतःचे प्रतिबिंबित करणारा कोड](../../../02-prompt-engineering)
  - [रचनेत विश्लेषण](../../../02-prompt-engineering)
  - [मल्टी-टर्न चॅट](../../../02-prompt-engineering)
  - [पाय- पायाने विचार प्रक्रियेचा वापर](../../../02-prompt-engineering)
  - [बांधलेल्या आउटपुट](../../../02-prompt-engineering)
- [खऱ्या अर्थाने काय शिकत आहात](../../../02-prompt-engineering)
- [पुढील टप्पे](../../../02-prompt-engineering)

## तुम्ही काय शिकाल

<img src="../../../translated_images/mr/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

पूर्वीच्या मॉड्यूलमध्ये, तुम्ही पाहिले कसे मेमरी संवादात्मक AI सक्षम करते आणि GitHub मॉडेल्सचा वापर करून मूलभूत संवाद साधले. आता आपण लक्ष केंद्रित करू कसे प्रश्न विचारायचे — प्रॉम्प्ट्स स्वतः — Azure OpenAI च्या GPT-5.2 वापरून. तुम्ही तुमचे प्रॉम्प्ट कसे रचता याचा तुमच्या प्रतिसादांच्या गुणवत्तेवर प्रचंड परिणाम होतो. आम्ही मूलभूत प्रॉम्प्टिंग तंत्रांचा आढावा घेऊन सुरुवात करतो, मग सहा प्रगत नमुन्यांकडे जातो जे GPT-5.2 च्या पूर्ण क्षमतेचा उपयोग करतात.

आम्ही GPT-5.2 वापरणार कारण ते विचार प्रक्रियेवर नियंत्रण देते - तुम्ही मॉडेलला सांगू शकता की उत्तर देण्यापूर्वी किती विचार करायचा आहे. हे वेगवेगळ्या प्रॉम्प्टिंग रणनीती अधिक स्पष्ट बनवते आणि तुम्हाला समजायला मदत करते की कोणती पद्धत केव्हा वापरायची. तसेच Azure कडून GPT-5.2 साठी GitHub मॉडेल्सच्या तुलनेत कमी दर मर्यादा मिळतात.

## पूर्वअटी

- मॉड्यूल 01 पूर्ण केलेला (Azure OpenAI संसाधने तैनात केली आहेत)
- मूळ निर्देशिकेत `.env` फाईल ज्यात Azure क्रेडेन्शियल्स असतील (मॉड्यूल 01 मधील `azd up` ने तयार केलीलेली)

> **सूचना:** जर तुम्ही मॉड्यूल 01 पूर्ण केला नसेल, तर कृपया प्रथम तिथल्या तैनाती सूचना पाळा.

## प्रॉम्प्ट इंजिनीअरिंग समजून घेणे

<img src="../../../translated_images/mr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="Prompt Engineering म्हणजे काय?" width="800"/>

प्रॉम्प्ट इंजिनीअरिंग म्हणजे अशी इनपुट मजकूर डिझाइन करणे जे सातत्याने तुम्हाला हवे असलेले परिणाम देते. फक्त प्रश्न विचारण्याचे नाही - ही विनंत्या अशा प्रकारे संरचित करणे होय ज्यामुळे मॉडेल नक्की काय हवे आहे आणि कसे वितरीत करायचे ते समजते.

याला सहकाऱ्याला देण्यात आलेल्या सूचना समजा. "बग दुरुस्त करा" अस्पष्ट आहे. "UserService.java च्या 45 व्या ओळीत नल्ल पॉइंटर एक्सेप्शन दुरुस्त करा, नल्ल तपासणी घालून" ही विशिष्ट सूचना आहे. भाषिक मॉडेल्स ही सारखीच कार्ये करतात - विशिष्टता आणि रचना महत्वाची असते.

<img src="../../../translated_images/mr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j कसे बसते" width="800"/>

LangChain4j हा आधारभूत नेटवर्क पुरवतो — मॉडेल कनेक्शन, मेमरी, आणि संदेश प्रकार — तर प्रॉम्प्ट पॅटर्न्स ही केवळ काळजीपूर्वक रचलेली मजकूर आहेत जी त्या आधारभूत नेटवर्कमध्ये पाठवली जातात. मुख्य घटक म्हणजे `SystemMessage` (जी AI ची वर्तणूक आणि भूमिका सेट करते) आणि `UserMessage` (जी तुमची खरी विनंती घेऊन येते).

## प्रॉम्प्ट इंजिनीअरिंग मूलतत्त्वे

<img src="../../../translated_images/mr/five-patterns-overview.160f35045ffd2a94.webp" alt="पाच प्रॉम्प्ट इंजिनीअरिंग पॅटर्न्सचे सारांश" width="800"/>

या मॉड्यूलमधील प्रगत नमुन्यांमध्ये प्रवेश करण्यापूर्वी, चला पाच मूलभूत प्रॉम्प्टिंग तंत्रांचा आढावा घेऊया. ही प्रत्येक प्रॉम्प्ट इंजिनियरने ओळखली पाहिजेत अशी मूलभूत रचना आहे. जर तुम्ही आधीच [क्विक स्टार्ट मॉड्यूल](../00-quick-start/README.md#2-prompt-patterns) अनुभवले असेल, तर तुम्हाला ही संकल्पनात्मक चौकट माहीत आहे.

### झिरो-शॉट प्रॉम्प्टिंग

सर्वात सोपी पद्धत: मॉडेलला थेट सूचना द्या, कोणतेही उदाहरण न देता. मॉडेल संपूर्णपणे आपल्या प्रशिक्षणावर अवलंबून असते कार्य समजून घेण्यासाठी आणि अंमलबजावणीसाठी. ही सोपी विनंत्यांसाठी चांगली कार्य करते जिथे अपेक्षित वर्तन स्पष्ट असते.

<img src="../../../translated_images/mr/zero-shot-prompting.7abc24228be84e6c.webp" alt="झिरो-शॉट प्रॉम्प्टिंग" width="800"/>

*उदाहरणांशिवाय थेट सूचना - मॉडेल सूचना फक्त वापरून कार्य समजून घेतो*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// प्रतिसाद: "सकारात्मक"
```

**कधी वापरावे:** सोप्या वर्गीकरणांसाठी, थेट प्रश्नांना, भाषांतरांसाठी, किंवा कोणत्याही कार्यासाठी जिथे मॉडेलला अधिक मार्गदर्शनशिवाय हाताळता येते.

### फ्यू-शॉट प्रॉम्प्टिंग

ज्या नमुन्याचे तुम्हाला अनुसरण करायचे आहे ते दाखवणारी उदाहरणे द्या. मॉडेल तुमच्या उदाहरणांतून अपेक्षित इनपुट-आउटपुट स्वरूप शिकते आणि नवीन इनपुटवर लागू करते. हे निरंतरतेसाठी प्रचंड सुधारणा करते जिथे अपेक्षित स्वरूप किंवा वर्तन स्पष्ट नसते.

<img src="../../../translated_images/mr/few-shot-prompting.9d9eace1da88989a.webp" alt="फ्यू-शॉट प्रॉम्प्टिंग" width="800"/>

*उदाहरणांकडून शिकणे - मॉडेल नमुना ओळखून नवीन इनपुटवर लागू करते*

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

**कधी वापरावे:** सानुकूल वर्गीकरणांसाठी, सातत्यपूर्ण स्वरुपासाठी, डोमेन-विशिष्ट कार्यांसाठी, किंवा झिरो-शॉट निकाल असंतुष्ट असतील तेव्हा.

### चेन ऑफ थॉट

मॉडेलला त्याची परिप्रेक्ष्यक्रमाने विचार प्रक्रिया दाखवण्यास विचारा. उत्तराकडे थेट न जात, मॉडेल समस्या तुकड्यांमध्ये विभागून प्रत्येक भाग स्पष्टपणे सोडवते. हे गणित, लॉजिक, आणि बहु-चरण विचार प्रक्रियेत अचूकता सुधारते.

<img src="../../../translated_images/mr/chain-of-thought.5cff6630e2657e2a.webp" alt="चेन ऑफ थॉट प्रॉम्प्टिंग" width="800"/>

*पाय- पायाने विचार प्रक्रिया - जटिल समस्या स्पष्ट तर्कसंगत टप्प्यांमध्ये विभागणे*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// मॉडेल दाखवते: 15 - 8 = 7, मग 7 + 12 = 19 सफरचंदं
```

**कधी वापरावे:** गणिती समस्या, लॉजिक पझल्स, डीबगींग, किंवा कायदा दाखवणे अचूकता व विश्वास सुधारते असे कोणतेही कार्य.

### भूमिका-आधारित प्रॉम्प्टिंग

तुमचा प्रश्न विचारण्यापूर्वी AI साठी एक व्यक्तिमत्व किंवा भूमिका सेट करा. ही संदर्भ देऊन प्रतिसादाचा टोन, खोलाई, आणि लक्ष केंद्रित करते. "सॉफ्टवेअर आर्किटेक्ट" वेगळे सल्ले देते "ज्युनियर डेव्हलपर" किंवा "सिक्युरिटी ऑडिटर" पेक्षा.

<img src="../../../translated_images/mr/role-based-prompting.a806e1a73de6e3a4.webp" alt="भूमिका-आधारित प्रॉम्प्टिंग" width="800"/>

*संदर्भ आणि व्यक्तिमत्व सेट करणे - दिलेल्या भूमिकेनुसार त्याच प्रश्नाला वेगळा प्रतिसाद मिळतो*

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

**कधी वापरावे:** कोड पुनरावलोकने, ट्यूशन किंवा सल्ला देणे, डोमेन-विशिष्ट तज्ञतेनुसार उत्तर हवे असल्यास.

### प्रॉम्प्ट टेम्पलेट्स

पर्यायी प्लेसहोल्डर्ससह पुनर्वापर करता येणारे प्रॉम्प्ट तयार करा. दरवेळी नवीन प्रॉम्प्ट लिहिण्याऐवजी, एकदा टेम्पलेट तयार करा आणि वेगवेगळ्या मूल्यांनी भरा. LangChain4j चा `PromptTemplate` क्लास हे `{{variable}}` सिंटॅक्ससह सुलभ करते.

<img src="../../../translated_images/mr/prompt-templates.14bfc37d45f1a933.webp" alt="प्रॉम्प्ट टेम्पलेट्स" width="800"/>

*पर्यायी प्लेसहोल्डर्ससह पुनर्वापर करता येणारे प्रॉम्प्ट - एक टेम्पलेट, अनेक वापर*

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

**कधी वापरावे:** वेगवेगळ्या इनपुटसह पुनरावर्ती क्वेरीज, बॅच प्रोसेसिंग, पुनर्वापर करता येणाऱ्या AI वर्कफ्लोजसाठी किंवा जेथे प्रॉम्प्ट संरचना सारखी असते पण डेटा बदलतो.

---

हे पाच मूलतत्त्वे सर्वसाधारणपणे बहुतेक प्रॉम्प्टिंग कार्यांसाठी तुमच्याकडे एक ठोस साधनसामुग्री देतात. या मॉड्यूलचे उरलेले भाग GPT-5.2 च्या विचार नियंत्रण, स्व-मूल्यमापन, आणि रचनात्मक आउटपुट क्षमतांवर आधारित **आठ प्रगत नमुन्यांवर** आधारित आहेत.

## प्रगत नमुने

मूलतत्त्वे स्पष्ट केल्यावर, या मॉड्यूलला विशेष बनविणारे आठ प्रगत नमुने पाहूया. सर्व समस्या एकसारखी पद्धत वापरू नयेत. काही प्रश्न जलद उत्तरे हवे आहेत, तर काहींना सखोल विचार आवश्यक आहे. काहींना दिसणारी विचार प्रक्रिया हवी आहे, तर काहींना फक्त निकाल पाहिजे. प्रत्येक नमुना वेगळ्या परिस्थितीसाठी ऑप्टिमाइझ केलेले आहे — आणि GPT-5.2 च्या विचार नियंत्रणामुळे फरक अधिक स्पष्ट होतो.

<img src="../../../translated_images/mr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="आठ प्रॉम्प्टिंग पॅटर्न्स" width="800"/>

*आठ प्रॉम्प्ट इंजिनीअरिंग नमुन्यांचा आढावा व त्यांचे उपयोग*

<img src="../../../translated_images/mr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 सह विचार नियंत्रण" width="800"/>

*GPT-5.2 च्या विचार नियंत्रणामुळे तुम्ही मॉडेलला किती विचार करायचा आहे हे ठरवू शकता — जलद थेट उत्तरे ते सखोल शोध*

<img src="../../../translated_images/mr/reasoning-effort.db4a3ba5b8e392c1.webp" alt="विचार प्रयत्नांची तुलना" width="800"/>

*कमी उत्सुकता (जलद, थेट) विरुद्ध जास्त उत्सुकता (सखोल, शोधपूर्ण) विचार पद्धती*

**कमी उत्सुकता (त्वरित व लक्ष केंद्रीत)** - सोप्या प्रश्नांसाठी जी तुम्हाला जलद, थेट उत्तर हवे आहे. मॉडेल केवळ २ टप्प्यांपुरतेच विचार करते. याचा वापर गणना, शोध, किंवा सरळ प्रश्नांसाठी करा.

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

> 💡 **GitHub Copilot सह शोधा:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) उघडा आणि विचारा:
> - "कमी उत्सुकता आणि जास्त उत्सुकता प्रॉम्प्टिंग नमुन्यांमध्ये काय फरक आहे?"
> - "प्रॉम्प्टमधील XML टॅग्स AI च्या प्रतिसादाच्या रचनेत कसे मदत करतात?"
> - "मी स्व-प्रतिबिंबन नमुने आणि थेट सूचना कधी वापरावे?"

**जास्त उत्सुकता (सखोल व सविस्तर)** - जटिल समस्यांसाठी जी तुम्हाला सखोल विश्लेषण हवे आहे. मॉडेल सखोल शोध घेतो व सविस्तर विचार प्रक्रिया दाखवतो. याचा वापर सिस्टीम डिझाईन, आर्किटेक्चर निर्णय, किंवा जटिल संशोधनासाठी करा.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**टास्क अंमलबजावणी (पाय-निहाय प्रगती)** - बहु-चरण वर्कफ्लोजसाठी. मॉडेल आधी योजना देतो, नंतर प्रत्येक टप्पा करताना त्याचे वर्णन करतो आणि नंतर सारांश देतो. याचा वापर माइग्रेशन, अंमलबजावणी, किंवा कोणत्याही बहु-चरण प्रक्रियेसाठी करा.

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

चेन-ऑफ-थॉट प्रॉम्प्टिंग मॉडेलला त्याची विचार प्रक्रिया दाखवण्यासाठी स्पष्टपणे विचारतो, ज्यामुळे जटिल कार्यांवर अचूकता सुधारते. पाय-निहाय विश्लेषण माणसांना आणि AI दोघांनाही तार्किक समज वाढवते.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** या नमुन्याबद्दल विचारा:
> - "मी दीर्घकालीन ऑपरेशनसाठी टास्क अंमलबजावणी नमुना कसा अनुकूल करू शकतो?"
> - "उत्पादन अनुप्रयोगांमधील टूल प्रीअॅम्बल्सचे सर्वोत्तम रचनेचे मार्ग काय आहेत?"
> - "UI मध्ये मध्यवर्ती प्रगती अद्यतने कशी कॅप्चर व प्रदर्शित करू शकतो?"

<img src="../../../translated_images/mr/task-execution-pattern.9da3967750ab5c1e.webp" alt="टास्क अंमलबजावणी नमुना" width="800"/>

*प्लॅन → अमलबजावणी → सारांश बहु-चरण कार्यांसाठी वर्कफ्लो*

**स्वतःचे प्रतिबिंबित करणारा कोड** - उत्पादन दर्जाचा कोड तयार करण्यासाठी. मॉडेल उत्पादन मानके आणि योग्य त्रुटी हाताळणी वापरून कोड तयार करतो. नवीन फीचर्स किंवा सेवा तयार करताना याचा वापर करा.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/mr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="स्वतःचे प्रतिबिंबन चक्र" width="800"/>

*पुनरावृत्ती सुधारणा चक्र - तयार करा, मूल्यमापन करा, समस्या ओळखा, सुधारणा करा, पुन्हा करा*

**रचनेत विश्लेषण** - सातत्यपूर्ण मूल्यमापनासाठी. मॉडेल कोड पुनरावलोकन निश्चित चौकटीतून करतो (योग्यतेसाठी, पद्धतींसाठी, कार्यक्षमतेसाठी, सुरक्षा आणि देखभालीसाठी). याचा वापर कोड पुनरावलोकन किंवा गुणवत्ता तपासणीसाठी करा.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** रचनेत विश्लेषणाबद्दल विचारा:
> - "मी विविध प्रकारच्या कोड पुनरावलोकनांसाठी विश्लेषण फ्रेमवर्क कसे सानुकूल करू शकतो?"
> - "रचनेत आउटपुट प्रोग्रामॅटिकरीत्या पार्स व अंमलात कसे आणावे?"
> - "विविध पुनरावलोकन सत्रांमध्ये सातत्यपूर्ण गंभीरतेची पातळी कशी सुनिश्चित करावी?"

<img src="../../../translated_images/mr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="रचनेत विश्लेषण नमुना" width="800"/>

*गंभीरतेच्या पातळीसह सातत्यपूर्ण कोड पुनरावलोकनासाठी चौकट*

**मल्टी-टर्न चॅट** - संदर्भ आवश्यक असलेल्या संभाषणांसाठी. मॉडेल मागील संदेश लक्षात ठेवतो आणि त्यावर आधार करून संवाद वाढवतो. सुधारित मदत सत्र किंवा जटिल प्रश्न-उत्तरासाठी वापर करा.

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/mr/context-memory.dff30ad9fa78832a.webp" alt="संदर्भ मेमरी" width="800"/>

*संवाद संदर्भ कसे जमा होतो अनेक टर्न्समध्ये, टोकन मर्यादेपर्यंत*

**पाय- पायाने विचार प्रक्रिया** - दृश्यमान लॉजिक आवश्यक समस्यांसाठी. मॉडेल प्रत्येक टप्प्यासाठी स्पष्ट विचार प्रक्रिया दाखवतो. गणिती प्रश्न, लॉजिक पझल्स किंवा विचार प्रक्रिया समजून घेण्यासाठी वापरा.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/mr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="पाय- पायाने नमुना" width="800"/>

*समस्यांचे स्पष्ट तार्किक टप्प्यांमध्ये विघटन*

**बांधलेले आउटपुट** - विशिष्ट स्वरूप आवश्यक प्रतिसादांसाठी. मॉडेल कठोरपणे स्वरूप आणि लांबीच्या नियमांचे पालन करते. सारांशांकरिता किंवा अचूक आउटपुट रचनेसाठी वापरा.

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

<img src="../../../translated_images/mr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="बांधलेले आउटपुट नमुना" width="800"/>

*विशिष्ट स्वरूप, लांबी, आणि रचना नियमांची अंमलबजावणी*

## अस्तित्वातील Azure संसाधने वापरणे

**तैनात केले आहे का ते तपासा:**

मूळ निर्देशिकेत `.env` फाइल आहे याची खात्री करा ज्यात Azure क्रेडेन्शियल्स आहेत (मॉड्यूल 01 दरम्यान तयार केलेली):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दाखवायला हवे
```

**अॅप्लिकेशन सुरू करा:**

> **सूचना:** जर तुम्ही आधीच मॉड्यूल 01 मधील `./start-all.sh` वापरून सर्व अॅप्लिकेशन्स सुरू केले असतील, तर हा मॉड्यूल पोर्ट 8083 वर आधीच चालू आहे. पुढील स्टार्ट कमांड्स वगळून थेट http://localhost:8083 येथे जा.

**पर्याय 1: स्प्रिंग बूट डॅशबोर्डचा वापर (VS Code वापरकर्त्यांसाठी शिफारस केली आहे)**

डेव्ह कंटेनरमध्ये स्प्रिंग बूट डॅशबोर्ड विस्तार आहे, जो सर्व स्प्रिंग बूट अॅप्लिकेशन्स व्यवस्थापित करण्यासाठी दृश्यात्मक इंटरफेस पुरवतो. तुम्ही तो VS Code च्या डाव्या बाजूला असलेल्या अॅक्टिव्हिटी बारमध्ये (स्प्रिंग बूट आयकॉन शोधा) पाहू शकता.
Spring Boot डॅशबोर्डमधून, आपण हे करू शकता:
- कार्यक्षेत्रातील उपलब्ध सर्व Spring Boot अनुप्रयोग पहा
- एका क्लिकने अनुप्रयोग सुरू/थांबवा
- अनुप्रयोग लॉग्स रिअल-टाईममध्ये पहा
- अनुप्रयोग स्थितीवर नजर ठेवा

सिंपली "prompt-engineering" च्या शेजारी असलेला प्ले बटण क्लिक करा हा मोड्यूल सुरू करण्यासाठी, किंवा सर्व मोड्यूल एकाच वेळी सुरू करा.

<img src="../../../translated_images/mr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**पर्याय 2: शेल स्क्रिप्ट वापरून**

सर्व वेब अनुप्रयोग (मॉड्युल 01-04) सुरू करा:

**Bash:**
```bash
cd ..  # मुळ निर्देशिकेतून
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # मूळ निर्देशिकेदेखीलून
.\start-all.ps1
```

किंवा फक्त ह्या मोड्यूलला सुरू करा:

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

दोन्ही स्क्रिप्ट्स रूट `.env` फाईलमधून पर्यावरण चल स्वयंचलितपणे लोड करतात आणि जर JAR फाईल्स अस्तित्वात नसतील तर त्या तयार करतील.

> **टीप:** आपण सुरू करण्यापूर्वी सर्व मॉड्यूल हाताने तयार करायचे असल्यास:
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

http://localhost:8083 आपल्या ब्राउजरमध्ये उघडा.

**थांबवण्यासाठी:**

**Bash:**
```bash
./stop.sh  # हा फक्त मॉड्यूल आहे
# किंवा
cd .. && ./stop-all.sh  # सर्व मॉड्यूल्स
```

**PowerShell:**
```powershell
.\stop.ps1  # हा मॉड्यूल फक्त
# किंवा
cd ..; .\stop-all.ps1  # सर्व मॉड्यूल्स
```

## अनुप्रयोग स्क्रीनशॉट्स

<img src="../../../translated_images/mr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*मुख्य डॅशबोर्ड ज्यावर ८ प्रॉम्प्ट इंजिनीअरिंग पॅटर्नस त्यांच्या वैशिष्ट्ये आणि वापर प्रकरणांसह दाखवले आहे*

## पॅटर्न एक्सप्लोर करणे

वेब इंटरफेस वेगवेगळ्या प्रॉम्प्टिंग धोरणांसह प्रयोग करण्याची परवानगी देते. प्रत्येक पॅटर्न वेगवेगळ्या समस्या सोडवतो - ते वापरून पाहा आणि पाहा की कोणती पद्धत केव्हा चांगली काम करते.

### कमी विरक्तता विरुद्ध जास्त विरक्तता

"200 चा 15% काय आहे?" असे सोपे प्रश्न कमी विरक्तता वापरून विचारा. आपल्याला त्वरित, थेट उत्तर मिळेल. आता काही जटिल विचार विचारण्याचा प्रयत्न करा जसे "हाय-ट्राफिक API साठी कॅशिंग धोरण डिझाइन करा" उच्च विरक्तता वापरून. पहा कसे मॉडेल हळूहळू काम करते आणि सविस्तर विचार करते. एकच मॉडेल, एकच प्रश्न संरचना - पण प्रॉम्प्ट त्याला किती विचार करायचा आहे हे सांगतो.

<img src="../../../translated_images/mr/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*कमी विचारसरणीने जलद गणना*

<img src="../../../translated_images/mr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*सविस्तर कॅशिंग धोरण (2.8MB)*

### कार्य अंमलबजावणी (टूल प्रारंभिक सूचना)

अनेक टप्प्यांच्या कार्यप्रवाहांना आधी नियोजन आणि प्रगतीचं वर्णन फायदेशीर ठरतं. मॉडेल काय करेल ते मांडते, प्रत्येक टप्प्याचं वर्णन करते, नंतर निकालांचे सारांश देते.

<img src="../../../translated_images/mr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*स्टेप-बाय-स्टेप वर्णनासह REST एंडपॉइंट तयार करणे (3.9MB)*

### स्व-परावर्तित कोड

"ईमेल व्हॅलिडेशन सेवा तयार करा" असा प्रयत्न करा. फक्त कोड जनरेट करून थांबण्याऐवजी, मॉडेल कोड तयार करते, गुणवत्ता निकषांवर तपासणी करते, कमकुवत बाजू ओळखते आणि सुधारणा करते. आपण पाहाल की तो उत्पादन मानकांपर्यंत कोड कसा सुधारतो.

<img src="../../../translated_images/mr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*पूर्ण ईमेल व्हॅलिडेशन सेवा (5.2MB)*

### संरचित विश्लेषण

कोड पुनरावलोकनासाठी सुसंगत मूल्यमापन फ्रेमवर्क आवश्यक आहे. मॉडेल कोड विशिष्ट श्रेण्या (बरोबरी, पद्धती, कार्यक्षमता, सुरक्षा) व गंभीरतेच्या पातळ्यांसह विश्लेषित करते.

<img src="../../../translated_images/mr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*फ्रेमवर्क-आधारित कोड पुनरावलोकन*

### बहु-चरण संवाद

"Spring Boot म्हणजे काय?" असे विचारा आणि लगेच "मला एक उदाहरण दाखवा" असे पुढील प्रश्न विचारा. मॉडेल आपला पहिला प्रश्न लक्षात ठेवते आणि आपल्याला विशेषतः Spring Boot चे उदाहरण देते. आठवण नसल्यास, दुसरा प्रश्न फार अस्पष्ट असतो.

<img src="../../../translated_images/mr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*प्रश्नांमधील संदर्भ जतन करणे*

### टप्प्याटप्प्याने विचार करणे

कोणतीतरी गणिती समस्या निवडा आणि दोन्ही Step-by-Step Reasoning आणि Low Eagerness वापरून प्रयत्न करा. कमी विरक्तता फक्त उत्तर देते - जलद पण अस्पष्ट. टप्प्याटप्प्याने ते सर्व गणना व निर्णय स्पष्ट करते.

<img src="../../../translated_images/mr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*टप्प्याटप्प्याने स्पष्ट केलेली गणिती समस्या*

### मर्यादित उत्पादन

जेव्हा आपल्याला विशिष्ट स्वरूप किंवा शब्दसंख्या आवश्यक असते, तेव्हा हा पॅटर्न काटेकोरपणे पालन करतो. बुलेट पॉईंट स्वरूपात नेमके १०० शब्दांची सारांश तयार करण्याचा प्रयत्न करा.

<img src="../../../translated_images/mr/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*स्वरूप नियंत्रणासह मशीन लर्निंग सारांश*

## आपण खरंच काय शिकत आहात

**विचार करण्याचा प्रयत्न सर्व काही बदलतो**

GPT-5.2 आपल्याला आपल्या प्रॉम्प्ट्सद्वारे संगणकीय प्रयत्न नियंत्रित करण्याची मुभा देतो. कमी प्रयत्न म्हणजे जलद प्रतिसाद आणि कमी शोध. जास्त प्रयत्न म्हणजे मॉडेल अधिक वेळ देऊन खोलवर विचार करते. आपण शिकत आहात की प्रयत्न कामाच्या गुंतागुंतीनुसार कसे जुळवायचे - सोप्या प्रश्नांवर वेळ वाया घालवू नका, पण जटिल निर्णय जलद करू नका.

**संरचना वर्तनासाठी मार्गदर्शक**

प्रॉम्प्टमध्ये XML टॅग दिसतात का? ते फक्त सजावट नाहीत. मॉडेल्स संरचित सूचना अधिक विश्वासार्हपणे पाळतात. जेव्हा आपल्याला अनेक टप्प्यांचे प्रक्रियात्मक किंवा गुंतागुंतीचे लॉजिक हवे असते, तेव्हा संरचना मॉडेलला त्याचा सद्यस्थिती आणि पुढील टप्पा ट्रॅक करण्यास मदत करते.

<img src="../../../translated_images/mr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*स्पष्ट विभागांसह आणि XML-शैलीतील संघटनेचा असलेल्या प्रॉम्प्टचे अवयव*

**स्व-मूल्यमापनाद्वारे गुणवत्ता**

स्व-परावर्तित पॅटर्न्स गुणवत्ता निकष स्पष्टपणे दाखवतात. मॉडेल "योग्य काम करत आहे" अशी आशा करण्याऐवजी, आपण त्याला "योग्य" म्हणजे काय ते नेमके सांगता: बरोबर तर्क, त्रुटी हाताळणी, कार्यक्षमता, सुरक्षा. मॉडेल नंतर स्वतःच्या आउटपुटचे मूल्यमापन करू शकते आणि सुधारणा करू शकते. त्यामुळे कोड तयार करणे लॉटरीऐवजी प्रक्रिया बनते.

**संदर्भ मर्यादित असतो**

बहु-चरण संभाषण प्रत्येक विनंतीसह संदेश इतिहास समाविष्ट करून कार्य करतात. पण त्याला मर्यादा आहे - प्रत्येक मॉडेलला टोकनची कमाल संख्या असते. संभाषणे वाढतानाच, आपण योग्य संदर्भ जपण्यासाठी धोरणांची गरज असते, ज्यामुळे तो मर्यादा ओलांडू नये. हा मोड्यूल आपल्याला स्मृती कशी काम करते हे दाखवतो; नंतर आपण शिका कधी सारांश करायचा, कधी विसरायचे, आणि कधी पुन्हा मिळवायचे.

## पुढील टप्पे

**पुढील मोड्यूल:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**नेव्हिगेशन:** [← मागील: मोड्युल 01 - परिचय](../01-introduction/README.md) | [मुख्य पानावर परत जा](../README.md) | [पुढील: मोड्युल 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) चा वापर करून भाषांतरित केला आहे. आम्ही अचूकतेसाठी प्रयत्न करतो, परंतु कृपया लक्षात घ्या की स्वयंचलित भाषांतरांमध्ये चुका किंवा अयोग्यताएं असू शकतात. मूळ दस्तऐवज त्याच्या स्थानिक भाषेत अधिकृत स्रोत मानला जावा. महत्त्वाची माहिती असल्यास, व्यावसायिक मानवी भाषांतर शिफारस केली जाते. या अनुवादाचा उपयोग केल्यामुळे उद्भवणाऱ्या कोणत्याही गैरसमजुती किंवा समजुतीबाबत आम्ही जबाबदार आहोत असे नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->