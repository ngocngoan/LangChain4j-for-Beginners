<img src="../../translated_images/mr/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j सुरुवातीसाठी

LangChain4j आणि Azure OpenAI GPT-5.2 सह AI अनुप्रयोग तयार करण्यासाठी एक कोर्स, मूलभूत चॅटपासून AI एजंटपर्यंत.

### 🌐 बहुभाषिक समर्थन

#### GitHub Action द्वारे समर्थित (स्वयंचलित आणि नेहमी अद्ययावत)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](./README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **स्थानिक कॉपी प्राधान्य द्यायची का?**
>
> या संग्रहात 50+ भाषांमधील अनुवादांचा समावेश आहे ज्यामुळे डाउनलोड आकार वाढतो. अनुवादांशिवाय क्लोन करण्यासाठी sparse checkout वापरा:
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
> हा मार्ग कोर्स पूर्ण करण्यासाठी आवश्यक सर्वकाही प्रदान करतो आणि डाउनलोड अधिक वेगाने होते.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## अनुक्रमणिका

1. [त्वरित प्रारंभ](00-quick-start/README.md) - LangChain4j सह सुरुवात करा
2. [परिचय](01-introduction/README.md) - LangChain4j चे मूलभूत तत्त्वे शिकूया
3. [प्रॉम्प्ट इंजिनिअरिंग](02-prompt-engineering/README.md) - प्रभावी प्रॉम्प्ट डिझाईनमध्ये पारंगत व्हा
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - बुद्धिमान ज्ञानाधारित प्रणाली तयार करा
5. [टूल्स](04-tools/README.md) - बाह्य साधने आणि सोपे सहाय्यक एकत्रित करा
6. [MCP (Model Context Protocol)](05-mcp/README.md) - मॉडेल कॉन्टेक्स्ट प्रोटोकॉल (MCP) आणि एजंटिक मॉड्यूलसह काम करा

### व्हिडिओ वॉकथ्रू

प्रत्येक मॉड्यूलसाठी एक लाइव्ह सत्र आहे ज्यात संकल्पना आणि कोड स्टेप बाय स्टेप समजावून दिले जातात.

| मॉड्यूल | व्हिडिओ |
|--------|-------|
| 01 - परिचय | [LangChain4j सह प्रारंभ](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - प्रॉम्प्ट इंजिनिअरिंग | [LangChain4j सह प्रॉम्प्ट इंजिनिअरिंग](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [LangChain4j सह RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - टूल्स आणि 05 - MCP | [टूल्स आणि MCP सह AI एजंट्स](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## शिक्षण मार्ग

**LangChain4j मध्ये नवीन आहात?** मुख्य संज्ञा आणि संकल्पनांसाठी [शब्दकोश](docs/GLOSSARY.md) पहा.

> **त्वरित प्रारंभ**

1. हा रिपॉझिटरी तुम्हच्या GitHub खात्यावर Fork करा
2. **Code** → **Codespaces** टॅब → **...** → **New with options...** क्लिक करा
3. डिफॉल्ट वापरा – हे या कोर्ससाठी तयार केलेला Development कंटेनर निवडेल
4. **Create codespace** क्लिक करा
5. वातावरण तयार होण्यासाठी 5-10 मिनिटे प्रतीक्षा करा
6. सुरू करण्यासाठी थेट [त्वरित प्रारंभ](./00-quick-start/README.md) कडे जा!

मॉड्यूल पूर्ण केल्यानंतर, LangChain4j च्या चाचणी संकल्पना अनुभवण्यासाठी [Testing Guide](docs/TESTING.md) पाहा.

> **टीप:** हा प्रशिक्षण GitHub मॉडेल्स आणि Azure OpenAI दोन्ही वापरते. [त्वरित प्रारंभ](00-quick-start/README.md) मॉड्यूल GitHub मॉडेल्स वापरतो (Azure सदस्यता आवश्यक नाही), तर 1-5 मॉड्यूल Azure OpenAI वापरतात. आपल्याकडे नसेल तर [मोफत Azure खाते](https://aka.ms/azure-free-account) तयार करा.


## GitHub Copilot सह शिकणे

कोडिंग जलद सुरू करण्यासाठी, हा प्रकल्प GitHub Codespace किंवा दिलेल्या devcontainer सह तुमच्या स्थानिक IDE मध्ये उघडा. या कोर्समध्ये वापरलेला devcontainer AI पेअर्ड प्रोग्रामिंगसाठी GitHub Copilot सह पूर्व-संरचित आहे.

प्रत्येक कोड उदाहरणात GitHub Copilot कडून विचारू शकणारे प्रश्न दिले आहेत जे तुमच्या समजुतीला खोल करू शकतात. 💡/🤖 प्रॉम्प्टसाठी पहा:

- **Java फाईल हेडर्स** - प्रत्येक उदाहरणाशी संबंधित प्रश्न
- **मॉड्यूल README** - कोड उदाहरणांनंतरच्या चौकशीसाठी प्रॉम्प्ट

**कसे वापरायचे:** कोणतीही कोड फाईल उघडा आणि Copilot कडे सुचवलेले प्रश्न विचारा. त्याला कोडबेसची संपूर्ण माहिती आहे आणि तो समजावू, विस्तार करू आणि पर्याय सुचवू शकतो.

अधिक जाणून घ्यायचे आहे का? [AI पेअर्ड प्रोग्रामिंगसाठी Copilot](https://aka.ms/GitHubCopilotAI) पहा.


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
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### कॉपायलट मालिका
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## मदत मिळवा

जर तुम्ही अडकले असाल किंवा AI अ‍ॅप्स तयार करण्याबाबत काही प्रश्न असतील, तर सामील व्हा:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

उत्पादनाबाबत अभिप्राय किंवा वेगळ्या त्रुटींसाठी भेट द्या:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## परवाना

MIT परवाना - तपशीलासाठी [LICENSE](../../LICENSE) फाइल पहा.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**सूचना**:
हे दस्तऐवज AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) याच्या मदतीने भाषांतरित केले आहे. आम्ही अचूकतेसाठी प्रयत्न करतो, परंतु कृपया लक्षात ठेवा की स्वयंचलित अनुवादांमध्ये चुका किंवा विसंगती असू शकतात. मूळ दस्तऐवज त्याच्या स्थानिक भाषेत अधिकृत स्रोत मानला पाहिजे. महत्त्वाच्या माहितीकरिता व्यावसायिक मानवी अनुवादाचा सल्ला घेणे श्रेयस्कर आहे. या अनुवादाच्या वापरामुळे उद्भवलेल्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थाबद्दल आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->