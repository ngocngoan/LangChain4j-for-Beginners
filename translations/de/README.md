<img src="../../translated_images/de/LangChain4j.90e1d693fcc71b50.webp" alt="LangChain4j" width="800"/>

# LangChain4j für Einsteiger

Ein Kurs zum Erstellen von KI-Anwendungen mit LangChain4j und Azure OpenAI GPT-5.2, von einfachem Chat bis hin zu KI-Agenten.

### 🌐 Mehrsprachige Unterstützung

#### Unterstützt durch GitHub Action (Automatisiert & Immer Aktuell)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabisch](../ar/README.md) | [Bengalisch](../bn/README.md) | [Bulgarisch](../bg/README.md) | [Birmanisch (Myanmar)](../my/README.md) | [Chinesisch (Vereinfacht)](../zh-CN/README.md) | [Chinesisch (Traditionell, Hongkong)](../zh-HK/README.md) | [Chinesisch (Traditionell, Macau)](../zh-MO/README.md) | [Chinesisch (Traditionell, Taiwan)](../zh-TW/README.md) | [Kroatisch](../hr/README.md) | [Tschechisch](../cs/README.md) | [Dänisch](../da/README.md) | [Niederländisch](../nl/README.md) | [Estnisch](../et/README.md) | [Finnisch](../fi/README.md) | [Französisch](../fr/README.md) | [Deutsch](./README.md) | [Griechisch](../el/README.md) | [Hebräisch](../he/README.md) | [Hindi](../hi/README.md) | [Ungarisch](../hu/README.md) | [Indonesisch](../id/README.md) | [Italienisch](../it/README.md) | [Japanisch](../ja/README.md) | [Kannada](../kn/README.md) | [Khmer](../km/README.md) | [Koreanisch](../ko/README.md) | [Litauisch](../lt/README.md) | [Malaiisch](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepalesisch](../ne/README.md) | [Nigerianisches Pidgin](../pcm/README.md) | [Norwegisch](../no/README.md) | [Persisch (Farsi)](../fa/README.md) | [Polnisch](../pl/README.md) | [Portugiesisch (Brasilien)](../pt-BR/README.md) | [Portugiesisch (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Rumänisch](../ro/README.md) | [Russisch](../ru/README.md) | [Serbisch (Kyrillisch)](../sr/README.md) | [Slowakisch](../sk/README.md) | [Slowenisch](../sl/README.md) | [Spanisch](../es/README.md) | [Suaheli](../sw/README.md) | [Schwedisch](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thailändisch](../th/README.md) | [Türkisch](../tr/README.md) | [Ukrainisch](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamesisch](../vi/README.md)

> **Lieber lokal klonen?**
>
> Dieses Repository enthält mehr als 50 Sprachübersetzungen, was die Downloadgröße erheblich erhöht. Um ohne Übersetzungen zu klonen, verwenden Sie Sparse Checkout:
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
> So erhalten Sie alles, was Sie brauchen, um den Kurs mit deutlich schnellerem Download abzuschließen.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Inhaltsverzeichnis

1. [Schnellstart](00-quick-start/README.md) - Einstieg in LangChain4j
2. [Einführung](01-introduction/README.md) - Erlernen der Grundlagen von LangChain4j
3. [Prompt Engineering](02-prompt-engineering/README.md) - Meistere effektives Prompt-Design
4. [RAG (Retrieval-Augmented Generation)](03-rag/README.md) - Aufbau intelligenter wissensbasierter Systeme
5. [Werkzeuge](04-tools/README.md) - Integration externer Tools und einfacher Assistenten
6. [MCP (Model Context Protocol)](05-mcp/README.md) - Arbeiten mit dem Model Context Protocol (MCP) und Agentenmodulen

### Video-Durchgänge

Jedes Modul hat eine begleitende Live-Session, in der wir die Konzepte und den Code Schritt für Schritt durchgehen.

| Modul | Video |
|--------|-------|
| 01 - Einführung | [Erste Schritte mit LangChain4j](https://www.youtube.com/live/nl_troDm8rQ) |
| 02 - Prompt Engineering | [Prompt Engineering mit LangChain4j](https://www.youtube.com/live/PJ6aBaE6bog) |
| 03 - RAG | [RAG mit LangChain4j](https://www.youtube.com/watch?v=_olq75ZH_eY) |
| 04 - Werkzeuge & 05 - MCP | [KI-Agenten mit Werkzeugen und MCP](https://www.youtube.com/watch?v=O_J30kZc0rw) |

---

## Lernpfad

**Neu bei LangChain4j?** Schau dir das [Glossar](docs/GLOSSARY.md) für Definitionen wichtiger Begriffe und Konzepte an.

> **Schnellstart**

1. Forke dieses Repository auf dein GitHub-Konto
2. Klicke auf **Code** → Tab **Codespaces** → **...** → **Neu mit Optionen...**
3. Verwende die Standardeinstellungen – damit wird der für diesen Kurs erstellte Entwicklungscontainer ausgewählt
4. Klicke auf **Codespace erstellen**
5. Warte 5-10 Minuten, bis die Umgebung bereit ist
6. Springe direkt zu [Schnellstart](./00-quick-start/README.md), um loszulegen!

Nach Abschluss der Module erkunde den [Testleitfaden](docs/TESTING.md), um LangChain4j-Testkonzepte in Aktion zu sehen.

> **Hinweis:** Dieses Training verwendet sowohl GitHub-Modelle als auch Azure OpenAI. Das Modul [Schnellstart](00-quick-start/README.md) verwendet GitHub-Modelle (kein Azure-Abonnement erforderlich), während die Module 1–5 Azure OpenAI nutzen. Erstelle ein [KOSTENLOSES Azure-Konto](https://aka.ms/azure-free-account), falls du noch keines hast.


## Lernen mit GitHub Copilot

Um schnell mit dem Codieren zu beginnen, öffne dieses Projekt in einem GitHub Codespace oder deiner lokalen IDE mit dem bereitgestellten Devcontainer. Der in diesem Kurs verwendete Devcontainer ist vorkonfiguriert mit GitHub Copilot für KI-gestütztes Paarprogrammieren.

Jedes Codebeispiel enthält vorgeschlagene Fragen, die du GitHub Copilot stellen kannst, um dein Verständnis zu vertiefen. Achte auf die 💡/🤖-Hinweise in:

- **Java-Dateikopfzeilen** – Fragen speziell zu jedem Beispiel
- **Modul-READMEs** – Erkundungsfragen nach Codebeispielen

**So nutzt du es:** Öffne eine beliebige Code-Datei und stelle Copilot die vorgeschlagenen Fragen. Er hat den vollständigen Kontext des Codes und kann erklären, erweitern und Alternativen vorschlagen.

Möchtest du mehr lernen? Schau dir [Copilot für KI-Paarprogrammierung](https://aka.ms/GitHubCopilotAI) an.


## Zusätzliche Ressourcen

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j für Einsteiger](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js für Einsteiger](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain für Einsteiger](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agenten
[![AZD für Einsteiger](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI für Einsteiger](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP für Einsteiger](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![KI-Agenten für Einsteiger](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generative KI-Serie
[![Generative KI für Einsteiger](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative KI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative KI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative KI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Kernlernen
[![ML für Einsteiger](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Datenwissenschaft für Einsteiger](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![KI für Einsteiger](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity für Anfänger](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Webentwicklung für Anfänger](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT für Anfänger](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR-Entwicklung für Anfänger](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot-Serie
[![Copilot für KI-Paarprogrammierung](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot für C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot-Abenteuer](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Hilfe erhalten

Wenn Sie nicht weiterkommen oder Fragen zur Entwicklung von KI-Anwendungen haben, treten Sie bei:

[![Microsoft Foundry Discord](https://img.shields.io/badge/Discord-Microsoft_Foundry_Community_Discord-blue?style=for-the-badge&logo=discord&color=5865f2&logoColor=fff)](https://aka.ms/foundry/discord)

Wenn Sie Produktfeedback oder Fehler beim Entwickeln haben, besuchen Sie:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

## Lizenz

MIT-Lizenz – Details finden Sie in der [LICENSE](../../LICENSE)-Datei.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, sollten Sie beachten, dass automatische Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Für wichtige Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die durch die Verwendung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->