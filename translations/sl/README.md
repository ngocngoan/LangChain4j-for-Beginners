<img src="../../translated_images/sl/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j za začetnike

Tečaj za gradnjo AI aplikacij z LangChain4j in Azure OpenAI GPT-5.2, od osnovnega klepeta do AI agentov.

### 🌐 Podpora za več jezikov

#### Podprto preko GitHub Action (avtomatizirano in vedno posodobljeno)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Khmer](../km/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](./README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **Raje klonirate lokalno?**
>
> To repozitorij vsebuje več kot 50 jezikovnih prevodov, kar znatno poveča velikost prenosa. Če želite klonirati brez prevodov, uporabite sparse checkout:
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
> To vam omogoča, da dobite vse, kar potrebujete za dokončanje tečaja z veliko hitrejšim prenosom.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Kazalo vsebine

1. [Hitri začetek](00-quick-start/README.md) - Začnite z LangChain4j
2. [Uvod](01-introduction/README.md) - Spoznajte osnove LangChain4j
3. [Oblikovanje povpraševanja](02-prompt-engineering/README.md) - Obvladajte učinkovito oblikovanje povpraševanja
4. [RAG (Generiranje z iskanjem)](03-rag/README.md) - Gradite inteligentne sisteme na osnovi znanja
5. [Orodja](04-tools/README.md) - Integrirajte zunanja orodja in preproste asistente
6. [MCP (Protokol konteksta modela)](05-mcp/README.md) - Delajte s Protokolom konteksta modela (MCP) in Agentskimi moduli

### Video vodenja

Vsak modul ima spremljevalno živo sejo, kjer korak za korakom obravnavamo koncepte in kodo.

| Modul | Video |
|--------|-------|
| 01 - Uvod | [Začetek z LangChain4j](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - Oblikovanje povpraševanja | [Oblikovanje povpraševanja z LangChain4j](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [RAG z LangChain4j](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - Orodja & 05 - MCP | [AI agenti z orodji in MCP](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## Učna pot

**Nova v LangChain4j?** Oglejte si [Slovarček](docs/GLOSSARY.md) za definicije ključnih izrazov in konceptov.

> **Hitri začetek**

1. Razvežite ta repozitorij v svoj GitHub račun
2. Kliknite **Code** → zavihek **Codespaces** → **...** → **New with options...**
3. Uporabite privzete nastavitve – izbralo bo razvojno posodo, ustvarjeno za ta tečaj
4. Kliknite **Create codespace**
5. Počakajte 5-10 minut, da je okolje pripravljeno
6. Pojdite neposredno na [Hitri začetek](./00-quick-start/README.md) in začnite!

Po končanih modulih raziskujte [Vodnik za testiranje](docs/TESTING.md), da si ogledate koncepte testiranja LangChain4j v praksi.

> **Opomba:** Ta usposabljanje uporablja tako GitHub modele kot Azure OpenAI. Modul [Hitri začetek](00-quick-start/README.md) uporablja GitHub modele (brez potrebe po naročnini na Azure), medtem ko moduli 1-5 uporabljajo Azure OpenAI. Začnite z [BREZPLAČNIM Azure računom](https://aka.ms/azure-free-account), če ga še nimate.


## Učenje s pomočjo GitHub Copilot

Za hiter začetek programiranja odprite ta projekt v GitHub Codespace ali v lokalnem IDE z uporabo priloženega devcontainerja. Devcontainer, uporabljen v tem tečaju, je predkonfiguriran z GitHub Copilot za AI so-pisanje kode.

Vsak primer kode vsebuje predlagana vprašanja, ki jih lahko zastavite GitHub Copilotu za poglobljeno razumevanje. Poiščite oznake 💡/🤖 v:

- **Glavah Java datotek** - vprašanja specifična za vsak primer
- **README-jih modulov** - raziskovalna vprašanja po primerih kode

**Kako uporabljati:** Odprite katerokoli datoteko s kodo in vprašajte Copilota predlagana vprašanja. Ima celoten kontekst kode in lahko pojasni, razširi in predlaga alternative.

Želite izvedeti več? Oglejte si [Copilot za AI so-pisanje kode](https://aka.ms/GitHubCopilotAI).


## Dodatni viri

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j za začetnike](https://img.shields.io/badge/LangChain4j%20za%20začetnike-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js za začetnike](https://img.shields.io/badge/LangChain.js%20za%20začetnike-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain za začetnike](https://img.shields.io/badge/LangChain%20za%20začetnike-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agent
[![AZD za začetnike](https://img.shields.io/badge/AZD%20za%20začetnike-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI za začetnike](https://img.shields.io/badge/Edge%20AI%20za%20začetnike-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP za začetnike](https://img.shields.io/badge/MCP%20za%20začetnike-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI agenti za začetnike](https://img.shields.io/badge/AI%20agenti%20za%20začetnike-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Serija Generativne AI
[![Generativna AI za začetnike](https://img.shields.io/badge/Generativna%20AI%20za%20začetnike-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generativna AI (.NET)](https://img.shields.io/badge/Generativna%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generativna AI (Java)](https://img.shields.io/badge/Generativna%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generativna AI (JavaScript)](https://img.shields.io/badge/Generativna%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Osnovno učenje
[![ML za začetnike](https://img.shields.io/badge/ML%20za%20začetnike-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Podatkovna znanost za začetnike](https://img.shields.io/badge/Podatkovna%20znanost%20za%20začetnike-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI za začetnike](https://img.shields.io/badge/AI%20za%20začetnike-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Kibernetska varnost za začetnike](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Spletni razvoj za začetnike](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT za začetnike](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR razvoj za začetnike](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Serija Copilot
[![Copilot za AI programiranje v paru](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot za C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot avantura](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Iskanje pomoči

Če zataknete ali imate kakršnakoli vprašanja o izdelavi AI aplikacij, se pridružite:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

Če imate povratne informacije o izdelku ali napake med razvojem, obiščite:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## Licenca

Licenca MIT - podrobnosti si oglejte v datoteki [LICENSE](../../LICENSE).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:  
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas opozarjamo, da avtomatizirani prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvirnem jeziku je treba razumeti kot avtoritativni vir. Za kritične informacije priporočamo strokovni človeški prevod. Za morebitne nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda, ne prevzemamo nobene odgovornosti.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->