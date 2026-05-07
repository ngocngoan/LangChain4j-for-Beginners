<img src="../../translated_images/te/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j ప్రారంభికులకు

LangChain4j మరియు Azure OpenAI GPT-5.2 తో AI అనువర్తనాలు నిర్మించడానికి ఒక కోర్స్, ప్రాథమిక చాట్ నుండి AI ఏజెంట్లు వరకు.

### 🌐 బహుభాషా మద్దతు

#### GitHub Action ద్వారా మద్దతు (స్వయంచాలక & ఎప్పటికప్పుడు అప్డేట్)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Khmer](../km/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](./README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **స్థానికంగా క్లోన్ చేయాలనుకుంటున్నారా?**
>
> ఈ రిపొజిటరీ 50+ భాషల అనువాదాలను కలిగి ఉంది, ఇది డౌన్లోడ్ పరిమాణాన్ని గణనీయంగా పెంచుతుంది. అనువాదాలు లేకుండా క్లోన్ చేయడానికి sparse checkout ఉపయోగించండి:
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
> ఇది కోర్సును పూర్తి చేయడానికి అవసరమైన అన్ని విషయాలను త్వరగా డౌన్లోడ్ చేసుకోగలుగుతుంది.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## కంటెంట్ పట్టిక

1. [త్వరిత ఆరంభం](00-quick-start/README.md) - LangChain4j తో ప్రారంభించండి
2. [పరిచయం](01-introduction/README.md) - LangChain4j యొక్క మూలబోధనలను తెలుసుకోండి
3. [ప్రాంప్ట్ ఇంజనీరింగ్](02-prompt-engineering/README.md) - సమర్థవంతమైన ప్రాంప్ట్ రూపకల్పనలో నైపుణ్యం పొందండి
4. [RAG (రిట్రీవల్-ఆగ్మెంటెడ్ జనరేషన్)](03-rag/README.md) - అర్థవంతమైన జ్ఞానాధారిత సిస్టమ్‌లను నిర్మించండి
5. [టూల్స్](04-tools/README.md) - బయటి పనిముట్లు మరియు సులభ సహాయకులను అనుసంఖ్యాయించండి
6. [MCP (మోడల్ కాన్‌టెక్స్ట్ ప్రోటోకాల్)](05-mcp/README.md) - మోడల్ కాన్‌టెక్స్ట్ ప్రోటోకాల్ (MCP) మరియు ఏజెంటిక్ మాడ్యూల్‌లతో పనిచేయండి

### వీడియో వాక్‌త్రోలు

ప్రతి మాడ్యూల్‌కు అనుసంధాన లైవ్ సెషన్ ఉంటుంది, అక్కడ మేము ఆలోచనలను మరియు కోడ్‌ను దశలవారీగా ట్రావర్స్ చేస్తాము.

| మాడ్యూల్ | వీడియో |
|--------|-------|
| 01 - పరిచయం | [LangChain4j తో ప్రారంభించడం](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - ప్రాంప్ట్ ఇంజనీరింగ్ | [LangChain4j తో ప్రాంప్ట్ ఇంజనీరింగ్](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [LangChain4j తో RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - టూల్స్ & 05 - MCP | [టూల్స్ మరియు MCP తో AI ఏజెంట్లు](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

##  నేర్చుకునే మార్గం

**LangChain4j కొత్తవురా?** ముఖ్య పదాలు మరియు భావాలను నిర్వచించడానికి [Glossary](docs/GLOSSARY.md) చూడండి.

> **త్వరిత ఆరంభం**

1. ఈ రిపోజిటరీని మీ GitHub ఖాతాకు Fork చేయండి
2. **Code** → **Codespaces** టాబ్ → **...** → **New with options...** క్లిక్ చేయండి
3. డిఫాల్ట్ ఎంపికలను ఉపయోగించండి – ఇది ఈ కోర్సు కోసం సృష్టించబడిన డెవలప్‌మెంట్ కంటైనర్‌ని ఎంచుకుంటుంది
4. **Create codespace** క్లిక్ చేయండి
5. వాతావరణం సిద్ధంగా అయ్యేందుకు 5-10 నిమిషాలు వేచి ఉండండి
6. ప్రారంభించడానికి నేరుగా [త్వరిత ఆరంభం](./00-quick-start/README.md)కి వెళ్లండి!

మాడ్యూల్‌లు పూర్తి చేసిన తరువాత, LangChain4j టెస్టింగ్ భావాలను కార్యంలో చూడటానికి [Testing Guide](docs/TESTING.md)ని పరిశీలించండి.

> **గమనిక:** ఈ శిక్షణ GitHub Models మరియు Azure OpenAI రెండింటినీ ఉపయోగిస్తుంది. [త్వరిత ఆరంభం](00-quick-start/README.md) మాడ్యూల్ GitHub Models ను ఉపయోగిస్తుంది (Azure సబ్‌స్క్రిప్షన్ అవసరం లేదు), మిగతా 1-5 మాడ్యూల్స్ Azure OpenAI ను ఉపయోగిస్తాయి. మీ వద్ద లేని వారు [ఉచిత Azure ఖాతా](https://aka.ms/azure-free-account) తో ప్రారంభించండి.


## GitHub Copilot తో నేర్చుకోవడం

త్వరగా కోడింగ్ ప్రారంభించడానికి, ఈ ప్రాజెక్టును GitHub Codespace లో లేదా మీ స్థానిక IDE లో ఇవ్వబడిన devcontainer తో తెరవండి. ఈ కోర్సులో ఉపయోగించబడుతున్న devcontainer GitHub Copilot తో AI పేయర్డ్ ప్రోగ్రామింగ్ కోసం ముందుగానే కాన్ఫిగర్ చేయబడింది.

ప్రతి కోడ్ ఉదాహరణలో GitHub Copilot ను అడగవచ్చు, అవి మీ అవగాహన ను మరింత లోతుగా చేయడానికి సూచించిన ప్రశ్నలు ఉంటాయి. 💡/🤖 సూచనలు ఈ క్రింది చోట్ల ఉంటాయి:

- **జావా ఫైల్ హెడ్డర్లు** - ప్రతి ఉదాహరణకు ప్రత్యేకమైన ప్రశ్నలు
- **మాడ్యూల్ READMEలు** - కోడ్ ఉదాహరణల తర్వాత అన్వేషణా సూచనలు

**ఎలా ఉపయోగించాలి:** ఏ కోడ్ ఫైల్ ను తెరువు మరియు Copilot కు సూచించిన ప్రశ్నలు అడుగు. దీనికి పూర్తి కోడ్ బేస్ సూత్రాలు తెలుసు, ఇది వివరణ ఇవ్వగలదు, పొడిగించగలదు, మరియు ప్రత్యామ్నాయాల సూచనలు చేయగలదు.

ఇంకా నేర్చుకోవాలనుకుంటున్నారా? [AI పేయర్డ్ ప్రోగ్రామింగ్ కోసం Copilot](https://aka.ms/GitHubCopilotAI) చూడండి.


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
 
### కోపైలట్ సిరీస్
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## సహాయం పొందడం

మీరు చిక్కుకున్నట్లయితే లేదా AI యాప్‌లను నిర్మించడంలో ఏవైనా ప్రశ్నలు ఉంటే, చేరండి:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

మీకు ఉత్పత్తి అభిప్రాయం లేదా నిర్మాణంలో లోపాలు ఉంటే సందర్శించండి:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## లైసెన్స్

MIT లైసెన్స్ - వివరాల కోసం [LICENSE](../../LICENSE) ఫైల్ చూడండి.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**నిరసన**:  
ఈ డాక్యుమెంట్‌ను AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడింది. మేము ఖచ్చితత్వానికి ప్రయత్నించినప్పటికీ, ఆటోమేటెడ్ అనువాదాలలో పూర్వగ్రహాలు లేదా తప్పిదాలు ఉండవచ్చు అని దయచేసి గమనించండి. స్థానిక భాషలో ఉన్న అసలు డాక్యుమెంట్ ను ప్రామాణిక మాధ్యమంగా పరిగణించాలి. కీలకమైన సమాచారానికి, ప్రొఫెషనల్ మానవ అనువాదం సిఫార్సుచేయబడుతుంది. ఈ అనువాదం వాడకం వల్ల ఏర్పడిన ఏ ఇతరర్థం లేకపోవటం లేదా తప్పుదోవ పట్టటం కోసం మేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->