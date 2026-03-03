# Module 01: LangChain4j के साथ शुरुआत करना

## Table of Contents

- [Video Walkthrough](../../../01-introduction)
- [What You'll Learn](../../../01-introduction)
- [Prerequisites](../../../01-introduction)
- [Understanding the Core Problem](../../../01-introduction)
- [Understanding Tokens](../../../01-introduction)
- [How Memory Works](../../../01-introduction)
- [How This Uses LangChain4j](../../../01-introduction)
- [Deploy Azure OpenAI Infrastructure](../../../01-introduction)
- [Run the Application Locally](../../../01-introduction)
- [Using the Application](../../../01-introduction)
  - [Stateless Chat (Left Panel)](../../../01-introduction)
  - [Stateful Chat (Right Panel)](../../../01-introduction)
- [Next Steps](../../../01-introduction)

## Video Walkthrough

इस लाइव सत्र को देखें जो इस मॉड्यूल के साथ शुरुआत करने का तरीका समझाता है:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## What You'll Learn

क्विक स्टार्ट में, आपने GitHub Models का उपयोग करके प्रॉम्प्ट भेजने, टूल कॉल करने, RAG पाइपलाइन बनाने और गार्डरिल्स टेस्ट करने के बारे में देखा। उन डेमो में संभावित कार्य दिखाई दिए — अब हम Azure OpenAI और GPT-5.2 पर स्विच करते हैं और उत्पादन-शैली के एप्लिकेशन बनाना शुरू करते हैं। यह मॉड्यूल संवादात्मक AI पर केंद्रित है जो संदर्भ याद रखता है और स्टेट बनाए रखता है — वे अवधारणाएँ जो क्विक स्टार्ट डेमो में उपयोग हुईं लेकिन स्पष्ट रूप से समझाई नहीं गईं।

इस गाइड के दौरान हम Azure OpenAI के GPT-5.2 का उपयोग करेंगे क्योंकि इसकी उन्नत तर्क क्षमताएं विभिन्न पैटर्न के व्यवहार को अधिक स्पष्ट बनाती हैं। जब आप मेमोरी जोड़ते हैं, तो आप अंतर साफ़ देख पाएंगे। इससे यह समझना आसान हो जाता है कि प्रत्येक घटक आपके एप्लिकेशन में क्या लाता है।

आप एक ऐसा एप्लिकेशन बनाएंगे जो दोनों पैटर्न प्रदर्शित करता है:

**Stateless Chat** - प्रत्येक अनुरोध स्वतंत्र होता है। मॉडल को पिछली बातों की कोई जानकारी नहीं होती। यह वह पैटर्न है जो आपने क्विक स्टार्ट में उपयोग किया।

**Stateful Conversation** - प्रत्येक अनुरोध में बातचीत का इतिहास शामिल होता है। मॉडल कई टर्न्स के बीच संदर्भ बनाए रखता है। यही वह आवश्यकताएँ होती हैं जो उत्पादन एप्लिकेशन के लिए होती हैं।

## Prerequisites

- Azure सदस्यता जिसमें Azure OpenAI एक्सेस हो
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **Note:** प्रदान किए गए devcontainer में Java, Maven, Azure CLI और Azure Developer CLI (azd) पहले से इंस्टॉल हैं।

> **Note:** यह मॉड्यूल Azure OpenAI पर GPT-5.2 का उपयोग करता है। डिप्लॉयमेंट `azd up` के माध्यम से स्वचालित रूप से कॉन्फ़िगर होता है - कोड में मॉडल नाम को परिवर्तन न करें।

## Understanding the Core Problem

भाषा मॉडल स्वतंत्र होते हैं। प्रत्येक API कॉल स्वतंत्र होती है। यदि आप कहते हैं "मेरा नाम जॉन है" और फिर पूछते हैं "मेरा नाम क्या है?", तो मॉडल को पता नहीं होगा कि आपने अभी परिचय दिया है। यह हर अनुरोध को मानता है जैसे यह आपकी पहली बातचीत हो।

यह सरल प्रश्नोत्तर के लिए ठीक है लेकिन वास्तविक एप्लिकेशन के लिए नकारात्मक है। कस्टमर सर्विस बॉट को आपको कही गई बातें याद रखनी होती हैं। पर्सनल असिस्टेंट को संदर्भ की जरूरत होती है। कोई भी बहु-टर्न बातचीत मेमोरी की मांग करती है।

निम्नलिखित डायग्राम दोनों दृष्टिकोणों के बीच तुलना करता है — बाईं तरफ, एक स्वतंत्र कॉल जो आपका नाम भूल जाता है; दाईं तरफ, ChatMemory का सहारा लेकर एक Stateful कॉल जो नाम याद रखता है।

<img src="../../../translated_images/hi/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*Stateless (स्वतंत्र कॉल) और Stateful (संदर्भ-संग्रहीत) बातचीत के बीच का अंतर*

## Understanding Tokens

बातचीत में उतरने से पहले, टोकन को समझना महत्वपूर्ण है - ये मूलभूत इकाइयाँ हैं जो भाषा मॉडल टेक्स्ट को प्रोसेस करती हैं:

<img src="../../../translated_images/hi/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*कैसे टेक्स्ट को टोकनों में तोड़ा जाता है - "I love AI!" चार अलग-अलग प्रोसेसिंग यूनिट बन जाते हैं*

टोकन AI मॉडल्स के लिए टेक्स्ट मापने और प्रोसेस करने का तरीका हैं। शब्द, विराम चिह्न, और यहां तक कि स्पेस भी टोकन हो सकते हैं। आपके मॉडल में यह सीमित होता है कि इसे एक बार में कितने टोकन प्रोसेस करने की अनुमति है (GPT-5.2 के लिए 400,000 कुल टोकन, जिसमें 272,000 तक इनपुट टोकन और 128,000 आउटपुट टोकन शामिल हैं)। टोकन को समझना बातचीत की लंबाई और लागत को नियंत्रित करने में मदद करता है।

## How Memory Works

चैट मेमोरी stateless समस्या को बातचीत के इतिहास को बनाए रखकर हल करता है। मॉडल को भेजने से पहले, फ्रेमवर्क प्रासंगिक पिछली संदेशों को जोड़ देता है। जब आप पूछते हैं "मेरा नाम क्या है?", सिस्टम पूरी बातचीत का इतिहास भेजता है ताकि मॉडल देख सके कि आपने पहले कहा था "मेरा नाम जॉन है।"

LangChain4j मेमोरी इम्प्लीमेंटेशन प्रदान करता है जो यह सब स्वचालित रूप से संभालता है। आप चुनते हैं कि कितने संदेश रखे जाएं और फ्रेमवर्क संदर्भ विंडो प्रबंधित करता है। नीचे दिया गया डायग्राम दर्शाता है कि MessageWindowChatMemory हाल की संदेशों की एक स्लाइडिंग विंडो कैसे बनाए रखता है।

<img src="../../../translated_images/hi/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory हाल की संदेशों की स्लाइडिंग विंडो बनाए रखता है, पुराने संदेश स्वतः हटा देता है*

## How This Uses LangChain4j

यह मॉड्यूल क्विक स्टार्ट का विस्तार करता है, Spring Boot एकीकृत करता है और बातचीत की मेमोरी जोड़ता है। भाग इस तरह जुड़ते हैं:

**Dependencies** - दो LangChain4j लाइब्रेरी जोड़ें:

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

**Chat Model** - Azure OpenAI को Spring bean के रूप में कॉन्फ़िगर करें ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

बिल्डर `azd up` द्वारा सेट किए गए पर्यावरण चर से क्रेडेंशियल पढ़ता है। Azure OpenAI से जुड़ने के लिए `baseUrl` अपने Azure endpoint पर सेट करें।

**Conversation Memory** - MessageWindowChatMemory के साथ चैट इतिहास ट्रैक करें ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` के साथ मेमोरी बनाएं ताकि आखिरी 10 संदेश रखें। उपयोगकर्ता और AI संदेश टाइप किए गए रैपर के साथ जोड़ें: `UserMessage.from(text)` और `AiMessage.from(text)`। इतिहास `memory.messages()` से प्राप्त करें और मॉडल को भेजें। सेवा प्रति बातचीत ID अलग मेमोरी उदाहरण संग्रहीत करती है, जिससे कई उपयोगकर्ता एक साथ चैट कर सकते हैं।

> **🤖 GitHub Copilot के साथ आज़माएं:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) खोलें और पूछें:
> - "जब विंडो पूरी हो जाती है तो MessageWindowChatMemory कैसे निर्णय लेता है कि कौन से संदेश हटाने हैं?"
> - "क्या मैं कस्टम मेमोरी स्टोरेज डेटाबेस के बजाय इन-मेमोरी के रूप में लागू कर सकता हूँ?"
> - "पुराने बातचीत के इतिहास को संक्षेप करने के लिए मैं समरी कैसे जोड़ सकता हूँ?"

Stateless चैट endpoint मेमोरी पूरी तरह छोड़ देता है - केवल `chatModel.chat(prompt)` जैसा कि क्विक स्टार्ट में था। Stateful endpoint संदेश मेमोरी में जोड़ता है, इतिहास प्राप्त करता है, और उसे हर अनुरोध के साथ संदर्भ में शामिल करता है। मॉडल की कॉन्फ़िगरेशन समान, पैटर्न अलग।

## Deploy Azure OpenAI Infrastructure

**Bash:**
```bash
cd 01-introduction
azd up  # सदस्यता और स्थान चुनें (eastus2 की सिफारिश की गई)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # सदस्यता और स्थान चुनें (eastus2 की सिफारिश की गई)
```

> **Note:** यदि आपको timeout त्रुटि मिले (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), तो बस `azd up` फिर से चलाएं। Azure संसाधन पृष्ठभूमि में अभी भी प्राविज़निंग में हो सकते हैं, और पुनः प्रयास से डिप्लॉयमेंट तब पूरा होगा जब संसाधन टर्मिनल स्थिति तक पहुंचें।

यह करेगा:
1. GPT-5.2 और text-embedding-3-small मॉडल के साथ Azure OpenAI संसाधन तैनात करेगा
2. प्रोजेक्ट रूट पर स्वचालित `.env` फ़ाइल बनाएगा जिसमें क्रेडेंशियल होंगे
3. सभी आवश्यक पर्यावरण चर सेट करेगा

**डिप्लॉयमेंट में समस्या?** विस्तृत समस्या समाधान के लिए [Infrastructure README](infra/README.md) देखें जिसमें सबडोमेन नाम संघर्ष, मैनुअल Azure पोर्टल डिप्लॉयमेंट चरण, और मॉडल कॉन्फ़िगरेशन गाइड शामिल हैं।

**डिप्लॉयमेंट सफल हुआ यह सत्यापित करें:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, आदि दिखाना चाहिए।
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, आदि दिखाना चाहिए।
```

> **Note:** `azd up` कमांड `.env` फ़ाइल स्वचालित रूप से बनाता है। यदि बाद में इसे अपडेट करने की आवश्यकता हो, तो आप `.env` फ़ाइल को मैन्युअल रूप से संपादित कर सकते हैं या इसे पुनः उत्पन्न कर सकते हैं:
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

## Run the Application Locally

**डिप्लॉयमेंट सत्यापित करें:**

सुनिश्चित करें कि रूट डायरेक्टरी में `.env` फ़ाइल मौजूद है जिसमें Azure क्रेडेंशियल्स हैं। इसे मॉड्यूल डायरेक्टरी (`01-introduction/`) से चलाएं:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दिखाना चाहिए
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दिखाना चाहिए
```

**एप्लिकेशन शुरू करें:**

**विकल्प 1: Spring Boot Dashboard का उपयोग करें (VS Code उपयोगकर्ताओं के लिए अनुशंसित)**

डेव कंटेनर में Spring Boot Dashboard एक्सटेंशन शामिल है, जो सभी Spring Boot एप्लिकेशन को प्रबंधित करने के लिए एक विज़ुअल इंटरफ़ेस प्रदान करता है। इसे VS Code के Activity Bar में बाईं ओर (Spring Boot आइकन देखें) पा सकते हैं।

Spring Boot Dashboard से आप कर सकते हैं:
- विषमता में उपलब्ध सभी Spring Boot एप्लिकेशन देखें
- एक क्लिक से एप्लिकेशन शुरू/रोकें
- एप्लिकेशन लॉग वास्तविक समय में देखें
- एप्लिकेशन की स्थिति देखें

केवल "introduction" के बगल में प्ले बटन क्लिक करें ताकि यह मॉड्यूल शुरू हो, या सभी मॉड्यूल को एक साथ शुरू करें।

<img src="../../../translated_images/hi/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

*VS Code में Spring Boot Dashboard — सभी मॉड्यूल को एक जगह से शुरू, बंद और मॉनिटर करें*

**विकल्प 2: शेल स्क्रिप्ट्स का उपयोग करें**

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

या केवल यह मॉड्यूल शुरू करें:

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

दोनों स्क्रिप्टें स्वचालित रूप से रूट `.env` फ़ाइल से पर्यावरण चर लोड करेंगी और यदि JARs मौजूद नहीं हैं तो उन्हें बनाएंगी।

> **Note:** यदि आप शुरू करने से पहले सभी मॉड्यूल मैन्युअली बनाना पसंद करते हैं:
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

अपने ब्राउजर में http://localhost:8080 खोलें।

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

## Using the Application

यह एप्लिकेशन दो चैट इम्प्लीमेंटेशन वाले वेब इंटरफ़ेस प्रदान करता है जो साथ-साथ हैं।

<img src="../../../translated_images/hi/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*डैशबोर्ड सरल चैट (stateless) और संवादात्मक चैट (stateful) विकल्प दोनों दिखाता है*

### Stateless Chat (Left Panel)

पहले इसे आज़माएं। पूछें "मेरा नाम जॉन है" और तुरंत उसके बाद पूछें "मेरा नाम क्या है?" मॉडल याद नहीं रखेगा क्योंकि प्रत्येक संदेश स्वतंत्र है। यह भाषा मॉडल एकीकरण के साथ मूल समस्या दर्शाता है - कोई बातचीत का संदर्भ नहीं।

<img src="../../../translated_images/hi/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI आपके पिछले संदेश से आपका नाम याद नहीं रखता*

### Stateful Chat (Right Panel)

अब समान अनुक्रम यहां आज़माएं। पूछें "मेरा नाम जॉन है" और फिर "मेरा नाम क्या है?" इस बार यह याद रखता है। अंतर MessageWindowChatMemory है - यह बातचीत के इतिहास को बनाए रखता है और हर अनुरोध के साथ उस संदर्भ को शामिल करता है। यह उत्पादन संवादात्मक AI का तरीका है।

<img src="../../../translated_images/hi/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI बातचीत में पहले के आपके नाम को याद रखता है*

दोनों पैनल एक ही GPT-5.2 मॉडल का उपयोग करते हैं। केवल अंतर मेमोरी है। यह स्पष्ट करता है कि मेमोरी आपके एप्लिकेशन में क्या लाती है और क्यों यह वास्तविक उपयोग मामलों के लिए आवश्यक है।

## Next Steps

**अगला मॉड्यूल:** [02-prompt-engineering - GPT-5.2 के साथ प्रॉम्प्ट इंजीनियरिंग](../02-prompt-engineering/README.md)

---

**नेविगेशन:** [← पिछला: Module 00 - क्विक स्टार्ट](../00-quick-start/README.md) | [मुख्य पृष्ठ पर वापस](../README.md) | [अगला: Module 02 - प्रॉम्प्ट इंजीनियरिंग →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यह दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। जबकि हम सटीकता के लिए प्रयासरत हैं, कृपया ध्यान रखें कि स्वचालित अनुवादों में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल दस्तावेज़ अपने मूल भाषा में ही अधिकृत स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए पेशेवर मानव अनुवाद की सलाह दी जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम जिम्मेदार नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->