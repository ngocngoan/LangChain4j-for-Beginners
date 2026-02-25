# Module 02: GPT-5.2 सँग प्रॉम्प्ट इञ्जिनियरिङ

## Table of Contents

- [तपाईंले के सिक्नुहुनेछ](../../../02-prompt-engineering)
- [पूर्वआवश्यकता](../../../02-prompt-engineering)
- [प्रॉम्प्ट इञ्जिनियरिङ बुझ्न](../../../02-prompt-engineering)
- [प्रॉम्प्ट इञ्जिनियरिङ आधारहरू](../../../02-prompt-engineering)
  - [शून्य-शट प्रॉम्प्टिङ](../../../02-prompt-engineering)
  - [थोरै-शट प्रॉम्प्टिङ](../../../02-prompt-engineering)
  - [चेन अफ थट](../../../02-prompt-engineering)
  - [भूमिका आधारित प्रॉम्प्टिङ](../../../02-prompt-engineering)
  - [प्रॉम्प्ट टेम्प्लेटहरू](../../../02-prompt-engineering)
- [उन्नत ढाँचाहरू](../../../02-prompt-engineering)
- [अस्तित्वमा रहेका Azure स्रोतहरू प्रयोग गर्दै](../../../02-prompt-engineering)
- [एप्लिकेसनका स्क्रीनशटहरू](../../../02-prompt-engineering)
- [ढाँचाहरू अन्वेषण गर्दै](../../../02-prompt-engineering)
  - [कम र उच्च उत्साह](../../../02-prompt-engineering)
  - [कार्य एक्सिक्युसन (टूल प्रीगेबलस)](../../../02-prompt-engineering)
  - [स्वयं-प्रतिबिम्बन कोड](../../../02-prompt-engineering)
  - [संरचित विश्लेषण](../../../02-prompt-engineering)
  - [बहु-चरण संवाद](../../../02-prompt-engineering)
  - [चरण-दर-चरण तर्क](../../../02-prompt-engineering)
  - [नियन्त्रित आउटपुट](../../../02-prompt-engineering)
- [तपाईं साँच्चै के सिक्दै हुनुहुन्छ](../../../02-prompt-engineering)
- [अर्को कदमहरू](../../../02-prompt-engineering)

## तपाईंले के सिक्नुहुनेछ

<img src="../../../translated_images/ne/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

अघिल्लो मोड्युलमा, तपाईंले मेमोरीले कसरी संवादात्मक AI सक्षम बनाउँछ हेर्नुभयो र GitHub मोडेलहरू प्रयोग गरेर आधारभूत अन्तरक्रियाहरू गर्नुभयो। अब हामी तपाईंले कसरी प्रश्न सोध्नु हुन्छ — प्रॉम्प्टहरू आफै — Azure OpenAI को GPT-5.2 प्रयोग गरेर त्यसमा केन्द्रित हुनेछौं। तपाईंले प्रॉम्प्ट कसरी संरचना गर्नुहुन्छ भन्ने कुरा तपाईंले पाउने प्रतिक्रिया गुणस्तरमा ठूलो प्रभाव पार्दछ। हामी पहिलोमा आधारभूत प्रॉम्प्टिङ प्रविधिहरूको समीक्षा गर्छौं, त्यसपछि GPT-5.2 को क्षमताहरू पूर्ण रूपमा उपयोग गर्ने आठ उन्नत ढाँचाहरू समावेश गर्छौं।

हामी GPT-5.2 प्रयोग गर्नेछौं किनकि यसले तर्क नियन्त्रक परिचय गराउँछ — तपाईं मोडेललाई जवाफ दिनुअघि कति सोच्न भनेर भन्न सक्नुहुन्छ। यसले विभिन्न प्रॉम्प्टिङ रणनीतिहरूलाई अझ स्पष्ट बनाउँछ र तपाईंलाई कुन उपाय कहिले प्रयोग गर्ने बुझ्न मद्दत गर्छ। हामीले Azure को कम दर सीमा पनि लाभ उठाउनेछौं जुन GPT-5.2 को लागि GitHub मोडेलहरू भन्दा कम छ।

## पूर्वआवश्यकता

- मोड्युल 01 पूरा गरिसकेको (Azure OpenAI स्रोतहरू तैनाथ गरिएका)
- `.env` फाइल मुख्य निर्देशिकामा Azure प्रमाणपत्रहरू सहित (मोड्युल 01 मा `azd up` द्वारा सिर्जना गरिएको)

> **Note:** यदि तपाईंले मोड्युल 01 पूरा गर्नु भएको छैन भने, त्यहाँको तैनाथ निर्देशनहरू पहिले पालना गर्नुहोस्।

## प्रॉम्प्ट इञ्जिनियरिङ बुझ्न

<img src="../../../translated_images/ne/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

प्रॉम्प्ट इञ्जिनियरिङ भनेको त्यसरी इन्पुट टेक्स्ट डिजाइन गर्नु हो जसले तपाईंलाई आवश्यक नतिजा निरन्तर प्राप्त गराउछ। यो केवल प्रश्न सोध्नु मात्र होइन — यो अनुरोधहरू त्यसरी संरचना गर्नु हो कि मोडेलले ठीक के तपाईं चाहानु हुन्छ र कसरी प्रदान गर्ने बुझोस्।

यसलाई सहकर्मीलाई निर्देशन दिनु जस्तै सोच्नुहोस्। "Bug fix गर" भनेको अस्पष्ट छ। "UserService.java लाइन 45 मा null pointer exception ठीक गर्न null check थप्नुहोस्" भनेको स्पष्ट छ। भाषा मोडेलहरू पनि यसैगरी काम गर्छन् — विशिष्टता र संरचना महत्त्वपूर्ण हुन्छ।

<img src="../../../translated_images/ne/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4jले पूर्वाधार प्रदान गर्दछ — मोडेलजोड, मेमोरी, र सन्देश प्रकारहरू — भने प्रॉम्प्ट ढाँचाहरू त्यो पूर्वाधार मार्फत पठाउनुभएको सावधानीपूर्वक संरचित टेक्स्ट मात्र हुन्। मुख्य निर्माण खण्डहरू `SystemMessage` (जसले AI को व्यवहार र भूमिका सेट गर्छ) र `UserMessage` (जसले तपाइँको वास्तविक अनुरोध बोकेको हुन्छ) हुन्।

## प्रॉम्प्ट इञ्जिनियरिङ आधारहरू

<img src="../../../translated_images/ne/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

यस मोड्युलका उन्नत ढाँचाहरूमा प्रवेश गर्नु अघि, पाउना पाँच आधारभूत प्रॉम्प्टिङ प्रविधिको समीक्षा गरौं। यी सबै प्रॉम्प्ट इञ्जिनियरहरूले जान्नु पर्ने उपकरणहरू हुन्। यदि तपाईंले पहिले नै [Quick Start module](../00-quick-start/README.md#2-prompt-patterns) पूरा गर्नुभएको छ भने, तपाईंले यी कार्यान्वयन गर्दै देख्नु भएको छ — यहाँ तिनीहरूको वैचारिक रूपरेखा प्रस्तुत गरिएको छ।

### शून्य-शट प्रॉम्प्टिङ

सबसम्म सरल विधि: मोडेललाई कुनै उदाहरण बिना सिधा निर्देशन दिनु। मोडेलले आफ्नो तालिममा निर्भर भएर कार्य बुझ्न र सम्पन्न गर्न प्रयास गर्छ। यो त्यस्ता सोझा अनुरोधहरूका लागि राम्रो हुन्छ जहाँ अपेक्षित व्यवहार स्पष्ट हुन्छ।

<img src="../../../translated_images/ne/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*उदाहरण बिना सिधा निर्देशन — मोडेल निर्देशनबाट मात्र कार्य अनुमान लगाउँछ*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// प्रतिक्रिया: "सकारात्मक"
```

**कहिले प्रयोग गर्ने:** सरल वर्गीकरणहरू, सिधा प्रश्नहरू, अनुवादहरू, वा कुनै पनि कार्य जसमा मोडेललाई अतिरिक्त मार्गनिर्देशन आवश्यक नपर्दछ।

### थोरै-शट प्रॉम्प्टिङ

तपाईंले मोडेलले अनुसरण गर्नुपर्ने ढाँचा देखाउन उदाहरणहरू दिनुहोस्। मोडेलले तपाईंका उदाहरणहरूबाट अपेक्षित इन्पुट-आउटपुट ढाँचा सिक्छ र नयाँ इन्पुटहरूमा लागू गर्छ। यसले त्यो श्रेणीमा जहाँ फारम्याट वा व्यवहार स्पष्ट छैन, स्थिरता निकै सुधार गर्छ।

<img src="../../../translated_images/ne/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*उदाहरणहरूबाट सिक्ने — मोडेलले ढाँचा पहिचान गरी नयाँ इन्पुटमा लागू गर्छ*

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

**कहिले प्रयोग गर्ने:** अनुकूलित वर्गीकरणहरू, स्थायी स्वरूप, डोमेन-विशिष्ट कार्यहरू, वा जब शून्य-शट परिणामहरू असंगत हुन्छन्।

### चेन अफ थट

मोडेललाई आफ्नो तर्क चरण-दर-चरण देखाउन लगाउनुहोस्। जवाफमा तुरुन्त छलाङ नमारिकन, मोडेलले समस्यालाई टुक्र्याएर स्पष्टरूपमा हरेक भाग कार्यान्वयन गर्दछ। यसले गणित, तर्क, र बहु-चरण तर्क कार्यहरूमा शुद्धता सुधार गर्दछ।

<img src="../../../translated_images/ne/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*चरण-दर-चरण तर्क — जटिल समस्याहरूलाई स्पष्ट तर्क चरणहरूमा विभाजन*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// मोडेल देखाउँछ: १५ - ८ = ७, त्यसपछि ७ + १२ = १९ स्याउहरू
```

**कहिले प्रयोग गर्ने:** गणित समस्याहरू, तर्कका पहेलिहरू, डिबगिङ, वा कुनै पनि कार्य जसमा तर्क प्रक्रिया देखाउनु शुद्धता र विश्वास बढाउँछ।

### भूमिका आधारित प्रॉम्प्टिङ

तपाईंको प्रश्न सोध्नु अघि AI लाई कुनै पात्र वा भूमिका सेट गर्नुहोस्। यसले प्रतिक्रिया को स्वर, गहिराइ, र केन्द्रित विषय निर्धारण गर्न सन्दर्भ प्रदान गर्छ। "सफ्टवेयर आर्किटेक्ट" ले "जूनियर डेभलपर" वा "सुरक्षा अडिटर" भन्दा फरक सुझाव दिन्छ।

<img src="../../../translated_images/ne/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*सन्दर्भ र पात्र निर्धारण — एउटै प्रश्नले विभिन्न भूमिकाहरूमा फरक प्रतिक्रिया पाउँछ*

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

**कहिले प्रयोग गर्ने:** कोड समीक्षा, ट्युटोरिङ, डोमेन-विशिष्ट विश्लेषण, वा जब आपत्कालीन विशेषज्ञताको स्तर वा दृष्टिकोण अनुसार प्रतिक्रिया आवश्यक हुन्छ।

### प्रॉम्प्ट टेम्प्लेटहरू

परिवर्तनीय प्लेसहोल्डरहरूसँग पुन: प्रयोग गर्न सकिने प्रॉम्प्टहरू बनाउनुहोस्। प्रत्येक पटक नयाँ प्रॉम्प्ट लेख्ने सट्टा, एक पटक टेम्प्लेट परिभाषित गर्नुहोस् र फरक मानहरू भर्नुहोस्। LangChain4j को `PromptTemplate` क्लासले `{{variable}}` सिन्ट्याक्स प्रयोग गरेर यो सजिलो बनाउँछ।

<img src="../../../translated_images/ne/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*परिवर्तनीय प्लेसहोल्डरहरूसँग पुन: प्रयोग हुने प्रॉम्प्टहरू — एक टेम्प्लेट, बहुमुखी प्रयोग*

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

**कहिले प्रयोग गर्ने:** फरक इन्पुट्सका साथ दोहोरिने सोधपुछ, ब्याच प्रोसेसिङ, पुन: प्रयोग हुने AI वर्कफ्लोज बनाउँदा, वा कुनै पनि परिदृश्य जहाँ प्रॉम्प्ट संरचना उस्तै तर डेटा फरक हुन्छ।

---

यी पाँच आधारभूत उपकरणहरूले तपाईंलाई अधिकांश प्रॉम्प्टिङ कार्यहरूको लागि मजबूत आधार दिन्छन्। यस मोड्युलको बाँकी भागमा ती माथि निर्माण गर्दै **आठ उन्नत ढाँचाहरू** प्रस्तुत गरिएको छ जुन GPT-5.2 को तर्क नियन्त्रक, स्व-मूल्यांकन, र संरचित आउटपुट क्षमताहरू प्रयोग गर्छन्।

## उन्नत ढाँचाहरू

आधारभूतहरू कभर भएपछि, यो मोड्युललाई विशेष बनाउने आठ उन्नत ढाँचाहरूमा जानुहोस्। सबै समस्याहरू एकै विधि चाहिँदैनन्। केही प्रश्नहरूले छिटो जवाफ चाहिन्छ, केहीले गहिरो सोच। केहीले देखिने तर्क चाहिन्छ, केहीलाई मात्र नतिजा चाहिन्छ। तल प्रत्येक ढाँचा फरक परिदृश्यका लागि अनुकूलित छ — र GPT-5.2 को तर्क नियन्त्रकले यी भिन्नतालाई अझ प्रष्ट बनाउँछ।

<img src="../../../translated_images/ne/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*आठ प्रॉम्प्ट इञ्जिनियरिङ ढाँचाहरू र तिनका उपयोग केसहरूको अवलोकन*

<img src="../../../translated_images/ne/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 को तर्क नियन्त्रणले तपाईंलाई मोडेलले कति सोच्ने भनेर निर्धारण गर्न दिन्छ — छिटो सिधा जवाफदेखि गहिरो अन्वेषणसम्म*

**कम उत्साह (छिटो र केन्द्रित)** - सरल प्रश्नहरूका लागि जहाँ तपाईंलाई छिटो, सिधा उत्तर चाहिन्छ। मोडेल न्यूनतम तर्क गर्दछ — अधिकतम २ स्टेप्स। गणना, खोज, वा सोझा प्रश्नका लागि प्रयोग गर्नुहोस्।

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
> - "कम उत्साह र उच्च उत्साह प्रॉम्प्टिङ ढाँचाहरूमा के फरक छ?"
> - "प्रॉम्प्टहरूमा XML ट्यागहरूले AI को प्रतिक्रिया कसरी संरचना गर्न मद्दत गर्छ?"
> - "स्वयं-प्रतिबिम्बन ढाँचाहरू कहिले प्रयोग गर्ने भने सिधा निर्देशन दिने?"

**उच्च उत्साह (गहिरो र विस्तृत)** - जटिल समस्याहरूका लागि जहाँ तपाईंलाई व्यापक विश्लेषण चाहिन्छ। मोडेलले गहिरो अन्वेषण गर्छ र विस्तारपूर्वक तर्क देखाउँछ। प्रणाली डिजाइन, वास्तुकला निर्णयहरू, वा जटिल अनुसन्धानका लागि प्रयोग गर्नुहोस्।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**कार्य कार्यान्वयन (चरण-दर-चरण प्रगति)** - बहु-चरण वर्कफ्लोजका लागि। मोडेलले सुरुमा योजना दिन्छ, काम गर्दा प्रत्येक चरण वर्णन गर्छ, अनि सारांश दिन्छ। माइग्रेसन, कार्यान्वयन, वा कुनै बहु-चरण प्रक्रियाका लागि प्रयोग गर्नुहोस्।

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

चेन-ओफ-थट प्रॉम्प्टिङले मोडेललाई आफ्नो तर्क देखाउन स्पष्ट रूपमा माग गर्छ, जसले जटिल कार्यहरूमा शुद्धता सुधार्छ। चरण-दर-चरण विश्लेषणले मान्छे र AI दुईवटैलाई तर्क बुझ्न सहयोग पुर्‍याउँछ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat संग प्रयास गर्नुहोस्:** यस ढाँचाको बारेमा सोध्नुहोस्:
> - "लामो-समय चल्ने अपरेसनहरूको लागि कार्य कार्यान्वयन ढाँचालाई कसरी अनुकूल बनाउने?"
> - "उत्पादन एप्लिकेसनहरूमा टूल प्रीगेबलहरू संरचना गर्नका लागि सबैभन्दा राम्रो अभ्यासहरू के हुन्?"
> - "UI मा मध्यवर्ती प्रगति अपडेट कसरी समात्ने र देखाउने?"

<img src="../../../translated_images/ne/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*बहु-चरण कार्यहरूको लागि योजना → कार्यान्वयन → सारांश Workflow*

**स्वयं-प्रतिबिम्बन कोड** - उत्पादन स्तरको कोड बनाउन। मोडेलले उत्पादन मापदण्ड अनुसार कोड जनरेट गर्छ जसमा उचित त्रुटि व्यवस्थापन हुन्छ। नयाँ फिचर वा सेवा निर्माण गर्दा प्रयोग गर्नुहोस्।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ne/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*आवर्ती सुधार चक्र - निर्माण गर्नुहोस्, मूल्याङ्कन गर्नुहोस्, समस्याहरू पहिचान गर्नुहोस्, सुधार गर्नुहोस्, दोहोर्याउनुहोस्*

**संरचित विश्लेषण** - निरन्तर मूल्याङ्कनका लागि। मोडेलले निश्चित फ्रेमवर्क प्रयोग गरेर कोड समीक्षा गर्छ (शुद्धता, अभ्यासहरू, प्रदर्शन, सुरक्षा, मर्मतसम्भारयोग्यता)। कोड समीक्षा वा गुणस्तर मूल्याङ्कनमा प्रयोग गर्नुहोस्।

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

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat सँग प्रयास गर्नुहोस्:** संरचित विश्लेषणको बारेमा सोध्नुहोस्:
> - "विभिन्न प्रकारका कोड समीक्षाहरूको लागि विश्लेषण फ्रेमवर्क कसरी अनुकूल बनाउने?"
> - "संरचित आउटपुटलाई प्रोग्रामेटिक रूपमा पार्स गर्ने र त्यसमा आधारित कार्यहरू गर्ने सबैभन्दा राम्रो तरिका के हो?"
> - "विभिन्न समीक्षा सत्रहरूमा निरन्तर तिव्रता स्तर कसरी सुनिश्चित गर्ने?"

<img src="../../../translated_images/ne/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*तिव्रता स्तरहरू सहित निरन्तर कोड समीक्षा फ्रेमवर्क*

**बहु-चरण संवाद** - सन्दर्भ चाहिने संवादहरूको लागि। मोडेलले अघिल्लो सन्देशहरू सम्झन्छ र त्यसमा आधारित उत्तर दिन्छ। अन्तरक्रियात्मक सहयोग सत्र वा जटिल Q&A का लागि प्रयोग गर्नुहोस्।

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

*कसरी संवाद सन्दर्भले बहु चरणहरूमा जम्मा भएर टोकन सिमानासम्म पुग्छ*

**चरण-दर-चरण तर्क** - देखिने तार्किक प्रक्रिया आवश्यक समस्या हरूका लागि। मोडेलले प्रत्येक चरणको स्पष्ट तर्क देखाउँछ। गणित समस्या, तर्क पहेली, वा सोच प्रक्रिया बुझ्न आवश्यक पर्दा प्रयोग गर्नुहोस्।

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

*समस्याहरूलाई स्पष्ट तार्किक चरणहरूमा विभाजन*

**नियन्त्रित आउटपुट** - विशेष फारम्याट आवश्यक पर्ने प्रतिक्रियाहरूको लागि। मोडेलले फारम्याट र लम्बाइका नियमहरू कडाइका साथ पालना गर्छ। सारांशहरू वा जहाँ तपाईंलाई ठीक प्रिन्ट आउट चाहिए, त्यहाँ प्रयोग गर्नुहोस्।

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

*विशिष्ट फारम्याट, लम्बाइ, र संरचना आवश्यकताहरू लागू गर्दै*

## अस्तित्वमा रहेका Azure स्रोतहरू प्रयोग गर्दै

**तैनाथ पुष्टि गर्नुहोस्:**

Azure प्रमाणपत्रहरू सहित `.env` फाइल मुख्य निर्देशिकामा अवस्थित छ भनेर सुनिश्चित गर्नुहोस् (मोड्युल 01 मा सिर्जना गरिएको):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT देखाउनु पर्छ
```

**एप्लिकेसन सुरु गर्नुहोस्:**

> **Note:** यदि तपाईंले मोड्युल 01 बाट सबै एप्लिकेसनहरू `./start-all.sh` प्रयोग गरेर पहिले नै सुरु गर्नुभएको छ भने, यो मोड्युल पोर्ट 8083 मा पहिले नै चलिरहेको छ। तलका सुरु आदेशहरू छोडेर सिधै http://localhost:8083 मा जान सक्नुहुन्छ।

**विकल्प 1: Spring Boot ड्यासबोर्ड प्रयोग गर्दै (VS Code प्रयोगकर्ताहरूका लागि सिफारिस गरिएको)**

डेभ कन्टेनरमा Spring Boot ड्यासबोर्ड एक्सटेन्सन समावेश छ, जसले सबै Spring Boot एप्लिकेसनहरू व्यवस्थापन गर्न दृश्यात्मक अन्तरफलक प्रदान गर्दछ। तपाईंले यो VS Code को बायाँपट्टि एक्टिभिटी बारमा (Spring Boot आइकन खोज्नुहोस्) फेला पार्नुहुनेछ।

Spring Boot ड्यासबोर्डबाट तपाईंले:
- कार्यस्थानमा सबै उपलब्ध Spring Boot एप्लिकेसनहरू देख्न सक्नुहुन्छ
- एक क्लिकमा एप्लिकेसनहरू सुरु/रोक्न सक्नुहुन्छ
- एप्लिकेसन लगहरू रियल-टाइम हेर्न सक्नुहुन्छ
- एप्लिकेसन स्थिति अनुगमन गर्न सक्नुहुन्छ
"prompt-engineering" को छेउमा प्ले बटनमा क्लिक गरेर यो मोड्युल सुरु गर्नुहोस्, वा सबै मोड्युलहरू एकैचोटी सुरु गर्नुहोस्।

<img src="../../../translated_images/ne/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**विकल्प २: शेल स्क्रिप्टहरू प्रयोग गर्दै**

सबै वेब अनुप्रयोगहरू सुरु गर्नुहोस् (मोड्युल ०१-०४):

**Bash:**
```bash
cd ..  # रूट निर्देशिका बाट
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # मूल निर्देशिकाबाट
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

दुवै स्क्रिप्टहरूले स्वतः मूल `.env` फाइलबाट वातावरण चरहरू लोड गर्छन् र यदि JAR फाइलहरू छैनन् भने निर्माण गर्छन्।

> **सूचना:** यदि तपाईं सबै मोड्युलहरूलाई म्यानुअली निर्माण गर्न चाहनुहुन्छ भने सुरु गर्नु अघि:
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

## अनुप्रयोगका स्क्रीनशटहरू

<img src="../../../translated_images/ne/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*मुख्य ड्यासबोर्डले सबै ८ प prompt इन्जिनियरिङ् ढाँचाहरू तिनीहरूको विशेषताहरू र उपयोग केसहरूसहित देखाउँछ*

## ढाँचाहरू अन्वेषण गर्दै

वेब इन्टरफेसले तपाईंलाई विभिन्न प्रॉम्प्टिङ् रणनीतिहरू परीक्षण गर्न दिन्छ। प्रत्येक ढाँचाले फरक समस्याहरू समाधान गर्छ - प्रयोग गरेर हेर्नुहोस् कुन अवस्थामा कुन तरिका उत्कृष्ट हुन्छ।

> **सूचना: स्ट्रिमिङ् बनाम नन-स्ट्रिमिङ्** — प्रत्येक ढाँचाको पृष्ठमा दुई बटनहरू छन्: **🔴 स्ट्रिम प्रतिक्रिया (लाइभ)** र एक **नन-स्ट्रिमिङ्** विकल्प। स्ट्रिमिङ्ले Server-Sent Events (SSE) प्रयोग गरेर मोडेलले टोकनहरू उत्पादन गर्दा तिनीहरूलाई वास्तविक समयमा देखाउँछ, जसले तपाईंलाई तुरुन्त प्रगति देखाउँदछ। नन-स्ट्रिमिङ् विकल्पले समग्र प्रतिक्रिया पुरा हुने प्रतिक्षा गर्छ। गहिरो तर्कका लागि प्रॉम्प्टहरू (जस्तै, हाई ईगरनेस, सेल्फ-रिफ्लेक्टिङ् कोड) को लागि नन-स्ट्रिमिङ् कल निकै लामो समय लगाउन सक्छ — कहिले काहीं मिनेटसम्म — र कुनै देखिने फिडब्याक हुँदैन। **जटिल प्रॉम्प्टहरू परीक्षण गर्दा स्ट्रिमिङ्ग प्रयोग गर्नुहोस्** ताकि तपाईं मोडेलको काम देख्न सक्नुहोस् र अनुरोध समय समाप्त भएको जस्तो अनुभूति नहोस्।
>
> **सूचना: ब्राउजर आवश्यकताः** — स्ट्रिमिङ्ग सुविधा Fetch Streams API (`response.body.getReader()`) प्रयोग गर्छ जुन पूर्ण ब्राउजरहरू (Chrome, Edge, Firefox, Safari) मा मात्र काम गर्छ। यो VS Code को बिल्ट-इन सिम्पल ब्राउजरमा काम गर्दैन, किनकि यसको वेबभ्यूले ReadableStream API लाई समर्थन गर्दैन। यदि तपाईं सिम्पल ब्राउजर प्रयोग गर्नुहुन्छ भने नन-स्ट्रिमिङ् बटनहरू सामान्य रूपमा काम गर्नेछन् — मात्र स्ट्रिमिङ्ग बटनहरू प्रभावित हुन्छन्। पूर्ण अनुभवको लागि बाह्य ब्राउजरमा `http://localhost:8083` खोल्नुहोस्।

### कम बनाम उच्च ईगरनेस

"200 को 15% कति हो?" जस्तो सरल प्रश्न कम ईगरनेस प्रयोग गरेर सोध्नुहोस्। तपाईंलाई तुरुन्त र सिधा उत्तर प्राप्त हुनेछ। अब "उच्च-ट्राफिक API का लागि क्यासिङ् रणनीति डिजाइन गर्नुहोस्" जस्तो जटिल प्रश्न उच्च ईगरनेस प्रयोग गरेर सोध्नुहोस्। **🔴 स्ट्रिम प्रतिक्रिया (लाइभ)** क्लिक गरेर मोडेलको विस्तृत विचार प्रक्रियालाई टोकन-टोकनमा हेर्नुहोस्। एउटै मोडेल, एउटै प्रश्न संरचना - तर प्रॉम्प्टले कति सोच्नुपर्ने कुरा बताउँछ।

### कार्य कार्यान्वयन (टूल प्रिम्बलहरू)

बहु-चरण कार्यहरू अग्रिम योजना र प्रगति कथनबाट लाभान्वित हुन्छन्। मोडेलले के गर्नेछ भन्छ, प्रत्येक चरण वर्णन गर्छ, र परिणामहरू सारांशित गर्छ।

### स्व-समीक्षात्मक कोड

"इमेल मान्यकरण सेवा बनाउनुहोस्" प्रयास गर्नुहोस्। केवल कोड उत्पादन गरी रोक्ने सट्टा, मोडेलले उत्पादन गर्छ, गुणस्तर मापनमा मूल्याङ्कन गर्छ, कमजोरीहरू पत्ता लगाउँछ, र सुधार गर्छ। तपाईंले मोडेललाई उत्पादन स्तरसम्म पुग्न दोहोर्याइरहेको देख्नुहुनेछ।

### संरचित विश्लेषण

कोड समीक्षा लागि सुसंगत मूल्याङ्कन ढाँचा आवश्यक हुन्छ। मोडेलले निश्चित वर्गीकरणहरू (सहीपन, अभ्यास, प्रदर्शन, सुरक्षा) र तीव्रता स्तर प्रयोग गरी कोड विश्लेषण गर्छ।

### बहु-चरण संवाद

"स्प्रिंग बूट के हो?" सोध्नुहोस् र तुरुन्तै "एक उदाहरण देखाउनुहोस्" सोध्नुहोस्। मोडेलले तपाईंको पहिलो प्रश्न सम्झन्छ र तपाईंलाई खास स्प्रिंग बूट उदाहरण दिन्छ। स्मरण नभए अर्काे प्रश्न धेरै अस्पष्ट हुन्थ्यो।

### चरण-दर-चरण तर्क

कुनै पनि गणितीय समस्या रोज्नुहोस् र यसलाई चरण-चरण तर्क र कम ईगरनेसका साथ प्रयास गर्नुहोस्। कम ईगरनेसले मात्र उत्तर दिन्छ - छिटो तर अस्पष्ट। चरण-दर-चरणले प्रत्येक गणना र निर्णय देखाउँछ।

### सिमा निर्धारण गरिएको आउटपुट

विशिष्ट ढाँचा वा शब्द गणना चाहिएको बेला, यो ढाँचाले कडाइका साथ पालना गर्छ। ठीक १०० शब्दहरूमा बुलेट पोइन्ट स्वरूपमा सारांश सिर्जना गरेर प्रयास गर्नुहोस्।

## तपाईं के वास्तवमा सिक्दै हुनुहुन्छ

**तर्क प्रयासले सबै कुरा परिवर्तन गर्छ**

GPT-5.2 ले तपाईंलाई प्रॉम्प्ट मार्फत गणनात्मक प्रयास नियन्त्रण गर्न अनुमति दिन्छ। कम प्रयास भनेको छिटो प्रतिक्रिया र कम अन्वेषण हो। उच्च प्रयास भनेको मोडेलले गहिरो सोचमा समय लिने हो। तपाईं सिक्दै हुनुहुन्छ कि कामको जटिलतालाई उपयुक्त प्रयास मिलाउनु - सरल प्रश्नमा समय फाल्न नहुने, तर जटिल निर्णयमा हतार नगर्ने।

**संरचनाले व्यवहारलाई मार्गदर्शन गर्छ**

प्रॉम्प्टहरूमा XML ट्यागहरू देख्नुहुन्छ? ती सजावट होइनन्। मोडेलहरूलाई संरचित निर्देशनहरू स्वतन्त्र पाठभन्दा धेरै भरपर्दो हुन्छन्। जब तपाईंलाई बहु-चरण प्रक्रिया वा जटिल तार्किकता चाहिन्छ, संरचनाले मोडेललाई थाहा हुन्छ यहा छ र के आउँछ।

<img src="../../../translated_images/ne/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*खुला खण्डहरू र XML-शैली संगठन सहित राम्रो संरचित प्रॉम्प्टको संरचना*

**गुणस्तर स्व-मूल्याङ्कन मार्फत**

स्व-समीक्षात्मक ढाँचाहरूले गुणस्तर मापन स्पष्ट बनाउँछन्। मोडेलले "सही गर्छ" भनेर आशा गर्ने सट्टा, तपाईंले "सही" के हो भन्नुहुन्छ: ठिक तार्किकता, त्रुटि व्यवस्थापन, प्रदर्शन, सुरक्षा। मोडेल आफ्नो आउटपुटलाई मूल्याङ्कन गरी सुधार गर्न सक्छ। यसले कोड सिर्जनालाई लटरीबाट प्रक्रिया बनाउँछ।

**सन्दर्भ सीमित छ**

बहु-चरण संवादले प्रत्येक अनुरोधसँग सन्देश इतिहास समावेश गरेर काम गर्छ। तर सीमा हुन्छ - प्रत्येक मोडेलको अधिकतम टोकन संख्या हुन्छ। संवाद बढ्दै गर्दा, सान्दर्भिक सन्दर्भ कायम राख्न तर सीमा नछुने रणनीतिहरू आवश्यक हुन्छ। यो मोड्युलले स्मरण कसरि काम गर्छ देखाउँछ; पछि तपाईंले कहिले सारांश बनाउन, कहिले भुल्न, र कहिले पुन: पुनः प्राप्त गर्न सिक्नुहुनेछ।

## अर्को चरणहरू

**अर्को मोड्युल:** [०३-र्याग - RAG (रिट्रिभल-अग्मेंटेड जेनेरेशन)](../03-rag/README.md)

---

**नेभिगेशन:** [← अघिल्लो: मोड्युल ०१ - परिचय](../01-introduction/README.md) | [मुख्यमा फर्कनुहोस्](../README.md) | [अर्को: मोड्युल ०३ - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यस कागजातलाई AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरेर अनुवाद गरिएको हो। हामी शुद्धताको लागि प्रयासरत रहे पनि, कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादहरूमा त्रुटि वा अशुद्धता हुन सक्छ। मूल कागजात यसको मूल भाषामा प्रामाणिक स्रोत मानिनु पर्छ। महत्वपूर्ण जानकारीको लागि, व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न कुनै पनि समझदारीमा भ्रम वा गलत व्याख्याका लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->