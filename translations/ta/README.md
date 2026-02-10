<img src="../../translated_images/ta/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 பன்மொழி ஆதரவு

#### GitHub Action மூலம் ஆதரவு (தானாகவும் எப்போதும் புதுப்பித்தலும்)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](./README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **உள்ளகமாக கிளோன் செய்வதைக் விரும்புகிறீர்களா?**

> இந்த களஞ்சியம் 50+ மொழி மொழிபெயர்ப்புகளை உள்ளடக்கியதால் பதிவிறக்கம் அளவு மிகவும் அதிகரிக்கும். மொழிபெயர்ப்புகளைத் தவிர்த்து கிளோன் செய்ய sparse checkout பயன்படுத்தவும்:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> இதன் மூலம் நீங்கள் இந்த பாடத்திட்டத்தை முடிக்க தேவையான அனைத்தையும் மிக விரைவான பதிவிறக்கம் ஒன்றில் பெற முடியும்.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4j ஆரம்பக்கருத்துகளுக்கு

LangChain4j மற்றும் Azure OpenAI GPT-5.2 உடன் AI பயன்பாடுகளை உருவாக்குவதற்கான ஒரு பாடநெறி, அடிப்படையான சந்திப்பிலிருந்து AI முகவர்களுக்குள்.

**LangChain4j இல் புதிதா?** முக்கியக் கருத்துக்களுக்கும் வகைகளுக்கும் விளக்கங்களுக்காக [பொருளாதாரம்](docs/GLOSSARY.md) பார்க்கவும்.

## உள்ளடக்க அட்டவணை

1. [விரைவு ஆரம்பம்](00-quick-start/README.md) - LangChain4j உடன் துவங்குங்கள்
2. [அறிமுகம்](01-introduction/README.md) - LangChain4j அடிப்படைகளை கற்றுக் கொள்ளுங்கள்
3. [பிராம்ட் பொறியியல்](02-prompt-engineering/README.md) - விளைவாத பிராம்ட் வடிவமைப்பைப் பிரதிபலிக்கவும்
4. [RAG (திருத்த கூட்டப்பட்ட உருவாக்கம்)](03-rag/README.md) - அறிவாற்றல் அடிப்படையிலான அறிவுக் கணக்குகளை உருவாக்கவும்
5. [கருவிகள்](04-tools/README.md) - வெளிப்புற கருவிகள் மற்றும் எளிய உதவியாளர்களுடன் ஒருங்கிணைப்பு
6. [MCP (மாதிரி சூழல் நெறிமுறை)](05-mcp/README.md) - மாதிரி சூழல் நெறிமுறை (MCP) மற்றும் முகவர்கள் மூலம் பணியாற்றுதல்
---

## கற்றல் பாதை

> **விரைவு தொடக்கம்**

1. இந்த களஞ்சியத்தை உங்கள் GitHub கணக்கிற்கு Fork செய்யவும்
2. **Code** → **Codespaces** தாவலை கிளிக் செய்யவும் → **...** → **New with options...** என்பதை தேர்ந்தெடுக்கவும்
3. இயல்புநிலைகளைப் பயன்படுத்தவும் – இது இந்த பாடத்திட்டத்துக்கான Developer container ஐத் தேர்ந்தெடுக்கிறது
4. **Create codespace** கிளிக் செய்யவும்
5. சூழலை தயாராக 5-10 நிமிடங்கள் காத்திருக்கவும்
6. துவங்க [விரைவு தொடக்கம்](./00-quick-start/README.md) க்கு நேரடியாக சென்று தொடங்குங்கள்!

பாடங்களை முடித்த பின் LangChain4j சோதனை கருத்துக்களை விளக்கக் காண [சோதனை வழிகாட்டி](docs/TESTING.md) பாடத்தை ஆராயவும்.

> **குறிப்பு:** இந்த பயிற்சி GitHub மாதிரிகளையும் Azure OpenAI-யையும் இரண்டும் பயன்படுத்துகிறது. [விரைவு தொடக்கம்](00-quick-start/README.md) முன்னாள் GitHub மாதிரிகளை பயன்படுத்துகிறது (Azure சந்தா தேவை இல்லை), ஆனால் 1-5 பாடங்கள் Azure OpenAI வைப் பயன்படுத்துகின்றன. நீங்கள் ஒரு [இலவச Azure கணக்கு](https://aka.ms/azure-free-account) இல் தொடங்கிக் கொள்ளலாம்.

## GitHub Copilot உடன் கற்றல்

வேகமாகக் குறியீடு செய்ய இந்தத் திட்டத்தை GitHub Codespace-இல் அல்லது உங்கள் உள்ளூர் IDE-இல் devcontainer உடன் திறக்கவும். இந்த devcontainer GitHub Copilot AI இணைக்கப்பட்ட கூட்டுப் programming க்கான முன்னமைக்கப்பட்ட அமைப்பை கொண்டுள்ளது.

ஒவ்வொரு குறியீடு எடுத்துக்காட்டிலும் GitHub Copilot-ஐ கேட்க ஏதுவாக பரிந்துரைக்கப்பட்ட கேள்விகள் உள்ளன. அவற்றை கீழ்காணும் இடங்களில் 💡/🤖 என்ற சுட்டிகளுடன் காணலாம்:

- **ஜாவா கோப்பு தலைப்புகள்** - ஒவ்வொரு எடுத்துக்காட்டுக்கும் தனிப்பட்ட கேள்விகள்
- **பாடம் READMEகள்** - குறியீடு எடுத்துக்காட்டுக்குப் பிறகு ஆய்வு மேலாண்மை

**எப்படி பயன்படுத்துவது:** எந்தவும் குறியீடு கோப்பையும் திறந்து பரிந்துரைக்கப்பட்ட கேள்விகளை Copilot-க்கு கேளுங்கள். இதில் முழு குறியீடு பொருள் context உள்ளது; விளக்க, விரிவாக்கம் மற்றும் மாற்றுச் சுட்டிகள் பரிந்துரைக்க முடியும்.

மேலும் அறிய விரும்புகிறீர்களா? [AI இணைந்து Programming க்கான Copilot](https://aka.ms/GitHubCopilotAI) ஐ பார்க்கவும்.


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
 
### Core Learning
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![ஆரம்பக்காரர்களுக்கான IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![ஆரம்பக்காரர்களுக்கான XR மேம்பாடு](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### கோபைலட் தொடர்
[![AI இணைத்துக் கொள்கையுடன் கோபைலட்](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET க்கான கோபைலட்](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![கோபைலட் சாகசம்](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## உதவி பெறுதல்

உங்கள் AI செயலிகளை உருவாக்குவதில் தடுமாற்றம் ஏற்படினால் அல்லது கேள்விகள் இருந்தால், பங்கேற்கவும்:

[![அசுரே AI ஃபவுண்ட்ரி டிஸ்கார்டு](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

உதயமாக்கும் போது உங்களிடம் தயாரிப்பு பின்னூட்டம் அல்லது பிழைகள் இருந்தால், பார்க்கவும்:

[![அசுரே AI ஃபவுண்ட்ரி டெவலப்பர் ஃபோரம்](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## உரிமம்

MIT உரிமம் - விவரங்களுக்கு [LICENSE](../../LICENSE) கோப்பை பார்வையிடவும்.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**குறிப்புரை**:  
இந்த ஆவணம் AI மொழி மாற்று சேவையான [Co-op Translator](https://github.com/Azure/co-op-translator) பயன்படுத்தி மொழிமாற்றம் செய்யப்பட்டுள்ளது. நாங்கள் துல்லியத்திற்காக முயற்சித்தாலும், தானியங்கி மொழி மாற்றங்களில் தவறுகள் அல்லது துல்லியமற்ற பகுதிகள் இருக்கலாம் என்பதை கவனிக்கவும். உரிய மொழியில் உள்ள அசல் ஆவணம் முதன்மையான மூலம் ஆகும் என்று கருதிக்கொள்ள வேண்டும். முக்கியமான தகவல்களுக்கு, தொழில்முறை மனித மொழி மாற்றத்தை பரிந்துரைக்கிறோம். இந்த மொழி மாற்றத்தைப் பயன்படுத்துவதால் நிகழும் எந்தவொரு தவறான புரிதல் அல்லது தவறான விளக்கங்களுக்கும் நாங்கள் பொறுப்பாக இருப்பதாக இல்லை.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->