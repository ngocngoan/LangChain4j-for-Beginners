# Module 02: GPT-5.2 के साथ प्रांप्ट इंजीनियरिंग

## Table of Contents

- [आप क्या सीखेंगे](../../../02-prompt-engineering)
- [पूर्वापेक्षाएँ](../../../02-prompt-engineering)
- [प्रांप्ट इंजीनियरिंग को समझना](../../../02-prompt-engineering)
- [प्रांप्ट इंजीनियरिंग के मूल तत्व](../../../02-prompt-engineering)
  - [शून्य-शॉट प्रांप्टिंग](../../../02-prompt-engineering)
  - [कुछ-शॉट प्रांप्टिंग](../../../02-prompt-engineering)
  - [चैन ऑफ थॉट](../../../02-prompt-engineering)
  - [भूमिका-आधारित प्रांप्टिंग](../../../02-prompt-engineering)
  - [प्रांप्ट टेम्पलेट्स](../../../02-prompt-engineering)
- [उन्नत पैटर्न](../../../02-prompt-engineering)
- [मौजूदा Azure संसाधनों का उपयोग](../../../02-prompt-engineering)
- [एप्लिकेशन स्क्रीनशॉट](../../../02-prompt-engineering)
- [पैटर्न का अन्वेषण](../../../02-prompt-engineering)
  - [कम बनाम उच्च उत्साह](../../../02-prompt-engineering)
  - [कार्य निष्पादन (टूल प्रीएम्बल्स)](../../../02-prompt-engineering)
  - [स्व-परावर्तित कोड](../../../02-prompt-engineering)
  - [संरचित विश्लेषण](../../../02-prompt-engineering)
  - [मल्टी-टर्न चैट](../../../02-prompt-engineering)
  - [चरण-दर-चरण तर्क](../../../02-prompt-engineering)
  - [प्रतिबंधित आउटपुट](../../../02-prompt-engineering)
- [आप वास्तव में क्या सीख रहे हैं](../../../02-prompt-engineering)
- [अगले कदम](../../../02-prompt-engineering)

## आप क्या सीखेंगे

<img src="../../../translated_images/hi/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

पिछले मॉड्यूल में, आपने देखा कि कैसे मेमोरी वार्तालापीय AI को सक्षम बनाती है और बुनियादी इंटरैक्शन के लिए GitHub Models का उपयोग किया। अब हम ध्यान केंद्रित करेंगे कि आप प्रश्न कैसे पूछते हैं — यानी स्वयं प्रांप्ट — Azure OpenAI के GPT-5.2 का उपयोग करते हुए। आप कैसे प्रांप्ट संरचित करते हैं, इसका आपके द्वारा प्राप्त उत्तरों की गुणवत्ता पर गहरा असर पड़ता है। हम मूल प्रांप्टिंग तकनीकों की समीक्षा से शुरू करते हैं, फिर GPT-5.2 की क्षमताओं का पूरा लाभ उठाने वाले आठ उन्नत पैटर्न पर चलते हैं।

हम GPT-5.2 का उपयोग करेंगे क्योंकि यह तर्क नियंत्रण पेश करता है - आप मॉडल को बता सकते हैं कि उत्तर देने से पहले वह कितना सोचें। इससे विभिन्न प्रांप्टिंग रणनीतियाँ अधिक स्पष्ट हो जाती हैं और आपको समझने में मदद मिलती है कि कब कौन सा तरीका उपयोग करना चाहिए। हमें Azure के GPT-5.2 के लिए कम दर सीमाओं से भी लाभ मिलेगा, जो GitHub Models की तुलना में बेहतर हैं।

## पूर्वापेक्षाएँ

- मॉड्यूल 01 पूरा किया हुआ हो (Azure OpenAI संसाधन तैनात)
- रूट डायरेक्टरी में `.env` फ़ाइल जिसमें Azure क्रेडेंशियल्स हों (मॉड्यूल 01 में `azd up` द्वारा बनाई गई)

> **नोट:** यदि आपने मॉड्यूल 01 पूरा नहीं किया है, तो पहले वहाँ दी गई तैनाती निर्देशकों का पालन करें।

## प्रांप्ट इंजीनियरिंग को समझना

<img src="../../../translated_images/hi/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

प्रांप्ट इंजीनियरिंग ऐसी इनपुट टेक्स्ट डिजाइन करने की प्रक्रिया है जो लगातार आपको आपकी आवश्यकताओं के अनुरूप परिणाम देती है। यह केवल प्रश्न पूछने के बारे में नहीं है - बल्कि इस बात के बारे में है कि आप अनुरोधों को इस तरह संरचित करें कि मॉडल पूरी तरह समझ सके कि आप क्या चाहते हैं और इसे कैसे पूरा करें।

इसे ऐसे सोचें जैसे किसी सहयोगी को निर्देश देना। "बग ठीक करो" अस्पष्ट है। "UserService.java की लाइन 45 में null pointer exception को null चेक डालकर ठीक करें" विशिष्ट है। भाषा मॉडल भी ऐसा ही काम करते हैं - विशिष्टता और संरचना महत्वपूर्ण होती है।

<img src="../../../translated_images/hi/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j इंफ्रास्ट्रक्चर प्रदान करता है — मॉडल कनेक्शन, मेमोरी, और संदेश प्रकार — जबकि प्रांप्ट पैटर्न केवल सावधानीपूर्वक संरचित टेक्स्ट होते हैं जो उस इंफ्रास्ट्रक्चर के माध्यम से भेजे जाते हैं। मुख्य निर्माण खंड हैं `SystemMessage` (जो AI के व्यवहार और भूमिका को निर्धारित करता है) और `UserMessage` (जो आपकी वास्तविक रिक्वेस्ट ले जाता है)।

## प्रांप्ट इंजीनियरिंग के मूल तत्व

<img src="../../../translated_images/hi/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

इस मॉड्यूल में उन्नत पैटर्न पर जाने से पहले, आइए पांच बुनियादी प्रांप्टिंग तकनीकों की समीक्षा करें। ये वे आधार हैं जिनसे हर प्रांप्ट इंजीनियर को परिचित होना चाहिए। यदि आप पहले से [Quick Start मॉड्यूल](../00-quick-start/README.md#2-prompt-patterns) से गुजर चुके हैं, तो आपने इन्हें देखा होगा — यहाँ उनका वैचारिक ढांचा है।

### शून्य-शॉट प्रांप्टिंग

सबसे सरल तरीका: मॉडल को बिना किसी उदाहरण के सीधे निर्देश देना। मॉडल पूरी तरह अपने प्रशिक्षण पर निर्भर रहता है कि वह कार्य को समझे और निष्पादित करे। यह उन सरल अनुरोधों के लिए अच्छा काम करता है जहाँ अपेक्षित व्यवहार स्पष्ट होता है।

<img src="../../../translated_images/hi/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*उदाहरणों के बिना सीधे निर्देश — मॉडल स्वयं निर्देश से कार्य निष्पादित करता है*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// प्रतिक्रिया: "सकारात्मक"
```

**कब उपयोग करें:** सरल वर्गीकरण, सीधे प्रश्न, अनुवाद, या कोई भी कार्य जिसे मॉडल अतिरिक्त मार्गदर्शन के बिना संभाल सकता है।

### कुछ-शॉट प्रांप्टिंग

ऐसे उदाहरण प्रदान करें जो वह पैटर्न दिखाएं जिसे आप मॉडल से अपनाना चाहते हैं। मॉडल आपके उदाहरणों से अपेक्षित इनपुट-आउटपुट फॉर्मेट सीखता है और नए इनपुट पर लागू करता है। यह उन कार्यों के लिए सुसंगतता नाटकीय रूप से सुधारता है जहाँ वांछित फॉर्मेट या व्यवहार स्पष्ट नहीं होता।

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

**कब उपयोग करें:** कस्टम वर्गीकरण, सुसंगत फॉर्मेटिंग, डोमेन-विशिष्ट कार्य, या जब शून्य-शॉट परिणाम असंगत हों।

### चैन ऑफ थॉट

मॉडल से चरण-दर-चरण तर्क दिखाने को कहें। सीधे उत्तर पर पहुंचने के बजाय, मॉडल समस्या को तोड़ता है और प्रत्येक भाग पर स्पष्ट रूप से काम करता है। यह गणित, तर्क, और बहु-चरणीय तर्क कार्यों पर सटीकता बढ़ाता है।

<img src="../../../translated_images/hi/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*चरण-दर-चरण तर्क — जटिल समस्याओं को स्पष्ट तार्किक चरणों में विभाजित करना*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// मॉडल दिखाता है: 15 - 8 = 7, फिर 7 + 12 = 19 सेब
```

**कब उपयोग करें:** गणित की समस्याएं, तर्क पहेलियाँ, डीबगिंग, या कोई भी कार्य जहाँ तर्क प्रक्रिया दिखाने से सटीकता और विश्वास बढ़ता हो।

### भूमिका-आधारित प्रांप्टिंग

अपना प्रश्न पूछने से पहले AI के लिए एक व्यक्तित्व या भूमिका निर्धारित करें। यह प्रतिक्रिया के स्वर, गहराई, और फोकस को आकार देता है। "सॉफ्टवेयर आर्किटेक्ट" से सलाह "जूनियर डेवलपर" या "सुरक्षा ऑडिटर" से अलग होती है।

<img src="../../../translated_images/hi/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*संदर्भ और भूमिका निर्धारित करना — एक ही प्रश्न को अलग-अलग भूमिकाओं के हिसाब से अलग जवाब मिलना*

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

**कब उपयोग करें:** कोड समीक्षा, ट्यूटोरिंग, डोमेन-विशिष्ट विश्लेषण, या जब आपको किसी विशेष विशेषज्ञता स्तर या दृष्टिकोण के लिए अनुकूलित उत्तर चाहिए।

### प्रांप्ट टेम्पलेट्स

प्रांप्ट को पुन: उपयोग योग्य बनाएं जिसमें वैरिएबल प्लेसहोल्डर्स हो। हर बार नया प्रांप्ट लिखने की बजाय, एक बार टेम्पलेट बनाएं और अलग-अलग मान भरें। LangChain4j की `PromptTemplate` क्लास `{{variable}}` सिंटैक्स के साथ इसे आसान बनाती है।

<img src="../../../translated_images/hi/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*वैरिएबल प्लेसहोल्डर्स के साथ पुन: उपयोग योग्य प्रांप्ट — एक टेम्पलेट, कई उपयोग*

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

**कब उपयोग करें:** बार-बार पूछे जाने वाले प्रश्न अलग इनपुट के साथ, बैच प्रोसेसिंग, पुन: उपयोग योग्य AI वर्कफ़्लोज़ बनाने के लिए, या जब प्रांप्ट संरचना समान रहे लेकिन डेटा बदलता हो।

---

ये पांच मूल बातें अधिकांश प्रांप्टिंग कार्यों के लिए एक ठोस टूलकिट प्रदान करती हैं। इस मॉड्यूल का बाकी हिस्सा इन्हीं पर आधारित है आठ उन्नत पैटर्न के साथ, जो GPT-5.2 के तर्क नियंत्रण, आत्म-मूल्यांकन, और संरचित आउटपुट क्षमताओं का लाभ उठाते हैं।

## उन्नत पैटर्न

मूल बातों को कवर करने के बाद, आइए उन आठ उन्नत पैटर्न पर जाएं जो इस मॉड्यूल को विशिष्ट बनाते हैं। सभी समस्याएँ एक जैसी नहीं होतीं। कुछ प्रश्नों को जल्दी उत्तर चाहिए, कुछ को गहरा सोच। कुछ को दृश्य तर्क चाहिए, कुछ को केवल परिणाम। नीचे प्रत्येक पैटर्न एक अलग परिदृश्य के लिए अनुकूलित है — और GPT-5.2 का तर्क नियंत्रण इन भिन्नताओं को और भी स्पष्ट कर देता है।

<img src="../../../translated_images/hi/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*आठ प्रांप्ट इंजीनियरिंग पैटर्न और उनके उपयोग मामलों का अवलोकन*

<img src="../../../translated_images/hi/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 का तर्क नियंत्रण आपको यह निर्दिष्ट करने देता है कि मॉडल को कितना सोचना चाहिए — तेज़ सीधे उत्तर से लेकर गहरे अन्वेषण तक*

**कम उत्साह (तेज़ & फोकस्ड)** - सरल प्रश्नों के लिए जहां आपको तेज़, सीधे उत्तर चाहिए। मॉडल न्यूनतम तर्क करता है - अधिकतम 2 चरण। इसका उपयोग गणनाओं, लुकअप या सीधे प्रश्नों के लिए करें।

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
> - "कम उत्साह और उच्च उत्साह प्रांप्टिंग पैटर्न में क्या अंतर है?"
> - "प्रांप्ट में XML टैग AI की प्रतिक्रिया को कैसे संरचित करने में मदद करते हैं?"
> - "स्व-परावर्तन पैटर्न और सीधे निर्देश कब इस्तेमाल करें?"

**उच्च उत्साह (गहरा & thorough)** - जटिल समस्याओं के लिए जहाँ आप व्यापक विश्लेषण चाहते हैं। मॉडल गहराई से खोज करता है और विस्तृत तर्क दिखाता है। इसका उपयोग सिस्टम डिज़ाइन, आर्किटेक्चर निर्णय, या जटिल शोध के लिए करें।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**कार्य निष्पादन (चरण-दर-चरण प्रगति)** - बहु-चरण वर्कफ़्लो के लिए। मॉडल पहले एक योजना प्रदान करता है, हर चरण का वर्णन करता है, फिर सारांश देता है। इसका उपयोग माइग्रेशन, इम्प्लीमेंटेशन, या किसी भी बहु-चरण प्रक्रिया के लिए करें।

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

चेन-ऑफ-थॉट प्रांप्टिंग से मॉडल से तर्क प्रक्रिया दिखाने को कहा जाता है, जो जटिल कार्यों की सटीकता सुधारता है। चरण-दर-चरण विश्लेषण मानव और AI दोनों को तर्क समझने में मदद करता है।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ प्रयास करें:** इस पैटर्न के बारे में पूछें:
> - "लंबे ऑपरेशनों के लिए कार्य निष्पादन पैटर्न को कैसे अनुकूलित करूँ?"
> - "उत्पादन अनुप्रयोगों में टूल प्रीएम्बल्स को संरचित करने के सर्वोत्तम अभ्यास क्या हैं?"
> - "UI में मध्यवर्ती प्रगति अद्यतनों को कैसे कैप्चर और प्रदर्शित कर सकता हूँ?"

<img src="../../../translated_images/hi/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*बहु-चरण कार्यों के लिए योजना → निष्पादित करें → सारांश वर्कफ़्लो*

**स्व-परावर्तित कोड** - उत्पादन-गुणवत्ता कोड बनाने के लिए। मॉडल उत्पादन मानकों का पालन करते हुए कोड बनाता है जिसमें उचित त्रुटि हैंडलिंग शामिल है। नए फीचर्स या सेवाएँ बनाने के लिए इसका उपयोग करें।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/hi/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*पुनरावृत्ति सुधार लूप - जेनरेट करें, मूल्यांकन करें, मुद्दे पहचानें, सुधार करें, दोहराएं*

**संरचित विश्लेषण** - सुसंगत मूल्यांकन के लिए। मॉडल एक स्थिर फ्रेमवर्क के तहत कोड की समीक्षा करता है (सहीपन, प्रथाएँ, प्रदर्शन, सुरक्षा, अनुरक्षितता)। इसे कोड समीक्षा या गुणवत्ता मूल्यांकन के लिए उपयोग करें।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैट के साथ प्रयास करें:** संरचित विश्लेषण के बारे में पूछें:
> - "मुझे विभिन्न प्रकार की कोड समीक्षाओं के लिए विश्लेषण फ्रेमवर्क कैसे अनुकूलित करना चाहिए?"
> - "संरचित आउटपुट को प्रोग्रामेटिक रूप से पार्स और उपयोग करने का सर्वोत्तम तरीका क्या है?"
> - "अलग-अलग समीक्षा सत्रों में सुसंगत गंभीरता स्तर कैसे सुनिश्चित करूँ?"

<img src="../../../translated_images/hi/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*गंभीरता स्तरों के साथ सुसंगत कोड समीक्षा के लिए फ्रेमवर्क*

**मल्टी-टर्न चैट** - ऐसे संवाद जिनमें संदर्भ आवश्यक हो। मॉडल पिछले संदेशों को याद रखता है और उस पर आधारित चर्चा करता है। इंटरैक्टिव सहायता सत्र या जटिल प्रश्नोत्तर के लिए इसका उपयोग करें।

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

*कई टर्न में वार्तालाप संदर्भ कैसे जमा होता है जब तक कि टोकन सीमा न पहुँच जाए*

**चरण-दर-चरण तर्क** - उन समस्याओं के लिए जिनमें दृश्य तर्क आवश्यक हो। मॉडल हर चरण के लिए स्पष्ट तर्क दिखाता है। गणित, तर्क पहेलियाँ, या सोच प्रक्रिया समझने के लिए इसका उपयोग करें।

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

**प्रतिबंधित आउटपुट** - उन प्रतिक्रियाओं के लिए जिनमें विशेष फॉर्मेट आवश्यकताएँ हों। मॉडल कड़ाई से फॉर्मेट और लंबाई नियमों का पालन करता है। सारांश या सटीक आउटपुट संरचना के लिए इसका उपयोग करें।

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

*विशिष्ट फॉर्मेट, लंबाई और संरचना नियम लागू करना*

## मौजूदा Azure संसाधनों का उपयोग

**तैनाती सत्यापित करें:**

सुनिश्चित करें कि रूट डायरेक्टरी में `.env` फ़ाइल मौजूद है जिसमें Azure क्रेडेंशियल्स हो (मॉड्यूल 01 के दौरान बनाई गई):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दिखाना चाहिए
```

**एप्लिकेशन शुरू करें:**

> **नोट:** यदि आपने पहले से मॉड्यूल 01 से `./start-all.sh` का उपयोग करके सभी एप्लिकेशन शुरू कर रखे हैं, तो यह मॉड्यूल पहले से ही पोर्ट 8083 पर चल रहा है। आप नीचे दिए गए शुरू करने के आदेश छोड़ कर सीधे http://localhost:8083 पर जा सकते हैं।

**विकल्प 1: स्प्रिंग बूट डैशबोर्ड का उपयोग (VS कोड उपयोगकर्ताओं के लिए अनुशंसित)**

डेव कंटेनर में स्प्रिंग बूट डैशबोर्ड एक्सटेंशन शामिल है, जो सभी स्प्रिंग बूट एप्लिकेशन को प्रबंधित करने के लिए एक दृश्य इंटरफ़ेस प्रदान करता है। आप इसे VS कोड की Activity Bar के बाएँ हिस्से में स्प्रिंग बूट आइकन के रूप में देख सकते हैं।

स्प्रिंग बूट डैशबोर्ड से आप:
- कार्यस्थान में सभी उपलब्ध स्प्रिंग बूट एप्लिकेशन देख सकते हैं
- एक क्लिक में एप्लिकेशन शुरू/रोक सकते हैं
- वास्तविक समय में एप्लिकेशन लॉग देख सकते हैं
- एप्लिकेशन की स्थिति मॉनिटर कर सकते हैं
सिर्फ "prompt-engineering" के बगल में प्ले बटन पर क्लिक करें इस मॉड्यूल को शुरू करने के लिए, या सभी मॉड्यूल एक साथ शुरू करें।

<img src="../../../translated_images/hi/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**विकल्प 2: शेल स्क्रिप्ट का उपयोग करना**

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

दोनों स्क्रिप्ट्स स्वचालित रूप से रूट `.env` फ़ाइल से पर्यावरण चर लोड करते हैं और यदि JAR मौजूद नहीं हैं तो वे उन्हें बनाएंगे।

> **नोट:** यदि आप शुरू करने से पहले सभी मॉड्यूल को मैन्युअल रूप से बनाना पसंद करते हैं:
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
./stop.sh  # यह मॉड्यूल केवल
# या
cd .. && ./stop-all.sh  # सभी मॉड्यूल्स
```

**PowerShell:**
```powershell
.\stop.ps1  # केवल यह मॉड्यूल
# या
cd ..; .\stop-all.ps1  # सभी मॉड्यूल्स
```

## एप्लिकेशन स्क्रीनशॉट्स

<img src="../../../translated_images/hi/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*मुख्य डैशबोर्ड जो सभी 8 प्रॉम्प्ट इंजीनियरिंग पैटर्न को उनकी विशेषताओं और उपयोग मामलों के साथ दिखाता है*

## पैटर्न्स का अन्वेषण

वेब इंटरफ़ेस आपको विभिन्न प्रॉम्प्टिंग रणनीतियों के साथ प्रयोग करने देता है। प्रत्येक पैटर्न अलग-अलग समस्याओं को हल करता है - उन्हें आज़माएं और देखें कि कब कौन सा तरीका सबसे अच्छा काम करता है।

### कम बनाम अधिक उत्साह

कम उत्साह का उपयोग करके "15% का 200 क्या है?" जैसे सरल प्रश्न पूछें। आपको तुरंत, सीधे उत्तर मिलेगा। अब कुछ जटिल पूछें जैसे "एक उच्च-ट्रैफिक API के लिए कैशिंग रणनीति डिज़ाइन करें" उच्च उत्साह के साथ। देखें कि मॉडल धीमा होता है और विस्तृत तर्क प्रदान करता है। वही मॉडल, वही प्रश्न संरचना - लेकिन प्रॉम्प्ट बताता है कि कितनी सोच करनी है।

<img src="../../../translated_images/hi/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*कम से कम तर्क के साथ त्वरित गणना*

<img src="../../../translated_images/hi/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*व्यापक कैशिंग रणनीति (2.8MB)*

### टास्क निष्पादन (टूल प्रीएम्बल्स)

बहु-चरण वर्कफ़्लो पूर्व नियोजन और प्रगति कथा से लाभान्वित होते हैं। मॉडल बताता है कि वह क्या करेगा, प्रत्येक चरण का वर्णन करता है, फिर परिणामों का सारांश प्रस्तुत करता है।

<img src="../../../translated_images/hi/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*चरण-दर-चरण कथा के साथ REST एंडपॉइंट बनाना (3.9MB)*

### आत्म-परावर्तित कोड

"एक ईमेल सत्यापन सेवा बनाएं" आज़माएं। केवल कोड उत्पन्न करने और रोकने की बजाय, मॉडल उत्पन्न करता है, गुणवत्ता मानदंडों के खिलाफ मूल्यांकन करता है, कमजोरियों की पहचान करता है, और सुधार करता है। आप देखेंगे कि यह तब तक पुनरावृत्ति करता है जब तक कोड उत्पादन मानकों को पूरा नहीं करता।

<img src="../../../translated_images/hi/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*पूर्ण ईमेल सत्यापन सेवा (5.2MB)*

### संरचित विश्लेषण

कोड समीक्षाओं के लिए सुसंगत मूल्यांकन फ्रेमवर्क आवश्यक हैं। मॉडल कोड का विश्लेषण करता है निर्धारित श्रेणियों (सहीपन, अभ्यास, प्रदर्शन, सुरक्षा) और गंभीरता स्तरों के साथ करता है।

<img src="../../../translated_images/hi/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*फ्रेमवर्क-आधारित कोड समीक्षा*

### बहु-चरणीय चैट

पूछें "स्प्रिंग बूट क्या है?" फिर तुरंत "मुझे एक उदाहरण दिखाओ" प्रश्न करें। मॉडल आपकी पहली प्रश्न को याद रखता है और विशेष रूप से स्प्रिंग बूट उदाहरण देता है। बिना मेमोरी के, दूसरा प्रश्न बहुत अस्पष्ट होता।

<img src="../../../translated_images/hi/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*प्रश्नों के बीच संदर्भ संरक्षण*

### चरण-दर-चरण तर्क

कोई गणित समस्या चुनें और इसे दोनों के साथ आज़माएं - चरण-दर-चरण तर्क और कम उत्साह। कम उत्साह केवल उत्तर देता है - तेज़ लेकिन अस्पष्ट। चरण-दर-चरण आपको हर गणना और निर्णय दिखाता है।

<img src="../../../translated_images/hi/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*स्पष्ट चरणों के साथ गणित की समस्या*

### सीमित आउटपुट

जब आपको विशेष प्रारूप या शब्द गणना की आवश्यकता हो, तो यह पैटर्न सख्ती से पालन सुनिश्चित करता है। ठीक 100 शब्दों में बुलेट प्वाइंट फॉर्मेट में सारांश उत्पन्न करने का प्रयास करें।

<img src="../../../translated_images/hi/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*मशीन लर्निंग सारांश प्रारूप नियंत्रण के साथ*

## आप वास्तव में क्या सीख रहे हैं

**तर्क प्रयास सब कुछ बदल देता है**

GPT-5.2 आपको आपके प्रॉम्प्ट के माध्यम से गणनात्मक प्रयास को नियंत्रित करने देता है। कम प्रयास का मतलब है तेज़ प्रतिक्रियाएं बिना अधिक खोज के। अधिक प्रयास का मतलब है कि मॉडल गहराई से सोचने के लिए समय लेता है। आप सीख रहे हैं कि प्रयास को कार्य की जटिलता के अनुसार मिलाएं - सरल प्रश्नों पर समय बर्बाद न करें, लेकिन जटिल निर्णयों में जल्दबाजी न करें।

**संरचना व्यवहार को निर्देशित करती है**

प्रॉम्प्ट में XML टैग देखें? वे सजावट नहीं हैं। मॉडल संरचित निर्देशों का पालन मुक्त रूप पाठ से अधिक विश्वसनीयता से करता है। जब आपको बहु-चरण प्रक्रियाओं या जटिल तर्क की आवश्यकता होती है, तो संरचना मॉडल को यह ट्रैक करने में मदद करती है कि यह कहाँ है और अगला क्या है।

<img src="../../../translated_images/hi/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*स्पष्ट खंडों और XML-शैली संगठन के साथ एक सुव्यवस्थित प्रॉम्प्ट की संरचना*

**आत्म-मूल्यांकन के माध्यम से गुणवत्ता**

स्वयं-परावर्तित पैटर्न गुणवत्ता मापदंडों को स्पष्ट बनाकर काम करते हैं। मॉडल "सही" करने की आशा करने के बजाय, आप इसे ठीक-ठीक बताते हैं कि "सही" का मतलब क्या है: सही तर्क, त्रुटि प्रबंधन, प्रदर्शन, सुरक्षा। फिर मॉडल अपनी आउटपुट का मूल्यांकन कर सकता है और सुधार कर सकता है। यह कोड उत्पन्न करने को लॉटरी से प्रक्रिया में बदल देता है।

**संदर्भ सीमित है**

बहु-चरणीय बातचीत हर अनुरोध के साथ संदेश इतिहास शामिल करके काम करती है। लेकिन एक सीमा है - हर मॉडल की अधिकतम टोकन गणना होती है। जैसे-जैसे बातचीत बढ़ती है, आपको प्रासंगिक संदर्भ को बनाए रखने के लिए रणनीतियाँ चाहिए बिना उस सीमा को पार किए। यह मॉड्यूल आपको दिखाता है कि मेमोरी कैसे काम करती है; बाद में आप सीखेंगे कि कब सारांश बनाना है, कब भूलना है, और कब पुनःप्राप्त करना है।

## अगले कदम

**अगला मॉड्यूल:** [03-rag - RAG (रिट्रीवल-ऑगमेंटेड जेनरेशन)](../03-rag/README.md)

---

**नेविगेशन:** [← पिछला: मॉड्यूल 01 - परिचय](../01-introduction/README.md) | [मुख्य पृष्ठ पर वापस जाएं](../README.md) | [अगला: मॉड्यूल 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यह दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। हम सटीकता के लिए प्रयासरत हैं, लेकिन कृपया ध्यान दें कि स्वचालित अनुवादों में त्रुटियां या असामंजस्य हो सकते हैं। मूल दस्तावेज़ अपनी मातृ भाषा में प्राधिकारित स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम ज़िम्मेदार नहीं होंगे।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->