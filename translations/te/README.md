<img src="../../translated_images/te/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# ప్రారంభకులకు LangChain4j

ప్రాథమిక చాట్ నుండి AI ఏజెంట్ల వరకు, LangChain4j మరియు Azure OpenAI GPT-5.2 తో AI అనువర్తనాలు నిర్మించడానికి కోర్సు.

### 🌐 బహుభాషా మద్దతు

#### GitHub యాక్షన్ ద్వారా మద్దతు (ఆటోమేటెడ్ & ఎప్పటికప్పుడు నవీకరించబడుతుంది)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](./README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **స్థానికంగా క్లోన్ చేయడం ఇష్టమా?**
>
> ఈ రిపాజిటరీలో 50+ భాషల అనువాదాలు ఉన్నాయి, అవి డౌన్‌లోడ్ పరిమాణాన్ని గణనీయంగా పెంచతాయి. అనువాదాలు లేకుండా క్లోన్ చేయడానికి, స్పార్స్ చెకౌట్ ఉపయోగించండి:
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
> ఇది ఈ కోర్సును పూర్తి చేయడానికి కావలసిన ప్రతి అంశాన్ని మీకు త్వరగా డౌన్‌లోడ్ చేయగలదు.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## విషయ సూచిక

1. [త్వరిత ప్రారంభం](00-quick-start/README.md) - LangChain4j తో ప్రారంభించండి
2. [పరిచయం](01-introduction/README.md) - LangChain4j యొక్క ప్రాథమికాలను తెలుసుకోండి
3. [ప్రాంప్ట్ ఇంజనీరింగ్](02-prompt-engineering/README.md) - సమర్థవంతమైన ప్రాంప్ట్ డిజైన్ నేర్చుకోండి
4. [RAG (రిట్రీవల్-ఆగుమెంటెడ్ జెనరేషన్)](03-rag/README.md) - తెలివైన జ్ఞాన ఆధారిత వ్యవస్థలు నిర్మించండి
5. [సాధనాలు](04-tools/README.md) - బాహ్య సాధనాలు మరియు సులభమైన సహాయకులను ఏకం చేయండి
6. [MCP (మోడల్ కాంటెక్ట్ ప్రోటోకాల్)](05-mcp/README.md) - మోడల్ కాంటెక్ట్ ప్రోటోకాల్ (MCP) మరియు ఏజెంటిక్ మాడ్యూల్‌లతో పని చేయండి
---

## శిక్షణ మార్గం

**LangChain4j కొత్తవారు?** కీలక పదాలు మరియు భావనల నిర్వచనాల కోసం [Glossary](docs/GLOSSARY.md) ను చూడండి.

> **త్వరిత ప్రారంభం**

1. ఈ రిపాజిటరీని మీ GitHub ఖాతాకు ఫోర్క్ చేయండి
2. **Code** → **Codespaces** ట్యాబ్ → **...** → **New with options...** పై క్లిక్ చేయండి
3. డిఫాల్ట్ ఎంపికలను ఉపయోగించండి – ఇది ఈ కోర్సు కోసం రూపొందించిన Development container ను ఎంచుకుంటుంది
4. **Create codespace** పై క్లిక్ చేయండి
5. వాతావరణం సిద్ధం అయ్యే వరకు 5-10 నిమిషాలు వేచి ఉండండి
6. వెంటనే [త్వరిత ప్రారంభం](./00-quick-start/README.md) కు వెళ్లి ప్రారంభించండి!

మాడ్యూల్స్ పూర్తి చేసిన తర్వాత, LangChain4j పరీక్షా కాన్సెప్ట్‌లను ప్రభావవంతంగా చూడటానికి [Testing Guide](docs/TESTING.md) ను అన్వేషించండి.

> **గమనిక:** ఈ శిక్షణలో GitHub Models మరియు Azure OpenAI రెండింటిని ఉపయోగిస్తారు. [త్వరిత ప్రారంభం](00-quick-start/README.md) మాడ్యూల్ GitHub Models (Azure సబ్స్క్రిప్షన్ అవసరం లేదు) ఉపయోగిస్తుంది, కానీ మాడ్యూల్‌లు 1-5 Azure OpenAI ఉపయోగిస్తాయి. మీకు అకౌంట్ లేనిదైతే, [ఉచిత Azure ఖాతా](https://aka.ms/azure-free-account) తో ప్రారంభించండి.


## GitHub Copilot తో మెరుగైన నేర్చుకోవడం

త్వరగతి కోడింగ్ కోసం, ఈ ప్రాజెక్ట్‌ను GitHub Codespace లేదా స్థానిక IDE లో devcontainer తో తెరవండి. ఈ కోర్సులో ఉపయోగించిన devcontainer GitHub Copilot తో ముందుగా కాన్ఫిగర్ చేయబడింది, ఇది AI జోడి ప్రోగ్రామింగ్ కోసం.

ప్రతి కోడ్ ఉదాహరణలో GitHub Copilot కి మీరు అడగాల్సిన సూచించిన ప్రశ్నలు ఉంటాయి, అవి మీ అర్ధంకు లోతుగా సహాయపడతాయి. 💡/🤖 సూచనల కోసం వీక్షించండి:

- **జావా ఫైల్ హెడ్డర్‌లు** - ప్రతి ఉదాహరణకు ప్రత్యేక ప్రశ్నలు
- **మాడ్యూల్ READMEలు** - కోడ్ ఉదాహరణల అనంతరం అన్వేషణ ప్రశ్నలు

**వినియోగ విధానం:** ఏదైనా కోడ్ ఫైల్ తెరచి సూచించిన ప్రశ్నలను Copilot కి అడగండి. దీనికి కోడ్‌బేస్ పూర్తి సందర్భం తెలుసు మరియు వివరణ, విస్తరణ, ప్రత్యామ్నాయాలు సూచించగలదు.

మరింత తెలుసుకోవాలంటే, [AI జోడి ప్రోగ్రామింగ్ కోసం Copilot](https://aka.ms/GitHubCopilotAI) ను చూడండి.


## అదనపు వనరులు

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
 
### కాపైలట్ సిరీస్
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## సహాయం పొందడం

AI యాప్స్ నిర్మించడంలో మీరు గందరగోళంలో పడితే లేదా ఎటువంటి ప్రశ్నలు ఉన్నాయంటే, చేరండి:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

మీకు ఉత్పత్తి అభిప్రాయం లేదా నిర్మాణ సమయంలో లోపాలు ఉంటే సందర్శించండి:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## లైసెన్స్

MIT లైసెన్స్ - వివరాలకు [LICENSE](../../LICENSE) ఫైల్ చూడండి.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**వేచ్ఛనార్థం**:  
ఈ దస్త్రం AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడింది. మనం సరి అయిన అనువాదానికి ప్రయత్నించినప్పటికీ, ఆటోమేటెడ్ అనువాదాల్లో పొరపాట్లు లేదా తప్పుదారులుండవచ్చు అని గమనించండి. అసలు భాషలో ఉన్న డాక్యుమెంట్‌ను అధికారిక మూలంగా పరిగణించాలి. ముఖ్యమైన సమాచారానికి, ప్రొఫెషనల్ మానవ అనువాదం చేయించుకోవడం మంచిది. ఈ అనువాదం వాడుకునే కారణంగా ఏర్పడే ఏవైనా అవగాహన లోపాలు లేదా తప్పుదారులు కోసం మేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->