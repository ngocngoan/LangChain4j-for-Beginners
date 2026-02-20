<img src="../../translated_images/pa/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j ਸ਼ੁਰੂਆਤੀ ਲਈ

LangChain4j ਅਤੇ Azure OpenAI GPT-5.2 ਨਾਲ AI ਐਪਲੀਕੇਸ਼ਨ ਬਣਾਉਣ ਲਈ ਇੱਕ ਕੋਰਸ, ਬੁਨਿਆਦੀ ਚੈਟ ਤੋਂ ਲੈ ਕੇ AI ਏਜੰਟ ਤੱਕ।

### 🌐 ਬਹੁਭਾਸ਼ੀ ਸਹਾਇਤਾ

#### GitHub ਐਕਸ਼ਨ ਰਾਹੀਂ ਸਮਰਥਿਤ (ਸਵੈਚਾਲਿਤ ਅਤੇ ਹਮੇਸ਼ਾ ਅਪ-ਟੂ-ਡੇਟ)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](./README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **ਮੁੜ ਕਲੋਨ ਕਰਨਾ ਚਾਹੁੰਦੇ ਹੋ?**
>
> ਇਸ ਰਿਪੋਜ਼ਟਰੀ ਵਿੱਚ 50+ ਭਾਸ਼ਾਈ ਅਨੁਵਾਦ ਸ਼ਾਮِل ਹਨ ਜੋ ਡਾਊਨਲੋਡ ਦਾ ਸਾਈਜ਼ ਕਾਫੀ ਵਧਾ ਦਿੰਦੇ ਹਨ। ਬਿਨਾਂ ਅਨੁਵਾਦਾਂ ਦੇ ਕਲੋਨ ਕਰਨ ਲਈ sparse checkout ਦੀ ਵਰਤੋਂ ਕਰੋ:
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
> ਇਹ ਤੁਹਾਨੂੰ ਕੋਰਸ ਨੂੰ ਬਹੁਤ ਤੇਜ਼ ਡਾਊਨਲੋਡ ਨਾਲ ਪੂਰਾ ਕਰਨ ਲਈ ਸਭ ਕੁਝ ਮੁਹੱਈਆ ਕਰਵਾਉਂਦਾ ਹੈ।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## ਸਮਗਰੀ ਸੂਚੀ

1. [ਤੁਰੰਤ ਸ਼ੁਰੂਆਤ](00-quick-start/README.md) - LangChain4j ਨਾਲ ਸ਼ੁਰੂ ਕਰੋ  
2. [ਪਰਿਚਯ](01-introduction/README.md) - LangChain4j ਦੇ ਬੁਨਿਆਦੀ ਸਿਧਾਂਤ ਸਿੱਖੋ  
3. [ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ](02-prompt-engineering/README.md) - ਪ੍ਰਭਾਵਸ਼ਾਲੀ ਪ੍ਰਾਂਪਟ ਡਿਜ਼ਾਈਨ ਵਿੱਚ ਮਾਹਰ ਬਣੋ  
4. [RAG (ਰਿਟਰੀਵਲ-ਆਗਮੈਂਟਡ ਜੈਨਰੇਸ਼ਨ)](03-rag/README.md) - ਬੁੱਧਿਮਾਨ ਗਿਆਨ-ਅਧਾਰਿਤ ਸਿਸਟਮ ਬਣਾਓ  
5. [ਸੰਸਾਧਨ](04-tools/README.md) - ਬਾਹਰੀ ਉਪਕਰਣ ਅਤੇ ਸਧਾਰਣ ਸਹਾਇਕ ਜੋੜੋ  
6. [MCP (ਮਾਡਲ ਸੰਦਰਭ ਪ੍ਰੋਟੋਕਾਲ)](05-mcp/README.md) - ਮਾਡਲ ਸੰਦਰਭ ਪ੍ਰੋਟੋਕਾਲ (MCP) ਅਤੇ ਏਜੈਂਟਿਕ ਮੌਡਿਊਲਾਂ ਨਾਲ ਕੰਮ ਕਰੋ  
---

## ਸਿੱਖਣ ਦਾ ਰਸਤਾ

**LangChain4j ਵਿੱਚ ਨਵਾਂ ਹੋ?** ਮੁੱਖ ਸ਼ਬਦਾਂ ਅਤੇ ਧਾਰਣਾਵਾਂ ਦੀ ਪਰਿਭਾਸ਼ਾ ਲਈ [ਗਲਾਸਰੀ](docs/GLOSSARY.md) ਵੇਖੋ।

> **ਤੁਰੰਤ ਸ਼ੁਰੂਆਤ**

1. ਇਸ ਰਿਪੋਜ਼ਟਰੀ ਨੂੰ ਆਪਣਾ GitHub ਖਾਤਾ 'ਤੇ ਫੋਰਕ ਕਰੋ  
2. **Code** → **Codespaces** ਟੈਬ → **...** → **New with options...** 'ਤੇ ਕਲਿੱਕ ਕਰੋ  
3. ਡਿਫਾਲਟ ਵਰਤੋਂ - ਇਹ ਕੋਰਸ ਲਈ ਬਣਾਈ ਗਈ ਡਿਵੈਲਪਮੈਂਟ ਕੰਟੇਨਰ ਨੂੰ ਚੁਣੇਗਾ  
4. **Create codespace** 'ਤੇ ਕਲਿੱਕ ਕਰੋ  
5. ਪ੍ਰਦਾਨਗੀ ਲਈ 5-10 ਮਿੰਟ ਦੀ ਉਡੀਕ ਕਰੋ  
6. ਸ਼ੁਰੂ ਕਰਨ ਲਈ ਸਿੱਧਾ [ਤੁਰੰਤ ਸ਼ੁਰੂਆਤ](./00-quick-start/README.md) ਨੂੰ ਜਾਓ!

ਮੌਡਿਊਲਾਂ ਨੂੰ ਪੂਰਾ ਕਰਨ ਤੋਂ ਬਾਅਦ, LangChain4j ਟੈਸਟਿੰਗ ਧਾਰਣਾਵਾਂ ਨੂੰ ਕਾਰਵਾਈ ਵਿੱਚ ਵੇਖਣ ਲਈ [ਟੈਸਟਿੰਗ ਗਾਈਡ](docs/TESTING.md) ਦੀ ਜਾਂਚ ਕਰੋ।

> **ਨੋਟ:** ਇਹ ਪ੍ਰਸ਼ਿਕਸ਼ਣ GitHub ਮਾਡਲਾਂ ਅਤੇ Azure OpenAI ਦੋਹਾਂ ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ। [ਤੁਰੰਤ ਸ਼ੁਰੂਆਤ](00-quick-start/README.md) ਮੌਡਿਊਲ GitHub ਮਾਡਲਾਂ ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ (Azure ਦੀ ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਦੀ ਲੋੜ ਨਹੀਂ), ਜਦਕਿ ਮੌਡਿਊਲ 1-5 Azure OpenAI ਵਰਤਦੇ ਹਨ। ਜੇਕਰ ਤੁਹਾਡੇ ਕੋਲ ਨਹੀਂ ਹੈ, ਤਾਂ [ਮੁਫ਼ਤ Azure ਖਾਤਾ](https://aka.ms/azure-free-account) ਨਾਲ ਸ਼ੁਰੂ ਕਰੋ।


## GitHub Copilot ਨਾਲ ਸਿੱਖਣਾ

ਤੁਰੰਤ ਕੋਡਿੰਗ ਸ਼ੁਰੂ ਕਰਨ ਲਈ, ਇਸ ਪ੍ਰਾਜੈਕਟ ਨੂੰ GitHub Codespace ਜਾਂ ਆਪਣੇ ਸਥਾਨਕ IDE ਵਿੱਚ devcontainer ਦੀ ਵਰਤੋਂ ਨਾਲ ਖੋਲ੍ਹੋ। ਇਸ ਕੋਰਸ ਵਿੱਚ ਵਰਤੀ ਗਈ devcontainer ਪਹਿਲਾਂ ਹੀ GitHub Copilot ਨਾਲ AI ਜੋੜਤ ਭਰਤੀ ਲਈ ਸੰਰਚਿਤ ਹੈ।

ਹਰ ਕੋਡ ਉਦਾਹਰਨ ਵਿੱਚ ਕੁਝ ਸੁਝਾਏ ਗਏ ਪ੍ਰਸ਼ਨ ਸ਼ਾਮਿਲ ਹਨ ਜੋ ਤੁਸੀਂ GitHub Copilot ਨੂੰ ਪੁੱਛ ਸਕਦੇ ਹੋ ਆਪਣੀ ਸਮਝ ਨੂੰ ਗਹਿਰਾ ਕਰਨ ਲਈ। 💡/🤖 ਪ੍ਰਾਪਤੀਆਂ ਲਈ ਨਜ਼ਰ ਰੱਖੋ:

- **ਜਾਵਾ ਫਾਇਲ ਹੈਡਰਜ਼** - ਹਰ ਉਦਾਹਰਨ ਲਈ ਖਾਸ ਪ੍ਰਸ਼ਨ  
- **ਮੌਡਿਊਲ README** - ਕੋਡ ਉਦਾਹਰਣਾਂ ਤੋਂ ਬਾਅਦ ਖੋਜ ਪ੍ਰੇਰਣਾ  

**ਕਿਵੇਂ ਵਰਤਣਾ ਹੈ:** ਕਿਸੇ ਵੀ ਕੋਡ ਫਾਇਲ ਨੂੰ ਖੋਲ੍ਹੋ ਅਤੇ Copilot ਨੂੰ ਸੁਝਾਏ ਗਏ ਪ੍ਰਸ਼ਨ ਪੁੱਛੋ। ਇਸਨੂੰ ਕੋਡਬੇਸ ਦੀ ਪੂਰੀ ਸੰਦਰਭ ਜਾਣਕਾਰੀ ਹੁੰਦੀ ਹੈ ਅਤੇ ਇਹ ਵਿਆਖਿਆ ਕਰ ਸਕਦਾ ਹੈ, ਵਧਾ ਸਕਦਾ ਹੈ ਅਤੇ ਵਿਕਲਪ ਸੁਝਾ ਸਕਦਾ ਹੈ।

ਹੋਰ ਜਾਣਨ ਲਈ, [AI ਜੋੜਤ ਭਰਤੀ ਲਈ Copilot](https://aka.ms/GitHubCopilotAI) ਵੇਖੋ।


## ਵਾਧੂ ਸਰੋਤ

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
 
### Generative AI ਕ੍ਰਮ
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### ਕੋਰ ਸਿੱਖਣ
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot ਸੀਰੀਜ਼
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## ਮਦਦ ਲੈਣਾ

ਜੇ ਤੁਹਾਨੂੰ ਅਟਕ ਜਾਂ ਕੋਈ ਸਵਾਲ ਹੋਣ ਤਾਂ AI ਐਪਸ ਬਣਾਉਣ ਬਾਰੇ, ਸ਼ਾਮਿਲ ਹੋਵੋ:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

ਜੇ ਤੁਹਾਡੇ ਕੋਲ ਉਤਪਾਦ ਫੀਡਬੈਕ ਜਾਂ ਗਲਤੀਆਂ ਹਨ ਜਦੋ ਤੁਸੀਂ ਬਣਾਉਂਦੇ ਹੋ ਤਾਂ ਜਾਓ:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## ਲਾਇਸੈਂਸ

MIT ਲਾਇਸੈਂਸ - ਵੇਰਵਾ ਲਈ [LICENSE](../../LICENSE) ਫਾਇਲ ਵੇਖੋ।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਡਿਸਕਲੇਮਰ**:
ਇਹ ਦਸਤਾਵੇਜ਼ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦਿਤ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਿੱਥੇ ਅਸੀਂ ਸਹੀਅਤੇ ਲਈ ਕੋਸ਼ਿਸ਼ ਕਰਦੇ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਰੱਖੋ ਕਿ ਸਵੈਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਕੁਝ ਗਲਤੀਆਂ ਜਾਂ ਅਸਪਸ਼ਟਤਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਪ੍ਰਮੁੱਖ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਜਰੂਰੀ ਜਾਣਕਾਰੀ ਲਈ, ਪ੍ਰੋਫੈਸ਼ਨਲ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਿਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਅਸੀਂ ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਤੋਂ ਪੈਦਾ ਹੋਣ ਵਾਲੀ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀ ਜਾਂ ਗਲਤ ਸਮਝ ਲਈ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->