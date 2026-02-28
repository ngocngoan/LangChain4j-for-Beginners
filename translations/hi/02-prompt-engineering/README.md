# Module 02: GPT-5.2 के साथ प्रांप्ट इंजीनियरिंग

## सामग्री सूची

- [वीडियो वॉकथ्रू](../../../02-prompt-engineering)
- [आप क्या सीखेंगे](../../../02-prompt-engineering)
- [पूर्वापेक्षाएँ](../../../02-prompt-engineering)
- [प्रांप्ट इंजीनियरिंग को समझना](../../../02-prompt-engineering)
- [प्रांप्ट इंजीनियरिंग की बुनियाद](../../../02-prompt-engineering)
  - [जीरो-शॉट प्रांप्टिंग](../../../02-prompt-engineering)
  - [फ्यू-शॉट प्रांप्टिंग](../../../02-prompt-engineering)
  - [चेन ऑफ थॉट](../../../02-prompt-engineering)
  - [रोल-आधारित प्रांप्टिंग](../../../02-prompt-engineering)
  - [प्रांप्ट टेम्प्लेट](../../../02-prompt-engineering)
- [उन्नत पैटर्न](../../../02-prompt-engineering)
- [मौजूदा Azure संसाधनों का उपयोग](../../../02-prompt-engineering)
- [एप्लिकेशन स्क्रीनशॉट्स](../../../02-prompt-engineering)
- [पैटर्न्स की खोज](../../../02-prompt-engineering)
  - [कम बनाम अधिक उत्सुकता](../../../02-prompt-engineering)
  - [कार्य निष्पादन (टूल प्रीएम्बल)](../../../02-prompt-engineering)
  - [स्वयं-विचारशील कोड](../../../02-prompt-engineering)
  - [संरचित विश्लेषण](../../../02-prompt-engineering)
  - [मल्टी-टर्न चैट](../../../02-prompt-engineering)
  - [कदम-दर-कदम तर्क](../../../02-prompt-engineering)
  - [सीमित आउटपुट](../../../02-prompt-engineering)
- [आप वास्तव में क्या सीख रहे हैं](../../../02-prompt-engineering)
- [अगले कदम](../../../02-prompt-engineering)

## वीडियो वॉकथ्रू

इस लाइव सेशन को देखें जो इस मॉड्यूल के साथ शुरुआत करने का तरीका समझाता है: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## आप क्या सीखेंगे

<img src="../../../translated_images/hi/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

पिछले मॉड्यूल में, आपने देखा कि मेमोरी कैसे संवादात्मक AI को सक्षम बनाती है और GitHub Models का उपयोग बेसिक इंटरैक्शंस के लिए किया गया। अब हम ध्यान केंद्रित करेंगे कि आप कैसे प्रश्न पूछते हैं — यानी प्रांप्ट्स — Azure OpenAI के GPT-5.2 का उपयोग करते हुए। जिस तरह आप अपने प्रांप्ट्स को संरचित करते हैं वह आपके प्राप्त होने वाले उत्तरों की गुणवत्ता को नाटकीय रूप से प्रभावित करता है। हम बुनियादी प्रांप्टिंग तकनीकों की समीक्षा से शुरू करते हैं, फिर आठ उन्नत पैटर्न में जाते हैं जो GPT-5.2 की क्षमताओं का पूरा लाभ उठाते हैं।

हम GPT-5.2 का उपयोग करेंगे क्योंकि यह तर्क नियंत्रण (reasoning control) प्रस्तुत करता है - आप मॉडल को यह बता सकते हैं कि जवाब देने से पहले उसे कितना सोच-विचार करना है। इससे अलग-अलग प्रांप्टिंग रणनीतियाँ अधिक स्पष्ट हो जाती हैं और यह समझने में मदद मिलती है कि किस दृष्टिकोण का कब उपयोग करना है। Azure के GPT-5.2 के लिए GitHub Models की तुलना में कम रेट लिमिट्स से भी लाभ मिलेगा।

## पूर्वापेक्षाएँ

- मॉड्यूल 01 पूर्ण किया हुआ (Azure OpenAI संसाधन तैनात)
- रूट डायरेक्टरी में `.env` फ़ाइल जिसमें Azure क्रेडेंशियल्स हैं (मॉड्यूल 01 में `azd up` द्वारा बनाई गई)

> **नोट:** यदि आपने मॉड्यूल 01 पूरा नहीं किया है, तो पहले वहां दिए तैनाती निर्देशों का पालन करें।

## प्रांप्ट इंजीनियरिंग को समझना

<img src="../../../translated_images/hi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

प्रांप्ट इंजीनियरिंग इस बारे में है कि आप ऐसा इनपुट टेक्स्ट डिजाइन करें जो लगातार आपको आवश्यक परिणाम देता हो। यह केवल प्रश्न पूछने के बारे में नहीं है - बल्कि यह अनुरोधों को इस तरह से संरचित करने के बारे में है कि मॉडल बिलकुल समझ पाए कि आप क्या चाहते हैं और इसे कैसे प्रदान करना है।

इसे ऐसे सोचें जैसे आप अपने सहकर्मी को निर्देश दे रहे हैं। "बग ठीक करो" अस्पष्ट है। "UserService.java लाइन 45 में null pointer exception को null check जोड़कर ठीक करो" विशिष्ट है। भाषा मॉडल भी इसी तरह काम करते हैं — विशिष्टता और संरचना मायने रखती है।

<img src="../../../translated_images/hi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j वह आधारभूत ढांचा प्रदान करता है — मॉडल कनेक्शन, मेमोरी, और संदेश प्रकार — जबकि प्रांप्ट पैटर्न केवल सावधानीपूर्वक संरचित टेक्स्ट होते हैं जिन्हें आप उस आधारभूत ढांचे के माध्यम से भेजते हैं। मुख्य निर्माण खंड हैं `SystemMessage` (जो AI का व्यवहार और भूमिका तय करता है) और `UserMessage` (जो आपका वास्तविक अनुरोध ले जाता है)।

## प्रांप्ट इंजीनियरिंग की बुनियाद

<img src="../../../translated_images/hi/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

इस मॉड्यूल में उन्नत पैटर्न्स में उतरने से पहले, आइए पांच मूल प्रांप्टिंग तकनीकों की समीक्षा करें। ये वे निर्माण खंड हैं जिन्हें हर प्रांप्ट इंजीनियर को जानना चाहिए। यदि आपने पहले ही [Quick Start मॉड्यूल](../00-quick-start/README.md#2-prompt-patterns) पर काम किया है, तो आपने इन्हें क्रियान्वित होते देखा होगा — यहाँ इनके पीछे का वैचारिक ढांचा दिया गया है।

### जीरो-शॉट प्रांप्टिंग

सबसे सरल तरीका: मॉडल को बिना उदाहरणों के सीधा निर्देश देना। मॉडल पूरी तरह से अपने प्रशिक्षण पर भरोसा करता है यह समझने और कार्य करने के लिए। यह तब काम करता है जब अपेक्षित व्यवहार स्पष्ट हो।

<img src="../../../translated_images/hi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*उदाहरणों के बिना सीधे निर्देश — मॉडल केवल निर्देश से कार्य अनुमानित करता है*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// प्रतिक्रिया: "सकारात्मक"
```

**कब उपयोग करें:** सरल वर्गीकरण, सीधे प्रश्न, अनुवाद, या कोई भी कार्य जिसमें मॉडल को अतिरिक्त मार्गदर्शन की आवश्यकता न हो।

### फ्यू-शॉट प्रांप्टिंग

उदाहरण प्रदान करें जो मॉडल को उस पैटर्न का पालन करना सिखाते हैं जिसे आप चाहते हैं। मॉडल आपके उदाहरणों से अपेक्षित इनपुट-आउटपुट प्रारूप सीखता है और इसे नए इनपुट पर लागू करता है। यह उन कार्यों के लिए सुसंगतता को नाटकीय रूप से बढ़ाता है जहाँ वांछित प्रारूप या व्यवहार स्पष्ट नहीं होता।

<img src="../../../translated_images/hi/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*उदाहरणों से सीखना — मॉडल पैटर्न पहचानता है और नए इनपुट पर लागू करता है*

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

**कब उपयोग करें:** अनुकूलित वर्गीकरण, सुसंगत प्रारूपण, डोमेन-विशिष्ट कार्य, या जब जीरो-शॉट परिणाम असंगत हों।

### चेन ऑफ थॉट

मॉडल से कहें कि वह अपना तर्क चरण-दर-चरण दिखाए। सीधे उत्तर तक नहीं पहुंचता, बल्कि समस्या को तोड़ कर प्रत्येक भाग को स्पष्ट रूप से समझाता है। इससे गणित, तर्क, और बहु-चरण तर्क कार्यों में सटीकता बढ़ती है।

<img src="../../../translated_images/hi/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*कदम-दर-कदम तर्क — जटिल समस्याओं को स्पष्ट तार्किक चरणों में विभाजित करना*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// मॉडल दिखाता है: 15 - 8 = 7, फिर 7 + 12 = 19 सेब
```

**कब उपयोग करें:** गणित की समस्याएं, तर्क पहेलियाँ, डिबगिंग, या कोई भी कार्य जिसमें तर्क प्रक्रिया दिखाने से सटीकता और विश्वास बढ़े।

### रोल-आधारित प्रांप्टिंग

प्रश्न पूछने से पहले AI के लिए एक भूमिका या व्यक्तित्व सेट करें। यह संदर्भ प्रदान करता है जो उत्तर के स्वर, गहराई, और फोकस को आकार देता है। "सॉफ्टवेयर आर्किटेक्ट" एक तरह की सलाह देता है, "जूनियर डेवलपर" या "सुरक्षा ऑडिटर" अलग।

<img src="../../../translated_images/hi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*संदर्भ और व्यक्तित्व सेट करना — एक ही प्रश्न को निर्धारित भूमिका के आधार पर अलग उत्तर मिलता है*

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

**कब उपयोग करें:** कोड समीक्षा, शिक्षण, डोमेन-विशिष्ट विश्लेषण, या जब किसी विशेष विशेषज्ञता स्तर या दृष्टिकोण के लिए उत्तर चाहिए।

### प्रांप्ट टेम्प्लेट

परिवर्ती प्लेसहोल्डर्स के साथ पुन: प्रयोज्य प्रांप्ट बनाएं। हर बार नया प्रांप्ट लिखने के बजाय, एक बार टेम्प्लेट परिभाषित करें और विभिन्न मान भरें। LangChain4j का `PromptTemplate` क्लास `{{variable}}` सिंटैक्स के साथ इसे आसान बनाता है।

<img src="../../../translated_images/hi/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*परिवर्तनीय प्लेसहोल्डर्स के साथ पुन: प्रयोज्य प्रांप्ट — एक टेम्प्लेट, कई उपयोग*

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

**कब उपयोग करें:** विभिन्न इनपुट के साथ बार-बार पूछताछ, बैच प्रोसेसिंग, पुन: प्रयोज्य AI वर्कफ़्लोज़ बनाना, या कोई ऐसा परिदृश्य जहाँ प्रांप्ट संरचना समान रहता है लेकिन डेटा बदलता रहता है।

---

ये पाँच मूल बातें आपको अधिकांश प्रांप्टिंग कार्यों के लिए एक ठोस उपकरण किट देती हैं। इस मॉड्यूल के बाकी हिस्से में हम इन पर **आठ उन्नत पैटर्न्स** के साथ निर्माण करते हैं जो GPT-5.2 के तर्क नियंत्रण, स्व-आकलन, और संरचित आउटपुट क्षमताओं का लाभ उठाते हैं।

## उन्नत पैटर्न

मूल बातें कवर करने के बाद, आइए उन आठ उन्नत पैटर्न्स पर जाएं जो इस मॉड्यूल को विशिष्ट बनाते हैं। सभी समस्याओं के लिए एक ही दृष्टिकोण जरूरी नहीं। कुछ प्रश्नों को तेज उत्तर चाहिए, कुछ को गहराई से सोचने की आवश्यकता होती है। कुछ को दिखाई देने वाला तर्क चाहिए, कुछ को केवल परिणाम। नीचे प्रत्येक पैटर्न अलग स्थिति के लिए अनुकूलित है — और GPT-5.2 का तर्क नियंत्रण इन मतभेदों को और भी स्पष्ट बनाता है।

<img src="../../../translated_images/hi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*आठ प्रांप्ट इंजीनियरिंग पैटर्न्स का अवलोकन और उनके उपयोग के मामले*

<img src="../../../translated_images/hi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 का तर्क नियंत्रण आपको यह निर्धारित करने देता है कि मॉडल को कितना सोच-विचार करना चाहिए — तेज सीधे उत्तर से लेकर गहरे अन्वेषण तक*

**कम उत्सुकता (तेज और केंद्रित)** - सरल प्रश्नों के लिए जहाँ आप तेज और सीधे उत्तर चाहते हैं। मॉडल न्यूनतम तर्क करता है - अधिकतम 2 चरण। इसे गणना, देखना, या सीधे प्रश्नों के लिए उपयोग करें।

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
> - "कम उत्सुकता और उच्च उत्सुकता वाले प्रांप्टिंग पैटर्न में क्या अंतर है?"
> - "प्रांप्ट में XML टैग AI के उत्तर को संरचित करने में कैसे मदद करते हैं?"
> - "स्वयं-विचार पैटर्न और सीधे निर्देश का उपयोग कब करें?"

**उच्च उत्सुकता (गहरा और व्यापक)** - जटिल समस्याओं के लिए जहाँ आप व्यापक विश्लेषण चाहते हैं। मॉडल पूरी तरह अन्वेषण करता है और विस्तृत तर्क दिखाता है। इसे सिस्टम डिज़ाइन, आर्किटेक्चर निर्णय, या जटिल शोध के लिए उपयोग करें।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**कार्य निष्पादन (कदम-दर-कदम प्रगति)** - बहु-चरण वर्कफ़्लोज़ के लिए। मॉडल पहले योजना देता है, काम करते हुए प्रत्येक चरण को बताता है, फिर सारांश प्रस्तुत करता है। इसे माइग्रेशन, कार्यान्वयन, या किसी भी बहु-चरण प्रक्रिया के लिए उपयोग करें।

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

चेन-ऑफ-थॉट प्रांप्टिंग स्पष्ट रूप से मॉडल से कहता है कि वह अपना तर्क प्रक्रिया दिखाए, जो जटिल कार्यों के लिए सटीकता बढ़ाता है। कदम-दर-कदम टूटना मनुष्यों और AI दोनों को तर्क समझने में मदद करता है।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ आज़माएं:** इस पैटर्न के बारे में पूछें:
> - "मैं लंबे चलने वाले ऑपरेशन्स के लिए कार्य निष्पादन पैटर्न को कैसे अनुकूलित करूँ?"
> - "उत्पादन एप्लिकेशन में टूल प्रीएम्बल को संरचित करने के सर्वोत्तम अभ्यास क्या हैं?"
> - "मैं UI में मध्यवर्ती प्रगति अपडेट्स कैसे कैप्चर और प्रदर्शन कर सकता हूँ?"

<img src="../../../translated_images/hi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*बहु-चरण कार्यों के लिए योजना → निष्पादन → सारांश वर्कफ़्लो*

**स्वयं-विचारशील कोड** - उत्पादन गुणवत्ता वाला कोड उत्पन्न करने के लिए। मॉडल उत्पादन मानकों और उचित त्रुटि प्रबंधन के साथ कोड बनाता है। इसे नई सुविधाओं या सेवाओं के निर्माण के दौरान इस्तेमाल करें।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*आवर्ती सुधार चक्र - उत्पन्न करें, मूल्यांकन करें, मुद्दे पहचानें, सुधार करें, दोहराएँ*

**संरचित विश्लेषण** - सुसंगत मूल्यांकन के लिए। मॉडल एक निश्चित फ्रेमवर्क (सहीपन, प्रथाएँ, प्रदर्शन, सुरक्षा, रखरखाव) के अनुसार कोड की समीक्षा करता है। इसे कोड समीक्षा या गुणवत्ता आकलन के लिए उपयोग करें।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ आज़माएं:** संरचित विश्लेषण के बारे में पूछें:
> - "मैं विभिन्न प्रकार की कोड समीक्षाओं के लिए विश्लेषण फ्रेमवर्क को कैसे अनुकूलित कर सकता हूँ?"
> - "संरचित आउटपुट को प्रोग्रामेटिक रूप से पार्स और क्रियान्वित करने का सर्वोत्तम तरीका क्या है?"
> - "विभिन्न समीक्षा सत्रों में सुसंगत गंभीरता स्तर सुनिश्चित करने के लिए क्या करें?"

<img src="../../../translated_images/hi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*गंभीरता स्तरों के साथ सुसंगत कोड समीक्षा के लिए फ्रेमवर्क*

**मल्टी-टर्न चैट** - ऐसे संवादों के लिए जिनमें संदर्भ चाहिए। मॉडल पिछले संदेश याद रखता है और उन पर निर्माण करता है। इसे इंटरैक्टिव सहायता सत्र या जटिल प्रश्नोत्तर के लिए उपयोग करें।

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/hi/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*कई चरणों में संवाद संदर्भ कैसे जमा होता है जब तक टोकन सीमा न पहुंच जाए*

**कदम-दर-कदम तर्क** - उन समस्याओं के लिए जिनमें स्पष्ट तर्क की आवश्यकता हो। मॉडल प्रत्येक चरण के लिए स्पष्ट तर्क दिखाता है। इसे गणित की समस्याओं, तर्क पहेलियों, या जब आप सोच प्रक्रिया समझना चाहते हैं, में उपयोग करें।

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hi/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*समस्याओं को स्पष्ट तार्किक चरणों में विभाजित करना*

**सीमित आउटपुट** - ऐसे उत्तरों के लिए जिनमें विशिष्ट प्रारूप आवश्यकताएँ हों। मॉडल सख्ती से प्रारूप और लंबाई नियमों का पालन करता है। इसे सारांशों के लिए या जब सटीक आउटपुट संरचना चाहिए, में उपयोग करें।

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

<img src="../../../translated_images/hi/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*विशिष्ट प्रारूप, लंबाई, और संरचना आवश्यकताओं को लागू करना*

## मौजूदा Azure संसाधनों का उपयोग

**तैनाती सत्यापित करें:**

सुनिश्चित करें कि रूट डायरेक्टरी में `.env` फ़ाइल मौजूद है जिसमें Azure क्रेडेंशियल्स हैं (मॉड्यूल 01 के दौरान बनाई गई):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दिखाना चाहिए
```

**एप्लिकेशन शुरू करें:**

> **नोट:** यदि आपने पहले मॉड्यूल 01 से `./start-all.sh` का उपयोग करके सभी एप्लिकेशन शुरू कर दिए हैं, तो यह मॉड्यूल पहले से 8083 पोर्ट पर चल रहा है। आप नीचे दिए गए स्टार्ट कमांड्स छोड़ सकते हैं और सीधे http://localhost:8083 पर जा सकते हैं।

**विकल्प 1: स्प्रिंग बूट डैशबोर्ड का उपयोग (VS Code उपयोगकर्ताओं के लिए अनुशंसित)**  

डेव कंटेनर में Spring Boot डैशबोर्ड एक्सटेंशन शामिल है, जो सभी Spring Boot एप्लिकेशन को प्रबंधित करने के लिए एक दृश्य इंटरफ़ेस प्रदान करता है। आप इसे VS Code के बाएं साइड में एक्टिविटी बार में पा सकते हैं (Spring Boot आइकन देखें)।

Spring Boot डैशबोर्ड से आप:
- वर्कस्पेस में उपलब्ध सभी Spring Boot एप्लिकेशन देख सकते हैं
- एक क्लिक से एप्लिकेशन शुरू/रोक सकते हैं
- एप्लिकेशन लॉग्स को रियल-टाइम में देख सकते हैं
- एप्लिकेशन की स्थिति मॉनिटर कर सकते हैं

इस मॉड्यूल को शुरू करने के लिए "prompt-engineering" के बगल में प्ले बटन पर बस क्लिक करें, या सभी मॉड्यूल एक साथ शुरू करें।

<img src="../../../translated_images/hi/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**विकल्प 2: शेल स्क्रिप्ट्स का उपयोग**

सभी वेब एप्लिकेशन (मॉड्यूल 01-04) शुरू करें:

**Bash:**
```bash
cd ..  # रूट डायरेक्टरी से
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

दोनों स्क्रिप्ट्स अपने आप रूट `.env` फाइल से एनवायरनमेंट वेरिएबल्स लोड करेंगी और अगर JARs मौजूद नहीं हैं तो उन्हें बनाएंगी।

> **नोट:** यदि आप शुरू करने से पहले सभी मॉड्यूल मैनुअली बनाना पसंद करते हैं:
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
.\stop.ps1  # केवल यह मॉड्यूल
# या
cd ..; .\stop-all.ps1  # सभी मॉड्यूल
```

## एप्लिकेशन स्क्रीनशॉट्स

<img src="../../../translated_images/hi/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*मुख्य डैशबोर्ड जो सभी 8 प्रॉम्प्ट इंजीनियरिंग पैटर्न्स उनके विशेषताओं और उपयोग मामलों के साथ दिखा रहा है*

## पैटर्न्स का अन्वेषण

वेब इंटरफ़ेस आपको विभिन्न प्रॉम्प्टिंग रणनीतियों के साथ प्रयोग करने देता है। प्रत्येक पैटर्न अलग समस्याओं को हल करता है - उन्हें आज़माएं और देखें कि कब कौन सा तरीका बेहतर होता है।

> **नोट: स्ट्रीमिंग बनाम नॉन-स्ट्रीमिंग** — प्रत्येक पैटर्न पेज पर दो बटन होते हैं: **🔴 Stream Response (Live)** और एक **Non-streaming** विकल्प। स्ट्रीमिंग सर्वर-सेंट इवेंट्स (SSE) का उपयोग करता है जिससे मॉडल द्वारा जेनरेट किए गए टोकन रियल-टाइम में प्रदर्शित होते हैं, तो आप तुरंत प्रगति देख सकते हैं। नॉन-स्ट्रीमिंग विकल्प पूरे प्रतिक्रिया के आने तक इंतजार करता है। गहरी तर्कशीलता वाले प्रॉम्प्ट्स (जैसे High Eagerness, Self-Reflecting Code) के लिए नॉन-स्ट्रीमिंग कॉल बहुत लंबा समय ले सकता है — कभी-कभी मिनटों तक — बिना कोई दृश्य प्रतिक्रिया के। **जटिल प्रॉम्प्ट के साथ प्रयोग करते समय स्ट्रीमिंग का उपयोग करें** ताकि आप मॉडल के काम को देख सकें और यह न लगे कि अनुरोध टाइम आउट हो गया है।
>
> **नोट: ब्राउज़र आवश्यकता** — स्ट्रीमिंग फीचर Fetch Streams API (`response.body.getReader()`) का उपयोग करता है जिसके लिए एक पूर्ण ब्राउज़र (Chrome, Edge, Firefox, Safari) आवश्यक है। यह VS Code के बिल्ट-इन Simple Browser में काम नहीं करता, क्योंकि इसका वेबव्यू ReadableStream API का समर्थन नहीं करता। यदि आप Simple Browser का उपयोग करते हैं, तो नॉन-स्ट्रीमिंग बटन सामान्य रूप से काम करेंगे — केवल स्ट्रीमिंग बटन प्रभावित होते हैं। पूरी सुविधा के लिए `http://localhost:8083` को बाहरी ब्राउज़र में खोलें।

### Low बनाम High Eagerness

Low Eagerness का उपयोग करके "15% of 200 क्या है?" जैसे सरल प्रश्न पूछें। आपको त्वरित, सीधा उत्तर मिलेगा। अब High Eagerness का उपयोग करके "एक हाई-ट्रैफिक API के लिए कैशिंग रणनीति डिजाइन करें" जैसी जटिल बात पूछें। **🔴 Stream Response (Live)** क्लिक करें और मॉडल की विस्तृत तर्कशीलता को टोकन दर टोकन देखेंगे। वही मॉडल, वही प्रश्न संरचना - लेकिन प्रॉम्प्ट उसे यह बताता है कि कितना सोचने की आवश्यकता है।

### कार्य निष्पादन (टूल प्रीएम्बल्स)

मल्टी-स्टेप वर्कफ़्लोज़ को पहले से योजना और प्रगति वर्णन की आवश्यकता होती है। मॉडल यह बताता है कि वह क्या करेगा, प्रत्येक चरण का वर्णन करता है, फिर परिणामों का सारांश प्रस्तुत करता है।

### Self-Reflecting Code

"एक ईमेल सत्यापन सेवा बनाएं" प्रयास करें। केवल कोड जेनरेट करने और रुकने की बजाय, मॉडल जेनरेट करता है, गुणवत्ता मानदंडों के खिलाफ मूल्यांकन करता है, कमजोरियां पहचानता है, और सुधार करता है। आप इसे तब तक दोहराते देखेंगे जब तक कि कोड उत्पादन मानकों को पूरा न कर ले।

### Structured Analysis

कोड समीक्षा के लिए सुसंगत मूल्यांकन रूपरेखा आवश्यक है। मॉडल कोड का विश्लेषण तय श्रेणियों (सहीपन, प्रथाएं, प्रदर्शन, सुरक्षा) और गंभीरता स्तरों के साथ करता है।

### Multi-Turn Chat

"Spring Boot क्या है?" पूछें और तुरंत "मुझे एक उदाहरण दिखाएं" से पालन करें। मॉडल आपका पहला प्रश्न याद रखता है और विशेष रूप से एक Spring Boot उदाहरण प्रदान करता है। बिना मेमोरी के, दूसरा प्रश्न बहुत अस्पष्ट होता।

### Step-by-Step Reasoning

कोई गणित समस्या चुनें और इसे दोनों Step-by-Step Reasoning और Low Eagerness के साथ आज़माएं। Low eagerness केवल आपको जवाब देता है - तेज़ लेकिन अस्पष्ट। Step-by-step आपको हर गणना और निर्णय दिखाता है।

### Constrained Output

जब आपको विशिष्ट प्रारूप या शब्द गणना की आवश्यकता हो, तो यह पैटर्न सख्ती से पालन सुनिश्चित करता है। 100 शब्दों में सारांश बुलेट प्वाइंट प्रारूप में बनाकर देखें।

## आप वास्तव में क्या सीख रहे हैं

**तर्कशील प्रयास सब कुछ बदल देता है**

GPT-5.2 आपको प्रॉम्प्ट्स के माध्यम से गणनात्मक प्रयास नियंत्रित करने की सुविधा देता है। कम प्रयास का मतलब तेज उत्तर और कम खोज है। उच्च प्रयास का मतलब मॉडल गहराई से सोचने में समय लेता है। आप सीख रहे हैं कि प्रयास को कार्य की जटिलता के अनुसार कैसे मिलाएं - सरल प्रश्नों पर समय बर्बाद न करें, लेकिन जटिल निर्णयों में जल्दी नहीं करें।

**संरचना व्यवहार को निर्देशित करती है**

प्रॉम्प्ट्स में XML टैग्स देखें? ये सजावट नहीं हैं। मॉडल संरचित निर्देशों का पालन मुक्त रूप पाठ की तुलना में अधिक भरोसेमंद तरीके से करते हैं। जब आपको बहु-चरणीय प्रक्रियाओं या जटिल तर्क की जरूरत होती है, तो संरचना मॉडल को यह ट्रैक करने में मदद करती है कि वह कहाँ है और आगे क्या करना है।

<img src="../../../translated_images/hi/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*स्पष्ट अनुभागों और XML-शैली के संगठन के साथ एक अच्छी तरह से संरचित प्रॉम्प्ट की संरचना*

**स्व-मूल्यांकन के माध्यम से गुणवत्ता**

स्व-प्रतिबिंबित पैटर्न गुणवत्ता मानदंडों को स्पष्ट बनाकर काम करते हैं। केवल उम्मीद करने के बजाय कि मॉडल "ठीक से करेगा", आप इसे बताते हैं कि "ठीक" का क्या मतलब है: सही तर्क, त्रुटि प्रबंधन, प्रदर्शन, सुरक्षा। फिर मॉडल अपनी आउटपुट का मूल्यांकन कर सकता है और सुधार कर सकता है। इससे कोड जेनरेशन एक लॉटरी से प्रक्रिया में बदल जाता है।

**संदर्भ सीमित है**

मल्टी-टर्न वार्तालाप प्रत्येक अनुरोध के साथ संदेश इतिहास शामिल करने से काम करता है। लेकिन इसकी एक सीमा है - हर मॉडल की अधिकतम टोकन सीमा होती है। जैसे-जैसे बातचीत बढ़ती है, आपको प्रासंगिक संदर्भ बनाए रखने के लिए रणनीतियाँ चाहिए बिना उस सीमा को पार किए। यह मॉड्यूल आपको मेमोरी कैसे काम करती है दिखाता है; बाद में आप सीखेंगे कि कब सारांश बनाना है, कब भूल जाना है, और कब पुनः प्राप्त करना है।

## अगले कदम

**अगला मॉड्यूल:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**नेविगेशन:** [← पिछला: मॉड्यूल 01 - परिचय](../01-introduction/README.md) | [मुख्य पृष्ठ पर वापस](../README.md) | [अगला: मॉड्यूल 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यह दस्तावेज़ एआई अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। हम सटीकता के लिए प्रयासरत हैं, लेकिन कृपया ध्यान दें कि स्वचालित अनुवादों में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल भाषा में मूल दस्तावेज़ ही अधिकारिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->