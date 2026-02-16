<img src="../../translated_images/tl/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 Suporta sa Maramihang Wika

#### Sinusuportahan sa pamamagitan ng GitHub Action (Awtomatiko at Laging Napapanahon)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](./README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **Mas gusto mo bang I-clone Lokal?**
>
> Ang repositoryo na ito ay naglalaman ng 50+ na pagsasalin ng wika na malaki ang dinadagdag sa laki ng pag-download. Para i-clone nang walang mga pagsasalin, gamitin ang sparse checkout:
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
> Bibigyan ka nito ng lahat ng kailangan mo para makumpleto ang kurso nang mas mabilis ang pag-download.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

> **Mas gusto mo bang I-clone Lokal?**
>
> Ang repositoryo na ito ay naglalaman ng 50+ na pagsasalin ng wika na malaki ang dinadagdag sa laki ng pag-download. Para i-clone nang walang mga pagsasalin, gamitin ang sparse checkout:
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
> Bibigyan ka nito ng lahat ng kailangan mo para makumpleto ang kurso nang mas mabilis ang pag-download.

# LangChain4j para sa mga Nagsisimula

Isang kurso para sa paggawa ng mga AI na aplikasyon gamit ang LangChain4j at Azure OpenAI GPT-5.2, mula sa basic chat hanggang sa mga AI agents.

**Bago ka sa LangChain4j?** Tingnan ang [Glossary](docs/GLOSSARY.md) para sa mga kahulugan ng mga pangunahing termino at konsepto.

## Talaan ng Nilalaman

1. [Mabilis na Pagsisimula](00-quick-start/README.md) - Simulan ang paggamit ng LangChain4j
2. [Introduksyon](01-introduction/README.md) - Alamin ang mga pundamental ng LangChain4j
3. [Prompt Engineering](02-prompt-engineering/README.md) - Maging bihasa sa epektibong disenyo ng prompt
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - Bumuo ng mga matalinong sistema batay sa kaalaman
5. [Mga Tools](04-tools/README.md) - Isama ang mga panlabas na kagamitan at simpleng mga katulong
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Magtrabaho gamit ang Model Context Protocol (MCP) at mga Agentic na module
---

## Landas ng Pagkatuto

> **Mabilis na Pagsisimula**

1. I-fork ang repositoryong ito sa iyong GitHub account
2. Pindutin ang **Code** → tab na **Codespaces** → **...** → **New with options...**
3. Gamitin ang mga default – pipiliin nito ang Development container na ginawa para sa kurso na ito
4. Pindutin ang **Create codespace**
5. Maghintay ng 5-10 minuto para maging handa ang kapaligiran
6. Direktang pumunta sa [Mabilis na Pagsisimula](./00-quick-start/README.md) upang magsimula!

Pagkatapos makumpleto ang mga module, tuklasin ang [Testing Guide](docs/TESTING.md) upang makita ang mga konsepto ng pagsubok sa LangChain4j na nasa aksyon.

> **Tandaan:** Ang pagsasanay na ito ay gumagamit ng parehong GitHub Models at Azure OpenAI. Ang [Mabilis na Pagsisimula](00-quick-start/README.md) na module ay gumagamit ng GitHub Models (hindi kailangan ng Azure subscription), habang ang mga module 1-5 ay gumagamit ng Azure OpenAI. Magsimula sa isang [LIBRENG Azure account](https://aka.ms/azure-free-account) kung wala ka pa nito.


## Pagkatuto gamit ang GitHub Copilot

Para mabilis na makapag-code, buksan ang proyektong ito sa GitHub Codespace o sa iyong lokal na IDE gamit ang ibinigay na devcontainer. Ang devcontainer na ginamit sa kurso na ito ay naka-pre-configure na may GitHub Copilot para sa AI paired programming.

Bawat halimbawa ng code ay may kasamang mga mungkahing tanong na maaari mong itanong sa GitHub Copilot para lumalim ang iyong pag-unawa. Hanapin ang mga 💡/🤖 na mga prompt sa:

- **Mga header ng Java file** - Mga tanong na partikular sa bawat halimbawa
- **Module READMEs** - Mga prompt para sa eksplorasyon pagkatapos ng mga halimbawa ng code

**Paano gamitin:** Buksan ang anumang code file at itanong sa Copilot ang mga mungkahing tanong. Mayroon itong buong konteksto ng codebase at maaaring magpaliwanag, magpalawak, at magmungkahi ng mga alternatibo.

Gusto mo bang matuto pa? Tingnan ang [Copilot para sa AI Paired Programming](https://aka.ms/GitHubCopilotAI).


## Karagdagang Mga Mapagkukunan

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j para sa Mga Nagsisimula](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js para sa Mga Nagsisimula](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain para sa Mga Nagsisimula](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agents
[![AZD para sa Mga Nagsisimula](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI para sa Mga Nagsisimula](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP para sa Mga Nagsisimula](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents para sa Mga Nagsisimula](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generative AI Series
[![Generative AI para sa Mga Nagsisimula](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Core Learning
[![ML para sa Mga Nagsisimula](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science para sa Mga Nagsisimula](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI para sa Mga Nagsisimula](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity para sa Mga Nagsisimula](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)

[![Web Dev para sa mga Nagsisimula](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT para sa mga Nagsisimula](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development para sa mga Nagsisimula](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot Series
[![Copilot para sa AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot para sa C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Pagkuha ng Tulong

Kung ikaw ay natigil o may mga tanong tungkol sa paggawa ng AI apps, sumali sa:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

Kung mayroon kang produkto feedback o mga error habang nagtatayo, bisitahin ang:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## Lisensya

MIT Lisensya - Tingnan ang [LICENSE](../../LICENSE) na file para sa mga detalye.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pahayag ng Paunawa**:
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagamat nagsusumikap kami na maging tumpak, mangyaring tandaan na maaaring may mga pagkakamali o hindi pagkakatugma ang automated na pagsasalin. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasaling-wika ng tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->