<img src="../../translated_images/sw/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 Msaada wa Lugha Nyingi

#### Unaungwa Mkono kupitia GitHub Action (Otomatiki & Daima Imesasishwa)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](./README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **Unapendelea Kurejelea Kwenye Kompyuta Binafsi?**

> Hifadhi hii ina tafsiri za lugha 50+ ambazo huongeza kiasi cha kupakua kwa kiasi kikubwa. Ili kurejelea bila tafsiri, tumia sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Hii inakupa kila kitu unachohitaji kukamilisha kozi kwa upakuaji wa kasi zaidi.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4j kwa Waanzilishi

Kozi ya kujenga programu za AI kwa LangChain4j na Azure OpenAI GPT-5, kutoka mazungumzo ya msingi hadi mawakala wa AI.

**Mpya kwa LangChain4j?** Angalia [Kamusi ya Maneno](docs/GLOSSARY.md) kwa ufafanuzi wa maneno muhimu na dhana.

## Jedwali la Yaliyomo

1. [Kuanza Haraka](00-quick-start/README.md) - Anza na LangChain4j
2. [Utangulizi](01-introduction/README.md) - Jifunze misingi ya LangChain4j
3. [Uhandisi wa Prompt](02-prompt-engineering/README.md) - Fahamu muundo mzuri wa prompt
4. [RAG (Kizazi cha Kuongezwa kwa Urejeshaji)](03-rag/README.md) - Jenga mifumo ya akili inayotegemea maarifa
5. [Zana](04-tools/README.md) - Unganisha zana za nje na wasaidizi rahisi
6. [MCP (Itifaki ya Muktadha wa Mfano)](05-mcp/README.md) - Fanya kazi na Itifaki ya Muktadha wa Mfano (MCP) na moduli za Agentic
---

## Njia ya Kujifunza

> **Kuanza Haraka**

1. Tengeneza nakala ya hifadhi hii kwenye akaunti yako ya GitHub
2. Bonyeza **Code** → kichupo cha **Codespaces** → **...** → **Mpya na chaguzi...**
3. Tumia chaguo za kawaida – hii itachagua chombo cha Maendeleo kilichotengenezwa kwa kozi hii
4. Bonyeza **Tengeneza codespace**
5. Subiri dakika 5-10 kwa mazingira kuwa tayari
6. Nenda moja kwa moja kwenye [Kuanza Haraka](./00-quick-start/README.md) kuanza!

Baada ya kumaliza moduli, chunguza [Mwongozo wa Upimaji](docs/TESTING.md) kuona dhana za upimaji za LangChain4j zikifanyika.

> **Kumbuka:** Mafunzo haya yanatumia GitHub Models na Azure OpenAI. Moduli ya [Kuanza Haraka](00-quick-start/README.md) inatumia GitHub Models (hakuna usajili wa Azure unahitajika), wakati moduli 1-5 zinatumia Azure OpenAI. Anza na [akaunti ya bure ya Azure](https://aka.ms/azure-free-account) kama huna.

## Kujifunza kwa GitHub Copilot

Ili kuanza kuandika msimbo haraka, fungua mradi huu katika GitHub Codespace au IDE yako ya ndani na devcontainer iliyotolewa. Devcontainer inayotumiwa katika kozi hii imesakinishwa tayari na GitHub Copilot kwa programu zilizounganishwa za AI.

Kila mfano wa msimbo unajumuisha maswali yanayopendekezwa ambayo unaweza kumuuliza GitHub Copilot ili kuongeza uelewa wako. Tazama vidokezo vya 💡/🤖 katika:

- **Vichwa vya faili za Java** - Maswali maalum kwa kila mfano
- **README za Moduli** - Vidokezo vya uchunguzi baada ya mifano ya msimbo

**Jinsi ya kutumia:** Fungua faili lolote la msimbo na muuulize Copilot maswali yaliyopendekezwa. Ina muktadha kamili wa msimbo na inaweza kueleza, kupanua, na kupendekeza mbadala.

Unataka kujifunza zaidi? Angalia [Copilot kwa Programu za AI Paired](https://aka.ms/GitHubCopilotAI).

## Rasilimali Zaidi

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j kwa Waanzilishi](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js kwa Waanzilishi](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Mawakala
[![AZD kwa Waanzilishi](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI wa Edge kwa Waanzilishi](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP kwa Waanzilishi](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Mawakala wa AI kwa Waanzilishi](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Mfululizo wa AI ya Kizazi
[![AI ya Kizazi kwa Waanzilishi](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI ya Kizazi (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![AI ya Kizazi (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![AI ya Kizazi (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Msingi wa Kujifunza
[![ML kwa Waanzilishi](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Sayansi ya Data kwa Waanzilishi](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI kwa Waanzilishi](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Usalama wa Mtandao kwa Waanzilishi](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Uendelezaji wa Mtandao kwa Waanzilishi](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT kwa Waanzilishi](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Mfululizo wa Copilot
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Kupata Msaada

Ikiwa utakwama au una maswali kuhusu kujenga programu za AI, jiunge:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

Ikiwa una maoni kuhusu bidhaa au makosa wakati wa kujenga tembelea:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## Leseni

Leseni ya MIT - Angalia faili la [LICENSE](../../LICENSE) kwa maelezo.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Onyo**:
Nyaraka hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuwa sahihi, tafadhali fahamu kwamba tafsiri za otomatiki zinaweza kuwa na makosa au kasoro. Nyaraka asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu kutoka kwa binadamu inashauriwa. Hatuwezi kuchukuliwa kuwa na dhamana kwa kutoelewana au tafsiri isiyo sahihi inayotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->