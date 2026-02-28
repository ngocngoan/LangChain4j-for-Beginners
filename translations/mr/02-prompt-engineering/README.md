# मॉड्यूल ०२: GPT-5.2 सह प्रॉम्प्ट अभियांत्रिकी

## विषय निर्देशिका

- [व्हिडिओ वॉकथ्रू](../../../02-prompt-engineering)
- [तुम्ही काय शिकाल](../../../02-prompt-engineering)
- [पूर्वअटी](../../../02-prompt-engineering)
- [प्रॉम्प्ट अभियांत्रिकी समजून घेणे](../../../02-prompt-engineering)
- [प्रॉम्प्ट अभियांत्रिकी मूलतत्त्वे](../../../02-prompt-engineering)
  - [झिरो-शॉट प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [फ्यू-शॉट प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [चेन ऑफ थॉट](../../../02-prompt-engineering)
  - [भूमिका-आधारित प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [प्रॉम्प्ट टेम्पलेट्स](../../../02-prompt-engineering)
- [प्रगत पॅटर्न्स](../../../02-prompt-engineering)
- [विद्यमान Azure संसाधने वापरणे](../../../02-prompt-engineering)
- [अॅप्लिकेशन स्क्रीनशॉट](../../../02-prompt-engineering)
- [पॅटर्न्स एक्सप्लोर करणे](../../../02-prompt-engineering)
  - [लो विरुद्ध हाय ईगरनेस](../../../02-prompt-engineering)
  - [कार्य अंमलबजावणी (टूल प्रीअ‍ॅम्बल्स)](../../../02-prompt-engineering)
  - [स्व-प्रतिबिंबन करणाऱ्या कोड](../../../02-prompt-engineering)
  - [संरचित विश्लेषण](../../../02-prompt-engineering)
  - [मल्टी-टर्न चॅट](../../../02-prompt-engineering)
  - [पायरी-पायरीने विचार करणे](../../../02-prompt-engineering)
  - [मर्यादित आउटपुट](../../../02-prompt-engineering)
- [तुम्ही खरोखर काय शिकत आहात](../../../02-prompt-engineering)
- [पुढील पावले](../../../02-prompt-engineering)

## व्हिडिओ वॉकथ्रू

या लाइव्ह सत्रात पाहा ज्यात हे मॉड्यूल कसे सुरू करावे ते समजावले आहे: [LangChain4j सह प्रॉम्प्ट अभियांत्रिकी - लाइव्ह सत्र](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## तुम्ही काय शिकाल

<img src="../../../translated_images/mr/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

मागील मॉड्यूलमध्ये, तुम्ही पाहिले कसे मेमरी संवादात्मक AI सक्षम करते आणि GitHub मॉडेल्स वापरून मूलभूत संवाद साधले. आता आपण लक्ष केंद्रित करू की आपण प्रश्न कसे विचारता — प्रॉम्प्ट्स स्वतः — Azure OpenAI च्या GPT-5.2 वापरून. तुम्ही प्रॉम्प्ट्स कसे संरचित करता त्याचा उत्तरांच्या गुणवत्तेवर मोठा परिणाम होतो. आपण मूलभूत प्रॉम्प्टिंग तंत्रांचा आढावा घेऊन सुरुवात करतो, नंतर GPT-5.2 च्या क्षमतांचा पूर्ण फायदा घेणारे आठ प्रगत पॅटर्न्स बघतो.

आपण GPT-5.2 वापरणार आहोत कारण हे विश्लेषण नियंत्रण सादर करते - तुम्ही मॉडेलला सांगू शकता की उत्तर देण्यापूर्वी किती विचार करायचा आहे. हे विविध प्रॉम्प्टिंग धोरणे अधिक स्पष्ट करते आणि तुम्हाला प्रत्येक पद्धत कधी वापरावी हे समजण्यास मदत करते. GitHub मॉडेल्सच्या तुलनेत Azure चे GPT-5.2 साठी कमी रेेट लिमिट्स देखील आपल्याला फायदेशीर ठरतील.

## पूर्वअटी

- मॉड्यूल ०१ पूर्ण केले (Azure OpenAI संसाधने तैनात)
- रूट डायरेक्टरीमध्ये `.env` फाइल Azure प्रमाणपत्रांसह (मॉड्यूल ०१ मध्ये `azd up` ने तयार केलेली)

> **टीप:** जर तुम्ही मॉड्यूल ०१ पूर्ण केले नसेल, तर प्रथम तिथल्या डिप्लॉयमेंट सूचना पाळा.

## प्रॉम्प्ट अभियांत्रिकी समजून घेणे

<img src="../../../translated_images/mr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

प्रॉम्प्ट अभियांत्रिकी म्हणजे असे इनपुट टेक्स्ट डिझाईन करणे जे तुम्हाला हवे असलेले निकाल सातत्याने देते. हे फक्त प्रश्न विचारण्याबाबत नाही - हे विनंत्यांचे व्यवस्थित रचनात्मक स्वरूप देण्याबाबत आहे, ज्यामुळे मॉडेल नेमके काय हवे आहे आणि कसे प्रदान करायचे हे समजते.

याला समजा तुम्ही सहकारीला सूचना देत आहात. "बग दुरुस्त करा" हे अस्पष्ट आहे. "UserService.java मध्ये ४५ व्या ओळीत null pointer exception दुरुस्त करा आणि null check जोडा" हे विशिष्ट आहे. भाषा मॉडेल्स देखील तसेच कार्य करतात - विशिष्टता आणि रचना महत्त्वाची आहे.

<img src="../../../translated_images/mr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j ही पायाभूत सुविधा पुरवते — मॉडेल कनेक्शन, मेमरी, आणि संदेश प्रकार — आणि प्रॉम्प्ट पॅटर्न्स फक्त काळजीपूर्वक रचलेल्या मजकुराचे टेक्स्ट आहेत जे त्या पायाभूत सुविधेमार्फत पाठवले जातात. मुख्य बांधणी ब्लॉक्स म्हणजे `SystemMessage` (जो AI चा वर्तन आणि भूमिका सेट करतो) आणि `UserMessage` (जो तुमचा खरा विनंती संदेश वाहून नेतो).

## प्रॉम्प्ट अभियांत्रिकी मूलतत्त्वे

<img src="../../../translated_images/mr/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

या मॉड्यूलमधील प्रगत पॅटर्न्समध्ये डुबकी मारण्यापूर्वी, आपण पाच मूलभूत प्रॉम्प्टिंग तंत्रे पुनरावलोकन करूया. ही त्या मूलतत्त्वे आहेत ज्यांना प्रत्येक प्रॉम्प्ट अभियंता जाणून असले पाहिजे. जर तुम्ही आधीच [क्विक स्टार्ट मॉड्यूल](../00-quick-start/README.md#2-prompt-patterns) मधून गेला असाल, तर तुम्ही हे प्रत्यक्षात पाहिले आहे — ही त्यांमागची संकल्पनात्मक चौकट आहे.

### झिरो-शॉट प्रॉम्प्टिंग

सर्वात सोपा उपाय: मॉडेलला कोणतेही उदाहरण न देता थेट सूचना द्या. मॉडेल पूर्णपणे त्याच्या प्रशिक्षणावर अवलंबून राहते काम समजून घेण्यासाठी आणि अंमलबजावणी करण्यासाठी. हे सोप्या विनंत्यांसाठी प्रभावी आहे जिथे अपेक्षित वर्तन स्पष्ट आहे.

<img src="../../../translated_images/mr/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*उदाहरणांशिवाय थेट सूचना — मॉडेल फक्त सूचनेवरून काम समजते*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// प्रतिसाद: "सकारात्मक"
```

**कधी वापरावे:** सोप्या वर्गीकरणांसाठी, थेट प्रश्नांसाठी, भाषांतरांसाठी, किंवा कोणत्याही कार्यासाठी जे अतिरिक्त मार्गदर्शनाशिवाय मॉडेल हाताळू शकते.

### फ्यू-शॉट प्रॉम्प्टिंग

तुमच्या अपेक्षित नमुन्याचा दाखला देणारी उदाहरणे द्या. मॉडेल तुमच्या उदाहरणांमधून अपेक्षित इनपुट-आउटपुट स्वरूप शिकते आणि नवीन इनपुट्सवर ते लागू करते. हे अशा कार्यांसाठी सातत्य वाढवते जिथे अपेक्षित स्वरूप किंवा वर्तन स्पष्ट नसते.

<img src="../../../translated_images/mr/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*उदाहरणांमधून शिकणे — मॉडेल नमुन्याची ओळख करून नव्या इनपुट्सवर त्याचा वापर*

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

**कधी वापरावे:** सानुकूल वर्गीकरणे, सातत्यपूर्ण स्वरूपन, क्षेत्र-विशिष्ट कार्ये, किंवा जेव्हा झिरो-शॉट परिणाम विसंगत असतात.

### चेन ऑफ थॉट

मॉडेलला त्याचा विचार प्रक्रिया टप्प्याटप्प्याने दाखवायला सांगा. थेट उत्तरावर उडी मारण्याऐवजी, मॉडेल समस्येचे तुकडे करून प्रत्येक भाग स्पष्टपणे सोडवते. हे गणित, तर्कशास्त्र, आणि अनेक पायऱ्यांच्या विचारप्रक्रियेतील अचूकता सुधारते.

<img src="../../../translated_images/mr/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*टप्प्याटप्प्याने विचार करणे — गुंतागुंतीच्या समस्यांना स्पष्ट तर्कशास्त्रातील पायऱ्यांमध्ये विभागणे*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// मॉडेल दाखवते: १५ - ८ = ७, नंतर ७ + १२ = १९ सफरचंदे
```

**कधी वापरावे:** गणिती समस्या, तर्कशास्त्राचे कोडे, डिबगिंग, किंवा जेथे विचार प्रक्रियेला दाखवणे अचूकता आणि विश्वास वाढवते.

### भूमिका-आधारित प्रॉम्प्टिंग

तुमचा प्रश्न विचारण्यापूर्वी AI साठी व्यक्तिमत्व किंवा भूमिका सेट करा. हे संदर्भ देते जे उत्तराची शैली, खोली, आणि लक्ष केंद्रित करते. "सॉफ्टवेअर आर्किटेक्ट" हे "जुनिअर डेव्हलपर" किंवा "सुरक्षा तपासक" पेक्षा वेगळे सल्ले देतो.

<img src="../../../translated_images/mr/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*संदर्भ आणि व्यक्तिमत्व सेट करणे — एकच प्रश्न वेगवेगळ्या भूमिकांसाठी वेगळी उत्तरे*

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

**कधी वापरावे:** कोड पुनरावलोकने, ट्युटरिंग, क्षेत्र-विशिष्ट विश्लेषण, किंवा जेव्हा तुम्हाला विशिष्ट कौशल्यस्तर किंवा दृष्टिकोनानुसार उत्तर हवे असते.

### प्रॉम्प्ट टेम्पलेट्स

चलनीक स्थानांसह पुन्हा वापरता येणाऱ्या प्रॉम्प्ट्स तयार करा. प्रत्येक वेळी नवीन प्रॉम्प्ट लिहिण्याऐवजी, एकदा टेम्पलेट定र करा आणि वेगवेगळ्या मूल्यांनी भरा. LangChain4j चा `PromptTemplate` वर्ग `{{variable}}` सिंटॅक्स सह हे सोपे करतो.

<img src="../../../translated_images/mr/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*चलनीक स्थानांसह पुनर्वापरयोग्य प्रॉम्प्ट्स — एक टेम्पलेट, अनेक उपयोग*

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

**कधी वापरावे:** वेगवेगळ्या इनपुट्ससह पुनरावृत्ती प्रश्नांसाठी, बॅच प्रक्रिया करण्यासाठी, पुनर्वापरयोग्य AI वर्कफ्लोज तयार करण्यासाठी, किंवा कोणत्याही परिस्थितीत जिथे प्रॉम्प्टची रचना सारखी राहते पण डेटा बदलतो.

---

हे पाच मूलभूत तंत्र तुमच्यासाठी बहुतेक प्रॉम्प्टिंग कार्यांसाठी मजबूत साधनसामग्री देतात. या मॉड्यूलच्या उरलेल्या भागात आपण **आठ प्रगत पॅटर्न्स** पाहणार आहोत जे GPT-5.2 च्या विचार नियंत्रण, स्व-मूल्यमापन, आणि संरचित आउटपुट क्षमतांचा पूर्ण प्रभाव घेतात.

## प्रगत पॅटर्न्स

मूलभूत तत्त्वांचा आढावा घेतल्यावर, चला पुढे जाणो आणि पाहू या आठ प्रगत पॅटर्न्स जे या मॉड्यूलला वेगळं बनवतात. सर्व समस्या एकसारख्या पद्धतीची गरज नसतात. काही प्रश्नांना जलद उत्तर हवे, काहींना खोल विचार करावा लागतो. काहींना स्पष्ट विचारांची प्रक्रिया पाहिजे, तर काहींना फक्त निष्पत्ती. खालील प्रत्येक पॅटर्न वेगळ्या परिस्थितीसाठी अनुकूल आहे — आणि GPT-5.2 चा विचार नियंत्रण हे फरक आणखी स्पष्ट करतो.

<img src="../../../translated_images/mr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*आठ प्रॉम्प्ट अभियांत्रिकी पॅटर्न्सचे विहंगावलोकन आणि त्यांचे वापर केस*

<img src="../../../translated_images/mr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 चा विचार नियंत्रण तुम्हाला सांगण्याची परवानगी देतो की मॉडेल ने किती विचार करायचा आहे — जलद थेट उत्तरांपासून खोल संशोधनापर्यंत*

**लो ईगरनेस (जलद आणि लक्ष केंद्रीत)** - सोप्या प्रश्नांसाठी जिथे तुम्हाला जलद, थेट उत्तर हवे आहे. मॉडेल कमी विचार करते - जास्तीत जास्त 2 पावले. गणना, शोध, किंवा थेट प्रश्नांसाठी याचा वापर करा.

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

> 💡 **GitHub Copilot सह एक्सप्लोर करा:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) उघडा आणि विचारा:
> - "लो ईगरनेस आणि हाय ईगरनेस प्रॉम्प्टिंग पॅटर्नमधील फरक काय आहे?"
> - "प्रॉम्प्टमधील XML टॅग AI च्या उत्तर संरचनेस कशी मदत करतात?"
> - "स्व-प्रतिबिंबन पॅटर्न्स आणि थेट सूचना कधी वापराव्यात?"

**हाय ईगरनेस (खोल आणि सखोल)** - जटिल समस्यांसाठी जिथे तुम्हाला व्यापक विश्लेषण हवे आहे. मॉडेल सखोल शोध घेतो आणि तपशीलवार विचार दाखवतो. सिस्टम डिझाइन, आर्किटेक्चर निर्णय, किंवा जटिल संशोधनासाठी वापरा.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**कार्य अंमलबजावणी (पायरी-पायरी प्रगती)** - बहु-चरण प्रक्रियांसाठी. मॉडेल एक पूर्वतयारी योजना देते, काम करतानाच्या प्रत्येक टप्प्यावर वर्णन करते, नंतर सारांश देते. माइग्रेशन्स, अंमलबजावणी, किंवा कोणत्याही बहु-चरण प्रक्रियेसाठी वापरा.

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

चेन-ऑफ-थॉट प्रॉम्प्टिंग मॉडेलला त्याचा विचार प्रक्रिया स्पष्ट करण्यास सांगतो, ज्यामुळे गुंतागुंतीच्या कार्यांसाठी अचूकता सुधारते. पायरी-पायरी विघटन माणूस आणि AI दोघांसाठीही तर्क समजण्यास मदत करते.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** या पॅटर्नविषयी विचारा:
> - "दीर्घकालीन ऑपरेशन्ससाठी कार्य अंमलबजावणी पॅटर्न कसा समायोजित करेन?"
> - "उत्पादन अॅप्लिकेशन्समध्ये टूल प्रीअ‍ॅम्बल्स संरचित करण्याचे सर्वोत्तम सराव काय आहेत?"
> - "मधली प्रगती अपडेट्स UI मध्ये कशी कॅप्चर आणि प्रदर्शित करू शकतो?"

<img src="../../../translated_images/mr/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*कार्याची योजना → अंमलबजावणी → सारांश बहु-चरण कार्यांसाठी वर्कफ्लो*

**स्व-प्रतिबिंबन करणारा कोड** - उत्पादन-गुणवत्तेचा कोड निर्माण करण्यासाठी. मॉडेल उत्पादन मानकांसह कोड तयार करते ज्यात योग्य त्रुटी हाताळणी असते. नवीन वैशिष्ट्ये किंवा सेवा तयार करताना वापरा.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/mr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*आव्हानात्मक सुधारणा चक्र - निर्माण करा, मूल्यमापन करा, समस्या शोधा, सुधारणा करा, पुनरावृत्ती करा*

**संरचित विश्लेषण** - सातत्यपूर्ण मूल्यमापनासाठी. मॉडेल एक निश्चित चौकट वापरून कोड पुनरावलोकन करते (योग्यत्व, सराव, कार्यक्षमता, सुरक्षा, देखभालयोग्यता). कोड पुनरावलोकने किंवा गुणवत्तेचे मूल्यमापन करताना वापरा.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅटसह प्रयत्न करा:** संरचित विश्लेषणाबाबत विचारा:
> - "वेगवेगळ्या प्रकारच्या कोड पुनरावलोकनांसाठी विश्लेषण फ्रेमवर्क कसे सानुकूलित करू?"
> - "संरचित आउटपुट प्रोग्रामॅटिकली कसे पार्स व अंमलात आणू?"
> - "वेगवेगळ्या पुनरावलोकन सत्रांमध्ये सातत्यपूर्ण गंभीरता स्तर कशी सुनिश्चित कराल?"

<img src="../../../translated_images/mr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*गंभीरता स्तरांसह सातत्यपूर्ण कोड पुनरावलोकनासाठी फ्रेमवर्क*

**मल्टी-टर्न चॅट** - संदर्भ आवश्यक असलेल्या संभाषणांसाठी. मॉडेल मागील संदेश लक्षात ठेवते आणि त्यावर पुढे वाढवते. इंटरऐक्टिव मदत सत्रां किंवा जटिल प्रश्नउत्तरांसाठी वापरा.

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

*एका टोकन मर्यादेपर्यंत संभाषण संदर्भ अनेक टर्नमध्ये कसा संकलित होतो*

**पायरी-पायरीने विचार करणे** - दृश्यमान तर्क आवश्यक असलेल्या समस्यांसाठी. मॉडेल प्रत्येक टप्प्यासाठी स्पष्ट विचार प्रक्रिया दाखवते. गणिती समस्या, तर्कशास्त्र कोडे, किंवा विचार प्रक्रिया समजून घेण्यासाठी वापरा.

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

*समस्यांना स्पष्ट तार्किक पायऱ्यांमध्ये विभागणे*

**मर्यादित आउटपुट** - विशिष्ट स्वरूप मागणाऱ्या प्रतिसादांसाठी. मॉडेल स्वरूप आणि लांबीच्या नियमांचे काटेकोरपणे पालन करते. सारांश किंवा नेमके आउटपुट संरचना हव्या असताना वापरा.

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

*विशिष्ट स्वरूप, लांबी, आणि संरचनात्मक मागण्या लागू करणे*

## विद्यमान Azure संसाधने वापरणे

**तैनाती सत्यापित करा:**

रूट डायरेक्टरीमध्ये `.env` फाइल अस्तित्वात आहे याची खात्री करा ज्यात Azure प्रमाणपत्रे आहेत (मॉड्यूल ०१ मध्ये तयार केलेली):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दाखवायला हवे
```

**अॅप्लिकेशन सुरू करा:**

> **टीप:** जर तुम्ही आधीच मॉड्यूल ०१ मधून `./start-all.sh` वापरून सर्व अॅप्लिकेशन्स सुरू केले असतील, तर हा मॉड्यूल 8083 पोर्टवर आधीपासून चालू आहे. खालील सुरूवातीचे आदेश वगळू शकता आणि थेट http://localhost:8083 येथे जाऊ शकता.

**पर्याय १: स्प्रिंग बूट डॅशबोर्ड वापरणे (VS Code वापरकर्त्यांसाठी शिफारसीय)**
डेव्ह कंटेनरमध्ये Spring Boot Dashboard एक्सटेंशन समाविष्ट आहे, जे सर्व Spring Boot अनुप्रयोगांचे व्यवस्थापन करण्यासाठी एक दृष्य इंटरफेस प्रदान करते. आपण ते VS Code च्या डाव्या बाजूला Activity Bar मध्ये शोधू शकता (Spring Boot चिन्ह शोधा).

Spring Boot Dashboard मधून, आपण करू शकता:
- कार्यक्षेत्रात उपलब्ध सर्व Spring Boot अनुप्रयोग पाहणे
- एक क्लिकने अनुप्रयोग सुरू/थांबविणे
- अनुप्रयोग लॉग रिअल-टाईममध्ये पाहणे
- अनुप्रयोग स्थितीची देखरेख करणे

"prompt-engineering" च्या बाजूला प्ले बटणावर क्लिक करा हा मॉड्युल सुरू करण्यासाठी, किंवा सर्व मॉड्युल एकाच वेळी सुरू करा.

<img src="../../../translated_images/mr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**पर्याय 2: शेल स्क्रिप्ट वापरणे**

सर्व वेब अनुप्रयोग सुरू करा (मॉड्युल 01-04):

**Bash:**
```bash
cd ..  # मूळ निर्देशिका पासून
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # रूट डिरेक्टरीमधून
.\start-all.ps1
```

किंवा केवळ हा मॉड्युल सुरू करा:

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

दोन्ही स्क्रिप्ट्स स्वयंचलितपणे root `.env` फाईलमधून एन्व्हायर्नमेंट व्हेरिएबल्स लोड करतात आणि जर JARs अस्तित्वात नसतील तर ते तयार करतील.

> **टीप:** जर आपण सर्व मॉड्युल्स मॅन्युअली तयार करायचे असतील तर ते सुरू करण्यापूर्वी:
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

http://localhost:8083 आपल्या ब्राउझरमध्ये उघडा.

**थांबविण्यासाठी:**

**Bash:**
```bash
./stop.sh  # हा फक्त मॉड्यूल आहे
# किंवा
cd .. && ./stop-all.sh  # सर्व मॉड्यूल
```

**PowerShell:**
```powershell
.\stop.ps1  # फक्त हा मॉड्यूल
# किंवा
cd ..; .\stop-all.ps1  # सर्व मॉड्यूल
```

## अनुप्रयोग स्क्रीनशॉट्स

<img src="../../../translated_images/mr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*प्रमुख डॅशबोर्ड ज्यामध्ये सर्व 8 prompt engineering पॅटर्न्स त्यांच्या वैशिष्ट्यांसह व उपयोग प्रकरणांसह दाखवले आहेत*

## पॅटर्न्सची तपासणी

वेब इंटरफेस तुम्हाला विविध प्रम्प्टिंग धोरणांसह प्रयोग करण्याची मुभा देतो. प्रत्येक पॅटर्न वेगळे प्रश्न सोडवतो - प्रयत्न करा आणि पाहा की कोणता दृष्टिकोन कधी चमकतो.

> **टीप: Streaming विरुद्ध Non-Streaming** — प्रत्येक पॅटर्न पृष्ठावर दोन बटणे असतात: **🔴 Stream Response (Live)** आणि एक **Non-streaming** पर्याय. Streaming Server-Sent Events (SSE) वापरते ज्यामुळे मॉडेल तयार करत असलेल्या टोकन्स वेळेत पाहता येतात, त्यामुळे प्रगती लगेच दिसते. Non-streaming पर्याय संपूर्ण प्रतिसादासाठी वाट पाहतो आणि नंतर दाखवतो. ज्याप्रमाणे जटिल विचार मागणारे प्रम्प्ट्स (उदा., High Eagerness, Self-Reflecting Code) जोरदार वेळ लागू शकतो — कधीकधी मिनिटे — कोणताही दृश्यमान प्रतिसादाशिवाय. **जटिल प्रम्प्ट्ससाठी प्रयोग करताना streaming वापरा** म्हणजे मॉडेल कसे कार्य करते हे तुम्हाला दिसेल आणि विनंती वेळ संपल्याचा भास येणार नाही.
>
> **टीप: ब्राउझर आवश्यकता** — Streaming फिचर Fetch Streams API (`response.body.getReader()`) वापरतो ज्यासाठी पूर्ण ब्राउझर आवश्यक आहे (Chrome, Edge, Firefox, Safari). VS Code च्या अंतर्निर्मित Simple Browser मध्ये हे काम करत नाही कारण त्याच्या webview ला ReadableStream API ची सुविधा नाही. Simple Browser वापरल्यास non-streaming बटणे सामान्यत: कार्य करतील — केवळ streaming बटणे प्रभावित होतील. संपूर्ण अनुभवासाठी http://localhost:8083 बाह्य ब्राउझरमध्ये उघडा.

### Low vs High Eagerness

Low Eagerness वापरून "What is 15% of 200?" असा सोपा प्रश्न विचारा. तुम्हाला एक त्वरित, थेट उत्तर मिळेल. आता High Eagerness वापरून "Design a caching strategy for a high-traffic API" असा कॉम्प्लेक्स प्रश्न विचारा. **🔴 Stream Response (Live)** क्लिक करा आणि मॉडेलचे सविस्तर विचार टोकन-टोकन दिसताना पहा. त्याच मॉडेलला, त्याच प्रश्न रचनेला - पण प्रम्प्ट मध्ये किती विचार करायचा ते सांगितले आहे.

### Task Execution (Tool Preambles)

बहु-टप्प्यांच्या कार्यपद्धतींसाठी आधीचे नियोजन व प्रगती वर्णन उपयुक्त ठरते. मॉडेल काय करणार आहे हे ओळखते, प्रत्येक पाऊल वर्णन करते आणि नंतर निकालांचा सारांश देते.

### Self-Reflecting Code

"Create an email validation service" प्रयत्न करा. फक्त कोड तयार करणे आणि थांबवण्याऐवजी, मॉडेल तयार करतो, गुणवत्ता निकषांशी तुलना करतो, त्रुटी ओळखतो आणि सुधारणा करतो. तुम्हाला ते पुन्हा पुन्हा पुनरावृत्ती करताना दिसेल तोपर्यंत कोड उत्पादन मानकांच्या अनुरूप होईपर्यंत.

### Structured Analysis

कोड पुनरावलोकनांना सुसंगत मूल्यांकन फ्रेमवर्क्स आवश्यक असतात. मॉडेल कोडचे विश्लेषण निश्चित वर्गांमध्ये (बरोबरी, सराव, कामगिरी, सुरक्षा) तीव्रतेच्या पातळ्यांसह करतो.

### Multi-Turn Chat

"Spring Boot काय आहे?" असा प्रश्न विचारा आणि लगेच "आकृती दाखवा" असा पुढचा प्रश्न विचारा. मॉडेल आपला पहिला प्रश्न लक्षात ठेवते आणि खास१२ के spring Boot ची उदाहरणे देते. स्मृतीशिवाय दुसरा प्रश्न फारसा अस्पष्ट ठरेल.

### Step-by-Step Reasoning

कोणतेही गणितीय प्रश्न निवडा आणि Step-by-Step Reasoning आणि Low Eagerness दोन्ही वापरून प्रयत्न करा. Low eagerness फक्त उत्तर देतो - वेगाने पण अस्पष्ट. Step-by-step तुम्हाला प्रत्येक गणना आणि निर्णय दाखवतो.

### Constrained Output

जेव्हा तुम्हाला विशिष्ट फॉरमॅट किंवा शब्दसंख्या हवी असते, तेव्हा हा पॅटर्न कडकपणे नियमांचे पालन करतो. अचूक 100 शब्दांचा सारांश बुलेट पॉइंट फॉरमॅटमध्ये तयार करून पाहा.

## तुम्ही खरोखर काय शिकत आहात

**तर्कशक्तीचा प्रयत्न सर्व काही बदलतो**

GPT-5.2 तुम्हाला तुमच्या प्रम्प्ट्सद्वारे संगणकीय प्रयत्न नियंत्रित करू देते. कमी प्रयत्न म्हणजे जलद प्रतिसाद कमी शोध घेऊन. जास्त प्रयत्न म्हणजे मॉडेल खोलवर विचार करण्यासाठी वेळ घेते. तुम्ही प्रयत्न आपल्या कार्याच्या जटिलतेशी जुळवायला शिकत आहात - सोप्या प्रश्नांवर वेळ वाया घालवू नका, पण जटिल निर्णयांना घाई करू नका.

**रचना वर्तन दिशादर्शक**

प्रम्प्टमधील XML टॅग्स लक्षात घेतले आहेत का? ते फक्त सजावटीसाठी नाहीत. मॉडेल्स संरचित सूचना मुक्तपणे दिलेल्या मजकुरापेक्षा अधिक विश्वासार्हपणे पाळतात. जेव्हा तुम्हाला बहु-चरण प्रक्रिया किंवा गुंतागुंतीची लॉजिक हवी, तेव्हा रचना मॉडेलला त्याच्या स्थितीचा मागोवा ठेवायला मदत करते आणि पुढे काय करायचे ते कळते.

<img src="../../../translated_images/mr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*स्पष्ट विभागांसह आणि XML-शैलीच्या आयोजनासह एक चांगल्या प्रकारे रचित प्रॉम्प्ट चे शारीरिक विज्ञान*

**स्व-मूल्यांकनाद्वारे गुणवत्ता**

स्व-प्रतिबिंबित पॅटर्न गुणवत्ता निकष स्पष्ट करतात. मॉडेल "योग्य करत असेल" अशी आशा करण्याऐवजी, तुम्ही त्याला अचूक काय योग्य आहे ते सांगता: बरोबरीची युक्ती, त्रुटी हाताळणी, कामगिरी, सुरक्षा. त्यानंतर मॉडेल स्वतःच्या उत्पादनाचे मूल्यमापन करून सुधारणा करू शकते. यामुळे कोड निर्मिती लॉटरी पासून प्रक्रियेत होऊन जाते.

**संदर्भ समाप्त आहे**

बहु-चरण संभाषणे प्रत्येक विनंतीसह संदेश इतिहास समाविष्ट करून कार्य करतात. पण मर्यादा आहे - प्रत्येक मॉडेलला टोकनांची कमाल संख्या असते. संभाषणे वाढल्यावर तुम्हाला संबंधित संदर्भ जपण्यासाठी धोरणे हवी होतात ज्यामुळे ती मर्यादा टाळता येईल. हा मॉड्युल तुम्हाला स्मृती कशी कार्य करते हे दाखवतो; नंतर तुम्ही समजाल की कधी सारांश करायचा, कधी विसरायचे आणि कधी पुनर्प्राप्त करायचे.

## पुढील पायऱ्या

**पुढील मॉड्युल:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**नेव्हिगेशन:** [← मागील: मॉड्युल 01 - परिचय](../01-introduction/README.md) | [मुख्य पृष्ठावर परत जा](../README.md) | [पुढे: मॉड्युल 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**सूचना**:  
हा दस्तऐवज AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून अनुवादित केला आहे. आम्ही अचूकतेसाठी प्रयत्न करतो, तरी कृपया लक्षात ठेवा की स्वयंचलित अनुवादांमध्ये चुका किंवा असंगतता असू शकते. मूळ दस्तऐवज हा त्याच्या स्थानिक भाषेत अधिकृत स्रोत मानला जावा. महत्त्वाच्या माहितीकरिता व्यावसायिक मानवी अनुवाद शिफारसीय आहे. या अनुवादाच्या वापरामुळे होणाऱ्या कोणत्याही गैरसमजुती किंवा चुकीच्या अर्थलागीस आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->