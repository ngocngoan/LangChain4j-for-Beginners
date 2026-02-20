<img src="../../translated_images/mr/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4jसाठी नवशिक्यांसाठी

LangChain4j आणि Azure OpenAI GPT-5.2 सह AI अनुप्रयोग तयार करण्यासाठी एक कोर्स, मूलभूत चॅटपासून AI एजंटपर्यंत.

### 🌐 बहुभाषिक समर्थन

#### GitHub Action द्वारे समर्थन (स्वयंचलित आणि नेहमी अद्ययावत)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](./README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **स्थानिक क्लोन करणे प्राधान्य देता?**
>
> या रेपॉझिटरीमध्ये 50+ भाषा भाषांतरांचा समावेश आहे ज्यामुळे डाउनलोड आकार मोठा होतो. भाषांतरांशिवाय क्लोन करण्यासाठी sparse checkout वापरा:
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
> यामुळे डाउनलोड खूप जलद होईल आणि आपण कोर्स पूर्ण करण्यासाठी आवश्यक सर्वकाही मिळेल.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## सामग्रीची यादी

1. [जलद प्रारंभ](00-quick-start/README.md) - LangChain4j सह सुरुवात करा
2. [परिचय](01-introduction/README.md) - LangChain4j चे मूलभूत तत्त्वे शिका
3. [प्रॉम्प्ट अभियांत्रिकी](02-prompt-engineering/README.md) - प्रभावी प्रॉम्प्ट डिझाइनमध्ये पारंगत व्हा
4. [RAG (रिट्रीव्हल-ऑगमेंटेड जनरेशन)](03-rag/README.md) - बुद्धिमान ज्ञानाधारित प्रणाली तयार करा
5. [साधने](04-tools/README.md) - बाह्य साधने आणि सोप्या सहाय्यकांना एकत्र करा
6. [MCP (मॉडेल संदर्भ प्रोटोकॉल)](05-mcp/README.md) - मॉडेल संदर्भ प्रोटोकॉल (MCP) आणि एजंटिक मॉड्यूल्ससह काम करा
---

## शिक्षणाचा मार्ग

**LangChain4j मध्ये नवीन आहात?** महत्त्वाच्या संज्ञा आणि संकल्पनांसाठी [शब्दसंग्रह](docs/GLOSSARY.md) पहा.

> **जलद प्रारंभ**

1. हा रेपॉझिटरी आपल्या GitHub खात्यात Fork करा
2. क्लिक करा **Code** → **Codespaces** टॅब → **...** → **New with options...**
3. डीफॉल्ट वापरा – त्यामुळे या कोर्ससाठी तयार केलेले Development container निवडले जाईल
4. क्लिक करा **Create codespace**
5. पर्यावरण तयार होईपर्यंत 5-10 मिनिटे थांबा
6. तात्काळ सुरू करण्यासाठी [जलद प्रारंभ](./00-quick-start/README.md) येथे जा!

मॉड्यूल पूर्ण केल्यानंतर, LangChain4j च्या चाचणी संकल्पना वापरून पाहण्यासाठी [परीक्षण मार्गदर्शक](docs/TESTING.md) एक्सप्लोर करा.

> **टिप:** हे प्रशिक्षण GitHub मॉडेल्स आणि Azure OpenAI दोन्ही वापरते. [जलद प्रारंभ](00-quick-start/README.md) मॉड्यूल GitHub मॉडेल्स वापरतो (Azure सदस्यत्व आवश्यक नाही), तर 1-5 मॉड्यूल Azure OpenAI वापरतात. आपल्याकडे नसेल तर [फ्री Azure खाते](https://aka.ms/azure-free-account) वापरून सुरू करा.


## GitHub Copilot सह शिक्षण

जलद कोडिंगसाठी, हा प्रोजेक्ट GitHub Codespace किंवा प्रदान केलेल्या devcontainer सह आपल्या स्थानिक IDE मध्ये उघडा. या कोर्ससाठी वापरलेला devcontainer आधीच GitHub Copilot सह कॉन्फिगर केलेला आहे जो AI जोडलेले प्रोग्रामिंगसाठी उपयुक्त आहे.

प्रत्येक कोड उदाहरणात GitHub Copilot कडून विचारायच्या सुचवलेल्या प्रश्नांचा समावेश आहे ज्यामुळे आपली समज अधिक खोल होते. 💡/🤖 प्रॉम्प्ट खालील ठिकाणी पहा:

- **Java फायलींच्या हेडरमध्ये** - प्रत्येक उदाहरणाच्या विशिष्ट प्रश्नांसाठी
- **मॉड्यूल README मध्ये** - कोड उदाहरणांनंतर एक्सप्लोरेशन प्रॉम्प्टसाठी

**कसे वापरावे:** कोणतीही कोड फाइल उघडा आणि Copilot कडून सुचवलेले प्रश्न विचारा. Copilot ला पूर्ण कोड संदर्भ उपलब्ध असतो आणि तो समजावू शकतो, विस्तार करू शकतो आणि पर्याय सुचवू शकतो.

अधिक शिकायचे आहे का? पहा [AI जोडलेले प्रोग्रामिंगसाठी Copilot](https://aka.ms/GitHubCopilotAI).


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
 
### Core Learning
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![कोपायलट मालिकाकडून सुरुवातीसाठी IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![सुरुवातीसाठी XR विकास](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---

### कोपायलट मालिका
[![एआय जुळवलेल्या प्रोग्रामिंगसाठी कोपायलट](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET साठी कोपायलट](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![कोपायलट साहस](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## मदत मिळवा

जर तुम्हाला अडचण आल्यास किंवा AI अॅप्स तयार करण्याबाबत कोणतेही प्रश्न असतील तर सामील व्हा:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

जर उत्पादनाबाबत अभिप्राय किंवा बग्स असतील तर येथे भेट द्या:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## परवाना

MIT परवाना - तपशीलांसाठी [LICENSE](../../LICENSE) फाइल पहा.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
हा दस्तऐवज AI अनुवाद सेवेद्वारे [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून अनुवादित करण्यात आला आहे. आम्ही अचूकतेसाठी प्रयत्नशील असलो तरी, कृपया लक्षात घ्या की स्वयंचलित अनुवादांमध्ये चुका किंवा अचूकतेचा अभाव असू शकतो. मूळ दस्तऐवज त्याच्या स्थानिक भाषेत अधिकृत स्रोत मानला पाहिजे. महत्त्वपूर्ण माहितीसाठी, व्यावसायिक मानवी अनुवादाची शिफारस केली जाते. या अनुवादाच्या वापरामुळे उद्भवणाऱ्या कोणत्याही गैरसमजांसाठी किंवा चुकीच्या अर्थसापेक्षतेसाठी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->