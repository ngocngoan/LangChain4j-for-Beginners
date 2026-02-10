<img src="../../translated_images/my/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 ဘာသာစကားများစွာကို ပံ့ပိုးသည်

#### GitHub Action ဖြင့်ပံ့ပိုး (အလိုအလျောက်နှင့် အမြဲမွမ်းမံနေသည်)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](./README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **ဒေသခံဘက်ကော်ပီလုပ်ချင်ပါသလား?**

> ဤသိုလှောင်ထားရာတွင် ၆၀ ကျော်သော ဘာသာစကားဘာသာပြန်ချက်များပါရှိကာ ဒေါင်းလုဒ်အရွယ်အစားကို များစွာတိုးပွားစေပါသည်။ ဘာသာပြန်ချက်များအပါအဝင်မဟုတ်ဘဲ ကော်ပီလုပ်လိုပါက sparse checkout ကို အသုံးပြုပါ:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> ၎င်းက သင့်အား သင်တန်းကို အပြီးသတ်ရန် လိုအပ်သော အရာအားလုံးကို ပိုမိုမြန်ဆန်သော ဒေါင်းလုဒ်ဖြင့် ပေးပါမည်။
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4j အစအသစ်

LangChain4j နှင့် Azure OpenAI GPT-5.2 ဖြင့် အခြေခံစကားပြောမှ စ၍ AI အေးဂျင့်များထိ AI အပလီကေးရှင်းများ တည်ဆောက်ခြင်း သင်တန်းဖြစ်သည်။

**LangChain4j အသုံးပြုရသစ်ပါသလား?** အဓိကအကြောင်းအရာများနှင့် အကြိမ်ကြောင်းများကို နားလည်ရန် [Glossary](docs/GLOSSARY.md) ကို ကြည့်ပါ။

## အကြောင်းအရာ ဇယား

1. [အလျင်အမြန် စတင်ခြင်း](00-quick-start/README.md) - LangChain4j ဖြင့် စတင်ရန်
2. [နိဒါန်း](01-introduction/README.md) - LangChain4j ၏ အခြေခံအချက်များ သင်ယူရန်
3. [Prompt အင်ဂျင်နီယာရင်း](02-prompt-engineering/README.md) - ထိရောက်သော prompt ဒီဇိုင်း ကျွမ်းကျင်ရန်
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - ဉစ္စာရုပ် ပညာမှတ်တမ်းစနစ်များ တည်ဆောက်ရန်
5. [ကိရိယာများ](04-tools/README.md) - ပြင်ပကိရိယာများနှင့် ရိုးရှင်းသော အကူအညီများ အမိုးခံာခြင်း
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP) နှင့် Agentic modules တွင် လုပ်ဆောင်ရန်
---

## သင်ယူမှု လမ်းကြောင်း

> **အလျင်အမြန် စတင်ခြင်း**

1. ဤသိုလှောင်ထားရာကို သင်၏ GitHub အကောင့်ထဲသို့ Fork လုပ်ပါ
2. **Code** ကို နှိပ်ပါ → **Codespaces** tabs → **...** → **New with options...** ကို နှိပ်ပါ
3. ပုံသေ defaults များအား သုံးပါ - ၎င်းသည် သင်္ချာသင်တန်းအတွက် ဖန်တီးထားသော Development container ကို ရွေးချယ်ပါမည်
4. **Create codespace** ကို နှိပ်ပါ
5. ပတ်ဝန်းကျင် အသင့်ရှိလာရန် ၅-၁၀ မိနစ် စောင့်ပါ
6. တိုက်ရိုက် [Quick Start](./00-quick-start/README.md) သို့ ချက်ချင်း ရောက်ရှိပါ!

မော်ဂျူးများ ပြီးမြောက်ပြီးနောက် LangChain4j စမ်းသပ်မှု နားလည်ရန် [Testing Guide](docs/TESTING.md) ကို လေ့လာပါ။

> **မှတ်ချက်** - သင်တန်းတွင် GitHub Models နှင့် Azure OpenAI နှစ်မျိုးကို အသုံးပြုသည်။ [Quick Start](00-quick-start/README.md) မော်ဂျူးသည် GitHub Models ကို အသုံးပြုသည် (Azure subscription မလိုအပ်)၊ မော်ဂျူး ၁ မှ ၅ ထိမှာ Azure OpenAI ကို အသုံးပြုပါသည်။ သင် အကောင့်မရှိသေးလျှင် [FREE Azure account](https://aka.ms/azure-free-account) ဖြင့် အစပြုပါ။


## GitHub Copilot ဖြင့် သင်ယူခြင်း

အမြန်ဆုံး ကုဒ်ရေးခြင်း စတင်ရန်၊ ဤပရောဂျက်ကို GitHub Codespace သို့မဟုတ် သင်၏ ဒေသခံ IDE ထဲတွင် devcontainer ဖြင့် ဖွင့်ပါ။ သင်တန်းတွင် အသုံးပြုသည့် devcontainer သည် GitHub Copilot နှင့် AI တွဲဖက် programming အတွက် ကြိုတင်ပြင်ဆင်ပြီးဖြစ်ပါသည်။

ကုဒ်နမူနာတိုင်းတွင် GitHub Copilot ကို မေးမြန်းနိုင်သည့် မေးခွန်းများ ပါရှိကာ သင်၏ နားလည်မှု နှင့် ကျွမ်းကျင်မှု တိုးမြှင့်ရန် အထောက်အကူဖြစ်စေပါသည်။ 💡/🤖 prompts များနှင့် တွေ့နိုင်သည် -

- **Java ဖိုင်ခေါင်းစီးများ** - နမူနာအတိုင်း မေးခွန်းများ
- **Module README များ** - ကုဒ်နမူနာပြီးနောက် စူးစမ်းမေးမြန်းမှု

**ဘယ်လိုသုံးမလဲ။** ကုဒ်ဖိုင် တစ်ခုခု ဖွင့်ပြီး Copilot ကို အဆိုပြုထားသော မေးခွန်းများ မေးပါ။ ၎င်းမှာ ကုဒ်အရင်းမြစ် ဗဟုသုတ ပြည့်စုံပြီး ရှင်းပြ၊ ဆက်လက်ဖွံ့ဖြိုး၊ အခြားရွေးချယ်စရာများကို အကြံပြုနိုင်ပါသည်။

ပိုမိုသင်ယူလိုပါသလား။ [Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI) ကို ကြည့်ပါ။


## အပိုဆောင်းအရင်းအမြစ်များ

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
 
### Copilot စီးရီးများ
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## ကူညီမှုရယူခြင်း

AI အက်ပ်များ တည်ဆောက်ရာတွင် အခက်အခဲရှိပါက သို့မဟုတ် မေးခွန်းများရှိပါက တက်ကြွစွာ ပူးပေါင်းဆွေးနွေးရန်:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

ထုတ်ကုန်အကြံပြုချက်များ သို့မဟုတ် အမှားများရှိပါက တည်ဆောက်နေစဉ် ဖြစ်ပါက ဤနေရာတွင် ဝင်ရောက်ဆက်သွယ်နိုင်ပါသည်-

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## အသုံးပြုခွင့်လက်မှတ်

MIT အသုံးပြုခွင့်လက်မှတ် - အသေးစိတ်များအတွက် [LICENSE](../../LICENSE) ဖိုင်အား ကြည့်ပါ။

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ကန့်သတ်ချက်**  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဆovendာဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှန်ကန်မှုအတွက် ကြိုးပမ်းနေသော်လည်း အလိုအလျောက် ဘာသာပြန်ချက်များတွင် အမှားများ သို့မဟုတ် မမှန်ကန်ချက်များ ပါရှိနိုင်ကြောင်း သတိပေးလိုပါသည်။ မူရင်းစာတမ်းကို တရားဝင် အရင်းအမြစ်အဖြစ် ယူဆသင့်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက် မူရင်းလူသား ဘာသာပြန်ကို အကြံပြုပါသည်။ ဤဘာသာပြန်ချက်ကို အသုံးပြုနေစဉ် ဖြစ်ပေါ်လာနိုင်သည့် ဘာသာပြန်မှားယွင်းချက်များ သို့မဟုတ် နားလည်မှု မှားယွင်းမှုများအတွက် ကျွန်ုပ်တို့ တာဝန်မယူပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->