<img src="../../translated_images/my/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j အတွက် စလိုတစ်ချိန်အသစ်များ

LangChain4j နှင့် Azure OpenAI GPT-5.2 ဖြင့် AI အက်ပ်လီကေးရှင်းများ တည်ဆောက်ခြင်း အတွက် သင်တန်း၊ မူရင်းစကားပြောတစ်ခုမှ AI အသံ နှင့်အတူ။

### 🌐 ဘာသာစကားအများအပြားထောက်ခံမှု

#### GitHub Action မှတဆင့် ထောက်ခံသည် (အလိုအလျောက် & အမြဲအသစ်နေသည်)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](./README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **မြန်မာ့ကိုဒေါင်းလုတ်လုပ်ချင်ပါသလား?**
>
> ဤ repository တွင် ဘာသာစကား ၅၀ ကျော်နဲ့အတူ အဓိကဒေါင်းလုတ် အရွယ်အစား များစွာပါဝင်ပါသည်။ ဘာသာစကားများ မပါဘဲ ကူးယူလိုပါက sparse checkout ကို အသုံးပြုပါ။
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
> ဒါကြောင့် သင်တို့ သင်တန်းအတွက် လိုအပ်မှုများအားလုံးကို အလျင်မြန်ဆုံး ဒေါင်းလုတ်လုပ်နိုင်ပါမည်။
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## အကြောင်းအရာ အညွှန်း

1. [အလျင်မြန်စတင်ခြင်း](00-quick-start/README.md) - LangChain4j နဲ့ စစလိုက်ပါ
2. [နိဒါန်း](01-introduction/README.md) - LangChain4j အခြေခံအချက်များ သင်ယူပါ
3. [Prompt Engineering](02-prompt-engineering/README.md) - ထိရောက်သော prompt ဒီဇိုင်း ကျွမ်းကျင်လိုက်ပါ
4. [RAG (ထောက်ပံ့မှု-ပိုဆောင်း ထုတ်လုပ်မှု)](03-rag/README.md) - ဉာဏ်ရည်မြင့် ဗဟုသုတအခြေပြု စနစ်တွေ တည်ဆောက်ပါ
5. [ကိရိယာများ](04-tools/README.md) - ပြင်ပကိရိယာများနှင့် လွယ်ကူသော အကူအညီပေးသူများ ပေါင်းစပ်ပါ
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP) နှင့် Agentic modules နှင့် လက်တွဲ အလုပ်လုပ်ပါ

### ဗီဒီယို လမ်းညွှန်များ

လေ့လာမှုတိုင်းမှာ အရာဝတ္ထုနှင့် ကုဒ်အဆင့် သင်ကြားပေးသည့် live အစိတ်အပိုင်းများ ပါဝင်သည်။

| Module | ဗီဒီယို |
|--------|-------|
| 01 - နိဒါန်း | [LangChain4j နှင့် စတင်ခြင်း](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - Prompt Engineering | [LangChain4j ဖြင့် Prompt Engineering](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [LangChain4j ဖြင့် RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - ကိရိယာများ & 05 - MCP | [ကိရိယာများနှင့် MCP ဖြင့် AI Agents](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## သင်ယူခြင်းလမ်းကြောင်း

**LangChain4j အသစ်လား?** အဓိက အဓိပ္ပါယ်များနှင့် အကြောင်းအရာများကို သိရှိရန် [Glossary](docs/GLOSSARY.md) ကို ကြည့်ပါ။

> **အလျင်မြန်စတင်ရန်**

1. သင့် GitHub အကောင့်သို့ repository ကို Fork လုပ်ပါ
2. **Code** → **Codespaces** tab → **...** → **New with options...** ကို နှိပ်ပါ
3. ဘာသာရပ်များကို သုံးစွဲပါ - သင်တန်းအတွက် ဖန်တီးထားသော Development container ကို ရွေးပါ
4. **Create codespace** ကို နှိပ်ပါ
5. ပတ်ဝန်းကျင် ပြင်ဆင်ရန် ၅-၁၀ မိနစ် စောင့်ပါ
6. စတင်ရန်အတွက် [အလျင်မြန်စတင်ခြင်း](./00-quick-start/README.md) သို့ တိုက်ရိုက် ဝင်ကြည့်ပါ!

မော်ဂျူးများ ပြီးစီးပြီးနောက် [Testing Guide](docs/TESTING.md) ကိုလည်း စမ်းသပ်တဲ့အကြောင်းအရာတွေနဲ့ တွေ့မြင်ပါ။

> **မှတ်ချက်:** ဤသင်တန်းတွင် GitHub Models နှင့် Azure OpenAI နှစ်ခုလုံးကို အသုံးပြုပါသည်။ [အလျင်မြန်စတင်ခြင်း](00-quick-start/README.md) အပေါ်မော်ဂျူးမှာ GitHub Models ပါဝင်ပြီး (Azure subscription မလိုအပ်ပါ), မော်ဂျူး ၁-၅ တွင် Azure OpenAI ကို အသုံးပြုသည်။ Azure အကောင့်မရှိပါက [အခမဲ့ Azure အကောင့်](https://aka.ms/azure-free-account) ဖြင့် စတင်ပါ။

## GitHub Copilot နဲ့ သင်ယူခြင်း

သင်ရေးသားမှု စတင်ရန်၊ ဤ project ကို GitHub Codespace သို့မဟုတ် သင့်ကိုယ်ပိုင် IDE တွင် ဖွင့်ပါ၊ သင်တန်းတွင် အသုံးပြုသော devcontainer မှာ GitHub Copilot နဲ့ AI ကိုယ်ဖော် programming အတွက် ကြိုတင်ပြင်ဆင်ထားပါသည်။

ကုဒ်နမူနာတိုင်းတွင် GitHub Copilot အား မေးမြန်းနိုင်သော မေးခွန်းများ ပါဝင်သည်။ အောက်ပါနေရာများတွင် 💡/🤖 အတိုင်းပြုစုထားပါသည် -

- **Java ဖိုင်ခေါင်းစီးများ** - နမူနာအပိုင်းအလိုက် မေးခွန်းများ
- **Module README များ** - ကုဒ်နမူနာပြီးနောက် သုတေသနမေးခွန်းများ

**အသုံးပြုပုံ:** ကုဒ်ဖိုင်မဆို ဖွင့်ပြီး Copilot အား မေးခွန်းများကို မေးပါ။ Copilot သည် ကုဒ်ဇယားအားလုံးကို လုံးဝနားလည်ကာ ရှင်းပြခြင်း၊ တိုးချဲ့ခြင်း၊ အခြားရွေးချယ်မှုများကို ညွှန်ပြနိုင်သည်။

ပိုမိုသိလိုပါသလား? [AI ကိုယ်ဖော် Programming အတွက် Copilot](https://aka.ms/GitHubCopilotAI) ကို ကြည့်ရှုပါ။

## အပိုဆောင်း အရင်းအမြစ်များ

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
[![အခြေခံများအတွက် ဆိုင်ဘာလုံခြုံရေး](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![အခြေခံများအတွက် ဝက်ဘ်တီထွင်ခြင်း](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![အခြေခံများအတွက် IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![အခြေခံများအတွက် XR ဖွံ့ဖြိုးရေး](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot စီးရီး
[![AI ကိုဂျွမ်းစုလုပ်ပေးမယ့် Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET အတွက် Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot စွန့်စားခန်း](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## ကူညီမှုရယူခြင်း

AI အက်ပ်တွေ ဖန်တီးရာမှာ အခက်အခဲရှိလျှင် သို့မဟုတ် မေးခွန်းများရှိလျှင် ဝင်ရောက်ဆွေးနွေးရန်:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

ထုတ်ကုန်တုံ့ပြန်ချက်များ သို့မဟုတ် တည်းဖြတ်ချက်များ ရောနှောမှုများရှိပါက:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## ခွင့်ပြုချက်

MIT ခွင့်ပြုချက် - အသေးစိတ်အချက်အလက်များအတွက် [LICENSE](../../LICENSE) ဖိုင်ကို ကြည့်ပါ။

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**အဆိုပြုချက်**  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်မှုဝန်ဆောင်မှုဖြစ်သော [Co-op Translator](https://github.com/Azure/co-op-translator) အသုံးပြု၍ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှုအတွက် ကြိုးစားသော်လည်း အလိုအလျောက်ဘာသာပြန်မှုများတွင် အမှားများ သို့မဟုတ် မှန်ကန်မှုမပြည့်စုံမှုများ ပါဝင်နိုင်ကြောင်း သတိပြုပါရန် ဖိတ်ခေါ်ပါသည်။ မူလစာရွက်စာတမ်းကို မိခင်ဘာသာဖြင့်သာ ယုံကြည်စိတ်ချရသောအရင်းအမြစ်အဖြစ်လည်း အားထားသင့်ပါသည်။ အရေးကြီးသောအချက်အလက်များအတွက် အထူးပြု လူသားဘာသာပြန်သူမှ ဘာသာပြန်ခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်မှုအသုံးပြုမှုကြောင့် ဖြစ်ပေါ်နိုင်သည့် ရိုင်းစိုင်းမှုများ သို့မဟုတ် နားလည်မှုမှားများအတွက် ကျွန်ုပ်တို့ မည်သည့် တာဝန်ပေးပို့မှုပြုမည် မဟုတ်ပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->