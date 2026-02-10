<img src="../../translated_images/hr/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

### 🌐 Višejezična podrška

#### Podržano putem GitHub akcije (Automatski i uvijek ažurirano)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[arapski](../ar/README.md) | [bengalski](../bn/README.md) | [bugarski](../bg/README.md) | [burmanski (Mjanmar)](../my/README.md) | [kineski (pojednostavljeni)](../zh-CN/README.md) | [kineski (tradicionalni, Hong Kong)](../zh-HK/README.md) | [kineski (tradicionalni, Makao)](../zh-MO/README.md) | [kineski (tradicionalni, Tajvan)](../zh-TW/README.md) | [hrvatski](./README.md) | [češki](../cs/README.md) | [danske](../da/README.md) | [nizozemski](../nl/README.md) | [estski](../et/README.md) | [finski](../fi/README.md) | [francuski](../fr/README.md) | [njemački](../de/README.md) | [grčki](../el/README.md) | [hebrejski](../he/README.md) | [hindski](../hi/README.md) | [mađarski](../hu/README.md) | [indonezijski](../id/README.md) | [talijanski](../it/README.md) | [japanski](../ja/README.md) | [kanadski](../kn/README.md) | [korejski](../ko/README.md) | [litvanski](../lt/README.md) | [malajski](../ms/README.md) | [malajalamski](../ml/README.md) | [maratški](../mr/README.md) | [nepalski](../ne/README.md) | [nigerijski pidžin](../pcm/README.md) | [norveški](../no/README.md) | [perzijski (farsi)](../fa/README.md) | [poljski](../pl/README.md) | [portugalski (Brazil)](../pt-BR/README.md) | [portugalski (Portugal)](../pt-PT/README.md) | [punjabi (gurmukhi)](../pa/README.md) | [rumunjski](../ro/README.md) | [ruski](../ru/README.md) | [srpski (ćirilica)](../sr/README.md) | [slovački](../sk/README.md) | [slovenski](../sl/README.md) | [španjolski](../es/README.md) | [svahili](../sw/README.md) | [švedski](../sv/README.md) | [tagalog (filipinski)](../tl/README.md) | [tamilski](../ta/README.md) | [telugu](../te/README.md) | [tajlandski](../th/README.md) | [turski](../tr/README.md) | [ukrajinski](../uk/README.md) | [urdu](../ur/README.md) | [vijetnamski](../vi/README.md)

> **Radije želite klonirati lokalno?**

> Ovaj repozitorij uključuje prijevode na više od 50 jezika što značajno povećava veličinu preuzimanja. Da biste klonirali bez prijevoda, koristite sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/LangChain4j-for-Beginners.git
> cd LangChain4j-for-Beginners
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Ovo vam daje sve što vam treba za dovršetak tečaja s mnogo bržim preuzimanjem.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

# LangChain4j za početnike

Tečaj za izgradnju AI aplikacija s LangChain4j i Azure OpenAI GPT-5.2, od osnovnog chatbota do AI agenata.

**Niste upoznati s LangChain4j?** Pogledajte [Rječnik](docs/GLOSSARY.md) za definicije ključnih pojmova i koncepata.

## Sadržaj

1. [Brzi početak](00-quick-start/README.md) - Počnite s LangChain4j
2. [Uvod](01-introduction/README.md) - Naučite osnove LangChain4j
3. [Izrada upita (Prompt Engineering)](02-prompt-engineering/README.md) - Savladajte učinkoviti dizajn upita
4. [RAG (Generiranje nadopunjeno dohvaćanjem)](03-rag/README.md) - Izgradite inteligentne sustave temeljene na znanju
5. [Alati](04-tools/README.md) - Integrirajte vanjske alate i jednostavne asistente
6. [MCP (Protokol konteksta modela)](05-mcp/README.md) - Radite s Protokolom konteksta modela (MCP) i agentskim modulima
---

## Put učenja

> **Brzi početak**

1. Forkajte ovaj repozitorij na svoj GitHub račun
2. Kliknite **Code** → kartica **Codespaces** → **...** → **New with options...**
3. Koristite zadane postavke – odabrat će se razvojno okruženje kreirano za ovaj tečaj
4. Kliknite **Create codespace**
5. Pričekajte 5-10 minuta da se okruženje pripremi
6. Odmah krenite na [Brzi početak](./00-quick-start/README.md) da započnete!

Nakon završetka modula, istražite [Vodič za testiranje](docs/TESTING.md) da vidite koncepte testiranja LangChain4j u praksi.

> **Napomena:** Ova obuka koristi i GitHub modele i Azure OpenAI. Modul [Brzi početak](00-quick-start/README.md) koristi GitHub modele (nije potrebna Azure pretplata), dok moduli 1-5 koriste Azure OpenAI. Započnite s [BESPLATNIM Azure računom](https://aka.ms/azure-free-account) ako ga nemate.


## Učenje uz GitHub Copilot

Za brz početak programiranja otvorite ovaj projekt u GitHub Codespace-u ili u svom lokalnom IDE-u s priloženim devcontainer-om. Devcontainer korišten u ovom tečaju dolazi unaprijed konfiguriran s GitHub Copilot-om za AI programsko uparivanje.

Svaki primjer koda uključuje predložena pitanja koja možete postaviti GitHub Copilotu kako biste produbili svoje razumijevanje. Potražite 💡/🤖 upite u:

- **zaglavljima Java datoteka** - pitanja specifična za svaki primjer
- **README datotekama modula** - pitanja za istraživanje nakon primjera koda

**Kako koristiti:** Otvorite bilo koju datoteku s kodom i postavite Copilotu predložena pitanja. Ima potpuni kontekst koda i može objasniti, proširiti i predložiti alternative.

Želite li saznati više? Pogledajte [Copilot za AI programsko uparivanje](https://aka.ms/GitHubCopilotAI).


## Dodatni resursi

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j za početnike](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js za početnike](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain za početnike](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agenti
[![AZD za početnike](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI za početnike](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP za početnike](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agenti za početnike](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Serija generativne AI
[![Generativna AI za početnike](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generativna AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generativna AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generativna AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Osnovno učenje
[![ML za početnike](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science za početnike](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI za početnike](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Kibernetička sigurnost za početnike](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web razvoj za početnike](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot serija
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Dobivanje pomoći

Ako zapnete ili imate pitanja o izradi AI aplikacija, pridružite se:

[![Azure AI Foundry Discord](https://img.shields.io/badge/Discord-Azure_AI_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

Ako imate povratne informacije o proizvodu ili pogreške tijekom izrade posjetite:

[![Azure AI Foundry Developer Forum](https://img.shields.io/badge/GitHub-Azure_AI_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## Licenca

MIT licenca - Pogledajte datoteku [LICENSE](../../LICENSE) za detalje.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje od odgovornosti**:
Ovaj je dokument preveden korištenjem AI prevoditeljskog servisa [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo osigurati točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati službenim i autoritativnim izvorom. Za kritične informacije preporuča se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakva nesporazuma ili pogrešne interpretacije koje proizlaze iz uporabe ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->