<img src="../../translated_images/hi/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# शुरुआती लोगों के लिए LangChain4j

LangChain4j और Azure OpenAI GPT-5.2 के साथ AI एप्लिकेशन बनाने का एक कोर्स, बुनियादी चैट से लेकर AI एजेंट्स तक।

### 🌐 बहुभाषी समर्थन

#### GitHub Action के माध्यम से समर्थित (स्वचालित और हमेशा अद्यतित)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[अरबी](../ar/README.md) | [बंगाली](../bn/README.md) | [बुल्गारियाई](../bg/README.md) | [बर्मी (म्यांमार)](../my/README.md) | [चीनी (सरलीकृत)](../zh-CN/README.md) | [चीनी (पारंपरिक, हांगकांग)](../zh-HK/README.md) | [चीनी (पारंपरिक, मकाऊ)](../zh-MO/README.md) | [चीनी (पारंपरिक, ताइवान)](../zh-TW/README.md) | [क्रोएशियाई](../hr/README.md) | [चेक](../cs/README.md) | [डेनिश](../da/README.md) | [डच](../nl/README.md) | [एस्टोनियाई](../et/README.md) | [फिनिश](../fi/README.md) | [फ्रेंच](../fr/README.md) | [जर्मन](../de/README.md) | [यूनानी](../el/README.md) | [हिब्रू](../he/README.md) | [हिंदी](./README.md) | [हंगेरियन](../hu/README.md) | [इंडोनेशियाई](../id/README.md) | [इटालियन](../it/README.md) | [जापानी](../ja/README.md) | [कन्नड़](../kn/README.md) | [कोरियाई](../ko/README.md) | [लिथुआनियाई](../lt/README.md) | [मलय](../ms/README.md) | [मलयालम](../ml/README.md) | [मराठी](../mr/README.md) | [नेपालि](../ne/README.md) | [नाइजीरियाई पिज़िन](../pcm/README.md) | [नॉर्वेजियन](../no/README.md) | [फ़ारसी (फ़ारसी)](../fa/README.md) | [पोलिश](../pl/README.md) | [पुर्तगाली (ब्राजील)](../pt-BR/README.md) | [पुर्तगाली (पुर्तगाल)](../pt-PT/README.md) | [पंजाबी (गुरमुखी)](../pa/README.md) | [रोमानियाई](../ro/README.md) | [रूसी](../ru/README.md) | [सर्बियाई (सिरिलिक)](../sr/README.md) | [स्लोवाक](../sk/README.md) | [स्लोवेनियाई](../sl/README.md) | [स्पेनिश](../es/README.md) | [स्वाहिली](../sw/README.md) | [स्वीडिश](../sv/README.md) | [टैगलॉग (फिलीपीनी)](../tl/README.md) | [तमिल](../ta/README.md) | [तेलुगु](../te/README.md) | [थाई](../th/README.md) | [तुर्की](../tr/README.md) | [यूक्रेनी](../uk/README.md) | [उर्दू](../ur/README.md) | [वियतनामी](../vi/README.md)

> **स्थानीय रूप से क्लोन करना पसंद करते हैं?**
>
> इस रिपॉजिटरी में 50+ भाषा अनुवाद शामिल हैं जो डाउनलोड साइज को काफी बढ़ा देते हैं। बिना अनुवाद के क्लोन करने के लिए sparse checkout का उपयोग करें:
>
> **Bash / macOS / Linux:**
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (Windows):**
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> यह आपको कोर्स पूरा करने के लिए आवश्यक सब कुछ तेज़ डाउनलोड के साथ देता है।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## सामग्री सूची

1. [त्वरित प्रारंभ](00-quick-start/README.md) - LangChain4j के साथ शुरुआत करें
2. [परिचय](01-introduction/README.md) - LangChain4j के मूल सिद्धांत सीखें
3. [प्रॉम्प्ट इंजीनियरिंग](02-prompt-engineering/README.md) - प्रभावी प्रॉम्प्ट डिजाइन में महारत हासिल करें
4. [RAG (रिट्रीवल-अगमेंटेड जनरेशन)](03-rag/README.md) - बुद्धिमान ज्ञान-आधारित सिस्टम बनाएं
5. [उपकरण](04-tools/README.md) - बाहरी उपकरणों और सरल सहायक को एकीकृत करें
6. [MCP (मॉडल संदर्भ प्रोटोकॉल)](05-mcp/README.md) - मॉडल संदर्भ प्रोटोकॉल (MCP) और एजेंटिक मॉड्यूल के साथ काम करें

### वीडियो वॉकथ्रू

प्रत्येक मॉड्यूल के साथ एक लाइव सेशन होता है जहाँ हम कदम दर कदम अवधारणाओं और कोड को समझाते हैं।

| मॉड्यूल | वीडियो |
|--------|-------|
| 01 - परिचय | [LangChain4j के साथ शुरुआत](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - प्रॉम्प्ट इंजीनियरिंग | [LangChain4j के साथ प्रॉम्प्ट इंजीनियरिंग](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [LangChain4j के साथ RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - उपकरण और 05 - MCP | [उपकरणों और MCP के साथ AI एजेंट्स](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## सीखने का मार्ग

**LangChain4j में नए हैं?** प्रमुख शब्दों और अवधारणाओं के लिए [शब्दावली](docs/GLOSSARY.md) देखें।

> **त्वरित प्रारंभ**

1. इस रिपॉजिटरी को अपने GitHub अकाउंट पर फोर्क करें
2. क्लिक करें **Code** → **Codespaces** टैब → **...** → **New with options...**
3. डिफ़ॉल्ट का उपयोग करें – यह इस कोर्स के लिए बनाए गए डेवलपमेंट कंटेनर का चयन करेगा
4. क्लिक करें **Create codespace**
5. पर्यावरण के तैयार होने के लिए 5-10 मिनट प्रतीक्षा करें
6. सीधे [त्वरित प्रारंभ](./00-quick-start/README.md) पर जाएं और शुरुआत करें!

मॉड्यूल पूरा करने के बाद, LangChain4j परीक्षण अवधारणाओं को देखने के लिए [परीक्षण गाइड](docs/TESTING.md) का अन्वेषण करें।

> **नोट:** यह प्रशिक्षण GitHub मॉडल्स और Azure OpenAI दोनों का उपयोग करता है। [त्वरित प्रारंभ](00-quick-start/README.md) मॉड्यूल GitHub मॉडल्स का उपयोग करता है (कोई Azure सदस्यता आवश्यक नहीं), जबकि मॉड्यूल 1-5 Azure OpenAI का उपयोग करते हैं। यदि आपके पास नहीं है तो [मुफ्त Azure अकाउंट](https://aka.ms/azure-free-account) के साथ शुरू करें।  


## GitHub Copilot के साथ सीखना

कोडिंग जल्दी शुरू करने के लिए, इस प्रोजेक्ट को GitHub Codespace में या अपने स्थानीय IDE में दिए गए devcontainer के साथ खोलें। इस कोर्स में उपयोग किया गया devcontainer GitHub Copilot के साथ पहले से कॉन्फ़िगर किया गया है जो AI पेयर प्रोग्रामिंग के लिए है।

प्रत्येक कोड उदाहरण में GitHub Copilot से पूछे जाने वाले प्रश्न शामिल हैं जो आपकी समझ को गहरा करेंगे। 💡/🤖 संकेत के लिए देखें:

- **Java फ़ाइल हेडर** - प्रत्येक उदाहरण के लिए विशिष्ट प्रश्न
- **मॉड्यूल README** - कोड उदाहरणों के बाद अन्वेषण संकेत

**कैसे उपयोग करें:** कोई भी कोड फ़ाइल खोलें और Copilot से सुझाए गए प्रश्न पूछें। इसके पास कोडबेस का पूरा संदर्भ है और यह समझा सकता है, विस्तारित कर सकता है, और विकल्प सुझा सकता है।

अधिक जानना चाहते हैं? देखें [AI पेयर प्रोग्रामिंग के लिए Copilot](https://aka.ms/GitHubCopilotAI)।

## अतिरिक्त संसाधन

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![शुरुआती लोगों के लिए LangChain4j](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![शुरुआती लोगों के लिए LangChain.js](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![शुरुआती लोगों के लिए LangChain](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / एजेंट्स
[![शुरुआती लोगों के लिए AZD](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![शुरुआती लोगों के लिए Edge AI](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![शुरुआती लोगों के लिए MCP](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![शुरुआती लोगों के लिए AI एजेंट्स](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### जनरेटिव AI श्रृंखला
[![शुरुआती लोगों के लिए जनरेटिव AI](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![जनरेटिव AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![जनरेटिव AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![जनरेटिव AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### कोर लर्निंग
[![शुरुआती लोगों के लिए ML](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![शुरुआती लोगों के लिए डेटा साइंस](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![शुरुआती लोगों के लिए AI](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![शुरुआती लोगों के लिए साइबरसुरक्षा](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![शुरुआती लोगों के लिए वेब डेवलपमेंट](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![शुरुआती लोगों के लिए IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![शुरुआती लोगों के लिए XR विकास](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### कॉपिलॉट सीरीज
[![AI युग्मित प्रोग्रामिंग के लिए कॉपिलॉट](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET के लिए कॉपिलॉट](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![कॉपिलॉट एडवेंचर](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## सहायता प्राप्त करें

यदि आप फंस जाते हैं या AI ऐप्स बनाने को लेकर कोई प्रश्न हैं, तो जुड़ें:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

यदि आपको उत्पाद प्रतिक्रिया या निर्माण के दौरान त्रुटियाँ मिलती हैं, तो देखें:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## लाइसेंस

MIT लाइसेंस - विवरण के लिए [LICENSE](../../LICENSE) फ़ाइल देखें।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यह दस्तावेज़ AI अनुवादन सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। हम सटीकता के लिए प्रयासरत हैं, लेकिन कृपया ध्यान दें कि स्वचालित अनुवाद में त्रुटियां या असत्यताएं हो सकती हैं। मूल दस्तावेज़ उसकी मूल भाषा में ही अधिकृत स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सलाह दी जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या भ्रांतियों के लिए हम जिम्मेदार नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->