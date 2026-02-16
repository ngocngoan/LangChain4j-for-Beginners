# Module 02: GPT-5.2 सँग प्रॉम्प्ट ईन्जिनियरिङ

## सामग्री तालिका

- [तपाईं के सिक्नेछ](../../../02-prompt-engineering)
- [पूर्वआवश्यकताहरू](../../../02-prompt-engineering)
- [प्रॉम्प्ट ईन्जिनियरिङ बुझ्न](../../../02-prompt-engineering)
- [प्रॉम्प्ट ईन्जिनियरिङका आधारहरू](../../../02-prompt-engineering)
  - [शून्य-शट प्रॉम्प्टिङ](../../../02-prompt-engineering)
  - [केही-शट प्रॉम्प्टिङ](../../../02-prompt-engineering)
  - [चेन अफ थट](../../../02-prompt-engineering)
  - [भूमिका-आधारित प्रॉम्प्टिङ](../../../02-prompt-engineering)
  - [प्रॉम्प्ट टेम्प्लेटहरू](../../../02-prompt-engineering)
- [उन्नत ढाँचा](../../../02-prompt-engineering)
- [अझैको Azure स्रोतहरू प्रयोग गर्दै](../../../02-prompt-engineering)
- [आवेदनका स्क्रिनशटहरू](../../../02-prompt-engineering)
- [ढाँचाहरू अन्वेषण गर्दै](../../../02-prompt-engineering)
  - [तल्लो बनाम उच्च उत्साह](../../../02-prompt-engineering)
  - [कार्य सञ्चालन (टूल प्राम्बल)](../../../02-prompt-engineering)
  - [आत्म-प्रतिबिम्बित कोड](../../../02-prompt-engineering)
  - [संरचित विश्लेषण](../../../02-prompt-engineering)
  - [बहु-चरण संवाद](../../../02-prompt-engineering)
  - [चरण-द्वारा-चरण तर्क](../../../02-prompt-engineering)
  - [सीमित आउटपुट](../../../02-prompt-engineering)
- [तपाईं साँच्चिकै के सिक्दै हुनुहुन्छ](../../../02-prompt-engineering)
- [अगाडि के गर्ने](../../../02-prompt-engineering)

## तपाईं के सिक्नेछ

<img src="../../../translated_images/ne/what-youll-learn.c68269ac048503b2.webp" alt="तपाईं के सिक्नेछ" width="800"/>

अघिल्लो module मा, तपाईंले सम्झनाले कसरी संवादात्मक AI सक्षम गर्छ भन्ने देख्नुभयो र साधारण अन्तरक्रियाका लागि GitHub मोडेलहरू प्रयोग गर्नुभयो। अब हामी प्रश्नहरू कसरी सोध्ने—प्रॉम्प्टहरू आफैं—Azure OpenAI को GPT-5.2 प्रयोग गरेर केन्द्रित हुनेछौं। तपाईंको प्रॉम्प्ट कसरी संरचित हुन्छ भन्नाले प्राप्त प्रतिक्रियाको गुणस्तरमा ठूलो प्रभाव पार्छ। हामी आधारभूत प्रॉम्प्टिङ प्रविधिहरूको पुनरावलोकनबाट सुरु गर्नेछौं, त्यसपछि GPT-5.2 को क्षमताहरूको पूर्ण लाभ उठाउने आठ उन्नत ढाँचाहरूमा अघि बढ्नेछौं।

हामी GPT-5.2 प्रयोग गर्नेछौं किनभने यसले तर्क नियन्त्रणलाई परिचय गराउँछ - तपाईंले मोडेललाई उत्तर दिन अघि कति सोच्ने हो भन्न सक्नुहुन्छ। यसले विभिन्न प्रॉम्प्टिङ रणनीतिहरूलाई स्पष्ट पार्छ र तपाईंलाई कुन विधि कहिले प्रयोग गर्ने बुझ्न मद्दत गर्छ। हामीले GitHub मोडेलहरू भन्दा Azure को GPT-5.2 मा कम दर सीमाहरूबाट पनि लाभ उठाउनेछौं।

## पूर्वआवश्यकताहरू

- Module 01 पूरा गरेको (Azure OpenAI स्रोतहरू तैनाथ गरिएको)
- मूल निर्देशिकामा `.env` फाइल Azure प्रमाणपत्रहरू सहित (Module 01 मा `azd up` द्वारा सिर्जना गरिएको)

> **नोट:** यदि तपाईंले Module 01 पूरा गर्नुभएको छैन भने, पहिले त्यहाँको तैनाथ निर्देशनहरू पछ्याउनुहोस्।

## प्रॉम्प्ट ईन्जिनियरिङ बुझ्न

<img src="../../../translated_images/ne/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="प्रॉम्प्ट ईन्जिनियरिङ के हो?" width="800"/>

प्रॉम्प्ट ईन्जिनियरिङ भनेको इनपुट पाठ डिजाइन गर्ने कुरा हो जसले तपाईंलाई निरन्तर आवश्यक परिणामहरू दिन्छ। यो मात्र प्रश्न सोध्ने कुरा होइन - यो अनुरोधहरूलाई यस्तो तरिकाले संरचना गर्ने कुरा हो कि मोडेलले के चाहिन्छ र कसरी दिने बुझ्न सकोस्।

यसलाई सहकर्मीलाई निर्देशन दिने जस्तो सोच्नुहोस्। "बग ठीक गर" अस्पष्ट छ। "UserService.java को ४५ औं लाइनमा null pointer exception ठीक गर्न null check थप" स्पष्ट छ। भाषा मोडेलहरूले पनि त्यस्तै काम गर्छन् - specificity र संरचना महत्त्वपूर्ण छन्।

<img src="../../../translated_images/ne/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="LangChain4j कसरी फिट हुन्छ" width="800"/>

LangChain4j ले पूर्वाधार प्रदान गर्छ—मोडेल जडानहरू, स्मृति, र सन्देश प्रकारहरू—र प्रॉम्प्ट ढाँचाहरू त्यो पूर्वाधार मार्फत पठाइने सावधानीपूर्वक संरचित पाठ मात्र हुन्। मुख्य निर्माण ब्लकहरू हुन् `SystemMessage` (जसले AI को व्यवहार र भूमिका सेट गर्छ) र `UserMessage` (जसले तपाईंको वास्तविक अनुरोध बोक्छ)।

## प्रॉम्प्ट ईन्जिनियरिङका आधारहरू

<img src="../../../translated_images/ne/five-patterns-overview.160f35045ffd2a94.webp" alt="प्रॉम्प्ट ईन्जिनियरिङका पाँच ढाँचाहरूको सिंहावलोकन" width="800"/>

यस module का उन्नत ढाँचाहरूमा जानु अघि, पाँच आधारभूत प्रॉम्प्टिङ प्रविधिहरूको समीक्षा गरौं। यी त हरेक प्रॉम्प्ट ईन्जिनियरले जान्नै पर्ने आधारभूत भवन ब्लकहरू हुन्। यदि तपाईंले पहिले नै [Quick Start module](../00-quick-start/README.md#2-prompt-patterns) पूरा गर्नुभएको छ भने, तपाईंले यिनीहरूलाई अभ्यासमा देखिसक्नुभएको छ — यी पछाडि भएको वैचारिक रूपरेखा हो।

### शून्य-शट प्रॉम्प्टिङ

सबैभन्दा सरल तरीका: कुनै उदाहरण बिना मोडेललाई सिधा निर्देशन दिनु। मोडेलले आफ्नो प्रशिक्षणमा आधारित भएर मात्रै कार्य बुझ्छ र सञ्चालन गर्छ। जब अपेक्षित व्यवहार स्पष्ट छ त्यस्ता सहज अनुरोधहरूका लागि यो राम्रो काम गर्छ।

<img src="../../../translated_images/ne/zero-shot-prompting.7abc24228be84e6c.webp" alt="शून्य-शट प्रॉम्प्टिङ" width="800"/>

*उदाहरण बिना सिधा निर्देशन — मोडेल निर्देशनबाट मात्र कार्य अनुमान गर्छ*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// प्रतिक्रिया: "सकारात्मक"
```

**कहिले प्रयोग गर्ने:** सरल वर्गीकरणहरू, प्रत्यक्ष प्रश्नहरू, अनुवादहरू, वा कुनै पनि कार्य जसलाई मोडेलले अतिरिक्त निर्देशन बिना गर्न सक्छ।

### केही-शट प्रॉम्प्टिङ

तपाईंले चाहेको ढाँचामा मोडेलले काम गर्न उदाहरणहरू प्रदान गर्नुहोस्। मोडेलले तपाईंका उदाहरणहरूबाट अपेक्षित इनपुट-आउटपुट ढाँचालाई सिक्छ र नयाँ इनपुटहरूमा लागू गर्छ। यसले त्यस्ता कार्यहरूको लागि निरन्तरता नाटकीय रूपमा सुधार्छ जहाँ चाहिएको ढाँचा वा व्यवहार स्पष्ट हुँदैन।

<img src="../../../translated_images/ne/few-shot-prompting.9d9eace1da88989a.webp" alt="केही-शट प्रॉम्प्टिङ" width="800"/>

*उदाहरणहरूबाट सिक्दै — मोडेलले ढाँचा चिनेर नयाँ इनपुटहरूमा लागू गर्छ*

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

**कहिले प्रयोग गर्ने:** कस्टम वर्गीकरणहरू, निरन्तर ढाँचा, डोमेनविशेष कार्यहरू, वा जब शून्य-शट नतिजा असंगत हुन्छ।

### चेन अफ थट

मोडेललाई चरण-द्वारा-चरण आफ्नो तर्क देखाउन भन्नुहोस्। सिधै जवाफ दिनुको सट्टा, मोडेलले समस्या विभाजन गरी प्रत्येक भाग विस्तारमा काम गर्छ। यसले गणित, तर्क, र बहु-चरण तर्क कार्यहरूमा शुद्धता सुधार्छ।

<img src="../../../translated_images/ne/chain-of-thought.5cff6630e2657e2a.webp" alt="चेन अफ थट प्रॉम्प्टिङ" width="800"/>

*चरण-द्वारा-चरण तर्क — जटिल समस्याहरूलाई स्पष्ट तर्कका कदमहरूमा विभाजन गर्दै*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// मोडेल देखाउँछ: १५ - ८ = ७, त्यसपछि ७ + १२ = १९ स्याउहरू
```

**कहिले प्रयोग गर्ने:** गणितका समस्याहरू, तर्क पजलहरू, डिबगिङ, वा कुनै पनि कार्य जहाँ तर्क प्रक्रियाले शुद्धता र विश्वास सुधार गर्छ।

### भूमिका-आधारित प्रॉम्प्टिङ

तपाईंको प्रश्न सोध्नु अघि AI का लागि कुनै व्यक्तित्व वा भूमिका सेट गर्नुहोस्। यसले जवाफको भाषा, गहिराइ, र फोकसलाई आकार दिन्छ। "सफ्टवेयर आर्किटेक्ट" ले "जूनियर विकासकर्ता" वा "सुरक्षा अडिटर" भन्दा फरक सल्लाह दिन्छ।

<img src="../../../translated_images/ne/role-based-prompting.a806e1a73de6e3a4.webp" alt="भूमिका-आधारित प्रॉम्प्टिङ" width="800"/>

*सन्दर्भ र व्यक्तित्व सेट गर्दै — सोही प्रश्न फरक भूमिकामा फरक जवाफ पाउँछ*

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

**कहिले प्रयोग गर्ने:** कोड समीक्षा, ट्यूटोरिङ, डोमेन विशिष्ट विश्लेषण, वा जब तपाईंलाई विशेष विशेषज्ञता स्तर वा दृष्टिकोणअनुसार जवाफ चाहिन्छ।

### प्रॉम्प्ट टेम्प्लेटहरू

परिवर्ती प्लेसहोल्डरहरू सहित पुन: प्रयोगयोग्य प्रॉम्प्टहरू सिर्जना गर्नुहोस्। प्रत्येक पटक नयाँ प्रॉम्प्ट लेख्नुभन्दा, एउटा टेम्प्लेट पटक-पटक बनाउनुहोस् र फरक मानहरू भर्नुहोस्। LangChain4j को `PromptTemplate` वर्गले `{{variable}}` सिन्ट्याक्समार्फत यो सजिलो बनाउँछ।

<img src="../../../translated_images/ne/prompt-templates.14bfc37d45f1a933.webp" alt="प्रॉम्प्ट टेम्प्लेटहरू" width="800"/>

*परिवर्ती प्लेसहोल्डर सहित पुन: प्रयोगयोग्य प्रॉम्प्टहरू — एउटा टेम्प्लेट, धेरै प्रयोगहरू*

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

**कहिले प्रयोग गर्ने:** फरक इनपुटहरूसँग दोहोरिने प्रश्नहरू, ब्याच प्रशोधन, पुन: प्रयोगयोग्य AI वर्कफ्लोहरू बनाउने, वा त्यस्तो अवस्थामा जहाँ प्रॉम्प्ट संरचना स्थिर रहन्छ तर डाटा बदलिन्छ।

---

यी पाँच आधारहरूले तपाईंलाई प्रायः प्रॉम्प्टिङ कार्यहरूका लागि बलियो उपकरण सेट दिन्छ। बाँकी module ले ईनमा थपेर **आठ उन्नत ढाँचा** निर्माण गर्छ जसले GPT-5.2 को तर्क नियन्त्रण, आत्म-मूल्यांकन, र संरचित आउटपुट क्षमता प्रयोग गर्छ।

## उन्नत ढाँचा

आधारभूतलाई समेटिसकेपछि, अब आठ उन्नत ढाँचाहरूमा जान्छौं जुन यस module लाई अनौठो बनाउँछ। सबै समस्याहरूलाई एउटै तरिका चाहिदैन। केही प्रश्नलाई छिटो जवाफ चाहिन्छ, केहीलाई गहिरो सोच। केहीले देखिने तर्क चाहिन्छ, केहीलाई मात्र नतिजा चाहिए। प्रत्येक तलको ढाँचा फरक परिदृश्यका लागि अनुकूलित गरिएको छ — र GPT-5.2 को तर्क नियन्त्रणले यी फरकलाई अझ स्पष्ट बनाउँछ।

<img src="../../../translated_images/ne/eight-patterns.fa1ebfdf16f71e9a.webp" alt="आठ प्रॉम्प्टिङ ढाँचाहरू" width="800"/>

*आठ प्रॉम्प्ट ईन्जिनियरिङ ढाँचाहरू र तिनीहरूको प्रयोग केसहरूको सिंहावलोकन*

<img src="../../../translated_images/ne/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="GPT-5.2 सँग तर्क नियन्त्रण" width="800"/>

*GPT-5.2 को तर्क नियन्त्रणले तपाईंलाई मोडेलले कति सोच्नुपर्ने हो निर्दिष्ट गर्न दिन्छ — छिटो सिधा जवाफदेखि गहिरो अन्वेषणसम्म*

<img src="../../../translated_images/ne/reasoning-effort.db4a3ba5b8e392c1.webp" alt="तर्क प्रयास तुलना" width="800"/>

*तल्लो उत्साह (छिटो, सिधा) बनाम उच्च उत्साह (गहिरो, अन्वेषणपूर्ण) तर्कका दृष्टिकोणहरू*

**तल्लो उत्साह (छिटो & केन्द्रित)** - सरल प्रश्नहरूका लागि जहाँ तपाईं छिटो र सिधा जवाफ चाहनुहुन्छ। मोडेलले न्यूनतम तर्क गर्छ - अधिकतम २ कदम। गणना, खोज, वा सीधा प्रश्नहरूका लागि यसलाई प्रयोग गर्नुहोस्।

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

> 💡 **GitHub Copilot सँग अन्वेषण गर्नुहोस्:** खोल्नुहोस् [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) र सोध्नुहोस्:
> - "तल्लो उत्साह र उच्च उत्साह प्रॉम्प्टिङ ढाँचाहरूबीच कस्तो भिन्नता छ?"
> - "प्रॉम्प्टहरूमा XML ट्यागहरूले AI को प्रतिक्रिया कसरी संरचनाबद्ध गर्न मद्दत गर्छ?"
> - "कहिले आत्म-प्रतिबिम्ब प्रविधाहरू र सिधा निर्देशन प्रयोग गर्ने?"

**उच्च उत्साह (गहिरो & पूर्ण)** - जटिल समस्याहरूका लागि जहाँ तपाईं व्यापक विश्लेषण चाहनुहुन्छ। मोडेलले पूर्ण रूपमा अन्वेषण गर्छ र विस्तृत तर्क देखाउँछ। प्रणाली डिजाइन, संरचना निर्णय, वा जटिल अनुसन्धानका लागि प्रयोग गर्नुहोस्।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**कार्य सञ्चालन (चरण-द्वारा-चरण प्रगति)** - बहु-चरण कार्यप्रवाहहरूका लागि। मोडेलले सुरुमा योजना दिन्छ, काम गर्दा प्रत्येक चरणको वर्णन गर्छ, अनि संक्षेप प्रदान गर्छ। माइग्रेशन, कार्यान्वयन, वा कुनै पनि बहु-चरण प्रक्रियाका लागि।

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

चेन-अफ-थट प्रॉम्प्टिङले मोडेललाई स्पष्ट तर्क प्रक्रिया देखाउन भन्न्छ, जसले जटिल कार्यहरूमा शुद्धता सुधार्छ। चरण-द्वारा-चरण विभाजनले मानिस र AI दुवैलाई तर्क बुझ्न मद्दत गर्छ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat सँग प्रयास गर्नुहोस्:** यस ढाँचाबारे सोध्नुहोस्:
> - "लामो समय चल्ने कार्यहरूको लागि कार्य सञ्चालन ढाँचा कसरी अनुकूल गर्ने?"
> - "उत्पादन अनुप्रयोगहरूमा टूल प्राम्बल संरचनाका लागि के उत्कृष्ट अभ्यासहरू छन्?"
> - "UI मा मध्यवर्ती प्रगति अद्यावधिकहरू कसरी समात्ने र देखाउने?"

<img src="../../../translated_images/ne/task-execution-pattern.9da3967750ab5c1e.webp" alt="कार्य सञ्चालन ढाँचा" width="800"/>

*कार्य योजना → कार्यान्वयन → संक्षेप बहु-चरण कार्यहरूको लागि*

**आत्म-प्रतिबिम्बित कोड** - उत्पादन-गुणस्तर कोड उत्पन्न गर्ने। मोडेलले उत्पादन मापदण्ड अनुसार त्रुटि नियन्त्रण सहित कोड बनाउँछ। नयाँ सुविधाहरू वा सेवाहरू बनाउँदा प्रयोग गर्नुहोस्।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ne/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="आत्म-प्रतिबिम्ब चक्र" width="800"/>

*पुनरावृत्त सुधार चक्र - उत्पादन, मूल्यांकन, समस्या पहिचान, सुधार, दोहोर्याउनु*

**संरचित विश्लेषण** - निरन्तर मूल्याङ्कनका लागि। मोडेलले निश्चित फ्रेमवर्क (सहीपन, अभ्यासहरू, प्रदर्शन, सुरक्षा, मर्मतयोग्यता) अनुसार कोड समीक्षा गर्छ। कोड समीक्षा वा गुणस्तर मूल्याङ्कनका लागि प्रयोग गर्नुहोस्।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat सँग प्रयास गर्नुहोस्:** संरचित विश्लेषणबारे सोध्नुहोस्:
> - "विभिन्न प्रकारका कोड समीक्षाहरूका लागि विश्लेषण फ्रेमवर्क कसरी अनुकूल गर्ने?"
> - "संरचित आउटपुटलाई कार्यक्रमगत रुपमा पार्स गरी काम गर्ने सर्वोत्तम तरिका के हो?"
> - "विभिन्न समीक्षा सत्रहरूमा समानता कायम राख्ने तरिका के हो?"

<img src="../../../translated_images/ne/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="संरचित विश्लेषण ढाँचा" width="800"/>

*गम्भीरता स्तरहरूसहित निरन्तर कोड समीक्षा फ्रेमवर्क*

**बहु-चरण संवाद** - सन्दर्भ आवश्यक संवादहरूका लागि। मोडेलले अघिल्लो सन्देशहरू सम्झन्छ र तिनीहरूमा आधारित जवाफ दिन्छ। अन्तरक्रियात्मक मद्दत सत्र वा जटिल Q&A का लागि उपयुक्त।

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

<img src="../../../translated_images/ne/context-memory.dff30ad9fa78832a.webp" alt="सन्दर्भ स्मृति" width="800"/>

*अन्योल सीमामा पुगेसम्म बहु चरणसम्म संवाद सन्दर्भ कसरी संचित हुन्छ*

**चरण-द्वारा-चरण तर्क** - देखिने तर्क आवश्यक पर्ने समस्याहरूका लागि। मोडेलले प्रत्येक चरणको स्पष्ट तर्क देखाउँछ। गणित, तर्क पजलहरूमा वा सोच प्रक्रिया बुझ्न आवश्यक हुँदा प्रयोग गर्नुहोस्।

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ne/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="चरण-द्वारा-चरण ढाँचा" width="800"/>

*समस्याहरूलाई स्पष्ट तर्कीय कदमहरूमा विभाजन*

**सीमित आउटपुट** - विशेष ढाँचा आवश्यक जवाफका लागि। मोडेलले ढाँचा र लम्बाइ नियमहरू कडाईका साथ पालना गर्छ। सारांशहरू वा सटीक आउटपुट संरचना आवश्यक पर्दा प्रयोग गर्नुहोस्।

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

<img src="../../../translated_images/ne/constrained-output-pattern.0ce39a682a6795c2.webp" alt="सीमित आउटपुट ढाँचा" width="800"/>

*विशेष ढाँचा, लम्बाइ, र संरचना आवश्यकताहरू लागू गर्दै*

## अझैको Azure स्रोतहरू प्रयोग गर्दै

**तैनाथी पुष्टि गर्नुहोस्:**

Azure प्रमाणपत्रहरू सहितको `.env` फाइल मूल निर्देशिकामा अवस्थित छ भनेर सुनिश्चित गर्नुहोस् (Module 01 का क्रममा सिर्जना भएको):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT देखाउनु पर्छ
```

**अनुप्रयोग सुरु गर्नुहोस्:**

> **नोट:** यदि तपाईंले पहिले देखि नै Module 01 मा `./start-all.sh` प्रयोग गरी सबै अनुप्रयोगहरू सुरु गर्नुभएको छ भने, यो module 8083 पोर्टमा चलिरहेको छ। तपाईं तलको सुरु आदेशहरू छोडेर सिधै http://localhost:8083 मा जान सक्नुहुन्छ।

**विकल्प १: Spring Boot ड्यासबोर्ड प्रयोग गर्दै (VS Code प्रयोगकर्ताहरूका लागि सिफारिस गरिएको)**

डेभ कन्टेनरमा Spring Boot ड्यासबोर्ड एक्सटेन्सन समावेश छ, जसले सबै Spring Boot अनुप्रयोगहरू व्यवस्थित गर्ने भिजुअल इन्टरफेस प्रदान गर्दछ। VS Code को Activity Bar को बाँया पट्टि Spring Boot आइकन हेर्नुहोस्।
Spring Boot ड्यासबोर्डबाट, तपाईं सक्नुहुन्छ:
- कार्यक्षेत्रमा उपलब्ध सबै Spring Boot अनुप्रयोगहरू हेर्नुहोस्
- एक क्लिकमा अनुप्रयोगहरू सुरु/बन्द गर्नुहोस्
- वास्तविक समयमा अनुप्रयोग लगहरू हेर्नुहोस्
- अनुप्रयोगको स्थिति अनुगमन गर्नुहोस्

सिधै "prompt-engineering" को छेउमा प्ले बटन क्लिक गरेर यो मोड्युल सुरु गर्नुहोस्, वा सबै मोड्युलहरू एकैचोटि सुरु गर्नुहोस्।

<img src="../../../translated_images/ne/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**विकल्प २: shell script प्रयोग गर्दै**

सबै वेब अनुप्रयोगहरू (मोड्युल ०१-०४) सुरु गर्नुहोस्:

**Bash:**
```bash
cd ..  # मूल निर्देशिका बाट
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # मूल निर्देशिका बाट
.\start-all.ps1
```

वा केवल यो मोड्युल सुरु गर्नुहोस्:

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

दुवै स्क्रिप्टहरूले मूल `.env` फाइलबाट वातावरण चरहरू स्वचालित रूपमा लोड गर्छन् र यदि JAR हरू अवस्थित छैनन् भने निर्माण गर्नेछन्।

> **टिप्पणी:** यदि तपाईंले सुरु गर्नु अघि सबै मोड्युलहरू म्यानुअल रूपमा निर्माण गर्न रुचाउनुहुन्छ भने:
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

तपाईंको ब्राउजरमा http://localhost:8083 खोल्नुहोस्।

**रोकिनका लागि:**

**Bash:**
```bash
./stop.sh  # यो मोड्युल मात्र
# या
cd .. && ./stop-all.sh  # सबै मोड्युलहरू
```

**PowerShell:**
```powershell
.\stop.ps1  # यो मात्र मोड्युल
# वा
cd ..; .\stop-all.ps1  # सबै मोड्युलहरू
```

## अनुप्रयोगका स्क्रीनशटहरू

<img src="../../../translated_images/ne/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*मुख्य ड्यासबोर्ड जसले सबै ८ वटा prompt engineering ढाँचाहरू उनीहरूको विशेषताहरू र प्रयोगका केसहरू सहित देखाउँछ*

## ढाँचाहरू अन्वेषण गर्नुहोस्

वेब इन्टरफेसले तपाईंलाई फरक-फरक प्रॉम्प्टिङ रणनीतिहरू प्रयोग गर्न दिन्छ। हरेक ढाँचा फरक समस्या समाधान गर्छ - तिनीहरू प्रयास गर्नुहोस् र कुन बेला कुन तरिका राम्रो छ हेर्नुहोस्।

### कम बनाम बढी उत्सुकता

"२०० को १५% के हो?" जस्तो सरल प्रश्न कम उत्सुकतासहित सोध्नुहोस्। तपाईंलाई तत्काल, सिधा उत्तर प्राप्त हुनेछ। अब "उच्च-ट्राफिक API का लागि क्यासिङ रणनीति डिजाइन गर्नुहोस्" जस्तो जटिल कुरा उच्च उत्सुकतासहित सोध्नुहोस्। मोडलले कसरी सुस्त हुँदै विस्तारै विश्लेषणात्मक जवाफ दिन्छ हेर्नुहोस्। एउटै मोडल, एउटै प्रश्न संरचना - तर प्रॉम्प्टले यो भन्छ कति धेरै सोच्ने हो।

<img src="../../../translated_images/ne/low-eagerness-demo.898894591fb23aa0.webp" alt="Low Eagerness Demo" width="800"/>

*न्यूनतम तर्कसहित छिटो गणना*

<img src="../../../translated_images/ne/high-eagerness-demo.4ac93e7786c5a376.webp" alt="High Eagerness Demo" width="800"/>

*सम्पूर्ण क्यासिङ रणनीति (२.८MB)*

### कार्य निष्पादन (टूल प्रीएम्बलहरू)

बहु-चरणीय कार्यहरू अग्रिम योजना र प्रगति वर्णनबाट लाभान्वित हुन्छन्। मोडलले के गर्नेछ भनेर बाहिर ल्याउँछ, प्रत्येक चरण वर्णन गर्छ, र परिणामहरू संक्षेप गर्छ।

<img src="../../../translated_images/ne/tool-preambles-demo.3ca4881e417f2e28.webp" alt="Task Execution Demo" width="800"/>

*चरण-द्वारा-चरण वर्णन सहित REST एन्डपोइन्ट सिर्जना गर्दै (३.९MB)*

### आत्म-प्रतिबिम्बित कोड

"इमेल पुष्टि सेवा सिर्जना गर्नुहोस्" प्रयास गर्नुहोस्। मात्र कोड सिर्जना गरेर बन्द नगरी मोडलले उत्पादन गर्छ, गुणस्तर मापन गर्छ, कमजोरीहरू पत्ता लगाउँछ, र सुधार गर्छ। तपाईंले देख्नुहुनेछ यो कितिकति दोहोर्याउँछ जबसम्म कोड उत्पादन स्तरमा पुग्दैन।

<img src="../../../translated_images/ne/self-reflecting-code-demo.851ee05c988e743f.webp" alt="Self-Reflecting Code Demo" width="800"/>

*पूर्ण इमेल पुष्टि सेवा (५.२MB)*

### संरचित विश्लेषण

कोड समीक्षा गर्न समान मूल्यांकन फ्रेमवर्क आवश्यक हुन्छ। मोडलले स्थिर वर्गीकरण (ठीकठाक, अभ्यासहरू, प्रदर्शन, सुरक्षा) मा आधारमा विश्लेषण गर्छ severity स्तरहरू सहित।

<img src="../../../translated_images/ne/structured-analysis-demo.9ef892194cd23bc8.webp" alt="Structured Analysis Demo" width="800"/>

*फ्रेमवर्क-आधारित कोड समीक्षा*

### बहु-चरण संवाद

"Spring Boot के हो?" सोध्नुहोस् र तुरुन्त पछि "एक उदाहरण देखाउनुहोस्" भनेर सोध्नुहोस्। मोडलले तपाईंको पहिलो प्रश्न सम्झन्छ र विशेष गरी Spring Boot को उदाहरण दिन्छ। स्मृति नहुँदा अर्को प्रश्न त्यति स्पष्ट हुन्न।

<img src="../../../translated_images/ne/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="Multi-Turn Chat Demo" width="800"/>

*प्रश्नहरू बीच सन्दर्भ संरक्षण*

### चरण-द्वारा-चरण तर्क

एउटा गणित समस्या रोज्नुहोस् र दुवै चरण-द्वारा-चरण तर्क र कम उत्सुकतासहित प्रयास गर्नुहोस्। कम उत्सुकता तपाईंलाई छिटो तर अस्पष्ट उत्तर दिन्छ। चरण-द्वारा-चरणले प्रत्येक गणना र निर्णय देखाउँछ।

<img src="../../../translated_images/ne/step-by-step-reasoning-demo.12139513356faecd.webp" alt="Step-by-Step Reasoning Demo" width="800"/>

*स्पष्ट चरणहरू सहित गणित समस्या*

### प्रतिबन्धित आउटपुट

जब तपाईंलाई निश्चित ढाँचा वा शब्द संख्या चाहिन्छ, यो ढाँचाले कडाईका साथ पालना गर्छ। १०० शब्दको बलट पोइन्ट फर्म्याटमा सारांश उत्पन्न गर्न प्रयास गर्नुहोस्।

<img src="../../../translated_images/ne/constrained-output-demo.567cc45b75da1633.webp" alt="Constrained Output Demo" width="800"/>

*ढाँचामा नियन्त्रणसहित मेशिन लर्निङ सारांश*

## तपाईं साँच्चै के सिक्दै हुनुहुन्छ

**तर्क गर्ने प्रयासले सबै कुरा परिवर्तन गर्छ**

GPT-5.2 ले तपाईंलाई प्रॉम्प्टमार्फत कम्प्युटेशनल प्रयास नियन्त्रित गर्न दिन्छ। कम प्रयास भनेको छिटो जवाफ र न्यूनतम अन्वेषण हो। उच्च प्रयास भनेको मोडलले गहिरो सोच्न समय लिन्छ। तपाईंले जटिलताअनुसार प्रयास मिलाउन सिक्दै हुनुहुन्छ - सामान्य प्रश्नहरूमा समय नव्यर्थ गर्नुहोस्, तर जटिल निर्णयहरूमा छिटो नगर्नुहोस्।

**संरचनाले व्यवहार निर्धारण गर्छ**

प्रॉम्प्टमा XML ट्यागहरू देख्नुभयो? तिनीहरू सजावट होइनन्। मोडलहरूले संरचित निर्देशनहरू स्वतन्त्र पाठ भन्दा बढी भरपर्दो रूपमा पछ्याउँछन्। जब तपाईंलाई बहु-चरण प्रक्रिया वा जटिल तर्क चाहिन्छ, संरचनाले मोडललाई स्थितिमा राख्न मद्दत गर्छ।

<img src="../../../translated_images/ne/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*स्पष्ट खण्डहरू र XML शैली संगठनसहित राम्रो संरचित प्रॉम्प्टको अवयव*

**गुणस्तर आत्म-मूल्यांकनबाट**

आत्म-प्रतिबिम्बित ढाँचाहरूले गुणस्तर मापदण्डहरू स्पष्ट रूपमा प्रस्तुत गर्छन्। मोडलले "सही" गरे भन्ने आशामा नरहेको, तपाईंले त्यसको अर्थ के हो भनेर बताउनुहुन्छ: सही तर्क, त्रुटि ह्यान्डलिंग, प्रदर्शन, सुरक्षा। मोडलले त्यसपछि आफ्नो आऊटपुट मूल्यांकन गर्छ र सुधार गर्छ। यसले कोड उत्पादनलाई लटरीबाट प्रक्रिया बनाउँछ।

**सन्दर्भ सीमित छ**

बहु-चरण संवादले हरेक अनुरोधसँग सन्देश इतिहास समावेश गरेर काम गर्छ। तर सीमा छ - हरेक मोडलसँग अधिकतम टोकन संख्या हुन्छ। जति संवाद बढ्छ, तपाईंलाई सान्दर्भिक सन्दर्भ राख्न रणनीतिहरू चाहिन्छ बिना सीमा पार नगरी। यो मोड्युलले तपाईंलाई स्मृतिले कसरी काम गर्छ देखाउँछ; पछि तपाईंले सञ्क्षेप कहिले गर्ने, भुल्ने र पुनःप्राप्त गर्ने सिक्नुहुनेछ।

## अर्को चरणहरू

**अर्को मोड्युल:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**नेभिगेसन:** [← अघिल्लो: मोड्युल ०१ - परिचय](../01-introduction/README.md) | [मुख्यमा फर्कनुहोस्](../README.md) | [अर्को: मोड्युल ०३ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
यस दस्तावेजलाई AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) को प्रयोग गरी अनुवाद गरिएको हो। हामी शुद्धताको लागि प्रयास गरिरहेका छौं, तर कृपया ध्यान दिनुहोस् कि स्वत: अनुवादमा त्रुटि वा अशुद्धता हुन सक्छ।मूल दस्तावेज आफ्नो स्थानीय भाषामा आधिकारिक स्रोतको रूपमा मानिनुपर्छ। महत्वपूर्ण जानकारीको लागि व्यावसायिक मानव अनुवाद सल्लाह दिइन्छ। यस अनुवादको प्रयोगबाट उत्पन्न हुने कुनै पनि गलतफहमी वा व्याख्यामा हामी जिम्मेवार हुने छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->