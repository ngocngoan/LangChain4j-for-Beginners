# Module 01: LangChain4j सह सुरूवात करणे

## अनुक्रमणिका

- [व्हिडिओ वॉकथ्रू](../../../01-introduction)
- [तुम्हाला काय शिकायला मिळेल](../../../01-introduction)
- [पूर्वअट](../../../01-introduction)
- [मूळ समस्येचे समज](../../../01-introduction)
- [टोकन्स समजून घेणे](../../../01-introduction)
- [स्मृती कशी कार्य करते](../../../01-introduction)
- [हे LangChain4j कसे वापरते](../../../01-introduction)
- [Azure OpenAI इन्फ्रास्ट्रक्चर तैनात करा](../../../01-introduction)
- [स्थानिक पद्धतीने अ‍ॅप्लिकेशन चालवा](../../../01-introduction)
- [अ‍ॅप्लिकेशन वापरणे](../../../01-introduction)
  - [स्टेटलेस चॅट (डावा पॅनेल)](../../../01-introduction)
  - [स्टेटफुल चॅट (उजवा पॅनेल)](../../../01-introduction)
- [पुढील पावले](../../../01-introduction)

## व्हिडिओ वॉकथ्रू

हा लाइव्ह सत्र पाहा जे या मॉड्यूलसह सुरूवात कशी करायची ते स्पष्ट करते:

<a href="https://www.youtube.com/live/nl_troDm8rQ?si=6b85S8xGjWnT2fX9"><img src="https://img.youtube.com/vi/nl_troDm8rQ/maxresdefault.jpg" alt="Getting Started with LangChain4j - Live Session" width="800"/></a>

## तुम्हाला काय शिकायला मिळेल

जर तुम्ही क्विक स्टार्ट पूर्ण केला असेल, तर तुम्ही प्रम्प्ट्स कसे पाठवायचे आणि उत्तरे कशी मिळवायची ते पाहिले आहे. ती ही पाया आहे, पण वास्तविक अ‍ॅप्लिकेशनसाठी अधिक गरज असते. हा मॉड्यूल तुम्हाला समजावतो की प्रतिबंधित संदर्भ आठवण आणि स्थिती कशी राखायची - एक वेळचा डेमो आणि उत्पादनासाठी तयार अ‍ॅप्लिकेशन यातील फरक काय असतो.

या मार्गदर्शकामध्ये आपण Azure OpenAI चा GPT-5.2 वापरू कारण त्याची प्रगत विचार करण्याची क्षमता विविध नमुन्यांचा व्यवहार अधिक स्पष्ट करते. जेव्हा तुम्ही स्मृती जोडता, तेव्हा फरक स्पष्ट दिसतो. यामुळे प्रत्येक घटक आपल्या अ‍ॅप्लिकेशनमध्ये काय आणतो हे समजणे सोपे होते.

तुम्ही एक अ‍ॅप्लिकेशन तयार कराल जे दोन्ही नमुने दाखवेल:

**स्टेटलेस चॅट** - प्रत्येक विनंती स्वतंत्र आहे. मॉडेलला मागील संदेशांची कोणतीही स्मृती नाही. हा तुम्ही क्विक स्टार्टमध्ये वापरलेला नमुना आहे.

**स्टेटफुल संवाद** - प्रत्येक विनंतीमध्ये संभाषण इतिहास असतो. मॉडेल अनेक टर्न्समध्ये संदर्भ राखते. हेच उत्पादनात वापरले जाते.

## पूर्वअट

- Azure सदस्यता आणि Azure OpenAI प्रवेश
- Java 21, Maven 3.9+
- Azure CLI (https://learn.microsoft.com/en-us/cli/azure/install-azure-cli)
- Azure Developer CLI (azd) (https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/install-azd)

> **टीप:** Java, Maven, Azure CLI आणि Azure Developer CLI (azd) पुरविलेल्या devcontainer मध्ये आधीच स्थापित आहेत.

> **टीप:** हा मॉड्यूल Azure OpenAI वरील GPT-5.2 वापरतो. तैनात करणे `azd up` द्वारे आपोआप कॉन्फिगर होते - कोडमधील मॉडेल नाव बदला नये.

## मूळ समस्येचे समज

भाषा मॉडेल्स स्टेटलेस असतात. प्रत्येक API कॉल स्वतंत्र असतो. जर तुम्ही "माझं नाव John आहे" पाठवले आणि नंतर "माझं नाव काय?" विचारले, तर मॉडेलला माहितच नसते की तुम्ही स्वतःची ओळख केली होती. ते प्रत्येक विनंती पहिल्या संभाषणाप्रमाणे वागवते.

हे सोप्या प्रश्नोत्तरेसाठी ठीक आहे पण वास्तविक अ‍ॅप्लिकेशनसाठी उपयुक्त नाही. ग्राहक सेवा बॉट्सने तुम्ही काय सांगितले ते आठवणे आवश्यक आहे. वैयक्तिक सहाय्यकाला संदर्भ आवश्यक आहे. कोणतीही बहु-टर्नची संभाषण स्मृतीची गरज असते.

<img src="../../../translated_images/mr/stateless-vs-stateful.cc4a4765e649c41a.webp" alt="Stateless vs Stateful Conversations" width="800"/>

*स्टेटलेस (स्वतंत्र कॉल) आणि स्टेटफुल (संदर्भ-साक्षर) संभाषण यातील फरक*

## टोकन्स समजून घेणे

संवादांत खोलवर जाण्यापूर्वी, टोकन्स समजणे महत्त्वाचे आहे - मजकुराचे मूलभूत घटक जे भाषा मॉडेल्स प्रक्रिया करतात:

<img src="../../../translated_images/mr/token-explanation.c39760d8ec650181.webp" alt="Token Explanation" width="800"/>

*मजकूर टोकनमध्ये कसा विभागला जातो याचे उदाहरण - "I love AI!" चार स्वतंत्र प्रक्रिया युनिट्समध्ये रूपांतरित*

टोकन्स हा AI मॉडेल्स मजकूर मोजण्यासाठी आणि प्रक्रिया करण्यासाठी वापरतात. शब्द, विरामचिन्हे, आणि अगदी रिकामे जागा देखील टोकन्स होऊ शकतात. तुमच्या मॉडेलवर किती टोकन्स एकावेळी प्रक्रिया करता येतात याची मर्यादा असते (GPT-5.2 साठी 400,000, ज्यात 272,000 इनपुट टोकन्स आणि 128,000 आउटपुट टोकन्स). टोकन्स समजून घ्याने संभाषणाची लांबी आणि खर्च व्यवस्थापित करणे सोपे होते.

## स्मृती कशी कार्य करते

चॅट स्मृती स्टेटलेस समस्येचे निराकरण करते संभाषण इतिहास राखून. तुमची विनंती मॉडेलकडे पाठवण्यापूर्वी, फ्रेमवर्क संबंधित मागील संदेश जोडते. जेव्हा तुम्ही "माझं नाव काय?" विचारता, तेव्हा प्रणाली पूर्ण संभाषण इतिहास पाठवते, ज्यामुळे मॉडेलला कळते की तुम्ही आधी "माझं नाव John आहे" म्हटले होते.

LangChain4j स्मृतीची पूर्तता आपोआप करते. तुम्ही किती संदेश राखायचे ते ठरवता आणि फ्रेमवर्क संदर्भ विंडो व्यवस्थापित करते.

<img src="../../../translated_images/mr/memory-window.bbe67f597eadabb3.webp" alt="Memory Window Concept" width="800"/>

*MessageWindowChatMemory हालचाल करणारी विंडो वापरून अलीकडील संदेश टिकवते, जुने संदेश आपोआप काढून टाकते*

## हे LangChain4j कसे वापरते

हा मॉड्यूल क्विक स्टार्टला विस्तारित करून Spring Boot समाकलित करतो आणि संभाषण स्मृती जोडतो. घटक कसे जुळतात ते येथे दिले आहे:

**आवश्यकता** - दोन LangChain4j लायब्ररी जोडा:

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

**चॅट मॉडेल** - Azure OpenAI ला Spring Bean म्हणून कॉन्फिगर करा ([LangChainConfig.java](../../../01-introduction/src/main/java/com/example/langchain4j/config/LangChainConfig.java)):

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

बिल्डर `azd up` द्वारे सेट केलेल्या पर्यावरणीय चलांमधून प्रमाणपत्र वाचतो. `baseUrl` Azure एंडपॉइंटवर सेट केल्याने OpenAI क्लायंट Azure OpenAI सह कार्य करतो.

**संभाषण स्मृती** - MessageWindowChatMemory सह चॅट इतिहास ट्रॅक करा ([ConversationService.java](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java)):

```java
ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

memory.add(UserMessage.from("My name is John"));
memory.add(AiMessage.from("Nice to meet you, John!"));

memory.add(UserMessage.from("What's my name?"));
AiMessage aiMessage = chatModel.chat(memory.messages()).aiMessage();
memory.add(aiMessage);
```

`withMaxMessages(10)` वापरून स्मृती तयार करा जेणेकरून शेवटचे 10 संदेश जतन होतील. वापरकर्त्याचे आणि AI चे संदेश `UserMessage.from(text)` आणि `AiMessage.from(text)` वापरून जोडा. इतिहास `memory.messages()` ने मिळवा आणि मॉडेलकडे पाठवा. सेवा प्रत्येक संभाषण आयडीसाठी वेगळ्या स्मृती संचयित करते, ज्यामुळे अनेक वापरकर्ते एकाच वेळी चॅट करू शकतात.

> **🤖 [GitHub Copilot](https://github.com/features/copilot) Chat वापरून प्रयत्न करा:** [`ConversationService.java`](../../../01-introduction/src/main/java/com/example/langchain4j/service/ConversationService.java) उघडा आणि विचारा:
> - "MessageWindowChatMemory विंडो पूर्ण झाल्यावर कोणते संदेश काढून टाकते कसे ठरवते?"
> - "मी इन-मेमरीऐवजी डेटाबेस वापरून कस्टम स्मृती संचायक कसा तयार करू शकतो?"
> - "मी जुना संभाषण इतिहास संक्षेपित करण्यासाठी सारांश कसा जोडू?"

स्टेटलेस चॅट एंडपॉइंट पूर्णपणे स्मृती वगळतो - फक्त `chatModel.chat(prompt)` क्विक स्टार्टप्रमाणे. स्टेटफुल एंडपॉइंट स्मृतीत संदेश जोडतो, इतिहास घेतो आणि प्रत्येक विनंतीसह संदर्भ समाविष्ट करतो. मॉडेल कॉन्फिगरेशन समान, नक्की नमुने वेगवेगळे.

## Azure OpenAI इन्फ्रास्ट्रक्चर तैनात करा

**Bash:**
```bash
cd 01-introduction
azd up  # सदस्यता आणि स्थान निवडा (eastus2 शिफारस केलेले)
```

**PowerShell:**
```powershell
cd 01-introduction
azd up  # सदस्यता आणि स्थान निवडा (eastus2 शिफारसीय)
```

> **टीप:** जर तुम्हाला Timeout त्रुटी आली(`RequestConflict: Cannot modify resource ... provisioning state is not terminal`), फक्त पुन्हा `azd up` चालवा. Azure संसाधने पार्श्वभूमीत अजूनही तयार होत असू शकतात, आणि पुन्हा प्रयत्न केल्यावर संसाधने अखेर पूर्ण झाल्यानंतर तैनाती पूर्ण होईल.

हे खालील गोष्टी करेल:
1. GPT-5.2 आणि text-embedding-3-small मॉडेल्ससह Azure OpenAI संसाधन तैनात करणे
2. प्रोजेक्ट रूटमध्ये प्रमाणपत्रांसह `.env` संच आपोआप तयार करणे
3. आवश्यक सर्व पर्यावरणीय चल उपस्थित करणे

**तैनात करण्यास समस्या येत आहेत?** अधिक सविस्तर समस्यांचे निराकरण, ज्यामध्ये उपडोमेन नावांचे संघर्ष, मॅन्युअल Azure पोर्टल तैनाती पायऱ्या, आणि मॉडेल कॉन्फिगरेशन मार्गदर्शन यांचा समावेश आहे, [Infrastructure README](infra/README.md) पहा.

**तैनातपणाची पुष्टी करा:**

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, इत्यादी दाखवले पाहिजे.
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, इत्यादी दाखवायला हवे.
```

> **टीप:** `azd up` आदेश आपोआप `.env` फाइल तयार करतो. नंतर ती सुधारण्यासाठी, तुम्ही `.env` फाइल मॅन्युअली किंवा पुन्हा तयार करू शकता:
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

## स्थानिक पद्धतीने अ‍ॅप्लिकेशन चालवा

**तैनाती पुष्टी करा:**

Azure प्रमाणपत्रांसह `.env` फाइल मूळ डिरेक्टरीत आहे याची खात्री करा:

**Bash:**
```bash
cat ../.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दाखवावेत
```

**PowerShell:**
```powershell
Get-Content ..\.env  # AZURE_OPENAI_ENDPOINT, API_KEY, DEPLOYMENT दाखवायला हवे
```

**अ‍ॅप्लिकेशन सुरू करा:**

**पर्याय 1: Spring Boot डॅशबोर्ड वापरणे (VS Code वापरकर्त्यांसाठी शिफारस)**

dev container मध्ये Spring Boot Dashboard विस्तार समाविष्ट आहे, जो सर्व Spring Boot अ‍ॅप्लिकेशन्सचे व्यवस्थापन करण्यासाठी एक दृश्यात्मक इंटरफेस पुरवतो. तुम्हाला VS Code च्या डाव्या Activity Bar मध्ये (Spring Boot चिन्ह पाहा) हे सापडेल.

Spring Boot Dashboard मध्ये, तुम्ही:
- वर्कस्पेसमधील सर्व उपलब्ध Spring Boot अ‍ॅप्लिकेशन्स पाहू शकता
- एक क्लिक करून अ‍ॅप्लिकेशन्स सुरू/थांबवू शकता
- रिअल-टाइम मध्ये अ‍ॅप्लिकेशन लॉग्स पाहू शकता
- अ‍ॅप्लिकेशन स्थिती मॉनिटर करू शकता

"introduction" जवळ असलेला प्ले बटण क्लिक करा आणि मॉड्यूल सुरू करा, किंवा एकदाच सर्व मॉड्यूल सुरू करा.

<img src="../../../translated_images/mr/dashboard.69c7479aef09ff6b.webp" alt="Spring Boot Dashboard" width="400"/>

**पर्याय 2: शेल स्क्रिप्ट वापरणे**

सर्व वेब अ‍ॅप्लिकेशन्स (मॉड्यूल 01-04) सुरू करा:

**Bash:**
```bash
cd ..  # मूळ निर्देशिकेतून
./start-all.sh
```

**PowerShell:**
```powershell
cd ..  # मूळ निर्देशिका पासून
.\start-all.ps1
```

किंवा फक्त हा मॉड्यूल सुरू करा:

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

दोन्ही स्क्रिप्ट मूळ `.env` फाइलमधून पर्यावरणीय चल आपोआप लोड करतात आणि जर JAR अस्तित्त्वात नसतील तर ते तयार करतील.

> **टीप:** तुम्हाला सुरू करण्यापूर्वी सर्व मॉड्यूल मॅन्युअली तयार करायचे असतील तर:
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

http://localhost:8080 तुमच्या ब्राउझरमध्ये उघडा.

**थांबविण्यासाठी:**

**Bash:**
```bash
./stop.sh  # हे फक्त मॉड्यूल
# किंवा
cd .. && ./stop-all.sh  # सर्व मॉड्यूल्स
```

**PowerShell:**
```powershell
.\stop.ps1  # केवळ हा मॉड्यूल
# किंवा
cd ..; .\stop-all.ps1  # सर्व मॉड्यूल्स
```

## अ‍ॅप्लिकेशन वापरणे

अ‍ॅप्लिकेशन दोन बाजूंबाजूंनी चॅट अंमलबजावणुकीसह वेब इंटरफेस पुरवते.

<img src="../../../translated_images/mr/home-screen.121a03206ab910c0.webp" alt="Application Home Screen" width="800"/>

*डॅशबोर्ड ज्यात सोपा चॅट (स्टेटलेस) आणि संभाषणात्मक चॅट (स्टेटफुल) पर्याय दर्शविलेला आहे*

### स्टेटलेस चॅट (डावा पॅनेल)

हे प्रथम केल्यानंतर पहा. "माझं नाव John आहे" असा प्रश्न विचारा आणि लगेच "माझं नाव काय?" विचारा. मॉडेलला आठवण राहणार नाही कारण प्रत्येक संदेश स्वतंत्र आहे. हे मूळतः भाषा मॉडेलचे एक मूलभूत समाकलन आहे - संभाषण संदर्भ नाही.

<img src="../../../translated_images/mr/simple-chat-stateless-demo.13aeb3978eab3234.webp" alt="Stateless Chat Demo" width="800"/>

*AI ला मागील संदेशातील तुमचं नाव आठवत नाही*

### स्टेटफुल चॅट (उजवा पॅनेल)

आता हाच क्रम येथे वापरून पाहा. "माझं नाव John आहे" विचारा आणि मग "माझं नाव काय?" विचारा. यावेळी ते आठवते. फरक MessageWindowChatMemory आहे - ते संभाषण इतिहास राखते आणि प्रत्येक विनंतीसह तो संदर्भ समाविष्ट करते. हीच उत्पादनातील संभाषण AI ची पद्धत आहे.

<img src="../../../translated_images/mr/conversational-chat-stateful-demo.e5be9822eb23ff59.webp" alt="Stateful Chat Demo" width="800"/>

*AI ला संभाषणात पूर्वी सांगितलेले तुमचं नाव आठवतं*

दोन्ही पॅनेल्स एकाच GPT-5.2 मॉडेलचा वापर करतात. यातील वेगळेपणा फक्त स्मृती आहे. यामुळे तुम्हाला तुमच्या अ‍ॅप्लिकेशनमध्ये स्मृती काय आणते आणि ती का महत्त्वाची आहे हे स्पष्ट होते.

## पुढील पावले

**पुढील मॉड्यूल:** [02-prompt-engineering - GPT-5.2 सह प्रॉम्प्ट इंजिनीअरिंग](../02-prompt-engineering/README.md)

---

**नेव्हिगेशन:** [← मागील: Module 00 - Quick Start](../00-quick-start/README.md) | [मुख्यपृष्ठाकडे परत](../README.md) | [पुढे: Module 02 - Prompt Engineering →](../02-prompt-engineering/README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**सूचना**:
हा दस्तऐवज AI भाषांतर सेवेचा वापर करून अनुवादित केला आहे [Co-op Translator](https://github.com/Azure/co-op-translator). आम्ही अचूकतेसाठी प्रयत्न करत असलो तरी, कृपया लक्षात ठेवा की स्वयंचलित भाषांतरांमध्ये चुका किंवा अचूकतेच्या त्रुटी असू शकतात. मूळ दस्तऐवज त्याच्या मूळ भाषेत अधिकृत स्रोत मानला जावा. महत्त्वाची माहिती असल्यास, व्यावसायिक मानवी भाषांतर घेणे शिफारसीय आहे. या भाषांतराच्या वापरामुळे उद्भवणाऱ्या कोणत्याही गैरसमजांबाबत किंवा चुकीच्या समजुतींबाबत आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->