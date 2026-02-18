<img src="../../translated_images/ta/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j புதியவர்களுக்காக

LangChain4j மற்றும் Azure OpenAI GPT-5.2 பயன்படுத்தி AI பயன்பாடுகளை உருவாக்க ஒரு பாடநெறி, அடிப்படையான உரையாடலிலிருந்து AI முகவர்களுக்கு வரை.

### 🌐 பன்மொழி ஆதரவு

#### GitHub செயல் மூலம் ஆதரவு (தானாகவும் எப்போதும் புதுப்பிக்கப்பட்டும்)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](./README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **உடன் உள்ளடக்கம் உள்ளூர் நகலெடுக்க விரும்புகிறாயா?**
>
> இந்த விடுதி 50+ மொழி மொழிபெயர்ப்புகளை உள்ளடக்கியிருப்பதால் பதிவிறக்கம் அளவை பெரிதாக அதிகரிக்கிறது. மொழிபெயர்ப்புகள் இல்லாமல் நகல் எடுக்க sparse checkout பயன்படுத்தவும்:
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
> இதனால் பாடநெறியை முடிக்க விரைவான பதிவிறக்கம் உடன் தேவையான அனைத்தும் கிடைக்கும்.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## உள்ளடக்கக் கட்டமைப்பு

1. [விரைவான தொடக்கம்](00-quick-start/README.md) - LangChain4j உடன் தொடங்குங்கள்
2. [அறிமுகம்](01-introduction/README.md) - LangChain4j அடிப்படைகளை கற்றுக்கொள்ளுங்கள்
3. [Prompt Engineering](02-prompt-engineering/README.md) - விளைவான முன்மொழிவை உருவாக்க கற்றுக்கொள்ளுங்கள்
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - நுண்ணறிவு சார்ந்த அறிவு அமைப்புகளை உருவாக்குங்கள்
5. [கருவிகள்](04-tools/README.md) - புற கருவிகள் மற்றும் எளிய உதவியாளர்களை ஒருங்கிணைக்கவும்
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP) மற்றும் Agentic பகுதிகளுடன் பணியுங்கள்
---

## கற்றல் பாதை

**LangChain4j புதியவரா?** முக்கிய நிபந்தனைகள் மற்றும் கருத்துக்களை விளக்கும் [Glossary](docs/GLOSSARY.md) பார்க்கவும்.

> **விரைவான தொடக்கம்**

1. இந்த களஞ்சியத்தை உங்கள் GitHub கணக்குக்கு காப்பி எடுக்கவும்
2. **Code** → **Codespaces** தாவலை கிளிக் செய்யவும் → **...** → **New with options...** தேர்வு செய்யவும்
3. இயல்புநிலைகளைப் பயன்படுத்தவும் – இது இந்த பாடநெறிக்காக உருவாக்கப்பட்ட Development container ஐ தேர்ந்தெடுக்கும்
4. **Create codespace** கிளிக் செய்யவும்
5. சூழலை இயங்க தயாராக 5-10 நிமிடங்கள் காத்திருங்கள்
6. தொடங்க [Quick Start](./00-quick-start/README.md) பக்கத்திற்கு நேராக செல்லுங்கள்!

பகுதிகள் முடிந்த பின், [Testing Guide](docs/TESTING.md) அழுத்தி LangChain4j சோதனை கருத்துக்களை காணவும்.

> **குறிப்பு:** இந்த பயிற்சி GitHub Models மற்றும் Azure OpenAI இரண்டையும் பயன்படுத்துகிறது. [Quick Start](00-quick-start/README.md) பகுதி GitHub Models பயன்படுத்துகிறது (Azure சந்தா தேவையில்லை), ஆனால் 1-5 பகுதிகள் Azure OpenAI பயன்படுத்தும். இல்லையெனில் [இலவச Azure கணக்கு](https://aka.ms/azure-free-account) கொண்டு தொடங்கவும்.


## GitHub Copilot உடன் கற்றல்

விரைவாக குறியீட்டையைத் தொடங்க, இந்த திட்டத்தை GitHub Codespace அல்லது வழங்கப்பட்ட devcontainer கொண்ட உள்ளூர் IDE இல் திறக்கவும். இந்த பாடநெறியில் பயன்படுத்தப்படும் devcontainer GitHub Copilot உடன் நிர்வகிக்கப்பட்டு உள்ளது, இது AI இணைக்கப்பட்ட நிரல் எழுதுதலில் உதவும்.

ஒவ்வொரு குறியீட்டு எடுத்துக்காட்டிலும் GitHub Copilot ஐ கேள்விகள் மூலம் உங்கள் புரிதல் ஆழமாகும். 💡/🤖 குறியீட்டில்:

- **Java கோப்பு தலைப்புகளில்** - ஒவ்வொரு எடுத்துக்காட்டிற்கான குறிப்பிட்ட கேள்விகள்
- **பகுதி READMEகளில்** - குறியீட்டு எடுத்துக்காட்டுகளுக்குப் பிறகு பின்னூட்ட கேள்விகள்

**பயன்பாடு:** எந்தக் குறியீடு கோப்பையும் திறந்து Copilot ஐ பரிந்துரைக்கும் கேள்விகளை கேளுங்கள். இது குறியீட்டு அடிப்படையின் முழு சூழலை அறிந்திருக்கும் மற்றும் விளக்கமளிக்கவும், விரிவுபடுத்தவும், மாற்று பரிந்துரைகள் செய்யவும் முடியும்.

மேலும் கற்றுக்கொள்ள விரும்புகிறீர்களா? [AI இணைக்கப்பட்ட நிரல் எழுதலுக்கான Copilot](https://aka.ms/GitHubCopilotAI) பார்க்கவும்.


## கூடுதல் வளங்கள்

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
 
### முக்கிய கற்றல்
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![ஆДУணட்டியாளர்களுக்கான IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![ஆДУணட்டியாளர்களுக்கான XR Development](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---

### நயவஞ்சகர் தொடர்
[![AI இணைந்த பணிகள் திட்டத்துக்கான Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET க்கான Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot சாகசம்](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## உதவி பெறுதல்

AI ஆப்கள் உருவாக்குதல் பற்றிய சந்தேகங்கள் அல்லது சிக்கல்கள் ஏற்பட்டால், இணைக:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

தயாரிப்புக்கான பின்னூட்டங்கள் அல்லது கட்டுமான தவறுகள் இருந்தால், செல்:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

##Parin License

MIT உரிமம் - விபரங்களுக்கு [LICENSE](../../LICENSE) கோப்பை பார்க்கவும்.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ஒத்துழைப்பு**:  
இந்த ஆவணம் AI மொழி மாற்ற சேவை [Co-op Translator](https://github.com/Azure/co-op-translator) மூலம் மொழி மாற்றப்பட்டுள்ளது. நாம் துல்லியத்திற்காக முயல்கிறோம் எனினும், இயந்திர மொழி மாற்றங்களில் பிழைகள் அல்லது தவறுகள் இருக்கக்கூடும் என்பதை தயவுசெய்து கருத்தில் கொள்ளவும். அச்சு மொழியில் உள்ள மூல ஆவணம் அதிகாரப்பூர்வமான மூலமாகக் கருதப்பட வேண்டும். முக்கியமான தகவலுக்கு, தொழில்முறை மனித மொழி மாற்றத்தை பரிந்துரைக்கின்றோம். இந்த மொழி மாற்றத்தின் பயன்பாட்டினால் ஏற்பட்ட எந்தவொரு தவறான புரிதல்கள் அல்லது பொருள்மாற்றங்களுக்கும் நாங்கள் பொறுப்பற்றவர்கள்.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->