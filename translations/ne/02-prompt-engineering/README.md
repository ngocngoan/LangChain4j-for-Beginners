# Module 02: GPT-5.2 सँग प्रम्प्ट इञ्जिनियरिङ

## Table of Contents

- [भिडियो हिँडाइ](../../../02-prompt-engineering)
- [तपाईंले के सिक्नुहुनेछ](../../../02-prompt-engineering)
- [आवश्यकताहरू](../../../02-prompt-engineering)
- [प्रम्प्ट इञ्जिनियरिङ बुझ्न](../../../02-prompt-engineering)
- [प्रम्प्ट इञ्जिनियरिङ आधारहरू](../../../02-prompt-engineering)
  - [शून्य-शट प्रम्प्टिङ](../../../02-prompt-engineering)
  - [थोरै-शट प्रम्प्टिङ](../../../02-prompt-engineering)
  - [सोचको शृंखला](../../../02-prompt-engineering)
  - [भूमिका-आधारित प्रम्प्टिङ](../../../02-prompt-engineering)
  - [प्रम्प्ट टेम्प्लेटहरू](../../../02-prompt-engineering)
- [शाखागत ढाँचाहरू](../../../02-prompt-engineering)
- [एप्लिकेशन चलाउनुहोस्](../../../02-prompt-engineering)
- [एप्लिकेशन स्क्रिनसटहरू](../../../02-prompt-engineering)
- [ढाँचाहरू अन्वेषण गर्दै](../../../02-prompt-engineering)
  - [कम बनाम उच्च उत्साह](../../../02-prompt-engineering)
  - [कार्य सञ्चालन (टुल प्राम्बल्स)](../../../02-prompt-engineering)
  - [आत्म-प्रतिबिम्बित कोड](../../../02-prompt-engineering)
  - [संरचित विश्लेषण](../../../02-prompt-engineering)
  - [धेरै पटकको कुरा](../../../02-prompt-engineering)
  - [क्रमिक विचार प्रक्रिया](../../../02-prompt-engineering)
  - [सीमित नतिजा](../../../02-prompt-engineering)
- [तपाईं साँच्चिकै के सिक्दै हुनुहुन्छ](../../../02-prompt-engineering)
- [अगाडि के गर्ने](../../../02-prompt-engineering)

## Video Walkthrough

यो लाइभ सेसन हेर्नुहोस् जसले यो मोड्युल कसरी सुरु गर्ने भनेर व्याख्या गर्छ:

<a href="https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke"><img src="https://img.youtube.com/vi/PJ6aBaE6bog/maxresdefault.jpg" alt="Prompt Engineering with LangChain4j - Live Session" width="800"/></a>

## What You'll Learn

तलको चित्रले यस मोड्युलमा तपाईंले विकास गर्ने मुख्य विषयहरू र सीपहरूको अवलोकन प्रदान गर्दछ — प्रम्प्ट सुधार प्रविधिहरूबाट लिएर तपाईंले पछ्याउने चरण-द्वारा-चरण कार्यप्रवाहसम्म।

<img src="../../../translated_images/ne/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

अघिल्लो मोड्युलहरूमा, तपाईंले GitHub मोडेलहरूसँग आधारभूत LangChain4j अन्तरक्रियाहरू अन्वेषण गर्नुभयो र मेमोरीले कसरी Azure OpenAI सँग संवादात्मक AI सक्षम गर्छ हेर्नुभयो। अब हामी कसरी प्रश्न सोध्ने — प्रम्प्टहरू आफैं — Azure OpenAI को GPT-5.2 प्रयोग गरी केन्द्रित हुने छौं। तपाईंको प्रम्प्टहरूको संरचना तपाईंले पाउने प्रतिक्रिया को गुणस्तरमा ठूलो प्रभाव पार्छ। हामी मूल प्रम्प्टिङ प्रविधिहरूको समीक्षा गर्दा सुरु गर्छौं, त्यसपछि आठ वटा उन्नत ढाँचाहरूमा जान्छौं जसले GPT-5.2 को क्षमताहरू पूर्ण रूपमा उपयोग गर्छन्।

हामी GPT-5.2 प्रयोग गर्नेछौं किनकि यसले तर्क नियन्त्रण परिचय गराउँछ - तपाईं मोडेललाई कति सोच्नुपर्ने हो भन्ने भन्न सक्नुहुन्छ। यसले विभिन्न प्रम्प्टिङ रणनीतिहरू अझ स्पष्ट बनाउँछ र तपाईंलाई कुन दृष्टिकोण कहिले प्रयोग गर्ने बुझ्न मद्दत गर्छ। हामीले Azure को GPT-5.2 का लागि कम दर सीमा पनि GitHub मोडेलहरूको तुलनामा लाभ उठाउनेछौं।

## Prerequisites

- Module 01 पुरा गरिएको (Azure OpenAI स्रोतहरू परिनियोजित)
- मूल फोल्डरमा `.env` फाइल Azure प्रमाणपत्रहरू सहित (Module 01 मा `azd up` द्वारा सिर्जना गरिएको)

> **Note:** यदि तपाईंले Module 01 पुरा गर्नुभएको छैन भने, त्यहाँ पहिले परिनियोजन निर्देशनहरू पालना गर्नुहोस्।

## Understanding Prompt Engineering

आधारमा, प्रम्प्ट इञ्जिनियरिङ अस्पष्ट निर्देशन र सटीक निर्देशन बीचको फरक हो, तलको तुलना देखाउँछ।

<img src="../../../translated_images/ne/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

प्रम्प्ट इञ्जिनियरिङ यस्तो इनपुट पाठ डिजाइन गर्न बारेमा हो जसले निरन्तर तपाईंलाई आवश्यक परिणाम दिन्छ। यो केवल प्रश्न सोध्ने विषय होइन - यो अनुरोधहरू यस्तो संरचना गर्ने विषय हो ताकि मोडेल ठीकै बुझोस् कि तपाईं के चाहनुहुन्छ र यसलाई कसरी उपलब्ध गराउनु पर्छ।

यसलाई एउटा सहकर्मीलाई निर्देशन दिने रूपमा सोच्नुहोस्। "बग ठिक पार" अस्पष्ट छ। "UserService.java को लाइन 45 मा नल प्वाइन्टर एक्सेप्सन ठिक पार्न नल जाँच थप" विशिष्ट छ। भाषा मोडेलहरू पनि यस्तै हुन्छन् - विशिष्टता र संरचनाले महत्त्व राख्छ।

तलको चित्रले देखाउँछ कि LangChain4j यस चित्रमा कसरी फिट हुन्छ — SystemMessage र UserMessage बिल्डिङ ब्लकहरू मार्फत तपाईंको प्रम्प्ट ढाँचाहरूलाई मोडेलसँग जोड्दै।

<img src="../../../translated_images/ne/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4jले पूर्वाधार प्रदान गर्छ — मोडेल कनेक्शनहरू, मेमोरी, र सन्देश प्रकारहरू — जबकि प्रम्प्ट ढाँचाहरू केवल सावधानीपूर्वक संरचित पाठहरू हुन् जुन तपाईं त्यस पूर्वाधार मार्फत पठाउनुहुन्छ। मुख्य बिल्डिङ ब्लकहरू `SystemMessage` (जो AI को व्यवहार र भूमिका सेट गर्छ) र `UserMessage` (जो तपाईंको वास्तविक अनुरोध बोक्छ) हुन्।

## Prompt Engineering Fundamentals

तल देखाइएका पाँच प्रमुख प्रविधिहरू प्रभावकारी प्रम्प्ट इञ्जिनियरिङको आधार हुन्। प्रत्येकले भाषा मोडेलहरूसँग तपाईं कसरी सञ्चार गर्नुहुन्छ भन्ने फरक पक्षसँग सम्बोधन गर्छ।

<img src="../../../translated_images/ne/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

यस मोड्युलका उन्नत ढाँचाहरूमा जानुअघि, पाँच आधारभूत प्रम्प्टिङ प्रविधिहरूको समीक्षा गरौं। यी ती बिल्डिङ ब्लकहरू हुन् जुन प्रत्येक प्रम्प्ट इञ्जिनियरले जान्नुपर्छ। यदि तपाईंले पहिले नै [चाँडो सुरु मोड्युल](../00-quick-start/README.md#2-prompt-patterns) प्रयोग गर्नुभएको छ भने, तपाईंले तिनीहरूलाई प्रयोगमा देख्नु भएको छ — यहाँ तिनीहरूको अवधारणात्मक आधार छ।

### Zero-Shot Prompting

सबसम्म सरल दृष्टिकोण: मोडेललाई कुनै उदाहरण बिना सिधा निर्देशन दिनु। मोडेलले प्रशिक्षणमा मात्र भर पर्छ कार्य बुझ्न र सम्पादन गर्न। यो सोझो अनुरोधहरूका लागि राम्रो काम गर्छ जहाँ अपेक्षित व्यवहार स्पष्ट छ।

<img src="../../../translated_images/ne/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*उदाहरण बिना सिधा निर्देशन — मोडेलले निर्देशनबाट मात्र कार्य अनुमान लगाउँछ*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// प्रतिक्रिया: "सकारात्मक"
```

**कहिले प्रयोग गर्ने:** सरल वर्गीकरणहरू, सिधा प्रश्नहरू, अनुवादहरू, वा कुनै पनि कार्य जसलाई मोडेलले अतिरिक्त मार्गदर्शन बिना पनि सम्हाल्न सक्छ।

### Few-Shot Prompting

तपाईंले चाहेको ढाँचाको नमूना उदाहरणहरू दिनुहोस्। मोडेलले तपाईंका उदाहरणहरूबाट अपेक्षित इनपुट-आउटपुट स्वरूप सिक्छ र नयाँ इनपुटहरूमा लागू गर्छ। यो अझ सुसंगतता ल्याउँछ जहाँ चाहिएको ढाँचा वा व्यवहार स्पष्ट छैन।

<img src="../../../translated_images/ne/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*उदाहरणबाट सिक्ने — मोडेलले ढाँचालाई चिन्न र नयाँ इनपुटमा लागू गर्न सक्षम हुन्छ*

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

**कहिले प्रयोग गर्ने:** अनुकूल वर्गीकरणहरू, सुसंगत स्वरूप, डोमेन-विशेष कार्यहरू, वा जब शून्य-शट नतिजाहरू असंगत हुन्छन्।

### Chain of Thought

मोडेललाई त्यसको तर्कलाई चरण-द्वारा-चरण देखाउन भन्नुहोस्। सिधै जवाफमा नलफ्टेर, मोडेल समस्यालाई टुक्राहरूमा विभाजन गरेर स्पष्ट रूपमा समाधान प्रक्रिया देखाउँछ। यसले गणित, तर्कशक्ति, र बहु-चरण तर्कहरुको सटीकता सुधार्छ।

<img src="../../../translated_images/ne/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*चरण-द्वारा-चरण तर्क — जटिल समस्याहरूलाई स्पष्ट तर्कका चरणहरूमा विभाजित गर्दै*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// मोडेलले देखाउँछ: १५ - ८ = ७, त्यसपछि ७ + १२ = १९ स्याउहरू
```

**कहिले प्रयोग गर्ने:** गणित समस्या, तर्क पहेलीहरू, डिबगिङ, वा कुनै पनि कार्य जहाँ तर्क प्रक्रिया देखाउँदा सटीकता र विश्वास बढ्छ।

### Role-Based Prompting

तपाईंको प्रश्न सोध्नुअघि AI को लागि एउटा पात्र वा भूमिका सेट गर्नुहोस्। यसले सन्दर्भ प्रदान गर्छ जसले जवाफको शैली, गहिराई, र फोकस निर्धारण गर्छ। "सफ्टवेयर वास्तुकार" ले "जुनियर विकासकर्ता" वा "सुरक्षा अडिटर" भन्दा फरक सल्लाह दिन्छ।

<img src="../../../translated_images/ne/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*सन्दर्भ र पात्र निर्धारण — एउटै प्रश्नलाई दिइने जवाफ भूमिका अनुसार फरक पर्छ*

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

**कहिले प्रयोग गर्ने:** कोड समीक्षा, ट्युटरिङ, डोमेन-विशेष विश्लेषण, वा जब तपाईंलाई निश्चित विशेषज्ञताको स्तर वा दृष्टिकोण अनुरूप जवाफ चाहिन्छ।

### Prompt Templates

पुन: प्रयोगयोग्य प्रम्प्टहरू variable placeholder हरू सहित सिर्जना गर्नुहोस्। प्रत्येक पटक नयाँ प्रम्प्ट लेख्नुभन्दा, एक पटक टेम्प्लेट परिभाषित गरेर फरक मानहरू भर्नुहोस्। LangChain4j को `PromptTemplate` क्लासले `{{variable}}` सिन्ट्याक्सलाई सहज बनाउँछ।

<img src="../../../translated_images/ne/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*पुन: प्रयोगयोग्य प्रम्प्टहरू variable placeholder सहित — एउटा टेम्प्लेट, धेरै प्रयोग*

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

**कहिले प्रयोग गर्ने:** फरक इनपुटहरूसँग पुनरावृत्त प्रश्नहरू, ब्याच प्रशोधन, पुन: प्रयोग योग्य AI कार्यप्रवाह निर्माण, वा कुनै पनि अवस्थामा जहाँ प्रम्प्ट संरचना उस्तै हुन्छ तर डाटा फरक हुन्छ।

---

यी पाँच आधारभूतहरूले तपाईंलाई धेरै प्रम्प्टिङ कार्यहरूको लागि बलियो उपकरण सेट दिन्छ। यस मोड्युलको बाँकी भागले तीमा **आठ उन्नत ढाँचाहरू** थप्छ जुन GPT-5.2 को तर्क नियन्त्रण, आत्म-मूल्याङ्कन, र संरचित नतिजा क्षमताहरूलाई उपयोग गर्छ।

## Advanced Patterns

आधारहरू कभर भएपछि, अब ती आठ उन्नत ढाँचाहरूमा जानुहोस् जुन यस मोड्युललाई विशेष बनाउँछ। सबै समस्याहरूलाई एउटै दृष्टिकोण चाहिँदैन। केही प्रश्नहरू छिटो जवाफ चाहिन्छ, काहीं गहिरो सोच आवश्यक छ। केहीले तर्क देखाउनुपर्छ, केहीले मात्र नतिजा चाहिन्छ। तलका प्रत्येक ढाँचा फरक परिदृश्यका लागि अनुकूलित छन् — र GPT-5.2 को तर्क नियन्त्रणले यी फरकहरू अझ स्पष्ट बनाउँछ।

<img src="../../../translated_images/ne/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*आठ प्रम्प्ट इञ्जिनियरिङ ढाँचाहरू र तिनीहरूका प्रयोग केसहरूको अवलोकन*

GPT-5.2 ले यी ढाँचाहरूमा अर्को आयाम थप्छ: *तर्क नियन्त्रण*। तलको स्लाइडरले देखाउँछ कि तपाईं कसरी मोडेलको सोचाई प्रयास समायोजन गर्न सक्नुहुन्छ — छिटो, सिधा जवाफबाट गहिरो, विस्तृत विश्लेषणसम्म।

<img src="../../../translated_images/ne/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 को तर्क नियन्त्रणले तपाईंलाई मोडेलले कति सोच्नु पर्छ — छिटो सिधा जवाफदेखि गहिरो अन्वेषणसम्म निर्धारण गर्न दिन्छ*

**कम उत्साह (छिटो र केन्द्रीत)** - सरल प्रश्नहरूसँग छिटो, सिधा जवाफ चाहिँदा। मोडेलले न्यूनतम तर्क गर्छ - अधिकतम २ चरण। गणना, खोज, वा सोझो प्रश्नहरूको लागि प्रयोग गर्नुहोस्।

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
> - "कम उत्साह र उच्च उत्साह प्रम्प्टिङ ढाँचाहरूबीच के फरक छ?"
> - "प्रम्प्टहरूमा XML ट्यागहरूले AI को प्रतिक्रिया कसरी संरचना गर्न मद्दत गर्छ?"
> - "कहिले मैले आत्म-प्रतिबिम्ब ढाँचाहरू र कहिले सिधा निर्देशन प्रयोग गर्ने?"

**उच्च उत्साह (गहिरो र विस्तृत)** - जटिल समस्याहरू जहाँ पूर्ण विश्लेषण चाहिन्छ। मोडेलले विस्तारमा अन्वेषण गर्छ र विस्तृत तर्क देखाउँछ। प्रणाली डिजाइन, वास्तुकला निर्णय, वा जटिल अनुसन्धानको लागि प्रयोग गर्नुहोस्।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**कार्य सञ्चालन (चरण-द्वारा-चरण प्रगति)** - बहु-चरण कार्यप्रवाहहरूको लागि। मोडेलले प्रारम्भमा योजना दिन्छ, प्रत्येक चरणमा काम गर्दा वर्णन गर्छ, र अन्तमा सारांश दिन्छ। माइग्रेशन, कार्यान्वयन, वा कुनै पनि बहु-चरण प्रक्रियाको लागि प्रयोग गर्नुहोस्।

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

Chain-of-Thought प्रम्प्टिङले मोडेललाई स्पष्ट रूपमा तर्क प्रक्रिया देखाउन भन्छ, जटिल कार्यहरूको सटीकता सुधार्न। चरण-द्वारा-चरण विभाजनले मानिस र AI दुवैलाई तर्क बुझ्न मद्दत गर्छ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat सँग प्रयास गर्नुहोस्:** यो ढाँचाबारे सोध्नुहोस्:
> - "म लामो समयमा चल्ने अपरेसनका लागि कार्य सञ्चालन ढाँचा कसरी अनुकूल गर्छु?"
> - "उत्पादन अनुप्रयोगहरूमा टुल प्राम्बलहरू संरचना गर्ने उत्तम अभ्यास के हुन्?"
> - "म UI मा माध्यमिक प्रगति अपडेटहरू कसरी कैद र प्रदर्शन गर्न सक्छु?"

तलको चित्रले यो योजना → कार्यान्वयन → सारांश कार्यप्रवाह देखाउँछ।

<img src="../../../translated_images/ne/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*बहु-चरण कार्यहरूको लागि योजना → कार्यान्वयन → सारांश कार्यप्रवाह*

**आत्म-प्रतिबिम्बित कोड** - उत्पादन-स्तरको कोड सिर्जना गर्न। मोडेलले उत्पादन मापदण्ड अनुरूप त्रुटि ह्यान्डलिङ सहित कोड उत्पादन गर्छ। नयाँ सुविधाहरू वा सेवाहरू बनाउने बेला प्रयोग गर्नुहोस्।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

तलको चित्रले यस पुनरावृत्ति सुधार लूप देखाउँछ — कोड उत्पन्न गर्नुहोस्, मूल्यांकन गर्नुहोस्, कमजोरीहरू पहिचान गर्नुहोस्, र सुधार गर्दै उत्पादन स्तरसम्म पुग्नुहोस्।

<img src="../../../translated_images/ne/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*पुनरावृत्ति सुधार लूप - उत्पन्न, मूल्याङ्कन, समस्याहरू चिन्हित, सुधार, पुनरावृत्ति*

**संरचित विश्लेषण** - निरन्तर मूल्याङ्कनका लागि। मोडेलले कोडलाई निश्चित ढाँचा (सहीपन, अभ्यासहरू, प्रदर्शन, सुरक्षा, मर्मतयोग्यता) अनुसार समीक्षा गर्छ। कोड समीक्षा वा गुणस्तर मूल्याङ्कनमा प्रयोग गर्न।

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
> - "म फरक प्रकारका कोड समीक्षा लागि विश्लेषण ढाँचा कसरी अनुकूल गर्न सक्छु?"
> - "संरचित नतिजालाई प्रोग्रामिङ रूपमा पार्स र प्रयोग गर्ने उत्तम तरिका के हो?"
> - "म विभिन्न समीक्षा सत्रहरूमा निरन्तर गम्भीरता स्तर कसरी सुनिश्चित गर्छु?"

तलको चित्रले यो संरचित फ्रेमवर्क कसरी कोड समीक्षा निरन्तर वर्गीकरण र गम्भीरता स्तरहरूसँग व्यवस्थित गर्छ देखाउँछ।

<img src="../../../translated_images/ne/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*गम्भीरता स्तरहरूसहित निरन्तर कोड समीक्षा फ्रेमवर्क*

**धेरै पटकको कुरा** - सन्दर्भ चाहिने संवादहरूको लागि। मोडेलले अघिल्लो सन्देशहरू सम्झन्छ र त्यसमा आधारित प्रतिक्रिया निर्माण गर्छ। अन्तर्क्रियात्मक सहायता सत्र वा जटिल Q&A का लागि प्रयोग गर्नुहोस्।

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("What is Spring Boot?"));
AiMessage aiMessage1 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage1);

memory.add(UserMessage.from("Show me an example"));
AiMessage aiMessage2 = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage2);
```

तलको चित्रले देखाउँछ कि कुरा सन्दर्भ कसरी प्रत्येक चरणसँग सङ्ग्रह हुन्छ र यो कत्तिको मोडेलको टोकन सीमासँग सम्बन्धित छ।

<img src="../../../translated_images/ne/context-memory.dff30ad9fa78832a.webp" alt="Context Memory" width="800"/>

*कसरी कुरा सन्दर्भ धेरै पटकहरूमा सङ्ग्रह भएर टोकन सीमा पुर्‍याउँछ*
**क्रमबद्ध तर्क** - दृश्य तर्क आवश्यक पर्ने समस्याहरूका लागि। मोडेल प्रत्येक चरणको स्पष्ट तर्क प्रदर्शन गर्छ। गणित समस्याहरू, तर्क पहेलीहरू, वा सोच्ने प्रक्रिया बुझ्न आवश्यक पर्दा यसलाई प्रयोग गर्नुहोस्।

```java
String prompt = """
    <instruction>Show your reasoning step-by-step</instruction>
    
    If a train travels 120 km in 2 hours, then stops for 30 minutes,
    then travels another 90 km in 1.5 hours, what is the average speed
    for the entire journey including the stop?
    """;

String response = chatModel.chat(prompt);
```

तलको चित्रले मोडेलले समस्याहरूलाई स्पष्ट, क्रमांकित तार्किक चरणहरूमा कसरी विभाजन गर्छ भनी देखाउँछ।

<img src="../../../translated_images/ne/step-by-step-pattern.a99ea4ca1c48578c.webp" alt="Step-by-Step Pattern" width="800"/>

*समस्याहरूलाई स्पष्ट तार्किक चरणहरूमा विभाजन गर्ने प्रक्रिया*

**सीमित आउटपुट** - विशिष्ट ढाँचा आवश्यकताहरूका लागि जवाफहरू। मोडेलले ढाँचा र लम्बाइ नियमहरू कडाइका साथ पालन गर्छ। सारांशहरू वा सावधानीपूर्वक संरचना आवश्यक पर्दा यसको प्रयोग गर्नुहोस्।

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

तलको चित्रले कसरि सीमाहरू मोडेललाई तपाईंको ढाँचा र लम्बाइ आवश्यकताहरूको कडाईसँग पालन गर्न निर्देश गर्छ देखाउँछ।

<img src="../../../translated_images/ne/constrained-output-pattern.0ce39a682a6795c2.webp" alt="Constrained Output Pattern" width="800"/>

*विशिष्ट ढाँचा, लम्बाइ, र संरचना आवश्यकताहरूको पालन गर्ने*

## एप्लिकेशन चलाउनुहोस्

**स्थापना पुष्टि गर्नुहोस्:**

रुट डाइरेक्टरीमा `.env` फाइल अवश्य छ भन्ने सुनिश्चित गर्नुहोस् जसमा Azure क्रेडेन्सियलहरू छन् (Module 01 मा सिर्जना गरिएको)। यो मोड्युल डाइरेक्टरी (`02-prompt-engineering/`) बाट चलाउनुहोस्:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT देखाउनु पर्छ
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT देखाउनु पर्छ
```

**एप्लिकेशन सुरु गर्नुहोस्:**

> **सूचना:** यदि तपाईंले पहिले नै रुट डाइरेक्टरीबाट `./start-all.sh` प्रयोग गरी सबै एप्लिकेशनहरू सुरु गर्नुभएको छ (Module 01 मा वर्णन गरिएको), यो मोड्युल पोर्ट 8083 मा पहिले नै चलिरहेको छ। तपाईंले तलका सुरु गर्ने कमाण्डहरू छोडेर सिधै http://localhost:8083 मा जान सक्नुहुन्छ।

**विकल्प 1: Spring Boot Dashboard प्रयोग गरेर (VS Code प्रयोगकर्ताहरूका लागि सिफारिस गरिएको)**

Dev container मा Spring Boot Dashboard एक्सटेन्शन समावेश गरिएको छ, जसले सबै Spring Boot एप्लिकेशनहरू व्यवस्थापन गर्न भिजुअल इन्टरफेस प्रदान गर्छ। VS Code को बाँया पट्टि Activity Bar मा यो फेला पार्न सकिन्छ (Spring Boot आइकन खोज्नुहोस्)।

Spring Boot Dashboard बाट तपाईंसँग के गर्न सकिन्छ:
- कार्यक्षेत्रमा सबै उपलब्ध Spring Boot एप्लिकेशनहरू हेर्नुहोस्
- एप्लिकेशनहरू एक क्लिकले सुरु/रोक्नुहोस्
- एप्लिकेशन लगहरू रियल-टाइममा हेर्नुहोस्
- एप्लिकेशन स्थिति अनुगमन गर्नुहोस्

"prompt-engineering" को छेउमा प्ले बटनमा क्लिक गरी यो मोड्युल सुरु गर्नुहोस्, वा सबै मोड्युलहरू एकैपटक सुरु गर्नुहोस्।

<img src="../../../translated_images/ne/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code मा Spring Boot Dashboard — सबै मोड्युलहरू एकै ठाउँबाट सुरु, रोक, र अनुगमन गर्नुहोस्*

**विकल्प २: shell scripts प्रयोग गरेर**

सबै वेब एप्लिकेशनहरू (मोड्युल ०१-०४) सुरु गर्नुहोस्:

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

दुवै स्क्रिप्टहरूले रुट `.env` फाइलबाट वातावरण सञ्झ्याल स्वतः लोड गर्छन् र जाभा JAR फाइलहरू नहुँदा बनाउँछन्।

> **सूचना:** यदि तपाईं सुरु गर्नु अघि सबै मोड्युलहरू म्यानुअली निर्माण गर्न चाहनुहुन्छ भने:
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

**रोक्नका लागि:**

**Bash:**
```bash
./stop.sh  # यो मोड्युल मात्र
# वा
cd .. && ./stop-all.sh  # सबै मोड्युलहरू
```

**PowerShell:**
```powershell
.\stop.ps1  # यो मोड्युल मात्र
# वा
cd ..; .\stop-all.ps1  # सबै मोड्युलहरू
```

## एप्लिकेशन स्क्रीनसटहरू

यो "prompt engineering" मोड्युलको मुख्य ईन्टरफेस हो जहाँ तपाईं आठवटा पैटर्नहरू एकै साथ प्रयोग गर्न सक्नुहुन्छ।

<img src="../../../translated_images/ne/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*मुख्य ड्यासबोर्ड जसले सबै ८ prompt engineering पैटर्नहरू उनीहरूको विशेषताहरू र प्रयोग केसहरूसहित देखाउँछ*

## पैटर्नहरू अन्वेषण गर्दै

वेब इन्टरफेसले तपाईंलाई विभिन्न prompting रणनीतिहरू प्रयोग गर्न दिन्छ। प्रत्येक पैटर्नले फरक-फरक समस्याहरू समाधान गर्छ - प्रयोग गरेर हेर्नुहोस् कुन तरिका कहिले राम्रो हुन्छ।

> **सूचना: Streaming vs Non-Streaming** — सबै पैटर्न पृष्ठमा दुईवटा बटनहरू उपलब्ध छन्: **🔴 Stream Response (Live)** र **Non-streaming** विकल्प। Streaming ले Server-Sent Events (SSE) प्रयोग गरी मोडेलले टोकनहरू उत्पादन गर्दा तुरुन्त देखाउँछ, यसैले तपाईं प्रगति तुरुन्तै हेर्न सक्नुहुन्छ। Non-streaming विकल्पले पूरा जवाफ आएपछि मात्र देखाउँछ। गहिरो तर्क आवश्यक पर्ने प्रम्प्टहरूमा (जस्तै High Eagerness, Self-Reflecting Code) non-streaming धेरै समय लाग्न सक्छ — कहिलेकाहीं मिनेटौं — र कुनै दृश्य प्रतिक्रिया नआउन सक्छ। **जटिल प्रम्प्टहरूसँग प्रयोग गर्दा streaming प्रयोग गर्नुहोस्** ताकि तपाईं मोडेलको काम देख्न सक्नुहुनेछ र ढुक्क हुनुहुनेछ कि अनुरोध टाइमआउट भएको छैन।
>
> **सूचना: ब्राउजर आवश्यकताहरू** — streaming सुविधाले Fetch Streams API (`response.body.getReader()`) प्रयोग गर्छ जुन पूर्ण ब्राउजर (Chrome, Edge, Firefox, Safari) मा मात्र काम गर्छ। VS Code को इन-बिल्ट Simple Browser मा यो काम गर्दैन किनकि यसको वेभ्भ्युले ReadableStream API समर्थन गर्दैन। यदि तपाईं Simple Browser प्रयोग गर्नुहुन्छ भने, non-streaming बटनहरूले सामान्य रूपमा काम गर्छन् - केवल streaming बटनहरू प्रभावित हुन्छन्। पूर्ण अनुभवका लागि `http://localhost:8083` बाह्य ब्राउजरमा खोल्नुहोस्।

### Low vs High Eagerness

"200 को 15% कति हो?" जस्तो सरल प्रश्न Low Eagerness प्रयोग गरेर सोध्नुहोस्। तपाईंलाई छिटो, सिधा जवाफ प्राप्त हुनेछ। अब "High-traffic API का लागि caching रणनीति डिजाइन गर्नुहोस्" जस्तो जटिल प्रश्न High Eagerness प्रयोग गरेर सोध्नुहोस्। **🔴 Stream Response (Live)** क्लिक गरी मोडेलको विस्तृत तर्क टोकन बट टोकन देख्नुहोस्। एउटै मोडेल, एउटै प्रश्न संरचना - तर प्रॉम्प्टले कति सोच्ने भनी निर्देशन दिन्छ।

### कार्य निष्पादन (Tool Preambles)

धेरै चरण भएका कार्यहरू लागि पूर्व योजना र प्रगति विवरण फाइदाजनक हुन्छ। मोडेलले के गर्नेछ भनेर रूपरेखा बनाउँछ, प्रत्येक चरण वर्णन गर्छ, र परिणाम सारांश गर्छ।

### Self-Reflecting Code

"इमेल मान्यकरण सेवा बनाउनुहोस्" प्रयास गर्नुहोस्। केवल कोड सिर्जना गरेर बन्द नगर्ने, मोडेल गुणस्तर मापदण्डअनुसार मूल्यांकन गर्दै, कमजोरी पहिचान गर्दै, र सुधार गर्दै जान्छ। तपाईंले उत्पादन स्तरसम्म कोड पुनरावृत्ति हुँदै जान्छ देख्नुहुनेछ।

### संरचित विश्लेषण

कोड समीक्षा निरन्तर मूल्यांकन ढाँचा आवश्यक हुन्छ। मोडेलले कोडलाई निश्चित वर्गीकरण (ठीक हुनु, अभ्यासहरू, प्रदर्शन, सुरक्षा) र गम्भीरता स्तरहरूसहित विश्लेषण गर्छ।

### मल्टि-टर्न च्याट

"Spring Boot के हो?" सोध्नुहोस् र तुरुन्त "उदाहरण देखाउ" सोध्नुहोस्। मोडेलले पहिलो प्रश्न सम्झिन्छ र तपाईंलाई Spring Boot को विशेष उदाहरण दिन्छ। सम्झनाहीन अवस्थामा दोस्रो प्रश्न धेरै अस्पष्ट हुन्छ।

### क्रमबद्ध तर्क

गणितको समस्या रोजेर Step-by-Step Reasoning र Low Eagerness दुबै प्रयोग गर्नुहोस्। Low eagerness ले उत्तर मात्र दिन्छ - छिटो तर अस्पष्ट। Step-by-step ले प्रत्येक गणना र निर्णय देखाउँछ।

### सीमित आउटपुट

विशिष्ट ढाँचा वा शब्द गणना आवश्यक पर्दा, यो पैटर्नले कडाइका साथ पालन गराउँछ। १०० शब्दको बुलेट पोइन्ट सारांश बनाउन प्रयास गर्नुहोस्।

## तपाईं के सिक्दै हुनुहुन्छ

**तर्क प्रयासले सबै कुरा परिवर्तन गर्छ**

GPT-5.2 ले तपाईंलाई प्रम्प्ट मार्फत कम्प्युटेशन प्रयास नियन्त्रण गर्न दिन्छ। कम प्रयासले छिटो उत्तर दिन्छ तर न्यूनतम अन्वेषण गर्छ। उच्च प्रयासले मोडेललाई गहिरो सोच्न समय दिन्छ। तपाईं कार्यको जटिलता अनुसार प्रयास मिलाउन सिक्दै हुनुहुन्छ - साधारण प्रश्नमा समय नबर्बाद गर्नुहोस्, तर जटिल निर्णयहरू तुरुन्त नगर्नुहोस्।

**संरचनाले व्यवहार निर्देशित गर्छ**

प्रम्प्टमा XML ट्यागहरूलाई ध्यान दिनुहोस? ती सजावट मात्र होइनन्। मोडेलहरू विनियोजित निर्देशनहरूलाई स्वतः पालन गर्छन्। मल्टि-स्टेप प्रक्रियाहरू वा जटिल तर्क आवश्यक पर्दा, संरचनाले मोडेललाई कहाँ छ र के गर्ने बताउँछ। तलको चित्रले राम्ररी संरचित प्रम्प्टलाई देखाउँछ जसमा `<system>`, `<instructions>`, `<context>`, `<user-input>`, और `<constraints>` जस्ता ट्यागहरूले निर्देशनहरू स्पष्ट खण्डहरूमा व्यवस्थित गर्छन्।

<img src="../../../translated_images/ne/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*स्पष्ट खण्डहरू र XML-शैली संगठनसहितको राम्ररी संरचित प्रम्प्टको बनावट*

**गुणस्तर स्व-मूल्यांकनबाट हुन्छ**

Self-reflecting पैटर्नहरूले गुणस्तर मापदण्ड स्पष्ट राख्छन्। मोडेललाई "सही" भन्नको सट्टा तपाईँले ठ्याक्कै के "सही" हो भनी बताउनुहुन्छ: सही तर्क, त्रुटि ह्यान्डलिङ, प्रदर्शन, सुरक्षा। त्यसपछि मोडेल आफ्नै आउटपुट मूल्यांकन गरी सुधार गर्छ। यसले कोड निर्माणलाई अनौठो प्रक्रियाबाट व्यवस्थित प्रक्रियामा बदल्छ।

**सन्दर्भ सीमित छ**

मल्टि-टर्न कुराकानी प्रत्येक अनुरोधसँग सन्देश इतिहास समेटेर काम गर्छ। तर सिमा हुन्छ - हरेक मोडेलसँग अधिकतम टोकन गणना हुन्छ। जति कुरा बढ्छ, तपाईंले सान्दर्भिक सन्दर्भ कायम राख्ने तर हद नाघ्ने छैन भन्ने रणनीति अपनाउनु पर्नेछ। यो मोड्युलले तपाईंलाई सम्झनाको काम सिकाउँछ; पछि तपाईंलाई जब सारांश बनाउन, कहिले बिर्सन, र कहिले पुनः प्राप्त गर्न भनेर सिकाइनेछ।

## अर्को कदमहरू

**अर्को मोड्युल:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**नेभिगेसन:** [← अघिल्लो: Module 01 - परिचय](../01-introduction/README.md) | [मुख्यमा फर्कनुहोस्](../README.md) | [अर्को: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यस दस्तावेजलाई AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरी अनुवाद गरिएको हो। हामी पूर्ण शुद्धताको प्रयास गरिरहेका छौं, तर कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादमा त्रुटिहरू वा गल्तीहरू हुन सक्छन्। मूल दस्तावेजलाई त्यसको मूल भाषामा नै अधिकारसम्पन्न स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीका लागि व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न कुनै पनि गलतफहमी वा गलत व्याख्याको लागि हामी उत्तरदायी हौंन।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->