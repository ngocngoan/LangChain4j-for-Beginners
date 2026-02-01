<img src="../../translated_images/hi/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 बहुभाषी समर्थन

#### GitHub एक्शन के माध्यम से समर्थित (स्वचालित और हमेशा अद्यतित)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](./README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **स्थानीय रूप से क्लोन करना पसंद करते हैं?**

> इस रिपॉजिटरी में 50+ भाषा अनुवाद शामिल हैं जो डाउनलोड साइज को काफी बढ़ाते हैं। अनुवाद के बिना क्लोन करने के लिए sparse checkout का उपयोग करें:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> इससे आपको कोर्स पूरा करने के लिए आवश्यक सारी चीजें बहुत तेजी से डाउनलोड मिलती हैं।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# शुरुआती लोगों के लिए LangChain4j

LangChain4j और Azure OpenAI GPT-5 के साथ AI एप्लिकेशन बनाने के लिए एक कोर्स, बेसिक चैट से लेकर AI एजेंट तक।

**LangChain4j में नए हैं?** मुख्य शब्दों और अवधारणाओं की परिभाषाओं के लिए [Glossary](docs/GLOSSARY.md) देखें।

## सामग्री तालिका

1. [Quick Start](00-quick-start/README.md) - LangChain4j के साथ शुरुआत करें
2. [Introduction](01-introduction/README.md) - LangChain4j के मूलभूत सिद्धांत सीखें
3. [Prompt Engineering](02-prompt-engineering/README.md) - प्रभावी प्रॉम्प्ट डिज़ाइन में महारत हासिल करें
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - बुद्धिमान ज्ञान-आधारित सिस्टम बनाएं
5. [Tools](04-tools/README.md) - बाहरी टूल्स और सरल सहायक उपकरण एकीकृत करें
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP) और Agentic मॉड्यूल के साथ काम करें
---

## लर्निंग पाथ

> **त्वरित शुरुआत**

1. इस रिपॉजिटरी को अपने GitHub अकाउंट पर Fork करें
2. क्लिक करें **Code** → **Codespaces** टैब → **...** → **New with options...**
3. डिफ़ॉल्ट का उपयोग करें – यह इस कोर्स के लिए बनाए गए डेवलपमेंट कंटेनर का चयन करेगा
4. क्लिक करें **Create codespace**
5. वातावरण के तैयार होने के लिए 5-10 मिनट प्रतीक्षा करें
6. सीधे [Quick Start](./00-quick-start/README.md) पर जाएं और शुरुआत करें!

मॉड्यूल पूरा करने के बाद, LangChain4j परीक्षण अवधारणाएँ देखने के लिए [Testing Guide](docs/TESTING.md) देखें।

> **ध्यान दें:** यह प्रशिक्षण GitHub मॉडल और Azure OpenAI दोनों का उपयोग करता है। [Quick Start](00-quick-start/README.md) मॉड्यूल GitHub मॉडल का उपयोग करता है (कोई Azure सब्सक्रिप्शन आवश्यक नहीं), जबकि मॉड्यूल 1-5 Azure OpenAI का उपयोग करते हैं। यदि आपके पास नहीं है तो [मुफ़्त Azure खाता](https://aka.ms/azure-free-account) के साथ शुरुआत करें।


## GitHub Copilot के साथ सीखना

त्वरित कोडिंग शुरू करने के लिए, इस प्रोजेक्ट को GitHub Codespace या स्थानीय IDE में दी गई devcontainer के साथ खोलें। इस कोर्स में उपयोग किया गया devcontainer AI पेयर्ड प्रोग्रामिंग के लिए GitHub Copilot के साथ पूर्व-संरचित आता है।

प्रत्येक कोड उदाहरण में GitHub Copilot से पूछने के लिए सुझाए गए प्रश्न शामिल हैं ताकि आपकी समझ गहरी हो सके। 💡/🤖 संकेत देखें:

- **Java फाइल हेडर** - प्रत्येक उदाहरण के लिए विशिष्ट प्रश्न
- **मॉड्यूल README** - कोड उदाहरणों के बाद अन्वेषण के लिए संकेत

**कैसे उपयोग करें:** कोई भी कोड फाइल खोलें और Copilot से सुझाए गए प्रश्न पूछें। इसे कोडबेस का पूरा संदर्भ पता है और यह समझा सकता है, बढ़ा सकता है, और विकल्प सुझा सकता है।

अधिक जानना चाहते हैं? देखें [AI पेयर्ड प्रोग्रामिंग के लिए Copilot](https://aka.ms/GitHubCopilotAI)।

## अतिरिक्त संसाधन

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / एजेंट्स
[![AZD for Beginners](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI for Beginners](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP for Beginners](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents for Beginners](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### जनरेटिव AI श्रृंखला
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### कोर लर्निंग
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![शुरुआत करने वालों के लिए XR विकास](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---

### कॉपिलट श्रृंखला  
[![AI युग्मित प्रोग्रामिंग के लिए कॉपिलट](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)  
[![C#/.NET के लिए कॉपिलट](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)  
[![कॉपिलट साहसिक](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)  
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## सहायता प्राप्त करें

अगर आप फंस जाते हैं या AI ऐप बनाने के बारे में कोई प्रश्न है, तो शामिल हों:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

अगर आपके पास उत्पाद प्रतिक्रिया या निर्माण के दौरान त्रुटियां हैं, तो देखें:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## लाइसेंस

MIT लाइसेंस - विवरण के लिए [LICENSE](../../LICENSE) फ़ाइल देखें।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
इस दस्तावेज़ का अनुवाद AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके किया गया है। यद्यपि हम सटीकता के लिए प्रयासरत हैं, कृपया ध्यान दें कि स्वचालित अनुवादों में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल दस्तावेज़ उसकी मूल भाषा में आधिकारिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए पेशेवर मानव अनुवाद की सलाह दी जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम जिम्मेदार नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->