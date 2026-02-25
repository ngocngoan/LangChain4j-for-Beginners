# मॉड्यूल 02: GPT-5.2 के साथ प्रॉम्प्ट इंजीनियरिंग

## सामग्री तालिका

- [आप क्या सीखेंगे](../../../02-prompt-engineering)
- [पूर्वापेक्षाएँ](../../../02-prompt-engineering)
- [प्रॉम्प्ट इंजीनियरिंग को समझना](../../../02-prompt-engineering)
- [प्रॉम्प्ट इंजीनियरिंग के मूल सिद्धांत](../../../02-prompt-engineering)
  - [ज़ीरो-शॉट प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [कुछ-शॉट प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [चेन ऑफ़ थॉट](../../../02-prompt-engineering)
  - [भूमिका-आधारित प्रॉम्प्टिंग](../../../02-prompt-engineering)
  - [प्रॉम्प्ट टेम्पलेट्स](../../../02-prompt-engineering)
- [उन्नत पैटर्न](../../../02-prompt-engineering)
- [मौजूदा Azure संसाधनों का उपयोग](../../../02-prompt-engineering)
- [एप्लिकेशन स्क्रीनशॉट](../../../02-prompt-engineering)
- [पैटर्न्स का अन्वेषण](../../../02-prompt-engineering)
  - [निम्न बनाम उच्च उत्सुकता](../../../02-prompt-engineering)
  - [कार्य निष्पादन (टूल प्रीएंबल्स)](../../../02-prompt-engineering)
  - [स्वयं-प्रतिबिंबित कोड](../../../02-prompt-engineering)
  - [संरचित विश्लेषण](../../../02-prompt-engineering)
  - [मल्टी-टर्न चैट](../../../02-prompt-engineering)
  - [कदम-दर-कदम तर्क](../../../02-prompt-engineering)
  - [बंधित आउटपुट](../../../02-prompt-engineering)
- [आप वास्तव में क्या सीख रहे हैं](../../../02-prompt-engineering)
- [अगले कदम](../../../02-prompt-engineering)

## आप क्या सीखेंगे

<img src="../../../translated_images/hi/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

पिछले मॉड्यूल में, आपने देखा कि मेमोरी कैसे संवादात्मक एआई को सक्षम करती है और GitHub मॉडलों का उपयोग करके बुनियादी इंटरैक्शन किए। अब हम इस बात पर ध्यान केंद्रित करेंगे कि आप प्रश्न कैसे पूछते हैं — स्वयं प्रॉम्प्ट — Azure OpenAI के GPT-5.2 का उपयोग करके। आप जो प्रॉम्प्ट बनाते हैं उसके ढांचे का आपके मिलने वाले उत्तर की गुणवत्ता पर गहरा प्रभाव पड़ता है। हम प्रॉम्प्टिंग तकनीकों की मूल समीक्षा से शुरू करेंगे, फिर आठ उन्नत पैटर्न की ओर बढ़ेंगे जो GPT-5.2 की क्षमताओं का पूरा लाभ उठाते हैं।

हम GPT-5.2 का उपयोग करेंगे क्योंकि यह तर्क नियंत्रण पेश करता है - आप मॉडल को बता सकते हैं कि उत्तर देने से पहले उसे कितना सोचना है। इससे विभिन्न प्रॉम्प्टिंग रणनीतियाँ अधिक स्पष्ट होती हैं और आपको समझने में मदद मिलती है कि किस दृष्टिकोण का उपयोग कब करना है। साथ ही GitHub मॉडलों की तुलना में Azure के GPT-5.2 के लिए कम दर सीमाओं का भी लाभ मिलेगा।

## पूर्वापेक्षाएँ

- मॉड्यूल 01 पूरा किया हुआ (Azure OpenAI संसाधन लागू किए गए)
- रूट डायरेक्टरी में `.env` फ़ाइल Azure क्रेडेंशियल्स के साथ (मॉड्यूल 01 में `azd up` द्वारा बनाई गई)

> **नोट:** यदि आपने मॉड्यूल 01 पूरा नहीं किया है, तो पहले वहाँ दिए गए तैनाती निर्देशों का पालन करें।

## प्रॉम्प्ट इंजीनियरिंग को समझना

<img src="../../../translated_images/hi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

प्रॉम्प्ट इंजीनियरिंग इनपुट टेक्स्ट को डिजाइन करने के बारे में है जो लगातार आपको आवश्यक परिणाम देता है। यह केवल सवाल पूछने के बारे में नहीं है - बल्कि अनुरोधों को इस तरह से संरचित करना है कि मॉडल बिल्कुल समझ जाए कि आप क्या चाहते हैं और उसे कैसे देना है।

इसे एक सहयोगी को निर्देश देने जैसा सोचें। "बग ठीक करें" अस्पष्ट है। "UserService.java की लाइन 45 में नल जाँच जोड़कर null pointer exception ठीक करें" विशिष्ट है। भाषा मॉडल भी इसी तरह काम करते हैं - विशिष्टता और संरचना महत्वपूर्ण हैं।

<img src="../../../translated_images/hi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j अवसंरचना प्रदान करता है — मॉडल कनेक्शन, मेमोरी, और मैसेज प्रकार — जबकि प्रॉम्प्ट पैटर्न केवल सावधानीपूर्वक संरचित पाठ हैं जिन्हें आप उस अवसंरचना के माध्यम से भेजते हैं। मुख्य निर्माण खंड हैं `SystemMessage` (जो AI के व्यवहार और भूमिका को सेट करता है) और `UserMessage` (जो आपका वास्तविक अनुरोध ले जाता है)।

## प्रॉम्प्ट इंजीनियरिंग के मूल सिद्धांत

<img src="../../../translated_images/hi/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

इस मॉड्यूल के उन्नत पैटर्न में छलांग लगाने से पहले, आइए पाँच मूलभूत प्रॉम्प्टिंग तकनीकों की समीक्षा करें। ये वे मूल निर्माण खंड हैं जिन्हें हर प्रॉम्प्ट इंजीनियर को जानना चाहिए। यदि आपने पहले से [क्विक स्टार्ट मॉड्यूल](../00-quick-start/README.md#2-prompt-patterns) किया है, तो आपने इन्हें क्रियान्वित देखा होगा — यहाँ उनकी संकल्पनात्मक रूपरेखा है।

### ज़ीरो-शॉट प्रॉम्प्टिंग

सबसे सरल तरीका: मॉडल को सीधे निर्देश दें बिना किसी उदाहरण के। मॉडल पूरी तरह से अपने प्रशिक्षण पर निर्भर करता है कि वह कार्य को समझे और निभाए। यह तब अच्छी तरह काम करता है जब अनुरोध सीधे और स्पष्ट होते हैं।

<img src="../../../translated_images/hi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*कोई उदाहरण न देते हुए सीधे निर्देश — मॉडल केवल निर्देश से कार्य को समझता है*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// प्रतिक्रिया: "सकारात्मक"
```

**कब उपयोग करें:** सरल वर्गीकरण, सीधे प्रश्न, अनुवाद, या कोई भी कार्य जिसे मॉडल बिना अतिरिक्त मार्गदर्शन के संभाल सकता है।

### कुछ-शॉट प्रॉम्प्टिंग

ऐसे उदाहरण प्रदान करें जो उस पैटर्न को दिखाते हैं जिसे आप चाहते हैं कि मॉडल फॉलो करे। मॉडल आपके उदाहरणों से अपेक्षित इनपुट-आउटपुट प्रारूप सीखता है और इसे नए इनपुट पर लागू करता है। यह उन कार्यों के लिए स्थिरता में सुधार करता है जहाँ वांछित प्रारूप या व्यवहार स्पष्ट नहीं होता।

<img src="../../../translated_images/hi/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*उदाहरणों से सीखना — मॉडल पैटर्न पहचानता है और इसे नए इनपुट पर लागू करता है*

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

**कब उपयोग करें:** कस्टम वर्गीकरण, संगत स्वरूपण, क्षेत्र-विशेष कार्य, या जब ज़ीरो-शॉट परिणाम असंगत हों।

### चेन ऑफ़ थॉट

मॉडल से कदम-दर-कदम अपना तर्क दिखाने के लिए कहें। सीधे उत्तर पर कूदने के बजाय, मॉडल समस्या को तोड़ता है और प्रत्येक भाग को स्पष्ट रूप से कार्य करता है। यह गणित, तर्क, और बहु-चरण तर्क कार्यों पर सटीकता बढ़ाता है।

<img src="../../../translated_images/hi/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*कदम-दर-कदम तर्क — जटिल समस्याओं को स्पष्ट तार्किक चरणों में तोड़ना*

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

### भूमिका-आधारित प्रॉम्प्टिंग

प्रश्न पूछने से पहले AI के लिए एक व्यक्तित्व या भूमिका सेट करें। यह संदर्भ प्रदान करता है जो उत्तर के स्वर, गहराई, और फोकस को आकार देता है। "सॉफ्टवेयर आर्किटेक्ट" "जूनियर डेवलपर" या "सुरक्षा ऑडिटर" से अलग सलाह देता है।

<img src="../../../translated_images/hi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*संदर्भ और भूमिका सेट करना — उसी प्रश्न पर अलग-अलग प्रतिक्रियाएँ रोल के आधार पर मिलती हैं*

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

**कब उपयोग करें:** कोड समीक्षा, ट्यूटोरिंग, क्षेत्र-विशेष विश्लेषण, या जब आपको किसी विशिष्ट विशेषज्ञता स्तर या दृष्टिकोण के अनुसार उत्तर चाहिए।

### प्रॉम्प्ट टेम्पलेट्स

फेर-उपयोग योग्य प्रॉम्प्ट बनाएं जिनमें वैरिएबल प्लेसहोल्डर हों। हर बार नया प्रॉम्प्ट लिखने की बजाय, एक बार टेम्पलेट डिफ़ाइन करें और अलग-अलग मान भरें। LangChain4j की `PromptTemplate` क्लास इसे `{{variable}}` सिंटैक्स से आसान बनाती है।

<img src="../../../translated_images/hi/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*वैरिएबल प्लेसहोल्डर के साथ पुन: उपयोग योग्य प्रॉम्प्ट — एक टेम्पलेट, कई उपयोग*

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

**कब उपयोग करें:** विभिन्न इनपुट के साथ दोहराए जाने वाले प्रश्न, बैच प्रोसेसिंग, पुन: उपयोग योग्य AI वर्कफ़्लो बनाना, या कोई भी परिदृश्य जहाँ प्रॉम्प्ट संरचना समान रहती है लेकिन डेटा बदलता है।

---

ये पाँच मूलभूत तकनीकें आपको अधिकांश प्रॉम्प्टिंग कार्यों के लिए एक ठोस उपकरण देती हैं। इस मॉड्यूल के बाकी हिस्से में इन्हीं के आधार पर **आठ उन्नत पैटर्न** बनाए गए हैं जो GPT-5.2 के तर्क नियंत्रण, आत्म-मूल्यांकन, और संरचित आउटपुट क्षमताओं का लाभ उठाते हैं।

## उन्नत पैटर्न

मूलभूत बातों को कवर करने के बाद, आइए उन आठ उन्नत पैटर्न्स पर जाएँ जो इस मॉड्यूल को विशिष्ट बनाते हैं। सभी समस्याओं के लिए एक ही दृष्टिकोण आवश्यक नहीं होता। कुछ प्रश्नों को तेज़ उत्तर चाहिए, कुछ को गहन सोच। कुछ को दृश्यमान तर्क, कुछ को केवल परिणाम। नीचे हर पैटर्न को एक अलग परिदृश्य के लिए अनुकूलित किया गया है — और GPT-5.2 का तर्क नियंत्रण ये अंतर और भी स्पष्ट करता है।

<img src="../../../translated_images/hi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*आठ प्रॉम्प्ट इंजीनियरिंग पैटर्न और उनके उपयोग के मामले का अवलोकन*

<img src="../../../translated_images/hi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 का तर्क नियंत्रण आपको यह निर्दिष्ट करने देता है कि मॉडल को कितना सोचना चाहिए — तेज सीधे उत्तर से लेकर गहरे अन्वेषण तक*

**निम्न उत्सुकता (तेज़ और केंद्रित)** - सरल प्रश्नों के लिए जहाँ आप तेज़, सीधे उत्तर चाहते हैं। मॉडल न्यूनतम तर्क करता है - अधिकतम 2 चरण। इसका उपयोग गणना, खोज, या सरल प्रश्नों के लिए करें।

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
> - "निम्न उत्सुकता और उच्च उत्सुकता प्रॉम्प्टिंग पैटर्न में क्या अंतर है?"
> - "प्रॉम्प्ट्स में XML टैग AI की प्रतिक्रिया को कैसे संरचित करते हैं?"
> - "स्वयं प्रतिबिंब पैटर्न कब और सीधे निर्देश कब उपयोग करना चाहिए?"

**उच्च उत्सुकता (गहन और व्यापक)** - जटिल समस्याओं के लिए जहाँ व्यापक विश्लेषण चाहिए। मॉडल विस्तार से तर्क दिखाता है और गहराई से अन्वेषण करता है। सिस्टम डिज़ाइन, आर्किटेक्चर निर्णय, या जटिल अनुसंधान के लिए उपयोग करें।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**कार्य निष्पादन (कदम-दर-कदम प्रगति)** - बहु-चरण वर्कफ़्लो के लिए। मॉडल एक प्रारंभिक योजना प्रदान करता है, प्रत्येक चरण का वर्णन करता है, फिर सारांश देता है। इसे माइग्रेशन, कार्यान्वयन, या किसी भी बहु-चरण प्रक्रिया के लिए उपयोग करें।

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

चेन-ऑफ-थॉट प्रॉम्प्टिंग मॉडल को स्पष्ट रूप से तर्क प्रक्रिया दिखाने के लिए कहता है, जो जटिल कार्यों में सही उत्तर देता है। कदम-दर-कदम टूटना मानव और AI दोनों को लॉजिक समझने में मदद करता है।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ कोशिश करें:** इस पैटर्न के बारे में पूछें:
> - "लंबे चलने वाले ऑपरेशनों के लिए कार्य निष्पादन पैटर्न मैं कैसे अनुकूलित करूँ?"
> - "प्रोडक्शन एप्लिकेशन में टूल प्रीएंबल्स को संरचित करने के सर्वोत्तम अभ्यास क्या हैं?"
> - "UI में मध्यवर्ती प्रगति अपडेट कैसे कैप्चर और प्रदर्शित कर सकता हूँ?"

<img src="../../../translated_images/hi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*परियोजना → निष्पादित करें → सारांश वर्कफ़्लो बहु-चरण कार्यों के लिए*

**स्वयं-प्रतिबिंबित कोड** - प्रोडक्शन-गुणवत्ता कोड बनाने के लिए। मॉडल प्रोडक्शन मानकों के अनुसार कोड जनरेट करता है जिसमें उचित त्रुटि हैंडलिंग होती है। नई सुविधाओं या सेवाओं के निर्माण में उपयोग करें।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*परिवर्तनकारी सुधार चक्र - जनरेट करें, मूल्यांकन करें, समस्याएँ पहचानें, सुधारें, पुनः दोहराएं*

**संरचित विश्लेषण** - संगत मूल्यांकन के लिए। मॉडल कोड की समीक्षा करता है एक निश्चित फ्रेमवर्क (सहीपन, प्रथाएँ, प्रदर्शन, सुरक्षा, वहनीयता) का उपयोग करके। कोड समीक्षा या गुणवत्ता आकलन के लिए उपयोग करें।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ कोशिश करें:** संरचित विश्लेषण के बारे में पूछें:
> - "सर्वर समीक्षा के विभिन्न प्रकारों के लिए विश्लेषण फ्रेमवर्क को कैसे अनुकूलित कर सकता हूँ?"
> - "संरचित आउटपुट को प्रोग्रामेटिक रूप से पार्स और क्रियान्वित करने का सर्वोत्तम तरीका क्या है?"
> - "विभिन्न समीक्षा सत्रों में गंभीरता स्तरों की निरंतरता कैसे सुनिश्चित करूँ?"

<img src="../../../translated_images/hi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*संगत कोड समीक्षा के लिए फ्रेमवर्क गंभीरता स्तरों के साथ*

**मल्टी-टर्न चैट** - संदर्भ की आवश्यकता वाले संवाद के लिए। मॉडल पिछली संदेशों को याद रखता है और उन पर निर्माण करता है। इंटरैक्टिव सहायता सत्रों या जटिल प्रश्नोत्तरों के लिए उपयोग करें।

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

*एकाधिक टर्न्स में संवाद संदर्भ कैसे जमा होता है जब तक टोकन सीमा न पहुँच जाए*

**कदम-दर-कदम तर्क** - उन समस्याओं के लिए जहाँ दृश्य तर्क आवश्यक हो। मॉडल प्रत्येक चरण के लिए स्पष्ट तर्क दिखाता है। गणित की समस्याओं, तर्क पहेलियों, या जब सोचने की प्रक्रिया समझनी हो, इसके लिए उपयोग करें।

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

*समस्याओं को स्पष्ट तार्किक चरणों में तोड़ना*

**बंधित आउटपुट** - उन प्रतिक्रियाओं के लिए जहाँ विशिष्ट प्रारूप आवश्यकताएँ हों। मॉडल सख्ती से प्रारूप और लंबाई नियम का पालन करता है। सारांशों या जब आपको सटीक आउटपुट संरचना चाहिए, उपयोग करें।

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

रूट डायरेक्टरी में `.env` फ़ाइल Azure क्रेडेंशियल्स के साथ मौजूद हो (मॉड्यूल 01 के दौरान बनाई गई):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दिखाना चाहिए
```

**एप्लिकेशन शुरू करें:**

> **नोट:** यदि आपने पहले से मॉड्यूल 01 से `./start-all.sh` का उपयोग करके सभी एप्लिकेशन शुरू कर दिए हैं, तो यह मॉड्यूल पहले से ही पोर्ट 8083 पर चल रहा है। आप नीचे दिए गए स्टार्ट कमांड छोड़ सकते हैं और सीधे http://localhost:8083 पर जा सकते हैं।

**विकल्प 1: स्प्रिंग बूट डैशबोर्ड का उपयोग (VS Code उपयोगकर्ताओं के लिए अनुशंसित)**

डेव कंटेनर में स्प्रिंग बूट डैशबोर्ड एक्सटेंशन शामिल है, जो सभी स्प्रिंग बूट एप्लिकेशन को प्रबंधित करने के लिए एक दृश्य इंटरफ़ेस प्रदान करता है। आप इसे VS Code के बाएँ Activity Bar में (Spring Boot आइकन देखें) पा सकते हैं।

स्प्रिंग बूट डैशबोर्ड से आप:
- कार्यक्षेत्र में सभी उपलब्ध स्प्रिंग बूट एप्लिकेशन देख सकते हैं
- एक क्लिक से एप्लिकेशन शुरू/रोक सकते हैं
- एप्लिकेशन लॉग्स वास्तविक समय में देख सकते हैं
- एप्लिकेशन स्थिति की निगरानी कर सकते हैं
सिर्फ "prompt-engineering" के बगल में प्ले बटन पर क्लिक करें इस मॉड्यूल को शुरू करने के लिए, या सभी मॉड्यूल एक साथ शुरू करें।

<img src="../../../translated_images/hi/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**विकल्प 2: शेल स्क्रिप्ट्स का उपयोग**

सभी वेब एप्लिकेशन (मॉड्यूल 01-04) शुरू करें:

**Bash:**
```bash
cd ..  # रूट निर्देशिका से
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # रूट डायरेक्टरी से
.\start-all.ps1
```

या सिर्फ इस मॉड्यूल को शुरू करें:

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

दोनों स्क्रिप्ट्स स्वचालित रूप से रूट `.env` फ़ाइल से पर्यावरण चर लोड करती हैं और यदि JAR मौजूद नहीं हैं तो उन्हें बनाएंगी।

> **Note:** यदि आप शुरू करने से पहले सभी मॉड्यूल मैन्युअल रूप से बनाना पसंद करते हैं:
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

*मुख्य डैशबोर्ड जो सभी 8 प्रॉम्प्ट इंजीनियरिंग पैटर्न्स को उनके लक्षणों और उपयोग के मामलों के साथ दिखाता है*

## पैटर्न्स की खोज

वेब इंटरफ़ेस आपको विभिन्न प्रॉम्प्टिंग रणनीतियों के साथ प्रयोग करने देता है। प्रत्येक पैटर्न अलग समस्याओं को हल करता है - इन्हें आज़माएँ यह देखने के लिए कि कौन सा तरीका कब बेहतर होता है।

> **Note: स्ट्रीमिंग बनाम नॉन-स्ट्रीमिंग** — हर पैटर्न पृष्ठ दो बटन प्रदान करता है: **🔴 स्ट्रीम प्रतिक्रिया (लाइव)** और एक **नॉन-स्ट्रीमिंग** विकल्प। स्ट्रीमिंग सर्वर-सेंट इवेंट्स (SSE) का उपयोग करती है ताकि मॉडल द्वारा उत्पन्न टोकन आपको तुरंत दिख सकें। नॉन-स्ट्रीमिंग विकल्प पूरे जवाब का इंतजार करता है। गहन तर्क वाले प्रॉम्प्ट्स (जैसे उच्च उत्साह, स्वयं-परावर्तित कोड) के लिए नॉन-स्ट्रीमिंग कॉल बहुत लंबा समय ले सकता है — कभी-कभी मिनटों तक — बिना कोई दृश्य प्रतिक्रिया दिए। **जटिल प्रॉम्प्ट्स के साथ प्रयोग करते समय स्ट्रीमिंग का उपयोग करें** ताकि आप मॉडल के काम करते हुए देख सकें और ऐसा न लगे कि अनुरोध समय समाप्त हो गया।
>
> **Note: ब्राउज़र आवश्यकता** — स्ट्रीमिंग फ़ीचर Fetch Streams API (`response.body.getReader()`) का उपयोग करता है जिसे पूर्ण ब्राउज़र (Chrome, Edge, Firefox, Safari) की आवश्यकता होती है। यह VS Code के इन-बिल्ट सिंपल ब्राउज़र में काम नहीं करता, क्योंकि इसका वेबव्यू ReadableStream API का समर्थन नहीं करता। यदि आप सिंपल ब्राउज़र का उपयोग करते हैं, तो नॉन-स्ट्रीमिंग बटन सामान्य रूप से काम करेंगे — केवल स्ट्रीमिंग बटन प्रभावित होंगे। पूर्ण अनुभव के लिए, `http://localhost:8083` को बाहरी ब्राउज़र में खोलें।

### कम बनाम उच्च उत्साह

साधारण सवाल पूछें जैसे "200 का 15% क्या है?" कम उत्साह के साथ। आपको तुरंत, सीधे उत्तर मिलेगा। अब कुछ जटिल पूछें जैसे "उच्च ट्रैफ़िक API के लिए कैशिंग रणनीति डिजाइन करें" उच्च उत्साह के साथ। **🔴 स्ट्रीम प्रतिक्रिया (लाइव)** पर क्लिक करें और देखें कि मॉडल का विस्तृत सोच चरण-दर-चरण दिखता है। वही मॉडल, वही प्रश्न संरचना - लेकिन प्रॉम्प्ट बताता है कि कितनी गहराई से सोचनी है।

### कार्य निष्पादन (टूल प्रीएम्बल्स)

मल्टी-स्टेप वर्कफ़्लो को पहले से योजना और प्रगति विवरण से लाभ होता है। मॉडल बताता है कि वह क्या करेगा, हर चरण का वर्णन करता है, फिर परिणाम संक्षेपित करता है।

### स्वयं-परावर्तित कोड

"एक ईमेल सत्यापन सेवा बनाएं" कोशिश करें। केवल कोड जेनरेट करने और रोकने के बजाय, मॉडल जेनरेट करता है, गुणवत्ता मापदंडों के खिलाफ मूल्यांकन करता है, कमजोरियों को पहचानता है, और सुधार करता है। आप देखेंगे कि यह तब तक दोहराता है जब तक कोड उत्पादन मानकों को पूरा नहीं करता।

### संरचित विश्लेषण

कोड समीक्षा के लिए सुसंगत मूल्यांकन फ्रेमवर्क की आवश्यकता होती है। मॉडल कोड को निर्धारित श्रेणियों (सहीता, अभ्यास, प्रदर्शन, सुरक्षा) और गंभीरता स्तरों के अनुसार विश्लेषण करता है।

### मल्टी-टर्न चैट

पूछें "Spring Boot क्या है?" और तुरंत बाद "मुझे एक उदाहरण दिखाओ"। मॉडल आपके पहले सवाल को याद रखता है और आपको एक विशेष Spring Boot उदाहरण देता है। बिना मेमोरी के, दूसरा सवाल बहुत अस्पष्ट होगा।

### चरण-दर-चरण तर्क

कोई गणित समस्या चुनें और इसे दोनों तरीके — चरण-दर-चरण तर्क और कम उत्साह के साथ आज़माएं। कम उत्साह केवल आपको उत्तर देता है - तेज़ लेकिन अस्पष्ट। चरण-दर-चरण आपको हर गणना और निर्णय दिखाता है।

### प्रतिबंधित आउटपुट

जब आपको विशिष्ट फ़ॉर्मेट या शब्द संख्या की आवश्यकता हो, यह पैटर्न सख्ती से पालन करता है। ठीक 100 शब्दों में बुलेट पॉइंट स्वरूप में सारांश बनाने की कोशिश करें।

## आप वास्तव में क्या सीख रहे हैं

**तर्क प्रयास सब कुछ बदल देता है**

GPT-5.2 आपको अपने प्रॉम्प्ट्स के माध्यम से संगणनात्मक प्रयास नियंत्रित करने देता है। कम प्रयास का मतलब तेज़ उत्तर के साथ न्यूनतम खोज। उच्च प्रयास का मतलब मॉडल गहराई से सोचता है। आप कार्य की जटिलता के अनुसार प्रयास मिलाना सीख रहे हैं - सरल सवालों पर समय बर्बाद न करें, लेकिन जटिल निर्णयों को भी जल्दी न करें।

**संरचना व्यवहार का मार्गदर्शन करती है**

प्रॉम्प्ट्स में XML टैग्स देखें? वे सजावटी नहीं हैं। मॉडल संरचित निर्देशों का पालन स्वतंत्र पाठ से अधिक विश्वसनीयता से करता है। जब आपको मल्टी-स्टेप प्रक्रियाएं या जटिल लॉजिक चाहिए, संरचना मॉडल को यह ट्रैक करने में मदद करती है कि वह कहाँ है और अगला क्या है।

<img src="../../../translated_images/hi/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*सुसंगठित प्रॉम्प्ट का ढांचा जिसमें स्पष्ट खंड और XML-शैली संगठन है*

**स्व-मूल्यांकन के माध्यम से गुणवत्ता**

स्वयं-परावर्तित पैटर्न गुणवत्ता मापदंडों को स्पष्ट करके काम करते हैं। यह आशा करने के बजाय कि मॉडल "सही करे", आप इसे स्पष्ट रूप से बताते हैं कि "सही" का अर्थ क्या है: सही लॉजिक, त्रुटि प्रबंधन, प्रदर्शन, सुरक्षा। मॉडल तब अपनी आउटपुट का मूल्यांकन कर सुधार कर सकता है। यह कोड जेनरेशन को लॉटरी से एक प्रक्रिया में बदल देता है।

**संदर्भ सीमित है**

मल्टी-टर्न वार्तालाप उस संदेश इतिहास को प्रत्येक अनुरोध के साथ शामिल करके काम करता है। लेकिन एक सीमा होती है - प्रत्येक मॉडल की अधिकतम टोकन संख्या होती है। जैसे-जैसे वार्तालाप बढ़ते हैं, आपको प्रासंगिक संदर्भ बनाए रखने के लिए रणनीतियाँ करनी होंगी बिना उस सीमा को पार किए। यह मॉड्यूल आपको दिखाता है कि मेमोरी कैसे काम करती है; बाद में आप सीखेंगे कि कब सारांश बनाना है, कब भूल जाना है, और कब पुनः प्राप्त करना है।

## अगले कदम

**अगला मॉड्यूल:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**नेविगेशन:** [← पिछला: मॉड्यूल 01 - परिचय](../01-introduction/README.md) | [मुख्य पेज पर वापस](../README.md) | [अगला: मॉड्यूल 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
यह दस्तावेज़ एआई अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनूदित किया गया है। यद्यपि हम सटीकता के लिए प्रयासरत हैं, कृपया ध्यान दें कि स्वचालित अनुवाद में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल भाषा में मौलिक दस्तावेज़ को प्राधिकृत स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->