# Module 01: LangChain4j के साथ शुरूआत

## Table of Contents

- [वीडियो वॉकथ्रू](../../../01-introduction)
- [आप क्या सीखेंगे](../../../01-introduction)
- [पूर्वापेक्षाएँ](../../../01-introduction)
- [मूल समस्या को समझना](../../../01-introduction)
- [टोकन को समझना](../../../01-introduction)
- [स्मृति कैसे काम करती है](../../../01-introduction)
- [यह LangChain4j का उपयोग कैसे करता है](../../../01-introduction)
- [Azure OpenAI इन्फ्रास्ट्रक्चर तैनात करना](../../../01-introduction)
- [एप्लिकेशन को स्थानीय रूप से चलाना](../../../01-introduction)
- [एप्लिकेशन का उपयोग करना](../../../01-introduction)
  - [Stateless चैट (बाएं पैनल)](../../../01-introduction)
  - [Stateful चैट (दाएं पैनल)](../../../01-introduction)
- [अगले कदम](../../../01-introduction)

## वीडियो वॉकथ्रू

इस लाइव सत्र को देखें जो बताता है कि इस मॉड्यूल के साथ कैसे शुरुआत करें:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="LangChain4j के साथ शुरूआत - लाइव सत्र" width="800"/></a>

## आप क्या सीखेंगे

यदि आपने त्वरित शुरुआत पूरी की है, तो आपने देखा होगा कि प्रॉम्प्ट कैसे भेजें और प्रतिक्रियाएँ कैसे प्राप्त करें। यह आधार है, लेकिन वास्तविक एप्लिकेशन को इससे अधिक आवश्यकता होती है। यह मॉड्यूल आपको बातचीत करने वाली AI बनाने का तरीका सिखाएगा जो संदर्भ को याद रखती है और स्थिति को बनाए रखती है - एक बार के डेमो और उत्पादन-तैयार एप्लिकेशन के बीच का अंतर।

हम पूरे मार्गदर्शन में Azure OpenAI के GPT-5.2 का उपयोग करेंगे क्योंकि इसकी उन्नत तर्क क्षमताएँ विभिन्न पैटर्न के व्यवहार को अधिक स्पष्ट बनाती हैं। जब आप मेमोरी जोड़ेंगे, तो आप स्पष्ट रूप से अंतर देखेंगे। इससे यह समझना आसान हो जाता है कि प्रत्येक घटक आपके एप्लिकेशन में क्या योगदान देता है।

आप एक ऐसा एप्लिकेशन बनाएंगे जो दोनों पैटर्न को प्रदर्शित करता है:

**Stateless चैट** - प्रत्येक अनुरोध स्वतंत्र होता है। मॉडल को पूर्व के संदेशों की कोई स्मृति नहीं होती। यह वह पैटर्न है जिसे आपने त्वरित शुरुआत में उपयोग किया था।

**Stateful बातचीत** - प्रत्येक अनुरोध में बातचीत का इतिहास शामिल होता है। मॉडल कई पहलुओं में संदर्भ को बनाए रखता है। यह वही है जो उत्पादन एप्लिकेशन को चाहिए।

## पूर्वापेक्षाएँ

- Azure सदस्यता जिसमें Azure OpenAI एक्सेस हो
- Java 21, Maven 3.9+ 
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **ध्यान दें:** Java, Maven, Azure CLI और Azure Developer CLI (azd) पहले से ही प्रदान किए गए devcontainer में इंस्टॉल हैं।

> **ध्यान दें:** यह मॉड्यूल Azure OpenAI पर GPT-5.2 उपयोग करता है। तैनाती स्वचालित रूप से `azd up` के माध्यम से कॉन्फ़िगर की जाती है - कोड में मॉडल का नाम बदलें नहीं।

## मूल समस्या को समझना

भाषा मॉडल stateless होते हैं। प्रत्येक API कॉल स्वतंत्र होती है। यदि आप "मेरा नाम जॉन है" भेजते हैं और फिर पूछते हैं "मेरा नाम क्या है?", तो मॉडल को कोई पता नहीं होता कि आपने अभी खुद का परिचय दिया था। यह प्रत्येक अनुरोध को ऐसा समझता है जैसे यह आपकी पहली बातचीत हो।

सरल प्रश्नोत्तर के लिए यह ठीक है लेकिन असली एप्लिकेशन के लिए इसका कोई लाभ नहीं। ग्राहक सेवा बोटों को याद रखना होता है कि आप क्या कह चुके हैं। व्यक्तिगत सहायक को संदर्भ चाहिए। कोई भी बहु-पहल बातचीत स्मृति की मांग करती है।

<img src="../../../translated_images/hi/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless बनाम Stateful बातचीत" width="800"/>

*Stateless (स्वतंत्र कॉल) और Stateful (संदर्भ-सचेत) बातचीत के बीच का अंतर*

## टोकन को समझना

बातचीत में जाने से पहले, टोकनों को समझना महत्वपूर्ण है - वे मूलभूत टेक्स्ट इकाइयाँ हैं जिन्हें भाषा मॉडल संसाधित करते हैं:

<img src="../../../translated_images/hi/token-explanation.c39760d8ec650181.webp" alt="टोकन व्याख्या" width="800"/>

*कैसे टेक्स्ट को टोकनों में तोड़ा जाता है - "I love AI!" चार अलग-अलग संसाधन इकाइयों में बदल जाता है*

टोकन AI मॉडल टेक्स्ट को मापने और संसाधित करने का तरीका हैं। शब्द, विराम चिह्न, और यहाँ तक कि स्पेस भी टोकन हो सकते हैं। आपके मॉडल के पास संसाधित करने की अधिकतम टोकन सीमा होती है (GPT-5.2 के लिए 400,000, जिसमें 272,000 इनपुट टोकन और 128,000 आउटपुट टोकन शामिल हैं)। टोकन समझने से आप बातचीत की लंबाई और लागत को नियंत्रित कर सकते हैं।

## स्मृति कैसे काम करती है

चैट स्मृति stateless समस्या को बातचीत इतिहास बनाए रखकर हल करती है। मॉडल को भेजने से पहले, फ्रेमवर्क प्रासंगिक पिछले संदेशों को जोड़ देता है। जब आप पूछते हैं "मेरा नाम क्या है?", तो सिस्टम पूरी बातचीत इतिहास भेजता है, जिससे मॉडल देख पाता है कि आपने पहले कहा था "मेरा नाम जॉन है।"

LangChain4j मेमोरी कार्यान्वयन प्रदान करता है जो इसे स्वचालित रूप से संभालता है। आप यह चुनते हैं कि कितने संदेश रखे जाएं और फ्रेमवर्क संदर्भ विंडो का प्रबंधन करता है।

<img src="../../../translated_images/hi/memory-window.bbe67f597eadabb3.webp" alt="मेमोरी विंडो अवधारणा" width="800"/>

*MessageWindowChatMemory हाल के संदेशों की एक स्लाइडिंग विंडो बनाए रखता है, स्वचालित रूप से पुराने संदेशों को हटा देता है*

## यह LangChain4j का उपयोग कैसे करता है

यह मॉड्यूल त्वरित शुरुआत का विस्तार करता है स्प्रिंग बूट को एकीकृत करके और बातचीत स्मृति जोड़कर। यहाँ घटक कैसे मेल खाते हैं:

**निर्भरता** - दो LangChain4j लाइब्रेरीज़ जोड़ें:

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

**चैट मॉडल** - Azure OpenAI को स्प्रिंग बीन के रूप में कॉन्फ़िगर करें ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

बिल्डर `azd up` द्वारा सेट किए गए पर्यावरण चर से क्रेडेंशियल पढ़ता है। आपका Azure एंडपॉइंट `baseUrl` में सेट होने से OpenAI क्लाइंट Azure OpenAI के साथ काम करता है।

**बातचीत स्मृति** - MessageWindowChatMemory के साथ चैट इतिहास ट्रैक करें ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` के साथ मेमोरी बनाएं ताकि आखिरी 10 संदेश रखे जा सकें। उपयोगकर्ता और AI संदेश टाइप किए हुए आवरणों के साथ जोड़ें: `UserMessage.from(text)` और `AiMessage.from(text)`। इतिहास प्राप्त करें `memory.messages()` से और इसे मॉडल को भेजें। सेवा प्रत्येक बातचीत ID के लिए अलग-अलग मेमोरी उदाहरण संग्रहीत करती है, जिससे कई उपयोगकर्ता एक साथ चैट कर सकते हैं।

> **🤖 GitHub Copilot Chat के साथ प्रयास करें:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) खोलें और पूछें:
> - "जब विंडो भर जाए तो MessageWindowChatMemory कौन से संदेशों को हटाने का निर्णय कैसे लेता है?"
> - "क्या मैं इनमेमोरी की बजाय डेटाबेस का उपयोग करके कस्टम मेमोरी स्टोरेज लागू कर सकता हूँ?"
> - "पुराने बातचीत इतिहास को संक्षेपित करने के लिए संक्षेप जोड़ने के लिए मैं कैसे करूँ?"

Stateless चैट एंडपॉइंट पूरी तरह मेमोरी को छोड़ देता है - बस `chatModel.chat(prompt)` जैसा कि त्वरित शुरुआत में था। Stateful एंडपॉइंट मेमोरी में संदेश जोड़ता है, इतिहास पुनः प्राप्त करता है, और प्रत्येक अनुरोध में वह संदर्भ शामिल करता है। एक ही मॉडल कॉन्फ़िगरेशन, विभिन्न पैटर्न।

## Azure OpenAI इन्फ्रास्ट्रक्चर तैनात करें

**Bash:**
```bash
cd 01-introduction
azd up  # सदस्यता और स्थान चुनें (eastus2 अनुशंसित)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # सदस्यता और स्थान चुनें (eastus2 अनुशंसित)
```

> **ध्यान दें:** यदि आपको टाइमआउट त्रुटि मिलती है (`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), तो बस `azd up` फिर से चलाएँ। Azure संसाधन अभी भी पृष्ठभूमि में प्रावधान कर रहे हो सकते हैं, और पुनः प्रयास से तैनाती पूरी हो जाती है जब संसाधन अंतिम स्थिति में पहुँच जाते हैं।

यह करेगा:
1. GPT-5.2 और text-embedding-3-small मॉडल के साथ Azure OpenAI संसाधन तैनात करेगा
2. प्रोजेक्ट रूट में स्वचालित रूप से क्रेडेंशियल के साथ `.env` फ़ाइल जनरेट करेगा
3. सभी आवश्यक पर्यावरण चर सेट करेगा

**तैनाती से जुड़ी समस्याएँ?** विवरणपूर्ण समस्या निवारण के लिए [Infrastructure README](infra/README.md) देखें, जिसमें सबडोमेन नाम संघर्ष, मैनुअल Azure पोर्टल तैनाती के चरण, और मॉडल कॉन्फ़िगरेशन मार्गदर्शन शामिल हैं।

**तैनाती सफल हुई या नहीं जांचें:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, आदि दिखाना चाहिए।
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, आदि दिखाना चाहिए।
```

> **ध्यान दें:** `azd up` कमांड स्वचालित रूप से `.env` फ़ाइल बनाता है। यदि बाद में इसे अपडेट करना हो तो आप है तो `.env` फ़ाइल को मैन्युअली संपादित कर सकते हैं या इसे दोबारा जनरेट कर सकते हैं:
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

## एप्लिकेशन को स्थानीय रूप से चलाएँ

**तैनाती सत्यापित करें:**

सुनिश्चित करें कि रूट डायरेक्टरी में Azure क्रेडेंशियल्स के साथ `.env` फ़ाइल मौजूद है:

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

Devcontainer में Spring Boot Dashboard एक्सटेंशन शामिल है, जो सभी Spring Boot एप्लिकेशन के प्रबंधन के लिए एक विज़ुअल इंटरफ़ेस प्रदान करता है। इसे आप VS Code की बायीं तरफ Activity Bar में पा सकते हैं (Spring Boot आइकन देखें)।

Spring Boot Dashboard से आप:
- कार्यक्षेत्र में उपलब्ध सभी Spring Boot एप्लिकेशन देख सकते हैं
- एक क्लिक से एप्लिकेशन शुरू/रोक सकते हैं
- वास्तविक समय में एप्लिकेशन लॉग्स देख सकते हैं
- एप्लिकेशन की स्थिति मॉनिटर कर सकते हैं

बस "introduction" के बगल में प्ले बटन क्लिक करें इस मॉड्यूल को शुरू करने के लिए, या एक साथ सभी मॉड्यूल शुरू करें।

<img src="../../../translated_images/hi/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**विकल्प 2: शेल स्क्रिप्ट्स का उपयोग करें**

सभी वेब एप्लिकेशन शुरू करें (मॉड्यूल 01-04):

**Bash:**
```bash
cd ..  # रूट डायरेक्टरी से
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # मूल निर्देशिका से
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

दोनों स्क्रिप्ट स्वचालित रूप से रूट `.env` फ़ाइल से पर्यावरण चर लोड करेंगी और यदि JARs मौजूद नहीं हैं तो इन्हें बनाएंगी।

> **ध्यान दें:** यदि आप शुरू करने से पहले सभी मॉड्यूल मैन्युअली बनाना पसंद करते हैं:
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

अपने ब्राउज़र में http://localhost:8080 खोलें।

**रोकने के लिए:**

**Bash:**
```bash
./stop.sh  # यह मॉड्यूल केवल
# या
cd .. && ./stop-all.sh  # सभी मॉड्यूल
```

**PowerShell:**
```powershell
.\stop.ps1  # केवल यह मॉड्यूल
# या
cd ..; .\stop-all.ps1  # सभी मॉड्यूल्स
```

## एप्लिकेशन का उपयोग करना

एप्लिकेशन एक वेब इंटरफ़ेस प्रदान करता है जिसमें दो चैट कार्यान्वयन साइड-बाय-साइड हैं।

<img src="../../../translated_images/hi/home-screen.121a03206ab910c0.webp" alt="एप्लिकेशन होम स्क्रीन" width="800"/>

*डैशबोर्ड, जिसमें Simple Chat (stateless) और Conversational Chat (stateful) विकल्प दोनों दिखाए गए हैं*

### Stateless चैट (बाएं पैनल)

पहले इसे आज़माएं। "मेरा नाम जॉन है" पूछें और तुरंत "मेरा नाम क्या है?" पूछें। मॉडल याद नहीं रखेगा क्योंकि प्रत्येक संदेश स्वतंत्र है। यह मूल भाषा मॉडल एकीकरण की समस्या को दर्शाता है - कोई बातचीत संदर्भ नहीं।

<img src="../../../translated_images/hi/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless चैट डेमो" width="800"/>

*AI आपके पिछले संदेश से नाम याद नहीं रखता*

### Stateful चैट (दाएं पैनल)

अब इसे भी आज़माएं। "मेरा नाम जॉन है" पूछें और फिर "मेरा नाम क्या है?" पूछें। इस बार यह याद रखेगा। अंतर MessageWindowChatMemory है - यह बातचीत इतिहास बनाए रखता है और प्रत्येक अनुरोध के साथ संदर्भ शामिल करता है। यही उत्पादन बातचीत वाली AI का तरीका है।

<img src="../../../translated_images/hi/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful चैट डेमो" width="800"/>

*AI बातचीत के आरंभिक हिस्से से आपका नाम याद रखता है*

दोनों पैनल एक ही GPT-5.2 मॉडल का उपयोग करते हैं। केवल अंतर मेमोरी है। यह स्पष्ट करता है कि मेमोरी आपके एप्लिकेशन में क्या लाती है और वास्तविक उपयोग मामलों के लिए यह क्यों आवश्यक है।

## अगले कदम

**अगला मॉड्यूल:** [02-prompt-engineering - GPT-5.2 के साथ प्रॉम्प्ट इंजीनियरिंग](../02-prompt-engineering/README.md)

---

**नेविगेशन:** [← पिछला: Module 00 - Quick Start](../00-quick-start/README.md) | [मुख्य पृष्ठ पर वापस जाएं](../README.md) | [अगला: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
इस दस्तावेज़ का अनुवाद AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके किया गया है। हालांकि हम सटीकता के लिए प्रयासरत हैं, कृपया ध्यान दें कि स्वचालित अनुवाद में त्रुटियाँ या असंगतियाँ हो सकती हैं। मूल दस्तावेज़ अपने मूल भाषा में ही प्रामाणिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए पेशेवर मानव अनुवाद की सलाह दी जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम जिम्मेदार नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->