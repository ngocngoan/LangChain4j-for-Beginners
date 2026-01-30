<img src="../../translated_images/my/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 ဘာသာစကားစုံ ထောက်ပံ့မှု

#### GitHub Action မှတဆင့် ထောက်ပံ့ထားသည် (အလိုအလျှောက်နှင့် အမြဲတမ်းအသစ်အဆန်း)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](./README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **ဒေသတွင်းကူးယူဖို့ နှစ်သက်ပါသလား။**

> ဒီ repository မှာ ဘာသာစကား ၅၀ ကျော် ဘာသာပြန်အကြောင်းအရာတွေ ပါဝင်တဲ့အတွက် ဒေါင်းလုပ်အရွယ်အစား တိုးလာပါတယ်။ ဘာသာပြန်မပါတဲ့ အရာကို clone လုပ်ချင်ရင် sparse checkout ကို သုံးပါ။
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> ဒါက သင်တန်းကို အမြန်ဆုံးပြီးမြောက်ဖို့ လိုအပ်တဲ့ အရာအားလုံးကို ပေးပါတယ်။
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4j အတွက် စတင်သူများ

LangChain4j နှင့် Azure OpenAI GPT-5 ဖြင့် AI application များ တည်ဆောက်ခြင်း အတွက် သင်တန်း၊ စကားပြောခြင်းမှ စပြီး AI ကိုယ်စားလှယ်များအထိ။

**LangChain4j အတွက် အသစ်လား?** အချက်အလက်များနှင့် အဓိပ္ပါယ်များအတွက် [Glossary](docs/GLOSSARY.md) ကို ကြည့်ပါ။

## အကြောင်းအရာဇယား

1. [အမြန် စတင်မည်](00-quick-start/README.md) - LangChain4j ဖြင့် စတင်ခြင်း
2. [နိဒါန်း](01-introduction/README.md) - LangChain4j အခြေခံ နည်းလမ်းများ စာမူ
3. [Prompt ဖန်တီးခြင်း](02-prompt-engineering/README.md) - ထိရောက်သော prompt ဒီဇိုင်း ကျွမ်းကျင်ခြင်း
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - နိုင်ငံ့ သိပ္ပံပညာ အခြေချမှု စနစ်များတည်ဆောက်ခြင်း
5. [ကိရိယာများ](04-tools/README.md) - ပြင်ပ ကိရိယာများနှင့် ရိုးရှင်းသော အကူအညီပေးသူများ ပေါင်းစည်းခြင်း
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP) နှင့် Agentic modules အသုံးပြုခြင်း
---

## သင်ယူမှုလမ်းကြောင်း

> **အမြန်စတင်ခြင်း**

1. ဒီ repository ကို သင့် GitHub အကောင့်သို့ Fork လုပ်ပါ
2. **Code** → **Codespaces** tab → **...** → **New with options...** ကိုနှိပ်ပါ
3. ပုံမှန်တန်ဖိုးများကို အသုံးပြုပါ - သင်တန်းအတွက် ဖန်တီးထားသော Development container ကို ရွေးပါ
4. **Create codespace** ကိုနှိပ်ပါ
5. ပတ်ဝန်းကျင် ပြင်ဆင်ပြီး ပြင်ဆင်ရန် ၅-၁၀ မိနစ် စောင့်ပါ
6. အမြန်ဆုံး စတင်ရန် [အမြန် စတင်မည်](./00-quick-start/README.md) သို့ သွားပါ!

module များ ပြီးမြောက်ပြီးနောက်၊ LangChain4j စမ်းသပ်မှု နားလည်မှု ကို လေ့လာရန် [Testing Guide](docs/TESTING.md) ကို တွေ့နိုင်သည်။

> **အမှတ်အသား:** ဒီသင်တန်းမှာ GitHub Models နဲ့ Azure OpenAI နှစ်ခုလုံးကို သုံးပါတယ်။ [အမြန် စတင်မည်](00-quick-start/README.md) module မှာ GitHub Models (Azure subscription မလိုအပ်ပါ) ကို သုံးပြီး၊ module 1-5 များမှာ Azure OpenAI ကို သုံးသည်။ Azure အကောင့် မရှိသောသူများ အတွက် [အခမဲ့ Azure အကောင့်](https://aka.ms/azure-free-account) ဖြင့် စတင်ပါ။

## GitHub Copilot နှင့် သင်ယူခြင်း

အမြန်လက်မောင်းရေးရန် သင့် GitHub Codespace သို့မဟုတ် ဒေသတွင်း IDE တွင်ဒီ project ကို ဖွင့်ပါ။ သင်တန်းတွင် သုံးသော devcontainer တွင် GitHub Copilot AI ပေးသော အစည်းအဝေး ပါဝင်ပြီး AI နှင့် နှိုင်းယှဉ် programming ချိတ်ဆက်စေသည်။

code ဥပမာတိုင်းတွင် GitHub Copilot ကို မေးခွန်းများပါရှိပြီး သင်၏ နားလည်မှုကို ပိုမိုမှန်ကန်စေသည်။ 💡/🤖 prompt များကို တွေ့ရန်:

- **Java ဖိုင် header များ** - အထူးသဖြင့် ဥပမာတစ်ခုချင်းစီနှင့် စပ်လျဉ်းသော မေးခွန်းများ
- **Module README များ** - code ဥပမာပြီးနောက် စူးစမ်းရန် စကားများ

**အသုံးပြုနည်း:** မည်သည့် code ဖိုင်မဆို ဖွင့်ပြီး Copilot ကို အဆိုပါမေးခွန်းများ မေးလို့ရသည်။ Copilot သည် codebase အပြည့်အစုံ နားလည်ပြီး ရှင်းပြ၊ တိုးချဲ့၊ နှင့် အခြားရွေးချယ်မှုများ အကြံပြုနိုင်သည်။

ပိုမိုသိချင်ပါသလား? [Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI) ကိုကြည့်ပါ။

## ထပ်ဆောင်း အရင်းအမြစ်များ

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
 
### Copilot စီးရီးများ
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## အကူအညီရရန်

AI အက်ပ်များတည်ဆောက်ရာတွင် ပြတ်တောက်သွားပါက ဒါမှမဟုတ် မေးခွန်းများရှိပါက ဝင်ရောက်ပါ။

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

ထုတ်ကုန်အကြံပြုချက်များ သို့မဟုတ် အဆင်မပြေမှုများရှိပါက သွားရောက်ကြည့်ရှုရန်။

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## ထုတ်ပြန်ခွင့်

MIT License - အသေးစိတ်အတွက် [LICENSE](../../LICENSE) ဖိုင်ကိုကြည့်ပါ။

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**သတိပြုချက်**  
ဤစာတမ်းကို AI ဘာသာပြန်စနစ် [Co-op Translator](https://github.com/Azure/co-op-translator) ဖြင့် ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှန်ကန်မှုကို ကြိုးစားသော်လည်း အလိုအလျောက် ဘာသာပြန်ထားမှုများတွင် မှားယွင်းချက်များ သို့မဟုတ် မမှန်ကန်မှုများ ပါဝင်နိုင်ကြောင်း သတိပြုပါရန် အသိပေးအပ်ပါသည်။ မူရင်းစာတမ်းကို မူရင်းလူမျိုးဘာသာဖြင့်သာ ယုံကြည်စိတ်ချရသော အရင်းအမြစ်အဖြစ် ချင်တင်သင့်ပါသည်။ ခိုင်မာသောသတင်းအချက်အလက်များအတွက် လူကြီးမင်းကို ကျွမ်းကျင်သော လူ့ဘာသာပြန် စနစ်ကို အသုံးပြုရန် အကြံပြုပါသည်။ ဤဘာသာပြန်ချက်ကို အသုံးပြုခြင်းကြောင့် ဖြစ်ပေါ်နိုင်သည့် အားနည်းချက်များ သို့မဟုတ် နားမလည်မှုများအတွက် ကျွန်ုပ်တို့ တာဝန်မရှိပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->