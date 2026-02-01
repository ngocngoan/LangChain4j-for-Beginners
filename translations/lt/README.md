<img src="../../translated_images/lt/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 Daugiakalbė parama

#### Palaikoma per GitHub Action (automatizuota ir visada atnaujinama)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](./README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **Norite kopijuoti vietoje?**

> Šiame saugykloje yra daugiau nei 50 kalbų vertimų, dėl kurių žymiai padidėja atsisiuntimo dydis. Norėdami kopijuoti be vertimų, naudokite ribotą atsisiuntimą (sparse checkout):
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Tai suteiks viską, ko reikia kursui užbaigti, žymiai greičiau atsisiunčiant.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4j pradedantiesiems

Kursas apie AI taikomųjų programų kūrimą naudojant LangChain4j ir Azure OpenAI GPT-5, pradedant nuo pagrindinių pokalbių iki AI agentų.

**Naujas LangChain4j?** Peržiūrėkite [Žodyną](docs/GLOSSARY.md), kuriame pateikti pagrindinių terminų ir sąvokų apibrėžimai.

## Turinys

1. [Greitas pradžia](00-quick-start/README.md) – Pradėkite naudotis LangChain4j
2. [Įvadas](01-introduction/README.md) – Sužinokite LangChain4j pagrindus
3. [Klausimų kūrimas](02-prompt-engineering/README.md) – Įvaldykite veiksmingą klausimų kūrimą
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) – Kurkite išmanias žinių sistemas
5. [Įrankiai](04-tools/README.md) – Integruokite išorinius įrankius ir paprastus padėjėjus
6. [MCP (Model Context Protocol)](05-mcp/README.md) – Dirbkite su Modelio konteksto protokolu (MCP) ir agentų moduliais
---

## Mokymosi kelias

> **Greitas pradžia**

1. Pasidarykite fork’ą šios saugyklos savo GitHub paskyroje
2. Spustelėkite **Code** → **Codespaces** skiltį → **...** → **Naujas su parinktimis...**
3. Naudokite numatytuosius nustatymus – taip bus pasirinktas šiam kursui sukurtas vystymo konteineris
4. Spustelėkite **Sukurti codespace**
5. Palaukite 5–10 minučių, kol aplinka pasirodys paruošta
6. Tiesiogiai pereikite prie [Greitos pradžios](./00-quick-start/README.md), kad pradėtumėte!

Įveikę modulius, išnagrinėkite [Testavimo vadovą](docs/TESTING.md), kad pamatytumėte LangChain4j testavimo koncepcijas veikiant.

> **Pastaba:** Šis mokymas naudoja tiek GitHub Modelius, tiek Azure OpenAI. [Greitos pradžios](00-quick-start/README.md) modulis naudoja GitHub Modelius (nereikia Azure prenumeratos), o 1–5 moduliai naudoja Azure OpenAI. Jei dar neturite, pradėkite su [nemokama Azure paskyra](https://aka.ms/azure-free-account).


## Mokymasis su GitHub Copilot

Norėdami greitai pradėti rašyti kodą, atidarykite šį projektą GitHub Codespace arba savo vietinėje IDE su pateiktu devcontainer. Šiame kurse naudojamas devcontainer yra iš anksto sukonfigūruotas su GitHub Copilot AI porinėje programavimo aplinkoje.

Kiekviename kodo pavyzdyje rasite rekomenduojamus klausimus, kuriuos galite užduoti GitHub Copilot, kad gilintumėte supratimą. Ieškokite 💡/🤖 paskatų:

- **Java failų antraštėse** – klausimai, skirti konkretiems pavyzdžiams
- **Modulių README** – tyrinėjimo paskatos po kodo pavyzdžių

**Kaip naudoti:** Atidarykite bet kurį kodo failą ir užduokite Copilot pateiktus klausimus. Jis turi pilną kodo pagrindo kontekstą ir gali paaiškinti, išplėsti ir pasiūlyti alternatyvų.

Norite sužinoti daugiau? Peržiūrėkite [Copilot AI poriniam programavimui](https://aka.ms/GitHubCopilotAI).


## Papildomi ištekliai

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j pradedantiesiems](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js pradedantiesiems](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agentai
[![AZD pradedantiesiems](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI pradedantiesiems](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP pradedantiesiems](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI agentai pradedantiesiems](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generatyvinis AI serija
[![Generatyvinis AI pradedantiesiems](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generatyvinis AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generatyvinis AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generatyvinis AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Pagrindinis mokymasis
[![ML pradedantiesiems](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Duomenų mokslo pradedantiesiems](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI pradedantiesiems](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Kibernetinis saugumas pradedantiesiems](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Interneto kūrimas pradedantiesiems](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![Daiktų internetas pradedantiesiems](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot serija
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Pagalbos gavimas

Jei įstringate arba turite klausimų apie AI programų kūrimą, prisijunkite prie:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

Jei turite atsiliepimų apie produktą arba randate klaidų kūrimo metu, apsilankykite:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## Licencija

MIT licencija - detales žr. faile [LICENSE](../../LICENSE).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas gimtąja kalba laikomas autoritetingu šaltiniu. Kritinei informacijai rekomenduojamas profesionalus vertimas žmogaus. Mes neatsakome už jokius nesusipratimus ar neteisingą interpretavimą, kilusius dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->