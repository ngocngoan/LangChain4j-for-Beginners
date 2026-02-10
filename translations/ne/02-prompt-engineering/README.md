# Module 02: GPT-5.2 सँग प्रम्प्ट इन्जिनियरिङ

## सामग्रीहरुको तालिका

- [तपाईंले के सिक्नुहुनेछ](../../../02-prompt-engineering)
- [पूर्वआवश्यकताहरू](../../../02-prompt-engineering)
- [प्रम्प्ट इन्जिनियरिङ बुझ्नुहोस्](../../../02-prompt-engineering)
- [यो कसरी LangChain4j प्रयोग गर्छ](../../../02-prompt-engineering)
- [मुख्य ढाँचा हरु](../../../02-prompt-engineering)
- [अघिल्लो Azure स्रोतहरू प्रयोग गर्दै](../../../02-prompt-engineering)
- [एप्लिकेशनका स्क्रिनसटहरू](../../../02-prompt-engineering)
- [ढाँचाहरू अन्वेषण गर्दै](../../../02-prompt-engineering)
  - [कम बनाम उच्च उत्साह](../../../02-prompt-engineering)
  - [कार्य कार्यान्वयन (टुल प्रीएम्बलहरू)](../../../02-prompt-engineering)
  - [आत्म-प्रतिबिम्बित कोड](../../../02-prompt-engineering)
  - [संरचित विश्लेषण](../../../02-prompt-engineering)
  - [बहु-पटकको संवाद](../../../02-prompt-engineering)
  - [चरण-दrचरण तर्क](../../../02-prompt-engineering)
  - [सीमित परिणाम](../../../02-prompt-engineering)
- [तपाईं साँच्चिकै के सिक्दै हुनुहुन्छ](../../../02-prompt-engineering)
- [अगाडि के गर्ने](../../../02-prompt-engineering)

## तपाईंले के सिक्नुहुनेछ

अघिल्लो मोड्युलमा, तपाईंले कसरी मेमोरीले संवादात्मक AI सक्षम बनाउँछ भनी देख्नुभयो र प्राथमिक अन्तरक्रियाहरूका लागि GitHub मोडेलहरू प्रयोग गर्नुभयो। अब हामी कसरी प्रश्न सोध्ने - प्रम्प्टहरू आफैँ - Azure OpenAI को GPT-5.2 प्रयोग गरेर ध्यान केन्द्रित गर्नेछौं। तपाईंले आफ्नो प्रम्प्टहरू कसरी संरचना गर्नुहुन्छ भन्ने कुरा प्रतिक्रिया गुणस्तरमा ठूलो प्रभाव पार्दछ।

हामी GPT-5.2 प्रयोग गर्नेछौं किनभने यसले तर्क नियन्त्रण परिचय गराउँछ - तपाईंले मोडेललाई कत्तिको सोच्नुपर्नेछ भनेर भन्न सक्नुहुन्छ। यसले विभिन्न प्रम्प्टिङ रणनीतिहरूलाई अझ स्पष्ट बनाउँछ र तपाईंलाई कुन समयमा कुन विधि प्रयोग गर्ने बुझ्न मद्दत गर्दछ। हामी Azure को कम दर सीमाहरूबाट पनि GPT-5.2 को फाइदा उठाउनेछौं जुन GitHub मोडेलहरू भन्दा कम हुन्छ।

## पूर्वआवश्यकताहरू

- Module 01 पूरा गरिएको (Azure OpenAI स्रोतहरू डिप्लोय गरिएको)
- मूल डाइरेक्टरीमा `.env` फाइलमा Azure प्रमाणपत्रहरू (Module 01 मा `azd up` द्वारा सिर्जना गरिएको)

> **सूचना:** यदि तपाईंले Module 01 पूरा गर्नुभएको छैन भने, पहिले तिनीहरूको डिप्लोयमेन्ट निर्देशनहरू पालना गर्नुहोस्।

## प्रम्प्ट इन्जिनियरिङ बुझ्नुहोस्

प्रम्प्ट इन्जिनियरिङ भनेको यस्तो इनपुट पाठ डिजाइन गर्नु हो जसले तपाईंलाई चाहिएको परिणाम दीर्घकालीन र निरन्तर सुनिश्चित गर्छ। केवल प्रश्न सोध्नु मात्र होइन - यो अनुरोधहरू यस्तो संरचना दिनु हो जसले मोडेललाई तपाईं के चाहनुहुन्छ र कसरी प्रदान गर्ने बुझ्न मद्दत गर्छ।

यसलाई सहकर्मीलाई निर्देशन दिनेजस्तो सोच्नुहोस्। "बग ठीक गर" अस्पष्ट छ। "UserService.java को लाइन ४५ मा नल पोइन्टर अपवाद ठीक गर्न नल चेक थप" विशिष्ट छ। भाषा मोडेलहरू पनि यस्तै काम गर्छन् - विशिष्टता र संरचनाले महत्व राख्छ।

## यो कसरी LangChain4j प्रयोग गर्छ

यस मोड्युलले पहिलेका मोड्युलहरूको जस्तै LangChain4j को आधार प्रयोग गरी उन्नत प्रम्प्टिङ ढाँचाहरू देखाउँछ, विशेष गरी प्रम्प्ट संरचना र तर्क नियन्त्रणमा केन्द्रित।

<img src="../../../translated_images/ne/langchain4j-flow.48e534666213010b.webp" alt="LangChain4j Flow" width="800"/>

*LangChain4j कसरी तपाईंका प्रम्प्टहरूलाई Azure OpenAI GPT-5.2 सँग जोड्छ*

**आश्रितहरू** - Module 02 ले `pom.xml` मा परिभाषित तलका langchain4j आश्रितहरू प्रयोग गर्छ:
```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-official</artifactId> <!-- Inherited from BOM in root pom.xml -->
</dependency>
```

**OpenAiOfficialChatModel कन्फिगरेसन** - [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java)

च्याट मोडेललाई OpenAI Official क्लाइन्टको प्रयोग गरी स्प्रिंग बीनको रूपमा म्यानुअल रूपमा कन्फिगर गरिन्छ, जुन Azure OpenAI अन्तबिन्दुहरूलाई समर्थन गर्दछ। Module 01 भन्दा मुख्य भिन्नता हाम्रो प्रम्प्टहरू `chatModel.chat()` लाई कसरी पठाउनेमा छ, मोडेल सेटअपमा होइन।

**सिस्टम र प्रयोगकर्ता सन्देशहरू** - [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java)

LangChain4j ले संदेश प्रकारहरूलाई स्पष्टता बढाउन अलग गर्छ। `SystemMessage` AI को व्यवहार र सन्दर्भ सेट गर्छ ("तपाईं एक कोड समीक्षक हुनुहुन्छ" जस्तै), जबकि `UserMessage` मा वास्तविक अनुरोध हुन्छ। यसले विभिन्न प्रयोगकर्ता प्रश्नहरूको बीचमा निरन्तर AI व्यवहार कायम राख्न मद्दत गर्छ।

```java
SystemMessage systemMsg = SystemMessage.from(
    "You are a helpful Java programming expert."
);

UserMessage userMsg = UserMessage.from(
    "Explain what a List is in Java"
);

String response = chatModel.chat(systemMsg, userMsg);
```

<img src="../../../translated_images/ne/message-types.93e0779798a17c9d.webp" alt="Message Types Architecture" width="800"/>

*SystemMessage ले सधैंको सन्दर्भ दिन्छ भने UserMessages मा व्यक्तिगत अनुरोधहरू हुन्छन्*

**MessageWindowChatMemory बहु-पटक संवादका लागि** - बहु-पटकको संवाद ढाँचाको लागि, हामी Module 01 का `MessageWindowChatMemory` पुन: प्रयोग गर्छौं। हरेक सत्रलाई आफ्नो अलग मेमोरी इन्सट्यान्स हुन्छ जुन `Map<String, ChatMemory>` मा भण्डारण हुन्छ, जसले धेरै सक्रिय संवादहरूलाई सन्दर्भ नमिलाई सक्षम बनाउँछ।

**प्रम्प्ट टेम्प्लेटहरू** - यहाँको मुख्य केन्द्रबिन्दु प्रम्प्ट इन्जिनियरिङ हो, नयाँ LangChain4j API होइन। प्रत्येक ढाँचाले उस्तै `chatModel.chat(prompt)` विधि प्रयोग गर्छ तर सावधानीपूर्वक संरचित प्रम्प्ट स्ट्रिङहरूका साथ। XML ट्यागहरू, निर्देशनहरू, र फर्म्याटिङ सबै प्रम्प्ट पाठको हिस्सा हुन्, LangChain4j सुविधाहरू होइन।

**तर्क नियन्त्रण** - GPT-5.2 को तर्क प्रयास नियन्त्रण प्रम्प्ट निर्देशनहरूको माध्यमबाट जस्तै "अधिकतम २ वटा तर्क चरणहरू" वा "पिड्लो जस्तो अन्वेषण गर्न" मार्फत हुन्छ। यी प्रम्प्ट इन्जिनियरिङ प्रविधिहरू हुन्, LangChain4j कन्फिगरेसनहरू होइन। लायब्रेरी केवल तपाईंका प्रम्प्टलाई मोडेलमा पुर्‍याउँछ।

मुख्य कुरा: LangChain4j ले पूर्वाधार दिन्छ (मोडेल कनेक्शन [LangChainConfig.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/config/LangChainConfig.java) द्वारा, मेमोरी, सन्देश व्यवस्थापन [Gpt5PromptService.java](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) द्वारा), यस मोड्युलले तपाईंलाई त्यो पूर्वाधार भित्र प्रभावकारी प्रम्प्ट कसरी बनाउने सिकाउनेछ।

## मुख्य ढाँचाहरू

सबै समस्याहरूलाई एउटै तरिकाले समाधान गर्न सकिँदैन। केही प्रश्नहरूलाई छिटो उत्तर चाहिन्छ, केहीलाई गहिरो सोच आवश्यक पर्छ। कतिपयलाई देखिने तर्क चाहिन्छ, केहीलाई मात्र परिणाम चाहिन्छ। यस मोड्युलले आठ प्रम्प्टिङ ढाँचाहरू समेट्छ - प्रत्येक भिन्न परिदृश्यका लागि अनुकूलित। तपाईं ती सबैसँग अभ्यास गर्नुपर्नेछ जसले कुन अवस्था कस्तो प्रभावकारी हुन्छ बुझ्न सहायता गर्छ।

<img src="../../../translated_images/ne/eight-patterns.fa1ebfdf16f71e9a.webp" alt="Eight Prompting Patterns" width="800"/>

*आठ प्रम्प्ट इन्जिनियरिङ ढाँचाहरू र तिनीहरूको उपयोग केसहरूको अवलोकन*

<img src="../../../translated_images/ne/reasoning-effort.db4a3ba5b8e392c1.webp" alt="Reasoning Effort Comparison" width="800"/>

*कम उत्साह (छिटो, प्रत्यक्ष) र उच्च उत्साह (गहिरो, अन्वेषणात्मक) तर्क विधिहरू*

**कम उत्साह (छिटो र केन्द्रित)** - सजिला प्रश्नहरूको लागि जहाँ तपाईं छिटो, प्रत्यक्ष उत्तर चाहनुहुन्छ। मोडेलले न्यूनतम तर्क गर्छ - अधिकतम 2 चरणहरू। गणना, खोज, वा सरल प्रश्नहरूको लागि प्रयोग गर्नुहोस्।

```java
String prompt = """
    <reasoning_effort>low</reasoning_effort>
    <instruction>maximum 2 reasoning steps</instruction>
    
    What is 15% of 200?
    """;

String response = chatModel.chat(prompt);
```

> 💡 **GitHub Copilot सँग अन्वेषण गर्नुहोस्:** [`Gpt5PromptService.java`](../../../02-prompt-engineering/src/main/java/com/example/langchain4j/prompts/service/Gpt5PromptService.java) खोल्नुहोस् र सोध्नुहोस्:
> - "कम उत्साह र उच्च उत्साह प्रम्प्टिङ ढाँचाहरू बीच के फरक छ?"
> - "प्रम्प्टमा XML ट्यागहरूले AI को प्रतिक्रिया कसरी संरचना गर्न मद्दत गर्छ?"
> - "म कहिले आत्म-प्रतिबिम्ब ढाँचाहरू र प्रत्यक्ष निर्देशन प्रयोग गर्ने?"

**उच्च उत्साह (गहिरो र पूर्ण)** - जटिल समस्याहरूका लागि जहाँ तपाईं विस्तृत विश्लेषण चाहनुहुन्छ। मोडेलले गहिरो अन्वेषण गर्छ र विस्तृत तर्क देखाउँछ। प्रणाली डिजाइन, आर्किटेक्चर निर्णय, वा जटिल अनुसन्धानका लागि प्रयोग गर्नुहोस्।

```java
String prompt = """
    <reasoning_effort>high</reasoning_effort>
    <instruction>explore thoroughly, show detailed reasoning</instruction>
    
    Design a caching strategy for a high-traffic REST API.
    """;

String response = chatModel.chat(prompt);
```

**कार्य कार्यान्वयन (चरण-दrचरण प्रगति)** - बहु चरण कार्यको लागि। मोडेलले पहिले योजना दिन्छ, काम गर्दै प्रत्येक चरण वर्णन गर्छ, र त्यसपछि सारांश दिन्छ। माइग्रेशन, कार्यान्वयन, वा कुनै बहु-चरण प्रक्रियाका लागि प्रयोग गर्नुहोस्।

```java
String prompt = """
    <task>Create a REST endpoint for user registration</task>
    <preamble>Provide an upfront plan</preamble>
    <narration>Narrate each step as you work</narration>
    <summary>Summarize what was accomplished</summary>
    """;

String response = chatModel.chat(prompt);
```

चेन-अफ-थट (Chain-of-Thought) प्रम्प्टले मोडेललाई आफ्नो तर्क प्रक्रिया देखाउन प्रोत्साहित गर्छ, जसले जटिल कार्यहरूको शुद्धतालाई सुधार गर्छ। चरण-दर-चरण विस्तृत व्याख्यान मानव र AI दुवैलाई तर्क बुझ्न मद्दत गर्छ।

> **🤖 [GitHub Copilot](https://github.com/features/copilot) च्याटसँग प्रयास गर्नुहोस्:** यस ढाँचाबारे सोध्नुहोस्:
> - "मैले लामो समयसम्म चल्ने अपरेसनहरूको लागि कार्य कार्यान्वयन ढाँचा कसरी अनुकूलन गर्ने?"
> - "उत्पादन अनुप्रयोगहरूमा टुल प्रीएम्बलहरूको संरचना गर्न के उत्तम अभ्यासहरू छन्?"
> - "UI मा मध्यवर्ती प्रगति अपडेट कसरी समेट्ने र देखाउने?"

<img src="../../../translated_images/ne/task-execution-pattern.9da3967750ab5c1e.webp" alt="Task Execution Pattern" width="800"/>

*बहु-चरण कामका लागि योजना → कार्यान्वयन → सारांश वर्कफ्लो*

**आत्म-प्रतिबिम्बित कोड** - उत्पादन-स्तर कोड जनरेट गर्न। मोडेलले कोड सिर्जना गर्छ, गुणस्तर मापदण्डसँग तुलना गर्छ, र आवर्ती सुधार गर्छ। नयाँ फिचरहरू वा सेवाहरू बनाउँदा प्रयोग गर्नुहोस्।

```java
String prompt = """
    <task>Create an email validation service</task>
    <quality_criteria>
    - Correct logic and error handling
    - Best practices (clean code, proper naming)
    - Performance optimization
    - Security considerations
    </quality_criteria>
    <instruction>Generate code, evaluate against criteria, improve iteratively</instruction>
    """;

String response = chatModel.chat(prompt);
```

<img src="../../../translated_images/ne/self-reflection-cycle.6f71101ca0bd28cc.webp" alt="Self-Reflection Cycle" width="800"/>

*पुनरावृत्त सुधार लूप - सिर्जना, मूल्यांकन, समस्या पहिचान, सुधार, दोहोर्याइ*

**संरचित विश्लेषण** - निरन्तर मूल्याङ्कनका लागि। मोडेलले कोडलाई स्थिर फ्रेमवर्क प्रयोग गरी समीक्षा गर्छ (शुद्धता, अभ्यासहरू, प्रदर्शन, सुरक्षा)। कोड समीक्षा वा गुणस्तर मूल्याङ्कनका लागि प्रयोग गर्नुहोस्।

```java
String prompt = """
    <code>
    public List getUsers() {
        return database.query("SELECT * FROM users");
    }
    </code>
    
    <framework>
    Evaluate using these categories:
    1. Correctness - Logic and functionality
    2. Best Practices - Code quality
    3. Performance - Efficiency concerns
    4. Security - Vulnerabilities
    </framework>
    """;

String response = chatModel.chat(prompt);
```

> **🤖 [GitHub Copilot](https://github.com/features/copilot) च्याटसँग प्रयास गर्नुहोस्:** संरचित विश्लेषणको बारेमा सोध्नुहोस्:
> - "बिभिन्न प्रकारका कोड समीक्षा का लागि विश्लेषण फ्रेमवर्क कसरी अनुकूलित गर्ने?"
> - "संरचित आउटपुट प्रोग्रामिङ ढंगले कसरी पार्स गर्ने र कार्यान्वयन गर्ने?"
> - "विभिन्न समीक्षा सत्रहरूमा समान सुरक्षा तह कसरी सुनिश्चित गर्ने?"

<img src="../../../translated_images/ne/structured-analysis-pattern.0af3b690b60cf2d6.webp" alt="Structured Analysis Pattern" width="800"/>

*गम्भीरता स्तरसहित निरन्तर कोड समीक्षा का लागि चार-श्रेणी फ्रेमवर्क*

**बहु-पटकको संवाद** - सन्दर्भ आवश्यक संवादहरूको लागि। मोडेलले अघिल्लो सन्देशहरू सम्झन्छ र त्यसमा आधारित उत्तर दिन्छ। अन्तरक्रियात्मक सहायता सत्र वा जटिल प्रश्नोत्तरहरूको लागि प्रयोग गर्नुहोस्।

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

*कसरी संवाद सन्दर्भ धेरै चरणहरूमा सङ्कलित हुन्छ र टोकन सिमा पुग्दासम्म राखिन्छ*

**चरण-दrचरण तर्क** - देखिने तर्क आवश्यक समस्याहरूको लागि। मोडेलले प्रत्येक चरणको स्पष्ट तर्क देखाउँछ। गणित समस्या, तर्क पजलहरू, वा सोचाइ प्रक्रिया बुझ्न आवश्यक पर्दा प्रयोग गर्नुहोस्।

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

*समस्याहरूलाई स्पष्ट तार्किक चरणहरूमा विघटन गरिरहेको*

**सीमित परिणाम** - निश्चित फर्म्याट आवश्यकताहरू भएको प्रतिक्रियाहरूको लागि। मोडेलले कडाइका साथ ढाँचा र लम्बाइ नियमहरू पालना गर्दछ। सारांश वा ठ्याक्कै आउटपुट संरचना आवश्यक पर्दा प्रयोग गर्नुहोस्।

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

*विशिष्ट ढाँचा, लम्बाइ र संरचना आवश्यकताहरू लागू गर्दै*

## अघिल्लो Azure स्रोतहरू प्रयोग गर्दै

**परिनियोजन सुनिश्चित गर्नुहोस्:**

मूल डाइरेक्टरीमा `.env` फाइल Azure प्रमाणपत्रहरूसहित छ कि छैन जाँच्नुहोस् (Module 01 मा बनाइएको):
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT देखाउनु पर्छ
```

**एप्लिकेशन सुरु गर्नुहोस्:**

> **सूचना:** यदि तपाईंले पहिले नै Module 01 बाट `./start-all.sh` प्रयोग गरी सबै एप्लिकेशन सुरु गर्नु भएको छ भने, यो मोड्युल पोर्ट 8083 मा चलिरहेको छ। तपाईं तलको सुरु आदेशहरू छोडेर सिधै http://localhost:8083 मा जान सक्नुहुन्छ।

**विकल्प १: Spring Boot Dashboard प्रयोग गर्दै (VS Code प्रयोगकर्ताहरूका लागि सिफारिस गरिएको)**

डेभ कन्टेनरमा Spring Boot Dashboard एक्सटेन्सन समावेश छ, जसले सबै Spring Boot एप्लिकेशनहरू नियन्त्रण गर्न भिजुअल इन्टरफेस दिन्छ। तपाईंले यसलाई VS Code को Activity Bar मा बायाँपट्टि (Spring Boot आइकन खोज्नुहोस्) भेट्टाउनुहुनेछ।

Spring Boot Dashboard बाट तपाईं:
- कार्यक्षेत्र भित्र उपलब्ध सबै Spring Boot एप्लिकेशनहरू देख्न सक्नुहुन्छ
- एक क्लिकमा एप्लिकेशनहरू सुरु/रोक्न सक्नुहुन्छ
- एप्लिकेशन लगहरू वास्तविक समयमा हेर्न सक्नुहुन्छ
- एप्लिकेशन स्थिति अनुगमन गर्न सक्नुहुन्छ

"prompt-engineering" छेउमा प्ले बटन क्लिक गरेर यो मोड्युल सुरु गर्नुहोस्, वा सबै मोड्युलहरू एकै पटक सुरु गर्नुहोस्।

<img src="../../../translated_images/ne/dashboard.da2c2130c904aaf0.webp" alt="Spring Boot Dashboard" width="400"/>

**विकल्प २: शेल स्क्रिप्टहरू प्रयोग गर्दै**

सबै वेब एप्लिकेशनहरू सुरु गर्नुहोस् (मोड्युल 01-04):

**Bash:**
```bash
cd ..  # मुख्य निर्देशिका बाट
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # रुट डाइरेक्टरीबाट
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

दुवै स्क्रिप्टहरूले मूल `.env` फाइलबाट वातावरण चरहरू स्वचालित रूपमा लोड गर्छन् र JAR छैन भने बनाउनेछन्।

> **सूचना:** यदि तपाईंले सुरु गर्नु अघि सबै मोड्युलहरू म्यानुअली बनाउने चाहनुहुन्छ भने:
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

आफ्नो ब्राउजरमा http://localhost:8083 खोल्नुहोस्।

**रोक्न:**

**Bash:**
```bash
./stop.sh  # यो मड्युल मात्र
# वा
cd .. && ./stop-all.sh  # सबै मड्युलहरू
```

**PowerShell:**
```powershell
.\stop.ps1  # यो मोड्युल मात्र
# वा
cd ..; .\stop-all.ps1  # सबै मोड्युलहरू
```

## एप्लिकेशनका स्क्रिनसटहरू

<img src="../../../translated_images/ne/dashboard-home.5444dbda4bc1f79d.webp" alt="Dashboard Home" width="800" style="border: 1px solid #ddd; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>

*मुख्य ड्यासबोर्ड जुन सबै ८ प्रम्प्ट इन्जिनियरिङ ढाँचाहरू उनीहरूको गुणहरू र उपयोग केसहरूसहित देखाउँछ*

## ढाँचाहरू अन्वेषण गर्दै

वेब इन्टरफेसले तपाईंलाई विभिन्न प्रम्प्टिङ रणनीतिहरू प्रयोग गरेर परीक्षण गर्न दिन्छ। प्रत्येक ढाँचाले फरक समस्याहरू समाधान गर्दछ - ती प्रयोग गरी हेर्नुहोस् कुन विधिले कहिले राम्रो काम गर्छ।

### कम बनाम उच्च उत्साह

"200 को १५% कति हो?" जस्तो सरल प्रश्न कम उत्साह प्रयोग गर्दै सोध्नुहोस्। तपाईं छिटो र प्रत्यक्ष उत्तर प्राप्त गर्नुहुनेछ। अब "एक उच्च ट्राफिक API का लागि क्यासिङ रणनीति डिजाइन गर्नुहोस्" जस्तो जटिल प्रश्न उच्च उत्साह प्रयोग गर्दै सोध्नुहोस्। मोडेल कसरी स्लो हुन्छ र विस्तृत तर्क दिन्छ हेर्नुहोस्। एउटै मोडेल, एउटै प्रश्न संरचना - तर प्रम्प्टले त्यसलाई कति सोच्नुपर्छ भन्ने बताउँछ।
<img src="../../../translated_images/ne/low-eagerness-demo.898894591fb23aa0.webp" alt="कम उत्साह डेमो" width="800"/>

*थोरै तर्कसहित छिटो गणना*

<img src="../../../translated_images/ne/high-eagerness-demo.4ac93e7786c5a376.webp" alt="उच्च उत्साह डेमो" width="800"/>

*व्यापक क्यासिङ रणनीति (2.8MB)*

### कार्य कार्यान्वयन (टूल प्र्याम्बलहरू)

बहु-चरण कार्यप्रवाहहरूलाई अग्रिम योजना र प्रगति वर्णनले लाभ पुर्‍याउँछ। मोडेलले के गर्नेछ भनेर रूपरेखा दिन्छ, प्रत्येक चरण वर्णन गर्दछ, र अन्त्यमा नतिजाहरू सारांशित गर्छ।

<img src="../../../translated_images/ne/tool-preambles-demo.3ca4881e417f2e28.webp" alt="कार्य कार्यान्वयन डेमो" width="800"/>

*चरण-द्वारा-चरण वर्णनसहित REST अन्त बिन्दु सिर्जना गर्दै (3.9MB)*

### आत्म-प्रतिबिम्बित कोड

"इमेल प्रमाणीकरण सेवा सिर्जना गर्नुहोस्" प्रयास गर्नुहोस्। केवल कोड निर्माण गरेर रोक्ने सट्टा, मोडेलले कोड निर्माण गर्छ, गुणस्तर मापदण्डहरूसँग मूल्यांकन गर्छ, कमजोरीहरू पहिचान गर्छ, र सुधार्छ। तपाईं यसलाई पुनरावृत्ति गर्दै अघिल्लो उत्पादन मापदण्डसम्म पुग्ने देख्नुहुनेछ।

<img src="../../../translated_images/ne/self-reflecting-code-demo.851ee05c988e743f.webp" alt="आत्म-प्रतिबिम्बित कोड डेमो" width="800"/>

*पूर्ण इमेल प्रमाणीकरण सेवा (5.2MB)*

### संरचित विश्लेषण

कोड समीक्षा गर्न लगातार मूल्यांकन फ्रेमवर्क आवश्यक पर्छ। मोडेलले निश्चित वर्गहरूसँग (ठीकठाक, अभ्यासहरू, प्रदर्शन, सुरक्षा) ती कोडहरू विश्लेषण गर्छ र गम्भीरता स्तरहरू प्रदान गर्छ।

<img src="../../../translated_images/ne/structured-analysis-demo.9ef892194cd23bc8.webp" alt="संरचित विश्लेषण डेमो" width="800"/>

*फ्रेमवर्क-आधारित कोड समीक्षा*

### बहु-पाप्चा कुराकानी

"स्प्रिंग बुट भनेको के हो?" सोध्नुहोस्, त्यसपछि तुरुन्तै "मलाई एउटा उदाहरण देखाउ" भनी सोध्नुहोस्। मोडेलले पहिलो प्रश्न सम्झन्छ र तपाईंलाई विशेष रूपमा स्प्रिंग बुटको उदाहरण दिन्छ। सम्झना नभए दोस्रो प्रश्न अत्यन्त अस्पष्ट हुने थियो।

<img src="../../../translated_images/ne/multi-turn-chat-demo.0d2d9b9a86a12b4b.webp" alt="बहु-पाप्चा कुराकानी डेमो" width="800"/>

*प्रश्नहरूमा सन्दर्भ संरक्षण*

### चरण-द्वारा-चरण तर्क

एउटा गणित समस्या रोज्नुहोस् र त्यसलाई चरण-द्वारा-चरण तर्क र कम उत्साह दुबैसँग प्रयास गर्नुहोस्। कम उत्साहले मात्रै उत्तर दिन्छ - छिटो तर अस्पष्ट। चरण-द्वारा-चरणले तपाईंलाई प्रत्येक गणना र निर्णय देखाउँछ।

<img src="../../../translated_images/ne/step-by-step-reasoning-demo.12139513356faecd.webp" alt="चरण-द्वारा-चरण तर्क डेमो" width="800"/>

*स्पष्ट चरणहरूसहित गणित समस्या*

### सीमित आउटपुट

विशिष्ट ढाँचा वा शब्द संख्या आवश्यक पर्दा, यो ढाँचा कडाइका साथ पालना सुनिश्चित गर्छ। निश्चित रूपमा १०० शब्दको बुलेट पोइन्टमा सारांस निकाल्न प्रयास गर्नुहोस्।

<img src="../../../translated_images/ne/constrained-output-demo.567cc45b75da1633.webp" alt="सीमित आउटपुट डेमो" width="800"/>

*ढाँचा नियन्त्रण सहित मेसिन लर्निङ सारांश*

## तपाईं वास्तवमा के सिक्दै हुनुहुन्छ

**तर्क प्रयासले सबै कुरा बदल्छ**

GPT-5.2 ले तपाईंलाई सोधपुछ मार्फत गणनात्मक प्रयास नियन्त्रण गर्न दिन्छ। कम प्रयासले छिटो र थोरै अन्वेषणसहित उत्तर दिन्छ। उच्च प्रयासले मोडेललाई गहिरो सोच्न समय दिन्छ। तपाईंले कार्यको जटिलतासँग मेल खाने प्रयास सिक्दै हुनुहुन्छ - साधारण प्रश्नमा समय नबर्बाद गर्नुहोस्, तर जटिल निर्णयमा पनि हतार नगर्नुहोस्।

**संरचनाले व्यवहार मार्गदर्शन गर्छ**

प्राम्प्टहरूको XML ट्यागहरू देख्नुभयो? तिनीहरू रंगीन होइनन्। मोडेलहरूले संरचित निर्देशनहरू स्वतन्त्र पाठभन्दा बढी विश्वसनीय रूपमा पछ्याउँछन्। जब तपाईंलाई बहुचरण प्रक्रिया वा जटिल तर्क चाहिन्छ, संरचनाले मोडेललाई रहेको ठाउँ र अगाडी के आउँछ ट्र्याक गर्न मद्दत गर्छ।

<img src="../../../translated_images/ne/prompt-structure.a77763d63f4e2f89.webp" alt="प्राम्प्ट संरचना" width="800"/>

*स्पष्ट भागहरू र XML-शैली संगठनसहित राम्रो संरचित प्राम्प्टको बनावट*

**गुणस्तर आत्म-मूल्यांकनबाट**

आत्म-प्रतिबिम्बित ढाँचाहरूले गुणस्तर मापदण्डहरू स्पष्ट बनाउँछन्। मोडेलले "ठिक गरे" भन्ने आशामा नबसि तपाईंले सही के हो त्यो स्पष्ट रूपमा बताउनुहुन्छ: सही तर्क, त्रुटि व्यवस्थापन, प्रदर्शन, सुरक्षा। त्यसपछि मोडेल आफ्ना नतिजाहरू मूल्यांकन गरी सुधार गर्छ। यसले कोड निर्माणलाई लटरीबाट प्रक्रिया बनाउँछ।

**सन्दर्भ सीमित छ**

बहु-पाप्चा कुराकानी प्रत्येक अनुरोधसँग सन्देश इतिहास समावेश गरेर काम गर्छ। तर एउटा सीमा छ - प्रत्येक मोडेलसँग अधिकतम टोकन गणना हुन्छ। कुराकानी बढ्दै जाँदा, तपाईंले उचित सन्दर्भ कायम राख्न रणनीतिहरू आवश्यक पर्छ जुन त्यो सीमा नछुयोस्। यस मोड्युलले तपाईंलाई सम्झनाले कसरी काम गर्छ देखाउँछ; पछि तपाईंले कहिले सारांश गर्ने, कहिले भूल्ने, र कहिले पुनः प्राप्त गर्ने जान्नु हुनेछ।

## अर्को कदम

**अर्को मोड्युल:** [03-rag - RAG (Retrieval-Augmented Generation)](../03-rag/README.md)

---

**नेभिगेसन:** [← पहिले: मोड्युल 01 - परिचय](../01-introduction/README.md) | [मुख्यमा फर्किनुहोस्](../README.md) | [अर्को: मोड्युल 03 - RAG →](../03-rag/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
यस दस्तावेजलाई AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) को माध्यमबाट अनुवाद गरिएको हो। हामी सही अनुवाद गर्न प्रयास गर्छौं, तर कृपया बुझ्नुहोस् कि स्वत: अनुवादमा त्रुटि वा असंगतिहरू हुन सक्दछन्। मूल दस्तावेजलाई यसको मातृ भाषामा आधिकारिक स्रोत मान्नुपर्छ। महत्वपूर्ण जानकारीको लागि, व्यावसायिक मानव अनुवाद सिफारिश गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न हुने कुनै पनि गलतफहमी वा गलत व्याख्याको लागि हामी जिम्मेवार हुँदैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->