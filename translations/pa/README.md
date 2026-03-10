<img src="../../translated_images/pa/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# ਬੁਨਿਆਦੀ ਜਾਣਕਾਰੀ ਲਈ LangChain4j

LangChain4j ਅਤੇ Azure OpenAI GPT-5.2 ਨਾਲ AI ਐਪਲੀਕੇਸ਼ਨਾਂ ਬਣਾਉਣ ਲਈ ਇੱਕ ਕੋਰਸ, ਬੇਸਿਕ ਚੈਟ ਤੋਂ ਲੈ ਕੇ AI ਏਜੰਟਾਂ ਤੱਕ।

### 🌐 ਬਹੁ-ਭਾਸ਼ਾਈ ਸਮਰਥਨ

#### GitHub ਐਕਸ਼ਨ ਰਾਹੀਂ ਸਮਰਥਿਤ (ਆਟੋਮੈਟਿਕ ਅਤੇ ਹਮੇਸ਼ਾਂ ਅੱਪ-ਟੂ-ਡੇਟ)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](./README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **ਲੋਕਲ ਕਲੋਨ ਕਰਨਾ ਪਸੰਦ ਕਰਦੇ ਹੋ?**
>
> ਇਸ ਰਿਪਾਜ਼ਟਰੀ ਵਿੱਚ 50+ ਭਾਸ਼ਾ ਅਨੁਵਾਦ ਸ਼ਾਮਿਲ ਹਨ ਜੋ ਡਾਊਨਲੋਡ ਸਾਈਜ਼ ਨੂੰ ਵੱਧਾ ਦਿੰਦੇ ਹਨ। ਅਨੁਵਾਦਾਂ ਤੋਂ ਬਿਨਾਂ ਕਲੋਨ ਕਰਨ ਲਈ sparse checkout ਵਰਤੋ:
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
> ਇਹ ਤੁਹਾਨੂੰ ਕੋਰਸ ਨੂੰ ਬਹੁਤ ਤੇਜ਼ੀ ਨਾਲ ਡਾਊਨਲੋਡ ਕਰਕੇ ਪੂਰਾ ਕਰਨ ਲਈ ਸਾਰਾ ਕੁਝ ਦਿੰਦਾ ਹੈ।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## ਸਾਰਣੀ

1. [ਤੇਜ਼ ਸ਼ੁਰੂਆਤ](00-quick-start/README.md) - LangChain4j ਸਾਥ ਸ਼ੁਰੂਆਤ ਕਰੋ
2. [ਪਰਿਚਯ](01-introduction/README.md) - LangChain4j ਦੇ ਮੁਢਲੇ ਅਸੂਲ ਸਿੱਖੋ
3. [ਪ੍ਰੌਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ](02-prompt-engineering/README.md) - ਪ੍ਰਭਾਵਸ਼ਾਲੀ ਪ੍ਰੌਂਪਟ ਡਿਜ਼ਾਇਨ ਵਿੱਚ ਮਹਾਰਤ ਹਾਸਲ ਕਰੋ
4. [RAG (ਰੀਟਰੀਵਲ-ਆਗਮੈਂਟਡ ਜਨਰੇਸ਼ਨ)](03-rag/README.md) - ਬੁੱਧਮਾਨ ਗਿਆਨ ਅਧਾਰਿਤ ਪ੍ਰਣਾਲੀਆਂ ਬਣਾਓ
5. [ਸੰਦ](04-tools/README.md) - ਬਾਹਰੀ ਸੰਦ ਅਤੇ ਸਧਾਰਣ ਸਹਾਇਕ ਸ਼ਾਮਿਲ ਕਰੋ
6. [MCP (ਮਾਡਲ ਸੰਦਰਭ ਪ੍ਰੋਟੋਕਾਲ)](05-mcp/README.md) - ਮਾਡਲ ਸੰਦਰਭ ਪ੍ਰੋਟੋਕਾਲ (MCP) ਅਤੇ ਏਜੰਟਿਕ ਮਾਡਿਊਲਾਂ ਨਾਲ ਕੰਮ ਕਰੋ

### ਵੀਡੀਓ ਚੱਲਣਾ

ਹਰ ਮੋਡਿਊਲ ਦੇ ਨਾਲ ਲਾਈਵ ਸੈਸ਼ਨ ਹੁੰਦਾ ਹੈ ਜਿੱਥੇ ਅਸੀਂ ਪਰਿਭਾਸ਼ਾਵਾਂ ਅਤੇ ਕੋਡ ਕਦਮ ਦਰ ਕਦਮ ਵੱਖਾਂ ਕਰਦੇ ਹਾਂ।

| ਮੋਡਿਊਲ | ਵੀਡੀਓ |
|--------|-------|
| 01 - ਪਰਿਚਯ | [LangChain4j ਨਾਲ ਸ਼ੁਰੂਆਤ](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - ਪ੍ਰੌਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ | [LangChain4j ਨਾਲ ਪ੍ਰੌਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [LangChain4j ਨਾਲ RAG](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - ਸੰਦ ਅਤੇ 05 - MCP | [ਸੰਦਾਂ ਅਤੇ MCP ਨਾਲ AI ਏਜੰਟ](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## ਸਿੱਖਣ ਦਾ ਰਸਤਾ

**LangChain4j ਵਿੱਚ ਨਵਾਂ?** ਮੁਖ਼ ਸਬਦਾਂ ਅਤੇ ਧਾਰਣਾਵਾਂ ਦੀ ਵਿਆਖਿਆ ਲਈ [ਸ਼ਬਦਾਵਲੀ](docs/GLOSSARY.md) ਵੇਖੋ।

> **ਤੇਜ਼ ਸ਼ੁਰੂਆਤ**

1. ਇਸ ਰਿਪਾਜ਼ਟਰੀ ਨੂੰ ਆਪਣੇ GitHub ਖਾਤੇ ਵਿੱਚ ਫੋਰਕ ਕਰੋ
2. ਕਲਿੱਕ ਕਰੋ **ਕੋਡ** → **ਕੋਡਸਪੇਸ** ਟੈਬ → **...** → **ਨਵਾਂ ਵਿਕਲਪਾਂ ਨਾਲ...**
3. ਡਿਫਾਲਟ ਵਰਤੋ – ਇਹ ਕੋਰਸ ਲਈ ਬਣਾਇਆ ਗਇਆ ਡਿਵੈਲਪਮੈਂਟ ਕੰਟੇਨਰ ਚੁਣੇਗਾ
4. ਕਲਿੱਕ ਕਰੋ **ਕੋਡਸਪੇਸ ਬਣਾਓ**
5. ਵਾਤਾਵਰਣ ਤਿਆਰ ਹੋਣ ਲਈ 5-10 ਮਿੰਟ ਉਡੀਕ ਕਰੋ
6. ਸਿੱਧਾ [ਤੇਜ਼ ਸ਼ੁਰੂਆਤ](./00-quick-start/README.md) 'ਤੇ ਜਾਓ ਅਤੇ ਸ਼ੁਰੂ ਕਰੋ!

ਮੋਡਿਊਲ ਮੁਕੰਮਲ ਕਰਨ ਤੋਂ ਬਾਅਦ, [ਟੈਸਟਿੰਗ ਗਾਈਡ](docs/TESTING.md) ਦੀ ਜਾਂਚ ਕਰੋ ਤਾਂ ਜੋ LangChain4j ਟੈਸਟਿੰਗ ਧਾਰਨਾਵਾਂ ਨੂੰ ਕਾਰਵਾਈ ਵਿੱਚ ਦੇਖ ਸਕੋ।

> **ਨੋਟ:** ਇਹ ਟ੍ਰੇਨਿੰਗ GitHub ਮਾਡਲਾਂ ਅਤੇ Azure OpenAI ਦੋਹਾਂ ਨੂੰ ਵਰਤਦੀ ਹੈ। [ਤੇਜ਼ ਸ਼ੁਰੂਆਤ](00-quick-start/README.md) ਮੋਡਿਊਲ GitHub ਮਾਡਲਾਂ ਦਾ ਇਸਤੇਮਾਲ ਕਰਦਾ ਹੈ (Azure ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਦੀ ਲੋੜ ਨਹੀਂ), ਜਦਕਿ ਮੋਡਿਊਲ 1-5 ਵਿੱਚ Azure OpenAI ਵਰਤੀ ਜਾਂਦੀ ਹੈ। ਜੇ ਤੁਹਾਡੇ ਕੋਲ ਐਕਾਊਂਟ ਨਹੀਂ ਹੈ ਤਾਂ [FREE Azure ਖਾਤਾ](https://aka.ms/azure-free-account) ਨਾਲ ਸ਼ੁਰੂ ਕਰੋ।


## GitHub Copilot ਨਾਲ ਸਿੱਖਣਾ

ਜਲਦੀ ਕੋਡਿੰਗ ਸ਼ੁਰੂ ਕਰਨ ਲਈ, ਇਸ ਪ੍ਰੋਜੈਕਟ ਨੂੰ GitHub ਕੋਡਸਪੇਸ ਜਾਂ ਆਪਣੇ ਸਥਾਨਕ IDE ਵਿੱਚ ਪ੍ਰਦਾਤ ਕੀਤੇ ਡਿਵਕੰਟੇਨਰ ਨਾਲ ਖੋਲ੍ਹੋ। ਇਸ ਕੋਰਸ ਵਿੱਚ ਵਰਤਿਆ ਡਿਵਕੰਟੇਨਰ GitHub Copilot ਲਈ ਪਹਿਲਾਂ ਤੋਂ ਸੰਰਚਿਤ ਹੈ ਜੋ AI ਜੋੜੀ ਪ੍ਰੋਗ੍ਰਾਮਿੰਗ ਲਈ ਹੈ।

ਹਰ ਕੋਡ ਉਦਾਹਰਣ ਵਿੱਚ ਇਹ ਗੱਲਾਂ ਸ਼ਾਮਿਲ ਹਨ ਜੋ ਤੁਸੀਂ GitHub Copilot ਨੂੰ ਸਮਝ ਨੂੰ ਗਹਿਰਾਈ ਵਿੱਚ ਲੈ ਜਾਣ ਲਈ ਪੁੱਛ ਸਕਦੇ ਹੋ। ਇਸ ਨੂੰ ਖੋਜਣ ਲਈ 💡/🤖 ਪ੍ਰੌਂਪਟਾਂ ਨੂੰ ਵੇਖੋ:

- **ਜਾਵਾ ਫਾਇਲ headers** - ਹਰ ਉਦਾਹਰਣ ਲਈ ਵਿਸ਼ੇਸ਼ ਸਵਾਲ
- **ਮੋਡਿਊਲ README** - ਕੋਡ ਉਦਾਹਰਣਾਂ ਤੋਂ ਬਾਅਦ ਖੋਜ ਪ੍ਰੌਂਪਟ

**ਕਿਵੇਂ ਵਰਤਣਾ ਹੈ:** ਕਿਸੇ ਵੀ ਕੋਡ ਫਾਇਲ ਨੂੰ ਖੋਲ੍ਹੋ ਅਤੇ Copilot ਨੂੰ ਦਿੱਖਾਏ ਸਵਾਲ ਪੁੱਛੋ। ਇਸ ਕੋਡਬੇਸ ਦੀ ਪੂਰੀ ਸੰਦਰਭ ਜਾਣਕਾਰੀ ਹੈ ਅਤੇ ਇਹ ਵਿਆਖਿਆ ਕਰ ਸਕਦਾ ਹੈ, ਵਧਾ ਸਕਦਾ ਹੈ ਅਤੇ ਵਿਕਲਪ ਸੋਜਾਉਂਦਾ ਹੈ।

ਹੁਣ ਹੋਰ ਜਾਣਨਾ ਚਾਹੁੰਦੇ ਹੋ? [Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI) ਵੇਖੋ।


## ਹੋਰ ਸਰੋਤ

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
 
### ਮੂਲ ਸਿੱਖਿਆ
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ਕੋਪਾਇਲਟ ਸੀਰੀਜ਼
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## ਮਦਦ ਲੈਣਾ

ਜੇਕਰ ਤੁਹਾਨੂੰ ਫਸਿਆ ਹੋਇਆ ਮਹਿਸੂਸ ਹੁੰਦਾ ਹੈ ਜਾਂ AI ਐਪ ਬਣਾਉਣ ਬਾਰੇ ਕੋਈ ਸਵਾਲ ਹਨ, ਤਾਂ ਸ਼ਾਮਿਲ ਹੋਵੋ:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

ਜੇ ਤੁਹਾਡੇ ਕੋਲ ਉਤਪਾਦ ਫੀਡਬੈਕ ਜਾਂ ਤਿਆਰ ਕਰਦੇ ਸਮੇਂ ਕੋਈ ਗਲਤੀਆਂ ਹਨ ਤਾਂ ਜਾਓ:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## ਲਾਇਸੈਂਸ

MIT ਲਾਇਸੈਂਸ - ਵੇਰਵਿਆਂ ਲਈ [LICENSE](../../LICENSE) ਫਾਈਲ ਵੇਖੋ।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਵੀਕਾਰੋक्ति**:
ਇਸ ਦਸਤਾਵੇਜ਼ ਦਾ ਅਨੁਵਾਦ ਏਆਈ ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਯਤਨਸ਼ੀਲ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਰੱਖੋ ਕਿ ਸਵੈਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸਪਸ਼ਟਤਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੇ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਹੀ ਪ੍ਰਮਾਣਿਕ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਮਹੱਤਵਪੂਰਨ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਿਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਅਸੀਂ ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਤੋਂ ਪੈਦਾ ਹੋਣ ਵਾਲੀਆਂ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀਆਂ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆਵਾਂ ਲਈ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->