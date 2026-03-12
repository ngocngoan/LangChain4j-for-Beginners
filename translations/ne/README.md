<img src="../../translated_images/ne/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j शुरुआतीहरूका लागि

LangChain4j र Azure OpenAI GPT-5.2 सँग AI अनुप्रयोगहरू बनाउनको लागि कोर्स, आधारभूत च्याटदेखि AI एजेन्टसम्म।

### 🌐 बहुभाषिक समर्थन

#### GitHub Action मार्फत समर्थन (स्वचालित र सधैं अद्यावधिक)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](./README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **स्थानीय रूपमा क्लोन गर्न चाहनुहुन्छ?**
>
> यो रिपोजिटरीमा ५०+ भाषा अनुवादहरू समावेश छन् जसले डाउनलोड साइज उल्लेख्य रूपमा बढाउँछ। अनुवाद बिना क्लोन गर्न sparse checkout प्रयोग गर्नुहोस्:
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
> यसले तपाईंलाई यस कोर्स पूरा गर्न आवश्यक सबै कुरा धेरै छिटो डाउनलोडको साथ दिन्छ।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## सामग्री तालिका

1. [छिटो शुरुवात](00-quick-start/README.md) - LangChain4j सँग शुरुवात गर्नुहोस्
2. [परिचय](01-introduction/README.md) - LangChain4j का मूलभूत सिद्धान्तहरू जान्नुहोस्
3. [प्रोम्प्ट इन्जिनियरिंग](02-prompt-engineering/README.md) - प्रभावकारी प्रोम्प्ट डिजाइनमा माहिर हुनुहोस्
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - बुद्धिमानी ज्ञान-आधारित प्रणालीहरू बनाउनुहोस्
5. [उपकरणहरू](04-tools/README.md) - बाह्य उपकरणहरू र साधारण सहायकरूपमा एकीकृत गर्नुहोस्
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP) र Agentic मोड्युलहरूसँग काम गर्नुहोस्

### भिडियो वाल्कथ्रू

प्रत्येक मोड्युलसँग एक साथी प्रत्यक्ष सत्र छ जहाँ हामी अवधारणाहरू र कोडलाई चरण-दर-चरण हिँड्छौं।

| मोड्युल | भिडियो |
|--------|-------|
| 01 - परिचय | [LangChain4j सँग शुरुवात](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - प्रोम्प्ट इन्जिनियरिंग | [LangChain4j सँग प्रोम्प्ट इन्जिनियरिंग](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [LangChain4j सँग RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - उपकरणहरू & 05 - MCP | [उपकरणहरू र MCP सँग AI एजेन्टहरू](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## सिकाइ मार्ग

**LangChain4j मा नयाँ हुनुहुन्छ?** मुख्य सर्तहरू र अवधारणाहरूको परिभाषाका लागि [शब्दावली](docs/GLOSSARY.md) हेर्नुहोस्।

> **छिटो शुरुवात**

1. यो रिपोजिटरी तपाईंको GitHub खातामा फोर्क गर्नुहोस्
2. क्लिक गर्नुहोस् **Code** → **Codespaces** ट्याब → **...** → **New with options...**
3. पूर्वनिर्धारित विकल्पहरू प्रयोग गर्नुहोस् – यसले यो कोर्सका लागि बनाएको Development container चयन गर्छ
4. क्लिक गर्नुहोस् **Create codespace**
5. ५-१० मिनेट पर्खनुहोस् जबसम्म वातावरण तयार हुँदैन
6. सिधै जानुहोस् [छिटो शुरुवात](./00-quick-start/README.md) मा र सुरु गर्नुहोस्!

मोड्युलहरू पूरा गरेपछि, [टेस्टिङ मार्गदर्शन](docs/TESTING.md) अन्वेषण गर्नुहोस् र LangChain4j परीक्षण अवधारणाहरू व्यवहारमा हेर्नुहोस्।

> **नोट:** यो तालिमले GitHub मोडेलहरू र Azure OpenAI दुवै प्रयोग गर्दछ। [छिटो शुरुवात](00-quick-start/README.md) मोड्युल GitHub मोडेलहरू प्रयोग गर्दछ (Azure सदस्यता आवश्यक छैन), जबकि मोड्युल १-५ Azure OpenAI प्रयोग गर्दछ। यदि तपाईंसँग Azure खाता छैन भने, [नि:शुल्क Azure खाता](https://aka.ms/azure-free-account) सँग शुरुवात गर्नुहोस्।


## GitHub Copilot सँग सिकाइ

शीघ्र कोडिङ सुरु गर्न, यो प्रोजेक्ट GitHub Codespace वा तपाईंको स्थानीय IDE मा दिइएको devcontainer सँग खोल्नुहोस्। यस कोर्समा प्रयोग गरिएको devcontainer GitHub Copilot सँग AI जोडिएको प्रोग्रामिङका लागि पूर्व-संरचित छ।

हरेक कोड उदाहरणसँग GitHub Copilot लाई सोध्न सकिने सुझाव प्रश्नहरू समावेश छन् जसले तपाईंको बुझाइ गहिरो बनाउँछ। संकेत खोज्नुहोस् 💡/🤖 हुन सक्ने:

- **Java फाइल हेडरहरू** - प्रत्येक उदाहरणका लागि विशेष प्रश्नहरू
- **मोड्युल READMEs** - कोड उदाहरण पछि अन्वेषण प्रोत्साहनहरू

**कसरी प्रयोग गर्ने:** कुनै पनि कोड फाइल खोल्नुहोस् र Copilot लाई सुझाइएको प्रश्नहरू सोध्नुहोस्। यसले सबै कोडबेसको पूरा सन्दर्भ छ र व्याख्या गर्न, विस्तार गर्न, र विकल्पहरू सुझाव गर्न सक्छ।

थप जान्न चाहनुहुन्छ? हेर्नुहोस् [AI जोडिएको प्रोग्रामिङको लागि Copilot](https://aka.ms/GitHubCopilotAI)।

## अतिरिक्त स्रोतहरू

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain for Beginners](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agents
[![AZD for Beginners](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI for Beginners](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP for Beginners](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents for Beginners](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generative AI Series
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### मुख्य सिकाइ
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![साइबरसुरक्षा लागि शुरुवातीहरू](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![वेब विकास लागि शुरुवातीहरू](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT लागि शुरुवातीहरू](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR विकास लागि शुरुवातीहरू](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### कोपिलट श्रृंखला
[![AI जोडिएको प्रोग्रामिङको लागि कोपिलट](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET का लागि कोपिलट](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![कोपिलट साहसिक](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## सहयोग प्राप्त गर्ने

यदि तपाईं अड्कीए वा AI एपहरू विकास गर्दा कुनै प्रश्न छ भने, सम्मिलित हुनुहोस्:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

यदि उत्पादन सुझाव वा त्रुटिहरू छन् विकास गर्दा यहाँ जानुहोस्:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## लाइसेन्स

MIT लाइसेन्स - विवरणको लागि [LICENSE](../../LICENSE) फाइल हेर्नुहोस्।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यस दस्तावेजलाई AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) को प्रयोग गरी अनुवाद गरिएको हो। हामी शुद्धताको लागि प्रयास गर्छौं, तर कृपया जान्नुस् कि स्वचालित अनुवादमा गल्ती वा असत्यताहरू हुन सक्छन्। मूल भाषामा रहेको दस्तावेजलाई अधिकारिक स्रोत मानिनु पर्छ। महत्त्वपूर्ण जानकारीका लागि व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न हुने कुनै पनि गलतफहमी वा गलत व्याख्याहरूको लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->