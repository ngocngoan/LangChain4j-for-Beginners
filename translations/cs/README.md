<img src="../../translated_images/cs/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j pro začátečníky

Kurz pro tvorbu AI aplikací s LangChain4j a Azure OpenAI GPT-5.2, od základního chatu až po AI agenty.

### 🌐 Podpora více jazyků

#### Podporováno přes GitHub Action (Automatizováno a vždy aktuální)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[arabština](../ar/README.md) | [bengálština](../bn/README.md) | [bulharština](../bg/README.md) | [barmský (Myanmar)](../my/README.md) | [čínština (zjednodušená)](../zh-CN/README.md) | [čínština (tradiční, Hongkong)](../zh-HK/README.md) | [čínština (tradiční, Macao)](../zh-MO/README.md) | [čínština (tradiční, Taiwan)](../zh-TW/README.md) | [chorvatština](../hr/README.md) | [čeština](./README.md) | [dánština](../da/README.md) | [nizozemština](../nl/README.md) | [estonština](../et/README.md) | [finština](../fi/README.md) | [francouzština](../fr/README.md) | [němčina](../de/README.md) | [řečtina](../el/README.md) | [hebrejština](../he/README.md) | [hindština](../hi/README.md) | [maďarština](../hu/README.md) | [indonéština](../id/README.md) | [italština](../it/README.md) | [japonština](../ja/README.md) | [kannadština](../kn/README.md) | [korejština](../ko/README.md) | [litevština](../lt/README.md) | [malajština](../ms/README.md) | [malajálam](../ml/README.md) | [maráthština](../mr/README.md) | [nepálština](../ne/README.md) | [nigerijská pygmžština](../pcm/README.md) | [norský](../no/README.md) | [perština (Farsi)](../fa/README.md) | [polština](../pl/README.md) | [portugalština (Brazílie)](../pt-BR/README.md) | [portugalština (Portugalsko)](../pt-PT/README.md) | [paňdžábština (Gurmukhi)](../pa/README.md) | [rumunština](../ro/README.md) | [ruština](../ru/README.md) | [srbština (cyrilice)](../sr/README.md) | [slovenština](../sk/README.md) | [slovinština](../sl/README.md) | [španělština](../es/README.md) | [svahilština](../sw/README.md) | [švédština](../sv/README.md) | [tagalog (filipínština)](../tl/README.md) | [tamilština](../ta/README.md) | [telugština](../te/README.md) | [thajština](../th/README.md) | [turečtina](../tr/README.md) | [ukrajinština](../uk/README.md) | [urdština](../ur/README.md) | [vietnamsky](../vi/README.md)

> **Preferujete klonování lokálně?**
>
> Toto úložiště obsahuje více než 50 překladů jazyků, což výrazně zvětšuje velikost stahování. Pro klonování bez překladů používejte sparse checkout:
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
> Tak získáte vše potřebné k dokončení kurzu s mnohem rychlejším stažením.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Obsah

1. [Rychlý start](00-quick-start/README.md) - Začněte s LangChain4j
2. [Úvod](01-introduction/README.md) - Naučte se základy LangChain4j
3. [Návrh promptů](02-prompt-engineering/README.md) - Ovládněte efektivní tvorbu promptů
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - Vytvářejte inteligentní systémy založené na znalostech
5. [Nástroje](04-tools/README.md) - Integrujte externí nástroje a jednoduché asistenty
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Pracujte s Modelem Context Protocol (MCP) a agentními moduly

### Video návody

Ke každému modulu patří živá relace, kde krok za krokem projdeme koncepty a kód.

| Modul | Video |
|--------|-------|
| 01 - Úvod | [Začínáme s LangChain4j](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - Návrh promptů | [Návrh promptů s LangChain4j](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [RAG s LangChain4j](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - Nástroje & 05 - MCP | [AI Agentů s Nástroji a MCP](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## Vzdělávací cesta

**Nový v LangChain4j?** Podívejte se na [Glossář](docs/GLOSSARY.md) pro definice klíčových termínů a konceptů.

> **Rychlý start**

1. Vytvořte fork tohoto úložiště na svůj GitHub účet
2. Klikněte na **Code** → záložka **Codespaces** → **...** → **Nový s možnostmi...**
3. Použijte výchozí hodnoty – to vybere vývojové kontejner vytvořené pro tento kurz
4. Klikněte na **Vytvořit codespace**
5. Počkejte 5-10 minut, než bude prostředí připraveno
6. Rovnou přejděte na [Rychlý start](./00-quick-start/README.md) a začněte!

Po dokončení modulů prozkoumejte [Průvodce testováním](docs/TESTING.md), kde uvidíte koncepty testování LangChain4j v praxi.

> **Poznámka:** Toto školení využívá jak GitHub Modely, tak Azure OpenAI. Modul [Rychlý start](00-quick-start/README.md) používá GitHub Modely (není potřeba předplatné Azure), zatímco moduly 1-5 využívají Azure OpenAI. Začněte s [ZDARMA Azure účtem](https://aka.ms/azure-free-account), pokud jej ještě nemáte.


## Výuka s GitHub Copilot

Pro rychlý začátek kódování otevřete tento projekt v GitHub Codespace nebo ve svém lokálním IDE s poskytnutým devcontainerem. Devcontainer použitý v tomto kurzu je předkonfigurován s GitHub Copilot pro AI programování v páru.

Každý kódový příklad obsahuje navržené otázky, které můžete GitHub Copilotovi položit pro hlubší porozumění. Hledejte 💡/🤖 výzvy v:

- **Hlavičkách Java souborů** - otázky specifické pro každý příklad
- **README moduly** - průzkumné otázky po kódových příkladech

**Jak používat:** Otevřete jakýkoliv kódový soubor a položte Copilotovi navržené otázky. Má plný kontext kódu a může vysvětlit, rozšířit či navrhnout alternativy.

Chcete se dozvědět více? Podívejte se na [Copilot pro AI programování v páru](https://aka.ms/GitHubCopilotAI).


## Další zdroje

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j pro začátečníky](https://img.shields.io/badge/LangChain4j%20pro%20začátečníky-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js pro začátečníky](https://img.shields.io/badge/LangChain.js%20pro%20začátečníky-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain pro začátečníky](https://img.shields.io/badge/LangChain%20pro%20začátečníky-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agenti
[![AZD pro začátečníky](https://img.shields.io/badge/AZD%20pro%20začátečníky-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI pro začátečníky](https://img.shields.io/badge/Edge%20AI%20pro%20začátečníky-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP pro začátečníky](https://img.shields.io/badge/MCP%20pro%20začátečníky-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agenti pro začátečníky](https://img.shields.io/badge/AI%20Agenti%20pro%20začátečníky-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Série Generativní AI
[![Generativní AI pro začátečníky](https://img.shields.io/badge/Generativní%20AI%20pro%20začátečníky-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generativní AI (.NET)](https://img.shields.io/badge/Generativní%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generativní AI (Java)](https://img.shields.io/badge/Generativní%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generativní AI (JavaScript)](https://img.shields.io/badge/Generativní%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Základní vzdělávání
[![ML pro začátečníky](https://img.shields.io/badge/ML%20pro%20začátečníky-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science pro začátečníky](https://img.shields.io/badge/Data%20Science%20pro%20začátečníky-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI pro začátečníky](https://img.shields.io/badge/AI%20pro%20začátečníky-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Kybernetická bezpečnost pro začátečníky](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Webový vývoj pro začátečníky](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT pro začátečníky](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Vývoj XR pro začátečníky](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Řada Copilot
[![Copilot pro AI párované programování](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot pro C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Dobrodružství](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Získání pomoci

Pokud se zaseknete nebo máte jakékoli dotazy ohledně vytváření AI aplikací, připojte se:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

Pokud máte zpětnou vazbu k produktu nebo narazíte na chyby při vývoji, navštivte:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## Licence

Licence MIT - viz soubor [LICENSE](../../LICENSE) pro podrobnosti.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). I když usilujeme o přesnost, mějte na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho rodném jazyce by měl být považován za závazný zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakékoli nedorozumění nebo nesprávné interpretace vyplývající z používání tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->