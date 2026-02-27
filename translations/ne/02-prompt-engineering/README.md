# मोड्युल ०२: GPT-5.2 सँग प्रॉम्प्ट इन्जिनियरिङ

## विषय सूची

- [भिडियो walkthrough](../../../02-prompt-engineering)
- [तपाईंले के सिक्नुहुनेछ](../../../02-prompt-engineering)
- [आवश्यकताहरू](../../../02-prompt-engineering)
- [प्रॉम्प्ट इन्जिनियरिङको बुझाइ](../../../02-prompt-engineering)
- [प्रॉम्प्ट इन्जिनियरिङका आधारभूत कुरा](../../../02-prompt-engineering)
  - [Zero-Shot Prompting](../../../02-prompt-engineering)
  - [Few-Shot Prompting](../../../02-prompt-engineering)
  - [Chain of Thought](../../../02-prompt-engineering)
  - [Role-Based Prompting](../../../02-prompt-engineering)
  - [प्रॉम्प्ट टेम्प्लेटहरू](../../../02-prompt-engineering)
- [उन्नत ढाँचाहरू](../../../02-prompt-engineering)
- [अस्तित्वमा रहेका Azure स्रोतहरू प्रयोग गर्दै](../../../02-prompt-engineering)
- [एप्लिकेसनका स्क्रिनसटहरू](../../../02-prompt-engineering)
- [ढाँचाहरूको अन्वेषण](../../../02-prompt-engineering)
  - [कम र उच्च उत्साह](../../../02-prompt-engineering)
  - [कार्य कार्यान्वयन (टुल प्रीएम्बलहरू)](../../../02-prompt-engineering)
  - [आत्म-प्रतिबिम्बित कोड](../../../02-prompt-engineering)
  - [संरचित विश्लेषण](../../../02-prompt-engineering)
  - [धेरै-टर्न च्याट](../../../02-prompt-engineering)
  - [चरण-द्वारा-चरण तर्क](../../../02-prompt-engineering)
  - [सीमित आउटपुट](../../../02-prompt-engineering)
- [तपाईं साँच्चिकै के सिक्दै हुनुहुन्छ](../../../02-prompt-engineering)
- [अर्को कदमहरू](../../../02-prompt-engineering)

## भिडियो walkthrough

यो प्रत्यक्ष सत्र हेर्नुहोस् जसले कसरी यो मोड्युल सुरु गर्ने बताउँछ: [Prompt Engineering with LangChain4j - Live Session](https://www.youtube.com/live/PJ6aBaE6bog?si=LDshyBrTRodP-wke)

## तपाईंले के सिक्नुहुनेछ

<img src="../../../translated_images/ne/what-youll-learn.c68269ac048503b2.webp" alt="What You'll Learn" width="800"/>

अघिल्लो मोड्युलमा, तपाईंले देख्नुभयो कसरी मेमोरीले संवादात्मक AI सक्षम बनाउँछ र GitHub मोडेलहरूलाई आधारभूत अन्तरक्रियाहरूका लागि प्रयोग गरिन्थ्यो। अब हामी कसरी प्रश्न सोध्ने—प्रॉम्प्टहरू आफैं—Azure OpenAI को GPT-5.2 प्रयोग गरेर केन्द्रित हुनेछौं। तपाईंले आफ्नो प्रॉम्प्टहरू कसरी संरचना गर्नुहुन्छ भन्ने कुरा तपाईंले पाउने प्रतिक्रियाको गुणस्तरमा निर्णायक प्रभाव पार्छ। हामी आधारभूत प्रॉम्प्टिङ प्रविधिहरूको समीक्षा गरेर सुरु गर्छौं, त्यसपछि आठवटा उन्नत ढाँचाहरूमा जान्छौं जसले GPT-5.2 को क्षमता पूर्ण रूपमा उपयोग गर्छ।

हामी GPT-5.2 प्रयोग गर्नेछौं किनभने यसले तर्क नियन्त्रण समावेश गर्दछ—तपाईं मोडेललाई जवाफ दिनुअघि कति सोच्नुपर्ने भनेर भन्न सक्नुहुन्छ। यसले विभिन्न प्रॉम्प्टिङ रणनीतिहरूलाई अझ स्पष्ट बनाउँछ र तपाईंलाई कुन तरिका कहिलेकाहीं प्रयोग गर्ने बुझ्न मद्दत गर्छ। साथै, Azure को GPT-5.2 का लागि GitHub मोडेलहरूको तुलनामा कम दर सीमाहरूको फाइदा पनि पाइन्छ।

## आवश्यकताहरू

- मोड्युल ०१ पूरा गरिएको (Azure OpenAI स्रोतहरू परिनियोजन गरिएको)
- `.env` फाइल रूट डिरेक्टरीमा Azure प्रमाणपत्रहरू सहित (मोड्युल ०१ मा `azd up` द्वारा सिर्जना गरिएको)

> **टिप्पणी:** यदि तपाईंले मोड्युल ०१ पूरा गर्नुभएन भने, पहिले त्यहाँको परिनियोजन निर्देशनहरू पालना गर्नुहोस्।

## प्रॉम्प्ट इन्जिनियरिङ बुझ्न

<img src="../../../translated_images/ne/what-is-prompt-engineering.5c392a228a1f5823.webp" alt="What is Prompt Engineering?" width="800"/>

प्रॉम्प्ट इन्जिनियरिङ भनेको यस्तो इनपुट टेक्स्ट डिजाइन गर्ने कार्य हो जुन तपाईंले आवश्यक नतिजा निरन्तर प्राप्त गर्नुहुनेछ। यो केवल प्रश्न सोध्नको बारेमा होइन—यो अनुरोधहरू संरचना गर्ने हो ताकि मोडेलले ठ्याक्कै के चाहिन्छ र कसरी प्रदान गर्ने बुझोस्।

यसलाई एक सहकर्मीलाई निर्देशन दिने जस्तै सोच्नुहोस्। "Bug फिक्स गर" अस्पष्ट छ। "UserService.java को लाइन ४५ मा रहेको null pointer exception लाई null check थपेर फिक्स गर" Specific छ। भाषा मोडेलहरूले पनि यही तरिकाले काम गर्दछन्—स्पष्टता र संरचनाले महत्त्व राख्छ।

<img src="../../../translated_images/ne/how-langchain4j-fits.dfff4b0aa5f7812d.webp" alt="How LangChain4j Fits" width="800"/>

LangChain4j ले इन्फ्रास्ट्रक्चर प्रदान गर्दछ—मोडेल कनक्सनहरू, मेमोरी, र सन्देश प्रकारहरू—जबकि प्रॉम्प्ट ढाँचाहरू त्यस्ता सावधानीपूर्वक संरचित टेक्स्टहरू हुन् जुन तपाईं उक्त इन्फ्रास्ट्रक्चर मार्फत पठाउनुहुन्छ। मुख्य निर्माण खन्डहरू हुन् `SystemMessage` (जो AI को व्यवहार र भूमिका सेट गर्दछ) र `UserMessage` (जो तपाईंको वास्तविक अनुरोध लिन्छ)।

## प्रॉम्प्ट इन्जिनियरिङका आधारभूत कुरा

<img src="../../../translated_images/ne/five-patterns-overview.160f35045ffd2a94.webp" alt="Five Prompt Engineering Patterns Overview" width="800"/>

यस मोड्युलका उन्नत ढाँचाहरूमा डुब्नु अघि, हामिले पाँच आधारभूत प्रॉम्प्टिङ प्रविधिहरू समीक्षा गरौं। यी प्रॉम्प्ट इन्जिनियरले जान्नुपर्ने आधारभूत निर्माण खन्डहरू हुन्। यदि तपाईंले पहिले नै [Quick Start मोड्युल](../00-quick-start/README.md#2-prompt-patterns) सम्पन्न गर्नुभएको छ भने तपाईंले यी क्रियाशील अवस्थामा देख्नुभएको छ—यहाँ तिनीहरूको अवधारणात्मक संरचना छ।

### Zero-Shot Prompting

सबैभन्दा सरल तरिका: मोडेललाई कुनै उदाहरण बिना सिधा निर्देशन दिनुहोस्। मोडेल आफ्नो प्रशिक्षणमा आधारित भएर काम बुझेर कार्यान्वयन गर्छ। यो सोझा अनुरोधहरूका लागि उपयुक्त हुन्छ जहाँ अपेक्षित व्यवहार स्पष्ट हुन्छ।

<img src="../../../translated_images/ne/zero-shot-prompting.7abc24228be84e6c.webp" alt="Zero-Shot Prompting" width="800"/>

*उदाहरण बिना सिधा निर्देशन—मोडेल निर्देशनबाट मात्र कार्य अनुमान लगाउँछ*

```java
String prompt = "Classify this sentiment: 'I absolutely loved the movie!'";
String response = model.chat(prompt);
// प्रतिक्रिया: "सकारात्मक"
```

**कहिले प्रयोग गर्ने:** सरल वर्गीकरणहरू, सिधा प्रश्नहरू, अनुवादहरू, वा कुनै पनि कार्य जसलाई मोडेल थप मार्गदर्शन बिना ह्यान्डल गर्न सक्छ।

### Few-Shot Prompting

उदाहरणहरू प्रदान गर्नुहोस् जसले तपाईंले मोडेललाई पछ्याउन चाहनुभएको ढाँचालाई देखाउँछ। मोडेलले तपाईंका उदाहरणहरूबाट अपेक्षित इनपुट-आउटपुट ढाँचा सिक्छ र नयाँ इनपुटहरूमा सो लागू गर्छ। यो जसमा आवश्यक ढाँचा वा व्यवहार स्पष्ट हुँदैन, त्यस्ता कामहरूको लागि धेरै प्रभावकारी हुन्छ।

<img src="../../../translated_images/ne/few-shot-prompting.9d9eace1da88989a.webp" alt="Few-Shot Prompting" width="800"/>

*उदाहरणहरूबाट सिक्दै—मोडेलले ढाँचालाई पहिचान गरी नयाँ इनपुटमा लागू गर्छ*

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

**कहिले प्रयोग गर्ने:** अनुकूल वर्गीकरण, सुसंगत फर्म्याटिङ, डोमेन-विशेष कार्यहरू, वा जब zero-shot परिणामहरू असंगत हुन्छन्।

### Chain of Thought

मोडेललाई चरण-द्वारा-चरण आफ्नो तर्क देखाउन भन्नुहोस्। जवाफमा सिधा जाने सट्टा, मोडेल समस्या टुक्र्याएर प्रत्येक भाग स्पष्ट रूपमा कार्यान्वयन गर्छ। यसले गणित, तर्क, र बहु-चरण तर्क कार्यहरूमा शुद्धता सुधार गर्दछ।

<img src="../../../translated_images/ne/chain-of-thought.5cff6630e2657e2a.webp" alt="Chain of Thought Prompting" width="800"/>

*चरण-द्वारा-चरण तर्क—जटिल समस्याहरूलाई स्पष्ट तर्कका चरणहरूमा विभाजन*

```java
String prompt = """
    Problem: A store has 15 apples. They sell 8 apples and then 
    receive a shipment of 12 more apples. How many apples do they have now?
    
    Let's solve this step-by-step:
    """;
String response = model.chat(prompt);
// मोडेलले देखाउँछ: १५ - ८ = ७, त्यसपछि ७ + १२ = १९ स्याउहरु
```

**कहिले प्रयोग गर्ने:** गणित समस्या, तर्क पहेलाहरू, डीबगिङ, वा कुनै पनि कार्य जसमा तर्क प्रक्रिया देखाउनाले शुद्धता र विश्वास सुधार हुन्छ।

### Role-Based Prompting

प्रश्न सोध्नु अघि AI को लागि एउटा पात्र वा भूमिका सेट गर्नुहोस्। यसले सन्दर्भ दिन्छ जसले जवाफको स्वर, गहिराइ, र केन्द्रितता निर्धारण गर्छ। एक "सफ्टवेयर आर्किटेक्ट" ले "जुनियर डेभलपर" वा "सुरक्षा अडिटर" भन्दा फरक सुझाव दिन्छ।

<img src="../../../translated_images/ne/role-based-prompting.a806e1a73de6e3a4.webp" alt="Role-Based Prompting" width="800"/>

*सन्दर्भ र पात्र सेट गर्दै—उही प्रश्न भूमिका अनुसार फरक जवाफ पाइन्छ*

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

**कहिले प्रयोग गर्ने:** कोड समीक्षा, ट्युटरिङ, डोमेन-विशेष विश्लेषण, वा जब तपाईंलाई कुनै निश्चित विशेषज्ञता स्तर वा दृष्टिकोण अनुरूप जवाफ चाहिन्छ।

### प्रॉम्प्ट टेम्प्लेटहरू

पुन: प्रयोग गर्न मिल्ने प्रॉम्प्टहरू सिर्जना गर्नुहोस् जसमा चल बदलिने क्षेत्रहरू हुन्छन्। प्रत्येक पटक नयाँ प्रॉम्प्ट लेख्ने सट्टा, एक पटक टेम्प्लेट परिभाषित गरेर विभिन्न मानहरू हाल्नुहोस्। LangChain4j को `PromptTemplate` वर्गले `{{variable}}` सिन्ट्याक्ससँग यो सजिलो बनाउँछ।

<img src="../../../translated_images/ne/prompt-templates.14bfc37d45f1a933.webp" alt="Prompt Templates" width="800"/>

*चल बदलिने क्षेत्रहरू सहित पुन: प्रयोग गर्न मिल्ने प्रॉम्प्टहरू—एक टेम्प्लेट, धेरै प्रयोगहरू*

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

**कहिले प्रयोग गर्ने:** फरक इनपुटहरूसँग दोहोरिएको प्रश्नहरू, ब्याच प्रशोधन, पुन: प्रयोग गर्न मिल्ने AI कार्यप्रवाह बनाउन, वा जहाँ प्रॉम्प्ट संरचना उस्तै रहन्छ तर डेटा फरकम हुन्छ।

---

यी पाँच आधारभूत कुराहरूले तपाईंलाई अधिकांश प्रॉम्प्टिङ कार्यहरूको लागि मजबुत उपकरण दिन्छ। बाँकी मोड्युलले यसमा आधारित भएर GPT-5.2 को तर्क नियन्त्रण, आत्म-मूल्याङ्कन, र संरचित आउटपुट क्षमताहरूलाई उपयोग गर्ने **आठवटा उन्नत ढाँचाहरू** प्रस्तुत गर्छ।

## उन्नत ढाँचाहरू

आधारभूत कुरा पुरा भएपछि, अब हामी ति आठ उन्नत ढाँचाहरूमा जान्छौं जसले यो मोड्युललाई अनौठो बनाउँछन्। सबै समस्याहरूलाई एउटै तरिका आवश्यक पर्दैन। केही प्रश्नहरूलाई छिटो जवाफ चाहिन्छ, केहीलाई गहिरो सोच चाहिन्छ। केहीलाई देखिने तर्क चाहिन्छ, केहीलाई मात्र नतिजा चाहिन्छ। प्रत्येक ढाँचा फरक परिदृश्यका लागि अनुकूलित छ—र GPT-5.2 को तर्क नियन्त्रणले यी भिन्नताहरू अझ स्पष्ट बनाउँछ।

<img src="../../../translated_images/ne/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*आठ वटा प्रॉम्प्ट इन्जिनियरिङ ढाँचाहरू र तिनका प्रयोग केसहरूको अवलोकन*

<img src="../../../translated_images/ne/reasoning-control.5cf85f0fc1d0c1f3.webp" alt="Reasoning Control with GPT-5.2" width="800"/>

*GPT-5.2 को तर्क नियन्त्रणले तपाईंले मोडेललाई कति सोच्नुपर्ने भनेर निर्दिष्ट गर्न दिन्छ—छिटो सिधा जवाफदेखि गहिरो अन्वेषणसम्म*

**कम उत्साह (छिटो र केन्द्रित)** - सोझा प्रश्नहरूका लागि जहाँ छिटो र सिधा जवाफ चाहिन्छ। मोडेलले न्यूनतम तर्क गर्छ—अधिकतम २ चरण। गणना, खोज, वा सोझा प्रश्नहरूको लागि प्रयोग गर्नुहोस्।

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
> - "प्रॉम्प्टहरूमा XML ट्यागहरूले AI को जवाफ कसरी संरचना गर्न मद्दत गर्छ?"
> - "म आफै प्रतिबिम्बित गर्ने ढाँचाहरू कहिले र सिधा निर्देशन कहिले प्रयोग गर्ने?"

**उच्च उत्साह (गहिरो र विस्तृत)** - जटिल समस्याहरूका लागि जहाँ व्यापक विश्लेषण चाहिन्छ। मोडेलले गहिरो अन्वेषण गर्छ र विस्तृत तर्क देखाउँछ। प्रणाली डिजाइन, आर्किटेक्चर निर्णय, वा जटिल अनुसन्धानका लागि प्रयोग गर्नुहोस्।

```java
String prompt = """
    Analyze this problem thoroughly and provide a comprehensive solution.
    Consider multiple approaches, trade-offs, and important details.
    Show your analysis and reasoning in your response.
    
    Problem: Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**कार्य कार्यान्वयन (चरण-द्वारा-चरण प्रगति)** - बहु-चरण कार्यप्रवाहहरूका लागि। मोडेलले सुरुमा योजना दिन्छ, प्रत्येक चरण कार्यान्वयन गर्दागर्दै वर्णन गर्छ, र अन्तमा सारांश दिन्छ। माइग्रेसन, कार्यान्वयन, वा कुनै बहु-चरण प्रक्रियामा प्रयोग गर्नुहोस्।

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

Chain-of-Thought प्रॉम्प्टिङले मोडेललाई आफ्नो तर्क प्रक्रिया देखाउन स्पष्ट रूपमा भन्छ, जसले जटिल कार्यहरूमा शुद्धता सुधार गर्छ। चरण-द्वारा-चरण विश्लेषणले मानिस र AI दुवैलाइ तर्क बुझ्न मद्दत पुर्‍याउँछ।

> **🤖 GitHub Copilot च्याट प्रयोग गरी प्रयास गर्नुहोस्:** यस ढाँचाबारे सोध्नुहोस्:
> - "लामो समय चल्ने कार्यहरूका लागि कार्य कार्यान्वयन ढाँचालाई कसरी अनुकूल बनाउने?"
> - "प्रोडक्सन एप्लिकेसनहरूमा टुल प्रीएम्बलहरू संरचना गर्ने उत्तम तरिका के हो?"
> - "UI मा मध्यवर्ती प्रगति अपडेट कसरी क्याप्चर र प्रदर्शन गर्ने?"

<img src="../../../translated_images/ne/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*बहु-चरण कार्यहरूको लागि योजना → कार्यान्वयन → सारांश workflow*

**आत्म-प्रतिबिम्बित कोड** - उत्पादन-गुणस्तर कोड बनाउनका लागि। मोडेलले उत्पादन मानकहरू अनुसरण गर्दै सही त्रुटि ह्यान्डलिङसहित कोड सिर्जना गर्छ। नयाँ सुविधाहरू वा सेवाहरू बनाउँदा प्रयोग गर्नुहोस्।

```java
String prompt = """
    Generate Java code with production-quality standards: Create an email validation service
    Keep it simple and include basic error handling.
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ne/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*पुनरावृत्त सुधार चक्र - सिर्जना गर्नुहोस्, मूल्याङ्कन गर्नुहोस्, समस्या पहिचान गर्नुहोस्, सुधार गर्नुहोस्, दोहोर्याउनुहोस्*

**संरचित विश्लेषण** - सुसंगत मूल्याङ्कनका लागि। मोडेलकोड समीक्षा गर्दा निश्चित फ्रेमवर्क (शुद्धता, अभ्यास, प्रदर्शन, सुरक्षा, मर्मतसम्भार) प्रयोग गर्छ। कोड समीक्षा वा गुणस्तर मूल्याङ्कनका लागि प्रयोग गर्नुहोस्।

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

> **🤖 GitHub Copilot च्याटसँग प्रयास गर्नुहोस्:** संरचित विश्लेषण बारे सोध्नुहोस्:
> - "विभिन्न प्रकारका कोड समीक्षाहरूका लागि विश्लेषण फ्रेमवर्क कसरी अनुकूल बनाउने?"
> - "संरचित आउटपुटलाई प्रोग्रामेटिक रूपमा पार्स र कार्यान्वयन गर्ने उत्तम तरिका के हो?"
> - "भिन्न समीक्षा सत्रहरूबीच सुसंगत गम्भीरता स्तर कसरी सुनिश्चित गर्ने?"

<img src="../../../translated_images/ne/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*गम्भीरता स्तरका साथ सुसंगत कोड समीक्षा फ्रेमवर्क*

**धेरै-टर्न च्याट** - सन्दर्भ आवश्यक संवादहरूको लागि। मोडेलले अघिल्लो सन्देशहरू सम्झिन्छ र तिनमा आधारित भएर जवाफ दिन्छ। अन्तरक्रियात्मक मद्दत सत्र वा जटिल Q&A का लागि प्रयोग गर्नुहोस्।

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

*संवाद सन्दर्भ कसरी धेरै-टर्नमा जम्मा हुन्छ र टोकन सीमा नजिक पुग्छ*

**चरण-द्वारा-चरण तर्क** - दृश्य तर्क आवश्यकताहरूका लागि। मोडेल प्रत्येक चरणको स्पष्ट तर्क देखाउँछ। गणित समस्या, तर्क पहेली, वा सोच्ने प्रक्रिया बुझ्न आवश्यक परेको बेला प्रयोग गर्नुहोस्।

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

*जटिल समस्याहरूलाई स्पष्ट तर्कका चरणहरूमा विभाजन*

**सीमित आउटपुट** - विशिष्ट फर्म्याट आवश्यकताका लागि। मोडेल कडाइका साथ फर्म्याट र लम्बाई नियम पालना गर्छ। सारांश वा ठ्याक्कै आउटपुट संरचना आवश्यक पर्दा प्रयोग गर्नुहोस्।

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

*विशिष्ट फर्म्याट, लम्बाई, र संरचना आवश्यकताहरू लागू गर्दै*

## अस्तित्वमा रहेका Azure स्रोतहरू प्रयोग गर्दै

**परिनियोजन प्रमाणित गर्नुहोस्:**

रूट डिरेक्टरीमा `.env` फाइल Azure प्रमाणपत्रहरू सहित छ भनी सुनिश्चित गर्नुहोस् (मोड्युल ०१ मा सिर्जना गरिएको):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT देखाउनुपर्छ
```

**एप्लिकेसन सुरु गर्नुहोस्:**

> **टिप्पणी:** यदि तपाईंले पहिले नै मोड्युल ०१ बाट `./start-all.sh` प्रयोग गरी सबै एप्लिकेसनहरू सुरु गर्नुभएको छ भने, यो मोड्युल पोर्ट ८०८३ मा पहिले नै चलिरहेको छ। तपाईं तलको सुरु गर्ने आदेशहरू छोडेर सिधै http://localhost:8083 मा जान सक्नुहुन्छ।

**विकल्प १: Spring Boot ड्यासबोर्ड प्रयोग गर्दै (VS Code प्रयोगकर्ताका लागि सिफारिस गरिएको)**
विकास कन्टेनरमा Spring Boot Dashboard विस्तार समावेश छ, जसले सबै Spring Boot अनुप्रयोगहरू व्यवस्थापन गर्न दृश्यात्मक इन्टरफेस प्रदान गर्दछ। तपाईंले यसलाई VS Code को बायाँपट्टि Activity Bar मा फेला पार्न सक्नुहुन्छ (Spring Boot आइकन खोज्नुहोस्)।

Spring Boot Dashboard बाट, तपाईंले गर्न सक्नुहुन्छ:
- कार्यक्षेत्रमा उपलब्ध सबै Spring Boot अनुप्रयोगहरू हेर्नुहोस्
- एक क्लिकमा अनुप्रयोगहरू सुरु/रोक्नुहोस्
- अनुप्रयोग लगहरू रियल-टाइममा हेर्नुहोस्
- अनुप्रयोगको स्थिति अनुगमन गर्नुहोस्

"prompt-engineering" सँगै रहेको प्ले बटन क्लिक गरेर यो मोड्युल सुरु गर्नुहोस्, वा सबै मोड्युलहरू एकैपटक सुरु गर्नुहोस्।

<img src="../../../translated_images/ne/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**विकल्प २: शेल स्क्रिप्टहरू प्रयोग गर्दै**

सबै वेब अनुप्रयोगहरू (मोड्युलहरू ०१-०४) सुरु गर्नुहोस्:

**Bash:**
```bash
cd ..  # मूल निर्देशिकाबाट
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # रूट निर्देशनिका बाट
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

दुवै स्क्रिप्टहरूले स्वचालित रूपमा रुट `.env` फाइलबाट वातावरण चरहरू लोड गर्छन् र यदि JAR हरू अवस्थित छैनन् भने तिनीहरूलाई निर्माण गर्नेछन्।

> **सूचना:** यदि तपाईंले सुरु गर्नु अघि सबै मोड्युलहरू म्यानुअली निर्माण गर्न चाहनुहुन्छ भने:
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

**रोक्न:**

**Bash:**
```bash
./stop.sh  # यो मोड्युल मात्र
# वा
cd .. && ./stop-all.sh  # सबै मोड्युलहरू
```

**PowerShell:**
```powershell
.\stop.ps1  # यो मोड्युल मात्रै
# वा
cd ..; .\stop-all.ps1  # सबै मोड्युलहरू
```

## अनुप्रयोगका स्क्रीनशटहरू

<img src="../../../translated_images/ne/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*मुख्य ड्यासबोर्ड जसले सबै ८ वटा prompt engineering ढाँचाहरूलाई तिनीहरूको विशेषताहरू र प्रयोग केसहरू सहित देखाउँछ*

## ढाँचाहरू अनुसन्धान

वेब इन्टरफेसले तपाईंलाई विभिन्न prompting रणनीतिहरूसँग प्रयोग गर्न दिन्छ। प्रत्येक ढाँचाले फरक समस्याहरू समाधान गर्दछ - कुन उपाय कब चम्किन्छ भनेर हेर्न तिनीहरूलाई प्रयास गर्नुहोस्।

> **सूचना: Streaming vs Non-Streaming** — प्रत्येक पैटर्न पृष्ठमा दुई बटनहरू हुन्छन्: **🔴 Stream Response (Live)** र एक **Non-streaming** विकल्प। Streaming ले Server-Sent Events (SSE) प्रयोग गर्छ जसले मोडेलले उत्पादन गर्ने टोकनहरू रियल-टाइममा देखाउँछ, जसले गर्दा प्रगति तुरुन्तै देखिन्छ। Non-streaming विकल्पले पूर्ण प्रतिक्रिया पर्खन्छ त्यसपछि मात्र देखाउँछ। गहिरो reasoning ट्रिगर गर्ने prompts (जस्तै High Eagerness, Self-Reflecting Code) को लागि, non-streaming कल धेरै लामो समय (कहिलेकाहीँ मिनेटहरू) लाग्न सक्छ — कुनै दृश्य प्रतिक्रिया बिना। **जटिल prompts को प्रयोग गर्दा streaming प्रयोग गर्नुहोस्** ताकि तपाईं मोडेल काम गरिरहेको देख्न सक्नुहुनेछ र अनुरोध तिहारिएको जस्तो प्रभावबाट जोगिन सक्नुहुनेछ।
>
> **सूचना: ब्राउजर आवश्यकताहरू** — Streaming सुविधा Fetch Streams API (`response.body.getReader()`) प्रयोग गर्छ जुन पूर्ण ब्राउजर (Chrome, Edge, Firefox, Safari) मा मात्र काम गर्छ। यो VS Code को बिल्ट-इन Simple Browser मा काम गर्दैन, किनभने यसको वेबभियूले ReadableStream API समर्थन गर्दैन। यदि तपाईं Simple Browser प्रयोग गर्नुहुन्छ भने, non-streaming बटनहरू सामान्य रूपमा काम गर्नेछन् — केवल streaming बटनहरू प्रभावित हुन्छन्। पूर्ण अनुभवको लागि `http://localhost:8083` लाई बाहिरी ब्राउजरमा खोल्नुहोस्।

### Low vs High Eagerness

Low Eagerness प्रयोग गरेर "What is 15% of 200?" जस्तो साधारण प्रश्न सोध्नुहोस्। तपाईंलाई तुरुन्तै, सिधा उत्तर प्राप्त हुनेछ। अब High Eagerness प्रयोग गरेर "Design a caching strategy for a high-traffic API" जस्तो जटिल कुरा सोध्नुहोस्। **🔴 Stream Response (Live)** क्लिक गर्नुहोस् र मॉडलको विस्तृत reasoning टोकन-दर-टोकन देख्नुहोस्। एउटै मोडेल, एउटै प्रश्न संरचना - तर prompt ले कति सोच्ने भनेर बताउँछ।

### Task Execution (Tool Preambles)

बहु-चरण कार्यप्रवाहहरूले अग्रिम योजना र प्रगति विवरणबाट फाइदा उठाउँछन्। मोडेलले के गर्नेछ भनेर बाहिर ल्याउँछ, प्रत्येक चरण वर्णन गर्छ, अनि परिणामहरूको सारांश दिन्छ।

### Self-Reflecting Code

“Create an email validation service” प्रयास गर्नुहोस्। केवल कोड उत्पादन गरेर रोक्ने ठाउँमा, मोडेलले उत्पादन गर्छ, गुणस्तर मापदण्डविरुद्ध मूल्यांकन गर्छ, कमजोरीहरू पहिचान गर्छ, र सुधार गर्छ। तपाईंले यो पटक-पटक दोहोर्याउंदै उत्पादन मापदण्डसम्म पुर्‍याउन देख्नुहुनेछ।

### Structured Analysis

कोड समीक्षा स्थिर मूल्यांकन फ्रेमवर्क आवश्यक हुन्छ। मोडेलले कोडलाई निश्चित वर्गीकरणहरू (सहीपन, अभ्यासहरू, प्रदर्शन, सुरक्षा) को साथ विश्लेषण गर्छ र गम्भीरता स्तर देखाउँछ।

### Multi-Turn Chat

“ What is Spring Boot?” सोध्नुहोस् र तुरुन्तै “Show me an example” सोध्नुहोस्। मोडेलले तपाईंको पहिलो प्रश्न सम्झन्छ र Spring Boot को उदाहरण दिन्छ। सम्झना नभए दोस्रो प्रश्न अस्पष्ट हुन्छ।

### Step-by-Step Reasoning

एउटा गणित समस्या छान्नुहोस् र Step-by-Step Reasoning र Low Eagerness दुवै प्रयोग गरेर प्रयास गर्नुहोस्। Low eagerness ले केवल उत्तर दिन्छ - छिटो तर अस्पष्ट। Step-by-step ले हरेक गणना र निर्णय देखाउँछ।

### Constrained Output

जब तपाईंलाई विशेष ढाँचा वा शब्द गणना आवश्यक हुन्छ, यो ढाँचाले कडाइका साथ पालना गर्नु पर्छ। ठीक १०० शब्दमा बुलेट पोइन्ट ढाँचामा सारांश तयार गर्ने प्रयास गर्नुहोस्।

## तपाईं साँच्चिकै के सिक्दै हुनुहुन्छ

**Reasoning Effort ले सबै परिवर्तन गर्दछ**

GPT-5.2 तपाईंलाई computational effort तपाईंको prompts मार्फत नियन्त्रण गर्न दिन्छ। कम effort भनेको छिटो प्रतिक्रिया र न्यूनतम अन्वेषण हो। उच्च effort भनेको मोडेल गहिरो सोच्न समय लिन्छ। तपाईंले effort लाई कार्यको जटिलताको अनुसार मिलाउन सिक्दै हुनुहुन्छ - साधारण प्रश्नमा समय नबर्बाद गर्नुहोस्, तर जटिल निर्णयहरूमा पनि हतार नगर्नुहोस्।

**संरचना व्यवहारलाई मार्गदर्शन गर्छ**

प्रॉम्प्टहरूमा XML ट्यागहरू देख्नुभयो? तिनीहरू सजावटका लागि होइनन्। मोडेलहरू संरचित निर्देशनहरू अधिक विश्वसनीय रूपमा पालना गर्छन् बनाम स्वतन्त्र पाठ। जब तपाईंलाई बहु-चरण प्रक्रियाहरू वा जटिल तर्क चाहिन्छ, संरचनाले मोडेलले कहाँ छ र के आउँछ भनेर ट्रयाक गर्न मद्दत गर्दछ।

<img src="../../../translated_images/ne/prompt-structure.a77763d63f4e2f89.webp" alt="Prompt Structure" width="800"/>

*स्पष्ट खण्डहरू र XML शैली संगठन सहित राम्रो संरचित प्रॉम्प्टको संरचना*

**गुणस्तर आफैं मूल्यांकन गरेर प्राप्त हुन्छ**

आफूलाई प्रतिबिम्बित गर्ने ढाँचाहरू गुणस्तर मापदण्डलाई स्पष्ट बनाएर काम गर्छन्। केवल आशा गर्नुभन्दा कि मोडेल “सही गर्छ”, तपाईंले के अर्थ "सही" भन्ने बताउनुहुन्छ: सही तर्क, त्रुटि ह्यान्डलिंग, प्रदर्शन, सुरक्षा। त्यसपछि मोडेलले आफ्नै उत्पादन मूल्यांकन र सुधार गर्न सक्छ। यसले कोड सिर्जनालाई लोटरी होइन प्रक्रिया बनाउँछ।

**सन्दर्भ सीमित छ**

बहु-चरण संवादहरू प्रत्येक अनुरोधसँग सन्देश इतिहास समावेश गरेर काम गर्छन्। तर सीमा छ - प्रत्येक मोडेलसँग अधिकतम टोकन संख्या हुन्छ। संवादहरू बढ्दै जाँदा, प्रासंगिक सन्दर्भ कायम राख्न रणनीतिहरू आवश्यक हुन्छन् तर त्यो सिमानामा पुग्न नदिन। यो मोड्युलले तपाईंलाई सम्झन कसरी काम गर्छ देखाउँछ; पछि तपाईं सिक्नुहुनेछ कहिले सारांश गर्ने, कहिले बिर्सने, र कहिले पुनःप्राप्त गर्ने।

## अर्को चरणहरू

**अर्को मोड्युल:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**नेभिगेसन:** [← पहिले: Module 01 - Introduction](../01-introduction/README.md) | [मुख्य पृष्ठमा फर्कनुहोस्](../README.md) | [अर्को: Module 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यो दस्तावेज [Co-op Translator](https://github.com/Azure/co-op-translator) नामक एआई अनुवाद सेवा प्रयोग गरी अनुवाद गरिएको हो। हामी शुद्धताको लागि प्रयास गर्छौं, तर कृपया बुझ्नुहोस् कि स्वचालित अनुवादहरूमा त्रुटि वा अशुद्धता हुन सक्छ। मूल दस्तावेज यसको मूल भाषामा अधिकारिक स्रोत मानिनु पर्छ। महत्वपूर्ण जानकारीको लागि पेशेवर मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न हुने कुनै पनि गलतफहमी वा गलत व्याख्याका लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->