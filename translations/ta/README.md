<img src="../../translated_images/ta/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# புதியவர்களுக்கான LangChain4j

தொடக்க அளவிலான உரையாடலிலிருந்து AI முகவர்கள் வரை LangChain4j மற்றும் Azure OpenAI GPT-5.2 உடன் AI பயன்பாடுகளை உருவாக்கும் பாடநெறி.

### 🌐 பன்மொழி ஆதரவு

#### GitHub Action மூலம் ஆதரிக்கப்படுகிறது (தானியங்கிய மற்றும் எப்போதும் עדכון)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[ஆரபிக்](../ar/README.md) | [பெங்காலி](../bn/README.md) | [பல்கேரியன்](../bg/README.md) | [பெருமியின் (மியான்மார்)](../my/README.md) | [சீன மொழி (சுருங்கப்பட்ட)](../zh-CN/README.md) | [சீன மொழி (சம்பிரதாய, ஹாங்காங்)](../zh-HK/README.md) | [சீன மொழி (சம்பிரதாய, மக்காவ்)](../zh-MO/README.md) | [சீன மொழி (சம்பிரதாய, தைவான்)](../zh-TW/README.md) | [குரோஷியன்](../hr/README.md) | [செக்](../cs/README.md) | [டேனிஷ்](../da/README.md) | [டச்சு](../nl/README.md) | [எஸ்டோனியன்](../et/README.md) | [பின்னிஷ்](../fi/README.md) | [பிரெஞ்சு](../fr/README.md) | [ஜெர்மன்](../de/README.md) | [கிரேக்கம்](../el/README.md) | [ஹீப்ரு](../he/README.md) | [ஹிந்தி](../hi/README.md) | [ஹங்கேரியன்](../hu/README.md) | [இந்தோனேஷியன்](../id/README.md) | [இத்தாலியன்](../it/README.md) | [ஜப்பானீஸ்](../ja/README.md) | [கன்னடம்](../kn/README.md) | [க்வெமர்](../km/README.md) | [கொரியன்](../ko/README.md) | [லிதுவேனியன்](../lt/README.md) | [மலாய்](../ms/README.md) | [மலையாளம்](../ml/README.md) | [மராத்தி](../mr/README.md) | [நெபாளி](../ne/README.md) | [நைஜீரியன் பிட்ஜின்](../pcm/README.md) | [நோர்வேஜியன்](../no/README.md) | [பெர்ஷியன் (ஃபார்சி)](../fa/README.md) | [போலீஷ்](../pl/README.md) | [போர்ச்சுகீஸ் (பிரேசில்)](../pt-BR/README.md) | [போர்ச்சுகீஸ் (போர்ச்சுகல்)](../pt-PT/README.md) | [பஞ்சாபி (குருமுகி)](../pa/README.md) | [ரோமானியன்](../ro/README.md) | [ரஷ்யன்](../ru/README.md) | [செர்பியன் (சிரிலிக்)](../sr/README.md) | [ஸ்லோவாக்](../sk/README.md) | [ஸ்லோவேனியன்](../sl/README.md) | [ஸ்பானிஷ்](../es/README.md) | [ஸ்வாஹிலி](../sw/README.md) | [ஸ்வீடிஷ்](../sv/README.md) | [தகாலொக் (பிலிப்பினோ)](../tl/README.md) | [தமிழ்](./README.md) | [தெலுங்கு](../te/README.md) | [தை](../th/README.md) | [துருக்கி](../tr/README.md) | [உக்ரைனியன்](../uk/README.md) | [உருது](../ur/README.md) | [வியேட்னாமீஸ்](../vi/README.md)

> **உள்ளூர் கிளோன் செய்ய விரும்புகிறீர்களா?**
>
> இந்த களஞ்சியம் 50+ மொழி மொழிபெயர்ப்புகள் அடங்கியுள்ளதனால் பதிவிறக்கம் அளவு பெரிதாகும். மொழிபெயர்ப்புகள் இல்லாமல் கிளோன் செய்ய விரும்பினால் sparse checkout-ஐப் பயன்படுத்துங்கள்:
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
> இது படிப்பை முடிக்க தேவையான அனைத்தையும் விரைவாக தருகிறது.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## உள்ளடக்க அட்டவணம்

1. [விரைவான தொடக்கம்](00-quick-start/README.md) - LangChain4j உடன் துவங்குங்கள்
2. [அறிமுகம்](01-introduction/README.md) - LangChain4j அடிப்படைகளை கற்றுக்கொள்ளுங்கள்
3. [உத்தரவு பொறியியலும்](02-prompt-engineering/README.md) - பயனுள்ள உத்தரவுகள் வடிவமைப்பை ஆதிக்கமாக கொள்ளுங்கள்
4. [RAG (திருத்துக்கூடிய-மொழிமாற்றம் உருவாக்குதல்)](03-rag/README.md) - புத்திரமான அறிவுத்தளம் அமைப்புகளை கட்டியெழுப்புங்கள்
5. [கருவிகள்](04-tools/README.md) - வெளிப்புற கருவிகள் மற்றும் எளிய உதவியாளர்களை இணைத்தல்
6. [MCP (மாதிரி சூழல் நெறிமுறை)](05-mcp/README.md) - மாதிரி சூழல் நெறிமுறை மற்றும் முகவரி அலகுகளுடன் பணியாற்றுதல்

### வீடியோ நடைமுறை

ஒவ்வொரு அத்தியாயத்துக்கும் தொடர்புடைய நேரலை அமர்வுகள் உள்ளன, அங்கு நாங்கள் படிகள் படி கருத்துக்களையும் குறியீட்டையும் விவரிக்கிறோம்.

| அத்தியாயம் | வீடியோ |
|--------|-------|
| 01 - அறிமுகம் | [LangChain4j உடன் துவங்குதல்](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - உத்தரவு பொறியியல் | [LangChain4j உடன் உத்தரவு பொறியியல்](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [LangChain4j உடன் RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - கருவிகள் & 05 - MCP | [கருவிகள் மற்றும் MCP உடன் AI முகவர்கள்](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

##  கற்றல் பாதை

**LangChain4j-க்கு புதியவரா?** முக்கியச் சொற்கள் மற்றும் கருத்துகளுக்கு [பதிவு பட்டியல்](docs/GLOSSARY.md) ஐப் பாருங்கள்.

> **விரைவான தொடக்கம்**

1. இந்த களஞ்சியத்தை உங்கள் GitHub கணக்கில் Fork செய்யவும்
2. **Code** → **Codespaces** tab → **...** → **New with options...** என்பதைக் கிளிக் செய்யவும்
3. இயல்புகளை பயன்படுத்தவும் – இது இந்த பாடநெறிக்கான Development container-ஐத் தேர்ந்தெடுக்கும்
4. **Create codespace** என கிளிக் செய்யவும்
5. சூழல் தயாராகும் வரை 5-10 நிமிடங்கள் காத்திருக்கவும்
6. ஆரம்பிக்க [விரைவான தொடக்கம்](./00-quick-start/README.md) இணைப்பிற்கு நேரடியாக செல்லுங்கள்!

அத்தியாயங்களை முடித்த பிறகு, LangChain4j சோதனை கருத்துக்களை செயல்படுத்த [சோதனை வழிகாட்டி](docs/TESTING.md) ஐ ஆராயவும்.

> **குறிப்பு:** இந்த பயிற்சி GitHub மாதிரிகள் மற்றும் Azure OpenAI இரண்டையும் பயன்படுத்துகிறது. [விரைவான தொடக்கம்](00-quick-start/README.md) அத்தியாயம் GitHub மாதிரிகளைப் பயன்படுத்துகிறது (Azure சந்தா தேவையில்லை), ஆனால் 1-5 அத்தியாயங்கள் Azure OpenAI-ஐ பயன்படுத்துகின்றன. ஆதரவற்றவர்கள் [இலவச Azure கணக்குடன்](https://aka.ms/azure-free-account) துவங்குங்கள்.


## GitHub Copilot உடன் கற்று கொள்ளுதல்

குறியீடு எழுத விரைவாக ஆரம்பிக்க, இந்த திட்டத்தை GitHub Codespace-ல் அல்லது உங்கள் உள்ளூர் IDE-வில் devcontainer உடன் திறக்கவும். இந்தப் பாடநெறியில் பயன்படுத்தப்படும் devcontainer GitHub Copilot-இன் AI ஒத்துழைப்பு பணிக்கான முன் அமைப்பு கொண்டுள்ளது.

ஒவ்வொரு குறியீடு உதாரணத்திலும் GitHub Copilot-இல் கேட்கவும் கூடிய பரிந்துரைக்கப்பட்ட கேள்விகள் உளுள்ளது, இது உங்கள் புரிதலை ஆழப்படுத்த உதவும். கீழ்க்காணும் இடங்களில் 💡/🤖 குறியடிகள் காணப்படுகின்றன:

- **Java கோப்பு தலைப்புகளில்** - ஒவ்வொரு உதாரணத்திற்கான குறிப்பிட்ட கேள்விகள்
- **அத்தியாய READMEகளில்** - குறியீடு உதாரணங்களுக்குப் பிறகு ஆராய்ச்சி வழிகாட்டிகள்

**வழிமுறை:** எந்த குறியீடு கோப்பினையும் திறந்து Copilot-இல் பரிந்துரைக்கப்பட்ட கேள்விகளை கேளுங்கள். இது குறியீட்டு தளத்தின் முழு உள்ளடக்கம் தெரியும், விளக்கலாம், விரிவாக்கலாம் மற்றும் மாற்றுகளை பரிந்துரைக்கலாம்.

மேலும் அறிய விரும்புகிறீர்களா? [AI ஒத்துழைப்பு பணிக்கான Copilot](https://aka.ms/GitHubCopilotAI) ஐப் பாருங்கள்.


## கூடுதல் வளங்கள்

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![புதியவர்களுக்கான LangChain4j](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![புதியவர்களுக்கான LangChain.js](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![புதியவர்களுக்கான LangChain](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / முகவர்கள்
[![புதியவர்களுக்கான AZD](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![புதியவர்களுக்கான Edge AI](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![புதியவர்களுக்கான MCP](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![புதியவர்களுக்கான AI முகவர்கள்](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### உருவாக்கும் AI தொடர்வு
[![புதியவர்களுக்கான உருவாக்கும் AI](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![உருவாக்கும் AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![உருவாக்கும் AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![உருவாக்கும் AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### முக்கியக் கற்றல்
[![புதியவர்களுக்கான எம்எல்](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![புதியவர்களுக்கான தரவு அறிவியல்](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![புதியவர்களுக்கான AI](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![ஆரம்பத்தில் இருக்கும் நபர்களுக்கான கையுறவு பாதுகாப்பு](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![ஆரம்பத்தில் இருக்கும் நபர்களுக்கான வலை வளர்ச்சி](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![ஆரம்பத்தில் இருக்கும் நபர்களுக்கான IOT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![ஆரம்பத்தில் இருக்கும் நபர்களுக்கான XR வளர்ச்சி](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---

### கோபைலட் தொடர்
[![கோபைலட் AI கூட்டு நிரலாக்கத்திற்காக](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET க்கான கோபைலட்](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![கோபைலட் சாகசம்](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## உதவி பெறுதல்

நீங்கள் சிக்கலில் மிக்கிடமோ அல்லது AI பயன்பாடுகள் கட்டுமானம் பற்றி ஏதேனும் கேள்விகள் இருந்தால், சேர்ந்துகொள்ளவும்:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

பொருள் கருத்துக்கள் அல்லது பிழைகள் இருந்தால் கட்டுமானத்தின் போது பின்வரும் முகவரிக்கு வந்து பார்க்கவும்:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## உரிமம்

MIT உரிமம் - விவரங்களுக்கு [LICENSE](../../LICENSE) கோப்பைப் பார்க்கவும்.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**மேற்குறிப்பு**:  
இந்த ஆவணம் [Co-op Translator](https://github.com/Azure/co-op-translator) என்ற AI மொழிபெயர்ப்பு சேவையைப் பயன்படுத்தி மொழிபெயர்க்கப்பட்டுள்ளது. துல்லியத்திற்காக நாம் முயற்சிப்பதாக இருந்தாலும், தானாக மேற்கொள்ளப்படும் மொழிபெயர்ப்பு தவறுகள் அல்லது தவறான தகவல்களை கொண்டிருக்க வாய்ப்பு உள்ளதை தயவுசெய்து கவனிக்கவும். தாய்மொழியில் உள்ள அசல் ஆவணம் அதிகாரபூர்வ மூலமாக கருதப்பட வேண்டும். முக்கியமான தகவல்களுக்கு, தொழில்முறை மனித மொழிபெயர்ப்பை பரிந்துரைக்கப்படுகிறது. இந்த மொழிபெயர்ப்பின் பயன்படுத்தல் காரணமாக ஏற்பட்ட ஏதாவது தவறான புரிதல்கள் அல்லது தவறான விளக்கங்களுக்கு எங்களை பொறுப்பேற்க முடியாது.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->