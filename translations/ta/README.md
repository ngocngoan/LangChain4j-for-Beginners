<img src="../../translated_images/ta/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 பன்மொழி ஆதரவு

#### GitHub செயல்திறன் மூலம் ஆதரிக்கப்படுகிறது (தானாகவும் எப்போதும் புதுப்பிக்கப்படும்)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](./README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **இடதுபாதியாக ஒட்ட விரும்புகிறீர்களா?**

> இக்கோப்பகம் 50+ மொழித் ترجமைகளை கொள்ளுகிறது, இது இறக்குமதி அளவை மிக்க அதிகரிக்கிறது. மொழிபெயர்ப்பு கருதி இல்லாமல் ஒட்ட, sparse checkout பயன்படுத்தவும்:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> இது இந்த பாடத்தை முடிக்க தேவையான அனைத்தையும் மிகவும் விரைவான இறக்குமதியுடன் வழங்கும்.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4j துவக்கக்காரர்களுக்கானது

LangChain4j மற்றும் Azure OpenAI GPT-5 உடன் AI பயன்பாடுகளை கட்டமைக்க ஒரு பாடநெறி, அடிப்படையான உரையாடலிலிருந்து AI முகவர்களுக்குள்.

**LangChain4j புதியவரா?** முக்கிய பதிகள் மற்றும் கருத்துக்களுக்கு [அகராதி](docs/GLOSSARY.md) பார்வையிடவும்.

## உள்ளடக்க பட்டியல்

1. [த്വரித தொடக்கம்](00-quick-start/README.md) - LangChain4j மூலம் துவங்குங்கள்  
2. [அறிமுகம்](01-introduction/README.md) - LangChain4j அடிப்படைகளை கற்றுக்கொள்ளவும்  
3. [உரையாடல் திட்டமிடல்](02-prompt-engineering/README.md) - விளைவை உண்டாக்கும் உரையாடல் வடிவமைப்பை உடைத்து கொள்ளவும்  
4. [RAG (திரும்பிச் சேர்க்கை-உள்ளடக்க உருவாக்கம்)](03-rag/README.md) - அறிவார்ந்த அறிவு அமைப்புகளை கட்டமைக்க  
5. [கருவிகள்](04-tools/README.md) - வெளிப்புற கருவிகள் மற்றும் சுலப உதவியாளர்களை இணைக்கவும்  
6. [MCP (மாதிரி சூழல் நெறிமுறை)](05-mcp/README.md) - மாதிரி சூழல் நெறிமுறை (MCP) மற்றும் முகவரித்திறன் தொகுதிகளுடன் பணியாற்றவும்  
---

## கற்றல் பாதை

> **த்வரித தொடக்கம்**

1. இந்தகோப்பகத்தை உங்கள் GitHub கணக்கிற்கு fork செய்யவும்  
2. **Code** → **Codespaces** தாவலை கிளிக் செய்யவும் → **...** → **New with options...**  
3. இயல்புகளைப் பயன்படுத்தவும் – இது இந்தப் பாடத்திற்கு உருவாக்கப்பட்ட Development container ஐத் தேர்ந்தெடுக்கும்  
4. **Create codespace** ஐ கிளிக் செய்யவும்  
5. சூழல் தயார் ஆக 5-10 நிமிடங்கள் காத்திருக்கவும்  
6. துவங்க [த்வரித தொடக்கம்](./00-quick-start/README.md) க்கு நேராக செல்!

 தொகுதிகளை முடித்த பின், LangChain4j சோதனைக்கான கருத்துக்களை செயல்படுத்த [சோதனை வழிகாட்டி](docs/TESTING.md) காணவும்.

> **குறிப்பு:** இந்த பயிற்சி GitHub மாதிரிகள் மற்றும் Azure OpenAI இரண்டையும் பயன்படுத்துகிறது. [த்வரித தொடக்கம்](00-quick-start/README.md) மாதிரி GitHub மாதிரிகளை பயன்படுத்துகிறது (Azure சந்தா தேவை இல்லை), ஆனால் 1-5 தொகுதிகள் Azure OpenAI பயன்படுத்துகின்றன. நீங்கள் Azure கணக்கு இல்லையெனில், [இலவச Azure கணக்கு](https://aka.ms/azure-free-account) கொண்டு துவங்குங்கள்.


## GitHub Copilot உடன் கற்றல்

விரைந்து குறியீடு எழுத தொடங்க, இந்த திட்டத்தை GitHub Codespace இல் அல்லது வழங்கப்பட்ட devcontainer உடன் உங்கள் உள்ளூர் IDE இல் திறக்கவும். இந்த பாடத்தில் பயன்படுத்தப்பட்ட devcontainer க்கு AI இணைந்து நிரல்படுத்த GitHub Copilot முன்கூட்டியே அமைக்கப்பட்டுள்ளது.

ஒவ்வொரு குறியீடு உதாரணத்திலும் உங்கள் புரிதலை ஆழமாக்க GitHub Copilot-க்கு கேட்கக்கூடிய பரிந்துரைக்கப்பட்ட கேள்விகள் உள்ளன. 💡/🤖 ஸ்வரம் பார்க்கவும்:

- **ஜாவா கோப்பு தலைப்புகள்** - ஒவ்வொரு உதாரணத்திற்குமான கேள்விகள்  
- **தொகுதி READMEகள்** - குறியீடு உதாரணங்களுக்கு பின் ஆராய்ச்சி வினா

**எப்படி பயன்படுத்துவது:** எந்த கோப்பும் திறந்து பரிந்துரைக்கப்பட்ட கேள்விகளை Copilot-க்கு கேளுங்கள். அது குறியீடுக் களஞ்சியத்தின் முழு சூழலைக் கொண்டுள்ளது மற்றும் விளக்க, விரிவாக்க, மாற்று பரிந்துரைகள் செய்யலாம்.

மேலும் அறிய விரும்புகிறீர்களா? [AI இணைந்து நிரல்படுத்தலுக்கு Copilot](https://aka.ms/GitHubCopilotAI) பார்க்கவும்.


## கூடுதல் வளங்கள்

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / முகவர்கள்
[![AZD for Beginners](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI for Beginners](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP for Beginners](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents for Beginners](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### உருவாக்கும் AI தொடர்
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### மையக் கற்பிதல்
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### கோபைலட் தொடர்
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## உதவி பெறுதல்

AI செயலிகளை உருவாக்குவதில் சிக்கல் படினால் அல்லது ஏதேனும் கேள்விகள் இருந்தால், இணையுங்கள்:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

உற்பத்தி கருத்து அல்லது பிழைகள் இருந்தால், கீழே செல்லவும்:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## உரிமம்

MIT உரிமம் - விரிவுகளுக்காக [LICENSE](../../LICENSE) கோப்பை பார்க்கவும்.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**கூறுதல்**:  
இந்த ஆவணம் AI மொழிபெயர்ப்பு சேவை [Co-op Translator](https://github.com/Azure/co-op-translator) மூலம் மொழிபெயர்க்கப்பட்டுள்ளது. நாங்கள் துல்லியத்திற்காக முயலுகின்றனர் என்றாலும், தானியங்கி மொழிபெயர்ப்புகளில் சில தவறுகள் அல்லது தவறான தகவல்கள் இருக்க வாய்ப்பு உள்ளது. அசல் ஆவணம் அதன் சொந்த மொழியில் அதிகாரப்பூர்வ ஆதாரமாக கருதப்பட வேண்டும். முக்கியமான தகவல்களுக்கு, தொழில்முறை மனித மொழிபெயர்ப்பை பரிந்துரைக்கிறோம். இந்த மொழிபெயர்ப்பை பயன்படுத்தியதன் மூலம் ஏற்பட்ட எதையும் நாங்கள் பொறுப்பேற்கவுள்ளதில்லை.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->