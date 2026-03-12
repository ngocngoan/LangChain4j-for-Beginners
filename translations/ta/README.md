<img src="../../translated_images/ta/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j ஆரம்பத்திற்கான பாடநெறி

LangChain4j மற்றும் Azure OpenAI GPT-5.2 உடன் AI செயலிகளை உருவாக்கும் கோர்ஸ், அடிப்படையான உரையாடல் முதல் AI முகவரிகள் வரை.

### 🌐 பல மொழி ஆதரவு

#### GitHub செயல்பாட்டின் மூலம் ஆதரவு (தானாகவும் எப்போதும் புதுப்பிக்கப்பட்டதுமானது)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](./README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **உள்ளூரில் கிளோன் செய்வது விரும்புமா?**
>
> இந்த ரெப்போசிடரியில் 50+ மொழி மொழிபெயர்ப்புகள் உள்ளன, இது பதிவிறக்கம் அளவைக் கூடுதலாக உயர்த்துகிறது. மொழிபெயர்ப்புகள் இல்லாமல் கிளோன் செய்ய sparse checkout பயன்படுத்தவும்:
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
> இதனால் இந்த பாடநெறியை முடிக்க விரைவான பதிவிறக்கம் கிடைக்கும்.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## உள்ளடக்க அட்டவணை

1. [விரைவான துவக்கம்](00-quick-start/README.md) - LangChain4j உடன் துவங்கு
2. [அறிமுகம்](01-introduction/README.md) - LangChain4j அடிப்படைகளை கற்றுக்கொள்ளுங்கள்
3. [ப்ராம்ட் இன்ஜினியரிங்](02-prompt-engineering/README.md) - பயன்பாட்டு ப்ராம்ட் வடிவமைப்பை கற்றுக்கொள்ளுங்கள்
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - அறிவுடைய அறிவு அடிப்படையிலான அமைப்புகளை உருவாக்குங்கள்
5. [கருவிகள்](04-tools/README.md) - வெளி கருவிகள் மற்றும் எளிய உதவியாளர்களை இணைக்கவும்
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP) மற்றும் முகவரிக் கூறுகளுடன் பணிபுரியவும்

### வீடியோ வழிகாட்டல்கள்

ஒவ்வொரு மொடியூலும் நேரலை அமர்வுடன் கூடியது, மாதிரியாக கருத்துக்கள் மற்றும் குறியீட்டை படிப்படியாக எடுத்துக்காட்டுகிறது.

| மொடியூல் | வீடியோ |
|--------|-------|
| 01 - அறிமுகம் | [LangChain4j உடன் துவக்கம்](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - ப்ராம்ட் இன்ஜினியரிங் | [LangChain4j உடன் ப்ராம்ட் இன்ஜினியரிங்](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [LangChain4j உடன் RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - கருவிகள் & 05 - MCP | [கருவிகளும் MCP-வுமாக AI முகவரிகள்](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

##  கற்றல் பாதை

**LangChain4j-க்கு புதியவரா?** முக்கிய காட்சிகள் மற்றும் கருத்துக்கள் விளக்கங்கள் தேற [அகரவணக்கம்](docs/GLOSSARY.md) பார்க்கவும்.

> **விரைவான துவக்கம்**

1. உங்கள் GitHub கணக்கில் இந்த ரெப்போசிடரியை Fork செய்யவும்
2. **Code** → **Codespaces** தாவலை கிளிக் செய்யவும் → **...** → **New with options...**
3. இயல்புநிலைகளை பயன்படுத்தவும் – இது இந்த பாடநெறிக்காக உருவாக்கப்பட்ட டெவலப்ப்மென்ட் கன்டெய்நரை தேர்வுசெய்யும்
4. **Create codespace** கிளிக் செய்யவும்
5. சூழல் தயாராக 5-10 நிமிடங்கள் காத்திருக்கவும்
6. துவங்க [விரைவான துவக்கம்](./00-quick-start/README.md) சென்று தொடங்குங்கள்!

மொடியூல்களை முடித்த பிறகு, [சோதனை வழிகாட்டி](docs/TESTING.md) மூலம் LangChain4j சோதனைக் கருத்துக்களைச் செயல்படுத்தி பாருங்கள்.

> **குறிப்பு:** இந்த பயிற்சி GitHub மின்னியல்களும் Azure OpenAI-யும் இரண்டும் பயன்படுத்துகிறது. [விரைவான துவக்கம்](00-quick-start/README.md) மொடியூல் GitHub மின்னியல்களைப் பயன்படுத்துகிறது (Azure சந்தா தேவை இல்லை), ஆனால் 1-5 மொடியூல்கள் Azure OpenAI-யைப் பயன்படுத்துகின்றன. ஒரு [இலவச Azure கணக்கு](https://aka.ms/azure-free-account) இல் துவங்கவும் இல்லையெனில்.


## GitHub Copilot உடன் கற்றல்

விரைவாக குறியீடு எழுத துவங்க, இந்த திட்டத்தை GitHub Codespace அல்லது உங்கள் உள்ளூரில் IDEயில் devcontainer உடன் திறக்கவும். இந்த பாடநெறியில் பயன்படுத்தப்படும் devcontainer GitHub Copilot ஐ AI கூட்டுறவு நிரலாக்கத்துக்காக முன்கூட்டியே அமைக்கப்பட்டுள்ளது.

ஒவ்வொரு குறியீடு உதாரணத்திலும் GitHub Copilot-இல் கேட்கக்கூடிய பரிந்துரைக்கப்பட்ட கேள்விகள் உள்ளன. இது உங்கள் புரிதலை மேம்படுத்த உதவும். 💡/🤖 குறியீடு உள்ள இடங்களில் பார்த்து:

- **Java கோப்பு தலைப்புகளில்** - ஒவ்வொரு உதாரணத்திற்கும் தனிச்சிப்பாபமான கேள்விகள்
- **மொடியூல் README-களில்** - குறியீடு உதாரணத்துக்குப் பிறகு ஆராய்ச்சி கேட்டுகள்

**எப்படி பயன்படுத்துவது:** எந்த குறியீடு கோப்பும் திறந்து Copilot பரிந்துரைக்கப்படும் கேள்விகளை கேளுங்கள். இது இடைமுக அடிப்படையை முழுமையாக அறிந்து, விளக்கம், விரிவாக்கம் மற்றும் மாற்று பரிந்துரைகளைச் செய்கிறது.

மேலும் கற்றுக்கொள்ள விரும்புகிறீர்களா? [AI கூட்டுறவு நிரலாக்கத்திற்கான Copilot](https://aka.ms/GitHubCopilotAI) பார்க்கவும்.


## கூடுதல் மூலங்கள்

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
 
### அடிப்படை கற்றல்
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![புதியவர்களுக்கு இணைய பாதுகாப்பு](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![புதியவர்களுக்கு வலை மேம்பாடு](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![புதியவர்களுக்கு ஐஓடி](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![புதியவர்களுக்கு XR மேம்பாடு](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### கோபைலட் தொடர்
[![கோபைலட் AI ஜோடி நிரலாக்கத்திற்கு](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NETக்கு கோபைலட்](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![கோபைலட் சாகசம்](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## உதவி பெறுதல்

AI செயலிகளை உருவாக்கும் போது சிக்கலில் சிக்கினால் அல்லது ஏதேனும் கேள்விகள் இருந்தால், சேரவும்:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

தயாரிப்புக்கு தொடர்பான கருத்துக்கள் அல்லது பிழைகள் இருந்தால், சந்தையுங்கள்:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## உரிமம்

MIT உரிமம் - விரிவிற்குத் [LICENSE](../../LICENSE) கோப்பை காண்க.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**எச்சரிக்கை**:  
இந்த ஆவணம் [Co-op Translator](https://github.com/Azure/co-op-translator) என்ற எஐ மொழி மாற்ற சேவையை பயன்படுத்தி மொழிபெயர்க்கப்பட்டுள்ளது. துல்லியத்திற்காக நாம் முயற்சிக்கின்றாலும், தானாக செய்யப்பட்ட மொழிபெயர்ப்புகளில் பிழைகள் அல்லது தவறுகள் இருக்க வாய்ப்பு உள்ளது என்பதை கவனத்தில் கொள்ளவும். அசல் ஆவணம் அதன் உள்ளூர் மொழியில் அதிகாரபூர்வ மூலமாக கருதப்பட வேண்டும். முக்கிய தகவல்களுக்கு, தொழில்முறையில் மனித மொழிபெயர்ப்பை பரிந்துரைக்கிறோம். இந்த மொழிபெயர்ப்பின் பயன்பாட்டினால் ஏற்பட்ட எந்த தவறறிதல்கள் அல்லது தவறுகளுக்கும் நாம் பொறுப்பில்லாது உள்ளோம்.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->