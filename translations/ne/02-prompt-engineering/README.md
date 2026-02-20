# Module 02: GPT-5.2 सँग Prompt Engineering

## Table of Contents

- [तपाईंले के सिक्नुहुनेछ](../../../02-prompt-engineering)
- [पूर्व आवश्यकताहरू](../../../02-prompt-engineering)
- [Prompt Engineering बुझ्न](../../../02-prompt-engineering)
- [Prompt Engineering आधारहरू](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [Prompt Templates](../../../02-prompt-engineering)
- [उन्नत ढाँचा](../../../02-prompt-engineering)
- [अवस्थित Azure स्रोतहरू प्रयोग गर्दै](../../../02-prompt-engineering)
- [एप्लिकेशन स्क्रीनशटहरू](../../../02-prompt-engineering)
- [ढाँचाहरू अन्वेषण गर्दै](../../../02-prompt-engineering)
  - [कम र उच्च उत्साह](../../../02-prompt-engineering)
  - [कार्य कार्यान्वयन (टूल प्रीएम्बलहरू)](../../../02-prompt-engineering)
  - [आत्म-प्रतिबिम्बन कोड](../../../02-prompt-engineering)
  - [सङ्गठित विश्लेषण](../../../02-prompt-engineering)
  - [बहु-चरणीय संवाद](../../../02-prompt-engineering)
  - [क्रमबद्ध तर्क](../../../02-prompt-engineering)
  - [सीमित आउटपुट](../../../02-prompt-engineering)
- [तपाईं साँच्चिकै के सिक्दै हुनुहुन्छ](../../../02-prompt-engineering)
- [अर्को कदमहरू](../../../02-prompt-engineering)

## तपाईंले के सिक्नुहुनेछ

<img src="../../../translated_images/ne/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

अघिल्लो मोड्युलमा, तपाईंले स्मृतिले कसरि संवादात्मक AI सक्षम बनाउँछ भनेर हेर्नुभयो र GitHub मोडेलहरू प्रयोग गर्दै आधारभूत अन्तरक्रियाहरू गर्नुभयो। अब हामीले प्रश्न सोध्ने तरिकामा — अर्थात् prompts मा — केंद्रित हुनेछौं, Azure OpenAI को GPT-5.2 प्रयोग गर्दै। तपाईंले कसरि prompts संरचना गर्नुहुन्छ भन्ने कुराले तपाईंले पाउने प्रतिक्रिया गुणस्तरमा ठूलो प्रभाव पार्छ। हामीले आधारभूत prompting प्रविधिहरू समीक्षा गरेर सुरु गर्नेछौं, त्यसपछि GPT-5.2 का क्षमताहरूलाई पूर्ण रूपमा प्रयोग गर्ने आठ उन्नत ढाँचाहरूमा जान्छौं।

GPT-5.2 प्रयोग गर्नेछौं किनभने यसले reasoning नियन्त्रण परिचय गराउँछ - तपाईं मोडेललाई जवाफ दिनुअघि कति सोच्नुपर्ने हो भनी भन्न सक्नुहुन्छ। यसले विभिन्न prompting रणनीतिहरूलाई अझ स्पष्ट बनाउँछ र तपाईंलाई कति प्रयोग गर्ने बुझ्न मद्दत गर्छ। साथै GitHub मोडेलहरू भन्दा Azure मा GPT-5.2 को कम rate limit बाट पनि फाइदा हुनेछ।

## पूर्व आवश्यकताहरू

- Module 01 पूरा गरेको (Azure OpenAI स्रोतहरू परिचालित)
- रूट डाइरेक्टरीमा `.env` फाइल Azure प्रमाणपत्रहरू सहित (Module 01 मा `azd up` द्वारा सिर्जना गरिएको)

> **Note:** यदि तपाईंले Module 01 पूरा गर्नुभएको छैन भने, पहिले त्यहाँको परिचालन निर्देशनहरू पालना गर्नुहोस्।

## Prompt Engineering बुझ्न

<img src="../../../translated_images/ne/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

Prompt engineering भनेको तपाईंलाई आवश्यक परिणाम लगातार प्राप्त गर्ने इनपुट टेक्स्ट डिजाइन गर्ने कुरा हो। यो केवल प्रश्न सोध्ने कुरा मात्र होइन - यो अनुरोधहरू यस्तो तरिकाले संरचना गर्ने हो कि मोडेलले ठीकै बुझोस् कि तपाईं के चाहनुहुन्छ र कसरि प्रदान गर्ने।

यसलाई सहकर्मीलाई निर्देशन दिने जस्तै सोच्नुहोस्। "Bug ठीक गर" अस्पष्ट छ। "UserService.java लाइन 45 मा null pointer exception ठीक गर, null check थपेर" विशिष्ट छ। भाषा मोडेलहरू पनि यस्तै हुन्छन् - विशिष्टता र संरचनाले महत्व राख्छ।

<img src="../../../translated_images/ne/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j पूर्वाधार प्रदान गर्छ — मोडेल कनेक्शनहरू, मेमोरी, र मेसेज प्रकारहरू — जबकि prompt ढाँचाहरू त्यस पूर्वाधार मार्फत पठाइने सावधानीपूर्वक संरचित टेक्स्ट मात्र हुन्। मुख्य आधारहरू हुन् `SystemMessage` (जसले AI को व्यवहार र भूमिका सेट गर्छ) र `UserMessage` (जसले तपाईंको वास्तविक अनुरोध बोकेको हुन्छ)।

## Prompt Engineering आधारहरू

<img src="../../../translated_images/ne/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

यस मोड्युलका उन्नत ढाँचामा जानुअघि, पाँच आधारभूत prompting प्रविधिहरूको समीक्षा गरौं। यी ती आधारहरू हुन् जुन प्रत्येक prompt engineer ले जान्नैपर्छ। यदि तपाईंले पहिले नै [छिटो सुरूवात मोड्युल](../00-quick-start/README.md#2-prompt-patterns) हेर्नुभयो भने, यहाँको उनीहरूको वैचारिक फ्रेमवर्क हो।

### Zero-Shot Prompting

सबसँग सजिलो तरिका: मोडेललाई कुनै उदाहरण बिना सिधा निर्देशन दिनुहोस्। मोडेलले आफ्नो तालिममा भर पर्दै कार्य बुझेर पूरा गर्छ। यो साधारण अनुरोधका लागि राम्रो काम गर्छ जहाँ अपेक्षित व्यवहार स्पष्ट हुन्छ।

<img src="../../../translated_images/ne/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*उदाहरण बिना सिधा निर्देशन — मोडेलले निर्देशनबाट काम अनुमान लगाउँछ*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// प्रतिक्रिया: "सकारात्मक"
```

**कहिले प्रयोग गर्ने:** सरल वर्गीकरणहरू, सिधा प्रश्नहरू, अनुवादहरू, वा कुनै पनि कार्य जुन मोडेलले थप दिशानिर्देशन बिना गर्न सक्छ।

### Few-Shot Prompting

तपाईंले मोडेलले पालना गर्नुपर्ने ढाँचालाई देखाउने उदाहरणहरू दिनुहोस्। मोडेलले तपाईंका उदाहरणबाट अपेक्षित इनपुट-आउटपुट ढाँचा सिक्छ र नयाँ इनपुटमा लागू गर्दछ। यसले त्यस्ता कार्यहरूको स्थिरता धेरै सुधार्छ जहाँ चाहिएको ढाँचा वा व्यवहार स्पष्ट हुँदैन।

<img src="../../../translated_images/ne/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*उदाहरणहरूले सिकाइ — मोडेलले ढाँचाको पहिचान गरी नयाँ इनपुटमा लागू गर्छ*

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

**कहिले प्रयोग गर्ने:** कस्टम वर्गीकरण, निरन्तर ढाँचाकरण, डोमेन-विशिष्ट कार्यहरू, वा जहाँ zero-shot नतिजा अस्थिर हुन्छ।

### Chain of Thought

मोडेललाई आफ्नो तर्क चरण-दर-चरण देखाउन भन्नुहोस्। जवाफमा छिटो पुग्ने सट्टा मोडेलले समस्यालाई तोडेर हरेक भाग स्पष्ट रूपमा सम्बोधन गर्छ। यसले गणित, तर्क, र बहु-चरणीय तर्क कार्यमा शुद्धता बढाउँछ।

<img src="../../../translated_images/ne/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*चरण-दर-चरण तर्क — जटिल समस्याहरूलाई स्पष्ट तार्किक चरणहरूमा भण्डारण गर्दै*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// मोडेलले देखाउँछ: १५ - ८ = ७, त्यसपछि ७ + १२ = १९ स्याउ
```

**कहिले प्रयोग गर्ने:** गणित समस्याहरू, तर्क पहेलीहरू, डिबगीङ्ग, वा कुनै पनि कार्य जहाँ तर्क प्रक्रियाले शुद्धता र भरोसा बढाउँछ।

### Role-Based Prompting

AI लाई प्रश्न सोध्नु अघि कुनै व्यक्तित्व वा भूमिका सेट गर्नुहोस्। यसले जवाफको शैली, गहिराई, र केन्द्रबिन्दु निर्धारण गर्न सन्दर्भ प्रदान गर्छ। “Software architect” ले “junior developer” वा “security auditor” भन्दा फरक सल्लाह दिन्छ।

<img src="../../../translated_images/ne/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*सन्दर्भ र व्यक्तित्व सेट गर्दै — सोधिएको सोधाइलाई भूमिका अनुसार फरक जवाफ*

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

**कहिले प्रयोग गर्ने:** कोड समीक्षा, ट्युटरिंग, डोमेन-विशिष्ट विश्लेषण, वा जब तपाईंलाई विशेषज्ञताको निश्चित स्तर वा दृष्टिकोण अनुसार जवाफ चाहिन्छ।

### Prompt Templates

वैरिएबल प्लेसहोल्डरहरू सहित पुन: प्रयोग गर्न मिल्ने prompts सिर्जना गर्नुहोस्। प्रत्येक पटक नयाँ prompt लेख्ने सट्टा, एउटा ढाँचा परिभाषित गरेर फरक मानहरू भर्नुहोस्। LangChain4j को `PromptTemplate` वर्ग यसलाई `{{variable}}` सिन्ट्याक्सको साथ सजिलो बनाउँछ।

<img src="../../../translated_images/ne/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*वैरिएबल प्लेसहोल्डरहरूसँग पुनः प्रयोगयोग्य prompts — एक ढाँचा, धेरै प्रयोग*

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

**कहिले प्रयोग गर्ने:** फरक इनपुटहरू संग पुनरावृत्ति प्रश्नहरू, ब्याच प्रोसेसिंग, पुनः प्रयोगयोग्य AI वर्कफ्लो बनाउँदा, वा जुनसुकै स्थिति जहाँ prompt संरचना एउटै रहन्छ तर डाटा फरक हुन्छ।

---

यी पाँच आधारभूतहरूले तपाईंलाई अधिकांश prompting कार्यहरूको लागि दृढ उपकरण सेट दिन्छन्। यो मोड्युलको बाँकी भागले GPT-5.2 को reasoning नियन्त्रण, आत्म-मूल्यांकन, र संरचित आउटपुट क्षमताहरू प्रयोग गर्ने **आठ उन्नत ढाँचाहरू** मा आधारित हुन्छ।

## उन्नत ढाँचा

आधारभूतहरू कभर गरिसकेपछि, यो मोड्युललाई विशिष्ट बनाउने आठ उन्नत ढाँचाहरूमा जान्छौं। सबै समस्याले एउटै तरिका चाहँदैनन्। केही प्रश्नलाई छिटो जवाफ चाहिन्छ, केहीलाई गहिरो सोच। केहीलाई देखिने तर्क चाहिन्छ, केहीलाई केवल परिणाम। प्रत्येक तल दिइएको ढाँचा फरक परिदृश्यका लागि अनुकूलित छ — र GPT-5.2 को reasoning नियन्त्रणले यी भिन्नताहरू अझै स्पष्ट बनाउँछ।

<img src="../../../translated_images/ne/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*आठ prompt engineering ढाँचाहरूको अवलोकन र तिनका प्रयोग केसहरू*

<img src="../../../translated_images/ne/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 reasoning नियन्त्रणले मोडेलले कति सोच्ने भनी तपाईंलाई निर्दिष्ट गर्न दिन्छ — छिटो सिधा जवाफदेखि गहिरो अन्वेषणसम्म*

**कम उत्साह (छिटो र केन्द्रित)** - सरल प्रश्नहरूको लागि जहाँ तपाईं छिटो, सिधा जवाफ चाहानुहुन्छ। मोडेलले न्यूनतम २ चरणसम्म मात्र reasoning गर्छ। गणना, खोज, वा स्पष्ट प्रश्नहरूका लागि प्रयोग गर्नुहोस्।

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

> 💡 **GitHub Copilot सँग अन्वेषण गर्नुहोस्:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) खोल्नुहोस् र सोध्नुहोस्:
> - "कम उत्साह र उच्च उत्साह prompting ढाँचाहरूमा के फरक छ?"
> - "प्रम्टमा XML ट्यागहरूले AI को प्रतिक्रिया संरचना गर्न कसरी सहयोग गर्छन्?"
> - "म आत्म-प्रतिबिम्ब ढाँचाहरू र सिधा निर्देशन कहिले प्रयोग गर्ने?"

**उच्च उत्साह (गहिरो र पूर्ण)** - जटिल समस्याहरूका लागि जहाँ तपाईंलाई विस्तृत विश्लेषण चाहिन्छ। मोडेलले गहिरो अन्वेषण गर्दछ र विस्तृत तर्क देखाउँछ। सिस्टम डिजाइन, आर्किटेक्चर निर्णयहरू, वा जटिल अनुसन्धानका लागि प्रयोग गर्नुहोस्।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**कार्य कार्यान्वयन (क्रमबद्ध प्रगति)** - बहु-चरणीय कार्यहरूका लागि। मोडेलले योजना दिन्छ, काम गर्दा हरेक चरण वर्णन गर्छ, र अन्तमा सारांश दिन्छ। माइग्रेशनहरू, कार्यान्वयन, वा कुनै बहु-चरण प्रक्रियाका लागि प्रयोग गर्नुहोस्।

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

Chain-of-Thought prompting ले मोडेललाई आफ्नो reasoning प्रक्रिया देखाउन स्पष्ट रूपमा भन्छ, जसले जटिल कार्यहरूमा शुद्धता सुधार गर्दछ। चरण-दर-चरण तोडफोडले मानव र AI दुवैलाई तर्क बुझ्न मद्दत गर्छ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैटमा प्रयास गर्नुहोस्:** यस ढाँचाबारे सोध्नुहोस्:
> - "लामो समयसम्म चल्ने अपरेसनका लागि task execution ढाँचालाई कसरी अनुकुलित गर्ने?"
> - "उत्पादन एप्लिकेसनमा टूल प्रीएम्बलहरूको संरचनाका लागि उत्तम अभ्यासहरू के छन्?"
> - "UI मा मध्यवर्ती प्रगति अद्यावधिक कसरी कैद र प्रदर्शन गर्ने?"

<img src="../../../translated_images/ne/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*बहु-चरणीय कार्यहरूको लागि योजना → कार्यान्वयन → सारांश वर्कफ्लो*

**आत्म-प्रतिबिम्बन कोड** - उत्पादन गुणस्तरको कोड निर्माणका लागि। मोडेलले उत्पादन मापदण्ड अनुसार, उचित त्रुटि ह्यान्डलिङ सहित कोड निर्माण गर्छ। नयाँ सुविधाहरू वा सेवा बनाउँदा प्रयोग गर्नुहोस्।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ne/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*क्रमिक सुधार चक्र - निर्माण, मूल्याङ्कन, समस्या पहिचान, सुधार, पुनरावृत्ति*

**सङ्गठित विश्लेषण** - निरन्तर मूल्याङ्कनका लागि। मोडेलले कोडलाई निश्चित फ्रेमवर्क (सहीपन, अभ्यास, प्रदर्शन, सुरक्षा, मर्मतयोग्यता) बाट समीक्षा गर्छ। कोड समीक्षा वा गुणस्तर मूल्याङ्कनमा प्रयोग गर्नुहोस्।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) चैटमा प्रयास गर्नुहोस्:** सङ्गठित विश्लेषणका बारे सोध्नुहोस्:
> - "विभिन्न प्रकारका कोड समीक्षाका लागि विश्लेषण फ्रेमवर्क कसरि अनुकूलित गर्ने?"
> - "संरचित आउटपुटलाई प्रोग्रामिंग तरिकाले पार्स र कार्यान्वयन गर्न उत्तम तरिका के हो?"
> - "विभिन्न समीक्षा सत्रमा निरन्तर गम्भीरता स्तर कसरी सुनिश्चित गर्ने?"

<img src="../../../translated_images/ne/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*गम्भीरता स्तर सहित निरन्तर कोड समीक्षा फ्रेमवर्क*

**बहु-चरणीय संवाद** - सन्दर्भ आवश्यक संवादहरूको लागि। मोडेलले अघिल्ला सन्देशहरू सम्झन्छ र त्यसलाई आधार बनाएर निर्माण गर्छ। अन्तरक्रियात्मक सहयोग सत्र वा जटिल प्रश्नोत्तरमा प्रयोग गर्नुहोस्।

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ne/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*अनेक चरणहरूमा संवाद सन्दर्भ कसरी जम्मा हुन्छ र टोकन सीमा पुग्दासम्म*

**क्रमबद्ध तर्क** - दृश्य तर्क आवश्यक पर्दा। मोडेलले हरेक चरणको स्पष्ट तर्क देखाउँछ। गणित समस्याहरू, तर्क पहेलीहरू, वा सोच प्रक्रिया बुझ्न आवश्यक पर्दा प्रयोग गर्नुहोस्।

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ne/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*समस्याहरूलाई स्पष्ट तार्किक चरणहरूमा तोड्ने*

**सीमित आउटपुट** - विशेष ढाँचा आवश्यक पर्ने जवाफहरूको लागि। मोडेलले ढाँचा र लम्बाइ नियमहरूलाई कडाइका साथ पालन गर्छ। सारांशहरू वा आवश्यक स्पष्ट आउटपुट संरचनाका लागि प्रयोग गर्नुहोस्।

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

<img src="../../../translated_images/ne/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*विशेष ढाँचा, लम्बाइ, र संरचना आवश्यकता लागू गर्दै*

## अवस्थित Azure स्रोतहरू प्रयोग गर्दै

**परिचालन पुष्टि गर्नुहोस्:**

रूट डाइरेक्टरीमा `.env` फाइल Azure प्रमाणपत्रहरू सहित छ भन्ने सुनिश्चित गर्नुहोस् (Module 01 को समयमा सिर्जना गरिएको):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT देखाउनु पर्छ
```

**एप्लिकेशन सुरु गर्नुहोस्:**

> **Note:** यदि तपाईंले Module 01 बाट `./start-all.sh` प्रयोग गरी सबै एप्लिकेशन पहिले नै सुरु गर्नुभएको छ भने, यो मोड्युल पोर्ट 8083 मा पहिले नै चलिरहेको छ। तलको स्टार्ट कमाण्डहरू छोड्न सक्नुहुन्छ र सिधै http://localhost:8083 मा जानुहोस्।

**विकल्प १: Spring Boot Dashboard प्रयोग गर्दै (VS Code प्रयोगकर्ताहरूका लागि सिफारिस गरिएको)**

Dev container मा Spring Boot Dashboard एक्सटेन्सन समावेश छ, जसले सबै Spring Boot एप्लिकेशनहरू व्यवस्थित गर्न भिजुअल इन्टरफेस प्रदान गर्छ। तपाईंले VS Code को बायाँपट्टि Activity Bar मा Spring Boot आइकन हेर्न सक्नुहुन्छ।

Spring Boot Dashboard बाट तपाईंले:
- कार्यक्षेत्रका सबै उपलब्ध Spring Boot एप्लिकेशनहरू हेर्न सक्नुहुन्छ
- एक क्लिकमा एप्लिकेशनहरू सुरु/रोक्न सक्नुहुन्छ
- वास्तविक समयमा एप्लिकेशन लगहरू हेर्न सक्नुहुन्छ
- एप्लिकेशन स्थिति अनुगमन गर्न सक्नुहुन्छ
"prompt-engineering" को छेउमा प्ले बटन क्लिक गरेर यो मोड्युल सुरु गर्नुहोस्, वा सबै मोड्युलहरू एकसाथ सुरु गर्नुहोस्।

<img src="../../../translated_images/ne/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**विकल्प २: शेल स्क्रिप्टहरू प्रयोग गरी**

सबै वेब अनुप्रयोगहरू (मोड्युल ०१-०४) सुरु गर्ने:

**Bash:**
```bash
cd ..  # रुट निर्देशिकाबाट
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # रूट डिरेक्टरीबाट
.\start-all.ps1
```

वा केवल यस मोड्युललाई मात्रै सुरु गर्ने:

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

दुवै स्क्रिप्टहरूले अटोमेटिक रूपमा रूट `.env` फाइलबाट वातावरण चरहरू लोड गर्छन् र यदि JARहरू नभएमा निर्माण पनि गर्दछन्।

> **टिप्पणी:** यदि तपाईं सबै मोड्युलहरूलाई म्यानुअली निर्माण गरेर सुरु गर्न चाहनुहुन्छ भने:
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

ब्राउजरमा http://localhost:8083 खोल्नुहोस्।

**रोक्नका लागि:**

**Bash:**
```bash
./stop.sh  # मात्र यो मोड्युल
# वा
cd .. && ./stop-all.sh  # सबै मोड्युलहरू
```

**PowerShell:**
```powershell
.\stop.ps1  # यो मोड्युल मात्र
# वा
cd ..; .\stop-all.ps1  # सबै मोड्युलहरू
```

## अनुप्रयोगका स्क्रिनसटहरू

<img src="../../../translated_images/ne/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*मुख्य ड्यासबोर्ड जसले सबै ८ वटा प्रम्प्ट इन्जिनियरिङ् प्याटर्नहरू उनीहरूको विशेषताहरू र प्रयोग केसहरू सहित देखाउँछ*

## प्याटर्नहरूको अन्वेषण

वेब इन्टरफेसले तपाईंलाई फरक फरक प्रम्प्टिङ रणनीतिहरू संग प्रयोग गर्न दिन्छ। प्रत्येक प्याटर्नले फरक समस्याहरू समाधान गर्छ - प्रयोग गरेर हेर्नुहोस् जुन बेला कुन तरिका प्रभावकारी हुन्छ।

### कम बनाम उच्च उत्साह

"200 को १५% कति हुन्छ?" जस्तो सरल प्रश्न कम उत्साह प्रयोग गरेर सोध्नुहोस्। तपाईंलाई तत्काल, सिधा उत्तर प्राप्त हुन्छ। अब "एक उच्च-ट्राफिक API का लागि क्यासिङ रणनीति डिजाइन गर्नुहोस्" जस्तो जटिल कुरा उच्च उत्साह प्रयोग गरेर सोध्नुहोस्। मोडल बिस्तारै सोच्दै विस्तृत तर्क प्रदान गर्छ। एउटै मोडल, एउटै प्रश्न संरचना - तर प्रम्प्टले तन्काउँछ कति सोच्नुपर्छ भनेर।

<img src="../../../translated_images/ne/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*कम विचारसँग तुरुन्त गणना*

<img src="../../../translated_images/ne/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*विस्तृत क्यासिङ रणनीति (२.८MB)*

### कार्य कार्यान्वयन (टूल प्रीएम्बलहरू)

बहु-चरण कार्यहरूलाई पूर्व योजना र प्रगतिशील कथनले फाइदा पुर्‍याउँछ। मोडलले के गर्नेछ भनेर कुरूप तरिकाले समेट्छ, प्रत्येक चरण वर्णन गर्छ र अन्त्यमा नतिजा संक्षेप गर्छ।

<img src="../../../translated_images/ne/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*चरण-द्वारा-चरण कथन सहित REST एन्डपोइन्ट बनाउँदै (३.९MB)*

### आत्म-प्रतिबिम्बित कोड

"एक ईमेल मान्यता सेवा बनाउनुहोस्" प्रयास गर्नुहोस्। केवल कोड मात्र निर्माण गरी रोक्ने होइन, मोडलले निर्माण गर्छ, गुणस्तर मापदण्डका विरुद्ध मूल्यांकन गर्छ, कमजोरीहरू पत्ता लगाउँछ र सुधार गर्दछ। तपाईंले देख्नुहुनेछ कि यसले उत्पादन स्तर सम्म पुग्दासम्म निरन्तर सुधार गर्दछ।

<img src="../../../translated_images/ne/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*पूर्ण ईमेल मान्यता सेवा (५.२MB)*

### संरचित विश्लेषण

कोड समीक्षा निरन्तर मूल्याङ्कन ढाँचाहरू आवश्यक पर्छन्। मोडलले कोडलाई निश्चित वर्गहरू (सहीपन, अभ्यास, प्रदर्शन, सुरक्षा) र गम्भीरता स्तरहरू प्रयोग गरेर विश्लेषण गर्छ।

<img src="../../../translated_images/ne/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*ढाँचामा आधारित कोड समीक्षा*

### बहु-चरण चैट

"Spring Boot के हो?" सोध्नुहोस् र तुरुन्तै "एक उदाहरण देखाउनुहोस्" भनी फलोअप गर्नुहोस्। मोडलले तपाईंको पहिलो प्रश्न सम्झन्छ र विशेष रूपमा Spring Boot को उदाहरण दिन्छ। सम्झना नभए त्यो दोस्रो प्रश्न धेरै अस्पष्ट हुने थियो।

<img src="../../../translated_images/ne/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*प्रश्नहरू बीच सन्दर्भ संरक्षक*

### चरण-द्वारा-चरण तर्क

कुनै गणित समस्या छान्नुहोस् र यसलाई चरण-द्वारा-चरण तर्क र कम उत्साह दुवै संग प्रयास गर्नुहोस्। कम उत्साहले तपाईंलाई छिटो तर अस्पष्ट उत्तर दिन्छ। चरण-द्वारा-चरणले प्रत्येक गणना र निर्णय देखाउँछ।

<img src="../../../translated_images/ne/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*स्पष्ट चरणहरूको साथ गणित समस्या*

### प्रतिबन्धित आउटपुट

जब तपाईंलाई विशिष्ट ढाँचा वा शब्द गणना चाहिन्छ, यो प्याटर्नले कडाइका साथ पालना गराउँछ। ठ्याक्कै १०० शब्दको बुलेट पोइन्ट स्वरूपमा सारांश उत्पन्न गर्न प्रयास गर्नुहोस्।

<img src="../../../translated_images/ne/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*ढाँचा नियन्त्रण सहित मेसिन लर्निङ सारांश*

## तपाईं वास्तवमा के सिक्दै हुनुहुन्छ

**तर्क गर्ने प्रयासले सबै कुरा परिवर्तन गर्छ**

GPT-5.2 ले तपाईंलाई प्रम्प्टहरू मार्फत कम्प्युटेशनल प्रयास नियन्त्रण गर्न दिन्छ। कम प्रयासले छिटो प्रतिक्रिया र कम खोज प्रदान गर्छ। उच्च प्रयासले गहिरो सोच्न समय लिन्छ। तपाईं कार्य जटिलता अनुसार प्रयास मिलाउन सिक्दै हुनुहुन्छ - सरल प्रश्नहरूमा समय नष्ट नगर्नुहोस्, जटिल निर्णयहरूमा पनि हतार नगरौं।

**संरचनाले व्यवहारलाई निर्देशन दिन्छ**

प्रम्प्टहरूमा XML ट्यागहरू देख्नुभयो? यी सजावट होइनन्। मोडेलहरूले संरचित निर्देशनहरू स्वतन्त्र टेक्स्टभन्दा बढी भरपर्दो रूपमा पछ्याउँछन्। बहु-चरण प्रक्रिया वा जटिल तर्क आवश्यक पर्दा संरचनाले मोडललाई यसको स्थिति र आगामी के हो ट्रयाक गर्न मद्दत गर्छ।

<img src="../../../translated_images/ne/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*स्पष्ट खण्डहरू र XML-शैली संगठनसहित राम्रो संरचित प्रम्प्टको संरचना*

**आत्म-मूल्याङ्कन मार्फत गुणस्तर**

आत्म-प्रतिबिम्बित प्याटर्नहरूले गुणस्तर मापदण्डलाई स्पष्ट बनाउँछ। मोडलले "सही गर्छ" भन्ने आशा गर्नुको सट्टा तपाईं यसलाई ठ्याक्कै के अर्थ लाग्छ भनेर बताउनुहुन्छ: सही तर्क, त्रुटि ह्यान्डलिङ, प्रदर्शन, सुरक्षा। मोडल त्यसपछि आफ्नै आउटपुट मूल्याङ्कन गरी सुधार गर्न सक्छ। यसले कोड निर्माणलाई लटरीबाट प्रक्रिया बनाउँछ।

**सन्दर्भ सीमित हुन्छ**

बहु-चरण वार्तालापले प्रत्येक अनुरोधसँग सन्देश इतिहास समावेश गरेर काम गर्छ। तर सीमा छ - प्रत्येक मोडलको अधिकतम टोकन संख्या हुन्छ। वार्तालाप बढ्दा, तपाईंले सान्दर्भिक सन्दर्भ राख्न तर त्यो सीमा नाघ्न नदिन योजना बनाउनुपर्ने हुन्छ। यो मोड्युलले तपाईंलाई स्मृति कसरी काम गर्छ देखाउँछ; पछि तपाईलाई कहिले सारांश गर्न, कहिले विर्सन र कहिले पुनः प्राप्त गर्न सिकाइनेछ।

## अगाडि कदमहरू

**अर्को मोड्युल:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**नेभिगेसन:** [← पहिले: मोड्युल ०१ - परिचय](../01-introduction/README.md) | [मुख्यमा फर्कनुहोस्](../README.md) | [अर्को: मोड्युल ०३ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकृति**:
यस दस्तावेजलाई AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरी अनुवाद गरिएको छ। हामी शुद्धताको प्रयास गर्छौं भनेपनि, कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादमा त्रुटि वा गलतफहमी हुने सम्भावना रहन्छ। मूल दस्तावेज यसको मौलिक भाषामा अधिकृत स्रोतको रूपमा मानिनुपर्छ। महत्वपूर्ण जानकारीका लागि पेशेवर मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न हुने कुनै पनि गलत बुझाइ वा व्याख्या लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->