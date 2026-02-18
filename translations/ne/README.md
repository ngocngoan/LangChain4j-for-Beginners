<img src="../../translated_images/ne/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j आरम्भकर्ताहरूका लागि

LangChain4j र Azure OpenAI GPT-5.2 सँग एआई अनुप्रयोगहरू निर्माण गर्ने पाठ्यक्रम, आधारभूत कुराकानीदेखि एआई एजेन्टसम्म।

### 🌐 बहुभाषी समर्थन

#### GitHub Action मार्फत समर्थित (स्वचालित र सधैं अद्यावधिक)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](./README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **स्थानीय रूपमा क्लोन गर्न चाहनुहुन्छ?**
>
> यस रिपोजिटरीमा ५०+ भाषा अनुवादहरू समावेश छन् जसले डाउनलोड आकारलाई धेरै बढाउँछ। अनुवादहरू बिना क्लोन गर्न, sparse checkout प्रयोग गर्नुहोस्:
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
> यसले तपाईंलाई छिटो डाउनलोडको साथ पाठ्यक्रम पूरा गर्न आवश्यक सबै कुरा दिन्छ।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## सामग्री तालिका

1. [छिटो सुरु](00-quick-start/README.md) - LangChain4j सँग सुरु गर्नुहोस्
2. [परिचय](01-introduction/README.md) - LangChain4j को आधारभूत कुरा सिक्नुहोस्
3. [प्रॉम्प्ट इन्जिनियरिङ्ग](02-prompt-engineering/README.md) - प्रभावकारी प्रॉम्प्ट डिजाइनमा महारत हासिल गर्नुहोस्
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - बुद्धिमान ज्ञान आधारित प्रणालीहरू बनाउनुहोस्
5. [उपकरणहरू](04-tools/README.md) - बाह्य उपकरणहरू र सरल सहायकहरू समावेश गर्नुहोस्
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP) र Agentic मोड्युलहरूसँग काम गर्नुहोस्
---

## सिकाइ मार्ग

**LangChain4j मा नयाँ?** मुख्य शब्दहरू र अवधारणाहरूको परिभाषा जान्नका लागि [Glossary](docs/GLOSSARY.md) हेर्नुहोस्।

> **छिटो सुरु**

1. यस रिपोजिटरीलाई आफ्नो GitHub खातामा Fork गर्नुहोस्
2. **Code** → **Codespaces** ट्याब → **...** → **New with options...** मा क्लिक गर्नुहोस्
3. पूर्वनिर्धारित मानहरू प्रयोग गर्नुहोस् – यसले यस पाठ्यक्रमको लागि विकास कन्टेनर चयन गर्नेछ
4. **Create codespace** मा क्लिक गर्नुहोस्
5. वातावरण तयार हुन ५-१० मिनेट पर्खनुहोस्
6. सुरु गर्नको लागि सिधै [छिटो सुरु](./00-quick-start/README.md) मा जानुहोस्!

मोड्युलहरू पूरा गरेपछि, LangChain4j परीक्षण अवधारणाहरू क्रियाशील रूपमा हेर्न [Testing Guide](docs/TESTING.md) अन्वेषण गर्नुहोस्।

> **सूचना:** यस तालिममा दुवै GitHub मोडेलहरू र Azure OpenAI प्रयोग गरिन्छ। [छिटो सुरु](00-quick-start/README.md) मोड्युलले GitHub मोडेलहरू प्रयोग गर्दछ (Azure सदस्यता आवश्यक छैन), जबकि मोड्युल १-५ मा Azure OpenAI प्रयोग गरिन्छ। यदि तपाईंंसँग छैन भने [FREE Azure खाता](https://aka.ms/azure-free-account) सँग सुरु गर्नुहोस्।


## GitHub Copilot सँग सिकाइ

छिटो कोडिङ सुरु गर्न, यो प्रोजेक्ट GitHub Codespace वा प्रदान गरिएको devcontainer सहितको स्थानीय IDE मा खोल्नुहोस्। यस पाठ्यक्रममा प्रयोग गरिएको devcontainer मा GitHub Copilot एआई जोडी प्रोग्रामिङ्गको लागि पूर्व-सेटअप गरिएको छ।

प्रत्येक कोड उदाहरणमा सुझावित प्रश्नहरू छन् जुन तपाईं GitHub Copilot लाई सोधेर आफ्नो बुझाइ गहिरो बनाउन सक्नुहुन्छ। 💡/🤖 संकेतहरू खोज्नुहोस्:

- **Java फाइल हेडरहरू** - प्रत्येक उदाहरणसँग सम्बन्धित प्रश्नहरू
- **मोड्युल READMEहरू** - कोड उदाहरण पछिको अन्वेषण सुझावहरू

**कसरी प्रयोग गर्ने:** कुनै पनि कोड फाइल खोल्नुहोस् र Copilot लाई सुझावित प्रश्नहरू सोध्नुहोस्। यसले कोडबेसको पूर्ण सन्दर्भ पाएको छ र व्याख्या, विस्तार, र वैकल्पिक सुझाव दिन सक्छ।

थप सिक्न चाहनुहुन्छ? [AI जोडी प्रोग्रामिङ्गका लागि Copilot](https://aka.ms/GitHubCopilotAI) हेर्नुहोस्।


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
 
### Core Learning
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT लागि शुरुवातकर्ताहरू](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR विकास लागि शुरुवातकर्ताहरू](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### कोपिलट शृंखला
[![AI जोडिएको प्रोग्रामिङका लागि कोपिलट](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET का लागि कोपिलट](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![कोपिलट साहसिक](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## सहायता प्राप्ति

यदि तपाईं अड्किनुभयो वा AI अनुप्रयोगहरू विकास गर्दा कुनै प्रश्नहरू छन् भने, सामेल हुनुहोस्:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

यदि तपाईंलाई उत्पादन सम्बन्धित प्रतिक्रिया वा निर्माण गर्दा त्रुटिहरू छन् भने भ्रमण गर्नुहोस्:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## अनुमति पत्र

MIT अनुमति पत्र - विवरणहरूको लागि [LICENSE](../../LICENSE) फाइल हेर्नुहोस्।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यो दस्तावेज AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) को प्रयोग गरेर अनुवाद गरिएको हो। हामी यथासम्भव शुद्धताको प्रयास गर्छौं, तर स्वचालित अनुवादमा त्रुटि वा अशुद्धिहरू हुन सक्छन् भनी कृपया जानकार हुनुहोस्। मूल भाषा रहेको दस्तावेजलाई आधिकारिक स्रोत मान्नुपर्छ। महत्वपूर्ण जानकारीका लागि व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट हुने कुनै पनि गलतफहमी वा गलत व्याख्यापनका लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->