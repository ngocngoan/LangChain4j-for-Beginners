<img src="../../translated_images/my/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j အတွက် စတင်သူများ

LangChain4j နှင့် Azure OpenAI GPT-5.2 ဖြင့် အခြေခံ ချက်မှ စ၍ AI ကိုယ်စားလှယ်များထိ AI အက်ပ်လီကေးရှင်းလ်များ တည်ဆောက်ခြင်းအတွက် သင်တန်း။

### 🌐 ဘာသာစကား မျိုးစုံ ထောက်ခံမှု

#### GitHub Action (အလိုအလျောက် နှင့် အမြဲပြန်တင်ပြီး) မှတဆင့် ထောက်ခံသည်

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](./README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **ဒေသခံတွင် Clone လုပ်ရန် ကြိုက်ပါသလား?**
>
> ဒီ repository မှာ ဘာသာစကား ၅၀ ကျော်ရှိပြီး ဒါကြောင့် ဒေါင်းလုဒ်အရွယ်အစားမြင့်သည်။ ဘာသာပြန်ချက်များမပါဘဲ clone လုပ်လိုပါက sparse checkout ကို အသုံးပြုပါ။
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
> ဒါက သင်တန်းတက်ရန် လိုအပ်သည့် အရာအားလုံးကို မြန်ဆန်စွာ ဒေါင်းလုဒ်ဆွဲပေးမည် ဖြစ်သည်။
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## အကြောင်းအရာ ဇယား

1. [အမြန် စတင်ခြင်း](00-quick-start/README.md) - LangChain4j နှင့် စတင်ပါ
2. [နိဒါန်း](01-introduction/README.md) - LangChain4j အခြေခံများ သင်ယူပါ
3. [Prompt Engineering](02-prompt-engineering/README.md) - ထိရောက်သော prompt ဒီဇိုင်း ကျွမ်းကျင်ပါ
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - ဉာဏ်ကြီးသော သိက္ခာဒေတာ စနစ်များ တည်ဆောက်ပါ
5. [Tools](04-tools/README.md) - အပြင် tools နှင့် ရိုးရှင်းသည့် အကူအညီများ ပေါင်းစည်းပါ
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Model Context Protocol (MCP) နှင့် Agentic module များနှင့် လုပ်ဆောင်ပါ
---

## သင်ယူရန် လမ်းကြောင်း

**LangChain4j အသစ်လား?** အဓိက စကားလုံးများနှင့် သဘောတရားများ အတွက် [Glossary](docs/GLOSSARY.md) ကို ကြည့်ပါ။

> **အမြန် စတင်ခြင်း**

1. ဤ repository ကို သင့် GitHub အကောင့်သို့ fork လုပ်ပါ
2. **Code** → **Codespaces** tab → **...** → **New with options...** ကို နှိပ်ပါ
3. ဒေဖောလ့်များကို အသုံးပြုပါ – ဤသင်တန်းအတွက် ဆောက်လုပ်ထားသော Development container ကို ရွေးချယ်မည်
4. **Create codespace** ကို နှိပ်ပါ
5. ပတ်ဝန်းကျင်ပြင်ဆင်ပြီးစီးရန် ၅-၁၀ မိနစ် စောင့်ဆိုင်းပါ
6. စတင်ရန် အမြန် စတင်ခြင်း [Quick Start](./00-quick-start/README.md) သို့ တိုက်ရိုက်ဝင်ပါ!

Modules ပြီးဆုံးပြီးနောက်၊ LangChain4j စမ်းသပ်မှု သဘောတရားများကို ကြည့်ရှုရန် [Testing Guide](docs/TESTING.md) ကို လေ့လာပါ။

> **မှတ်စု:** ဒီသင်တန်းမှာ GitHub Models နဲ့ Azure OpenAI နှစ်ခုစလုံး အသုံးပြုပါတယ်။ [Quick Start](00-quick-start/README.md) module မှာ GitHub Models ကို အသုံးပြုမှာဖြစ်ပြီး (Azure subscription မလိုအပ်ပါ)၊ မော်ဂျူး ၁ မှ ၅ ထိမှာ Azure OpenAI ကို အသုံးပြုပါမယ်။ Azure အကောင့် မရှိသေးပါက [အခမဲ့ Azure အကောင့်](https://aka.ms/azure-free-account) မှာ စတင်ပါ။


## GitHub Copilot နဲ့ သင်ယူခြင်း

coding ကို မြန်ဆန်စတင်ရန်၊ ဒီပရောဂျက်ကို GitHub Codespace သို့မဟုတ် တည်နေရာရှိ IDE တွင် devcontainer အတူ ဖွင့်ပါ။ သင်တန်းတွင် အသုံးပြုသော devcontainer တွင် GitHub Copilot AI ဖက်ဖက်ပေါင်း programming အတွက် အသင့်ပြင်ဆင်ပြီး ပါဝင်သည်။

ကိုးကားသင်ခန်းစာ၌ GitHub Copilot ကို မေးမြန်းနိုင်သော မေးခွန်းများ ပါဝင်သည်။ 💡/🤖 prompt များကို အောက်ပါနေရာများတွင် ရှာပါ -

- **Java ဖိုင် header များ** - ဥပမာအလိုက် မေးခွန်းများ
- **Module README များ** - ကုဒ် ဥပမာများနောက် ထပ်ဆင့် စူးစမ်းရန် prompt များ

**သုံးနည်း** - ဖိုင်တစ်ခုစီ ဖွင့်ပြီး Copilot ကို မေးခွန်းများ စုံစမ်းမေးမြန်းပါ။ Copilot သည် ကုဒ်အခြေအနေနှင့် ပြည့်စုံသုံးသပ်ပြီး ရှင်းလင်း ဖွင့်ဆိုနိုင်ခြင်း၊ တိုးချဲ့နိုင်ခြင်း၊ အခြားရွေးချယ်စရာများ အကြံပြုနိုင်သည်။

ပိုမိုသိရှိရန် [Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI) ကို ကြည့်ပါ။ 


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
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot Series
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## ကူညီမှု ရယူခြင်း

AI အက်ပ်များ ဖန်တီးရာတွင် ပြဿနာရှိပါက သို့မဟုတ် မေးခွန်းရှိပါက ဇပ်ဝင်ပါ:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

ထုတ်ကုန်တုံ့ပြန်ချက် သို့မဟုတ် ဖန်တီးနေစဉ် အမှားများရှိပါက ဤနေရာသို့ သွားပါ:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## ခွင့်ပြုချက်

MIT License - အသေးစိတ်အတွက် [LICENSE](../../LICENSE) ဖိုင်ကို ကြည့်ပါ။

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**သတိပေးချက်**  
ဤစာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကူညီ၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှန်ကန်မှုအတွက် ကြိုးစားဆောင်ရွက်သော်လည်း၊ စက်ရုပ်ဘာသာပြန်ခြင်းဟာ မှားယွင်းမှုများ သို့မဟုတ် မမှန်ကန်မှုများ ပါဝင်နိုင်ကြောင်း ကိုကျေးဇူးပြု၍ သိထားပါ။ မူလစာတမ်းကို မူခံဘာသာဖြင့် အာဏာပိုင်အရင်းအမြစ်အဖြစ် ယူဆရန် လိုအပ်ပါသည်။ အရေးကြီးသော သတင်းအချက်အလက်များအတွက် လူအရည်အချင်းပြည့်မီသော ဘာသာပြန်သူများအား အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုခြင်းကနေ ဖြစ်ပေါ်လာသော နားလည်မှုမှားယွင်းမှုများအတွက် ကျွန်ုပ်တို့မှာ တာဝန်မထားရှိပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->