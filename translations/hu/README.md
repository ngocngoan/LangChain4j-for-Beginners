<img src="../../translated_images/hu/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j kezdőknek

Egy tanfolyam AI alkalmazások építéséhez LangChain4j-vel és Azure OpenAI GPT-5.2-vel, az alapvető chat rendszerektől az AI ügynökökig.

### 🌐 Többnyelvű támogatás

#### GitHub Action által támogatott (Automatizált és mindig naprakész)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](./README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **Inkább helyileg klónoznád?**
>
> Ez a tárház 50+ nyelvi fordítást tartalmaz, ami jelentősen megnöveli a letöltési méretet. Ha fordítások nélkül szeretnél klónozni, használj sparse checkoutot:
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
> Ez minden szükséges fájlt megad a tanfolyam elvégzéséhez, sokkal gyorsabb letöltéssel.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Tartalomjegyzék

1. [Gyors kezdés](00-quick-start/README.md) - Kezdj el ismerkedni a LangChain4j-vel
2. [Bevezetés](01-introduction/README.md) - Tanuld meg a LangChain4j alapjait
3. [Prompt tervezés](02-prompt-engineering/README.md) - Sajátítsd el a hatékony prompt alkotást
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - Intelligens tudásalapú rendszerek építése
5. [Eszközök](04-tools/README.md) - Külső eszközök és egyszerű asszisztensek integrálása
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Dolgozz az MCP-vel és agentikus modulokkal

### Videós bemutatók

Minden modulhoz élő élő helyszíni bemutató társul, ahol lépésről lépésre végigjárjuk a fogalmakat és a kódot.

| Modul | Videó |
|--------|-------|
| 01 - Bevezetés | [LangChain4j gyors indulás](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - Prompt tervezés | [Prompt tervezés LangChain4j-vel](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [RAG LangChain4j-vel](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - Eszközök & 05 - MCP | [AI ügynökök eszközökkel és MCP-vel](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## Tanulási útvonal

**Új vagy a LangChain4j-ben?** Nézd meg a [Fogalomtárat](docs/GLOSSARY.md) a kulcsfogalmak és definíciók megértéséhez.

> **Gyors kezdés**

1. Forkold ezt a tárhelyet a saját GitHub fiókodba
2. Kattints a **Code** → **Codespaces** fülre → **...** → **New with options...**
3. Hagyd meg az alapértelmezeteket – ez kiválasztja a tanfolyam fejlesztői konténerét
4. Kattints a **Create codespace** gombra
5. Várj 5-10 percet, amíg az környezet készen áll
6. Ugorj egyenesen a [Gyors kezdéshez](./00-quick-start/README.md) és kezdj el tanulni!

A modulok elvégzése után tekintsd meg a [Tesztelési útmutatót](docs/TESTING.md), hogy láthasd a LangChain4j tesztelési koncepcióit működés közben.

> **Megjegyzés:** Ez a képzés GitHub Modelleket és Azure OpenAI-t is használ. A [Gyors kezdés](00-quick-start/README.md) modul GitHub Modelleket használ (nem szükséges Azure előfizetés), míg az 1-5 modulok Azure OpenAI-t használnak. Kezdd el egy [INGYENES Azure fiókkal](https://aka.ms/azure-free-account), ha még nincs.

## Tanulás GitHub Copilot-tal

Gyors kódolás indításához nyisd meg ezt a projektet egy GitHub Codespace-ben vagy a helyi IDE-dben a mellékelt devcontainerrel. A tanfolyamban használt devcontainer előre beállított GitHub Copilot-tal rendelkezik AI páros programozáshoz.

Minden kódpélda tartalmaz javasolt kérdéseket, amelyeket feltehetsz a GitHub Copilot-nak a mélyebb megértés érdekében. Figyeld a 💡/🤖 jelzéseket:

- **Java fájl fejlécek** - Példánként specifikus kérdések
- **Modul README-k** - Felfedező kérdések a kódpéldák után

**Használati útmutató:** Nyisd meg bármelyik kód fájlt, és tedd fel a javasolt kérdéseket Copilot-nak. Teljes kontextusa van a kód bázisra, és képes magyarázni, bővíteni, illetve alternatívákat javasolni.

Szeretnél többet megtudni? Nézd meg a [Copilot az AI páros programozáshoz](https://aka.ms/GitHubCopilotAI).

## További források

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j kezdőknek](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js kezdőknek](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain kezdőknek](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Ügynökök
[![AZD kezdőknek](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI kezdőknek](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP kezdőknek](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI ügynökök kezdőknek](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generatív AI sorozat
[![Generatív AI kezdőknek](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generatív AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generatív AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generatív AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Alapvető tanulás
[![ML kezdőknek](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Adattudomány kezdőknek](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI kezdőknek](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Kiberbiztonság kezdőknek](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Webfejlesztés kezdőknek](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT kezdőknek](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR fejlesztés kezdőknek](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot Sorozat
[![Copilot mesterséges intelligenciával páros programozáshoz](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot C#/.NET-hez](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot kaland](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Segítségkérés

Ha elakadsz vagy kérdésed van az MI-alkalmazások építésével kapcsolatban, csatlakozz:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

Ha termék-visszajelzésed vagy hibák vannak az építés során, látogass el ide:

[![Microsoft Foundry fejlesztői fórum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## Licenc

MIT Licenc - A részletekért lásd a [LICENSE](../../LICENSE) fájlt.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Felelősségkizárás**:  
Ezt a dokumentumot a [Co-op Translator](https://github.com/Azure/co-op-translator) AI fordító szolgáltatás segítségével fordítottuk. Bár a pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások tartalmazhatnak hibákat vagy pontatlanságokat. Az eredeti dokumentum az anyanyelvén tekintendő hiteles forrásnak. Fontos információk esetén professzionális emberi fordítást javaslunk. Nem vállalunk felelősséget az ebből a fordításból eredő félreértésekért vagy értelmezési hibákért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->