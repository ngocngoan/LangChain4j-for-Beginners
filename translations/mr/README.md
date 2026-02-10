<img src="../../translated_images/mr/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 मल्टी-भाषा समर्थन

#### GitHub Action द्वारे समर्थित (स्वयंचलित & नेहमी अद्ययावत)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](./README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **स्थानिकरित्या क्लोन करणे प्राधान्य देता?**

> या रिपॉझिटरीमध्ये 50+ भाषांतील भाषांतर समाविष्ट आहे ज्यामुळे डाउनलोड आकार लक्षणीयरीत्या वाढतो. भाषांतरांशिवाय क्लोन करण्यासाठी, sparse checkout वापरा:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> हे आपल्याला कोर्स पूर्ण करण्यासाठी आवश्यक असलेले सर्व काही देते आणि एक वेगवान डाउनलोड देखील आहे.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4jसाठी नवीन शिकणाऱ्यांसाठी

LangChain4j आणि Azure OpenAI GPT-5.2 सह AI अनुप्रयोग तयार करण्यासाठी कोर्स, मूलभूत चॅटपासून AI एजंटपर्यंत.

**LangChain4j नवीन आहात?** महत्त्वाच्या संज्ञा आणि संकल्पनांसाठी [शब्दसंग्रह](docs/GLOSSARY.md) पहा.

## विषयांची यादी

1. [जलद प्रारंभ](00-quick-start/README.md) - LangChain4j सह सुरुवात करा
2. [परिचय](01-introduction/README.md) - LangChain4j ची मूलतत्त्वे शिका
3. [प्रॉम्प्ट अभियांत्रण](02-prompt-engineering/README.md) - प्रभावी प्रॉम्प्ट डिझाइनमध्ये पारंगत व्हा
4. [RAG (रे Retrieval-Augmented Generation)](03-rag/README.md) - बुद्धिमान ज्ञानाधारित प्रणाली तयार करा
5. [साधने](04-tools/README.md) - बाह्य साधने आणि साधी सहाय्यक एकत्र करा
6. [MCP (मॉडेल संदर्भ प्रोटोकॉल)](05-mcp/README.md) - मॉडेल संदर्भ प्रोटोकॉल (MCP) आणि एजंटिक मॉड्यूल्ससह काम करा
---

##  शिकण्याचा मार्ग

> **जलद प्रारंभ**

1. या रिपॉझिटरीला आपल्या GitHub खात्यात Fork करा
2. निवडा **Code** → **Codespaces** टॅब → **...** → **New with options...**
3. डिफॉल्ट पर्याय वापरा – याचा वापर या कोर्ससाठी तयार केलेल्या Development कंटेनर निवडेल
4. क्लिक करा **Create codespace**
5. वातावरण तयार होईपर्यंत 5-10 मिनिटे प्रतीक्षा करा
6. सरळ [जलद प्रारंभ](./00-quick-start/README.md) कडे पुढे जा आणि सुरुवात करा!

मॉड्यूल पूर्ण केल्यानंतर, LangChain4j च्या चाचणी संकल्पना पाहण्यासाठी [चाचणी मार्गदर्शक](docs/TESTING.md) तपासा.

> **टीप:** हा प्रशिक्षण GitHub Models आणि Azure OpenAI दोन्ही वापरतो. [जलद प्रारंभ](00-quick-start/README.md) मॉड्यूल GitHub Models वापरते (Azure सदस्यत्व आवश्यक नाही), तर मॉड्यूल 1-5 Azure OpenAI वापरतात. आपल्याकडे नसेल तर [फ्री Azure खाते](https://aka.ms/azure-free-account) घेऊन सुरू करा.


## GitHub Copilot सह शिक्षण

कोडिंग लवकर सुरू करण्यासाठी, या प्रकल्पाला GitHub Codespace मध्ये उघडा किंवा स्थानिक IDE मध्ये प्रदान करण्यात आलेल्या devcontainer सह उघडा. या कोर्समध्ये वापरलेले devcontainer आधीच GitHub Copilot सह AI जोडणी प्रोग्रामिंगसाठी कॉन्फिगर केलेले आहे.

प्रत्येक कोड उदाहरणात GitHub Copilot कडे विचारू शकतील असे सूचना प्रश्न आहेत जे आपली समज वाढवतील. 💡/🤖 संकेतांसाठी पहा:

- **Java फाइल हेडर्स** - प्रत्येक उदाहरणासाठी विशिष्ट प्रश्न
- **मॉड्यूल README** - कोड उदाहरणांनंतर अन्वेषण सूचनांसाठी

**कसे वापरावे:** कोणतीही कोड फाईल उघडा आणि Copilot कडे सूचित प्रश्न विचारा. त्याला कोडबेसचा संपूर्ण संदर्भ आहे आणि तो समजावून सांगू, विस्तार करू शकतो, व पर्याय सुचवू शकतो.

अधिक जाणून घेऊ इच्छिता? पहा [AI जोडणी प्रोग्रामिंगसाठी Copilot](https://aka.ms/GitHubCopilotAI).


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
[![नई वापरकर्त्यांसाठी IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![नई वापरकर्त्यांसाठी XR विकास](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot मालिका
[![AI जोडलेल्या प्रोग्रामिंगसाठी Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET साठी Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot साहस](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## मदत मिळवा

जर तुम्हाला अडचण आली किंवा AI अनुप्रयोग तयार करण्याबाबत काही प्रश्न असतील तर, सामील व्हा:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

उत्पादन टिप्पणी किंवा बिल्ड करत असताना त्रुटी असल्यास भेट द्या:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## परवाना

MIT परवाना - तपशीलांसाठी [LICENSE](../../LICENSE) फाईल पहा.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**सूचना**:  
हा दस्तऐवज AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) चा वापर करून अनुवादित केला आहे. आम्ही अचूकतेसाठी प्रयत्न करत आहोत, तरीही कृत्रिम अनुवादांमध्ये चुका किंवा अचूकतेच्या त्रुटी असू शकतात, याची कृपया नोंद घ्या. मूळ दस्तऐवज त्याच्या स्थानिक भाषेत प्राधिकृत स्रोत मानला जाण्याजोगा आहे. महत्त्वाची माहिती असल्यास व्यावसायिक मानवी अनुवाद करणे शिफारसीय आहे. या अनुवादाच्या वापरामुळे उगम होणाऱ्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थ लावणीसाठी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->