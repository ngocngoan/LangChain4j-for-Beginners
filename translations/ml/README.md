<img src="../../translated_images/ml/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j ആരംഭിക്കുന്നവർക്കുള്ള

LangChain4j ഉം Azure OpenAI GPT-5.2 ഉം ഉപയോഗിച്ച് അടിസ്ഥാന ചാറ്റിൽ നിന്നു എഐ ഏജന്റുകൾ വരെ എഐ ആപ്ലിക്കേഷനുകൾ നിർമ്മിക്കാൻ ഒരു കോഴ്സ്.

### 🌐 ബഹുഭാഷാ പിന്തുണ

#### GitHub Action മുഖേന പിന്തുണയുള്ളത് (സ്വയം പ്രവർത്തിക്കുന്നതും എപ്പോഴും പുതിയതും)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](./README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **നിങ്ങൾക്ക് ലോക്കലിൽ ക്ലോൺ ചെയ്യാൻ ആഗ്രഹമുണ്ടോ?**
>
> ഈ റെപോസിറ്ററിയിൽ 50+ ഭാഷാ പരിഭാഷകൾ ഉൾക്കൊള്ളുന്നുണ്ടെന്നതുൽ കാരണം ഡൗൺലോഡ് വലുപ്പം വളരെയധികം കൂടും. പരിഭാഷകളില്ലാതെ ക്ലോൺ ചെയ്യാൻ sparse checkout ഉപയോഗിക്കുക:
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
> ഇതിലൂടെ കോഴ്സ് പൂർത്തിയാക്കാൻ ആവശ്യമായ എല്ലാം വേഗത്തിൽ ഡൗൺലോഡ് ചെയ്യാം.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## ഉള്ളടക്കങ്ങളുടെ പട്ടിക

1. [ക്വിക്ക് തുടക്കം](00-quick-start/README.md) - LangChain4j ഉപയോഗിച്ച് ആരംഭിക്കുക
2. [പരിചയം](01-introduction/README.md) - LangChain4j അടിസ്ഥാനങ്ങൾ പഠിക്കുക
3. [പ്രോംപ്റ്റ് എഞ്ചിനീയറിംഗ്](02-prompt-engineering/README.md) - ഫലപ്രദമായ പ്രോംപ്റ്റ് രൂപകൽപ്പനയിൽ പ്രാവീണ്യം നേടുക
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - ബുദ്ധিমানായ അറിവ് അടിസ്ഥാനമാക്കിയ സിസ്റ്റങ്ങൾ നിർമ്മിക്കുക
5. [പാക്കേജുകൾ](04-tools/README.md) - ബാഹ്യ ടൂളുകളും ലളിതമായ അസിസ്റ്റന്റ്സും സംയോജിപ്പിക്കുക
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP) ഉം ഏജന്റിക്模块ുകളും ഉപയോഗിക്കുക

### വീഡിയോ വാക്ക്ത്രൂകൾ

ഓരോ മോഡ്യൂളിനും ഒരു ലൈവ് സെഷൻ ഉണ്ട്, അവിടെ സങ്കല്പങ്ങളും കോഡും പടിപ്പടിയായി വിശദീകരിക്കുന്നു.

| Module | Video |
|--------|-------|
| 01 - Introduction | [LangChain4j ഉപയോഗിച്ച് തുടങ്ങൽ](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - Prompt Engineering | [LangChain4j ilə പ്രോംപ്റ്റ് എഞ്ചിനീയറിംഗ്](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [LangChain4j తో RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - Tools & 05 - MCP | [ടൂളുകളും MCP ഉം ഉള്ള AI ഏജന്റുകൾ](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## പഠന പാത

**LangChain4j പുതിയവരാണോ?** പ്രധാന പദങ്ങളും ആശയങ്ങളും വ്യാഖ്യാനിക്കാൻ [Glossary](docs/GLOSSARY.md) കാണുക.

> **ക്വിക്ക് സ്റ്റാർട്ട്**

1. ഈ റെപ്പോസിറ്ററി നിങ്ങളുടെ GitHub അക്കൗണ്ടിലേക്ക് Fork ചെയ്യുക
2. **Code** → **Codespaces** ടാബ് → **...** → **New with options...** ക്ലിക്ക് ചെയ്യുക
3. ഡിഫോൾട്ടുകൾ ഉപയോഗിക്കുക – കോഴ്സിനായി സൃഷ്ടിച്ച Development container തിരഞ്ഞെടുക്കും
4. **Create codespace** ക്ലിക്ക് ചെയ്യുക
5. പരിസ്ഥിതി സജ്ജമാകാൻ 5-10 മിനിറ്റ് കാത്തിരിക്കുക
6. തുടങ്ങാൻ [Quick Start](./00-quick-start/README.md) ൽ നേരിട്ട് പോകുക!

മോഡ്യൂളുകൾ പൂർത്തിയാക്കിയ ശേഷം, LangChain4j ടെസ്റ്റിംഗ് ആശയങ്ങൾ പ്രവർത്തനത്തിൽ കാണാൻ [Testing Guide](docs/TESTING.md) ഉപയോഗിക്കുക.

> **കുറിപ്പുഃ** ഈ പരിശീലനം GitHub Models ഉം Azure OpenAI ഉം രണ്ടും ഉപയോഗിക്കുന്നു. [Quick Start](00-quick-start/README.md) മോഡ്യൂൾ GitHub Models (Azure സബ്സ്ക്രിപ്ഷൻ ആവശ്യമില്ല) ആണ് ഉപയോഗിക്കുന്നത്, എന്നാൽ 1-5 മോഡ്യൂളുകൾ Azure OpenAI ഉപയോഗിക്കുന്നു. നിങ്ങൾക്ക് ഒരു സൗജന്യ Azure അക്കൗണ്ട് ഇല്ലെങ്കിൽ [FREE Azure account](https://aka.ms/azure-free-account) ഉപയോഗിച്ച് തുടങ്ങാം.


## GitHub Copilot ഉപയോഗിച്ച് പഠിക്കുക

കൂടുതൽ വേഗത്തിൽ കോഡിംഗ് ആരംഭിക്കാൻ, ഈ പ്രോജക്ട് GitHub Codespace-ൽ അല്ലെങ്കിൽ ലൊക്കൽ IDE-യിൽ നൽകിയ devcontainer ഉപയോഗിച്ച് തുറക്കുക. ഈ കോഴ്സിൽ ഉപയോഗിച്ച devcontainer GitHub Copilot AI ജോഡിക്കുള്ള പ്രോഗ്രാമിംഗിന് മുൻകൂട്ടി ക്രമീകരിച്ചിട്ടുള്ളതാണ്.

ഓരോ കോഡ് ഉദാഹരണത്തിനും GitHub Copilot-നോട് ചോദിക്കാൻ താല്പര്യമുള്ള ചോദ്യങ്ങളും ഉണ്ട്, അതിലൂടെ നിങ്ങളുടെ ബോധ്യമുയർത്താം. 💡/🤖 അടയാളങ്ങൾ നോക്കുക:

- **ജാവ ഫയൽ ഹെഡറുകൾ** - ഉദാഹരണങ്ങൾക്ക് അനുയോജ്യമായ ചോദ്യങ്ങൾ
- **മോഡ്യൂൾ README-കൾ** - കോഡ് ഉദാഹരണങ്ങൾക്ക് ശേഷം പരീക്ഷണ ചോദ്യങ്ങൾ

**ഉപയോഗിക്കാനുള്ള മാർഗം:** ഏതെങ്കിലും കോഡ് ഫയൽ തുറന്ന് നിർദ്ദേശിച്ച ചോദ്യങ്ങൾ Copilot-നോട് ചോദിക്കുക. കോഡ്ബേസിന്റെ മുഴുവൻ പ്രാധാന്യവും understanding-ഉം ഉണ്ട്, വിശദീകരിക്കാം, വിപുലീകരിക്കാം, മറ്റ് വകവെക്കലുകൾ നിർദ്ദേശിക്കാം.

കൂടുതൽ അറിയാൻ [Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI) കാണുക.


## അധിക ഉറവിടങ്ങൾ

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
[![സൈബർസെക്യൂരിറ്റി ഫോർ ബിഗിനേഴ്‌സ്](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![വെബ് ഡെവ് ഫോർ ബിഗിനേഴ്‌സ്](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![ഐഒടി ഫോർ ബിഗിനേഴ്‌സ്](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![എക്സ്‌ആർ ഡെവലപ്മെന്റ് ഫോർ ബിഗിനേഴ്‌സ്](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### കോപൈലറ്റ് സീരിസ്
[![AI പെയർഡ് പ്രോഗ്രാമിംഗിനായി കോപൈലറ്റ്](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET-നായി കോപൈലറ്റ്](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![കോപൈലറ്റ് അഡ്വഞ്ചർ](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## സഹായം നേടുക

AI ആപ്പുകൾ നിർമ്മിക്കുന്നതിൽ നിങ്ങൾക്ക് തടസ്സമുണ്ടാകുകയോ എന്തെങ്കിലുമൊരു ചോദ്യം ഉണ്ടായിരിയ്ക്കുകയോ ചെയ്താൽ ചേർക്കുക:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

ഉൽപ്പന്നം സംബന്ധിച്ച ഫീഡ്ബാക്ക് നൽകാനോ എറററുകൾ ഒഴിവാക്കാനോ ആഗ്രഹിച്ചാൽ സന്ദർശിക്കുക:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## ലൈസൻസ്

MIT ലൈസൻസ് - വിശദാംശങ്ങൾക്ക് [LICENSE](../../LICENSE) ഫയൽ കാണുക.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**അസാധുസൂചന**:
ഈ ദസ്താവേജം AI തർജ്ജുമാ സേവനം [Co-op Translator](https://github.com/Azure/co-op-translator) ഉപയോഗിച്ച് തർജ്ജമ ചെയ്തതാണ്. ഞങ്ങൾ കൃത്യതയ്ക്ക് ശ്രമിക്കുന്നുവെങ്കിലും, സ്വയം പ്രവർത്തിക്കുന്ന തർജ്ജുമകളിൽ പിശക് അല്ലെങ്കിൽ അശുദ്ധികൾ ഉണ്ടാകാനുള്ള സാധ്യതയുണ്ട്. അതിനാൽ, പ്രാഥമിക ഭാഷയിലെ പ്രമാണം അതിന്റെ ഔദ്യോഗിക ഉറവിടമായി കണക്കാക്കപ്പെടണം. ഗഹനമായ വിവരങ്ങൾക്ക്, പ്രൊഫഷണൽ മനുഷ്യ തർജ്ജുമ നിർദ്ദേശിക്കുന്നു. ഈ തർജുമയുടെ ഉപയോഗത്തിലെ തെറ്റിദ്ധാരണകൾക്കോ തെറ്റായ പങ്കുവെപ്പുകൾക്കോ ഞങ്ങൾ ഉത്തരവാദിത്തമില്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->