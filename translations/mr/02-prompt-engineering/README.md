# Module 02: GPT-5.2 सह प्रांप्ट अभियांत्रिकी

## अनुक्रमणिका

- [आपण काय शिकणार आहात](../../../02-prompt-engineering)
- [आवश्यक पूर्वशर्ता](../../../02-prompt-engineering)
- [प्रांप्ट अभियांत्रिकी समजून घेणे](../../../02-prompt-engineering)
- [प्रांप्ट अभियांत्रिकीच्या मूलतत्त्वे](../../../02-prompt-engineering)
  - [झीरो-शॉट प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [फ्यू-शॉट प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [चेन ऑफ थॉट](../../../02-prompt-engineering)
  - [भूमिका-आधारित प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [प्रॉम्प्ट टेम्पलेट्स](../../../02-prompt-engineering)
- [अत्याधुनिक नमुने](../../../02-prompt-engineering)
- [विद्यमान Azure संसाधने वापरणे](../../../02-prompt-engineering)
- [अ‍ॅप्लिकेशन स्क्रीनशॉट्स](../../../02-prompt-engineering)
- [नमुने एक्सप्लोर करणे](../../../02-prompt-engineering)
  - [कमी विरक्ती व उच्च विरक्ती](../../../02-prompt-engineering)
  - [कार्य अंमलबजावणी (टूल प्रीअॅंबल्स)](../../../02-prompt-engineering)
  - [स्व-प्रतिबिंबित करणारे कोड](../../../02-prompt-engineering)
  - [स्ट्रक्चर्ड विश्लेषण](../../../02-prompt-engineering)
  - [मल्टी-टर्न चॅट](../../../02-prompt-engineering)
  - [पायरी-भरीत कारणे](../../../02-prompt-engineering)
  - [मर्यादित आउटपुट](../../../02-prompt-engineering)
- [आपण खरोखर काय शिकत आहात](../../../02-prompt-engineering)
- [पुढील पावले](../../../02-prompt-engineering)

## आपण काय शिकणार आहात

<img src="../../../translated_images/mr/what-youll-learn.c68269ac048503b2.webp" alt="आपण काय शिकणार आहात" width="800"/>

मागील मॉड्यूलमध्ये, आपण पाहिले की मेमरी कशी संवेदनशील AI ला सक्षम करते आणि GitHub मॉडेल्सचा वापर मूळ संवादांसाठी केला. आता आपण या व्हर प्रश्न कसा विचारता यावर लक्ष केंद्रित करू — म्हणजेच प्रॉम्प्ट्स — Azure OpenAI च्या GPT-5.2 वापरून. आपण प्रॉम्प्ट्स कसे रचता हे प्रतिसादांच्या दर्जावर फार परिणाम करते. आम्ही मूलभूत प्रॉम्प्टिंग तंत्रे पुनरावलोकन करू आणि नंतर GPT-5.2 च्या संपूर्ण क्षमतेचा लाभ घेणारे आठ उच्चस्तरीय नमुने पाहू.

आम्ही GPT-5.2 वापरणार आहोत कारण त्यात कारण प्रक्रियेवर नियंत्रण आहे — तुम्ही मॉडेलला सांगू शकता की उत्तर देण्यापूर्वी किती विचार करायचा आहे. यामुळे विविध प्रॉम्प्टिंग धोरणे अधिक स्पष्ट होतात आणि तुम्हाला प्रत्येक दृष्टिकोन कधी वापरायचा हे समजायला मदत होते. तसेच GitHub मॉडेल्सच्या तुलनेत Azure मध्ये GPT-5.2 साठी कमी दर मर्यादा आहेत ज्याचा आम्हाला फायदा होतो.

## आवश्यक पूर्वशर्ता

- मॉड्यूल 01 पूर्ण (Azure OpenAI संसाधने तैनात केली आहेत)
- मुख्य निर्देशिकेत `.env` फाइल ज्यात Azure प्रमाणपत्रे आहेत (`azd up` च्या मदतीने मॉड्यूल 01 मध्ये तयार केलेली)

> **टिप:** जर आपण मॉड्यूल 01 पूर्ण केले नसेल, तर तिथल्या तैनाती सूचना प्रथम पूर्ण करा.

## प्रांप्ट अभियांत्रिकी समजून घेणे

<img src="../../../translated_images/mr/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="प्रांप्ट अभियांत्रिकी म्हणजे काय?" width="800"/>

प्रांप्ट अभियांत्रिकी म्हणजे अशी इनपुट मजकूर डिझाइन करणे जे तुम्हाला सतत हवे ते परिणाम देते. हे फक्त प्रश्न विचारण्याबद्दल नाही — तर अशा विनंत्या रचण्याबद्दल आहे की मॉडेल नेमके काय हवे याचा आणि ते कसे प्रदान करायचे याचा समजून घेते.

हे असे समजा की तुम्ही एखाद्या सहकाऱ्याला सूचना देता. "बग दुरुस्त करा" हा अस्पष्ट आहे. "UserService.java च्या 45 व्या ओळीत नल तपासणी जोडून नल पॉइंटर अपवाद दुरुस्त करा" हा स्पष्ट आहे. भाषा मॉडेल्स देखील याच प्रकारे कार्य करतात — स्पष्टता आणि रचनेस महत्त्वाची आहे.

<img src="../../../translated_images/mr/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j कसा बसतो" width="800"/>

LangChain4j हे इन्फ्रास्ट्रक्चर प्रदान करते — मॉडेल कनेक्शन्स, मेमरी आणि मेसेज प्रकार — तर प्रॉम्प्ट पॅटर्न्स फक्त काळजीपूर्वक रचलेला मजकूर आहे जो या इन्फ्रास्ट्रक्चरच्या माध्यमातून पाठविला जातो. मुख्य बांधकाम ब्लॉक्स म्हणजे `SystemMessage` (जो AI चा वर्तन आणि भूमिका सेट करतो) आणि `UserMessage` (जो तुमची खरी विनंती घेऊन येतो).

## प्रांप्ट अभियांत्रिकीच्या मूलतत्त्वे

<img src="../../../translated_images/mr/five-patterns-overview.160f35045ffd2a94.webp" alt="पाच प्रांप्ट अभियांत्रिकी नमुन्यांचा आढावा" width="800"/>

या मॉड्यूलमधील अत्याधुनिक नमुन्यांमध्ये डुबकी मारण्याआधी, चला पाच मूलभूत प्रांप्टिंग तंत्रांचे पुनरावलोकन करू. हे आहेत त्या मूलतत्त्वांचे ब्लॉक्स जे प्रत्येक प्रॉम्प्ट इंजिनिअरला माहित असावे. जर तुम्ही आधीच [क्विक स्टार्ट मॉड्यूल](../00-quick-start/README.md#2-prompt-patterns) केले असेल, तर तुम्ही हे प्रत्यक्षात पाहिले आहेत — येथे त्यांचे संकल्पनात्मक चौकट दिली आहे.

### झीरो-शॉट प्रॉम्प्टिंग

सर्वात सोपा दृष्टीकोन: मॉडेलला थेट सूचना देणे, न कोणी उदाहरण दिले नाही. मॉडेल पूर्णपणे त्याच्या प्रशिक्षणावर अवलंबून राहते म्हणजे कार्य समजून घेणे आणि अंमलबजावणी करणे. हे सोप्या विनंत्यांसाठी उत्तम असते जिथे अपेक्षित वर्तन स्पष्ट असते.

<img src="../../../translated_images/mr/zero-shot-prompting.7abc24228be84e6c.webp" alt="झीरो-शॉट प्रॉम्प्टिंग" width="800"/>

*उदाहरणांशिवाय थेट सूचना — मॉडेल केवळ सूचनेवरून कार्य समजते*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// प्रतिसाद: "सकारात्मक"
```

**कधी वापरावे:** सोप्या वर्गीकरणांसाठी, थेट प्रश्नांसाठी, भाषांतरांसाठी, किंवा कोणत्याही कार्यासाठी ज्यासाठी मॉडेल अधिक मार्गदर्शनाशिवाय हसकते.

### फ्यू-शॉट प्रॉम्प्टिंग

तुम्हाला हवे असलेले नमुने द्या जे मॉडेलला अनुसरायचा नमुना दर्शवतात. मॉडेल तुमच्या उदाहरणांमधून अपेक्षित इनपुट-आउटपुट फॉरमॅट शिकते आणि ते नवीन इनपुट्सवर लागू करते. यामुळे अशा कार्यांसाठी सुसंगतता लक्षात येऊ शकते जिथे अपेक्षित स्वरूप किंवा वर्तन स्पष्ट नसते.

<img src="../../../translated_images/mr/few-shot-prompting.9d9eace1da88989a.webp" alt="फ्यू-शॉट प्रॉम्प्टिंग" width="800"/>

*उदाहरणांकडून शिकणे — मॉडेल नमुना ओळखते आणि नवीन इनपुट्सवर लागू करते*

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

**कधी वापरावे:** सानुकूल वर्गीकरणांसाठी, सुसंगत फॉरमॅटिंगसाठी, डोमेन-विशिष्ट कार्यांसाठी, किंवा जेव्हा झीरो-शॉट परिणाम विसंगत असतात.

### चेन ऑफ थॉट

मॉडेलला त्याच्या विचारप्रक्रियेचा टप्प्या-टप्प्याने दाखवायला सांगा. थेट उत्तर देण्याऐवजी, मॉडेल सांगितलेल्या समस्या तुकड्यांमध्ये विभागते आणि स्पष्टपणे प्रत्येक भागावर काम करते. यामुळे गणित, तार्किक, आणि मल्टी-स्टेप कारणे वाढते.

<img src="../../../translated_images/mr/chain-of-thought.5cff6630e2657e2a.webp" alt="चेन ऑफ थॉट प्रॉम्प्टिंग" width="800"/>

*टप्प्या-टप्प्याने कारणे — गुंतागुंतीच्या समस्यांचा स्पष्ट तार्किक विभाग*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// मॉडेल दर्शविते: १५ - ८ = ७, नंतर ७ + १२ = १९ सफरचंद
```

**कधी वापरावे:** गणित समस्या, तार्किक गूढ गोष्टी, डीबगिंग, किंवा कोणत्याही कार्यासाठी जिथे कारण प्रक्रिया दाखवणे अचूकता आणि विश्वास वाढवते.

### भूमिका-आधारित प्रॉम्प्टिंग

प्रश्न विचारण्यापूर्वी AI साठी एक व्यक्तिमत्व किंवा भूमिका सेट करा. हे उत्तराच्या टोन, खोली, आणि फोकस मध्ये संदर्भ प्रदान करते. "सॉफ्टवेअर आर्किटेक्ट" वेगळ्या सल्ला देतो, "जुनियर डेव्हलपर" किंवा "सुरक्षा ऑडिटर" यांना वेगळा.

<img src="../../../translated_images/mr/role-based-prompting.a806e1a73de6e3a4.webp" alt="भूमिका-आधारित प्रॉम्प्टिंग" width="800"/>

*संदर्भ आणि भूमिका सेट करणे — त्याच प्रश्नासाठी भूमिका नुसार वेगळे उत्तर*

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

**कधी वापरावे:** कोड पुनरावलोकने, ट्युटरिंग, डोमेन-विशिष्ट विश्लेषण, किंवा जेथे तुम्हाला विशिष्ट तज्ञतेच्या स्तरानुसार किंवा दृष्टिकोनानुसार उत्तर हवे आहे.

### प्रॉम्प्ट टेम्पलेट्स

चल बिंदू (वेरिएबल) जागा घेणारे पुनर्वापर करता येण्याजोगे प्रॉम्प्ट तयार करा. प्रत्येक वेळी नवीन प्रॉम्प्ट लिहिण्याऐवजी, एकदा टेम्प्लेट बनवा आणि त्यात वेगवेगळ्या मूल्ये भरा. LangChain4j चा `PromptTemplate` वर्ग `{{variable}}` सिंटॅक्ससह हे सहज करतो.

<img src="../../../translated_images/mr/prompt-templates.14bfc37d45f1a933.webp" alt="प्रॉम्प्ट टेम्पलेट्स" width="800"/>

*चल जागा घेणार्‍या पुनर्वापरयोग्य प्रॉम्प्ट्स — एक टेम्पलेट, अनेक वापर*

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

**कधी वापरावे:** वेगळ्या इनपुटसह पुनरावृत्ती प्रश्नांसाठी, बॅच प्रोसेसिंगसाठी, पुनर्वापर योग्या AI कार्यप्रवाहांसाठी, किंवा अशा परिस्थितीसाठी जिथे प्रॉम्प्ट रचना तशीच राहते पण डेटा बदलतो.

---

ही पाच मूलतत्त्वे तुमच्यासाठी बहुतांश प्रॉम्प्टिंग कार्यांसाठी एक ठोस साधनसामग्री देतात. उर्वरीत हा मॉड्यूल आठ **उच्चस्तरीय नमुन्यांवर** आधारित आहे जे GPT-5.2 च्या कारण नियंत्रण, स्व-मूल्यांकन आणि स्ट्रक्चर्ड आउटपुट क्षमता वापरतात.

## अत्याधुनिक नमुने

मूलतत्त्वे समजून झाल्यावर, चला या मॉड्यूलला खास बनवणारे आठ उच्चस्तरीय नमुने पाहू. सर्व समस्यांना एकसारखा दृष्टीकोन आवश्यक नाही. काही प्रश्नांना जलद उत्तरे पाहिजेत, काहींना खोल विचार करायचा आहे. काहींना दिसणारा विचार हवा, काहींना केवळ परिणाम हवा. खालील प्रत्येक नमुना वेगवेगळ्या परिस्थितीसाठी ऑप्टिमाइझ केलेला आहे — आणि GPT-5.2 चे कारण नियंत्रण या फरकांना आणखी स्पष्ट करतो.

<img src="../../../translated_images/mr/eight-patterns.fa1ebfdf16f71e9a.webp" alt="आठ प्रांप्टिंग नमुन्यांचा आढावा" width="800"/>

*आठ प्रांप्ट अभियांत्रिकी नमुन्यांचा आणि त्यांचा उपयोग*

<img src="../../../translated_images/mr/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 सह कारण नियंत्रण" width="800"/>

*GPT-5.2 चे कारण नियंत्रण तुम्हाला किती विचार करायचा हे सांगू देते — जलद थेट उत्तरांपासून खोल संशोधनापर्यंत*

**कमी विरक्ती (जलद आणि केंद्रित)** - साध्या प्रश्नांसाठी जेथे तुम्हाला जलद, थेट उत्तर हवे आहे. मॉडेल कमी प्रमाणात विचार करतो - जास्तीत जास्त २ टप्पे. याचा वापर गणना, शोध किंवा सोप्या प्रश्नांसाठी करा.

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

> 💡 **GitHub Copilot सह अन्वेषण करा:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) उघडा आणि विचार करा:
> - "कमी विरक्ती आणि उच्च विरक्ती प्रॉम्प्टिंग नमुन्यांमध्ये काय फरक आहे?"
> - "प्रॉम्प्ट्समधील XML टॅग AI चा प्रतिसाद कसा रचतात?"
> - "मी स्व-प्रतिबिंबित पॅटर्न्स आणि थेट सूचनांचा कधी वापर करावा?"

**उच्च विरक्ती (गहन आणि सखोल)** - गुंतागुंतीच्या समस्यांसाठी जेथे तुम्हाला सखोल विश्लेषण हवे. मॉडेल सखोलपणे शोध घेतो आणि तपशीलवार कारणे दाखवतो. याचा वापर सिस्टम डिझाइन, आर्किटेक्चर निर्णय किंवा गुंतागुंतीच्या संशोधनासाठी करा.

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**कार्य अंमलबजावणी (पायरी-भरीत प्रगती)** - मल्टी-स्टेप कार्यप्रवाहांसाठी. मॉडेल एक पूर्व योजना प्रदान करतो, प्रत्येक टप्पा काम करताना सांगतो आणि नंतर सारांश देतो. याचा वापर स्थलांतर, अंमलबजावणी, किंवा कोणत्याही मल्टी-स्टेप प्रक्रियेसाठी करा.

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

चेन-ऑफ-थॉट प्रॉम्प्टिंग मॉडेलला त्याच्या विचार प्रक्रियेचे टप्प्याटप्प्याने प्रदर्शन करण्यासाठी स्पष्टपणे सांगते, ज्यामुळे गुंतागुंतीच्या कार्यांसाठी अचूकता सुधारते. टप्प्याटप्प्याने विभागणे माणूस आणि AI दोघांनाही तर्क समजून घेण्यासाठी मदत करते.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅट सह प्रयत्न करा:** या पॅटर्न बद्दल विचारा:
> - "लांब चलणाऱ्या ऑपरेशन्ससाठी कार्य अंमलबजावणी पॅटर्न कसा वापरावा?"
> - "उत्पादन अ‍ॅप्लिकेशन्समध्ये टूल प्रीअॅंबल्स व्यवस्थित करण्याचे सर्वोत्तम सराव काय आहेत?"
> - "मधले प्रगती अद्यतन UI मध्ये कसे कैद करावे आणि दर्शवावे?"

<img src="../../../translated_images/mr/task-execution-pattern.9da3967750ab5c1e.webp" alt="कार्य अंमलबजावणी नमुना" width="800"/>

*योजना → अंमलबजावणी → सारांश मल्टी-स्टेप कार्यांसाठी कार्यप्रवाह*

**स्व-प्रतिबिंबित कोड** - उत्पादन-गुणवत्तेचा कोड तयार करण्यासाठी. मॉडेल उत्पादन मानकांचे पालन करत कोड तयार करते ज्यामध्ये योग्य त्रुटी हाताळणी असते. नवीन वैशिष्ट्ये किंवा सेवा तयार करताना याचा वापर करा.

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/mr/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="स्व-प्रतिबिंब प्रक्रिया" width="800"/>

*आधी-पुढे सुधारणा सायकल - तयार करा, मूल्यांकन करा, समस्या ओळखा, सुधारणा करा, पुनरावृत्ती करा*

**स्ट्रक्चर्ड विश्लेषण** - सुसंगत मूल्यांकनासाठी. मॉडेल कोडचे पुनरावलोकन ठराविक चौकटीने करतो (योग्यता, सराव, कामगिरी, सुरक्षा, देखभाल क्षमता). कोड पुनरावलोकने किंवा गुणवत्ता मूल्यांकनासाठी वापरा.

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चॅट सह प्रयत्न करा:** स्ट्रक्चर्ड विश्लेषण बद्दल विचारा:
> - "भिन्न प्रकारच्या कोड पुनरावलोकनांसाठी विश्लेषण चौकट कशी सानुकूल करावी?"
> - "स्ट्रक्चर्ड आउटपुट प्रोग्रामॅटिक पद्धतीने पार्स आणि काम कसे करावे?"
> - "भिन्न पुनरावलोकन सत्रांमध्ये सुसंगत गंभीरता पातळी कशी सुनिश्चित करावी?"

<img src="../../../translated_images/mr/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="स्ट्रक्चर्ड विश्लेषण नमुना" width="800"/>

*गंभीरता पातळ्यांसह सुसंगत कोड पुनरावलोकनासाठी चौकट*

**मल्टी-टर्न चॅट** - संदर्भ आवश्यक असलेल्या संवादांसाठी. मॉडेल पूर्वीच्या मेसेज लक्षात ठेवतो आणि त्यावर आधारित संवाद वाढवतो. संवादात्मक मदत सत्रे किंवा गुंतागुंतीच्या प्रश्नोत्तरेसाठी वापरा.

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

*संवाद संदर्भ कसा एकत्रित होतो अनेक टर्नपर्यंत तो टोकन मर्यादेपर्यंत पोहोचतो*

**पायरी-भरीत कारणे** - दृश्यमान तर्क आवश्यक समस्यांसाठी. मॉडेल प्रत्येक टप्प्यासाठी स्पष्ट कारणे दाखवतो. गणित समस्या, तार्किक गूढ, किंवा विचार प्रक्रिया समजून घेण्यासाठी वापरा.

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/mr/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="पायरी-भरीत नमुना" width="800"/>

*समस्यांचे स्पष्ट तार्किक टप्प्यात विभाग*

**मर्यादित आउटपुट** - विशिष्ट फॉरमॅट आवश्यक असलेल्या प्रतिसादांसाठी. मॉडेल कडकपणे फॉरमॅट आणि लांबी नियमांचे पालन करतो. सारांशांसाठी किंवा नेमके आउटपुट संरचना आवश्यक असलेल्यासाठी वापरा.

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

<img src="../../../translated_images/mr/constrained-output-pattern.0ce39a682a6795c2.webp" alt="मर्यादित आउटपुट नमुना" width="800"/>

*विशिष्ट फॉरमॅट, लांबाई आणि रचना नियमांची अंमलबजावणी*

## विद्यमान Azure संसाधने वापरणे

**तैनातपण तपासा:**

प्रमुख निर्देशिकेत `.env` फाइल आहे याची खात्री करा ज्यामध्ये Azure प्रमाणपत्रे आहेत (मॉड्यूल 01 मध्ये तयार केलेली):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दाखवायला हवे
```

**अ‍ॅप्लिकेशन प्रारंभ करा:**

> **टिप:** जर तुम्ही आधीच सर्व अ‍ॅप्लिकेशन्स मॉड्यूल 01 मधील `./start-all.sh` कमांडने सुरू केले असतील, तर हा मॉड्यूल आधीच पोर्ट 8083 वर चालू आहे. खालील प्रारंभ आदेश वगळता थेट http://localhost:8083 वर जा.

**पर्याय 1: स्प्रिंग बूट डॅशबोर्ड वापरणे (VS Code वापरकर्त्यांसाठी शिफारस)**

डेव्ह कंटेनरमध्ये स्प्रिंग बूट डॅशबोर्ड विस्तार समाविष्ट आहे, जो सर्व स्प्रिंग बूट अ‍ॅप्लिकेशन्स व्यवस्थापित करण्यासाठी व्हिज्युअल इंटरफेस प्रदान करतो. तुम्हाला ते VS Code च्या डाव्या बाजूच्या अ‍ॅक्टिव्हिटी बारमध्ये (स्प्रिंग बूट चिन्ह शोधा) मिळेल.

स्प्रिंग बूट डॅशबोर्डमधून तुम्ही:
- कार्यक्षेत्रातील उपलब्ध सर्व स्प्रिंग बूट अ‍ॅप्लिकेशन्स पाहू शकता
- एक क्लिकने अ‍ॅप्लिकेशन्स सुरू/थांबवू शकता
- अ‍ॅप्लिकेशन लॉग्स रिअल-टाइममध्ये पाहू शकता
- अ‍ॅप्लिकेशन स्थितीवर लक्ष ठेवू शकता
"prompt-engineering" च्या जवळील प्ले बटणावर क्लिक करून हा मॉड्यूल सुरू करा, किंवा सर्व मॉड्यूल एकावेळी सुरू करा.

<img src="../../../translated_images/mr/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**पर्याय 2: शेल स्क्रिप्ट वापरून**

सर्व वेब अनुप्रयोग सुरू करा (मॉड्यूल 01-04):

**Bash:**
```bash
cd ..  # रूट निर्देशिका पासून
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # मुळ निर्देशिकेतून
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

दोन्ही स्क्रिप्ट स्वयंचलितपणे रूट `.env` फाइलमधील पर्यावरणीय चल लोड करतात आणि जर JARs अस्तित्वात नसतील तर त्यांना तयार करतात.

> **टीप:** जर तुम्हाला सुरुवात करण्यापूर्वी सर्व मॉड्यूल मॅन्युअली तयार करायचे असतील:
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

तुमच्या ब्राउझरमध्ये http://localhost:8083 उघडा.

**थांबवण्यासाठी:**

**Bash:**
```bash
./stop.sh  # हा फक्त मॉड्युल
# किंवा
cd .. && ./stop-all.sh  # सर्व मॉड्युल्स
```

**PowerShell:**
```powershell
.\stop.ps1  # हा फक्त मॉड्यूल
# किंवा
cd ..; .\stop-all.ps1  # सर्व मॉड्यूल्स
```

## अनुप्रयोगाचे स्क्रीनशॉट

<img src="../../../translated_images/mr/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*सर्व 8 प्रॉम्प्ट इंजिनिअरिंग पॅटर्न, त्यांची वैशिष्ट्ये आणि वापरकेस दाखवणारे मुख्य डॅशबोर्ड*

## पॅटर्न्सचा अन्वेषण

वेब इंटरफेस तुम्हाला वेगवेगळ्या प्रॉम्प्टिंग स्ट्रॅटेजींसोबत प्रयोग करण्याची परवानगी देतो. प्रत्येक पॅटर्न वेगळ्या समस्या सोडवतो - त्यांचा वापर करून पाहा आणि जाणून घ्या की कोणता दृष्टिकोन कधी प्रभावी ठरतो.

### कमी आणि जास्त उत्साह

"200च्या 15% किती?" असा साधा प्रश्न कमी उत्साहाने विचारा. तुम्हाला त्वरीत, थेट उत्तर मिळेल. आता "उच्च-ट्रॅफिक API साठी कॅशिंग धोरण डिझाइन करा" असा जास्त उत्साह वापरून गुंतागुंतीचा प्रश्न विचारा. मॉडेल कसे मंदावते आणि सविस्तर स्पष्टीकरण देते ते पहा. तोच मॉडेल, तोच प्रश्न रचना - पण प्रॉम्प्ट मॉडेलला किती विचार करायचा हे सांगतो.

<img src="../../../translated_images/mr/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*कमी विचारसरणीसह जलद गणना*

<img src="../../../translated_images/mr/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*सविस्तर कॅशिंग धोरण (2.8MB)*

### कार्य अंमलबजावणी (टूल प्रीअँबल्स)

मल्टी-स्टेप वर्कफ्लोजना आधीच नियोजन आणि प्रगतीचे वर्णन लागते. मॉडेल काय करेल हे ठरवते, प्रत्येक टप्प्याचे वर्णन करते, नंतर परिणामांचा सारांश देते.

<img src="../../../translated_images/mr/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*स्टेप-बाय-स्टेप वर्णनासह REST एंडपॉइंट तयार करणे (3.9MB)*

### स्व-परावर्तित कोड

"ईमेल व्हॅलिडेशन सेवा तयार करा" असा प्रयत्न करा. फक्त कोड तयार करून थांबण्याऐवजी, मॉडेल तयार करते, गुणवत्ता निकषांनुसार मूल्यांकन करते, कमकुवत भाग ओळखते आणि सुधारणा करते. तुम्हाला दिसेल ते कोड उत्पादन मानकांपर्यंत पोहोचले की तो किती वेळा पुनरावृत्ती करतो.

<img src="../../../translated_images/mr/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*पूर्ण ईमेल व्हॅलिडेशन सेवा (5.2MB)*

### संरचित विश्लेषण

कोड पुनरावलोकनासाठी सातत्यपूर्ण मूल्यांकन फ्रेमवर्क आवश्यक आहे. मॉडेल निश्चित श्रेणींमध्ये (बरोबरी, पद्धती, कामगिरी, सुरक्षा) आणि तीव्रतेच्या स्तरांनी कोडचे विश्लेषण करते.

<img src="../../../translated_images/mr/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*फ्रेमवर्क-आधारित कोड पुनरावलोकन*

### बहुपट संभाषण

"Spring Boot म्हणजे काय?" असा प्रश्न विचारा आणि लगेच "मला एक उदाहरण दाखवा" असे विचार करा. मॉडेल तुमचा पहिला प्रश्न लक्षात ठेवते आणि विशेषतः Spring Boot उदाहरण देते. जर लक्ष राहिले नाही तर दुसरा प्रश्न फारसा स्पष्ट नसता.

<img src="../../../translated_images/mr/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*प्रश्नांमध्ये संदर्भ जपणे*

### टप्प्याटप्प्याने विचारसरणी

कोणतीही गणिताची समस्या निवडा आणि दोन्ही Step-by-Step Reasoning आणि Low Eagerness वापरून पाहा. कमी उत्साह फक्त जलद उत्तर देतो - पण अस्पष्ट. टप्प्याटप्प्याने तुम्हाला प्रत्येक गणना आणि निर्णय दाखवतो.

<img src="../../../translated_images/mr/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*स्पष्ट टप्प्यांसह गणित समस्या*

### बंधनकारक आउटपुट

जेव्हा तुमच्याकडे विशिष्ट स्वरुप किंवा शब्दसंख्या हवी असेल, तेव्हा हा पॅटर्न कठोरपणे पालन करतो. बुलेट पॉईंट स्वरुपात अचूक 100 शब्दांची सारांश तयार करणे प्रयत्न करा.

<img src="../../../translated_images/mr/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*स्वरूप नियंत्रित मशीन लर्निंग सारांश*

## तुम्ही प्रत्यक्ष काय शिकत आहात

**विचारण्याचा प्रयत्न सर्व काही बदलतो**

GPT-5.2 तुम्हाला प्रॉम्प्टद्वारे संगणकीय प्रयत्न नियंत्रित करण्याची परवानगी देतो. कमी प्रयत्न म्हणजे जलद प्रतिसाद आणि कमी अन्वेषण. जास्त प्रयत्न म्हणजे मॉडेल वेळ घेऊन सखोल विचार करते. तुम्ही शिकत आहात की कधी किती प्रयत्न करायचे - सोपे प्रश्न झटपट करा, पण गुंतागुंतीच्या निर्णयांमध्ये घाई करू नका.

**रचना वर्तनास मार्गदर्शित करते**

प्रॉम्प्टमधील XML टॅग्स लक्षात घेत आहेत का? ते फक्त सजावटीसाठी नाहीत. मॉडेल रचनाबद्ध सूचनांचे पालन फ्रीफॉर्म टेक्स्ट पेक्षा अधिक विश्वासार्हपणे करते. जेव्हा तुम्हाला मल्टी-स्टेप प्रक्रिया किंवा गुंतागुंतीचा लॉजिक हवा असतो, तेव्हा रचना मॉडेलला त्याची स्थिती आणि पुढे काय आहे हे ट्रॅक करण्यात मदत करते.

<img src="../../../translated_images/mr/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*स्पष्ट विभाग आणि XML-शैली संघटनासह चांगल्या रचनाबद्ध प्रॉम्प्टची रचना*

**स्व-मूल्यांकनाद्वारे गुणवत्ता**

स्व-परावर्तित पॅटर्न गुणवत्ता निकष स्पष्ट करून कार्य करतात. "मॉडेल योग्य करते" अशी अपेक्षा ठेवण्याऐवजी, तुम्ही त्याला बरोबर म्हणजे काय हे सांगता: बरोबर लॉजिक, त्रुटी हाताळणी, कामगिरी, सुरक्षा. त्यानंतर मॉडेल स्वतःच्या आउटपुटचे मूल्यांकन करून सुधारणा करू शकते. यामुळे कोड निर्मिती एक प्रक्रिया होते, ज्या आधी लॉटरीसारखी होती.

**संदर्भ अमर्यादित नाही**

मल्टी-टर्न संभाषणे प्रत्येक विनंतीसह संदेश इतिहास समाविष्ट करून कार्य करतात. पण मर्यादा आहे - प्रत्येक मॉडेलला टोकनची कमाल संख्या असते. संभाषणे वाढत गेल्यावर, सुसंगत संदर्भ राखण्यासाठी तुम्हाला रणनीती हवी असते ज्यामुळे मर्यादा पूर्ण होत नाही. हा मॉड्यूल तुम्हाला स्मृती कशी कार्य करते हे दाखवतो; नंतर तुम्ही कधी सारांश करायचा, कधी विसरायचे, आणि कधी पुनर्प्राप्त करायचे हे शिकलात.

## पुढील पाऊले

**पुढील मॉड्यूल:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**नेव्हिगेशन:** [← मागील: मॉड्यूल 01 - परिचय](../01-introduction/README.md) | [मुख्य पानावर परत जा](../README.md) | [पुढे: मॉड्यूल 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून भाषांतरित करण्यात आला आहे. आम्ही अचूकतेसाठी प्रयत्न करतो, तरी कृपया लक्षात घ्या की स्वयंचलित भाषांतरे चुका किंवा अचूकतेत त्रुटी असू शकतात. मूळ दस्तऐवज त्याच्या स्थानिक भाषेत अधिकारपूर्वक स्रोत मानला पाहिजे. महत्त्वाच्या माहितींसाठी व्यावसायिक मानवी भाषांतर करण्याचा सल्ला दिला जातो. या भाषांतराचा वापर केल्यामुळे झालेल्या कोणत्याही गैरसमजुतीसाठी किंवा चुकीच्या अर्थसारणेसाठी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->