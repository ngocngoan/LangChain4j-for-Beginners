<img src="../../translated_images/ml/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 ബഹുഭാഷാ പിന്തുണ

#### GitHub ആക്ഷൻ വഴി പിന്തുണ (സ്വയം പ്രവർത്തിക്കുന്നതും എപ്പോഴും പുതുക്കപ്പെട്ടതുമായ)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](./README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **പ്രാദേശികമായി ക്ലോൺ ചെയ്യാൻ ആഗ്രഹിക്കുന്നുവോ?**

> ഈ റിപ്പോസിറ്ററിയിൽ 50 ൽ അധികം ഭാഷാതർജ്ജമകൾ ഉൾപ്പെടുന്നുവെന്ന് ഡൗൺലോഡ് വലുപ്പം വലിയതായി മാറുന്നു. ഭാഷാതർജ്ജമകൾ ഇല്ലാതെ ക്ലോൺ ചെയ്യാൻ sparse checkout ഉപയോഗിക്കുക:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> ഇത് നിങ്ങൾക്ക് കോഴ്‌സ് പൂർത്തിയാക്കാൻ ആവശ്യമായ എല്ലാ കാര്യവും വളരെ വേഗത്തിലുള്ള ഡൗൺലോഡോടെ നൽകും.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# തുടങ്ങിയവർക്ക് വേണ്ടി LangChain4j

LangChain4j ഉപയോഗിച്ച് AI പ്രയോഗങ്ങൾ നിർമ്മിക്കുന്നതിനുള്ള ഒരു കോഴ്‌സ്, അടിസ്ഥാന ചാറ്റിൽ നിന്നും AI ഏജന്റുകളിലേക്കും Azure OpenAI GPT-5 ഉപയോഗിച്ച്.

**LangChain4j പുതിയവരാണോ?** പ്രധാന പദങ്ങൾക്കും ആശയങ്ങൾക്കും വേണ്ടി [Glossary](docs/GLOSSARY.md) പരിശോധിക്കുക.

## ഉള്ളടക്ക പട്ടിക

1. [ക്വിക്ക് സ്റ്റാർട്ട്](00-quick-start/README.md) - LangChain4j ഉപയോഗിച്ച് തുടങ്ങുക
2. [പരിച്ചയം](01-introduction/README.md) - LangChain4j ന്റെ അടിസ്ഥാനങ്ങൾ പഠിക്കുക
3. [പ്രോംപ്റ്റ് എഞ്ചിനീയറിങ്](02-prompt-engineering/README.md) - ഫലപ്രദമായ പ്രോംപ്റ്റ് രൂപകൽപ്പനയിൽ പകുതി നേടുക
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - ബുദ്ധിമുട്ടുവേറെ അറിവ് അടിസ്ഥാനമുള്ള സിസ്റ്റങ്ങൾ നിർമ്മിക്കുക
5. [ഉപകരണങ്ങൾ](04-tools/README.md) - ബാഹ്യ ഉപകരണങ്ങളും ലളിതവുമായ അസിസ്റ്റന്റുകൾ സംയോജിപ്പിക്കുക
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP)ക്കും ഏജൻസിക്ക് മോട്യൂളുകൾക്കും പ്രവർത്തിക്കുക
---

## പഠന മാർഗ്ഗരേഖ

> **ക്വിക്ക് സ്റ്റാർട്ട്**

1. ഈ റിപ്പോസിറ്ററി നിങ്ങളുടെ GitHub അക്കൗണ്ടിലേക്ക് ഫോർക്ക് ചെയ്യുക
2. **Code** → **Codespaces** ടാബിൽ → **...** → **New with options...** ക്ലിക്ക് ചെയ്യുക
3. ഡിഫോൾട്ടുകൾ ഉപയോഗിക്കുക – ഇത് ഈ കോഴ്‌സിന് നിർമ്മിച്ച ഡെവലപ്പ്മെന്റ് കണ്ടെയ്‌നർ തെരഞ്ഞടുക്കും
4. **Create codespace** ക്ലിക്ക് ചെയ്യുക
5. പരിതസ്ഥിതി സജ്ജമാകാൻ 5-10 മിനിറ്റ് കാത്തിരിക്കുക
6. തുടങ്ങാൻ നേരിട്ട് [ക്വിക്ക് സ്റ്റാർട്ടിലേക്ക്](./00-quick-start/README.md) പോവുക!

മൊഡ്യൂളുകൾ പൂർത്തിയാക്കിയതിന് ശേഷം LangChain4j ടെസ്റ്റിംഗ് ആശയങ്ങളെ പരിചയപ്പെടാൻ [Testing Guide](docs/TESTING.md) പരിശോധിക്കുക.

> **കുറിപ്പ്:** ഈ പരിശീലനം GitHub മോഡലുകളും Azure OpenAI വും ഉപയോഗിക്കുന്നു. [ക്വിക്ക് സ്റ്റാർട്ട്](00-quick-start/README.md) മോഡ്യൂൾ GitHub മോഡലുകൾ ഉപയോഗിക്കുന്നു (Azure സബ്സ്ക്രിപ്ഷൻ ആവശ്യമില്ല), എന്നാൽ 1-5 മൊഡ്യൂളുകൾ Azure OpenAI ഉപയോഗിക്കുന്നു. നിങ്ങൾക്കൊരു അക്കൗണ്ട് ഇല്ലെങ്കിൽ [FREE Azure account](https://aka.ms/azure-free-account) ഉപയോഗിച്ച് തുടക്കം ചെയ്യുക.


## GitHub Copilot ഉപയോഗിച്ച് പഠിക്കൽ

അടിയന്തരമായി കോഡിംഗ് തുടങ്ങാൻ, GitHub Codespaceൽ അല്ലെങ്കിൽ നൽകപ്പെട്ട devcontainer അടക്കം നിങ്ങളുടെ പ്രാദേശിക IDE-യിൽ ഈ പ്രോജക്ട് തുറക്കൂ. ഈ കോഴ്‌സിൽ ഉപയോഗിക്കുന്ന devcontainer GitHub Copilot പഠനസഹായി പ്രോഗ്രാമിങ്ങിനായി മുൻകൂട്ടി ക്രമീകരിച്ചിരിക്കുന്നു.

ഓരോ കോഡ് ഉദാഹരണത്തിലും GitHub Copilot-നെ ചോദിച്ച് കൂടുതൽ വിശദമായി പഠിക്കാൻ കഴിയും ಪ್ರಶ್ನകൾ ഉണ്ട്. 💡/🤖 പ്രോംപ്റ്റുകൾ ഈ സ്ഥലങ്ങളിൽ കാണാം:

- **ജാവ ഫയൽ ഹെഡറുകൾ** - ഓരോ ഉദാഹരണത്തിനും പ്രത്യേകം ചോദ്യങ്ങൾ
- **മൊഡ്യൂൾ READMEകൾ** - കോഡ് ഉദാഹരണങ്ങൾക്കുശേഷം അന്വേഷിക്കൽ പ്രോംപ്റ്റുകൾ

**എങ്ങനെ ഉപയോഗിക്കാം:** ഏതെങ്കിലും കോഡ് ഫയൽ തുറക്കുകയും Copilot ന് ആ പ്രശ്‌നങ്ങളിൽ കൂടി ചോദിക്കുകയും ചെയ്യുക. ഇതിന് കോഡ്‌ബേസ് പൂർണ്ണമായും മനസിലാകുന്നു, വിശദീകരിക്കാം, വിപുലീകരിക്കാം, മറുവക നിങ്ങൾക്ക് നിർദ്ദേശങ്ങൾ നൽകാം.

കൂടുതൽ അറിയാൻ: [Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI) പരിശോധിക്കുക.


## അധിക സ്രോതസ്സുകൾ

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

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
 
### കോപിലോട്ട് പരമ്പര  
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)  
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)  
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)  
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## സഹായം നേടുക

എഐ ആപ്പുകൾ നിർമ്മിക്കുമ്പോൾ നിങ്ങൾക്ക് തടസ്സമുണ്ടായാൽ അല്ലെങ്കിൽ ഏതെങ്കിലും ചോദ്യങ്ങളുണ്ടെങ്കിൽ, ചേരുക:  

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

ഉൽപ്പന്ന പ്രതികരണം ചെയ്യുകയോ കൊണ്ടുപോകുമ്പോൾ പിഴവുകൾ ഉണ്ടായാൽ സന്ദർശിക്കുക:  

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## ലൈസൻസ്

MIT ലൈസൻസ് - വിശദീകരണങ്ങൾക്ക് [LICENSE](../../LICENSE) ഫയൽ കാണുക.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**എൽപ്പിക്കൽ**:  
ഈ രേഖ [കോ-ഓപ് ട്രാൻസ്ലേറ്റർ](https://github.com/Azure/co-op-translator) എന്ന എ.ഐ. പരിഭാഷാ സേവനം ഉപയോഗിച്ച് വിവർത്തനം ചെയ്യപ്പെട്ടിരിക്കുന്നു. ഞങ്ങൾ ശരിയായ വിവർത്തനത്തിനായി പരിശ്രമിക്കുന്നప్పഴും, ഓട്ടോമേറ്റഡ് വിവർത്തനത്തിൽ പിശകുകളും കൃത്യതാ ക്ഷാമവും ഉണ്ടായിരിക്കാവുന്നതായാണ് ശ്രദ്ധിക്കുക. മാതൃഭാഷയിൽ ഉള്ള അസൽ രേഖ അവകാശസമ്പന്നമായ ഉറവിടമെന്ന രീതിയിൽ കണക്കാക്കണം. നിർണായക വിവരങ്ങൾക്ക് വിദഗ്ധ മാനവ വിവർത്തനം ശുപാർശ ചെയ്യപ്പെടുന്നു. ഈ വിവർത്തനം ഉപയോഗിക്കുന്നതിൽ നിന്നുള്ള തെറ്റായ ബോധം അല്ലെങ്കിൽ തെറ്റായ വ്യാഖ്യാനങ്ങൾക്ക 우리는 ഉത്തരവായിരിക്കാൻ കഴിയില്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->