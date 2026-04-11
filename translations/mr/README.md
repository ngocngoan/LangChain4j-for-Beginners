<img src="../../translated_images/mr/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j सुरुवातीसाठी

LangChain4j आणि Azure OpenAI GPT-5.2 वापरून AI अनुप्रयोग तयार करण्यासाठी एक कोर्स, मूलभूत चॅटपासून AI एजंट्सपर्यंत.

### 🌐 बहुभाषिक समर्थन

#### GitHub क्रिया द्वारे समर्थित (स्वयंचलित आणि नेहमी अद्ययावत)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Khmer](../km/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](./README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **स्थानिक क्लोन प्राधान्य देता?**
>
> या रेपॉजिटरीमध्ये 50+ भाषांमध्ये भाषांतर केले आहे ज्यामुळे डाउनलोड आकार मोठा होतो. भाषांतरांशिवाय क्लोन करण्यासाठी sparse checkout वापरा:
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
> यात तुम्हाला कोर्स पूर्ण करण्यासाठी आवश्यक सर्व काही खूप गतिमान डाउनलोडसह मिळेल.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## अनुक्रमणिका

1. [त्वरित सुरुवात](00-quick-start/README.md) - LangChain4j सह सुरुवात करा
2. [परिचय](01-introduction/README.md) - LangChain4j चे मूलतत्त्व जाणून घ्या
3. [प्रॉम्प्ट अभियांत्रिकी](02-prompt-engineering/README.md) - प्रभावी प्रॉम्प्ट डिझाइनमध्ये पारंगत व्हा
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - बुद्धिमान ज्ञानाधारित प्रणाली तयार करा
5. [टूल्स](04-tools/README.md) - बाह्य साधने आणि सोप्या सहाय्यकांसह एकत्रित करा
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP) आणि Agentic मॉड्यूलसह काम करा

### व्हिडिओ वॉकथ्रू

प्रत्येक मॉड्यूलसाठी एक सत्र आहे जिथे आपण संकल्पना आणि कोड टप्प्याटप्प्याने पाहतो.

| मॉड्यूल | व्हिडिओ |
|--------|-------|
| 01 - परिचय | [LangChain4j सह सुरुवात](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - प्रॉम्प्ट अभियांत्रिकी | [LangChain4j सह प्रॉम्प्ट अभियांत्रिकी](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [LangChain4j सह RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - टूल्स & 05 - MCP | [टूल्स आणि MCP सह AI एजंट्स](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## शिक्षण मार्ग

**LangChain4j मध्ये नवीन आहात का?** महत्त्वाच्या संकल्पनांसाठी [शब्दसंग्रह](docs/GLOSSARY.md) पहा.

> **त्वरित सुरुवात**

1. ही रेपॉजिटरी तुमच्या GitHub खात्यावर Fork करा
2. **Code** → **Codespaces** टॅब → **...** → **New with options...** क्लिक करा
3. डीफॉल्ट्स वापरा – यामुळे या कोर्ससाठी तयार केलेले विकास कंटेनर निवडले जाईल
4. **Create codespace** क्लिक करा
5. वातावरण तयार होण्यासाठी 5-10 मिनिटे प्रतीक्षा करा
6. थेट [त्वरित सुरुवात](./00-quick-start/README.md) येथे जा आणि सुरू करा!

मॉड्यूल पूर्ण केल्यानंतर, LangChain4j च्या टेस्टिंग संकल्पना पहाण्यासाठी [टेस्टिंग मार्गदर्शक](docs/TESTING.md) एक्सप्लोर करा.

> **नोट:** हे प्रशिक्षण GitHub मॉडेल्स आणि Azure OpenAI दोन्ही वापरते. [त्वरित सुरुवात](00-quick-start/README.md) मॉड्यूल GitHub मॉडेल्स वापरते (Azure सदस्यता आवश्यक नाही), तर मॉड्यूल 1-5 मध्ये Azure OpenAI वापरले जाते. जर तुमच्याकडे नसेल तर [मोफत Azure खाते](https://aka.ms/azure-free-account) वापरून सुरू करा.


## GitHub Copilot सह शिक्षण

कोडिंग लवकर सुरू करण्यासाठी, हा प्रकल्प GitHub Codespace मध्ये किंवा दिलेल्या devcontainer सह तुमच्या स्थानिक IDE मध्ये उघडा. या कोर्समध्ये वापरलेला devcontainer GitHub Copilot सह पूर्व-संरचीत केलेला आहे, जो AI आधारित जोडीदार प्रोग्रामिंगसाठी आहे.

प्रत्येक कोड उदाहरणात GitHub Copilot कडे विचारू शकणारे सुचवलेले प्रश्न समाविष्ट आहेत ज्यामुळे तुमची समज वाढेल. 💡/🤖 प्रॉम्प्टसाठी पाहा:

- **Java फाइलच्या हेडर** - प्रत्येक उदाहरणासाठी विशिष्ट प्रश्न
- **मॉड्यूल README** - कोड उदाहरणांनंतरच्या शोध प्रश्न

**कसे वापरायचे:** कोणतीही कोड फाइल उघडा आणि Copilot कडे सुचवलेले प्रश्न विचारा. त्याला संपूर्ण कोडबेसची पार्श्वभूमी असते आणि तो समजावून सांगू शकतो, विस्तारित करू शकतो, आणि पर्याय सुचवू शकतो.

आणि अधिक शिकायचे आहे का? पाहा [Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI).


## अतिरिक्त संसाधने

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
 
### कोर शिक्षण
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![सुरक्षितता सुरुवातीसाठी](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![वेब विकास सुरुवातीसाठी](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT सुरुवातीसाठी](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR विकास सुरुवातीसाठी](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### कॉपायलट मालिका
[![AI जोडलेल्या प्रोग्रामिंगसाठी कॉपायलट](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET साठी कॉपायलट](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![कॉपायलट साहस](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## मदत घेणे

जर तुम्ही अडकले असाल किंवा AI अॅप्स तयार करताना काही प्रश्न असतील तर सामील व्हा:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

जर तुमच्याकडे उत्पादन संदर्भात अभिप्राय किंवा त्रुटी असतील तर येथे भेट द्या:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## परवाना

MIT परवाना - तपशीलांसाठी [LICENSE](../../LICENSE) फाइल पहा.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**सवालाखालील सूचना**:
हा दस्तऐवज AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून अनुवादित केला आहे. आम्ही अचूकतेसाठी प्रयत्न करतो, कृपया लक्षात ठेवा की स्वयंचलित अनुवादांमध्ये चुका किंवा अचूकतेत त्रुटी असू शकतात. मूळ दस्तऐवज त्याच्या देशी भाषेत अधिकृत स्रोत म्हणून विचारात घ्यावा. महत्त्वपूर्ण माहितीच्या बाबतीत व्यावसायिक मानवी अनुवादाची शिफारस केली जाते. या अनुवादाच्या वापरामुळे होणाऱ्या कोणत्याही गैरसमजुती किंवा चुकीच्या अर्थ लावण्याबद्दल आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->