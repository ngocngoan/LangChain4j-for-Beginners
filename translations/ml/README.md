<img src="../../translated_images/ml/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j ന്യൂബീസിനായി

മൂലീകരണ ചാറ്റിൽ നിന്ന് AI ഏജന്റുകളിലേക്കുള്ള LangChain4j ഉം Azure OpenAI GPT-5.2 ഉം ഉപയോഗിച്ച് AI അപ്ലിക്കേഷനുകൾ നിർമ്മിക്കാൻ ഒരു കോഴ്‌സ്.

### 🌐 ബഹുഭാഷാ പിന്തുണ

#### GitHub പ്രവർത്തനത്താൽ (സ്വയം ക്രമീകരിക്കപ്പെടുന്നും എപ്പോഴും പുതുക്കപ്പെടുന്നതും) പിന്തുണയ്ക്കുന്നു

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Khmer](../km/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](./README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **പ്രാദേശികമായി കോഡ് ക്ലോൺ ചെയ്യാനാഗ്രഹമുണ്ടോ?**
>
> ഈ റെപ്പോസിറ്ററിയിൽ 50-ത്തിലധികം ഭാഷാ വിവർത്തനങ്ങൾ ഉൾക്കൊള്ളുന്നു, ഇത് ഡൗൺലോഡ് വലിപ്പം വലിയവുമാക്കുന്നു. വിവർത്തനങ്ങൾ ഇല്ലാതെ ക്ലോൺ ചെയ്യാൻ sparse checkout ഉപയോഗിക്കുക:
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
> ഇതുവഴി നിങ്ങൾക്ക് കോഴ്‌സ് പൂര്‍ത്തിയാക്കുന്നതിന് ആവശ്യമായ എല്ലാ കാര്യങ്ങളും വളരെ വേഗത്തിൽ ഡൗൺലോഡ് ചെയ്യാം.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## ഉള്ളടക്കങ്ങളുടെ പട്ടിക

1. [ഷീഘ്രം ആരംഭിക്കുക](00-quick-start/README.md) - LangChain4j ഉപയോഗിച്ച് ആരംഭിക്കുക
2. [പരിചയം](01-introduction/README.md) - LangChain4j യുടെ അടിസ്ഥാനങ്ങൾ പഠിക്കുക
3. [പ്രോംപ്റ്റ് എഞ്ചിനീയറിംഗ്](02-prompt-engineering/README.md) - ഫലപ്രദമായ പ്രോംപ്റ്റ് രൂപകൽപ്പനയിൽ നിപുണത നേടുക
4. [RAG (റീട്രിവൽ-ഓഗ്മെന്റഡ് ജനറേഷൻ)](03-rag/README.md) - ബുദ്ധിമുട്ടുള്ള വിജ്ഞാന അധിഷ്ഠിത സിസ്റ്റങ്ങൾ നിർമ്മിക്കുക
5. [ടൂളുകൾ](04-tools/README.md) - ബാഹ്യ ഉപകരണങ്ങൾക്കും ലളിത സഹായി സംവിധാനങ്ങൾക്കും സംയോജിപ്പിക്കുക
6. [MCP (മോഡൽ കോൺടെക്സ്റ്റ് പ്രോട്ടോക്കോൾ)](05-mcp/README.md) - മോഡൽ കോൺടെക്സ്റ്റ് പ്രോട്ടോക്കോൾ (MCP) ഉം ഏജന്റിക് മൊഡ്യൂളുകളും ഉപയോഗിക്കുക

### വീഡിയോ മാർഗ്ഗനിർദ്ദേശങ്ങൾ

ഒരൊറ്റ മോട്യൂളിനും ലൈവ് സെഷൻ കൂടെ ഉണ്ട്, വളരെ വിശദമായി ആശയങ്ങളെയും കോഡിനെയും നാം പുറകോട്ട് ഉൾക്കാഴ്ച നടത്തുന്നു.

| മോട്യൂൾ | വീഡിയോ |
|--------|-------|
| 01 - പരിചയം | [LangChain4j ഉപയോഗിച്ച് സ്റ്റാർട്ട് ചെയ്യുന്നു](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - പ്രോംപ്റ്റ് എഞ്ചിനീയറിംഗ് | [LangChain4j ഉള്ള പ്രോംപ്റ്റ് എഞ്ചിനീയറിംഗ്](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [LangChain4j ഒപ്പം RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - ടൂളുകളും 05 - MCP | [ടൂളുകളും MCP ഉം ഉള്ള AI ഏജന്റുകൾ](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## പഠന പാത

**LangChain4j പ്രാരംഭങ്ങളോ?** പ്രധാന തുടങ്ങിയ പദങ്ങളും ആശയങ്ങളും മനസിലാക്കാൻ [Glossary](docs/GLOSSARY.md) കാണുക.

> **ഷീഘ്രം ആരംഭിക്കുക**

1. ഈ റെപ്പോസിറിയി നിങ്ങളുടെ GitHub അക്കൗണ്ടിലേക്കു ഫോർക്കുചെയ്യുക
2. **Code** → **Codespaces** ടാബ് → **...** → **New with options...** ക്ലിക് ചെയ്യുക
3. ഡിഫോൾട്ട് സജ്ജീകരണങ്ങൾ ഉപയോഗിക്കുക – ഇത് ഈ കോഴ്‌സിനായി സജ്ജീകരിച്ച ഡെവലപ്പ്മെന്റ് കോൺടെയ്നർ തിരഞ്ഞെടുക്കും
4. **Create codespace** ക്ലിക്ക് ചെയ്യുക
5. പരിസ്ഥിതി സജ്ജമാകാൻ 5-10 മിനിറ്റ് കാത്തിരിക്കുക
6. ആരംഭിക്കാൻ [ഷീഘ്രം ആരംഭിക്കുക](./00-quick-start/README.md) കാണുക!

മോട്യൂളുകൾ പൂർത്തിയാക്കിയ ശേഷം, LangChain4j ടസ്റ്റിങ് ആശയങ്ങൾ പ്രയോഗത്തിൽ കാണാൻ [Testing Guide](docs/TESTING.md) പരീക്ഷിക്കൂ.

> **കുറിപ്പ്:** ഈ പരിശീലനം GitHub മോഡലുകളും Azure OpenAI ഉം കൂട്ടിച്ച് ഉപയോഗിക്കുന്നു. [ഷീഘ്രം ആരംഭിക്കുക](00-quick-start/README.md) മോട്യൂളിൽ GitHub മോഡലുകൾ (Azure സബ്സ്ക്രിപ്ഷൻ ആവശ്യമില്ല) ആണ് ഉപയോഗിക്കുന്നത്, മോട്യൂളുകൾ 1-5 Azure OpenAI ഉപയോഗിക്കുന്നു. നിങ്ങൾക്ക് ഇല്ലെങ്കിൽ [നിയമിത Azure അക്കൗണ്ട്](https://aka.ms/azure-free-account) ഉപയോഗിച്ച് ആരംഭിക്കുക.


## GitHub Copilot ഉപയോഗിച്ച് പഠനം

ദ്രുതമായി കോഡിംഗ് തുടങ്ങാൻ, ഈ പ്രോജക്ട് GitHub Codespace ൽ അല്ലെങ്കിൽ നൽകിയ ഡെവ്‌കോൺടെയ്നർ ഉപയോഗിച്ച് നിങ്ങളുടെ ലോക്കൽ IDE യിൽ തുറക്കുക. ഈ കോഴ്‌സിലെ ഡെവ്‌കോൺടെയ്നർ GitHub Copilot ഉപയോഗിച്ച് AI കൂട്ടുപ്രോഗ്രാമിംഗിന് മുൻകൂട്ടി ക്രമീകരിച്ചിരിക്കുന്നതാണ്.

ഓരോ കോഡ് ഉദാഹരണത്തിലും GitHub Copilot യിൽ ചോദിക്കാവുന്ന നിർദ്ദേശങ്ങൾക്ക് ചോദ്യങ്ങളും ഉൾപ്പെട്ടിരിക്കുന്നു, അത് നിങ്ങളുടെ മനസ്സിലാക്കലിനെ അടിതെറിക്കാൻ സഹായിക്കും. 💡/🤖 പ്രോംപ്റ്റുകൾ താഴെ കണ്ടു:

- **ജാവാ ഫയൽ ഹെഡറുകൾ** - ഓരോ ഉദാഹരണത്തിന്റെയും പ്രത്യേക ചോദ്യങ്ങൾ
- **മൊഡ്യൂൾ READMEs** - കോഡ് ഉദാഹരണങ്ങളുടെയുശേഷം പരീക്ഷണ ചോദ്യങ്ങൾ

**ഉപയോഗวิതി:** ഏതെങ്കിലും കോഡ് ഫയൽ തുറന്ന് Copilot നു നിർദ്ദേശിച്ച ചോദ്യങ്ങൾ ചോദിക്കുക. കോഡ്ബേസ് യഥോചിതമായി മനസ്സിലാക്കാൻ കഴിയും, വിശദീകരിക്കുകയും, വിപുലീകരിക്കുകയും, ബദൽ നിർദ്ദേശങ്ങൾ നൽകുകയും ചെയ്യുന്നു.

കൂടുതൽ അറിയാൻ താൽപര്യമുണ്ടോ? [GitHub Copilot AI കൂട്ടുപ്രോഗ്രാമിംഗിനായി](https://aka.ms/GitHubCopilotAI) കാണുക.


## അധിക സ്രോതസ്സ്

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
 
### കോർ ലേണിംഗ്
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![സൈബർസെക്യൂരിറ്റി ഫോർ ബേഗിൻസേഴ്സ്](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![വേബ് ഡെവ് ഫോർ ബേഗിൻസേഴ്സ്](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![ഐഒടി ഫോർ ബേഗിൻസേഴ്സ്](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![എക്സ്‌ആർ ഡെവലപ്പ്മെന്റ് ഫോർ ബേഗിൻസേഴ്സ്](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---

### കോപൈലറ്റ് പരമ്പര  
[![എഐ കൂട്ടായ പ്രോഗ്രാമിങ്ങിനുള്ള കോപൈലറ്റ്](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)  
[![C#/.NET നുള്ള കോപൈലറ്റ്](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)  
[![കോപൈലറ്റ് അഡ്വഞ്ചർ](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)  
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## സഹായം നേടൽ

നിങ്ങൾ കുടുങ്ങുകയുണ്ടെങ്കിൽ അല്ലെങ്കിൽ എഐ ആപ്ലിക്കേഷനുകൾ നിർമ്മിക്കുമ്പോൾ ഏതെങ്കിലും ചോദ്യം ഉണ്ടെങ്കിൽ ചേരുക:  

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

ഉൽപ്പന്ന പ്രതികരണങ്ങൾ ഉണ്ടെങ്കിൽ അല്ലെങ്കിൽ നിർമ്മാണത്തിൽ പിഴവുകൾ ഉണ്ടെങ്കിൽ സന്ദർശിക്കുക:  

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## ലൈസൻസ്

MIT ലൈസൻസ് - വിശദാംശങ്ങൾക്ക് [LICENSE](../../LICENSE) ഫയൽ കാണുക.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**പ്രതിജ്ഞ**:  
ഈ പ്രമാണം AI പരിഭാഷാ സേവനം [Co-op Translator](https://github.com/Azure/co-op-translator) ഉപയോഗിച്ച് പരിഭാഷ ചെയ്തതാണ്. ഞങ്ങൾ കൃത്യതയ്ക്ക് ശ്രമിക്കുന്നതെങ്കിൽ, യാന്ത്രിക പരിഭാഷകൾ പിശകുകൾ അല്ലെങ്കിൽ അകാരണതകൾ ഉള്ളതായിരിക്കാമെന്നും ദയവായി ശ്രദ്ധിക്കുക. നിദ്ധീഷ്ട ഭാഷയിലെ യഥാർത്ഥ പ്രമാണം അതിന്റെ നിയമാനുസൃത ഉറവിടമായി പരിഗണിക്കപ്പെടണം. നിർണായക വിവരങ്ങൾക്ക്, വ്യവസായിക മനുഷ്യ പരിഭാഷ നിർദ്ദേശിക്കപ്പെടുന്നു. ഈ പരിഭാഷ ഉപയോഗിക്കുന്നതിൽ നിന്നുള്ള അസമജ്യം അല്ലെങ്കിൽ തെറ്റായ വ്യാഖ്യാനങ്ങൾക്ക് ഞങ്ങൾക്ക് ഉത്തരവാദിത്വം ഉണ്ടാകുന്നില്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->