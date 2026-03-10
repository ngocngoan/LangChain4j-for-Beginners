<img src="../../translated_images/sw/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j kwa Waanzilishi

Kozi ya kujenga programu za AI kwa kutumia LangChain4j na Azure OpenAI GPT-5.2, kutoka mazungumzo ya msingi hadi wawakilishi wa AI.

### 🌐 Msaada wa Lugha Nyingi

#### Unaungwa mkono kupitia GitHub Action (Otometi & Daima Iliyosasishwa)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](./README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **Unapendelea Kunakili Kwenye Kompyuta Yako?**
>
> Hifadhidata hii ina tafsiri zaidi ya 50 za lugha ambazo huongeza kiasi cha kupakua kwa kiasi kikubwa. Ili kunakili bila tafsiri, tumia sparse checkout:
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
> Hii inakupa kila kitu unachohitaji kumaliza kozi kwa upakuaji wa haraka zaidi.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Jedwali la Maudhui

1. [Mwanzo Haraka](00-quick-start/README.md) - Anza na LangChain4j
2. [Utangulizi](01-introduction/README.md) - Jifunze misingi ya LangChain4j
3. [Uhandisi wa Prompt](02-prompt-engineering/README.md) - Fahamu namna ya kubuni prompt kwa ufanisi
4. [RAG (Uundaji ulioimarishwa kwa Upataji)](03-rag/README.md) - Jenga mifumo ya maarifa yenye akili
5. [Zana](04-tools/README.md) - Unganisha zana za nje na wasaidizi rahisi
6. [MCP (Itifaki ya Muktadha wa Mfano)](05-mcp/README.md) - Fanya kazi na Itifaki ya Muktadha wa Mfano (MCP) na moduli za Agentic

### Video za Maelekezo

Kila moduli ina kikao cha moja kwa moja kinachoshirikiana ambapo tunapitisha kwa hatua dhana na msimbo hatua kwa hatua.

| Moduli | Video |
|--------|-------|
| 01 - Utangulizi | [Anza Kutumia LangChain4j](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - Uhandisi wa Prompt | [Uhandisi wa Prompt na LangChain4j](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [RAG na LangChain4j](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - Zana & 05 - MCP | [Wawakilishi wa AI na Zana na MCP](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## Njia ya Kujifunza

**Mpya kwa LangChain4j?** Angalia [Fasira](docs/GLOSSARY.md) kwa maana za maneno muhimu na dhana.

> **Mwanzo Haraka**

1. Fanya fork ya hifadhidata hii kwa akaunti yako ya GitHub
2. Bonyeza **Code** → kichupo cha **Codespaces** → **...** → **Mpya na chaguzi...**
3. Tumia chaguo za chaguomsingi – hii itachagua kontena la Maendeleo lililoundwa kwa ajili ya kozi hii
4. Bonyeza **Tengeneza codespace**
5. Subiri dakika 5-10 mazingira yawe tayari
6. Ruka moja kwa moja kwenda [Mwanzo Haraka](./00-quick-start/README.md) kuanza!

Baada ya kumaliza moduli, chunguza [Mwongozo wa Kupima](docs/TESTING.md) kuona dhana za upimaji za LangChain4j zikiendeshwa.

> **Kumbuka:** Mafunzo haya yanatumia Modeli za GitHub na Azure OpenAI. Moduli ya [Mwanzo Haraka](00-quick-start/README.md) inatumia Modeli za GitHub (hakuna usajili wa Azure unahitajika), wakati moduli 1-5 zinatumia Azure OpenAI. Anza na [akaunti YA BURE ya Azure](https://aka.ms/azure-free-account) kama huna.

## Kujifunza kwa GitHub Copilot

Ili kuanza kuandika msimbo kwa haraka, fungua mradi huu kwenye GitHub Codespace au IDE yako ya ndani kwa kontena la maendeleo lililotolewa. Kontena lililotumika katika kozi hii limewekewa GitHub Copilot kwa usaidizi wa kuandika programu kwa pamoja na AI.

Kila mfano wa msimbo unajumuisha maswali yaliyopendekezwa unaweza kumuuliza GitHub Copilot ili kuongeza uelewa wako. Tafuta alama za 💡/🤖 katika:

- **Vichwa vya faili za Java** - Maswali maalum kwa kila mfano
- **README za Moduli** - Maswali ya uchunguzi baada ya mifano ya msimbo

**Jinsi ya kutumia:** Fungua faili lolote la msimbo na uliza Copilot maswali yaliyopendekezwa. Ina muktadha kamili wa msimbo na inaweza kuelezea, kuongeza, na kupendekeza mbadala.

Unataka kujifunza zaidi? Angalia [Copilot kwa Usanidi wa Programu kwa Pamoja na AI](https://aka.ms/GitHubCopilotAI).

## Rasilimali Zaidi

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j kwa Waanzilishi](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js kwa Waanzilishi](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain kwa Waanzilishi](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Wawakilishi
[![AZD kwa Waanzilishi](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI kwa Waanzilishi](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP kwa Waanzilishi](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Wawakilishi wa AI kwa Waanzilishi](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Mfululizo wa AI ya Kizazi
[![AI ya Kizazi kwa Waanzilishi](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI ya Kizazi (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![AI ya Kizazi (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![AI ya Kizazi (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Mafunzo ya Msingi
[![ML kwa Waanzilishi](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Sayansi ya Takwimu kwa Waanzilishi](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI kwa Waanzilishi](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Mfululizo wa Copilot
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Kupata Msaada

Ikiwa umekwama au una maswali yoyote kuhusu kujenga programu za AI, jiunge:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

Ikiwa unayo maoni ya bidhaa au makosa wakati wa kujenga tembelea:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## Leseni

Leseni ya MIT - Tazama faili la [LICENSE](../../LICENSE) kwa maelezo.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Maandishi ya Kutolea Maelezo**:
Hati hii imefasiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kwa usahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au upungufu katika usahihi. Hati ya asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu ya binadamu inashauriwa. Hatuchukui jukumu lolote kwa matatizo au tafsiri potofu yanayotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->