# Module 01: LangChain4j सँग सुरूवात गर्ने

## सामग्री सूचि

- [भिडियो वाकथ्रू](../../../01-introduction)
- [तपाईंले के सिक्नुहुनेछ](../../../01-introduction)
- [पूर्वआवश्यकताहरू](../../../01-introduction)
- [मुख्य समस्यालाई बुझ्नुहोस्](../../../01-introduction)
- [टोकनहरू बुझ्नुहोस्](../../../01-introduction)
- [मेमोरी कसरी काम गर्छ](../../../01-introduction)
- [यसले LangChain4j कसरी प्रयोग गर्छ](../../../01-introduction)
- [Azure OpenAI पूर्वाधार परिनियोजन गर्नुहोस्](../../../01-introduction)
- [एप्लिकेशन स्थानीय रूपमा चलाउनुहोस्](../../../01-introduction)
- [एप्लिकेशन प्रयोग गर्दै](../../../01-introduction)
  - [Stateless Chat (बायाँ प्यानल)](../../../01-introduction)
  - [Stateful Chat (दायाँ प्यानल)](../../../01-introduction)
- [अर्को कदमहरू](../../../01-introduction)

## भिडियो वाकथ्रू

यो प्रत्यक्ष सत्र हेरौं जसले यस मोड्युलसँग कसरी सुरु गर्ने व्याख्या गर्दछ:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## तपाईंले के सिक्नुहुनेछ

छिटो शुरुवातमा, तपाईंले GitHub मोडेलहरूको प्रयोग गरेर प्रॉम्प्ट पठाउन, उपकरणहरू कल गर्न, RAG पाइपलाइन बनाउने र गार्डरिल परीक्षण गर्नुभयो। ती डेमोले सम्भावनाहरू देखाए — अब हामी Azure OpenAI र GPT-5.2 मा सर्न्छौं र उत्पादन-शैली एप्लिकेशनहरू बनाउन थाल्छौं। यो मोड्युलले तदारुकता र राज्य कायम राख्ने संवादात्मक AI मा केन्द्रित छ — ती अवधारणाहरू जुन ती छिटो शुरुवात डेमोहरूले पर्दा पछाडि प्रयोग गरे तर व्याख्या गरेनन्।

हामी यस गाइडभरि Azure OpenAI को GPT-5.2 प्रयोग गर्नेछौं किनभने यसको उन्नत तर्क क्षमताले विभिन्न ढाँचाहरूको व्यवहार स्पष्ट बनाउँछ। जब तपाईं मेमोरी थप्नुहुन्छ, तपाईंले फरक स्पष्ट देख्नुहुनेछ। यसले तपाईंको एप्लिकेशनमा प्रत्येक कम्पोनेन्ट के ल्याउँछ बुझ्न सजिलो बनाउँछ।

तपाईं एक एप्लिकेशन बनाउनुहुनेछ जसले दुवै ढाँचाहरू देखाउँछ:

**Stateless Chat** - हरेक अनुरोध स्वतन्त्र हुन्छ। मोडेलले अघिल्लो सन्देशहरूको कुनै मेमोरी राख्दैन। यो बजार्ने क्रममा तपाईंले प्रयोग गरेको ढाँचा हो।

**Stateful Conversation** - हरेक अनुरोधले संवाद इतिहास समावेश गर्दछ। मोडेलले धेरै पल्टको सन्दर्भ कायम राख्छ। उत्पादन एप्लिकेशनहरूलाई यही आवश्यक पर्छ।

## पूर्वआवश्यकताहरू

- Azure सदस्यता जसमा Azure OpenAI पहुँच छ
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **सूचना:** Java, Maven, Azure CLI र Azure Developer CLI (azd) दिइएको devcontainer मा पहिले नै इन्स्टल गरिएका छन्।

> **सूचना:** यस मोड्युलले Azure OpenAI मा GPT-5.2 प्रयोग गर्छ। परिनियोजन `azd up` प्रयोग गरेर स्वतः कन्फिगर हुन्छ - कोडमा मोडेल नाम परिवर्तन नगर्नुहोस्।

## मुख्य समस्यालाई बुझ्नुहोस्

भाषा मोडेलहरू stateless हुन्छन्। हरेक API कल स्वतन्त्र हुन्छ। तपाईंले "मेरो नाम जोन हो" पठाउनु भयो र त्यसपछि "मेरो नाम के हो?" सोध्नु भयो भने, मोडेललाई तपाईंले आफूलाई चिनाउनु भएको थाहा हुँदैन। यसले प्रत्येक अनुरोधलाई तपाईंले कहिल्यै गरेको पहिलो संवाद जस्तै व्यवहार गर्छ।

यो सरल Q&A का लागि ठीक छ तर वास्तविक एप्लिकेशनका लागि बेकार हो। ग्राहक सेवा बोटहरूले तपाईंले भनेका कुराहरू सम्झिनु पर्छ। व्यक्तिगत सहायकहरूले सन्दर्भ चाहिन्छ। कुनै पनि बहु-पल्ट संवादलाई मेमोरी आवश्यक हुन्छ।

तलको चित्रले दुई उपायहरू तुलना गर्दछ — बायाँतर्फ stateless कल जसले तपाईंको नाम बिर्सिन्छ; दायाँतर्फ ChatMemory द्वारा समर्थित stateful कल जसले नाम सम्झन्छ।

<img src="../../../translated_images/ne/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*stateless (स्वतन्त्र कलहरू) र stateful (सन्दर्भ-बोध गर्ने) संवादहरूको भेद*

## टोकनहरू बुझ्नुहोस्

संवादहरूमा जाने अघि, टोकनहरू बुझ्न महत्त्वपूर्ण छ - भाषा मोडेलहरूले प्रक्रिया गर्ने पाठका आधारभूत एकाइहरू:

<img src="../../../translated_images/ne/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*टेक्स्ट कसरी टोकनहरूमा विभाजित हुन्छ - "I love AI!" ले ४ अलग प्रोसेसिङ युनिटमा परिणत हुन्छ*

टोकनहरूले AI मोडेलहरूले पाठ मापन र प्रक्रिया गर्दछन्। शब्दहरू, विरामचिन्हहरू, र खाली ठाउँहरू पनि टोकन हुन सक्छन्। तपाईंको मोडेलसँग एक समयमा कति टोकन प्रक्रिया गर्न सक्छ भन्ने सीमा हुन्छ (GPT-5.2 का लागि ४००,०००, जसमा २७२,००० इनपुट र १२८,००० आउटपुट टोकन सम्म)। टोकन बुझ्नाले संवादको लम्बाइ र लागत व्यवस्थापन गर्न सजिलो हुन्छ।

## मेमोरी कसरी काम गर्छ

च्याट मेमोरीले stateless समस्यालाई संवाद इतिहास कायम गरेर समाधान गर्छ। मोडेलमा अनुरोध पठाउनु अघि, फ्रेमवर्कले सान्दर्भिक अघिल्लो सन्देशहरू अगाडि राख्छ। जब तपाईं "मेरो नाम के हो?" सोध्नुहुन्छ, सिस्टमले सम्पूर्ण संवाद इतिहास पठाउँछ, जसले मोडेललाई थाहा हुन्छ तपाईंले पहिले "मेरो नाम जोन हो" भन्नुभएको छ।

LangChain4j ले मेमोरी कार्यान्वयनहरू प्रदान गर्छ जुन यो स्वतः व्यवस्थापन गर्दछ। तपाईं चाहिएको सन्देश संख्या चयन गर्नुहुन्छ र फ्रेमवर्कले सन्दर्भ विन्डो व्यवस्थापन गर्छ। तलको चित्रले MessageWindowChatMemory कसरी हालसालैका सन्देशहरूको स्लाइडिङ विन्डो कायम राख्छ देखाउँछ।

<img src="../../../translated_images/ne/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory ले हालसालैका सन्देशहरूको स्लाइडिङ विन्डो कायम राख्छ, पुराना स्वतः हटाउँदै*

## यसले LangChain4j कसरी प्रयोग गर्छ

यो मोड्युलले छिटो सुरु सँग Spring Boot एकीकृत गरेर र संवाद मेमोरी थपेर विस्तार गर्दछ। भागहरू कसरी जोडिन्छन्:

**निर्भरता** - दुई LangChain4j पुस्तकालयहरू थप्नुहोस्:

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

**च्याट मोडेल** - Azure OpenAI लाई Spring bean को रूपमा कन्फिगर गर्नुहोस् ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

```java
@Bean
public OpenAiOfficialChatModel openAiOfficialChatModel() {
    return OpenAiOfficialChatModel.builder()
            .baseUrl(azureEndpoint)
            .apiKey(azureApiKey)
            .modelName(deploymentName)
            .timeout(Duration.ofMinutes(5))
            .maxRetries(3)
            .build();
}
```

बिल्डरले `azd up` द्वारा सेट गरिएको वातावरण चरहरूबाट प्रमाणपत्रहरू पढ्छ। `baseUrl` लाई तपाईंको Azure अन्तबिन्दुमा सेट गर्दा OpenAI क्लाएन्ट Azure OpenAI सँग काम गर्दछ।

**संवाद मेमोरी** - MessageWindowChatMemory ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)) प्रयोग गरेर च्याट इतिहास ट्र्याक गर्नुहोस्:

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` सँग मेमोरी बनाउनुहोस् जसले पछिल्ला १० सन्देशहरू राख्छ। प्रयोगकर्ता र AI सन्देशहरू `UserMessage.from(text)` र `AiMessage.from(text)` सहित थप्नुहोस्। इतिहास `memory.messages()` बाट प्राप्त गरी मोडेलमा पठाउनुहोस्। सेवा प्रत्येक संवाद ID का लागि अलग मेमोरी उदाहरणहरू भण्डारण गर्छ, जसले एकै साथ बहु प्रयोगकर्ताहरूलाई च्याट गर्न अनुमति दिन्छ।

> **🤖 GitHub Copilot सँग प्रयास गर्नुहोस्:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) खोल्नुहोस् र सोध्नुहोस्:
> - "MessageWindowChatMemory ले विन्डो पुरा हुँदा कुन सन्देशहरू हटाउने निर्णय कसरी गर्छ?"
> - "के म इन-मेमोरीको सट्टा डेटाबेस प्रयोग गरेर कस्टम मेमोरी भण्डारण लागू गर्न सक्छु?"
> - "पुराना संवाद इतिहास सङ्कुचित गर्न संक्षेपण कसरी थप्ने?"

stateless च्याट endpoints मेमोरीलाई पूर्ण रूपमा बाइपास गर्छ - जस्तै छिटो शुरुवातमा `chatModel.chat(prompt)` मात्र। stateful endpoint ले मेमोरीमा सन्देशहरू थप्छ, इतिहास पुन:प्राप्त गर्छ, र हरेक अनुरोधसँग त्यस सन्दर्भ समावेश गर्दछ। मोडेल कन्फिगरेसन समान छ, तर फरक ढाँचाहरू।

## Azure OpenAI पूर्वाधार परिनियोजन गर्नुहोस्

**Bash:**
```bash
cd 01-introduction
azd up  # सदस्यता र स्थान छान्नुहोस् (eastus2 सिफारिश गरिन्छ)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # सदस्यता र स्थान चयन गर्नुहोस् (eastus2 सिफारिस गरिएको)
```

> **सूचना:** यदि तपाईंले Timeout त्रुटि (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`) सामना गर्नुहुन्छ भने, `azd up` पुन: चलाउनुहोस्। Azure स्रोतहरू अझै पृष्ठभूमिमा प्रावधान भइरहेको हुन सक्छ, र पुन: प्रयासले स्रोतहरू टर्मिनल अवस्थामा पुगेपछि परिनियोजन पूरा गर्न मद्दत गर्दछ।

यसले गर्नेछ:
1. GPT-5.2 र text-embedding-3-small मोडेलहरू सहित Azure OpenAI स्रोत परिनियोजन
2. परियोजना मूलीमा प्रमाणपत्रहरूको साथ `.env` फाइल स्वचालित रूपमा सिर्जना
3. सबै आवश्यक वातावरण चरहरू सेटअप

**परिनियोजन समस्याहरू छन्?** विस्तृत समस्यासमाधानका लागि [Infrastructure README](infra/README.md) हेर्नुहोस्, जहाँ सबडोमेन नाम द्वन्द्व, Azure Portal मा म्यानुअल परिनियोजन चरणहरू, र मोडेल कन्फिगरेसन सल्लाह छन्।

**परिनियोजन सफल भयो भनी जाँच गर्नुहोस्:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, आदि देखाउनु पर्छ।
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, आदिलाई देखाउनु पर्दछ।
```

> **सूचना:** `azd up` कमाण्डले `.env` फाइल स्वचालित रूपमा बनाउँछ। पछि यसलाई अपडेट गर्न आवश्यक भएमा, तपाईंले `.env` फाइल म्यानुअली सम्पादन गर्न सक्नुहुन्छ वा पुन:निर्माण गर्न:
>
> **Bash:**
> ```bash
> cd ..
> bash .azd-env.sh
> ```
>
> **PowerShell:**
> ```powershell
> cd ..
> .\.azd-env.ps1
> ```

## एप्लिकेशन स्थानीय रूपमा चलाउनुहोस्

**परिनियोजन जाँच गर्नुहोस्:**

Azure प्रमाणपत्रहरूसँग `.env` फाइल मूली निर्दिष्टामा अवश्य छ। यसलाई मोड्युल डिरेक्टरी (`01-introduction/`) बाट चलाउनुहोस्:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT देखाउनु पर्छ
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT देखाउनु पर्छ
```

**एप्लिकेशनहरू सुरु गर्नुहोस्:**

**विकल्प १: Spring Boot Dashboard प्रयोग गर्दै (VS Code प्रयोगकर्ताहरूका लागि सिफारिस गरिएको)**

डिभ कन्टेनरमा Spring Boot Dashboard एक्सटेन्सन समावेश गरिएको छ, जसले सबै Spring Boot एप्लिकेशनहरूलाई दृश्यात्मक रूपमा व्यवस्थापन गर्न सक्षम पार्दछ। VS Code को बाँया पट्टि Activity Bar मा Spring Boot आइकन खोज्नुहोस्।

Spring Boot Dashboard बाट तपाईंले:
- कार्यक्षेत्रका सबै Spring Boot एप्लिकेशनहरू हेर्न सक्नुहुन्छ
- एप्लिकेशनहरू एक क्लिकले सुरू / रोक्न सक्नुहुन्छ
- एप्लिकेशन लगहरू वास्तविक समयमा हेर्न सक्नुहुन्छ
- एप्लिकेशन स्थिति अनुगमन गर्न सक्नुहुन्छ

सिर्फ "introduction" छेउमा प्ले बटन क्लिक गर्नुहोस् वा सबै मोड्युलहरू एकैपटक सुरु गर्नुहोस्।

<img src="../../../translated_images/ne/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code मा Spring Boot Dashboard — सबै मोड्युलहरू एकत्रित स्थानबाट सुरु, रोक्न र अनुगमन गर्न*

**विकल्प २: shell script प्रयोग गरेर**

सबै वेब एप्लिकेशनहरू सुरु गर्नुहोस् (मोदुल ०१-०४):

**Bash:**
```bash
cd ..  # मूल निर्देशिका बाट
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # रूट डाइरेक्टरीबाट
.\start-all.ps1
```

वा केवल यो मोड्युल सुरु गर्नुहोस्:

**Bash:**
```bash
cd 01-introduction
./start.sh
```

**PowerShell:**
```powershell
cd 01-introduction
.\start.ps1
```

दुवै स्क्रिप्टहरूले मूली `.env` फाइलबाट वातावरण चरहरू स्वतः लोड गर्छन् र JAR नहुँदा निर्माण गर्छन्।

> **सूचना:** सबै मोडुलहरूलाई म्यानुअली बनाएर सुरु गर्न मन परे:
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

तपाईंको ब्राउजरमा http://localhost:8080 खोल्नुहोस्।

**रोक्न:**

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

## एप्लिकेशन प्रयोग गर्दै

एप्लिकेशनले दुवै तर्फ दुई च्याट कार्यान्वयनहरू सहित वेब इन्टरफेस प्रदान गर्दछ।

<img src="../../../translated_images/ne/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*ड्यासबोर्ड जसले Simple Chat (stateless) र Conversational Chat (stateful) विकल्पहरू देखाउँछ*

### Stateless Chat (बायाँ प्यानल)

यसलाई पहिलो प्रयास गर्नुहोस्। "मेरो नाम जोन हो" भनी सोध्नुहोस् र तुरुन्तै "मेरो नाम के हो?" प्रश्न गर्नुहोस्। मोडेलले सम्झनेछैन किनभने प्रत्येक सन्देश स्वतन्त्र छ। यो आधारभूत भाषा मोडेल एकीकरणसँग मुख्य समस्या देखाउँछ - कुनै संवाद सन्दर्भ छैन।

<img src="../../../translated_images/ne/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI ले अघिल्लो सन्देशबाट तपाईंको नाम सम्झँदैन*

### Stateful Chat (दायाँ प्यानल)

अब यहाँ उस्तै अनुक्रम प्रयास गर्नुहोस्। "मेरो नाम जोन हो" र त्यसपछि "मेरो नाम के हो?" सोध्नुहोस्। यस पटक यसले सम्झन्छ। भिन्नता MessageWindowChatMemory हो - यसले संवाद इतिहास कायम राख्छ र प्रत्येक अनुरोधमा समावेश गर्दछ। यस्तै उत्पादन संवादात्मक AI काम गर्छ।

<img src="../../../translated_images/ne/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI ले संवादको प्रारम्भमा भनेको तपाईंको नाम सम्झन्छ*

दुबै प्यानलले उही GPT-5.2 मोडेल प्रयोग गर्छन्। फरक मात्र मेमोरी हो। यसले तपाईंको एप्लिकेशनमा मेमोरी के ल्याउँछ र किन यो वास्तविक प्रयोगका लागि अत्यावश्यक छ स्पष्ट पार्दछ।

## अर्को कदमहरू

**अर्को मोड्युल:** [02-prompt-engineering - GPT-5.2 सँग प्रॉम्प्ट इन्जिनियरिङ](../02-prompt-engineering/README.md)

---

**नेभिगेसन:** [← अघिल्लो: Module 00 - Quick Start](../00-quick-start/README.md) | [मुख्यमा फर्किनुहोस्](../README.md) | [अर्को: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यो दस्तावेज AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरेर अनुवाद गरिएको हो। हामी शुद्धताका लागि प्रयासरत छौं भनेपनि, कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादमा त्रुटिहरू वा अशुद्धता हुनसक्छ। मूल दस्तावेज यसको मूल भाषामै आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीका लागि पेशेवर मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न कुनैपनि गलतफहमी वा गलत व्याख्याका लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->