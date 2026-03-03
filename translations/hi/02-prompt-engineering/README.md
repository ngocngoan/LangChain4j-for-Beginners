# मॉड्यूल 02: GPT-5.2 के साथ प्रॉम्प्ट इंजिनीयरिंग

## सामग्री सूची

- [वीडियो वॉकथ्रू](../../../02-prompt-engineering)
- [आप क्या सीखेंगे](../../../02-prompt-engineering)
- [पूर्वापेक्षाएँ](../../../02-prompt-engineering)
- [प्रॉम्प्ट इंजिनीयरिंग को समझना](../../../02-prompt-engineering)
- [प्रॉम्प्ट इंजिनीयरिंग के मूल सिद्धांत](../../../02-prompt-engineering)
  - [जीरो-शॉट प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [फ्यू-शॉट प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [चेन ऑफ थॉट](../../../02-prompt-engineering)
  - [रोल-आधारित प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [प्रॉम्प्ट टेम्पलेट्स](../../../02-prompt-engineering)
- [उन्नत पैटर्न](../../../02-prompt-engineering)
- [एप्लिकेशन चलाएँ](../../../02-prompt-engineering)
- [एप्लिकेशन के स्क्रीनशॉट](../../../02-prompt-engineering)
- [पैटर्न्स का अन्वेषण](../../../02-prompt-engineering)
  - [कम बनाम अधिक उत्सुकता](../../../02-prompt-engineering)
  - [टास्क निष्पादन (टूल प्रीऐम्बल्स)](../../../02-prompt-engineering)
  - [स्वयं-प्रतिबिंबित कोड](../../../02-prompt-engineering)
  - [संरचित विश्लेषण](../../../02-prompt-engineering)
  - [मल्टी-टर्न चैट](../../../02-prompt-engineering)
  - [चरण-दर-चरण तर्क](../../../02-prompt-engineering)
  - [सीमित आउटपुट](../../../02-prompt-engineering)
- [आप वास्तव में क्या सीख रहे हैं](../../../02-prompt-engineering)
- [अगले कदम](../../../02-prompt-engineering)

## वीडियो वॉकथ्रू

इस लाइव सेशन को देखें जो इस मॉड्यूल के साथ शुरू करने का तरीका समझाता है:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## आप क्या सीखेंगे

निम्नलिखित चित्र इस मॉड्यूल में आप जिन मुख्य विषयों और कौशलों को विकसित करेंगे उनका अवलोकन प्रदान करता है — प्रॉम्प्ट परिष्करण तकनीकों से लेकर उस चरण-दर-चरण वर्कफ़्लो तक जिसे आप पालन करेंगे।

<img src="../../../translated_images/hi/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

पिछले मॉड्यूल में, आपने GitHub Models के साथ LangChain4j के बुनियादी इंटरैक्शंस और देखा कि कैसे मेमोरी Azure OpenAI के साथ संवादात्मक AI को सक्षम बनाती है। अब हम ध्यान केंद्रित करेंगे कि आप प्रश्न कैसे पूछते हैं — यानि प्रॉम्प्ट्स स्वयं — Azure OpenAI के GPT-5.2 का उपयोग करते हुए। जिस तरह से आप अपने प्रॉम्प्ट्स को संरचित करते हैं, वह आपके प्राप्त उत्तरों की गुणवत्ता को नाटकीय रूप से प्रभावित करता है। हम मूलभूत प्रॉम्प्टिंग तकनीकों की समीक्षा से शुरू करेंगे, फिर आठ उन्नत पैटर्न की ओर बढ़ेंगे जो GPT-5.2 की क्षमताओं का पूरा लाभ उठाते हैं।

हम GPT-5.2 का उपयोग करेंगे क्योंकि यह तर्क नियंत्रण पेश करता है - आप मॉडल को बता सकते हैं कि जवाब देने से पहले कितना सोचना है। यह विभिन्न प्रॉम्प्टिंग रणनीतियों को अधिक स्पष्ट बनाता है और आपको समझने में मदद करता है कि किस स्थिति में कौन-सी विधि का उपयोग करना है। साथ ही, Azure की GPT-5.2 के लिए कम रेट लिमिट्स GitHub Models के मुकाबले हमें लाभ पहुंचाएंगी।

## पूर्वापेक्षाएँ

- मॉड्यूल 01 पूरा किया हो (Azure OpenAI संसाधन तैनात किए गए हों)
- `.env` फाइल रूट डायरेक्टरी में हो जिसमें Azure क्रेडेंशियल्स हों (`azd up` द्वारा मॉड्यूल 01 में बनाई गई)

> **नोट:** यदि आपने मॉड्यूल 01 पूरा नहीं किया है, तो पहले वहाँ के तैनाती निर्देशों का पालन करें।

## प्रॉम्प्ट इंजिनीयरिंग को समझना

मूल रूप से, प्रॉम्प्ट इंजिनीयरिंग अस्पष्ट निर्देशों और सटीक निर्देशों के बीच का अंतर है, जैसा कि नीचे तुलना में दिखाया गया है।

<img src="../../../translated_images/hi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

प्रॉम्प्ट इंजिनीयरिंग वह प्रक्रिया है जिसमें ऐसा इनपुट टेक्स्ट डिजाइन किया जाता है जो आपको लगातार आवश्यक परिणाम देता है। यह केवल प्रश्न पूछने के बारे में नहीं है — बल्कि यह अनुरोधों को इस प्रकार संरचित करने के बारे में है ताकि मॉडल बिल्कुल समझ सके कि आप क्या चाहते हैं और कैसे प्रदान करना है।

इसे एक सहकर्मी को निर्देश देने के रूप में सोचें। "बग ठीक करें" अस्पष्ट है। "UserService.java की लाइन 45 में नल चेक जोड़कर नल पॉइंटर एक्सेप्शन ठीक करें" विशिष्ट है। भाषा मॉडल भी इसी तरह काम करते हैं — विशिष्टता और संरचना महत्वपूर्ण होती है।

नीचे का चित्र दिखाता है कि LangChain4j इस परिदृश्य में कैसे फिट बैठता है — आपके प्रॉम्प्ट पैटर्न को SystemMessage और UserMessage बिल्डिंग ब्लॉक्स के माध्यम से मॉडल से जोड़ता है।

<img src="../../../translated_images/hi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j आधारभूत संरचना प्रदान करता है — मॉडल कनेक्शंस, मेमोरी, और मैसेज प्रकार — जबकि प्रॉम्प्ट पैटर्न वे सावधानीपूर्वक संरचित टेक्स्ट हैं जिन्हें आप उस आधारभूत संरचना के माध्यम से भेजते हैं। मुख्य बिल्डिंग ब्लॉक हैं `SystemMessage` (जो AI के व्यवहार और भूमिका को सेट करता है) और `UserMessage` (जो आपका वास्तविक अनुरोध लेकर आता है)।

## प्रॉम्प्ट इंजिनीयरिंग के मूल सिद्धांत

नीचे दिखाए गए पांच मुख्य तकनीकें प्रभावी प्रॉम्प्ट इंजिनीयरिंग की नींव हैं। प्रत्येक एक अलग पहलू को संबोधित करता है कि आप भाषा मॉडल के साथ कैसे संवाद करते हैं।

<img src="../../../translated_images/hi/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

इस मॉड्यूल में उन्नत पैटर्न्स में जाने से पहले, आइए पांच मौलिक प्रॉम्प्टिंग तकनीकों की समीक्षा करें। ये वे बिल्डिंग ब्लॉक्स हैं जिन्हें हर प्रॉम्प्ट इंजीनियर को जानना चाहिए। यदि आपने पहले ही [क्विक स्टार्ट मॉड्यूल](../00-quick-start/README.md#2-prompt-patterns) पूरा किया है, तो आपने इन्हें क्रियान्वित होते देखा होगा — यहाँ इनके पीछे का सैद्धांतिक ढांचा है।

### जीरो-शॉट प्रॉम्प्टिंग

सबसे सरल तरीका: मॉडल को कोई उदाहरण दिए बिना सीधे निर्देश दें। मॉडल पूरी तरह से अपने प्रशिक्षण पर निर्भर करता है ताकि वह कार्य समझे और निष्पादित करे। यह सरल अनुरोधों के लिए अच्छा काम करता है जहाँ अपेक्षित व्यवहार स्पष्ट होता है।

<img src="../../../translated_images/hi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*उदाहरण बिना सीधे निर्देश — मॉडल केवल निर्देश से कार्य का अनुमान लगाता है*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// प्रतिक्रिया: "सकारात्मक"
```

**कब उपयोग करें:** सरल वर्गीकरण, सीधे प्रश्न, अनुवाद, या कोई भी कार्य जिसे मॉडल बिना अतिरिक्त मार्गदर्शन के संभाल सकता है।

### फ्यू-शॉट प्रॉम्प्टिंग

उदाहरण प्रदान करें जो उस पैटर्न को प्रदर्शित करते हैं जिसे आप मॉडल से लागू करना चाहते हैं। मॉडल आपके उदाहरणों से अपेक्षित इनपुट-आउटपुट प्रारूप सीखता है और इसे नए इनपुट्स पर लागू करता है। इसका उपयोग उन कार्यों के लिए करें जहाँ वांछित प्रारूप या व्यवहार स्पष्ट नहीं है और निरंतरता महत्वपूर्ण है।

<img src="../../../translated_images/hi/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*उदाहरणों से सीखना — मॉडल पैटर्न पहचानता है और नए इनपुट्स पर लागू करता है*

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

**कब उपयोग करें:** कस्टम वर्गीकरण, सुसंगत प्रारूपण, डोमेन-विशिष्ट कार्य, या जब जीरो-शॉट परिणाम असंगत हों।

### चेन ऑफ थॉट

मॉडल से कहें कि वह अपना तर्क चरण-दर-चरण दिखाए। सीधे उत्तर पर कूदने के बजाय, मॉडल समस्या को टुकड़ों में तोड़ता है और प्रत्येक भाग पर स्पष्ट रूप से काम करता है। इससे गणित, तर्क और बहु-चरण तर्क कार्यों में सटीकता बढ़ती है।

<img src="../../../translated_images/hi/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*चरण-दर-चरण तर्क — जटिल समस्याओं को स्पष्ट तार्किक चरणों में तोड़ना*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// मॉडल दिखाता है: 15 - 8 = 7, फिर 7 + 12 = 19 सेब
```

**कब उपयोग करें:** गणित की समस्याएँ, तर्क पहेलियाँ, डिबगिंग, या कोई भी कार्य जहाँ तर्क प्रक्रिया दिखाने से सटीकता और विश्वास बढ़ता है।

### रोल-आधारित प्रॉम्प्टिंग

एआई के लिए प्रश्न पूछने से पहले कोई व्यक्ति या भूमिका निर्धारित करें। यह संदर्भ प्रदान करता है जो उत्तर के टोन, गहराई और फोकस को आकार देता है। "सॉफ्टवेयर आर्किटेक्ट" का सुझाव "जूनियर डेवलपर" या "सुरक्षा ऑडिटर" से अलग होगा।

<img src="../../../translated_images/hi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*संदर्भ और व्यक्ति सेट करना — एक ही प्रश्न को सौंपे गए भूमिका के अनुसार अलग जवाब मिलता है*

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

**कब उपयोग करें:** कोड समीक्षा, ट्यूटरिंग, डोमेन-विशिष्ट विश्लेषण, या जब अलग विशेषज्ञता स्तर या दृष्टिकोण के लिए उत्तर चाहिए।

### प्रॉम्प्ट टेम्पलेट्स

चर प्लेसहोल्डर्स के साथ पुन: उपयोग योग्य प्रॉम्प्ट बनाएं। हर बार नया प्रॉम्प्ट लिखने के बजाय, एक बार टेम्पलेट परिभाषित करें और अलग-अलग मान भरें। LangChain4j का `PromptTemplate` क्लास इसे `{{variable}}` सिंटैक्स के साथ आसान बनाता है।

<img src="../../../translated_images/hi/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*चर प्लेसहोल्डर्स के साथ पुन: उपयोग योग्य प्रॉम्प्ट — एक टेम्पलेट, कई उपयोग*

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

**कब उपयोग करें:** अलग इनपुट के साथ पुनरावृत्ति प्रश्न, बैच प्रोसेसिंग, पुन: उपयोग योग्य AI वर्कफ़्लोज़ बनाना, या कोई भी परिदृश्य जहाँ प्रॉम्प्ट संरचना समान रहती है पर डेटा बदलता है।

---

ये पाँच मूल बातें आपको अधिकांश प्रॉम्प्टिंग कार्यों के लिए एक मजबूत उपकरण प्रदान करती हैं। इस मॉड्यूल का बाकी हिस्सा इन पर आधारित आठ उन्नत पैटर्न प्रदान करता है जो GPT-5.2 के तर्क नियंत्रण, स्वयं-मূল्यांकन, और संरचित आउटपुट क्षमताओं का उपयोग करते हैं।

## उन्नत पैटर्न

मूलभूत बातें हो जाने के बाद, आइए उन आठ उन्नत पैटर्न्स की ओर बढ़ें जो इस मॉड्यूल को अनूठा बनाते हैं। सभी समस्याओं के लिए एक ही तरीका जरूरी नहीं होता। कुछ प्रश्न त्वरित उत्तर चाहते हैं, कुछ को गहरे सोच-विचार की जरूरत होती है। कुछ को दिखाई देने वाला तर्क चाहिए, और कुछ को केवल परिणाम चाहिए। नीचे प्रत्येक पैटर्न किसी विशिष्ट परिदृश्य के लिए अनुकूलित है — और GPT-5.2 का तर्क नियंत्रण इस भेद को और भी स्पष्ट बनाता है।

<img src="../../../translated_images/hi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*आठ प्रॉम्प्ट इंजिनीयरिंग पैटर्न्स और उनके उपयोग मामलों का अवलोकन*

GPT-5.2 इन पैटर्न्स में एक और आयाम जोड़ता है: *तर्क नियंत्रण*। नीचे स्लाइडर दिखाता है कि आप मॉडल के सोचने के प्रयास को कैसे समायोजित कर सकते हैं — त्वरित, सीधे उत्तरों से लेकर गहरी, व्यापक विश्लेषण तक।

<img src="../../../translated_images/hi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 का तर्क नियंत्रण आपको निर्दिष्ट करने देता है कि मॉडल कितना विचार करे — तेज़ सीधे उत्तर से लेकर गहरे अन्वेषण तक*

**कम उत्सुकता (त्वरित और केंद्रित)** - सरल प्रश्नों के लिए जहाँ आप तेज़, सीधे उत्तर चाहते हैं। मॉडल न्यूनतम तर्क करता है - अधिकतम 2 चरण। गणना, देखups, या सीधे प्रश्नों के लिए इसका उपयोग करें।

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

> 💡 **GitHub Copilot के साथ एक्सप्लोर करें:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) खोलें और पूछें:
> - "कम उत्सुकता और अधिक उत्सुकता प्रॉम्प्टिंग पैटर्न में क्या अंतर है?"
> - "प्रॉम्प्ट्स में XML टैग AI के जवाब को संरचित करने में कैसे मदद करते हैं?"
> - "मुझे स्वयं-प्रतिबिंबित पैटर्न्स कब और सीधे निर्देश कब उपयोग करने चाहिए?"

**अधिक उत्सुकता (गहरा और व्यापक)** - जटिल समस्याओं के लिए जहाँ आप व्यापक विश्लेषण चाहते हैं। मॉडल पूरी तरह से खोज करता है और विस्तृत तर्क दिखाता है। सिस्टम डिज़ाइन, आर्किटेक्चर निर्णय, या जटिल शोध के लिए इसका उपयोग करें।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**टास्क निष्पादन (चरण-दर-चरण प्रगति)** - बहु-चरण वर्कफ़्लो के लिए। मॉडल एक प्रारंभिक योजना प्रदान करता है, हर चरण का वर्णन करता है, फिर सारांश देता है। माइग्रेशन, कार्यान्वयन, या कोई भी बहु-चरण प्रक्रिया के लिए इसका उपयोग करें।

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

चेन-ऑफ-थॉट प्रॉम्प्टिंग स्पष्ट रूप से मॉडल से तर्क प्रक्रिया दिखाने को कहता है, जटिल कार्यों की सटीकता बढ़ाता है। चरण-दर-चरण टूटना मानव और AI दोनों को तार्किक समझ देता है।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ ट्राई करें:** इस पैटर्न के बारे में पूछें:
> - "लंबे समय तक चलने वाले ऑपरेशन्स के लिए टास्क निष्पादन पैटर्न को कैसे अनुकूलित करें?"
> - "प्रोडक्शन एप्लिकेशन में टूल प्रीऐम्बल्स को संरचित करने के श्रेष्ठ अभ्यास क्या हैं?"
> - "UI में मध्यवर्ती प्रगति अपडेट कैसे कैप्चर और प्रदर्शित करें?"

नीचे की आकृति इस योजना → निष्पादन → सारांश वर्कफ़्लो को दर्शाती है।

<img src="../../../translated_images/hi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*बहु-चरण कार्यों के लिए योजना → निष्पादन → सारांश वर्कफ़्लो*

**स्वयं-प्रतिबिंबित कोड** - प्रोडक्शन-गुणवत्ता कोड उत्पन्न करने के लिए। मॉडल प्रोडक्शन मानकों के अनुसार कोड जनरेट करता है जिसमें उचित त्रुटि नियंत्रण होता है। नए फीचर्स या सेवाएँ बनाने के लिए इसका उपयोग करें।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

नीचे की आकृति इस पुनरावृत्त सुधार चक्र को दिखाती है — उत्पन्न करें, मूल्यांकन करें, कमजोरियाँ पहचानें, और सुधार करें जब तक कोड प्रोडक्शन मानकों को पूरा न कर ले।

<img src="../../../translated_images/hi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*पुनरावृत्त सुधार चक्र - उत्पन्न करें, मूल्यांकन करें, मुद्दे पहचानें, सुधार करें, दोहराएं*

**संरचित विश्लेषण** - सुसंगत मूल्यांकन के लिए। मॉडल एक तय फ़्रेमवर्क (सहीपन, प्रथाएँ, प्रदर्शन, सुरक्षा, रखरखाव) का उपयोग करते हुए कोड की समीक्षा करता है। कोड समीक्षा या गुणवत्ता आकलन के लिए उपयोग करें।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ ट्राई करें:** संरचित विश्लेषण के बारे में पूछें:
> - "विभिन्न प्रकार की कोड समीक्षाओं के लिए विश्लेषण फ़्रेमवर्क को कैसे अनुकूलित करें?"
> - "संरचित आउटपुट को प्रोग्रामेटिक रूप से पार्स और क्रियान्वित करने का सर्वोत्तम तरीका क्या है?"
> - "विभिन्न समीक्षा सत्रों में निरंतर गंभीरता स्तर कैसे सुनिश्चित करें?"

नीचे की आकृति दिखाती है कि कैसे यह संरचित फ़्रेमवर्क कोड समीक्षा को निरंतर श्रेणियों में गंभीरता स्तर के साथ व्यवस्थित करता है।

<img src="../../../translated_images/hi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*गंभीरता स्तर के साथ सुसंगत कोड समीक्षा के लिए फ़्रेमवर्क*

**मल्टी-टर्न चैट** - वार्तालाप के लिए जिसमें संदर्भ की आवश्यकता हो। मॉडल पहले के संदेश याद रखता है और उन पर आधारित प्रतिक्रियाएँ बनाता है। इंटरैक्टिव सहायता सत्र या जटिल प्रश्नोत्तर के लिए उपयोग करें।

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

नीचे की आकृति दिखाती है कि कैसे वार्तालाप संदर्भ कई टर्नों के दौरान जमा होता है और यह मॉडल के टोकन सीमा से कैसे संबंधित है।

<img src="../../../translated_images/hi/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*कैसे वार्तालाप संदर्भ कई टर्नों में जमा होता है जब तक कि टोकन सीमा पहुँच न जाए*
**स्टेप-बाय-स्टेप तर्क** - ऐसे समस्याओं के लिए जहाँ स्पष्ट तर्क आवश्यक होता है। मॉडल प्रत्येक चरण के लिए स्पष्ट तर्क प्रस्तुत करता है। इसे गणित की समस्याओं, तर्क पहेलियों या तब उपयोग करें जब आपको सोचने की प्रक्रिया समझनी हो।

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

नीचे दिया गया चित्र दिखाता है कि कैसे मॉडल समस्याओं को स्पष्ट, क्रमांकित तर्कात्मक चरणों में विभाजित करता है।

<img src="../../../translated_images/hi/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*समस्याओं को स्पष्ट तर्कात्मक चरणों में विभाजित करना*

**बाध्य आउटपुट** - ऐसे उत्तरों के लिए जिनमें विशिष्ट स्वरूप आवश्यकताएँ होती हैं। मॉडल सख्ती से स्वरूप और लंबाई नियमों का पालन करता है। इसे सारांशों या जब आपको सटीक आउटपुट संरचना चाहिए तब उपयोग करें।

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

निम्नलिखित चित्र दिखाता है कि कैसे बाधाएं मॉडल को सख्ती से आपके स्वरूप और लंबाई आवश्यकताओं के अनुरूप आउटपुट उत्पन्न करने के लिए मार्गदर्शन करती हैं।

<img src="../../../translated_images/hi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*विशिष्ट स्वरूप, लंबाई और संरचना आवश्यकताओं को लागू करना*

## एप्लिकेशन चलाएँ

**डिप्लॉयमेंट सत्यापित करें:**

सुनिश्चित करें कि `.env` फ़ाइल रूट डायरेक्टरी में मौजूद है जिसमें Azure क्रेडेंशियल्स हैं (Module 01 के दौरान बनाया गया)। इसे मॉड्यूल डायरेक्टरी (`02-prompt-engineering/`) से चलाएं:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दिखाने चाहिए
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दिखाना चाहिए
```

**एप्लिकेशन शुरू करें:**

> **नोट:** यदि आपने पहले ही रूट डायरेक्टरी से `./start-all.sh` का उपयोग करके सभी एप्लिकेशन शुरू कर दिए हैं (जैसा कि Module 01 में वर्णित है), तो यह मॉड्यूल पोर्ट 8083 पर पहले से चल रहा है। आप नीचे दिए गए स्टार्ट कमांड छोड़ सकते हैं और सीधे http://localhost:8083 पर जा सकते हैं।

**ऑप्शन 1: Spring Boot डैशबोर्ड का उपयोग करना (VS Code उपयोगकर्ताओं के लिए अनुशंसित)**

डेव कंटेनर में Spring Boot डैशबोर्ड एक्सटेंशन शामिल है, जो सभी Spring Boot एप्लिकेशन को प्रबंधित करने के लिए एक दृश्य इंटरफ़ेस प्रदान करता है। आप इसे VS Code के Activity Bar के बाईं ओर देख सकते हैं (Spring Boot आइकन देखें)।

Spring Boot डैशबोर्ड से आप:
- वर्कस्पेस में सभी उपलब्ध Spring Boot एप्लिकेशन देख सकते हैं
- एक क्लिक से एप्लिकेशन शुरू/रोक सकते हैं
- वास्तविक समय में एप्लिकेशन के लॉग देख सकते हैं
- एप्लिकेशन की स्थिति मॉनिटर कर सकते हैं

"prompt-engineering" के बगल में प्ले बटन क्लिक करके इस मॉड्यूल को शुरू करें, या सभी मॉड्यूल एक साथ शुरू करें।

<img src="../../../translated_images/hi/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code में Spring Boot डैशबोर्ड—सभी मॉड्यूल एक ही जगह से शुरू, रोकें और मॉनिटर करें*

**ऑप्शन 2: शेल स्क्रिप्ट का उपयोग करना**

सभी वेब एप्लिकेशन शुरू करें (मॉड्यूल 01-04):

**Bash:**
```bash
cd ..  # रूट निर्देशिका से
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # रूट निर्देशिका से
.\start-all.ps1
```

या केवल इस मॉड्यूल को शुरू करें:

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

दोनों स्क्रिप्ट रूट `.env` फ़ाइल से पर्यावरण चर स्वचालित रूप से लोड करती हैं और यदि JAR फ़ाइलें मौजूद नहीं हैं तो उन्हें बनाएंगी।

> **नोट:** यदि आप शुरू करने से पहले सभी मॉड्यूल मैन्युअली बिल्ड करना पसंद करते हैं:
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

अपने ब्राउज़र में http://localhost:8083 खोलें।

**रोकने के लिए:**

**Bash:**
```bash
./stop.sh  # केवल यह मॉड्यूल
# या
cd .. && ./stop-all.sh  # सभी मॉड्यूल
```

**PowerShell:**
```powershell
.\stop.ps1  # यह मॉड्यूल केवल
# या
cd ..; .\stop-all.ps1  # सभी मॉड्यूल्स
```

## एप्लिकेशन स्क्रीनशॉट्स

यहां प्रॉम्प्ट इंजीनियरिंग मॉड्यूल का मुख्य इंटरफ़ेस है, जहाँ आप आठों पैटर्न एक साथ प्रयोग कर सकते हैं।

<img src="../../../translated_images/hi/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*मुख्य डैशबोर्ड जो सभी 8 प्रॉम्प्ट इंजीनियरिंग पैटर्न उनके लक्षणों और उपयोग मामलों के साथ दिखाता है*

## पैटर्न्स की खोज

वेब इंटरफ़ेस आपको विभिन्न प्रॉम्प्टिंग रणनीतियों के साथ प्रयोग करने देता है। प्रत्येक पैटर्न अलग-अलग समस्याओं को हल करता है - देखें कि कौन सा पैटर्न कब कार्य करता है।

> **नोट: स्ट्रीमिंग बनाम नॉन-स्ट्रीमिंग** — प्रत्येक पैटर्न पेज पर दो बटन हैं: **🔴 स्ट्रीम रिस्पांस (लाइव)** और एक **नॉन-स्ट्रीमिंग** विकल्प। स्ट्रीमिंग Server-Sent Events (SSE) का उपयोग करता है ताकि मॉडल द्वारा जेनरेट किए जा रहे टोकन वास्तविक समय में दिखाई दें, इसलिए आप तुरंत प्रगति देख सकते हैं। नॉन-स्ट्रीमिंग विकल्प पूरी प्रतिक्रिया आने तक प्रतीक्षा करता है। गहरे तर्क वाले प्रॉम्प्ट्स (जैसे High Eagerness, Self-Reflecting Code) के लिए नॉन-स्ट्रीमिंग कॉल बहुत लंबा समय ले सकता है — कभी-कभी मिनटों तक — बिना कोई दृश्य प्रतिक्रिया। **जटिल प्रॉम्प्ट्स के साथ प्रयोग करते समय स्ट्रीमिंग का उपयोग करें** ताकि आप मॉडल को काम करते देखें और ऐसा न लगे कि अनुरोध टाइम आउट हो गया हो।
>
> **नोट: ब्राउज़र आवश्यकता** — स्ट्रीमिंग फीचर Fetch Streams API (`response.body.getReader()`) का उपयोग करता है, जो पूर्ण ब्राउज़र (Chrome, Edge, Firefox, Safari) में ही कार्य करता है। यह VS Code के इन-बिल्ट Simple Browser में काम नहीं करता क्योंकि उसका वेबव्यू ReadableStream API को सपोर्ट नहीं करता। यदि आप Simple Browser का उपयोग करते हैं, तो नॉन-स्ट्रीमिंग बटन सामान्य रूप से काम करेंगे — केवल स्ट्रीमिंग बटन प्रभावित होंगे। पूर्ण अनुभव के लिए बाहरी ब्राउज़र में `http://localhost:8083` खोलें।

### कम बनाम अधिक उत्सुकता

"15% of 200 क्या है?" जैसे सरल प्रश्न Low Eagerness के साथ पूछें। आपको तुरंत, सीधे उत्तर मिलेगा। अब "उच्च-ट्रैफ़िक API के लिए कैशिंग रणनीति डिज़ाइन करें" जैसा जटिल सवाल High Eagerness के साथ पूछें। **🔴 स्ट्रीम रिस्पांस (लाइव)** क्लिक करें और देखें कि मॉडल कैसे टोकन-दर-टोकन विस्तृत तर्क प्रस्तुत करता है। समान मॉडल, समान प्रश्न संरचना - लेकिन प्रॉम्प्ट बताता है कि कितना सोच-विचार करना है।

### कार्य निष्पादन (टूल प्रीएम्बल्स)

मल्टी-स्टेप वर्कफ़्लोज पहले से योजना और प्रगति narration से लाभ उठाते हैं। मॉडल बताता है कि वह क्या करेगा, हर चरण का वर्णन करता है, फिर परिणाम सारांशित करता है।

### स्व-प्रतिबिंबित कोड

"एक ईमेल सत्यापन सेवा बनाएं" आज़माएं। केवल कोड जेनरेट करने और रुकने के बजाय, मॉडल जेनरेट करता है, गुणवत्ता मानदंडों के खिलाफ मूल्यांकन करता है, कमजोरियां पहचानता है, और सुधार करता है। आप देखेंगे कि यह तब तक पुनरावृत्ति करता है जब तक कोड उत्पादन मानकों को पूरा न कर ले।

### संरचित विश्लेषण

कोड समीक्षा के लिए सुसंगत मूल्यांकन फ्रेमवर्क की आवश्यकता होती है। मॉडल कोड का विश्लेषण तय श्रेणियों (सहीपन, प्रथाएँ, प्रदर्शन, सुरक्षा) के साथ गंभीरता स्तरों के आधार पर करता है।

### मल्टी-टर्न चैट

"Spring Boot क्या है?" पूछें, फिर तुरंत "मुझे एक उदाहरण दिखाओ" पूछें। मॉडल आपका पहला प्रश्न याद रखता है और आपको विशेष रूप से Spring Boot का उदाहरण देता है। बिना मेमोरी के, दूसरा प्रश्न बहुत अस्पष्ट होगा।

### स्टेप-बाय-स्टेप तर्क

कोई गणित की समस्या चुनें और इसे दोनों स्टेप-बाय-स्टेप तर्क और कम उत्सुकता के साथ आज़माएं। कम उत्सुकता सिर्फ उत्तर देता है — तेज लेकिन अस्पष्ट। स्टेप-बाय-स्टेप आपको हर गणना और निर्णय दिखाता है।

### बाध्य आउटपुट

जब आपको विशिष्ट स्वरूप या शब्द संख्या की आवश्यकता हो, यह पैटर्न सख्ती से पालन करता है। ठीक 100 शब्दों के बुलेट पॉइंट फॉर्मेट में सारांश बनाएं।

## आप वास्तव में क्या सीख रहे हैं

**तर्क प्रयास सब कुछ बदल देता है**

GPT-5.2 आपको प्रॉम्प्ट के जरिए गणना प्रयास नियंत्रित करने देता है। कम प्रयास तेज उत्तर देता है जिसमें न्यूनतम खोज होती है। उच्च प्रयास का मतलब है कि मॉडल गहराई से सोचने के लिए समय लेता है। आप सीख रहे हैं कि कार्य की जटिलता के अनुसार प्रयास मिलाएं — सरल प्रश्नों पर समय बर्बाद न करें, लेकिन जटिल निर्णय जल्दी न करें।

**संरचना व्यवहार को मार्गदर्शित करती है**

प्रॉम्प्ट में XML टैग्स देखें? वे केवल सजावट नहीं हैं। मॉडल संरचित निर्देशों का पालन मुक्त रूप के मुकाबले अधिक भरोसेमंद तरीके से करता है। जब आपको मल्टी-स्टेप प्रक्रियाएं या जटिल तर्क चाहिए, संरचना मॉडल को यह ट्रैक करने में मदद करती है कि वह कहाँ है और अगला क्या है। नीचे दिया गया चित्र एक अच्छी तरह से संरचित प्रॉम्प्ट को तोड़ता है, दिखाता है कि `<system>`, `<instructions>`, `<context>`, `<user-input>`, और `<constraints>` जैसे टैग आपके निर्देशों को स्पष्ट खंडों में कैसे व्यवस्थित करते हैं।

<img src="../../../translated_images/hi/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*एक अच्छी तरह से संरचित प्रॉम्प्ट की संरचना जिसमें स्पष्ट अनुभाग और XML-शैली संगठन है*

**स्व-मूल्यांकन के जरिए गुणवत्ता**

स्व-प्रतिबिंबित पैटर्न गुणवत्ता मानदंडों को स्पष्ट बनाकर काम करते हैं। मॉडल पर "यह सही करेगा" की आशा करने के बजाय, आप उसे बताते हैं कि "सही" का क्या अर्थ है: सही तर्क, त्रुटि हैंडलिंग, प्रदर्शन, सुरक्षा। मॉडल तब अपनी आउटपुट का मूल्यांकन कर सकता है और सुधार कर सकता है। इससे कोड निर्माण लॉटरी नहीं बल्कि एक प्रक्रिया बन जाता है।

**संदर्भ सीमित है**

मल्टी-टर्न वार्तालाप प्रत्येक अनुरोध के साथ संदेश इतिहास शामिल करके काम करता है। लेकिन सीमा होती है — प्रत्येक मॉडल की अधिकतम टोकन संख्या होती है। संवाद बढ़ने पर आपको ऐसी रणनीतियां अपनानी होंगी जिससे प्रासंगिक संदर्भ बना रहे बिना उस सीमा को पार किए। यह मॉड्यूल आपको मेमोरी कैसे काम करती है दिखाता है; बाद में आप सीखेंगे कब सारांश बनाना है, कब भूलना है, और कब पुनः प्राप्त करना है।

## अगले कदम

**अगला मॉड्यूल:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**नेविगेशन:** [← पिछला: Module 01 - परिचय](../01-introduction/README.md) | [मुख्य पृष्ठ पर वापस](../README.md) | [अगला: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यह दस्तावेज़ एआई अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। जबकि हम सटीकता के लिए प्रयासरत हैं, कृपया ध्यान दें कि स्वचालित अनुवादों में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल भाषा में मूल दस्तावेज़ को अधिकृत स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->